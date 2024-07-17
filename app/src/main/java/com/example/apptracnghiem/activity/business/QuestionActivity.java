package com.example.apptracnghiem.activity.business;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptracnghiem.R;
import com.example.apptracnghiem.database.Database;
import com.example.apptracnghiem.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuestionActivity extends AppCompatActivity {
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView TextViewQuestionCount;
    private TextView textViewCategory;
    private TextView textViewCountDown;
    private Database dbHelper;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;

    private CountDownTimer countDownTimer;
    private ArrayList<Question> questionList;
    private long timeLeftInMillis;
    private int questionCounter;
    private int questionSize;
    int categoryID;
    private int Score;
    private boolean answered;

    private Question currentQuestion;

    private int count = 0;
    private ArrayList<Question> answeredQuestionList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        anhxa();

        Intent intent = getIntent();
        categoryID = intent.getIntExtra("idcategories", 0);
        String categoryName = intent.getStringExtra("categoriesname");

        textViewCategory.setText("Chủ đề : " + categoryName);

        Database database = new Database(this);

        questionList = database.getQuestions(categoryID);

        questionSize = questionList.size();

        Collections.shuffle(questionList);

        showNextQuestion();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuestionActivity.this, "Hãy chọn đáp án", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion() {
        rb1.setTextColor(Color.BLACK);
        rb2.setTextColor(Color.BLACK);
        rb3.setTextColor(Color.BLACK);
        rb4.setTextColor(Color.BLACK);

        rbGroup.clearCheck();

        if (questionCounter < questionSize) {

            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());

            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;

            TextViewQuestionCount.setText("Câu hỏi : " + questionCounter + " /" + questionSize);

            answered = false;

            buttonConfirmNext.setText("Xác nhận");

            timeLeftInMillis = 30000;

            startCountDown();

        } else {
            finishQuestion();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;

                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();

                checkAnswer();
            }
        }.start();
    }

    private void checkAnswer() {
        answered = true;

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());

        int answer = rbGroup.indexOfChild(rbSelected) + 1;

        currentQuestion.setAnsweredQuestion(rbGroup.indexOfChild(rbSelected));
        answeredQuestionList.add(currentQuestion);

        if (answer == currentQuestion.getAnswer()) {
            Score = Score + 10;
            textViewScore.setText("Điểm : " + Score);

        }
        showSolution();
    }

    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch (currentQuestion.getAnswer()) {
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Đáp án là A");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Đáp án là B");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Đáp án là C");
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                textViewQuestion.setText("Đáp án là D");
                break;
        }
        if (questionCounter < questionSize) {
            buttonConfirmNext.setText("Câu tiếp");
        } else {
            buttonConfirmNext.setText("Hoàn thành");
        }
        countDownTimer.cancel();

    }

    private void updateCountDownText() {

        int minutes = (int) ((timeLeftInMillis / 1000) / 60);

        int seconds = (int) ((timeLeftInMillis / 1000) % 60);

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        textViewCountDown.setText(timeFormatted);

        if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(Color.BLACK);
        }
    }

    private void finishQuestion() {
        Intent resultIntent = new Intent(QuestionActivity.this,AnsweredListActivity.class);

        SharedPreferences preferences = getSharedPreferences("share", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(preferences.getInt("highscore",0) < Score) {
            editor.putInt("highscore", Score);
            editor.apply();
        }

        dbHelper.addNewScore(getSharedPreferences("user_session", MODE_PRIVATE).getString("email", "Guest"),categoryID,Score);
        resultIntent.putExtra("answeredList",  answeredQuestionList);
        resultIntent.putExtra("score",  Score);
        startActivity(resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        count++;
        if (count >= 1) {
            finishQuestion();
        }
        count = 0;
    }

    private void anhxa() {
        dbHelper =  new Database(this);
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        TextViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCategory = findViewById(R.id.text_view_category);

        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);

        buttonConfirmNext = findViewById(R.id.button_confim_next);
    }
}
