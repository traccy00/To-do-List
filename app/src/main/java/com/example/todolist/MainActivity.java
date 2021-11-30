package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //chuyển fragment
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (menuItem.getItemId()) {
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
        });
    }

    public void onCreateTaskActivity(View view) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }
}