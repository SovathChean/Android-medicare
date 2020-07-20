package com.example.mankind;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class setupActivity extends AppCompatActivity implements View.OnClickListener{
    private CircleImageView setUpImage;
    private Button upload;
    private Uri imageUri;
    private EditText yourName;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private FirebaseFirestore firestore;
    private String user_id;
    private String download_uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);
        setUpImage = findViewById(R.id.profile);
        upload = findViewById(R.id.upload);
        yourName = findViewById(R.id.user_name);
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();


        setUpImage.setOnClickListener(this);
        upload.setOnClickListener(this);
        user_id = auth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        upload.setEnabled(false);
        firestore.collection("Users").document(user_id).get().addOnCompleteListener(this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                     if(task.getResult().exists())
                     {
                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                         Toast.makeText(setupActivity.this, image, Toast.LENGTH_LONG).show();

                         yourName.setText(name);
                         RequestOptions requestOptions = new RequestOptions();
                         requestOptions.placeholder(R.drawable.default_profile);

                         Glide.with(setupActivity.this).setDefaultRequestOptions(requestOptions).load(image).into(setUpImage);
                     }
                     else
                     {
                         Toast.makeText(setupActivity.this, "Data doesn't exist", Toast.LENGTH_LONG).show();
                     }
                     progressBar.setVisibility(View.INVISIBLE);
                     upload.setEnabled(true);
                }
                else{
                    String error = task.getException().getMessage();
                    Toast.makeText(setupActivity.this, "Firestore Error"+error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void showFileChooser(){
      if(ContextCompat.checkSelfPermission(setupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
          Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
          ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
      }
      else{
          CropImage.activity()
                  .setGuidelines(CropImageView.Guidelines.ON)
                  .setAspectRatio(1, 1)
                  .start(this);
      }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                setUpImage.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
    public void uploadProfile(){
         final String name = yourName.getText().toString().trim();


         if(!TextUtils.isEmpty(name) && imageUri != null){

             user_id = auth.getCurrentUser().getUid();
             final StorageReference img_path = storageReference.child("profile_image").child(user_id + ".jpg");


             img_path.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                 @Override
                 public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                     img_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                         @Override
                         public void onSuccess(Uri uri) {
                            download_uri = uri.toString();
                         }
                     });
                    if(task.isSuccessful())
                    {

                        Map<String, String> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("image", download_uri);


                      firestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(setupActivity.this, new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if(task.isSuccessful()){
                                  Toast.makeText(setupActivity.this, "set account setting successfully", Toast.LENGTH_LONG).show();
                                  Intent intent = new Intent(setupActivity.this, MainActivity.class);
                                  startActivity(intent);
                                  finish();
                              }
                              else{
                                  String error = task.getException().getMessage();
                                  Toast.makeText(setupActivity.this, "Error"+error, Toast.LENGTH_LONG).show();
                              }

                           progressBar.setVisibility(View.INVISIBLE);
                          }
                      });
                    }
                    else{
                        String error = task.getException().getMessage();
                        Toast.makeText(setupActivity.this, "Error"+error, Toast.LENGTH_LONG).show();
                    }
                 }
             });
         }
    }

    @Override
    public void onClick(View view) {
      if(view == setUpImage)
      {
          showFileChooser();
      }
      else if (view == upload){
          uploadProfile();
      }
    }
}
