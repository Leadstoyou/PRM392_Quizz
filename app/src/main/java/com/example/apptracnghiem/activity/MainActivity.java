package com.example.apptracnghiem.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.apptracnghiem.R;
import com.example.apptracnghiem.activity.auth.SigninActivity;
import com.example.apptracnghiem.activity.business.QuestionActivity;
import com.example.apptracnghiem.activity.user.UserProfileActivity;
import com.example.apptracnghiem.activity.user.ViewTestActivity;
import com.example.apptracnghiem.database.Database;
import com.example.apptracnghiem.model.Category;
import com.example.apptracnghiem.util.Common;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_QUESTION = 1;
    private TextView textViewHighScore;

    private Spinner spinnerCategory;
    private Button buttonStareQuestion;
    private Button buttonUserProfile, buttonSignOut, buttonViewTest, buttonDownload ;
    private SharedPreferences sharedPreferences;
    private int highscore;
    private int PERMISSION_WRITE_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        loadCategories();

        loadHighScore();
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);

        buttonStareQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuestion();
            }
        });

        buttonUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserProfile();
            }
        });
        buttonSignOut.setOnClickListener(v -> {
            Common.clearSession(sharedPreferences);
            startActivity(new Intent(MainActivity.this, SigninActivity.class));
            finish();
        });
        buttonViewTest.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewTestActivity.class));
        });
        buttonDownload.setOnClickListener(v -> {
            verifyStoragePermissions();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_FILE);
            } else {
                downloadExcel();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadHighScore();
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_WRITE_FILE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadExcel();
            } else {
                Toast.makeText(this, "grant permission fault", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void downloadExcel() {
        new Thread(() -> {
            new Common().exportToExcel(this);
        }).start();
    }

    private void openUserProfile() {
        // Tạo Intent để mở UserProfileActivity
        Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
        startActivity(intent);
    }

    private void loadHighScore() {
        SharedPreferences preferences = getSharedPreferences("share", MODE_PRIVATE);
        highscore = preferences.getInt("highscore", 0);
        textViewHighScore.setText("Điểm cao : " + highscore);
    }

    private void startQuestion() {
        Category category = (Category) spinnerCategory.getSelectedItem();
        int categoryID = category.getId();
        String categoryName = category.getName();

        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);

        intent.putExtra("idcategories", categoryID);
        intent.putExtra("categoriesname", categoryName);

        startActivityForResult(intent, REQUEST_CODE_QUESTION);
    }

    private void AnhXa() {
        textViewHighScore = findViewById(R.id.textview_high_score);
        buttonStareQuestion = findViewById(R.id.button_start_question);
        spinnerCategory = findViewById(R.id.spinner_category);
        buttonUserProfile = findViewById(R.id.button_user_profile);
        buttonSignOut = findViewById(R.id.main_sign_out);
        buttonViewTest = findViewById(R.id.button_view_tests);
        buttonDownload = findViewById(R.id.button_download_data);

    }

    private void loadCategories() {
        Database database = new Database(this);
        List<Category> categories = database.getDataCategories();

        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);

        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategory.setAdapter(categoryArrayAdapter);
    }

    public void verifyStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
                    startActivityForResult(intent, 100);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 100);
                }
            }
        }
    }

}