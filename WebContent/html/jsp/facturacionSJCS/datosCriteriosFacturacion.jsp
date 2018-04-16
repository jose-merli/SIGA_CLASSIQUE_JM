<!DOCTYPE html>
<%@page import="com.siga.ws.CajgConfiguracion"%>
<html>
<head>
<!-- datosCriteriosFacturacion.jsp -->

<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL PEQUE�A -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String	idInstitucion = usr.getLocation(); 
	Integer pcajgActivo = CajgConfiguracion.getTipoCAJG(Integer.parseInt(idInstitucion));
	String nombreInstitucion = (String)request.getAttribute("nombreInstitucion");
	String idFacturacion = (String)request.getAttribute("idFacturacion");
	String prevision = (String)request.getAttribute("prevision");
	if (prevision==null || !prevision.equals("S")) {
		prevision = "N";
	}
	
%>	


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<html:javascript formName="DatosCriteriosFacturacionForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	
	
</head>

<body>

	<table class="tablaTitulo" cellspacing="0">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="factSJCS.datosFacturacion.titulo1"/> <%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
		</td>
	</tr>
	</table>



	<html:javascript formName="DatosGeneralesFacturacionForm" staticJavascript="false" />
	<html:form action="/FCS_DatosGeneralesFacturacion.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "prevision" value = "<%=prevision%>"/>
	<html:hidden property = "idFacturacion" value = "<%=idFacturacion%>"/>
	

	<siga:ConjCampos leyenda="factSJCS.datosGenerales.leyenda">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="factSJCS.datosFacturacion.literal.hitos"/>&nbsp;(*)
		</td>				
		<td><%	String[] dato1 = {"G"};%>
			<siga:ComboBD nombre = "hito" tipo="cmbHitoGeneral" clase="boxCombo"  accion="accionHito(this);"  obligatorio="true"/>						
		</td>
		</tr>				

		<!-- FILA -->
		<tr>

		<!-- -->
		<td class="labelText">
			<siga:Idioma key="factSJCS.datosFacturacion.literal.gruposFacturacion"/>&nbsp;(*)
		</td>
		<td><%	String[] dato2 = {usr.getLocation()};%>
			<siga:ComboBD nombre = "grupoF" tipo="grupoFacturacion" clase="boxCombo" obligatorio="true" parametro="<%=dato2%>"/>
		</td>
		</tr>
		<% if (pcajgActivo==CajgConfiguracion.TIPO_CAJG_TXT_ALCALA){ %>
		
				<tr id="trTipoPago" style="display:none">
					<td class="labelText">Tipo certificaci�n</td>
					<td><html:select name="DatosGeneralesFacturacionForm" styleId="convenio" styleClass="boxCombo" style="width:150px;" property="convenio" >
							<html:option value=''>&nbsp;</html:option>
							<html:option value='0'>Subvenci�n J.G.</html:option>		
							<html:option value='1'>Convenio T.O.</html:option>						
						</html:select>
					</td>
				</tr>
		<%}%>
	
		</table>

	</siga:ConjCampos>



	</html:form>
	

	<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
	function accionHito(hito){
		if(document.getElementById('trTipoPago')){		
			if(hito.value==10)
				document.getElementById('trTipoPago').style.display = 'block';
			else
				document.getElementById('trTipoPago').style.display = 'none';
		}
	}

	
		function accionGuardarCerrar() {
            if (document.forms[0].hito.selectedIndex < 1) {
                alert('<siga:Idioma key="factSJCS.datosFacturacion.literal.hitos"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
                fin();
                return false;
            }else{
                if (document.forms[0].grupoF.selectedIndex < 1) {
                       alert('<siga:Idioma key="factSJCS.datosFacturacion.literal.gruposFacturacion"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
                       fin();
                       return false;
                }
                else{
                      document.forms[0].modo.value="insertarCriterio";
                      document.forms[0].submit();
                      window.top.returnValue="MODIFICADO";                 
                }
            }
        }
		
		function accionGuardar() 
		{	

		    document.forms[0].modo.value="insertarCriterio";
			document.forms[0].submit();
			window.top.returnValue="MODIFICADO";			
		}
		
		// Asociada al boton Cerrar
		function accionCerrar() 
		{		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("NORMAL");
		}

		// Asociada al boton Restablecer
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		function growl(msg,type){
			//jQuery('.notice-item-wrapper').remove();
			jQuery.noticeAdd({
				text: msg,
				type: type
			});
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
