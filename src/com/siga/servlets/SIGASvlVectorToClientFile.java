/*
 * Created on Apr 1, 2005
 * @author emilio.grau
 * 
 */
package com.siga.servlets;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.utils.ClsLogging;
import com.atos.utils.Row;
import com.siga.Utilidades.UtilidadesString;

/**
 * 
 */
public class SIGASvlVectorToClientFile extends HttpServlet {
	
	private static String CHARSET = "UTF-8";
	public static final String UTF8_BOM = "\uFEFF";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		doPost(request,response);
	}
	/**
	 * nombreFichero: El nombre del fichero que se va a descargar	 
	 * cabeceras el nombre delas cabeceras de la tabla
	 * camposnombre de los campos de base de datos
	 * datos select de la bases de datos
	 * ATENCION!!EL NUMERO DE CAMPOS TIENE QUE SER EL MISMO QUE CABECERA 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		Vector datos = (Vector)request.getAttribute("datos");
		String nombreFichero = (String)request.getAttribute("descripcion"); 
		//En el caso de que el nombre de la consulta venga con ':', los sustituimos por "/" para que el sistema se lo descargue con otro nombre
		//como hace en el caso de contener cualquier otro caracter extraño.
		nombreFichero=UtilidadesString.replaceAllIgnoreCase(nombreFichero,":","/");
		String[] cabeceras = (String[])request.getAttribute("cabeceras");
		String[] campos = (String[])request.getAttribute("campos");
		if(campos==null)
			campos = cabeceras;
		if(cabeceras==null)
			cabeceras = campos;
		
		PrintWriter out = null;
		
		try {
			if((campos == null && cabeceras==null) || cabeceras.length!=campos.length)
				throw new Exception("Inconsitencia entre número de campos y numero de cabeceras");
				
			//BNS INC_10281 AÑADO CHARSET PARA CODIFICAR € 
			response.setContentType("text/csv; charset="+CHARSET);
			response.setHeader(
			"Content-Disposition",
			"attachment; filename=\""+nombreFichero+".xls\"" );
			//BNS INC_10281 AÑADO PrintWriter PARA CONTROLAR CODIFICACIÓN EN DISTINTAS JVMs 
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), Charset.forName(CHARSET)), true);
			
			if (Charset.forName(CHARSET).equals(Charset.forName("UTF-8")))
				out.write(UTF8_BOM);
			
			for (int i=0;i<datos.size();i++){
				String linea = "";
				String cabecera = "";
				String columna = "";
				Row row = null;
				try {
					row = (Row)datos.elementAt(i);
				} catch (ClassCastException cce) {
					row = new Row();
					row.setRow((Hashtable)datos.elementAt(i));
				}
				
				
				if (i==0){
					for (int j=0;j<cabeceras.length;j++){
						cabecera += cabeceras[j]+";";
					}
					cabecera=cabecera.toLowerCase()+"\r\n";
					out.write(cabecera);
				}
				//BNS INC_10281 CAMBIO SEPARADOR \t por ; ya que en UTF-8 no funciona en excel 
				for (int k=0;k<campos.length;k++){
					// inc6861 // linea += UtilidadesString.sustituirParaExcell(row.getString(campos[k]))+";";
					linea += UtilidadesString.sustituirParaExcell(row.getString(campos[k]).replaceAll("\n", " ").replaceAll(";", "\u037E"))+";";
				}
				linea=linea+"\r\n";
				out.write(linea);				
		    	
				
			}
			out.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
			ClsLogging.writeFileLogError("Excepcion " +e,e,3);
		}
		finally {
			
				if (out!=null)
				  out.close();
			
			
		}
	}
}
