package com.luke.fyp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText creon;
    private EditText notes;
    private TextView suggestedCreon;
    private double entryFat;
    private User u;
    private String qa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        db = new DatabaseAccess(this);
        myAutoComplete1 = (AutoCompleteTextView) findViewById(R.id.autoMealName);
        myAutoComplete2 = (AutoCompleteTextView) findViewById(R.id.autoComponentName);
        creon = (EditText) findViewById(R.id.creonTaken2);
        notes = (EditText) findViewById(R.id.mealNotes);
        quantity = (EditText) findViewById(R.id.quantityTaken);
        suggestedCreon = (TextView) findViewById(R.id.suggested);
        b = (Button) findViewById(R.id.addMealToEntry);
        b.setEnabled(false);
        component1 = null;
        combination1 = null;
        qa = "1";
        entryFat = 0;
        m = new ArrayList<>();
        String[] items = {"1","1/2","1/3","1/4","1/5","1/6","1/8"};
        Spinner dropdown = (Spinner)findViewById(R.id.spinnerFraction);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(addEntryActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String item =(String) arg0.getItemAtPosition(arg2);
                qa = item;
                setQuantity();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

        MyApp myapp = ((MyApp)getApplicationContext());
        u = myapp.getUser();
        new getComponentsAndCombinations().execute();


    }

    public void setQuantity()
    {
        if(combination1 == null)
        {
            Toast.makeText(addEntryActivity.this, "Please select meal first", Toast.LENGTH_SHORT).show();
        }
        else {
            if (qa.equals("1")) {
                combination1.setQuantity(1);
            } else if (qa.equals("1/2")) {
                combination1.setQuantity(0.5);
            } else if (qa.equals("1/4")) {
                combination1.setQuantity(0.25);
            } else if (qa.equals("1/3")) {
                combination1.setQuantity(0.33333);
            } else if (qa.equals("1/5")) {
                combination1.setQuantity(0.2);
            } else if (qa.equals("1/6")) {
                combination1.setQuantity(0.1666666);
            } else if (qa.equals("1/8")) {
                combination1.setQuantity(0.125);
            }
        }

    }


    private class getComponentsAndCombinations extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            combinations = db.getAllCombinations();
            components = db.getAllComponents();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dropDownDisplayCombinations();
            dropDownDisplayComponents();

        }
    }

    public void dropDownDisplayCombinations()
    {
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
                        setQuantity();
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
                        String servingType = component1.getServingType();
                        if(component1.getServingType().equals("Grams"))
                        {
                            quantity.setHint("Quantity in Grams");
                        }

                        else if(component1.getServingType().equals("Millilitres"))
                        {
                            quantity.setHint("Quantity in Millilitres");
                        }

                        else if(servingType.equals("Tablespoon"))
                        {
                            quantity.setHint("Quantity in Tablespoons");
                        }

                        else if(servingType.equals("Teaspoon"))
                        {
                            quantity.setHint("Quantity in Teaspoons");
                        }

                        else if(servingType.equals("Oz"))
                        {
                            quantity.setHint("Quantity in Ozs");
                        }

                        else if(servingType.equals("Floz"))
                        {
                            quantity.setHint("Quantity in Flozs");
                        }

                        else if(servingType.equals("Pints"))
                        {
                            quantity.setHint("Quantity in Pints");
                        }

                        else if(servingType.equals("Lbs"))
                        {
                            quantity.setHint("Quantity in lbs");
                        }

                        else if(servingType.equals("Milligram"))
                        {
                            quantity.setHint("Quantity in milligrams");
                        }

                        else if(servingType.equals("Cup"))
                        {
                            quantity.setHint("Quantity in cups");
                        }

                        else if(servingType.equals("Portion"))
                        {
                            quantity.setHint("Quantity in portions");
                        }

                        else if(servingType.equals("Unit"))
                        {
                            quantity.setHint("Quantity in units");
                        }

                        else if(servingType.equals("Slices"))
                        {
                            quantity.setHint("Quantity in slices");
                        }

                    }
                }

            }

            /*@Override
            public void onNothingSelected(AdapterView<?> arg0) {
                text.setText("");

            }*/
        });
    }



    public void addMealToEntry(View v) {
        if (combination1 == null) {
            Toast.makeText(addEntryActivity.this, "Invalid Meal", Toast.LENGTH_SHORT).show();
        }
        else {
            myAutoComplete1.setEnabled(false);
            b.setEnabled(false);
            entryFat += (combination1.getFat()*combination1.getQuantity());
            double cb = (entryFat / u.getFatPerCreon());
            suggestedCreon.setText("Suggested Creon = " + Math.round(cb));
        }
    }

    public void addComponentToEntry(View v)
    {
        myAutoComplete2.clearListSelection();
        if (component1 == null) {

        } else {
            component1.setQuantity(Double.parseDouble(quantity.getText().toString()));
            m.add(component1);
            //populateListView();
            quantity.setText("");
            Toast.makeText(addEntryActivity.this, "Component Quantity = " + component1.getQuantity(), Toast.LENGTH_SHORT).show();
            Toast.makeText(addEntryActivity.this, "Component fat = " + component1.getTotalFat(), Toast.LENGTH_SHORT).show();
            entryFat += component1.getTotalFat();
            Toast.makeText(addEntryActivity.this, "Entry fat = " + entryFat, Toast.LENGTH_SHORT).show();
            double cr = (entryFat / u.getFatPerCreon());
            suggestedCreon.setText("Suggested Creon = " + Math.round(cr));


        }
        component1 = null;
        myAutoComplete2.setText("");
        quantity.setText("");
        populateListView();

    }

    public void saveEntry(View v)
    {
        Calendar calendar = Calendar.getInstance();
        long datetime = calendar.getTimeInMillis();
        db.insertEntry(Integer.parseInt(creon.getText().toString()), notes.getText().toString(), datetime);

        int id = db.getIDForRecentEntry();

        if(combination1 == null)
        {

        }
        else{
            db.addEntryCombination(id, combination1.getID(), combination1.getQuantity());
        }

        for(int i = 0; i < m.size(); i++)
        {
            db.addEntryComponent(m.get(i).getID(), id, m.get(i).getQuantity());
        }

        myAutoComplete1.setText("");
        creon.setText("");
        notes.setText("");
        myAutoComplete1.clearListSelection();
        m.clear();
        populateListView();
        suggestedCreon.setText("");
    }



    private void populateListView()
    {
        ArrayAdapter<Component> adapt = new MyListAdapter();
        ListView list =(ListView) findViewById(R.id.componentListView6);
        list.setAdapter(adapt);

    }


    private class MyListAdapter extends ArrayAdapter<Component>
    {
        public MyListAdapter()
        {
            super(addEntryActivity.this, R.layout.item_view1, m);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.item_view1, parent, false);

            }

            Component mr = m.get(position);
            TextView mName = (TextView)itemView.findViewById(R.id.itemViewMealName1);
            mName.setText(mr.getName());
            TextView mNotes = (TextView)itemView.findViewById(R.id.itemViewNotes1);
            mNotes.setText(mr.getQuantity() + " " + mr.getServingType());

            return itemView;
        }
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

        else if(id == R.id.addItemMenu)
        {
            Intent intent = new Intent(getApplicationContext(), addComponent.class);
            startActivity(intent);
            new getComponentsAndCombinations().execute();
            return true;
        }

        else if(id == R.id.addMealMenu)
        {
            Intent intent = new Intent(getApplicationContext(), addCombination.class);
            startActivity(intent);
            new getComponentsAndCombinations().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
