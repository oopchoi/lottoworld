import java.util.Arrays;

/**
 * Created by bryan on 2015-11-06.
 */
public class Combination3 {
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
                {1,3,4,6,12,13,16,18,19,20,23,24,26,30,32,34,35,38,40,41,42}
        };

        for(int i=0; i<input.length; i++){
        //for(int i=0; i<1; i++){
            System.out.println("start--" + i);
            int cnt = 0;
            // 1~5
            for(int j=0; j<input[i].length-2; j++){
                Arrays.sort(input[i]);
                // 2~6
                for(int m=j+1; m<input[i].length-1; m++){
                    for(int n=m+1; n<input[i].length; n++){
                        for(int v=0; v<3; v++) {
                            switch (v){
                                case 0:System.out.print(input[i][j] + ",");
                                    break;
                                case 1:System.out.print(input[i][m] + ",");
                                    break;
                                case 2:System.out.println(input[i][n]);
                                    cnt++;
                                    break;
                            }

                        }
                    }
                }
            }
            System.out.println("end--" + i + ":" + cnt);
        }

    }
}
