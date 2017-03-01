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
 * ZIP压缩工具 
 *  
 
 */  
public class Zip {  
  
    public static final String EXT = ".zip";  
    private static final String BASE_DIR = "";  
    private static final String PATH = File.separator;  
    private static final int BUFFER = 1024;  
    public static ArrayList<String> fileName=new  ArrayList<String>();
	
    /** 
     * 文件 解压缩 
     *  
     * @param srcPath 
     *            源文件路径 
     *  
     * @throws Exception 
     */  
    public static void main(String[] args) throws Exception{  
        // 解压到指定目录  
        //decompress("E:\\datatop.zip", "E:\\stack");  
        // 解压到当前目录  
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
     * 解压缩 
     *  
     * @param srcFile 
     * @throws Exception 
     */  
    public static void decompress(File srcFile) throws Exception {  
        String basePath = srcFile.getParent();  
        decompress(srcFile, basePath);  
    }  
  
    /** 
     * 解压缩 
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
     * 解压缩 
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
     * 文件 解压缩 
     *  
     * @param srcPath 
     *            源文件路径 
     * @param destPath 
     *            目标文件路径 
     * @throws Exception 
     */  
    public static void decompress(String srcPath, String destPath)  
            throws Exception {  
  
        File srcFile = new File(srcPath);  
        decompress(srcFile, destPath);  
    }  
  
    /** 
     * 文件 解压缩 
     *  
     * @param destFile 
     *            目标文件 
     * @param zis 
     *            ZipInputStream 
     * @throws Exception 
     */  
    private static void decompress(File destFile, ZipInputStream zis)  
            throws Exception {  
  
        ZipEntry entry = null;  
        while ((entry = zis.getNextEntry()) != null) {  
  
            // 文件  
            String dir = destFile.getPath() + File.separator + entry.getName();  
  
            File dirFile = new File(dir);  
  
            // 文件检查  
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
     * 文件探针 
     *  
     *  
     * 当父目录不存在时，创建目录！ 
     *  
     *  
     * @param dirFile 
     */  
    private static void fileProber(File dirFile) {  
  
        File parentFile = dirFile.getParentFile();  
        if (!parentFile.exists()) {  
  
            // 递归寻找上级目录  
            fileProber(parentFile);  
  
            parentFile.mkdir();  
        }  
  
    }  
  
    /** 
     * 文件解压缩 
     *  
     * @param destFile 
     *            目标文件 
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