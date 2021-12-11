package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

public class MainActivity extends AppCompatActivity {

    boolean doubleBackToExit = false;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private final String TAG = "MainActivityhihi";

//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExit) {
//            super.onBackPressed();
//            return;
//        }
//        this.doubleBackToExit = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                doubleBackToExit = false;
//            }
//        }, 2000);
//    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.taskListFragment:
                    fragmentTransaction.replace(R.id.fragmentContainerView, new TaskListFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.calendarFragment:
                    fragmentTransaction.replace(R.id.fragmentContainerView, new CalendarFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.meFragment:
                    fragmentTransaction.replace(R.id.fragmentContainerView, new MeFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.settingFragment:
                    fragmentTransaction.replace(R.id.fragmentContainerView, new SettingFragment());
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //chuyển fragment
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.taskListFragment);
    }

    public void onCreateTaskActivity(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }
}