package com.example.application;

import static com.example.application.MainActivity.applicationMainFragment;
import static com.example.application.MainActivity.bottomChosen;
import static com.example.application.MainActivity.navigationListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewNewsFragment extends Fragment implements IdChecker{

    private TextInputEditText titleInput;
    private TextInputEditText imageUrlInput;
    private TextInputEditText descriptionInput;
    private MaterialButton createButton;
    private OnNewsCreatedListener newsCreatedListener;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private Firebase firebase;
    private String newsId;

    public interface OnNewsCreatedListener {
        void onNewsCreated(NewsItem newsItem);
    }

    public void setOnNewsCreatedListener(OnNewsCreatedListener listener) {
        this.newsCreatedListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_news_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Инициализация элементов
        titleInput = view.findViewById(R.id.title_input);
        imageUrlInput = view.findViewById(R.id.image_url_input);
        descriptionInput = view.findViewById(R.id.description_input);
        createButton = view.findViewById(R.id.create_news_button);
        View backButton = view.findViewById(R.id.back_button);

        // Обработка нажатия кнопки "Назад"
        backButton.setOnClickListener(v -> {
            bottomChosen = "profile";
            navigationListener.navigateToFragment(applicationMainFragment, false);
        });

        // Обработка нажатия кнопки "Создать"
        createButton.setOnClickListener(v -> {
            createNews();
        });
    }

    private void createNews() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        String title = titleInput.getText().toString().trim();
        String imageUrl = imageUrlInput.getText().toString().trim();
        String description = descriptionInput.getText().toString().trim();

        if (title.isEmpty()) {
            titleInput.setError(getString(R.string.field_required));
            return;
        }

        if (description.isEmpty()) {
            descriptionInput.setError(getString(R.string.field_required));
            return;
        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String authorUid = mAuth.getUid();

        newsId = RandomIdGenerator.generateRandomString(10);

        NewsItem newNews = new NewsItem(newsId, title, authorUid, currentDate, imageUrl, description);

        AddNews();
        mDatabase.child("news").child(newsId).setValue(newNews);

        navigationListener.navigateToFragment(new ApplicationMainFragment(), false);
        Toast.makeText(requireContext(), R.string.news_created_successfully, Toast.LENGTH_SHORT).show();
    }

    void AddNews(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> array = new ArrayList<>();

                for (DataSnapshot ds : snapshot.child("news").getChildren()) {
                    array.add(ds.getKey());
                }

                if (!checkId(newsId, array)) {
                    String newId = RandomIdGenerator.generateRandomString(10);
                    while (!checkId(newId, array)) {
                        newId = RandomIdGenerator.generateRandomString(10);
                    }
                    mDatabase.child("news").child(newsId).setValue(newId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}