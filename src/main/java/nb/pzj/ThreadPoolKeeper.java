package nb.pzj;

import java.util.concurrent.*;

/**
 * 维护一个无界线程池
 */
public class ThreadPoolKeeper {

    private static ExecutorService executorService = new ThreadPoolExecutor(12, Integer.MAX_VALUE,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<>());

    public static ExecutorService getThreadPool() {
        return executorService;
    }
}
