package com.example.apptracnghiem.activity.user;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.apptracnghiem.R;
import com.example.apptracnghiem.activity.auth.SigninActivity;
import com.example.apptracnghiem.database.Database;
import com.example.apptracnghiem.model.User;

public class UserProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 2;
    private User user;
    private ImageView userAvatar;
    private EditText usernameEditText, currentPassword, newPassword, reNewPassword;
    private Button editUserNameBtn, editPasswordBtn, editAvatarBtn;
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
        editAvatarBtn.setOnClickListener(this::handeEditAvatar);
        Log.d("ads",String.valueOf(user.getAvatar()));
        if (user.getAvatar() != null) {
            Log.d("ads",String.valueOf(Uri.parse(user.getAvatar())));
            userAvatar.setImageURI(Uri.parse(user.getAvatar()));
        }
    }



    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                userAvatar.setImageURI(selectedImage);
                user.setAvatar(selectedImage.toString());
                dbHelper.updateUser(user);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageSelector();
            } else {
                Toast.makeText(this, "grant permission fault", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void handeEditAvatar(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE);
        } else {
            openImageSelector();
        }
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
        User updatedUser = user;
        updatedUser.setUsername(usernameEditText.getText().toString());
        boolean isOke = dbHelper.updateUser(updatedUser);

        if (isOke) {
            Toast.makeText(this, "Cập nhật Username thành công", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Cập nhật Username thất bại", Toast.LENGTH_SHORT).show();
        }
    }

    private void bindingView() {
        dbHelper = new Database(this);

        usernameEditText = findViewById(R.id.user_profile_edit_user_name);
        currentPassword = findViewById(R.id.user_profile_current_password);
        newPassword = findViewById(R.id.user_profile_new_password);
        reNewPassword = findViewById(R.id.user_profile_re_new_password);

        editUserNameBtn = findViewById(R.id.user_profile_btn_edit_user_name);
        editPasswordBtn = findViewById(R.id.user_profile_btn_edit_password);
        editAvatarBtn = findViewById(R.id.user_profile_btn_edit_avatar);
        userAvatar = findViewById(R.id.user_profile_avatar);
        emailField = findViewById(R.id.user_profile_tv_email);

        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "Guest");

        user = dbHelper.getUserByEmail(email);
        if (user == null) {
            Intent intent = new Intent(this, SigninActivity.class);
            startActivity(intent);
        }
    }

}
