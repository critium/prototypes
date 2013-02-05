package test;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import static test.Resource.*;
import static test.Permission.*;

public class UserDAO {
    private static Map<String, User> users;

    public User getUser(String userId){
        if(users == null){
            init();
        }
        return users.get(userId);
    }

    /*
     *The following is the creation of the test data
     */
    public void init(){

        // setup the basic roles
        Role ex = new Role("Examiner");
        Role re = new Role("Reviewer");
        Role su = new Role("Supervisor");

        // this is the join table roles_permission_resource;
        push(ex, CaseData, VIEW);
        push(re, CaseData, VIEW);
        push(su, CaseData, VIEW);
        push(su, CaseData, REASSIGN);

        push(ex, OfficeAction, VIEW);
        push(ex, OfficeAction, EDIT);
        push(ex, OfficeAction, DELETE);
        push(ex, OfficeAction, CREATE);

        push(re, OfficeAction, VIEW);
        push(re, OfficeAction, EDIT);
        push(re, OfficeAction, DELETE);
        push(re, OfficeAction, CREATE);

        push(su, OfficeAction, CREATE);

        // setup the basic users
        User francis = new User("francis");
        User marlin  = new User("marlin" );
        User ken     = new User("ken"    );
        User kelly   = new User("kelly"  );

        Group tc1600 = new Group("TC-1600", true, true);
        Group tc1300 = new Group("TC-1300", true, false);
        tc1600.getRoles().add(ex);
        tc1600.getRoles().add(re);
        tc1600.getRoles().add(su);
        tc1300.getRoles().add(re);
        francis.getGroups().add(tc1600);
        francis.getGroups().add(tc1300);


        //francis.getRoles().add(ex);
        //francis.getRoles().add(re);
        //francis.getRoles().add(su);

        //marlin.getRoles().add(ex);
        //marlin.getRoles().add(re);

        //ken.getRoles().add(ex);

        //kelly.getRoles().add(ex);
        //kelly.getRoles().add(re);
        //kelly.getRoles().add(su);

        users = new HashMap<String, User>();
        users.put(francis.loginId, francis);
        users.put(marlin.loginId, marlin);
        users.put(ken.loginId, ken);
        users.put(kelly.loginId, kelly);
    }

    private void push(Role role, Resource resource, Permission permission){
        Map<Resource, List<Permission>> permissionMap = role.getPermissions();
        List<Permission> permissions = permissionMap.get(resource);
        if(permissions == null){
            permissions = new ArrayList<Permission>();
        }

        permissions.add(permission);

        permissionMap.put(resource, permissions);
    }
}
