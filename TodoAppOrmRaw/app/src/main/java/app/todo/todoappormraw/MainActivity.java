package app.todo.todoappormraw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TaskOpenDatabaseHelper dbHelper;
    Dao<Task, Long> taskDao;
    Dao<TaskOwner, Long> taskOwnerDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = OpenHelperManager.getHelper(this,
                TaskOpenDatabaseHelper.class);

        try {
            taskDao = dbHelper.getTaskDao();
            taskOwnerDao = dbHelper.getTaskOwnerDao();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // just cleaning the database for examples to work
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), 0, 0);

        // 1 ZAPISYWANIE DO BAZY DANYCH #################################
        Log.d(TAG, "1 ZAPISYWANIE DO BAZY DANYCH #################################");

        // PRZYKLAD 1 dodajemy tylko jedno pole (title), date bedzie null-------------------------
        Log.d(TAG, "PRZYKLAD 1 dodajemy tylko jedno pole (title), date bedzie null------------");

        Task task = new Task("Nasza pierwsza notatka", null, null);


        try {
            testOrmLite();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void testOrmLite() throws SQLException {
        // just cleaning the database for examples to work
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), dbHelper.getConnectionSource(), 0, 0);

        // 1 ZAPISYWANIE DO BAZY DANYCH #################################

        // PRZYKLAD 1 dodajemy tylko jedno pole (title), date bedzie null-------------------------
        Task task = new Task("Nasza pierwsza notatka", null, null);
        taskDao.create(task);

        // PRZYKLAD 2 dodajemy dwa pola (title i date) ------------------------------------------
        task = new Task("Nasza druga notatka");
        taskDao.create(task);

        // PRZYKLAD 3 dodajemy notatki w petli (zeby zapelnic baze)  ---------------------------
        List<Task> taskList = new ArrayList<Task>();
        for (int i = 1; i <= 10; i++) {
            task = new Task("Nasza notatka " + String.valueOf(i));
            taskList.add(task);
        }
        taskDao.create(taskList);


        // 2 ODCZYTYWANIE Z BAZY DANYCH #################################

        // PRZYKLAD 1 rawQuery, reczne tworzenie zapytania SELECT ------------------------------

        GenericRawResults<Task> results = taskDao.queryRaw("select * from task",
                new RawRowMapper<Task>() {
                    @Override
                    public Task mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                        return new Task(Long.parseLong(resultColumns[0]), resultColumns[1], resultColumns[2], resultColumns[3]);
                    }
                });

        for (Task t:
                results) {
            Log.d(TAG, t.toString());
        }

        // PRZYKLAD 2 koorzystanie z funkcji pomocniczej query ------------------------------
        // odpytujemy baze danych o notatki miedzy indeksami 3 i 7
        int startIndex = 3;
        int endIndex = 7;
        QueryBuilder<Task, Long> queryBuilder = taskDao.queryBuilder();
        queryBuilder.where().between("id", startIndex, endIndex);
        PreparedQuery<Task> preparedQuery = queryBuilder.prepare();
        List<Task> results3 = taskDao.query(preparedQuery);

        for (Task todo:
                results3) {
            Log.d("Result3: ", todo.toString());
        }

        // PRZYKLAD 3 queryForAll  ------------------------------
        List<Task> results2 = taskDao.queryForAll();
        for (Task todo:
                results2) {
            Log.d("Result: ", todo.toString());
        }

        // 3 USUWANIE Z BAZY DANYCH #################################
        // PRZYKLAD 1 - usuniecie elementu o ID = 6 ------------------

        int indexToDelete = 6;
        DeleteBuilder<Task, Long> deleteBuilder = taskDao.deleteBuilder();
        deleteBuilder.where().eq("id", indexToDelete);
        PreparedDelete<Task> preparedDelete = deleteBuilder.prepare();
        taskDao.delete(preparedDelete);

        fetchAllTasks();

        // PRZYKLAD 2 - usuniecie elementu o ID = 9 ------------------

        indexToDelete = 9;
        taskDao.deleteById(Long.valueOf(indexToDelete));

        fetchAllTasks();

        /// PRZYKLAD 3 - usuniecie wszystkich elementow------------------

        List<Task> resultsToDelete = taskDao.queryForAll();
        taskDao.delete(resultsToDelete);

        fetchAllTasks();



        // ################################################################
        // Przyklady z dwiema tabelami (foreign keys)
        // ################################################################

        TaskOwner ownerAdam = new TaskOwner("Adam");
        TaskOwner ownerMichal = new TaskOwner("Michal");
        TaskOwner ownerPiotr = new TaskOwner("Piotr");

        taskOwnerDao.create(ownerAdam);
        taskOwnerDao.create(ownerMichal);
        taskOwnerDao.create(ownerPiotr);



        task = new Task("New task 1 with owner");
        task.setTaskOwner(ownerAdam);
        taskDao.create(task);

        task = new Task("New task 2 with owner");
        task.setTaskOwner(ownerAdam);
        taskDao.create(task);

        Log.d(TAG, "Tasks1");
        Log.d(TAG, ownerAdam.toString());
        ForeignCollection<Task> tasksOfOwner = ownerAdam.getTasks();

        // NOT updated automatically, will give you null pointer exception here
        /*for (Task t:
                tasksOfOwner) {
            Log.d(TAG, task.toString());
        }*/
        fetchAllTasks();

        ownerAdam = taskOwnerDao.queryForId(ownerAdam.getId());


        Log.d(TAG, "Tasks2");
        Log.d(TAG, ownerAdam.toString());
        tasksOfOwner = ownerAdam.getTasks();
        for (Task t:
                tasksOfOwner) {
            Log.d(TAG, task.toString());
        }

        fetchAllTasks();


    }

    private void fetchAllTasks() {
        Log.d(TAG, "fetchAllTasks");
        try {
            List<Task> results = taskDao.queryForAll();
            for (Task task:
                    results) {
                Log.d("Result: ", task.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fetchAllOwners() {
        Log.d(TAG, "fetchAllOwners");
        try {
            List<TaskOwner> results = taskOwnerDao.queryForAll();
            for (TaskOwner taskOwner:
                    results) {
                Log.d("Result: ", taskOwner.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
