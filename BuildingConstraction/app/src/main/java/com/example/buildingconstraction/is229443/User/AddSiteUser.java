package com.example.buildingconstraction.is229443.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.buildingconstraction.R;
import com.example.buildingconstraction.is229443.Model.PostModel;
import com.example.buildingconstraction.is229443.contants.AppContants;
import com.example.buildingconstraction.is229443.sharedPreference.AppSharedPref;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;


public class AddSiteUser extends AppCompatActivity {

    private ImageView imageViewConentImage;
    private EditText et_title;
    private EditText et_description;
    private Button btnSubmit;
    private ImageButton ibtn_cam,ibtn_gal;
    private int REQUEST_IMAGE_CAPTURE = 101;
    private int PICK_IMAGE = 102;
    private static final String TAG = "AddSiteUser";
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    AlertDialog alertDialog;
    private UploadTask uploadTask;
    private StorageReference reference;
    private String title,description,postedBy,PostDate,imageUrlLink,key,comment;
    boolean isApproved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);
        imageViewConentImage = findViewById(R.id.iv_container_of_post_image);
        et_title = findViewById(R.id.et_post_title);
        et_description = findViewById(R.id.et_description);
        btnSubmit = findViewById(R.id.btnsubmit);
        ibtn_cam = findViewById(R.id.ibtn_takePicture);
        ibtn_gal = findViewById(R.id.ibtn_pickimage);

        Intent intent = getIntent();
        title = intent.getStringExtra(AppContants.Title);
        if(!TextUtils.isEmpty(title)) {
            description = intent.getStringExtra(AppContants.description);
            postedBy = intent.getStringExtra(AppContants.PostedBy);
            PostDate = intent.getStringExtra(AppContants.PostDate);
            imageUrlLink = intent.getStringExtra(AppContants.imageUriLink);
            isApproved = intent.getBooleanExtra(AppContants.Approval, false);
            key = intent.getStringExtra(AppContants.Key);

            Glide.with(this)
                    .load(imageUrlLink)
                    .into(imageViewConentImage);
            et_title.setText(title);
            et_description.setText(description);
            btnSubmit.setText("Update");

        }


        firebaseStorage = FirebaseStorage.getInstance();

        alertDialog =new  AlertDialog.Builder(this)
                .setTitle("Please wait")
                .setMessage("We Are working on it")
                .setCancelable(false)
                .create();
        storageReference = firebaseStorage.getReference();

        ibtn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureAnImage();
            }
        });

        ibtn_gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickAnImageFromGallery();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
    }

    private void submitData() {
        alertDialog.show();
        uploadImage();
    }

    private void pickAnImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void captureAnImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            assert data!=null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewConentImage.setImageBitmap(imageBitmap);
        }else if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            Log.i(TAG, "onActivityResult: "+"imagePicked");
            if(data!=null) {
                Uri uri = data.getData();
                assert uri != null;
                Log.i(TAG, "onActivityResult: "+uri.toString());
                imageViewConentImage.setImageURI(uri);
            }else {
                Log.i(TAG, "onActivityResult: data is null");
            }
        }

    }


    private void uploadImage() {
        Bitmap bitmap = ((BitmapDrawable) imageViewConentImage.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        int random = new Random().nextInt(1000);
        reference = storageReference.child("Mustofa_"+random+"_Mahmud.png");
        uploadTask = reference.putBytes(data);
        if(uploadTask!=null){
            Log.i(TAG, "getDownLoadUrl: upwork not null");
        }
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.i(TAG, "then: not successful");
                    alertDialog.dismiss();
                    Log.i(TAG, "then: "+task.getException().toString());
                    throw Objects.requireNonNull(task.getException());
                }

                // Continue with the task to get the download URL
                Task<Uri> downloadUrl = reference.getDownloadUrl();
                Log.i(TAG, "then: "+downloadUrl.toString());
                return downloadUrl;
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String userName = AppSharedPref.getSharedPreferences().getString(AppContants.UserName,null);
                    //
                    Log.i(TAG, "onComplete: result "+downloadUri.toString());
                    String title = et_title.getText().toString().trim();
                    String description = et_description.getText().toString().trim();
                    String downloadUrl = downloadUri.toString().trim();
                    String postedBy = userName;
                    String Date = getDate();
                    PostModel postModel = new PostModel(title,downloadUrl,description,false,Date,postedBy);
                    if(!btnSubmit.getText().toString().trim().equals("Update")) {
                         key = FirebaseDatabase.getInstance()
                                .getReference()
                                .child("posts")
                                .child(userName)
                                .push().getKey();
                    }

                    postModel.setPostKey(key);
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("posts")
                            .child(userName)
                            .child(key)
                            .setValue(postModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i(TAG, "onSuccess: "+"Uploaded");
                                    alertDialog.dismiss();
                                    AddSiteUser.super.onBackPressed();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: failed to upload " +e.toString());
                            alertDialog.dismiss();
                        }
                    });

                    Log.i(TAG, "onComplete: "+downloadUri);
                } else {
                    alertDialog.dismiss();
                    Log.i(TAG, "onComplete: error in here");
                }
            }
        });

    }

    public String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

}
