package it.sevenbits.taskmanager.core.model;

/**
 * Enum for status of task
 */

public enum TaskStatus {

    /**
     * Task in progress
     */
    inbox,

    /**
     * Task is complete
     */
    done,

    /**
     * Empty task
     */
    empty;

    /**
     * Get TaskStatus status by its String version
     * @param status status to convert into TaskStatus
     * @return TaskStatus Object
     */

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

    /**
     * Compare this TaskStatus with another
     * @param status another TaskStatus Object
     * @return true if equal, false if not
     */

    public boolean is(final TaskStatus status) {
         return TaskStatus.this == status;
    }

}
