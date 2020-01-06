package com.example.myimagedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ImagesActivity extends AppCompatActivity
{

    private RecyclerView recyclerView;
    private ImagesAdapter imagesAdapter;

    private DatabaseReference databaseReference;
    private List<UploadFile> uploads;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        recyclerView = findViewById(R.id.recyclerView_Images);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploads= new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)                            //dataSnapShot is a list of items in database
            {
                for(DataSnapshot imageItems : dataSnapshot.getChildren())
                {
                    UploadFile upload = imageItems.getValue(UploadFile.class);
                    uploads.add(upload);
                }

                imagesAdapter = new ImagesAdapter(ImagesActivity.this, uploads);
                recyclerView.setAdapter(imagesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(ImagesActivity.this, databaseError.getMessage().toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });




    }
}
