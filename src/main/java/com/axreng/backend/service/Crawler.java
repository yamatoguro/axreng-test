package com.axreng.backend.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    private String BASE_URL = "";
    private boolean crawling = false;
    private ArrayList<String> matches = new ArrayList<String>();

    public boolean isCrawling() {
        return crawling;
    }

    public void setCrawling(boolean crawling) {
        this.crawling = crawling;
    }

    public ArrayList<String> getMatches() {
        return matches;
    }

    public boolean aquireBaseUrl() {
        try {
            BASE_URL = System.getenv("BASE_URL");
            return true;
        } catch (NullPointerException e) {
            System.out.println("Environment variable not found"); // TODO: Handle this exception
            return false;
        } catch (SecurityException e) {
            System.out.println("Can't access environment variable"); // TODO: Handle this exception
            return false;
        }
    }

    private URL verifyUrl(String url) {
        if (!url.toLowerCase().startsWith("http://"))
            return null;
        if (!url.toLowerCase().startsWith(BASE_URL))
            return null;
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            return null;
        }
        return verifiedUrl;
    }

    private String downloadPage(URL pageUrl) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    pageUrl.openStream()));
            String line;
            StringBuffer pageBuffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                pageBuffer.append(line);
            }
            return pageBuffer.toString();
        } catch (Exception e) {
        }
        return null;
    }

    private ArrayList<String> retrieveLinks(
            URL pageUrl, String pageContents, HashSet<String> crawledList) {
        Pattern p = Pattern.compile("<a\\s*(.*?)\\s*href\\s*=\\s*\"?(.*?)[\"|>]",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(pageContents);
        ArrayList<String> linkList = new ArrayList<String>();
        while (m.find()) {
            String link = m.group(2).trim();
            System.out.println(link);

            if (link.toLowerCase().startsWith("../"))
                link = BASE_URL + link.substring(3);
            if (!link.toLowerCase().startsWith("http://") && !link.toLowerCase().startsWith("https://"))
                link = BASE_URL + link;
            System.out.println(link);

            if (link.length() < 1) {
                continue;
            }
            if (link.charAt(0) == '#') {
                continue;
            }
            if (link.indexOf("mailto:") != -1) {
                continue;
            }
            if (link.toLowerCase().indexOf("javascript") != -1) {
                continue;
            }
            if (link.indexOf("://") == -1) {
                if (link.charAt(0) == '/') {
                    link = "http://" + pageUrl.getHost() + link;
                } else {
                    String file = pageUrl.getFile();
                    if (file.indexOf('/') == -1) {
                        link = "http://" + pageUrl.getHost() + "/" + link;
                    } else {
                        String path = file.substring(0, file.lastIndexOf('/') + 1);
                        link = "http://" + pageUrl.getHost() + path + link;
                    }
                }
            }
            int index = link.indexOf('#');
            if (index != -1) {
                link = link.substring(0, index);
            }
            URL verifiedLink = verifyUrl(link);
            if (verifiedLink == null) {
                continue;
            }

            if (crawledList.contains(link)) {
                continue;
            }
            linkList.add(link);
        }
        return (linkList);
    }

    private boolean searchStringMatches(
            String pageContents, String searchString,
            boolean caseSensitive) {
        String searchContents = pageContents;

        if (!caseSensitive) {
            searchContents = pageContents.toLowerCase();
        }

        Pattern p = Pattern.compile("[\\s]+");
        String[] terms = p.split(searchString);
        for (int i = 0; i < terms.length; i++) {
            if (caseSensitive) {
                if (searchContents.indexOf(terms[i]) == -1) {
                    return false;
                }
            } else {
                if (searchContents.indexOf(terms[i].toLowerCase()) == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public void crawl(
            String startUrl, int maxUrls, boolean limitHost,
            String searchString, boolean caseSensitive) {
        HashSet<String> crawledList = new HashSet<String>();
        LinkedHashSet<String> toCrawlList = new LinkedHashSet<String>();
        toCrawlList.add(startUrl);

        while (crawling && toCrawlList.size() > 0) {

            String url = (String) toCrawlList.iterator().next();
            toCrawlList.remove(url);
            URL verifiedUrl = verifyUrl(url);

            crawledList.add(url);
            String pageContents = downloadPage(verifiedUrl);
            if (pageContents != null && pageContents.length() > 0) {
                ArrayList<String> links = retrieveLinks(verifiedUrl, pageContents, crawledList);
                toCrawlList.addAll(links);
                if (searchStringMatches(pageContents, searchString,
                        caseSensitive)) {
                    addMatch(url);
                }
            }
        }
    }

    private void addMatch(String url) {
        matches.add(url);
    }
}
