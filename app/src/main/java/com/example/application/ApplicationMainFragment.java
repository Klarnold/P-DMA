package com.example.application;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.application.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ApplicationMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicationMainFragment extends Fragment {

    FragmentNavigationListener navigationListener;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    DatabaseReference usersReference;
    BottomNavigationView bottomNavigationView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ApplicationMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApplicationMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApplicationMainFragment newInstance(String param1, String param2) {
        ApplicationMainFragment fragment = new ApplicationMainFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_application_main, container, false);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        usersReference = mDatabase.getReference("users/" + Objects.requireNonNull(mAuth.getCurrentUser()).getUid());


        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        BottomNavigationView navView = view.findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.games_page) {
                    navigationListener.navigateToBottomBar(new GamesFragment());

                    return true;
                }
                else if (id == R.id.news_page){
                    navigationListener.navigateToBottomBar(new NewsFragment());
                    return true;
                }
                else if (id == R.id.profile_page){
                    navigationListener.navigateToBottomBar(new ProfileFragment());
                    return true;
                }

                return false;
            }
        });


//        meetingMainTextView = view.findViewById(R.id.meetingMainTextView);
        getData();
        navigationListener.navigateToBottomBar(new GamesFragment());
//        meetingMainTextView.setText(mDatabase.getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("nickname").getKey());
        return view;
    }

    private void getData(){
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
//                meetingMainTextView.setText("Hi, " + user.getNickname() + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), "Ошибка в получении данных пользователя", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // определение метода для перехода на новый фрагмент
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigationListener) {
            navigationListener = (FragmentNavigationListener) context;
        } else {
            throw new RuntimeException(context + " must implement FragmentNavigationListener");
        }
    }
}