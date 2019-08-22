package it.sevenbits.taskmanager.core.model.Task;

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
    done;

    /**
     * Get TaskStatus status by its String version
     * @param status status to convert into TaskStatus
     * @return TaskStatus Object
     */

     public static TaskStatus resolveString(final String status) {
        if (status == null) {
            return null;
        }

        switch (status) {
            case "inbox":
                return TaskStatus.inbox;
            case "done" :
                return TaskStatus.done;
            default:
                return null;
        }

    }

    /**
     * Get TaskStatus status by its String version or get default status
     * @param status status to convert into TaskStatus
     * @param defaultStatus default status
     * @return TaskStatus Object
     */

    public static TaskStatus resolveStringOrDefault(final String status, final TaskStatus defaultStatus) {
        if (status == null) {
            return defaultStatus;
        }

        switch (status) {
            case "inbox":
                return TaskStatus.inbox;
            case "done" :
                return TaskStatus.done;
            default:
                return defaultStatus;
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

    /**
     * Compare String TaskStatus with some TaskStatus
     * @param stringStatus some TaskStatus in String form
     * @return true if equal, false if not
     */

    public boolean is(final String stringStatus) {
        return TaskStatus.this == TaskStatus.resolveString(stringStatus);
    }

}
