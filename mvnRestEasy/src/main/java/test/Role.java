package test;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Role {
    public String name;
    private Map<Resource, List<Permission>> permission;

    public Role(String roleName){
        this.name = roleName;
    }

    public Map<Resource, List<Permission>> getPermissions(){
        if(permission == null) {
            permission = new HashMap<Resource, List<Permission>>();
        }
        return permission;
    }
}
