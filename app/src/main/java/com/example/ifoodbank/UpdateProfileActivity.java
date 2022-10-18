package com.example.ifoodbank;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class UpdateProfileActivity extends AppCompatActivity {

    private SQLiteAdapter mySQLiteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToRead();

        TextView showName = (TextView) findViewById(R.id.show_full_name);
        TextView showEmail = (TextView) findViewById(R.id.show_email);
        TextInputEditText fullName = (TextInputEditText) findViewById(R.id.full_name);
        TextInputEditText phoneNo = (TextInputEditText) findViewById(R.id.phone_no);
        TextInputEditText email = (TextInputEditText) findViewById(R.id.email);
        TextInputEditText address = (TextInputEditText) findViewById(R.id.address);
        Button saveUpdate = (Button) findViewById(R.id.save_update_info_btn);

        SharedPreferences sharedPreferences = getSharedPreferences("authenticateInfo",MODE_PRIVATE);
        String emailString = sharedPreferences.getString("email", "");

        showEmail.setText(emailString);
        email.setText(emailString);

        ArrayList<String> content = mySQLiteAdapter.queryRow(emailString);

        mySQLiteAdapter.close();

        String oldFullName, oldPhoneNo, oldAddress = "";

        if (content.size() != 0){
            oldFullName = content.get(0);
            oldPhoneNo = content.get(1);
            oldAddress = content.get(3);

            showName.setText(oldFullName);
            fullName.setText(oldFullName);
            phoneNo.setText(oldPhoneNo);
            address.setText(oldAddress);
        }

        saveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String editedFullName = fullName.getText().toString();
                String editedPhoneNo = phoneNo.getText().toString();
                String editedEmail = email.getText().toString();
                String editedAddress = address.getText().toString();

                if (content.size() != 0){

                    mySQLiteAdapter.openToWrite();
                    mySQLiteAdapter.update(emailString, editedFullName, editedPhoneNo, editedEmail, editedAddress);
                    mySQLiteAdapter.close();
                }
                else {
                    mySQLiteAdapter.openToWrite();
                    System.out.println(editedFullName);
                    System.out.println(editedPhoneNo);
                    System.out.println(editedEmail);
                    System.out.println(editedAddress);
                    mySQLiteAdapter.insert(editedFullName, editedPhoneNo, editedEmail, editedAddress);
                    mySQLiteAdapter.close();
                }

                Toast.makeText(getApplicationContext(), "Profile Updated Successful.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                intent.putExtra("Email", editedEmail);
                startActivity(intent);
            }
        });



    }
}