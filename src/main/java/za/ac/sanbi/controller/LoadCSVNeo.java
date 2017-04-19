package za.ac.sanbi.controller;

import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import za.ac.sanbi.domain.NeoUserDetails;
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
    	
    	File file = new File("./" + request.getParameter("process"));
        String path = "file:///" + file.getAbsolutePath();
        String query = constructQuery(path);
        runNeoQuery(query);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        NeoUserDetails user = (NeoUserDetails) auth.getPrincipal();
        file.renameTo(new File("./users/" + user.getUsername() + "/processed/" + file.getName()));
        
        return "redirect:/track";
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
        template.query(query, paramsQuery);
    }
}
