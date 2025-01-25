package Taavi.Lond;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelReader {
    private static final Logger logger = LoggerFactory.getLogger(ExcelReader.class);

    public List<DataRow> readExcelFile(String filePath) throws IOException {
        List<DataRow> dataRows = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            logger.info("Opening Excel file: {}", filePath);

            Sheet sheet = workbook.getSheetAt(0); // First sheet
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

//                int Koht = (int) row.getCell(0).getNumericCellValue();
//                String Rahvus = row.getCell(1).getStringCellValue();
//                String Klubi = row.getCell(2).getStringCellValue();
//                String Nimi = row.getCell(3).getStringCellValue();
//                int Punkte = (int) row.getCell(42).getNumericCellValue(); // AQ column

                // Add null checks for each cell before accessing its value
                int Koht = getNumericCellValue(row, 0);
                String Rahvus = getStringCellValue(row, 1);
                String Klubi = getStringCellValue(row, 2);
                String Nimi = getStringCellValue(row, 3);
                int Punkte = getNumericCellValue(row, 42); // AQ column

                DataRow dataRow = new DataRow(Koht, Rahvus, Klubi, Nimi, Punkte);
                dataRows.add(dataRow);
            }
            logger.info("Successfully read {} rows from Excel file.", dataRows.size());
        } catch (Exception e) {
            logger.error("Error reading Excel file", e);
            throw e;
        }
        return dataRows;
    }

    // Helper method to safely get numeric cell value
    private int getNumericCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
            return (int) cell.getNumericCellValue();
        }
        return 0; // Default value if the cell is null or not numeric
    }

    // Helper method to safely get string cell value
    private String getStringCellValue(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell != null && cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        }
        return ""; // Default value if the cell is null or not a string
    }

}
