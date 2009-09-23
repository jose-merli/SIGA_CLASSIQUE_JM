<!-- datosPeriodoGuardias.jsp -->
<!-- 
	 Permite recoger datos para la generacion de cartas de asistencia
	 VERSIONES:
	 miguel.villegas 18-05-2005 
--

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.lang.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();

	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	Hashtable datos=(Hashtable)request.getAttribute("DATOS");

	String	botones="GM,R,C";
%>	

<%@page import="java.util.Properties"%>
<%@page import="java.util.Hashtable"%>
<html>
<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>			

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="DefinirListaGuardiasForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<!--siga:Titulo 
			titulo="censo.busquedaHistorico.literal.titulo1" 
			localizacion="censo.busquedaHistorico.literal.titulo1"/-->
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="informes.listaGuardias.cabecera"/>
				</td>
			</tr>
		</table>

		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
			 que se van a mostrar, para que quepen dentro de la ventana.
			 Los elementos que copieis dentro, que tengan el estilo 
			 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
		-->
		<div id="camposRegistro" class="posicionModalPeque" align="center">

			<!-- INICIO: CAMPOS -->
			<!-- Zona de campos de busqueda o filtro -->

			<table  class="tablaCentralCamposPeque"  align="center">

				<html:form action="/JGR_DefinirListaGuardias.do" method="post" target="submitArea32">	
					<html:hidden property = "modo" value = '<%=(String)datos.get("MODO")%>'/>
					<html:hidden property = "accion" value = '<%=(String)datos.get("ACCION")%>'/>
					<html:hidden property = "idLista" value = '<%=(String)datos.get("IDLISTA")%>'/>
					<html:hidden property = "idInstitucion" value = '<%=(String)datos.get("IDINSTITUCION")%>'/>
					<tr>				
						<td>
							<siga:ConjCampos leyenda="informes.listaGuardias.periodo">							
								<table class="tablaCampos" align="center">	
									<!-- FILA -->
									<tr>				
										<td class="labelText">
											<siga:Idioma key="informes.listaGuardias.desde"/>&nbsp;
										</td>				
										<td>	
											<html:text property="fechaInicio" size="10" styleClass="box" readOnly="true"></html:text>
											<a href='javascript://' onClick="return showCalendarGeneral(fechaInicio);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
												<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0">
											</a>
						  				</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="informes.listaGuardias.hasta"/>&nbsp;
										</td>				
										<td>	
											<html:text property="fechaFin" size="10" styleClass="box" readOnly="true"></html:text>
											<a href='javascript://' onClick="return showCalendarGeneral(fechaFin);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
												<img src="<%=app%>/html/imagenes/calendar.gif" alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  border="0">
											</a>
						  				</td>
									</tr>
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</html:form>	
			</table>
			<!-- FIN: CAMPOS -->

			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			<!-- Aqui comienza la zona de botones de acciones -->

			<!-- INICIO: BOTONES REGISTRO -->
			<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
				 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
				 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
				 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
				 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
				 La propiedad modal dice el tamanho de la ventana (M,P,G)
			-->
		
			<siga:ConjBotonesAccion botones='<%=botones%>' modo=''  modal="P"/>
			
			<!-- FIN: BOTONES REGISTRO -->

	
			<!-- INICIO: SCRIPTS BOTONES -->
			<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
			<script language="JavaScript">


			<!-- Asociada al boton GuardarCerrar -->
			function accionGenerarInforme() 
			{					
				sub();
				if (document.forms[0].fechaInicio.value=="") {
					mensaje='<siga:Idioma key="informes.listaGuardias.desde" /> <siga:Idioma key="messages.campoObligatorio.error"/>';
					alert(mensaje);
					fin();
					return false;
				}
				if (document.forms[0].fechaFin.value==""){
					mensaje='<siga:Idioma key="informes.listaGuardias.hasta" /> <siga:Idioma key="messages.campoObligatorio.error"/>';
					alert(mensaje);
					fin();
					return false;
				}				
				
				
				if (compararFecha (document.forms[0].fechaInicio, document.forms[0].fechaFin) == 1) {
					mensaje='<siga:Idioma key="messages.fechas.rangoFechas"/>';
					alert(mensaje);
					fin();
					return false;
				} else {

					
					document.forms[0].modo.value="finalizarInforme";
					document.forms[0].submit();
				}	
			}

			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				// esta funcion cierra la ventana y devuelve 
				// un valor a la ventana padre (USAR SIEMPRE)
				top.cierraConParametros("NORMAL");
			}

			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				document.forms[0].reset();
			}
			
			function refrescarLocal() {
			
			
			}

			</script>
			<!-- FIN: SCRIPTS BOTONES -->
	
			<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

		</div>
		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		
		<iframe name="submitArea32" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
