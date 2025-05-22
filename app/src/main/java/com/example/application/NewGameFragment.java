package com.example.application;

import static com.example.application.MainActivity.navigationListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewGameFragment extends Fragment implements IdChecker{
    private TextInputEditText titleInput;
    private TextInputEditText imageUrlInput;
    private TextInputEditText descriptionInput;
    private MaterialButton createButton;
    private NewGameFragment.OnGameCreatedListener gameCreatedListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String gameId;

    public interface OnGameCreatedListener {
        void onGameCreated(GameItem gameItem);
    }

    public void setOnGameCreatedListener(NewGameFragment.OnGameCreatedListener listener) {
        this.gameCreatedListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_game_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация элементов
        titleInput = view.findViewById(R.id.title_input);
        imageUrlInput = view.findViewById(R.id.image_url_input);
        descriptionInput = view.findViewById(R.id.description_input);
        createButton = view.findViewById(R.id.create_game_button);
        View backButton = view.findViewById(R.id.back_button);

        // Обработка нажатия кнопки "Назад"
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        // Обработка нажатия кнопки "Создать"
        createButton.setOnClickListener(v -> {
            createGame();
        });
    }

    private void createGame() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String title = titleInput.getText().toString().trim();
        String imageUrl = imageUrlInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();

        // Валидация полей
        if (title.isEmpty()) {
            titleInput.setError(getString(R.string.field_required));
            return;
        }

        if (description.isEmpty()) {
            descriptionInput.setError(getString(R.string.field_required));
            return;
        }

        // Получаем текущую дату
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Получаем ID автора (здесь нужно реализовать получение текущего пользователя)
        String authorUid = mAuth.getUid();

        gameId = RandomIdGenerator.generateRandomString(12);

        // Создаем новую новость
        GameItem newGame = new GameItem(gameId, title, authorUid, currentDate, imageUrl, description);

        AddGames();
        mDatabase.child("games").child(gameId).setValue(newGame);

        navigationListener.navigateToFragment(new ApplicationMainFragment(), false);
        // Показываем уведомление об успешном создании
        Toast.makeText(requireContext(), R.string.game_created_successfully, Toast.LENGTH_SHORT).show();
    }

    void AddGames(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> array = new ArrayList<>();

                for (DataSnapshot ds : snapshot.child("games").getChildren()) {
                    array.add(ds.getKey());
                }

                if (!checkId(gameId, array)) {
                    String newId = RandomIdGenerator.generateRandomString(12);
                    while (!checkId(newId, array)) {
                        newId = RandomIdGenerator.generateRandomString(12);
                    }
                    mDatabase.child("game").child(gameId).setValue(newId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
