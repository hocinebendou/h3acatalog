package za.ac.sanbi.controller;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import za.ac.sanbi.repositories.UploadRepository;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.LinkedHashMap;

/**
 * Created by hocine on 2017/04/17.
 */
@Controller
public class LoadCSVNeo {

    @Autowired
    UploadRepository uploadRepository;
    @Autowired Session template;

    @RequestMapping(value = "/track", params = {"process"}, method = RequestMethod.POST)
    public String processCSVNeo(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception{

        String path = "http://localhost:8080" + request.getParameter("process");
        String query = constructQuery(path);
        runNeoQuery(query);

        File file = new ClassPathResource("static" + request.getParameter("process")).getFile();
        file.renameTo(new File("/users/archive/processed/test.csv"));
        return "homePage";
    }

    private String constructQuery(String path) {
        String query = "";
        query += "LOAD CSV WITH HEADERS FROM \"" + path + "\" AS row ";
        query += "MERGE (s:NeoStudy {acronym: row.acronym, title: row.title, description: row.description}) ";
        query += "MERGE (d:NeoDesign {name: row.design})";
        query += "WITH s, d, row ";
        query += "MERGE (d)-[r:STUDY_DESIGN]->(s)";

        return query;
    }

    private void runNeoQuery(String query) {
        LinkedHashMap<String, String> paramsQuery = new LinkedHashMap<>();
        Result result = template.query(query, paramsQuery);
    }
}
