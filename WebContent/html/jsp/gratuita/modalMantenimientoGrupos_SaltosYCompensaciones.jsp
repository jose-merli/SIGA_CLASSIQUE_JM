<!DOCTYPE html>
<html>
<head>
<!-- modalMantenimientoGrupos_SaltosYCompensaciones.jsp -->

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

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.ScsSaltoCompensacionGrupoBean"%>
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
	String idInstitucion = UtilidadesHash.getString(datosIniciales,ScsSaltoCompensacionGrupoBean.C_IDINSTITUCION);
	String idSaltoCompensacionGrupo = UtilidadesHash.getString(datosIniciales,ScsSaltoCompensacionGrupoBean.C_IDSALTOCOMPENSACIONGRUPO);
	String idTurno = UtilidadesHash.getString(datosIniciales,ScsSaltoCompensacionGrupoBean.C_IDTURNO);
	String idGuardia = UtilidadesHash.getString(datosIniciales,ScsSaltoCompensacionGrupoBean.C_IDGUARDIA);
	String idGrupoGuardia = UtilidadesHash.getString(datosIniciales,ScsSaltoCompensacionGrupoBean.C_IDGRUPOGUARDIA);
	String salto = UtilidadesHash.getString(datosIniciales,ScsSaltoCompensacionGrupoBean.C_SALTOCOMPENSACION);
	String fecha = UtilidadesHash.getString(datosIniciales,ScsSaltoCompensacionGrupoBean.C_FECHA);	
	String motivos = UtilidadesHash.getString(datosIniciales,ScsSaltoCompensacionGrupoBean.C_MOTIVO);
	String fechaCumplimiento = UtilidadesHash.getString(datosIniciales,ScsSaltoCompensacionGrupoBean.C_FECHACUMPLIMIENTO);
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
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>

<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="SaltosYCompensacionesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	
	<!--Step 2 -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

	<script>
		jQuery.noConflict();
	</script>
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
			<html:form action="${path}"  method="post">
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
					<html:hidden property = "idGrupoGuardia" value = "<%=idGrupoGuardia%>"/>
					<html:hidden property = "idSaltoCompensacionGrupo" value = "<%=idSaltoCompensacionGrupo%>"/>
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
							<siga:ComboBD nombre = "idTurno" tipo="turnosSinBaja" clase="boxCombo" obligatorio="true" accion="Hijo:idGuardia" parametro="<%=dato%>" ancho="500"/>
						</td>		
					</tr>
					
					<tr>
						<td  class="labelText" nowrap>
							<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.guardia"/>&nbsp;(*)
						</td>		
						<td colspan="3">
							<siga:ComboBD nombre = "idGuardia" tipo="cmbGuardiasSyC" clase="boxCombo" obligatorio="true" hijo="t" accion="Hijo:idGrupoGuardia;" ancho="500"/> 
						</td>		
					</tr>
					
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key='gratuita.modalNuevo_SaltosYCompensaciones.literal.grupo'/>&nbsp;(*)
						</td>
						<td colspan="3">
							<siga:ComboBD nombre="idGrupoGuardia" tipo="cmbGruposSyC" clase="boxCombo" obligatorio="true" hijo="t" accion="parent.forzarAjax();"/>
						</td>	
					</tr>
					
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.letrados"/>
						</td>
						<td colspan="3">
							<html:textarea property="letrado" 
							style="overflow-y:auto; overflow-x:hidden; width:380px; height:70px; resize:none;" styleClass="boxConsulta"
							readOnly="true"></html:textarea>
						</td>
					</tr>		
					
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.fecha"/>&nbsp;(*)
						</td>		
						<td nowrap>
							<siga:Fecha nombreCampo="fecha" posicionY="50" posicionX="50" readOnly="true" />
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
						<td>
							&nbsp;
						</td>
					</tr>
						
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.motivos"/>
						</td>		
						<td colspan="3">
							<html:textarea name="SaltosYCompensacionesForm" property="motivos"  
							onChange="cuenta(this,1024)" onKeyDown="cuenta(this,1024);" 
							style="overflow-y:auto; overflow-x:hidden; width:500px; height:100px; resize:none;" styleClass="boxCombo" 
							value="" readOnly="false"></html:textarea>
						</td>		
					</tr>	
					
					<tr>
						<td>
							&nbsp;
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
							<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensaciones.literal.grupoSYC"/>
						</td>
						<td>
							<html:text name="SaltosYCompensacionesForm" property="letrado" size="10" maxlength="300" styleClass="boxConsulta" value="<%=letrado%>" readOnly="true" />
						</td>
						
						<td class="labelText">
							<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.tipo"/>
						</td>
						<td class="labelTextValor" nowrap>
<% 							
							if(salto.equals("S")) {
%>
								<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.saltoGrupo"/>
<% 
							} else if(salto.equals("C")) { 
%>
								<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.compensacionGrupo"/>
<% 
							} 
%>
						</td>
					</tr>	
						
					<tr>
						<td class="labelText" nowrap>
							<siga:Idioma key="gratuita.modalMantenimiento_SaltosYCompensaciones.literal.fecha"/>&nbsp;(*)
						</td>
						<td nowrap>
<% 
							if(modo.equalsIgnoreCase("EDITAR")) { 
%>
								<siga:Fecha nombreCampo="fecha" valorInicial="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fecha)%>" disabled="false" />
<% 
							}  else {
%>
								<siga:Fecha nombreCampo="fecha" valorInicial="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fecha)%>" disabled="true" />
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
									onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"
									style="overflow-y:auto; overflow-x:hidden; width:380px; height:100px; resize:none;" styleClass="box" 
									value="<%=motivos%>" readOnly="false"></html:textarea>
<% 
							} else { 
%>
								<html:textarea name="SaltosYCompensacionesForm" property="motivos" 
									style="overflow-y:auto; overflow-x:hidden; width:380px; height:100px; resize:none;" styleClass="boxConsulta" 
									value="<%=motivos%>" readOnly="true" />
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
	
		<ajax:updateFieldFromSelect  
			baseUrl="/SIGA${path}.do?modo=getAjaxLetradosInscritos"
		    source="idGrupoGuardia" target="letrado"
			parameters="idTurno={idTurno},idGuardia={idGuardia},idGrupoGuardia={idGrupoGuardia}" />
							
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
	
		function forzarAjax(){
			document.getElementById('idGrupoGuardia').onchange();
		}
	
		//Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {	

			<% if(modo.equalsIgnoreCase("NUEVO")) { %>

				//Valido e inserto:
				sub();
				if (document.forms[0].idGuardia.value==""){
					alert('<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensacionesGrupo.literal.error1"/>');
					fin();
					return false;
					
				}else if (document.forms[0].idGrupoGuardia.value==""){
					alert('<siga:Idioma key="gratuita.modalNuevo_SaltosYCompensacionesGrupo.literal.error2"/>');
					fin();
					return false;
				
				}else if (validateSaltosYCompensacionesForm(document.SaltosYCompensacionesForm)) {
					document.forms[0].modo.value = "insertarGrupos";
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
					document.forms[0].modo.value = "modificarGrupos";
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