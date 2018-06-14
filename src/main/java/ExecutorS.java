
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorS {

    private static ExecutorService pool = Executors.newCachedThreadPool();

    public static void execute(Runnable task){

        pool.execute(task);

    }

}
