package app.todo.todoappraw.db;

import android.provider.BaseColumns;

/**
 * Jest to pomocnicza klasa która zawiera definicje
 * Stringów które służą do tworzenia zapytań SQL
 */
public class TaskContract {

    /**
     * Nazwa bazy danych
     */
    public static final String DB_NAME = "todo.db";

    /**
     * Wersja bazy danych
     */
    public static final int DB_VERSION = 1;


    public class TaskEntry implements BaseColumns {

        /**
         * Nazwa tablicy którą stworzymy w naszej bazie danych.
         */
        public static final String TABLE = "tasks";

        /**
         * Kolumna z tablicy o nazwie title
         */
        public static final String COL_TASK_TITLE = "title";
        public static final String COL_TASK_DATE = "date";

    }
}
