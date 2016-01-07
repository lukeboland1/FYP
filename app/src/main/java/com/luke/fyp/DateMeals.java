package com.luke.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateMeals extends AppCompatActivity {
    private ArrayList<MealRecord> mealRecords;
    private DatabaseAccess db;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_meals);
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        TextView text = (TextView)findViewById(R.id.dateText);
        text.setText(date);
        String date2 = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        try{
        c.setTime(sdf.parse(date));
        c.add(Calendar.DATE, 1);  // number of days to add
        date2 = sdf.format(c.getTime());  // dt is now the new date
        db = new DatabaseAccess(this);
        //mealRecords = db.getMealRecordsFromDate();
            //populateListView();
        }
        catch(Exception e)
        {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date_meals, menu);
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

    private void populateListView()
    {
        ArrayAdapter<MealRecord> adapt = new MyListAdapter();
        ListView list =(ListView) findViewById(R.id.dateItemListView);
        list.setAdapter(adapt);

    }

    private class MyListAdapter extends ArrayAdapter<MealRecord>
    {
        public MyListAdapter()
        {
            super(DateMeals.this, R.layout.item_view, mealRecords);
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
            mDate.setText(date);
            TextView mCreon = (TextView)itemView.findViewById(R.id.itemViewCreonTaken);
            mCreon.setText("Creon taken =" + mr.getCreonTaken());
            TextView mNotes = (TextView)itemView.findViewById(R.id.itemViewNotes);
            mNotes.setText(mr.getNotes());


            return itemView;
        }
    }
}
