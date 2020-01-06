package com.example.myimagedb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
{
    private static final int IMAGE_PIC_REQUEST = 1;

    private EditText etFileName;
    private Button btnChooseImage;
    private ImageView ivImage;
    private ProgressBar pbUploadProgress;
    private Button btnUploadImage;
    private Button btnShowUploads;
    private Uri imageUri;

    private StorageReference storageRef;
    private DatabaseReference dataBaseRef;

    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFileName = findViewById(R.id.editText_Image_Name);
        btnChooseImage = findViewById(R.id.button_Choose_Image);
        ivImage = findViewById(R.id.imageView_Image);
        pbUploadProgress = findViewById(R.id.progressBar_Upload_Progress);
        btnUploadImage = findViewById(R.id.button_Upload_Image);
        btnShowUploads = findViewById(R.id.button_Show_Upload);

        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        dataBaseRef = FirebaseDatabase.getInstance().getReference("uploads");


        btnChooseImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openFileChooser();
            }
        });

        btnUploadImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(uploadTask != null)
                    Toast.makeText(MainActivity.this, "Image upload is in progress...", Toast.LENGTH_SHORT).show();
                else
                    uploadFile();

            }
        });


        btnShowUploads.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openImageActivity();
            }
        });


    }



    private void openFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_PIC_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_PIC_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageUri = data.getData();

            Picasso.with(this).load(imageUri).into(ivImage);

        }
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile()
    {
        if(imageUri != null)
        {
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));      //uploaded file

            uploadTask = fileReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                        {

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable()                                      // delay the  resetting of progress bar to zero by 5 second
                            {
                                @Override
                                public void run()
                                {
                                    pbUploadProgress.setProgress(0);
                                }
                            }, 500);

                            Toast.makeText(MainActivity.this, "Image is uploaded successfully.", Toast.LENGTH_LONG).show();
                            UploadFile uploadFile = new UploadFile(etFileName.getText().toString().trim(), fileReference.getDownloadUrl().toString());      // we are making object of our uploaded image to store it into storage
                            String uploadId = dataBaseRef.push().getKey();             //make an entry with unique id in database
                            dataBaseRef.child(uploadId).setValue(uploadFile);          //we store uploaded image to chunk of memory with above id

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                        {

                            double progress = 100 * (taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            pbUploadProgress.setProgress((int) progress);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "No file is selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void openImageActivity()
    {
        Intent intent = new Intent(this, com.example.myimagedb.ImagesActivity.class);
        startActivity(intent);
    }
}
