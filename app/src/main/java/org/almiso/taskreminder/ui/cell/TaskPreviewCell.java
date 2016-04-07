package org.almiso.taskreminder.ui.cell;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.almiso.taskreminder.R;
import org.almiso.taskreminder.model.Task;
import org.almiso.taskreminder.utils.AndroidUtilities;
import org.almiso.taskreminder.utils.Constants;

/**
 * Created by Alexandr Sosorev on 06.04.2016.
 */
public class TaskPreviewCell extends FrameLayout {

    private AppCompatCheckBox checkTask;
    private AppCompatTextView tvTask;

    public TaskPreviewCell(Context context) {
        super(context);
        initCell(context);
    }

    public TaskPreviewCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCell(context);
    }

    public TaskPreviewCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCell(context);
    }

    public TaskPreviewCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCell(context);
    }

    private void initCell(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cell_task_priview, this, false);

        checkTask = (AppCompatCheckBox) view.findViewById(R.id.checkTask);

        tvTask = (AppCompatTextView) view.findViewById(R.id.tvTask);
        tvTask.setTypeface(AndroidUtilities.getTypeface(Constants.FONT_ROBOTO_REGULAR));
        addView(view);
    }

    public void init(Task task) {
        checkTask.setChecked(task.taskState == Task.TASK_STATE_DONE);

        if(task.taskState == Task.TASK_STATE_DONE){
            tvTask.setPaintFlags(tvTask.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvTask.setTextColor(getResources().getColor(R.color.st_text_secondary));
        } else {
            tvTask.setPaintFlags(tvTask.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            tvTask.setTextColor(getResources().getColor(R.color.st_text_primary));
        }


        tvTask.setText(task.value);
    }

}
