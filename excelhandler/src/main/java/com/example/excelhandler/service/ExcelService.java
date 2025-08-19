package com.example.excelhandler.service;

import com.example.excelhandler.entity.Employee;
import com.example.excelhandler.repository.EmployeeRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ExcelService {

    private final EmployeeRepository repository;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ExcelService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    /**
     * Expected columns (row 0 header): Name | Email | Salary | In Time | Out Time
     * In/Out Time can be Excel date cell or String "yyyy-MM-dd HH:mm".
     */
    public void saveFromExcel(MultipartFile file) throws IOException {
        List<Employee> parsed = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) return;

            int lastRow = sheet.getLastRowNum();
            for (int i = 1; i <= lastRow; i++) { // skip header row
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                Employee e = new Employee();
                e.setName(getString(row.getCell(0)));
                e.setEmail(getString(row.getCell(1)));
                e.setSalary(getDouble(row.getCell(2)));
                e.setInTime(getLocalDateTime(row.getCell(3)));
                e.setOutTime(getLocalDateTime(row.getCell(4)));

                if (e.getEmail() == null || e.getEmail().isBlank()) {
                    // skip invalid rows without email
                    continue;
                }
                parsed.add(e);
            }
        }

        // Upsert by email
        for (Employee e : parsed) {
            repository.findByEmail(e.getEmail()).ifPresentOrElse(existing -> {
                existing.setName(e.getName());
                existing.setSalary(e.getSalary());
                existing.setInTime(e.getInTime());
                existing.setOutTime(e.getOutTime());
                repository.save(existing);
            }, () -> repository.save(e));
        }
    }

    public ByteArrayInputStream exportToExcel() throws IOException {
        String[] cols = {"Name", "Email", "Salary", "In Time", "Out Time"};
        List<Employee> employees = repository.findAll();

        try (Workbook wb = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = wb.createSheet("Employees");

            // Header
            Row header = sheet.createRow(0);
            for (int i = 0; i < cols.length; i++) header.createCell(i).setCellValue(cols[i]);

            // Data
            int r = 1;
            for (Employee e : employees) {
                Row row = sheet.createRow(r++);
                row.createCell(0).setCellValue(nz(e.getName()));
                row.createCell(1).setCellValue(nz(e.getEmail()));
                row.createCell(2).setCellValue(e.getSalary());
                row.createCell(3).setCellValue(e.getInTime() != null ? e.getInTime().format(FORMATTER) : "");
                row.createCell(4).setCellValue(e.getOutTime() != null ? e.getOutTime().format(FORMATTER) : "");
            }

            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);

            wb.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    // -------- helpers --------
    private static boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

    private static String getString(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> null;
        };
    }

    private static double getDouble(Cell cell) {
        if (cell == null) return 0.0;
        return switch (cell.getCellType()) {
            case NUMERIC -> cell.getNumericCellValue();
            case STRING -> {
                try { yield Double.parseDouble(cell.getStringCellValue().trim()); }
                catch (Exception e) { yield 0.0; }
            }
            default -> 0.0;
        };
    }

    private static LocalDateTime getLocalDateTime(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getLocalDateTimeCellValue();
            } else if (cell.getCellType() == CellType.STRING) {
                String s = cell.getStringCellValue().trim();
                if (s.isEmpty()) return null;
                return LocalDateTime.parse(s, FORMATTER);
            }
        } catch (Exception ignored) {}
        return null;
    }

    private static String nz(String s) { return s == null ? "" : s; }
}
