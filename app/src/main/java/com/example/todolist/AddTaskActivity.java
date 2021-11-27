package com.example.todolist;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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

import com.example.todolist.DAO.TaskDAO;
import com.example.todolist.common.AppDatabase;
import com.example.todolist.common.Constant;
import com.example.todolist.common.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

//implements AdapterView.OnItemSelectedListener
public class AddTaskActivity extends AppCompatActivity {
    private static int SPEECH_REQUEST_CODE = 0;
    private static int SPEECH_REQUEST_CODE_1 = 1;

    EditText edtDate, edtStartTime, edtEndTime, edtDescription, edtTitle, edtRing;
    TextView tvStartTime, tvEndTime;
    //    Spinner spinner1, spinner2;
    int selectedHour, selectedMinutes, selectedDayOfMonth, selectedMonth, selectedYear;
    String lastSetTime;
    CheckBox allDay;
    Button btnAddTime, btnSpeechDescription;
    Utils utils;
    Switch switchRing;

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

        utils = new Utils();

        btnAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    edtStartTime.setEnabled(false);
                    edtEndTime.setEnabled(false);
                } else {
                    edtStartTime.setEnabled(true);
                    edtEndTime.setEnabled(true);
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
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        calendar.set(Calendar.MONTH, month);
        String month_name = month_date.format(calendar.getTime());
        int year = calendar.get(Calendar.YEAR);
        edtDate.setText(month_name + ", " + date + " " + year);

        String currentTime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
        edtRing.setText(currentTime);
        edtStartTime.setText(currentTime);
        int defaultEndTime = calendar.get(Calendar.HOUR_OF_DAY) + 1;
        edtEndTime.setText(defaultEndTime + ":" + calendar.get(Calendar.MINUTE));
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
                if (m == 0) {
                    message = h + ":00";
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


    public void getDate(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String message = month + ", " + dayOfMonth + " " + year;
                Log.i("loggg", "month:" + month);
                edtDate.setText(message);
                selectedDayOfMonth = dayOfMonth;
                selectedMonth = month;
                selectedYear = year;
            }
        }, selectedYear, selectedMonth, selectedDayOfMonth);
        dialog.show();
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
        String startTime = edtStartTime.getText().toString();
        String endTime = edtEndTime.getText().toString();
        if (allDay.isChecked()) {
            startTime = Constant.START_TIME_DEFAULT;
            endTime = Constant.END_TIME_DEFAULT;
        }

        String description = edtDescription.getText().toString();
        String ring = edtRing.getText().toString();
        taskDAO.createTask(title, date, startTime, endTime, description, ring);
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
        if(editText == edtDescription) {
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