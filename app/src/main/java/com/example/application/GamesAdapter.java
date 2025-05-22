package com.example.application;

import static com.example.application.MainActivity.mDatabase;
import static com.example.application.MainActivity.navigationListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GameViewHolder> {

    private List<GameItem> gamesList;
    private List<GameItem> gamesListFull;
    private FragmentManager fragmentManager;

    public GamesAdapter(List<GameItem> gamesList) {
        this.gamesList = gamesList;
        this.gamesListFull = new ArrayList<>(gamesList);
    }

    public void addGames(List<GameItem> newGames) {
        gamesList.addAll(newGames);
        notifyDataSetChanged();
    }

    public void filter(String text) {
        List<GameItem> filteredList = new ArrayList<>();
        text = text.toLowerCase();

        if (text.isEmpty()) {
            filteredList.addAll(gamesListFull);
        } else {
            for (GameItem item : gamesListFull) {
                if (item.getTitle().toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }
        }

        updateList(filteredList);
    }

    public void updateList(List<GameItem> filteredList) {
        gamesList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameItem gameItem = gamesList.get(position);
        holder.bind(gameItem);



        holder.itemView.setOnClickListener(v -> {
            GameDetailFragment detailFragment = GameDetailFragment.newInstance(gameItem);
            navigationListener.navigateToFragment(detailFragment, false);
        });
    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        public ImageView gameIcon;
        private TextView titleTextView;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameIcon = itemView.findViewById(R.id.game_icon);
            titleTextView = itemView.findViewById(R.id.game_title);
        }

        public void bind(GameItem gameItem) {
            titleTextView.setText(gameItem.getTitle());
            // gameIcon.setImageResource();
        }
    }
}
