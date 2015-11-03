/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Thien Nhan
 */
public class Http {

    String server;

    public Http() {
         this.server="http://172.28.13.168/WEBSERVICE/restful";
         
    }
  
 
     public String signIn(String id,String pw,String ip,int port) throws MalformedURLException, IOException
    {   
        String query= "/dangnhap?id="+id+"&pw="+pw+"&ip="+ip+"&port="+String.valueOf(port);
        System.out.println(this.server+query);
        URLConnection connection = new URL(this.server+query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
       // System.out.println(br.readLine());
      
       
        return "success";
    }
   
     public String getOnlineUser(String id) throws MalformedURLException, IOException{
        String query="/getonlineuser?id="+id;
           URLConnection connection = new URL(this.server+query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return br.readLine();
     }
     public String signOut(String id) throws MalformedURLException, IOException
    {   
        
        String query= "/dangxuat?id="+id;
        URLConnection connection = new URL(this.server+query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        System.out.println(br.readLine());
        return "success";
    }
     public String callConnection(String from,String to) throws MalformedURLException, IOException
     {
         String query="/call?from=" +from+"&to="+to;
           URLConnection connection = new URL(this.server+query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        System.out.println(br.readLine());
        return "success";
     }
     public String listenCall(String to) throws MalformedURLException, IOException{
        String query="/listen?to="+to;
           URLConnection connection = new URL(this.server+query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return br.readLine();
     }
     public String addLiveAudio(String id,String ip,String port) throws MalformedURLException, IOException{
         String query="/addlive?id="+id+"&ip="+ip+"&port="+port;
         URLConnection connection = new URL(this.server+query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
         BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return br.readLine();
     }
     public String getLiveAudio() throws MalformedURLException, IOException{
         String query="/getlive";
         URLConnection connection = new URL(this.server+query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
         BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return br.readLine();
     }
     public String enterRoom(String id_user,String id_room) throws MalformedURLException, IOException{
          String query="/enterroom?id_user="+id_user+"&id_room="+id_room;
         URLConnection connection = new URL(this.server+query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
         BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return br.readLine();
     }
     public String stopLive(String id) throws MalformedURLException, IOException{
         String query="/stoplive?id="+id;
         URLConnection connection = new URL(this.server+query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
         BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return br.readLine();
     }
     public String getUserInRoom(String id) throws MalformedURLException, IOException{
           String query="/getuserinroom?id="+id;
         URLConnection connection = new URL(this.server+query).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
         BufferedReader br=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return br.readLine();
     }
     public void getFile(int port,String filename) throws IOException{
         int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    Socket sock = null;
      ServerSocket receive_socket=null;
    int FILE_SIZE=1000000;
    try {
     receive_socket=new ServerSocket(port);
       System.out.println("Connecting...");
     sock =receive_socket.accept();
      // receive file
      byte [] mybytearray  = new byte [FILE_SIZE];
      InputStream is = sock.getInputStream();
      fos = new FileOutputStream(filename);
      bos = new BufferedOutputStream(fos);
      bytesRead = is.read(mybytearray,0,mybytearray.length);
      current = bytesRead;

      do {
         bytesRead =
            is.read(mybytearray, current, (mybytearray.length-current));
         if(bytesRead >= 0) current += bytesRead;
      } while(bytesRead > -1);

      bos.write(mybytearray, 0 , current);
      bos.flush();
   JOptionPane.showMessageDialog(null,"Done");
    }
    finally {
        
      if (fos != null) fos.close();
      if (bos != null) bos.close();
      if (sock != null) sock.close();
    
      receive_socket.close();
}
}
     public void sendFile(String ip,int port,String path) throws IOException{
         
    BufferedInputStream bis = null;
    OutputStream os = null;
    ServerSocket servsock = null;
    Socket sock = null;
    
      try{
        sock = new Socket(ip,port);
         File myFile = new File(path);
            byte[] mybytearray = new byte[(int) myFile.length()];
           FileInputStream  fis = new FileInputStream(myFile);
             bis = new BufferedInputStream(fis);
            bis.read(mybytearray, 0, mybytearray.length);
           os = sock.getOutputStream();
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
      }  finally {
          if (bis != null) bis.close();
          if (os != null) os.close();
          if (sock!=null) sock.close();
        }
  } 
     
     
    void signIn(String id, JTextField pw) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
