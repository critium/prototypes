package test.domain;

import java.util.Map;
import java.util.HashMap;

/**
 * Domain object of linkage of Case to Process Instance.  Since this is a placeholder, there is no
 * database backing this domain object and its just a plain simple map.
 */
public class CaseProcess {
    private Map<String, String> thedatabase = new HashMap<String, String>();

    public CaseProcess(){
    }

    public String getProcessForCase(String caseId){
        return thedatabase.get(caseId);
    }

    public void setProcessForCase(String caseId, String processId){
        thedatabase.put(caseId, processId);
    }
}
