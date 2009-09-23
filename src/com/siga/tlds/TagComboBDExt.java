/*
 * Created on 16-sep-2004
 */
package com.siga.tlds;

import java.io.*;
import java.util.*;

import com.atos.utils.*;

import javax.servlet.http.*;
import com.siga.Utilidades.*;

import javax.servlet.jsp.tagext.*;

/**
 * Este combo solamente se utiliza para casos de combos anidados donde el combo hijo
 * es de selección multiple. Con este tag no funciona el tercer nivel de anidamiento.
 * @author daniel.campos
 */

public class TagComboBDExt extends TagSupport {
	
	// *****************************************************
	public static class ParejaNombreID implements Serializable {

		// Atributos
		private String nombre, idNombre;
		
		// Constructor
		ParejaNombreID () {
			this.idNombre = "";
			this.nombre = "";
		}

		ParejaNombreID (String id, String nombre) {
			this.idNombre = id;
			this.nombre = nombre;
		}

		// Metodos Get
		public String getNombre		() 		{ return this.nombre;	}
		public String getIdNombre	() 		{ return this.idNombre;	}
		
		// Metodos Set
		public void setNombre 	(String s) 	{ this.nombre = s;		}
		public void setIdNombre (String i) 	{ this.idNombre = i;	}
		public void setIdNombre (Integer i) { this.idNombre = i.toString();	}
	}
	// *****************************************************

	private static int OK = 1;
	private static int ERROR = -1;
	private static String parametroWhere = "@parametro@";
	
	// *****************************************************
	
//	Cambio para ArrayList -> por id
	ArrayList elementoSel = new ArrayList();
//	Cambio para Int -> por filas	
	int porFilas = 0;	
//	int elementoSel;	
	int filasMostrar = 1;
	String nombre, tipo, estilo, clase, accion, ancho, pestana;
	String sqlplano="";
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
	
	public void setObligatorio	(String dato) 	{ this.obligatorio = UtilidadesString.stringToBoolean(dato); }
	public void setObligatorioSinTextoSeleccionar (String dato) {
		this.obligatorio = true;
		this.obligatorioSinTextoSeleccionar = UtilidadesString.stringToBoolean(dato);
	}
	public void setReadonly	(String dato) 	{ 
		this.readonly = UtilidadesString.stringToBoolean(dato); 
	}
	
	// Cambio a ArrayList
	public void setElementoSel (ArrayList datos)	{ 
		porFilas = 0;
		this.elementoSel = datos;		
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
			Vector almacenBD = new Vector();
			int contador = 1;
			ReadProperties p = new ReadProperties ("Combo.properties");
			String consultaSQL = p.returnProperty(this.tipo, true);
			if (consultaSQL == null) {
				rc = this.getTipoIncorrecto (almacenBD);
			}
			else {
			    
			    if (this.sqlplano.equals("")) {
	
					if ((this.parametros != null) && (this.parametros.length > 0) && (!this.parametros[0].equals(""))) {
						Vector v2 = ReemplazaParametrosBind(consultaSQL, this.parametros,codigos,contador);
					    consultaSQL = (String) v2.get(0);
					    codigos = (Hashtable) v2.get(1);
					    contador = ((Integer) v2.get(2)).intValue();
					}
		
					Hashtable nuevosCodigos = new Hashtable();
					for (int j=1;!codigos.isEmpty() && j<=contador;j++) {
						nuevosCodigos.put(new Integer(j),codigos.get(new Integer(j)));
					}
								
					if (consultaSQL.indexOf(parametroWhere) > -1)
					{
					    rc = this.getNoData(almacenBD);
					}
					else
					{
					    rc = getDatosConsultaBind(almacenBD, consultaSQL,codigos);
					}
			    } else {
			        // este es el caso como funcionaba antes
			        // Se debe utilizar para cuando los parametros a pasar no son valores
			        // sino que son concatenaciones a la consulta (in listavalores, concatenaciones)
			        if ((this.parametros != null) && (this.parametros.length > 0) && (!this.parametros[0].equals(""))) {
						consultaSQL = ReemplazaParametros(consultaSQL, this.parametros);
					}
					if (consultaSQL.indexOf(parametroWhere) > -1)
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
		return TagComboBDExt.OK;
	}

	private int getNoData (Vector datos)
	{
	    //datos.add(new ParejaNombreID ("1", "No data BD"));
	    //this.elementoSel.clear();
	    //this.elementoSel.add("1");
	    
	    // RGG 04-03-2005 que no salga estodatos.add(new ParejaNombreID (""+this.elementoSel, "No data BD"));
	    
	    return TagComboBDExt.OK;
	}
	
	private void pintaSelect (Vector datos) throws Exception 
	{		
		pageContext.getResponse().setContentType("text/html");
		try {
			PrintWriter out = pageContext.getResponse().getWriter();
			if (this.hijo) {
				HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
				String pathAplicacion = request.getContextPath();
				String width=this.getAnchoIframe();
				int altura = 19;
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
				
				out.println("<iframe onfocus=\"eee" + this.nombre + "(this);\"  ID=\"" + this.nombre + "Frame\" "+
								   " SRC=\"" + pathAplicacion + "/html/jsp/general/comboAnidadoExt.jsp?" +
											"nombre=" + ((this.nombre == null)?"":this.nombre) +
											"&tipo=" + ((this.tipo == null)?"":this.tipo) + 									"&clase=" + ((this.clase == null)?"":this.clase) + 
											"&estilo=width:"+width+"px;"+((this.estilo == null)?"":this.estilo) + 
											"&obligatorio=" + this.obligatorio + 
											"&elementoSel=" + this.elementoSel +
											"&seleccionMultiple=" + this.seleccionMultiple +
											"&parametros=" + this.arrayStringToString(this.parametros) + 
											"&accion=" + ((this.accion == null)?"":this.accion) +
											"&filasMostrar=" + this.filasMostrar + 
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
				out.println("<input type=\"hidden\" name=\"" + this.nombre + "\" value=\"\">");
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
						if (elementoSel.contains(dato.getIdNombre())) {
							if (varios) {
								aux += "<br>";
							}
							aux += " <input type=\"hidden\"  name=\"" + this.nombre + "\" value=\""+dato.getIdNombre()+"\"/>";
							aux += " <input type=\"text\" readonly class=\""+this.clase+"\" value=\""+dato.getNombre()+"\" style=\"width:"+ancho+"px;\"/>";
							varios = true;
						}
					}	
//					Seleccion por posicion					
					if (this.porFilas==1) {		
						Integer ent = new Integer(i);
						if (elementoSel.contains(ent.toString())) {
							aux += " <input type=\"text\" readonly class=\""+this.clase+"\" value=\""+dato.getNombre()+"\"  style=\"width:"+ancho+"px;\" />";
							// aqui no pongo el hidden ni el name en el anterior
							break;
						}	
					}
				}
				out.print(aux);
				
			} else {

				String widthStyle=(this.ancho!=null?"width:"+ancho+"px;":"");
				String aux = "";
				aux = "<Select name = \"" + this.nombre + "\" style = \""+widthStyle+this.estilo +"\" class = \""+ this.clase + "\" " +
		           		(this.seleccionMultiple==true?"multiple ":"") + (this.filasMostrar==1?"":"size = \"" + this.filasMostrar +"\" ") +
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
							if (this.pestana!=null && (this.pestana.equalsIgnoreCase("t") || this.pestana.equalsIgnoreCase("true"))){
								aux += "\r\nvar destino_" + this.nombre + contador + "=top.frames[0].document.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src;\r\n";
							}else{
								aux += "\r\nvar destino_" + this.nombre + contador + "=top.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src;\r\n";
							}
							
							aux += "var tam_" + this.nombre + contador + " = destino_" + this.nombre + contador + ".indexOf('&id=');\r\n";
							aux += "if(tam_" + this.nombre + contador + "==-1)";
							aux += "\r\n{\r\n";
							aux += "	tam_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".length;\r\n";
							aux += "}\r\n";
							aux += "destino_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".substring(0,tam_" + this.nombre + contador + ")+'&id='+" + this.nombre+".value;\r\n";
							//aux += "window.parent.document.all." + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame.src=destino_" + this.nombre + contador + ";\r\n";
							
							if (this.pestana!=null && (this.pestana.equalsIgnoreCase("t") || this.pestana.equalsIgnoreCase("true"))){
								aux += "top.frames[0].document.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src=destino_" + this.nombre + contador + ";\r\n";
							}else{
								aux += "top.frames[0].document.getElementById('" + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame').src=destino_" + this.nombre + contador + ";\r\n";
							}
		
							/*aux += "var destino_" + this.nombre + contador + "=document.all." + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame.src;";
							aux += "var tam_" + this.nombre + contador + " = destino_" + this.nombre + contador + ".indexOf('&id=');if(tam_" + this.nombre + contador + "==-1){tam_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".length;}";
							aux += "destino_" + this.nombre + contador + "=destino_" + this.nombre + contador + ".substring(0,tam_" + this.nombre + contador + ")+'&id='+" + this.nombre+".value;";
							aux += "document.all." + sAccionAux.trim().substring(5, iPosFinal>-1 ? iPosFinal : sAccionAux.length()) + "Frame.src=destino_" + this.nombre + contador + ";";*/
							
							sAccionAux = iPosFinal==-1 ? "" : sAccionAux.substring(iPosFinal+1);
							sAccionAux = sAccionAux.trim();
		
							contador++;
						}
						
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
						HttpSession session = pageContext.getSession();
						UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	
						if (!this.obligatorioSinTextoSeleccionar)
							datos.insertElementAt(new ParejaNombreID ("", UtilidadesString.getMensajeIdioma(usrbean, "general.combo.seleccionar")), 0);
					}
					for (int i = 0; i < datos.size(); i++) {
						ParejaNombreID dato =  (ParejaNombreID) datos.get(i);
						String  option = "<option value=\"" + dato.getIdNombre() + "\"";
	//					Seleccion por id					
						if (this.porFilas==0) {					
							if (elementoSel.contains(dato.getIdNombre())) {
								option += " selected ";
							}
						}	
	//					Seleccion por posicion					
						if (this.porFilas==1) {		
							Integer ent = new Integer(i);
							if (elementoSel.contains(ent.toString())) {
								option += " selected ";
							}	
						}
						option += " >" + dato.getNombre() + "</option>";
						out.print(option);
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
	    if (ancho==null || ancho.trim().equals("")) return "265"; else return ancho;
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
			datos.add(dato);
		}
		
		return TagComboBDExt.OK;
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
			datos.add(dato);
		}
		
		return TagComboBDExt.OK;
	}
	
	private Vector ReemplazaParametrosBind (String consulta, String dato[],Hashtable codigos, int contador) 
	{
	    Vector salida = new Vector();
		for (int i = 0; i < dato.length; i++) {
			if ((dato[i] == null) || (dato[i].trim().equalsIgnoreCase(""))){
				break;
			}
			//consulta = consulta.replaceFirst("(?i)"+parametroWhere, dato[i]);
			consulta = consulta.replaceFirst("(?i)"+parametroWhere, ":"+contador);
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
		
	private String ReemplazaParametros (String consulta, String dato[]) 
	{
		for (int i = 0; i < dato.length; i++) {
			if ((dato[i] == null) || (dato[i].trim().equalsIgnoreCase(""))){
				break;
			}

			consulta = consulta.replaceFirst("(?i)"+parametroWhere, dato[i]);
		}
		return consulta;

//		if (consulta.indexOf(TagComboBDExt.parametroWhere) > 0) 
//			return TagComboBDExt.ERROR;
//		return TagComboBDExt.OK;
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
}