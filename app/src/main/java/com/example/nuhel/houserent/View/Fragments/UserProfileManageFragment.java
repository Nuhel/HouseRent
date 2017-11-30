package com.example.nuhel.houserent.View.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nuhel.houserent.Adapter.OwnPostRecyclerViewAdapter;
import com.example.nuhel.houserent.Controller.FragmentControllerAfterUserLog_Reg;
import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.Controller.ProjectKeys;
import com.example.nuhel.houserent.R;
import com.example.nuhel.houserent.View.AddPostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static com.nightonke.boommenu.ButtonEnum.TextOutsideCircle;

public class UserProfileManageFragment extends Fragment {

    private RecyclerView recyclerView;
    private View view;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = null;
    private StorageReference filepath;

    public UserProfileManageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = view == null ? inflater.inflate(R.layout.user_profile_manage, container, false) : view;
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        new OwnPostRecyclerViewAdapter(view.getContext(), recyclerView);

        BoomMenuButton bmb = view.findViewById(R.id.bmb);

        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_4_1);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_4_1);
        bmb.setButtonEnum(TextOutsideCircle);
        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {

            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder().listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {

                    switch (index) {
                        case 0:
                            GetFirebaseAuthInstance.getFirebaseAuthInstance().signOut();
                            ((FragmentControllerAfterUserLog_Reg) getActivity()).setFrag();
                            break;

                        case 1:
                            showConfirmDialog("Sure to delete user photo?");
                            break;
                        case 2:
                            showConfirmDialog("Sure to change user photo?");
                            break;

                        case 3:
                            startActivity(new Intent(getContext(), AddPostActivity.class));
                            break;
                    }

                }
            });

            if (i == 0) {
                builder.normalImageRes(R.drawable.addposticon).normalText("Log Out");
            } else if (i == 1) {
                builder.normalImageRes(R.drawable.addposticon).normalText("Delete Profile Picture");
            } else if (i == 2) {
                builder.normalImageRes(R.drawable.addposticon).normalText("Change Profile Photo");
            } else {
                builder.normalImageRes(R.drawable.addposticon).normalText("Add New Post");
            }

            bmb.addBuilder(builder);
        }

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                updateUserPhoto(getCompressedFileUri(result.getUri()));
            } else {
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private Uri getCompressedFileUri(Uri path) {
        File thumb_bitmap = null;
        try {
            thumb_bitmap = new Compressor(getContext())
                    .setQuality(75)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .compressToFile(new File(path.getPath()));
            Uri uri = Uri.fromFile(thumb_bitmap);
            return uri;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void deleteUserPhoto() {

        Toast.makeText(getContext(), "Deleting Profile photo\nYou Will Notify Shortly", Toast.LENGTH_SHORT).show();
        mAuth = GetFirebaseAuthInstance.getFirebaseAuthInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = GetFirebaseInstance.GetInstance().getReference().child(ProjectKeys.USERDIR).child(mAuth.getCurrentUser().getUid());
        filepath = mStorageRef.child(ProjectKeys.PROFILEIMAGEDIR).child(mAuth.getCurrentUser().getUid() + ProjectKeys.JPGIMAGEFORMAT);

        filepath.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Map map = new HashMap();
                map.put(ProjectKeys.USERDIRPROFILEIMAGE, ProjectKeys.NOIMG);
                mDatabase = GetFirebaseInstance.GetInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
                mDatabase.updateChildren(map);
                Toast.makeText(getContext(), "Prfile Photo deleted succesfully", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void updateUserPhoto(Uri imageUri) {
        mAuth = GetFirebaseAuthInstance.getFirebaseAuthInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabase = GetFirebaseInstance.GetInstance().getReference().child(ProjectKeys.USERDIR).child(mAuth.getCurrentUser().getUid());
        filepath = mStorageRef.child(ProjectKeys.PROFILEIMAGEDIR).child(mAuth.getCurrentUser().getUid() + ProjectKeys.JPGIMAGEFORMAT);
        Toast.makeText(getContext(), "Updating Profile Photo\nPlease Wait", Toast.LENGTH_SHORT).show();
        filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    final String download_url = task.getResult().getDownloadUrl().toString();
                    Map map = new HashMap();
                    map.put(ProjectKeys.USERDIRPROFILEIMAGE, download_url);
                    mDatabase = GetFirebaseInstance.GetInstance().getReference().child(ProjectKeys.USERDIR).child(mAuth.getCurrentUser().getUid());
                    mDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Profile Photo Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Profile Photo Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showConfirmDialog(String title) {

        int flag = 0;

        if (title.contains("delete")) {
            flag = 0;
        } else if (title.contains("change")) {
            flag = 1;
        }

        final int fl = flag;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (fl == 0) {
                            deleteUserPhoto();
                        } else if (fl == 1) {

                            CropImage.activity()
                                    .setAspectRatio(1, 1)
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .setMinCropWindowSize(500, 500)
                                    .start(getActivity());

                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        }
    }


}