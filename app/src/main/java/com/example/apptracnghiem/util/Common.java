package com.example.apptracnghiem.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.apptracnghiem.database.Database;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.library.worksheet.cellstyles.WorkSheet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Common {
    public static void clearSession(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void exportToExcel( Context context) {
         String fileName = "Question_data";
        Cursor cursor = new Database(context).getAllQuestionData();
        if (cursor != null && cursor.getCount() > 0) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(fileName);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(cursor.getColumnName(i));
            }
                Log.d("headerRow" , String.valueOf(headerRow));
            int rowIndex = 1;
            while (cursor.moveToNext()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    Cell cell = row.createCell(i);
                    switch (cursor.getType(i)) {
                        case Cursor.FIELD_TYPE_INTEGER:
                            cell.setCellValue(cursor.getInt(i));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            cell.setCellValue(cursor.getFloat(i));
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            cell.setCellValue(cursor.getString(i));
                            break;
                        case Cursor.FIELD_TYPE_BLOB:
                            cell.setCellValue(new String(cursor.getBlob(i)));
                            break;
                        default:
                            cell.setCellValue(cursor.getString(i));
                            break;
                    }
                }
            }
            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File filePath = new File(directory, fileName + ".xlsx");
            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                workbook.write(outputStream);
                Toast.makeText(context, "Exported to " + filePath, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            cursor.close();
        }
    }
}
