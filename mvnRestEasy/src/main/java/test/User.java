package test;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class User {
    public Long id;
    public String loginId;
    public String firstName;
    public String lastName;

    public List<Group> groups;

    public User (String username){
        this.loginId = username;
    }

    public User (String loginId, String firstName, String lastName){
        this.loginId = loginId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public List<Group> getGroups(){
        if(groups == null){
            groups = new ArrayList<Group>();
        }

        return groups;
    }

    public Group getGroup(String group){
        for(Group g: getGroups()){
            if(group.equals(g.name)){
                return g;
            }
        }

        return null;

    }

    public List<Role> getRolesForGroup(String group){
        Group g = getGroup(group);

        if(g != null){
            return g.getRoles();
        }

        return null;
    }

}
