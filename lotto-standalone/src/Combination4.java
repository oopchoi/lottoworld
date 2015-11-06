import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by bryan on 2015-11-06.
 */
public class Combination4 {
    public static void main(String[] args) {
//        int [][] input = {
//                {3,6,13,23,24,35,1},
//                {3,4,12,20,24,34,41},
//                {4,19,20,26,30,35,24},
//                {1,3,18,32,40,41,16},
//                {4,18,23,30,34,41,19},
//                {13,19,20,32,38,42,4}
//        };

        int [][] input = {
                {1,2,3,4,5,7,8,10,11,12,13,14,15,16,17,18,19,20,24,25,26,27,29,31,33,34,35,36,37,38,39,40,43,44,45}
        };

        ArrayList<ArrayList> completeCombo = new ArrayList<ArrayList>();

        for(int i=0; i<input.length; i++){
        //for(int i=0; i<1; i++){
            System.out.println("start--" + i);
            int cnt = 0;
            for(int j=0; j<input[i].length-3; j++){
                Arrays.sort(input[i]);
                for(int m=j+1; m<input[i].length-2; m++){
                    for(int n=m+1; n<input[i].length-1; n++){
                        for (int k=n+1; k<input[i].length; k++) {
                            ArrayList item = new ArrayList();
                            for (int v = 0; v < 4; v++) {
                                switch (v) {
                                    case 0:
                                        System.out.print(input[i][j] + ",");
                                        item.add(input[i][j]);
                                        break;
                                    case 1:
                                        System.out.print(input[i][m] + ",");
                                        item.add(input[i][m]);
                                        break;
                                    case 2:
                                        System.out.print(input[i][n] + ",");
                                        item.add(input[i][n]);
                                        break;
                                    case 3:
                                        System.out.println(input[i][k]);
                                        item.add(input[i][k]);
                                        completeCombo.add(item);
                                        cnt++;
                                        break;
                                }

                            }
                        }
                    }
                }
            }
            System.out.println("end--" + i + ":" + cnt);

            insertCombo(completeCombo);
        }

    }

    private static void insertCombo(ArrayList completeCombo){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/book_ex",
                    "zerock",
                    "zerock");

            String query = "INSERT INTO tbl_fix4_combination (num_1, num_2, num_3, num_4) VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStmt = conn.prepareStatement(query);

            PreparedStatement truncateStmt = conn.prepareStatement("truncate tbl_fix4_combination");
            truncateStmt.execute();

            int cnt = 0;
            for (int i=0; i<completeCombo.size(); i++) {
                //int num_1 = 0, num_2 = 0, num_3 = 0, num_4 = 0, num_5 = 0, num_6 = 0;
                int num_1 = 0, num_2 = 0, num_3 = 0, num_4 = 0;

                ArrayList item = (ArrayList)completeCombo.get(i);
                int sum = 0;
                for(int j=0; j<item.size(); j++){
                    sum += (int)item.get(j);
                }
                num_1 = (int)item.get(0);
                num_2 = (int)item.get(1);
                num_3 = (int)item.get(2);
                num_4 = (int)item.get(3);
                //num_5 = (int)item.get(4);
                //num_6 = (int)item.get(5);
                preparedStmt.setInt(1, num_1);
                preparedStmt.setInt(2, num_2);
                preparedStmt.setInt(3, num_3);
                preparedStmt.setInt(4, num_4);
                //preparedStmt.setInt(5, num_5);
                //preparedStmt.setInt(6, num_6);
                preparedStmt.execute();
                cnt++;

            }
            System.out.println("cnt:" + cnt);

            conn.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
