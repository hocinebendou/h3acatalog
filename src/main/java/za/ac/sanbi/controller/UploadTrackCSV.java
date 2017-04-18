package za.ac.sanbi.controller;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import za.ac.sanbi.domain.NeoUserDetails;
import za.ac.sanbi.track.TrackCSVFiles;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hocine on 2017/04/18.
 */
@Controller
public class UploadTrackCSV {

    public static final Resource ARCHIVE_DIR_RAW = new FileSystemResource("./src/main/resources/static/users/archive/raw");

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String onUpload(MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

        if (file.isEmpty() || !isCSV(file)) {
            redirectAttributes.addFlashAttribute("error", "Incorrect file. Please upload a CSV.");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        NeoUserDetails user = (NeoUserDetails) auth.getPrincipal();

        Resource dir = null;
        switch (user.getRole()) {
            case "ARCHIVE":
                dir = ARCHIVE_DIR_RAW;
                break;
            default:
                throw new IllegalArgumentException("Invalid Role: " + user.getRole());
        }

        Resource fResource = copyFileToDir(file, dir);
        redirectAttributes.addFlashAttribute("success", "File uploaded successfully!");

        return "redirect:/admin";
    }

    @RequestMapping(value = "/track")
    public String trackCSVFiles(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        NeoUserDetails user = (NeoUserDetails) auth.getPrincipal();
        try {
            File rawFolder = new ClassPathResource("static/users/" + user.getUsername() + "/raw").getFile();
            List<TrackCSVFiles> rawFiles = listCSVFiles(user, rawFolder, "raw");
            model.addAttribute("rawFiles", rawFiles);
        } catch (IOException e){
            e.printStackTrace();
        }

        try {
            File processedFolder = new ClassPathResource("static/users/" + user.getUsername() + "/processed").getFile();
            List<TrackCSVFiles> processedFiles = listCSVFiles(user, processedFolder, "processed");
            model.addAttribute("processedFiles", processedFiles);
        } catch (IOException e){
            e.printStackTrace();
        }

        return "admin/trackPage";
    }

    @RequestMapping(value = "/track", params = {"delete"}, method = RequestMethod.POST)
    public String deleteCSVFile(HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException{
        String pathToFileToDelete = request.getParameter("delete");
        File fileToDelete = new ClassPathResource("static" + pathToFileToDelete).getFile();

        Boolean deleted = false;
        if (fileToDelete.exists())
            deleted = fileToDelete.delete();

        if (deleted)
            redirectAttributes.addFlashAttribute("success", "File deleted successfully!");
        else
            redirectAttributes.addFlashAttribute("error", "Error: File not found!");

        return "redirect:/track";
    }

    private List<TrackCSVFiles> listCSVFiles(NeoUserDetails user, File folder, String type) {
        List<TrackCSVFiles> listRet = new ArrayList<>();

        try {
            for (String fileName : folder.list()) {
                String path = "/users/" + user.getUsername() + "/raw/" + fileName;
                TrackCSVFiles file = new TrackCSVFiles(path, fileName, user.getUsername(), user.getRole(), "raw");
                listRet.add(file);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return listRet;
    }

    private Resource copyFileToDir(MultipartFile file, Resource dir) throws IOException{

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String prefix =  "archive_" + LocalDate.now().toString() + "_";
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
