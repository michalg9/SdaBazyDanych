package app.todo.todoappraw;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

// Dwa importy potrzebne do odczytania daty i czasu
import java.text.SimpleDateFormat;
import java.util.Date;


import app.todo.todoappraw.db.TaskContract;
import app.todo.todoappraw.db.TaskDbHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    TaskDbHelper dbHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TaskDbHelper(this);
        db = dbHelper.getWritableDatabase();

        // just cleaning the database for examples to work
        dbHelper.onUpgrade(db, 0, 0);

        // 1 ZAPISYWANIE DO BAZY DANYCH #################################
        Log.d(TAG, "1 ZAPISYWANIE DO BAZY DANYCH #################################");

        // PRZYKLAD 1 dodajemy tylko jedno pole (title), date bedzie null-------------------------
        Log.d(TAG, "PRZYKLAD 1 dodajemy tylko jedno pole (title), date bedzie null------------");

        // Content Values zawiera wartości które wstawimy do bazy danych
        ContentValues values = new ContentValues();

        // pierwszy argument funkcji put to nazwa kolumny w bazie danych, drugi to zapisywana wartość
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, "Nasza pierwsza notatka");

        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);


        // PRZYKLAD 2 dodajemy dwa pola (title i date) ------------------------------------------
        Log.d(TAG, "PRZYKLAD 2 dodajemy dwa pola (title i date) -------------------");

        ContentValues values2 = new ContentValues();

        values2.put(TaskContract.TaskEntry.COL_TASK_TITLE, "Nasza druga notatka");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateAndTime = sdf.format(new Date());

        values2.put(TaskContract.TaskEntry.COL_TASK_DATE, currentDateAndTime);

        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values2,
                SQLiteDatabase.CONFLICT_REPLACE);

        // PRZYKLAD 3 dodajemy notatki w petli (zeby zapelnic baze)  ---------------------------
        Log.d(TAG, "PRZYKLAD 3 dodajemy notatki w petli (zeby zapelnic baze)  -----------");

        for (int i = 1; i <= 10; i++) {
            values = new ContentValues();
            values.put(TaskContract.TaskEntry.COL_TASK_TITLE, "Nasza notatka numer " + String.valueOf(i));
            currentDateAndTime = sdf.format(new Date());
            values.put(TaskContract.TaskEntry.COL_TASK_DATE, currentDateAndTime);

            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);

            Log.d(TAG, "insert successful");

        }



        // 2 ODCZYTYWANIE Z BAZY DANYCH #################################
        Log.d(TAG, "2 ODCZYTYWANIE Z BAZY DANYCH #################################");


        // PRZYKLAD 1 rawQuery, reczne tworzenie zapytania SELECT ------------------------------

        Log.d(TAG, "PRZYKLAD 1 rawQuery, reczne tworzenie zapytania SELECT ---------------------");

        Cursor cursor = db.rawQuery("SELECT " + TaskContract.TaskEntry.COL_TASK_TITLE + " FROM " + TaskContract.TaskEntry.TABLE,
                null);

        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            String title = cursor.getString(columnIndex);
            Log.d(TAG, "Task Title: " + title);
        }

        // PRZYKLAD 2 koorzystanie z funkcji pomocniczej query ------------------------------

        Log.d(TAG, "PRZYKLAD 2 koorzystanie z funkcji pomocniczej query ---------------------");

        // odpytujemy baze danych o notatki miedzy indeksami 3 i 7
        int startIndex = 3;
        int endIndex = 7;

        cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[] {TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                TaskContract.TaskEntry._ID + " BETWEEN ? AND ?",
                new String[] {String.valueOf(startIndex), String.valueOf(endIndex)},
                null, null, null);

        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
            int id = cursor.getInt(columnIndex);

            columnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            String title = cursor.getString(columnIndex);
            Log.d(TAG, "ID: " + String.valueOf(id) + " Title: " + title);
        }

        // PRZYKLAD 3 koorzystanie z funkcji pomocniczej query 2------------------------------

        Log.d(TAG, "PRZYKLAD 3 koorzystanie z funkcji pomocniczej query 2 ---------------------");

        // odpytujemy baze danych o notatki miedzy indeksami 3 i 7
        startIndex = 3;
        endIndex = 5;

        cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[] {TaskContract.TaskEntry.COL_TASK_TITLE},
                TaskContract.TaskEntry._ID + " BETWEEN ? AND ?",
                new String[] {String.valueOf(startIndex), String.valueOf(endIndex)},
                null, null, null);

        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            String title = cursor.getString(columnIndex);
            Log.d(TAG, "Title: " + title);
        }



        // 3 USUWANIE Z BAZY DANYCH #################################
        Log.d(TAG, "3 USUWANIE Z BAZY DANYCH #################################");

        // PRZYKLAD 1 - usuniecie elementu o ID = 6 ------------------
        Log.d(TAG, "PRZYKLAD 1 - usuniecie elementu o ID = 6 -------------");

        int indexToDelete = 6;
        db.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry._ID + " = ?",
                new String[] {String.valueOf(indexToDelete)});

        fetchAllDatabase();

        // PRZYKLAD 2 - usuwamy wszystko ----------------------------
        Log.d(TAG, "PRZYKLAD 2 - usuwamy wszystko ---------------");

        db.delete(TaskContract.TaskEntry.TABLE, null, null);

        fetchAllDatabase();


    }


    private void fetchAllDatabase() {
        Log.d(TAG, "fetchAllDatabase");

        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[] {TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE, TaskContract.TaskEntry.COL_TASK_DATE},
                null,
                null,
                null, null, null);

        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex(TaskContract.TaskEntry._ID);
            int id = cursor.getInt(columnIndex);

            columnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            String title = cursor.getString(columnIndex);

            columnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DATE);
            String date = cursor.getString(columnIndex);

            Log.d(TAG, "ID: " + String.valueOf(id) + " Title: " + title + " Date: " + date);
        }
    }
}
