package com.example.mankind;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class setupFragment extends Fragment implements View.OnClickListener {
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setup, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        progressBar = view.findViewById(R.id.progressBar);
        setUpImage = view.findViewById(R.id.profile);
        upload = view.findViewById(R.id.upload);
        yourName = view.findViewById(R.id.user_name);
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();


        setUpImage.setOnClickListener(this);
        upload.setOnClickListener(this);
        user_id = auth.getCurrentUser().getUid();
        progressBar.setVisibility(View.VISIBLE);
        upload.setEnabled(false);
        firestore.collection("Users").document(user_id).get().addOnCompleteListener(getActivity(), new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                        Toast.makeText(getContext(), image, Toast.LENGTH_LONG).show();

                        yourName.setText(name);
                        RequestOptions requestOptions = new RequestOptions();
                        requestOptions.placeholder(R.drawable.default_profile);

                        Glide.with(getContext()).setDefaultRequestOptions(requestOptions).load(image).into(setUpImage);
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Data doesn't exist", Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    upload.setEnabled(true);
                }
                else{
                    String error = task.getException().getMessage();
                    Toast.makeText(getContext(), "Firestore Error"+error, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
    public void showFileChooser(){
        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        else{
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                        Toast.makeText(getContext(), download_uri, Toast.LENGTH_LONG).show();

                        firestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "set account setting successfully", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), "Error"+error, Toast.LENGTH_LONG).show();
                                }

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                    else{
                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), "Error"+error, Toast.LENGTH_LONG).show();
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
