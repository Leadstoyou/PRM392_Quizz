package com.example.apptracnghiem.activity.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptracnghiem.R;
import com.example.apptracnghiem.activity.MainActivity;
import com.example.apptracnghiem.database.Database;

public class SigninActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin;
    private TextView textViewSignUp;
    private Database dbHelper;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);

        dbHelper = new Database(this);
        editTextEmail = findViewById(R.id.sign_in_editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);

        checkSession();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SigninActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isValid = dbHelper.checkUser(email, password);
                    if (isValid) {
                        saveSession(email);
                        Toast.makeText(SigninActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        // Start main activity or quiz activity
                        startActivity(new Intent(SigninActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SigninActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SigninActivity.this, SignupActivity.class));
            }
        });
    }

    private void checkSession() {
        String username = sharedPreferences.getString("email", null);
        if (username != null) {
            startActivity(new Intent(SigninActivity.this, MainActivity.class));
            finish();
        }
    }

    private void saveSession(String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.apply();
    }
}