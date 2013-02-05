package test.events;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.ws.rs.core.MultivaluedMap;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

/**
 * This actually is a factory
 */
public class EventHelper {
    Class                           concrete;
    Constructor                     constructor;
    Object                          instance;
    MultivaluedMap<String,String>   form;
    Map<String,List<String>>        errors;
    String                          namespace;

    public EventHelper(MultivaluedMap<String,String> form){
        this.form = form;
        this.errors = new HashMap<String,List<String>>();

        String clazz = form.getFirst("class");
        construct(clazz);
        //addFields();
        initFields();
        initValues();
        init(form);
    }

    public EventHelper(String clazz){
        this.errors = new HashMap<String,List<String>>();

        construct(clazz);
        initFields();
    }

    private void init(MultivaluedMap<String, String> form){
        Event event = getEvent();
        event.init(form);
    }

    private void initFields(){
        Event event = getEvent();
        System.out.println("Pushing SubEvent " + event.getClass().getName());

        List<EventField> fields = new ArrayList<EventField>();

        if(event != null && event instanceof WorkflowEvent){
            WorkflowEvent wEvent = (WorkflowEvent) event;
            List<EventField> subFields = wEvent.describeFields();

            event.setNamespace(wEvent.namespace());

            if(subFields != null){
                fields.addAll(subFields);
            }
        }

        event.initFields(fields);
    }

    private void initValues(){
        Event event = getEvent();
        Map<String, String> defValues = new HashMap<String, String>();

        if(event != null && event instanceof WorkflowEvent){
            WorkflowEvent wEvent = (WorkflowEvent) event;

            defValues.put("namespace", wEvent.namespace());

            if(wEvent.defaultValues() != null){
                defValues.putAll(wEvent.defaultValues());
            }

            event.initValues(defValues);
        }
    }

    private void construct(String clazz){

        try {
            Class[] params = new Class[]{};
            Object[] args  = new Object[]{};

            concrete    = Class.forName(clazz);
            constructor = concrete.getConstructor(params);
            instance    = constructor.newInstance(args);

        } catch (ClassNotFoundException e){
            pushErr("class", "this parameter is invalid: " + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchMethodException e){
            pushErr("class", "this parameter is invalid: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException e){
            pushErr("class", "this parameter cannot be reached");
            e.printStackTrace();
        } catch (InvocationTargetException e){
            pushErr("class", "illegal target, this parameter cannot be reached");
            e.printStackTrace();
        } catch (InstantiationException e){
            pushErr("class", "illegal target, this parameter cannot be created");
            e.printStackTrace();
        }

        if(errors.size() > 0){
            // turn this into checked exception.
            throw new RuntimeException(errors.toString());
        }
    }

    private void pushErr(String key, String val){
        List<String> thisVal = errors.get(key);

        if(thisVal == null){
            thisVal = new ArrayList<String>();
        }

        thisVal.add(val);
        errors.put(key, thisVal);
    }

    /**
     * Returns the event to be emitted.
     */
    public Event getEvent(){
        if(instance != null && instance instanceof Event){
            return (Event) instance;
        }

        return null;
    }

    /**
     * Returns the raw instance of the event.
     */
    private Object getInstance(){
        if(instance == null){
            // should be a checked exception.
            throw new RuntimeException("could not instantiate instance.  See previous errors");
        }

        return instance;
    }

    /**
     * Validates the event against its own definition.
     */
    public final Map<String,List<String>> validate(MultivaluedMap<String, String> form){
        Map<String,List<String>> errors = new HashMap<String,List<String>>();

        Event event = getEvent();
        Collection<EventField> fields = event.getFields();

        for(EventField field: fields){
            List<String> err = field.validate();

            //System.out.println("TRYING: " + field.interfaceKey + " " + field.getValue() + " " + err);

            if(err != null){
                errors.put(field.interfaceKey, err);
            }
        }

        errors.putAll(this.errors);

        return errors;
    }
}
