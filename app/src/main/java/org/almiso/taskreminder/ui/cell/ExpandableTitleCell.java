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
public class ExpandableTitleCell extends FrameLayout {

    public interface ExpandableTitleCellDelegate {
        void onExpandClicked(View view);
    }

    private boolean isOpened;
    private ExpandableTitleCellDelegate delegate = null;

    private AppCompatTextView tvTitle;
    private AppCompatImageView imageExpand;


    public ExpandableTitleCell(Context context) {
        super(context);
        initCell(context);
    }

    public ExpandableTitleCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCell(context);
    }

    public ExpandableTitleCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCell(context);
    }

    public ExpandableTitleCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCell(context);
    }


    private void initCell(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cell_expandable_title, this, false);

        RelativeLayout rootLayout = (RelativeLayout) view.findViewById(R.id.rootLayout);
        rootLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (delegate != null) {
                    delegate.onExpandClicked(v);
                }
            }
        });

        tvTitle = (AppCompatTextView) view.findViewById(R.id.tvTitle);
        tvTitle.setTypeface(AndroidUtilities.getTypeface(Constants.FONT_CONDENSED_REGULAR));

        imageExpand = (AppCompatImageView) view.findViewById(R.id.imageExpand);
        imageExpand.setColorFilter(getResources().getColor(R.color.st_text_primary));
        addView(view);
        setIsOpened(false);
    }

    public void setDelegate(ExpandableTitleCellDelegate delegate) {
        this.delegate = delegate;
    }

    public void setTitle(String value) {
        tvTitle.setText(value);
    }

    public void setTitle(int resId) {
        tvTitle.setText(resId);
    }


    public void setIsOpened(boolean value) {
        this.isOpened = value;
        updateExpandImg();
    }

    private void updateExpandImg() {
        if (isOpened) {
            imageExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_less_grey600_24dp));
        } else {
            imageExpand.setImageDrawable(getResources().getDrawable(R.drawable.ic_expand_more_grey600_24dp));
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
