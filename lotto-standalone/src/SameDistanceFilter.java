import com.xharu.util.ArrayListConverter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bryan on 2015-12-18.
 */
public class SameDistanceFilter {
    // 회차별 당번
    private static ArrayList list = new ArrayList();
    private static ArrayList<ArrayList> dangCombination;

    public static void main(String[] args) {

        try {
            //insertFullCombination(); // 최초에 한번 디비에 전체 조합을 저장해둔다.

            BufferedReader in = new BufferedReader(new FileReader("D:\\workspace\\temp\\xharu\\src\\test.txt"));
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

            // 당번 목록 변환 (제거하기 위함)
            ArrayListConverter converter = new ArrayListConverter();
            dangCombination = converter.convert(list, now);

            deleteData(1,3,5,7);
            deleteData(2,4,6,8);
            deleteData(3,5,7,9);
            deleteData(4,6,8,10);
            deleteData(5,7,9,11);
            deleteData(6,8,10,12);
            deleteData(7,9,11,13);
            deleteData(8,10,12,14);
            deleteData(9,11,13,15);
            deleteData(10,12,14,16);
            deleteData(11,13,15,17);
            deleteData(12,14,16,18);
            deleteData(13,15,17,19);
            deleteData(14,16,18,20);
            deleteData(15,17,19,21);
            deleteData(16,18,20,22);
            deleteData(17,19,21,23);
            deleteData(18,20,22,24);
            deleteData(19,21,23,25);
            deleteData(20,22,24,26);
            deleteData(21,23,25,27);
            deleteData(22,24,26,28);
            deleteData(23,25,27,29);
            deleteData(24,26,28,30);
            deleteData(25,27,29,31);
            deleteData(26,28,30,32);
            deleteData(27,29,31,33);
            deleteData(28,30,32,34);
            deleteData(29,31,33,35);
            deleteData(30,32,34,36);
            deleteData(31,33,35,37);
            deleteData(32,34,36,38);
            deleteData(33,35,37,39);
            deleteData(34,36,38,40);
            deleteData(35,37,39,41);
            deleteData(36,38,40,42);
            deleteData(37,39,41,43);
            deleteData(38,40,42,44);
            deleteData(39,41,43,45);
            deleteData(1,4,7,10);
            deleteData(2,5,8,11);
            deleteData(3,6,9,12);
            deleteData(4,7,10,13);
            deleteData(5,8,11,14);
            deleteData(6,9,12,15);
            deleteData(7,10,13,16);
            deleteData(8,11,14,17);
            deleteData(9,12,15,18);
            deleteData(10,13,16,19);
            deleteData(11,14,17,20);
            deleteData(12,15,18,21);
            deleteData(13,16,19,22);
            deleteData(14,17,20,23);
            deleteData(15,18,21,24);
            deleteData(16,19,22,25);
            deleteData(17,20,23,26);
            deleteData(18,21,24,27);
            deleteData(19,22,25,28);
            deleteData(20,23,26,29);
            deleteData(21,24,27,30);
            deleteData(22,25,28,31);
            deleteData(23,26,29,32);
            deleteData(24,27,30,33);
            deleteData(25,28,31,34);
            deleteData(26,29,32,35);
            deleteData(27,30,33,36);
            deleteData(28,31,34,37);
            deleteData(29,32,35,38);
            deleteData(30,33,36,39);
            deleteData(31,34,37,40);
            deleteData(32,35,38,41);
            deleteData(33,36,39,42);
            deleteData(34,37,40,43);
            deleteData(35,38,41,44);
            deleteData(36,39,42,45);
            deleteData(1,5,9,13);
            deleteData(2,6,10,14);
            deleteData(3,7,11,15);
            deleteData(4,8,12,16);
            deleteData(5,9,13,17);
            deleteData(6,10,14,18);
            deleteData(7,11,15,19);
            deleteData(8,12,16,20);
            deleteData(9,13,17,21);
            deleteData(10,14,18,22);
            deleteData(11,15,19,23);
            deleteData(12,16,20,24);
            deleteData(13,17,21,25);
            deleteData(14,18,22,26);
            deleteData(15,19,23,27);
            deleteData(16,20,24,28);
            deleteData(17,21,25,29);
            deleteData(18,22,26,30);
            deleteData(19,23,27,31);
            deleteData(20,24,28,32);
            deleteData(21,25,29,33);
            deleteData(22,26,30,34);
            deleteData(23,27,31,35);
            deleteData(24,28,32,36);
            deleteData(25,29,33,37);
            deleteData(26,30,34,38);
            deleteData(27,31,35,39);
            deleteData(28,32,36,40);
            deleteData(29,33,37,41);
            deleteData(30,34,38,42);
            deleteData(31,35,39,43);
            deleteData(32,36,40,44);
            deleteData(33,37,41,45);
            deleteData(1,6,11,16);
            deleteData(2,7,12,17);
            deleteData(3,8,13,18);
            deleteData(4,9,14,19);
            deleteData(5,10,15,20);
            deleteData(6,11,16,21);
            deleteData(7,12,17,22);
            deleteData(8,13,18,23);
            deleteData(9,14,19,24);
            deleteData(10,15,20,25);
            deleteData(11,16,21,26);
            deleteData(12,17,22,27);
            deleteData(13,18,23,28);
            deleteData(14,19,24,29);
            deleteData(15,20,25,30);
            deleteData(16,21,26,31);
            deleteData(17,22,27,32);
            deleteData(18,23,28,33);
            deleteData(19,24,29,34);
            deleteData(20,25,30,35);
            deleteData(21,26,31,36);
            deleteData(22,27,32,37);
            deleteData(23,28,33,38);
            deleteData(24,29,34,39);
            deleteData(25,30,35,40);
            deleteData(26,31,36,41);
            deleteData(27,32,37,42);
            deleteData(28,33,38,43);
            deleteData(29,34,39,44);
            deleteData(30,35,40,45);
            deleteData(1,7,13,19);
            deleteData(2,8,14,20);
            deleteData(3,9,15,21);
            deleteData(4,10,16,22);
            deleteData(5,11,17,23);
            deleteData(6,12,18,24);
            deleteData(7,13,19,25);
            deleteData(8,14,20,26);
            deleteData(9,15,21,27);
            deleteData(10,16,22,28);
            deleteData(11,17,23,29);
            deleteData(12,18,24,30);
            deleteData(13,19,25,31);
            deleteData(14,20,26,32);
            deleteData(15,21,27,33);
            deleteData(16,22,28,34);
            deleteData(17,23,29,35);
            deleteData(18,24,30,36);
            deleteData(19,25,31,37);
            deleteData(20,26,32,38);
            deleteData(21,27,33,39);
            deleteData(22,28,34,40);
            deleteData(23,29,35,41);
            deleteData(24,30,36,42);
            deleteData(25,31,37,43);
            deleteData(26,32,38,44);
            deleteData(27,33,39,45);
            deleteData(1,8,15,22);
            deleteData(2,9,16,23);
            deleteData(3,10,17,24);
            deleteData(4,11,18,25);
            deleteData(5,12,19,26);
            deleteData(6,13,20,27);
            deleteData(7,14,21,28);
            deleteData(8,15,22,29);
            deleteData(9,16,23,30);
            deleteData(10,17,24,31);
            deleteData(11,18,25,32);
            deleteData(12,19,26,33);
            deleteData(13,20,27,34);
            deleteData(14,21,28,35);
            deleteData(15,22,29,36);
            deleteData(16,23,30,37);
            deleteData(17,24,31,38);
            deleteData(18,25,32,39);
            deleteData(19,26,33,40);
            deleteData(20,27,34,41);
            deleteData(21,28,35,42);
            deleteData(22,29,36,43);
            deleteData(23,30,37,44);
            deleteData(24,31,38,45);
            deleteData(1,9,17,25);
            deleteData(2,10,18,26);
            deleteData(3,11,19,27);
            deleteData(4,12,20,28);
            deleteData(5,13,21,29);
            deleteData(6,14,22,30);
            deleteData(7,15,23,31);
            deleteData(8,16,24,32);
            deleteData(9,17,25,33);
            deleteData(10,18,26,34);
            deleteData(11,19,27,35);
            deleteData(12,20,28,36);
            deleteData(13,21,29,37);
            deleteData(14,22,30,38);
            deleteData(15,23,31,39);
            deleteData(16,24,32,40);
            deleteData(17,25,33,41);
            deleteData(18,26,34,42);
            deleteData(19,27,35,43);
            deleteData(20,28,36,44);
            deleteData(21,29,37,45);
            deleteData(1,10,19,28);
            deleteData(2,11,20,29);
            deleteData(3,12,21,30);
            deleteData(4,13,22,31);
            deleteData(5,14,23,32);
            deleteData(6,15,24,33);
            deleteData(7,16,25,34);
            deleteData(8,17,26,35);
            deleteData(9,18,27,36);
            deleteData(10,19,28,37);
            deleteData(11,20,29,38);
            deleteData(12,21,30,39);
            deleteData(13,22,31,40);
            deleteData(14,23,32,41);
            deleteData(15,24,33,42);
            deleteData(16,25,34,43);
            deleteData(17,26,35,44);
            deleteData(18,27,36,45);
            deleteData(1,11,21,31);
            deleteData(2,12,22,32);
            deleteData(3,13,23,33);
            deleteData(4,14,24,34);
            deleteData(5,15,25,35);
            deleteData(6,16,26,36);
            deleteData(7,17,27,37);
            deleteData(8,18,28,38);
            deleteData(9,19,29,39);
            deleteData(10,20,30,40);
            deleteData(11,21,31,41);
            deleteData(12,22,32,42);
            deleteData(13,23,33,43);
            deleteData(14,24,34,44);
            deleteData(15,25,35,45);
            deleteData(1,12,23,34);
            deleteData(2,13,24,35);
            deleteData(3,14,25,36);
            deleteData(4,15,26,37);
            deleteData(5,16,27,38);
            deleteData(6,17,28,39);
            deleteData(7,18,29,40);
            deleteData(8,19,30,41);
            deleteData(9,20,31,42);
            deleteData(10,21,32,43);
            deleteData(11,22,33,44);
            deleteData(12,23,34,45);
            deleteData(1,13,25,37);
            deleteData(2,14,26,38);
            deleteData(3,15,27,39);
            deleteData(4,16,28,40);
            deleteData(5,17,29,41);
            deleteData(6,18,30,42);
            deleteData(7,19,31,43);
            deleteData(8,20,32,44);
            deleteData(9,21,33,45);
            deleteData(1,14,27,40);
            deleteData(2,15,28,41);
            deleteData(3,16,29,42);
            deleteData(4,17,30,43);
            deleteData(5,18,31,44);
            deleteData(6,19,32,45);
            deleteData(1,15,29,43);
            deleteData(2,16,30,44);
            deleteData(3,17,31,45);
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

    }

    private static void deleteData(int n0, int n1, int n2, int n3){
        try {
//            for(int i=0; i<dangCombination.size(); i++){
//                ArrayList items = (ArrayList)dangCombination.get(i);
//                int sameCnt = 0;
//                for(int j=0; j<items.size(); j++){
//                    if((int)items.get(j)==n0 || (int)items.get(j)==n1 || (int)items.get(j)==n2 || (int)items.get(j)==n3){
//                        sameCnt++;
//                    }
//                }
//                if(sameCnt==4){
//                    System.out.println(i + ":" + items.get(0) + "," + items.get(1) + "," + items.get(2) + "," + items.get(3) + "," + items.get(4) + "," + items.get(5));
//                }
//            }

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/book_ex",
                    "zerock",
                    "zerock");

            PreparedStatement deleteStmt;
            String query;

            query = "delete from tbl_combination where num_1=" + n0 + " and num_2=" + n1 + " and num_3=" + n2 + " and num_4=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_1=" + n0 + " and num_2=" + n1 + " and num_3=" + n2 + " and num_5=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_1=" + n0 + " and num_2=" + n1 + " and num_3=" + n2 + " and num_6=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_1=" + n0 + " and num_2=" + n1 + " and num_4=" + n2 + " and num_5=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_1=" + n0 + " and num_2=" + n1 + " and num_4=" + n2 + " and num_6=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_1=" + n0 + " and num_2=" + n1 + " and num_5=" + n2 + " and num_6=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_1=" + n0 + " and num_3=" + n1 + " and num_4=" + n2 + " and num_5=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_1=" + n0 + " and num_3=" + n1 + " and num_4=" + n2 + " and num_6=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_1=" + n0 + " and num_3=" + n1 + " and num_5=" + n2 + " and num_6=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_1=" + n0 + " and num_4=" + n1 + " and num_5=" + n2 + " and num_6=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_2=" + n0 + " and num_3=" + n1 + " and num_4=" + n2 + " and num_5=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_2=" + n0 + " and num_3=" + n1 + " and num_4=" + n2 + " and num_6=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_2=" + n0 + " and num_3=" + n1 + " and num_5=" + n2 + " and num_6=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_2=" + n0 + " and num_4=" + n1 + " and num_5=" + n2 + " and num_6=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();
            query = "delete from tbl_combination where num_3=" + n0 + " and num_4=" + n1 + " and num_5=" + n2 + " and num_6=" + n3 + ";";
            deleteStmt = conn.prepareStatement(query);
            deleteStmt.execute();

            System.out.println("complete:" + n0 + "," + n1 + "," + n2 + "," + n3);
            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
