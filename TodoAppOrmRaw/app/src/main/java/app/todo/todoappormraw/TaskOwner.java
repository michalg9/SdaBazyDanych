package app.todo.todoappormraw;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "taskOwner")
public class TaskOwner {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(columnName = "ownerName")
    private String ownerName;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Task> tasks;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( "TaskOwner{" +
                        "id=" + id +
                        ", ownerName='" + ownerName);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    TaskOwner() {
    }

    TaskOwner(String ownerName) {
        this.ownerName = ownerName;
    }

    public ForeignCollection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ForeignCollection<Task> tasks) {
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }
}
