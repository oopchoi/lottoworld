package com.xharu.combination;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by bryan on 2015-09-30.
 */
public interface Combiner {
    ArrayList<ArrayList> combine(int min, int max, int nownum, int nummax, ArrayList<Integer> exceptionNum);
}
