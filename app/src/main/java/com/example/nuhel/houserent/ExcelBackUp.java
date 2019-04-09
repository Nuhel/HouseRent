package com.example.nuhel.houserent;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.nuhel.houserent.Adapter.HomeAddListDataModel;
import com.example.nuhel.houserent.Adapter.SnapShotToDataModelParser;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.Controller.ProjectKeys;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.util.LinkedHashMap;

public class ExcelBackUp extends AppCompatActivity {
//textView

    private DatabaseReference db;
    private LinkedHashMap<String, HomeAddListDataModel> add_list;
    private Button button2;
    private Context context;
    private String[] colums = {"firstName", "lastName", "email", "dateOfBirth"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_back_up);

        this.db = GetFirebaseInstance.GetInstance().getReference(ProjectKeys.ALLADSDIR);
        this.add_list = new LinkedHashMap<>();
        this.context = getBaseContext();

        ChildEventListener vl =  new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String key = dataSnapshot.getKey();
                if (add_list.get(key) == null) {
                    HomeAddListDataModel model = new SnapShotToDataModelParser().getModel(dataSnapshot, context);
                    add_list.put(dataSnapshot.getKey(), model);

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(getBaseContext(),databaseError.toString(),Toast.LENGTH_SHORT);
            }
        };


        db.orderByKey().addChildEventListener(vl);

        button2 = findViewById(R.id.button2);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= 23) {
                    if (
                            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {

                        write(add_list);

                    }else{
                        ActivityCompat.requestPermissions(ExcelBackUp.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            write(add_list);
        }
    }


    private void write( LinkedHashMap<String, HomeAddListDataModel> add_list){
        File sd = Environment.getExternalStorageDirectory();
        String csvFile = "myData.xls";

        File directory = new File(sd.getAbsolutePath());
        //create directory if not exist
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }

        try {
            //file path
            File file = new File(directory, csvFile);

            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook;
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("userList", 0);



            sheet.addCell(new Label(0, 0, "Title"));
            sheet.addCell(new Label(1, 0, "Kitchen"));
            sheet.addCell(new Label(2, 0, "Velocony"));
            sheet.addCell(new Label(3, 0, "Bedroom"));
            sheet.addCell(new Label(4, 0, "Bathroom"));
            sheet.addCell(new Label(5, 0, "Rent"));
            sheet.addCell(new Label(6, 0, "Phone Number"));

            for (int i =0; i<= add_list.size()-1; i++){

                sheet.addCell(new Label(0, i+1,  ((HomeAddListDataModel) (add_list.values().toArray()[i])).getTitle()));
                sheet.addCell(new Label(1, i+1,  ((HomeAddListDataModel) (add_list.values().toArray()[i])).getKitchen()));
                sheet.addCell(new Label(2, i+1,  ((HomeAddListDataModel) (add_list.values().toArray()[i])).getVelcony()));
                sheet.addCell(new Label(3, i+1,  ((HomeAddListDataModel) (add_list.values().toArray()[i])).getBedroom()));
                sheet.addCell(new Label(4, i+1,  ((HomeAddListDataModel) (add_list.values().toArray()[i])).getBathroom()));
                sheet.addCell(new Label(5, i+1,  ((HomeAddListDataModel) (add_list.values().toArray()[i])).getRent()));
                sheet.addCell(new Label(6, i+1,  ((HomeAddListDataModel) (add_list.values().toArray()[i])).getPhone()));


            }


            workbook.write();
            workbook.close();
            Toast.makeText(getApplication(), "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }
}
