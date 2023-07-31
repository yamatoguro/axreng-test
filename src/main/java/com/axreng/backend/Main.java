package com.axreng.backend;

import static spark.Spark.get;
import static spark.Spark.notFound;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.Map;

import com.axreng.backend.model.CrawlData;
import com.axreng.backend.model.ResponsePOST;
import com.axreng.backend.service.Service;
import com.google.gson.Gson;

public class Main {
    public static Map<String, CrawlData> tasks = new HashMap<String, CrawlData>();

    public static void main(String... args) {
        get("/crawl/:id", (req, res) -> {
            Service service = new Service();
            String id = req.params("id");
            Gson gson = new Gson();
            if (!tasks.containsKey(id)) {
                return service.getResponseError(res, id, gson);
            }
            return service.getResponseGet(res, service, id, gson);
        });
        get("/test/:keywords", (req, res) -> {
            Service service = new Service();
            String result = service.startCrawlTask(req.params("keywords"));
            service = null;
            Gson gson = new Gson();
            ResponsePOST response = new ResponsePOST();
            response.setId(result);
            res.body(gson.toJson(response));
            return res.body();
        });
        post("/crawl", (req, res) -> (new Service()).startCrawlTask(req.body()));
        notFound(
                "<html><head><style>h1,h2 {text-align: center;text-transform: uppercase;font-weight: 400;}h1 {margin-top:30vh;font-size: 6rem;}h2 {font-size: 2rem;}</style></head><body><div><h1>404</h1><h2>Page not Found</h2></div></body></html>");
    }
}
