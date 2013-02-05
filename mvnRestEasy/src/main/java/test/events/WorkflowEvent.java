package test.events;

import java.util.List;
import java.util.Map;

public interface WorkflowEvent {
    public List<EventField> describeFields();
    public Map<String, String> defaultValues();
    public String namespace();
}
