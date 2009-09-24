<!-- listadoPartidasPresupuestarias.jsp -->
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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaBean"%>
<%@ page import="com.siga.beans.ScsPartidaPresupuestariaAdm"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	Vector obj = (Vector) ses.getAttribute("resultado");
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<title><siga:Idioma key="gratuita.partidaPresupuestaria.literal.partidaPresupuestaria"/></title>	
	<script language="JavaScript">	
		function refrescarLocal()
		{
			parent.buscar();
		}		
	</script>
</head>

<body>
	<%if (obj.size()>0){%>
	<html:form action="/PartidaPresupuestariaAction.do" method="post" target="submitArea"	 style="display:none">
		<input type="hidden" name="modo" value="">

		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="filaSelD">
		<input type="hidden" name="tablaDatosDinamicosD">
		<input type="hidden" name="actionModal" value="">
	</html:form>	
		
		<siga:TablaCabecerasFijas 		   
		   nombre="listadoPartidas"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.partidaPresupuestaria.literal.nombre,gratuita.partidaPresupuestaria.literal.descripcion,factSJCS.datosFacturacion.literal.importePartida,"
		   tamanoCol="30,45,15,10"
		   alto="100%"
		   modal="P" 
		   activarFilaSel="true" >
		   
  			<%
	    	int recordNumber=1;
	    	Hashtable registro = new Hashtable();
			while (recordNumber-1 < obj.size()) {
				ScsPartidaPresupuestariaBean fila = (ScsPartidaPresupuestariaBean)obj.get(recordNumber-1);
				registro = fila.getOriginalHash();
			%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones="C,E,B" clase="listaNonEdit">
					<td><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=fila.getIdPartidaPresupuestaria()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=fila.getFechaMod()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=fila.getUsuMod()%>"><input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=fila.getIdInstitucion()%>"><%=registro.get("NOMBREPARTIDA")%></td>
					<td><%=registro.get("DESCRIPCION")%>&nbsp;</td>
					<td align="right"><%=UtilidadesNumero.formatoCampo(registro.get("IMPORTEPARTIDA").toString())%>&nbsp;&nbsp;&euro;</td>
				</siga:FilaConIconos>		
			<%  
				recordNumber++; 
			}
			%>
		</siga:TablaCabecerasFijas>

	<%
	}else {
	%>
	<siga:TablaCabecerasFijas 		   
		   nombre="listadoPartidas"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.partidaPresupuestaria.literal.nombre,gratuita.partidaPresupuestaria.literal.descripcion,factSJCS.datosFacturacion.literal.importePartida,"
		   tamanoCol="30,45,15,10"
		   			alto="100%"


		   modal="P"
  	    >
  	    </siga:TablaCabecerasFijas>
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
	<%
	}
	%>	

	<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->	
</body>	
</html>