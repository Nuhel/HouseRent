package com.example.nuhel.houserent.View.Fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.nuhel.houserent.Adapter.AddPostPopUpRViewAdapter;
import com.example.nuhel.houserent.Adapter.OwnPostRecyclerViewAdapter;
import com.example.nuhel.houserent.Controller.FragmentControllerAfterUserLog_Reg;
import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.Controller.ProjectKeys;
import com.example.nuhel.houserent.CustomImagePicker.Action;
import com.example.nuhel.houserent.R;
import com.example.nuhel.houserent.View.PopUps.AddPostPopUpView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static com.nightonke.boommenu.ButtonEnum.TextOutsideCircle;

public class UserProfileManageFragment extends Fragment {


    private RecyclerView recyclerView;
    private View view;
    private ArrayList<String> postIds;
    private DatabaseReference my_postlist_ref;
    private DatabaseReference all_postlist_ref;
    private String userUid;

    private ArrayList<Uri> converted_imagePaths;
    private ArrayList<Uri> original_imagePaths;

    private int cu = 0;
    private String postkey;
    private ArrayList<String> downloadLinks;
    private int PLACE_PICKER_REQUEST = 999;
    private AddPostPopUpRViewAdapter addPostPopUpRViewAdapter;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = null;

    private StorageReference filepath;

    private AddPostPopUpView addPostPopUpView;


    public UserProfileManageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        userUid = GetFirebaseAuthInstance.getFirebaseAuthInstance().getCurrentUser().getUid();
        all_postlist_ref = GetFirebaseInstance.GetInstance().getReference("HomeAddList");

        original_imagePaths = new ArrayList<>();
        converted_imagePaths = new ArrayList<>();
        downloadLinks = new ArrayList<>();

        view = view == null ? inflater.inflate(R.layout.user_profile_manage, container, false) : view;


        initializePostIds();


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
                            showMakePostDialog();
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

    private void showMakePostDialog() {

        addPostPopUpView = new AddPostPopUpView(getContext(), getActivity().getSupportFragmentManager());

        final View dview = addPostPopUpView.getView();

        final Dialog dialog2 = new Dialog(getContext());


        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(null);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setContentView(dview);
        WindowManager.LayoutParams lp = dialog2.getWindow().getAttributes();
        Window window = dialog2.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
        lp.gravity = Gravity.CENTER;


        dialog2.show();

        ImageButton gellaryPickerbtn = addPostPopUpView.getGellaryPickerbtn();

        ImageButton clsbtn = addPostPopUpView.getClosebtn();

        gellaryPickerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isStoragePermissionGranted()) {
                    Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                    startActivityForResult(i, 200);
                }

            }
        });


        addPostPopUpView.getSearchButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlace();
            }
        });


        clsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog2.cancel();
                dialog2.dismiss();
                addPostPopUpView = null;

            }
        });


        Button postbtn = addPostPopUpView.getPostButton();

        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImages(addPostPopUpRViewAdapter.getconverted_imagePaths());
            }
        });


        addPostPopUpRViewAdapter = new AddPostPopUpRViewAdapter(getContext());

        RecyclerView recyclerView = dview.findViewById(R.id.horizontal_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(addPostPopUpRViewAdapter);

    }

    private void initializePostIds() {
        postIds = new ArrayList<>();
        my_postlist_ref = GetFirebaseInstance.GetInstance().getReference("User").child(userUid).child("posts");
        my_postlist_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                postIds.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int index = postIds.indexOf(dataSnapshot.getKey());
                postIds.set(index, dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int index = postIds.indexOf(dataSnapshot.getKey());
                postIds.remove(index);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String postKeyGenerator() {
        if (postIds.size() > 0) {
            int number = Integer.parseInt(postIds.get(postIds.size() - 1).split("PostNo-")[1]) + 1;
            return userUid + "PostNo-" + number;
        } else {
            return userUid + "PostNo-0";
        }
    }


    private void uploadImages(ArrayList<Uri> converted_imagePaths) {
        final ArrayList<Uri> uplopad_images = converted_imagePaths;
        postkey = postKeyGenerator();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference filepath = mStorageRef.child("post_images").child(postkey + "imgno-" + cu + ".jpeg");

        filepath.putFile(uplopad_images.get(cu)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    downloadLinks.add(task.getResult().getDownloadUrl().toString());
                } else {
                    Toast.makeText(getContext(), "Image Upload Failed", Toast.LENGTH_SHORT).show();

                }
                cu++;
                if (cu <= uplopad_images.size() - 1) {
                    Toast.makeText(getContext(), "Task Done " + String.valueOf(cu), Toast.LENGTH_SHORT).show();
                    uploadImages(uplopad_images);
                } else {
                    Toast.makeText(getContext(), "Task Done Uploaded", Toast.LENGTH_SHORT).show();

                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("area", "nuh");

                    for (int looper = 0; looper <= downloadLinks.size() - 1; looper++) {
                        map1.put("image" + (looper + 1), downloadLinks.get(looper));
                    }
                    String id = GetFirebaseAuthInstance.getFirebaseAuthInstance().getCurrentUser().getUid();
                    map1.put("owner", id);
                    all_postlist_ref.child(postkey).setValue(map1);
                    my_postlist_ref.child(postkey).setValue("f");

                }
            }
        });

    }

    private void getPlace() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(getActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 200) {
            converted_imagePaths.clear();
            if (resultCode == RESULT_OK) {
                String[] links = data.getStringArrayExtra("all_path");
                for (int a = 0; a <= links.length - 1; a++) {
                    Uri orguri = Uri.parse(links[a]);
                    if (original_imagePaths.indexOf(orguri) < 0) {
                        original_imagePaths.add(orguri);
                        converted_imagePaths.add(getCompressedFileUri(orguri));
                    }
                }

                addPostPopUpRViewAdapter.updateView(converted_imagePaths, original_imagePaths);
            }


        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getContext());
                String toastMsg = String.format("Place: %s", place.getLatLng());
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();


                if (addPostPopUpView != null) {

                    addPostPopUpView.updateMap(place.getLatLng(), place.getName().toString());
                }
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

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


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((getActivity()).checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {


        }
    }
}