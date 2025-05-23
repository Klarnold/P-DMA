package com.example.application;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.application.databinding.ActivityMainBinding;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.application.FragmentNavigationListener;

public class MainActivity extends AppCompatActivity implements FragmentNavigationListener{

    ActivityMainBinding binding;
    FragmentManager fragmentManager;
    public static DatabaseReference mDatabase;
    public static FirebaseAuth mAuth;
    public static NewsFragment newsFragment;
    public static GamesFragment gamesFragment;
    public static ProfileFragment profileFragment;
    public static String bottomChosen;
    public static ApplicationMainFragment applicationMainFragment;

    public static FragmentNavigationListener navigationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable( MainActivity.this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            setContentView(R.layout.activity_main);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main, AuthFragment.class, null)
                        .commit();
            }
            return insets;
        });

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        bottomChosen = "games";
        // Создаем фрагменты только если они null (чтобы не пересоздавать при повороте)
        if (newsFragment == null) newsFragment = new NewsFragment();
        if (gamesFragment == null) gamesFragment = new GamesFragment();
        if (profileFragment == null) profileFragment = new ProfileFragment();
        if (applicationMainFragment == null) applicationMainFragment = new ApplicationMainFragment();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        navigationListener = (FragmentNavigationListener) this;
        fragmentManager = getSupportFragmentManager();

    }


    @Override
    public void navigateToFragment(Fragment fragment, boolean addToBackStack) { // метод для перехода на выбранный фрагмент
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void navigateToBottomBar(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout, fragment);
        transaction.commit();
    }


}