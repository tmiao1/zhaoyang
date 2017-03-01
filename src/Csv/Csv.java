package Csv;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

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

import com.datacleaning.message.Sender;

import Readin.Readin;
public class Csv {
		static int test_flag = 1;
		static String rootpath="E:\\output4\\";
    	public static void main(String args[]) throws IOException {
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
                destination = session.createQueue("csv");
                consumer = session.createConsumer(destination);
                while (true) {
                    //设置接收者接收消息的时间，为了便于测试，这里谁定为100s
                    TextMessage message = (TextMessage) consumer.receive(3000);
                    Thread.sleep(3000);
                    if (null != message) {
                    	String s = message.getText();//收到的消息字符串
                        System.out.println("1收到消息" + s);
                        File file=new File(s);
                        String filename=file.getName();
                        String name=filename.substring(0, filename.lastIndexOf("."));
                        readCsv(s,name);
                        fileSender.connection("csv");
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
    	public static void readCsv(String path,String name) throws IOException{
        Readin read = new Readin();
        String encoding=null;
        File inFile = new File(path); // 读取的CSV文件
        //File outFile = new File("C://out.csv");//写出的CSV文件
        String inString = "";
        //String tmpString = "";
        try {
              BufferedReader reader = new BufferedReader(new FileReader(inFile));
	          File file = new File(path);
	          InputStream in= new java.io.FileInputStream(file);
	          byte[] b = new byte[3];
	          in.read(b);
	          in.close();
	          if (b[0] == -17 && b[1] == -69 && b[2] == -65){
	          if(test_flag==1)
	        	  System.out.println(file.getName() + "：编码为UTF-8");
	          encoding="UTF-8";
	          }
	          else
	        	  encoding="GBK"; 
            //BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
	          if(test_flag==1)
	        	  System.out.println(encoding);
            int cnt = 0;
            while((inString = reader.readLine())!= null){
               String newStr = new String(inString.getBytes(), encoding);
               if(test_flag==1)
            	   System.out.println(newStr);
               char[] strChar=inString.toCharArray();
               String result=""; 
               for(int i=0;i<strChar.length;i++){ 
            	   result +=Integer.toHexString(strChar[i])+ " "; 
               } 
               if(test_flag==1)
            	   System.out.println(result);
               read.writeStrToFile(newStr+"\\s",rootpath+name+".txt",0);
               read.writeStrToFile(newStr,rootpath+name+".txt",1); 
               //writer.write(inString);
               //writer.newLine();
               //cnt++;
            }
            reader.close();
            //writer.close();
        } catch (FileNotFoundException ex) {
            System.out.println("没找到文件！");
        } catch (IOException ex) {
            System.out.println("读写文件出错！");
        }
    }
    } 
