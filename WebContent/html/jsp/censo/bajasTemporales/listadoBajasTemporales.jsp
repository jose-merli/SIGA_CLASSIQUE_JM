<!DOCTYPE html>
<html>
<head>
<!-- listadoBajasTemporales.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>

	<!--Step 2 -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>
 
	<!-- Step 4 -->
	<!-- Importar el js propio de la pagina-->
	<script src="<html:rewrite page='/html/js/censo/bajasTemporales.js'/>" type="text/javascript"></script>
	
	<script>			
		function borrar(fila){
			if (confirm('<siga:Idioma key="messages.deleteConfirmation"/>')){
				return borrarFila(fila);
			}			
		}		
	</script>
 </head>

<body onload="onInit();">

	<% Date fechaActual = GstDate.convertirFecha(UtilidadesBDAdm.getFechaBD(""),ClsConstants.DATE_FORMAT_SHORT_SPANISH);%>

	<siga:Table
		name="bajasTemporales"
		border="1"
		columnSizes="5,8,18,17,16,10,10,8,8"
		columnNames="<input type='checkbox' name='chkGeneral' onclick='marcarDesmarcarTodos(this);'/>,
			censo.bajastemporales.colegiado.numero,
			censo.bajastemporales.colegiado.nombre,
			censo.bajastemporales.tipo,
			censo.bajastemporales.descripcion,
			censo.bajastemporales.fechaInicio,
			censo.bajastemporales.fechaFin,
			censo.bajastemporales.estadoBaja,">		
			
		<logic:empty name="BajasTemporalesForm"	property="bajasTemporales">
			<tr class="notFound">
   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>	 	
		</logic:empty>
		
		<logic:notEmpty name="BajasTemporalesForm"	property="bajasTemporales">
			<logic:iterate name="BajasTemporalesForm" property="bajasTemporales" id="bajaTemporalBean" indexId="index">
				<%index = index-1;%>
				<bean:define id="bajaTemporalForm" name="bajaTemporalBean" property="bajaTemporalForm" type="com.siga.censo.form.BajasTemporalesForm"/>
				<bean:define id="botones" name="bajaTemporalForm" property="botones" type="java.lang.String"/>
				
				<c:set var="fechaActual" value="<%=fechaActual%>"/>
				<c:set var="disab" value=""/>
				
				<%	if(bajaTemporalForm.getFechaHasta()!=null && !bajaTemporalForm.getFechaHasta().equals("")){
						Date fHasta = GstDate.convertirFecha(bajaTemporalForm.getFechaHasta(),ClsConstants.DATE_FORMAT_SHORT_SPANISH);	
						if (fechaActual.compareTo(fHasta) > 0){
							botones = "";%>
							<c:set var="disab" value="disabled"/>
					<% } 
				} %>
				
				<siga:FilaConIconos	fila='<%=String.valueOf(index.intValue()+1)%>'
		  			pintarEspacio="no"
		  			visibleConsulta="no"
		  			botones="<%=botones%>"
		  			clase="listaNonEdit">
		  			
					<td align='center' width='5%'>
						<input type="hidden" id="idInstitucion_<bean:write name='index'/>" value="<bean:write name="bajaTemporalBean" property="idInstitucion" />">
						<input type="hidden" id="idPersona_<bean:write name='index'/>" value="<bean:write name="bajaTemporalBean" property="idPersona" />">
						<input type="hidden" id="colegiadoNumero_<bean:write name='index'/>" value="<bean:write name="bajaTemporalForm" property="colegiadoNumero" />">
						<input type="hidden" id="colegiadoNombre_<bean:write name='index'/>" value="<bean:write name="bajaTemporalForm" property="colegiadoNombre" />">
						<input type="hidden" id="fechaAlta_<bean:write name='index'/>" value="<bean:write name="bajaTemporalBean" property="fechaAlta" />">
						<input type="hidden" id="validado_<bean:write name='index'/>" value="<bean:write name="bajaTemporalBean" property="validado" />">
						<input type="hidden" id="fechaDesde_<bean:write name='index'/>" value="<bean:write name="bajaTemporalBean" property="fechaDesde" />">
						<input type="hidden" id="fechaHasta_<bean:write name='index'/>" value="<bean:write name="bajaTemporalBean" property="fechaHasta" />">
					
						<input type="checkbox" value="<%=String.valueOf(index.intValue()+1)%>" name="chkBajasTemporales" ${disab}>
	 				</td>
	 				
					<td align='center' width='8%'>
						<c:out value="${bajaTemporalForm.colegiadoNumero}"/>
					</td>
					
					<td align='left' width='18%'>
						<c:out value="${bajaTemporalForm.colegiadoNombre}"/>
					</td>
					
					<c:choose>
						<c:when test="${BajasTemporalesForm.situacion=='B'||BajasTemporalesForm.fichaColegial==true}">
							<td align='center' width='17%'>
								<c:out value="${bajaTemporalForm.tipoDescripcion}"/>
							</td>
							
							<td align='left' width='16%'>
								<c:choose>
									<c:when test="${bajaTemporalForm.descripcion!=null && bajaTemporalForm.descripcion!=''}">
										<c:out value="${bajaTemporalForm.descripcion}" />
									</c:when>
									<c:otherwise>
										  &nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							
							<td align='center' width='10%'>
								<c:out value="${bajaTemporalForm.fechaDesde}"/>
							</td>
							<td align='center' width='10%'>
								<c:out value="${bajaTemporalForm.fechaHasta}"/>
							</td>
							<td align='center'  width='8%'>
								<c:out value="${bajaTemporalForm.estadoBajaTxt}"/>
							</td>
						</c:when>
													
						<c:otherwise>
							<td align='center' width='17%'>
								<c:choose>
									<c:when test="${bajaTemporalForm.tipo!=null && bajaTemporalForm.tipo!=''}">
										<c:out value="${bajaTemporalForm.tipo}" />
									</c:when>
									<c:otherwise>
										  &nbsp;
									</c:otherwise>
								</c:choose>							
							</td>
							
							<td align='left' width='16%'>&nbsp;</td>
							<td align='center' width='10%'>&nbsp;</td>
							<td align='center' width='10%'>&nbsp;</td>
							<td align='center'  width='8%'>&nbsp;</td>  
						</c:otherwise>
					</c:choose>	
				</siga:FilaConIconos>
			</logic:iterate>
		</logic:notEmpty>
	</siga:Table>

<c:if test="${BajasTemporalesForm.fichaColegial==true}">
	<input type="hidden" id="colegiadoNumeroFichaColegial"
		value="<bean:write name="BajasTemporalesForm" property="colegiadoNumero" />">
	<input type="hidden" id="colegiadoNombreFichaColegial"
		value="<bean:write name="BajasTemporalesForm" property="colegiadoNombre" />">
	<input type="hidden" id="idPersonaFichaColegial"
		value="<bean:write name="BajasTemporalesForm" property="idPersona" />">
	
	<table class="botonesDetalle" align="center">
		<tr>
			<td style="width: 900px;">&nbsp;</td>
				<!--aalg: se controlan los permisos según el modo de acceso -->
			<c:if test="${BajasTemporalesForm.accion.equals('editar') || (BajasTemporalesForm.accion.equals('ver') && BajasTemporalesForm.usrBean.letrado==true)}">	
				<td class="tdBotones">
					<input type="button" alt="<siga:Idioma key="general.boton.new"/>"  id="idButton" onclick="return accionNuevo(true,'<siga:Idioma key="general.message.seleccionar"/>');" class="button" name="idButton" value="<siga:Idioma key="general.boton.new"/>">
				</td>
				<td class="tdBotones"><input type="button"
					alt="<siga:Idioma key="censo.bajastemporales.boton.aceptarSolicitud"/>"
					  id="idValidarSolicitud"
					onclick="return accionValidarSolicitud('<siga:Idioma key="general.message.seleccionar"/>');" class="button"
					name="idValidarSolicitud"
					value="<siga:Idioma key="censo.bajastemporales.boton.aceptarSolicitud"/>">
				</td>
				<td class="tdBotones"><input type="button"
					alt="<siga:Idioma key="censo.bajastemporales.boton.denegarSolicitud"/>"
					  id="idDenegarSolicitud"
					onclick="return accionDenegarSolicitud('<siga:Idioma key="general.message.seleccionar"/>');" class="button"
					name="idDenegarSolicitud"
					value="<siga:Idioma key="censo.bajastemporales.boton.denegarSolicitud"/>"">
				</td>
			</c:if>
		</tr>
	</table>
	
				<!-- Check para pasar a modo historico donde se muestran los turnos dados de baja -->
			<div style="position:absolute; left:400px;bottom:30px;z-index:99;">
				<table align="center" border="0">
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.gestionInscripciones.vertodas" /> 
							<c:choose>
								<c:when test="${BajasTemporalesForm.incluirRegistrosConBajaLogica != null && BajasTemporalesForm.incluirRegistrosConBajaLogica == 'S'}">
									<input type="checkbox" id="bajaLogica" name="bajaLogica" onclick="incluirRegBajaLogica(this);" checked/>
								</c:when>
								<c:otherwise>
									<input type="checkbox" id="bajaLogica" name="bajaLogica" onclick="incluirRegBajaLogica(this);"/>
								</c:otherwise>
							</c:choose>	
						</td>							
					</tr>
				</table>
			</div>
	

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="${path}"  name="FormBajasTemporales"
		type="com.siga.censo.form.BajasTemporalesForm">
		<html:hidden styleId="modo" property="modo" />
		<html:hidden styleId="idInstitucion" property="idInstitucion" />
		<html:hidden styleId="idPersona" property="idPersona" />
		<html:hidden styleId="colegiadoNombre" property="colegiadoNombre" />
		<html:hidden styleId="colegiadoNumero" property="colegiadoNumero" />
		<html:hidden styleId="fechaAlta" property="fechaAlta" />
		<html:hidden styleId="fechaDesde"   property="fechaDesde" />
		<html:hidden styleId="fechaHasta"   property="fechaHasta" />
		<html:hidden styleId="tipo" property="tipo" />
		<html:hidden styleId="datosSeleccionados" property="datosSeleccionados" />
		<html:hidden styleId="incluirRegistrosConBajaLogica"   property="incluirRegistrosConBajaLogica" />
		<input type="hidden" id="actionModal" name="actionModal" />
	</html:form>
</c:if>
<input type="hidden" id="deshabilitarValidaciones" value="false">
<c:if test="${BajasTemporalesForm.usrBean.letrado==true}">
		<script>
			document.getElementById('deshabilitarValidaciones').value=true;
		</script>
	</c:if>

<script type="text/javascript">

		var messageError='${BajasTemporalesForm.msgError}';

		var messageAviso='${BajasTemporalesForm.msgAviso}';

		if (messageAviso)
			alert(messageAviso);
		if (messageError)
			alert(messageError);
		
if(document.getElementById("chkGeneral")){
	  marcarDesmarcarTodos(document.getElementById("chkGeneral"));
}

if(!document.getElementById('idBusqueda')){
	setLocalizacion('<siga:Idioma key="censo.fichaCliente.sjcs.bajastemporales.localizacion"/>');
	setTitulo('<siga:Idioma key="general.ventana.cgae"/>', '<siga:Idioma key="censo.fichaCliente.sjcs.bajastemporales.cabecera"/>');
}

// Funcion que agrega el concepto de baja logica
function incluirRegBajaLogica(o) {
	if (o.checked) {
		document.FormBajasTemporales.incluirRegistrosConBajaLogica.value = "S";
	} else {
		document.FormBajasTemporales.incluirRegistrosConBajaLogica.value = "N";
	}
	
	document.FormBajasTemporales.idPersona.value="${BajasTemporalesForm.idPersona}";
	document.FormBajasTemporales.modo.value = "verHistorico";
	document.FormBajasTemporales.submit();
}

function marcarDesmarcarTodos(o) 
{ 
	var ele = document.getElementsByName("chkBajasTemporales");
	for (i = 0; i < ele.length; i++) {
		if(ele[i].disabled == false)
			ele[i].checked = o.checked;
	}
}

function refrescarLocal() {	
	document.FormBajasTemporales.target = "_self";	
	document.FormBajasTemporales.submit();
}

</script>
	
</body>


</html>