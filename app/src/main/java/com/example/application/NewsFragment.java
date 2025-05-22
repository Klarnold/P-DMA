package com.example.application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private DatabaseReference mDatabase;
    private RecyclerView newsRecycler;
    private NewsAdapter newsAdapter;
    private List<NewsItem> newsItemList = new ArrayList<>();
    private SearchView searchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference("news");

        // Инициализация RecyclerView
        newsRecycler = view.findViewById(R.id.news_recycler);
        newsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Инициализация SearchView
        searchView = view.findViewById(R.id.search);
        setupSearchView();

        // Получаем FragmentManager из активности
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Настройка адаптера
        newsAdapter = new NewsAdapter(newsItemList, fragmentManager);
        newsRecycler.setAdapter(newsAdapter);

        getData();

        return view;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newsAdapter.filter(newText);
                return false;
            }
        });
    }

    private void getData() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<NewsItem> newList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    newList.add(ds.getValue(NewsItem.class));
                }
                newsAdapter.addNews(newList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(),
                        "Ошибка загрузки новостей",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}