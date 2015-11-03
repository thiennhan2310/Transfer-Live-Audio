/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment1;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.MediaLocator;
import javax.swing.JOptionPane;

/**
 *
 * @author Thien Nhan
 */
public class listenRequest extends Thread{
    Socket socket;
    ServerSocket serverSocket;
    callReceive receive;
    callTransmit transmit;
      CallControll frame;
    int port;
    public listenRequest(int port) throws IOException{   
        this.port=port;
        if(serverSocket==null)
        serverSocket=new ServerSocket(this.port);
    }
    
    public void listen() throws IOException, UnknownHostException, InterruptedException
    {
        socket=serverSocket.accept();
        BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.handleRequest(br.readLine());
        socket.close();
        
    }
    public void sendRequest(String to,int port,String rq) throws IOException
    {
        //call-from-port; ok-to-port;
        socket=new Socket(to,port);
        socket.getOutputStream().write(rq.getBytes("UTF-8"));
        socket.close();
        
    }
    public  void handleRequest(String rq) throws UnknownHostException, IOException, InterruptedException{
        String[] temp=rq.split("-");
        String action=temp[0];
        String ip=temp[1]; //
        System.out.println(rq);
        int port=Integer.parseInt(temp[2]);
         String localIp=InetAddress.getLocalHost().toString();
        if(action.equals("call")){
            //Nhan yeu cau call
           
    int confirm= JOptionPane.showConfirmDialog(null, "Có 1 cuộc gọi tới!","Xác Nhận",JOptionPane.YES_NO_OPTION);
            if(confirm==JOptionPane.YES_OPTION){
                System.out.println(String.valueOf(port)+"-"+String.valueOf(this.port));
                this.sendRequest(ip,port,"ok-"+localIp.split("/")[1]+"-"+this.port);
                
                 transmit = new callTransmit(new MediaLocator("javasound://44100"),ip,"22222");
                transmit.start();
                 
                 String[] session={localIp.split("/")[1]+"/22222"};
                 
                receive=new callReceive(session);
                receive.initialize();
               frame=  new CallControll(receive,transmit,ip,port,this.port);
              frame.setVisible(true);
            }
            else{
                transmit = new callTransmit(new MediaLocator("javasound://44100"),ip,"22222");
                transmit.start();
                Thread.sleep(3000);
               transmit.stop();
                 this.sendRequest(ip,port,"no-"+localIp.split("/")[1]+"-"+this.port);
            }
        
        }
        else if(action.equals("no")){
            JOptionPane.showMessageDialog(null,"Từ chối nhận cuộc gọi");
        }
        else if(action.equals("sendfile")){
            String filename=temp[3];
            String path=temp[4];
           int confirm= JOptionPane.showConfirmDialog(null, "IP: "+ip+" muốn gửi cho bạn file "+filename,"Xác Nhận",JOptionPane.YES_NO_OPTION);
            if(confirm==JOptionPane.YES_OPTION){
                sendRequest(ip, port, "acceptreceivefile-"+localIp.split("/")[1]+"-"+this.port+"-"+path);
                new Http().getFile(this.port+1000, filename);
            }
            else{
                sendRequest(ip, port, "denyreceivefile-"+localIp.split("/")[1]+"-"+this.port);
            }
        }
        else if(action.equals("acceptreceivefile")){
            String path=temp[3];
            new Http().sendFile(ip, port+1000, path);
            JOptionPane.showMessageDialog(null, "Done!");
        }
          else if(action.equals("denyreceivefile")){
           JOptionPane.showMessageDialog(null, "Không Đồng ý Nhận file");
        }
      else if(action.equals("ok")){
           transmit = new callTransmit(new MediaLocator("javasound://44100"),ip,"22222");
                transmit.start();      
                 String[] session={localIp.split("/")[1]+"/22222"};
                receive=new callReceive(session);
                receive.initialize();
                 frame=  new CallControll(receive,transmit,ip,port,this.port);
              frame.setVisible(true);
      }
         else if(action.equals("stop")){
                  if(transmit!=null)  transmit.stop();  
                    if(receive!=null) receive.close();
                    frame.setVisible(false);
                  
      }
          else if(action.equals("miss")){
               frame.setVisible(false);
               JOptionPane.showMessageDialog(null, "Lỡ 1 cuộc gọi từ"+ip);
                  
      }
               
    }
     public void run(){
       while(true){          
          try {
               listen();            
              
           } catch (IOException ex) {
               Logger.getLogger(listenRequest.class.getName()).log(Level.SEVERE, null, ex);
           } catch (InterruptedException ex) {
               Logger.getLogger(listenRequest.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
    
     }
}
