package com.funnco.wikiparser.service;

import com.funnco.wikiparser.dto.WikiPageModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParseService {

    private Document getDocument(String query) throws IOException {
        String url = "https://ru.wikipedia.org/w/index.php?search=" + query +
                "&title=Служебная:Поиск&profile=advanced&fulltext=1&ns0=1&limit=1000";
        return Jsoup.connect(url).maxBodySize(0).get();
    }

    private LocalDateTime extractRedactionDateTimeFromData(String data){
        return LocalDateTime.parse(data.substring(data.indexOf('-')+2), DateTimeFormatter.ofPattern("HH:mm, d MMMM yyyy"));
    }

    private int extractWordCountFromData(String data){
        return Integer.parseInt(data.substring(data.indexOf('(')+1, data.indexOf(" слов")).replace(" ", ""));
    }


    private List<WikiPageModel> parseDocumentForPages(Document document){
        Elements elements = document.select("li.mw-search-result");
        return elements.stream().map(element -> {
            String pageData = element.getElementsByClass("mw-search-result-data").get(0).text();
            String title = element.getElementsByClass("mw-search-result-heading").get(0).text();
            String url = "https://ru.wikipedia.org/wiki/"+title.replace(" ","_");
            String searchMatch = element.getElementsByClass("searchresult").get(0).text();
            LocalDateTime redactionTime = extractRedactionDateTimeFromData(pageData);
            int wordCount = extractWordCountFromData(pageData);

            return new WikiPageModel(url, title, redactionTime, wordCount, searchMatch);
        }).collect(Collectors.toList());
    }

    public List<WikiPageModel> getPagesByQuery(String query) {
        try {
            return parseDocumentForPages(getDocument(query));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(500), "Internal server error, please contact developer");
        }
    }
}
