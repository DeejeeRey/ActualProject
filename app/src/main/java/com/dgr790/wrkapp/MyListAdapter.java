package com.dgr790.wrkapp;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> usernames;
    private final ArrayList<String> points;
    private final ArrayList<Integer> positions;

    public MyListAdapter(Activity context, ArrayList<String> usernames, ArrayList<String> points, ArrayList<Integer> positions) {
        super(context, R.layout.listview_item, usernames);

        this.context = context;
        this.usernames = usernames;
        this.points = points;
        this.positions = positions;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_item, null, true);

        TextView lvPosition = (TextView) rowView.findViewById(R.id.lvPosition);
        TextView lvName = (TextView) rowView.findViewById(R.id.lvName);
        TextView lvScore = (TextView) rowView.findViewById(R.id.lvScore);

        lvPosition.setText(positions.get(position) + "");
        lvName.setText(usernames.get(position));
        lvScore.setText(points.get(position));

        return rowView;
    }
}
