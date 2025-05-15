package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreException;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.gson.Gson;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import pt.unl.fct.di.apdc.firstwebapp.util.RegisterData;

@Path("/register")
public class RegisterResource {

    private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());
    private static final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

    private final Gson g = new Gson();

    public RegisterResource() {}	// Default constructor, nothing to do

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(RegisterData data) {
        LOG.fine("Attempt to register user: " + data.id);

        if(data.cc_bi == null) data.cc_bi = "";
        if(data.role == null) data.role = "ENDUSER";
        if(data.nif == null) data.nif = "";
        if(data.company == null) data.company = "";
        if(data.job == null) data.job = "";
        if(data.address == null) data.address = "";
        if(data.company_nif == null) data.company_nif = "";
        if(data.account_state == null) data.account_state = "INACTIVE";


        if(!data.validRegistration())
            return Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter.").build();


        try {
            Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.id);

            Entity user = Entity.newBuilder(userKey)
                    .set("user_id", data.id)
                    .set("user_full_name", data.full_name)
                    .set("user_email", data.email)
                    .set("user_phone", data.phone)
                    .set("user_pwd", DigestUtils.sha512Hex(data.password))
                    .set("user_profile", data.profile)
                    .set("user_cc_bi", data.cc_bi)
                    .set("user_role", data.role)
                    .set("user_nif", data.nif)
                    .set("user_company", data.company)
                    .set("user_job", data.job)
                    .set("user_address", data.address)
                    .set("user_company_nif", data.company_nif)
                    .set("user_account_state", data.account_state)
                    .set("user_creation_time", Timestamp.now())
                    .build();

            datastore.add(user);
            LOG.info("User registered " + data.id);

            return Response.ok().entity("{\"message\":\"User registered successfully.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        catch(DatastoreException e) {
            LOG.log(Level.ALL, e.toString());
            return Response.status(Status.BAD_REQUEST).entity(e.getReason()).build();
        }
    }
}