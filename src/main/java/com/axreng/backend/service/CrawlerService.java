package com.axreng.backend.service;

public class CrawlerService {
    public static String getCrawlByID(String id) {

        return "GET -> /crawl/" + id;
    }

    public static String executeCrawl(String... keywords){
        return String.join(" - ", keywords);
    }
}
