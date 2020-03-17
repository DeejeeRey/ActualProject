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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class LeaderboardFragment extends Fragment {

    private ListView lvList;

    private ArrayList<String> fullnames = new ArrayList<String>();
    private ArrayList<String> firstnames = new ArrayList<String>();
    private ArrayList<String> secondnames = new ArrayList<String>();
    private ArrayList<String> points = new ArrayList<String>();
    private ArrayList<Integer> positions = new ArrayList<Integer>();

    private int i = 0;

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

                Comparator<HashMap<String, String>> scoreComparator = new Comparator<HashMap<String,String>>() {

                    @Override
                    public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                        // Get the distance and compare the distance.
                        Integer distance1 = Integer.parseInt(String.valueOf(o1.get("Score")));
                        Integer distance2 = Integer.parseInt(String.valueOf(o2.get("Score")));

                        return distance1.compareTo(distance2);
                    }
                };

                Collections.sort(userList, scoreComparator);
                Collections.reverse(userList);

                setViews();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setViews() {

        for (HashMap<String,String> entry : userList) {
            i++;
            for(String key : entry.keySet()) {
                String value = String.valueOf(entry.get(key));
                if (key.equals("First Name")) {
                    firstnames.add(value);
                    positions.add(i);
                } else if (key.equals("Second Name")) {
                    secondnames.add(value);
                } else if (key.equals("Score")) {
                    points.add(value);
                } else {

                }
            }
        }

        int c = 0;

        for (String name : firstnames) {
            fullnames.add(name + " " + secondnames.get(c));
            c++;
        }

        LeaderboardListAdapter adapter = new LeaderboardListAdapter(getActivity(), fullnames, points, positions);
        lvList.setAdapter(adapter);


    }

}
