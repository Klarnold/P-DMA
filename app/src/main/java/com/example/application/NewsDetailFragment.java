package com.example.application;

import static com.example.application.MainActivity.applicationMainFragment;
import static com.example.application.MainActivity.bottomChosen;
import static com.example.application.MainActivity.mDatabase;
import static com.example.application.MainActivity.navigationListener;
import static com.example.application.MainActivity.newsFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NewsDetailFragment extends Fragment {

    private DatabaseReference mDatabase;
    private static final String ARG_NEWS = "newsItem";

    private NewsItem newsItem;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    public static NewsDetailFragment newInstance(NewsItem newsItem) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NEWS, newsItem);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newsItem = (NewsItem) getArguments().getParcelable(ARG_NEWS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.news_detail_fragment, container, false);
    }

    public void getData(NewsItem newsItem, TextView authorTextView){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                authorTextView.setText(Objects.requireNonNull(snapshot.child("users").child(newsItem.getUid()).child("nickname").getValue()).toString());
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

        ImageButton backButton = view.findViewById(R.id.back_button);
        TextView titleTextView = view.findViewById(R.id.news_detail_title);
        ImageView imageView = view.findViewById(R.id.news_detail_image);
        TextView authorTextView = view.findViewById(R.id.news_detail_author);
        TextView dateTextView = view.findViewById(R.id.news_detail_date);
        TextView descriptionTextView = view.findViewById(R.id.news_detail_description);

        if (newsItem != null) {
            titleTextView.setText(newsItem.getTitle());
            getData(newsItem, authorTextView);
            dateTextView.setText(newsItem.getDate());
            descriptionTextView.setText(newsItem.getDescription());
        }

        backButton.setOnClickListener(v -> {
            bottomChosen = "news";
            navigationListener.navigateToFragment(applicationMainFragment, false);
        });


    }

}
