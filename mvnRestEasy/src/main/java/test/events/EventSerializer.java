package test.events;

import java.util.List;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class EventSerializer extends JsonSerializer<EventField> {

    @Override
    public void serialize(
            EventField field,
            JsonGenerator g,
            SerializerProvider serializerProvider) throws IOException {

        // outer objectj
        g.writeStartObject();

        g.writeFieldName(field.interfaceKey);
        // inner object
        g.writeStartObject();
        g.writeStringField("value", field.getValue());
        g.writeFieldName("attributes");
        g.writeObject(field.getAttributes());
        //end inner object
        g.writeEndObject();

        //end outer object
        g.writeEndObject();

        // the following code is more readable, but who cares,
        // its written once.
/*
 *        g.writeStartObject();
 *        // using writeraw because using jsongenerator sucks.
 *        String json =
 *            c_param(field.interfaceKey,
 *                    s_param("value", field.getValue()),
 *                    c_param("attributes", array(field.getAttributes()))
 *                  );
 *
 *        g.writeRaw(json);
 *
 *        g.writeEndObject();
 */

    }

    public String array(List<EventField.Attribute> attributes){
        StringBuffer sb = new StringBuffer();

        sb.append('[');
        if(attributes != null){
            for(int i=0; i<attributes.size(); i++){
                EventField.Attribute attr = attributes.get(i);
                sb.append(s(attr.toString()));
                if(i>0 && i< attributes.size()-1) {
                    sb.append(',');
                }

            }
        }
        sb.append(']');
        return sb.toString();
    }

    private String s(String s){
        return '"' + s + '"';
    }


    /**
     * Complex param
     */
    public String c_param(String key, String... values){
        StringBuffer sb = new StringBuffer();

        if(key != null && values != null && values.length > 0){
            sb .append(s(key)) .append(':');
            if(values.length > 1){
                for(int i=0; i < values.length; i++){
                    String value = values[i];
                    sb.append(value);
                    if(i>0 && i<values.length-1) {
                        sb.append(',');
                    }
                }
            } else {
                sb.append(values[0]);
            }
        }
        return sb.toString();
    }

    /**
     * Simple param.
     */
    public String s_param(String key, String value){
        StringBuffer sb = new StringBuffer();

        if(key != null ){
            sb .append(s(key)) .append(':') .append(s(value)) ;
        }
        return sb.toString();
    }

    /**
     */
    public String object(String key, String... values){
        StringBuffer sb = new StringBuffer();

        if(key != null && values != null && values.length > 0){
            sb.append('{');
            sb.append(s(key)).append(':');
            sb.append('{');
            for(int i=0; i < values.length; i++){
                String value = values[i];
                sb.append(value);
                if(i>0 && i<values.length-1) {
                    sb.append(',');
                }
            }
            sb.append('}');
            sb.append('}');
        }
        return sb.toString();

    }
}

