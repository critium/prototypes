package test.events;

import java.util.EnumSet;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(using =EnumEventSerializer.class)
public class EnumEventField<E extends Enum<E>> extends EventField {
    private EnumSet<E> enums;

    public EnumEventField(String interfaceKey, String workflowKey, EnumSet<E> enums){
        super(interfaceKey, workflowKey);
        this.enums = enums;
    }

    public EnumSet<E> getEnums(){
        return enums;
    }
}
