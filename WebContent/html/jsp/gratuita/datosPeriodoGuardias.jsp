<!-- datosPeriodoGuardias.jsp -->
<!-- 
	 Permite recoger datos para la generacion de cartas de asistencia
	 VERSIONES:
	 miguel.villegas 18-05-2005 
--

<!-- CABECERA JSP -->
<%@page import="com.atos.utils.UsrBean"%>
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
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	String idInstitucion = userBean.getLocation();
	Hashtable datos=(Hashtable)request.getAttribute("DATOS");

	String	botones="GM,R,C";
%>	

<%@page import="java.util.Properties"%>
<%@page import="java.util.Hashtable"%>
<html>
<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
		<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
			
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
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
					<html:hidden property = "comunicacion"/>
					<tr>				
						<td>
							<siga:ConjCampos leyenda="informes.listaGuardias.periodo">							
								<table class="tablaCampos" align="center">	
									<!-- FILA -->
									<tr>				
										<td class="labelText">
											<siga:Idioma key="informes.listaGuardias.desde"/>&nbsp;(*)
										</td>				
										<td>	
											<siga:Fecha nombreCampo="fechaInicio"></siga:Fecha>
											
						  				</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="informes.listaGuardias.hasta"/>&nbsp;(*)
										</td>				
										<td>	
											<siga:Fecha nombreCampo="fechaFin"></siga:Fecha>
											
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
	<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property="idTipoInforme" value="LIGUA"/>
	<html:hidden property="enviar" value="1"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<html:hidden property="tipoPersonas"/>
	
	<input type='hidden' name='actionModal'>
</html:form>			
			<!-- FIN: BOTONES REGISTRO -->

	
			<!-- INICIO: SCRIPTS BOTONES -->
			<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
			<script language="JavaScript">


			// Asociada al boton GuardarCerrar -->
			function accionGenerarInforme() 
			{	

				//alert("pepe");
				var fechaIni = document.forms[0].fechaInicio.value;
				var fechaFin = document.forms[0].fechaFin.value;
				sub();
				if (fechaIni=="") {
					mensaje='<siga:Idioma key="informes.listaGuardias.desde" /> <siga:Idioma key="messages.campoObligatorio.error"/>';
					alert(mensaje);
					fin();
					return false;
				}
				if (fechaFin==""){
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
				} 

				if(document.forms[0].comunicacion.value=="true"){

					var auxlist = <%=(String)datos.get("IDLISTA")%>;
					var idInstitucion = <%=(String)datos.get("IDINSTITUCION")%>;
				   	datos = "idInstitucion==" +idInstitucion+"##idTipoPersonas==1##fechaIni=="+fechaIni+"##fechaFin=="+fechaFin+"##idLista=="+auxlist+"##idTipoInforme==LIGUA"; 
				   	document.InformesGenericosForm.datosInforme.value =datos;
					var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");				
					accionCerrar();
				}else{	
					document.forms[0].modo.value="finalizarInforme";
					document.forms[0].submit();
		
				}
				
			}
			//-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				// esta funcion cierra la ventana y devuelve 
				// un valor a la ventana padre (USAR SIEMPRE)
				top.cierraConParametros("NORMAL");
			}

			//-- Asociada al boton Restablecer -->
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
