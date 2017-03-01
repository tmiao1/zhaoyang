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
        System.out.println(ActiveMQConnection.DEFAULT_USER);
        connectionFactory = new ActiveMQConnectionFactory(
                //ActiveMQConnection.DEFAULT_USER,
        		username,
                ActiveMQConnection.DEFAULT_PASSWORD,
                tcpnumber);
        try {
            // ����ӹ����õ����Ӷ���
            connection = connectionFactory.createConnection();
            // ����
            connection.start();
            // ��ȡ��������
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
            // ��ȡsessionע�����ֵxingbo.xu-queue��һ����������queue��������ActiveMq��console����
            destination = session.createQueue("Miao");
            consumer = session.createConsumer(destination);
            while (true) {
                //���ý����߽�����Ϣ��ʱ�䣬Ϊ�˱��ڲ��ԣ�����˭��Ϊ100s
                TextMessage message = (TextMessage) consumer.receive(3000);
                //Thread.sleep(3000);
                if (null != message) {
                	String s = message.getText();
                    System.out.println("1�յ���Ϣ" + s);
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
        System.out.println(ActiveMQConnection.DEFAULT_USER);
        connectionFactory = new ActiveMQConnectionFactory(
                //ActiveMQConnection.DEFAULT_USER,
        		"datacleaning1",
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://192.168.8.55:61616");
        try {
            // ����ӹ����õ����Ӷ���
            connection = connectionFactory.createConnection();
            // ����
            connection.start();
            // ��ȡ��������
            session = connection.createSession(Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);
            // ��ȡsessionע�����ֵxingbo.xu-queue��һ����������queue��������ActiveMq��console����
            destination = session.createQueue("Miao");
            consumer = session.createConsumer(destination);
            while (true) {
                //���ý����߽�����Ϣ��ʱ�䣬Ϊ�˱��ڲ��ԣ�����˭��Ϊ100s
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
