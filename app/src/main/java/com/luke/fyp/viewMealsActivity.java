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

public class viewMealsActivity extends AppCompatActivity implements TextWatcher {
    private DatabaseAccess db;
    private ArrayList<Meal> meals;
    private ArrayList<MealRecord> mealRecords;
    private AutoCompleteTextView myAutoComplete;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meals);
        db = new DatabaseAccess(this);

        myAutoComplete = (AutoCompleteTextView)findViewById(R.id.myautocomplete);

        dropDownDisplayMeals();
        //displayMeals();
    }

    public void dropDownDisplayMeals()
    {
        meals = db.getAllMeals();
        String[] items = new String[meals.size()];
        for(int i = 0; i < meals.size(); i++)
        {
            items[i] = (meals.get(i).getName());
        }
        myAutoComplete = (AutoCompleteTextView)findViewById(R.id.myautocomplete);

        myAutoComplete.addTextChangedListener(this);
        myAutoComplete.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items));
        myAutoComplete.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                String item = (String) arg0.getItemAtPosition(arg2);
                Meal m = new Meal();
                mealRecords = new ArrayList<MealRecord>();
                boolean found = false;
                for (int i = 0; i < meals.size() && !found; i++) {
                    if (meals.get(i).getName().equals(item)) {
                        mealRecords = db.getMealRecordsFromName(meals.get(i).getName());
                        found = true;
                    }
                }

                populateListView();

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

    }

    private void populateListView()
    {
        ArrayAdapter<MealRecord> adapt = new MyListAdapter();
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
            ArrayList<Meal> meal = db.getAllMeals();
            return meal;
        }


    }

    private class getMealRecords extends AsyncTask<String, Object, ArrayList<MealRecord>> {

        @Override
        protected ArrayList<MealRecord> doInBackground(String... params) {
            ArrayList<MealRecord> mealR = db.getMealRecordsFromName(params[0]);
            return mealR;
        }


    }

    private class MyListAdapter extends ArrayAdapter<MealRecord>
    {
        public MyListAdapter()
        {
            super(viewMealsActivity.this, R.layout.item_view, mealRecords);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);

            }

            MealRecord mr = mealRecords.get(position);
            TextView mName = (TextView)itemView.findViewById(R.id.itemViewMealName);
            mName.setText(mr.getName());
            TextView mDate = (TextView)itemView.findViewById(R.id.itemViewDateTaken);
            mDate.setText((convertDate(mr.getDateTaken(), "dd/MM/yyyy")));
            TextView mCreon = (TextView)itemView.findViewById(R.id.itemViewCreonTaken);
            mCreon.setText("Creon taken =" + mr.getCreonTaken());
            TextView mNotes = (TextView)itemView.findViewById(R.id.itemViewNotes);
            mNotes.setText(mr.getNotes());


            return itemView;
        }
    }


}
