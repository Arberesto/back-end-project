package it.sevenbits.taskmanager.core.repository;

public enum PaginationSort {

    /**
     *
     */

    DESC,

    /**
     *
     */

    ASC;

    public static PaginationSort resolveString(final String order) {
        if (order == null) {
            return null;
        }

        switch (order.toUpperCase()) {
            case "DESC":
                return PaginationSort.DESC;
            case "ASC" :
                return PaginationSort.ASC;
            default:
                return null;
        }

    }
}
