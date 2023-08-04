package com.axreng.backend.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Future;

public class CrawlData {
    private ArrayList<String> terms;
    private ArrayList<String> urls;
    private String id;
    private Future<String> crawlProccess;
    private Instant startInstant = Instant.now();
    private Instant finishInstant;

    public CrawlData(String id) {
        terms = new ArrayList<String>();
        urls = new ArrayList<String>();
        this.id = id;
    }

    public ArrayList<String> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<String> terms) {
        this.terms = terms;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Future<String> getCrawlProccess() {
        return crawlProccess;
    }

    public void setCrawlProccess(Future<String> crawlProccess) {
        this.crawlProccess = crawlProccess;
    }

    public Instant getStartInstant() {
        return startInstant;
    }

    public void setStartInstant(Instant startInstant) {
        this.startInstant = startInstant;
    }

    public Instant getFinishInstant() {
        return finishInstant;
    }

    public void setFinishInstant(Instant finishInstant) {
        this.finishInstant = finishInstant;
    }
}
