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
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiFunction;

/**
 * Hello world!
 */
public class App {
    static final char life = '1';
    static final char nonLife = '0';
    private static ForkJoinPool forkJoinPool;
    private static CommonTask commonTask;
    public static void main(String[] args) throws IOException {
        String readingFileName = args[0];
        File file = new File("");
        Path pathToReadingFile = Paths.get(file.getAbsolutePath() + "\\" + readingFileName + ".txt");
        String writingFileName = args[1];
        Path pathToWritingFile = Paths.get(file.getAbsolutePath() + "\\" + writingFileName + ".txt");
        int n = Integer.parseInt(args[2]);
        String[] arrIn;

        arrIn = readingFromFile(pathToReadingFile);
        arrIn=computePole(1,arrIn,n);
        writingToFile(pathToWritingFile, arrIn);

    }
     static String[] computePole(int numberThreads, String[] arrIn, int countIterations){
         forkJoinPool = new ForkJoinPool(numberThreads);
        for (int i=0;i<countIterations;i++) {
            commonTask=new CommonTask(arrIn);
            forkJoinPool.invoke(commonTask);
            arrIn=commonTask.getResult();
        }
        return arrIn;
    }



     static void writingToFile(Path fullName, String[] arr) throws IOException {
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

     static String[] readingFromFile(Path fullName) throws IOException {
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
