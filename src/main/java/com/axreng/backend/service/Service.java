package com.axreng.backend.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.axreng.backend.Main;
import com.axreng.backend.model.CrawlData;
import com.axreng.backend.model.ResponseERROR;
import com.axreng.backend.model.ResponseGET;
import com.axreng.backend.util.Utils;
import com.google.gson.Gson;

import spark.Response;

public class Service {

    public String getBaseURL() {
        try {
            String url = System.getenv("BASE_URL");
            if (url == null)
                throw new NullPointerException();
            return url;
        } catch (NullPointerException e) {
            System.out.println("ERROR: Environment variable not found");
            return "ERROR: Environment variable not found";
        } catch (SecurityException e) {
            System.out.println("ERROR: Can't access environment variable");
            return "ERROR: Can't access environment variable";
        }
    }

    public String[] getCrawlByID(String id) {
        try {
            Main.tasks.get(id).getCrawlProccess().get(10, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println("Proccess " + id + " not finished yet");
        }
        // String result = "GET -> /crawl/" + " / ID => " + id + " | "
        // + ((Main.tasks.get(id).getCrawlProccess().isDone()) ? "done" : "active") +
        // "<br>"
        // + ((Main.tasks.get(id).getUrls().size() > 0) ? String.join("<br>",
        // Main.tasks.get(id).getUrls()) : "");
        if (Main.tasks.get(id).getUrls().size() > 0) {
            String[] result = Arrays.copyOf(Main.tasks.get(id).getUrls().toArray(), Main.tasks.get(id).getUrls().size(), String[].class);
            return result;
        }
        return null;

    }

    private String executeCrawl(String param, CrawlData data) {
        ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(param.split(",")));
        for (Iterator<String> iterator = keywords.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (key.length() < 4 || key.length() > 32) {
                System.out.println("Unallowed keyword -> " + key);
                iterator.remove();
            }
        }
        data.setTerms(keywords);
        data.setStartInstant(Instant.now());
        Crawler crawler = new Crawler();
        String url = getBaseURL();
        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://"))
            return url;
        crawler.aquireBaseUrl(url);
        crawler.setCrawling(true);
        crawler.crawl(url, 0, false, true, data);
        crawler.setCrawling(false);
        if (data.getUrls().size() > 0)
            return String.join("\n", data.getUrls());
        return "No URLs found with search terms";
    }

    public String startCrawlTask(String param) throws InterruptedException, ExecutionException {
        String id = Utils.generateID();
        // String result = "";
        CrawlData data = new CrawlData(id);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() {
                return executeCrawl(param, data);
            }
        };
        Future<String> future = executor.submit(callable);
        data.setCrawlProccess(future);
        Main.tasks.put(id, data);
        return id;
    }

    public Object getResponseGet(Response res, Service service, String id, Gson gson) {
        String[] result = service.getCrawlByID(id);
        service = null;
        ResponseGET response = new ResponseGET();
        response.setId(id);
        response.setStatus((Main.tasks.get(id).getCrawlProccess().isDone()) ? "done" : "active");
        response.setUrls(result);
        res.body(gson.toJson(response));
        return gson.toJson(response);
    }

    public Object getResponseError(Response res, String id, Gson gson) {
        ResponseERROR response = new ResponseERROR();
        response.setStatus(404);
        response.setMessage("crawl not found: " + id);
        res.body(gson.toJson(response));
        return gson.toJson(response);
    }
}