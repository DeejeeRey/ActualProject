package com.dgr790.wrkapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Bundle containing user info
        Bundle extras = getIntent().getExtras();
        String[] userInformation = extras.getStringArray("userInformation");

        if (userInformation[6].equals("true")) {
            updateDB(userInformation);
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
    }

    @Override
    public void onBackPressed() {
    }

    // Updates database
    private void updateDB(String[] info) {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentDB = FirebaseDatabase.getInstance().getReference().child("Users").child(id);

        HashMap newInfo = new HashMap();
        newInfo.put("First Name", info[0]);
        newInfo.put("Second Name", info[1]);
        newInfo.put("Username", info[2]);
        newInfo.put("Email", info[3]);
        newInfo.put("Score", Integer.parseInt(info[4]));
        newInfo.put("Times", Integer.parseInt(info[5]));

        currentDB.updateChildren(newInfo);
    }


    // Used to select fragment
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFrag = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFrag = new HomeFragment();
                            break;
//                        case R.id.nav_game:
//                            selectedFrag = new GameFragment();
//                            break;
                        case R.id.nav_leaderboard:
                            selectedFrag = new LeaderboardFragment();
                            break;
                        case R.id.nav_plan:
                            selectedFrag = new PlanFragment();
                            break;
                        case R.id.nav_profile:
                            selectedFrag = new ProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFrag).commit();

                    return true;
                }
            };

}
