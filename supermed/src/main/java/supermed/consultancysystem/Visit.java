package supermed.consultancysystem;

import supermed.statisticsframework.event.Event;

/**
 * Created by Alexander on 14.12.2016.
 */
public class Visit extends Event {

    private String appointment;
    private String anamnesis;
    private String diagnosis;
    private String patientID;

    public Visit(String eventID, String doctorId, String branchID, String patientId, String
            start, String end) {
        super(eventID, doctorId, branchID, start, end);
        this.patientID = patientId;
    }

    public Visit(Event event, String patientID) {
        super(event);
        this.patientID = patientID;
    }

    public void setMedicalDisposal(String disposal) {

    }

    public void setMedicalDiagnosis(String diagnosis) {

    }

    public String getPatientID() {
        return patientID;
    }


}
