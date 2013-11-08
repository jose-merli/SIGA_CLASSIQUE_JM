<!DOCTYPE html>
<html>
<head>
<!-- consultaGrupoFacturacion.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.*"%>



<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	Vector obj = (Vector) request.getAttribute("resultado");
%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo 
		titulo="gratuita.gruposFacturacion.cabecera" 
		localizacion="gratuita.gruposFacturacion.ruta"/>	
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script type="text/javascript">		
		function refrescarLocal()
		{
			parent.buscar();
		}		
	</script>
	
</head>

<body>

<%if (obj.size()>0){%>
	<html:form action="/JGR_MantenimientoGruposFacturacion.do" method="post" target="submitArea">
		<input type="hidden" name="modo" value="">
	</html:form>	
		
		<siga:Table 		   
		   name="listadoGruposFacturacion"
		   border="2"
		   columnNames="gratuita.gruposFacturacion.literal.nombre,gratuita.gruposFacturacion.literal.porcentajeInicio,gratuita.gruposFacturacion.literal.porcentajeExtrajudicial,"
		   columnSizes="50,20,20,10"
		   modal="P">
  	    <%if (obj.size()>0){%>
  			<%
	    	int recordNumber=1;
			while (recordNumber-1 < obj.size())
			{			
				Hashtable fila = (Hashtable)obj.get(recordNumber-1);				
			%>
			<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' visibleConsulta="no" botones="E,B" clase="listaNonEdit">
				<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.get("IDGRUPOFACTURACION").toString()%>"><%=fila.get("NOMBRE").toString()%></td>
				<td align="center"><%=fila.get("PORCENTAJEINICIO").toString()%>%&nbsp;</td>
				<td align="center"><%=fila.get("PORCENTAJEEXTRAJUDICIAL").toString()%>%&nbsp;</td>
			</siga:FilaConIconos>		
			<% recordNumber++;} %>		
		<%}else {%>
			<tr class="notFound">
   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
		<%}%>
	</siga:Table>

	<%
	}else {
	%>
	<p class="nonEditRed" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
	<%
	}
	%>	

<div style="position:absolute;top:430;z-index:3;left:16">
	<siga:ConjBotonesAccion botones="N"/>
</div>	

<script type="text/javascript">
	function accionNuevo(){
		document.forms[0].modo.value = "nuevo";
		var resultado=ventaModalGeneral(document.forms[0].name,"P");
		if (resultado=="MODIFICADO") refrescarLocal();
	}
</script>

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>	
</html>