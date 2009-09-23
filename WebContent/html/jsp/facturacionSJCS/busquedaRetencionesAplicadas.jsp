<!-- busquedaRetencionesAplicadas.jsp -->

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
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.*"%>

<% 

	UsrBean usr=(UsrBean)session.getAttribute("USRBEAN");
	//String dato[] = {(String)usr.getLocation()};
	

%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
	<script src="<html:rewrite page="/html/js/calendarJs.jsp"/>" type="text/javascript"></script>	
	<script src="<html:rewrite page="/html/jsp/general/validacionSIGA.jsp"/>" type="text/javascript"></script>	
	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:TituloExt titulo="factSJCS.busquedaRetAplicadas.literal.titulo"  localizacion="factSJCS.busquedaRetAplicadas.literal.localizacion"/>
	
</head>

<body onLoad="ajusteAlto('resultado');">

	<!-- INICIO: FORMULARIO DE BUSQUEDA DE CLIENTES -->
	
	<!-- FIN: FORMULARIO DE BUSQUEDA DE CLIENTES -->	
	<html:form action = "/FCS_BusquedaRentencionesAplicadas.do" method="POST" target="resultado">

		<html:hidden property="modo"/>
		<html:hidden property="letrado"/>
		
		
		<table width="100%" border="0">
			<tr>
				<td>
					<siga:BusquedaPersona tipo="colegiado" titulo='<%=UtilidadesString.getMensajeIdioma(usr, "factSJCS.busquedaRetAplicadas.literal.letrado")%>' idPersona="letrado" >
					</siga:BusquedaPersona>
				</td>
			</tr>
			<tr>
				<td>
					<siga:ConjCampos leyenda="factSJCS.busquedaRetAplicadas.literal.fechasRetenciones">
						<table>
							<tr>
								
								<td class="labelText">

									<siga:Idioma key="factSJCS.busquedaRetAplicadas.literal.fechaDesde"/>

								</td>
								<td>
									<html:text property="fechaDesde"  size="10" styleClass="box" readOnly="true" />
									&nbsp;
									<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
						
								</td>
								<td class="labelText">
									<siga:Idioma key="factSJCS.busquedaRetAplicadas.literal.fechaHasta"/>
								</td>
								<td>
									<html:text property="fechaHasta"  size="10" styleClass="box" readOnly="true" />
									&nbsp;
									<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
								</td>
								<td class="labelText">
									
								</td>
								<td>	
																	
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
				<tr>
					<td>
						<siga:ConjCampos leyenda="factSJCS.busquedaRetAplicadas.literal.datosRetencion">
							<table width="100%">
							<tr>
								
								
								<td class="labelText">
									<siga:Idioma key="factSJCS.busquedaRetAplicadas.literal.tipoRetencion"/>
								</td>
								<td>	
								
								<select name="idTipoRetencion" class="boxCombo">
									<option value="" selected></option>
									<option value="<%=ClsConstants.TIPO_RETENCION_PORCENTAJE%>"><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.porcentual"/></option>
									<option value="<%=ClsConstants.TIPO_RETENCION_IMPORTEFIJO%>"><siga:Idioma key="FactSJCS.mantRetencionesJ.literal.importeFijo"/></option>
								</select>
									
								</td>	
								<td class="labelText">
									<siga:Idioma key="factSJCS.busquedaRetAplicadas.literal.destinatarioRetencion"/>
								</td>
								<td >	
									<siga:ComboBD nombre="idDestinatario" tipo="destinatariosRetencionesFCS" ancho="300" clase="boxCombo" filasMostrar="1" seleccionMultiple="false" obligatorio="false" parametro="<%=new String[]{(String)usr.getLocation()}%>"/>
								</td>	
								<td width="20%">
								</td>
		
							</tr>
												<tr>
								
								
								<td class="labelText">
									<siga:Idioma key="factSJCS.busquedaRetAplicadas.literal.abonoRelacionado"/>
								</td>
								<td>	
									<html:text property="abonoRelacionado"  size="10" styleClass="box" />
								</td>	
								<td class="labelText">
									<siga:Idioma key="factSJCS.busquedaRetAplicadas.literal.pagoRelacionado"/>
								</td>
								<td >	
								<html:text property="pagoRelacionado"  size="10" styleClass="box" />
									
								</td>	
								<td>
								</td>
		
							</tr>
							
							</table>
						</siga:ConjCampos>
					
					</td>
				
				</tr>
				



		</table>
		
		
	</html:form>

	<br>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- Formularios auxiliares para la busqueda de persona-->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<html:hidden property="actionModal" value=""/>
		<html:hidden property="modo" value="abrirBusquedaModal"/>
		
	</html:form>
		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		function buscar ()
		{
			sub();
			document.BusquedaRetencionesAplicadasForm.modo.value = "buscar";
			document.BusquedaRetencionesAplicadasForm.submit();
			
			/*if (document.InformeJustificacionMasivaForm.letrado.value) {
				document.InformeJustificacionMasivaForm.modo.value = "buscar";
				document.InformeJustificacionMasivaForm.submit();
			}else{
				alert ("<siga:Idioma key="factSJCS.busquedaRetAplicadas.mensaje.aviso.letradoRequerido"/>");
				fin();
				return false;
					
			}*/
			//fin();
		}
	

		  function generaExcel()
		{
			sub();
			document.BusquedaRetencionesAplicadasForm.modo.value ="generaExcel";
			document.BusquedaRetencionesAplicadasForm.submit();
			
			fin();
			
		}

	</script>

	<siga:ConjBotonesBusqueda   botones="B,ge"  titulo=""/>

	<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" style="display:none"></iframe>

	<iframe align="center" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>
	
	
	
	
  </body>
  
</html>