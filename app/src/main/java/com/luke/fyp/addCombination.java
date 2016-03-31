package com.luke.fyp;

import android.content.ComponentCallbacks;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import java.util.ArrayList;

public class addCombination extends AppCompatActivity implements TextWatcher {
    private EditText mEdit;
    private AutoCompleteTextView myAutoComplete;
    private DatabaseAccess db;
    private ArrayList<Component> components;
    private ArrayList<Component> m;
    private Component component1;
    private EditText quantity;
    private ArrayList<Combination> combinations;
    private Button b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_combination);
        component1 = null;
        mEdit = (EditText) findViewById(R.id.combinationName);
        quantity = (EditText) findViewById(R.id.quantity);
        b = (Button) findViewById(R.id.addToCombination);
        db = new DatabaseAccess(this);
        m = new ArrayList<>();
        myAutoComplete = (AutoCompleteTextView)findViewById(R.id.myautocomplete1);
        new loadObjects().execute();
    }

    public void dropDownDisplayComponents()
    {
        String[] items = new String[components.size()];
        for(int i = 0; i < components.size(); i++) {
            items[i] = (components.get(i).getName());
        }

        myAutoComplete.addTextChangedListener(this);
        myAutoComplete.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items));
        myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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

                        quantity.setEnabled(true);
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
    private void populateListView()
    {
        ArrayAdapter<Component> adapt = new MyListAdapter();
        ListView list =(ListView) findViewById(R.id.componentListView);
        list.setAdapter(adapt);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_combination, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.addItemCombination)
        {
            Intent intent = new Intent(getApplicationContext(), addComponent.class);
            startActivity(intent);
            new loadObjects().execute();
            return true;
        }

        else if (id == android.R.id.home)
        {
            super.onBackPressed();
            return true;
        }

        else if (id == R.id.editMealMenu)
        {
            Intent intent = new Intent(getApplicationContext(), editMeal.class);
            startActivity(intent);
            return true;
        }


        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), settings.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
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

    }

    private class MyListAdapter extends ArrayAdapter<Component>
    {
        public MyListAdapter()
        {
            super(addCombination.this, R.layout.item_view2, m);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.item_view2, parent, false);

            }

            final Component mr = m.get(position);
            TextView mName = (TextView)itemView.findViewById(R.id.itemViewMealName2);
            mName.setText(mr.getName());
            TextView mNotes = (TextView)itemView.findViewById(R.id.itemViewNotes2);
            mNotes.setText(" " + mr.getQuantity() + " " + mr.getServingType());
            final Button up = (Button)itemView.findViewById(R.id.button6);
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m.remove(position);
                    populateListView();
                }
            });

            return itemView;
        }

    }

    public void addToCombination(View v)
    {
        myAutoComplete.clearListSelection();
        if(component1 == null)
        {

        }
        else {
            component1.setQuantity(Double.parseDouble(quantity.getText().toString()));
            m.add(component1);
            populateListView();
            quantity.setText("");
        }
        myAutoComplete.setText("");
        component1 = null;
        quantity.setText("");
        quantity.setHint("Quantity");
        quantity.setEnabled(false);
        b.setEnabled(false);
    }

    public void saveCombination(View v)
    {

        boolean found = false;
        for(int i = 0; i < combinations.size() && !found; i++)
        {
            if(combinations.get(i).getName().equals(mEdit.getText().toString()))
            {
                found = true;
            }
        }

        if(found)
        {
            Toast.makeText(addCombination.this, "Meal name already exists, please choose another", Toast.LENGTH_SHORT).show();
            mEdit.setText("");
        }
        else if(mEdit.getText().toString().equals(""))
        {
            Toast.makeText(addCombination.this, "Please Enter Meal Name", Toast.LENGTH_SHORT).show();
        }
        else if(m.size() <= 0)
        {
            Toast.makeText(addCombination.this, "Please Add At least 1 ingredient", Toast.LENGTH_SHORT).show();
        }

        else {
            new AddNewCombination().execute(mEdit.getText().toString());
        }

    }

    private class AddNewCombination extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean tof = db.insertCombination(params[0], 0);
            int combID = db.getIDFromNameCombination(params[0]);
            for(int i = 0; i < m.size(); i++) {
                db.addComponentCombination(m.get(i).getID(), combID, m.get(i).getQuantity());
            }
            return tof;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Toast.makeText(addCombination.this, "Meal added", Toast.LENGTH_LONG).show();
            myAutoComplete.setText("");
            myAutoComplete.clearListSelection();
            m.clear();
            mEdit.setText("");
            populateListView();


        }
    }

    private class loadObjects extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            components = db.getAllComponents();
            combinations = db.getAllCombinations();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dropDownDisplayComponents();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }







}
