package com.example.checklist;

import android.content.Intent;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;
import android.widget.Toast;

import com.example.checklist.Adapter.list_adapter;
import com.example.checklist.Login.login;
import com.example.checklist.models.Directory_List;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class listview_layout extends AppCompatActivity implements addListDialog.OnInputSelected, list_adapter.OnItemClickListener,NavigationView.OnNavigationItemSelectedListener{

    //Variables
    private String mEditTextTitleName;
    private ArrayList<Directory_List> list;
    private list_adapter mAdapter;
    private TextView drawerText;

    //Final Variables
    public final static String titleNameId = "TitleId";

    //Custom ToolBar
    private Toolbar mToolBar;

    //Drawer Layout
    DrawerLayout drawerLayout;

    //NavigationView
    NavigationView navView;

    ActionBarDrawerToggle actionBarDrawerToggle;

    //Recycle View
    private RecyclerView list_task;


    //Firebase Database
    DatabaseReference databaseTaskList;

    FirebaseAuth mAuth;
    FirebaseUser user;


    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.listview_layout);

        mAuth = FirebaseAuth.getInstance();
        user =  mAuth.getCurrentUser();

        //toolbar initialization
        mToolBar = findViewById(R.id.myToolBar);
        setSupportActionBar(mToolBar);

        //Drawer Layout
        drawerLayout=findViewById(R.id.drawer);

        drawerText = findViewById(R.id.textView);
        //NavigationView
        navView = findViewById(R.id.navigationview);


        //actionbardrawertoggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,mToolBar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        navView.setNavigationItemSelectedListener(this);
        navView.bringToFront();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(user == null){
            finish();
            startActivity(new Intent(this, login.class));
        }

        //RecycleView initialization
        list_task = (RecyclerView) findViewById(R.id.list_list_task);

        //ArrayList Initialization
        list = new ArrayList<>();

        //Firebase
        databaseTaskList = FirebaseDatabase.getInstance().getReference("Lists").child(user.getUid());

        //Retrieving data from firebase Database
        retrieveListNames();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(list_task);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_add:
                add_List_Dialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addListName() {

        String id = databaseTaskList.push().getKey();
        Log.d("ListViewLayout", "it hits here" + id);
        Directory_List listName = new Directory_List(id, mEditTextTitleName);

        databaseTaskList.child(id).setValue(listName);

        Toast.makeText(this, "List Added", Toast.LENGTH_SHORT).show();
    }
    private void retrieveListNames(){
        databaseTaskList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Directory_List mTitleName = ds.getValue(Directory_List.class);
                    list.add(mTitleName);
                }

                mAdapter = new list_adapter(listview_layout.this,list,listview_layout.this); //required type context providing ValueEventListener
                list_task.setAdapter(mAdapter);
                list_task.setLayoutManager(new LinearLayoutManager(listview_layout.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("listview_layout", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void add_List_Dialog() {
        addListDialog dialog = new addListDialog();
        dialog.show(getSupportFragmentManager(),"");

}

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAbsoluteAdapterPosition();
            Directory_List dl = list.get(position);
            if(direction == ItemTouchHelper.LEFT){
                DatabaseReference tasks= FirebaseDatabase.getInstance().getReference("Items").child(dl.getId());
                tasks.removeValue();
                databaseTaskList.child(list.get(position).getId()).removeValue();

                mAdapter.notifyItemRemoved(position);

            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(listview_layout.this,R.color.trashBackground))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    /*  Overriding Interface Methods */
    @Override
    public void sendInput(String input) {
        mEditTextTitleName = input;
        addListName();
    }

    @Override
    public void OnItemClick(int position, String id) {
        Intent intent = new Intent(this, task_item_list_activity.class);
        intent.putExtra(titleNameId, id);
        startActivity(intent);
        //Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch(item.getItemId()){
            case R.id.logout_action:
                mAuth.signOut();
                finish();
                startActivity(new Intent(this, login.class));
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }

    }
}
