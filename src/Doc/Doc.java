package Doc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import Readin.Readin;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.FieldsDocumentPart;
import org.apache.poi.hwpf.usermodel.Fields;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;

import java.io.File;   
import java.io.FileInputStream;   
import java.io.InputStream;   
  
import java.lang.reflect.Field;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.poi.POIXMLDocument;   
import org.apache.poi.POIXMLTextExtractor;   
import org.apache.poi.hwpf.extractor.WordExtractor;   
import org.apache.poi.openxml4j.opc.OPCPackage;   
import org.apache.poi.xwpf.extractor.XWPFWordExtractor; 
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.datacleaning.message.*;
public class Doc {
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
            destination = session.createQueue("Doc");
            consumer = session.createConsumer(destination);
            while (true) {
                //设置接收者接收消息的时间，为了便于测试，这里谁定为100s
                TextMessage message = (TextMessage) consumer.receive(3000);
                Thread.sleep(3000);
                if (null != message) {
                	String s = message.getText();
                    System.out.println("1收到消息" + s);
                    File file=new File(s);
                    String filename=file.getName();
                    String name=filename.substring(0, filename.lastIndexOf("."));
                    String indexname=filename.substring(filename.lastIndexOf(".")+1,filename.length());
                    if(indexname =="doc")
                    	readDoc(s,name);
                    else if(indexname =="docx")
                    	readWORD2007(s,name);
                    fileSender.connection("Txt");
                    fileSender.sendMessage("E:\\output3\\"+name+".txt");
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

	 public static void readDoc(String filename,String name){
		 Readin reader = new Readin();
		 //public String s;
		 
	  try{
	      FileInputStream in = new FileInputStream(filename);//载入文档
	      POIFSFileSystem pfs = new POIFSFileSystem(in);   
	      HWPFDocument hwpf = new HWPFDocument(pfs);   
	      Range range = hwpf.getRange();//得到文档的读取范围
	      TableIterator it = new TableIterator(range);
	     //迭代文档中的表格
	      
	      InputStream is = new FileInputStream(filename);
	      WordExtractor ex = new WordExtractor(is);
	      String text2003 = ex.getText();
	      reader.writeStrToFile(text2003,"E:\\output3\\"+name+".txt",0);
	      System.out.println("E:\\output3\\"+name+".txt");
	      System.out.println(text2003);
	      while (it.hasNext()) {   
	          Table tb = (Table) it.next();   
	          //迭代行，默认从0开始
	          for (int i = 0; i < tb.numRows(); i++) {   
	              TableRow tr = tb.getRow(i);   
	              //迭代列，默认从0开始
	              for (int j = 0; j < tr.numCells(); j++) {   
	                  TableCell td = tr.getCell(j);//取得单元格
	                  //取得单元格的内容
	                  //System.out.println(tr.numCells());
	                  for(int k=0;k<td.numParagraphs();k++){   
	                      Paragraph para =td.getParagraph(k);   
	                      String s = para.text();
	                      reader.writeStrToFile(s,"E:\\output3\\"+name+".txt",0);
	                      //System.out.println("1");
	                  }
	                  
					
	                 // System.out.println("1");
	                  //reader.writeStrToFile("","E:\\111.txt",1);
	                  //end for    
	              }   //end for
	              reader.writeStrToFile("","E:\\output3\\"+name+".txt",1);
	              //System.out.println("1");
	          }   //end for
	      } //end while
	  }catch(Exception e){
	   e.printStackTrace();
	  }
	 }//end method
	 public static void readWORD2007(String file,String name) throws Exception {
		 Readin reader = new Readin();
		 String s=new XWPFWordExtractor(POIXMLDocument.openPackage(file)).getText();
		 System.out.println(s);
	     reader.writeStrToFile(s,"E:\\output3\\"+name+".txt",0); 
	}
}
