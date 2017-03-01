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
                tcpnumber);
        Readin reader = new Readin();
        Sender fileSender = new Sender();
        Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY); 
        int minute = c.get(Calendar.MINUTE); 
        int second = c.get(Calendar.SECOND); 
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createQueue("Mdb");
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
		//存储从每个数据库单元读取的字符串数据
		String s=null;
		//步骤1：加载驱动程序
		String sDriver="sun.jdbc.odbc.JdbcOdbcDriver";
		try{
			Class.forName(sDriver);
		}
		catch(Exception e){
			System.out.println("无法加载驱动程序");
			return;
		}
		System.out.println("步骤1：加载驱动程序——成功！");
		java.sql.Connection dbCon=null;
		Statement stmt=null;
        ResultSet rs = null;  
        String tableName = null;
		String sCon="jdbc:odbc:cad;DBQ="+path;
		try{
			dbCon=DriverManager.getConnection(sCon);
			if(dbCon!=null){
				System.out.println("步骤2：连接数据库——成功！");
			}
			//步骤3：建立JDBC的Statement对象
			stmt=dbCon.createStatement();
			if(stmt!=null){
				System.out.println("步骤3：建立JDBC的Statement对象——成功！");
			}
		}
		catch(SQLException e){
			System.out.println("连接错误："+sCon);
			System.out.println(e.getMessage());
			if(dbCon!=null){
				try{
					dbCon.close();
				}
				catch(SQLException e2){}
			}
			return;
		}
		try{//执行数据库查询，返回结果
			 Readin reader = new Readin();
			java.sql.Connection conn = DriverManager.getConnection(sCon);  
            ResultSet tables = conn.getMetaData().getTables(  
            		path, null, null,  
                    new String[] { "TABLE" });  
            // 获取第一个表名  
            if (tables.next()) {  
                tableName = tables.getString(3);// getXXX can only be used once  
            } else {  
                return;  
            }  
            stmt = (Statement) conn.createStatement();  
            // 读取第一个表的内容  
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
	            System.out.println(" 表的名称 " + tt + "   表的类型 " + tp);   
	        }   
	        // 关闭连接   
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
			     System.out.println("表名：" + rs.getString(3));  
			     System.out.println("表所属用户名：" + rs.getString(2));  
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
                    //关闭步骤3所开启的statement对象
                    stmt.close();
                    System.out.println("关闭statement对象");
                }
                catch(SQLException e){}
                try{
                    //关闭数据库连接
                    dbCon.close();
                    System.out.println("关闭数据库连接对象");
                }
                catch(SQLException e){}
           }
      }
}