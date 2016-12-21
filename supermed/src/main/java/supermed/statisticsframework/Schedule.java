package supermed.statisticsframework;

import supermed.usermanagementsystem.user.Employee;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexander on 19.12.2016.
 */
public class Schedule {
    private Employee employee;
    private List<Event> events;

    public Schedule() {

    }

    public Schedule(Employee employee, List<Event> events) {
        this.employee = employee;
        this.events = events;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        events = new LinkedList<Event>();
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public Employee getEmployee() {
        return employee;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }
}
