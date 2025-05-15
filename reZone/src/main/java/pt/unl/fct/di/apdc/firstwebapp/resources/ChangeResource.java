package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.datastore.*;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.unl.fct.di.apdc.firstwebapp.util.*;

@Path("/change")
public class ChangeResource {

    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    private static final KeyFactory userKeyFactory = datastore.newKeyFactory().setKind("User");

    public ChangeResource() {}

    @POST
    @Path("/role")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeRole(ChangeRoleData data) {

        if(data.changer == null || data.user == null || data.old_role == null || data.new_role == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a changer, user, old role and new role.")
                    .build();
        }

        Key changerKey = userKeyFactory.newKey(data.changer);
        Entity changer = datastore.get(changerKey);

        if(changer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Changer not found.")
                    .build();
        }

        Key userKey = userKeyFactory.newKey(data.user);
        Entity user = datastore.get(userKey);

        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }

        boolean backoffice = data.changer.equals("BACKOFFICE") && !((data.old_role.equals("ENDUSER") && data.new_role.equals("PARTNER")) ||
                                                                    (data.old_role.equals("PARTNER") && data.new_role.equals("ENDUSER")));
        boolean enduser = data.changer.equals("ENDUSER");

        if(backoffice || enduser) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Changer does not have permission to change the user's role.")
                    .build();
        }

        if(!data.old_role.equals(user.getString("user_role"))) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Please enter the correct old role.")
                    .build();
        }

        if(data.old_role.equals(data.new_role)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Please enter a different new role.")
                    .build();
        }

        Entity updatedUser = Entity.newBuilder(user)
                .set("user_role", data.new_role)
                .build();

        datastore.update(updatedUser);

        return Response.ok().entity("Role changed successfully.").build();
    }

    @POST
    @Path("/state")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeState(ChangeStateData data) {

        if(data.changer == null || data.user == null || data.old_state == null || data.new_state == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a changer, user, old state and new state.")
                    .build();
        }

        Key changerKey = userKeyFactory.newKey(data.changer);
        Entity changer = datastore.get(changerKey);

        if(changer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Changer not found.")
                    .build();
        }

        Key userKey = userKeyFactory.newKey(data.user);
        Entity user = datastore.get(userKey);

        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }

        boolean backoffice = data.changer.equals("BACKOFFICE") && !((data.old_state.equals("ACTIVE") && data.new_state.equals("INACTIVE")) ||
                                                                    (data.old_state.equals("INACTIVE") && data.new_state.equals("ACTIVE")));

        if(backoffice) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Changer does not have permission to change the user's state.")
                    .build();
        }

        if(!data.old_state.equals(user.getString("user_state"))) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Please enter the correct old state.")
                    .build();
        }

        if(data.old_state.equals(data.new_state)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Please enter a different new state.")
                    .build();
        }

        Entity updatedUser = Entity.newBuilder(user)
                .set("user_state", data.new_state)
                .build();

        datastore.update(updatedUser);

        return Response.ok().entity("State changed successfully.").build();
    }

    @POST
    @Path("/attributes")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changeAttributes(ChangeAttributesData data) {

        if(data.changer == null || data.user == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a changer and an user.")
                    .build();
        }

        Key changerKey = userKeyFactory.newKey(data.changer);
        Entity changer = datastore.get(changerKey);

        if(changer == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Changer not found.")
                    .build();
        }

        Key userKey = userKeyFactory.newKey(data.user);
        Entity user = datastore.get(userKey);

        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }

        String changerRole = changer.getString("user_role");
        String targetRole = user.getString("user_role");

        boolean enduser = changerRole.equals("ENDUSER");
        boolean backoffice = changerRole.equals("BACKOFFICE");
        boolean admin = changerRole.equals("ADMIN");

        // ENDUSER restrictions
        if(enduser) {
            if(!user.getString("user_state").equals("ACTIVE")) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("ENDUSER needs to be ACTIVE.")
                        .build();
            }
            if(!data.changer.equals(data.user)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("ENDUSER can only modify their own account.")
                        .build();
            }
            if(data.new_id != null || data.new_email != null || data.new_full_name != null ||
                    data.new_role != null || data.new_account_state != null) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("ENDUSER cannot change username, email, name, role or account state.")
                        .build();
            }
        }

        // BACKOFFICE restrictions
        if(backoffice) {
            if(!targetRole.equals("ENDUSER") && !targetRole.equals("PARTNER")) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("BACKOFFICE can only modify ENDUSER or PARTNER accounts.")
                        .build();
            }
            if(!user.getString("user_state").equals("ACTIVE")) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("BACKOFFICE can only modify activated accounts.")
                        .build();
            }
            if(data.new_id != null || data.new_email != null) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("BACKOFFICE cannot change username or email.")
                        .build();
            }
        }

        Entity.Builder builder = Entity.newBuilder(user);

        if(data.new_password != null) builder.set("user_pwd", org.apache.commons.codec.digest.DigestUtils.sha512Hex(data.new_password));
        if(data.new_phone != null) builder.set("user_phone", data.new_phone);
        if(data.new_profile != null) builder.set("user_profile", data.new_profile);
        if(data.new_job != null) builder.set("user_job", data.new_job);
        if(data.new_cc_bi != null) builder.set("user_cc_bi", data.new_cc_bi);
        if(data.new_address != null) builder.set("user_address", data.new_address);
        if(data.new_nif != null) builder.set("user_nif", data.new_nif);
        if(data.new_company != null) builder.set("user_company", data.new_company);
        if(data.new_company_nif != null) builder.set("user_company_nif", data.new_company_nif);

        if(admin && data.new_id != null) builder.set("user_id", data.new_id);
        if(admin && data.new_email != null) builder.set("user_email", data.new_email);
        if(admin && data.new_full_name != null) builder.set("user_full_name", data.new_full_name);
        if(admin && data.new_role != null) builder.set("user_role", data.new_role);
        if(admin && data.new_account_state != null) builder.set("user_state", data.new_account_state);

        datastore.update(builder.build());
        return Response.ok("User attributes updated successfully.").build();
    }

    @POST
    @Path("/password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordData data) {

        if(data.user == null || data.old_password == null || data.new_password == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Please provide a user, old password and new password.")
                    .build();
        }

        Key userKey = userKeyFactory.newKey(data.user);
        Entity user = datastore.get(userKey);

        if(user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("User not found.")
                    .build();
        }

        String old_password_hashed = DigestUtils.sha512Hex(data.old_password);
        String new_password_hashed = DigestUtils.sha512Hex(data.new_password);

        if(!user.getString("user_pwd").equals(old_password_hashed))
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Old password does not match.")
                    .build();

        if(user.getString("user_pwd").equals(new_password_hashed))
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Please provide a new password different from the old password.")
                    .build();

        Entity updatedUser = Entity.newBuilder(user)
                .set("user_pwd", new_password_hashed)
                .build();

        datastore.update(updatedUser);

        return Response.ok().entity("Password changed successfully.").build();
    }
}
