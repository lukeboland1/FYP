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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class editMeal extends AppCompatActivity implements TextWatcher {
    private DatabaseAccess db;
    private ArrayList<Component> components;
    private ArrayList<Component> allComponents;
    private ArrayList<Component> toBeRemoved;
    private ArrayList<Combination> combinations;
    private AutoCompleteTextView myAutoComplete;
    private ArrayList<Component> newComponents;
    private AutoCompleteTextView myAutoComplete1;
    private Combination combination1;
    private Component component1;
    private EditText quantity;
    private Button b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_meal);
        toBeRemoved = new ArrayList<>();
        newComponents = new ArrayList<>();
        db = new DatabaseAccess(this);
        myAutoComplete = (AutoCompleteTextView)findViewById(R.id.combinationNameEdit);
        myAutoComplete1 = (AutoCompleteTextView)findViewById(R.id.myautocompleteEdit1);
        quantity = (EditText) findViewById(R.id.quantityEdit);
        b = (Button) findViewById(R.id.addToCombinationEdit);
        combination1 = null;
        new loadObjects().execute();

    }

    public void dropDownDisplayCombinations()
    {
        String[] items = new String[combinations.size()];
        for(int i = 0; i < combinations.size(); i++) {
            items[i] = (combinations.get(i).getName());
        }

        myAutoComplete.addTextChangedListener(this);
        myAutoComplete.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items));
        myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                String item = (String) arg0.getItemAtPosition(arg2);
                boolean found = false;
                for (int i = 0; i < combinations.size() && !found; i++) {
                    if (combinations.get(i).getName().equals(item)) {
                        combination1 = combinations.get(i);
                        components = combination1.getComponents();
                        populateListView();

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
        String[] items = new String[allComponents.size()];
        for(int i = 0; i < allComponents.size(); i++) {
            items[i] = (allComponents.get(i).getName());
        }

        myAutoComplete1.addTextChangedListener(this);
        myAutoComplete1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items));
        myAutoComplete1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                String item = (String) arg0.getItemAtPosition(arg2);
                boolean found = false;
                for (int i = 0; i < allComponents.size() && !found; i++) {
                    if (allComponents.get(i).getName().equals(item)) {
                        component1 = allComponents.get(i);
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
        ListView list =(ListView) findViewById(R.id.componentListViewEdit);
        list.setAdapter(adapt);

    }

    private class MyListAdapter extends ArrayAdapter<Component>
    {
        public MyListAdapter()
        {
            super(editMeal.this, R.layout.item_view2, components);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.item_view2, parent, false);

            }

            final Component mr = components.get(position);
            TextView mName = (TextView)itemView.findViewById(R.id.itemViewMealName2);
            mName.setText(mr.getName());
            TextView mNotes = (TextView)itemView.findViewById(R.id.itemViewNotes2);
            mNotes.setText(" " + mr.getQuantity() + " " + mr.getServingType());
            final Button up = (Button)itemView.findViewById(R.id.button6);
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toBeRemoved.add(mr);
                    components.remove(position);
                    populateListView();
                }
            });

            return itemView;
        }
    }


    public void addToCombination(View v)
    {
        myAutoComplete1.clearListSelection();
        if(component1 == null)
        {

        }
        else {
            component1.setQuantity(Double.parseDouble(quantity.getText().toString()));
            newComponents.add(component1);
            components.add(component1);
            populateListView();
            quantity.setText("");
        }
        component1 = null;
        myAutoComplete1.setText("");
        quantity.setText("");
        quantity.setHint("Quantity");
        quantity.setEnabled(false);
        b.setEnabled(false);
    }


    public void saveCombination(View v)
    {

       if(components.size() <= 0)
        {
            Toast.makeText(editMeal.this, "Please Add At least 1 ingredient", Toast.LENGTH_SHORT).show();
        }

       if(combination1 == null)
       {
           Toast.makeText(editMeal.this, "Please Select Valid Meal to Edit", Toast.LENGTH_SHORT).show();
       }

        else {
            new saveThisCombination().execute();
        }

    }

    private class saveThisCombination extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            int combID = combination1.getID();

            for(int j = 0; j < toBeRemoved.size(); j++)
            {
                db.removeComponentCombination(toBeRemoved.get(j).getID(), combID);
            }

            for (int i = 0; i < newComponents.size(); i++)
            {
                db.addComponentCombination(newComponents.get(i).getID(), combID, newComponents.get(i).getQuantity());
            }


            components.clear();
            newComponents.clear();
            toBeRemoved.clear();
            combination1 = null;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            myAutoComplete.clearListSelection();
            myAutoComplete.setText("");
            populateListView();
            new loadCombinations().execute();
            Toast.makeText(editMeal.this, "Meal Edited", Toast.LENGTH_SHORT).show();

        }
    }


    private class loadObjects extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            allComponents = db.getAllComponents();
            combinations = db.getAllCombinations();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dropDownDisplayCombinations();
            dropDownDisplayComponents();
        }
    }

    private class loadCombinations extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            combinations = db.getAllCombinations();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dropDownDisplayCombinations();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_meal, menu);
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
            Intent intent = new Intent(getApplicationContext(), settings.class);
            startActivity(intent);
            return true;
        }

        else if (id == android.R.id.home)
        {
            super.onBackPressed();
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
}
