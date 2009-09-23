<!-- consultaGrupoFacturacion.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector obj = (Vector) request.getAttribute("resultado");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
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
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>	
		
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoGruposFacturacion"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.gruposFacturacion.literal.nombre,gratuita.gruposFacturacion.literal.porcentajeInicio,gratuita.gruposFacturacion.literal.porcentajeExtrajudicial,"
		   tamanoCol="50,20,20,10"
		   			alto="100%"


		   modal="P"
  	    >
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
			<tr>
			<td colspan="4">
				<br>
				<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br>
			</td>
			</tr>
		<%}%>
	</siga:TablaCabecerasFijas>

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