<!-- modalConsulta_DefinirCalendarioGuardia.jsp-->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
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

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//Datos propios del jsp:
	String numeroColegiado = request.getAttribute("NUMEROCOLEGIADO")==null?"":(String)request.getAttribute("NUMEROCOLEGIADO");
	String nombre = request.getAttribute("NOMBRE")==null?"":(String)request.getAttribute("NOMBRE");
	
	String fechaS = request.getAttribute("FECHASUSTITUCION")==null?"-":(String)request.getAttribute("FECHASUSTITUCION");
	String letradoS = request.getAttribute("LETRADOSUSTITUIDO")==null?"-":(String)request.getAttribute("LETRADOSUSTITUIDO");
	String comentario = request.getAttribute("COMENTARIO")==null?"-":(String)request.getAttribute("COMENTARIO");
	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	
</head>

<body>

<!-- TITULO -->
<!-- Barra de titulo actualizable desde los mantenimientos -->
<table class="tablaTitulo" cellspacing="0" heigth="32">
<tr>
	<td id="titulo" class="titulosPeq">
			<siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.tituloGuardia"/>
	</td>
</tr>
</table>
	

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposPeque" align="center">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_DefinirCalendarioGuardia.do" method="post">
		<html:hidden property = "modo" value = ""/>

	<!-- INICIO: CAMPOS DEL REGISTRO -->
	<tr>			
	<td>
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.infoLetrado">
		<table class="tablaCampos" align="center" cellspacing="0" cellpadding="0">
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.letrado"/>
			</td>		
			<td class="labelText">
				<%=nombre%>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>
			</td>		
			<td class="labelText">
				<%=numeroColegiado%>
			</td>
		</tr>	
		<tr>
			<td class="labelText">
				<siga:Idioma key="certificados.solicitudes.ventanaEdicion.comentario"/>
			</td>		
			<td class="labelTextValor">
				<%=comentario%>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.literal.letrado.sustituido"/>
			</td>		
			<td class="labelTextValor">
				<%=letradoS%>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.literal.fecha.sustitucion"/>
			</td>		
			<td class="labelTextValor">
				<%=fechaS%>
			</td>
		</tr>		
		</table>
	</siga:ConjCampos>		
	</td>
	</tr>
	<tr>
	<td>
	<siga:ConjCampos leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.infoGuardia">
		<table class="tablaCampos" align="center" cellspacing="0" cellpadding="0" border="0">
		<tr>
			<td class="labelText" width="">
				<siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.fechaInicio"/>
			</td>
			<td>
				<html:text name="DefinirCalendarioGuardiaForm" property="fechaInicio" size="10" styleClass="boxConsulta" readonly="true"></html:text>
			</td>		
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.fechaFin"/>
			</td>
			<td>
				<html:text name="DefinirCalendarioGuardiaForm" property="fechaFin" size="10" styleClass="boxConsulta" readonly="true"></html:text>
			</td>		
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="gratuita.modalMantenimiento_DefinirCalendarioGuardia.literal.diasGuardia"/>
			</td>
			<td>
				<html:textarea name="DefinirCalendarioGuardiaForm" property="guardiasPeriodo" cols="10" rows="3" style="overflow:auto" styleClass="boxConsulta" readOnly="true" ></html:textarea>				
			</td>		
		</tr>
		</table>
	</siga:ConjCampos>				
	</td>
	</tr>
	</table>
	</html:form>			
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->
	<siga:ConjBotonesAccion botones="C" modal="P"/>
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>