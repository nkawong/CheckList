package com.example.checklist;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checklist.Adapter.task_list_adapter;
import com.example.checklist.models.item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class task_item_list_activity extends AppCompatActivity implements addItemDialog.OnEditTextSelected, task_list_adapter.OnItemClickListener {

    //CheckBox Variable
    CheckBox checkBox;
    //String from addItemDialog
    private String input;

    //TitleNameId of list
    private String titleId;

    //Custom ToolBar
    private Toolbar mToolBar;

    //Recycler View
    private RecyclerView taskList;
    RecyclerView.Adapter mAdapter;

    //ArrayList Tasks
    ArrayList<item> tasks;

    //Firebase Database
    DatabaseReference items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_item);

        //toolbar
        mToolBar = findViewById(R.id.myToolBar);
        setSupportActionBar(mToolBar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RecyclerView Initialization
        taskList = (RecyclerView) findViewById(R.id.tasks_list);

        //ArrayList Initialization
        tasks = new ArrayList<>();

        //Intent getExtra
        Intent intent = getIntent();

        //titleId Initialization
        titleId = intent.getStringExtra(listview_layout.titleNameId);

        //Database Initialization
         items = FirebaseDatabase.getInstance().getReference("Items").child(titleId);

         //Retrieve data from Firebase Database
        retrieveItems();


        //Checkbox
        //checkBox = (CheckBox) findViewById(R.id.checkBox);

        //ItemTouchHelper Variable
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(taskList);


    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }


        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAbsoluteAdapterPosition();
            if(direction == ItemTouchHelper.LEFT){
                items.child(tasks.get(position).getId()).removeValue();
                mAdapter.notifyItemRemoved(position);

            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(task_item_list_activity.this,R.color.trashBackground))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.task_list_menu, menu);
        return true;
    }

    private void retrieveItems() {
        items.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tasks.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                     item itemName = ds.getValue(item.class);
                     //Log.d("Task item list", "Title Name of Item:  " + itemName.getItem_name());
                     tasks.add(itemName);
                 }
                mAdapter = new task_list_adapter(task_item_list_activity.this, tasks,task_item_list_activity.this);
                taskList.setAdapter(mAdapter);
                taskList.setLayoutManager(new LinearLayoutManager(task_item_list_activity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("listview_layout", "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                //Toast.makeText(this, "It works!!!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_addTask:
                add_Item_Dialog();
                //Toast.makeText(this, "Button works", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void add_Item_Dialog() {
        addItemDialog addItemDialog = new addItemDialog();
        addItemDialog.show(getSupportFragmentManager(),"");
    }

    @Override
    public void sendEditText(String editTextAdd) {
        input = editTextAdd;
        addItem(input);
    }

    private void addItem(String input) {
        String id = items.push().getKey();

        item mItem = new item(input, id);

        items.child(id).setValue(mItem);

        Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(int position) {
        item mItems = tasks.get(position);
        items.child(mItems.getId()).child("completed").setValue(!mItems.isCompleted());
        //Toast.makeText(this, "Item Completed", Toast.LENGTH_SHORT).show();
    }
}
