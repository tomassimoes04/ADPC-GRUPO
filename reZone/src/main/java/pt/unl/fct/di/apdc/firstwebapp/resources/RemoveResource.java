package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.cloud.datastore.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.apdc.firstwebapp.util.RemoveUserData;

@Path("/remove")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RemoveResource {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

    public RemoveResource() {}

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeUser(RemoveUserData data) {

        if(data.deleter == null || data.deleter.isEmpty() || data.deleter_role == null ||
                data.deleter_role.isEmpty() || data.user == null || data.user.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a valid deleter, deleter role, user and user role.")
                    .build();

        Key deleterKey = userKeyFactory.newKey(data.deleter);
        Entity deleter = datastore.get(deleterKey);

        Key userKey = userKeyFactory.newKey(data.user);
        Entity user = datastore.get(userKey);

        if(deleter == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Deleter not found.")
                    .build();

        if(user == null)
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found or already removed.")
                    .build();

        boolean admin = data.deleter_role.equals("ADMIN");
        boolean backoffice = data.deleter_role.equals("BACKOFFICE") && (data.user_role.equals("ENDUSER") || data.user_role.equals("PARTNER"));

        if(!admin && !backoffice)
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Deleter does not have permission to remove user.")
                    .build();

        datastore.delete(userKey);
        return Response.ok().entity("User has been removed.").build();
    }
}
