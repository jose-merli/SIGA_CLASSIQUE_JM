<!DOCTYPE html PUBLIC "-//IETF//DTD HTML 2.0//EN">
<html>
<head>

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	

	String idPago = (String)request.getAttribute("idPago");
	Vector resultado = (Vector)request.getAttribute("resultado");
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
</head>

<body>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="factSJCS.deducirCobros.titulo1"/>
			</td>
		</tr>
	</table>

	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/FCS_DatosGeneralesPago.do" method="post" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idPagosJG" value = "<%=idPago%>"/>
		<html:hidden property = "idInstitucion" value = "<%=(String)usr.getLocation()%>"/>
		<input type="hidden" name="idsParaEnviar" value="" />
		
		<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="actionModal" value="">
	</html:form>	
			
	<p class="titulitos" style="text-align:center" ><siga:Idioma key="factSJCS.deducirCobros.literal.explicacion"/></p>
	<br>
	
	<siga:Table 
	   name="tablaDatos"
	   border="1"
	   columnNames=",factSJCS.detalleFacturacion.literal.nColegiado,factSJCS.detalleFacturacion.literal.colegiado"
	   columnSizes="10,25,65">		
<%
		if (resultado==null || resultado.size()==0) { 
%>
			<tr class="notFound">
	  			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>	
<% 
		} else { 
			for (int i=0; i<resultado.size(); i++) {
				ColegiadosPagosBean bean = (ColegiadosPagosBean)resultado.elementAt(i);
%>
				<tr class="listaNonEdit">
					<td align="center">
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=bean.getIdPersona()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=bean.getIdInstitucion()%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=bean.getNcolegiado()%>"> 
						
						<input type="checkbox" name="marcado" value="<%=bean.getIdPersona()%>" />
					</td>
					<td align="center"><%=bean.getNcolegiado()%></td>
					<td align="left"><%=bean.getNombreColegiado()%></td>								
				</tr>
				<!-- FIN REGISTRO -->
				<!-- FIN: ZONA DE REGISTROS -->
<% 
			}
		} //fin del else
%>
	</siga:Table>
		
	<siga:ConjBotonesAccion botones="MT,DT,CP,C" modal="G" clase="botonesDetalle"/>

	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		//Asociada al boton Cerrar
		function accionCerrarPago() {		
			sub();
			var aDatos = new Array();
			
			var oCheck = document.getElementsByName("marcado");
			
			for(i=0; i<oCheck.length; i++) {
				if (oCheck[i].checked) {
					var indice=aDatos.length;

					for (j=0; j<aDatos.length; j++) {
						var dato1 = aDatos[j];
						var dato2 = oCheck[i].value;
					}
					
					aDatos[j] = oCheck[i].value;
				}
			}

			document.forms[0].idsParaEnviar.value="";

			for (i=0; i<aDatos.length; i++) {
				document.forms[0].idsParaEnviar.value += ";" + aDatos[i];
			}

			var fname = document.forms[0].name;
			document.forms[0].modo.value="cerrarPagoManual";
			
			// con pantalla de espera
			window.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.factSJCS.procesandoPago';
		}		

		//Asociada al boton Cerrar
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}		

		//Asociada al boton MarcarTodos
		function accionMarcarTodos() {		
			if (document.getElementById("oculto1_1")!=null) {
				var checks = document.getElementsByName("marcado");
				if (checks.type != 'checkbox') {
					for (i = 0; i < checks.length; i++) {
						if (checks[i].disabled==false) {
							checks[i].checked=1;		
						}
					}
					
				} else {
					if (checks.disabled==false) {
						checks.checked=1;		
					}
				}
			}	
		}
	
		//Asociada al boton DesmarcarTodos
		function accionDesmarcarTodos() {		
			if (document.getElementById("oculto1_1")!=null) {
				var checks = document.getElementsByName("marcado");
				if (checks.type != 'checkbox') {
					for (i = 0; i < checks.length; i++) {
						checks[i].checked=0;		
					}
					
				} else {
				  checks.checked=0;
				}
			}	
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>