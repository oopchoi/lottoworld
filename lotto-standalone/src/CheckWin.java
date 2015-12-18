import com.xharu.combination.FullCombiner;
import com.xharu.util.ArrayListConverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bryan on 2015-12-18.
 */
public class CheckWin {
    public static void main(String[] args) {
        // 최종 조합 변수 초기화
        ArrayList<ArrayList> completeCombo = null;
        completeCombo = getTableData("tbl_combination");
        // 680 회 당첨번호
        check(completeCombo, 4,10,19,29,32,42,30);
    }

    private static void check(ArrayList<ArrayList> completeCombo, int n0, int n1, int n2, int n3, int n4, int n5, int n6){
        int cnt_1 = 0, cnt_2 = 0, cnt_3 = 0, cnt_4 = 0, cnt_5 = 0;
        for(int i=0; i<completeCombo.size(); i++){
            ArrayList items = completeCombo.get(i);
            int cntWin = 0;
            boolean existBoball = false;
            for(int j=0; j<items.size(); j++){
                if((int)items.get(j)==n0 || (int)items.get(j)==n1 || (int)items.get(j)==n2 || (int)items.get(j)==n3 || (int)items.get(j)==n4 || (int)items.get(j)==n5 ){
                    cntWin++;
                }else if((int)items.get(j)==n6){
                    existBoball = true;
                }
            }
            if(cntWin==3){
                //System.out.println("5등");
                cnt_5++;
            }else if(cntWin==4){
                //System.out.println("4등");
                cnt_4++;
            }else if(cntWin==5){
                if(existBoball) {
                    //System.out.println("2등");
                    cnt_2++;
                }else{
                    //System.out.println("3등");
                    cnt_3++;
                }
            }else if(cntWin==6){
                //System.out.println("1등");
                cnt_1++;
            }
        }
        System.out.println("1등:"+cnt_1 + ",2등:" +cnt_2 + ",3등:" +cnt_3 + ",4등:" +cnt_4 + ",5등:" +cnt_5);
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
}
