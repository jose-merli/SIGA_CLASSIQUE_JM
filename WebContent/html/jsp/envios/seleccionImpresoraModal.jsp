<!-- seleccionImpresoraModal.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL PEQUEÑA -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.CerSolicitudCertificadosBean"%>
<%@ page import="com.siga.beans.PysProductosInstitucionAdm"%>
<%@ page import="com.siga.beans.EnvEnviosBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String idInstitucion[] = {user.getLocation()};
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	
		<!-- Validaciones en Cliente -->
	<html:javascript formName="DefinirEnviosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>

</head>

<body>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:form action="/ENV_DefinirEnvios.do" method="POST" target="submitArea">
		<html:hidden property = "hiddenFrame" value = "1"/>
		<html:hidden property = "modo" value = "insertarEnvioModal"/>			
	<tr>				
	<td>

		<table class="tablaCampos" align="center" border="0">
			
			<tr>	
				<td class="labelText">
						<siga:Idioma key="envios.definir.literal.tipoenvio"/>
				</td>
				<td>	
					<siga:ComboBD nombre = "idImpresora" tipo="cmbImpresoras" clase="boxCombo" obligatorio="true" parametro="<%=idInstitucion%>"/>						
				</td>				
			</tr>
			
		</table>

	</td>
	</tr>

	</html:form>
	
	</table>



	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="Y,C" modal="P" />


	<!-- INICIO: SCRIPTS BOTONES -->
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			if (document.forms[0]	.idImpresora.value!=""){
				top.cierraConParametros(document.forms[0].idImpresora.value);
			} else {
				alert('El campo Impresora es obligatorio');
			}
			
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.top.close();
		}
	
	function recargarCombos() 
		{		
			var tmp1 = document.getElementsByName("comboTipoEnvio");
				var tmp2 = tmp1[0];			 
				tmp2.onchange();
		}
	
	</script>
	
	<!-- FIN: SCRIPTS BOTONES -->

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
