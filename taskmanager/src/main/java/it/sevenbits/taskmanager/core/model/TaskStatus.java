package it.sevenbits.taskmanager.core.model;


public enum TaskStatus {
    inbox,
    done,empty;

     public static TaskStatus resolveString(final String status) {
        switch (status) {
            case "inbox":
                return TaskStatus.inbox;
            case "done" :
                return TaskStatus.done;
            default:
                return TaskStatus.empty;
        }

    }
}
