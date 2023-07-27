package com.axreng.backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.axreng.backend.Main;
import com.axreng.backend.util.Utils;

public class CrawlerService {

    public String getCrawlByID(String id) {
        return "GET -> /crawl/" + " / ID => " + Utils.generateID();
    }

    public String executeCrawl(String param, String id) {
        String url = System.getenv("BASE_URL");
        System.out.println(url);
        ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(param.split(",")));
        for (Iterator<String> iterator = keywords.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (key.length() < 4 || key.length() > 32) {
                System.out.println("Unallowed keyword -> " + key);
                iterator.remove();
            }
        }
        return String.join(" ", keywords);
    }

    public String startCrawlTask(String param) throws InterruptedException, ExecutionException {
        String id = Utils.generateID();
        String result = "";
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() {
                return executeCrawl(param, id);
            }
        };
        Future<String> future = executor.submit(callable);
        Main.tasks.put(id, future);
        result = future.get();
        executor.shutdown();
        return result.concat(" | ID: " + id + " | Status: " + ((Main.tasks.get(id).isDone())?"done":"active"));
    }
}
