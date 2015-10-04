package com.xharu.combination;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by bryan on 2015-09-30.
 */
public class FullCombiner implements Combiner {
    @Override
    public ArrayList<ArrayList> combine(int min, int max, int nownum, int nummax, ArrayList<Integer> exceptionNum){
        ArrayList<Map> temp1=getCaseByCaseNums(min,max,nownum,nummax,exceptionNum);
        ArrayList<ArrayList> temp2=getFullCaseList(temp1);
        return temp2;
    }

    private ArrayList<ArrayList> getFullCaseList(ArrayList<Map> tg){
        if(tg == null) return null;
        ArrayList<ArrayList> ret = new ArrayList<ArrayList>();
        int i, j, k;
        ArrayList<ArrayList> temp;
        ArrayList<Integer> item;
        for(i=0; i<tg.size(); i++){
            if(tg.get(i).get("cases") != null){
                temp=getFullCaseList((ArrayList)tg.get(i).get("cases"));

                for(j=0; j<temp.size(); j++){
                    item=new ArrayList<Integer>();
                    item.add((Integer) tg.get(i).get("nowNum"));
                    for(k=0; k<temp.get(j).size(); k++){
                        item.add((Integer)temp.get(j).get(k));
                    }
                    ret.add(item);
                }
            } else {
                item=new ArrayList<Integer>();
                item.add((Integer)tg.get(i).get("nowNum"));
                ret.add(item);
            }
        }
        return ret;
    }


    private ArrayList<Integer> copyArrayList(ArrayList<Integer> tg){
        if(tg == null) return new ArrayList<Integer>();

        ArrayList<Integer> ret = new ArrayList<Integer>();
        int i;
        for(i=0; i<tg.size(); i++){
            ret.add(tg.get(i));
        }
        return ret;
    }
    private ArrayList<Integer> getCaseNums(int min, int max, ArrayList<Integer> exceptionNum){
        ArrayList<Integer> ret = new ArrayList<Integer>();
        int i;
        int j;
        boolean pass;
        for(i=min; i<=max; i++){
            if(exceptionNum!=null) {
                pass=false;
                for (j = 0; j < exceptionNum.size(); j++) {
                    if (exceptionNum.get(j) == i) {
                        pass = true;
                        break;
                    }
                }
                if (pass) {
                    continue;
                }
            }
            ret.add(i);
        }
        return ret;
    }
    private ArrayList getCaseByCaseNums(int min, int max, int nownum, int nummax, ArrayList<Integer> exceptionNum){
        ArrayList<Map> ret = new ArrayList<Map>();
        int i, j;
        ArrayList<Integer> sendEN = copyArrayList(exceptionNum);
        ArrayList<Integer> ls;
        Map item;

        ls=getCaseNums(min,max,exceptionNum); // 제외수를 제외한 나머지 숫자들
        if(ls == null) return null;

        for(j=0; j<ls.size(); j++){
            item = new HashMap<String, Object>();
            item.put("nowNum", ls.get(j));
            if(sendEN != null) {
                sendEN.add(ls.get(j));
            }
            if(nownum<nummax){
                item.put("cases", getCaseByCaseNums(min, max, nownum+1, nummax, sendEN));
            }
            ArrayList<Integer> temp = (ArrayList<Integer>)item.get("cases");
            if(temp!=null && temp.size()>0 || nownum==nummax){
                ret.add(item);
            }
        }
        return ret;
    }




}