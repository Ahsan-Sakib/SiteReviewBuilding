package com.example.buildingconstraction.is229443.User;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buildingconstraction.R;
import com.example.buildingconstraction.is229443.Admin.SiteListAdapterForBothUserAndAdmin;
import com.example.buildingconstraction.is229443.Model.PostModel;
import com.example.buildingconstraction.is229443.contants.AppContants;
import com.example.buildingconstraction.is229443.sharedPreference.AppSharedPref;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robertlevonyan.views.customfloatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class SiteListUserActivity extends AppCompatActivity {

    private FloatingActionButton btnaddsite;
    private SiteListAdapterForBothUserAndAdmin siteListAdapter;
    private RecyclerView recyclerView;
    private ArrayList<PostModel> postModels;
    private static final String TAG = "SiteListUserActivity";
    private String UserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list_user);
        Intent intent = getIntent();
        UserName = intent.getStringExtra(AppContants.UserName);
        if(!TextUtils.isEmpty(UserName)){
            SharedPreferences sharedPreferences = AppSharedPref.getSharedPreferences();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(AppContants.UserName,UserName);
            editor.apply();
        }
        btnaddsite = findViewById(R.id.btn_add_site);
        recyclerView = findViewById(R.id.recyclerViewFroUserPost);
        postModels = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        fetchData();
        siteListAdapter = new SiteListAdapterForBothUserAndAdmin(postModels,this,1);
        recyclerView.setAdapter(siteListAdapter);
        siteListAdapter.notifyDataSetChanged();
        btnaddsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddSiteUser.class);
                startActivity(intent);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        postModels.clear();
        fetchData();
        siteListAdapter.notifyDataSetChanged();
    }

    private void fetchData() {
        if(!UserName.isEmpty()){
            UserName = AppSharedPref.getSharedPreferences().getString(AppContants.UserName,null);
        }
        final DatabaseReference posts = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("posts")
                .child(UserName);

        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postModels.clear();
                    for(DataSnapshot dataOfPost : dataSnapshot.getChildren()){
                        PostModel post = dataOfPost.getValue(PostModel.class);
                        Log.i(TAG, "onDataChange: "+post.getTitle());
                        postModels.add(post);
                    }
                siteListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
