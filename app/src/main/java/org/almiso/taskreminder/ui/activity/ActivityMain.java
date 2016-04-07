package org.almiso.taskreminder.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import org.almiso.taskreminder.R;
import org.almiso.taskreminder.core.TasksController;
import org.almiso.taskreminder.model.Task;
import org.almiso.taskreminder.model.TasksList;
import org.almiso.taskreminder.ui.cell.EmptyCell;
import org.almiso.taskreminder.ui.cell.ShadowDownSectionCell;
import org.almiso.taskreminder.ui.cell.TaskListCell;
import org.almiso.taskreminder.ui.cell.TextCell;
import org.almiso.taskreminder.ui.cell.base.Holder;
import org.almiso.taskreminder.utils.AndroidUtilities;
import org.almiso.taskreminder.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;

public class ActivityMain extends AppCompatActivity {

    protected static final String TAG = "MainActivity";

    /* Controls */
    private UiAdapter adapter;

    /* Fields */
    private int rowCount = 0;
    private HashMap<Integer, TasksList> tasksLists = new HashMap<>();
    private int emptyRow;
    private int sectionEndRow;

    @Override
    public void onResume() {
        super.onResume();
        updateRowsIds();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Init fab */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setColorFilter(getResources().getColor(R.color.st_white));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long listId = TasksController.getInstance().addNewEmptyTasksList();
                    openActivityTasks((int) listId, view);
                }
            });
        }


        /* set up view */
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(false);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        assert recyclerView != null;
        recyclerView.setBackgroundColor(getResources().getColor(R.color.st_card_background));

        recyclerView.setHasFixedSize(false);

        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new UiAdapter(this);
        recyclerView.setAdapter(adapter);

        updateRowsIds();
        onIntent(getIntent());
    }

    private void updateRowsIds() {
        rowCount = 0;
        emptyRow = -1;
        sectionEndRow = -1;
        tasksLists.clear();

        ArrayList<TasksList> lists = TasksController.getInstance().getTasksLists();
        if (lists.isEmpty()) {
            emptyRow = rowCount++;
            sectionEndRow = rowCount++;
            return;
        }

        for (TasksList list : lists) {
            tasksLists.put(rowCount++, list);
        }
    }

    private void openActivityTasks(int listId, View view) {

        Intent intent = new Intent(ActivityMain.this, ActivityTask.class);
        intent.putExtra(ActivityTask.EXTRA_LIST_ID, listId);

        if (view == null) {
            startActivity(intent);
            return;
        }

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                ActivityMain.this, view, ActivityTask.EXTRA_ROOT_LAYOUT);
        ActivityCompat.startActivity(ActivityMain.this, intent, options.toBundle());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onIntent(intent);
    }

    private void onIntent(Intent intent) {

        if (Constants.ACTION_OPEN_TASKS_LIST.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                final int taskListId = extras.getInt(Constants.EXTRA_TASK_LIST_ID);
                openActivityTasks(taskListId, null);
            }
        }
        if (Constants.ACTION_OPEN_TASKS_LIST_AND_DONE_TASK.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                final int taskListId = extras.getInt(Constants.EXTRA_TASK_LIST_ID);
                final int taskId = extras.getInt(Constants.EXTRA_TASK_ID);

                boolean isTaskListActive = TasksController.getInstance().getActiveListId() == taskListId;
                TasksController.getInstance().onTaskStateChanged(taskListId, taskId, Task.TASK_STATE_DONE, isTaskListActive);

                AndroidUtilities.RunOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        updateRowsIds();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }

    }

    private class UiAdapter extends RecyclerView.Adapter {

        private Context context;

        public UiAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = null;
            if (viewType == 0) {
                view = new EmptyCell(context);
            } else if (viewType == 1) {
                view = new TextCell(context);
            } else if (viewType == 2) {
                view = new TaskListCell(context);
            } else if (viewType == 3) {
                view = new ShadowDownSectionCell(context);
            }

            if (view != null) {
                RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);

                if (viewType == 1) {
                    view.setBackgroundColor(getResources().getColor(R.color.st_white));
                } else if (viewType == 2) {
                    RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(AndroidUtilities.dp(8), AndroidUtilities.dp(8), AndroidUtilities.dp(8), AndroidUtilities.dp(8));
                    view.setLayoutParams(layoutParams);
                }
            }
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder.getItemViewType() == 1) {
                TextCell textCell = (TextCell) holder.itemView;

                StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setFullSpan(true);
                holder.itemView.setLayoutParams(layoutParams);

                if (position == emptyRow) {
                    textCell.setText(R.string.st_no_tasks_list);
                    textCell.setGravity(Gravity.CENTER);
                }
            } else if (holder.getItemViewType() == 2) {
                TaskListCell taskListCell = (TaskListCell) holder.itemView;

                final TasksList list = tasksLists.get(position);
                if (list == null) {
                    return;
                }

                int activeTasksListId = TasksController.getInstance().getActiveListId();
                taskListCell.init(list, list.id == activeTasksListId);
                taskListCell.setDelegate(new TaskListCell.TaskListCellDelegate() {
                    @Override
                    public void onRootClicked(View view) {
                        openActivityTasks(list.id, view);
                    }
                });
            } else if (holder.getItemViewType() == 3) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setFullSpan(true);
                holder.itemView.setLayoutParams(layoutParams);
            }
        }


        @Override
        public int getItemViewType(int position) {
            if (position == emptyRow) {
                return 1;
            } else if (tasksLists.containsKey(position)) {
                return 2;
            } else if (position == sectionEndRow) {
                return 3;
            }


            return 0;
        }
    }
}
