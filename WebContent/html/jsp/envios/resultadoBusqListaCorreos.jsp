<!-- resultadoBusqListaCorreos.jsp -->
<!-- 
	 VERSIONES:
	 juan.grau 07-03-2005 Versi�n inicial
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
<%@ page import="com.siga.beans.EnvListaCorreosBean"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	Vector vDatos = (Vector)request.getAttribute("datos");	
	request.removeAttribute("datos");
	
	//Par�metro para distinguir el boton de borrado
	request.setAttribute("listado","listas");
%>	

<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo 
		titulo="envios.listas.literal.titulo" 
		localizacion="envios.listas.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<html:form action="/ENV_ListaCorreos.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">
			
		    <html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>

		<siga:TablaCabecerasFijas 
	   	      nombre="tablaDatos"
	   		  borde="1"
	   		  clase="tableTitle"
	   		  nombreCol="envios.listas.literal.lista,
	   		  envios.listas.literal.descripcion, 
	   		  envios.listas.literal.dinamica,"
	   		  tamanoCol="20,60,10,10"
	   		  alto="100%"
	   		  activarFilaSel="true" >
		   		  
		    <!-- INICIO: ZONA DE REGISTROS -->
<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}
				
				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		EnvListaCorreosBean bean = (EnvListaCorreosBean)vDatos.elementAt(i);	
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="C,E,B" clase="listaNonEdit">
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdInstitucion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdListaCorreo()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=bean.getDescripcion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=bean.getDinamica()%>">
						
					<td><%=UtilidadesString.mostrarDatoJSP(bean.getNombre())%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(bean.getDescripcion())%></td>
					<td><%
								if (bean.getDinamica().equals("S"))
								{ %>
									<siga:Idioma key="general.yes"/>
							<%} else {%>
									<siga:Idioma key="general.no"/>
							<%}%>
					</td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			<!-- FIN: ZONA DE REGISTROS -->
			</siga:TablaCabecerasFijas>
			

		<!-- FIN: LISTA DE VALORES -->
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	<script language="JavaScript">

		
		function refrescarLocal()
		{			
			parent.buscar() ;			
		}		

	</script>
		
	</body>
</html>