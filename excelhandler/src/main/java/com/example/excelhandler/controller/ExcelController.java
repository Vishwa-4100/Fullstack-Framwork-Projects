package com.example.excelhandler.controller;

import com.example.excelhandler.service.ExcelService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ExcelController {

    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("employees", excelService.getAllEmployees());
        return "index";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            excelService.saveFromExcel(file);
            return "redirect:/?success=true";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/?error=true";
        }
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download() {
        try {
            InputStreamResource body = new InputStreamResource(excelService.exportToExcel());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employees.xlsx")
                    .contentType(MediaType.parseMediaType(
                      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(body);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
