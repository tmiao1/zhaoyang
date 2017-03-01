package com.datacleaning.message;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {
	private static final int SEND_NUMBER = 1;
	static String tcpnumber = "tcp://192.168.8.55:61616";
	static String queueName = "allFileList";
	// Connection ��JMS �ͻ��˵�JMS Provider ������
	Connection connection = null;
	// Session�� һ�����ͻ������Ϣ���߳�
	Session linkSession;
	// MessageProducer����Ϣ������
	MessageProducer msgProducer;

	public static void main(String[] args) {
		Sender fileSender = new Sender();
		String allFileQueueName = "allFileList";
		fileSender.connection(allFileQueueName);
		fileSender.sendMessage("miao");
	}

	public static void sendMessage(Session session, MessageProducer producer,
			String task) throws Exception {
		for (int i = 1; i <= SEND_NUMBER; i++) {
			TextMessage message = session.createTextMessage(task);
			// ������Ϣ��Ŀ�ĵط�
			// System.out.println("������Ϣ��" + "ActiveMq ���͵���Ϣ" + i);
			producer.send(message);
		}
	}

	public static void start(String Path) {
		File fileList = new File(Path);
		String[] rem = fileList.list();
		// ConnectionFactory �����ӹ�����JMS ������������
		ConnectionFactory connectionFactory;
		// Connection ��JMS �ͻ��˵�JMS Provider ������
		Connection connection = null;
		// Session�� һ�����ͻ������Ϣ���߳�
		Session session;
		// Destination ����Ϣ��Ŀ�ĵ�;��Ϣ���͸�˭.
		Destination destination;
		// MessageProducer����Ϣ������
		MessageProducer producer;
		// TextMessage message;
		// ����ConnectionFactoryʵ�����󣬴˴�����ActiveMq��ʵ��jar
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, tcpnumber);
		try {
			// ����ӹ����õ����Ӷ���
			connection = connectionFactory.createConnection();
			// ����
			connection.start();
			// ��ȡ��������
			session = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);
			// ��ȡsessionע�����ֵxingbo.xu-queue��һ����������queue��������ActiveMq��console����
			destination = session.createQueue("Miao");
			// �õ���Ϣ�����ߡ������ߡ�
			producer = session.createProducer(destination);
			// ���ò��־û����˴�ѧϰ��ʵ�ʸ�����Ŀ����
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// ������Ϣ���˴�д������Ŀ���ǲ��������߷�����ȡ

			// for(int i=0;i<rem.length;i++){
			sendMessage(session, producer, "Test");
			// }
			session.commit();
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

	public void connection(String queueName) {
		// ConnectionFactory �����ӹ�����JMS ������������
		ConnectionFactory connectionFactory;
		Destination destination;
		connectionFactory = new ActiveMQConnectionFactory(
		// ActiveMQConnection.DEFAULT_USER,
				"room704",
				// ActiveMQConnection.DEFAULT_PASSWORD,
				"pact518xiyangyang", "tcp://192.168.8.55:61616");
		try {
			// ����ӹ����õ����Ӷ���
			connection = connectionFactory.createConnection();
			// ����
			connection.start();
			// ��ȡ��������
			linkSession = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);
			// ��ȡsessionע�����ֵxingbo.xu-queue��һ����������queue��������ActiveMq��console����
			destination = linkSession.createQueue(queueName);
			// �õ���Ϣ�����ߡ������ߡ�
			msgProducer = linkSession.createProducer(destination);
			// ���ò��־û����˴�ѧϰ��ʵ�ʸ�����Ŀ����
			msgProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// ������Ϣ���˴�д������Ŀ���ǲ��������߷�����ȡ
			System.out.println("connect ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String info){//info��ʾҪ���ݵ���Ϣ
		TextMessage message;
		try {
			message = linkSession.createTextMessage(info);
		
		// ������Ϣ��Ŀ�ĵط�
		// System.out.println("������Ϣ��" + "ActiveMq ���͵���Ϣ" + i);
		msgProducer.send(message);
		linkSession.commit();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage(ArrayList<String> infoes){
		try {
			for(String info : infoes){
				TextMessage message;
				message = linkSession.createTextMessage(info);
				msgProducer.send(message);
			}
			linkSession.commit();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (null != connection){
				try {
					connection.close();
				} catch (JMSException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}