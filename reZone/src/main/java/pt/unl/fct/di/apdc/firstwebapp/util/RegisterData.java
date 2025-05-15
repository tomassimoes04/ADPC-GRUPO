package pt.unl.fct.di.apdc.firstwebapp.util;

public class RegisterData {

    public String id, password, confirmation, email, full_name, phone, profile,
                    cc_bi, role, nif, company, job, address, company_nif, account_state;

    public RegisterData() {

    }

    public RegisterData(String email, String id, String full_name, String phone, String password, String confirmation, String profile,
                        String cc_bi, String role, String nif, String company, String job, String address, String company_nif, String account_state) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.confirmation = confirmation;
        this.full_name = full_name;
        this.phone = phone;
        this.profile = profile;
        this.cc_bi = cc_bi;
        this.role = role;
        this.nif = nif;
        this.company = company;
        this.job = job;
        this.address = address;
        this.company_nif = company_nif;
        this.account_state = account_state;
    }

    private boolean nonEmptyOrBlankField(String field) {
        return field != null && !field.isBlank();
    }

    public boolean validRegistration() {


        return nonEmptyOrBlankField(email) &&
                nonEmptyOrBlankField(id) &&
                nonEmptyOrBlankField(full_name) &&
                nonEmptyOrBlankField(phone) &&
                nonEmptyOrBlankField(password) &&
                nonEmptyOrBlankField(confirmation) &&
                nonEmptyOrBlankField(profile) &&
                email.contains("@") &&
                password.equals(confirmation) &&
                (profile.equals("private") || profile.equals("public")) &&
                (role.equals("ENDUSER") || role.equals("BACKOFFICE") || role.equals("ADMIN") || role.equals("PARTNER")) &&
                (account_state.equals("ACTIVE") || account_state.equals("INACTIVE") || account_state.equals("SUSPENDED"));
    }
}