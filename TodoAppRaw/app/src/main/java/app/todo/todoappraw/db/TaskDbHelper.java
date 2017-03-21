package app.todo.todoappraw.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Jest to postawowa funkcja która służy do komunikacji z bazą SQLite
 */
public class TaskDbHelper extends SQLiteOpenHelper {

    /**
     * Konstruktor w którym wywołjemy konstruktor klasy nadrzędnej @SQLiteOpenHelper,
     * któremy przekazujemy kontekst, nazwę bazy danych i jej wersję
     * @param context
     */
    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    /**
     * Funkcja wywoływana automatycznie pierwszy raz kiedy otwieramy naszą aplikację.
     * Tworzymy w niej tabelę korzystając z polecenia SQL CREATE TABLE
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL," +
                TaskContract.TaskEntry.COL_TASK_DATE + " TEXT);";

        db.execSQL(createTable);
    }

    /**
     * Funkcja wywoływana kiedy w aplikacji mamy nową wersję bazy danych.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
    }
}
