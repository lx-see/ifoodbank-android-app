package com.example.ifoodbank;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.*;

public class ApplyFoodActivity extends AppCompatActivity {
    String foodbankName = "";
    String foodbankDesc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_form);

        TextView apply_foodbank_name = findViewById(R.id.apply_foodbank_name);
        TextView apply_foodbank_desc = findViewById(R.id.apply_foodbank_desc);

        EditText full_name = findViewById(R.id.full_name);
        EditText phone_no = findViewById(R.id.phone_no);
        EditText email = findViewById(R.id.email);

        SharedPreferences sharedPreference = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
        String emailTxt = sharedPreference.getString("email", "");
        email.setText(emailTxt);

        EditText address = findViewById(R.id.address);

        Intent intent = getIntent();
        if (intent != null){
            foodbankName = getIntent().getStringExtra("foodbankName");
            foodbankDesc = getIntent().getStringExtra("foodbankDesc");
        }

        apply_foodbank_name.setText(foodbankName);
        apply_foodbank_desc.setText(foodbankDesc);

        SQLiteAdapter mySQLiteAdapter;
        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
        String emailString = sharedPreferences.getString("email", "");
        ArrayList<String> content = mySQLiteAdapter.queryRow(emailString);
        mySQLiteAdapter.close();
        String oldFullName, oldPhoneNo, oldAddress = "";

        if (content.size() != 0){
            oldFullName = content.get(0);
            oldPhoneNo = content.get(1);
            oldAddress = content.get(3);

            full_name.setText(oldFullName);
            phone_no.setText(oldPhoneNo);
            email.setText(emailString);
            address.setText(oldAddress);
        }

        // to submit application form
        Button submitFoodApplicationForm = findViewById(R.id.btn_apply_confirm);

        submitFoodApplicationForm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // get inserted data from UI EditText

                String apply_full_name = full_name.getText().toString();
                String apply_phone_no = phone_no.getText().toString();
                String apply_email = email.getText().toString();
                String apply_address = address.getText().toString();

                // if required columns are not inputted
                if(apply_full_name.equals("") || apply_phone_no.equals("") ||
                        apply_email.equals("") || apply_address.equals("")){
                    // show message reminding user the columns must be filled
                    Toast.makeText(ApplyFoodActivity.this, "Cannot be empty!",
                            Toast.LENGTH_SHORT).show();
                }

                else{
                    // add application form details to firebase
                    FirestoreUtils firebase = new FirestoreUtils();
                    Map<String, Object> document = new HashMap<>();
                    document.put("address", apply_address);
                    document.put("email", apply_email);
                    document.put("foodbank_name", foodbankName);
                    document.put("full_name", apply_full_name);
                    document.put("phone_no", apply_phone_no);

                    firebase.addDocument("ApplicationData", document);
                }

                Toast.makeText(ApplyFoodActivity.this, "Your application had submitted!",
                        Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("applicationInfo",MODE_PRIVATE);
                SharedPreferences.Editor edits = sharedPreferences.edit();

                String foodbankNameList = sharedPreferences.getString("foodbankNameList", "");
                String applicationTimeList = sharedPreferences.getString("applicationTimeList", "");

                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");
                Date date = new Date();

                edits.putString("foodbankNameList", foodbankNameList+","+foodbankName);
                edits.putString("applicationTimeList", applicationTimeList+","+sdf.format(date));

                edits.commit();

                ApplyFoodActivity.this.finish();
            }
        });

        // Navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(ApplyFoodActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        intent = new Intent(ApplyFoodActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(ApplyFoodActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }
}
