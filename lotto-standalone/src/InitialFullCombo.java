import com.xharu.combination.FullCombiner;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * Created by bryan on 2015-10-08.
 */
public class InitialFullCombo {
    public static void main(String[] args) {
        try {
            insertFullCombination(); // 최초에 한번 디비에 전체 조합을 저장해둔다.
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
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

            PreparedStatement truncateStmt = conn.prepareStatement("truncate tbl_sum_120_180");
            truncateStmt.execute();

            System.out.println("Start!");

            int cnt = 0;
            for (int i=0; i<completeCombo.size(); i++) {
                int num_1 = 0, num_2 = 0, num_3 = 0, num_4 = 0, num_5 = 0, num_6 = 0;

                ArrayList item = completeCombo.get(i);
                int sum = 0;
                for(int j=0; j<item.size(); j++){
                    sum += (int)item.get(j);
                }
                // 전체 8,145,060 중에 5,270,487
                //if(sum>=120&&sum<=180){
                // 전체 8,145,060 중에 8,139,884
                //if(sum>=48&&sum<=238){
                // 전체 8,145,060 중에 7,926,281
                if(sum>=70&&sum<=200){
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
                    if(cnt%100000==0){
                        System.out.println(cnt);
                    }
                }

            }
            System.out.println("cnt:" + cnt);

            int start = 1;
            int end = cnt;
            int step = 10000;
            for(int i=start; i<=end+step; i+=step){
                CallableStatement stat = conn.prepareCall("call pc_set_num_filters(?, ?)");
                stat.setInt(1, i);
                stat.setInt(2, i*step);
                stat.execute();
                if(i%100000==0){
                    System.out.println(i);
                }
            }
            System.out.println("Finish!");
            conn.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
