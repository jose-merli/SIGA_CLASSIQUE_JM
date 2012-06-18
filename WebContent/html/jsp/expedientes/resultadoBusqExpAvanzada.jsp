<!-- resultadoBusqExpAvanzada.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 28-12-2004 Versión inicial
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
<%@ page import="com.siga.expedientes.ExpPermisosTiposExpedientes"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idInstitucion = userBean.getLocation();
	String botones = "";
	Vector vDatos = (Vector)request.getAttribute("datos");
	ExpPermisosTiposExpedientes perm=(ExpPermisosTiposExpedientes)request.getAttribute("permisos");
	
	request.removeAttribute("datos");
%>	

<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="expedientes.auditoria.literal.titulo" 
			localizacion="expedientes.auditoria.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<html:form action="/EXP_AuditoriaExpedientes.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">
			<html:hidden property = "modo" value = ""/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
		
		
		
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="expedientes.auditoria.literal.institucion,
		   		  	expedientes.auditoria.literal.tipo,
		   		  	expedientes.auditoria.literal.nexpediente,
					expedientes.tiposexpedientes.literal.estado,
		   		  	expedientes.auditoria.literal.fecha,
		   		  	expedientes.auditoria.literal.nombreyapellidos,"
		   		  tamanoCol="15,15,8,14,8,30,10"
		   			alto="100%"
		   			ajusteBotonera="true"	
		   			activarFilaSel="true">
		   		  
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
				  		//ExpExpedienteBean bean = (ExpExpedienteBean)vDatos.elementAt(i);
				  		Row fila = (Row)vDatos.elementAt(i);	
				  		if (fila.getString("IDINSTITUCION").equals(idInstitucion)){	
				  			botones="C,E,B";
				  		}else{
				  			botones="C,E";
				  		}
				  		botones=perm.getBotones(fila.getString("IDINSTITUCION_TIPOEXPEDIENTE"),fila.getString("IDTIPOEXPEDIENTE"),botones);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" clase="listaNonEdit">
					<td><%=fila.getString("ABREVIATURA")%></td>
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=fila.getString("IDINSTITUCION")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=fila.getString("IDINSTITUCION_TIPOEXPEDIENTE")%>">						
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=fila.getString("IDTIPOEXPEDIENTE")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=fila.getString("NUMEROEXPEDIENTE")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=fila.getString("ANIOEXPEDIENTE")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBRETIPOEXPEDIENTE"))%>">
						<%=fila.getString("NOMBRETIPOEXPEDIENTE")%>
					</td>
					<td><%=fila.getString("ANIOEXPEDIENTE")+" / "+fila.getString("NUMEROEXPEDIENTE")%></td>
					<td><%=fila.getString("NOMBREESTADO").equals("")?"&nbsp;":fila.getString("NOMBREESTADO") %></td>
					<td><%=GstDate.getFormatedDateShort("",fila.getString("FECHA"))%></td>
					<td><%=fila.getString("DENUNCIADO")%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			<!-- FIN: ZONA DE REGISTROS -->
			</siga:TablaCabecerasFijas>
			

		<!-- FIN: LISTA DE VALORES -->

	<!-- INICIO: BOTONES REGISTRO -->

		<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function refrescarLocal()
		{			
			document.forms[0].modo.value="buscarPor";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();
			
		}


	<!-- Asociada al boton Volver -->
		function accionVolver() 
		{		
			document.forms[0].modo.value="abrirAvanzada";
			document.forms[0].target="mainWorkArea";	
			document.forms[0].submit();	
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>