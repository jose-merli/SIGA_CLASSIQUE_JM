package com.siga.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.management.Notification;
import javax.management.NotificationListener;

import weblogic.management.timer.Timer;

//import com.atos.utils.ClsConstants;
//import com.atos.utils.ClsLogging;


/**
 * @author raul.ggonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TimerDesvan  implements NotificationListener {

	
	public TimerDesvan() {
		
	}	
	
	public void destroy() {
		//System.out.println("salio");
	}	

	public void init() {
		try {	
	
			long lIntervalo = Long.parseLong("5")*60*1000;
	        Timer timer = new Timer();
	        timer.addNotificationListener(this, null, "logDesvannn");

	        Date timerTriggerAt = new Date((new Date()).getTime() + 60000L);
	        Integer idNotificacion = timer.addNotification("logDesvannn", "logDesvannn", this, timerTriggerAt, lIntervalo);

	        timer.start();
		
	        ////////////////////////////
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void handleNotification(Notification notif, Object handback)
	 {

		try {
			
			
			URL url = new URL("http://www.desvan.net/");
			
			SimpleDateFormat sdf = new SimpleDateFormat("-ddMMyy-HHmm");
			String hora = sdf.format(new Date());
			
			File file = new File("C:\\logDesvan\\logDesvan"+hora+".htm");
			int BUFFER_SIZE = 8 * 1024;   

			System.out.println("DESVAN: log creado " + hora);

			FileOutputStream out = new FileOutputStream(file);
			InputStream in = url.openStream();
	        int bytesRead = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
        } catch (IOException ex) {
			ex.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
        }
	    
	 }	
	
}
