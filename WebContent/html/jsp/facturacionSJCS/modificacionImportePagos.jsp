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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	String idPago="", importe="", tipo="", idPersona="", importeIrpf="", porcentajeIrpf="";

	try {
		idPago = (String)request.getAttribute("idPago");
		importe = (String)request.getAttribute("importe");
		tipo = (String)request.getAttribute("tipo");
		idPersona = (String)request.getAttribute("idPersona");
		importeIrpf = (String)request.getAttribute("importeIrpf");
		porcentajeIrpf = (String)request.getAttribute("porcentajeIrpf");
	} catch(Exception e) {idPago=""; importe=""; tipo=""; idPersona=""; importeIrpf=""; porcentajeIrpf="";}
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
 	
	
</head>

<body>

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="factSJCS.detallePagos.titulo1"/>
		</td>
	</tr>
	</table>


<!-- INICIO -->
<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:javascript formName="DatosDetallePagoForm" staticJavascript="false" />
	<html:form action="/FCS_DetallePago.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
	<html:hidden property = "idPago" value = "<%=idPago%>"/>
	<html:hidden property = "tipo" value = "<%=tipo%>"/>
	<html:hidden property = "importeIrpf" value = "<%=importeIrpf%>"/>
	<html:hidden property = "porcentajeIrpf" value = "<%=porcentajeIrpf%>"/>
			
	<tr>				
	<td>

	<siga:ConjCampos leyenda="factSJCS.detallePagos.leyenda">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="factSJCS.detalleFacturacion.literal.importe"/>
		</td>				
		<td class="labelText">
			<html:text name="DatosDetallePagoForm" property="importeTotal" maxlength="13" styleClass="box" value="<%=importe%>" readonly="false"/> &euro;
		</td>
		</tr>				
		<tr>
		<td>
		&nbsp;
		</td>
		</tr>

		<!-- FILA -->
		<tr>

		<!-- -->
		<td>
			&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
		</tr>
		<tr>
		<td>
		&nbsp;
		</td>
		</tr>

		<!-- FILA -->
		<tr>				
		<td>
		&nbsp;
		</td>
		</tr>
		<tr>				
		<td>
		&nbsp;
		</td>
		</tr>
	
		</table>

	</siga:ConjCampos>


	</td>
	</tr>

	</html:form>
	
	</table>

		<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">


		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
		    document.forms[0].modo.value="modificarPrecioPago";
			document.forms[0].submit();
			window.top.returnValue="MODIFICADO";			
			
		}

		function accionGuardar() 
		{	
		    document.forms[0].modo.value="modificarPrecioPago";
			document.forms[0].submit();
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			//Refrescamos para resetear los valores de las tablas y que no den problemas entre si:
			top.cierraConParametros("MODIFICADO");
		}

		<!-- Asociada al boton Restablecer -->
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
