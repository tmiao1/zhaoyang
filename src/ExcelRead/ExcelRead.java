package ExcelRead;
import java.sql.*;
import java.io.*;

import Readin.Readin;
import Elx.Elx;

import java.util.ArrayList;
import java.util.Iterator;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.xmlbeans.*;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.datacleaning.message.*;
public class ExcelRead {
		 static int test_flag = 1;
		 static String rootpath="E:\\output2\\";
		 public static ArrayList<String> fileName=new  ArrayList<String>();
		    public static void main(String[] args) throws IOException {
		        // ConnectionFactory ：连接工厂，JMS 用它创建连接
		        ConnectionFactory connectionFactory;
		        // Connection ：JMS 客户端到JMS Provider 的连接
		        Connection connection = null;
		        // Session： 一个发送或接收消息的线程
		        Session session;
		        // Destination ：消息的目的地;消息发送给谁.
		        Destination destination;
		        // 消费者，消息接收者
		        MessageConsumer consumer;
		        connectionFactory = new ActiveMQConnectionFactory(
		                ActiveMQConnection.DEFAULT_USER,
		                ActiveMQConnection.DEFAULT_PASSWORD,
		                "tcp://192.168.8.55:61616");
		        Readin reader = new Readin();
		        Sender fileSender = new Sender();
		        try {
		            // 构造从工厂得到连接对象
		            connection = connectionFactory.createConnection();
		            // 启动
		            connection.start();
		            // 获取操作连接
		            session = connection.createSession(Boolean.FALSE,
		                    Session.AUTO_ACKNOWLEDGE);
		            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
		            destination = session.createQueue("Xls");
		            consumer = session.createConsumer(destination);
		            while (true) {
		                //设置接收者接收消息的时间，为了便于测试，这里谁定为100s
		                TextMessage message = (TextMessage) consumer.receive(3000);
		                Thread.sleep(3000);
		                if (null != message) {
		                	String s = message.getText();
		                    if(test_flag == 1)
		                    	System.out.println("1收到消息" + s);
		                    File file=new File(s);
		                    String filename=file.getName();
		                    String name=filename.substring(0, filename.lastIndexOf("."));
		                    readXml(s,name);
		                    fileSender.connection("Txt");
		                    fileSender.sendMessage(rootpath+name+".txt");
		                }
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            try {
		                if (null != connection)
		                    connection.close();
		            } catch (Throwable ignore) {
		            }
		        }
 	}
	public static void readXml(String fileName,String name){
		String ret="";
		Readin reader = new Readin();
		boolean isE2007 = false;	//判断是否是excel2007格式
		if(fileName.endsWith("xlsx"))
			isE2007 = true;
		try {
			InputStream input = new FileInputStream(fileName);	//建立输入流
			
			Workbook workbook  = null;
			//根据文件格式(2003或者2007)来初始化
			if(isE2007){
				//Elx in =new Elx();
				//in.read(fileName);
				workbook = new XSSFWorkbook(input);
			}
			else
				workbook = new HSSFWorkbook(input);

			int sheets = workbook.getNumberOfSheets(); 
			// 获得表数 
		for(int num=0;num<sheets;num++){
			Sheet sheet = workbook.getSheetAt(num);		//获得第一个表单
			Iterator<Row> rows = sheet.rowIterator();	//获得第一个表单的迭代器
			while (rows.hasNext()) {
				Row row = rows.next();	//获得行数据
				if(test_flag == 1)
					System.out.println("Row #" + row.getRowNum());	//获得行号从0开始
				Iterator<Cell> cells = row.cellIterator();	//获得第一行的迭代器
				while (cells.hasNext()) {
					Cell cell = cells.next();
					if(test_flag == 1)
						System.out.println("Cell #" + cell.getColumnIndex());
					switch (cell.getCellType()) {	//根据cell中的类型来输出数据
					/*case HSSFCell.CELL_TYPE_NUMERIC:
						System.out.println(cell.getNumericCellValue());
						
						break;
					case HSSFCell.CELL_TYPE_STRING:
						System.out.println(cell.getStringCellValue());
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN:
						System.out.println(cell.getBooleanCellValue());
						break;
					case HSSFCell.CELL_TYPE_FORMULA:
						System.out.println(cell.getCellFormula());
						break;
					default:
						System.out.println("unsuported sell type");
					break;
					cell.setCellType(Cell.CELL_TYPE_STRING);
					reader.writeStrToFile(cellValue,"E:\\data\\12345.txt",0);*/
			        case Cell.CELL_TYPE_BLANK:   
			            ret = "";   
			            reader.writeStrToFile(ret+" ",rootpath+name+".txt",0);
			            if(test_flag == 1)
			            	System.out.println(ret);
			            break;   
			        case Cell.CELL_TYPE_BOOLEAN:   
			            ret = String.valueOf(cell.getBooleanCellValue()); 
			            reader.writeStrToFile(ret+" ",rootpath+name+".txt",0);
			            if(test_flag == 1)
			            	System.out.println(ret);
			            break;   
			        case Cell.CELL_TYPE_ERROR:   
			            ret = null;   
			            reader.writeStrToFile(ret+" ",rootpath+name+".txt",0);
			            if(test_flag == 1)
			            	System.out.println(ret);
			            break;   
			        case Cell.CELL_TYPE_FORMULA:   
			            workbook = cell.getSheet().getWorkbook();   
			            //CreationHelper crateHelper = workbook.getCreationHelper();   
			            //FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();   
			            ret = cell.getCellFormula(); 
			            reader.writeStrToFile(ret+" ",rootpath+name+".txt",0);
			            if(test_flag == 1)
			            	System.out.println(ret);
			            break;   
			        case Cell.CELL_TYPE_NUMERIC:   
			           
			              ret = NumberToTextConverter.toText(cell.getNumericCellValue());   
			              reader.writeStrToFile(ret+" ",rootpath+name+".txt",0);
			              if(test_flag == 1)
			            	  System.out.println(ret);
			            break;   
			        case Cell.CELL_TYPE_STRING:   
			            ret = cell.getRichStringCellValue().getString();   
			            reader.writeStrToFile(ret+" ",rootpath+name+".txt",0);
			            if(test_flag == 1)
			            	System.out.println(ret);
			            break;   
			        default:   
			            ret = null;
			            if(test_flag == 1)
			            	System.out.println("error");
			            break;
			       
					}
					
				}reader.writeStrToFile("",rootpath+name+".txt",1); 
			}
		  }
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}

