package org.almiso.taskreminder.ui.cell;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import org.almiso.taskreminder.R;
import org.almiso.taskreminder.utils.AndroidUtilities;
import org.almiso.taskreminder.utils.Constants;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class ListNameCell extends FrameLayout {

    public interface ListNameCellDelegate {
        void onNameUpdated(String value);
    }

    private AppCompatTextView tvName;
    private ListNameCellDelegate delegate = null;
    private Context context;
    private String name = "";

    public ListNameCell(Context context) {
        super(context);
        initCell(context);
    }

    public ListNameCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCell(context);
    }

    public ListNameCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCell(context);
    }

    public ListNameCell(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initCell(context);
    }


    private void initCell(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cell_list_name, this, false);

        tvName = (AppCompatTextView) view.findViewById(R.id.tvName);
        tvName.setTypeface(AndroidUtilities.getTypeface(Constants.FONT_CONDENSED_BOLD));

        view.findViewById(R.id.rootLayout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogEditTaskListName();
            }
        });
        addView(view);
    }

    /* public methods */

    public void setDelegate(ListNameCellDelegate delegate) {
        this.delegate = delegate;
    }

    public void setName(String value) {
        name = value;
        tvName.setText(value);
    }

    private void openDialogEditTaskListName() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_edit_text, this, false);

        final AppCompatEditText editTaskValue = (AppCompatEditText) view.findViewById(R.id.editTaskValue);
        editTaskValue.setTypeface(AndroidUtilities.getTypeface(Constants.FONT_ROBOTO_REGULAR));
        editTaskValue.setText(name);
        editTaskValue.setSelection(editTaskValue.getText().length());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setTitle(R.string.st_app_name);
        builder.setPositiveButton(R.string.st_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = editTaskValue.getText().toString();
                tvName.setText(name);
                if (delegate != null) {
                    delegate.onNameUpdated(name);
                }
            }
        });
        builder.setNeutralButton(R.string.st_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = "";
                tvName.setText(name);
                if (delegate != null) {
                    delegate.onNameUpdated(name);
                }
            }
        });
        builder.setNegativeButton(R.string.st_cancel, null);
        AppCompatDialog dialog = builder.create();
        dialog.show();

//        AndroidUtilities.showKeyboard(editTaskValue);
    }
}
