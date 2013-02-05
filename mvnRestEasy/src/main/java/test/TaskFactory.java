package test;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;

import test.events.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

/**
 * Prototype quality
 */
public class TaskFactory {
    private static JsonFactory jfactory = new JsonFactory();
    private static final String ID = "id";
    private static final String NS = "processId";
    private static final String ACTIONS = "actions";
    private static final String SPLITTER = ",";
    private static final String NS_SEPARATOR = ".";
    private Properties properties;

    public TaskFactory(){
        ClassLoader classLoader = this.getClass().getClassLoader();
        properties = new Properties();

        try {
            java.io.InputStream is = classLoader.getResourceAsStream("events.properties");
            properties.load(is);
        } catch (java.io.IOException ioe){
            throw new RuntimeException("Might as well die now", ioe);
        }
    }

    public Task createTask(String rawTaskJson, String rawActionsJson) {
        Task newTask = null;

        try {
            newTask = createTask(rawTaskJson);

            List<Event> events = toEvents(newTask.namespace, rawActionsJson);

            newTask.setEvents(events);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (java.io.IOException ioe){
            ioe.printStackTrace();
        }

        return newTask;
    }

    // note to implementer.  Probably better off spending some time writing a jackson
    // deserializer instead of the streaming approach.
    public List<Task> createTasks(String rawTasksJson) {

        List<Task> taskList = new ArrayList<Task>();

        try {
            JsonParser jParser = jfactory.createJsonParser(rawTasksJson);

            // expecting a shallow json object only
            JsonToken theToken;
            while ((theToken = jParser.nextToken()) != JsonToken.END_ARRAY) {
                if(theToken == JsonToken.VALUE_NULL || theToken == JsonToken.START_ARRAY){
                    continue;
                } else {
                    Task newTask = createTask(jParser);
                    taskList.add(newTask);
                }
            }

            jParser.close();

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (java.io.IOException ioe){
            ioe.printStackTrace();
        }

        return taskList;
    }

    public void setActions(Task newTask, String rawActionsJson) {
        try {
            List<Event> events = toEvents(newTask.namespace, rawActionsJson);

            newTask.setEvents(events);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (java.io.IOException ioe){
            ioe.printStackTrace();
        }
    }

    private Task createTask(String rawTaskJson)
        throws JsonGenerationException, JsonMappingException, java.io.IOException{

        JsonParser jParser = jfactory.createJsonParser(rawTaskJson);

        Task newTask = createTask(jParser);

        jParser.close();

        return newTask;
    }

    // note to implementer.  Probably better off spending some time writing a jackson
    // deserializer instead of the streaming approach.
    private Task createTask(JsonParser jParser)
        throws JsonGenerationException, JsonMappingException, java.io.IOException{

        Map<String, String> params = new HashMap<String,String>();
        String id = null;
        String ns = null;

        // expecting a shallow json object only
        JsonToken theToken;

        // 'hydrate' using raw tasks.
        while ((theToken = jParser.nextToken()) != JsonToken.END_OBJECT) {
            String fieldName = jParser.getCurrentName();
            String rawText = jParser.getText();
            if(theToken == JsonToken.VALUE_NULL || theToken == JsonToken.START_OBJECT){
                continue;
            } else {
                if(ID.equals(fieldName)){
                    id = rawText;
                } else if (NS.equals(fieldName)){
                    ns = rawText;
                } else {
                    params.put(fieldName, rawText);
                }
            }
        }

        return new Task(id, ns, params);
    }

    private List<Event> toEvents(String namespace, String rawTaskAction)
        throws JsonGenerationException, JsonMappingException, java.io.IOException{

        List<Event> events = new ArrayList<Event>();

        JsonParser jParser = jfactory.createJsonParser(rawTaskAction);

        // expecting a shallow json object only
        JsonToken theToken;

        // 'hydrate' using raw tasks.
        while ((theToken = jParser.nextToken()) != JsonToken.END_ARRAY) {
            String fieldName = jParser.getCurrentName();
            String rawText = jParser.getText();
            if(theToken == JsonToken.VALUE_NULL || theToken == JsonToken.START_ARRAY){
                continue;
            } else {
                if(rawText != null){
                    String pKey = namespace + NS_SEPARATOR + rawText;
                    String pVal = properties.getProperty(pKey);

                    if(pVal != null){
                        EventHelper h = new EventHelper(pVal);
                        events.add(h.getEvent());
                    }
                }
            }

        }
        jParser.close();

        return events;
    }
}
