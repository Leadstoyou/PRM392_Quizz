package com.example.apptracnghiem.activity.business;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptracnghiem.R;
import com.example.apptracnghiem.activity.MainActivity;
import com.example.apptracnghiem.adapter.AnsweredAdapter;
import com.example.apptracnghiem.model.Question;

import java.util.List;

public class AnsweredListActivity extends AppCompatActivity {
    private AnsweredAdapter answeredAdapter;
    private List<Question> listQuestion;
    private TextView title;
    private int score;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answered_test);
        Intent intent = getIntent();

        title = findViewById(R.id.view_answerd_test_title);
        RecyclerView recyclerViewAnswered = findViewById(R.id.answerd_test_recycle_view);
        recyclerViewAnswered.setLayoutManager(new LinearLayoutManager(this));

        listQuestion = (List<Question>) intent.getSerializableExtra("answeredList");
        score = intent.getIntExtra("score", -1);

        title.setText("Bạn đã được " + score +" điểm :");
        answeredAdapter = new AnsweredAdapter(listQuestion);
        recyclerViewAnswered.setAdapter(answeredAdapter);
    }

}
