package pt.unl.fct.di.apdc.firstwebapp.util;

public class ChangeRoleData {

    public String changer;
    public String user;
    public String old_role;
    public String new_role;

    public ChangeRoleData() {}

    public ChangeRoleData(String changer, String user, String old_role, String new_role) {
        this.changer = changer;
        this.user = user;
        this.old_role = old_role;
        this.new_role = new_role;
    }
}