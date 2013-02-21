package test;

import test.events.*;
import test.domain.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.CookieParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;
import java.util.ArrayList;

@Path("cases")
public class CaseService {

    // should be injected by spring
    private static CaseProcess cp = new CaseProcess();

    /** returns all the cases, not going to implement this on the prototype */
    //@GET
    //public Response get(){
    //}

    /** returns a single case */
    @GET
	@Path("{caseId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response get(
            @CookieParam("pto.current.userid") String userId,
            @PathParam ("caseId") String caseId) {

        String processId = cp.getProcessForCase(caseId);

        System.out.println(userId + " " + caseId);
        List<String> aList = new ArrayList<String>();
        aList.add(userId);
        aList.add(caseId);

        if(processId != null){
            // get all tasks for process...
            // get all the links for each task.
            // turn that into the action links.
            return Response.status(200).entity(userId + " " + caseId).build();
        } else {
            return Response.status(200).entity(toJson(aList)).build();
        }
    }

    /** Gets the action's definition JSON Schema */
    @GET
    @Path("{caseId}/links/{action}/{taskId}")
    public Response get(
            @CookieParam("pto.current.userid") String userId,
            @PathParam("caseId") String caseId,
            @PathParam("action") String action,
            String taskId) {

        return Response.status(200).build();
    }

    /** Posts the action */
    @POST
    @Path("{caseId}/links/{action}/{taskId}")
	@Consumes("application/json")
    public Response post(
            @CookieParam("pto.current.userid") String userId,
            @PathParam("caseId") String caseId,
            @PathParam("action") String action,
            String taskId) {

        // get the tasks for this case.
        // for each task, find the action and its assigned user.
        // If the user assigned and task action are equal, activate it.
        // if no matches are found, send 404 or 402
        return Response.status(200).build();
    }

    /*
     * NOTES:  Looks like forcing not putting the task ids in here
     * would cause performance issues with continuous looping.
     *
     * V2. Adding taskid
     */

    /**
     * Helper class to convert complex object to json text
     */
    private static String toJson(Object user) {
        ObjectMapper mapper = new ObjectMapper();
        String result = null;

        try {
            result = mapper.writeValueAsString(user);
        } catch (java.io.IOException ioe){
        }

        return result;
    }
}
