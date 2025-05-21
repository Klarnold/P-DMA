package com.example.application;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    private RecyclerView newsRecycler;
    private NewsAdapter newsAdapter;
    private List<News> newsList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        // Инициализация RecyclerView
        newsRecycler = view.findViewById(R.id.news_recycler);
        newsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Загрузка начальных данных
        loadInitialNews();

        // Настройка адаптера
        newsAdapter = new NewsAdapter(newsList);
        newsRecycler.setAdapter(newsAdapter);

        // Бесконечная прокрутка
        newsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                int totalItems = layoutManager.getItemCount();

                if (lastVisibleItem == totalItems - 1) {
                    loadMoreNews(); // Подгрузка новых данных
                }
            }
        });

        return view;
    }

    // Загрузка начальных данных
    private void loadInitialNews() {
        newsList.add(new News("Заголовок 1", "user123", "2024-05-21", "https://example.com/image1.jpg", "Описание 1"));
        newsList.add(new News("Заголовок 2", "user456", "2024-05-20", "https://example.com/image2.jpg", "Описание 2"));
    }

    // Подгрузка новых данных
    private void loadMoreNews() {
        List<News> newNews = new ArrayList<>();
        newNews.add(new News("Новая новость 1", "user789", "2024-05-22", "https://example.com/image3.jpg", "Новое описание 1"));
        newNews.add(new News("Новая новость 2", "user101", "2024-05-23", "https://example.com/image4.jpg", "Новое описание 2"));

        newsAdapter.addNews(newNews);
    }
}