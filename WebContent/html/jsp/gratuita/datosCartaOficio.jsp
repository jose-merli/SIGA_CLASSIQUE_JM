<!-- datosCartaOficio.jsp -->
<!-- 
	 Permite recoger datos para la generacion de cartas de Oficio
	 VERSIONES:
	 miguel.villegas 27-05-2005 
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.lang.*"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	String	botones="C,G,R";
	
	String ficheroDownload = (String)request.getAttribute("nombreFichero");
	String rutaFicheroDownload = (String)request.getAttribute("rutaFichero");
	String borrarFicheroDownload = (String)request.getAttribute("borrarFichero");

	if((ficheroDownload != null) && (!ficheroDownload.equals(""))) {
		botones +=",D"; // boton de descarga
	}
%>	

<html>
<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>	
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>			

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="BuscarDesignasForm" staticJavascript="false" />  
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
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="informes.cartaOficio.cabecera"/>
				</td>
			</tr>
		</table>

		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
			 que se van a mostrar, para que quepen dentro de la ventana.
			 Los elementos que copieis dentro, que tengan el estilo 
			 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
		-->
		<div id="camposRegistro" class="posicionModalMedia" align="center">

			<!-- INICIO: CAMPOS -->
			<!-- Zona de campos de busqueda o filtro -->

			<table  class="tablaCentralCamposMedia"  align="center">

				<html:form action="/JGR_Designas" method="POST">
					<html:hidden property = "modo" value = ""/>
					<html:hidden property = "idTurno" value = ""/>	
					<html:hidden property = "idGuardia" value = ""/>	
					<html:hidden property = "ficheroDownload"       value = "<%=ficheroDownload%>"/>	
					<html:hidden property = "rutaFicheroDownload"   value = "<%=rutaFicheroDownload%>"/>	
					<html:hidden property = "borrarFicheroDownload" value = "<%=borrarFicheroDownload%>"/>	
					
					<tr>				
						<td>
							<siga:ConjCampos leyenda="informes.cartaAsistencia.datos">							
								<table class="tablaCampos" align="center">	
									<!-- FILA -->
									<tr>				
										<td class="labelText">
											<siga:Idioma key="informes.cartaAsistencia.textoCabecera"/>&nbsp;
										</td>				
										<td>	
 											<html:textarea cols="100" rows="6" property="cabeceraCarta" styleClass="box"></html:textarea>
						  				</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="informes.cartaAsistencia.textoMotivo"/>&nbsp;
										</td>				
										<td>	
 											<html:textarea cols="100" rows="6" property="motivoCarta" styleClass="box"></html:textarea> 
						  				</td>
									</tr>
									<tr>				
										<td class="labelText">
											<siga:Idioma key="informes.cartaAsistencia.textoPie"/>&nbsp;
										</td>				
										<td>	
 											<html:textarea cols="100" rows="6" property="pieCarta" styleClass="box"></html:textarea> 
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
		
			<siga:ConjBotonesAccion botones='<%=botones%>' modo=''  modal="M"/>
			
			<!-- FIN: BOTONES REGISTRO -->

	
			<!-- INICIO: SCRIPTS BOTONES -->
			<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
			<script language="JavaScript">


			//Asociada al boton GuardarCerrar -->
			function accionGuardar() 
			{					
				var f=document.forms[0];
				f.modo.value="finalizarCarta";		
				//f.submit();		
				submitArea.location='/SIGA/html/jsp/general/loadingWindowOpener.jsp?formName='+f.name+'&msg=messages.factSJCS.procesandoInforme';
			}

			//Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				// esta funcion cierra la ventana y devuelve 
				// un valor a la ventana padre (USAR SIEMPRE)
				top.cierraConParametros("NORMAL");
			}

			//Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				document.forms[0].reset();
			}
			
			function accionDownload() 
			{		
				document.getElementById('idButton').style.display="none";
				document.forms[0].modo.value="download";
				document.forms[0].submit();
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