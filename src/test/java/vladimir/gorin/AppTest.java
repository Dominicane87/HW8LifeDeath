package vladimir.gorin;

import static org.junit.Assert.assertTrue;
import static vladimir.gorin.App.readingFromFile;
import static vladimir.gorin.App.writingToFile;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void computePole() throws Exception {
        String readingFileName = "fileForReading";
        File file = new File("");
        Path pathToReadingFile = Paths.get(file.getAbsolutePath() + "\\" + readingFileName + ".txt");
        boolean[] arrIn;

        arrIn = readingFromFile(pathToReadingFile);

        long start = System.currentTimeMillis();
        App.computePole(arrIn,10);
        long finishOneThread = System.currentTimeMillis()-start;


        start = System.currentTimeMillis();
        App.computePole(arrIn,10);
        long finishSecondThread = System.currentTimeMillis()-start;

        System.out.println("Finish Second Process: "+finishSecondThread);
        System.out.println("Finish One Process: "+finishOneThread);
        assertTrue((finishSecondThread<finishOneThread));
    }


}
