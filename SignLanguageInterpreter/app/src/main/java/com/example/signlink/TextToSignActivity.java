package com.example.signlink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.signlink.databinding.ActivityMainBinding;
import com.example.signlink.databinding.ActivityTextToSignBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;


public class TextToSignActivity extends AppCompatActivity {

    ActivityTextToSignBinding binding;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTextToSignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(TextToSignActivity.this);
                progressDialog.setMessage("Fetching Image.......");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String imageID = binding.edittexttts.getText().toString();
                storageReference = FirebaseStorage.getInstance().getReference("images/"+imageID+".png");

                try {
                    File localFile = File.createTempFile("tempfile",".png");
                    storageReference.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                    binding.imageviewtts.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    if(progressDialog.isShowing())
                                        progressDialog.dismiss();

                                    Toast.makeText(TextToSignActivity.this, "Failed To Retrieve", Toast.LENGTH_SHORT).show();
                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}