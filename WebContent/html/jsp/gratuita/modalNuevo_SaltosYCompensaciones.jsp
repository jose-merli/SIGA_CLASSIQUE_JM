<!DOCTYPE html>
<html>
<head>
<!-- modalNuevo_SaltosYCompensaciones.jsp -->
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ScsSaltosCompensacionesBean"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	

	//Para el Combo de Turnos
	String dato[] = {(String)usr.getLocation()};
%>



<!-- HEAD -->


	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="SaltosYCompensacionesNuevoForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

</head>

<body>

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.titulo"/>
	</td>
</tr>
</table>
	

	
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposPeque" align="center" border="0" valign="top" >
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<!-- Comienzo del formulario con los campos -->	
	<html:form  action="${path}"  method="post">
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "modo" value = "insertar"/>
		<html:hidden property = "idPersona" value ="" />


	<!-- INICIO: CAMPOS DEL REGISTRO -->
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.turno"/>&nbsp;(*)
			</td>		
			<td colspan="3">
				<siga:ComboBD nombre = "idTurno" tipo="turnos" clase="boxCombo" obligatorio="false" accion="Hijo:idGuardia" parametro="<%=dato%>" ancho="400"/>
			</td>		
		</tr>
		<tr>
			<td  class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.guardia"/>
			</td>		
			<td colspan="3">
				<siga:ComboBD nombre = "idGuardia" tipo="guardias" clase="boxCombo" obligatorio="false" hijo="t" ancho="400"/> 
			</td>		
		</tr>

		
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.fecha"/>&nbsp;(*)
			</td>		
			<td>				
				<siga:Fecha nombreCampo="fecha"></siga:Fecha>
			</td>
			<td class="labelText" colspan="2">
				<html:radio name="SaltosYCompensacionesForm" property="salto" value="S"></html:radio>				
				&nbsp;
				<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.salto"/>
				<html:radio name="SaltosYCompensacionesForm" property="salto" value="C"></html:radio>		
				&nbsp;
				<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.compensacion"/>
			</td>
		</tr>		
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.motivos"/>
			</td>		
			<td colspan="3">
				<html:textarea name="SaltosYCompensacionesForm" property="motivos"  onChange="cuenta(this,1024)" cols="65" rows="2" style="overflow:auto" style="width=400;height=80" onkeydown="cuenta(this,1024);" styleClass="boxCombo" value="" readOnly="false"></html:textarea>
			</td>		
		</tr>	
		<tr>
		<td>
		&nbsp;
		</td>
		</tr>	
		<tr>
			<td class="labelText">
				<siga:Idioma key='gratuita.seleccionColegiadoJG.literal.colegiado'/>
			</td>
			<td colspan="3">
				<input type="text" name="nombreMostrado" class="boxConsulta" readonly value="" style="width:'400px';">
			</td>	
		</tr>
		<tr>
			<td colspan="4">
				<html:hidden property="flagSalto" value=""></html:hidden>
				<html:hidden property="flagCompensacion" value=""></html:hidden>
				<html:hidden property="numeroLetrado" value=""></html:hidden>
					<siga:BusquedaSJCS	
						propiedad="seleccionLetrado" 
						botones="M"
						concepto="SALTOSCOMP" 
						operacion="Asignacion" 
						nombre="SaltosYCompensacionesForm" 
						campoTurno="idTurno" 
						campoGuardia="idGuardia"
						campoFecha="fecha"
						campoPersona="idPersona" 
						campoColegiado="numeroLetrado" 
						mostrarNColegiado="true"
						campoNombreColegiado="nombreMostrado" 
						campoFlagSalto="flagSalto" 
						campoFlagCompensacion="flagCompensacion" 
						modo="nuevo"
					/>
			</td>
		</tr>
	</table>
	</html:form>			
	
	
	
	<!-- Formulario para rellenar el nColegiado desde el action de censo -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,C" modal="P" />
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
			
		
		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			//Valido e inserto:
			sub();
			if (document.forms[0].idTurno.value==""){
				alert('<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.error1"/>');
				fin();
				return false;
 			}else if (validateSaltosYCompensacionesNuevoForm(document.SaltosYCompensacionesForm)) {
					document.forms[0].modo.value = "insertar";
					document.forms[0].target = "submitArea";							
					document.forms[0].submit();	
				}else{
				
					fin();
					return false;
				} 
		}

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

<!-- FIN ******* CAPA DE PRESENTACION ****** -->

	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>