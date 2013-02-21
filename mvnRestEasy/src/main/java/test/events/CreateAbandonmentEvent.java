
package test.events;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.core.MultivaluedMap;

import static test.events.EventField.Attribute.*;
/**
 * class=test.events.RequestSignatureEvent&type=new&source=user&contextId=01&author_user_id=john&examiner_user_id=fdelacruz&spe_user_id=sales-rep&author_email=request.signature.workflow@uspto.gov&examiner_email=francis.delacruz@uspto.gov&spe_email=francis.delacruz@uspto.gov
 */
public class CreateAbandonmentEvent
    extends BeginWorkflowEvent {

    @Override
    public String namespace(){
        return "this should be deprecated";
    }

    @Override
    public Map<String, String> defaultValues(){
        return null;
    }

}
