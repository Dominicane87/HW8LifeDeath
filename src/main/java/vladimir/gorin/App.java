package vladimir.gorin;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Hello world!
 */
public class App {
    public static final char life='1';
    public static final char nonLife='0';

    public static void main(String[] args) throws IOException {
        String readingFileName = args[0];
        File file = new File("");
        String pathToReadingFile = file.getAbsolutePath() + "\\" + readingFileName + ".txt";
        String writingFileName = args[1];
        String pathToWritingFile = file.getAbsolutePath() + "\\" + writingFileName + ".txt";
        int n = Integer.parseInt(args[2]);
        String[] arrIn;


        arrIn=readingFromFile(pathToReadingFile);
        for (int i = 0; i < arrIn.length; i++) {
            arrIn=computeCell(arrIn,i);
            writingToFile(pathToWritingFile, arrIn);

        }




    }
    public static String[] computeCell(String[] arr, int i){
        int countNeibours=countNeighbours(arr,i);
        if (arr[i].equals(String.valueOf(nonLife))){
           if (countNeibours==3){
               arr[i]=String.valueOf(life);
           }
        }
        if (arr[i].equals(String.valueOf(life))){
            if ((countNeibours<2)||(countNeibours>3)){
                arr[i]=String.valueOf(nonLife);
            }
        }
        return arr;
    }
    public static int countNeighbours(String[] arr, int i){
        int count=0;
        int size=(int) Math.sqrt(arr.length);
        int n=arr.length;
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
        BiFunction<Integer,Integer,Integer> leftUp=(iInterface,sizeInterface)->iInterface-(sizeInterface+1);
        BiFunction<Integer,Integer,Integer> up=(iInterface,sizeInterface)->iInterface-sizeInterface;
        BiFunction<Integer,Integer,Integer> rightUp=(iInterface,sizeInterface)->iInterface-(sizeInterface-1);
        BiFunction<Integer,Integer,Integer> right=(iInterface,sizeInterface)->iInterface+1;
        BiFunction<Integer,Integer,Integer> rightDown=(iInterface,sizeInterface)->iInterface+(sizeInterface+1);
        BiFunction<Integer,Integer,Integer> down=(iInterface,sizeInterface)->iInterface+sizeInterface;
        BiFunction<Integer,Integer,Integer> leftDown=(iInterface,sizeInterface)->iInterface+(sizeInterface-1);
        BiFunction<Integer,Integer,Integer> left=(iInterface,sizeInterface)->iInterface-1;

        Map<String,BiFunction<Integer,Integer,Integer>> map=new HashMap<String,BiFunction<Integer,Integer,Integer>>(){{
            put("leftUp",leftUp);
            put("up",up);
            put("rightUp",rightUp);
            put("right",right);
            put("rightDown",rightDown);
            put("down",down);
            put("leftDown",leftDown);
            put("left",left);

        }};



        if (i<size){
            list.remove("leftUp");
            list.remove("up");
            list.remove("rightUp");
        }
        if ((i>=n-size)&(i<=n-1)){
            list.remove("leftDown");
            list.remove("down");
            list.remove("rightDown");
        }
        if (i%size==0){
            list.remove("leftUp");
            list.remove("left");
            list.remove("leftDown");
        }
        if ((i+1)%size==0){
            list.remove("rightUp");
            list.remove("right");
            list.remove("rightDown");
        }

        for (String s : list) {
            int i1=map.get(s).apply(i,size);
            if (arr[i1].equals(String.valueOf(life))) {
                count++;
            }
        }
        System.out.println(count);
        return count;
    }

    public static void writingToFile(String fullName, String[] arr) throws IOException {
        int size=(int) Math.sqrt((double)arr.length);
        byte[] buffer;
        File file=new File(fullName);
        FileOutputStream fileOutputStream=new FileOutputStream(file);
        for (int i = 0; i < arr.length; i++) {
            buffer = arr[i].getBytes();
            fileOutputStream.write(buffer, 0, buffer.length);
            if ((i+1)%size==0){
                buffer = "\n".getBytes();
                fileOutputStream.write(buffer, 0, buffer.length);
            }
        }
    }

    public static String[] readingFromFile(String fullName) throws IOException {
        File file=new File(fullName);
        FileInputStream fileInputStream = new FileInputStream(file);
        int c;
        StringBuilder stringBuilder = new StringBuilder();
        while ((c = fileInputStream.read()) != -1) {
           if (((char) c==life)||(char) c==nonLife) {
               stringBuilder.append((char) c + ";");
           }
        }
        String arr[];
        arr=stringBuilder.toString().split(";");
        return arr;
    }
}
