package com.dgr790.wrkapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class LeaderboardFragment extends Fragment {

    private ListView lvList;

    private ArrayList<String> fullnames;
    private ArrayList<String> points;
    private ArrayList<Integer> positions;

    private ArrayList<HashMap<String, String>> userList;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        lvList = (ListView) v.findViewById(R.id.lvList);

        mAuth = FirebaseAuth.getInstance();

        setupListView();




        return v;
    }

    private void setupListView() {
        DatabaseReference currentDB = FirebaseDatabase.getInstance().getReference().child("Users");

        currentDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userList =  new ArrayList<>();

                for (DataSnapshot user: dataSnapshot.getChildren()) {
                    HashMap<String, String> userTemp = (HashMap<String, String>) user.getValue();
                    userList.add(userTemp);
                }

                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXX");
                System.out.println(userList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//        usernames = new ArrayList<>();
//        points = new ArrayList<>();
//        positions = new ArrayList<>();
//        usernames.add("David Reynolds");
//        usernames.add("Becky Hart");
//        usernames.add("Beth Science");
//        points.add("700 Points");
//        points.add("600 Points");
//        points.add("500 Points");
//        positions.add(1);
//        positions.add(2);
//        positions.add(3);

//        MyListAdapter adapter = new MyListAdapter(getActivity(), fullnames, points, positions);
//        lvList.setAdapter(adapter);

    }

}
