package test;

import test.events.*;

import java.util.List;
import java.util.Map;

public class Task {
    public String taskId;
    public String namespace;
    public Map<String, String> properties;
    public List<Event> events;

    public Task(String taskId, String namespace, Map<String, String> properties){
        this.taskId = taskId;
        this.properties = properties;
        this.namespace = namespace;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

}
