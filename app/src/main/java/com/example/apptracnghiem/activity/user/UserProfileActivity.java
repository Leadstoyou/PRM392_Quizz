package com.example.apptracnghiem.activity.user;

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
import com.example.apptracnghiem.activity.auth.SigninActivity;
import com.example.apptracnghiem.database.Database;
import com.example.apptracnghiem.model.User;

public class UserProfileActivity extends AppCompatActivity {
    private User user;
    private EditText usernameEditText, currentPassword,newPassword,reNewPassword;
    private Button editUserNameBtn,editPasswordBtn;
    private TextView emailField;
    private SharedPreferences sharedPreferences;
    private Database dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        bindingView();
        bindingAction();
    }

    private void bindingAction() {
        emailField.setText(user.getEmail());
        usernameEditText.setText(user.getUsername());

        editUserNameBtn.setOnClickListener(this::handleEditUserName);
        editPasswordBtn.setOnClickListener(this::handleEditPassword);
    }

    private void handleEditPassword(View view) {
        String currentPasswordInput = currentPassword.getText().toString().trim();
        String newPasswordInput = newPassword.getText().toString().trim();
        String reNewPasswordInput = reNewPassword.getText().toString().trim();

        if (!currentPasswordInput.equals(user.getPassword())) {
            Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPasswordInput.equals(reNewPasswordInput)) {
            Toast.makeText(this, "Mật khẩu mới và xác nhận mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        user.setPassword(newPasswordInput);
        boolean isUpdated = dbHelper.updateUser(user);

        if (isUpdated) {
            Toast.makeText(this, "Cập nhật mật khẩu thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cập nhật mật khẩu thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleEditUserName(View view) {
        User updatedUser =  user;
        updatedUser.setUsername(usernameEditText.getText().toString());
        boolean isOke = dbHelper.updateUser(updatedUser);
    }

    private void bindingView() {
        dbHelper = new Database(this);

        usernameEditText = findViewById(R.id.user_profile_edit_user_name);
        currentPassword = findViewById(R.id.user_profile_current_password);
        newPassword = findViewById(R.id.user_profile_new_password);
        reNewPassword = findViewById(R.id.user_profile_re_new_password);

        editUserNameBtn = findViewById(R.id.user_profile_btn_edit_user_name);
        editPasswordBtn = findViewById(R.id.user_profile_btn_edit_password);

        emailField = findViewById(R.id.user_profile_tv_email);
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "Guest");

        user = dbHelper.getUserByEmail(email);
        if(user == null) {
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
        }
    }

}
