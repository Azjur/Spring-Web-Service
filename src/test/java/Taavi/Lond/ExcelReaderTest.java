package Taavi.Lond;

import java.util.List;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExcelReaderTest {

    @Test
    void testReadExcelFile() throws Exception {
        ExcelReader reader = new ExcelReader();
        String testFilePath = "C:\\Users\\taavi\\Downloads\\Eesti-edetabel-2024_12_31.xlsx";
        List<DataRow> dataRows = reader.readExcelFile(testFilePath);

        assertNotNull(dataRows);
        assertFalse(dataRows.isEmpty());
        assertEquals("Expected Club", dataRows.get(0).getKlubi()); // Replace with actual values
    }
}
