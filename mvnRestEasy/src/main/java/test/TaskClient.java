package test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.List;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Client side consumer to JBPM services.
 */
public class TaskClient {

    // should be DI'd
    private TaskFactory taskFactory = new TaskFactory();;

    // should be read from properties
    private String urlBase = "http://localhost:8080/pe2e-workflow/workflow";

    // same with the following
    // see gov.uspto.pe2e.workflow.service.WorkflowService for more endpoints.
    /** tasks assigned to the user */
    private String assignedTasksForUser = urlBase + "/user/tasks/assigned/{user}";

    /** tasks available to user */
    private String availableTasksForUser = urlBase + "/tasks/available/{user}";

    /** tasks available for group */
    private String tasksForGroup = urlBase + "/tasks/available/groups";

    /** actions available for the task */
    private String actionsURL = urlBase + "/tasks/{task}/actions";

    /** task available for the task */
    private String taskURL = urlBase + "/tasks/{task}";

    /** accepted content type */
    private String JSON = "application/json";

    /**
     * note to implementer.  Should make workflow's task definition
     * and this task defnition the same
     */
    public List<Task> getAssignedTasksForUserWithActions(String userId){
        // get the tasks
        String rawTasksJson = getAvailableTasksForUser(userId);
        // convert to task object.
        List<Task> tasks = taskFactory.createTasks(rawTasksJson);

        // fill task object actions.
        for(Task task: tasks){
            String rawActionsJson = getActions(task.taskId);

            taskFactory.setActions(task, rawActionsJson);
        }

        // return task object list
        return tasks;
    }

    /**
     * note to implementer.  Should make workflow's task definition
     * and this task defnition the same.
     */
    public Task getTaskWithActions(String taskId){
        String rawTaskJson = getTask(taskId);
        String rawActionsJson = getActions(taskId);
        Task t = taskFactory.createTask(rawTaskJson, rawActionsJson);
        return t;
    }
    String getTask(String taskId){
        String url = taskURL.replace("{task}", taskId);;
        return GET(url, JSON);
    }
    String getActions(String taskId){
        String url = actionsURL.replace("{task}", taskId);
        return GET(url, JSON);
    }
    String getAssignedTasksForUser(String userId){
        String url = assignedTasksForUser.replace("{user}", userId);
        return GET(url, JSON);
    }
    String getAvailableTasksForUser(String userId){
        String url = availableTasksForUser.replace("{user}", userId);
        return GET(url, JSON);
    }
    String getTasksForGroup(){
        String url = tasksForGroup;
        return GET(url, JSON);
    }

    private String GET(String url, String contentType){
        StringBuffer buf = new StringBuffer();
        try {

            ClientRequest request = new ClientRequest(url);
            request.accept(contentType);

            ClientResponse<String> response = request.get(String.class);

            int status = response.getStatus();
            boolean errorOrNull = true;

            switch(status){
                case 200:
                    errorOrNull = false;
                    break;
                case 201:
                    errorOrNull = false;
                    break;
                case 204:
                    break;
                default:
                    errorOrNull = true;
                    throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());

            }

            if(!errorOrNull){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                            new ByteArrayInputStream(response.getEntity().getBytes())));

                String output;
                while ((output = br.readLine()) != null) {
                    buf.append(output);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buf.toString();
    }
}
