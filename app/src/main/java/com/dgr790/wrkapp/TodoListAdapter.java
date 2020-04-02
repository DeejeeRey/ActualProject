package com.dgr790.wrkapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TodoListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> tasks;
    private boolean thisTime;

    public TodoListAdapter(Activity context, ArrayList<String> tasks) {
        super(context, R.layout.todo_item, tasks);

        this.context = context;
        this.tasks = tasks;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.todo_item, null, true);

        TextView lvName = (TextView) rowView.findViewById(R.id.lvName);
        Button lvButton = (Button) rowView.findViewById(R.id.lvButton);

        lvName.setText(tasks.get(position));
        lvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasks.remove(position);
                notifyDataSetChanged();
                updateDB(position);
            }
        });

        return rowView;
    }

    private void updateDB(final int position) {
        thisTime = true;

        final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference currentDB = FirebaseDatabase.getInstance().getReference().child("Tasks");


        currentDB.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("yes");
                HashMap<String, Integer> userHashMap = (HashMap<String, Integer>) dataSnapshot.getValue();


                if (thisTime) {
                    thisTime = false;
                    int i = 0;
                    for (String key : userHashMap.keySet()) {
                        if (i == position) {
                            userHashMap.remove(key);
                        }
                        i++;
                    }

                    currentDB.child(id).setValue(userHashMap);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
