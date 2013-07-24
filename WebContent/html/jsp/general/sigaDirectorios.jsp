<!DOCTYPE html>
<html>
<head>
<!-- infoDirectorio.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>

<%@ page import="com.siga.general.InfoDirectorio"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.*"%>
<%@ page import="java.io.File" %>

<% String app=request.getContextPath(); %>

<% 
		
try {

	Vector vDatos = null;
	ResourceBundle rp=ResourceBundle.getBundle("SIGA");
	String pathBase = rp.getString("facturacion.directorioFisicoPagosBancosJava");
	pathBase = pathBase.substring(0,pathBase.lastIndexOf("/ficheros"));
	

	
 	boolean buscar = false;
 	String path = (String)request.getParameter("path");
 	String patron = (String)request.getParameter("patron");
 	
 	if (path==null) {
		path = (String)request.getSession().getAttribute("path");
		request.getSession().removeAttribute("path");
	}
 	
 	if (patron==null) {
		patron = (String)request.getSession().getAttribute("patron");
		request.getSession().removeAttribute("patron");
	}

	if (path != null && !path.trim().equals("") && !path.trim().equalsIgnoreCase("null")) {
		if (path.startsWith("C:")) {
			// windows
			path = path.substring(2,path.length());
			path = UtilidadesString.replaceAllIgnoreCase(path,"\\","/");
		}
		if (patron!=null && !patron.equalsIgnoreCase("") && path.startsWith(pathBase)){
			vDatos = InfoDirectorio.busqueda(path, patron);
		}else if (path.startsWith(pathBase)){
			vDatos = InfoDirectorio.getInfoDirectorio(path);
		}else{
			path=pathBase;
		}
	} else {
		path=pathBase;
	}
	String pathUP=path.substring(0, path.lastIndexOf("/"));
	
	String accion = "";
	if (request.getParameter("accion")!=null)  {
		accion = (String) request.getParameter("accion");
	}
	if (accion.equals("borrar")) {
		String sRutaFichero = (String)request.getParameter("rutaFichero");
    	File f= new File(sRutaFichero);
    	f.delete();
    	if (sRutaFichero.lastIndexOf("\\")!=-1) {
	    	path = sRutaFichero.substring(0, sRutaFichero.lastIndexOf("\\"));
	    } else if (sRutaFichero.lastIndexOf("/")!=-1) {
	    	path = sRutaFichero.substring(0, sRutaFichero.lastIndexOf("/"));
	    }
    	File aux = new File(path);
//		path = path.substring(0,path.lastIndexOf(aux.getName()));	
		if (path.startsWith("C:")) {
			// windows
			path = path.substring(2,path.length());
			path = UtilidadesString.replaceAllIgnoreCase(path,"\\","/");
			
		}
		buscar=true;
		
	}	
	
%>


<%@page import="com.siga.Utilidades.UtilidadesString"%>

<script type="text/javascript" src="http://code.jquery.com/jquery-1.6.1.min.js"></script>
    <title>SIGA Directorios</title>
    <style>
	body {
		font: normal 9px auto "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
		color: #4f6b72;
		background: #eeeeee;
	}
	
	a {
		color: #c75f3e;
	}
	
	h2 {
		padding: 0 0 5px 0;
		width: 700px;	 
		font: italic 16px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
		text-align: left;
	}


	td {
		border-right: 1px solid #C1DAD7;
		border-bottom: 1px solid #C1DAD7;
		background: #fff;
		font: 11px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;
		padding: 6px 6px 6px 12px;
		color: #4f6b72;
	}
	
	
	td.alt {
		background: #F5FAFA;
		color: #797268;
	}
   
    .dir{color:blue; font: 12px "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;cursor:hand}
    .dirSans{color:blue; font: 13px italic "Trebuchet MS", Verdana, Arial, Helvetica, sans-serif;}

    </style>

<script>
	function fDescargar (fichero, path) 
	{
		document.descargar.nombreFichero.value = fichero;
		document.descargar.rutaFichero.value = unescape(path);
		document.descargar.accion.value = "";
		document.descargar.submit();
	}

	function fSubir (nombreArchivo, rutaArchivo, formulario)
	{	
		var aux = document.getElementById(formulario);
		aux.rutaFichero.value = unescape(rutaArchivo);
		aux.nombreFichero.value = nombreArchivo; 
		aux.accion.value = "subir";
		aux.submit();
	}
	
	function fBorrar (nombreArchivo, rutaArchivo)
	{	
		if (confirm("¿Está seguro de querer borrar el fichero?")) {
			document.borrar.rutaFichero.value = unescape(rutaArchivo);
			document.borrar.nombreFichero.value = nombreArchivo; 
			document.borrar.accion.value = "borrar";
			document.borrar.submit();
		}
	}
	
	function dNavegar (directorio)
	{	
		document.consulta.path.value = unescape(directorio);
		document.consulta.submit();
	}
	
	function mostrarFiltro(){
		jQuery("#filtro").show();
		jQuery("#filtroMSG").show();
		jQuery("#boton").hide();
	}
		
</script>
<script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script>
</head>
<body style="font-family:verdana;" <% if (buscar) out.println("onLoad='document.consulta.submit();'"); %>>
	<h2>SIGADirectorios</h2>
	<table>
		<form name="consulta" action="<%=app%>/html/jsp/general/sigaDirectorios.jsp" method="POST">
			<tr>
				<td>Ruta</td>
				<td><input type="text" size="70"  name="path" value="<%=path%>"/></td>
				<td><input type="submit" id="boton" name="ejecutar" value="Acceder"/></td>
			</tr>
			<tr id="filtro" style="display:none">
				<td>Filtro </td>
				<td><input type="text" size="70"  name="patron" value=""/></td>
				<td><input type="submit" id="boton" name="ejecutar" value="Buscar"/></td>
			</tr>
			<tr id="filtroMSG" style="display:none">
				<td colspan="3" style="color:red">El uso del filtro de busqueda no debe realizarse sin especificar una ruta. <br> Se comprobara el nombre de TODAS las carpetas y ficheros de forma recursiva, lo que puede producir una sobrecarga del sístema.</td>
			</tr>
			
		</form>
	</table>
	
	<b style="position:absolute;top:0px;right:0px" onclick="mostrarFiltro();">+</b>

<% if (vDatos != null && vDatos.size() > 0) { %>

	<form name="descargar" action="<%=app%>/ServletDescargaFichero.svrl" method="POST">
		<input type="hidden" name="nombreFichero" value=""/>
		<input type="hidden" name="rutaFichero"   value=""/>
		<input type="hidden" name="accion"        value=""/>
		<input type="hidden" name="borrarFichero" value=""/>
	</form>

	<form name="borrar" action="<%=app%>/html/jsp/general/sigaDirectorios.jsp" method="POST">
		<input type="hidden" name="nombreFichero" value=""/>
		<input type="hidden" name="rutaFichero"   value=""/>
		<input type="hidden" name="accion"        value="borrar"/>
	</form>

	
	

	<table border="0">
	
		<tr>
			<td colspan="5">
			<B class="dir" onclick="dNavegar('<%=pathUP%>')">..</B>
			</td>
		</tr>

<% 	for (int i = 0; i < vDatos.size(); i++) { 
		Hashtable h = (Hashtable)vDatos.get(i);
		String nombre = (String)h.get("nombre");
		String path2 = (String)h.get("path");
		String tipo   = (String)h.get("tipo");
		String acceso = (String)h.get("acceso");
		String fecha  = (String)h.get("fecha");
		int nivel = Integer.parseInt((String)h.get("nivel"));
		
	%>
		<tr>
		   <td width="400px">
			   <% for (int j = 0; j < nivel; j++) { %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<% } %>
			   <% if(tipo.startsWith("dd")) { %>
			   		<B class="dir" onclick="dNavegar('<%=path2%>')"><%=nombre%></B>
			   <%}else if(tipo.startsWith("d")) { %>
			   		<B class="dirSans"><%=path%></B>
			   <%}else{%>
			   		<%=nombre%>
			   <%}%>
		   </td>
		   <td width="80px"><%=acceso%></td>
		   <td width="200px"><%=fecha%></td>
		   <% if(tipo.equals("d")){%>
			   <td colspan="2">	  
			   		<form name="subir_<%=i%>" action="<%=app%>/ServletDescargaFichero.svrl"  method="POST" enctype="multipart/form-data">
						<input type="hidden" name="nombreFichero" value=""/>
						<input type="hidden" name="rutaFichero"   value=""/>
						<input type="hidden" name="accion"        value=""/>
						<input type="hidden" name="borrarFichero" value=""/>	
						<input type="file" id="fileUpload" name="archivo" />
						<input type="button" value="Subir archivo" onclick="fSubir(archivo.value, '<%=path2%>', 'subir_<%=i%>')" />
					</form>
			   </td> 
		   <% }else if(tipo.equals("dd")){%>  
		   		<td colspan="2">&nbsp;</td>
		   <% }else{ %>
			   <td><% if(!tipo.startsWith("d")) {%><a name="<%=i%>" href="#" onclick="fDescargar('<%=nombre%>', '<%=path2%>')">Descargar</a> <%}%> </td>
			   <td><% if(!tipo.startsWith("d")) {%><a name="<%=i%>" href="#" onclick="fBorrar('<%=nombre%>', '<%=path2%>')">Eliminar</a> <%}%> </td>
		   <% }%>  
		</tr>

   <% } %>
   
	</table>
	
<% } %>


<% } catch (Exception e) { %>
		<p><b>Excepcion:</b> <%e.printStackTrace(new java.io.PrintWriter(out)); %></p>
<% } %>
	
</body>
</html>