package com.example.application;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesFragment extends Fragment {


    private RecyclerView gamesRecycler;
    private GamesAdapter adapter;
    private List<GameItem> gamesList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GamesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GamesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GamesFragment newInstance(String param1, String param2) {
        GamesFragment fragment = new GamesFragment();
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
        View view = inflater.inflate(R.layout.fragment_games, container, false);
        // Inflate the layout for this fragment

        gamesRecycler = view.findViewById(R.id.games_recycler);

        // Настройка RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        gamesRecycler.setLayoutManager(layoutManager);

        // Создание и установка адаптера
        adapter = new GamesAdapter(gamesList);
        gamesRecycler.setAdapter(adapter);

        // Добавление элементов
        addGameItems();
        addGameItems();
        addGameItems();
        addGameItems();
        addGameItems();
        addGameItems();
        addGameItems();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void addGameItems() {
        // Добавление тестовых данных
        gamesList.add(new GameItem("Игра 1", R.drawable.games_page));
        gamesList.add(new GameItem("Игра 2", R.drawable.games_page));
        gamesList.add(new GameItem("Игра 3", R.drawable.games_page));
        gamesList.add(new GameItem("Игра 4", R.drawable.games_page));

        // Уведомление адаптера об изменениях
        adapter.notifyDataSetChanged();
    }

    // Метод для добавления нового элемента
    public void addNewGame(String title, int iconResId) {
        gamesList.add(new GameItem(title, iconResId));
        adapter.notifyItemInserted(gamesList.size() - 1);
    }
}