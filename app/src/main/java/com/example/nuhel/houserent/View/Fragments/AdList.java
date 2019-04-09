package com.example.nuhel.houserent.View.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.nuhel.houserent.Adapter.HomeAddListDataModel;
import com.example.nuhel.houserent.Adapter.RecyclerViewAdapter;
import com.example.nuhel.houserent.ExcelBackUp;
import com.example.nuhel.houserent.R;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class AdList extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {


    private RecyclerView recyclerView;
    private View view;
    private RecyclerViewAdapter adapter;
    private Context context;
    public static LinkedHashMap<String, HomeAddListDataModel> add_list;
    private FloatingActionButton btn;

    public AdList() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = view == null ? inflater.inflate(R.layout.activity_main_ads_list, container, false) : view;

        btn = view.findViewById(R.id.fab);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (
                            context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED
                                    && context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {

                        write(add_list);

                    }else{
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }
                }
            }
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        this.adapter = new RecyclerViewAdapter(view.getContext(), recyclerView);
        return view;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (TextUtils.isEmpty(s)) {
            adapter.getFilter().filter("");
        } else {
            adapter.getFilter().filter(s);
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.searchId);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);
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
            Toast.makeText(context, "Data Exported in a Excel Sheet", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            write(add_list);
        }
    }
}
