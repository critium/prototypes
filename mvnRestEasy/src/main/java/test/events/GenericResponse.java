package test.events;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Set;
import java.util.EnumSet;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.core.MultivaluedMap;

import static test.events.EventField.Attribute.*;
public abstract class GenericResponse
    extends Event implements WorkflowEvent {

    @Override
    public String namespace(){
        return "requestSignatureWorkflow";
    }

    @Override
    public List<EventField> describeFields(){
        List<EventField> myFields = new ArrayList<EventField>();

        EnumSet<Response> enums = EnumSet.allOf(GenericResponse.Response.class);

        EnumEventField response = new EnumEventField
            (getResponseName(), getResponseName(), enums);

        response
            .add(REQUIRED)
            .add(ENUM)
            .add(new GenericResponse.EnumValidator());

        myFields.add(response);

        return myFields;
    }

    @Override
    public Map<String, String> defaultValues(){
        Map<String, String> defValues = new HashMap<String, String>();

        defValues.put("type", getType());

        return defValues;
    }

    public abstract String getResponseName();
    public abstract String getType();

    public enum Response {
        signed, rejected, cancelled
    }

    public class EnumValidator implements EventField.Validator {
        List<String> failReason;

        @Override
        public boolean validate(EventField field){
           failReason   = new ArrayList<String>();
           String value = field.getValue();
           String key   = field.interfaceKey;

           System.out.println("EnumVal: " +  key + ":" + value);
           if(field instanceof EnumEventField){
               EnumEventField iField = (EnumEventField) field;
               Set enums = iField.getEnums();
               for(Object e: enums){
                   System.out.println("ENUM: " + e.toString());
                   if( e.toString().equals(value) ){
                       return true;
                   }
               }
           }

           failReason.add("VALUE: " + value + " does not match enum set");

           return false;
        }

        @Override
        public List<String> getFailReason(){
            return failReason;
        }
    }
}
