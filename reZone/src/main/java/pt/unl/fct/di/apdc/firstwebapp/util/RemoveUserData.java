package pt.unl.fct.di.apdc.firstwebapp.util;

public class RemoveUserData {

    public String deleter;
    public String deleter_role;
    public String user;
    public String user_role;

    public RemoveUserData() {}

    public RemoveUserData(String deleter, String deleter_role, String user, String user_role) {
        this.deleter = deleter;
        this.deleter_role = deleter_role;
        this.user = user;
        this.user_role = user_role;
    }
}
