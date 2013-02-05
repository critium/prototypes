package test.events;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.core.MultivaluedMap;

import static test.events.EventField.Attribute.*;
public class SPEResponse
    extends GenericResponse {

    @Override
    public String getResponseName(){
        return "speResponse";
    }
    @Override
    public String getType(){
        return "speResponse";
    }

}
