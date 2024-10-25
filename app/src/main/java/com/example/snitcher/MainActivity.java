package com.example.snitcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.snitcher.Adapters.UserAdapter;
import com.example.snitcher.Model.UserModel;
import com.example.snitcher.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    ActivityMainBinding binding;
    Toolbar toolbar;
    FirebaseDatabase database;
    DatabaseReference reference;
    String uid;
    RecyclerView recyclerView;
    ArrayList<UserModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("reVRse"); // Sets "reVRse" as the title on the toolbar
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this,binding.drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.bringToFront();
        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =item.getItemId();
                if (id==R.id.myProfile)
                {
                    Intent intent = new Intent(MainActivity.this,UserProfile.class);
                    startActivity(intent);
                }
                if (id==R.id.vrcontent)
                {
                    Intent intent = new Intent(MainActivity.this,dashboard.class);
                    startActivity(intent);
                }
                    if (id==R.id.settings)
                {
                    Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                }   if (id==R.id.groupChat)
                {
                    Toast.makeText(MainActivity.this, "Group Chat", Toast.LENGTH_SHORT).show();
                }
                if (id==R.id.logOut)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Log Out");
                    builder.setMessage("Are You Sure You Want To Log Out?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            auth.signOut();
                            startActivity(new Intent(MainActivity.this,LogInAcitvity.class));
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }
                return true;
            }
        });
        binding.drawerLayout.closeDrawer(GravityCompat.START);

        recyclerView=findViewById(R.id.userRecyclerView);
        list = new ArrayList<UserModel>();
        auth= FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        reference=database.getReference("user");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (auth.getCurrentUser() != null) {
            uid = auth.getCurrentUser().getUid();
        }
        if (!uid.isEmpty())
        {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();
                    for (DataSnapshot snapshot1 :  snapshot.getChildren())
                    {
                        UserModel user = snapshot1.getValue(UserModel.class);
                        if(user != null && !user.getUserId().equals(uid)){
                            list.add(user);
                        }
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(),"Task Failed",Toast.LENGTH_SHORT).show();
                }
            });
        }
        recyclerView.setAdapter(new UserAdapter(MainActivity.this,list));
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
        super.onBackPressed();
        }
    }
}