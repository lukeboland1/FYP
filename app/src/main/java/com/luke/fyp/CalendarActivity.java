package com.luke.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {
    private CalendarView calendar;
    private ArrayList<Entry> mealRecords;
    private ArrayList<Entry> entries;
    private DatabaseAccess db;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        db = new DatabaseAccess(this);
        calendar = (CalendarView)findViewById(R.id.calendar);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 00);
        c.set(Calendar.MINUTE, 00);
        c.set(Calendar.SECOND, 00);
        long datetime1 = c.getTimeInMillis();
        c.add(Calendar.DATE, 1);
        long datetime2 = c.getTimeInMillis();

        entries = db.getAllEntries();
        mealRecords = new ArrayList<>();
        for(int i = 0; i < entries.size(); i++)
        {
            if(entries.get(i).isBetweenTwoDates(datetime1, datetime2))
            {
                mealRecords.add(entries.get(i));
            }
        }

        populateListView();


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth){
                //month++;
                long time1 = 0;
                long time2 = 0;
                date = year  + "/" + String.format("%02d", month) + "/" + dayOfMonth + " 00:00:00";
                String date2 = "";
                Calendar c = Calendar.getInstance();
                c.set(year, month, dayOfMonth, 00, 00, 00);
                time1 = c.getTimeInMillis();
                c.add(Calendar.DATE, 1);
                time2 = c.getTimeInMillis();
                mealRecords.clear();
                for(int i = 0; i < entries.size(); i++)
                {
                    if(entries.get(i).isBetweenTwoDates(time1, time2))
                    {
                        mealRecords.add(entries.get(i));
                    }
                }

                populateListView();

            }


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
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

        else if (id == R.id.sendDatabase1) {
            Intent intent = new Intent(getApplicationContext(), writeFileActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateListView()
    {
        ArrayAdapter<Entry> adapt = new MyListAdapter();
        ListView list =(ListView) findViewById(R.id.listViewItems);
        list.setAdapter(adapt);

    }

    private class MyListAdapter extends ArrayAdapter<Entry>
    {
        public MyListAdapter()
        {
            super(CalendarActivity.this, R.layout.item_view, mealRecords);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);

            }

            final Entry e = mealRecords.get(position);
            TextView mName = (TextView)itemView.findViewById(R.id.itemViewMealName);
            String names = "";
            if(e.getCombinations().size() > 0) {
                names+=(e.getCombinations().get(0).getName());
            }
            mName.setText(names);
            TextView mDate = (TextView)itemView.findViewById(R.id.itemViewDateTaken);
            mDate.setText((convertDate(e.getDateTaken(), "dd/MM/yyyy")));
            TextView mCreon = (TextView)itemView.findViewById(R.id.itemViewCreonTaken);
            mCreon.setText("Creon taken =" + e.getCreon10000taken());
            TextView mNotes = (TextView)itemView.findViewById(R.id.itemViewNotes);
            mNotes.setText(e.getNotes());
            String notes = "";
            for(int i = 0; i < e.getComponents().size(); i++)
            {
                notes+=" " + (e.getComponents().get(i).getName()) + " " + e.getComponents().get(i).getQuantity() + " " + e.getComponents().get(i).getServingType() + "\n";
            }
            mNotes.setText(notes);

            final ImageButton up = (ImageButton)itemView.findViewById(R.id.tooManyCreon);
            final ImageButton down = (ImageButton)itemView.findViewById(R.id.tooFewCreon);
            if(e.getResults() == 1){
                up.setImageResource(R.drawable.arrowupred);
                down.setImageResource(R.drawable.arrowdownwhite);
            }
            else if(e.getResults() == 2) {
                up.setImageResource(R.drawable.arrowupwhite);
                down.setImageResource(R.drawable.arrowdownred);
            }
            else
            {
                up.setImageResource(R.drawable.arrowupwhite);
                down.setImageResource(R.drawable.arrowdownwhite);
            }

            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for(int i = 0; i < entries.size(); i++)
                    {
                        if (entries.get(i).getId() == e.getId())
                        {

                            if (e.getResults() == 1)
                            {
                                db.updateResult(0, e.getId());
                                down.setImageResource(R.drawable.arrowdownwhite);
                                up.setImageResource(R.drawable.arrowupwhite);
                                entries.get(i).setResults(0);
                            }
                            else
                            {
                                db.updateResult(1, e.getId());
                                down.setImageResource(R.drawable.arrowdownwhite);
                                up.setImageResource(R.drawable.arrowupred);
                                entries.get(i).setResults(1);
                            }
                        }
                    }
                }
            });

            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int i = 0; i < entries.size(); i++)
                    {
                        if (entries.get(i).getId() == e.getId())
                        {

                            if (e.getResults() == 2)
                            {
                                db.updateResult(0, e.getId());
                                down.setImageResource(R.drawable.arrowdownwhite);
                                up.setImageResource(R.drawable.arrowupwhite);
                                entries.get(i).setResults(0);
                            }
                            else
                            {
                                db.updateResult(2, e.getId());
                                down.setImageResource(R.drawable.arrowdownred);
                                up.setImageResource(R.drawable.arrowupwhite);
                                entries.get(i).setResults(2);
                            }
                        }
                    }
                }
            });

            itemView.setTag(e.getId());
            up.setTag(e.getId());
            down.setTag(e.getId());


            return itemView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }
}
