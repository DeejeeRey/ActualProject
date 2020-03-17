package com.dgr790.wrkapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TodoListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> tasks;

    public TodoListAdapter(Activity context, ArrayList<String> tasks) {
        super(context, R.layout.todo_item, tasks);

        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.todo_item, null, true);

        TextView lvName = (TextView) rowView.findViewById(R.id.lvName);

        lvName.setText(tasks.get(position));

        return rowView;
    }
}
