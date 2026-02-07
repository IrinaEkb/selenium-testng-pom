package utils;

import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;

public class ExcelUtil {
    public static String getCellData(String filePath, String sheetName, int row, int col) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            Workbook workbook = WorkbookFactory.create(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            return sheet.getRow(row).getCell(col).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
