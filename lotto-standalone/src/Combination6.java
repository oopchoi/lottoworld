import java.util.Arrays;

/**
 * Created by bryan on 2015-11-06.
 */
public class Combination6 {
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

        for(int i=0; i<input.length; i++){
        //for(int i=0; i<1; i++){
            System.out.println("start--" + i);
            int cnt = 0;
            for(int j=0; j<input[i].length-5; j++){
                Arrays.sort(input[i]);
                if(input[i][j]>20) break;
                for(int m=j+1; m<input[i].length-4; m++){
                    for(int n=m+1; n<input[i].length-3; n++){
                        for (int k=n+1; k<input[i].length-2; k++) {
                            for (int x=k+1; x<input[i].length-1; x++) {
                                for (int y=x+1; y<input[i].length; y++) {
                                    for (int v = 0; v < 6; v++) {
                                        switch (v) {
                                            case 0:
                                                System.out.print(input[i][j] + ",");
                                                break;
                                            case 1:
                                                System.out.print(input[i][m] + ",");
                                                break;
                                            case 2:
                                                System.out.print(input[i][n] + ",");
                                                break;
                                            case 3:
                                                System.out.print(input[i][k] + ",");
                                                break;
                                            case 4:
                                                System.out.print(input[i][x] + ",");
                                                break;
                                            case 5:
                                                System.out.println(input[i][y]);
                                                cnt++;
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            System.out.println("end--" + i + ":" + cnt);
        }

    }
}
