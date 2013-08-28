/*
 * Created on 16-sep-2004
 * Modificado RGG 01-03-2006 Cambio para traducir las descripciones desde recursos. 
 */
package com.siga.tlds;

import java.io.*;
import java.util.*;

import com.atos.utils.*;

import javax.servlet.http.*;
import com.siga.Utilidades.*;
import com.siga.general.ParejaNombreID;


import javax.servlet.jsp.tagext.*;

import org.redabogacia.sigaservices.app.util.ReadProperties;
import org.redabogacia.sigaservices.app.util.SIGAReferences;

/**
 * @author daniel.campos
 */

public class TagComboBD extends TagSupport {
	
	private static int OK = 1;
	private static int ERROR = -1;
	private static String parametroWhere  = "@parametro@";
	private static String parametroIn = "@parametroIn@";
	private static String parametroIdioma = "@idioma@";
	
	// *****************************************************
	
//	Cambio para ArrayList -> por id
	ArrayList elementoSel = new ArrayList();
//	Cambio para Int -> por filas	
	int porFilas = 0;	
//	int elementoSel;	
	int filasMostrar = 1;
	String nombre, tipo, estilo, clase, accion, ancho, pestana;
	String sqlplano="";
	Object parametrosIn[];
	String parametros[];
	boolean obligatorio = false, seleccionMultiple = false, hijo = false, obligatorioSinTextoSeleccionar = false;
	boolean readonly = false;

	public void setNombre		(String dato) 	{ this.nombre 	= dato;	}
	public void setTipo			(String dato) 	{ this.tipo 	= dato;	}
	public void setClase		(String dato) 	{ this.clase	= dato;	}
	public void setEstilo		(String dato) 	{ this.estilo	= dato;	}
	public void setAncho		(String dato) 	{ 
		if (dato!=null){
			int i=-1;
			try {i=Integer.parseInt(dato);} catch (NumberFormatException e) {}
			this.ancho =(i<50?null:Integer.toString(i)); 
		}
	}
	public void setPestana		(String dato) 	{ this.pestana	= dato;	}
	public void setSqlplano		(String dato) 	{ this.sqlplano	= dato;	}
	public void setParametrosIn		(Object[] dato) 	{ this.parametrosIn	= dato;	}
	
	public void setObligatorio	(String dato) 	{ this.obligatorio = UtilidadesString.stringToBoolean(dato); }
	public void setObligatorioSinTextoSeleccionar (String dato) {
		this.obligatorioSinTextoSeleccionar = UtilidadesString.stringToBoolean(dato);
		if (this.obligatorioSinTextoSeleccionar){
		    this.obligatorio = true;
		}
	}
	public void setReadonly	(String dato) 	{ 
		this.readonly = UtilidadesString.stringToBoolean(dato); 
	}
	
	// Cambio a ArrayList
	public void setElementoSel (ArrayList datos)	{ 
		porFilas = 0;
		this.elementoSel = datos;		
	}
	public void setElementoSelstring (String datos)			{ 		
		ArrayList a = new ArrayList();
		String[] arrayDatos = datos.split(",");
		for (String dato:arrayDatos){
			Integer ent = new Integer(dato);
			a.add(ent);
		}
		porFilas = 0;		
		this.elementoSel = a;
	}
	
	public void setElementoSel (int datos)			{ 
		ArrayList a = new ArrayList();
		Integer ent = new Integer(datos);
		a.add(ent.toString());
		porFilas = 1;		
		this.elementoSel = a;
	}
	
	public void setAccion (String dato)			 	{ this.accion = dato;	}
	public void setFilasMostrar (int dato)			{ this.filasMostrar	= dato;	}
	
	public void setSeleccionMultiple (String dato) 	{ this.seleccionMultiple = UtilidadesString.stringToBoolean(dato); }

	public void setParametro (String dato[])		{ this.parametros = dato;	}
	public void setHijo		 (String dato)			{ this.hijo = UtilidadesString.stringToBoolean(dato); }

	public int doStartTag() {		return EVAL_BODY_INCLUDE;	 	 	}
	
	public int doEndTag() {

		try {
			int rc = 0;
			Hashtable codigos = new Hashtable();
			int contador=1;
			Vector almacenBD = new Vector();
		    ReadProperties p= new ReadProperties(SIGAReferences.RESOURCE_FILES.QUERY);
//			ReadProperties p = new ReadProperties ("Combo.properties");
			String consultaSQL = p.returnProperty(this.tipo, true);
			if (consultaSQL == null) {
				rc = this.getTipoIncorrecto (almacenBD);
			}
			else {

				if (this.sqlplano.equals("")) {

					consultaSQL = UtilidadesString.ReemplazaIdioma(consultaSQL, pageContext.getSession(), parametroIdioma);
					StringTokenizer st = new StringTokenizer(consultaSQL, "@");
					//int i=1;
//					st.nextToken();
					int iParametro=0;
					int iParametroInt=0;
					while (st.hasMoreElements())
					{
						String elem=st.nextToken();
						if(elem.equals("parametro")){
							if (this.parametros != null && this.parametros.length > 0 && this.parametros.length>iParametro && this.parametros[iParametro] != null && !this.parametros[iParametro].equals("")&& !this.parametros[iParametro].equals("null") ){
								Vector v2 = ReemplazaParametrosBind(parametroWhere,consultaSQL, parametros[iParametro],codigos,contador);
								iParametro++;
								consultaSQL = (String) v2.get(0);
								codigos = (Hashtable) v2.get(1);
								contador = ((Integer) v2.get(2)).intValue();
							}	

						}else if(elem.equals("parametroIn")){
							if (this.parametrosIn != null && this.parametrosIn.length > 0 && this.parametrosIn.length>iParametroInt && this.parametrosIn[iParametroInt] != null && !this.parametrosIn[iParametroInt].equals("")&& !this.parametrosIn[iParametroInt].equals("null") ) {
//								consultaSQL = ReemplazaParametrosIn(parametroIn,consultaSQL, this.parametrosIn);
								Vector v2 = ReemplazaParametrosBindIn(parametroIn,consultaSQL, (Object[]) this.parametrosIn[iParametroInt],codigos,contador);
								iParametroInt++;
								if(v2.size()>1){
									consultaSQL = (String) v2.get(0);
									codigos = (Hashtable) v2.get(1);
									contador = ((Integer) v2.get(2)).intValue();
								}

							}
						}

						///i++;

					}



//					if (this.parametros != null)
//					if (this.parametros.length > 0)
//					if (this.parametros[0] != null)
//					if (!this.parametros[0].equals("")) {
//					Vector v2 = ReemplazaParametrosBind(parametroWhere,consultaSQL, this.parametros,codigos,contador);
//					consultaSQL = (String) v2.get(0);
//					codigos = (Hashtable) v2.get(1);
//					contador = ((Integer) v2.get(2)).intValue();
//					}
//					if (this.parametrosIn != null)
//					if (this.parametrosIn.length > 0)
//					if (this.parametrosIn[0] != null)
//					if (!this.parametrosIn[0].equals("")) {
////					consultaSQL = ReemplazaParametrosIn(parametroIn,consultaSQL, this.parametrosIn);
//					Vector v2 = ReemplazaParametrosBindIn(parametroIn,consultaSQL, this.parametrosIn,codigos,contador);
//					if(v2.size()>1){
//					consultaSQL = (String) v2.get(0);
//					codigos = (Hashtable) v2.get(1);
//					contador = ((Integer) v2.get(2)).intValue();
//					}

//					}

					Hashtable nuevosCodigos = new Hashtable();
					for (int j=1;!codigos.isEmpty() && j<contador;j++) {
						nuevosCodigos.put(new Integer(j),codigos.get(new Integer(j)));
					}

					if (consultaSQL.indexOf(parametroWhere) > -1 ||consultaSQL.indexOf(parametroIn)> -1)
					{
						rc = this.getNoData(almacenBD);
					}
					else
					{
						rc = getDatosConsultaBind(almacenBD, consultaSQL,nuevosCodigos);
					}
				} else {
					// este es el caso como funcionaba antes
					// Se debe utilizar para cuando los parametros a pasar no son valores
					// sino que son concatenaciones a la consulta (in listavalores, concatenaciones)
					consultaSQL = UtilidadesString.ReemplazaIdioma(consultaSQL, pageContext.getSession(), parametroIdioma);

					if (this.parametros != null && this.parametros.length > 0 && this.parametros[0] != null && !this.parametros[0].equals("")){

						consultaSQL = UtilidadesString.ReemplazaParametros(parametroWhere,consultaSQL, this.parametros);
					}
					if (this.parametrosIn != null && this.parametrosIn.length > 0 && this.parametrosIn[0] != null&& !this.parametrosIn[0].equals("")) {
						consultaSQL = ReemplazaParametrosIn(parametroIn,consultaSQL, this.parametrosIn);
					}

					if (consultaSQL.indexOf(parametroWhere) > -1 ||consultaSQL.indexOf(parametroIn) > -1)
					{
						rc = this.getNoData(almacenBD);
					}
					else
					{
						rc = getDatosConsulta (almacenBD, consultaSQL);
					}			        
				}
			}
			pintaSelect (almacenBD); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return EVAL_PAGE; 			// continua la ejecucion de la pagina
	}
	
	private int getTipoIncorrecto (Vector datos) {
		datos.add(new ParejaNombreID ("1", "Tipo de datos incorrecto"));
//		Cambio para ArrayList
		this.elementoSel.clear();
		//this.elementoSel.add(new Integer(1).toString());
		this.elementoSel.add("1");
//		this.elementoSel=1;
		return TagComboBD.OK;
	}

	private int getNoData (Vector datos)
	{
	    //datos.add(new ParejaNombreID ("1", "No data BD"));
	    //this.elementoSel.clear();
	    //this.elementoSel.add("1");
	    
	    // RGG 04-03-2005 que no salga estodatos.add(new ParejaNombreID (""+this.elementoSel, "No data BD"));
	    
	    return TagComboBD.OK;
	}
	
	private void pintaSelect (Vector datos) throws Exception 
	{		
		pageContext.getResponse().setContentType("text/html");

		HttpSession session = pageContext.getSession();
		UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);


		try {
			PrintWriter out = pageContext.getResponse().getWriter();
			if (this.hijo) {
				HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
				String pathAplicacion = request.getContextPath();
				String width=this.getAnchoIframe();
				int altura = 23;
				altura += ((new Integer(this.filasMostrar).intValue()-1)*17); 

				out.println("<script>");
				out.println("function eee" + this.nombre + "(aa) {");
				
				///////////////////////////////////////////////////////////////
				// Si estamos en una version anterior a la IE7 no hacemos nada
				out.println("   if (!window.XMLHttpRequest) {");  
				out.println("     return;");
				out.println("   }");
				///////////////////////////////////////////////////////////////
				
				out.println("	try { ");
				out.println("	var pps=aa.contentWindow.document.getElementsByTagName('select');");
				out.println("   for (i=0;i<pps.length;i++) {");
				out.println("		pps[i].focus();");
				out.println("	}");
				out.println("	} catch (ex) {} ");
				out.println("}");
				out.println("</script>");

				out.println("<iframe  ID=\"" + this.nombre + "Frame\" "+
								   " SRC=\"" + pathAplicacion + "/html/jsp/general/comboAnidado.jsp?" +
											"nombre=" + ((this.nombre == null)?"":this.nombre) +
											"&tipo=" + ((this.tipo == null)?"":this.tipo) + 
											"&clase=" + ((this.clase == null)?"":this.clase) + 
											"&estilo=width:"+width+"px;"+((this.estilo == null)?"":this.estilo) + 
											"&ancho=" + this.ancho + 
											"&obligatorio=" + this.obligatorio + 
											"&elementoSel=" + this.elementoSel +
											"&seleccionMultiple=" + this.seleccionMultiple +
											"&parametros=" + this.arrayStringToString(this.parametros) + 
											"&parametrosIn=" + this.arrayObjectToString(this.parametrosIn) +
											"&accion=" + ((this.accion == null)?"":this.accion) +
											"&filasMostrar=" + this.filasMostrar + 
											"&obligatorioSinTextoSeleccionar=" + this.obligatorioSinTextoSeleccionar +
											//"&readonly=" + ((this.readonly)?"":""+this.readonly) +     
											"&readonly=" + ((this.readonly)?"true":"false") +
											"&pestana=" + ((this.pestana == null)?"":this.pestana) + "\""+
						           " WIDTH=\"" + width + "\" "+
						           " HEIGHT=\""+altura+"\" "+
								   " FRAMEBORDER=\"0\" "+
								   " MARGINWIDTH=\"2\" "+
								   " MARGINHEIGHT=\"1\" "+
								   " SCROLLING=\"NO\">");
				out.println("</iframe>");
//				out.println("<input type=\"hidden\" id=\""+ this.nombre +"\" name=\"" + this.nombre + "\">");
				out.println("<input type=\"hidden\" id=\"" + this.nombre + "\" name=\"" + this.nombre + "\" value=\"\">");
				return;
			}
			if (readonly)
			{
				// caso de readonly (solo etiqueta)
				String aux = ""; 
				boolean varios = false;
				for (int i = 0; i < datos.size(); i++) {
					ParejaNombreID dato =  (ParejaNombreID) datos.get(i);
//					Seleccion por id					
					if (this.porFilas==0) {
						boolean bEsta = false;
						if (elementoSel.contains(dato.getIdNombre())) {
							bEsta = true;
						}
						else {
							////////////////////////
							// Ponemos esto pq lo necesitamos cuando es un combo hijo y el id a seleccionar esta separado por comas 
							// Ejem id: 2046, 5, 6, 2 --> Deberia ser '2046,5,6,2'
							try {
								String dede = elementoSel.toString();
								dede = dede.substring(0, dede.length()-1).substring(1).replaceAll(" ", "");
								if (dede.equalsIgnoreCase(dato.getIdNombre())) {
									bEsta = true;
								}
							} catch (Exception e) {	}
						}
						////////////////////////
						
						if (bEsta) {
							if (varios) {
								aux += "<br>";
							}
							aux += " <input type=\"hidden\"  name=\"" + this.nombre + "\" value=\""+dato.getIdNombre()+"\"/>";
							aux += " <input type=\"text\" readonly class=\""+this.clase+"\" value=\""+UtilidadesString.getMensajeIdiomaCombo(usrbean,dato.getNombre())+"\" style=\"width:" + this.ancho + "px;\"/>";
							varios = true;
						}
					}	
					
//					Seleccion por posicion					
					if (this.porFilas==1) {		
						Integer ent = new Integer(i);
						if (elementoSel.contains(ent.toString())) {
							aux += " <input type=\"text\" readonly class=\""+this.clase+"\" value=\""+UtilidadesString.getMensajeIdiomaCombo(usrbean,dato.getNombre())+"\"  style=\"width:"+ancho+"px;\" />";
							// aqui no pongo el hidden ni el name en el anterior
							break;
						}
					}
				}
				if (aux.trim().equals("")) {
					aux += " &nbsp;";
				}
				out.print(aux);
				
			} else {

				String widthStyle=(this.ancho!=null?"width:"+ancho+"px;":"");
				String aux = "";
				aux = "<select id=\"" + this.nombre + "\" name=\"" + this.nombre + "\" style=\"" + widthStyle + ((this.estilo == null)?"":this.estilo) +"\" class=\""+ this.clase + "\" " +
		           		(this.seleccionMultiple==true?"multiple ":"") + (this.filasMostrar==1?"":"size=\"" + this.filasMostrar +"\" ") +
						" onChange=\"";
				
				if (accion!=null)
				{
					String sAccionAux = this.accion.trim();
					String sAccionAux2 = "";
					
					if (sAccionAux.indexOf(";")>-1)
					{
					    sAccionAux2=sAccionAux.substring(sAccionAux.indexOf(";")+1);
					    sAccionAux=sAccionAux.substring(0, sAccionAux.indexOf(";")+1);
					}
					sAccionAux = sAccionAux.replaceAll("  ", " ");
					sAccionAux = sAccionAux.replaceAll("  ", " ");
					sAccionAux = sAccionAux.replaceAll(" ,", ",");
					
					if (this.accion.trim().toLowerCase().startsWith("hijo:"))
					{
					    int contador=0;
					    
						while (sAccionAux.toLowerCase().indexOf("hijo:")>-1)
						{
							int iPosFinal = sAccionAux.indexOf(",");
							
							if (iPosFinal==-1)
							{
							    iPosFinal = sAccionAux.indexOf(";");
							}
		
							//aux += "\r\nvar destino_" + this.nombre + contador + "=window.parent.document.all." + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame.src;\r\n";
							if (this.pestana!=null){
								if(UtilidadesString.stringToBoolean(this.pestana)){
									aux += "\r\n var destino_" + this.nombre + contador + "=window.top.frames[0].document.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src;\r\n";
								
								}else if(isNumber(this.pestana)){
									String formulario = ".document.frames[0]";
									int nivel = Integer.parseInt(this.pestana);
									for(int i=0; i < nivel-1; i++){
										formulario = formulario + ".document.frames[0]";
									}
									aux += "\r\n var destino_" + this.nombre + contador + "=window.top.frames[0]"+formulario+".document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src;\r\n";

								}else{
									aux += "\r\nvar destino_" + this.nombre + contador + "='';";
									aux += "if(window.top.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame')){";
									aux += "\r\n destino_" + this.nombre + contador + "=window.top.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src;}\r\n";
								}
							}else{
								aux += "\r\nvar destino_" + this.nombre + contador + "='';";
								aux += "if(window.top.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame')){";
								aux += "\r\n destino_" + this.nombre + contador + "=window.top.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src;}\r\n";
							}
							
							aux += "var tam_" + this.nombre + contador + " = destino_" + this.nombre + contador + ".indexOf('&id=');\r\n";
							aux += "if(tam_" + this.nombre + contador + "==-1)";
							aux += "\r\n{\r\n";
							aux += "	tam_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".length;\r\n";
							aux += "}\r\n";
							aux += "destino_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".substring(0,tam_" + this.nombre + contador + ")+'&id='+" + this.nombre+".value;\r\n";
							//aux += "window.parent.document.all." + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame.src=destino_" + this.nombre + contador + ";\r\n";
							
							if (this.pestana!=null){
								//aalg: INC_10700_SIGA
								if (UtilidadesString.stringToBoolean(this.pestana)){
									aux += "window.top.frames[0].document.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src=destino_" + this.nombre + contador + ";\r\n";
								}else if(isNumber(this.pestana)){
									String formulario = ".document.frames[0]";
									int nivel = Integer.parseInt(this.pestana);
									for(int i=0; i < nivel-1; i++){
										formulario = formulario + ".document.frames[0]";
									}
									aux += "window.top.frames[0]"+formulario+".document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src=destino_" + this.nombre + contador + ";\r\n";
								}else{
									aux += "if(window.top.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame')){  window.top.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src=destino_" + this.nombre + contador + ";}\r\n";
								}
							}else{
								aux += "if(window.top.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame')){ window.top.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src=destino_" + this.nombre + contador + ";}\r\n";
							}
		
							/*aux += "var destino_" + this.nombre + contador + "=document.all." + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame.src;";
							aux += "var tam_" + this.nombre + contador + " = destino_" + this.nombre + contador + ".indexOf('&id=');if(tam_" + this.nombre + contador + "==-1){tam_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".length;}";
							aux += "destino_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".substring(0,tam_" + this.nombre + contador + ")+'&id='+" + this.nombre+".value;";
							aux += "document.all." + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame.src=destino_" + this.nombre + contador + ";";*/
							
							sAccionAux = iPosFinal==-1 ? "" : sAccionAux.substring(iPosFinal+1);
							sAccionAux = sAccionAux.trim();
		
							contador++;
						}

						
						// ========================================

					    while (sAccionAux2.toLowerCase().indexOf("hijo:")>-1)
						{
							int iPosFinal = sAccionAux2.indexOf(",");
							
							if (iPosFinal==-1)
							{
							    iPosFinal = sAccionAux2.indexOf(";");
							}
		
							//aux += "\r\nvar destino_" + this.nombre + contador + "=window.parent.document.all." + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame.src;\r\n";
							if (this.pestana!=null){
								if(UtilidadesString.stringToBoolean(this.pestana)){
									aux += "\r\n var destino_" + this.nombre + contador + "=window.top.frames[0].document.frames[0].document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame').src;\r\n";
								}else if(isNumber(this.pestana)){
									String formulario = ".document.frames[0]";
									int nivel = Integer.parseInt(this.pestana);
									for(int i=0; i < nivel-1; i++){
										formulario = formulario + ".document.frames[0]";
									}
									aux += "\r\n var destino_" + this.nombre + contador + "=window.top.frames[0]"+formulario+".document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame').src;\r\n";
								}else{
									aux += "\r\nvar destino_" + this.nombre + contador + "='';";
									aux += "if(window.top.frames[0].document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame')){";
									aux += "\r\n destino_" + this.nombre + contador + "=window.top.frames[0].document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame').src;}\r\n";
								}
							}else{
								aux += "\r\nvar destino_" + this.nombre + contador + "='';";
								aux += "if(window.top.frames[0].document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame')){";
								aux += "\r\n destino_" + this.nombre + contador + "=window.top.frames[0].document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame').src;}\r\n";
							}
							
							aux += "var tam_" + this.nombre + contador + " = destino_" + this.nombre + contador + ".indexOf('&id=');\r\n";
							aux += "if(tam_" + this.nombre + contador + "==-1)";
							aux += "\r\n{\r\n";
							aux += "	tam_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".length;\r\n";
							aux += "}\r\n";
							aux += "destino_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".substring(0,tam_" + this.nombre + contador + ")+'&id='+" + this.nombre+".value;\r\n";
							//aux += "window.parent.document.all." + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame.src=destino_" + this.nombre + contador + ";\r\n";
							
							if (this.pestana!=null){
								//aalg: INC_10700_SIGA
								if(UtilidadesString.stringToBoolean(this.pestana)){
									aux += "window.top.frames[0].document.frames[0].document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame').src=destino_" + this.nombre + contador + ";\r\n";
								}else if(isNumber(this.pestana)){
									String formulario = ".document.frames[0]";
									int nivel = Integer.parseInt(this.pestana);
									for(int i=0; i < nivel-1; i++){
										formulario = formulario + ".document.frames[0]";
									}
									aux += "window.top.frames[0]"+formulario+".document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame').src=destino_" + this.nombre + contador + ";\r\n";								
								}else{
									aux += "if(window.top.frames[0].document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame')){ window.top.frames[0].document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame').src=destino_" + this.nombre + contador + ";}\r\n";
								}
							}else{
								aux += "if(window.top.frames[0].document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame')){ window.top.frames[0].document.getElementById('" + sAccionAux2.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux2.length()) + "Frame').src=destino_" + this.nombre + contador + ";}\r\n";
							}
		
							/*aux += "var destino_" + this.nombre + contador + "=document.all." + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame.src;";
							aux += "var tam_" + this.nombre + contador + " = destino_" + this.nombre + contador + ".indexOf('&id=');if(tam_" + this.nombre + contador + "==-1){tam_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".length;}";
							aux += "destino_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".substring(0,tam_" + this.nombre + contador + ")+'&id='+" + this.nombre+".value;";
							aux += "document.all." + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame.src=destino_" + this.nombre + contador + ";";*/
							
							sAccionAux2 = iPosFinal==-1 ? "" : sAccionAux2.substring(iPosFinal+1);
							sAccionAux2 = sAccionAux2.trim();
		
							contador++;
						}

						
						// ??????????????????????????????????????????
						
						aux += sAccionAux2;
					}
		/*			if (this.accion.trim().toLowerCase().startsWith("hijo:")) {
		//				aux += "document.all." + this.accion.trim().toLowerCase().substring(5) + "Frame.src=" + "document.all." + this.hijo + "Frame.src+'?id='+" + this.nombre + ".value";
						aux += "var destino=document.all." + this.accion.trim().substring(5) + "Frame.src;";
						aux += "var tam = destino.indexOf('&id=');if(tam==-1){tam= destino.length;}";
						aux += "destino=destino.substring(0,tam)+'&id='+" + this.nombre+".value;";
						aux += "document.all." + this.accion.trim().substring(5) + "Frame.src=destino;";
					}
		*/
					else {
						aux += this.accion;
					}
				}
				
				aux += "\" >";
				out.print(aux);
				if (datos.size() < 1) {
					out.print("<option value=\"\" selected ></option>");
					// RGG 04-03-2005 out.print("<option value=\"\">No data BD</option>");
				}
				else {
					// Si el campo es obligatorio introducimos un texto
					if (!this.obligatorio) {
						datos.insertElementAt(new ParejaNombreID ("", " "), 0);
					}
					else {
						if (!this.obligatorioSinTextoSeleccionar)
							datos.insertElementAt(new ParejaNombreID ("", UtilidadesString.getMensajeIdiomaCombo(usrbean, "general.combo.seleccionar")), 0);
					}
					String grupoAnt="";
					boolean primeraVez =true;
					for (int i = 0; i < datos.size(); i++) {
						ParejaNombreID dato =  (ParejaNombreID) datos.get(i);
						
						// RGG cambio paa OPTGROUP
						String nombreCompleto = dato.getNombre();
						String grupo = "";
						int pos1=nombreCompleto.indexOf("[[");
						if (pos1!=-1) {
							grupo = nombreCompleto.substring(pos1+2,nombreCompleto.indexOf("]]"));
							nombreCompleto = nombreCompleto.substring(0,pos1);
						}
						if (!grupo.equals("") && !grupoAnt.equals(grupo)) {
							if (!primeraVez) {
								out.print("</optgroup>");
							}
							primeraVez=false;
							out.print("<optgroup label=\""+grupo+"\">");
							grupoAnt = grupo;
						}
						String  option = "<option value=\"" + dato.getIdNombre() + "\"";
	//					Seleccion por id					
						if (this.porFilas==0) {					
							if (elementoSel.contains(dato.getIdNombre())) {
								option += " selected ";
							}
							////////////////////////
							// Ponemos esto pq lo necesitamos cuando es un combo hijo y el id a seleccionar esta separado por comas 
							// Ejem id: 2046, 5, 6, 2 --> Deberia ser '2046,5,6,2'
							try {
								String dede = elementoSel.toString();
								dede = dede.substring(0, dede.length()-1).substring(1).replaceAll(" ", "");
								if (dede.equalsIgnoreCase(dato.getIdNombre())) {
									option += " selected ";
								}
							} catch (Exception e) {	}
							////////////////////////
						}	
	//					Seleccion por posicion					
						if (this.porFilas==1) {		
							Integer ent = new Integer(i);
							if (elementoSel.contains(ent.toString())) {
								option += " selected ";
							}	
						}
						option += " >" + UtilidadesString.getMensajeIdiomaCombo(usrbean,nombreCompleto) + "</option>";
						out.print(option);
						
						grupoAnt=grupo;

						
					}
					if (!grupoAnt.equals("")) {
						out.print("</optgroup>");
					}
				}
				out.print("</Select>");
			}
		}
		catch (Exception e)
		{
			throw e;
		}
	}
	
	private String getAnchoIframe()
	{
//	    return ancho==null || ancho.trim().equals("") ? "265" : ancho;
// RGG	    if (ancho==null || ancho.trim().equals("")) return "265"; else return ancho;
	    if (ancho==null || ancho.trim().equals("")) return "165"; else return ancho;
	}

	
	private int getDatosConsulta (Vector datos, String consultaSQL) throws Exception
	{
		if (datos == null) 
			datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();
			if (rc.queryNLS(consultaSQL)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					ParejaNombreID dato = new ParejaNombreID();
					dato.setIdNombre((String)fila.getRow().get("ID"));
					dato.setNombre((String)fila.getRow().get("DESCRIPCION"));
					datos.add(dato);
				}
			}
		} 
		catch (Exception e) { 	
			ParejaNombreID dato = new ParejaNombreID();
			dato.setIdNombre("1");
			dato.setNombre("Error B.D.");
			//dato.setNombre("");
			datos.add(dato);
		}
		
		return TagComboBD.OK;
	}
	
	private int getDatosConsultaBind (Vector datos, String consultaSQL, Hashtable codigos) throws Exception
	{
		if (datos == null) 
			datos = new Vector();
		
		// Acceso a BBDD
		RowsContainer rc = null;
		try { 
			rc = new RowsContainer();
			if (rc.queryNLSBind(consultaSQL,codigos)) {
				for (int i = 0; i < rc.size(); i++)	{
					Row fila = (Row) rc.get(i);
					ParejaNombreID dato = new ParejaNombreID();
					dato.setIdNombre((String)fila.getRow().get("ID"));
					dato.setNombre((String)fila.getRow().get("DESCRIPCION"));
					datos.add(dato);
				}
			}
		} 
		catch (Exception e) { 	
			ParejaNombreID dato = new ParejaNombreID();
			dato.setIdNombre("1");
			dato.setNombre("Error B.D.");
			//dato.setNombre("");
			datos.add(dato);
		}
		
		return TagComboBD.OK;
	}
		
	private String ReemplazaParametrosIn (String nombreParametro,String consulta, Object dato[]) 
	{
		StringBuffer cadena = new StringBuffer();
		boolean isString = false;
		for (int i = 0; i < dato.length; i++) {
			Object iDato = dato[i];

			if (iDato instanceof String) {
				String strDato = (String) iDato;
				isString =  true;
				if ((dato[i] == null) || (strDato.trim().equalsIgnoreCase(""))){
					break;
				}
				cadena.append(strDato+",");
			}else if (iDato instanceof String[]) {
				String[] aDato = (String[]) iDato;
				cadena = new StringBuffer();
				for (int j = 0; j < aDato.length; j++) {
					String iaDato = aDato[j];
					cadena.append(iaDato+",");
				}
				consulta = consulta.replaceFirst("(?i)"+nombreParametro, cadena.substring(0,cadena.length()-1));
			}

		}
		
		if(isString)
			consulta = consulta.replaceFirst("(?i)"+nombreParametro, cadena.substring(0,cadena.length()-1));
		return consulta;


	}
	
	
	private Vector ReemplazaParametrosBind (String nombreParametro,String consulta, String dato[],Hashtable codigos, int contador) 
	{
	    Vector salida = new Vector();
		for (int i = 0; i < dato.length; i++) {
			if ((dato[i] != null) && (dato[i].trim().equalsIgnoreCase("null"))){
				break;
			}
			
			if ((dato[i] == null) || (dato[i].trim().equalsIgnoreCase(""))){
				break;
			}
			if (consulta.indexOf(nombreParametro.toUpperCase())<0 && consulta.indexOf(nombreParametro.toLowerCase())<0) {
				break;
			}
			//consulta = consulta.replaceFirst("(?i)"+parametroWhere, dato[i]);
			consulta = consulta.replaceFirst("(?i)"+nombreParametro, ":"+contador);
			codigos.put(new Integer(contador),dato[i]);
			contador++;
		}
		salida.add(consulta);
		salida.add(codigos);
		salida.add(new Integer(contador-1));
		return salida;

//		if (consulta.indexOf(TagComboBD.parametroWhere) > 0) 
//			return TagComboBD.ERROR;
//		return TagComboBD.OK;
	}
	private Vector ReemplazaParametrosBind (String nombreParametro,String consulta, String parametro,Hashtable codigos, int contador) 
	{
	    Vector salida = new Vector();
		
			
			//consulta = consulta.replaceFirst("(?i)"+parametroWhere, dato[i]);
			consulta = consulta.replaceFirst("(?i)"+nombreParametro, ":"+contador);
			codigos.put(new Integer(contador),parametro);
			contador++;
		
		salida.add(consulta);
		salida.add(codigos);
		salida.add(new Integer(contador));
		return salida;

//		if (consulta.indexOf(TagComboBD.parametroWhere) > 0) 
//			return TagComboBD.ERROR;
//		return TagComboBD.OK;
	}
	
//	private Vector ReemplazaParametrosBindIn (String nombreParametro,String consulta, String dato[],Hashtable codigos, int contador) 
//	{
//	    Vector salida = new Vector();
//	    StringBuffer cadena = new StringBuffer();
//	    boolean findIt = false;
//	    
//		for (int i = 0; i < dato.length; i++) {
//			if ((dato[i] != null) && (dato[i].trim().equalsIgnoreCase("null"))){
//				break;
//			}
//			
//			if ((dato[i] == null) || (dato[i].trim().equalsIgnoreCase(""))){
//				break;
//			}
//			if (consulta.toUpperCase().indexOf(nombreParametro.toUpperCase())<0 ) {
//				break;
//			}
//			findIt = true;
//			contador++;
//			cadena.append(":"+contador+",");
//			codigos.put(new Integer(contador),dato[i]);
//			
//			
//		}
//		if(findIt){
//			consulta = consulta.replaceFirst("(?i)"+nombreParametro,cadena.substring(0,cadena.length()-1) );
//			salida.add(consulta);
//			salida.add(codigos);
//			salida.add(new Integer(contador));
//		}
//		return salida;
//
//
//	}
	private Vector ReemplazaParametrosBindIn (String nombreParametro,String consulta, Object iDato,Hashtable codigos, int contador) 
	{
		Vector salida = new Vector();
		StringBuffer cadena = new StringBuffer();
		String[] aDato = (String[]) iDato;

		if (consulta.toUpperCase().indexOf(nombreParametro.toUpperCase())<0 ) {
			return salida;
		}

		for (int j = 0; j < aDato.length; j++) {
			String iaDato = aDato[j];

			
			cadena.append(":"+contador+",");
			codigos.put(new Integer(contador),iaDato);
			contador++;
		}

		consulta = consulta.replaceFirst("(?i)"+nombreParametro,cadena.substring(0,cadena.length()-1) );
		salida.add(0,consulta);
		salida.add(1,codigos);
		salida.add(2,new Integer(contador));

		return salida;
	}
	
	
	
	
	private Vector ReemplazaIdiomaBind (String consulta, String dato[],Hashtable codigos, int contador) 
	{
	    Vector salida = new Vector();
	    String idioma = "";
		HttpSession session = pageContext.getSession();
		UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
		try {
			idioma = "" + usrbean.getLanguage();
		}
		catch (Exception e) {
			idioma = "1"; // Por defecto español para la pantalla inicial de los 3 combos (es temporal)
		}
		boolean existe=(consulta.indexOf(parametroIdioma.toLowerCase())!=-1 || consulta.indexOf(parametroIdioma.toUpperCase())!=-1);
		
		while (existe) {
			consulta = consulta.replaceFirst("(?i)"+parametroIdioma, ":"+contador);
			codigos.put(new Integer(contador),idioma);
			contador++;
			existe=(consulta.indexOf(parametroIdioma.toLowerCase())!=-1 || consulta.indexOf(parametroIdioma.toUpperCase())!=-1);
		}
		salida.add(consulta);
		salida.add(codigos);
		salida.add(new Integer(contador));
		return salida;

//		if (consulta.indexOf(TagComboBD.parametroWhere) > 0) 
//			return TagComboBD.ERROR;
//		return TagComboBD.OK;
	}
	
	
	
/*	Vector getCamposConsulta(String consultaSQL) {
		Vector campos = new Vector();
		try {
			consultaSQL = consultaSQL.toLowerCase();
			StringTokenizer datos = new StringTokenizer(consultaSQL, "select");
			String tipo = datos.nextToken();
			tipo = tipo.trim();
			datos = new StringTokenizer(tipo, "from");
			tipo = datos.nextToken();
			tipo = tipo.trim();
			datos = new StringTokenizer(tipo, ",");
			for (int i = 0; datos.hasMoreElements(); i++) {
				String campo = datos.nextToken();
				campo = campo.trim();
				campos.add(campo);
			}
		}
		catch (Exception e) {
			return null;
		}
		
		return campos;
	}
*/
	
	private String arrayStringToString (String a[]) {
		String rc = "";
		if (a == null) return rc;
		for (int i = 0; i < a.length; i++) {
		
				rc += a[i];
			
			if ((i+1) < a.length) rc += ","; 
		}
		return rc;
	}
	private String arrayObjectToString (Object a[]) {
		if (a == null) return "";
		StringBuffer rc = new StringBuffer("");
		
		
		for (int i = 0; i < a.length; i++) {
			String[] s = (String[]) a[i];
			//rc.append("[");
			for (int j = 0; j < s.length; j++) {
				String string = s[j];
				rc.append(string);
				if(j!=s.length-1)
					rc.append(",");
			}
			
			//rc.append("]");
			
			
		
			if(i!=a.length-1) 
				rc.append(";");
		}
		return rc.toString();
	}
	
	private boolean isNumber(String in){
		try {
            Integer.parseInt(in);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
	}
}