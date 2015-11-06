package com.xharu.combination;

import java.util.ArrayList;

/**
 * Created by oopchoi on 15. 10. 3..
 */
public class OrderEliminator implements Eliminator {
    @Override
    public ArrayList<ArrayList> remove(ArrayList<ArrayList> list, ArrayList orderCount) {
        for(int i=list.size()-1; i>=0; i--) {
            // 마지막 행 아이템
            ArrayList item = list.get(i);
            int continueCount = 1;
            boolean flag = false;
            // 들어있는 개수만큼 반복
            for(int j=1; j<item.size(); j++){
                // 이전 수
                int x = (int)item.get(j-1);
                // 지금 수
                int y = (int)item.get(j);
                // 전 수에서 1더한 값이 지금수와 같으면
                if(x+1==y){
                    // 카운트 증가
                    continueCount++;
                    //System.out.println("continueCount:"+continueCount);
                    // 카운트가 사용자가 입력한 수와 같으면
                    if(continueCount==(int)orderCount.get(0)){
                        // 제거 대상임
                        flag = true;
                        break;
                    }
                }else{
                    continueCount = 1;
                }

            }
            if(flag){
                list.remove(i);
            }
        }
        return list;
    }


}
