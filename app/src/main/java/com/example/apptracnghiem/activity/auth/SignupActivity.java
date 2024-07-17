package com.example.apptracnghiem.activity.auth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apptracnghiem.R;
import com.example.apptracnghiem.database.Database;
import com.example.apptracnghiem.model.User;
import com.example.apptracnghiem.util.EmailSender;

public class SignupActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextPassword, editTextRePassword, editTextEmail;
    private Button buttonSignUp;
    private Database dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dbHelper = new Database(this);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRePassword = findViewById(R.id.editTextRePassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSignUp = findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String username = editTextUsername.getText().toString().trim();
                    String password = editTextPassword.getText().toString().trim();
                    String rePassword = editTextRePassword.getText().toString().trim();
                    String email = editTextEmail.getText().toString().trim();

                    if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                        Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Toast.makeText(SignupActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!password.equals(rePassword)) {
                        Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    User user = dbHelper.getUserByEmail(email);
                    Log.d("user", String.valueOf(user));
                    if (user != null) {
                        Toast.makeText(SignupActivity.this, "Email already existed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    boolean isInserted = dbHelper.addUser(email, username, password);
                    if (isInserted) {
                        new EmailSender().sendCongratToNewUser(SignupActivity.this, email, username);
                        Toast.makeText(SignupActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "User Name is existed", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("error", e.toString());
                }
            }
        });
    }
}