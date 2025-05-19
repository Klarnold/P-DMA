package com.example.application;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FrameLayout frameLayout;
    private Button signUpBtn;
    private Button signInBtn;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fragmentManager = getFragmentManager();
        frameLayout = findViewById(R.id.activity_frame);
        TextView frameTextView = new TextView(this);
        frameTextView.setText("aaa");
        frameLayout.addView(frameTextView);
        mAuth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        signInBtn = findViewById(R.id.signInBtn);
        signUpBtn = findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Не оставляйте пустые поля!", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                frameTextView.setText(mAuth.getCurrentUser().getEmail());
                                Toast.makeText(RegisterActivity.this, "Регистрация прошла успешно " + mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Пользователь НЕ был зарегестрирован", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
//                mAuth.
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Intent intent = new Intent();
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
                else{
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String user_id = mAuth.getCurrentUser().getEmail();
                                frameTextView.setText(user_id);
                                Toast.makeText(RegisterActivity.this, user_id + " " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
//                                fragmentManager.beginTransaction().remove();
                            } else{
                                Toast.makeText(RegisterActivity.this, "Авторизация отменена", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(RegisterActivity.this, "Пользователя с такими почтой и паролем не существует", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}