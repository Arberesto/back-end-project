package it.sevenbits.taskmanager.web.security;

/**
 * Enum for user roles
 */

public enum AuthoritiesList {

    /**
     *
     */
    USER,

    /**
     *
     */

    ADMIN,

    /**
     *
     */

    ANONIMOUS;

    /**
     *Get AuthoritiesList from string
     * @param value string value
     * @return correct AuthoritiesList or null if uncorrect;
     */

    public static AuthoritiesList resolveString(final String value) {
        if (value == null) {
            return null;
        }

        switch (value.toUpperCase()) {
            case "USER":
                return AuthoritiesList.USER;
            case "ADMIN" :
                return AuthoritiesList.ADMIN;
            case "ANONIMOUS" :
                return AuthoritiesList.ANONIMOUS;
            default:
                return null;
        }

    }
}
