package com.dgr790.wrkapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

    private ListView lvList;

    private ArrayList<String> usernames;
    private ArrayList<String> points;
    private ArrayList<Integer> positions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        lvList = (ListView) v.findViewById(R.id.lvList);

        setupListView();


        return v;
    }

    private void setupListView() {
        usernames = new ArrayList<>();
        points = new ArrayList<>();
        positions = new ArrayList<>();
        usernames.add("David Reynolds");
        usernames.add("Becky Hart");
        usernames.add("Beth Science");
        points.add("700 Points");
        points.add("600 Points");
        points.add("500 Points");
        positions.add(1);
        positions.add(2);
        positions.add(3);

        MyListAdapter adapter = new MyListAdapter(getActivity(), usernames, points, positions);
        lvList.setAdapter(adapter);

    }

}
