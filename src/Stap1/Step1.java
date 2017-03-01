package Stap1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.regex.*;
import java.lang.*;
import java.util.ArrayList;

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
public class Step1 {
	//ʶ��ɹ���־λ
	public static boolean[] check = new boolean[10000];
	//�洢�������ݡ����͡�λ���Լ����ȵĶ�ά����
	public static ArrayList<ArrayList<data>> success_data_list=new  ArrayList<ArrayList<data>>();
	//�洢�����������͵Ķ�ά����
	public static ArrayList<ArrayList<String>> success_content_list=new  ArrayList<ArrayList<String>>();
	//���ʶ��ʧ�ܵ�����
	public static ArrayList<ArrayList<data>> lose_data_list=new  ArrayList<ArrayList<data>>();
	//public static String link=new String();
	//���ʧ�����ݵı�����Ԫ
	private static data find;
	
	public static void main(String[] args) {
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
            destination = session.createQueue("Txt");
            consumer = session.createConsumer(destination);
            while (true) {
                //���ý����߽�����Ϣ��ʱ�䣬Ϊ�˱��ڲ��ԣ�����˭��Ϊ100s
                TextMessage message = (TextMessage) consumer.receive(3000);
                //Thread.sleep(3000);
                if (null != message) {
                	String s = message.getText();
                	File file=new File(s);
                    String filename=file.getName();
                    String name=filename.substring(0, filename.lastIndexOf("."));
                    System.out.println("1�յ���Ϣ" + s);
                    readTxt(s,name); 
                }
                
                success_content_list.clear();
        		success_data_list.clear();
        		success_data_list=new  ArrayList<ArrayList<data>>();
        		success_content_list=new  ArrayList<ArrayList<String>>();
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
	

	/*************************************************   
	Function:       readTxt(String,String)
	Description:    ����Ϣ����Txt�е�txt�ı����������н���  
	Calls:          readTxtFile(String)
	Called By:      main()
	Table Accessed: 
	Table Updated: 
	Input:         
   			 @param filePath
                 	���д�����ļ�����·��
     		 @param name
               		ԭ�ļ�����(������׺��)
	Output:         
			 @param Intermediate_variable_data0
			    	�洢ʶ��ɹ�������Ϣ�ĵ�λ����
			 @param Intermediate_variable_data1
			    	�洢ʶ��ɹ�������Ϣ�ĵ�λ���������ڱ���˳�����   	
	Return:         void
	others:  
			 @throws Exception
	*************************************************/
	public static void readTxt(String filePath,String name) throws IOException{
		//д�ļ�����ʵ�廯
		Readin reader = new Readin();
		//��¼ÿ�����Ը���,��ͬ��λ�ö�Ӧ�ض�������
		int feature_total_num[]=new int[20];
		//��־λ��Ŀǰÿ��ͬ��Ԫ�ع涨����Ϊ2��ȡֵΪ0-2
		int data_flag[]=new int[20];
		int phone = 0;
		int max = 16;
		String feature = null;
		ArrayList<String> normal = new ArrayList<String>();
		ArrayList<String> page = new ArrayList<String>();
		for (int i = 0; i < 1000; i++){
			check[i] = true;	
		}
		
		for (int i = 0; i < 20; i++){
			data_flag[i] = 0;
		}
		
		readTxtFile(filePath);
		ArrayList<data> temp_data_list =new ArrayList<data>();
		//���ά�������ĵ�λ����
		data Intermediate_variable_data0=new data();
		data Intermediate_variable_data1=new data();
		for(int i=0;i<success_data_list.size();i++){
			temp_data_list = success_data_list.get(i);
			
			for(int j =0 ;j < temp_data_list.size();j++){
				Intermediate_variable_data0 = temp_data_list.get(j);
				System.out.print("success:data "+ (j+1) 
						+ "	����	"+Intermediate_variable_data0.type+" " +Intermediate_variable_data0.content 
						+" "+Intermediate_variable_data0.length+" "+Intermediate_variable_data0.first_position
						+" "+Intermediate_variable_data0.last_position +"	");
		}	
			
			System.out.println();
	}
		
	//����ÿ������λ��	
		for(int i=0;i<success_data_list.size();i++){
			temp_data_list = success_data_list.get(i);
			
			for(int j =0 ;j < temp_data_list.size();j++){
				Intermediate_variable_data0 = temp_data_list.get(j);
				for(int k=j+1;k<temp_data_list.size();k++){
					Intermediate_variable_data0 = temp_data_list.get(j);
					Intermediate_variable_data1 = temp_data_list.get(k);
					if(Intermediate_variable_data1.first_position<Intermediate_variable_data0.first_position){
						exchange(temp_data_list,k,j);
					}
			}
		}	
	}
		
		
		for(int i=0;i<success_data_list.size();i++){
			temp_data_list = success_data_list.get(i);
			
			for (int k = 0; k < 20; k++){
				data_flag[k] = 0;
			}
			
			for(int j =0 ;j < temp_data_list.size();j++){
				Intermediate_variable_data0 = temp_data_list.get(j);
				//�ж�ÿ�����ԣ�ÿ��һ�㲻����������,data_flag[n]�ֱ����ͬ������ÿ�еĸ���
				if(Intermediate_variable_data0.type=="email" && data_flag[0]==0){
					feature_total_num[0]++;
					data_flag[0]++;
				}
				else if(Intermediate_variable_data0.type=="Name" &&  data_flag[1]==0){	
					feature_total_num[1]++;
					data_flag[1]++;
				}
				else if(Intermediate_variable_data0.type=="Number" &&  data_flag[2]==0){	
					feature_total_num[2]++;
					data_flag[2]++;
				}
				else if(Intermediate_variable_data0.type=="Telephone" &&  data_flag[3]==0){	
					feature_total_num[3]++;
					data_flag[3]++;
					if(data_flag[5]>0)
						phone++;
				}
				else if(Intermediate_variable_data0.type=="Address" &&  data_flag[4]==0){	
					feature_total_num[4]++;
					data_flag[4]++;
				}
				else if(Intermediate_variable_data0.type=="Phone" &&  data_flag[5]==0){	
					feature_total_num[5]++;
					data_flag[5]++;
					if(data_flag[3]>0)
						phone++;
				}
				else if(Intermediate_variable_data0.type=="Data" &&  data_flag[6]==0){	
					feature_total_num[6]++;
					data_flag[6]++;
				}
				else if(Intermediate_variable_data0.type=="email" &&  data_flag[0]==1){	
					feature_total_num[7]++;
					data_flag[1]++;
				}
				else if(Intermediate_variable_data0.type=="Name" &&  data_flag[1]==1){	
					feature_total_num[8]++;
					data_flag[1]++;
				}
				else if(Intermediate_variable_data0.type=="Number" &&  data_flag[2]==1){	
					feature_total_num[9]++;
					data_flag[2]++;
				}
				else if(Intermediate_variable_data0.type=="Telephone" &&  data_flag[3]==1){	
					feature_total_num[10]++;
					data_flag[3]++;
				}
				else if(Intermediate_variable_data0.type=="Address" &&  data_flag[4]==1){	
					feature_total_num[11]++;
					data_flag[4]++;
				}
				else if(Intermediate_variable_data0.type=="Phone" &&  data_flag[5]==1){	
					feature_total_num[12]++;
					data_flag[5]++;
				}
				else if(Intermediate_variable_data0.type=="Data" &&  data_flag[6]==1){	
					feature_total_num[13]++;
					data_flag[6]++;
				}
				else if(Intermediate_variable_data0.type=="Bank" &&  data_flag[7]==0){	
					feature_total_num[14]++;
					data_flag[7]++;
				}
				else if(Intermediate_variable_data0.type=="Bank" &&  data_flag[7]==1){	
					feature_total_num[15]++;
					data_flag[7]++;
				}
				
		}	
			
			for(int m=0;m<max;m++){
				data_flag[m]=0;
			}
			
	}
		
		for(int m=0;m<max;m++){
			//����ʶ���������Բ�ȫ��������ԵĶ�ά������
			if(10*feature_total_num[m]/success_data_list.size()>1){
				if( m == 0){
					feature="email";
					page.add(feature);
				}
				else if( m == 1){	
					feature ="Name";
					page.add(feature);
				}
				else if( m == 2){	
					feature ="Number";
					page.add(feature);
				}
				else if( m == 3){	
					feature ="Telephone";
					page.add(feature);
				}
				else if( m == 4){	
					feature ="Address";
					page.add(feature);
				}
				//��ʾ��������г���tel��phoneͬʱ����5�����ϱ�ʾ����phone�������
				else if( m == 5 && phone > 5){	
					feature ="Phone";
					page.add(feature);
				}
				else if( m == 6){	
					feature ="Date";
					page.add(feature);
				}
				else if( m == 7){
					feature="email";
					page.add(feature);
				}
				else if( m == 8){	
					feature ="Name";
					page.add(feature);
				}
				else if( m == 9){	
					feature ="Number";
					page.add(feature);
				}
				else if( m == 10){	
					feature ="Telephone";
					page.add(feature);
				}
				else if( m == 11){	
					feature ="Address";
					page.add(feature);
				}
				else if( m == 12){	
					feature ="Phone";
					page.add(feature);
				}
				else if( m == 13){	
					feature ="Date";
					page.add(feature);
				}
				
		}
			
		}
		success_content_list.add(page);	
		
		for(int i=0;i<success_data_list.size();i++){
			temp_data_list = success_data_list.get(i);
			ArrayList<String> page1 =new ArrayList<String>();
			add(temp_data_list,page1);
			success_content_list.add(page1);
		}
		

		//��ʶ��͵�����Ľ��д���Ը��ļ���������txt��
		for(int i=0;i<success_content_list.size();i++){
			normal=success_content_list.get(i);
			
			for(int j=0;j<normal.size();j++){
				System.out.print(normal.get(j)+"  ");
				reader.writeStrToFile(normal.get(j)+"  ",
						"E:\\test\\"+name+".txt",0);
			}
			
			reader.writeStrToFile("","E:\\test\\"+name+".txt",1);
			System.out.println();
		}
		
		for(int i=0;i<lose_data_list.size();i++){
			temp_data_list = lose_data_list.get(i);
			for(int j=0;j<temp_data_list.size();j++){
				System.out.print(temp_data_list.get(j).content+" ");
				reader.writeStrToFile(temp_data_list.get(j).content+"  ",
						"E:\\test\\"+name+"errorfile"+".txt",0);
			}
			reader.writeStrToFile("","E:\\test\\"+name+"errorfile"+".txt",1);
			
			System.out.println();
		}
		
}
//�������λ�á����ȡ����Ե����ݽṹ
	static	class data {
		String content="";
		int first_position=0;
		int last_position=0;
		int length=0;
		int flag=1;
		String type ="";
	}
	
	
	static	class lose_data {
		String lose_content="";
		int lose_first_position=0;
		int lose_last_position=0;
		int lose_length=0;
		int lose_flag=1;
		String type ="";
		boolean check_number = false;
		boolean check_chinese = false;
		boolean check_english = false;
		boolean check_symbol_X = false;
	}
	/**
     * ͨ��������ʽ��ÿ�����ݽ����жϣ�����ʶ�������ص�txt�ļ���
     * 
     * @param filePath
     *            ������ļ�����·��
     * 
     */
	public static void readTxtFile(String filePath) {
		try {
			int i=0,len =0,first=0,last =0;
			String encoding = null;
			data find = new data();
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // �ж��ļ��Ƿ����
				 BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));    
		          int p = (bin.read() << 8) + bin.read();
		          
		          switch (p) {    
		              case 0xefbb:    
		            	  encoding = "UTF-8";    
		                  break;    
		              case 0xfffe:    
		            	  encoding = "Unicode";    
		                  break;    
		              case 0xfeff:    
		            	  encoding = "UTF-16BE";    
		                  break;    
		              default:    
		            	  encoding = "GBK";    
		          }    
		          
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// ���ǵ������ʽ
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				
				while ((lineTxt = bufferedReader.readLine()) != null) {
					System.out.println(lineTxt);
					for (i = 0; i < lineTxt.length(); i++) {
						check[i] = true;
					}
					
					ArrayList<data> temp_data_list =new ArrayList<data>();
					ArrayList<data> temp_data_list2 =new ArrayList<data>();
					//����ƥ�亯������ƥ�䣬����ʶ���Ľ��д��һλ����
					compareEmail(lineTxt,temp_data_list);
					compareTel(lineTxt,temp_data_list);
					comparePhone(lineTxt,temp_data_list);
					compareAddr(lineTxt,temp_data_list);
					compareName(lineTxt,temp_data_list);
					compareName2(lineTxt,temp_data_list);
					compareDate(lineTxt,temp_data_list);
					compareBank(lineTxt,temp_data_list);
					compareBankName(lineTxt,temp_data_list);
					compareCar(lineTxt,temp_data_list);
		//			comparenumber(lineTxt,temp_data_list2);
					//compareNumber(lineTxt,temp_data_list);
					//compareNumber2(lineTxt,temp_data_list);

					//���ݱ�־λ����δ�ܹ�ʶ�������
					for (i = 0; i < lineTxt.length(); i++){
						if(check[i] == true){
							len++;
							
							if(len==1){
							first = i;
							}
							else if(i==lineTxt.length()){
								find.content = lineTxt.substring(first);
								find.length=last-first;
								find.first_position=first;
								find.last_position=last-1;
								temp_data_list2.add(find);
								len=0;
							}
							
						}
						else if(check[i]== false && len!=0){
							last = i;
							System.out.println("-----"+first+" "+last+" "+ lineTxt.length());
							find.content = lineTxt.substring(first, last);
							find.length=last-first;
							find.first_position=first;
							find.last_position=last-1;
							temp_data_list2.add(find);
							len=0;
						}
						
					}
					lose_data_list.add(temp_data_list2);
					success_data_list.add(temp_data_list);
					for (i = 0; i < lineTxt.length(); i++){
						check[i] = true;
					}
					
					first=0;
					last=0;
				}
				read.close();
			} else {
				System.out.println("�Ҳ���ָ�����ļ�");
			}
		} catch (Exception e) {
			System.out.println("��ȡ�ļ����ݳ���");
			e.printStackTrace();
		}
	}
 
	public static void add(ArrayList<data> temp_data_list,ArrayList<String> page) throws IOException{
		ArrayList<String> normal;  //ȡ����Ž�����ȷ�Ķ�ά����ĵ�һ�е�һά����
		data Intermediate_variable_data0=new data();
		Readin reader = new Readin();
		//int phone=0;
		//phone=1;
		normal= success_content_list.get(0);
			for(int y=0;y<normal.size();y++){
				for(int j =0 ;j < temp_data_list.size();j++){
					Intermediate_variable_data0 = temp_data_list.get(j);
					if(Intermediate_variable_data0.type == normal.get(y) && Intermediate_variable_data0.flag==1){
						page.add(Intermediate_variable_data0.content);
						Intermediate_variable_data0.flag=0;
						break;
					}
		}
				
				
	  }
	}
	
	/**
     * �������һ�������ƥ�䲢ʶ�����е�����E-mail
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareEmail(String s,ArrayList<data> temp_data_list) {
		// BufferedReader in;
		int locate = 0;
		int ends = s.length();
		int length = 0;
		int i=0;
		Pattern pattern = Pattern
				.compile("([\\u4E00-\\u9FA5]*(\\w|-)+"
						+ "[\\u4E00-\\u9FA5]*)@(\\w+|126)\\.\\w+");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			data Intermediate_variable_data0 = new data();
			System.out.println(matcher.group(0));
			length = matcher.group(0).length();		
			Intermediate_variable_data0.content = matcher.group(0);
			Intermediate_variable_data0.length = length;
			Intermediate_variable_data0.first_position = matcher.start() + locate;
			Intermediate_variable_data0.last_position = matcher.start() + locate+ length-1;
			Intermediate_variable_data0.type = "email";
			temp_data_list.add(Intermediate_variable_data0);
			for (i = matcher.start() + locate; i <matcher.start() + locate+ length; i++){
				check[i] = false;
			}
			
			if (matcher.start() + length <= ends) {
				s = s.substring(matcher.start() + length, ends);
				ends = s.length();
				locate = locate + matcher.start() + length;
				matcher = pattern.matcher(s);
			}
		}
	}
	/**
     * �������һ�������ƥ�䲢ʶ�����е��������п���
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareBank(String s,ArrayList<data> temp_data_list) {
		int locate = 0;
		int ends = s.length();
		int length = 0;
		int i=0;
		Pattern pattern = Pattern.compile("6\\d{5}\\s?\\d{10}\\s?\\d{3}");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			data Intermediate_variable_data0 = new data();

			System.out.println(matcher.group(0));
			length = matcher.group(0).length();		
			Intermediate_variable_data0.content = matcher.group(0);
			Intermediate_variable_data0.length = length;
			Intermediate_variable_data0.first_position = matcher.start() + locate;
			Intermediate_variable_data0.last_position = matcher.start() + locate+ length-1;
			Intermediate_variable_data0.type = "Bank";
			temp_data_list.add(Intermediate_variable_data0);
			for (i = matcher.start() + locate; i <matcher.start() + locate+ length; i++){
				check[i] = false;
			}
			
			if (matcher.start() + length <= ends) {
				s = s.substring(matcher.start() + length, ends);
				ends = s.length();
				locate = locate + matcher.start() + length;
				matcher = pattern.matcher(s);
			}
			
		}
	}
	/**
     * �������һ�������ƥ�䲢ʶ�����е���������(���е�����)
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareNum(String s,ArrayList<data> temp_data_list) {
		//��ʾ�ַ���ƥ��ĳ�ʼλ��
		int locate = 0;
		//��ʾ�ַ���ƥ�����ֹλ��
		int ends = s.length();
		int length = 0;
		int i=0;
		//���жϵ��ַ�����־λ���������Ϊtrueʱ���־�ַ���ʶ��ɹ�
		boolean flag = true;
		//������ַ�������ƥ������ݽṹ
		Pattern pattern = Pattern.compile("(\\d+)");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			data Intermediate_variable_data0 = new data();
			flag=true;
			length = matcher.group(0).length();
			
			if (matcher.start() + length <= ends ) {
				s = s.substring(matcher.start() + length, ends);
				ends = s.length();
				for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) {
					if (check[i] == false) {
						flag = false;
						break;
					}
				}
				if (flag == true) {
					Intermediate_variable_data0.content = matcher.group(0);
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position = matcher.start() + locate;
					Intermediate_variable_data0.last_position = matcher.start() + locate+ length-1;
					Intermediate_variable_data0.type = "number";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) 
						check[i]=false;
					System.out.println(matcher.group(0));
				}
				locate = locate + matcher.start() + length;
				matcher = pattern.matcher(s);

			}
		}
	}
	
	/**
     * �������һ�������ƥ�䲢ʶ�����е����зָ���
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareDiv(String s,ArrayList<data> temp_data_list) {
		//��ʾ�ַ���ƥ��ĳ�ʼλ��
		int locate = 0;
		//��ʾ�ַ���ƥ�����ֹλ��
		int ends = s.length();
		int length = 0;
		int i = 0;
		//���жϵ��ַ�����־λ���������Ϊtrueʱ���־�ַ���ʶ��ɹ�
		boolean flag = true;
		//������ַ�������ƥ������ݽṹ
		Pattern pattern = Pattern.compile("(-)(-*)(\\s*)(\\w)");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			System.out.println("-------------------------");
			data Intermediate_variable_data0 = new data();
			flag=true;
			System.out.println(matcher.group(0));
			length = matcher.group(0).length()-1;
			System.out.println(length +" " + ends);
			if (matcher.start() + length <= ends ) {
				s = s.substring(matcher.start() + length, ends);
				System.out.println("---------------"+s);
				ends = s.length();
				for (i = matcher.start() + locate; i < matcher.start() + locate+ length-1; i++) {
					if (check[i] == false) {
						flag = false;
						break;
					}
				}
				if (flag == true) {
					Intermediate_variable_data0.content = matcher.group(0).substring(0,matcher.group(0).length()-1);
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position = matcher.start() + locate;
					Intermediate_variable_data0.last_position = matcher.start() + locate+ length;
					Intermediate_variable_data0.type = "Div";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate; i < matcher.start() + locate+ length-1; i++){ 
						check[i]=false;
					}
					
				}
				locate = locate + matcher.start() + length-1;
				matcher = pattern.matcher(s);

			}
		}
	}
	/**
     * �������һ�������ƥ�䲢ʶ�����е�������������
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void comparePhone(String s,ArrayList<data> temp_data_list) {
		int locate = 0;
		int ends = s.length();
		int length = 0;
		int i=0;
		boolean flag = true;
		Pattern pattern = Pattern.compile("[^0-9](0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})"
				+ "(\\-[0-9]{1,4})?[^0-9]");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			System.out.println("--!");
			data Intermediate_variable_data0 = new data();
			flag=true;
			length = matcher.group(0).length()-2;		
			System.out.println(matcher.start() + " " +length +" " + ends);
			
			if (matcher.start() + length <= ends ) {
				s = s.substring(matcher.start() + length+1, ends);
				System.out.println("+++"+s);
				ends = s.length();
				for (i = matcher.start() + locate+1; i < matcher.start() + 
						locate+ length+1; i++) {
					if (check[i] == false) {
						flag = false;
						break;
					}
				}
				
				if (flag == true) {
					Intermediate_variable_data0.content = 
							matcher.group(0).substring(1,matcher.group(0).length()-1);
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position 
							= matcher.start() + locate+1;
					Intermediate_variable_data0.last_position 
							= matcher.start() + locate+ length+1;
					Intermediate_variable_data0.type = "Phone";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate+1; i < matcher.start() 
							+ locate+ length-1; i++) 
						check[i]=false;
					
				}
				locate = locate + matcher.start() + length-1;
				matcher = pattern.matcher(s);

			}
		}
	}
	/**
     * �������һ�������ƥ�䲢ʶ�����е���������
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareDate(String s,ArrayList<data> temp_data_list) {
		int locate = 0;
		int ends = s.length();
		int length = 0;
		int i=0;
		boolean flag = true;
		Pattern pattern = Pattern.compile("([^0-9]((((1[6-9]|[2-9]\\d)\\d{2})"
				+ "(-|\\s|/|\\.|\\u5e74))?(1[02]|0?[13578])(-|\\s|/|\\.|\\u6708)"
				+ "([12]\\d|3[01]|0?[1-9])(-|\\s|/|\\.|\\u65e5)?)|"
				+ "((((1[6-9]|[2-9]\\d)\\d{2})(-|\\s|/|.|\\u5e74)?)"
				+ "(1[012]|0?[13456789])(-|\\s|/|\\.|\\u65e5)"
				+ "([12]\\d|30|0?[1-9])(-|\\s|/|\\.|\\u65e5)?)|(((1[6-9]|[2-9]\\d)"
				+ "\\d{2})-0?2-(1\\d|2[0-8]|0?[1-9]))|(((1[6-9]|[2-9]\\d)"
				+ "(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))"
				+ "-0?2-29-))");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			data Intermediate_variable_data0 = new data();
			flag=true;
			//System.out.println(matcher.group(0));
			length = matcher.group(0).length()-1;
			//System.out.println(length +" " + ends);
			if (matcher.start() + length <= ends ) {
				s = s.substring(matcher.start() + length+1, ends);
				//System.out.println("---------------"+s);
				ends = s.length();
				for (i = matcher.start() + locate+1; i < matcher.start() + locate+ length; i++) {
					if (check[i] == false) {
						flag = false;
						break;
					}
				}
				if (flag == true) {
					Intermediate_variable_data0.content = matcher.group(0).substring(1,matcher.group(0).length());;
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position = matcher.start() + locate;
					Intermediate_variable_data0.last_position = matcher.start() + locate+ length-1;
					Intermediate_variable_data0.type = "Date";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) 
						check[i]=false;
					
				}
				locate = locate + matcher.start() + length+1;
				matcher = pattern.matcher(s);

			}
		}
	}
	
	/**
     * �������һ�������ƥ�䲢ʶ�����е�������������
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareName(String s,ArrayList<data> temp_data_list) {
		int locate = 0;
		int ends = s.length();
		int length = 0;
		int i=0;
		boolean flag = true;
		Pattern pattern = Pattern.compile("[^\\u4E00-\\u9FA5][\\u4E00-\\u9FA5]{2,3}[^\\u4E00-\\u9FA5]");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			data Intermediate_variable_data0 = new data();
			flag=true;
			//System.out.println(matcher.group(0));
			length = matcher.group(0).length()-2;
			
			//System.out.println(matcher.start() + " " +length +" " + ends);
			if (matcher.start() + length <= ends ) {
				s = s.substring(matcher.start() + length+1, ends);
				System.out.println("name"+s);
				ends = s.length();
				
				for (i = matcher.start() + locate+1; i < matcher.start() + locate+ length + 1; i++) {
					if (check[i] == false) {
						flag = false;
						break;
					}
				}
				if (flag == true) {
					Intermediate_variable_data0.content = matcher.group(0).substring(1,matcher.group(0).length()-1);
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position = matcher.start() + locate+1;
					Intermediate_variable_data0.last_position = matcher.start() + locate+ length;
					Intermediate_variable_data0.type = "Name";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate+1; i < matcher.start() + locate+ length+1; i++) 
						check[i]=false;	
				}
				locate = locate + matcher.start() + length+1;
				matcher = pattern.matcher(s);

			}
		}
	}
	
	public static void compareName2(String s,ArrayList<data> temp_data_list) {
		int locate = 0;
		int ends = s.length();
		int length = 0;
		int i=0;
		boolean flag = true;
		Pattern pattern = Pattern.compile("^[\\u4E00-\\u9FA5]{2,3}[^\\u4E00-\\u9FA5]");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			data Intermediate_variable_data0 = new data();
			flag=true;
			//System.out.println(matcher.group(0));
			length = matcher.group(0).length()-1;	
			//System.out.println(matcher.start() + " " +length +" " + ends);
			if (matcher.start() + length <= ends ) {
				s = s.substring(matcher.start() + length, ends);
				ends = s.length();			
				//System.out.println((matcher.start() + locate+1)+" "+length);
				for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) {
					if (check[i] == false) {
						flag = false;
						break;
					}
					
				}
				if (flag == true) {
					Intermediate_variable_data0.content = matcher.group(0).substring(0,matcher.group(0).length()-1);
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position = matcher.start() + locate+1;
					Intermediate_variable_data0.last_position = matcher.start() + locate+ length;
					Intermediate_variable_data0.type = "Name";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) 
						check[i]=false;
					
				}
				locate = locate + matcher.start() + length+1;
				matcher = pattern.matcher(s);

			}
		}
	}
	
	/**
     * �������һ�������ƥ�䲢ʶ�����е����о����д��ڵ�����
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareNumber(String s,ArrayList<data> temp_data_list) {
		int locate = 0;
		int ends = s.length();
		int length = 0;
		int i=0;
		boolean flag = true;
		Pattern pattern = Pattern.compile("[^0-9](\\d+)((-|\\t|\\.)\\d+)?[^0-9]");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			//System.out.println("-------------------------");
			data Intermediate_variable_data0 = new data();
			flag=true;
			//System.out.println(matcher.group(0));
			length = matcher.group(0).length()-2;			
			//System.out.println(matcher.start() + " " +length +" " + ends);
			if (matcher.start() + length <= ends ) {
				s = s.substring(matcher.start() + length+1, ends);
				System.out.println("number"+s);
				ends = s.length();
				for (i = matcher.start() + locate+1; i < matcher.start() + locate+ length+1; i++) {
					if (check[i] == false) {
						flag = false;
						break;
					}
					
				}
				if (flag == true) {
					Intermediate_variable_data0.content = matcher.group(0).substring(1,matcher.group(0).length()-1);
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position = matcher.start() + locate;
					Intermediate_variable_data0.last_position = matcher.start() + locate+ length-2;
					Intermediate_variable_data0.type = "Number";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate+1; i < matcher.start() + locate+ length+1; i++) 
						check[i]=false;
					
				}
				locate = locate + matcher.start() + length+1;
				matcher = pattern.matcher(s);

			}
		}
	}
	
	/**
     * �������һ�������ƥ�䲢ʶ�����е����о���ǰ���ڵ�����
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareNumber2(String s,ArrayList<data> temp_data_list) {
		//��ʾÿ����ƥ����ַ�����ʼλ��
		int locate = 0;  
		int ends = s.length();
		int length = 0;
		int i=0;
		boolean flag = true;
		Pattern pattern = Pattern.compile("^(\\d+)(\\.\\d+)?[^0-9]");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			//System.out.println("-------------------------");
			data Intermediate_variable_data0 = new data();
			flag=true;
			//System.out.println(matcher.group(0));
			length = matcher.group(0).length()-1;
			//System.out.println(matcher.start() + " " +length +" " + ends);
			if (matcher.start() + length <= ends ) {
				s = s.substring(matcher.start() + length+1, ends);
				//System.out.println("number"+s);
				ends = s.length();
				for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) {
					if (check[i] == false) {
						flag = false;
						break;
					}
				}

				if (flag == true) {
					Intermediate_variable_data0.content = matcher.group(0).substring(0,matcher.group(0).length()-1);
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position = matcher.start() + locate;
					Intermediate_variable_data0.last_position = matcher.start() + locate+ length-1;
					Intermediate_variable_data0.type = "Number";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) 
						check[i]=false;
					
				}
				locate = locate + matcher.start() + length;
				matcher = pattern.matcher(s);

			}
		}
	}
	
	/**
     * �������һ�������ƥ�䲢ʶ�����е������ֻ�����
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareTel(String s,ArrayList<data> temp_data_list) {
		int locate = 0;
		int ends = s.length();
		int length = 0;
		int i=0;
		boolean flag = true;
		Pattern pattern = Pattern.compile("((\\(\\d{3}\\))|(\\d{3}\\-))?(13|15|18)[0-9]\\d{8}|15[89]\\d{8}");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			//System.out.println("-------------------------");
			data Intermediate_variable_data0 = new data();
			flag=true;
			//System.out.println(matcher.group(0));
			length = matcher.group(0).length();
			
			//System.out.println(matcher.start() + " " +length +" " + ends);
			if (matcher.start() + length <= ends ) {
				if(matcher.start() + length < ends)
					s = s.substring(matcher.start() + length+1, ends);
				else
					s = "";
				//System.out.println("tel"+s);
				ends = s.length();
				for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) {
					if (check[i] == false) {
						flag = false;
						break;
					}
				}
				if (flag == true) {
					Intermediate_variable_data0.content = matcher.group(0);
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position = matcher.start() + locate;
					Intermediate_variable_data0.last_position = matcher.start() + locate+ length;
					Intermediate_variable_data0.type = "Telephone";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate; i < matcher.start() + locate+ length-1; i++) 
						check[i]=false;
					
				}
				locate = locate + matcher.start() + length;
				matcher = pattern.matcher(s);

			}
		}
	}
	
	/**
     * �������һ�������ƥ�䲢ʶ�����е��������ĵ�ַ
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareAddr(String s,ArrayList<data> temp_data_list) {
		int locate = 0;
		int ends = s.length();
		int length = 0;
		int i=0;
		boolean flag = true;
		Pattern pattern = Pattern.compile("((([\\u4e00-\\u9fa5]*\\u7701)?([\\u4e00-\\u7700]|\\s*|[\\u7702-\\u9fa5])*[\\w]?[\\u5e02|\\u53bf|\\u53f7|\\u533a|\\u680b]([\\u4e00-\\u9fa5]|\\d|\\s*)*([-]{0,2}\\d+)?)+)([\\u4e00-\\u9fa5]|\\w)*");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			//System.out.println("-------------------------");
			data Intermediate_variable_data0 = new data();
			flag=true;
			//System.out.println(matcher.group(0));
			length = matcher.group(0).length();
			
			//System.out.println(matcher.start() + " " +length +" " + ends);
			if (matcher.start() + length <= ends ) {
				if(matcher.start() + length < ends)
					s = s.substring(matcher.start() + length, ends);
				else 
					s="";
				//System.out.println("addr"+s);
				ends = s.length();
				for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) {
					if (check[i] == false) {
						flag = false;
						//System.out.println("error"+locate);
						break;
					}
				}
				if (flag == true) {
					Intermediate_variable_data0.content = matcher.group(0);
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position = matcher.start() + locate;
					Intermediate_variable_data0.last_position = matcher.start() + locate+ length;
					Intermediate_variable_data0.type = "Address";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) 
						check[i]=false;
					
				}
				locate = locate + matcher.start() + length;
				matcher = pattern.matcher(s);

			}
		}
	}
	
	
	/**
     * �������һ�������ƥ�䲢ʶ�����е����г��ƺ�
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareCar(String s,ArrayList<data> temp_data_list) {
		int locate = 0;
		int ends = s.length();
		int length = 0;
		int i=0;
		boolean flag = true;
		Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z|0-9]{5}");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			data Intermediate_variable_data0 = new data();
			flag=true;
			length = matcher.group(0).length();
			if (matcher.start() + length <= ends ) {
				if(matcher.start() + length < ends)
					s = s.substring(matcher.start() + length, ends);
				else 
					s="";
				ends = s.length();
				for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) {
					if (check[i] == false) {
						flag = false;
						break;
					}
				}
				if (flag == true) {
					Intermediate_variable_data0.content = matcher.group(0);
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position = matcher.start() + locate;
					Intermediate_variable_data0.last_position = matcher.start() + locate+ length;
					Intermediate_variable_data0.type = "CarNumber";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) 
						check[i]=false;
					
					
				}
				locate = locate + matcher.start() + length;
				matcher = pattern.matcher(s);

			}
		}
	}
	
	/**
     * �������һ�������ƥ�䲢ʶ�����е�����������
     * 
     * @param s
     *            Ҫʶ������
     * @param temp_data_list
     *            �����������һά����
     * @throws Exception
     */
	
	public static void compareBankName(String s,ArrayList<data> temp_data_list) {
		int locate = 0;
		int ends = s.length();
		int length = 0;
		int i=0;
		boolean flag = true;
		Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]*[\\u94f6][\\u884c]");
		Matcher matcher = pattern.matcher(s);
		while (matcher.find()) {
			data Intermediate_variable_data0 = new data();
			flag=true;
			length = matcher.group(0).length();
			if (matcher.start() + length <= ends ) {
				if(matcher.start() + length < ends)
					s = s.substring(matcher.start() + length, ends);
				else 
					s="";
				ends = s.length();
				for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) {
					if (check[i] == false) {
						flag = false;
						System.out.println("error"+locate);
						break;
					}
				}
				if (flag == true) {
					Intermediate_variable_data0.content = matcher.group(0);
					Intermediate_variable_data0.length = length;
					Intermediate_variable_data0.first_position = matcher.start() + locate;
					Intermediate_variable_data0.last_position = matcher.start() + locate+ length;
					Intermediate_variable_data0.type = "BankName";
					temp_data_list.add(Intermediate_variable_data0);
					for (i = matcher.start() + locate; i < matcher.start() + locate+ length; i++) 
						check[i]=false;
					
					
				}
				locate = locate + matcher.start() + length;
				matcher = pattern.matcher(s);

			}
		}
	}
	
	/**
     * �������һ�������ƥ�䲢ʶ�����е�����E-mail
     * 
     * @param temp_data_list
     *            ���Ҫ��λ���ݵ�һά����
     * @param a
     *            Ҫ����λ�õĵ�һ�����ݵ�����λ
     * @param b
     *            Ҫ����λ�õĵڶ������ݵ�����λ
     * @throws Exception
     */
	
	public static void exchange(ArrayList<data> temp_data_list,int a,int b){
		data c,d;
		c=temp_data_list.get(a);
		d=temp_data_list.get(b);
		temp_data_list.set(a,d);
		temp_data_list.set(b,c);
		
	}
}