package Db;
import java.io.IOException;
import java.sql.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import com.datacleaning.message.*;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import Readin.Readin;
public class Db {
	//private static final Connection DbCon = null;
	private static Calendar c1;
	static String tcpnumber = "tcp://192.168.8.55:61616";
	static String rootpath="E:\\output\\";
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
                tcpnumber);
        Readin reader = new Readin();
        Sender fileSender = new Sender();
        Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY); 
        int minute = c.get(Calendar.MINUTE); 
        int second = c.get(Calendar.SECOND); 
        try {
            // ����ӹ����õ����Ӷ���
            connection = connectionFactory.createConnection();
            // ����
            connection.start();
            // ��ȡ��������
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
            // ��ȡsessionע�����ֵxingbo.xu-queue��һ����������queue��������ActiveMq��console����
            destination = session.createQueue("Mdb");
            consumer = session.createConsumer(destination);
            while (true) {
                //���ý����߽�����Ϣ��ʱ�䣬Ϊ�˱��ڲ��ԣ�����˭��Ϊ100s
                TextMessage message = (TextMessage) consumer.receive(3000);
                Thread.sleep(3000);
                if (null != message) {
                	String s = message.getText();
                    System.out.println("1�յ���Ϣ" + s);
                    File file=new File(s);
                    String filename=file.getName();
                    String name=filename.substring(0, filename.lastIndexOf("."));
                    readMdb(s,name);
                    fileSender.connection("Txt");
                    fileSender.sendMessage("E:\\output\\"+name+".txt");
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
        c1 = Calendar.getInstance();
        int hour1 = c1.get(Calendar.HOUR_OF_DAY); 
        int minute1 = c1.get(Calendar.MINUTE); 
        int second1 = c1.get(Calendar.SECOND); 
        System.out.print(hour+ " "+minute+" "+second);
        System.out.print(hour1+ " "+minute1+" "+second1);
	}
	public static void readMdb(String path,String name) throws IOException{
		//�洢��ÿ�����ݿⵥԪ��ȡ���ַ�������
		String s=null;
		//����1��������������
		String sDriver="sun.jdbc.odbc.JdbcOdbcDriver";
		try{
			Class.forName(sDriver);
		}
		catch(Exception e){
			System.out.println("�޷�������������");
			return;
		}
		System.out.println("����1�������������򡪡��ɹ���");
		java.sql.Connection dbCon=null;
		Statement stmt=null;
        ResultSet rs = null;  
        String tableName = null;
		String sCon="jdbc:odbc:cad;DBQ="+path;
		try{
			dbCon=DriverManager.getConnection(sCon);
			if(dbCon!=null){
				System.out.println("����2���������ݿ⡪���ɹ���");
			}
			//����3������JDBC��Statement����
			stmt=dbCon.createStatement();
			if(stmt!=null){
				System.out.println("����3������JDBC��Statement���󡪡��ɹ���");
			}
		}
		catch(SQLException e){
			System.out.println("���Ӵ���"+sCon);
			System.out.println(e.getMessage());
			if(dbCon!=null){
				try{
					dbCon.close();
				}
				catch(SQLException e2){}
			}
			return;
		}
		try{//ִ�����ݿ��ѯ�����ؽ��
			 Readin reader = new Readin();
			java.sql.Connection conn = DriverManager.getConnection(sCon);  
            ResultSet tables = conn.getMetaData().getTables(  
            		path, null, null,  
                    new String[] { "TABLE" });  
            // ��ȡ��һ������  
            if (tables.next()) {  
                tableName = tables.getString(3);// getXXX can only be used once  
            } else {  
                return;  
            }  
            stmt = (Statement) conn.createStatement();  
            // ��ȡ��һ���������  
            rs = stmt.executeQuery("select * from " + tableName);  
            ResultSetMetaData data = rs.getMetaData();  
            while (rs.next()) {  
                for (int i = 1; i <= data.getColumnCount(); i++) {  
                	s=rs.getString(i);
                    //System.out.print(s + "    "); 
                    if(s==null)
                    reader.writeStrToFile("null"+"\\s",rootpath+name+".txt",0);
                    else 
                    reader.writeStrToFile(s+"\\s",rootpath+name+".txt",0);
                }  
                reader.writeStrToFile("\\s",rootpath+name+".txt",1);
                //System.out.println();  
            }  
			/*
			dbCon=DriverManager.getConnection(sCon);
			DatabaseMetaData meta = dbCon.getMetaData(); 
			String tt, tp;   
			ResultSet rs = meta.getTables(null, null, null, null);   
	      
	        while (rs.next()) {   
	            tt = rs.getString("TABLE_NAME");   
	            tp = rs.getString("TABLE_TYPE");   
	            System.out.println(" ������� " + tt + "   ������� " + tp);   
	        }   
	        // �ر�����   
	        dbCon.close();    
			 /*rs=stmt.executeQuery(sSQL);
			
			ResultSetMetaData rsm = rs.getMetaData();
	        int size = rsm.getColumnCount();
	        while (rs.next()) {
	            for (int i = 1; i <= size; i++) {
	                if (i > 1) System.out.print(",  ");
	                String columnValue = rs.getString(i);
	                System.out.print(columnValue + " " + rsm.getColumnName(i));
	            }
	            System.out.println("");
	        }
			/*dbCon=DriverManager.getConnection(sCon);
			DatabaseMetaData meta = dbCon.getMetaData();  
			   ResultSet rs = meta.getTables(null, null, null,  
			     new String[] { "TABLE" });  
			   while (rs.next()) {  
			     System.out.println("������" + rs.getString(3));  
			     System.out.println("�������û�����" + rs.getString(2));  
			     System.out.println("------------------------------");  
			   }  
			  dbCon.close();  
	        String sql4="SELECT   * FROM   acd WHERE   Flags=0   AND   Type=1";
	        rs=stmt.executeQuery(sql4); 
	        while(rs.next()){  
	            System.out.println(rs.getString("name"));  
	                
	          } */ 
		}
		catch(SQLException e){
			System.out.println(e.getMessage());
		}
            finally{
                try{
                    //�رղ���3��������statement����
                    stmt.close();
                    System.out.println("�ر�statement����");
                }
                catch(SQLException e){}
                try{
                    //�ر����ݿ�����
                    dbCon.close();
                    System.out.println("�ر����ݿ����Ӷ���");
                }
                catch(SQLException e){}
           }
      }
}