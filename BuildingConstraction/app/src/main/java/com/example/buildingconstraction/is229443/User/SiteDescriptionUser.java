package com.example.buildingconstraction.is229443.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buildingconstraction.R;
import com.example.buildingconstraction.is229443.Model.PostModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Random;


public class SiteDescriptionUser extends AppCompatActivity {

    private ImageView imageViewConentImage;
    private EditText et_title;
    private EditText et_description;
    private Button btnSubmit;
    private ImageButton ibtn_cam,ibtn_gal;
    private int REQUEST_IMAGE_CAPTURE = 101;
    private int PICK_IMAGE = 102;
    private static final String TAG = "SiteDescriptionUser";
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_description_user);
        imageViewConentImage = findViewById(R.id.iv_container_of_post_image);
        et_title = findViewById(R.id.et_post_title);
        et_description = findViewById(R.id.et_description);
        btnSubmit = findViewById(R.id.btnsubmit);
        ibtn_cam = findViewById(R.id.ibtn_takePicture);
        ibtn_gal = findViewById(R.id.ibtn_pickimage);
        firebaseStorage = FirebaseStorage.getInstance();
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
        final UploadTask uploadTask = storageReference.child("Mustofa_mahud"+random+".jpeg").putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.i(TAG, "onFailure: "+exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i(TAG, "onSuccess: Uploaded successfully");
                getDownLoadUrl(uploadTask);
            }
        });
    }

    private void getDownLoadUrl(UploadTask uploadTask) {
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference.child("testImage.png").getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    String title = et_title.getText().toString().trim();
                    String description = et_description.getText().toString().trim();
                    String downloadUrl = downloadUri.toString().trim();
                    String postedBy = "Mustofa Mahmud";
                    String Date = "12/12/2019";
                    PostModel postModel = new PostModel(title,downloadUrl,description,false,Date,postedBy);
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("posts")
                            .child("Mustofa_mahmud")
                            .push()
                            .setValue(postModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i(TAG, "onSuccess: "+"Uploaded");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "onFailure: failed to upload " +e.toString());
                                }
                            });

                    Log.i(TAG, "onComplete: "+downloadUri);
                } else {
                    Log.i(TAG, "onComplete: error in here");
                }
            }
        });
    }

}
