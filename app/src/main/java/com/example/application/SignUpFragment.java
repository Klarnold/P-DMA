package com.example.application;

import android.content.Context;
import android.os.Bundle;

import static com.example.application.MainActivity.navigationListener;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    private Button signUpBtn;
    private Button signInBtn;
    private EditText emailEditText;
    private EditText passwordEditText;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        TextView frameTextView = view.findViewById(R.id.authTextView);
        frameTextView.setText("Создание новой учётной записи");
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
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
                                createNewUser(task.getResult().getUser(), view.findViewById(R.id.nicknameEditText));
                                navigationListener.navigateToFragment(new ApplicationMainFragment(), true);
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
                navigationListener.navigateToFragment(new AuthFragment(), true);
            }
        });
        return view;
    }


    private void createNewUser(FirebaseUser userFromRegistration, EditText nicknameEditText){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String nickname = nicknameEditText.getText().toString();
        String email = userFromRegistration.getEmail();
        String userId = userFromRegistration.getUid();

        User user = new User(nickname, email);
        mDatabase.child("users").child(userId).setValue(user);
    }


}