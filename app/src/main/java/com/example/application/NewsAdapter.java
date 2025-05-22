package com.example.application;

import static com.example.application.MainActivity.navigationListener;
import static com.example.application.MainActivity.mDatabase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> newsItemList;
    private List<NewsItem> newsItemListFull; // Полный список для фильтрации
    private FragmentManager fragmentManager;

    public NewsAdapter(List<NewsItem> newsItemList, FragmentManager fragmentManager) {
        this.newsItemList = newsItemList;
        this.newsItemListFull = new ArrayList<>(newsItemList);
        this.fragmentManager = fragmentManager;
    }

    // Метод для обновления списка
    public void updateList(List<NewsItem> newList) {
        newsItemList = newList;
        notifyDataSetChanged();
    }

    // Метод для фильтрации новостей
    public void filter(String text) {
        List<NewsItem> filteredList = new ArrayList<>();
        text = text.toLowerCase().trim();

        if (text.isEmpty()) {
            filteredList.addAll(newsItemListFull);
        } else {
            for (NewsItem item : newsItemListFull) {
                if (item.getTitle().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }
        }

        updateList(filteredList);
    }

    public void addNews(List<NewsItem> newNews) {
        newsItemList.addAll(newNews);
        newsItemListFull.addAll(newNews);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsItemList.get(position);
        holder.bind(newsItem);

        holder.itemView.setOnClickListener(v -> {
            NewsDetailFragment detailFragment = NewsDetailFragment.newInstance(newsItem);
            navigationListener.navigateToFragment(detailFragment, false);
        });
    }

    @Override
    public int getItemCount() {
        return newsItemList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final ImageView imageView;
        private final TextView authorTextView;
        private final TextView dateTextView;
        private final TextView descriptionTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.news_title);
            imageView = itemView.findViewById(R.id.news_image);
            authorTextView = itemView.findViewById(R.id.news_author);
            dateTextView = itemView.findViewById(R.id.news_date);
            descriptionTextView = itemView.findViewById(R.id.news_description);
        }

        public void bind(NewsItem newsItem) {
            titleTextView.setText(newsItem.getTitle());
            getData(newsItem);
            dateTextView.setText(newsItem.getDate());
            descriptionTextView.setText(newsItem.getDescription());
        }

        private void getData(NewsItem newsItem) {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    authorTextView.setText(Objects.requireNonNull(
                            snapshot.child("users")
                                    .child(newsItem.getUid())
                                    .child("nickname")
                                    .getValue()).toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(itemView.getContext(),
                            "Ошибка загрузки автора",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}