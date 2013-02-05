package test.events;

import java.util.List;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import test.events.EventField;
import test.events.EventSerializer;
import static test.events.EventField.Attribute.*;

public class Event /*implements Externalizable*/ {
    /** These required fields are required for all event types */
    private Map<String, EventField> fields = new HashMap<String, EventField>();

    // namespace ties it to the BRMS workflow name
    private String NAMESPACE = "genericEvent";

    /**
     * TS of this event.
     */
    private Date timestamp;

    /**
     * The context of the event.  This is where the other context/property
     * type values go in.  Should be considered un-trusted since they
     * are not validated.
     */
    private Map<String, String> context;

    /**
     * Set the default required fields for all events.
     */
    private void initFields(){
        push("type", "eventType", REQUIRED);
        push("source",REQUIRED);
        push("namespace","contextType", REQUIRED);
        push("contextId",REQUIRED);

        // this is specific to each event.
        EventField classField = new EventField("class", "class").add(REQUIRED);
        classField.set(this.getClass().getName());
        push(classField);

        setValue("namespace", NAMESPACE);
    }

    /**
     * INitialize the default values
     */
    protected void initValues(Map<String, String> defaultValues){
        if(defaultValues != null){
            Set<String> keys = defaultValues.keySet();

            for(String key: keys){
                String value = defaultValues.get(key);
                setValue(key, value);
            }
        }
    }

    /**
     * Grow the fields
     */
    protected void initFields(List<EventField> fields){
        initFields();

        for(EventField field: fields){
            push(field);
        }
    }

    public void setNamespace(String namespace){
        NAMESPACE = namespace;
    }

    /**
     * Helper method to the sub-classes.
     */
    public List<EventField> push(
            List<EventField> fields, String fieldName, EventField.Attribute... attributes){

        return push(fields, fieldName, fieldName, attributes);
    }

    /**
     * Helper method to the sub-classes.
     */
    public List<EventField> push(
            List<EventField> fields,
            String interfaceKey, String workflowKey,
            EventField.Attribute... attributes){

        if(fields != null){
            fields.add(new EventField(interfaceKey, workflowKey, attributes));
        }

        return fields;
    }

    /**
     * Convenience method for pushing event fields.
     */
    protected void push(EventField field){
        //System.out.println("Pushing into main: " + field.interfaceKey);
        fields.put(field.interfaceKey, field);
    }

    /**
     * Convenience method for pushing event fields.
     */
    protected void push(String key, EventField.Attribute... attributes ){
        push(key, key, attributes);
    }

    /**
     * Creates events and pushes them into the field mapping.
     */
    protected void push(String interfaceKey, String workflowKey, EventField.Attribute... attributes){
        EventField field = new EventField(interfaceKey, workflowKey, attributes);
        push(field);
    }

    private Map<String, EventField> getEventFields(){
        if(fields.size() == 0){
            initFields();
        }

        return fields;
    }

    /**
     * REturns the event fields for validation and constructoini
     * of event descriptor.
     */
    public Collection<EventField> getFields(){
        // event fields are guaranteed to be non-null.
        return getEventFields().values();
    }

    /**
     * called by the event helper
     */
    protected void init(MultivaluedMap<String, String> form){
        Map<String, EventField> eventFields = getEventFields();

        this.timestamp = new Date();

        for(EventField field: eventFields.values()){
            String tKey = field.interfaceKey;
            String tVal = form.getFirst(tKey);

            if(tVal != null){
                field.set(tVal);
            }
        }

        // this is for all the 'free- floating'
        // key value pairs not captured by the event.
        // This is technically illegal and should only
        // be used for debugging.
        Set<String> keys = form.keySet();
        for(String key: keys){
            getContext().put(key, form.getFirst(key));
        }
    }

    /**
     * Sets the value of the event field (if found).
     */
    public void setValue(String key, String value){
        EventField field = getEventFields().get(key);

        if(field != null){
            field.set(value);
        }
    }

    /**
     * Returns teh context of the event.  The contents
     * of the context is not validated and should be considered
     * not trusted.
     */
    public final Map<String, String> getContext(){
        if(context == null){
            context = new HashMap<String, String>();
        }

        return context;
    }
}

