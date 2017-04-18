package za.ac.sanbi.controller;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import za.ac.sanbi.repositories.UploadRepository;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by hocine on 2017/04/17.
 */
@Controller
public class LoadCSVNeo {

    @Autowired
    UploadRepository uploadRepository;
    @Autowired Session template;

    @RequestMapping("/loadcsv")
    public String loadCSVNeo(@RequestParam("path") String filePath, Model model) {

        String path = "http://localhost:8080/hocine/archive_2017-04-17_1720655791805931728.csv";
        String query = constructQuery(path);
        runNeoQuery(query);

        return "homePage";
    }

    private String constructQuery(String path) {
        String query = "";
        query += "LOAD CSV WITH HEADERS FROM \"" + path + "\" AS row ";
        query += "MERGE (s:Study {acronym: row.acronym, title: row.title, description: row.description}) ";
        query += "MERGE (d:Design {name: row.design})";
        query += "WITH s, d, row ";
        query += "MERGE (d)-[r:STUDY_DESIGN]->(s)";

        return query;
    }

    private void runNeoQuery(String query) {
        LinkedHashMap<String, String> paramsQuery = new LinkedHashMap<>();
        Result result = template.query(query, paramsQuery);
    }
}
