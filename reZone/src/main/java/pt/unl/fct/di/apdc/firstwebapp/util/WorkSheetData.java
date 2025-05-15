package pt.unl.fct.di.apdc.firstwebapp.util;

public class WorkSheetData {

    public String work_ref;
    public String work_description;
    public String work_target;
    public String work_adjudication_state;
    public String work_adjudication_date;
    public String work_starting_date;
    public String work_conclusion_date;
    public String work_entity_account;
    public String work_adjudication_identity;
    public String work_company_nif;
    public String work_state;
    public String work_observations;



    public WorkSheetData() {}

    public WorkSheetData(String work_ref, String work_description, String work_target, String work_adjudication_state,
                         String work_adjudication_date, String work_starting_date, String work_conclusion_date, String work_entity_account,
                         String work_adjudication_identity, String work_company_nif, String work_state, String work_observations) {
        this.work_ref = work_ref;
        this.work_description = work_description;
        this.work_target = work_target;
        this.work_adjudication_state = work_adjudication_state;
        this.work_adjudication_date = work_adjudication_date;
        this.work_starting_date = work_starting_date;
        this.work_conclusion_date = work_conclusion_date;
        this.work_entity_account = work_entity_account;
        this.work_adjudication_identity = work_adjudication_identity;
        this.work_company_nif = work_company_nif;
        this.work_state = work_state;
        this.work_observations = work_observations;
    }

    private boolean nonEmptyOrBlankField(String field) {
        return field != null && !field.isBlank();
    }

    public boolean validRegistration() {
        if (work_adjudication_state.equals("AJUDICATED"))
            return nonEmptyOrBlankField(work_ref) &&
                nonEmptyOrBlankField(work_description) &&
                nonEmptyOrBlankField(work_target) &&
                nonEmptyOrBlankField(work_adjudication_state);
        else if(work_adjudication_state.equals("NON ADJUDICATED"))
            return nonEmptyOrBlankField(work_ref) &&
                    nonEmptyOrBlankField(work_description) &&
                    nonEmptyOrBlankField(work_target) &&
                    nonEmptyOrBlankField(work_adjudication_state) &&
                    !nonEmptyOrBlankField(work_starting_date) &&
                    !nonEmptyOrBlankField(work_conclusion_date) &&
                    !nonEmptyOrBlankField(work_entity_account) &&
                    !nonEmptyOrBlankField(work_adjudication_identity) &&
                    !nonEmptyOrBlankField(work_company_nif) &&
                    !nonEmptyOrBlankField(work_state) &&
                    !nonEmptyOrBlankField(work_observations) &&
                    (work_state.equals("NOT STARTED") || work_state.equals("ON COURSE") || work_state.equals("COMPLETED"));
        return false;
    }
}
