package dinson.customview.manager;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程管理
 *
 * @author Dinson - 2017/9/8
 */
public class ThreadManager {

    private static ThreadPool sThreadPool;

    public static ThreadPool getThreadPool() {
        if (sThreadPool == null) {
            synchronized (ThreadManager.class) {
                if (sThreadPool == null) {

                    int cpuCount = Runtime.getRuntime().availableProcessors();

                    int maxPoolSize = cpuCount * 2 + 1;

                    sThreadPool = new ThreadPool(cpuCount, maxPoolSize, 1L);
                }
            }
        }
        return sThreadPool;
    }

    /**
     * 线程池
     */
    public static class ThreadPool {
        private int corePoolSize;
        private int maxPoolSize;
        private long keepAliveTime;
        private ThreadPoolExecutor executor;

        private ThreadPool(int corePoolSize, int maxPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maxPoolSize = maxPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable runnable) {
            if (executor == null) {
                executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                    TimeUnit.SECONDS, new LinkedBlockingDeque<>());
            }
            executor.execute(runnable);
        }

        public void cancel(Runnable runnable) {
            executor.getQueue().remove(runnable);
        }
    }

}
