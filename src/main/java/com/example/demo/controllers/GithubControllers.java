package com.example.demo.controllers;

import java.util.List;
import java.util.Map;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

import com.example.demo.services.GitHubService;

@RestController
public class GithubControllers {
   
   @Autowired
   private GitHubService gitHubService;

   @GetMapping("/") // Root URL
   public String home() {
      return "redirect:/index.html"; // Serve static file from /static/index.html
   }

   @GetMapping("/export-github-users")
    public ResponseEntity<byte[]> exportGithubUsers() {
        List<Map<String, Object>> users = gitHubService.getGithubUsers();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            Document document = new Document(new com.itextpdf.kernel.pdf.PdfDocument(writer));

            // Title
            document.add(new Paragraph("GitHub Users List").setFontSize(20).setBold());

            // Add each user to the PDF
            for (int i = 0; i < Math.min(users.size(), 50); i++) {
                Map<String, Object> user = users.get(i);
                document.add(new Paragraph("Username: " + user.get("login")));
                document.add(new Paragraph("Profile URL: " + user.get("html_url")));
                document.add(new Paragraph("-----------------------------"));
            }

            document.close();

            // Return PDF as response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "github_users.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
      }
}
