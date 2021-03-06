package com.example.todolist.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.R;
import com.example.todolist.service.RecyclerViewClickListener;
import com.example.todolist.entity.Category;

import java.text.ParseException;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private int selectedPos = RecyclerView.NO_POSITION;
    int saveLastSelectedPos = -1;
    private List<Category> categoryList;
    private RecyclerViewClickListener recyclerViewClickListener;
    private String className;
    public View.OnClickListener itemClickListener;
    public RecyclerView rvTaskList;

    public CategoryListAdapter() {}

    public CategoryListAdapter(List<Category> categoryList, RecyclerViewClickListener recyclerViewClickListener, String className) {
        this.categoryList = categoryList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.className = className;
    }

    public CategoryListAdapter(List<Category> categoryList,RecyclerViewClickListener recyclerViewClickListener, View.OnClickListener itemClickListener, String className) {
        this.categoryList = categoryList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.itemClickListener = itemClickListener;
        this.className = className;
    }

    @NonNull
    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category_name);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.MyViewHolder holder, int position) {
        if (saveLastSelectedPos == selectedPos) {// khi c??ng m???t category click 2 l???n ngh??a l?? deselect
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        } else {//n???u kh??ng s??? xem x??t position n??o ???????c ch???n ????? hightlight background cho n??
            holder.itemView.setBackgroundColor(selectedPos == position ? Color.GRAY : Color.TRANSPARENT);
        }
        Category category = categoryList.get(position);
        holder.tvName.setText(category.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (className.equals("AddTaskActivity")) {
                    saveLastSelectedPos = selectedPos;//l??c n??y selectedPost v???n gi??? gi?? tr??? c??, ta l???y gi?? tr??? n??y
                    selectedPos = holder.getLayoutPosition();//gi?? tr??? ???????c click m???i
                    if (saveLastSelectedPos == selectedPos) {//category deselect ngh??a l?? kh??ng ???????c ch???n
                        selectedPos = -1;//ko category nao dc chon
                        try {
                            recyclerViewClickListener.onCategoryClick(0);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            recyclerViewClickListener.onCategoryClick(category.getId());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    notifyDataSetChanged();
                } else if (className.equals("CategoryListFragment")) {
                    try {
                        recyclerViewClickListener.onCategoryClick(category.getId());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    holder.itemView.setOnClickListener(itemClickListener);
                } else if(className.equals("TaskListFragment")) {
                    try {
                        recyclerViewClickListener.onCategoryClick(category.getId());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
