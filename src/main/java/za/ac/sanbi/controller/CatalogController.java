package za.ac.sanbi.controller;

import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import za.ac.sanbi.domain.NeoUserDetails;

import java.io.*;
import java.time.LocalDate;

/**
 * Created by hocine on 2017/04/13.
 */
@Controller
public class CatalogController {

    public static final Resource ARCHIVE_DIR_RAW = new FileSystemResource("./archive/raw");

    @RequestMapping("/")
    public String goTohomePage() {
        return "homePage";
    }

    @RequestMapping("/login")
    public String goToLoginPage() {
        return "login/loginPage";
    }

    @RequestMapping("/admin")
    public String goToAdminPage() {
        return "admin/adminPage";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String onUpload(MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {

        if (file.isEmpty() || !isCSV(file)) {
            redirectAttributes.addFlashAttribute("error", "Incorrect file. Please upload a CSV.");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        NeoUserDetails user = (NeoUserDetails) auth.getPrincipal();

        Resource dir = null;
        switch (user.getRole()) {
            case "USER":
                dir = ARCHIVE_DIR_RAW;
                break;
            default:
                throw new IllegalArgumentException("Invalid Role: " + user.getRole());
        }

        copyFileToDir(file, dir);
        redirectAttributes.addFlashAttribute("success", "File uploaded successfully!");

        return "redirect:/admin";
    }

    private Resource copyFileToDir(MultipartFile file, Resource dir) throws IOException{

        String fileExtension = getFileExtension(file.getOriginalFilename());
        String prefix =  "archive_" + LocalDate.now().toString() + "_";
        File tempFile = File.createTempFile(prefix, fileExtension, dir.getFile());
        try (InputStream in = file.getInputStream();
             OutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
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
