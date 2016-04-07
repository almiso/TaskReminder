package org.almiso.taskreminder.ui.cell;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.almiso.taskreminder.R;
import org.almiso.taskreminder.model.Task;
import org.almiso.taskreminder.utils.AndroidUtilities;
import org.almiso.taskreminder.utils.Constants;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class TaskCell extends FrameLayout {

    public interface TaskCellDelegate {
        void onDeleteClicked();

        void onTaskValueChanged(String value);

        void onTaskStateChanged();
    }

    private AppCompatCheckBox checkBoxState;
    private AppCompatTextView tvTaskValue;

    private TaskCellDelegate delegate = null;
    private Context context;
    private String taskValue = "";

    public TaskCell(Context context) {
        super(context);
        initCell(context);
    }

    public TaskCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCell(context);
    }

    public TaskCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCell(context);
    }

    public TaskCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCell(context);
    }

    private void initCell(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cell_task, this, false);

        checkBoxState = (AppCompatCheckBox) view.findViewById(R.id.checkBoxState);
        checkBoxState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    delegate.onTaskStateChanged();
                }
            }
        });

        tvTaskValue = (AppCompatTextView) view.findViewById(R.id.tvTaskValue);
        tvTaskValue.setTypeface(AndroidUtilities.getTypeface(Constants.FONT_ROBOTO_REGULAR));


        LinearLayout rootLayout = (LinearLayout) view.findViewById(R.id.rootLayout);
        rootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditTask();
            }
        });

        addView(view);
    }

    /* public methods */

    public void setDelegate(TaskCellDelegate delegate) {
        this.delegate = delegate;
    }

    public void setTaskValue(String value) {
        this.taskValue = value;
        tvTaskValue.setText(value);
    }

    public void setTaskState(@Task.TaskState int state) {
        switch (state) {
            case Task.TASK_STATE_DONE:
                checkBoxState.setChecked(true);
                tvTaskValue.setPaintFlags(tvTaskValue.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvTaskValue.setTextColor(getResources().getColor(R.color.st_text_secondary));
                break;
            case Task.TASK_STATE_NOT_DONE:
                checkBoxState.setChecked(false);
                tvTaskValue.setPaintFlags(tvTaskValue.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                tvTaskValue.setTextColor(getResources().getColor(R.color.st_text_primary));
                break;
        }
    }

    /* private methods */

    private void openDialogEditTask() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_edit_text, this, false);

        final AppCompatEditText editTaskValue = (AppCompatEditText) view.findViewById(R.id.editTaskValue);
        editTaskValue.setTypeface(AndroidUtilities.getTypeface(Constants.FONT_ROBOTO_REGULAR));
        editTaskValue.setText(taskValue);
        editTaskValue.setSelection(editTaskValue.getText().length());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle(R.string.st_app_name);
        builder.setPositiveButton(R.string.st_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                taskValue = editTaskValue.getText().toString();
                if (delegate != null) {
                    delegate.onTaskValueChanged(taskValue);
                }
            }
        });
        builder.setNeutralButton(R.string.st_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (delegate != null) {
                    delegate.onDeleteClicked();
                }
            }
        });
        builder.setNegativeButton(R.string.st_cancel, null);
        AppCompatDialog dialog = builder.create();
        dialog.show();
    }

}
