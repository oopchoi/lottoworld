import com.xharu.combination.*;
import com.xharu.util.ArrayListConverter;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Main {

    // 회차별 당번
    private static ArrayList list = new ArrayList();

    private static int totalCount = 0;

    public static void main(String[] args) {
        try {
            //insertFullCombination(); // 최초에 한번 디비에 전체 조합을 저장해둔다.

            BufferedReader in = new BufferedReader(new FileReader("/Users/oopchoi/development/Git/workspace_intellij/lottoworld/lotto-standalone/src/test.txt"));
            String s;
            int now = 0;
            boolean first = true;
            while ((s = in.readLine()) != null) {
                String[] split = s.split(",");

                Map<String, Object> map = new HashMap<String, Object>();
                int [] arr = {Integer.valueOf(split[1]), Integer.valueOf(split[2]), Integer.valueOf(split[3]), Integer.valueOf(split[4]), Integer.valueOf(split[5]), Integer.valueOf(split[6]), Integer.valueOf(split[7])};
                if(first){
                    now = Integer.valueOf(split[0]);
                    first = false;
                }
                map.put(split[0], arr);
                list.add(map);
            }
            System.out.println(list.size());
            in.close();

            // 최종 조합 변수 초기화
            ArrayList<ArrayList> completeCombo = null;
            completeCombo = getTableData("tbl_combination");

            // 제외수
            //ArrayList<Integer> exceptNumbers = new ArrayList<Integer>();
            //exceptNumbers.add(44);
            // 지정한 번호 -- 지정한 번호는 의미 없고 마지막에 고정수 조합 뽑는 로직이 중요!!
            //ArrayList<Integer> fixNumbers = new ArrayList<Integer>();
            //fixNumbers.add(5);
            //fixNumbers.add(12);

            // 제외수 조합 제거
            //FullCombiner combiner = new FullCombiner();
            //completeCombo = combiner.combine(1, 45, 1, 6, exceptNumbers);
            //System.out.println("제외수 조합 제거 후 : " + completeCombo.size());

            // 지정한 번호가 없는 조합 제외기 생성
            //FixElseEliminator eliminatorFixElse = new FixElseEliminator();
            //지정한 번호가 없는 조합 제거
            //completeCombo = eliminatorFixElse.remove(completeCombo, fixNumbers);
            //System.out.println("지정한 번호가 없는 조합 제거 후 : " + completeCombo.size());

            // 당번 목록 변환 (제거하기 위함)
            ArrayListConverter converter = new ArrayListConverter();
            ArrayList<ArrayList> exceptCombo = converter.convert(list, now);
            System.out.println("필터링 전 총 조합 수 : " + completeCombo.size());

            // 필터 조합 제외기 생성
            FilterEliminator filterEliminator = new FilterEliminator();
            ArrayList filterNumbers = new ArrayList();
            filterNumbers.add(new int[]{40,20,27,34,37,14,17,1,5,26,4,8,13,18,31,43,25,39,7,44,45,11,19,3,10,15,36,2,12,24,33,35,29,38,16});
            completeCombo = filterEliminator.removeByArray(completeCombo, filterNumbers, 3, 6);
            System.out.println("필수 필터링 후 조합 수 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            filterNumbers.add(new int[]{2,4,6,11,19,22,23,31,33,34,40,45});
            completeCombo = filterEliminator.removeByArray(completeCombo, filterNumbers, 2, 4);
            System.out.println("필수 필터링 후 조합 수 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            filterNumbers.add(new int[]{35,10,13,31,9});
            completeCombo = filterEliminator.removeByArray(completeCombo, filterNumbers, 1, 3);
            System.out.println("필수 필터링 후 조합 수 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            filterNumbers.add(new int[]{36,37,38,41,42});
            completeCombo = filterEliminator.removeByArray(completeCombo, filterNumbers, 1, 3);
            System.out.println("필수 필터링 후 조합 수 : " + completeCombo.size());


            // 당번 조합 제외기 생성
            WonEliminator eliminatorWon = new WonEliminator();

            // ############### 중요 #############################################
            // 당번 조합 제외기를 사용해서 당번 카운트가 특정 개수인 조합만 얻어내기
            // 매회 4개 매칭 조합은 최소 하나 이상 많으면 3~4개 조합이 존재한다.
            // 따라서 당번 조합 중에 4개만 매칭 되는 조합을 사용하여 1등 조합을 만들어낼 가능성이 높아진다.
            // 만약 4개만 매칭 될 회차를 안다면 4개 고정수를 사용하여 1등 조합을 만들어낼 가능성이 상당히 높아진다.
            // 과연 어떻게 하면 4개만 매칭 될 기존 회차를 찾아낼 수 있을까?
            //ArrayList only4List = eliminatorWon.getOnlyCount(4, completeCombo, exceptCombo);
            completeCombo = eliminatorWon.getOnlyCount(4, completeCombo, exceptCombo);
            System.out.println("회귀별 당번 4개 매칭 조합 수 : " + completeCombo.size());
            //printCombo(only4List);
            // ############### 중요 #############################################


            //boolean existCombo = checkWonCombo(exceptCombo, completeCombo);
            // 당번 조합 제거
            //completeCombo = eliminatorWon.remove(completeCombo, exceptCombo);

            //boolean existCombo = checkWonCombo(exceptCombo, completeCombo);
            //System.out.println(existCombo);
            //System.out.println("당번 조합 제거 후 : " + completeCombo.size());

            // 회차당 당번 조합 번호 중 각 5개 이상 매치되는 조합은 제거
            // 보너스볼이 당번으로 넘어오는 기준일 때 4개 이하 매치되는 조합은 매회마다 존재하므로 1등하려면 5개 이상으로 잡아야 함.
            completeCombo = eliminatorWon.remove(5, completeCombo, exceptCombo);
            System.out.println("당번 조합 5개 이상 매칭 제거 후 : " + completeCombo.size());

            // 연번 조합 제외기 생성
            OrderEliminator orderEliminator = new OrderEliminator();
            ArrayList orderCount = new ArrayList();
            orderCount.add(3);
            // 3연번 조합은 제거
            completeCombo = orderEliminator.remove(completeCombo, orderCount);
            System.out.println("연번 조합 제거 후 : " + completeCombo.size());

            OverEliminator overEliminator = new OverEliminator();
            ArrayList overCount = new ArrayList();
            overCount.add(4);
            // 한 번대에 4개 이상인 조합 제거
            completeCombo = overEliminator.remove(completeCombo, overCount);
            System.out.println("한 번대에 4개 이상인 조합 제거 후 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            // 2,5,7,11,13,17,19,23,29,31,37,41,43 : 소수
            filterNumbers.add(2);
            filterNumbers.add(5);
            filterNumbers.add(7);
            filterNumbers.add(11);
            filterNumbers.add(13);
            filterNumbers.add(17);
            filterNumbers.add(19);
            filterNumbers.add(23);
            filterNumbers.add(29);
            filterNumbers.add(31);
            filterNumbers.add(37);
            filterNumbers.add(41);
            filterNumbers.add(43);
            // 필터 해당 조합 외 제거
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 0, 4);
            System.out.println("소수 조합 제거 후 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            // 3,6,9,12,15,18,21,24,27,30,33,36,39,42,45 : 3배수
            filterNumbers.add(3);
            filterNumbers.add(6);
            filterNumbers.add(9);
            filterNumbers.add(12);
            filterNumbers.add(15);
            filterNumbers.add(18);
            filterNumbers.add(21);
            filterNumbers.add(24);
            filterNumbers.add(27);
            filterNumbers.add(30);
            filterNumbers.add(33);
            filterNumbers.add(36);
            filterNumbers.add(39);
            filterNumbers.add(42);
            filterNumbers.add(45);
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 0, 4);
            System.out.println("3배수 조합 제거 후 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            // 1,4,8,10,14,16,20,22,25,26,28,32,34,35,38,40,44 : 합성수
            filterNumbers.add(1);
            filterNumbers.add(4);
            filterNumbers.add(8);
            filterNumbers.add(10);
            filterNumbers.add(14);
            filterNumbers.add(16);
            filterNumbers.add(20);
            filterNumbers.add(22);
            filterNumbers.add(25);
            filterNumbers.add(26);
            filterNumbers.add(28);
            filterNumbers.add(32);
            filterNumbers.add(34);
            filterNumbers.add(35);
            filterNumbers.add(38);
            filterNumbers.add(40);
            filterNumbers.add(44);
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 0, 4);
            System.out.println("합성수 조합 제거 후 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            //06, 09, 12, 21, 13, 31, 14, 41, 23, 32, 24, 42, 34, 43
            filterNumbers.add(6);
            filterNumbers.add(9);
            filterNumbers.add(12);
            filterNumbers.add(21);
            filterNumbers.add(13);
            filterNumbers.add(31);
            filterNumbers.add(14);
            filterNumbers.add(41);
            filterNumbers.add(23);
            filterNumbers.add(32);
            filterNumbers.add(24);
            filterNumbers.add(42);
            filterNumbers.add(34);
            filterNumbers.add(43);
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 0, 4);
            System.out.println("동형수 조합 제거 후 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            // 04,08,12,16,20,24,28,32,36,40,44
            filterNumbers.add(4);
            filterNumbers.add(8);
            filterNumbers.add(12);
            filterNumbers.add(16);
            filterNumbers.add(20);
            filterNumbers.add(24);
            filterNumbers.add(28);
            filterNumbers.add(32);
            filterNumbers.add(36);
            filterNumbers.add(40);
            filterNumbers.add(44);
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 0, 4);
            System.out.println("4배수 조합 제거 후 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            // 05,10,15,20,25,30,35,40,45
            filterNumbers.add(5);
            filterNumbers.add(10);
            filterNumbers.add(15);
            filterNumbers.add(20);
            filterNumbers.add(25);
            filterNumbers.add(30);
            filterNumbers.add(35);
            filterNumbers.add(40);
            filterNumbers.add(45);
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 0, 4);
            System.out.println("5배수 조합 제거 후 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            // 01,03,04,06,10,15,21,28,36,45
            filterNumbers.add(1);
            filterNumbers.add(3);
            filterNumbers.add(4);
            filterNumbers.add(6);
            filterNumbers.add(10);
            filterNumbers.add(15);
            filterNumbers.add(21);
            filterNumbers.add(28);
            filterNumbers.add(36);
            filterNumbers.add(45);
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 0, 4);
            System.out.println("삼각수 조합 제거 후 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            // 01,07,09,13,17,19,25,31,33,37,41,43
            filterNumbers.add(1);
            filterNumbers.add(7);
            filterNumbers.add(9);
            filterNumbers.add(13);
            filterNumbers.add(17);
            filterNumbers.add(19);
            filterNumbers.add(25);
            filterNumbers.add(31);
            filterNumbers.add(33);
            filterNumbers.add(37);
            filterNumbers.add(41);
            filterNumbers.add(43);
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 0, 4);
            System.out.println("대각선 조합 제거 후 : " + completeCombo.size());

            filterNumbers = new ArrayList();
            //01 13 16 21 22 23 30 31 34 35 39 45
            filterNumbers.add(1);
            filterNumbers.add(13);
            filterNumbers.add(16);
            filterNumbers.add(21);
            filterNumbers.add(22);
            filterNumbers.add(23);
            filterNumbers.add(30);
            filterNumbers.add(31);
            filterNumbers.add(34);
            filterNumbers.add(35);
            filterNumbers.add(39);
            filterNumbers.add(45);
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 0, 3);
            System.out.println("콜드수 조합 제거 후 : " + completeCombo.size());

            // 0~2개 필터
            filterNumbers = new ArrayList();
            filterNumbers.add(new int[]{2,7,8,11,12});
            filterNumbers.add(new int[]{4,10,21,9,18,14});
            filterNumbers.add(new int[]{17,28,26,20,15,6,23});
            filterNumbers.add(new int[]{29,31,13,27,37,19,39});
            filterNumbers.add(new int[]{33,36,40,42,39});
            filterNumbers.add(new int[]{44,45,35,41,38,32,39});
            filterNumbers.add(new int[]{5,43,24,25,3,16,23,39,1});
            filterNumbers.add(new int[]{2,6,12,15,32,42});
            filterNumbers.add(new int[]{1,23,25,38,41});
            filterNumbers.add(new int[]{3,18,26,34,40});
            completeCombo = filterEliminator.removeByArray(completeCombo, filterNumbers, 0, 3);
            System.out.println("제외/고정 5수 조합 제거 후 : " + completeCombo.size());


            // 0~3개 필터
            filterNumbers = new ArrayList();
            filterNumbers.add(new int[]{5,7,8,9,10,13,17,21,24,28,29,31,33,35,36,43,44,45});
            filterNumbers.add(new int[]{1,8,15,22,29,36,43});
            filterNumbers.add(new int[]{2,9,16,23,30,37,44});
            filterNumbers.add(new int[]{3,10,17,24,31,38,45});
            filterNumbers.add(new int[]{4,11,18,25,32,39});
            filterNumbers.add(new int[]{5,12,19,26,33,40});
            filterNumbers.add(new int[]{6,13,20,27,34,41});
            filterNumbers.add(new int[]{7,14,21,28,35,42});

            filterNumbers.add(new int[]{4,14,15,23,25,41});
            filterNumbers.add(new int[]{1,3,22,24,25,38});
            filterNumbers.add(new int[]{2,4,14,18,20,27});
            filterNumbers.add(new int[]{1,6,7,22,43,44});
            filterNumbers.add(new int[]{4,5,6,15,21,30});
            filterNumbers.add(new int[]{4,12,24,27,35,45});
            filterNumbers.add(new int[]{1,10,13,30,31,33});
            filterNumbers.add(new int[]{10,11,20,29,38,44});
            filterNumbers.add(new int[]{6,8,12,18,31,37});
            filterNumbers.add(new int[]{2,6,7,15,23,27});
            filterNumbers.add(new int[]{2,3,10,28,37,40});
            filterNumbers.add(new int[]{5,10,12,27,30,40});
            filterNumbers.add(new int[]{8,25,34,35,37,44});
            filterNumbers.add(new int[]{11,18,20,22,27,33});
            filterNumbers.add(new int[]{1,5,13,25,37,44});
            filterNumbers.add(new int[]{6,14,18,21,34,43});
            filterNumbers.add(new int[]{9,16,17,20,36,39});
            filterNumbers.add(new int[]{2,10,15,16,19,36});
            filterNumbers.add(new int[]{13,15,23,33,35,37});
            filterNumbers.add(new int[]{4,14,18,24,41,43});
            filterNumbers.add(new int[]{2,12,23,29,34,43});
            filterNumbers.add(new int[]{14,19,33,38,39,41});
            filterNumbers.add(new int[]{6,16,17,23,34,40});
            filterNumbers.add(new int[]{2,22,35,36,44,45});
            filterNumbers.add(new int[]{4,8,21,24,31,45});
            filterNumbers.add(new int[]{8,26,27,29,33,39});
            filterNumbers.add(new int[]{8,11,28,29,38,45});
            filterNumbers.add(new int[]{3,11,21,22,25,43});
            filterNumbers.add(new int[]{1,16,18,20,34,45});
            filterNumbers.add(new int[]{1,11,25,26,43,45});
            filterNumbers.add(new int[]{2,5,10,18,31,44});
            filterNumbers.add(new int[]{9,19,25,32,35,37});
            filterNumbers.add(new int[]{3,6,15,38,41,44});
            filterNumbers.add(new int[]{5,7,13,28,34,44});
            filterNumbers.add(new int[]{19,22,32,33,40,42});
            filterNumbers.add(new int[]{1,2,4,16,30,38});
            filterNumbers.add(new int[]{5,6,12,23,35,41});
            filterNumbers.add(new int[]{4,8,17,19,23,27});
            filterNumbers.add(new int[]{3,8,28,31,33,41});
            filterNumbers.add(new int[]{2,6,7,10,35,40});
            filterNumbers.add(new int[]{7,16,18,29,41,43});
            filterNumbers.add(new int[]{2,3,9,18,19,24});
            filterNumbers.add(new int[]{3,6,11,34,38,44});
            filterNumbers.add(new int[]{7,17,28,30,43,44});
            filterNumbers.add(new int[]{1,18,25,35,41,42});
            filterNumbers.add(new int[]{5,14,15,16,27,35});
            filterNumbers.add(new int[]{6,18,29,32,33,42});
            filterNumbers.add(new int[]{16,17,18,24,26,28});
            filterNumbers.add(new int[]{13,22,36,37,39,41});
            filterNumbers.add(new int[]{7,20,26,31,33,41});
            filterNumbers.add(new int[]{5,12,19,29,31,45});
            filterNumbers.add(new int[]{6,10,25,28,30,35});
            filterNumbers.add(new int[]{23,28,31,34,37,38});
            filterNumbers.add(new int[]{19,22,32,33,40,42});
            filterNumbers.add(new int[]{1,2,4,16,30,38});
            filterNumbers.add(new int[]{5,6,12,23,35,41});
            filterNumbers.add(new int[]{4,8,17,19,23,27});
            filterNumbers.add(new int[]{3,8,28,31,33,41});
            filterNumbers.add(new int[]{2,6,7,10,35,40});
            filterNumbers.add(new int[]{7,16,18,29,41,43});
            filterNumbers.add(new int[]{2,3,9,18,19,24});
            filterNumbers.add(new int[]{3,6,11,34,38,44});
            filterNumbers.add(new int[]{7,17,28,30,43,44});
            filterNumbers.add(new int[]{20,25,27,34,36,42});
            filterNumbers.add(new int[]{5,17,22,39,42,43});
            filterNumbers.add(new int[]{10,17,20,23,36,40});
            filterNumbers.add(new int[]{2,13,20,29,32,33});
            filterNumbers.add(new int[]{5,11,16,26,33,42});
            filterNumbers.add(new int[]{1,7,19,23,33,42});
            filterNumbers.add(new int[]{7,11,25,35,37,38});
            filterNumbers.add(new int[]{19,20,21,25,29,36});
            filterNumbers.add(new int[]{2,4,6,20,37,38});
            filterNumbers.add(new int[]{19,20,24,34,43,45});
            filterNumbers.add(new int[]{9,26,33,34,39,40});
            filterNumbers.add(new int[]{4,17,21,27,34,37});
            filterNumbers.add(new int[]{9,10,11,23,33,35});
            filterNumbers.add(new int[]{3,7,11,12,27,38});
            filterNumbers.add(new int[]{10,12,20,21,27,33});
            filterNumbers.add(new int[]{6,18,23,24,39,43});
            filterNumbers.add(new int[]{1,6,18,20,22,25});
            filterNumbers.add(new int[]{14,16,22,26,30,44});
            filterNumbers.add(new int[]{6,7,9,21,40,42});
            filterNumbers.add(new int[]{24,25,29,32,34,38});
            filterNumbers.add(new int[]{4,14,28,29,30,33});
            filterNumbers.add(new int[]{5,6,15,26,34,39});
            filterNumbers.add(new int[]{16,22,23,32,39,44});
            filterNumbers.add(new int[]{5,13,23,26,38,40});
            filterNumbers.add(new int[]{18,25,30,34,38,45});
            filterNumbers.add(new int[]{18,23,30,34,36,43});
            filterNumbers.add(new int[]{16,21,27,39,43,44});
            filterNumbers.add(new int[]{12,13,15,18,23,30});
            filterNumbers.add(new int[]{1,3,9,14,32,41});
            filterNumbers.add(new int[]{4,8,11,13,14,36});
            filterNumbers.add(new int[]{6,7,11,33,34,43});
            filterNumbers.add(new int[]{4,7,8,19,34,41});
            filterNumbers.add(new int[]{6,13,28,36,42,45});
            filterNumbers.add(new int[]{12,14,16,28,33,41});

            filterNumbers.add(new int[]{15,17,26,31,4});
            filterNumbers.add(new int[]{6,15,22,34,39});
            filterNumbers.add(new int[]{16,18,32,31,19});
            filterNumbers.add(new int[]{22,23,38,42,16});
            filterNumbers.add(new int[]{4,6,8,12,34});
            filterNumbers.add(new int[]{8,17,22,34,42});
            filterNumbers.add(new int[]{6,11,26,32,40});
            filterNumbers.add(new int[]{6,15,25,30,41});
            filterNumbers.add(new int[]{44,7,6,15,39});
            filterNumbers.add(new int[]{40,17,43,7,12});
            filterNumbers.add(new int[]{40,15,4,11,31});
            filterNumbers.add(new int[]{16,15,21,42,4});
            filterNumbers.add(new int[]{6,11,13,25,42});
            filterNumbers.add(new int[]{6,11,16,30,43});
            filterNumbers.add(new int[]{28,36,37,40,8});
            filterNumbers.add(new int[]{6,14,3,8,37});
            filterNumbers.add(new int[]{33,37,44,11,41});
            filterNumbers.add(new int[]{6,11,12,32,40});
            filterNumbers.add(new int[]{10,12,8,35,44});
            filterNumbers.add(new int[]{2,13,16,21,42});
            filterNumbers.add(new int[]{1,2,11,34,22});
            filterNumbers.add(new int[]{23,3,33,27,11});
            filterNumbers.add(new int[]{1,10,40,13,29});
            filterNumbers.add(new int[]{20,13,44,33,41});
            filterNumbers.add(new int[]{2,4,6,27,29});
            filterNumbers.add(new int[]{2,12,1,21,26});
            filterNumbers.add(new int[]{9,13,22,35,39});
            filterNumbers.add(new int[]{26,39,1,40,28});
            filterNumbers.add(new int[]{2,22,6,15,24});
            filterNumbers.add(new int[]{3,12,32,39,42});
            filterNumbers.add(new int[]{17,14,2,28,35});
            filterNumbers.add(new int[]{22,23,27,4,6});
            filterNumbers.add(new int[]{2,10,18,23,4});
            filterNumbers.add(new int[]{15,4,28,34,40});
            filterNumbers.add(new int[]{11,42,43,1,5});
            filterNumbers.add(new int[]{29,10,44,1,22});
            filterNumbers.add(new int[]{29,44,4,18,32});
            filterNumbers.add(new int[]{6,15,26,34,44});
            filterNumbers.add(new int[]{16,21,25,23,27});
            filterNumbers.add(new int[]{14,27,41,22,9});
            filterNumbers.add(new int[]{5,13,26,34,35});
            filterNumbers.add(new int[]{10,14,27,32,40});
            filterNumbers.add(new int[]{11,6,27,44,13});
            filterNumbers.add(new int[]{15,16,34,31,17});
            filterNumbers.add(new int[]{25,28,33,31,12});
            filterNumbers.add(new int[]{37,14,18,16,4});
            filterNumbers.add(new int[]{2,14,20,41,34});
            filterNumbers.add(new int[]{25,17,8,6,5});
            filterNumbers.add(new int[]{17,20,9,1,34});
            filterNumbers.add(new int[]{6,8,16,26,28});
            filterNumbers.add(new int[]{8,11,21,25,26});
            filterNumbers.add(new int[]{1,12,17,19,38});
            filterNumbers.add(new int[]{36,10,15,26,45});
            filterNumbers.add(new int[]{22,25,33,3,6});
            filterNumbers.add(new int[]{1,8,11,20,38});
            filterNumbers.add(new int[]{22,26,40,34,30});
            filterNumbers.add(new int[]{5,13,27,35,41});
            filterNumbers.add(new int[]{6,13,20,27,34});
            filterNumbers.add(new int[]{4,6,25,27,29});
            filterNumbers.add(new int[]{5,12,19,27,35});
            filterNumbers.add(new int[]{9,12,42,24,43});
            filterNumbers.add(new int[]{5,9,11,15,23});
            filterNumbers.add(new int[]{23,30,32,35,40});
            filterNumbers.add(new int[]{30,12,13,27,35});
            filterNumbers.add(new int[]{15,18,19,37,43});
            filterNumbers.add(new int[]{14,6,19,43,24});
            filterNumbers.add(new int[]{4,12,28,30,41});
            filterNumbers.add(new int[]{9,11,15,22,36});
            filterNumbers.add(new int[]{1,13,22,39,43});
            filterNumbers.add(new int[]{7,12,21,24,45});
            filterNumbers.add(new int[]{4,9,14,17,23});
            filterNumbers.add(new int[]{1,9,11,19,30});
            filterNumbers.add(new int[]{5,14,16,24,30});
            filterNumbers.add(new int[]{39,6,8,22,1});
            filterNumbers.add(new int[]{25,42,13,1,16});
            filterNumbers.add(new int[]{21,12,27,44,4});
            filterNumbers.add(new int[]{2,7,31,38,44});
            filterNumbers.add(new int[]{7,11,27,38,44});
            filterNumbers.add(new int[]{6,19,36,30,2});
            filterNumbers.add(new int[]{41,33,15,17,18});
            filterNumbers.add(new int[]{22,23,24,26,32});
            filterNumbers.add(new int[]{1,10,11,12,39});
            filterNumbers.add(new int[]{6,22,38,17,11});
            filterNumbers.add(new int[]{23,10,15,45,35});
            filterNumbers.add(new int[]{3,4,11,32,43});
            filterNumbers.add(new int[]{10,20,24,33,41});
            filterNumbers.add(new int[]{2,11,23,12,22});
            filterNumbers.add(new int[]{22,17,38,26,20});
            filterNumbers.add(new int[]{6,15,27,31,41});
            filterNumbers.add(new int[]{25,31,40,42,45});
            filterNumbers.add(new int[]{2,12,22,44,40});
            filterNumbers.add(new int[]{3,9,16,33,39});
            filterNumbers.add(new int[]{3,5,17,18,32});
            filterNumbers.add(new int[]{24,14,37,40,18});
            filterNumbers.add(new int[]{6,41,24,12,23});
            filterNumbers.add(new int[]{4,27,35,37,44});
            filterNumbers.add(new int[]{6,8,19,17,39});
            filterNumbers.add(new int[]{4,6,22,24,39});
            filterNumbers.add(new int[]{4,16,25,37,43});
            filterNumbers.add(new int[]{6,27,43,17,32});
            filterNumbers.add(new int[]{4,42,35,39,16});
            filterNumbers.add(new int[]{6,12,30,22,11});
            filterNumbers.add(new int[]{15,22,23,16,43});
            filterNumbers.add(new int[]{9,12,22,31,42});
            filterNumbers.add(new int[]{20,27,34,37,40});
            filterNumbers.add(new int[]{6,7,11,15,35});
            filterNumbers.add(new int[]{12,2,11,1,38});
            filterNumbers.add(new int[]{16,18,3,12,29});
            filterNumbers.add(new int[]{1,6,30,34,40});
            filterNumbers.add(new int[]{19,4,6,45,21});
            filterNumbers.add(new int[]{18,4,16,19,39});
            filterNumbers.add(new int[]{1,3,10,22,34});
            filterNumbers.add(new int[]{7,27,43,9,34});
            filterNumbers.add(new int[]{27,24,37,19,40});
            filterNumbers.add(new int[]{3,20,24,28,38});
            filterNumbers.add(new int[]{6,20,28,38,39});
            filterNumbers.add(new int[]{16,21,2,13,44});
            filterNumbers.add(new int[]{12,20,9,34,27});
            filterNumbers.add(new int[]{22,30,27,28,26});
            filterNumbers.add(new int[]{2,5,7,20,33});
            filterNumbers.add(new int[]{14,27,28,32,45});
            filterNumbers.add(new int[]{8,18,28,38,42});
            filterNumbers.add(new int[]{6,12,22,37,38});
            filterNumbers.add(new int[]{4,19,20,34,37});
            filterNumbers.add(new int[]{8,13,17,27,36});
            filterNumbers.add(new int[]{1,19,30,27,32});
            filterNumbers.add(new int[]{24,22,3,23,29});
            filterNumbers.add(new int[]{16,17,18,19,20});
            filterNumbers.add(new int[]{1,3,6,8,14});
            filterNumbers.add(new int[]{27,2,3,37,41});
            filterNumbers.add(new int[]{11,35,22,19,15});
            filterNumbers.add(new int[]{4,6,13,14,29});
            filterNumbers.add(new int[]{3,14,43,39,15});
            filterNumbers.add(new int[]{28,8,13,43,15});
            filterNumbers.add(new int[]{35,34,13,18,19});
            filterNumbers.add(new int[]{28,14,15,38,6});
            filterNumbers.add(new int[]{11,18,25,27,38});
            filterNumbers.add(new int[]{1,2,3,4,5});
            filterNumbers.add(new int[]{44,24,4,37,17});
            filterNumbers.add(new int[]{21,28,2,11,35});
            filterNumbers.add(new int[]{6,17,8,31,41});
            filterNumbers.add(new int[]{8,22,29,32,30});
            filterNumbers.add(new int[]{1,12,14,20,40});
            filterNumbers.add(new int[]{12,13,21,29,34});
            filterNumbers.add(new int[]{8,11,17,23,21});
            filterNumbers.add(new int[]{30,11,17,40,5});
            filterNumbers.add(new int[]{3,5,14,17,20});
            filterNumbers.add(new int[]{8,11,28,31,41});
            filterNumbers.add(new int[]{3,39,30,15,19});
            filterNumbers.add(new int[]{3,11,16,39,37});
            filterNumbers.add(new int[]{1,23,2,44,17});
            filterNumbers.add(new int[]{34,12,16,24,13});
            filterNumbers.add(new int[]{13,16,17,34,25});
            filterNumbers.add(new int[]{8,15,18,29,33});
            filterNumbers.add(new int[]{23,21,24,2,42});
            filterNumbers.add(new int[]{39,34,14,4,15});
            filterNumbers.add(new int[]{30,32,1,29,41});
            filterNumbers.add(new int[]{14,22,26,40,11});
            filterNumbers.add(new int[]{6,11,12,30,34});
            filterNumbers.add(new int[]{8,11,26,41,1});
            filterNumbers.add(new int[]{11,14,6,1,38});
            filterNumbers.add(new int[]{40,37,17,30,12});
            filterNumbers.add(new int[]{4,36,44,11,13});
            filterNumbers.add(new int[]{3,16,18,27,30});
            filterNumbers.add(new int[]{3,12,21,30,32});
            filterNumbers.add(new int[]{43,41,40,42,39});
            filterNumbers.add(new int[]{27,37,8,13,33});
            filterNumbers.add(new int[]{2,10,22,32,42});
            filterNumbers.add(new int[]{1,2,3,4,5});
            filterNumbers.add(new int[]{12,14,15,19,29});
            filterNumbers.add(new int[]{6,14,19,30,39});
            filterNumbers.add(new int[]{27,9,22,6,26});
            filterNumbers.add(new int[]{4,9,21,40,45});
            filterNumbers.add(new int[]{1,12,16,22,34});
            filterNumbers.add(new int[]{25,34,6,16,4});
            filterNumbers.add(new int[]{22,45,32,34,13});
            filterNumbers.add(new int[]{19,27,39,21,4});
            filterNumbers.add(new int[]{10,11,31,32,33});
            filterNumbers.add(new int[]{3,13,31,35,42});
            filterNumbers.add(new int[]{7,3,23,30,44});
            filterNumbers.add(new int[]{22,16,1,39,30});
            filterNumbers.add(new int[]{4,12,42,34,22});
            filterNumbers.add(new int[]{5,6,7,8,9});
            filterNumbers.add(new int[]{3,19,21,25,28});
            filterNumbers.add(new int[]{19,43,42,39,11});
            filterNumbers.add(new int[]{24,17,10,33,31});
            filterNumbers.add(new int[]{21,26,28,35,37});
            filterNumbers.add(new int[]{4,17,33,34,43});
            filterNumbers.add(new int[]{4,16,20,28,41});
            filterNumbers.add(new int[]{9,10,32,37,40});
            filterNumbers.add(new int[]{5,4,3,2,1});
            filterNumbers.add(new int[]{15,21,34,32,8});
            filterNumbers.add(new int[]{27,12,30,21,42});
            filterNumbers.add(new int[]{1,5,13,24,27});
            filterNumbers.add(new int[]{5,12,19,36,43});
            filterNumbers.add(new int[]{22,23,24,34,30});
            filterNumbers.add(new int[]{2,18,32,34,41});
            filterNumbers.add(new int[]{1,3,23,34,39});
            filterNumbers.add(new int[]{5,6,15,26,39});
            filterNumbers.add(new int[]{23,24,25,26,27});
            filterNumbers.add(new int[]{2,13,14,24,42});
            filterNumbers.add(new int[]{2,22,32,18,17});
            filterNumbers.add(new int[]{5,35,40,30,8});
            filterNumbers.add(new int[]{3,4,5,6,11});
            filterNumbers.add(new int[]{19,33,37,27,23});
            filterNumbers.add(new int[]{2,11,20,29,38});
            filterNumbers.add(new int[]{16,20,21,2,28});
            filterNumbers.add(new int[]{10,20,30,40,26});
            filterNumbers.add(new int[]{8,34,13,18,2});
            filterNumbers.add(new int[]{6,16,26,36,45});
            filterNumbers.add(new int[]{26,33,16,30,41});
            filterNumbers.add(new int[]{15,25,37,42,38});
            filterNumbers.add(new int[]{5,14,23,32,41});
            filterNumbers.add(new int[]{37,38,39,2,8});
            filterNumbers.add(new int[]{9,24,11,32,41});
            filterNumbers.add(new int[]{13,23,25,36,39});
            filterNumbers.add(new int[]{2,5,22,24,25});
            filterNumbers.add(new int[]{10,38,8,20,23});
            filterNumbers.add(new int[]{32,22,39,27,13});
            filterNumbers.add(new int[]{24,28,32,39,40});
            filterNumbers.add(new int[]{10,14,34,32,26});
            filterNumbers.add(new int[]{8,10,12,38,44});
            filterNumbers.add(new int[]{34,30,6,18,43});
            filterNumbers.add(new int[]{9,18,27,36,45});
            filterNumbers.add(new int[]{38,22,15,16,29});
            filterNumbers.add(new int[]{8,18,28,30,38});
            filterNumbers.add(new int[]{3,11,12,42,30});
            filterNumbers.add(new int[]{7,20,29,44,45});
            filterNumbers.add(new int[]{2,4,12,26,40});
            filterNumbers.add(new int[]{4,19,20,32,40});
            filterNumbers.add(new int[]{2,18,22,27,38});
            filterNumbers.add(new int[]{1,5,26,12,29});
            filterNumbers.add(new int[]{11,25,32,39,38});
            filterNumbers.add(new int[]{6,12,17,34,38});
            filterNumbers.add(new int[]{8,24,26,37,7});
            filterNumbers.add(new int[]{41,42,43,44,45});
            filterNumbers.add(new int[]{14,32,43,3,34});
            filterNumbers.add(new int[]{5,15,18,22,23});
            filterNumbers.add(new int[]{38,45,23,26,18});
            filterNumbers.add(new int[]{31,7,17,10,44});
            filterNumbers.add(new int[]{5,6,12,30,41});
            filterNumbers.add(new int[]{8,11,27,37,45});
            filterNumbers.add(new int[]{6,9,21,23,25});
            filterNumbers.add(new int[]{2,12,22,13,39});
            filterNumbers.add(new int[]{8,12,27,37,42});
            filterNumbers.add(new int[]{2,12,6,23,38});
            filterNumbers.add(new int[]{1,2,13,14,21});
            filterNumbers.add(new int[]{45,33,37,13,19});
            filterNumbers.add(new int[]{19,30,39,21,22});
            filterNumbers.add(new int[]{8,32,6,45,30});
            filterNumbers.add(new int[]{5,6,4,2,9});
            filterNumbers.add(new int[]{6,19,21,25,36});
            filterNumbers.add(new int[]{24,28,34,7,10});
            filterNumbers.add(new int[]{26,27,28,30,31});
            filterNumbers.add(new int[]{6,15,30,35,42});
            filterNumbers.add(new int[]{6,11,35,30,40});
            filterNumbers.add(new int[]{6,12,15,16,13});
            filterNumbers.add(new int[]{6,9,25,28,42});
            filterNumbers.add(new int[]{38,32,27,12,15});
            filterNumbers.add(new int[]{11,23,42,16,25});
            filterNumbers.add(new int[]{2,12,22,32,42});
            filterNumbers.add(new int[]{1,2,3,4,5});
            filterNumbers.add(new int[]{2,15,26,38,41});
            filterNumbers.add(new int[]{19,28,30,33,44});
            filterNumbers.add(new int[]{4,8,13,19,16});
            filterNumbers.add(new int[]{2,12,22,32,41});
            filterNumbers.add(new int[]{1,20,40,41,34});
            filterNumbers.add(new int[]{4,12,13,25,26});
            filterNumbers.add(new int[]{18,23,12,21,31});
            filterNumbers.add(new int[]{5,6,16,24,33});
            filterNumbers.add(new int[]{31,15,17,27,41});
            filterNumbers.add(new int[]{19,21,22,30,42});
            filterNumbers.add(new int[]{10,11,38,39,40});
            filterNumbers.add(new int[]{6,20,36,43,45});
            filterNumbers.add(new int[]{12,32,24,39,43});
            filterNumbers.add(new int[]{32,34,37,4,18});
            filterNumbers.add(new int[]{6,12,13,30,35});
            filterNumbers.add(new int[]{1,12,23,25,30});
            filterNumbers.add(new int[]{11,13,14,18,19});
            filterNumbers.add(new int[]{4,15,19,13,42});
            filterNumbers.add(new int[]{6,13,34,37,39});
            filterNumbers.add(new int[]{6,16,30,34,43});
            filterNumbers.add(new int[]{7,18,31,15,44});
            filterNumbers.add(new int[]{6,8,16,39,42});
            filterNumbers.add(new int[]{13,14,15,16,17});
            filterNumbers.add(new int[]{8,15,20,31,39});
            filterNumbers.add(new int[]{1,2,3,9,16});
            filterNumbers.add(new int[]{5,8,13,27,43});
            filterNumbers.add(new int[]{5,13,21,25,26});
            filterNumbers.add(new int[]{1,6,11,24,27});
            filterNumbers.add(new int[]{5,12,13,14,17});
            filterNumbers.add(new int[]{1,30,28,34,35});
            filterNumbers.add(new int[]{3,12,22,15,43});
            filterNumbers.add(new int[]{17,29,4,3,2});
            filterNumbers.add(new int[]{6,12,26,34,42});

            filterNumbers.add(new int[]{24,33,20,4,1});
            filterNumbers.add(new int[]{27,37,44,34,4});
            filterNumbers.add(new int[]{7,8,20,32,42});
            filterNumbers.add(new int[]{8,10,1,45,3});
            filterNumbers.add(new int[]{7,12,24,32,41});
            filterNumbers.add(new int[]{7,31,36,39,44});
            filterNumbers.add(new int[]{7,11,28,17,12});
            filterNumbers.add(new int[]{45,7,44,20,10});
            filterNumbers.add(new int[]{36,1,7,42,25});
            filterNumbers.add(new int[]{7,31,45,10,44});
            filterNumbers.add(new int[]{20,36,16,44,35});
            filterNumbers.add(new int[]{6,7,19,32,45});
            filterNumbers.add(new int[]{7,9,10,20,45});
            filterNumbers.add(new int[]{7,31,44,45,17});
            filterNumbers.add(new int[]{1,2,33,34,35});
            filterNumbers.add(new int[]{36,7,10,25,27});
            filterNumbers.add(new int[]{4,12,44,45,21});
            filterNumbers.add(new int[]{44,17,7,33,8});
            filterNumbers.add(new int[]{7,17,23,31,45});
            filterNumbers.add(new int[]{7,25,45,35,15});
            filterNumbers.add(new int[]{34,39,44,5,29});
            filterNumbers.add(new int[]{41,2,9,16,22});
            filterNumbers.add(new int[]{31,17,28,13,40});
            filterNumbers.add(new int[]{5,13,19,43,24});
            filterNumbers.add(new int[]{8,24,13,33,30});
            filterNumbers.add(new int[]{17,20,31,36,41});
            filterNumbers.add(new int[]{3,4,6,8,13});
            filterNumbers.add(new int[]{8,9,18,12,22});
            filterNumbers.add(new int[]{10,11,14,30,41});
            filterNumbers.add(new int[]{14,11,17,8,21});
            filterNumbers.add(new int[]{10,17,29,44,45});
            filterNumbers.add(new int[]{6,9,30,32,44});
            filterNumbers.add(new int[]{1,45,9,12,21});
            filterNumbers.add(new int[]{1,10,7,14,31});
            filterNumbers.add(new int[]{13,23,33,42,45});
            filterNumbers.add(new int[]{7,13,17,43,33});
            filterNumbers.add(new int[]{25,4,22,31,35});
            filterNumbers.add(new int[]{44,33,7,17,43});
            filterNumbers.add(new int[]{23,4,36,45,20});
            filterNumbers.add(new int[]{25,29,9,15,38});
            filterNumbers.add(new int[]{5,10,17,29,33});
            filterNumbers.add(new int[]{37,26,38,12,30});
            filterNumbers.add(new int[]{10,20,31,45,44});
            filterNumbers.add(new int[]{6,7,44,10,20});
            filterNumbers.add(new int[]{35,37,38,40,45});
            filterNumbers.add(new int[]{9,19,25,30,44});
            filterNumbers.add(new int[]{5,42,45,13,43});
            filterNumbers.add(new int[]{45,7,44,20,1});
            filterNumbers.add(new int[]{31,7,17,33,44});
            filterNumbers.add(new int[]{45,31,28,17,3});
            filterNumbers.add(new int[]{1,22,30,34,45});
            filterNumbers.add(new int[]{6,8,24,33,28});
            filterNumbers.add(new int[]{44,33,7,17,41});
            filterNumbers.add(new int[]{1,4,18,45,36});
            filterNumbers.add(new int[]{6,14,29,17,31});
            filterNumbers.add(new int[]{19,27,18,37,41});
            filterNumbers.add(new int[]{43,7,36,45,37});
            filterNumbers.add(new int[]{22,1,34,30,13});
            filterNumbers.add(new int[]{10,30,19,5,41});
            filterNumbers.add(new int[]{22,23,4,29,30});
            filterNumbers.add(new int[]{6,36,43,1,28});
            filterNumbers.add(new int[]{7,38,41,44,34});
            filterNumbers.add(new int[]{39,25,31,36,7});
            filterNumbers.add(new int[]{1,10,15,24,7});
            filterNumbers.add(new int[]{7,1,9,27,28});
            filterNumbers.add(new int[]{23,5,38,19,39});
            filterNumbers.add(new int[]{45,43,24,34,22});
            filterNumbers.add(new int[]{1,10,12,17,23});
            filterNumbers.add(new int[]{2,3,4,44,45});
            filterNumbers.add(new int[]{41,15,26,23,22});
            filterNumbers.add(new int[]{1,36,20,45,40});
            filterNumbers.add(new int[]{17,31,36,21,42});
            filterNumbers.add(new int[]{8,15,20,31,39});
            filterNumbers.add(new int[]{7,9,34,39,41});
            filterNumbers.add(new int[]{19,43,29,17,36});
            filterNumbers.add(new int[]{8,10,12,13,11});
            filterNumbers.add(new int[]{6,16,45,44,34});
            filterNumbers.add(new int[]{2,3,5,6,7});
            filterNumbers.add(new int[]{36,30,10,12,22});
            filterNumbers.add(new int[]{29,1,20,33,32});
            filterNumbers.add(new int[]{20,29,30,32,7});
            filterNumbers.add(new int[]{19,23,36,41,37});
            filterNumbers.add(new int[]{1,2,3,4,5});
            filterNumbers.add(new int[]{31,40,45,27,16});
            filterNumbers.add(new int[]{20,30,32,31,19});
            filterNumbers.add(new int[]{36,43,17,22,23});
            filterNumbers.add(new int[]{32,37,29,7,45});
            filterNumbers.add(new int[]{9,28,22,30,41});
            filterNumbers.add(new int[]{33,44,45,10,5});
            filterNumbers.add(new int[]{4,7,20,29,36});
            filterNumbers.add(new int[]{7,10,17,33,44});
            filterNumbers.add(new int[]{10,17,7,33,44});
            filterNumbers.add(new int[]{35,12,23,32,17});
            filterNumbers.add(new int[]{25,2,18,23,28});
            filterNumbers.add(new int[]{37,31,15,13,7});
            filterNumbers.add(new int[]{5,24,27,35,39});
            filterNumbers.add(new int[]{5,6,13,14,38});
            filterNumbers.add(new int[]{32,19,44,10,6});
            filterNumbers.add(new int[]{31,2,3,9,16});
            filterNumbers.add(new int[]{8,10,18,37,45});
            filterNumbers.add(new int[]{4,15,35,17,23});
            filterNumbers.add(new int[]{5,17,27,37,42});
            filterNumbers.add(new int[]{38,5,44,31,26});
            filterNumbers.add(new int[]{10,45,24,11,20});
            filterNumbers.add(new int[]{2,4,11,28,34});
            filterNumbers.add(new int[]{24,25,26,27,28});
            filterNumbers.add(new int[]{45,38,37,6,43});
            filterNumbers.add(new int[]{10,22,33,36,44});
            filterNumbers.add(new int[]{2,32,27,12,41});
            filterNumbers.add(new int[]{3,45,41,31,23});
            filterNumbers.add(new int[]{3,11,33,28,36});
            filterNumbers.add(new int[]{7,8,9,17,37});
            filterNumbers.add(new int[]{1,22,23,34,39});
            filterNumbers.add(new int[]{3,33,43,23,13});
            filterNumbers.add(new int[]{4,8,14,41,43});
            filterNumbers.add(new int[]{28,26,27,13,11});
            filterNumbers.add(new int[]{5,6,41,7,10});
            filterNumbers.add(new int[]{7,10,17,29,33});
            filterNumbers.add(new int[]{16,25,32,45,27});
            filterNumbers.add(new int[]{36,45,5,2,27});
            filterNumbers.add(new int[]{1,11,21,31,45});
            filterNumbers.add(new int[]{26,25,27,40,2});
            filterNumbers.add(new int[]{1,29,37,41,45});
            filterNumbers.add(new int[]{41,15,28,21,35});
            filterNumbers.add(new int[]{13,4,34,38,22});
            filterNumbers.add(new int[]{36,20,33,40,11});
            filterNumbers.add(new int[]{19,32,10,42,7});
            filterNumbers.add(new int[]{1,9,22,32,39});
            filterNumbers.add(new int[]{13,11,5,4,6});
            filterNumbers.add(new int[]{1,3,23,34,39});
            filterNumbers.add(new int[]{7,10,29,33,44});
            filterNumbers.add(new int[]{31,38,39,40,36});
            filterNumbers.add(new int[]{7,9,29,43,45});
            filterNumbers.add(new int[]{23,16,15,30,32});
            filterNumbers.add(new int[]{5,15,25,35,45});
            filterNumbers.add(new int[]{40,38,23,34,11});
            filterNumbers.add(new int[]{5,7,29,33,44});
            filterNumbers.add(new int[]{36,16,38,43,45});
            filterNumbers.add(new int[]{9,19,29,39,44});
            filterNumbers.add(new int[]{10,17,32,18,4});
            filterNumbers.add(new int[]{1,3,9,5,12});
            filterNumbers.add(new int[]{8,9,11,20,10});
            filterNumbers.add(new int[]{4,41,39,20,2});
            filterNumbers.add(new int[]{3,5,6,8,44});
            filterNumbers.add(new int[]{3,9,10,15,19});
            filterNumbers.add(new int[]{20,43,44,45,10});
            filterNumbers.add(new int[]{3,18,19,30,45});
            filterNumbers.add(new int[]{13,23,25,36,39});
            filterNumbers.add(new int[]{18,20,43,44,45});
            filterNumbers.add(new int[]{7,22,26,39,44});
            filterNumbers.add(new int[]{1,10,19,28,37});
            filterNumbers.add(new int[]{30,23,16,44,41});
            filterNumbers.add(new int[]{4,18,23,25,27});
            filterNumbers.add(new int[]{20,34,23,16,30});
            filterNumbers.add(new int[]{17,22,23,26,42});
            filterNumbers.add(new int[]{7,6,12,5,43});
            filterNumbers.add(new int[]{2,42,39,19,23});
            filterNumbers.add(new int[]{4,34,38,39,23});
            filterNumbers.add(new int[]{28,29,11,6,45});
            filterNumbers.add(new int[]{18,29,31,44,45});
            filterNumbers.add(new int[]{11,14,42,43,28});
            filterNumbers.add(new int[]{5,44,20,8,19});
            filterNumbers.add(new int[]{7,10,17,29,44});
            filterNumbers.add(new int[]{11,10,9,8,7});
            filterNumbers.add(new int[]{4,3,21,22,23});
            filterNumbers.add(new int[]{1,2,7,39,40});
            filterNumbers.add(new int[]{7,17,33,44,21});
            filterNumbers.add(new int[]{43,27,37,16,19});
            filterNumbers.add(new int[]{10,36,29,1,40});
            filterNumbers.add(new int[]{45,44,43,42,41});
            filterNumbers.add(new int[]{8,11,14,23,27});
            filterNumbers.add(new int[]{13,18,23,27,42});
            filterNumbers.add(new int[]{1,18,20,33,40});
            filterNumbers.add(new int[]{1,22,30,34,35});
            filterNumbers.add(new int[]{7,33,36,43,45});
            filterNumbers.add(new int[]{10,38,30,17,42});
            filterNumbers.add(new int[]{43,44,45,7,33});
            filterNumbers.add(new int[]{19,4,36,28,23});
            filterNumbers.add(new int[]{5,2,29,20,25});
            filterNumbers.add(new int[]{20,2,25,29,41});
            filterNumbers.add(new int[]{13,14,15,16,17});
            filterNumbers.add(new int[]{20,43,45,29,36});
            filterNumbers.add(new int[]{20,45,16,43,38});
            filterNumbers.add(new int[]{7,44,20,45,29});
            filterNumbers.add(new int[]{1,2,3,4,5});
            filterNumbers.add(new int[]{32,8,18,5,33});
            filterNumbers.add(new int[]{19,20,21,22,23});
            filterNumbers.add(new int[]{38,27,5,6,39});
            filterNumbers.add(new int[]{10,20,30,40,45});
            filterNumbers.add(new int[]{1,2,17,30,31});
            filterNumbers.add(new int[]{4,29,43,45,20});
            filterNumbers.add(new int[]{4,7,29,45,44});
            filterNumbers.add(new int[]{42,36,39,13,40});
            filterNumbers.add(new int[]{35,18,19,13,31});
            filterNumbers.add(new int[]{20,30,44,42,43});
            filterNumbers.add(new int[]{2,4,19,23,44});
            filterNumbers.add(new int[]{5,7,45,19,33});
            filterNumbers.add(new int[]{8,9,10,11,12});
            filterNumbers.add(new int[]{11,23,30,40,41});
            filterNumbers.add(new int[]{44,29,43,36,21});
            filterNumbers.add(new int[]{8,9,10,17,28});
            filterNumbers.add(new int[]{7,45,44,17,29});
            filterNumbers.add(new int[]{16,18,19,45,44});
            filterNumbers.add(new int[]{1,2,3,25,26});
            filterNumbers.add(new int[]{28,29,7,44,45});
            filterNumbers.add(new int[]{36,37,38,44,45});
            filterNumbers.add(new int[]{5,19,20,29,36});
            filterNumbers.add(new int[]{7,14,16,20,45});
            filterNumbers.add(new int[]{5,13,21,25,26});
            filterNumbers.add(new int[]{40,14,25,43,7});
            filterNumbers.add(new int[]{7,10,17,30,29});

            completeCombo = filterEliminator.removeByArray(completeCombo, filterNumbers, 0, 3);
            System.out.println("제외/고정 5수 조합 제거 후 : " + completeCombo.size());

//            filterNumbers = new ArrayList();
//            filterNumbers.add(new int[]{10,	17,	33,	44});
//
//            completeCombo = filterEliminator.removeByArray(completeCombo, filterNumbers, 4, 4);


            // 최종 조합 디비에 넣기
            //insertCombo(completeCombo);


            // 최종 조합 결과 출력
//            printCombo(completeCombo);
//

            ArrayList<Integer> allEspectNumber = getEspectCombo(completeCombo);
            allEspectNumber.sort(new NoAscCompare());
            System.out.println("예상번호 개수:"+allEspectNumber.size());
            for(int value:allEspectNumber){
                System.out.print(value + ",");
            }

            // 고정수 역할 배열
            //int [] alwaysNumbers = new int[]{4};
            // 고정수를 사용하여 랜덤 조합을 뽑을 때 completeCombo 를 변형 시키므로 이 후에 사용하면 안됨.
            //ArrayList fiveCombo = getRandomCombo(completeCombo, 10, alwaysNumbers);
            //printCombo(fiveCombo);


            // 673회 1개 고정수 그룹
            int [] arr = new int[]{35};
            ArrayList t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);
            arr = new int[]{10};
            t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);
            arr = new int[]{13};
            t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);
            arr = new int[]{31};
            t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);
            arr = new int[]{9};
            t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);
            arr = new int[]{36};
            t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);
            arr = new int[]{37};
            t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);
            arr = new int[]{38};
            t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);
            arr = new int[]{41};
            t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);
            arr = new int[]{42};
            t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);
            arr = new int[]{4};
            t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);
            arr = new int[]{34};
            t0 = getRandomCombo(completeCombo, 5, arr);
            printCombo(t0);


            System.out.println("---------- end -----------------");
            System.out.println("Total:" + completeCombo.size() + " 중에 " + totalCount + " 조합");






        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    private static ArrayList getRandomCombo(ArrayList list, int count, int [] alwaysNumbers){
        System.out.println("\nstart ------------------");
        System.out.println("- 고정수 -");
        for(int n=0; n<alwaysNumbers.length; n++) {
            int x = alwaysNumbers[n];
            if(alwaysNumbers.length>1)
                System.out.print(x + ", ");
            else
                System.out.print(x);
        }
        System.out.println("\n고정수 제거하기 전 list.size():"+list.size());

        ArrayList newList = new ArrayList();
        for(int m=list.size()-1; m>=0; m--){
            ArrayList item = (ArrayList)list.get(m);
            int cnt = 0;
            for(int n=0; n<alwaysNumbers.length; n++){
                int x = alwaysNumbers[n];
                for(int k=0; k<item.size(); k++){
                    int y = (int)item.get(k);
                    if(x==y){
                        cnt++;
                    }
                }
            }
            if(alwaysNumbers.length>cnt){
                //list.remove(m);
            }else{
                ArrayList newItem = new ArrayList();
                for(int k=0; k<item.size(); k++){
                    int y = (int)item.get(k);
                    newItem.add(y);
                }
                newList.add(newItem);
            }
        }

        System.out.println("고정수 제거한 후 newList.size():"+newList.size());

        ArrayList fiveCombo = new ArrayList();
        if(count>newList.size()) count = newList.size();
        for(int i=0; fiveCombo.size()<count; i++){
            int randInt = (int) (Math.random() * newList.size());
            ArrayList item = (ArrayList)newList.get(randInt);
            boolean f = false;
            for(int j=0; j<fiveCombo.size(); j++){
                ArrayList t0 = (ArrayList)fiveCombo.get(j);
                int c = 0;
                for(int m=0; m<item.size(); m++){
                    int x = (int)item.get(m);
                    for(int n=0; n<t0.size(); n++){
                        int y = (int)t0.get(n);
                        if(x==y) c++;
                    }
                }
                // c가 6이면 동일한 조합이므로
                if(c==6){
                    f=true;
                    break;
                }
            }
            if(i==0||!f) {
                fiveCombo.add(item);
            }
        }

        totalCount += count;
        return fiveCombo;
    }

    private static ArrayList getEspectCombo(ArrayList list){
        ArrayList result = new ArrayList();
        for(int i=0; i<list.size(); i++){
            ArrayList item = (ArrayList)list.get(i);
            for(int j=0; j<item.size(); j++){
                int x = (int)item.get(j);
                boolean existNum = false;
                for(int k=0; k<result.size(); k++){
                    if(x==(int)result.get(k)){
                        existNum = true;
                        break;
                    }
                }
                if(!existNum){
                    result.add(x);
                }
            }
        }
        return result;
    }

    private static ArrayList getTableData(String tableName){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/book_ex",
                    "zerock",
                    "zerock");

            String query = "select * from " + tableName;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            ArrayList list = new ArrayList();
            while (rs.next())
            {
                ArrayList item = new ArrayList();
                item.add(rs.getInt("num_1"));
                item.add(rs.getInt("num_2"));
                item.add(rs.getInt("num_3"));
                item.add(rs.getInt("num_4"));
                item.add(rs.getInt("num_5"));
                item.add(rs.getInt("num_6"));
                list.add(item);
            }
            st.close();
            return list;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * sum>=120&&sum<=180 조합번호 합계가 120~180 인 경우만 디비에 저장한다.
     * */
    private static void insertFullCombination(){
        FullCombiner combiner = new FullCombiner();
        ArrayList<ArrayList> completeCombo = combiner.combine(1, 45, 1, 6, new ArrayList<>());
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/book_ex",
                    "zerock",
                    "zerock");

            String query = "INSERT INTO tbl_sum_120_180 (num_1, num_2, num_3, num_4, num_5, num_6) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = conn.prepareStatement(query);

            int cnt = 0;
            for (int i=0; i<completeCombo.size(); i++) {
                int num_1 = 0, num_2 = 0, num_3 = 0, num_4 = 0, num_5 = 0, num_6 = 0;

                ArrayList item = completeCombo.get(i);
                int sum = 0;
                for(int j=0; j<item.size(); j++){
                    sum += (int)item.get(j);
                }
                //5270487
                //if(sum>=120&&sum<=180){
                    num_1 = (int)item.get(0);
                    num_2 = (int)item.get(1);
                    num_3 = (int)item.get(2);
                    num_4 = (int)item.get(3);
                    num_5 = (int)item.get(4);
                    num_6 = (int)item.get(5);
                    preparedStmt.setInt(1, num_1);
                    preparedStmt.setInt(2, num_2);
                    preparedStmt.setInt(3, num_3);
                    preparedStmt.setInt(4, num_4);
                    preparedStmt.setInt(5, num_5);
                    preparedStmt.setInt(6, num_6);
                    preparedStmt.execute();
                    cnt++;
                //}

            }
            System.out.println("cnt:" + cnt);

            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private static void insertCombo(ArrayList completeCombo){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/book_ex",
                    "zerock",
                    "zerock");

            String query = "INSERT INTO tbl_final_combination (num_1, num_2, num_3, num_4, num_5, num_6) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStmt = conn.prepareStatement(query);

            PreparedStatement truncateStmt = conn.prepareStatement("truncate tbl_final_combination");
            truncateStmt.execute();

            int cnt = 0;
            for (int i=0; i<completeCombo.size(); i++) {
                int num_1 = 0, num_2 = 0, num_3 = 0, num_4 = 0, num_5 = 0, num_6 = 0;

                ArrayList item = (ArrayList)completeCombo.get(i);
                int sum = 0;
                for(int j=0; j<item.size(); j++){
                    sum += (int)item.get(j);
                }
                //5270487
                if(sum>=120&&sum<=180){
                    num_1 = (int)item.get(0);
                    num_2 = (int)item.get(1);
                    num_3 = (int)item.get(2);
                    num_4 = (int)item.get(3);
                    num_5 = (int)item.get(4);
                    num_6 = (int)item.get(5);
                    preparedStmt.setInt(1, num_1);
                    preparedStmt.setInt(2, num_2);
                    preparedStmt.setInt(3, num_3);
                    preparedStmt.setInt(4, num_4);
                    preparedStmt.setInt(5, num_5);
                    preparedStmt.setInt(6, num_6);
                    preparedStmt.execute();
                    cnt++;
                }

            }
            System.out.println("cnt:" + cnt);

            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // 조합 목록 중에 지난 회차까지 당첨 조합이 있는지 체크하는 메소드
    private static boolean checkWonCombo(ArrayList full, ArrayList won){
        for(int i=0; i<full.size(); i++){
            ArrayList fullItem = (ArrayList)full.get(i);
            boolean flag = false;
            for(int j=0; j<fullItem.size(); j++){
                int x = (int)fullItem.get(j);
                for (int k=0; k<won.size(); k++){
                    ArrayList wonItem = (ArrayList)won.get(k);
                    int y = (int)wonItem.get(j);
                    if(x!=y){
                        flag = true;    // true면 조합이 당번에 없다는 의미임.
                        break;
                    }
                }
                if(flag){
                    break;
                }
            }
            if(!flag){
                return true;
            }

        }
        return false;
    }

    private static void printCombo(ArrayList<ArrayList> list){
        System.out.println("-- 조합 --");
        if(list == null){
            System.out.println("list is null");
        }
        int cnt = 0;
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("/Users/oopchoi/development/Git/workspace_intellij/lottoworld/lotto-standalone/src/test2.txt"));
            for(ArrayList i:list){
                for(int m=0; m<i.size(); m++) {
                    int x = (Integer)i.get(m);
                    if(m==i.size()-1){
                        System.out.print(x + "\n");
                        out.write(x + "\n");
                    }else{
                        System.out.print(x + ", ");
                        out.write(x + ", ");
                    }
                }
                cnt++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Count : "+cnt);
    }

    /**
     * No 오름차순
     */
    static class NoAscCompare implements Comparator<Integer> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(Integer arg0, Integer arg1) {
            return arg0 < arg1 ? -1 : arg0 > arg1 ? 1:0;
        }

    }

    /**
     * No 내림차순
     */
    static class NoDescCompare implements Comparator<Integer> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(Integer arg0, Integer arg1) {
            return arg0 > arg1 ? -1 : arg0 < arg1 ? 1:0;
        }

    }
}