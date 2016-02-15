package com.luke.fyp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class viewMealsActivity extends AppCompatActivity implements TextWatcher {
    private DatabaseAccess db;
    private ArrayList<Component> components;
    private ArrayList<Combination> combinations;
    private AutoCompleteTextView myAutoComplete;
    private ArrayList<Entry> entries;
    private ArrayList<Entry> foundEntries;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meals);
        db = new DatabaseAccess(this);
        entries = db.getAllEntries();
        components = db.getAllComponents();
        combinations = db.getAllCombinations();
        if(combinations.size() > 0)
        {
            Toast.makeText(viewMealsActivity.this, ""+combinations.get(0).getName(), Toast.LENGTH_SHORT).show();
        }
        myAutoComplete = (AutoCompleteTextView)findViewById(R.id.myautocomplete);

        dropDownDisplayMeals();
        //displayMeals();
    }

    public void dropDownDisplayMeals()
    {
        String[] items = new String[combinations.size() + components.size()];
        for(int i = 0; i < combinations.size(); i++)
        {
            items[i] = (combinations.get(i).getName());
        }

        int p = combinations.size();

        for(int i = 0; i < components.size(); i++)
        {
            items[p+i] = (components.get(i).getName());
        }
        myAutoComplete = (AutoCompleteTextView)findViewById(R.id.myautocomplete);

        myAutoComplete.addTextChangedListener(this);
        myAutoComplete.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items));
        myAutoComplete.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                String item = (String) arg0.getItemAtPosition(arg2);
                boolean found = false;
                for (int i = 0; i < components.size() && !found; i++) {
                    if (components.get(i).getName().equals(item)) {
                        foundEntries = components.get(i).getEntries(entries);
                        found = true;
                        if(foundEntries.size() > 0) {
                            Toast.makeText(viewMealsActivity.this, ""+foundEntries.get(0).getCreonTaken(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                for (int i = 0; i < combinations.size() && !found; i++) {
                    if (combinations.get(i).getName().equals(item)) {
                        foundEntries = combinations.get(i).getEntries(entries);
                        found = true;
                        if(foundEntries.size() > 0) {
                            Toast.makeText(viewMealsActivity.this, ""+foundEntries.get(0).getCreonTaken(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                populateListView();

            }

            //@Override
           // public void onNothingSelected(AdapterView<?> arg0) {
             //   text.setText("");

            //}
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

    }

    private void populateListView()
    {
        ArrayAdapter<Entry> adapt = new MyListAdapter();
        ListView list =(ListView) findViewById(R.id.itemListView);
        list.setAdapter(adapt);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_meals, menu);
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

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    private class getAllMeals extends AsyncTask<String, Object, ArrayList<Meal>> {

        @Override
        protected ArrayList<Meal> doInBackground(String... params) {
            //ArrayList<Meal> meal = db.getAllMeals();
            //return meal;
            return null;
        }


    }

    private class getMealRecords extends AsyncTask<String, Object, ArrayList<MealRecord>> {

        @Override
        protected ArrayList<MealRecord> doInBackground(String... params) {
            //ArrayList<MealRecord> mealR = db.getMealRecordsFromName(params[0]);
            //return mealR;
            return null;
        }


    }

    private class MyListAdapter extends ArrayAdapter<Entry>
    {
        public MyListAdapter()
        {
            super(viewMealsActivity.this, R.layout.item_view, foundEntries);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);

            }

            Entry e = foundEntries.get(position);
            TextView mName = (TextView)itemView.findViewById(R.id.itemViewMealName);
            mName.setText(e.getName());
            TextView mDate = (TextView)itemView.findViewById(R.id.itemViewDateTaken);
            mDate.setText((convertDate(e.getDateTaken(), "dd/MM/yyyy")));
            TextView mCreon = (TextView)itemView.findViewById(R.id.itemViewCreonTaken);
            mCreon.setText("Creon taken =" + e.getCreonTaken());
            TextView mNotes = (TextView)itemView.findViewById(R.id.itemViewNotes);
            mNotes.setText(e.getNotes());

            return itemView;
        }
    }


}
