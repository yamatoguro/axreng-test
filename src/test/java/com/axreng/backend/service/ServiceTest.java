package com.axreng.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.lang.SecurityException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.axreng.backend.Main;
import com.axreng.backend.model.CrawlData;
import com.axreng.backend.model.ResponseGET;
import com.axreng.backend.model.ResponsePOST;
import com.google.gson.Gson;

import spark.Response;

public class ServiceTest {
    @Mock
    static Service service;
    @Mock
    static Crawler crawler;

    @BeforeAll
    static void setup() {
        service = mock(Service.class);
        crawler = mock(Crawler.class);
    }

    @Test
    void testGetBaseURL() {
        service = spy(Service.class);
        when(service.getEnv()).thenAnswer(invocation -> {
            return "https://c99.io/";
        });
        String url = service.getBaseURL();
        assertEquals("https://c99.io/", url);
    }

    @Test
    void testGetBaseURLNullPointer() {
        service = spy(Service.class);
        when(service.getEnv()).thenAnswer(invocation -> {
            throw new NullPointerException();
        });
        String url = service.getBaseURL();
        assertEquals("ERROR: Environment variable not found", url);
    }

    @Test
    void testGetBaseURLSecurity() {
        service = spy(Service.class);
        when(service.getEnv()).thenAnswer(invocation -> {
            throw new SecurityException();
        });
        String url = service.getBaseURL();
        assertEquals("ERROR: Can't access environment variable", url);
    }

    @Test
    void testGetCrawlByID() {
        service = new Service();
        Crawler crawler = new Crawler();
        String id = "30vbllyb";
        CrawlData data = new CrawlData(id);
        String baseUrl = "https://c99.io/";
        crawler.setBaseUrl(baseUrl);
        data.getTerms().add("Building");
        crawler.setCrawling(true);
        crawler.crawl(crawler.getBaseUrl(), 1, true, true, data);
        crawler.setCrawling(false);
        Main.tasks = new HashMap<String, CrawlData>();
        Main.tasks.put(id, data);
        String url = service.getCrawlByID(id)[0];
        assertTrue(url.equals(baseUrl));
    }

    @Test
    void testGetCrawlNotFound() {
        service = new Service();
        Main.tasks = new HashMap<String, CrawlData>();
        Response res = mock(Response.class);
        String id = "30vbllyb";
        String response = service.getCrawl(id, res);
        assertEquals("{\"status\":404,\"message\":\"crawl not found: 30vbllyb\"}", response);
    }

    @Test
    void testGetCrawl() {
        try {
            service = new Service();
            Main.tasks = new HashMap<String, CrawlData>();
            Response res = mock(Response.class);
            String id = service.startCrawlTask("Building");
            CrawlData data = Main.tasks.get(id);
            assertTrue(id == data.getId());
            Main.tasks.get(id).getCrawlProccess().get();
            String result = service.getCrawl(id, res);
            Gson gson = new Gson();
            ResponseGET resp = gson.fromJson(result, ResponseGET.class);
            assertEquals("done", resp.getStatus());
            assertTrue(resp.getId().length() == 8);
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    void testPostCrawl() {
        try {
            service = new Service();
            Main.tasks = new HashMap<String, CrawlData>();
            Response res = mock(Response.class);
            String result = service.postCrawl("Building", res);
            Gson gson = new Gson();
            ResponsePOST resp = gson.fromJson(result, ResponsePOST.class);
            assertTrue(resp.getId().length() == 8);
            Main.tasks.get(resp.getId()).getCrawlProccess().get();
            assertTrue(Main.tasks.get(resp.getId()).getCrawlProccess().isDone());
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }
    }
}
