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
    		// ConnectionFactory �����ӹ�����JMS ������������
            ConnectionFactory connectionFactory;
            // Connection ��JMS �ͻ��˵�JMS Provider ������
            Connection connection = null;
            // Session�� һ�����ͻ������Ϣ���߳�
            Session session;
            // Destination ����Ϣ��Ŀ�ĵ�;��Ϣ���͸�˭.
            Destination destination;
            // �����ߣ���Ϣ������
            MessageConsumer consumer;
            connectionFactory = new ActiveMQConnectionFactory(
                    ActiveMQConnection.DEFAULT_USER,
                    ActiveMQConnection.DEFAULT_PASSWORD,
                    "tcp://192.168.8.55:61616");
            Readin reader = new Readin();
            Sender fileSender = new Sender();

            try {
                // ����ӹ����õ����Ӷ���
                connection = connectionFactory.createConnection();
                // ����
                connection.start();
                // ��ȡ��������
                session = connection.createSession(Boolean.FALSE,
                        Session.AUTO_ACKNOWLEDGE);
                // ��ȡsessionע�����ֵxingbo.xu-queue��һ����������queue��������ActiveMq��console����
                destination = session.createQueue("csv");
                consumer = session.createConsumer(destination);
                while (true) {
                    //���ý����߽�����Ϣ��ʱ�䣬Ϊ�˱��ڲ��ԣ�����˭��Ϊ100s
                    TextMessage message = (TextMessage) consumer.receive(3000);
                    Thread.sleep(3000);
                    if (null != message) {
                    	String s = message.getText();//�յ�����Ϣ�ַ���
                        System.out.println("1�յ���Ϣ" + s);
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
        File inFile = new File(path); // ��ȡ��CSV�ļ�
        //File outFile = new File("C://out.csv");//д����CSV�ļ�
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
	        	  System.out.println(file.getName() + "������ΪUTF-8");
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
            System.out.println("û�ҵ��ļ���");
        } catch (IOException ex) {
            System.out.println("��д�ļ�����");
        }
    }
    } 
