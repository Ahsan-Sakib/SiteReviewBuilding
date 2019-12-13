package com.example.buildingconstraction.is229443.Admin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.example.buildingconstraction.R;
import com.example.buildingconstraction.is229443.contants.AppContants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


public class AdminSiteDescription extends AppCompatActivity {

    private TextView tvAdminSiteTitle,tvAdminSiteDescription,tvAdminSiteDate,tvAdminSiteApproval,tvAdminSitePostedBy;
    private Button btnAdminSiteAccept,btnAdminSiteReject,btnAdminSiteDownloadImage;
    private ImageView ivAdminSiteImage;
    private static final String TAG = "AdminSiteDescription";
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private AlertDialog alertDialog;
    private AlertDialog alertDialogForRejectComment;
    private String title,description,postedBy,PostDate,imageUrlLink,key;
    boolean isApproved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_description_for_admin);
        tvAdminSiteTitle = findViewById(R.id.tv_admin_site_title);
        tvAdminSiteDescription = findViewById(R.id.tv_admin_site_description);
        tvAdminSiteDate = findViewById(R.id.tv_admin_site_date);
        tvAdminSitePostedBy = findViewById(R.id.tv_admin_site_posted_by);
        tvAdminSiteApproval = findViewById(R.id.tv_admin_site_approval);
        ivAdminSiteImage = findViewById(R.id.iv_admin_site_image);
        btnAdminSiteReject = findViewById(R.id.btn_admin_site_reject);
        btnAdminSiteAccept = findViewById(R.id.btn_admin_site_accept);
        btnAdminSiteDownloadImage = findViewById(R.id.btn_admin_download_image);
        tvAdminSiteApproval.setGravity(Gravity.RIGHT);
        tvAdminSiteDate.setGravity(Gravity.LEFT);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        alertDialog =new  AlertDialog.Builder(this)
                .setTitle("Please wait")
                .setMessage("Download on Progress")
                .setCancelable(false)
                .create();

        final EditText input = new EditText(AdminSiteDescription.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        alertDialogForRejectComment = new AlertDialog.Builder(this)
                .setView(input)
                .setTitle("Write Rejection Commnet")
                .setPositiveButton("Comment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rejectPost(input.getText().toString().trim());
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();


        Intent intent = getIntent();
        title = intent.getStringExtra(AppContants.Title);
        description  = intent.getStringExtra(AppContants.description);
        postedBy =  intent.getStringExtra(AppContants.PostedBy);
        PostDate =  intent.getStringExtra(AppContants.PostDate);
        imageUrlLink =  intent.getStringExtra(AppContants.imageUriLink);
        isApproved = intent.getBooleanExtra(AppContants.Approval,false);
        key = intent.getStringExtra(AppContants.Key);
        Log.i(TAG, "onCreate: "+key);


        tvAdminSiteTitle.setText("Title : " + title);
        tvAdminSiteDate.setText("Post Date : "+PostDate);
        if(isApproved) {
            tvAdminSiteApproval.setText("Approved");
        }
        else{
            tvAdminSiteApproval.setText("Not Approved");
        }

        tvAdminSitePostedBy.setText("Posted By  : " + postedBy);
        tvAdminSiteDescription.setText("Description : "  + description);


        Glide.with(this)
                .load(imageUrlLink)
                .into(ivAdminSiteImage);

        btnAdminSiteAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("posts")
                        .child(postedBy)
                        .child(key)
                        .child("approval")
                        .setValue(true);
                tvAdminSiteApproval.setText("Approved");

                FirebaseDatabase.getInstance()
                        .getReference()
                        .child("posts")
                        .child(postedBy)
                        .child(key)
                        .child("rejectMessage")
                        .setValue(null);
                ;
            }
        });

        btnAdminSiteDownloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkYourPermission()) {
                    downloadImage(imageUrlLink);
                }
            }
        });

        btnAdminSiteReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alertDialogForRejectComment.show();
            }
        });

    }

    private void rejectPost(String rejectMEssage) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child("posts")
                .child(postedBy)
                .child(key)
                .child("approval")
                .setValue(false);

        FirebaseDatabase.getInstance()
                .getReference()
                .child("posts")
                .child(postedBy)
                .child(key)
                .child("rejectMessage")
                .setValue(rejectMEssage);

        tvAdminSiteApproval.setText("Not Approved");
    }

    private boolean checkYourPermission() {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(AdminSiteDescription.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},108);
            return false;
        }
        return true;
    }


    private void downloadImage(String imagePath) {
        alertDialog.show();
        if(createFolder()) {
            Log.i(TAG, "downloadImage: Started");
            Log.i(TAG, "downloadImage: download from " + imagePath);
            if (!TextUtils.isEmpty(imagePath)) {
                final long ONE_MEGABYTE = 1024 * 1024 * 10;
                firebaseStorage.getReferenceFromUrl(imagePath).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Log.i(TAG, "onSuccess: Complete download storeing is on going");
                        int rand = new Random().nextInt();

                        File file = new File(Environment.getExternalStorageDirectory()+"/BuildingConstuction", "photo_" + tvAdminSitePostedBy.getText().toString().trim() + rand + ".jpeg");
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(file.getPath());
                            fileOutputStream.write(bytes);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            Log.i(TAG, "onSuccess: storing complete");
                            Toast.makeText(getApplicationContext(), "Image Download Completed", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } catch (FileNotFoundException e) {
                            Log.i(TAG, "onSuccess: " + e.toString());
                            alertDialog.dismiss();
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.i(TAG, "onSuccess: " + e.toString());
                            e.printStackTrace();
                            alertDialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "onFailure: Failed to download");
                        alertDialog.dismiss();
                    }
                });
            } else {
                alertDialog.dismiss();
            }
        }else{
            Log.i(TAG, "downloadImage: folder not exits");
            alertDialog.dismiss();
        }
    }

    private boolean createFolder() {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir_fst = new File(root + "/BuildingConstuction");
        if(myDir_fst.exists()){
            return true;
        }else{
            return myDir_fst.mkdirs();
        }
    }
}
