package com.dgr790.wrkapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.List;

public class PlanFragment extends Fragment {

    private ListView lvList;
    private EditText addTaskET;
    private Button btnAddTask;

    private ArrayList<String> taskList;
    private ArrayList<String> taskListTemp;
    private HashMap<String, Integer> userHashMap;

    private TodoListAdapter adapter;

    private boolean firstTime;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_plan, container, false);

        firstTime = true;

        lvList = (ListView) v.findViewById(R.id.lvList);
        addTaskET = (EditText) v.findViewById(R.id.addTaskET);
        btnAddTask = (Button) v.findViewById(R.id.btnAddTask);

        taskList = new ArrayList<String>();
        taskListTemp = new ArrayList<String>();



        adapter = new TodoListAdapter(getActivity(), taskList);
        lvList.setAdapter(adapter);


        updateTaskList();

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstTime = false;
                String task = addTaskET.getText().toString();
                if (task.isEmpty()) {
                    showMessage("Enter a task");
                } else {
                    taskList.add(task);
                    adapter.notifyDataSetChanged();
                    updateDB(task);
                    addTaskET.setText("");
                    showMessage("Task added");
                }
            }
        });



        return v;
    }

    // Gets tasklist from database
    private void updateTaskList() {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference currentDB = FirebaseDatabase.getInstance().getReference().child("Tasks");


        currentDB.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userHashMap = (HashMap<String, Integer>) dataSnapshot.getValue();

                if ((!(userHashMap == null)) && firstTime) {

                    taskList.clear();
                    taskListTemp.clear();

                    for (String key : userHashMap.keySet()) {
                        taskListTemp.add(key);
                    }

                    for (String val : taskListTemp) {
                        taskList.add(val);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    // Puts new tasks into database using Hash Maps
    private void updateDB(String task) {
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference currentDB = FirebaseDatabase.getInstance().getReference().child("Tasks").child(id);

        HashMap taskMap = new HashMap();
        taskMap.put(task, 1);
        currentDB.updateChildren(taskMap);

        taskMap.clear();


    }

    private void showMessage(String m) {
        Toast.makeText(this.getContext(), m, Toast.LENGTH_SHORT).show();
    }
}
