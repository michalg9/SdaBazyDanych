package app.todo.todoappuiandsql;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tudelft on 3/22/2017.
 */

public class TaskAdapter extends  RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private static final String TAG = "TaskAdapter";

    private ArrayList<Task> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView taskId;
        public TextView taskTitle;
        public TextView taskDate;

        public ViewHolder(View view) {
            super(view);
            taskId = (TextView) view.findViewById(R.id.task_id);
            taskTitle = (TextView) view.findViewById(R.id.task_title);
            taskDate = (TextView) view.findViewById(R.id.task_date);
        }
    }

    public void add(Task item) {
        mDataset.add(item);
        notifyItemInserted(mDataset.size()-1);
    }

    public void remove(Task item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TaskAdapter(ArrayList<Task> myDataset) {
        mDataset = myDataset;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String title = mDataset.get(position).getTitle();
        holder.taskTitle.setText(mDataset.get(position).getTitle());

        /*holder.taskTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(title);
            }
        });*/

        //holder.taskId.setText("Footer: " + mDataset.get(position));
        holder.taskId.setText(String.valueOf(mDataset.get(position).getId()));

        holder.taskDate.setText(mDataset.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
