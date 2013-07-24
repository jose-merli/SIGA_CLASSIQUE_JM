<!DOCTYPE html>
<html>
<head>
<!-- infoDirectorioOLD.jsp -->

<%@page import="com.siga.general.InfoDirectorio"%>
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
 	if (path==null) {
		path = (String)request.getSession().getAttribute("path");
		request.getSession().removeAttribute("path");
	}
	if (path != null && !path.trim().equals("") && !path.trim().equalsIgnoreCase("null")) {
		if (path.startsWith("C:")) {
			// windows
			path = path.substring(2,path.length());
			path = UtilidadesString.replaceAllIgnoreCase(path,"\\","/");
		}
		if (path.startsWith(pathBase)){
			vDatos = InfoDirectorio.getInfoDirectorio(path);
		}else{
			path=pathBase;
		}
	}
	else {
		path=pathBase;
	}
	
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

    <title>Info Direcciones</title>
    <style>
    	td {border:solid 1px #DDDDDD}
    </style>
</head>
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
	
</script>
<body style="font-family:verdana;" <% if (buscar) out.println("onLoad='document.consulta.submit();'"); %>>
	<h2>Path siga</h2>
	<table>
		<form name="consulta" action="<%=app%>/html/jsp/general/infoDirectorioOLD.jsp" method="POST">
			<tr>
				<td>Path: </td>
				<td><input type="text" size="50"  name="path" value="<%=path%>"/></td>
			</tr>
			
			<tr>
				<td><input type="reset" name="limpiar" value="Limpiar"/></td>
				<td><input type="submit" name="ejecutar" value="Ejecutar"/></td>
			</tr>
			
		</form>
	</table>

<% if (vDatos != null && vDatos.size() > 0) { %>

	<form name="descargar" action="<%=app%>/ServletDescargaFichero.svrl" method="POST">
		<input type="hidden" name="nombreFichero" value=""/>
		<input type="hidden" name="rutaFichero"   value=""/>
		<input type="hidden" name="accion"        value=""/>
		<input type="hidden" name="borrarFichero" value=""/>
		
		
	</form>

	<form name="borrar" action="<%=app%>/html/jsp/general/infoDirectorioOLD.jsp" method="POST">
		<input type="hidden" name="nombreFichero" value=""/>
		<input type="hidden" name="rutaFichero"   value=""/>
		<input type="hidden" name="accion"        value="borrar"/>
	</form>

	<p><b>Resultados:</b></p>
	<table border="0">
	
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
		   <td>
		   
		   <% for (int j = 0; j < nivel; j++) { %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<% } %>
		   <% if(tipo.equals("d")) { %><B><%}%>
		   <%=nombre%>
		   <% if(tipo.equals("d")) { %></B><%}%>
		   </td>
		   <td width="80px"><%=acceso%></td>
		   <td width="200px"><%=fecha%></td>
		   <td><% if(!tipo.equals("d")) {%><a name="<%=i%>" href="#" onclick="fDescargar('<%=nombre%>', '<%=path2%>')">Descargar</a> <%}%> </td>
		   <td><% if(!tipo.equals("d")) {%><a name="<%=i%>" href="#" onclick="fBorrar('<%=nombre%>', '<%=path2%>')">Eliminar</a> <%}%> </td>
		   <% if(tipo.equals("d")){
			   
		   %>
		   <td>	  
		   		<form name="subir_<%=i%>" action="<%=app%>/ServletDescargaFichero.svrl"  method="POST" enctype="multipart/form-data">
					<input type="hidden" name="nombreFichero" value=""/>
					<input type="hidden" name="rutaFichero"   value=""/>
					<input type="hidden" name="accion"        value=""/>
					<input type="hidden" name="borrarFichero" value=""/>					
					<INPUT type="file" name="archivo" value="" />
					<input type="button" value="add" onclick="fSubir(archivo.value, '<%=path2%>', 'subir_<%=i%>')" />
				</form>
		   </td> 
			   
			   
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