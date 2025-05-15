package pt.unl.fct.di.apdc.firstwebapp.util;

public class ChangeAttributesData {

    public String changer;
    public String user;

    public String new_id, new_password, new_email, new_full_name, new_phone, new_profile,
            new_cc_bi, new_role, new_nif, new_company, new_job, new_address, new_company_nif, new_account_state;

    public ChangeAttributesData() {}

    public ChangeAttributesData(String changer, String user,
                                String email, String new_id, String full_name, String phone, String new_password, String profile,
                                String cc_bi, String role, String nif, String company, String job, String address, String company_nif, String account_state) {
        this.changer = changer;
        this.user = user;
        this.new_id = new_id;
        this.new_email = email;
        this.new_password = new_password;
        this.new_full_name = full_name;
        this.new_phone = phone;
        this.new_profile = profile;
        this.new_cc_bi = cc_bi;
        this.new_role = role;
        this.new_nif = nif;
        this.new_company = company;
        this.new_job = job;
        this.new_address = address;
        this.new_company_nif = company_nif;
        this.new_account_state = account_state;
    }
}
