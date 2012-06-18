<!-- asignarPlantillaCertificado2.jsp -->
<!-- Certificado ordinario -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");

	String idInstitucion=(String)request.getAttribute("idInstitucion");
	String idInstitucionX=(String)request.getAttribute("idInstitucionX");
	String idPersonaX=(String)request.getAttribute("idPersonaX");
	String idBoton=(String)request.getAttribute("idBoton");

	String paramInstitucion[] = {idInstitucion};
	
	String fechaSolicitud = UtilidadesBDAdm.getFechaBD("");
	
	ArrayList aMetodoSol = new ArrayList();
	aMetodoSol.add(1);
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">

		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		<!-- Para el calendario -->
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">

		<!-- Funcion asociada a boton Finalizar -->
		function accionCerrar()
		{
			window.close();
		}

		function completarColegiacion()
		{
			//if(document.forms[0].idInstitucionColegiacion.value== null || document.forms[0].idInstitucionColegiacion.value == ""){
				document.forms[0].idInstitucionColegiacion.value = document.forms[0].idInstitucionPresentador.value;
			//}
		}		

		<!-- Asociada al boton Aceptar -->
		function accionAceptar()
		{
			//var aux=document.forms[0].idPlantilla.value;
			var aux2=document.forms[0].idInstitucionPresentador.value;
			var aux3=document.forms[0].idProductoCertificado.value;
			var metodo = document.forms[0].metodoSolicitud.value;
			var fecha = document.forms[0].fechaSolicitud.value;
			var aux4=document.forms[0].idInstitucionColegiacion.value;
			if(aux3=="")
			{
				var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.productoCertificado"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
			/*
			if(aux=="")
			{
				var mensaje = "<siga:Idioma key="certificados.mantenimiento.literal.plantilla"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
			*/
			if(aux2=="")
			{
				var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.presentador"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
			
			if(aux4=="")
			{
				var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.colegiadoen"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}			

			var a = new Array;
			//a[0]=aux;
			a[0]=aux2;
			a[1]=aux3;
			a[2]=metodo;
			a[3]=fecha;
			a[4]=aux4;

			top.cierraConParametros(a);
		}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body>

		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="certificados.mantenimiento.literal.seleccionPlantillayCertificado"/>
				</td>
			</tr>
		</table>

			<fieldset>
			<table class="tablaCentralCamposPeque" align="center">
				<form name="x">
				<tr>
					<td class="labelText">
						<siga:Idioma key="certificados.mantenimiento.literal.productoCertificado"/>&nbsp(*)
					</td>
					<td>
						<siga:ComboBD nombre="idProductoCertificado" tipo="cmbCertificadosOrdinadios" clase="boxCombo" parametro="<%=paramInstitucion %>" obligatorio="true"/>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="pys.solicitudCompra.literal.presentador"/>&nbsp;(*)
					</td>
					<td>
						<siga:ComboBD nombre="idInstitucionPresentador" tipo="cmbInstitucionesAbreviadas" clase="boxCombo" readonly="false" obligatorio="true" accion="completarColegiacion()" />									
					</td>
				</tr></table>
				<table><td>
					<siga:ConjCampos>
						<table>
							<tr>
								<td class="labelText" width="200px" >
									<siga:Idioma key="pys.solicitudCompra.literal.colegiadoen"/>&nbsp;(*)
								</td>
								<td>
									<siga:ComboBD nombre="idInstitucionColegiacion"  tipo="cmbInstitucionesAbreviadas" clase="boxCombo" readonly="false" obligatorio="true"/>									
								</td>
							</tr>	
							<tr >
								<td class="labelText" colspan="2">
									<i><siga:Idioma key="pys.solicitudCompra.literal.indicacion"/></i>
								</td>
							</tr>
						</table>	
					</siga:ConjCampos>
				</td></table>
				<table>
				<tr>&nbsp;</tr>
				<tr>	
					<td class="labelText">
						<siga:Idioma key="certificados.solicitudes.literal.fechaSolicitud"/>
					</td>				
					<td>
						<siga:Fecha nombreCampo="fechaSolicitud" valorInicial="<%=fechaSolicitud%>"></siga:Fecha>&nbsp;
						<a onClick="return showCalendarGeneral(fechaSolicitud);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="certificados.solicitudes.literal.metodoSolicitud"/>
					</td>				
					<td>
						<siga:ComboBD nombre="metodoSolicitud" tipo="comboMetodoSolicitud" obligatorio="false" ElementoSel="<%=aMetodoSol%>" clase="boxCombo"/>
					</td>
				</tr>
				</form>		
			</table>
			</fieldset>

			<siga:ConjBotonesAccion botones="A,C" modal="P"/>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>