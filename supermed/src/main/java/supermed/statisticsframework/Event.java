package supermed.statisticsframework;

/**
 * Created by Alexander on 14.12.2016.
 */
public class Event {
    private String expectedStartDate;
    private String expectedEndDate;
    private String actualStartDate;
    private String actualEndDate;
    private EventStatus status;

    public Event(String start, String end) {
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
}
