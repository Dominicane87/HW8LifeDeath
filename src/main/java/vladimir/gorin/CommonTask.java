package vladimir.gorin;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

import static vladimir.gorin.App.life;
import static vladimir.gorin.App.nonLife;

public class CommonTask extends RecursiveAction {

    private String[] arrIn;
    private String[] result;

    private int count;

    CommonTask(String[] arrIn) {
        this.arrIn = arrIn;
        result=new String[arrIn.length];
        System.arraycopy(arrIn, 0, result, 0, arrIn.length);
    }

    public void setResult(String[] result) {
        this.result = result;
    }

    @Override
    protected void compute() {
        List<MyTask> tasks = new ArrayList<>();
        for (int i = 0; i < arrIn.length; i++) {
            MyTask task = new MyTask(i, arrIn);
            tasks.add(task);
        }

        Collection<MyTask> dirTasks = ForkJoinTask.invokeAll(tasks);
        for (MyTask t : dirTasks) {
            t.join();
            count = t.getResult();
            if (count != -1) {
                if (result[count].equals(String.valueOf(nonLife))) {
                    result[count] = String.valueOf(life);
                } else {
                    result[count] = String.valueOf(nonLife);
                }
            }
        }
    }

    String[] getResult() {
        return result;
    }
}

