package com.example.apptracnghiem.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apptracnghiem.R;
import com.example.apptracnghiem.model.Question;

import java.util.List;

public class AnsweredAdapter extends RecyclerView.Adapter<AnsweredAdapter.AnsweredViewHolder> {

    private List<Question> itemList;

    public AnsweredAdapter(List<Question> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public AnsweredViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_answered_test, parent, false);
        return new AnsweredViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnsweredViewHolder holder, int position) {
        holder.rb1.setTextColor(Color.BLACK);
        holder.rb2.setTextColor(Color.BLACK);
        holder.rb3.setTextColor(Color.BLACK);
        holder.rb4.setTextColor(Color.BLACK);

        Question item = itemList.get(position);
        holder.textViewQuestion.setText(item.getQuestion());
        holder.rb1.setText(item.getOption1());
        holder.rb2.setText(item.getOption2());
        holder.rb3.setText(item.getOption3());
        holder.rb4.setText(item.getOption4());

        RadioButton chooseBtn = (RadioButton) holder.rbGroup.getChildAt(item.getAnsweredQuestion());
        holder.rbGroup.check(chooseBtn.getId());
        boolean flag = true;
        switch (item.getAnswer()) {
            case 1:
                holder.rb1.setTextColor(Color.GREEN);
                if(chooseBtn.getId() == holder.rb1.getId()) {
                    flag = false;
                }
                break;
            case 2:
                holder.rb2.setTextColor(Color.GREEN);
                if(chooseBtn.getId() == holder.rb2.getId()) {
                    flag = false;
                }
                break;
            case 3:
                holder.rb3.setTextColor(Color.GREEN);
                if(chooseBtn.getId() == holder.rb3.getId()) {
                    flag = false;
                }
                break;
            case 4:
                holder.rb4.setTextColor(Color.GREEN);
                if(chooseBtn.getId() == holder.rb4.getId()) {
                    flag = false;
                }
                break;
        }
        if(flag) {
            chooseBtn.setTextColor(Color.RED);
        }
        for (int i = 0; i < holder.rbGroup.getChildCount(); i++) {
            holder.rbGroup.getChildAt(i).setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class AnsweredViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewQuestion;
        public RadioButton rb1, rb2, rb3, rb4;
        public RadioGroup rbGroup;

        public AnsweredViewHolder(View view) {
            super(view);
            textViewQuestion = view.findViewById(R.id.answerd_text_view_question);
            rbGroup = view.findViewById(R.id.answerd_radio_group);
            rb1 = view.findViewById(R.id.answerd_radio_button1);
            rb2 = view.findViewById(R.id.answerd_radio_button2);
            rb3 = view.findViewById(R.id.answerd_radio_button3);
            rb4 = view.findViewById(R.id.answerd_radio_button4);
        }
    }
}
