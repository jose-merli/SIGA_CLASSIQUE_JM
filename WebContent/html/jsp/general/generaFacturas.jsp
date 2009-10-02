<!-- generaFacturas.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>

<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="javax.transaction.*"%>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="es.satec.siga.util.SIGAReferences"%>
<% 
String app=request.getContextPath(); 
HttpSession ses=request.getSession();
Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
%>

<% 
		
String idInstitucion = "";
String idSerieFacturacion = "";
String idFacturacionProgramada = "";		

try {
	String mensaje = "";
	Vector vDatos = new Vector();	
	UserTransaction tx = null;
	try {
		tx = user.getTransaction();
		
		idInstitucion = (String)request.getParameter("idInstitucion");
		if (idInstitucion==null) idInstitucion="";
		idSerieFacturacion = (String)request.getParameter("idSerieFacturacion");
		if (idSerieFacturacion==null) idSerieFacturacion="";
		idFacturacionProgramada = (String)request.getParameter("idFacturacionProgramada");
		if (idFacturacionProgramada==null) idFacturacionProgramada="";
		
		if (idInstitucion!=null && !idInstitucion.trim().equals("") &&
		    idSerieFacturacion!=null && !idSerieFacturacion.trim().equals("") &&
		    idFacturacionProgramada!=null && !idFacturacionProgramada.trim().equals("")) {
			
			FacFacturaAdm facturaAdm = new FacFacturaAdm(user);
			if (0!=facturaAdm.almacenarOldParaBorrar(request,
										new Integer(idInstitucion),
										new Long(idSerieFacturacion),
										new Long(idFacturacionProgramada), 
										false,
										null, 
										tx)) {
				throw new Exception("Error en almacenar");
			}
			
			// Obtencion de la ruta donde se almacenan las facturas en formato PDF			
		    ReadProperties rp = new ReadProperties(SIGAReferences.RESOURCE_FILES.SIGA);
		    String rutaAlmacen = rp.returnProperty("facturacion.directorioFisicoFacturaPDFJava")+rp.returnProperty("facturacion.directorioFacturaPDFJava");
		    String idserieidprogramacion = idSerieFacturacion.toString()+"_" + idFacturacionProgramada.toString();
			String barraAlmacen = "";
			String nombreFicheroAlmacen = "";
			if (rutaAlmacen.indexOf("/") > -1){ 
				barraAlmacen = "/";
			}
			if (rutaAlmacen.indexOf("\\") > -1){ 
				barraAlmacen = "\\";
			}    		
			rutaAlmacen += barraAlmacen+idInstitucion.toString()+barraAlmacen+idserieidprogramacion;
			File rutaPDF=new File(rutaAlmacen);
			File[] todos = rutaPDF.listFiles();
			for (int i=0;i<todos.length;i++) {
				vDatos.add(todos[i]);
			}
		
		} else {
			mensaje="Introduzca los parámetros";
		}
	 } catch (Exception e) {
	 	e.printStackTrace();
	 	mensaje=e.toString();
	 	try { tx.rollback(); } catch (Exception ee) {};
	 }	    
	    
%>

<html>
<head>
    <title>Generación de facturas</title>
    <style>
    	td {border:solid 1px #DDDDDD}
    </style>
</head>
<script>
	function fDescargar (fichero, path) 
	{
		document.descargar.nombreFichero.value=fichero;
		document.descargar.rutaFichero.value=unescape(path);
		document.descargar.submit();
	}
</script>
<body style="font-family:verdana;">
	<h2>Generación de facturas</h2>
	<table>
		<form name="generar" action="<%=app%>/html/jsp/general/generaFacturas.jsp" method="POST">
			<tr>
				<td>Id Institucion: </td>
				<td><input type="text" size="50"  name="idInstitucion" value="<%=idInstitucion%>"/></td>
			</tr>
			<tr>
				<td>Id Serie facturación: </td>
				<td><input type="text" size="50"  name="idSerieFacturacion" value="<%=idSerieFacturacion%>"/></td>
			</tr>
			<tr>
				<td>Id Facturación programada: </td>
				<td><input type="text" size="50"  name="idFacturacionProgramada" value="<%=idFacturacionProgramada%>"/></td>
			</tr>
			
			<tr>
				<td><input type="reset" name="limpiar" value="Limpiar"/></td>
				<td><input type="submit" name="ejecutar" value="Generar"/></td>
			</tr>
			
		</form>
	</table>

<% if (!mensaje.equals("")) { %>
	<p><b>Mensaje:</b> &nbsp;<%=mensaje %></p>
<% } %>


<% if (vDatos != null && vDatos.size() > 0) { %>


	<form name="descargar" action="<%=app%>/ServletDescargaFichero.svrl" method="POST">
		<input type="hidden" name="nombreFichero" value=""/>
		<input type="hidden" name="rutaFichero"   value=""/>
	</form>


	<p><b>Resultados:</b> &nbsp;<%=vDatos.size() %></p>
	<table border="0">
	
	
	
<% 	for (int i = 0; i < vDatos.size(); i++) { 
		File f = (File)vDatos.get(i);
		String nombre = f.getName();
		
	%>
		<tr>
		   <td>
		   <%=nombre%>
		   </td>
		   <td width="80px">&nbsp;&nbsp;</td>
		   <td><a name="<%=i%>" href="#" onclick="fDescargar('<%=nombre%>', escape('<%=f.getAbsolutePath()%>'))">Descargar Factura</a></td>
		</tr>

   <% } %>
   
	</table>
	
<% } %>


<% } catch (Exception e) { %>
		<p><b>Excepcion:</b> <%e.printStackTrace(new java.io.PrintWriter(out)); %></p>
<% } %>
	
</body>
</html>