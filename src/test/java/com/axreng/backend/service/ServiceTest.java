package com.axreng.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import java.lang.SecurityException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.axreng.backend.Main;
import com.axreng.backend.model.CrawlData;

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
        when(service.getEnv()).thenAnswer(invocation -> {return "https://c99.io/";});
        String url = service.getBaseURL();
        assertEquals("https://c99.io/", url);
    }

    @Test
    void testGetBaseURLNullPointer() {
        service = spy(Service.class);
        when(service.getEnv()).thenAnswer(invocation -> {throw new NullPointerException();});
        String url = service.getBaseURL();
        assertEquals("ERROR: Environment variable not found", url);
    }

    @Test
    void testGetBaseURLSecurity() {
        service = spy(Service.class);
        when(service.getEnv()).thenAnswer(invocation -> {throw new SecurityException();});
        String url = service.getBaseURL();
        assertEquals("ERROR: Can't access environment variable", url);
    }

    @Test
    void testGetCrawlByID() {
        service = new Service();
        Crawler crawler = new Crawler();
        CrawlData data = new CrawlData("30vbllyb");
        crawler.setBaseUrl("https://c99.io/");
        data.getTerms().add("Building");
        crawler.setCrawling(true);
        crawler.crawl(crawler.getBaseUrl(), 1, true, true, data);
        crawler.setCrawling(false);
        Main.tasks = new HashMap<String, CrawlData>();
        Main.tasks.put("30vbllyb", data);
        String url = service.getCrawlByID("30vbllyb")[0];
        assertTrue(url.equals("https://c99.io/"));
    }

    @Test
    void testGetCrawl() {

    }

    @Test
    void testPostCrawl() {

    }
}
