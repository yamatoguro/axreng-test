package com.axreng.backend;

import static spark.Spark.get;
import static spark.Spark.notFound;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.Map;

import com.axreng.backend.model.CrawlData;
import com.axreng.backend.service.Service;

public class Main {
    public static Map<String, CrawlData> tasks;

    public static void main(String... args) {
        tasks = new HashMap<String, CrawlData>();
        Service service = new Service();
        get("/crawl/:id", (req, res) -> service.getCrawl(req, res));
        post("/crawl", (req, res) -> service.postCrawl(req, res));
        notFound(
                "<html><head><style>h1,h2 {text-align: center;text-transform: uppercase;font-weight: 400;}h1 {margin-top:30vh;font-size: 6rem;}h2 {font-size: 2rem;}</style></head><body><div><h1>404</h1><h2>Page not Found</h2></div></body></html>");
    }
}
