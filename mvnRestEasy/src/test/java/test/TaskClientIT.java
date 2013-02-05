package test;

import test.*;

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

public class TaskClientIT {
    TaskClient tc;

    @Before
    public void setUp(){
        tc = new TaskClient();
    }

    @Test
    public void testGetTaskWithActions(){
        Task t = tc.getTaskWithActions("18");
        System.out.println("JSON: " + toJson(t));
        //assert
    }

    @Test
    public void testGetAssignedTasksForUserWithActions(){
        List<Task> tasks = tc.getAssignedTasksForUserWithActions("john");
        System.out.println("JSON: " + toJson(tasks));
        //assert
    }

    @Test
    public void testGetActions(){
        String result = tc.getActions("18");
        System.out.println("ACTIONS: " + result);
        assertNotNull(result);
        //assert
    }
    @Test
    public void testAssignedTasksForUser(){
        String result = tc.getAssignedTasksForUser("fdelacruz");
        System.out.println("AssignedTasks: " + result);
        assertNotNull(result);
        //assert
    }
    @Test
    public void testAvailableTasksForUser(){
        String result = tc.getAvailableTasksForUser("fdelacruz");
        System.out.println("Available TASKS: " + result);
        assertNotNull(result);
        //assert
    }
    @Test
    public void testGetTasksForGroup(){
        String result = tc.getTasksForGroup();
        System.out.println("TASKS FOR GROUP: " + result);
        assertNotNull(result);
        //assert
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
