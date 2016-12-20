package supermed.statisticsframework;

/**
 * Created by Alexander on 14.12.2016.
 */
public class Event {
    private String eventID;
    private String employeeID;
    private String expectedStartDate;
    private String expectedEndDate;
    private String actualStartDate;
    private String actualEndDate;
    private EventStatus status;

    public Event(String id, String employeeID, String start, String end) {
        this.eventID = id;
        this.employeeID = employeeID;
        expectedStartDate = start;
        expectedEndDate = end;
        actualStartDate = start;
        actualEndDate = end;
    }

    public void setActualStartDate(String startDate) {
        actualEndDate = startDate;
    }

    public void setActualEndDate(String endDate) {
        actualEndDate = endDate;
    }

    public String getActualStartDate() {
        return actualStartDate;
    }

    public String getActualEndDate() {
        return actualEndDate;
    }

    public String getExpectedStartDate() {
        return expectedStartDate;
    }

    public String getExpectedEndDate() {
        return expectedEndDate;
    }

    public String getExpectedStartDay() {
        return expectedStartDate.substring(0, 10);
    }

    public String getExpectedEndDay() {
        return expectedEndDate.substring(0, 10);
    }

    public String getExpectedStartTime() {
        return expectedStartDate.substring(11);
    }

    public String getExpectedEndTime() {
        return expectedEndDate.substring(11);
    }

    public String getActualStartTime() {
        return actualStartDate.substring(11);
    }

    public String getActualEndTime() {
        return actualEndDate.substring(11);
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public EventStatus getStatus() {
        return status;
    }
}
