package com.siga.Utilidades;

import java.applet.Applet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import netscape.javascript.JSObject; 

//Applet para obtener la IP del CLiente y enviarla a un Servlet que la inserta en Sesion con el nombre "IPCLIENTE".
public class AppletDireccionIP extends Applet {

	private static final long serialVersionUID = 1538230694419253136L;
	
	private String direccionIP = "";
	
	public void init(){ 
		try {
			//Obtengo la direccion URL de conexion del CLiente y de ella el host y el puerto:
			URL origin= this.getCodeBase(); //Clase del Applet
			String hostName = origin.getHost();
			int port = origin.getPort();

			System.out.println("HostName:"+hostName);
			System.out.println("Port:"+port);
		
			System.out.println("Creamos el socket...");
			//Creo un socket a ese host y puerto y obtengo la ip de origen (la del cliente):
						
			Socket local= new Socket(hostName, port);
			
			//Socket local= new Socket(hostName, port);
			InetAddress l3= local.getLocalAddress();
			this.direccionIP = l3.getHostAddress();
			local.close();
		} catch (Exception e){
			e.printStackTrace();
			direccionIP = "IP NO ENCONTRADA";
		}
	} 

	public void start(){
		System.out.println("Local IP:"+this.direccionIP);
		this.enviarform();
	}
	
	public void stop(){
	} 
	
	private void enviarform() {
		try { 
	       JSObject win = (JSObject) JSObject.getWindow(this);
	       win.eval("establecerIP(\""+ this.direccionIP + "\")");
        } catch (Exception e){
			System.out.println("ERROR en Applet AppletDireccionIP.");
			e.printStackTrace();
        }	       	     
	}

	private void enviarDatosGET() {
        URL urlServlet = null;
        String parametro="";
        
        try {
        	System.out.println("Método enviarDatosGET() en Applet AppletDireccionIP.");

			//        	
        	//Construyo la url para conectarme a traves del Applet al Servlet que actualiza en sesion la ip del cliente:
        	//
            String host = this.getCodeBase().getHost();
            String protocolo = this.getCodeBase().getProtocol();
            int port = this.getCodeBase().getPort();            
            String peticion = "/SIGA/SIGADireccionIP.svrl";
           	parametro = "IPCLIENTE=" + URLEncoder.encode(this.direccionIP,"UTF-8");
           	urlServlet = new URL("http://localhost:80/SIGA/SIGADireccionIP.svrl");           	

			//
            //Realizo la conexion al Servlet:
            //
            URLConnection conexion = urlServlet.openConnection();
            conexion.setUseCaches (false); //Para que el Navegador no use la cache.
            
            //Cargo la pagina:
            this.getAppletContext().showDocument(urlServlet);
        } catch (Exception e){
			System.out.println("ERROR en Applet AppletDireccionIP.");
			e.printStackTrace();
        }
	}

	private void enviarDatosPOST() {
        URL urlServlet = null;
        String parametro="";
        
        try {
        	System.out.println("--> init()");
        	
			//        	
        	//Construyo la url para conectarme a traves del Applet al Servlet que actualiza en sesion la ip del cliente:
        	//
            String host = this.getCodeBase().getHost();
            String protocolo = this.getCodeBase().getProtocol();
            int port = this.getCodeBase().getPort();     
            parametro = "IPCLIENTE=" + URLEncoder.encode(this.direccionIP,"UTF-8");
            String peticion = "/SIGA/SIGADireccionIP.svrl?"+parametro;
           	           	           	
            urlServlet = new URL(protocolo,host,port,peticion);
        	this.getAppletContext().showDocument(urlServlet);
        } catch (Exception e){
			System.out.println("ERROR en Applet AppletDireccionIP.");
			e.printStackTrace();
        }
	}

	private void enviarDatosPOSTOLD() {
        URL urlServlet = null;
        String parametro="";
        
        try {
        	System.out.println("--> init()");
        	
			//        	
        	//Construyo la url para conectarme a traves del Applet al Servlet que actualiza en sesion la ip del cliente:
        	//
            String host = this.getCodeBase().getHost();
            String protocolo = this.getCodeBase().getProtocol();
            int port = this.getCodeBase().getPort();            
            String peticion = "/SIGA/SIGADireccionIP.svrl";
           	parametro = "IPCLIENTE=" + URLEncoder.encode(this.direccionIP,"UTF-8");
           	
           	
           	System.out.println("-->port 443 usado para mandar la ip calculada al servidor...");
            urlServlet = new URL("https",host,443,peticion);
            //urlServlet = new URL(protocolo,host,port,peticion);
            

			//
            //Realizo la conexion al Servlet:
            //
            URLConnection conexion = urlServlet.openConnection();
            conexion.setDoOutput(true); //Avisamos al Navegador del envio de datos.            
            conexion.setUseCaches (false); //Para que el Navegador no use la cache.
            
            //
            //Mando al Servlet en la url por método GET la IP del Cliente:
            //
            OutputStreamWriter bufferSalida = new OutputStreamWriter(conexion.getOutputStream());
            bufferSalida.write(parametro);                                   
            bufferSalida.flush(); //Forzamos el vaciado del buffer para realizar el envio.
            
            //
            //Recibimos la respuesta del Servlet (esperamos un "$$OK$$")
            //
            BufferedReader bufferEntrada = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            String linea = bufferEntrada.readLine();
            
            //Cerramos los 2 buffer:
            bufferSalida.close();
            bufferEntrada.close();
        } catch (Exception e){
			System.out.println("ERROR en Applet AppletDireccionIP.");
			e.printStackTrace();
        }
	}

}