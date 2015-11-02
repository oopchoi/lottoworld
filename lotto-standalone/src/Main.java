import com.xharu.combination.*;
import com.xharu.util.ArrayListConverter;

import java.io.*;
import java.sql.*;
import java.util.*;

/*
 * 1. test.txt 파일을 업데이트 한다
 * 2. 회귀별 3회 이상 미출 조합을 종합하여 필출 필터로 사용하고 고정 4수를 Fix4Maker.java로 만든다.
 * 3. 콜드수 필터를 만든다.
 * 4. 고정5수, 제외5수 0~3 필터를 만든다.
 * 5. 자동 0~4수 필터를 만든다.
* */
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
            // 1수 이상 필터를 우선해야 성능을 높일 수 있다.
//            filterNumbers.add(new int[]{40,20,27,34,37,14,17,1,5,26,4,8,13,18,31,43,25,39,7,44,45,11,19,3,10,15,36,2,12,24,33,35,29,38,16});
//            completeCombo = filterEliminator.removeByArray(completeCombo, filterNumbers, 3, 6);
//            System.out.println("1수 이상 필터링 후 조합 수 : " + completeCombo.size());
//
//            filterNumbers = new ArrayList();
//            filterNumbers.add(new int[]{2,4,6,11,19,22,23,31,33,34,40,45});
//            completeCombo = filterEliminator.removeByArray(completeCombo, filterNumbers, 2, 4);
//            System.out.println("1수 이상 필터링 후 조합 수 : " + completeCombo.size());

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
            System.out.println("3연번 이상 조합 제거 후 : " + completeCombo.size());

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

            // 0~3개 필터 (고정5수 제외5수, 콜드수, 세로라인 등)
            // 세로 라인을 제외하고 매주 갱신해야 함.
            filterNumbers = new ArrayList();
            filterNumbers.add(new int[]{7,10,17,30,29});

            completeCombo = filterEliminator.removeByArray(completeCombo, filterNumbers, 0, 3);
            System.out.println("0~3필터 제거 후 : " + completeCombo.size());

            // 0~4개 필터 (완전자동 조합)
            filterNumbers = new ArrayList();
            filterNumbers.add(new int[]{1,2,3,4,5,6});

            completeCombo = filterEliminator.removeByArray(completeCombo, filterNumbers, 0, 4);
            System.out.println("0~4필터 제거 후 : " + completeCombo.size());

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

            // 675회 고정수 그룹
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

        ArrayList returnCombo = new ArrayList();
        if(count>newList.size()) count = newList.size();
        for(int i=0; returnCombo.size()<count; i++){
            int randInt = (int) (Math.random() * newList.size());
            ArrayList item = (ArrayList)newList.get(randInt);
            boolean f = false;
            for(int j=0; j<returnCombo.size(); j++){
                ArrayList t0 = (ArrayList)returnCombo.get(j);
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
                returnCombo.add(item);
            }
        }

        totalCount += count;
        return returnCombo;
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