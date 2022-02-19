package fr.imt_atlantique.tp1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText first_name, name, town;
    Spinner department;
    Button showPopupBtn, closePopupBtn, closePhonePopupBtn, callBtn, deleteBtn;
    PopupWindow popupWindow;
    LinearLayout linearLayout1;
    ArrayList<String> arrayList;
    ListView phoneList;
    EditText phone;
    ArrayAdapter<String> adapter;
    Button dateButton;
    Button phoneButton;
    String nameKey, first_nameKey, townKey, birthKey, departmentKey, phoneKey;
    private static final String NAME_KEY = "NAME_KEY", FIRST_NAME_KEY ="FIRST_NAME_KEY", TOWN_KEY = "TOWN_KEY", BIRTH_KEY = "BIRTH_KEY", DEPARTMENT_KEY = "DEPARTMENT_KEY", PHONE_KEY = "PHONE_KEY";
    private static final String SHARED_PREF = "SHARED_PREF";
    android.content.SharedPreferences s;
    SharedPreferences.Editor e;
    Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("create","create");

        first_name = findViewById(R.id.editFirstName);
        name = findViewById(R.id.editTextName);
        town = findViewById(R.id.editBirthTown);
        department = findViewById(R.id.spinnerDepartment);
        dateButton = (Button) findViewById(R.id.date);

        phoneList = findViewById(R.id.phoneList);
        phone = findViewById(R.id.editTextPhone);
        phoneButton = findViewById(R.id.buttonPhone);
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, arrayList);
        phoneList.setAdapter(adapter);

        showPopupBtn = (Button) findViewById(R.id.showPopupBtn);
        linearLayout1 = (LinearLayout) findViewById(R.id.linearLayout);


        set = new LinkedHashSet<String>();

        s = getSharedPreferences(SHARED_PREF,MODE_PRIVATE);

        if(savedInstanceState==null){
            Log.i("ok","gghg");


            first_name.setText(s.getString(FIRST_NAME_KEY,null));
            name.setText(s.getString(NAME_KEY,null));
            dateButton.setText(s.getString(BIRTH_KEY, null));
            town.setText(s.getString(TOWN_KEY,null));
            department.setSelection(s.getInt(DEPARTMENT_KEY,0));
            arrayList.addAll(s.getStringSet(PHONE_KEY, null));

        }

        else{
            Log.i("rate","rate");
            first_name.setText(savedInstanceState.getString(FIRST_NAME_KEY));
            name.setText(savedInstanceState.getString(NAME_KEY));
            town.setText(savedInstanceState.getString(TOWN_KEY));
            dateButton.setText(savedInstanceState.getString((BIRTH_KEY)));
            department.setSelection(savedInstanceState.getInt(DEPARTMENT_KEY));
            arrayList.addAll(savedInstanceState.getStringArrayList(PHONE_KEY));
        }

        //////////////////////////////////////////////////////
        // Birth Date ////////////////////////////////////////
        //button to choose the date (birth)
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateButton.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        //////////////////////////////////////////////////////
        // Phone List ////////////////////////////////////////

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // this line adds the data of your EditText and puts in your array
                arrayList.add(phone.getText().toString() );
                phone.getText().clear();
                // next thing you have to do is check if your adapter has changed
                adapter.notifyDataSetChanged();
            }
        });

        // to delete a phone when the user clicks on it
        phoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //instantiate the phone_popup.xml layout file
                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.phone_popup,null);

                callBtn = customView.findViewById(R.id.callBtn);
                deleteBtn = customView.findViewById(R.id.deleteBtn);
                closePhonePopupBtn = (Button) customView.findViewById(R.id.closePhonePopupBtn);

                //instantiate popup window
                popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //display the popup window
                popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);

                //close the popup window on button click
                closePhonePopupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                // delete the phone from the list
                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        arrayList.remove(arrayList.get(i));
                        adapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });

            }
        });

        //////////////////////////////////////////////////////
        // Pop up ///////////////////////////////////////////

        showPopupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //instantiate the popup.xml layout file
                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup,null);

                closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);

                String de ="";
                int dep_index = (int) department.getSelectedItemId();
                if(!(dep_index==0)){
                    de = department.getItemAtPosition(dep_index).toString();
                }

                // get the information from the edit texts
                TextView text = customView.findViewById(R.id.textPopup);
                String data = first_name.getText().toString() + "\n" + name.getText().toString() + "\n" + dateButton.getText().toString()+ "\n"+ town.getText().toString() + "\n" + de + "\n" + arrayList.toString();
                text.setText(data);
                //instantiate popup window
                popupWindow = new PopupWindow(customView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                //display the popup window
                popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);

                //close the popup window on button click
                closePopupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void resetAction(MenuItem item){

        // reset the forms
        first_name.getText().clear();
        name.getText().clear();
        dateButton.setText(R.string.birth_select);
        town.getText().clear();
        department.setSelection(0);
        phone.getText().clear();
        arrayList.clear();
        adapter.notifyDataSetChanged();

        //delete the saved information
        e = s.edit();
        e.putString(FIRST_NAME_KEY, "");
        e.putString(NAME_KEY,"");
        e.putString(TOWN_KEY, "");
        e.putString(BIRTH_KEY, "");
        e.putInt(DEPARTMENT_KEY, 0);
        if(!(set==null)){
        set.clear();}
        e.putStringSet(PHONE_KEY, set);
        e.apply();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);


        outState.putString(NAME_KEY, name.getText().toString());
        outState.putString(FIRST_NAME_KEY, first_name.getText().toString());
        outState.putString(TOWN_KEY, town.getText().toString());
        outState.putString(BIRTH_KEY, dateButton.getText().toString());
        outState.putInt(DEPARTMENT_KEY, department.getSelectedItemPosition());
        outState.putStringArrayList(PHONE_KEY, arrayList);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        first_name.setText(savedInstanceState.getString(FIRST_NAME_KEY));
        name.setText(savedInstanceState.getString(NAME_KEY));
        town.setText(savedInstanceState.getString(TOWN_KEY));
        dateButton.setText(savedInstanceState.getString((BIRTH_KEY)));
        department.setSelection(savedInstanceState.getInt(DEPARTMENT_KEY));
        arrayList.addAll(savedInstanceState.getStringArrayList(PHONE_KEY));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        e = s.edit();
        e.putString(FIRST_NAME_KEY, first_name.getText().toString());
        e.putString(NAME_KEY,name.getText().toString());
        e.putString(TOWN_KEY, town.getText().toString());
        e.putString(BIRTH_KEY, dateButton.getText().toString());
        e.putInt(DEPARTMENT_KEY, department.getSelectedItemPosition());
        set.addAll(arrayList);
        e.putStringSet(PHONE_KEY, set);
        e.apply();

    }
}
