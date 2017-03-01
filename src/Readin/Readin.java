package Readin;

import java.io.*;
import java.util.*;

public class Readin {
	public static void main(String[] args) throws IOException{
		writeStrToFile("defsdfds","E:\\output2\\111.txt",0);
	}
	
	 public static void writeStrToFile(String str,String path,int flag) throws IOException//要写入的字符串，要写入的文件路径
	 {
	  File file = new File(path);
	  String checkpath = "";
	  checkpath = path.substring(0, path.lastIndexOf('\\'));
	  //System.out.println(checkpath);
	  File checkfile = new File(checkpath);
	  if(checkfile.exists()){
	  FileWriter writer = new FileWriter(file,true);
	  if(flag == 1){
		  //writer.write(str);
		  writer.write("\r\n");
	  }
	  else {
		  //System.out.println(str);
		  //System.out.println('1');
		  writer.write(str);
		  //writer.write(',');
	  }
	  writer.close();
	  
	 }
	  else{
		  System.out.println("error");  
	  }
	 }
	 
}
