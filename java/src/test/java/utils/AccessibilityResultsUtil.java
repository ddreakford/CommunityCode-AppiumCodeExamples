package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class AccessibilityResultsUtil {

    private static final String REPORTS_DIR = "reports/accessibility";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static void saveResults(Object scanResults, String platform, String scanName) {
        try {
            ensureReportsDirectoryExists();
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String baseFileName = String.format("%s-accessibility-scan_%s", platform.toLowerCase(), timestamp);

            saveAsJson(scanResults, baseFileName);
            saveAsHtml(scanResults, baseFileName, platform, scanName);

            System.out.println("Accessibility scan results saved:");
            System.out.println("  JSON: " + REPORTS_DIR + "/" + baseFileName + ".json");
            System.out.println("  HTML: " + REPORTS_DIR + "/" + baseFileName + ".html");

        } catch (Exception e) {
            System.err.println("Failed to save accessibility scan results: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void ensureReportsDirectoryExists() throws IOException {
        Path reportsPath = Paths.get(REPORTS_DIR);
        if (!Files.exists(reportsPath)) {
            Files.createDirectories(reportsPath);
            System.out.println("Created reports directory: " + REPORTS_DIR);
        }
    }

    private static void saveAsJson(Object scanResults, String baseFileName) throws IOException {
        String jsonFileName = REPORTS_DIR + "/" + baseFileName + ".json";
        objectMapper.writeValue(new File(jsonFileName), scanResults);
    }

    private static void saveAsHtml(Object scanResults, String baseFileName, String platform, String scanName) throws IOException {
        String htmlFileName = REPORTS_DIR + "/" + baseFileName + ".html";
        String jsonContent = objectMapper.writeValueAsString(scanResults);

        String htmlContent = generateHtmlReport(jsonContent, platform, scanName, baseFileName);

        try (FileWriter writer = new FileWriter(htmlFileName)) {
            writer.write(htmlContent);
        }
    }

    private static String generateHtmlReport(String jsonContent, String platform, String scanName, String baseFileName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return String.format(
            "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Accessibility Scan Results - %s</title>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            font-family: Arial, sans-serif;\n" +
            "            margin: 20px;\n" +
            "            background-color: #f5f5f5;\n" +
            "        }\n" +
            "        .container {\n" +
            "            max-width: 1200px;\n" +
            "            margin: 0 auto;\n" +
            "            background-color: white;\n" +
            "            padding: 20px;\n" +
            "            border-radius: 8px;\n" +
            "            box-shadow: 0 2px 10px rgba(0,0,0,0.1);\n" +
            "        }\n" +
            "        .header {\n" +
            "            border-bottom: 2px solid #007acc;\n" +
            "            padding-bottom: 20px;\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "        .header h1 {\n" +
            "            color: #007acc;\n" +
            "            margin: 0;\n" +
            "        }\n" +
            "        .metadata {\n" +
            "            background-color: #f8f9fa;\n" +
            "            padding: 15px;\n" +
            "            border-radius: 5px;\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "        .metadata table {\n" +
            "            width: 100%%;\n" +
            "            border-collapse: collapse;\n" +
            "        }\n" +
            "        .metadata td {\n" +
            "            padding: 8px 0;\n" +
            "            border-bottom: 1px solid #dee2e6;\n" +
            "        }\n" +
            "        .metadata td:first-child {\n" +
            "            font-weight: bold;\n" +
            "            width: 150px;\n" +
            "        }\n" +
            "        .json-container {\n" +
            "            background-color: #f8f8f8;\n" +
            "            border: 1px solid #ddd;\n" +
            "            border-radius: 5px;\n" +
            "            overflow: auto;\n" +
            "        }\n" +
            "        pre {\n" +
            "            margin: 0;\n" +
            "            padding: 20px;\n" +
            "            white-space: pre-wrap;\n" +
            "            word-wrap: break-word;\n" +
            "        }\n" +
            "        .footer {\n" +
            "            margin-top: 30px;\n" +
            "            text-align: center;\n" +
            "            color: #6c757d;\n" +
            "            font-size: 0.9em;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <h1>Accessibility Scan Results</h1>\n" +
            "            <p>Deque axe DevTools for Mobile - %s Platform</p>\n" +
            "        </div>\n" +
            "\n" +
            "        <div class=\"metadata\">\n" +
            "            <table>\n" +
            "                <tr>\n" +
            "                    <td>Platform:</td>\n" +
            "                    <td>%s</td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td>Scan Name:</td>\n" +
            "                    <td>%s</td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td>Timestamp:</td>\n" +
            "                    <td>%s</td>\n" +
            "                </tr>\n" +
            "                <tr>\n" +
            "                    <td>Report File:</td>\n" +
            "                    <td>%s.json</td>\n" +
            "                </tr>\n" +
            "            </table>\n" +
            "        </div>\n" +
            "\n" +
            "        <h2>Scan Results (JSON)</h2>\n" +
            "        <div class=\"json-container\">\n" +
            "            <pre>%s</pre>\n" +
            "        </div>\n" +
            "\n" +
            "        <div class=\"footer\">\n" +
            "            <p>Generated by Appium with Deque axe DevTools for Mobile integration</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>",
            platform.toUpperCase(),
            platform.toUpperCase(),
            platform.toUpperCase(),
            scanName,
            timestamp,
            baseFileName,
            jsonContent
        );
    }
}