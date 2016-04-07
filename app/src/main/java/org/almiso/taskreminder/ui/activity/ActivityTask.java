package org.almiso.taskreminder.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.almiso.taskreminder.R;
import org.almiso.taskreminder.core.TasksController;
import org.almiso.taskreminder.model.Task;
import org.almiso.taskreminder.ui.cell.DividerCell;
import org.almiso.taskreminder.ui.cell.EmptyCell;
import org.almiso.taskreminder.ui.cell.ExpandableTitleCell;
import org.almiso.taskreminder.ui.cell.ListNameCell;
import org.almiso.taskreminder.ui.cell.NewTaskCell;
import org.almiso.taskreminder.ui.cell.TaskCell;
import org.almiso.taskreminder.ui.cell.base.Holder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alexandr Sosorev on 04.04.2016.
 */
public class ActivityTask extends AppCompatActivity {

    private static final String TAG = "ActivityTask";
    public static final String EXTRA_ROOT_LAYOUT = "extra_root_layout";
    public static final String EXTRA_LIST_ID = "extra_list_id";

    /* Controls */
    private UiAdapter adapter;
    private RecyclerView recyclerView;

    /* Fields */
    private int rowCount = 0;
    private int listNameRow;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private int newTaskRow;
    private int dividerRow;
    private int doneTitleRow;

    /* Data */
    private int taskListId = -1;
    private boolean isTaskListActive = false;
    private boolean isDoneOpened = true;

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            taskListId = extras.getInt(EXTRA_LIST_ID);
        }


        setContentView(R.layout.activity_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /* set up view */
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);

        assert recyclerView != null;
        recyclerView.setHasFixedSize(false);
        ViewCompat.setTransitionName(recyclerView, EXTRA_ROOT_LAYOUT);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new UiAdapter(this);
        recyclerView.setAdapter(adapter);

        isTaskListActive = TasksController.getInstance().getActiveListId() == taskListId;
        updateBackground(isTaskListActive);

        updateRowsIds();
    }


    private void updateRowsIds() {
        rowCount = 0;
        listNameRow = -1;
        tasks.clear();
        newTaskRow = -1;
        dividerRow = -1;
        doneTitleRow = -1;

        listNameRow = rowCount++;

        ArrayList<Task> mTasks = TasksController.getInstance().getTasks(taskListId);
        if (!mTasks.isEmpty()) {

            boolean hasDoneTasks = false;

            for (Task task : mTasks) {
                if (task.taskState == Task.TASK_STATE_NOT_DONE) {
                    tasks.put(rowCount++, task);
                } else {
                    hasDoneTasks = true;
                }
            }

            newTaskRow = rowCount++;

            if (hasDoneTasks) {
                dividerRow = rowCount++;
                doneTitleRow = rowCount++;

                if (isDoneOpened) {
                    for (Task task : mTasks) {
                        if (task.taskState == Task.TASK_STATE_DONE) {
                            tasks.put(rowCount++, task);
                        }
                    }
                }

            }
        } else {
            newTaskRow = rowCount++;
        }
    }

    private void openDialogDeleteList() {
        AppCompatDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.st_app_name)
                .setMessage(R.string.st_ask_delete)
                .setPositiveButton(R.string.st_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TasksController.getInstance().deleteTasksList(taskListId, isTaskListActive);
                        onBackPressed();
                    }
                }).setNegativeButton(R.string.st_no, null).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void onTurnTaskListClicked() {
        isTaskListActive = !isTaskListActive;
        updateBackground(isTaskListActive);
        TasksController.getInstance().setActiveListId(isTaskListActive ? taskListId : -1);
        supportInvalidateOptionsMenu();
    }

    private void updateBackground(boolean isActiveList) {
        if (isActiveList) {
            recyclerView.setBackgroundColor(getResources().getColor(R.color.st_tasks_list_active));
        } else {
            recyclerView.setBackgroundColor(getResources().getColor(R.color.st_white));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task, menu);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.st_task_list);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemMap = menu.findItem(R.id.action_turn);
        if (isTaskListActive) {
            itemMap.setTitle(R.string.st_turn_off);
        } else {
            itemMap.setTitle(R.string.st_turn_on);
        }


        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            openDialogDeleteList();
            return true;
        } else if (id == R.id.action_turn) {
            onTurnTaskListClicked();
            return true;
        }


        return super.onOptionsItemSelected(item);
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
                view = new ListNameCell(context);
            } else if (viewType == 2) {
                view = new DividerCell(context);
            } else if (viewType == 3) {
                view = new ExpandableTitleCell(context);
            } else if (viewType == 4) {
                view = new NewTaskCell(context);
            } else if (viewType == 5) {
                view = new TaskCell(context);
            }


            if (view != null) {
                RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
            }
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder.getItemViewType() == 1) {
                ListNameCell listNameCell = (ListNameCell) holder.itemView;
                listNameCell.setName(TasksController.getInstance().getTasksListName(taskListId));
                listNameCell.setDelegate(new ListNameCell.ListNameCellDelegate() {
                    @Override
                    public void onNameUpdated(String value) {
                        TasksController.getInstance().updateTasksListName(taskListId, value, isTaskListActive);
                    }
                });
            } else if (holder.getItemViewType() == 2) {
                DividerCell dividerCell = (DividerCell) holder.itemView;
                dividerCell.setHorizontalPadding((int) getResources().getDimension(R.dimen.activity_horizontal_margin));
            } else if (holder.getItemViewType() == 3) {
                ExpandableTitleCell expandableTitleCell = (ExpandableTitleCell) holder.itemView;

                expandableTitleCell.setTitle(R.string.st_marked);
                expandableTitleCell.setIsOpened(isDoneOpened);
                expandableTitleCell.setDelegate(new ExpandableTitleCell.ExpandableTitleCellDelegate() {
                    @Override
                    public void onExpandClicked(View view) {
                        isDoneOpened = !isDoneOpened;
                        updateRowsIds();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

            } else if (holder.getItemViewType() == 4) {
                NewTaskCell newTaskCell = (NewTaskCell) holder.itemView;
                newTaskCell.setDelegate(new NewTaskCell.NewTaskCellDelegate() {
                    @Override
                    public void onNewTaskClicked(View view) {
                        TasksController.getInstance().addNewEmptyTask(taskListId);
                        updateRowsIds();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            } else if (holder.getItemViewType() == 5) {
                TaskCell taskCell = (TaskCell) holder.itemView;


                final Task task = tasks.get(position);
                if (task == null) {
                    return;
                }
                taskCell.setTaskValue(task.value);
                taskCell.setTaskState(task.taskState);
                taskCell.setDelegate(new TaskCell.TaskCellDelegate() {
                    @Override
                    public void onDeleteClicked() {
                        TasksController.getInstance().deleteTask(taskListId, task.id, isTaskListActive);
                        updateRowsIds();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onTaskValueChanged(String value) {
                        task.value = value;
                        TasksController.getInstance().onTaskValueChanged(taskListId, task.id, value, isTaskListActive);
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onTaskStateChanged() {
                        @Task.TaskState int prevState = task.taskState;
                        @Task.TaskState int state;
                        if (prevState == Task.TASK_STATE_NOT_DONE) {
                            state = Task.TASK_STATE_DONE;
                        } else {
                            state = Task.TASK_STATE_NOT_DONE;
                        }
                        TasksController.getInstance().onTaskStateChanged(taskListId, task.id, state, isTaskListActive);

                        updateRowsIds();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

            }

        }


        @Override
        public int getItemViewType(int position) {
            if (position == listNameRow) {
                return 1;
            } else if (position == dividerRow) {
                return 2;
            } else if (position == doneTitleRow) {
                return 3;
            } else if (position == newTaskRow) {
                return 4;
            } else if (tasks.containsKey(position)) {
                return 5;
            }

            return 0;
        }
    }
}
