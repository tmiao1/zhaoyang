package com.datacleaning.message;

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

import com.datacleaning.message.Sender;
public class Receiver {
	static String username = "datacleaning1";
	static String tcpnumber = "tcp://192.168.8.55:61616";
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
        System.out.println(ActiveMQConnection.DEFAULT_USER);
        connectionFactory = new ActiveMQConnectionFactory(
                //ActiveMQConnection.DEFAULT_USER,
        		username,
                ActiveMQConnection.DEFAULT_PASSWORD,
                tcpnumber);
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createQueue("Miao");
            consumer = session.createConsumer(destination);
            while (true) {
                //设置接收者接收消息的时间，为了便于测试，这里谁定为100s
                TextMessage message = (TextMessage) consumer.receive(3000);
                //Thread.sleep(3000);
                if (null != message) {
                	String s = message.getText();
                    System.out.println("1收到消息" + s);
                } else {
                    break;
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
    
    public void connection(String queueName){
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
        System.out.println(ActiveMQConnection.DEFAULT_USER);
        connectionFactory = new ActiveMQConnectionFactory(
                //ActiveMQConnection.DEFAULT_USER,
        		"datacleaning1",
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://192.168.8.55:61616");
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createQueue("Miao");
            consumer = session.createConsumer(destination);
            while (true) {
                //设置接收者接收消息的时间，为了便于测试，这里谁定为100s
                TextMessage message = (TextMessage) consumer.receive(3000);
                //Thread.sleep(3000);
                if (null != message) {
                	String s = message.getText();	
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
    public static void getFileName(String path)
    {
        File file = new File(path);
        String[] rem =file.list();
        //System.out.println("-----"+rem[0]);
        for(int i=0;i<rem.length;i++){
        	fileName.add(path + "\\" +rem[i]);
        }
    }
}
