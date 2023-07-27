package com.axreng.backend.service;

import com.axreng.backend.util.IDGenerator;

public class CrawlerService {
    public static String getCrawlByID(String id) {

        return "GET -> /crawl/"+ " / ID => " + IDGenerator.generateID();
    }

    public static String executeCrawl(String... keywords){
        return String.join(" - ", keywords);
    }
}
