package vladimir.gorin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveAction;
import java.util.function.BiFunction;

import static vladimir.gorin.App.life;
import static vladimir.gorin.App.nonLife;

public class MyTask extends RecursiveAction {

    private int result;
    private int computeCell;
    private String[] arrIn;

    MyTask(int computeCell, String[] arrIn) {
        this.computeCell = computeCell;
        this.arrIn = arrIn;
    }

    int getResult() {
        return result;
    }

    @Override
    protected void compute() {
        result=computeCell(arrIn,computeCell);

    }
    private static int computeCell(String[] arr, int i) {
        int countNeibours = countNeighbours(arr, i);
        if (arr[i].equals(String.valueOf(nonLife))) {
            if (countNeibours == 3) {
                return i;
            }
        }
        if (arr[i].equals(String.valueOf(life))) {
            if ((countNeibours < 2) || (countNeibours > 3)) {
                return i;
            }
        }
        return -1;
    }

    private static int countNeighbours(String[] arr, int i) {
        int count = 0;
        int size = (int) Math.sqrt(arr.length);
        int n = arr.length;
        ArrayList<String> list = new ArrayList<String>() {
            {
                add("leftUp");
                add("up");
                add("rightUp");
                add("right");
                add("rightDown");
                add("down");
                add("leftDown");
                add("left");
            }
        };
        BiFunction<Integer, Integer, Integer> leftUp = (iInterface, sizeInterface) -> iInterface - (sizeInterface + 1);
        BiFunction<Integer, Integer, Integer> up = (iInterface, sizeInterface) -> iInterface - sizeInterface;
        BiFunction<Integer, Integer, Integer> rightUp = (iInterface, sizeInterface) -> iInterface - (sizeInterface - 1);
        BiFunction<Integer, Integer, Integer> right = (iInterface, sizeInterface) -> iInterface + 1;
        BiFunction<Integer, Integer, Integer> rightDown = (iInterface, sizeInterface) -> iInterface + (sizeInterface + 1);
        BiFunction<Integer, Integer, Integer> down = Integer::sum;
        BiFunction<Integer, Integer, Integer> leftDown = (iInterface, sizeInterface) -> iInterface + (sizeInterface - 1);
        BiFunction<Integer, Integer, Integer> left = (iInterface, sizeInterface) -> iInterface - 1;

        Map<String, BiFunction<Integer, Integer, Integer>> map = new HashMap<String, BiFunction<Integer, Integer, Integer>>() {{
            put("leftUp", leftUp);
            put("up", up);
            put("rightUp", rightUp);
            put("right", right);
            put("rightDown", rightDown);
            put("down", down);
            put("leftDown", leftDown);
            put("left", left);

        }};


        if (i < size) {
            list.remove("leftUp");
            list.remove("up");
            list.remove("rightUp");
        }
        if ((i >= n - size) & (i <= n - 1)) {
            list.remove("leftDown");
            list.remove("down");
            list.remove("rightDown");
        }
        if (i % size == 0) {
            list.remove("leftUp");
            list.remove("left");
            list.remove("leftDown");
        }
        if ((i + 1) % size == 0) {
            list.remove("rightUp");
            list.remove("right");
            list.remove("rightDown");
        }

        for (String s : list) {
            int i1 = map.get(s).apply(i, size);
            if (arr[i1].equals(String.valueOf(life))) {
                count++;
            }
        }
        return count;
    }
}

