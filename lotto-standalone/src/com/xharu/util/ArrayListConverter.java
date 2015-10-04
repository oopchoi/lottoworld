package com.xharu.util;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by bryan on 2015-10-01.
 */
public class ArrayListConverter {
    public ArrayList<ArrayList> convert(ArrayList<Map<String, Object>> list, int now){
        ArrayList<ArrayList> returnList = new ArrayList<>();
        // 회차가 역순으로 되어 있기 때문에 맵의 키를 위한 카운트 변수가 별도로 필요함.
        int cnt = now-list.size()+1;
        for(int i=list.size()-1; i>=0; i--){
            Map<String, Object> item = list.get(i);
            int [] numbers = (int[])item.get(cnt++ + "");
            ArrayList numberList = new ArrayList();
            // 보너스 번호는 제거
            for(int j=0; j<numbers.length-1; j++){
                numberList.add(numbers[j]);
            }
            returnList.add(numberList);
        }
        return returnList;
    }
}
