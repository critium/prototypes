package test;

import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ObjectMapper;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import java.util.List;
public class TaskFactoryTest {
    @Test
    public void getEvent(){
        TaskFactory cf = new TaskFactory();
        // this is actually incorrect
        String taskJson = "{'id':'1', 'processId':'RequestSignatureWorkflow'}"
            .replace("\'", "\"");

        String actionJson = "['reject','sign']"
            .replace("\'", "\"");

        Task c = cf.createTask(taskJson, actionJson);

        if(c != null){
            System.out.println(toJson(c));
        } else {
            System.out.println("C is NULL?");
        }
    }

    @Test
    public void getEvent2(){
        TaskFactory cf = new TaskFactory();
        // this is actually incorrect
        String taskJson = "[{'id':'1', 'processId':'RequestSignatureWorkflow'},{'id':'2', 'processId':'RequestSignatureWorkflow'}]"
            .replace("\'", "\"");

        String actionJson = "['reject','sign']"
            .replace("\'", "\"");

        List<Task> taskList = cf.createTasks(taskJson);
        for(Task task: taskList){
            cf.setActions(task, actionJson);
            if(task != null){
                System.out.println(toJson(task));
            } else {
                System.out.println("task is NULL?");
            }
        }

    }

    /**
     * Helper class to convert complex object to json text
     */
    private static String toJson(Object form) {
        ObjectMapper mapper = new ObjectMapper();
        String result = null;

        try {
            result = mapper.writeValueAsString(form);
        } catch (java.io.IOException ioe){
            ioe.printStackTrace();
        }

        return result;
    }
}
