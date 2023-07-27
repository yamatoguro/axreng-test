package com.axreng.backend;

import static spark.Spark.*;

import com.axreng.backend.service.CrawlerService;

public class Main {
    public static void main(String[] args) {
        get("/crawl/:id", (req, res) -> CrawlerService.getCrawlByID(req.params("id")));
        post("/crawl", (req, res) -> CrawlerService.executeCrawl(req.body()));
    }
}
