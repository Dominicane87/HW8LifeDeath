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
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Hello world!
 */
public class App {
    static final boolean life = true;
    static final boolean nonLife = false;
    private static ExecutorService fixedThreadPool;

    public static void main(String[] args) throws Exception {
        String readingFileName = args[0];
        File file = new File("");
        Path pathToReadingFile = Paths.get(file.getAbsolutePath() + "\\" + readingFileName + ".txt");
        String writingFileName = args[1];
        Path pathToWritingFile = Paths.get(file.getAbsolutePath() + "\\" + writingFileName + ".txt");
        int n = Integer.parseInt(args[2]);
        boolean[] arrIn;

        arrIn = readingFromFile(pathToReadingFile);
        fixedThreadPool = Executors.newFixedThreadPool(arrIn.length);
        arrIn = computePole(arrIn, n);
        writingToFile(pathToWritingFile, arrIn);
        fixedThreadPool.shutdown();
    }

    static boolean[] computePole(boolean[] arrIn, int countIterations) throws Exception {
        int count = -1;
        boolean[] tmpIn = Arrays.copyOf(arrIn, arrIn.length);
        boolean[] result = Arrays.copyOf(arrIn, arrIn.length);
        List<MyTask> list = new ArrayList<>();
        for (int i = 0; i < countIterations; i++) {
            for (int i1 = 0; i1 < arrIn.length; i1++) {
                list.add(new MyTask(i1, arrIn));
            }
            List<Future<Integer>> futures=fixedThreadPool.invokeAll(list);

            for (Future<Integer> future : futures) {
                count = future.get();
                if (count != -1) {
                    if (tmpIn[count]==nonLife) {
                        result[count] = life;
                    } else {
                        result[count] = nonLife;
                    }
                }
            }
            tmpIn = result;
        }
        return tmpIn;
    }

        static void writingToFile (Path fullName, boolean[]arr) throws IOException {
            int size = (int) Math.sqrt(arr.length);

            try (BufferedWriter writer = Files.newBufferedWriter(fullName, StandardCharsets.UTF_8,
                    StandardOpenOption.WRITE)) {
                for (int i = 0; i < arr.length; i++) {
                    writer.write(arr[i]?"1":"0");
                    if ((i + 1) % size == 0) {
                        writer.newLine();
                    }
                }
                writer.flush();
            }
        }

        static boolean[] readingFromFile (Path fullName) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            String row;
            try (BufferedReader bufferedReader = Files.newBufferedReader(fullName, StandardCharsets.UTF_8)) {
                while ((row = bufferedReader.readLine()) != null) {
                    stringBuilder.append(row);
                }
            }
            String result = stringBuilder.toString();
            int n = result.length();
            boolean[] arr = new boolean[n];
            for (int i = 0; i < n; i++) {
                arr[i] = result.substring(i, i + 1).equals("1")?true:false;
            }
            return arr;
        }
    }
