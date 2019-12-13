package com.example.buildingconstraction.is229443;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buildingconstraction.R;
import com.example.buildingconstraction.is229443.Model.AdminModel;
import com.example.buildingconstraction.is229443.Model.UserModel;
import com.example.buildingconstraction.is229443.User.SiteListUserActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.WHITE;

public class ConstractionMonitorMainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button loginbt;
    private EditText idEt,passEt;
    private TextView roleadminTv,roleuserTv;
    int isroleselected = 0 ;
    Boolean isSuccess = false;

    private static final String TAG = "ConstractionMonitorMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraction_monitor_main);
        loginbt = findViewById(R.id.bt_login);
        idEt = findViewById(R.id.et_userId);
        passEt = findViewById(R.id.et_password);
        roleadminTv = findViewById(R.id.tv_admin);
        roleuserTv = findViewById(R.id.tv_user);

        loginbt.setOnClickListener(this);
        roleadminTv.setOnClickListener(this);
        roleuserTv.setOnClickListener(this);

    }

    public void getData(){
        String id = idEt.getText().toString();
        String password = passEt.getText().toString();
        if(isroleselected != 0  && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(password)){
            switch (isroleselected){
                case 1:
                    loginAsAdmin(id,password);
                    Log.d(TAG, "getData: "+id+" " +password+" " +isroleselected);
                    break;
                case 2:
                    loginAsUser(id,password);
                    Log.d(TAG, "getData: "+id+" " +password+" " +isroleselected);
                    break;
            }
        }
        else {
            Toast.makeText(this,"Give all Information",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bt_login:
                getData();
                break;

            case R.id.tv_admin:
                isroleselected = 1;
                roleadminTv.setBackgroundColor(BLUE);
                roleuserTv.setBackgroundColor(WHITE);
                break;

            case R.id.tv_user:
                roleuserTv.setBackgroundColor(BLUE);
                roleadminTv.setBackgroundColor(WHITE);
                isroleselected = 2;
                break;

        }
    }

    //Admin Login Authentication check
    private void loginAsAdmin(final String id, final String password){
        FirebaseDatabase  firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        DatabaseReference admin = reference.child("Admin");
        admin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    AdminModel obj = (AdminModel) data.getValue(AdminModel.class);
                    if(obj.getId().equals(id) && obj.getPassword().equals(password)){
                        Toast.makeText(getApplicationContext(),"Succees",Toast.LENGTH_SHORT).show();
                        //showSiteList();
                        isSuccess = true;
                        break;
                    }
                }
                if(isSuccess == false){
                    Toast.makeText(getApplicationContext(),"Wrong UserId Or Password",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "onCancelled: "+databaseError.toString());
            }
        });

    }

    //user login authentication check
    private void loginAsUser(final String id, final String password){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        DatabaseReference user = reference.child("engineer");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    UserModel objuser = data.getValue(UserModel.class);
                    if(objuser.getId().equals(id) && objuser.getPassword().equals(password)  ){
                        isSuccess = true;
                        showSiteUserList();
                    }
                }

                if(isSuccess == false){
                    Toast.makeText(getApplicationContext(),"False user Id",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void showSiteUserList(){
        Intent intent = new Intent(this, SiteListUserActivity.class);
        startActivity(intent);
    }
}
