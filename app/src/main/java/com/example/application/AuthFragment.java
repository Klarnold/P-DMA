package com.example.application;

import static android.app.ProgressDialog.show;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthFragment extends Fragment {

    private FragmentNavigationListener navigationListener;
    private FragmentManager fragmentManager;
    private FrameLayout frameLayout;
    private Button signUpBtn;
    private Button signInBtn;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;

    public AuthFragment(){
        super(R.layout.auth_fragment);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.auth_fragment, container, false);
        EditText icon = view.findViewById(R.id.passwordEditText);

        fragmentManager = getFragmentManager();
//        frameLayout = view.findViewById(R.id.activity_frame);
        TextView frameTextView = view.findViewById(R.id.authTextView);
        frameTextView.setText("aaa");
        mAuth = FirebaseAuth.getInstance();
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        view.getClass();
        signInBtn = view.findViewById(R.id.signInBtn);
        signUpBtn = view.findViewById(R.id.signUpBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(requireActivity(), "Не оставляйте пустые поля!", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                frameTextView.setText(mAuth.getCurrentUser().getEmail());
                                Toast.makeText(requireActivity(), "Регистрация прошла успешно " + mAuth.getCurrentUser().getUid(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(requireActivity(), "Пользователь НЕ был зарегестрирован", Toast.LENGTH_SHORT).show();
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
                if (TextUtils.isEmpty(email)  || TextUtils.isEmpty(password)){
                    navigationListener.navigateToFragment(new ApplicationMainFragment(), true); // осуществляет переход на фрагмент главной страницы
                }
                else{
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String user_id = mAuth.getCurrentUser().getEmail();
                                frameTextView.setText(user_id);
                                Toast.makeText(requireActivity(), user_id + " " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                navigationListener.navigateToFragment(new ApplicationMainFragment(), true);
//                                fragmentManager.beginTransaction().remove();
                            } else{
                                Toast.makeText(requireActivity(), "Авторизация отменена", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(requireActivity(), "Пользователя с такими почтой и паролем не существует", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // определение метода для перехода на новый фрагмент
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigationListener) {
            navigationListener = (FragmentNavigationListener) context;
        } else {
            throw new RuntimeException(context + " must implement FragmentNavigationListener");
        }
    }
}
