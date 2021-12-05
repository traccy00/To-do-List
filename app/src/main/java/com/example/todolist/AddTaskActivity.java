package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.todolist.DAO.CategoryDAO;
import com.example.todolist.DAO.TaskDAO;
import com.example.todolist.adapter.CategoryListAdapter;
import com.example.todolist.common.AppDatabase;
import com.example.todolist.common.Constant;
import com.example.todolist.common.Utils;
import com.example.todolist.entity.Category;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

//implements AdapterView.OnItemSelectedListener
public class AddTaskActivity extends AppCompatActivity implements RecyclerViewClickListener {
    private static final int SPEECH_REQUEST_CODE = 0;
    private static final int SPEECH_REQUEST_CODE_1 = 1;

    private EditText edtDate, edtStartTime, edtEndTime, edtDescription, edtTitle, edtRing;
    //    Spinner spinner1, spinner2;
    private int selectedHour, selectedMinutes;
    private String lastSetTime;
    private CheckBox allDay;
    private Button btnAddTime, btnSpeechDescription;
    private Utils utils;
    private Switch switchRing;
    private boolean clickedAddTime;
    private RecyclerView rvCategoryList;
    private List<Category> categoryList;
    private int categoryId = 0;
    Calendar calendar = Calendar.getInstance();
    int selectedYear = calendar.get(Calendar.YEAR);
    int selectedMonth = calendar.get(Calendar.MONTH);
    int selectedDayOfMonth = calendar.get(Calendar.DATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        edtTitle = findViewById(R.id.edt_title);
        edtDate = findViewById(R.id.edt_date);
        edtStartTime = findViewById(R.id.edt_start_time);
        edtEndTime = findViewById(R.id.edt_end_time);
        edtDescription = findViewById(R.id.edt_description);
        edtRing = findViewById(R.id.edt_ring);
        allDay = findViewById(R.id.checkBox);
        btnAddTime = findViewById(R.id.btn_add_time);
        switchRing = findViewById(R.id.switch1);
        edtRing.setEnabled(false);
        btnSpeechDescription = findViewById(R.id.btn_speech_description);
        rvCategoryList = findViewById(R.id.rv_categories_2);

        utils = new Utils();
        clickedAddTime = false;
        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedAddTime = true;
                allDay.setChecked(false);
                btnAddTime.setVisibility(View.GONE);
                ((TextView) findViewById(R.id.tv_end_time)).setVisibility(View.VISIBLE);
                edtStartTime.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.tv_start_time)).setVisibility(View.VISIBLE);
                edtEndTime.setVisibility(View.VISIBLE);
            }
        });

        allDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (allDay.isChecked()) {
                    edtStartTime.setVisibility(View.GONE);
                    edtEndTime.setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.tv_end_time)).setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.tv_start_time)).setVisibility(View.GONE);
                    btnAddTime.setVisibility(View.VISIBLE);
                } else {
                    edtStartTime.setVisibility(View.VISIBLE);
                    edtEndTime.setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_end_time)).setVisibility(View.VISIBLE);
                    ((TextView) findViewById(R.id.tv_start_time)).setVisibility(View.VISIBLE);
                    btnAddTime.setVisibility(View.GONE);
                }
            }
        });

        switchRing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchRing.isChecked()) {
                    edtRing.setEnabled(true);
                } else {
                    edtRing.setEnabled(false);
                }
            }
        });


//        spinner1 = findViewById(R.id.spinner1);
//        spinner2 = findViewById(R.id.spinner2);
//        //create an ArrayAdapter using the string array and a default spinner layout
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter
//                .createFromResource(this, R.array.twentyfour_hourclock, android.R.layout.simple_spinner_item);
//        //specify which layout use array
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        //apply adapter to spinner
//        spinner1.setAdapter(adapter);
//        spinner2.setAdapter(adapter);
//        spinner1.setSelection(0);
//        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                String valueSelected =
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        //scroll view for EditText edtDescription
        edtDescription.setScroller(new Scroller(getApplicationContext()));
        edtDescription.setMaxLines(15);
        edtDescription.setVerticalScrollBarEnabled(true);
        edtDescription.setMovementMethod(new ScrollingMovementMethod());

        //default date when go to create task screen
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        edtDate.setText(date + "/" + month + "/" + year);

        String currentTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        edtRing.setText(currentTime);
        edtStartTime.setText(currentTime);
        int defaultEndTime = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        edtEndTime.setText(defaultEndTime + ":" + calendar.get(Calendar.MINUTE));

        //recycler view category
        AppDatabase db = Room
                .databaseBuilder(this.getApplicationContext(), AppDatabase.class, "todoDB")
                .allowMainThreadQueries()
                .build();
        CategoryDAO categoryDAO = db.categoryDAO();
        categoryList = categoryDAO.getAll();
        CategoryListAdapter categoryListAdapter =
                new CategoryListAdapter(categoryList, this, "AddTaskActivity");
        rvCategoryList.setLayoutManager(new LinearLayoutManager
                (this.getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rvCategoryList.setAdapter(categoryListAdapter);
    }

    public void validateTime(EditText editText) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String startTime = edtStartTime.getText().toString();
                String endTime = edtEndTime.getText().toString();
                int hourStart = Integer.parseInt(utils.getHour(startTime));
                int hourEnd = Integer.parseInt(utils.getHour(endTime));
                if (hourStart > hourEnd) {
                    showAlertDialogValidateTime(startTime, endTime);
                }
            }
        });
    }

    //get time for start time & end time & ring bell
    public void getTime(EditText edt) {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int h, int m) {
                String message;
                if (m < 10) {
                    message = h + ":0" + m;
                } else {
                    message = h + ":" + m;
                }
                edt.setText(message);
                selectedHour = h;
                selectedMinutes = m;
            }
        }, selectedHour, selectedMinutes, true);
        dialog.show();
    }

    public void getStartTime(View view) {
        lastSetTime = edtStartTime.getText().toString();
        getTime(edtStartTime);
        validateTime(edtStartTime);
    }

    public void getEndTime(View view) {
        getTime(edtEndTime);
        validateTime(edtEndTime);
    }

    //get date of task
    public void getDate(View view) {
//        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                String message = dayOfMonth + "/" + month + "/" + year;
//                edtDate.setText(message);
//                selectedDayOfMonth = dayOfMonth;
//                selectedMonth = month;
//                selectedYear = year;
//            }
//        }, selectedYear, selectedMonth, selectedDayOfMonth);
//        dialog.show();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                edtDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                selectedDayOfMonth = dayOfMonth;
                selectedMonth = monthOfYear;
                selectedYear = year;
            }
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);
        datePickerDialog.show();
    }

    public void onInsertTask(View view) {
        AppDatabase db = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "todoDB")
                .allowMainThreadQueries()
                .build();
        TaskDAO taskDAO = db.taskDAO();
        String title = edtTitle.getText().toString();
        if (title.matches("")) {
            Toast.makeText(this, "You did not enter title ", Toast.LENGTH_SHORT).show();
            return;
        }
        String date = edtDate.getText().toString();
        String startTime = "";
        String endTime = "";
        if (allDay.isChecked() || !clickedAddTime) {
            startTime = Constant.START_TIME_DEFAULT;
            endTime = Constant.END_TIME_DEFAULT;
        } else if (clickedAddTime == true && !allDay.isChecked()) {
            startTime = edtStartTime.getText().toString();
            endTime = edtEndTime.getText().toString();
        }
        String description = edtDescription.getText().toString();
        String ring = "";
        //check if setting ring bell
        if (switchRing.isChecked()) {
            ring = edtRing.getText().toString();
        }
        //get category
        taskDAO.createTask(title, date, startTime, endTime, description, ring, 0, categoryId);

        Toast.makeText(getApplicationContext(), "Add task successfully", Toast.LENGTH_SHORT).show();
//        Log.d("myapp", Log.getStackTraceString(new Exception()));
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void showAlertDialogValidateTime(String startTime, String endTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("You want your task from " + endTime + "-" + startTime + "?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edtStartTime.setText(endTime);
                        edtEndTime.setText(startTime);
                        Toast.makeText(AddTaskActivity.this, "You've clicked Ok", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        edtStartTime.setText(lastSetTime);
                        Toast.makeText(AddTaskActivity.this, "You've clicked No", Toast.LENGTH_LONG).show();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void getRingTime(View view) {
        getTime(edtRing);
    }

    //create an Intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer(EditText editText) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        if (editText == edtDescription) {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } else if (editText == edtTitle) {
            startActivityForResult(intent, SPEECH_REQUEST_CODE_1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            //write spoken text to EditText
            edtDescription.setText(spokenText);
        } else if (requestCode == SPEECH_REQUEST_CODE_1 && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            //write spoken text to EditText
            edtTitle.setText(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void onSpeech(View view) {
        displaySpeechRecognizer(edtDescription);
    }

    public void onSpeech1(View view) {
        displaySpeechRecognizer(edtTitle);
    }

    //get clicked category id for insert task
    @Override
    public void onCategoryClick(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void onTaskClick(int taskId) {

    }


    //    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////        spinner1.setOnItemSelectedListener(this);
//         parent.getItemAtPosition(position);
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

}