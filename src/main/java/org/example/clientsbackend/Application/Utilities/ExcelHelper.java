package org.example.clientsbackend.Application.Utilities;

import org.apache.poi.ss.usermodel.*;
import java.util.List;

public class ExcelHelper {

    public static Sheet createTableSheet(Workbook workbook, List<List<String>> table, String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);

        for(int i = 0; i < table.size(); i++) {
            Row row = sheet.createRow(i);
            for(int j = 0; j < table.get(i).size(); j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(table.get(i).get(j));
            }
        }
        return sheet;
    }

}
