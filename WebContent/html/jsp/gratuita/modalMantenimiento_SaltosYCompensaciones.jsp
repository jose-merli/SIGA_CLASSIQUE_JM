<!DOCTYPE html>
<html>
<head>
<!-- modalMantenimiento_SaltosYCompensaciones.jsp -->

<!-- CABECERA JSP -->
<%@page import="com.siga.Utilidades.UtilidadesFecha"%>
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ScsSaltosCompensacionesBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();

	//Datos:
	String modo = (String)request.getAttribute("modo");
	Hashtable datosIniciales = new Hashtable();
	datosIniciales = (Hashtable)request.getAttribute("DATOSINICIALES");
	String turno = UtilidadesHash.getString(datosIniciales,"NOMBRETURNO");
	String guardia = UtilidadesHash.getString(datosIniciales,"NOMBREGUARDIA");
	String letrado = UtilidadesHash.getString(datosIniciales,"LETRADO");
	
	//Datos de la Tabla:
	String idInstitucion = UtilidadesHash.getString(datosIniciales,ScsSaltosCompensacionesBean.C_IDINSTITUCION);
	String idTurno = UtilidadesHash.getString(datosIniciales,ScsSaltosCompensacionesBean.C_IDTURNO);
	String idSaltosTurno = UtilidadesHash.getString(datosIniciales,ScsSaltosCompensacionesBean.C_IDSALTOSTURNO);
	String idGuardia = UtilidadesHash.getString(datosIniciales,ScsSaltosCompensacionesBean.C_IDGUARDIA);
	String idPersona = UtilidadesHash.getString(datosIniciales,ScsSaltosCompensacionesBean.C_IDPERSONA);
	String salto = UtilidadesHash.getString(datosIniciales,ScsSaltosCompensacionesBean.C_SALTOCOMPENSACION);
	String fecha = UtilidadesHash.getString(datosIniciales,ScsSaltosCompensacionesBean.C_FECHA);	
	if(fecha==null || fecha.equalsIgnoreCase("")){
		fecha=GstDate.getHoyJsp();
	}
	String motivos = UtilidadesHash.getString(datosIniciales,ScsSaltosCompensacionesBean.C_MOTIVOS);
	String fechaCumplimiento = UtilidadesHash.getString(datosIniciales,ScsSaltosCompensacionesBean.C_FECHACUMPLIMIENTO);
	if (fechaCumplimiento!=null && !fechaCumplimiento.equals(""))
		fechaCumplimiento = GstDate.getFormatedDateShort(usr.getLanguage(),fechaCumplimiento);

	String estilo="box";
	if(modo.equalsIgnoreCase("VER")) {
		estilo="boxConsulta";
	}

	//Para el Combo de Turnos
	String dato[] = {(String)usr.getLocation()};
%>

<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="SaltosYCompensacionesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>	
</head>

<body>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" height="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.titulo"/>
			</td>
		</tr>
	</table>

	<fieldset>	
		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->
		<table class="tablaCentralCamposPeque" align="center"  border="0">
	
			<!-- Comienzo del formulario con los campos -->	
			<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
			<html:form  action="${path}"  method="post">
<% 
				if(modo.equalsIgnoreCase("NUEVO")) { 
%>
					<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
					<html:hidden property = "modo" value = "insertar"/>
					<html:hidden property = "idPersona" value ="" />	
<%  
				} else { 
%>
					<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
					<html:hidden property = "modo" value = "modificar"/>		
					<html:hidden property = "idInstitucion" value = "<%=idInstitucion%>"/>
					<html:hidden property = "idTurno" value = "<%=idTurno%>"/>
					<html:hidden property = "idGuardia" value = "<%=idGuardia%>"/>
					<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
					<html:hidden property = "idSaltosTurno" value = "<%=idSaltosTurno%>"/>
					<html:hidden property = "salto" value = "<%=salto%>"/>
<% 
				} 
%>
				
				<!-- INICIO: CAMPOS DEL REGISTRO -->
				<!-- SUBCONJUNTO DE DATOS -->
<% 
				if(modo.equalsIgnoreCase("NUEVO")) { 
%>
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.turno"/>&nbsp;(*)
						</td>		
						<td colspan="3">
							<siga:ComboBD nombre = "idTurno" tipo="turnosSinBaja" clase="boxCombo" obligatorio="false" accion="Hijo:idGuardia" parametro="<%=dato%>" ancho="380"/>
						</td>		
					</tr>
					
					<tr>
						<td  class="labelText">
							<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.guardia"/>
						</td>		
						<td colspan="3">
							<siga:ComboBD nombre = "idGuardia" tipo="guardias" clase="boxCombo" obligatorio="false" hijo="t" ancho="380"/> 
						</td>		
					</tr>
		
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.fecha"/>&nbsp;(*)
						</td>		
						<td nowrap>
						 	<siga:Fecha nombreCampo="fecha" valorInicial="<%=fecha%>" readOnly="true" posicionX="50" posicionY="50" />
							
						</td>
						
						<td class="labelText" colspan="2">
							<html:radio name="SaltosYCompensacionesForm" property="salto" value="S" />											
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.salto"/>
							&nbsp;
							<html:radio name="SaltosYCompensacionesForm" property="salto" value="C" />		
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.compensacion"/>			
						</td>
					</tr>		
					
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.motivos"/>
						</td>		
						<td colspan="3">
							<html:textarea name="SaltosYCompensacionesForm" property="motivos"  
								onChange="cuenta(this,1024)" onKeyDown="cuenta(this,1024);"
								style="overflow-y:auto; overflow-x:hidden; width:380px; height:70px; resize:none;" styleClass="boxCombo" 
								value="" readOnly="false"></html:textarea>
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
							<input type="text" name="nombreMostrado" class="boxConsulta" readonly value="" style="width:380px;">
						</td>	
					</tr>
					
					<tr>
						<td colspan="4">
							<html:hidden property="flagSalto" value="" />
							<html:hidden property="flagCompensacion" value="" />
							<html:hidden property="numeroLetrado" value="" />
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
								modo="nuevo"/>
						</td>
					</tr>
		
<% 
				} else { 
%>
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.turno"/>
						</td>
						<td colspan="3">
							<html:text name="SaltosYCompensacionesForm" property="turnosSinBaja" maxlength="100" styleClass="boxConsulta" value="<%=turno%>" readOnly="true" style="width:380px" />
						</td>
					</tr>
					
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.guardia"/>
						</td>
						<td colspan="3">
							<html:text name="SaltosYCompensacionesForm" property="guardia" maxlength="100" styleClass="boxConsulta" value="<%=guardia%>" readOnly="true" style="width:380px" />
						</td>
					</tr>
					
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.letrado"/>
						</td>
						<td>
							<html:text name="SaltosYCompensacionesForm" property="letrado" size="10" maxlength="300" styleClass="boxConsulta" value="<%=letrado%>" readOnly="true" />
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.tipo"/>
						</td>
						<td class="labelTextValor">
<% 
							if(salto.equals("S")) {
%>
								<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.salto"/>
<% 
							} else { 
%>
								<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.compensacion"/>
<% 
							} 
%>
						</td>
					</tr>		
					
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.fecha"/> (*)
						</td>
						<td nowrap>							
<% 
							if(modo.equalsIgnoreCase("EDITAR")) { 
%>
								<siga:Fecha nombreCampo="fecha" posicionY="100" posicionX="100" readOnly="true" valorInicial="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fecha)%>" />
<% 
							} else {  
%>
								<html:text name="SaltosYCompensacionesForm" property="fecha" size="10" styleClass="boxConsulta" value="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fecha)%>" readOnly="true" />
<% 
							}  
%>
						</td>
						
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.fechaUso"/>
						</td>
						<td nowrap>
<% 
							if(modo.equalsIgnoreCase("EDITAR")) { 
%>
								<siga:Fecha nombreCampo="fechaCumplimiento" readOnly="true"  valorInicial="<%=fechaCumplimiento%>" />
<% 
							} else {  
%>
								<html:text name="SaltosYCompensacionesForm" property="fechaCumplimiento" size="10" styleClass="boxConsulta" value="<%=fechaCumplimiento%>" readOnly="true" />
<% 
							}  
%>													
						</td>
					</tr>	
			
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.motivos"/>
						</td>
						<td colspan="3">
<% 
							if(modo.equalsIgnoreCase("EDITAR")) { 
%>
								<html:textarea name="SaltosYCompensacionesForm" property="motivos"  
									onChange="cuenta(this,1024)" onKeyDown="cuenta(this,1024);"
									style="overflow-y:auto; overflow-x:hidden; width:380px; height:70px; resize:none;" styleClass="box" 
									value="<%=motivos%>" readOnly="false"></html:textarea>
<% 
							} else { 
%>
								<html:textarea name="SaltosYCompensacionesForm" property="motivos"  
									style="overflow-y:auto; overflow-x:hidden; width:380px; height:70px; resize:none;" styleClass="boxConsulta" 
									value="<%=motivos%>" readOnly="true"></html:textarea>
<% 
							} 
%>
						</td>		
					</tr>			
<% 
				} 
%>			
			</html:form>
		</table>				
	</fieldset>	
	
	<!-- Formulario para rellenar el nColegiado desde el action de censo -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
	</html:form>
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->

	<siga:ConjBotonesAccion botones="Y,C" modal="P" modo="<%=modo%>" />

	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
		
		//Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {	
			<% if(modo.equalsIgnoreCase("NUEVO")) { %>

				//Valido e inserto:
				sub();
				if (document.forms[0].idTurno.value==""){
					alert('<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.error1"/>');
					fin();
					return false;
				}else if (document.forms[0].idPersona.value==""){
					alert('<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.error3"/>');
					fin();
					return false;
	 			}else if (validateSaltosYCompensacionesForm(document.SaltosYCompensacionesForm)) {
					document.forms[0].modo.value = "insertar";
					document.forms[0].target = "submitArea";							
					document.forms[0].submit();	
				}else{
					
					fin();
					return false;
				}
				
			<% } else { %>

				//Valido y modifico:
				sub();	
				if (validateSaltosYCompensacionesForm(document.SaltosYCompensacionesForm)) {
					document.forms[0].modo.value = "modificar";
					document.forms[0].target = "submitArea";							
					document.forms[0].submit();	
				}else{
				
					fin();
					return false;
				}
			<% } %>	
		}

		//Asociada al boton Cerrar
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}		
		
<%
		if (modo.equalsIgnoreCase("NUEVO")) {
%>			
			jQuery(document).keydown(function (e) {
				if (e.keyCode === 8) {
			    	var nombreElemento = jQuery(e.target).attr("name");
			    	if (nombreElemento!="fecha" && nombreElemento!="motivos") {			        
			            return false;
			        }
			    }
			});
<%
		}
%>
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