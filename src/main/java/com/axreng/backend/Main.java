package com.axreng.backend;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import com.axreng.backend.model.CrawlData;
import com.axreng.backend.service.Service;

public class Main {
    public static Map<String, CrawlData> tasks = new HashMap<String, CrawlData>();

    public static void main(String... args) {
        get("/crawl/:id", (req, res) -> {
            Service service = new Service();
            String result = service.getCrawlByID(req.params("id"));
            service = null;
            return result;
        });
        get("/test/:keywords", (req, res) -> {
            Service service = new Service();
            String result = service.startCrawlTask(req.params("keywords"));
            service = null;
            return result;
        });
        post("/crawl", (req, res) -> (new Service()).startCrawlTask(req.body()));
        notFound(
                "<html><head><style>h1,h2 {text-align: center;text-transform: uppercase;font-weight: 400;}h1 {margin-top:30vh;font-size: 6rem;}h2 {font-size: 2rem;}</style></head><body><div><h1>404</h1><h2>Page not Found</h2></div></body></html>");
    }
}
