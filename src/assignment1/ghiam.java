package assignment1;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.DataSink;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoDataSinkException;
import javax.media.NoDataSourceException;
import javax.media.NoPlayerException;
import javax.media.NoProcessorException;
import javax.media.Processor;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import javax.media.protocol.FileTypeDescriptor;
import jmapps.util.StateHelper;

 public class ghiam extends Thread  {
   
     String name;
     String duongdan;
     DataSink filewriter;
     StateHelper sh;
    public ghiam(String duongdan,String name)
    {
		    this.name=name;
		    this.duongdan=duongdan;
    }
   @Override
    public void run(){
        {    
        	try {
            DataSource di = Manager.createDataSource(new MediaLocator(duongdan));
            System.out.println("ghi am "+duongdan);
            Processor p = Manager.createProcessor(di.getLocator());
            sh = new StateHelper(p);
            sh.configure();
           
            if (!sh.configure(10000)) {
                System.exit(-1);
            }
            p.setContentDescriptor(new FileTypeDescriptor(FileTypeDescriptor.WAVE));
            if (!sh.configure(10000)) {
                System.exit(-1);
            }
            p.realize();
            while (p.getState() < p.Realized) {
                Thread.sleep(50);
            }
            DataSource source = p.getDataOutput();
            MediaLocator dest = new MediaLocator("file://C:/temp/"+name+".wav");
            filewriter = null;
            filewriter = Manager.createDataSink(source, dest);
            filewriter.open();
            filewriter.start();
         sh.playToEndOfMedia(Integer.MAX_VALUE);
            
             } catch (IOException ex) {
                 Logger.getLogger(ghiam.class.getName()).log(Level.SEVERE, null, ex);
             } catch (NoDataSourceException ex) {
                 Logger.getLogger(ghiam.class.getName()).log(Level.SEVERE, null, ex);
             } catch (NoProcessorException ex) {
                Logger.getLogger(ghiam.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(ghiam.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoDataSinkException ex) {
                Logger.getLogger(ghiam.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
public void stopre()        
	{
		sh.close();
		filewriter.close();

	}
    
 }