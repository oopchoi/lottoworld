import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by bryan on 2015-10-23.
 *
 * 각 행에서 1개씩 뽑아내서 4개의 고정수를 완전조합으로 추출해 낸다.
 */
public class Fix4Maker {

    public static void main(String[] args) {

//        int [][] input = {
//                {1,2,3},
//                {11,12,13},
//                {21,22,23}
//        };
        int [][] input = {
                {3,6,13,23,24,35,1},
                {3,4,12,20,24,34,41},
                {4,19,20,26,30,35,24},
                {1,3,18,32,40,41,16},
                {4,18,23,30,34,41,19},
                {13,19,20,32,38,42,4}
        };

        int cnt = 4;


        System.out.println(input.length + "는 " + cnt + "보다 항상 크거나 같아야 한다.");
        combination4(cnt, input);


    }
    private static void combination4(int cnt, int [][] input){

        ArrayList results = new ArrayList();

        ArrayList lens = new ArrayList();

        for(int i=0; i<input.length; i++){
            lens.add(input[i].length);
        }
        // 첫 행의 열 개수
        for(int n=0; n<(int)lens.get(0); n++) {
            // 마지막 행의 열 개수
            for(int m=0; m<(int)lens.get(lens.size()-1); m++) {
                // 세 번째 행의 열 개수
                for (int k = 0; k < (int)lens.get(lens.size()-2); k++) {
                    // 두 번째 행의 열 개수
                    for (int i = 0; i < (int)lens.get(lens.size()-3); i++) {
                        int[] result = new int[cnt];
                        // 로우 개수 (=입력할 개수)
                        for (int j = 0; j < cnt; j++) {
                            // 첫번째 수는 제일 바깥쪽 인덱스 사용
                            if (j == 0) {
                                result[j] = input[j][n];
                            }
                            // 두번째는 증가
                            else if (j == 1 ) {
                                result[j] = input[j][i];
                            }
                            // 세번째는 서브 행의 열 인덱스 사용
                            else if (j == 2 ) {
                                result[j] = input[j][k];
                            }
                            // 나머지는 기준행의 열 인덱스 사용
                            else {
                                result[j] = input[j][m];
                            }
                        }
                        Arrays.sort(result);
                        results.add(result);
                    }
                }
            }
        }
        distinct(results);
    }
    private static void distinct(ArrayList list){
        for(int i=list.size()-1; i>=0; i--){
            boolean flag = false;
            int [] arr = (int[]) list.get(i);
            for(int j=0; j<arr.length; j++){
                if(j>0){
                    if(arr[j]==arr[j-1]){
                        flag = true;
                        break;
                    }
                }
            }
            if(flag) {
                list.remove(i);
            }else{
                //System.out.println(Arrays.toString(arr));
            }
        }

        //System.out.println(list.size());

        for(int i=list.size()-1; i>=0; i--){
            int [] arr = (int[]) list.get(i);
            int len = list.size();
            for(int j=len-1; j>=0; j--){
                if(i==j) continue;
                int [] arr2 = (int[]) list.get(j);
                if(Arrays.toString(arr).equals(Arrays.toString(arr2))){
                    list.remove(i);
                    break;
                }
            }
        }

        Collections.sort(list, new Comparator<int[]>() {
            @Override
            public int compare(int [] first,
                               int[] second) {

                int firstValue = (int)first[0];
                int secondValue = (int)second[0];

                // 올림차순 정렬
                if (firstValue < secondValue) {
                    return -1;
                } else if (firstValue > secondValue) {
                    return 1;
                } else /* if (firstValue == secondValue) */ {
                    return 0;
                }
            }
        });

        for(int i=0; i<list.size(); i++){
            System.out.println(Arrays.toString((int[])list.get(i)));
        }
        System.out.println(list.size());
    }

}
