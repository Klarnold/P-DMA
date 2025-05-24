package com.example.application;

import static com.example.application.MainActivity.bottomChosen;
import static com.example.application.MainActivity.navigationListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class GameDetailFragment extends Fragment {

    private DatabaseReference mDatabase;
    private static final String ARG_GAME = "gameItem";

    private GameItem gameItem;

    public GameDetailFragment() {
        // Required empty public constructor
    }

    public static GameDetailFragment newInstance(GameItem gameItem) {
        GameDetailFragment fragment = new GameDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_GAME, gameItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gameItem = (GameItem) getArguments().getParcelable(ARG_GAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.game_detail_fragment, container, false);
    }

    public void getData(GameItem gameItem, TextView authorTextView){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                authorTextView.setText(Objects.requireNonNull(snapshot.child("users").child(gameItem.getUid()).child("nickname").getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Инициализация элементов интерфейса
        ImageButton backButton = view.findViewById(R.id.back_button);
        TextView titleTextView = view.findViewById(R.id.game_detail_title);
        ImageView imageView = view.findViewById(R.id.game_detail_image);
        TextView authorTextView = view.findViewById(R.id.game_detail_author);
        TextView dateTextView = view.findViewById(R.id.game_detail_date);
        TextView descriptionTextView = view.findViewById(R.id.game_detail_description);

        // Установка данных
        if (gameItem != null) {
            titleTextView.setText(gameItem.getTitle());
            getData(gameItem, authorTextView);
            dateTextView.setText(gameItem.getDate());
            descriptionTextView.setText(gameItem.getDescription());
        }

        // Обработка нажатия кнопки "назад"
        backButton.setOnClickListener(v -> {
            bottomChosen = "games";
            navigationListener.navigateToFragment(new AuthFragment(), false);
        });


    }
}
