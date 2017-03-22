package app.todo.todoappjustui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private ArrayList<Task> myDataset = new ArrayList<>();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                mAdapter.remove(task);
            }
        }));

        // fill in our container (mAdapter) with some dummy data
        prepareDummyTasks();

    }

    private void prepareDummyTasks() {
        for (int i = 0; i < 15; i++) {
            Task task = new Task();
            myDataset.add(task);
            Log.d(TAG, task.toString());
        }

        mAdapter.notifyDataSetChanged();
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
                                Task task = new Task(taskText);
                                mAdapter.add(task);

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

}
