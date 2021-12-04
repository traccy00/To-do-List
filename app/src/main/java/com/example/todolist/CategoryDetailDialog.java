package com.example.todolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.room.Room;

import com.example.todolist.DAO.CategoryDAO;
import com.example.todolist.common.AppDatabase;
import com.example.todolist.common.Constant;
import com.example.todolist.entity.Category;

public class CategoryDetailDialog extends AppCompatDialogFragment implements RecyclerViewClickListener{

    private int categoryId;

    public CategoryDetailDialog(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = this.getContext();
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "todoDB")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
        CategoryDAO categoryDAO = db.categoryDAO();
        Category category = categoryDAO.findById(categoryId);
        //category detail dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout layout = new LinearLayout(this.getContext());
        final EditText editText = new EditText(this.getContext());
        layout.addView(editText);
        if(category != null) {
            editText.setText(category.getName());
        }
//        View view = inflater.inflate(R.layout.dialog_fragment, null);
        builder.setView(layout)
                .setTitle(Constant.CATEGORY_DETAIL_TITLE)
                .setNegativeButton(Constant.NEGATIVE_BUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                })
                .setPositiveButton(Constant.POSITIVE_BUTTON, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //update category
                        String editedName = editText.getText().toString();
                        if(!(editedName).equalsIgnoreCase(category.getName())) {
                            categoryDAO.updateCategory(editedName, categoryId);
                            Toast.makeText(context, Constant.UPDATE_SUCCESS, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void onCategoryClick(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void onTaskClick(int taskId) {

    }
}
