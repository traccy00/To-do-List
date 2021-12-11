//package com.example.todolist;
//
//import android.app.Activity;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.TableRow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.room.Room;
//
//import com.example.todolist.DAO.CategoryDAO;
//import com.example.todolist.DAO.TaskDAO;
//import com.example.todolist.adapter.SpinnerAdapter;
//import com.example.todolist.adapter.TaskListAdapter;
//import com.example.todolist.common.AppDatabase;
//import com.example.todolist.common.Constant;
//import com.example.todolist.entity.Category;
//import com.example.todolist.entity.Task;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//
//import java.util.Calendar;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class DetailTask extends BottomSheetDialogFragment {
//
//    public static final String TAG = "DetailTask";
//
//    public static DetailTask detailTask() {
//        return new DetailTask();
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.layout_sheet_dialog, container, false);
//        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        String categoryName = "";
//        if (category == null) {
//            categoryName = "Default";
//        } else {
//            categoryName = category.getName();
//        }
//
//        String time = "";
//        if (task.getStartTime().equals(Constant.START_TIME_DEFAULT) && task.getEndTime().equals(Constant.END_TIME_DEFAULT)) {
//            time = "All day";
//        } else {
//            time = task.getStartTime() + "-" + task.getEndTime();
//        }
//
//        TextView tvTitle = getView().findViewById(R.id.tv_title_sheet);
//        TextView tvDatetime = getView().findViewById(R.id.tv_datetime_sheet);
//        TextView tvRing = getView().findViewById(R.id.tv_ring_sheet);
//        TextView tvDescription = getView().findViewById(R.id.tv_description_sheet);
//        TextView tvCategory = getView().findViewById(R.id.tv_category_sheet);
//        TableRow tableRow = getView().findViewById(R.id.tablerow_datetime);
//
//        tvTitle.setText(task.getTitle());
//        tvDatetime.setText(task.getDate() + "\n" + time);
//        tvRing.setText(task.getRing());
//        tvDescription.setText(task.getDescription());
//        tvCategory.setText(categoryName);
//
//        Button btnEdit = getView().findViewById(R.id.btn_edit);
//        Button btnDelete = getView().findViewById(R.id.btn_delete);
//        Button btnClose = getView().findViewById(R.id.btn_close);
//        Button btnSave = getView().findViewById(R.id.btn_save);
//        Button btnMarkAsCompleted = getView().findViewById(R.id.btn_mark_complete);
//        CheckBox allDay = getView().findViewById(R.id.checkBox);
//        Button btnAddTime = getView().findViewById(R.id.btn_add_time_sheet);
//
//        EditText edtTitle = getView().findViewById(R.id.edt_title);
//        EditText edtDate = getView().findViewById(R.id.edt_date_sheet);
//        EditText edtStartTime = getView().findViewById(R.id.edt_start_time_sheet);
//        EditText edtEndTime = getView().findViewById(R.id.edt_end_time_sheet);
//        EditText edtRing = getView().findViewById(R.id.edt_ring);
//        EditText edtDescription = getView().findViewById(R.id.edt_description);
//        Spinner spinner = getView().findViewById(R.id.spinner_category_sheet);
//        //spinner category list
//        List<Category> categoryList = categoryDAO.getAll();
//        categoryList.add(new Category(0, "Default"));
//        adapter = new SpinnerAdapter(itemView.getContext(), android.R.layout.simple_spinner_item, categoryList.toArray(new Category[categoryList.size()]));
//        spinner.setAdapter(adapter);
//        String finalCategoryName = categoryName;
//        List<Category> categoryList1 = categoryList.stream().filter(c -> c.getName().equals(finalCategoryName)).collect(Collectors.toList());
//        int spinnerPosition = adapter.getPosition(categoryList1.get(0));
//        spinner.setSelection(spinnerPosition);
//
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Category selectCategory = adapter.getItem(position);
//                selectCategoryId = selectCategory.getId();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
//
//        String finalTime = time;
//        btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //view detail gone
//                tvTitle.setVisibility(View.GONE);
//                tvDatetime.setVisibility(View.GONE);
//                tvRing.setVisibility(View.GONE);
//                tvDescription.setVisibility(View.GONE);
//                tvCategory.setVisibility(View.GONE);
//                btnMarkAsCompleted.setVisibility(View.GONE);
//                btnEdit.setVisibility(View.GONE);
//                //edit screen for detail visible
//                tableRow.setVisibility(View.VISIBLE);
//                allDay.setVisibility(View.VISIBLE);
//                edtTitle.setVisibility(View.VISIBLE);
//                edtDate.setVisibility(View.VISIBLE);
//                if (finalTime.equals("All day")) {
//                    allDay.setChecked(true);
//                    edtStartTime.setVisibility(View.GONE);
//                    edtEndTime.setVisibility(View.GONE);
//                    btnAddTime.setVisibility(View.VISIBLE);
//                } else {
//                    edtStartTime.setText(task.getStartTime());
//                    edtEndTime.setText(task.getEndTime());
//                    edtStartTime.setVisibility(View.VISIBLE);
//                    edtEndTime.setVisibility(View.VISIBLE);
//                    btnAddTime.setVisibility(View.GONE);
//                }
//                edtRing.setVisibility(View.VISIBLE);
//                edtDescription.setVisibility(View.VISIBLE);
//                spinner.setVisibility(View.VISIBLE);
//
//                edtTitle.setText(task.getTitle());
//                edtDate.setText(task.getDate());
//
//                edtRing.setText(task.getRing());
//
//                allDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        if (allDay.isChecked()) {
//                            edtStartTime.setVisibility(View.GONE);
//                            edtEndTime.setVisibility(View.GONE);
//                            btnAddTime.setVisibility(View.VISIBLE);
//                            edtStartTime.setText(Constant.START_TIME_DEFAULT);
//                            edtEndTime.setText(Constant.END_TIME_DEFAULT);
//                        } else {
//                            edtStartTime.setVisibility(View.VISIBLE);
//                            edtEndTime.setVisibility(View.VISIBLE);
//                            btnAddTime.setVisibility(View.GONE);
//                            Calendar calendar = Calendar.getInstance();
//                            edtStartTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
//                            edtEndTime.setText((calendar.get(Calendar.HOUR_OF_DAY) + 1) + ":" + calendar.get(Calendar.MINUTE));
//                        }
//                    }
//                });
//
//                btnAddTime.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        allDay.setChecked(false);
//                        edtStartTime.setVisibility(View.VISIBLE);
//                        edtEndTime.setVisibility(View.VISIBLE);
//                        btnAddTime.setVisibility(View.GONE);
//                    }
//                });
//                edtDescription.setText(task.getDate());
//
//                btnSave.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        TaskDAO taskDAO = db.taskDAO();
//                        taskDAO.updateTaskDetail(taskId, edtTitle.getText().toString(),
//                                edtDate.getText().toString(), edtStartTime.getText().toString(),
//                                edtEndTime.getText().toString(), edtDescription.getText().toString(),
//                                edtRing.getText().toString(), selectCategoryId);
//                        Toast.makeText(itemView.getContext(), "Task updated", Toast.LENGTH_SHORT).show();
//                        bottomSheetDialog.dismiss();
//                    }
//                });
//
//            }
//        });
//        btnMarkAsCompleted.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TaskDAO taskDAO = db.taskDAO();
//                taskDAO.updateTaskById(1, taskId);
//                Toast.makeText(itemView.getContext(), "Task updated", Toast.LENGTH_SHORT).show();
//                bottomSheetDialog.dismiss();
//            }
//        });
//
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TaskDAO taskDAO = db.taskDAO();
//                taskDAO.deleteById(taskId);
//                bottomSheetDialog.dismiss();
//            }
//        });
//
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.dismiss();
//            }
//        });
//
//        bottomSheetDialog.setContentView(getView());
//        bottomSheetDialog.show();
//    }
//
//    @Override
//    public void onDismiss(@NonNull DialogInterface dialog) {
//        super.onDismiss(dialog);
//        Activity activity = getActivity();
//        if (activity instanceof DialogCloseListener) {
//            ((DialogCloseListener) activity).handlerDialogClose(dialog);
//        }
//    }
//}
