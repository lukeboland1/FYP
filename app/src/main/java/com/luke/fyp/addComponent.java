package com.luke.fyp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class addComponent extends AppCompatActivity {
    private DatabaseAccess db;
    private ArrayList<Component> components;
    private EditText mEdit;
    private EditText fat;
    String servingType;
    boolean canStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_component);
        db = new DatabaseAccess(this);
        mEdit   = (EditText)findViewById(R.id.componentName);
        fat = (EditText)findViewById(R.id.fatPerServing);
        new setupDisplay().execute();

        /*
        String[] items = {"Millilitres", "Grams", "Unit", "Tablespoon", "Teaspoon", "Oz", "Floz", "Pints", "Lbs", "Cup", "Milligram", "Portion"};
        Spinner dropdown = (Spinner)findViewById(R.id.spinnerServingType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String item =(String) arg0.getItemAtPosition(arg2);
                servingType = item;
                canStore = true;
                if(servingType.equals("Grams"))
                {
                    fat.setHint("Fat per 100 grams");
                }
                else if(servingType.equals("Millilitres"))
                {
                    fat.setHint("Fat per 100 millilitres");
                }

                else if(servingType.equals("Tablespoon"))
                {
                    fat.setHint("Fat per Tablespoon");
                }

                else if(servingType.equals("Teaspoon"))
                {
                    fat.setHint("Fat per Teaspoon");
                }

                else if(servingType.equals("Oz"))
                {
                    fat.setHint("Fat per Oz");
                }

                else if(servingType.equals("Floz"))
                {
                    fat.setHint("Fat per Floz");
                }

                else if(servingType.equals("Pints"))
                {
                    fat.setHint("Fat per Pint");
                }

                else if(servingType.equals("Lbs"))
                {
                    fat.setHint("Fat per lb");
                }

                else if(servingType.equals("Milligram"))
                {
                    fat.setHint("Fat per 100 milligrams");
                }

                else if(servingType.equals("Cup"))
                {
                    fat.setHint("Fat per cup");
                }

                else if(servingType.equals("Portion"))
                {
                    fat.setHint("Fat per portion");
                }

                else if(servingType.equals("Unit"))
                {
                    fat.setHint("Fat per unit");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });*/
    }



    public void saveComponent(View v)
    {
        if(!fat.getText().toString().equals("")) {
            new AddNewComponent().execute(mEdit.getText().toString(), fat.getText().toString());
        }
        else
        {
            Toast.makeText(addComponent.this, "Please enter fat per serving", Toast.LENGTH_SHORT).show();
        }
    }

    private class AddNewComponent extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean found = false;
            for(int i = 0; i < components.size() && !found; i++)
            {
                if(components.get(i).getName().equals(params[0]))
                {
                    found = true;
                }
            }
            boolean tof = false;

            if(!found) {
                tof = db.insertComponent(params[0], Integer.parseInt(params[1]), servingType);
                components = db.getAllComponents();
            }

            return tof;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                Toast.makeText(getApplicationContext(), "Item stored", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Failed to store item", Toast.LENGTH_SHORT).show();
            }
            mEdit.setText("");
            fat.setText("");
            populateListView();

        }
    }

    private class setupDisplay extends AsyncTask<String, Object, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            components = db.getAllComponents();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            populateListView();
            String[] items = {"Millilitres", "Grams", "Unit", "Tablespoon", "Teaspoon", "Oz", "Floz", "Pints", "Lbs", "Cup", "Milligram", "Portion"};
            Spinner dropdown = (Spinner)findViewById(R.id.spinnerServingType);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(addComponent.this, android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                    String item =(String) arg0.getItemAtPosition(arg2);
                    servingType = item;
                    canStore = true;
                    if(servingType.equals("Grams"))
                    {
                        fat.setHint("Fat per 100 grams");
                    }
                    else if(servingType.equals("Millilitres"))
                    {
                        fat.setHint("Fat per 100 millilitres");
                    }

                    else if(servingType.equals("Tablespoon"))
                    {
                        fat.setHint("Fat per Tablespoon");
                    }

                    else if(servingType.equals("Teaspoon"))
                    {
                        fat.setHint("Fat per Teaspoon");
                    }

                    else if(servingType.equals("Oz"))
                    {
                        fat.setHint("Fat per Oz");
                    }

                    else if(servingType.equals("Floz"))
                    {
                        fat.setHint("Fat per Floz");
                    }

                    else if(servingType.equals("Pints"))
                    {
                        fat.setHint("Fat per Pint");
                    }

                    else if(servingType.equals("Lbs"))
                    {
                        fat.setHint("Fat per lb");
                    }

                    else if(servingType.equals("Milligram"))
                    {
                        fat.setHint("Fat per 100 milligrams");
                    }

                    else if(servingType.equals("Cup"))
                    {
                        fat.setHint("Fat per cup");
                    }

                    else if(servingType.equals("Portion"))
                    {
                        fat.setHint("Fat per portion");
                    }

                    else if(servingType.equals("Unit"))
                    {
                        fat.setHint("Fat per unit");
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {


                }
            });

        }
    }




    private void populateListView()
    {
        ArrayAdapter<Component> adapt = new MyListAdapter();
        ListView list =(ListView) findViewById(R.id.componentListView5);
        list.setAdapter(adapt);

    }


    private class MyListAdapter extends ArrayAdapter<Component>
    {
        public MyListAdapter()
        {
            super(addComponent.this, R.layout.item_view, components);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);

            }

            Component mr = components.get(position);
            TextView mName = (TextView)itemView.findViewById(R.id.itemViewMealName);
            mName.setText(mr.getName());
            TextView mNotes = (TextView)itemView.findViewById(R.id.itemViewNotes);
            mNotes.setText("" + mr.getQuantity() + " " + mr.getServingType());


            return itemView;
        }
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_component, menu);
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
}
