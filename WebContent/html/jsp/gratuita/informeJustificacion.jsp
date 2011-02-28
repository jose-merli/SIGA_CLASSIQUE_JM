<!-- informeJustificacionMasiva.jsp -->

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
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>


<% 

	UsrBean usr=(UsrBean)session.getAttribute("USRBEAN");
	String app=request.getContextPath();
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
	<script src="<html:rewrite page="/html/js/calendarJs.jsp"/>" type="text/javascript"></script>	
	<script src="<html:rewrite page="/html/jsp/general/validacionSIGA.jsp"/>" type="text/javascript"></script>	
	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo titulo="gratuita.informeJustificacionMasiva.literal.titulo"  localizacion="factSJCS.informes.ruta"/>
	
</head>

<body onLoad="ajusteAlto('resultado');inicio();">

	<!-- INICIO: FORMULARIO DE BUSQUEDA DE CLIENTES -->
	
	<!-- FIN: FORMULARIO DE BUSQUEDA DE CLIENTES -->	
	<html:form action = "/JGR_InformeJustificacion.do" method="POST" target="submitArea21">
		
		<html:hidden property="modo"/>
		<html:hidden property="idPersona"/>
		<html:hidden property="mostrarTodas"/>
		<html:hidden property="activarRestriccionesFicha"/>
		
		
		<table width="100%" border="0">
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.informeJustificacionMasiva.leyenda.datosJustificacion">
				
						<table>
							
							<tr>
							    <td colspan ="4">
								    <siga:BusquedaPersona tipo="colegiado" idPersona="idPersona" >
									</siga:BusquedaPersona>
								</td>
							</tr>
							<tr>
								<td class="labelText" >
									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.mostrarHistorico"/>
									&nbsp;
								</td>
								<td class="labelText">	
									<input type="checkbox" name="mostrarSoloPendientes" value="on">
									&nbsp;
								</td>
									
								<td class="labelText" >
							   		<siga:Idioma key="gratuita.informeJustificacionMasiva.activarRestricciones.literal"/>&nbsp;									
										<input type="checkbox" name="activarRestricciones" value="on" >
								</td>
								<td>
								&nbsp;
								</td>
									
							</tr>
							<tr>
							<td colspan ="4">
							
								<table>
									<tr>
								
										<td width="20%">
										</td>
										<td width="18%">
										</td>
										<td width="5%">
										</td>
										<td width="18%">
										</td>
										<td width="5%">
										</td>
										<td width="10">	
										
										</td>	
										<td width="5%">
										</td>
										<td width="14%">	
										
										</td>	
										
				
									</tr>
									<tr>
										<td class="labelText">
			
												<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.fecha.Justif"/>&nbsp;<siga:Idioma key="general.literal.desde"/>
			
										</td>
										<td>
											<html:text property="fechaJustificacionDesde"  size="10" styleClass="box" readOnly="true" />
											&nbsp;
											<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaJustificacionDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
									
										</td>
										<td class="labelText">
											<siga:Idioma key="general.literal.hasta"/>
										</td>
										<td>
											<html:text property="fechaJustificacionHasta"  size="10" styleClass="box" readOnly="true" />
												&nbsp;
												<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaJustificacionHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
										</td>
										
										
										<td class="labelText" >
												<siga:Idioma key="gratuita.editarDesigna.literal.estado"/>
										</td>
										<td>	
											<Select name="estado" class="boxCombo">
															<option value=''></option>
															<option value='V' ><siga:Idioma key="gratuita.estadoDesignacion.activo"/></option>
															<option value='F' ><siga:Idioma key="gratuita.estadoDesignacion.finalizado"/></option>
															<option value='A' ><siga:Idioma key="gratuita.estadoDesignacion.anulado"/></option>
											</Select>
										</td>
										<td class="labelText">
												<siga:Idioma key="gratuita.busquedaDesignas.literal.actuacionesValidadas"/>
										</td>
										<td >	
											<Select name="actuacionesPendientes" class="boxCombo">
												<option value='' ></option>
											
												<option value='No'><siga:Idioma key="general.no"/></option>
												<option value='Si' ><siga:Idioma key="general.yes"/></option>
												<option value='SinActuaciones'><siga:Idioma key="gratuita.busquedaDesignas.literal.sinActuaciones"/></option>
											</Select>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						</table>
							
				</siga:ConjCampos>
				</td>
			</tr>
			<tr>
				<td>
					<siga:ConjCampos  leyenda="gratuita.busquedaDesignas.literal.datosDesigna">
						<table>
						
							<tr>
								
								<td width="20%">
								</td>
								<td width="18%">
								</td>
								<td width="5%">
								</td>
								<td width="18%">
								</td>
								<td width="5%">
								</td>
								<td width="10">	
								
								</td>	
								<td width="5%">
								</td>
								<td width="14%">	
								
								</td>	
								
		
							</tr>
						
						
							<tr>
								
								<td class="labelText">

									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.fechaSalida"/>&nbsp;<siga:Idioma key="general.literal.desde"/>

								</td>
								<td>
									<html:text property="fechaDesde"  size="10" styleClass="box" readOnly="true" />
									&nbsp;
									<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
						
								</td>
								<td class="labelText">
									<siga:Idioma key="general.literal.hasta"/>
								</td>
								<td>
									<html:text property="fechaHasta"  size="10" styleClass="box" readOnly="true" />
									&nbsp;
									<a id="iconoCalendarioA" onClick="return showCalendarGeneral(fechaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<html:rewrite page="/html/imagenes/calendar_hi.gif"/>',1);"><img src="<html:rewrite page="/html/imagenes/calendar.gif"/>" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0"></a>
								</td>
							
								
								
								<td class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.nombre"/>
								</td>
								<td>	
									<html:text  property="interesadoNombre" size="15" maxlength="100" styleClass="box" ></html:text>
								</td>	
								<td class="labelText">
									<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.apellidos"/>
								</td>
								<td >	
									<html:text  property="interesadoApellidos" size="30" maxlength="100" styleClass="box" ></html:text>
								</td>	
								
		
							</tr>
							
							
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
						

		</table>
		
		
	</html:form>

	<br>
	
		<siga:ConjBotonesBusqueda   botones="ij"  titulo=""/>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	
	
	<!-- Formularios auxiliares para la busqueda de persona-->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<html:hidden property="actionModal" value=""/>
		<html:hidden property="modo" value="abrirBusquedaModal"/>
		
	</html:form>
	


	
	


	
	
	
<script language="JavaScript">
function inicio ()
{
		document.getElementById("mostrarSoloPendientes").checked = "checked";
}
	function informeJustificacion ()
	{
		sub();
		document.InformeJustificacionMasivaForm.activarRestriccionesFicha.value =document.getElementById("activarRestricciones").checked;
		document.InformeJustificacionMasivaForm.mostrarTodas.value =document.getElementById("mostrarSoloPendientes").checked;
		document.InformeJustificacionMasivaForm.modo.value = "informe";
		//document.InformeJustificacionMasivaForm.submit();
		var f = document.InformeJustificacionMasivaForm.name;	
		document.frames.submitArea21.location = '<html:rewrite page="/html/jsp/general/loadingWindowOpener.jsp"/>?formName=' + f + '&msg=messages.wait';
			
			
	}
	

	</script>	
	<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" style="display:none"></iframe>
	<iframe name="submitArea21" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	
  </body>
  
</html>