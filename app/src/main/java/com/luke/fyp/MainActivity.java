package com.luke.fyp;

import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DatabaseAccess db;
    private LinearLayout button;
    private LinearLayout button1;
    private LinearLayout button2;
    private LinearLayout button3;
    private LinearLayout button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseAccess(this);
        new doesUserExist().execute();
        new setUser().execute();
        setContentView(R.layout.activity_main);

        button = (LinearLayout)findViewById(R.id.button);
        button1 = (LinearLayout)findViewById(R.id.button2);
        button2 = (LinearLayout)findViewById(R.id.button3);
        button3 = (LinearLayout)findViewById(R.id.button4);
        button4 = (LinearLayout)findViewById(R.id.button5);
        buttonEffect(button);
        buttonEffect(button1);
        buttonEffect(button2);
        buttonEffect(button3);
        buttonEffect(button4);


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        menu.add(0, 1, 0, "Add Item");
            // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe01E1E1E, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }

    public void entry(View v)
    {
        Intent intent = new Intent(getApplicationContext(), addEntryActivity.class);
        startActivity(intent);
    }

    public void meal(View v)
    {
        Intent intent = new Intent(getApplicationContext(), addCombination.class);
        startActivity(intent);
    }

    public void item(View v)
    {
        Intent intent = new Intent(getApplicationContext(), addComponent.class);
        startActivity(intent);
    }

    public void calendar(View v)
    {
        Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
        startActivity(intent);
    }

    public void records(View v)
    {
        Intent intent = new Intent(getApplicationContext(), viewMealsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
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

    private class doesUserExist extends AsyncTask<Integer, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            if(!db.doesUserExist())
            {
                Intent intent = new Intent(getApplicationContext(), firstWelcomeActivity.class);
                startActivity(intent);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

        }
    }


    private class setUser extends AsyncTask<Integer, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... params) {
            ((MyApp) MainActivity.this.getApplication()).setUser(db.getUser());
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

        }
    }
}
