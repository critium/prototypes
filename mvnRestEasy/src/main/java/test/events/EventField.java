package test.events;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.codehaus.jackson.map.annotate.JsonSerialize;
/**
 * This class describes the relationsip between the interface(from rest service) fields
 * and workflow key. This is a single one to one mapping event field.
 * If the need arises, Turn this into a many to many
 * event field mapping.
 *
 *
 * It is also designed as a one way street.  Going from interface to workflow.
 * It can be extended to do two way comparisons on the Transformer and Validator.
 */
@JsonSerialize(using = EventSerializer.class)
public class EventField {
    /** the value of the field */
    private String value;

    /** The incoming key, from the service */
    public final String interfaceKey;

    /** The key, as it goes to the workflow engine */
    public final String workflowKey;

    /** There can be only one transformer */
    private EventField.Transformer transformer;

    /** There can be multiple validators */
    private List<EventField.Validator> validators;

    /** List of attributes of the field.  Used primarily for validation */
    private List<EventField.Attribute> attributes;

    /**
     * The default constructor.
     */
    public EventField(String interfaceKey, String workflowKey){
        this.interfaceKey = interfaceKey;
        this.workflowKey  = workflowKey;
        this.transformer  = getDefaultTransformer();
        this.validators   = new ArrayList<EventField.Validator>();
        this.attributes   = new ArrayList<EventField.Attribute>();

        validators.add(getDefaultValidator());
    }

    /**
     * Returns the field's attributes.
     */
    public List<EventField.Attribute> getAttributes(){
        return attributes;
    }

    public EventField(
            String interfaceKey, String workflowKey,
            EventField.Attribute... attributes){

        this.interfaceKey = interfaceKey;
        this.workflowKey  = workflowKey;
        this.transformer  = getDefaultTransformer();
        this.validators   = new ArrayList<EventField.Validator>();
        this.attributes   = new ArrayList<EventField.Attribute>();


        this.attributes.addAll(Arrays.asList(attributes));
        validators.add(getDefaultValidator());
    }

    /**
     * Adds an attribute
     */
    public EventField add(EventField.Attribute attr){
        this.attributes.add(attr);

        return this;
    }

    /**
     * Adds a validator
     */
    public EventField add(EventField.Validator validator){
        this.validators.add(validator);

        return this;
    }

    /**
     * Sets the value of the field.
     */
    public void set(String value){
        this.value = value;
    }

    /**
     * Gets the raw value of the field.
     */
    public String getValue(){
        return this.value;
    }

    /**
     * Get the value for workflow
     */
    public String getWorkflowValue(){
        return this.transformer.toWorkflow(this.value);
    }

    /**
     * The default transformer is an identity transfomer.
     */
    public EventField.Transformer getDefaultTransformer(){
        return new EventField.Transformer(){
            @Override
            public String toWorkflow(String value){
                return value;
            }
        };
    }

    /**
     * Validates the field.
     */
    public List<String> validate(){
        for(EventField.Validator validator: validators){
            if(!validator.validate(this)){
               return validator.getFailReason();
            }
        }

        return null;
    }

    /**
     * The defalut validator checks for REQUIRED, not null.
     */
    public EventField.Validator getDefaultValidator(){
        return new EventField.Validator(){
            List<String> failReason;

            @Override
            public boolean validate(EventField field){
                failReason = new ArrayList<String>();
                boolean validates = true;
                String value = field.getValue();
                String key = field.interfaceKey;

                System.out.println("Checking: " + key + " ["+ value+"]");
                if(field.attributes.indexOf(Attribute.REQUIRED) > -1 && value == null) {
                    System.out.println("DefVal: " + key + " " + Attribute.REQUIRED);
                    failReason.add("field: " + field.interfaceKey + " has attribute " + Attribute.REQUIRED);
                    validates = false;
                }

                return validates;
            }

            @Override
            public List<String> getFailReason(){
                return failReason;
            }
        };
    }

    /**
     * Add new attributes as required.
     */
    public enum Attribute {
        REQUIRED, OPTIONAL, ENUM
    }

    /**
     * Interface to validate values from event.  Currently only designed to validate to workflow.
     */
    public interface Validator {
        public boolean validate(EventField field);
        public List<String> getFailReason();
    }

    /**
     * Interface to transform value to workflow. Currently designed to transform
     * interface to workflow.
     */
    public interface Transformer {
        public String toWorkflow(String value);
    }
}
