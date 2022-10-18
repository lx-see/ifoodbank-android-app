package com.example.ifoodbank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity{
    private static final String TAG = "GalleryActivity";
    private FirestoreUtils utils = new FirestoreUtils();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        int position = getIntent().getIntExtra("position", 0);

        //vars
        ArrayList<String> mNames = new ArrayList<>();

        FirebaseFirestore db = utils.getDb();
        db.collection("Foodbank")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String contactNum = (String) document.getData().get("contactNum");
                                String email = (String) document.getData().get("email");
                                String foodDesc = (String) document.getData().get("foodDesc");
                                long foodQuantity = (long) document.getData().get("foodQuantity");
                                Boolean halal = (Boolean) document.getData().get("halal");
                                String imgPath = (String) document.getData().get("imgPath");
                                String location = (String) document.getData().get("location");
                                String foodbankName = (String) document.getData().get("storeName");

                                mNames.add(foodbankName);

                                if (mNames.size()-1==position){
                                    setInformation(imgPath, foodbankName, location, contactNum, foodDesc, foodQuantity, email, halal);
                                    Button btn_go_google_maps = findViewById(R.id.btn_go_google_maps);
                                    btn_go_google_maps.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(GalleryActivity.this,MapsActivity.class);
                                            intent.putExtra("location", location);
                                            intent.putExtra("foodbankName", foodbankName);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void setInformation(String imgPath, String foodbankName, String location, String contactNum, String foodDesc, Long foodQuantity, String email, Boolean halal){

        String isHalal = "";
        if (halal){
            isHalal = "HALAL";
        }

        ImageView image = findViewById(R.id.image);
        Glide.with(this)
                .asBitmap()
                .load(imgPath)
                .into(image);

        TextView fb_name = findViewById(R.id.name);
        TextView fb_desc = findViewById(R.id.desc);
        TextView fb_halal = findViewById(R.id.halal);
        TextView fb_contact = findViewById(R.id.contact);
        TextView fb_email = findViewById(R.id.email);
        TextView fb_address = findViewById(R.id.address);

        fb_name.setText(foodbankName);
        fb_desc.setText(foodDesc);
        fb_halal.setText(""+isHalal+" / "+foodQuantity+" Pax Available");
        fb_contact.setText(contactNum);
        fb_email.setText(email);
        fb_address.setText(location);

    }

}
