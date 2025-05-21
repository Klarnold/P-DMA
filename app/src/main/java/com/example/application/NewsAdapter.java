package com.example.application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> newsList;

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;
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
        News news = newsList.get(position);
        holder.title.setText(news.getTitle());
        holder.author.setText(news.getAuthorUid());
        holder.date.setText(news.getDate());
        holder.description.setText(news.getDescription());

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    // ViewHolder
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, date, description;
        ImageView image;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_title);
            author = itemView.findViewById(R.id.news_author);
            date = itemView.findViewById(R.id.news_date);
            description = itemView.findViewById(R.id.news_description);
            image = itemView.findViewById(R.id.news_image);
        }
    }

    // Метод для добавления новых новостей
    public void addNews(List<News> newNews) {
        int oldSize = newsList.size();
        newsList.addAll(newNews);
        notifyItemRangeInserted(oldSize, newNews.size());
    }
}
