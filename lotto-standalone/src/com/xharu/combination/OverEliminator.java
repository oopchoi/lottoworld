package com.xharu.combination;

import java.util.ArrayList;

/**
 * Created by oopchoi on 15. 10. 6..
 */
public class OverEliminator implements Eliminator
{
    @Override
    public ArrayList<ArrayList> remove(ArrayList<ArrayList> list, ArrayList overCount) {
        for(int i=list.size()-1; i>=0; i--) {
            ArrayList item = list.get(i);
            int[] count = new int[]{0,0,0,0,0};
            int continueCount = 1;
            for(int j=0; j<item.size(); j++){
                int x = (int) Math.floor((int) item.get(j) / 10);
                count[x] += 1;
            }
            if(count[0]>=(int)overCount.get(0) || count[1]>(int)overCount.get(0) || count[2]>(int)overCount.get(0) || count[3]>(int)overCount.get(0) || count[4]>(int)overCount.get(0)){
                list.remove(i);
            }
        }
        return list;
    }
}
