package app.todo.todoappormraw;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class TaskOpenDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "todo";
    private static final int DATABASE_VERSION = 1;

    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     */
    private Dao<Task, Long> taskDao;
    private Dao<TaskOwner, Long>  taskOwnerDao;

    public TaskOpenDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION,
                /**
                 * R.raw.ormlite_config is a reference to the ormlite_config.txt file in the
                 * /res/raw/ directory of this project
                 * */
                R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            /**
             * creates the Todo database table
             */
            TableUtils.createTable(connectionSource, Task.class);
            TableUtils.createTable(connectionSource, TaskOwner.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            /**
             * Recreates the database when onUpgrade is called by the framework
             */
            TableUtils.dropTable(connectionSource, Task.class, false);
            TableUtils.dropTable(connectionSource, TaskOwner.class, false);
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an instance of the data access object
     * @return
     * @throws SQLException
     */
    public Dao<Task, Long> getTaskDao() throws SQLException {
        if(taskDao == null) {
            taskDao = getDao(Task.class);
        }
        return taskDao;
    }

    /**
     * Returns an instance of the data access object
     * @return
     * @throws SQLException
     */
    public Dao<TaskOwner, Long> getTaskOwnerDao() throws SQLException {
        if(taskOwnerDao == null) {
            taskOwnerDao = getDao(TaskOwner.class);
        }
        return taskOwnerDao;
    }
}