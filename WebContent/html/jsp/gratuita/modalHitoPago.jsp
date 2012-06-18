<!-- modalHitoPago.jsp -->
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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	String nombreHito = (String)request.getAttribute("nombreHito");
	String idHito = (String)request.getAttribute("idHito");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	ScsHitoFacturableGuardiaBean hito= (ScsHitoFacturableGuardiaBean)request.getSession().getAttribute("HITO");
	ArrayList seleccionado = new ArrayList();
	seleccionado.add(idHito);

	String modo = request.getAttribute("modo")==null?"":(String)request.getAttribute("modo");
	String estilo = "box";
	if (modo.equalsIgnoreCase("VER")) estilo = "boxConsulta";
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<html:javascript formName="DefinirHitosFacturablesGuardiasForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	
	<!--TITULO Y LOCALIZACION -->
<!--gratuita.modalHitoFacturable.literal.titulo" -->
	<siga:Titulo 
		titulo="gratuita.modalDefinirHitosFacturables.literal.editarHitoPago"
		localizacion="gratuita.listadoHitosPago.literal.localizacion"/>
	
</head>

<body>

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulos">
			<siga:Idioma key="gratuita.modalDefinirHitosFacturables.literal.editarHitoFacturable"/>
		</td>
	</tr>
	</table>


<!-- INICIO -->
<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:javascript formName="DefinirHitosFacturablesGuardiasForm" staticJavascript="false" />
	<html:form action="/JGR_DefinirHitosPago.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<tr>				
	<td>
<!--gratuita.modalHitoFacturable.literal.hitoFacturable-->
	<siga:ConjCampos leyenda="gratuita.modalHitoPago.literal.hitoPago">

		<table class="tablaCampos" align="center">

		<tr>
		<td class="labelText"><!--gratuita.listadoHistosFacturables.literal.hito-->
			<siga:Idioma key="gratuita.listadoHistosFacturables.literal.hito"/>
		</td>				
		<td>
			<%	String[] dato1 = {"G"};%>
			<% if (modo.equalsIgnoreCase("VER")) { %>
				<html:text name="DefinirHitosFacturablesGuardiasForm" property="descripcion" size="40" maxlength="40" styleClass="boxConsulta" value="<%=nombreHito%>" readOnly="true"></html:text>
			<%} else { %>
				<siga:ComboBD nombre = "descripcion" tipo="cmbHitoFacturable" clase="boxCombo" obligatorio="true" parametro="<%=dato1%>" elementoSel="<%=seleccionado%>" />
			<% } %>
		</td>
		</tr>				

		<tr>
		<td class="labelText"><!--gratuita.listadoHistosFacturables.literal.precio-->
			<siga:Idioma key="gratuita.listadoHistosFacturables.literal.precio"/>
		</td>
		<td>
			<html:text name="DefinirHitosFacturablesGuardiasForm" property="precio" size="15" maxlength="15" styleClass="<%=estilo%>" value="<%=Float.toString(hito.getPrecioHito())%>"></html:text>
		</td>
		</tr>
			
		</table>

	</siga:ConjCampos>


	</td>
	</tr>

	</html:form>
	
	</table>

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="Y,R,C" modal="P" modo="<%=modo%>" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			if (document.forms[0].descripcion.value!=""){
				if (validateDefinirHitosFacturablesGuardiasForm(document.DefinirHitosFacturablesGuardiasForm)){
				    document.forms[0].modo.value="modificar";
					document.forms[0].submit();
					window.top.returnValue="MODIFICADO";			
				}
			}
			else alert('Es necesario introducir '+'<siga:Idioma key="gratuita.listadoHistosFacturables.literal.hito"/>');
		}

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("NORMAL");
		}

		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}


	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
