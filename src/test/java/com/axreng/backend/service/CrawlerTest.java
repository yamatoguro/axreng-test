package com.axreng.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.axreng.backend.model.CrawlData;

public class CrawlerTest {
    @Test
    void testSetBASE_URL() {
        Crawler crawler = new Crawler();
        assertFalse(crawler.isCrawling());
        assertTrue(crawler.setBaseUrl("https://c99.io/"));
        assertEquals("https://c99.io/", crawler.getBaseUrl());
    }

    @Test
    void testIsCrawling() {
        Crawler crawler = new Crawler();
        assertFalse(crawler.isCrawling());
        crawler.setCrawling(true);
        assertTrue(crawler.isCrawling());
    }

    @Test
    void testCrawl() {
        Crawler crawler = new Crawler();
        assertFalse(crawler.isCrawling());
        assertTrue(crawler.setBaseUrl("https://c99.io/"));
        assertEquals("https://c99.io/", crawler.getBaseUrl());

        CrawlData data = new CrawlData("30vbllyb");
        data.getTerms().add("Building");
        crawler.setCrawling(true);
        crawler.crawl(crawler.getBaseUrl(), 1, true, true, data);
        crawler.setCrawling(false);
        assertEquals(1, data.getUrls().size());
        assertEquals("https://c99.io/", data.getUrls().get(0));
    }
}
