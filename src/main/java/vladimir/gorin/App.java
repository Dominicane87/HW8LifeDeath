package vladimir.gorin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Hello world!
 */
public class App {
    static final char life = '1';
    static final char nonLife = '0';
    private static ExecutorService fixedThreadPool;

    public static void main(String[] args) throws Exception {
        String readingFileName = args[0];
        File file = new File("");
        Path pathToReadingFile = Paths.get(file.getAbsolutePath() + "\\" + readingFileName + ".txt");
        String writingFileName = args[1];
        Path pathToWritingFile = Paths.get(file.getAbsolutePath() + "\\" + writingFileName + ".txt");
        int n = Integer.parseInt(args[2]);
        String[] arrIn;

        arrIn = readingFromFile(pathToReadingFile);
        fixedThreadPool = Executors.newFixedThreadPool(arrIn.length);
        arrIn = computePole(arrIn, n);
        writingToFile(pathToWritingFile, arrIn);
        fixedThreadPool.shutdown();
    }

    static String[] computePole(String[] arrIn, int countIterations) throws Exception {
        int count;
        String[] tmpIn = Arrays.copyOf(arrIn, arrIn.length);
        String[] result = Arrays.copyOf(arrIn, arrIn.length);
        List<MyTask> list = new ArrayList<>();
        for (int i = 0; i < countIterations; i++) {
            for (int i1 = 0; i1 < tmpIn.length; i1++) {
                list.add(new MyTask(i1, tmpIn));
            }
            fixedThreadPool.invokeAll(list);
            for (MyTask task : list) {
                count = task.call();
                if (count != -1) {
                    if (tmpIn[count].equals(String.valueOf(nonLife))) {
                        result[count] = String.valueOf(life);
                    } else {
                        result[count] = String.valueOf(nonLife);
                    }
                }
            }
            tmpIn = result;
            result= new String[tmpIn.length];
        }
        return tmpIn;
    }

        static void writingToFile (Path fullName, String[]arr) throws IOException {
            int size = (int) Math.sqrt(arr.length);

            try (BufferedWriter writer = Files.newBufferedWriter(fullName, StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE)) {
                for (int i = 0; i < arr.length; i++) {
                    writer.write(arr[i]);
                    if ((i + 1) % size == 0) {
                        writer.newLine();
                    }
                }
                writer.flush();
            }
        }

        static String[] readingFromFile (Path fullName) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            String row;
            try (BufferedReader bufferedReader = Files.newBufferedReader(fullName, StandardCharsets.UTF_8)) {
                while ((row = bufferedReader.readLine()) != null) {
                    stringBuilder.append(row);
                }
            }
            String result = stringBuilder.toString();
            int n = result.length();
            String[] arr = new String[n];
            for (int i = 0; i < n; i++) {
                arr[i] = result.substring(i, i + 1);
            }
            return arr;
        }
    }
