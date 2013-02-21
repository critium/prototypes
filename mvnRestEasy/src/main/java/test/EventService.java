
package test;

import test.events.*;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import javax.jms.JMSException;
import org.jboss.resteasy.annotations.providers.jaxb.WrappedMap;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ObjectMapper;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Path("event")
public class EventService {

    /**
     * this is the old way.  Have to migrate this to the new way.
     * 1. Create the cases end point
     * 2. Convert to JSON.
     *
     * Q1.  linkage of cases to workflow?  Link processinstanceid to applicationid.
     */

	@POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({MediaType.APPLICATION_JSON})
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


        // enter a transaction//
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

	@POST
    @Consumes("application/json")
    @Produces({MediaType.APPLICATION_JSON})
    @Path("passthrough")
    public Response passthrough(String rawForm) {
        try {
            HashMap<String,String> rawMessage =
                new ObjectMapper().readValue(rawForm, HashMap.class);

            System.out.println("RAWFORM: " + toJson(rawMessage));

            EventDispatcher.emit(rawMessage);

            return Response.status(200).entity(toJson(rawMessage)).build();

        } catch (java.io.IOException ioe){
            ioe.printStackTrace();
            return Response.status(503).entity(ioe.getMessage()).build();

        } catch (/*this should be a declared exception*/ JMSException jmse){
            jmse.printStackTrace();
            return Response.status(503).entity(jmse.getMessage()).build();

        }
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

