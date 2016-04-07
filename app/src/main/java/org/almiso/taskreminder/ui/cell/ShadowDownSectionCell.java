package org.almiso.taskreminder.ui.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.almiso.taskreminder.R;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class ShadowDownSectionCell extends FrameLayout {

    public ShadowDownSectionCell(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cell_shadow_down_section, this, false);
        addView(view);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
