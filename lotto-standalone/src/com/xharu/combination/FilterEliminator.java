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
    public ArrayList<ArrayList> removeByArray(ArrayList<ArrayList> list, ArrayList filterList, int fromCount, int toCount){
        for(int k=0; k<filterList.size(); k++){
            int y[] = (int[])filterList.get(k);
            int yLength = y.length;
            for(int i=list.size()-1; i>=0; i--){
                int count = 0;
                ArrayList item = list.get(i);
                for(int j=0; j<item.size(); j++){
                    int x = (int)item.get(j);
                    for(int m=0; m<yLength; m++){
                        if(x==y[m]){
                            count++;
                        }
                    }
                }
                if(!(count>=fromCount && count<=toCount)){
                    list.remove(i);
                }
            }
        }
        return list;
    }

    /**
     * 제외수 필터
     * @param list
     * @param filterList
     * @return
     */
    public ArrayList<ArrayList> removeByArray(ArrayList<ArrayList> list, ArrayList filterList){
        for(int k=0; k<filterList.size(); k++){
            int y[] = (int[])filterList.get(k);
            int yLength = y.length;
            for(int i=list.size()-1; i>=0; i--){
                int count = 0;
                ArrayList item = list.get(i);
                boolean flag = false;
                for(int j=0; j<item.size(); j++){
                    int x = (int)item.get(j);
                    for(int m=0; m<yLength; m++){
                        if(x==y[m]){
                            flag = true;
                            break;
                        }
                    }
                    if(flag){
                        break;
                    }
                }
                if(flag){
                    list.remove(i);
                }
            }
        }
        return list;
    }
}
