import com.xharu.combination.*;
import com.xharu.util.ArrayListConverter;

import java.io.*;
import java.sql.*;
import java.util.*;

public class Main {

    // 회차별 당번
    private static ArrayList list = new ArrayList();

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
            // 고정수
            //ArrayList<Integer> fixNumbers = new ArrayList<Integer>();
            //fixNumbers.add(14);

            // 제외수 조합 제거
            //FullCombiner combiner = new FullCombiner();
            //completeCombo = combiner.combine(1, 45, 1, 6, exceptNumbers);
            //System.out.println("제외수 조합 제거 후 : " + completeCombo.size());

            // 고정수 없는 조합 제외기 생성
            //FixElseEliminator eliminatorFixElse = new FixElseEliminator();
            // 고정수 없는 조합 제거
            //completeCombo = eliminatorFixElse.remove(completeCombo, fixNumbers);
            //System.out.println("고정수 없는 조합 제거 후 : " + completeCombo.size());

            // 당번 목록 변환 (제거하기 위함)
            ArrayListConverter converter = new ArrayListConverter();
            ArrayList<ArrayList> exceptCombo = converter.convert(list, now);
            // 당번 조합 제외기 생성
            WonEliminator eliminatorWon = new WonEliminator();
            //boolean existCombo = checkWonCombo(exceptCombo, completeCombo);
            // 당번 조합 제거
            completeCombo = eliminatorWon.remove(completeCombo, exceptCombo);

            //boolean existCombo = checkWonCombo(exceptCombo, completeCombo);
            //System.out.println(existCombo);
            System.out.println("당번 조합 제거 후 : " + completeCombo.size());

            // 연번 조합 제외기 생성
            OrderEliminator orderEliminator = new OrderEliminator();
            ArrayList orderCount = new ArrayList();
            orderCount.add(1);
            orderCount.add(2);
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

            // 필터 조합 제외기 생성
            FilterEliminator filterEliminator = new FilterEliminator();
            ArrayList filterNumbers = new ArrayList();
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
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 1, 3);
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
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 1, 3);
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
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 1, 3);
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
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 1, 3);
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
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 1, 3);
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
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 1, 3);
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
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 1, 3);
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
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 1, 3);
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
            completeCombo = filterEliminator.remove(completeCombo, filterNumbers, 1, 3);
            System.out.println("콜드수 조합 제거 후 : " + completeCombo.size());




            insertCombo(completeCombo);


            // 최종 조합 결과 출력
//            printCombo(completeCombo);
//
            ArrayList fiveCombo = getRandomCombo(completeCombo, 10);
            printCombo(fiveCombo);

            ArrayList<Integer> allEspectNumber = getEspectCombo(completeCombo);
            allEspectNumber.sort(new NoAscCompare());
            System.out.println("예상번호 개수:"+allEspectNumber.size());
            for(int value:allEspectNumber){
                System.out.print(value + ",");
            }




        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    private static ArrayList getRandomCombo(ArrayList list, int count){
        ArrayList fiveCombo = new ArrayList();
        for(int i=0; i<count; i++){
            Random random = new Random();
            int randInt = (int) (Math.random() * list.size());
            fiveCombo.add(list.get(randInt));
        }
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
            // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub
            return arg0 > arg1 ? -1 : arg0 < arg1 ? 1:0;
        }

    }
}