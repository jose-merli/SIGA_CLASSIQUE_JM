package com.siga.tlds;

/*
 * VESIONES:
 * Luis Miguel Sánchez PIÑA 21/02/2005 Primera versión.
 */

import java.io.*;
import com.atos.utils.*;
import com.siga.Utilidades.*;

public class TagFilaExtExt extends TagFilaExt
{
	String sNombreTablaPadre="";
	
	
	public void setNombreTablaPadre(String dato)
	{
		this.sNombreTablaPadre = dato;
	}
	
	public int doStartTag() 
	{
		return super.doStartTag();
	}
	
	protected void pintaImagenExt(PrintWriter out, String path, String accion, boolean permitido, UsrBean usrBean, String icono, String name,String alt)
	{
		String aux = "";
		
		if (permitido)
		{
			aux = "<img id=\"iconoboton_"+ accion + this.fila + "\" src=\"" + path + "/html/imagenes/b" + accion + "_off.gif\" " +
			  	"style=\"cursor:hand;\" ";
			  	if (alt==null || alt.equals("")) {
			  	  aux +=" alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, "general.boton." + accion) + "\" ";
			  	}else {
			  	  aux +=" alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, alt) + "\" ";
			  	}
			  	//aux += "name=\"iconoFila\" " +
			  	aux += " name=\"" + accion + "_" + this.fila + "\" " +
			  	"border=\"0\" " +
			  	
			  	"onClick=\"selectRow_" + this.sNombreTablaPadre + "(" + this.fila + "); "+ accion + "_" + this.sNombreTablaPadre + "(" + this.fila + "); \" " +
			  	//"onClick=\" selectRow(" + this.fila + "); "+ accion + "(" + this.fila + "); \" " +
			  	"onMouseOut=\"MM_swapImgRestore()\" " +
			  	"onMouseOver=\"MM_swapImage('" + accion + "_" + this.fila + "','','" + path + "/html/imagenes/b" + accion + "_on.gif',1)\">";

			out.println(aux);
		}
		
		else
		{
			aux = "<img id=\"iconoboton_"+ accion + this.fila + "\" src=\"" + path + "/html/imagenes/b" + icono + "_disable.gif\" ";
		  		if (alt==null || alt.equals("")) {
			  	  aux +=" alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, "general.boton." + accion) + "\" ";
			  	}else {
			  	  aux +=" alt=\"" + UtilidadesString.getMensajeIdioma(usrBean, alt) + "\" ";
			  	}
		  		//aux += "name=\"iconoFila\" " +
			  	aux += " name=\"" + accion + "_" + this.fila + "\" " +
				  "border=\"0\"" +
				  ">";
		
			out.println(aux);
		}
	}
}