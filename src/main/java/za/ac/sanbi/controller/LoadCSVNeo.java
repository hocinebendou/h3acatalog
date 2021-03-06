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
    	
    	String[] parameters = request.getParameter("fileOwnerRole").split("-");
    	String fileOwner = parameters[0];
    	String fileRole = parameters[1];
    	String userRole = parameters[2];
        
        if (fileRole.isEmpty() || !userRole.equals("ADMIN")) return "redirect:/logout";
        
        File file = new File("./" + request.getParameter("process"));
        String path = "file:///" + file.getAbsolutePath();
        NeoUserDetails user = userRepository.findByUsername(fileOwner);
    	String query = "";
    	switch (fileRole) {
    		case "ARCHIVE":
    			query = constructArchiveQuery(path, file);
    			break;
    		case "BIOBANK":
    			query = constructBiobankQuery(path, user.getBiobankname());
    			break;
    		default:
    			break;
    	}
        runNeoQuery(query);

        file.renameTo(new File("./users/" + user.getUsername() + "/processed/" + file.getName()));
        
        String pathRedirect = "redirect:/track?biobank=" + fileOwner; 
        
        return pathRedirect;
    }

    private String constructArchiveQuery(String path, File file) {
    	String fileType = file.getName().split("_")[1];
    	String query = "";
    	if (fileType.equals("study")) {
	        query += "LOAD CSV WITH HEADERS FROM \"" + path + "\" AS row ";
	        query += "MERGE (s:NeoStudy {acronym: row.acronym, title: row.title, description: row.description}) ";
	        query += "MERGE (d:NeoDesign {name: row.design}) ";
	        query += "WITH s, d, row ";
	        query += "MERGE (d)-[r:STUDY_DESIGN]->(s)";
    	} else if (fileType.equals("individual")) {
    		query += "LOAD CSV WITH HEADERS FROM \"" + path + "\" AS row ";
    		query += "MATCH(s:NeoStudy {acronym: row.acronym}) ";
    		query += "WITH s, row, count(*) AS c ";
    		query += "WHERE c = 1 ";
    		query += "MERGE (p:NeoSample {sampleId: row.sample_id}) ";
    		query += "ON CREATE SET ";
    		query += "p.species = row.species ";
    		query += "MERGE (d:NeoCharacter {name: row.case_control}) ";
    		query += "MERGE (g:NeoGender {name: row.sex}) ";
    		query += "MERGE (e:NeoEthnicity {name: row.ethnicity}) ";
    		query += "MERGE (p)-[rc:HAS_CHARACTER]->(d) ";
    		query += "MERGE (p)-[rg:HAS_GENDER]->(g) ";
    		query += "MERGE (p)-[re:HAS_ETHNICITY]->(e) ";
    		query += "MERGE (s)-[r:HAS_SAMPLE]->(p) ";
    	}
        return query;
    }
    
    private String constructBiobankQuery(String path, String biobankName) {
    	String query = "";
    	query += "LOAD CSV WITH HEADERS FROM \"" + path + "\" AS row ";  
    	query += "MATCH(p:NeoSample {sampleId: row.PARTICIPANT_ID}) ";
    	query += "SET ";
    	query += "p.collectionDate=row.COLLECTION_DATE, ";
    	query += "p.ageAtCollection=row.AGE_AT_COLLECTION, ";
    	query += "p.sampleVolume=row.SAMPLE_VOLUME, ";
    	query += "p.dnaConcentration=row.DNA_CONCENTRATION, ";
    	query += "p.dnaPurity_260_280=row.DNA_PURITY_260_280, ";
    	query += "p.extractionMethod=row.EXTRACTION_METHOD, ";
    	query += "p.biobankName=\"" + biobankName + "\"";
    	query += "MERGE (c:NeoCountry {name: row.COUNTRY}) ";
    	query += "MERGE (t:NeoSpecType {name: row.SPECIMEN_TYPE}) ";
    	query += "MERGE (p)-[rc:HAS_COUNTRY]->(c) ";
    	query += "MERGE (p)-[rt:HAS_SPECTYPE]->(t)";
    	
    	return query;
    }
    
    private void runNeoQuery(String query) {
        LinkedHashMap<String, String> paramsQuery = new LinkedHashMap<>();
        template.query(query, paramsQuery);
    }
}
