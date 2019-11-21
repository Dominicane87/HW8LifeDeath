package vladimir.gorin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;

import static vladimir.gorin.App.LIFE;
import static vladimir.gorin.App.NON_LIFE;

public class MyTask implements Callable<Integer> {

    private int computeCell;
    private boolean[] arrIn;

    MyTask(int computeCell, boolean[] arrIn) {
        this.computeCell = computeCell;
        this.arrIn = arrIn;
    }

    @Override
    public Integer call()  {
        return computeCell(arrIn,computeCell);
    }

    private static int computeCell(boolean[] arr, int i) {
        int countNeibours = countNeighbours(arr, i);
        if (arr[i]== NON_LIFE) {
            if (countNeibours == 3) {
                return i;
            }
        }
        if (arr[i]== LIFE) {
            if ((countNeibours < 2) || (countNeibours > 3)) {
                return i;
            }
        }
        return -1;
    }

    private static int countNeighbours(boolean[] arr, int i) {
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
            if (arr[i1]== LIFE) {
                count++;
            }
        }
        return count;
    }



}

