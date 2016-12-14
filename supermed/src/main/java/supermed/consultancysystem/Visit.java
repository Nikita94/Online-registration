package supermed.consultancysystem;

import supermed.statisticsframework.Event;

/**
 * Created by Alexander on 14.12.2016.
 */
public class Visit extends Event {
    private String doctorID;
    private String patientID;
    public Visit(String doctorId, String patientId, String start, String end) {
        super(start, end);
        this.doctorID = doctorId;
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
