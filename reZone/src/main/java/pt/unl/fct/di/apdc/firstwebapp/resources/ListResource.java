package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.cloud.datastore.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.apdc.firstwebapp.util.ListData;

import java.util.*;

@Path("/list")
public class ListResource {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

    public ListResource() {}

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response listUsers(ListData data) {

        if(data.lister == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a lister.")
                    .build();
        }

        Key listerKey = userKeyFactory.newKey(data.lister);
        Entity lister = datastore.get(listerKey);

        if(lister == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Lister not found.")
                    .build();
        }

        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("User")
                .build();
        QueryResults<Entity> results = datastore.run(query);

        List<Map<String, String>> usersList = new ArrayList<>();
        String listerRole = lister.getString("user_role");

        while(results.hasNext()) {
            Entity user = results.next();
            String targetRole = user.getString("user_role");

            switch (listerRole) {
                case "ENDUSER" -> {
                    if (!targetRole.equals("ENDUSER")) continue;
                    if (!user.getString("user_profile").equals("public")) continue;
                    if (!user.getString("user_state").equals("ACTIVE")) continue;

                    usersList.add(buildFilteredUser(user, List.of("username", "email", "name")));
                }
                case "BACKOFFICE" -> {
                    if (!targetRole.equals("ENDUSER")) continue;
                    usersList.add(buildFilteredUser(user, user.getNames()));
                }
                case "ADMIN" -> usersList.add(buildFilteredUser(user, user.getNames()));
            }
        }

        return Response.ok(usersList).build();

    }

    private Map<String, String> buildFilteredUser(Entity user, Collection<String> attributes) {
        Map<String, String> filtered = new HashMap<>();
        for(String attr : attributes) {
            if(user.contains(attr)) {
                Value<?> val = user.getValue(attr);
                filtered.put(attr, val.get().toString());
            } else {
                filtered.put(attr, "NOT DEFINED");
            }
        }
        return filtered;
    }

}
