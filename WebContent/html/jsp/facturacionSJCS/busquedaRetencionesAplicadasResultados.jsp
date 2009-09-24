<!-- busquedaRetencionesAplicadasResultados.jsp -->

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

<%@ page import="com.atos.utils.*"%>
<%@page import="com.siga.Utilidades.*"%>



<!-- JSP -->
<% 
	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	//Vector v = (Vector)request.getAttribute("resultado");
	
	
%>	




<html>
<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
	<script src="<html:rewrite page="/html/js/calendarJs.jsp"/>" type="text/javascript"></script>	
	<script src="<html:rewrite page="/html/jsp/general/validacionSIGA.jsp"/>" type="text/javascript"></script>
	
</head>

<body>

		<html:form action="/FCS_BusquedaRentencionesAplicadas.do" method="post" target="submitArea" >
			<html:hidden property="modo"    value=""/>
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
			
			
			</html:form>
			
		
			
		
		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="factSJCS.busquedaRetAplicadas.literal.tipoRetencion,factSJCS.busquedaRetAplicadas.literal.letrado,factSJCS.busquedaRetAplicadas.literal.destinatarioRetencion
				,factSJCS.busquedaRetAplicadas.literal.fechaDesde,factSJCS.busquedaRetAplicadas.literal.fechaHasta,factSJCS.busquedaRetAplicadas.literal.fechaRetencion
				,factSJCS.busquedaRetAplicadas.literal.abonoRelacionado,factSJCS.busquedaRetAplicadas.literal.pagoRelacionado,factSJCS.busquedaRetAplicadas.literal.importeRetenido"
		   tamanoCol="10,20,10,10,10,10,10,10,10,0" 
		   ajusteBotonera="true" >
		<logic:empty  name="BusquedaRetencionesAplicadasForm" property="retencionesAplicadas">
		<tr>
			<td colspan="9"><br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br></td>
		</tr>
	</logic:empty>
	
	<logic:notEmpty name="BusquedaRetencionesAplicadasForm" property="retencionesAplicadas">
		
		<logic:iterate name="BusquedaRetencionesAplicadasForm" property="retencionesAplicadas" id="retencionAplicada" indexId="index">
		
		<siga:FilaConIconos 
	  				fila='<%=String.valueOf(index.intValue()+1)%>'

	  				botones="" 
	  				pintarEspacio="no"
	  				visibleConsulta="no"
	  				visibleEdicion = "no"
	  				visibleBorrado = "no"
	  				clase="listaNonEdit">
		
				<%-- fila <bean:write name='index'/> escribe la informacion de la linea de comunicacion--%>
				<bean:define property="persona" name="retencionAplicada" id="persona"></bean:define>
				<bean:define property="retencionJudicial" name="retencionAplicada" id="retencionJudicial"></bean:define>
				<bean:define property="destinatarioRetencion" name="retencionJudicial" id="destinatarioRetencion"></bean:define>
				<bean:define id="fechaInicio" name="retencionJudicial" property="fechaInicio"/>
				<bean:define id="fechaFin" name="retencionJudicial" property="fechaFin"/>
				<bean:define id="fechaRetencion" name="retencionAplicada" property="fechaRetencion"/>
				<bean:define id="importeRetenido" name="retencionAplicada" property="importeRetenido"/>
				
				
				
				
				<td><bean:write name="retencionJudicial" property="descTipoRetencion" /></td>
				<td><bean:write name="persona" property="nombre" /></td>
				<td><bean:write name="destinatarioRetencion" property="nombre" /></td>
				<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio))%></td>
				<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin))%></td>
				<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),fechaRetencion))%></td>
				<td><bean:write name="retencionAplicada" property="abonoRelacionado" /></td>
				<td><bean:write name="retencionAplicada" property="pagoRelacionado" /></td>
				<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(importeRetenido.toString()))%></td>
				
				
			</siga:FilaConIconos>
			
			
			
		</logic:iterate>
		
	</logic:notEmpty>



	</siga:TablaCabecerasFijas>
	
	

<!-- FIN: LISTA DE VALORES -->

	
	<!-- INICIO: BOTONES BUSQUEDA -->	
		
		
	<!-- FIN: BOTONES BUSQUEDA -->

	<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" style="display:none"></iframe>
 		
 		
	
	
	</body>

</html>
		
		
