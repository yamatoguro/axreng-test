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

import com.axreng.backend.Main;
import com.axreng.backend.model.CrawlData;
import com.axreng.backend.model.ResponseERROR;
import com.axreng.backend.model.ResponseGET;
import com.axreng.backend.model.ResponsePOST;
import com.axreng.backend.util.Utils;
import com.google.gson.Gson;

import spark.Request;
import spark.Response;

public class Service {

    public String getBaseURL() {
        try {
            String url = getEnv();
            if (url == null)
                throw new NullPointerException();
            return url;
        } catch (NullPointerException e) {
            return "ERROR: Environment variable not found";
        } catch (SecurityException e) {
            return "ERROR: Can't access environment variable";
        }
    }

    String getEnv() throws NullPointerException, SecurityException {
        return System.getenv("BASE_URL");
    }

    public String[] getCrawlByID(String id) {
        try {
            // Main.tasks.get(id).getCrawlProccess().get(10, TimeUnit.MILLISECONDS);
            String[] result = Arrays.copyOf(Main.tasks.get(id).getUrls().toArray(), Main.tasks.get(id).getUrls().size(),String[].class);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private String executeCrawl(String param, CrawlData data) {
        ArrayList<String> keywords = new ArrayList<String>(Arrays.asList(param.split(",")));
        for (Iterator<String> iterator = keywords.iterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (key.length() < 4 || key.length() > 32) {
                iterator.remove();
            }
        }
        data.setTerms(keywords);
        data.setStartInstant(Instant.now());
        Crawler crawler = new Crawler();
        String url = getBaseURL();
        if (!url.toLowerCase().startsWith("http://") && !url.toLowerCase().startsWith("https://"))
            return url;
        crawler.setBaseUrl(url);
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

    public Object getCrawl(Request req, Response res) {
        String id = req.params("id");
        Gson gson = new Gson();
        if (!Main.tasks.containsKey(id)) {
            ResponseERROR response = new ResponseERROR();
            response.setStatus(404);
            response.setMessage("crawl not found: " + id);
            res.body(gson.toJson(response));
            return gson.toJson(response);
        }
        String[] result = getCrawlByID(id);
        ResponseGET response = new ResponseGET();
        response.setId(id);
        response.setStatus((Main.tasks.get(id).getCrawlProccess().isDone()) ? "done" : "active");
        response.setUrls(result);
        res.body(gson.toJson(response));
        return gson.toJson(response);
    }

    public Object postCrawl(Request req, Response res) {
        String result;
        Gson gson = new Gson();
        try {
            result = startCrawlTask(req.params("keywords"));
            ResponsePOST response = new ResponsePOST();
            response.setId(result);
            res.body(gson.toJson(response));
            return res.body();
        } catch (InterruptedException | ExecutionException e) {
            ResponseERROR response = new ResponseERROR();
            response.setStatus(500);
            response.setMessage("an error ocurred while processing request | " + e.getMessage());
            res.body(gson.toJson(response));
            return res.body();
        }
    }
}