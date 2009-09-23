/*
 * Created on 15-dic-2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.siga.tlds;

import java.io.PrintWriter;
import java.util.Hashtable;

import com.atos.utils.UsrBean;
import com.siga.Utilidades.UtilidadesString;
import com.siga.gui.processTree.SIGAPTConstants;

/**
 * @author Carmen.Garcia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TagFilaExt extends TagFila {
	protected FilaExtElement[] elementos=null;
	protected Hashtable accesosVec=new Hashtable(); 
	
	public void setElementos(FilaExtElement[] _elementos) {
		accesosVec.put(SIGAPTConstants.ACCESS_DENY,"1");
		accesosVec.put(SIGAPTConstants.ACCESS_NONE,"0");
		accesosVec.put(SIGAPTConstants.ACCESS_READ,"2");
		accesosVec.put(SIGAPTConstants.ACCESS_FULL,"3");
		accesosVec.put(SIGAPTConstants.ACCESS_SIGAENPRODUCCION,"40");
		elementos=_elementos;
	}
	
	public int doStartTag() 
	{
		return super.doStartTag();
	}
	
	protected void printLines(PrintWriter out,
			UsrBean usrBean,String tipoAcceso,String pathAplicacion) throws Exception {
		super.printLines(out,usrBean,tipoAcceso,pathAplicacion);
		
		if (elementos!=null) {
			for (int i=0;i<elementos.length;i++) {
				if (elementos[i]==null) continue;
				pintaImagenExt(out,
						pathAplicacion,
						elementos[i].getAction(),
						checkAcceso(tipoAcceso,elementos[i].getAccesoMin()),
						usrBean,
						elementos[i].getIconName(),
						elementos[i].getNote(),
						elementos[i].getAlt());
			}
		}
	}

	protected boolean checkAcceso(String accesoReal,String accesoMinimo) {
		try {
			if (((String)accesosVec.get(accesoReal)).compareTo((String)accesosVec.get(accesoMinimo)) <0) {
				return false;
			}	
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	protected void pintaImagenExt(PrintWriter out, 
			String path, 
			String accion, 
			boolean permitido, 
			UsrBean usrBean,
			String icono,
			String name,
			String alt) 
	{
		String aux = "";
		
		if (permitido) {
			/* LMS (04/01/2005) */
		    /* En lugar de poner una imagen dentro de un enlace, se pone únicamente la imagen. */
			/*aux = "<a href='javascript://' " +
				  "onClick=\"selectRow(" + this.fila + "); "+ accion + "(" + this.fila + ");\" " +
				  "onMouseOut=\"MM_swapImgRestore()\" " +
				  "onMouseOver=\"MM_swapImage('" + accion + "_" + this.fila + "','','" + path + "/html/imagenes/b" + icono + "_on.gif',1)\">";
	
			aux += "<img src=\"" + path + "/html/imagenes/b" + icono + "_off.gif\" " +
				  "alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, "general.boton." + accion) + "\" " +
				  "name=\"" + accion + "_" + this.fila + "\" " +
//				  "width='26' " +
//				  "height='27' " +
				  "border=\"0\"" +
				  ">";
			
			aux += "</a>";*/
		aux = "<img id=\"iconoboton_"+ accion + this.fila + "\" src=\"" + path + "/html/imagenes/b" + icono + "_off.gif\" " +
			  "style=\"cursor:hand;\" " +
			  "alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, alt) + "\" " +
			  //"name=\"" + accion + "_" + this.fila + "\" " +
			  "name=\"iconoFila\" " +
//			  "width='26' " +
//			  "height='27' " +
			  "border=\"0\" " +
			  //"onClick=\"selectRow(" + this.fila + "); "+ accion + "(" + this.fila + "); parent.buscar();\" " +
			  //"onClick=\"deshabilitariconos('iconoFila');selectRow(" + this.fila + "); "+ accion + "(" + this.fila + ");habilitariconos('iconoFila'); \" " +
			  "onClick=\" selectRow(" + this.fila + "); "+ accion + "(" + this.fila + "); \" " +
			  
			  "onMouseOut=\"MM_swapImgRestore()\" " +
			  "onMouseOver=\"MM_swapImage('" + accion + "_" + this.fila + "','','" + path + "/html/imagenes/b" + icono + "_on.gif',1)\">";

			out.println(aux);
		}
		else {
			aux = "<img id=\"iconoboton_"+ accion + this.fila + "\"  src=\"" + path + "/html/imagenes/b" + icono + "_disable.gif\" " +
				  "alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, alt) + "\" " +
				  //"name=\"iconoFila\" " +
				  "name=\"" + accion + "_" + this.fila + "\" " +
//				  "width='26' " +
//				  "height='27' " +
				  "border=\"0\"" +
				  ">";
			out.println(aux);
		}
	}
	
}
