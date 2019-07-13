import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentFileSearch {

    private final ExecutorService es;
    private final int numOfThreads;
    //private Object monitor = new Object();
    private AtomicInteger counter;
    private CountDownLatch latch;

    public ConcurrentFileSearch() {
        numOfThreads = Runtime.getRuntime().availableProcessors();
        this.es =  Executors.newFixedThreadPool(numOfThreads);
        counter = new AtomicInteger(numOfThreads);
        latch = new CountDownLatch(numOfThreads);
    }


    public Map<String,String> findFileBFS(String rootDir, String filename) {
        File root = new File(rootDir);
        if (!root.exists()) {
            throw new IllegalArgumentException("Root does not exists");
        }
        System.out.println(numOfThreads);
        //Store found matched files
        Map<String, String> foundMatch = new ConcurrentHashMap<>();
        //To store files and directories
        BlockingQueue<File> queue = new LinkedBlockingQueue<>();
        queue.add(root);
        String upperCasedFilename = filename.toUpperCase();
        for(int i = 0; i < numOfThreads; i++) {
            es.execute(new Worker(queue, upperCasedFilename, foundMatch));
        }
        try {
            latch.await();
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println(counter.get() +" from main");
        es.shutdownNow();

        return foundMatch;

    }

    private class Worker implements Runnable {

        BlockingQueue<File> queue;
        String upperCasedFilename;
        Map<String, String> foundMatch;

        public Worker(BlockingQueue queue, String filename, Map<String, String> map) {
            this.queue = queue;
            this.upperCasedFilename = filename;
            this.foundMatch = map;
        }

        @Override
        public void run() {
            System.out.println("[Thread # " + Thread.currentThread().getName() + " start execution]");
            while (counter.get()>0){
                counter.getAndDecrement();
                try {
                    File currentFile = queue.poll(1000,TimeUnit.MILLISECONDS); // <- Костыль?
                    if (currentFile != null) {
                        File[] arrayOfFiles = currentFile.listFiles();
                        if (arrayOfFiles != null) {
                            for (File file : arrayOfFiles) {
                                if (file.getName().toUpperCase().contains(upperCasedFilename)) {
                                    try {
                                        foundMatch.put( file.getCanonicalPath(), "| Nsme: "+file.getName());
                                    } catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }
                                if (file.isDirectory()) {
                                    queue.put(file);
                                    counter.getAndIncrement();
                                }
                            }
                        }
                    }
                } catch (InterruptedException e){
                   Thread.currentThread().interrupt();
                }
            }
            System.out.println("latch countDown");
            latch.countDown();
        }
    }
}
