package test;
import java.util.List;
import java.util.ArrayList;

public class Group {
    public List<Role> roles;
    public String name;
    public boolean signatureAuthorityInd;
    public boolean supervisorInd;

    public Group(
            String name,
            boolean signatureAuthorityInd,
            boolean supervisorInd
            ) {
        this.name=name;
        this.signatureAuthorityInd = signatureAuthorityInd;
        this.supervisorInd = supervisorInd;
    }

    public List<Role> getRoles(){
        if(roles == null){
            roles = new ArrayList<Role>();
        }
        return roles;
    }
}
