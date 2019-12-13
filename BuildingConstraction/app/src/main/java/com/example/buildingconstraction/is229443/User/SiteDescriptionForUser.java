package com.example.buildingconstraction.is229443.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.buildingconstraction.R;
import com.example.buildingconstraction.is229443.contants.AppContants;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SiteDescriptionForUser extends AppCompatActivity {

    private TextView tvUserSiteTitle,tvUserSiteDescription,tvUserSiteDate,tvUserSiteApproval,tvUserSitePostedBy;
    private Button Btn_editPost;
    private ImageView ivUserSiteImage;
    private static final String TAG = "UserSiteDescription";
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String title,description,postedBy,PostDate,imageUrlLink,key,comment;
    boolean isApproved;
    private TextView tvUserRejectionComment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_description_for_user);

        tvUserSiteTitle = findViewById(R.id.tv_user_site_title);
        tvUserSiteDescription = findViewById(R.id.tv_user_site_description);
        tvUserSiteDate = findViewById(R.id.tv_user_site_date);
        tvUserSitePostedBy = findViewById(R.id.tv_user_site_posted_by);
        tvUserSiteApproval = findViewById(R.id.tv_user_site_approval);
        ivUserSiteImage = findViewById(R.id.iv_user_site_image);
        tvUserRejectionComment = findViewById(R.id.tv_user_site_rejectMessage);
        Btn_editPost = findViewById(R.id.btn_user_editpost);
        tvUserSiteApproval.setGravity(Gravity.RIGHT);
        tvUserSiteDate.setGravity(Gravity.LEFT);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        Intent intent = getIntent();
        title = intent.getStringExtra(AppContants.Title);
        description  = intent.getStringExtra(AppContants.description);
        postedBy =  intent.getStringExtra(AppContants.PostedBy);
        PostDate =  intent.getStringExtra(AppContants.PostDate);
        imageUrlLink =  intent.getStringExtra(AppContants.imageUriLink);
        isApproved = intent.getBooleanExtra(AppContants.Approval,false);
        key = intent.getStringExtra(AppContants.Key);
        comment = intent.getStringExtra(AppContants.Comment);


        tvUserSiteTitle.setText("Title : " + title);
        tvUserSiteDate.setText("Post Date : "+PostDate);
        if(isApproved) {
            tvUserSiteApproval.setText("Approved");
        }
        else{
            tvUserSiteApproval.setText("Not Approved");
        }

        tvUserSitePostedBy.setText("Posted By  : " + postedBy);
        tvUserSiteDescription.setText("Description : "  + description);

        if(!TextUtils.isEmpty(comment)){
            tvUserRejectionComment.setText("Rejection comment : " + comment);
        }


        Glide.with(this)
                .load(imageUrlLink)
                .into(ivUserSiteImage);





        Btn_editPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddSiteUser.class);
                intent.putExtra(AppContants.Title,title);
                intent.putExtra(AppContants.description,description);
                intent.putExtra(AppContants.PostedBy,postedBy);
                intent.putExtra(AppContants.PostDate,PostDate);
                intent.putExtra(AppContants.imageUriLink,imageUrlLink);
                intent.putExtra(AppContants.Approval,isApproved);
                intent.putExtra(AppContants.Key,key);
                startActivity(intent);
            }
        });



    }
}
