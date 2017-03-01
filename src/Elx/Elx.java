package Elx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * xls������
 * 
 * @author hjn
 * 
 */
public class Elx {
	
  public static void read(String filePath) throws IOException {
    String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
    InputStream stream = new FileInputStream(filePath);
    Workbook wb = null;
    if (fileType.equals("xls")) {
      wb = new HSSFWorkbook(stream);
    } else if (fileType.equals("xlsx")) {
      wb = new XSSFWorkbook(stream);
    } else {
      System.out.println("�������excel��ʽ����ȷ");
    }
    Sheet sheet1 = wb.getSheetAt(0);
    for (Row row : sheet1) {
      for (Cell cell : row) {
        System.out.print(cell.getStringCellValue() + "  ");
      }
      System.out.println();
    }
  }

  public static boolean write(String outPath) throws Exception {
    String fileType = outPath.substring(outPath.lastIndexOf(".") + 1, outPath.length());
    System.out.println(fileType);
    // ���������ĵ�����
    Workbook wb = null;
    if (fileType.equals("xls")) {
      wb = new HSSFWorkbook();
    } else if (fileType.equals("xlsx")) {
      wb = new XSSFWorkbook();
    } else {
      System.out.println("�����ĵ���ʽ����ȷ��");
      return false;
    }
    // ����sheet����
    Sheet sheet1 = (Sheet) wb.createSheet("sheet1");
    // ѭ��д��������
    for (int i = 0; i < 5; i++) {
      Row row = (Row) sheet1.createRow(i);
      // ѭ��д��������
      for (int j = 0; j < 8; j++) {
        Cell cell = row.createCell(j);
        cell.setCellValue("����" + j);
      }
    }
    // �����ļ���
    OutputStream stream = new FileOutputStream(outPath);
    // д������
    wb.write(stream);
    // �ر��ļ���
    stream.close();
    return true;
  }


  public static void main(String[] args) {
    try {
      write("D:" + File.separator + "out.xlsx");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      read("E:111.xlsx");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
