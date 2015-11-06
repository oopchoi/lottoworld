import java.util.Arrays;

/**
 * Created by bryan on 2015-11-06.
 */
public class Combination2 {
    public static void main(String[] args) {
        int [][] input = {
                {3,6,13,23,24,35,1},
                {3,4,12,20,24,34,41},
                {4,19,20,26,30,35,24},
                {1,3,18,32,40,41,16},
                {4,18,23,30,34,41,19},
                {13,19,20,32,38,42,4}
        };

        System.out.println("start--");
        int cnt = 0;
        for(int i=0; i<input.length; i++){
            //for(int i=0; i<1; i++){
            // 1~5
            for(int j=0; j<input[i].length-1; j++){
                Arrays.sort(input[i]);
                // 2~6
                for(int m=j+1; m<input[i].length; m++){
                    System.out.println("exceptCombo = new ArrayList<>();");
                    System.out.println("ArrayList a"+cnt+" = new ArrayList();");
                    for(int v=0; v<3; v++) {
                        switch (v){
                            case 0://System.out.print(input[i][j] + ",");
                                System.out.println("a" + cnt + ".add(" + input[i][j] + ");");
                                break;
                            case 1://System.out.println(input[i][m]);
                                System.out.println("a" + cnt + ".add("+input[i][m]+");");
                                System.out.println("exceptCombo.add(a"+cnt+");");
                                System.out.println("resultCombo = eliminatorWon.removeUnderCount(2, tool.copy(completeCombo), exceptCombo);");
                                System.out.println("totalCount += resultCombo.size();");
                                System.out.println("printCombo(resultCombo);");
                                cnt++;
                                break;
                        }

                    }
                }
            }

        }
//        int cnt = 0;
//        for(int i=0; i<input.length; i++){
//        //for(int i=0; i<1; i++){
//            // 1~5
//            for(int j=0; j<input[i].length-1; j++){
//                Arrays.sort(input[i]);
//                // 2~6
//                for(int m=j+1; m<input[i].length; m++){
//
//                        for(int v=0; v<3; v++) {
//                            switch (v){
//                                case 0:System.out.print(input[i][j] + ",");
//                                    break;
//                                case 1:System.out.println(input[i][m]);
//                                    cnt++;
//                                    break;
//                            }
//
//                        }
//                }
//            }
//
//        }
        System.out.println("end--:" + cnt);

    }
}
