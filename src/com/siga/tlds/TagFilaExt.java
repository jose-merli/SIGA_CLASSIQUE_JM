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
				String label = elementos[i].getLabel();
				if(label==null){
					pintaImagenExt(out,
						pathAplicacion,
						elementos[i].getAction(),
						checkAcceso(tipoAcceso,elementos[i].getAccesoMin()),
						usrBean,
						elementos[i].getIconName(),
						elementos[i].getNote(),
						elementos[i].getAlt());
				}else{
					pintaButtonExt(out,
							elementos[i].getAction(),
							checkAcceso(tipoAcceso,elementos[i].getAccesoMin()),
							usrBean,
							elementos[i].getAlt(),
							label,
							elementos[i].getWidth());
					
					
				}
			}
		}
	}

	protected boolean checkAcceso(String accesoReal,String accesoMinimo) {
		try {
			String strAccesoReal = (String)accesosVec.get(accesoReal); 
			String strAccesoMinimo = (String)accesosVec.get(accesoMinimo); 
			if ((strAccesoReal).compareTo(strAccesoMinimo) <0) {
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
			String namet,
			String alt
			) 
	{
		String aux = "";
		
		if (permitido) {
			//Si trae label es que quiere poner un boton con texto
			aux = "<img id=\"iconoboton_"+ accion + this.fila + "\" src=\"" + path + "/html/imagenes/b" + icono + "_off.gif\" " +
				  "style=\"cursor:pointer;\" " +
				  "alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, alt) + "\" " +
				  "name=\"iconoFila\" " +
				  "title=\"" + UtilidadesString.getMensajeIdioma(usrBean, alt) + "\" " +
				  "border=\"0\" " +
				  "onClick=\" selectRow(" + this.fila + "); "+ accion + "(" + this.fila + "); \" " +
				  "onMouseOut=\"MM_swapImgRestore()\" " +
				  "onMouseOver=\"MM_swapImage('" + accion + "_" + this.fila + "','','" + path + "/html/imagenes/b" + icono + "_on.gif',1)\">";
			out.println(aux);
		}
		else {
			aux = "<img id=\"iconoboton_"+ accion + this.fila + "\"  src=\"" + path + "/html/imagenes/b" + icono + "_disable.gif\" " +
				  "alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, alt) + "\" " +
				  "title=\"" + UtilidadesString.getMensajeIdioma(usrBean, alt) + "\" " +
				  "name=\"" + accion + "_" + this.fila + "\" " +
				  "border=\"0\"" +
				  ">";
			
			
			out.println(aux);
		}
	}
	protected void pintaButtonExt(PrintWriter out, 
			String accion, 
			boolean permitido, 
			UsrBean usrBean,
			String alt,
			String label, String width) 
	{
		String aux = "";
		
		if (permitido) {
			
			//Si trae label es que quiere poner un boton con texto
			
				aux = "<input type=\"button\"  id = \"idButton\" " +
					"value=\"" + UtilidadesString.getMensajeIdioma(usrBean, label) + "\" " +
					"alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, alt) + "\" " +
					"class=\"buttonEnTabla\" " +
					" style=\"width:"+width+ "\" " +
					
					"onclick=\"selectRow(" + this.fila + "); "+ accion + "(" + this.fila + "); \"\"/>";
			out.println(aux);
		}
		else {
				aux = "<input type=\"button\"  id = \"idButton\" " +
					"value=\"" + UtilidadesString.getMensajeIdioma(usrBean, label) + "\" " +
					"alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, alt) + "\" " +
					"class=\"buttonEnTabla\" " +
					" style=\"width:"+width+ "\" " +
					">";
			out.println(aux);
		}
	}
	
	
}
