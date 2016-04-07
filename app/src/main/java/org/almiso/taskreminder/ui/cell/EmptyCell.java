package org.almiso.taskreminder.ui.cell;

import android.content.Context;
import android.widget.FrameLayout;

import org.almiso.taskreminder.utils.AndroidUtilities;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class EmptyCell extends FrameLayout {

    private int cellHeight;

    public EmptyCell(Context context) {
        this(context, 8);
    }

    public EmptyCell(Context context, int height) {
        super(context);
        cellHeight = height;
    }

    public void setHeight(int height) {
        cellHeight = height;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(cellHeight), MeasureSpec.EXACTLY));
    }
}
