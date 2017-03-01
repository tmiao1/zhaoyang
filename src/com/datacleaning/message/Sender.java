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
	// Connection ：JMS 客户端到JMS Provider 的连接
	Connection connection = null;
	// Session： 一个发送或接收消息的线程
	Session linkSession;
	// MessageProducer：消息发送者
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
			// 发送消息到目的地方
			// System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
			producer.send(message);
		}
	}

	public static void start(String Path) {
		File fileList = new File(Path);
		String[] rem = fileList.list();
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory;
		// Connection ：JMS 客户端到JMS Provider 的连接
		Connection connection = null;
		// Session： 一个发送或接收消息的线程
		Session session;
		// Destination ：消息的目的地;消息发送给谁.
		Destination destination;
		// MessageProducer：消息发送者
		MessageProducer producer;
		// TextMessage message;
		// 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, tcpnumber);
		try {
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			// 获取操作连接
			session = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);
			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
			destination = session.createQueue("Miao");
			// 得到消息生成者【发送者】
			producer = session.createProducer(destination);
			// 设置不持久化，此处学习，实际根据项目决定
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// 构造消息，此处写死，项目就是参数，或者方法获取

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
		// ConnectionFactory ：连接工厂，JMS 用它创建连接
		ConnectionFactory connectionFactory;
		Destination destination;
		connectionFactory = new ActiveMQConnectionFactory(
		// ActiveMQConnection.DEFAULT_USER,
				"room704",
				// ActiveMQConnection.DEFAULT_PASSWORD,
				"pact518xiyangyang", "tcp://192.168.8.55:61616");
		try {
			// 构造从工厂得到连接对象
			connection = connectionFactory.createConnection();
			// 启动
			connection.start();
			// 获取操作连接
			linkSession = connection.createSession(Boolean.TRUE,
					Session.AUTO_ACKNOWLEDGE);
			// 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
			destination = linkSession.createQueue(queueName);
			// 得到消息生成者【发送者】
			msgProducer = linkSession.createProducer(destination);
			// 设置不持久化，此处学习，实际根据项目决定
			msgProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			// 构造消息，此处写死，项目就是参数，或者方法获取
			System.out.println("connect ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String info){//info表示要传递的消息
		TextMessage message;
		try {
			message = linkSession.createTextMessage(info);
		
		// 发送消息到目的地方
		// System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
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