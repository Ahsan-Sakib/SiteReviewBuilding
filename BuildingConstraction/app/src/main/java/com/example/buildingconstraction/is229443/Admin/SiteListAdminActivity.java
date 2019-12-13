package com.example.buildingconstraction.is229443.Admin;

import android.os.Bundle;
import android.provider.ContactsContract;

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
    private SiteListAdapter siteListAdapter;
    private RecyclerView recyclerView;
    private ArrayList<PostModel> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list2);
        arrayList = new ArrayList<>();
        arrayList = fetchData();
        recyclerView = findViewById(R.id.rv_site);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        siteListAdapter = new SiteListAdapter(arrayList,this);
        recyclerView.setAdapter(siteListAdapter);
        siteListAdapter.notifyDataSetChanged();
    }

    private ArrayList<PostModel> fetchData() {
        final ArrayList<PostModel> tempHolder = new ArrayList<>();
        DatabaseReference posts = FirebaseDatabase.getInstance().getReference().child("posts");
        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataOfPostedBy : dataSnapshot.getChildren()){
                    for(DataSnapshot dataOfPost : dataOfPostedBy.getChildren()){
                        PostModel post = dataOfPost.getValue(PostModel.class);
                        tempHolder.add(post);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return tempHolder;
    }
}
