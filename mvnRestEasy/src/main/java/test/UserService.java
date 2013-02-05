package test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.List;

@Path("user")
public class UserService {
    private UserDAO mockDao = new UserDAO();
    private TaskClient taskClient = new TaskClient();

	@GET
	@Path("{userName}")
	public Response getUser(@PathParam("userName") String username) {
		User theUser = mockDao.getUser(username);
		return Response.status(200).entity(toJson(theUser)).build();
	}

	@GET
	@Path("{userName}/group/{group}/")
	public Response getUser(
            @PathParam("userName") String username,
            @PathParam("group") String group) {

		User theUser = mockDao.getUser(username);
        if(theUser != null){
            Group g = theUser.getGroup(group);

            if(g != null){
                return Response.status(200).entity(toJson(g)).build();
            } else {
                return Response.status(404).build();
            }
        } else {
            return Response.status(404).build();
        }
	}

	@GET
	@Path("{userName}/tasks")
	public Response getTasks(@PathParam("userName") String username) {
        List<Task> tasks = taskClient.getAssignedTasksForUserWithActions(username);
		return Response.status(200).entity(toJson(tasks)).build();
	}

	@GET
	@Path("{userName}/tasks/{taskId}")
	public Response getTask(
            @PathParam("userName") String username,
            @PathParam("taskId") String taskId) {
        Task theTask = taskClient.getTaskWithActions(taskId);
		return Response.status(200).entity(toJson(theTask)).build();
	}

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

