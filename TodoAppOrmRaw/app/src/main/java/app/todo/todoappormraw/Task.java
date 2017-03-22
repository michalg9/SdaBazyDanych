package app.todo.todoappormraw;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateFinished=" + dateFinished +
                '}';
    }

    public Task(){
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


}
