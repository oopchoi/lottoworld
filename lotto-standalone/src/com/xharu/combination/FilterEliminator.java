package com.xharu.combination;

import java.util.ArrayList;

/**
 * Created by oopchoi on 15. 10. 3..
 */
public class FilterEliminator implements Eliminator {
    @Override
    public ArrayList<ArrayList> remove(ArrayList<ArrayList> list, ArrayList filterList) {
        return list;
    }

    public ArrayList<ArrayList> remove(ArrayList<ArrayList> list, ArrayList filterList, int fromCount, int toCount){
        for(int i=list.size()-1; i>=0; i--) {
            ArrayList item = list.get(i);
            int count = 0;
            for (int j = 0; j < item.size(); j++) {
                int x = (int) item.get(j);
                for(int k=0; k<filterList.size(); k++){
                    int y = (int)filterList.get(k);
                    if(x==y){
                        count++;
                    }
                }
            }
            if(!(count>=fromCount && count<=toCount)){
                list.remove(i);
            }
        }
        return list;
    }
}
