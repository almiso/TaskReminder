package org.almiso.taskreminder.ui.cell;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.almiso.taskreminder.R;
import org.almiso.taskreminder.core.TaskReminderApplication;
import org.almiso.taskreminder.utils.AndroidUtilities;
import org.almiso.taskreminder.utils.Constants;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class TextCell extends FrameLayout {

    private TextView textView;

    public TextCell(Context context) {
        super(context);
        init(context);
    }

    public TextCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        textView = (TextView) inflater.inflate(R.layout.cell_text, this, false);
        textView.setTypeface(AndroidUtilities.getTypeface(Constants.FONT_ROBOTO_REGULAR));
        textView.setGravity(Gravity.START);
        addView(textView);
    }


    public void setText(String text) {
        textView.setText(text);
    }

    public void setText(int resId) {
        textView.setText(TaskReminderApplication.applicationContext.getString(resId));
    }

    public void setGravity(int gravity) {
        textView.setGravity(gravity);
    }


    public void setTextSize(int dimenTextSize) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(dimenTextSize));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}