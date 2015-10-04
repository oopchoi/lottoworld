package com.xharu.combination;

import java.util.ArrayList;

/**
 * Created by bryan on 2015-10-02.
 */
public class FixElseEliminator implements Eliminator {
    @Override
    public ArrayList<ArrayList> remove(ArrayList<ArrayList> list, ArrayList fixList) {
        for(int i=list.size()-1; i>=0; i--) {
            ArrayList item = list.get(i);
            boolean flag = false;
            for (int j = 0; j < item.size(); j++) {
                int x = (int) item.get(j);
                for (int k = 0; k < fixList.size(); k++) {
                    int y = (int) fixList.get(k);
                    if (x == y) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    break;
                }
            }
            if(!flag){
                list.remove(i);
            }
        }
        return list;
    }

}
