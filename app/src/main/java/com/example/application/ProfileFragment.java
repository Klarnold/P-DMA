package com.example.application;

import static android.app.Activity.RESULT_OK;

import static com.example.application.MainActivity.navigationListener;
import static com.example.application.MainActivity.mAuth;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    TextInputEditText nicknameEditText;
    TextInputEditText emailEditText;
    ShapeableImageView changeProfileImageButton;
    Button createNewGameBtn;
    Button createNewNewsBtn;
    ShapeableImageView changeNicknameBtn;
    ShapeableImageView changeEmailBtn;
    Button saveChangesBtn;
    Button logoutButton;
    ImageView profileImageView;
    DatabaseReference mDatabase;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        nicknameEditText = view.findViewById(R.id.nickname_profile);
        emailEditText = view.findViewById(R.id.profile_email);
        createNewGameBtn = view.findViewById(R.id.create_game_btn);
        createNewNewsBtn = view.findViewById(R.id.create_news_btn);
        logoutButton = view.findViewById(R.id.logout_button);
        profileImageView = view.findViewById(R.id.profile_image_view);
        changeEmailBtn = view.findViewById(R.id.change_email_btn);
        changeNicknameBtn = view.findViewById(R.id.change_nickname_btn);
        saveChangesBtn = view.findViewById(R.id.save_changes_btn);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        nicknameEditText.setFocusable(false);
        emailEditText.setFocusable(false);

        getData(profileImageView, nicknameEditText, emailEditText);

        logoutButton.setOnClickListener(v -> showLogoutConfirmationDialog());

        changeProfileImageButton = view.findViewById(R.id.change_profile_image_btn);
        createNewNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationListener.navigateToFragment(new NewNewsFragment(), false);
            }
        });
        createNewGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationListener.navigateToFragment(new NewGameFragment(), false);
            }
        });

        // Блокируем редактирование по умолчанию
        setEditTextsEditable(false);

        // Обработчики кнопок изменения
        changeNicknameBtn.setOnClickListener(v -> {
            setEditTextsEditable(false); // Сначала блокируем оба поля
            nicknameEditText.setFocusableInTouchMode(true);
            nicknameEditText.requestFocus();
            showKeyboard(nicknameEditText);
        });

        changeEmailBtn.setOnClickListener(v -> {
            setEditTextsEditable(false); // Сначала блокируем оба поля
            emailEditText.setFocusableInTouchMode(true);
            emailEditText.requestFocus();
            showKeyboard(emailEditText);
        });

        // Обработчик потери фокуса
        nicknameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                setEditTextsEditable(false);
            }
        });

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                setEditTextsEditable(false);
            }
        });

        saveChangesBtn.setOnClickListener(v ->{
            try {
//                String tempEmail = nicknameEditText.getText().toString();
                if (!isValidEmail(emailEditText.getText().toString()))
                    throw new Exception();
                mDatabase.child("users").child(Objects.requireNonNull(mAuth.getUid())).child("nickname").setValue(nicknameEditText.getText().toString());
                mDatabase.child("users").child(Objects.requireNonNull(mAuth.getUid())).child("email").setValue(emailEditText.getText().toString());
                Toast.makeText(getContext(), "Изменения были успешно сохранены!", Toast.LENGTH_SHORT).show();
            } catch (Exception e){
                Toast.makeText(getContext(), "Изменения не были сохранены :(", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Метод для проверки валидности email
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern) && !email.isEmpty();
    }

    public void getData(ImageView _profileImageView, EditText _nicknameEditText, EditText _emailEditText){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                _nicknameEditText.setText(Objects.requireNonNull(snapshot.child("users").child(Objects.requireNonNull(mAuth.getUid())).child("nickname").getValue()).toString());
                _emailEditText.setText(Objects.requireNonNull(snapshot.child("users").child(Objects.requireNonNull(mAuth.getUid())).child("email").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Подтверждение выхода")
                .setMessage("Вы уверены, что хотите выйти из аккаунта?")
                .setPositiveButton("Выйти", (dialog, which) -> {
                    // Здесь реализуйте логику выхода
                    performLogout();
                })
                .setNegativeButton("Отмена", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }


    // Метод для выполнения выхода (замените на свою реализацию)
    private void performLogout() {
        mAuth.signOut();
        Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show();

        navigationListener.navigateToFragment(new AuthFragment(), false);
    }

    // Метод для управления состоянием редактирования
    private void setEditTextsEditable(boolean editable) {
        nicknameEditText.setFocusable(editable);
        nicknameEditText.setFocusableInTouchMode(editable);
        emailEditText.setFocusable(editable);
        emailEditText.setFocusableInTouchMode(editable);

        if (!editable) {
            nicknameEditText.clearFocus();
            emailEditText.clearFocus();
            hideKeyboard();
        }
    }

    // Показать клавиатуру
    private void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    // Скрыть клавиатуру
    private void hideKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}