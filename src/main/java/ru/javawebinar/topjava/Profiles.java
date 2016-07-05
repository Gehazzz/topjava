package ru.javawebinar.topjava;

/**
 * User: gkislin
 * Date: 19.08.2014
 */
public class Profiles {
    public static final String
            POSTGRES = "postgres",
            HSQLDB = "hsqldb",
            JDBC = "jdbc",
            JPA = "jpa",
            DATA_JPA = "datajpa";

    public static final String ACTIVE_DB = POSTGRES;
    public static final String ACTIVE_JPA = JPA;
    public static final String ACTIVE_JDBC = JDBC;
    public static final String ACTIVE_HSQL = HSQLDB;
    public static final String ACTIVE_DATA_JPA = DATA_JPA;
}
