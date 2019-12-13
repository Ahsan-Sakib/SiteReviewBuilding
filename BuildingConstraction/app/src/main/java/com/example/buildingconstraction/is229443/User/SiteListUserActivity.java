package com.example.buildingconstraction.is229443.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buildingconstraction.R;


public class SiteListUserActivity extends AppCompatActivity {

    private Button btnaddsite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list_user);
        btnaddsite = findViewById(R.id.btn_add_site);
        btnaddsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SiteDescriptionUser.class);
                startActivity(intent);
            }
        });
    }
}
