<!DOCTYPE html>
<html>
<head>
<!-- listadoComponentes.jsp -->
<!-- 
	 VERSIONES:
	 juan.grau 07-03-2005 Versión inicial
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants" %>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	
	Vector vDatos = (Vector)request.getAttribute("datos");	
	request.removeAttribute("datos");
	
	Hashtable dataBackup = (Hashtable)request.getSession().getAttribute("DATABACKUP");	
	String editable = (String)dataBackup.get("editable");
	boolean bEditable = editable.equals("1")? true : false;
	String botonBorrar = "";
	if (bEditable) botonBorrar="B";

%>	



<!-- HEAD -->
	
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="envios.listas.literal.titulo" 
		localizacion="envios.listas.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<html:form action="/ENV_ListaCorreos.do" method="POST" target="mainWorkArea" style="display:none">
			
		    <html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			<html:hidden property = "borrarComponente" value = "true"/>

		</html:form>


			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="envios.listas.literal.nombreyapellidos,
		   		  	censo.fichaCliente.literal.colegiado,
		   		  	censo.busquedaClientes.literal.nif,"
		   		  columnSizes="60,15,15,10"
		   		  modal="m">
	   		     		    		  
		   		  
		    <!-- INICIO: ZONA DE REGISTROS -->
<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%
				}
				
				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		Row fila = (Row) vDatos.elementAt(i);	
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones='<%=botonBorrar%>' clase="listaNonEdit" visibleConsulta="no" visibleEdicion="no">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=fila.getString("IDINSTITUCION")%>"/>
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=fila.getString("IDPERSONA")%>"/>
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=fila.getString("IDLISTACORREO")%>"/>
					<%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBREYAPELLIDOS"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NCOLEGIADO"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NIFCIF"))%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			<!-- FIN: ZONA DE REGISTROS -->
			</siga:Table>
			

		<!-- FIN: LISTA DE VALORES -->
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	<script language="JavaScript">

		
		function refrescarLocal()
		{			
			parent.cargarListado();			
		}	
		

	</script>
		
	</body>
</html>