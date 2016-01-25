package com.luke.fyp;

import java.util.ArrayList;

/**
 * Created by Luke on 18/01/2016.
 */
public class Combination extends Component {
    private ArrayList<I_Component> components;

    public Combination(ArrayList<I_Component> components)
    {
        this.components = components;
    }

    @Override
    public int getFatContent()
    {
        int num = 0;
        for(int i = 0; components.size() > i; i++)
        {
            num += components.get(i).getFatContent();
        }

        return num;

    }

    @Override
    public String getName()
    {
        String n = "";
        for(int i = 0; components.size() > i; i++)
        {
            n += components.get(i).getName();
        }
        return n;
    }


    public void addComponent(I_Component component)
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
