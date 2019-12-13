package com.example.buildingconstraction.is229443.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buildingconstraction.R;
import com.example.buildingconstraction.is229443.Model.PostModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SiteListAdminActivity extends AppCompatActivity {
    private SiteListAdapterForBothUserAndAdmin siteListAdapter;
    private RecyclerView recyclerView;
    private ArrayList<PostModel> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list_for_admin);
        arrayList = new ArrayList<>();
        fetchData();
        recyclerView = findViewById(R.id.rv_site);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        siteListAdapter = new SiteListAdapterForBothUserAndAdmin(arrayList,this,0);
        recyclerView.setAdapter(siteListAdapter);
        siteListAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        arrayList.clear();
        fetchData();
        siteListAdapter.notifyDataSetChanged();
    }

    private void fetchData() {
        DatabaseReference posts = FirebaseDatabase.getInstance().getReference().child("posts");
        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot dataOfPostedBy : dataSnapshot.getChildren()){
                    for(DataSnapshot dataOfPost : dataOfPostedBy.getChildren()){
                        PostModel post = dataOfPost.getValue(PostModel.class);
                        arrayList.add(post);
                    }
                }
                siteListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
