package com.xharu.combination;

import java.util.ArrayList;

/**
 * Created by oopchoi on 15. 10. 3..
 */
public class OrderEliminator implements Eliminator {
    @Override
    public ArrayList<ArrayList> remove(ArrayList<ArrayList> list, ArrayList orderCount) {
        for(int i=list.size()-1; i>=0; i--) {
            ArrayList item = list.get(i);
            int continueCount = 1;
            for(int j=0; j<item.size(); j++){
                int x = (int)item.get(j);
                if(j>0){
                    int y = (int)item.get(j-1)+1;
                    //System.out.println(x + ", " + y);
                    if(x==y){
                        continueCount++;
                        //System.out.println("continueCount:"+continueCount);
                        if(continueCount==(int)orderCount.get(0)){
                            break;
                        }
                    }else{
                        continueCount = 1;
                    }

                }
            }
            if(continueCount==(int)orderCount.get(0)){
                list.remove(i);
            }
        }
        return list;
    }

}
