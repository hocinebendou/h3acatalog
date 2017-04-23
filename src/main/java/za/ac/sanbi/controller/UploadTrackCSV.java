package za.ac.sanbi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import za.ac.sanbi.domain.NeoUserDetails;
import za.ac.sanbi.repositories.UserRepository;
import za.ac.sanbi.track.TrackCSVFiles;

/**
 * Created by hocine on 2017/04/18.
 */
@Controller
public class UploadTrackCSV {

	//TODO: SELECT RESOURCE BASED ON USERNAME (WRITE A FUNCTION FOR SELECTION)
    public static final Resource ARCHIVE_DIR_RAW = new FileSystemResource("./users/archive/raw");
    
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String onUpload(MultipartFile file, RedirectAttributes redirectAttributes, Model model) {
    	
    	if (file.isEmpty() || !isCSV(file)) {
            redirectAttributes.addFlashAttribute("error", "Incorrect file. Please upload a CSV.");
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        NeoUserDetails user = (NeoUserDetails) auth.getPrincipal();
        
        Resource dir = userDirectory(user);
        
        try {
	        copyFileToDir(file, dir, user);
	        redirectAttributes.addFlashAttribute("success", "File uploaded successfully!");
        } catch(Exception e) {
        	redirectAttributes.addFlashAttribute("error", "Erro when loading the file!");
        }
        
        model.addAttribute("user", user);
        
        return "redirect:/admin";
    }
    
    private Resource userDirectory(NeoUserDetails user) {
    	return new FileSystemResource("./users/" + user.getUsername() + "/raw");
    }
    
    @Autowired UserRepository userRepository;
    
    @RequestMapping(value = "/track")
    public String trackCSVFiles(@RequestParam(name="biobank", defaultValue="") String biobankName, Model model) {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	NeoUserDetails currentUser = (NeoUserDetails) auth.getPrincipal();
    	NeoUserDetails user = null;
    	if (biobankName != null && !biobankName.isEmpty())
    		user = userRepository.findByUsername(biobankName);
    	else
    		user = currentUser;
    	
    	
    	File rawFolder = null;
    	File processedFolder = null;
    	
    	// TODO: REPETED CODE SHOULD BE REWRITED TO FUNCTION
    	try {
    		rawFolder = new File("./users/" + user.getUsername() + "/raw");
    		List<TrackCSVFiles> rawFiles = listCSVFiles(user.getUsername(), currentUser.getRole(), rawFolder, "raw");
    		model.addAttribute("rawFiles", rawFiles);
    	} catch(NullPointerException e) {
    		System.err.println("Err: Folder of files to process is empty!");
    	}
    	
    	try {
	        //File processedFolder = new ClassPathResource("./users/" + user.getUsername() + "/processed").getFile();
	        processedFolder = new File("./users/" + user.getUsername() + "/processed");
	        List<TrackCSVFiles> processedFiles = listCSVFiles(user.getUsername(), currentUser.getRole(), processedFolder, "processed");
		    model.addAttribute("processedFiles", processedFiles);
    	} catch(NullPointerException e) {
    		System.err.println("Err: Folder of files processed is empty!");
    	}
    	
    	model.addAttribute("user", currentUser);
    	
        return "admin/trackPage";
    }

    @RequestMapping(value = "/track", params = {"delete"}, method = RequestMethod.POST)
    public String deleteCSVFile(HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException{
        String pathToFileToDelete = request.getParameter("delete");
        //File fileToDelete = new ClassPathResource("static" + pathToFileToDelete).getFile();
        File fileToDelete = new File("." + pathToFileToDelete);
        Boolean deleted = false;
        if (fileToDelete.exists())
            deleted = fileToDelete.delete();

        if (deleted)
            redirectAttributes.addFlashAttribute("success", "File deleted successfully!");
        else
            redirectAttributes.addFlashAttribute("error", "Error: File not found!");

        return "redirect:/track";
    }

    private List<TrackCSVFiles> listCSVFiles(String username, String role, File folder, String type) throws NullPointerException {
        List<TrackCSVFiles> listRet = new ArrayList<>();

        for (String fileName : folder.list()) {
            String path = "/users/" + username + "/raw/" + fileName;
            TrackCSVFiles file = new TrackCSVFiles(path, fileName, username, role, "raw");
            listRet.add(file);
        }

        return listRet;
    }

    private Resource copyFileToDir(MultipartFile file, Resource dir, NeoUserDetails user) throws IOException{

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String prefix =  user.getUsername() + "_" + LocalDate.now().toString() + "_";
        File tempFile = File.createTempFile(prefix, fileExtension, dir.getFile());
        try (InputStream in = file.getInputStream();
             OutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
        return new FileSystemResource(tempFile);
    }

    private boolean isCSV(MultipartFile file) {
        return file.getContentType().startsWith("text/csv");
    }
    private static String getFileExtension(String name) {
        return name.substring(name.lastIndexOf("."));
    }
}
