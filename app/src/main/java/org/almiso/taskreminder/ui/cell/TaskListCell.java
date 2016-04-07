package org.almiso.taskreminder.ui.cell;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.almiso.taskreminder.R;
import org.almiso.taskreminder.model.Task;
import org.almiso.taskreminder.model.TasksList;
import org.almiso.taskreminder.utils.AndroidUtilities;
import org.almiso.taskreminder.utils.Constants;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class TaskListCell extends CardView {

    public interface TaskListCellDelegate {
        void onRootClicked(View view);
    }

    private Context context;

    private TaskListCellDelegate delegate = null;
    private AppCompatTextView tvName;

    private LinearLayout layoutTasks;
    private FrameLayout layoutBackground;

    public TaskListCell(Context context) {
        super(context);
        initCell(context);
    }

    public TaskListCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCell(context);
    }

    public TaskListCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCell(context);
    }

    private void initCell(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cell_task_list, this, false);

        LinearLayout rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
        rootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    delegate.onRootClicked(v);
                }
            }
        });

        tvName = (AppCompatTextView) view.findViewById(R.id.tvName);
        tvName.setTypeface(AndroidUtilities.getTypeface(Constants.FONT_CONDENSED_BOLD));

        layoutTasks = (LinearLayout) view.findViewById(R.id.layoutTasks);
        layoutBackground = (FrameLayout) view.findViewById(R.id.layoutBackground);
        addView(view);
    }

    public void setDelegate(TaskListCellDelegate delegate) {
        this.delegate = delegate;
    }

    public void init(TasksList list, boolean isActive) {
        if (list == null) {
            return;
        }
        AndroidUtilities.prepareView(tvName, !TextUtils.isEmpty(list.name));
        tvName.setText(list.name);
        layoutTasks.removeAllViews();

        if (isActive) {
            layoutBackground.setBackgroundColor(getResources().getColor(R.color.st_tasks_list_active));
        } else {
            layoutBackground.setBackgroundColor(getResources().getColor(R.color.st_white));
        }

        boolean hasDoneTasks = false;
        for (Task task : list.tasks) {
            if (task.taskState == Task.TASK_STATE_NOT_DONE) {
                TaskPreviewCell cell = new TaskPreviewCell(context);
                cell.init(task);
                layoutTasks.addView(cell);
            } else {
                hasDoneTasks = true;
            }
        }
        if (hasDoneTasks) {
            for (Task task : list.tasks) {
                if (task.taskState == Task.TASK_STATE_DONE) {
                    TaskPreviewCell cell = new TaskPreviewCell(context);
                    cell.init(task);
                    layoutTasks.addView(cell);
                }
            }
        }
    }
}
