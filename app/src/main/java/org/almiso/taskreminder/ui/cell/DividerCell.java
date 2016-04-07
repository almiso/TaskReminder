package org.almiso.taskreminder.ui.cell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.almiso.taskreminder.R;
import org.almiso.taskreminder.utils.AndroidUtilities;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class DividerCell extends View {

    private static Paint paint;
    private int verticalPadding = 8;
    private int horizontalPadding = 0;


    public DividerCell(Context context) {
        super(context);
        init(context);
    }

    public DividerCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DividerCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        verticalPadding = 8;
        if (paint == null) {
            paint = new Paint();
            paint.setColor(getResources().getColor(R.color.st_divider_light));
            paint.setStrokeWidth(2);
        }
    }

    public void setVerticalPadding(int padding) {
        verticalPadding = padding;
        requestLayout();
    }

    public void setHorizontalPadding(int padding) {
        horizontalPadding = padding;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), AndroidUtilities.dp(verticalPadding * 2) + 1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(AndroidUtilities.dp(horizontalPadding), AndroidUtilities.dp(verticalPadding), getWidth() - AndroidUtilities.dp(horizontalPadding), AndroidUtilities.dp(verticalPadding), paint);
    }
}
