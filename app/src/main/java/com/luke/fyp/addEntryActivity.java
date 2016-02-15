package com.luke.fyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

public class addEntryActivity extends AppCompatActivity  implements TextWatcher {
    private EditText mEdit;
    private AutoCompleteTextView myAutoComplete1;
    private AutoCompleteTextView myAutoComplete2;
    private DatabaseAccess db;
    private ArrayList<Combination> combinations;
    private ArrayList<Component> components;
    private ArrayList<Component> m;
    private Component component1;
    private Combination combination1;
    private EditText quantity;
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        db = new DatabaseAccess(this);
        myAutoComplete1 = (AutoCompleteTextView) findViewById(R.id.autoMealName);
        myAutoComplete2 = (AutoCompleteTextView) findViewById(R.id.autoComponentName);
        quantity = (EditText) findViewById(R.id.quantityTaken);
        b = (Button) findViewById(R.id.addMealToEntry);
        b.setEnabled(false);
        component1 = null;
        combination1 = null;
        m = new ArrayList<>();
        dropDownDisplayCombinations();
        dropDownDisplayComponents();

    }

    public void dropDownDisplayCombinations()
    {
        combinations = db.getAllCombinations();
        String[] items = new String[combinations.size()];
        for(int i = 0; i < combinations.size(); i++) {
            items[i] = (combinations.get(i).getName());
        }

        myAutoComplete1.addTextChangedListener(this);
        myAutoComplete1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items));
        myAutoComplete1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                String item = (String) arg0.getItemAtPosition(arg2);
                boolean found = false;
                for (int i = 0; i < combinations.size() && !found; i++) {
                    if (combinations.get(i).getName().equals(item)) {
                        combination1 = combinations.get(i);
                        found = true;
                        b.setEnabled(true);
                    }
                }

            }

            /*@Override
            public void onNothingSelected(AdapterView<?> arg0) {
                text.setText("");

            }*/
        });
    }

    public void dropDownDisplayComponents()
    {
        components = db.getAllComponents();
        String[] items = new String[components.size()];
        for(int i = 0; i < components.size(); i++) {
            items[i] = (components.get(i).getName());
        }

        myAutoComplete2.addTextChangedListener(this);
        myAutoComplete2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items));
        myAutoComplete2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                String item = (String) arg0.getItemAtPosition(arg2);
                boolean found = false;
                for (int i = 0; i < components.size() && !found; i++) {
                    if (components.get(i).getName().equals(item)) {
                        component1 = components.get(i);
                        found = true;
                    }
                }

            }

            /*@Override
            public void onNothingSelected(AdapterView<?> arg0) {
                text.setText("");

            }*/
        });
    }



    @Override
    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
        b.setEnabled(false);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_entry, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addMealToEntry(View v)
    {
        myAutoComplete1.setEnabled(false);
        b.setEnabled(false);
    }

    public void addComponentToEntry(View v)
    {
        myAutoComplete2.clearListSelection();
        if(component1 == null)
        {

        }
        else {
            component1.setQuantity(Integer.parseInt(quantity.getText().toString()));
            m.add(component1);
            //populateListView();
            quantity.setText("");
        }
        component1 = null;
        myAutoComplete2.setText("");
        quantity.setText("");

    }

    public void saveEntry(View v)
    {
        Calendar calendar = Calendar.getInstance();
        long datetime = calendar.getTimeInMillis();
        db.insertEntry("",0,"",datetime);
        int id = db.getIDForRecentEntry();
        if(combination1 == null)
        {

        }
        else{
            db.addEntryCombination(id, combination1.getID());
        }

        for(int i = 0; i < m.size(); i++)
        {
            db.addEntryComponent(m.get(i).getID(), id, m.get(i).getQuantity());
        }

        myAutoComplete1.setText("");
        myAutoComplete1.clearListSelection();
    }
}
