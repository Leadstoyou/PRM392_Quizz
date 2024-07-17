package com.example.apptracnghiem.activity.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptracnghiem.R;
import com.example.apptracnghiem.adapter.TestAdapter;
import com.example.apptracnghiem.database.Database;
import com.example.apptracnghiem.model.Test;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class ViewTestActivity extends AppCompatActivity {
    private TestAdapter testAdapter;
    private List<Test> testList;
    private Database dbHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_tests);
            dbHelper = new Database(this);

            RecyclerView recyclerViewTests = findViewById(R.id.recyclerView_tests);
            recyclerViewTests.setLayoutManager(new LinearLayoutManager(this));

            testList = dbHelper.getScoreByUserEmail(getSharedPreferences("user_session", MODE_PRIVATE).getString("email", "Guest"));
            testAdapter = new TestAdapter(this, testList);

            recyclerViewTests.setAdapter(testAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

