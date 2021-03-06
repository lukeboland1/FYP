package com.luke.fyp;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Luke on 18/01/2016.
 */
public class Combination extends Component {
    private ArrayList<Component> components;

    public Combination(ArrayList<Component> components)
    {
        this.components = components;
    }

    public Combination(){};

    public ArrayList<Component> getComponents() {
        return components;
    }

    @Override
    public double getFatContent()
    {
        double num = 0;
        for(int i = 0; components.size() > i; i++)
        {
            num += components.get(i).getTotalFat();
        }

        return num;

    }

    public void setComponents(ArrayList<Component> components)
    {
        this.components = components;
    }

    public double getFat()
    {
        double num = 0;
        for(int i = 0; components.size() > i; i++)
        {
            num += components.get(i).getTotalFat();
        }

        return num;

    }


    public void addComponent(Component component)
    {
        components.add(component);
    }

    public void removeComponent(int id)
    {
        boolean tof = false;
        for(int i = 0; components.size() > i && !tof; i++)
        {
            if(components.get(i).getID() == id)
            {
                tof = true;
                components.remove(i);
            }
        }
    }
}
