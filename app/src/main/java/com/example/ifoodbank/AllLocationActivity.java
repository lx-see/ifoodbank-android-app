package com.example.ifoodbank;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;

public class AllLocationActivity extends AppCompatActivity {
    private static final String TAG = "LocMainActivity";
    private FirestoreUtils utils = new FirestoreUtils();

    //vars
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mAddress = new ArrayList<>();
    private ArrayList<String> mContact = new ArrayList<>();
    private ArrayList<String> mDesc = new ArrayList<>();
    private ArrayList<Long> mFoodQuantities = new ArrayList<>();
    private ArrayList<String> mEmails = new ArrayList<>();
    private ArrayList<Boolean> mHalals = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initImageBitmaps();

        // Navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(AllLocationActivity.this,MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_calendar:
                        intent = new Intent(AllLocationActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_profile:
                        intent = new Intent(AllLocationActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }

    private void initImageBitmaps(){
        // Requests foodbank data from firebase
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

                                mImageUrls.add(imgPath);
                                mNames.add(foodbankName);
                                mAddress.add(location);
                                mContact.add(contactNum);
                                mDesc.add(foodDesc);
                                mFoodQuantities.add(foodQuantity);
                                mEmails.add(email);
                                mHalals.add(halal);

                                if (mNames.size()==12){
                                    initRecyclerView();
                                }
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LocRecyclerViewAdapter adapter = new LocRecyclerViewAdapter(this, mNames, mAddress, mImageUrls, mFoodQuantities);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

class LocRecyclerViewAdapter extends RecyclerView.Adapter<LocRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mAddress = new ArrayList<>();
    private ArrayList<Long> mQuantity = new ArrayList<>();
    private Context mContext;

    public LocRecyclerViewAdapter(Context context, ArrayList<String> imageNames, ArrayList<String> imageAddress, ArrayList<String> images, ArrayList<Long> foodQuantities ) {
        mImageNames = imageNames;
        mAddress = imageAddress;
        mImages = images;
        mQuantity = foodQuantities;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_location, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);

        holder.imageName.setText(mImageNames.get(position));
        holder.address.setText(mAddress.get(position));
        holder.foodQuantity.setText(""+mQuantity.get(position)+"pax");

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Selected food bank: " + mImageNames.get(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, GalleryActivity.class);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView imageName;
        TextView address;
        TextView foodQuantity;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.foodbank_name);
            address = itemView.findViewById(R.id.foodbank_location);
            foodQuantity = itemView.findViewById(R.id.food_quantity);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
