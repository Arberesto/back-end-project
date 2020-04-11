package it.sevenbits.taskmanager.core.repository.tasks;

public enum PaginationSort {

    /**
     *
     */

    DESC,

    /**
     *
     */

    ASC;

    /**
     * Get sort order from string
     * @param value string value
     * @return correct PaginationSort or null if uncorrect;
     */

    public static PaginationSort resolveString(final String value) {
        if (value == null) {
            return null;
        }

        switch (value.toUpperCase()) {
            case "DESC":
                return PaginationSort.DESC;
            case "ASC" :
                return PaginationSort.ASC;
            default:
                return null;
        }

    }
}
