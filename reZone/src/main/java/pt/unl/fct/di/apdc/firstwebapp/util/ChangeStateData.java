package pt.unl.fct.di.apdc.firstwebapp.util;

public class ChangeStateData {

    public String changer;
    public String user;
    public String old_state;
    public String new_state;

    public ChangeStateData() {}

    public ChangeStateData(String changer, String user, String old_state, String new_state) {
        this.changer = changer;
        this.user = user;
        this.old_state = old_state;
        this.new_state = new_state;
    }
}
