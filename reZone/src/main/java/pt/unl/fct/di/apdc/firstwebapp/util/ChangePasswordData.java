package pt.unl.fct.di.apdc.firstwebapp.util;

public class ChangePasswordData {

    public String user;
    public String old_password;
    public String new_password;

    public ChangePasswordData() {}

    public ChangePasswordData(String user, String old_password, String new_password) {
        this.user = user;
        this.old_password = old_password;
        this.new_password = new_password;
    }
}
