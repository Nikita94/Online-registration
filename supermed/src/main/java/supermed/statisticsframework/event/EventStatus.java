package supermed.statisticsframework.event;

/**
 * Created by Alexander on 14.12.2016.
 */
public enum EventStatus {
    PLANNED("planned"), FINISHED("finished"), DENIED("denied"), POSTPONED("postponed");
    String status;

    EventStatus(String role) {
        this.status = role;
    }

    public String getName() {
        return status;
    }

    public static EventStatus createStatus(String status) {
        if (status != null) {
            for (EventStatus resource : EventStatus.values()) {
                if (status.equalsIgnoreCase(resource.status)) {
                    return resource;
                }
            }
        }
        return null;
    }
}
