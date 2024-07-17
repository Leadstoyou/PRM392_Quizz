package com.example.apptracnghiem.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptracnghiem.R;
import com.example.apptracnghiem.database.Database;
import com.example.apptracnghiem.model.Test;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private final Context context;
    private final List<Test> testList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public TestAdapter(Context context, List<Test> testList) {
        this.context = context;
        this.testList = testList;
    }


    @NonNull
    @Override
    public TestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test, parent, false);
        return new TestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TestViewHolder holder, int position) {
        Database dbHelper = new Database(this.context);
        Log.d("testList",String.valueOf(testList));
        Log.d("postoion",String.valueOf(position));
        Test test = testList.get(position);
        Log.d("postoion123",dbHelper.getCategoryById(test.getCategory_id()).getName());
        holder.testNameTextView.setText(dbHelper.getCategoryById(test.getCategory_id()).getName());
        holder.testScoreTextView.setText("Score: " + test.getScore());
        holder.testDateTextView.setText("Date: " + test.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public static class TestViewHolder extends RecyclerView.ViewHolder {
        public TextView testNameTextView, testScoreTextView, testDateTextView;

        public TestViewHolder(View view) {
            super(view);
            testNameTextView = view.findViewById(R.id.textView_test_name);
            testScoreTextView = view.findViewById(R.id.textView_test_score);
            testDateTextView = view.findViewById(R.id.textView_test_date);
        }
    }
}