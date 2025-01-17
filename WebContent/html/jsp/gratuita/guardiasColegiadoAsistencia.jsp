<!DOCTYPE html>
<html>
<head>
<!-- guardiasColegiadoAsistencia.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.gratuita.form.AsistenciasForm"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	
	Vector obj = (Vector) request.getAttribute("resultado");
	if(obj==null || obj.size()==0){
		%>
			<script>top.cierraConParametros(null);</script>
		<%
	}
	FilaExtElement[] elems=new FilaExtElement[1];
	elems[0]=new FilaExtElement("seleccionar","seleccionar",SIGAConstants.ACCESS_READ);  	
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script language="JavaScript">
		function seleccionar(fila) {
   	    	var cole = 'oculto' + fila + '_' + 1;
			var aux = new Array();
			aux[0]=document.getElementById(cole).value;
			top.cierraConParametros(aux);
		}
	</script>
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="gratuita.nuevaAsistencia.literal.titulo"/>
			</td>
		</tr>
	</table>
	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<% 	
		String nC="";
		String tC="";
		String botones="";
	  	nC="gratuita.guardiasColegiadoAsistencia.literal.ncolegiado,gratuita.guardiasColegiadoAsistencialiteral.nombreyapellidos,";
		tC="10,65,";
	%>
	<html:form action="/JGR_MantenimientoAsistencia.do" method="post" target="mainWorkArea">
		<input type="hidden" name="modo" />
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="actionModal" value="">
	</html:form>	
		
	<!-- campos a pasar -->
	<siga:Table 
		name="guardiasColegiadoAsistencia"
		border="2"
		columnNames="<%=nC%>"
		columnSizes="<%=tC%>">
<%
		if (obj!=null && obj.size()>0){
			String fecha = "";
		    int recordNumber=1;
			while ((recordNumber) <= obj.size()) {	 
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);
%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="<%=botones%>" elementos="<%=elems%>" visibleBorrado="no" visibleEdicion="no" visibleConsulta="no" clase="listaNonEdit">
					<td  align="center"><input type='hidden' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=hash.get("NCOLEGIADO")%>'><%=hash.get("NCOLEGIADO")%></td>
					<td  align="center"><%=hash.get("NOMBREYAPELLIDOS")%></td>
				</siga:FilaConIconos>
<% 
				recordNumber++;
			} 
		} else { 
%>
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="gratuita.retenciones.noResultados"/></td>
			</tr>
<%
		}

		if (obj!=null && obj.size()==1) {
%>
			<script>seleccionar(1);</script>
<%
		}
%>
	</siga:Table>	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		// Funcion asociada a boton limpiar
		function accionCerrar() {		
			window.top.close();
		}
	</script>

	<!-- INICIO: BOTONES BUSQUEDA -->	
	<siga:ConjBotonesAccion botones="c" clase="botonesDetalle"  modal="M"/>	
	<!-- FIN: BOTONES BUSQUEDA -->
			
	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>