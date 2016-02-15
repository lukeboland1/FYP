package com.luke.fyp;

import android.content.ComponentCallbacks;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class addCombination extends AppCompatActivity implements TextWatcher {
    private EditText mEdit;
    private AutoCompleteTextView myAutoComplete;
    private DatabaseAccess db;
    private ArrayList<Component> components;
    private ArrayList<Component> m;
    private Component component1;
    private EditText quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_combination);
        component1 = null;
        mEdit = (EditText) findViewById(R.id.combinationName);
        quantity = (EditText) findViewById(R.id.quantity);
        db = new DatabaseAccess(this);
        m = new ArrayList<>();
        myAutoComplete = (AutoCompleteTextView)findViewById(R.id.myautocomplete1);
        dropDownDisplayComponents();
    }

    public void dropDownDisplayComponents()
    {
        components = db.getAllComponents();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
            super(addCombination.this, R.layout.item_view, m);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if(itemView == null)
            {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);

            }

            Component mr = m.get(position);
            TextView mName = (TextView)itemView.findViewById(R.id.itemViewMealName);
            mName.setText(mr.getName());
            TextView mNotes = (TextView)itemView.findViewById(R.id.itemViewNotes);
            mNotes.setText("" + mr.getQuantity());


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
            component1.setQuantity(Integer.parseInt(quantity.getText().toString()));
            m.add(component1);
            populateListView();
            quantity.setText("");
        }
        myAutoComplete.setText("");
        quantity.setText("");
    }

    public void saveCombination(View v)
    {
        db.insertCombination(mEdit.getText().toString(), 0);
        int combID = db.getIDFromNameCombination(mEdit.getText().toString());
        for(int i = 0; i < m.size(); i++) {
            db.addComponentCombination(m.get(i).getID(), combID, m.get(i).getQuantity());
        }

        Toast.makeText(addCombination.this, "Added Combination", Toast.LENGTH_LONG).show();
        myAutoComplete.setText("");
        myAutoComplete.clearListSelection();
        m.clear();
        populateListView();

    }



}
