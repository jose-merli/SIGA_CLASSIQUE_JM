/*
 * Created on Apr 1, 2005
 * @author emilio.grau
 * 
 */
package com.siga.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsConstants;
import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.siga.Utilidades.UtilidadesString;

/**
 * 
 */
public class SIGASvlVectorToClientFileConExtension extends HttpServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		Vector datos = (Vector)request.getAttribute("datos");
		String nombreFichero = (String)request.getAttribute("nombreFichero");
		String rutaFichero = (String)request.getAttribute("rutaFichero"); 
		String[] cabeceras = (String[])request.getAttribute("cabeceras");
		String extension= (String)request.getAttribute("extension");
		Boolean imprimirCabecera = (Boolean)request.getAttribute("imprimirCabecera");
		OutputStream out = null;
		OutputStream out1 = null;
		if (imprimirCabecera == null) {
			imprimirCabecera = Boolean.FALSE;
		}
			
		
		try {
			File archivo = new File(rutaFichero+ClsConstants.FILE_SEP+nombreFichero+"."+extension);
			
			 out = new FileOutputStream(archivo);
			 out1 = response.getOutputStream();
			
			if (extension!=null && extension.equals("txt")){
				response.setContentType("'text/plain'");
				response.setHeader(
						"Content-Disposition",
						"attachment; filename=\""+nombreFichero+".txt\"" );
						
			}else{
				response.setContentType("'text/csv'");
				response.setHeader(
						"Content-Disposition",
						"attachment; filename=\""+nombreFichero+".xls\"" );
						
			}
			
			
			for (int i=0;i<datos.size();i++){
				String linea = "";
				String cabecera = "";
				
				Row row = (Row)datos.elementAt(i);
				if (imprimirCabecera.booleanValue()){
					for (int j=0;j<cabeceras.length;j++){
						cabecera += cabeceras[j]+ClsConstants.SEPARADOR;
					}
					cabecera=cabecera.toLowerCase()+"\r\n";
					out.write(cabecera.getBytes());
					out1.write(cabecera.getBytes());
					imprimirCabecera = Boolean.FALSE;
				}
				
				for (int k=0;k<cabeceras.length;k++){
					linea += UtilidadesString.sustituirParaExcell(row.getString(cabeceras[k]))+ClsConstants.SEPARADOR;
				}
				
				linea=linea+"\r\n";
				out.write(linea.getBytes());
				out1.write(linea.getBytes());
				
			}
			/*out.flush();
			out1.flush();
			out.close();
			out1.close();*/
			
			//response.flushBuffer();
			
		} catch (Exception e) {
			e.printStackTrace();
			ClsLogging.writeFileLogError("Excepcion " +e,e,3);
		}finally{

            if(out1 !=null){
            	out1.flush();
            	out1.close();
            	out1 = null;
            }

            if(out!=null){
                    out.flush();
                    out.close();
                    out = null;

            }
        }
		
	}

}
