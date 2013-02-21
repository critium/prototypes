
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
public abstract class BeginWorkflowEvent
    extends Event implements WorkflowEvent {

    @Override
    public List<EventField> describeFields(){
        List<EventField> myFields = new ArrayList<EventField>();

        push(myFields, "author_user_id"   , REQUIRED);
        push(myFields, "examiner_user_id" , REQUIRED);
        push(myFields, "spe_user_id"      , REQUIRED);
        push(myFields, "author_email"     , REQUIRED);
        push(myFields, "examiner_email"   , REQUIRED);
        push(myFields, "spe_email"        , REQUIRED);

        return myFields;
    }

}
