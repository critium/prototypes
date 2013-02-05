
package test;

import test.events.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import javax.jms.JMSException;

import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Map;
import java.util.List;

@Path("event")
public class EventService {

	@POST
    @Consumes("application/x-www-form-urlencoded")
    public Response post(MultivaluedMap<String, String> form) {
        EventHelper helper = new EventHelper(form);

        // process the event
        Event thisEvent = helper.getEvent();

        // valdiatethe results
        Map<String,List<String>> errors = helper.validate(form);
        if(errors.size() >0 ) {
            String json1 = toJson(errors);

            // should i return the field descriptions back?
            String json2 = toJson(thisEvent.getFields());

            // IMPLEMENTOR, please package error better than this
            return Response.status(400).entity(json1+json2).build();
        }


        // in a transaction//
        try {
            System.out.println("Attempting to dispatch event");
            EventDispatcher.emit(thisEvent);
            return Response.status(200).entity(toJson(thisEvent.getContext())).build();

        } catch (/*this should be a declared exception*/ JMSException jmse){
            System.out.println("error in dispatch event");
            jmse.printStackTrace();
            return Response.status(503).entity(jmse.getMessage()).build();
        }
        // end transaction//
	}

    /**
     * Helper class to convert complex object to json text
     */
    private static String toJson(Object form) {
        ObjectMapper mapper = new ObjectMapper();
        String result = null;

        try {
            result = mapper.writeValueAsString(form);
        } catch (java.io.IOException ioe){
            ioe.printStackTrace();
        }

        return result;
    }
}

