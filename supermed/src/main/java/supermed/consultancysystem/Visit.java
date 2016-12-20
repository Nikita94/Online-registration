package supermed.consultancysystem;

import supermed.statisticsframework.Event;
import supermed.statisticsframework.Schedule;

/**
 * Created by Alexander on 14.12.2016.
 */
public class Visit extends Event {

    private String appointment;
    private String anamnesis;
    private String diagnosis;
    private String patientID;

    public Visit(String eventID, String doctorId, String patientId, String start, String end) {
        super(eventID, doctorId, start, end);
        this.patientID = patientId;
    }

    public void setMedicalDisposal(String disposal) {

    }

    public void setMedicalDiagnosis(String diagnosis) {

    }

    public void setActualStartDate(String startDate) {

    }

    public void setActualEndDate(String endDate) {

    }

}
