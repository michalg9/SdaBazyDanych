package app.todo.todoappuiandsql;


import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    private int id;
    private String title;
    private String date;

    private static int idToAutogenerate = 1;


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public Task(int index, String title, String date) {
        this.id = index;
        this.title = title;
        this.date = date;
    }

    public Task(int id, String title) {
        this.id = id;
        this.title = title;
        this.date = getCurrentDate();
    }

    public Task(String title) {
        this.id = idToAutogenerate++;
        this.title = title;
        this.date = getCurrentDate();
    }

    public Task() {
        this.id = idToAutogenerate++;
        this.title = "Notatka " + String.valueOf(this.id);
        date = getCurrentDate();
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }
    @Override
    public String toString() {
        return String.valueOf(id) + " - " + title;
    }


}
