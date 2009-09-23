package com.siga.app;

import java.io.File;


//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import javax.management.Notification;
//import javax.management.NotificationListener;
//
//import weblogic.management.timer.Timer;
//
//import com.atos.utils.ClsConstants;
//import com.atos.utils.ClsLogging;


/*
 * Created on Oct 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author raul.ggonzalez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PruebaRaul  {



	// HOLA Raul FERNANDO 1



	
	public static void main(String[] args) {
		try {	
		    File f = new File("C:\\Documents and Settings\\raulg.ITCGAE\\Mis documentos\\DESVAN\\clog\\img");
			String[] children = f.list();
		    for (int i = 0; i<children.length; i++) {
			    File aux = new File(f, children[i]);
			    String nombre=aux.getName();
			    nombre = nombre.replaceAll(" ","_");
			    aux.renameTo(new File("C:\\Documents and Settings\\raulg.ITCGAE\\Mis documentos\\DESVAN\\clog\\img\\"+nombre));
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
