package Zip;  
  
import java.io.BufferedInputStream;  
import java.io.BufferedOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.CRC32;  
import java.util.zip.CheckedInputStream;  
import java.util.zip.CheckedOutputStream;  
import java.util.zip.ZipEntry;  
import java.util.zip.ZipInputStream;  
import java.util.zip.ZipOutputStream;  
import java.io.File;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import test.Sender;
//import Stap1.Step1.data;

//import Stap1.Step1.data;
  
/** 
 * ZIPѹ������ 
 *  
 
 */  
public class Zip {  
  
    public static final String EXT = ".zip";  
    private static final String BASE_DIR = "";  
    private static final String PATH = File.separator;  
    private static final int BUFFER = 1024;  
    public static ArrayList<String> fileName=new  ArrayList<String>();
	
    /** 
     * �ļ� ��ѹ�� 
     *  
     * @param srcPath 
     *            Դ�ļ�·�� 
     *  
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception{  
        // ��ѹ��ָ��Ŀ¼  
        //decompress("E:\\datatop.zip", "E:\\stack");  
        // ��ѹ����ǰĿ¼  
    	//System.setProperty("file.encoding", "GBK");
    	String filepath=null;
    	filepath="E:\\stack";
    	Sender.start(filepath);
    	System.out.println("success");
    	//Zipdecompress(filepath);

    }
    public static void Zipdecompress(String filepath) throws Exception{
    	getFileName(filepath);
    	for(int i=0;i<fileName.size();i++){
    	String s = fileName.get(i);
    	//Sender.start(s);
    	System.out.println(s+"success");
    	String prefix=s.substring(s.lastIndexOf(".")+1);
    	if(prefix.equals("zip") || prefix.equals("gz")){
    		String first = s.substring(0,s.lastIndexOf("."));
    		decompress(s,first); 
    		fileName.add(first);
    	System.out.println(s+"success");
    	}
    	else if(prefix.equals(s))
    	{
    		System.out.println(s+"success");
    		Sender.start(s);
    		getFileName(s);
    	}
    	else {
    		System.out.println(s+"loss");
    	}
    	System.out.println(s.length());
    	}
    	for(int i=0;i<fileName.size();i++)
    		System.out.println("-------------" +fileName.get(i));
    	//System.out.println(Arrays.toString(ss));
    }  
  
    
    public static void decompress(String srcPath) throws Exception {  
        File srcFile = new File(srcPath);  
  
        decompress(srcFile);  
    }  
  
    /** 
     * ��ѹ�� 
     *  
     * @param srcFile 
     * @throws Exception 
     */  
    public static void decompress(File srcFile) throws Exception {  
        String basePath = srcFile.getParent();  
        decompress(srcFile, basePath);  
    }  
  
    /** 
     * ��ѹ�� 
     *  
     * @param srcFile 
     * @param destFile 
     * @throws Exception 
     */  
    public static void decompress(File srcFile, File destFile) throws Exception {  
  
        CheckedInputStream cis = new CheckedInputStream(new FileInputStream(  
                srcFile), new CRC32());  
  
        ZipInputStream zis = new ZipInputStream(cis);  
  
        decompress(destFile, zis);  
  
        zis.close();  
  
    }  
  
    /** 
     * ��ѹ�� 
     *  
     * @param srcFile 
     * @param destPath 
     * @throws Exception 
     */  
    public static void getFileName(String path)
    {
        File file = new File(path);
        String[] rem =file.list();
        //System.out.println("-----"+rem[0]);
        for(int i=0;i<rem.length;i++){
        fileName.add(path + "\\" +rem[i]);
        }
    }
    public static void decompress(File srcFile, String destPath)  
            throws Exception {  
        decompress(srcFile, new File(destPath));  
  
    }  
  
    /** 
     * �ļ� ��ѹ�� 
     *  
     * @param srcPath 
     *            Դ�ļ�·�� 
     * @param destPath 
     *            Ŀ���ļ�·�� 
     * @throws Exception 
     */  
    public static void decompress(String srcPath, String destPath)  
            throws Exception {  
  
        File srcFile = new File(srcPath);  
        decompress(srcFile, destPath);  
    }  
  
    /** 
     * �ļ� ��ѹ�� 
     *  
     * @param destFile 
     *            Ŀ���ļ� 
     * @param zis 
     *            ZipInputStream 
     * @throws Exception 
     */  
    private static void decompress(File destFile, ZipInputStream zis)  
            throws Exception {  
  
        ZipEntry entry = null;  
        while ((entry = zis.getNextEntry()) != null) {  
  
            // �ļ�  
            String dir = destFile.getPath() + File.separator + entry.getName();  
  
            File dirFile = new File(dir);  
  
            // �ļ����  
            fileProber(dirFile);  
  
            if (entry.isDirectory()) {  
                dirFile.mkdirs();  
            } else {  
                decompressFile(dirFile, zis);  
            }  
  
            zis.closeEntry();  
        }  
    }  
  
    /** 
     * �ļ�̽�� 
     *  
     *  
     * ����Ŀ¼������ʱ������Ŀ¼�� 
     *  
     *  
     * @param dirFile 
     */  
    private static void fileProber(File dirFile) {  
  
        File parentFile = dirFile.getParentFile();  
        if (!parentFile.exists()) {  
  
            // �ݹ�Ѱ���ϼ�Ŀ¼  
            fileProber(parentFile);  
  
            parentFile.mkdir();  
        }  
  
    }  
  
    /** 
     * �ļ���ѹ�� 
     *  
     * @param destFile 
     *            Ŀ���ļ� 
     * @param zis 
     *            ZipInputStream 
     * @throws Exception 
     */  
    private static void decompressFile(File destFile, ZipInputStream zis)  
            throws Exception {  
  
        BufferedOutputStream bos = new BufferedOutputStream(  
                new FileOutputStream(destFile));  
  
        int count;  
        byte data[] = new byte[BUFFER];  
        while ((count = zis.read(data, 0, BUFFER)) != -1) {  
            bos.write(data, 0, count);  
        }  
  
        bos.close();  
    }  
  
}  