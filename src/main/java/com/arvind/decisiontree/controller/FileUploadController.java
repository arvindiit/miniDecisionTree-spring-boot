package com.arvind.decisiontree.controller;

import com.arvind.decisiontree.player.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class FileUploadController {


    @Autowired
    private PlayerService playerService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        return "uploadForm";
    }

    @GetMapping("/refresh")
    public String refresh(Model model) throws IOException {
        return "uploadForm";
    }

    @PostMapping("/upload")
    @ResponseBody
    public List<String> handleFileUpload(@RequestParam("mainFile") MultipartFile mainFile,
                                         @RequestParam("supplementFile") MultipartFile supplementFile,
                                         @RequestParam("consultation") String consultation,
                                         RedirectAttributes redirectAttributes) {

        try{
            playerService.setMainFile(destinationFile(mainFile));
            playerService.setSupplementaryFile(destinationFile(supplementFile));
            playerService.setConsultation(consultation);
            return playerService.consult();
        }catch(IOException e){
            redirectAttributes.addFlashAttribute("message",
                    "Error in parsing file");
        } catch (JAXBException e) {
            redirectAttributes.addFlashAttribute("message",
                    "Error in consulting file");
        }
        return null;
    }



    private File destinationFile(MultipartFile file) throws IOException {

        if (!file.isEmpty()) {

            String realPathtoUploads = request.getServletContext().getRealPath("/upload");
            if (!new File(realPathtoUploads).exists()) {
                new File(realPathtoUploads).mkdir();
            }
            String orgName = file.getOriginalFilename();
            String filePath = realPathtoUploads + orgName;
            File dest = new File(filePath);
            file.transferTo(dest);
            return dest;
        }

        return null;
    }
}
