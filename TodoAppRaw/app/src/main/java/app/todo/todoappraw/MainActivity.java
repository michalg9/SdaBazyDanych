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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TaskDbHelper(this);

        // 1 ZAPISYWANIE DO BAZY DANYCH #################################
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        // PRZYKLAD 1 ---------------------------------------------------

        // Content Values zawiera wartości które wstawimy do bazy danych
        ContentValues values = new ContentValues();

        // pierwszy argument funkcji put to nazwa kolumny w bazie danych, drugi to zapisywana wartość
        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, "Nasza pierwsza notatka");

        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);


        // PRZYKLAD 2 ---------------------------------------------------

        ContentValues values2 = new ContentValues();

        values2.put(TaskContract.TaskEntry.COL_TASK_TITLE, "Nasza druga notatka");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateAndTime = sdf.format(new Date());

        values2.put(TaskContract.TaskEntry.COL_TASK_DATE, currentDateAndTime);

        db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                null,
                values2,
                SQLiteDatabase.CONFLICT_REPLACE);



        // 2 ODCZYTYWANIE Z BAZY DANYCH #################################


        // PRZYKLAD 1 ---------------------------------------------------

        Cursor cursor = db.rawQuery("SELECT " + TaskContract.TaskEntry.COL_TASK_TITLE + " FROM " + TaskContract.TaskEntry.TABLE,
                null);

        while (cursor.moveToNext()) {
            int columnIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            String title = cursor.getString(columnIndex);
            Log.d(TAG, "Task title: " + title);
        }


        // 3 USUWANIE Z BAZY DANYCH #################################

        // PRZYKLAD 1 - usuwamy wszystko
        db.delete(TaskContract.TaskEntry.TABLE, null, null);


    }
}
