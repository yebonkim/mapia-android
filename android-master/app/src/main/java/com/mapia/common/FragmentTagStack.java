package com.mapia.common;

import java.util.ArrayList;

/**
 * Created by daehyun on 15. 6. 5..
 */
public class FragmentTagStack{
    private ArrayList<String> tagArrayList;
    private int topIndex;
    public FragmentTagStack(){
        super();
        this.topIndex = -1;
        this.tagArrayList = new ArrayList<String>();
    }

    public String getFront(){
        if(this.topIndex<0){
            return null;
        }
        return this.tagArrayList.get(this.topIndex);
    }

    public int getFrontIndex(){
        return this.topIndex;
    }

    public String getPrev(){
        if(this.topIndex<1){
            return null;
        }
        return this.tagArrayList.get(this.topIndex -1);
    }

    public int getSize(){
        return this.tagArrayList.size();
    }

    public String pop(){
        if(this.topIndex < 0){
            return null;
        }
        String s = this.tagArrayList.get(this.topIndex);
        this.tagArrayList.remove(this.topIndex);
        --this.topIndex;
        return s;
    }

    public void push(final String s){
        this.tagArrayList.add(s);
        ++this.topIndex;
    }
}
