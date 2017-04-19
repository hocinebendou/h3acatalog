package za.ac.sanbi.controller;

import java.io.File;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import za.ac.sanbi.domain.NeoUserDetails;
import za.ac.sanbi.repositories.UserRepository;

/**
 * Created by hocine on 2017/04/17.
 */
@Controller
public class LoadCSVNeo {

    @Autowired
    UserRepository userRepository;
    
    @Autowired Session template;

    @RequestMapping(value = "/track", params = {"process"}, method = RequestMethod.POST)
    public String processCSVNeo(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception{
    	
    	String parameter = request.getParameter("parameter");
    	File file = new File("./" + request.getParameter("process"));
        String path = "file:///" + file.getAbsolutePath();
        String query = constructQuery(path);
        runNeoQuery(query);

        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //NeoUserDetails currentUser = (NeoUserDetails) auth.getPrincipal();
        NeoUserDetails user = userRepository.findByUsername(parameter);
        file.renameTo(new File("./users/" + user.getUsername() + "/processed/" + file.getName()));
        
        String pathRedirect = "redirect:/track?biobank=" + parameter; 
        
        return pathRedirect;
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
