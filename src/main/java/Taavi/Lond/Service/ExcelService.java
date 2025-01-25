package Taavi.Lond.Service;

import Taavi.Lond.DataRow;
import Taavi.Lond.ExcelReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ExcelService {

    @Value("${html.filePath}")
    private String htmlFilePath;

    private List<DataRow> cachedData;
    private long lastModifiedTime;

    // Returns the data from the most recent Excel file
    public List<DataRow> getExcelData() throws IOException {
        if (cachedData == null || isFileUpdated()) {
            String latestExcelFilePath = findLatestExcelFile(htmlFilePath);
            cachedData = extractDataFromExcelFile(latestExcelFilePath);
            lastModifiedTime = getFileLastModifiedTime(latestExcelFilePath);
        }
        return cachedData;
    }

    // Check if the HTML file has been updated based on last modified time
    private boolean isFileUpdated() {
        long currentModifiedTime = getFileLastModifiedTime(htmlFilePath);
        return currentModifiedTime > lastModifiedTime;
    }

    // Get the last modified time of the HTML file or the Excel file
    private long getFileLastModifiedTime(String filePath) {
        try {
            Path path = Paths.get(filePath);
            BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
            return attrs.lastModifiedTime().toMillis();
        } catch (IOException e) {
            throw new RuntimeException("Error checking file last modified time", e);
        }
    }

    // Extract data from the HTML file to find the latest Excel file
    private String findLatestExcelFile(String htmlFilePath) throws IOException {
        Path path = Paths.get(htmlFilePath);
        Document doc = Jsoup.parse(path.toFile(), "UTF-8");

        // Find links to Excel files
        List<Element> excelLinks = doc.select("a[href$=.xlsx], a[href$=.xls]");

        // Find the latest Excel file URL
        String latestExcelUrl = excelLinks.stream()
                .map(link -> link.attr("href"))
                .max(Comparator.comparing(this::extractDateFromFileName))
                .orElseThrow(() -> new RuntimeException("No Excel file found in HTML"));

        // Download the latest Excel file and return the local file path
        return downloadFile(latestExcelUrl);
    }

    // Extract a date or version from the file name (assumes the filename includes date information)
    private Long extractDateFromFileName(String fileName) {
        // Example: if the filename has a date like "report_2025_01_19.xlsx"
        // Modify this based on your filename format
        String dateStr = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
        try {
            // Parse date into milliseconds (you can modify this to match the filename format)
            return Long.parseLong(dateStr.replaceAll("\\D", ""));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Error parsing date from file name", e);
        }
    }

    // Extract data from the Excel file (you can reuse your ExcelReader class or implement your own logic)
    private List<DataRow> extractDataFromExcelFile(String filePath) throws IOException {
        // Assuming you have a method to extract data from Excel files
        ExcelReader reader = new ExcelReader();
        return reader.readExcelFile(filePath);  // Adjust if your reader needs modification
    }

    private String downloadFile(String fileUrl) throws IOException {
        // Create a temporary directory for downloads
        Path tempDir = Files.createTempDirectory("excel_downloads");
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        Path tempFile = tempDir.resolve(fileName);

        // Download the file
        try (InputStream in = new URL(fileUrl).openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error downloading file from URL: " + fileUrl, e);
        }

        return tempFile.toString(); // Return the local file path
    }

}
