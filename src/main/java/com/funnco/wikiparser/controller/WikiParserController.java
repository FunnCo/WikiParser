package com.funnco.wikiparser.controller;


import com.funnco.wikiparser.dto.WikiPageModel;
import com.funnco.wikiparser.service.ParseService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/wikiparser")
public class WikiParserController {

    final ParseService parseService;

    public WikiParserController(ParseService parseService) {
        this.parseService = parseService;
    }

    @RequestMapping("/parse")
    public ResponseEntity<List<WikiPageModel>> getJSONWikiPages(@RequestParam("query") String query) {
        if(query.isBlank()){
            return ResponseEntity.badRequest().build();
        }
        List<WikiPageModel> list = parseService.getPagesByQuery(query);
        return ResponseEntity.ok(list);
    }
}
