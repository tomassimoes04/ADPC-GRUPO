package pt.unl.fct.di.apdc.firstwebapp.resources;

import com.google.cloud.tasks.v2.*;
import com.google.gson.Gson;
import com.google.protobuf.Timestamp;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


@Path("/utils")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") 
public class ComputationResource {

	private static final Logger LOG = Logger.getLogger(ComputationResource.class.getName()); 
	private final Gson g = new Gson();

	private static final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

	public ComputationResource() {} //nothing to be done here @GET

	@GET
	@Path("/guide")
	@Produces(MediaType.TEXT_PLAIN)
	public Response hello() throws IOException{
		LOG.fine("A guide describing the available operations to the user.");
		return Response.ok("Hello User! Here is a quick guide to all the operations you can use with my website:").build();
		/**
		try {
			throw new IOException("UPS");
		} catch (Exception e) {
			LOG.log(Level.SEVERE, "Exception on Method /hello", e);
			return Response.temporaryRedirect(URI.create("/error/500.html")).build();
		}
		 */
	}
}