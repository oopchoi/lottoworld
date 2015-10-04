package com.xharu.combination;

import com.xharu.combination.Eliminator;

import java.util.ArrayList;

/**
 * Created by bryan on 2015-10-01.
 */
public class WonEliminator implements Eliminator {
    @Override
    public ArrayList<ArrayList> remove(ArrayList<ArrayList> list, ArrayList exceptList) {
        for(int i=list.size()-1; i>=0; i--) {
            ArrayList item = list.get(i);
            for (int j = 0; j < exceptList.size(); j++) {
                ArrayList exceptItem = (ArrayList)exceptList.get(j);
                boolean flag = false;
                for(int k=0; k<exceptItem.size(); k++) {
                    int x = (int) item.get(k);
                    int y = (int) exceptItem.get(k);
                    if (x != y) {
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    list.remove(i);
                }
            }
        }
        return list;
    }
}
