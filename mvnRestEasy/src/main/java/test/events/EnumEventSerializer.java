package test.events;

import java.util.List;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class EnumEventSerializer extends JsonSerializer<EnumEventField> {

    @Override
    public void serialize(
            EnumEventField field,
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
        g.writeFieldName("enum");
        g.writeObject(field.getEnums());
        //end inner object
        g.writeEndObject();

        //end outer object
        g.writeEndObject();
    }
}

