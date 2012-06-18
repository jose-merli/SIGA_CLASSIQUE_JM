<!-- nuevaPrevisionFacturacion.jsp -->
<!-- VENTANA DENTRO DE VENTANA MODAL MEDIANA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
	 	yolanda.garcia 27-01-2005 Creación
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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
	String idInstitucion = user.getLocation();
	
	String alertaFechasErroneas = (String)request.getAttribute("alertaFechasErroneas");
	request.removeAttribute("alertaFechasErroneas");
%>
	
<html>

	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="PrevisionesFacturacionForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
			<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp"type="text/javascript"></script>		
			
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->


		<!-- INICIO: TITULO Y LOCALIZACION -->
			<!-- Escribe el título y localización en la barra de título del frame principal -->
			<siga:Titulo 
				titulo="facturacion.previsionesFacturacion.literal.titulo" 
				localizacion="facturacion.previsionesFacturacion.literal.localizacion"/>
			<!-- FIN: TITULO Y LOCALIZACION -->

		<!-- Calendario -->
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
		
		<script>
			function load()
			{
				if("<%=alertaFechasErroneas%>" == "true")
				{	
					alert("<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.mensajeFechasErroneas"/>");
				}
			}
		</script>
	</head>

	<body onLoad="load()";>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.titulo"/>
				</td>
			</tr>
		</table>

		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 	que se van a mostrar, para que quepen dentro de la ventana.
	 	Los elementos que copieis dentro, que tengan el estilo 
	 	"tablaTitulo" se deben modificar por "tablaCentralPeque" 
		-->
		<div id="camposRegistro" class="posicionModalMedia" align="center">

		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->

		<table  class="tablaCentralCamposMedia"  align="center">

			<html:form action="/FAC_PrevisionesFacturacion.do" method="POST" focus="idSerieFacturacion" target="submitArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
				
				<tr>				
					<td>
						<!-- SUBCONJUNTO DE DATOS -->
						<!-- Conjunto de campos recuadrado y con titulo -->
							<table class="tablaCampos" align="center">
	
								<!-- FILA -->
								<%
	    							String dato[] = new String[1];
								dato[0] = idInstitucion;
								%>
				
								<tr align="center"> 
									<td class="labelText" width="180">
										<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.descripcion"/>&nbsp;(*)
									</td>
									<td colspan="3" align="left">
										<input type ="text" value="" id="descripcionPrevision" name="descripcionPrevision" class="box" style="width:'300px';">
									</td>
								</tr>

								<tr align="center"> 
									<td class="labelText" width="180">
										<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.serieFacturacion"/>&nbsp;(*)
									</td>
									<td colspan="3" align="left"><siga:ComboBD nombre="idSerieFacturacion" tipo="cmbSerieFacturacion" clase="boxCombo" parametro="<%=dato%>"/></td>
								</tr>
								
						</table>

	<siga:ConjCampos leyenda="facturacion.seriesFacturacion.literal.periodosFacturacion">
							<table class="tablaCampos" align="center">
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.fechaInicioProductos"/>&nbsp;(*)
									</td>
									<td>
										<siga:Fecha nombreCampo="fechaInicioProductos"/>
										<a onClick="return showCalendarGeneral(fechaInicioProductos);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<siga:Idioma key="general.literal.seleccionarFecha"/>'  border="0"></a>
									</td>
									
									<td class="labelText">
										<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.fechaFinProductos"/>&nbsp;(*)
									</td>
									<td>
										<siga:Fecha nombreCampo="fechaFinProductos"/>
										<a onClick="return showCalendarGeneral(fechaFinProductos);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>'  border="0"></a>
									</td>
								</tr>	
								<tr> 
									<td class="labelText">
										<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.fechaInicioServicios"/>&nbsp;(*)
									</td>
									<td>
										<siga:Fecha nombreCampo="fechaInicioServicios"/>
										<a onClick="return showCalendarGeneral(fechaInicioServicios);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>'  border="0"></a>
									</td>
        								
									<td class="labelText">
										<siga:Idioma key="facturacion.nuevaPrevisionFacturacion.literal.fechaFinServicios"/>&nbsp;(*)
									</td>
									<td>
										<siga:Fecha nombreCampo="fechaFinServicios"/>
										<a onClick="return showCalendarGeneral(fechaFinServicios);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>'  border="0"></a>
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

		<siga:ConjBotonesAccion botones="Y,C" modal="M" />

		<!-- FIN: BOTONES REGISTRO -->

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">

			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				sub();
				var fechaOk =  false;
				
				//Chequeo las fechas:
				if ( (compararFecha(document.forms[0].fechaInicioProductos,document.forms[0].fechaFinProductos)==2) &&
					 (compararFecha(document.forms[0].fechaInicioServicios,document.forms[0].fechaFinServicios)==2) )
					fechaOk =  true;
				else
					fechaOk =  false;

				if (validatePrevisionesFacturacionForm(document.PrevisionesFacturacionForm)){
					  if (fechaOk) {
							if(confirm('<siga:Idioma key="messages.confirm.updateData"/>')) 
							{
								document.forms[0].modo.value="insertar";
								var f = document.forms[0].name;				
								document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+f+'&msg=facturacion.mensaje.generandoPrevision';
								//document.forms[0].submit();
							} else {
								fin();
							}
							
					 } else {
	 						alert('<siga:Idioma key="gratuita.modalNuevo_DefinirCalendarioGuardia.literal.aviso1"/>');
	 						fin();
	 				 }
				} else {
					fin();
				}
			}

			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				// esta funcion cierra la ventana y devuelve 
				// un valor a la ventana padre (USAR SIEMPRE)
				top.cierraConParametros("NORMAL");
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
