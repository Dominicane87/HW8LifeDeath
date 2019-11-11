package vladimir.gorin;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Hello world!
 */
public class App {
    private static final char life = '1';
    private static final char nonLife = '0';

    public static void main(String[] args) throws IOException {
        String readingFileName = args[0];
        File file = new File("");
        Path pathToReadingFile = Paths.get(file.getAbsolutePath() + "\\" + readingFileName + ".txt");
        String writingFileName = args[1];
        Path pathToWritingFile = Paths.get(file.getAbsolutePath() + "\\" + writingFileName + ".txt");
        int n = Integer.parseInt(args[2]);
        String[] arrIn;
        String[] arrIOut;

        arrIn = readingFromFile(pathToReadingFile);
        for (int i = 0; i < arrIn.length; i++) {
            arrIOut = computeCell(arrIn, i);
            arrIn=arrIOut;
        }
        writingToFile(pathToWritingFile, arrIn);

    }

    private static String[] computeCell(String[] arr, int i) {
        int countNeibours = countNeighbours(arr, i);
        if (arr[i].equals(String.valueOf(nonLife))) {
            if (countNeibours == 3) {
                arr[i] = String.valueOf(life);
            }
        }
        if (arr[i].equals(String.valueOf(life))) {
            if ((countNeibours < 2) || (countNeibours > 3)) {
                arr[i] = String.valueOf(nonLife);
            }
        }
        return arr;
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

    private static void writingToFile(Path fullName, String[] arr) throws IOException {
        int size = (int) Math.sqrt(arr.length);

        try (BufferedWriter writer = Files.newBufferedWriter(fullName, StandardCharsets.UTF_8,
                StandardOpenOption.WRITE)) {
            for (int i = 0; i < arr.length; i++) {
                writer.write(arr[i]);
                if ((i+1)%size==0){
                    writer.newLine();
                }
            }
            writer.flush();
        }
    }

    private static String[] readingFromFile(Path fullName) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String row;
        try (BufferedReader bufferedReader = Files.newBufferedReader(fullName, StandardCharsets.UTF_8)) {
            while ((row = bufferedReader.readLine()) != null) {
                stringBuilder.append(row);
            }
        }
        String result=stringBuilder.toString();
        int n=result.length();
        String[] arr=new String[n];
        for (int i = 0; i < n; i++) {
            arr[i]=result.substring(i,i+1);
        }
        return arr;
    }
}
