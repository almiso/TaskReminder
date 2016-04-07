package org.almiso.taskreminder.ui.cell;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import org.almiso.taskreminder.R;
import org.almiso.taskreminder.utils.AndroidUtilities;
import org.almiso.taskreminder.utils.Constants;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class NewTaskCell extends FrameLayout {

    public interface NewTaskCellDelegate {
        void onNewTaskClicked(View view);
    }

    private NewTaskCellDelegate delegate = null;

    public NewTaskCell(Context context) {
        super(context);
        initCell(context);
    }

    public NewTaskCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCell(context);
    }

    public NewTaskCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCell(context);
    }

    public NewTaskCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCell(context);
    }

    private void initCell(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cell_new_task, this, false);

        AppCompatImageView imageMore = (AppCompatImageView) view.findViewById(R.id.imageDeleteTask);
        imageMore.setColorFilter(getResources().getColor(R.color.st_text_primary));

        AppCompatTextView tvInfo = (AppCompatTextView) view.findViewById(R.id.tvInfo);
        tvInfo.setTypeface(AndroidUtilities.getTypeface(Constants.FONT_CONDENSED_REGULAR));

        RelativeLayout rootLayout = (RelativeLayout) view.findViewById(R.id.rootLayout);
        rootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delegate != null) {
                    delegate.onNewTaskClicked(v);
                }
            }
        });
        addView(view);
    }


    public void setDelegate(NewTaskCellDelegate delegate) {
        this.delegate = delegate;
    }
}
