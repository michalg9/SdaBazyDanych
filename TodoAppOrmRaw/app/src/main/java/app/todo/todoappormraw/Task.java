package app.todo.todoappormraw;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.text.SimpleDateFormat;
import java.util.Date;

@DatabaseTable(tableName = "task")
public class Task {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(canBeNull = false)
    private String title;

    @DatabaseField
    private String dateCreated;

    @DatabaseField
    private String dateFinished;

    @DatabaseField(columnName = "taskOwner", foreign = true)
    private TaskOwner taskOwner;

    @Override
    public String toString() {
        String taskOwnerString;
        if (taskOwner != null) {
            taskOwnerString = taskOwner.toString();
        }
        else {
            taskOwnerString = "null";
        }
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateFinished=" + dateFinished +
                ", taskOwner=" + taskOwnerString +
                '}';
    }

    public Task(){
    }

    public Task(String title) {
        this.title = title;
        this.dateCreated = getDateTime();
    }

    public Task(String title, String dateCreated, String dateFinished) {
        this.title = title;
        this.dateCreated = dateCreated;
        this.dateFinished = dateFinished;
    }

    public Task(Long id, String title, String dateCreated, String dateFinished) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
        this.dateFinished = dateFinished;
    }


    public TaskOwner getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(TaskOwner taskOwner) {
        this.taskOwner = taskOwner;
    }

    public void finishTask() {
        this.dateFinished = getDateTime();
    }

    private String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateAndTime = sdf.format(new Date());
        return currentDateAndTime;
    }
}
