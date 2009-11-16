<!-- abrirAlerta.jsp -->
<!-- 
	 VERSIONES:
	 juan.grau 02/02/2005 Versi�n inicial
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
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.expedientes.form.ExpAlertaForm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	String editable = (String)request.getParameter("editable");		
	String soloSeguimiento = (String)request.getParameter("soloSeguimiento");	
	boolean bEditable=false;
	if (soloSeguimiento.equals("true")){
		bEditable=false;
	}else{
		bEditable = editable.equals("1")? true : false;
	}
	
	//String bEditable = (String)request.getParameter("editable");
	//boolean editable = bEditable.equals("1")? true : false;
	String botones = bEditable? "B" : "";
	
	String denunciado = (String)request.getAttribute("denunciado");
	String nombreTipoExpediente = (String)request.getParameter("nombreTipoExpediente");
	String numExpediente = (String)request.getParameter("numeroExpediente");
	String anioExpediente = (String)request.getParameter("anioExpediente");
	String idTipoExpediente = (String)request.getParameter("idTipoExpediente");
	String idInstitucion = (String)request.getParameter("idInstitucion");
	String idInstitucionTipoExpediente = (String)request.getParameter("idInstitucion_TipoExpediente");
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("volverAuditoriaExpedientes");
	
	request.removeAttribute("datos");	
%>	

<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
		<siga:Titulo 
			titulo="expedientes.auditoria.alertas.cabecera" 
			localizacion="expedientes.auditoria.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>
	
		<table class="tablaTitulo" align="center" cellspacing="0">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<% ExpAlertaForm f = (ExpAlertaForm)request.getAttribute("ExpAlertaForm"); %>
					<%=f.getTituloVentana()%>
				</td>
			</tr>


		<html:form action="/EXP_Auditoria_Alertas.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>		

		</table>
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="expedientes.auditoria.literal.fecha,
					expedientes.auditoria.literal.fase,
		   		  	expedientes.auditoria.literal.estado,
					expedientes.auditoria.literal.alerta,"
		   		  tamanoCol="15,20,25,30,10"
		   			alto="100%"
		   			ajusteBotonera="true"		

		   	>
		   		  
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
				  		Row fila = (Row)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" clase="listaNonEdit">
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=idInstitucion%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=idInstitucionTipoExpediente%>">						
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=idTipoExpediente%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=numExpediente%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=anioExpediente%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=fila.getString("IDALERTA")%>">						
					<td><%=UtilidadesString.formatoFecha(fila.getString("FECHAALERTA"),"yyyy/MM/dd HH:mm:ss","dd/MM/yyyy HH:mm:ss")%></td>
					<td><%=fila.getString("FASE")%></td>
					<td><%=fila.getString("ESTADO")%></td>
					<td><%=fila.getString("TEXTO")%></td>					
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

<!--<% if (!busquedaVolver.equals("volverNo")) { %>-->
		<html:form action="/EXP_AuditoriaExpedientes.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "avanzada" value = ""/>		
		</html:form>
<!--<% } %>	-->
	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function refrescarLocal()
		{	
			document.location.reload();
		}

	<!-- Asociada al boton Volver -->
		function accionVolver() 
		{		
			<% if (busquedaVolver.equals("AB")) { %>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
				document.forms[1].modo.value="buscarPor";
				document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
			<% } else if (busquedaVolver.equals("NB")){ %>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true&buscar=true";
				document.forms[1].modo.value="abrir";
			<% } else if(busquedaVolver.equals("AV")) { %>
				document.forms[1].action = "<%=app%>/EXP_AuditoriaExpedientes.do?noReset=true";
				document.forms[1].modo.value="buscarPor";
				document.forms[1].avanzada.value="<%=ClsConstants.DB_TRUE %>";
			<% }  else if (busquedaVolver.equals("Al")){%>
				document.forms[1].action = "<%=app%>/EXP_Consultas.do?noReset=true&buscar=true";
				document.forms[1].modo.value="abrir";
			<% } %>
			
			document.forms[1].submit();	
		}
		
	<!-- Asociada al boton Nuevo -->
		function accionNuevo() 
		{		
			
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>