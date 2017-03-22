package app.todo.todoappuiandsql;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import app.todo.todoappuiandsql.db.TaskContract;
import app.todo.todoappuiandsql.db.TaskDbHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    //UI STUFF
    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private ArrayList<Task> myDataset = new ArrayList<>();;

    // DATABASE STUFF
    TaskDbHelper dbHelper;
    SQLiteDatabase db;


    private AsyncTask asyncTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new TaskDbHelper(this);
        db = dbHelper.getReadableDatabase();

        mRecyclerView = (RecyclerView) findViewById(R.id.todo_list);

        // use a linear layout manager
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)
        mAdapter = new TaskAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        // define delete task behavior on clicking the item on the list
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Task task = myDataset.get(position);
                Toast.makeText(getApplicationContext(), task.toString() + " is selected (press long to remove)!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Task task = myDataset.get(position);
                Toast.makeText(getApplicationContext(), task.toString() + " is removed!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Usuwamy task " + task.toString());

                asyncTask = new DeleteTask().execute(task.getTitle());

                //mAdapter.remove(task);
            }
        }));


        asyncTask = new LoadTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_add_task:
                Log.d(TAG, "Action add task");

                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Dodaj nowa notatke")
                        .setMessage("Jakie zadanie chcesz dodac?")
                        .setView(taskEditText)
                        .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String taskText = String.valueOf(taskEditText.getText());
                                Log.d(TAG, "Task to add: " + taskText);

                                Task currentTask = new Task(taskText);

                                ContentValues values = new ContentValues();
                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, currentTask.getTitle());
                                values.put(TaskContract.TaskEntry.COL_TASK_DATE, currentTask.getDate());

                                // we egonre ID column, because it will be handled by a database
                                asyncTask = new InsertTask().execute(values);

                                Log.d(TAG, "Zadanie dodane: " + taskText);

                            }
                        })
                        .setNegativeButton("Anuluj", null)
                        .create();

                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // Mozna wyczyscic to co dzieje sie w tych taskach
    // poneiwaz korzystamy z elastycznego RecyclerView
    // Zapraszam do sprobowania samemu (hint: mozemy dodawac i usuwac po jednym elemencie)
    // Nie musimy za kazdym razem odswierzac calej listy
    abstract private class BaseTask<T> extends AsyncTask<T, Void, Cursor> {
        @Override
        public void onPostExecute(Cursor result) {

            myDataset.clear();
            while(result.moveToNext()) {
                int colInd = result.getColumnIndex(TaskContract.TaskEntry._ID);
                int index = result.getInt(colInd);

                colInd = result.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
                String title = result.getString(colInd);

                colInd = result.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DATE);
                String date = result.getString(colInd);

                Task currentTask = new Task(index, title, date);
                myDataset.add(currentTask);
            }
            mAdapter.notifyDataSetChanged();

            //currentCursor = result;
            asyncTask = null;
        }

        Cursor doQuery() {
            Cursor result=
                    db
                            .query(TaskContract.TaskEntry.TABLE,
                                    new String[]{
                                            TaskContract.TaskEntry._ID,
                                            TaskContract.TaskEntry.COL_TASK_TITLE,
                                            TaskContract.TaskEntry.COL_TASK_DATE},
                                    null, null, null, null, null);

            result.getCount();

            return(result);
        }
    }

    private class LoadTask extends BaseTask<Void> {
        @Override
        protected Cursor doInBackground(Void... params) {
            return(doQuery());
        }
    }

    private class InsertTask extends BaseTask<ContentValues> {
        @Override
        protected Cursor doInBackground(ContentValues... values) {
            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                    null,
                    values[0],
                    SQLiteDatabase.CONFLICT_REPLACE);

            return(doQuery());
        }
    }

    private class DeleteTask extends BaseTask<String> {
        @Override
        protected Cursor doInBackground(String... value) {
            db.delete(TaskContract.TaskEntry.TABLE,
                    TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                    new String[]{value[0]});

            return(doQuery());
        }
    }

}
