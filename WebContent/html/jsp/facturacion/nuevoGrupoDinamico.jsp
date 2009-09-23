<!-- nuevoGrupoDinamico.jsp -->
<!-- VENTANA DENTRO DE VENTANA MODAL PEQUEÑA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
	 	yolanda.garcia 10-01-2005 Creación
-->

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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.facturacion.form.AsignacionConceptosFacturablesForm"%>
<%@ page import="com.atos.utils.UsrBean"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
%>

<%
	UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
	String idInstitucion = user.getLocation();
	String idSerieFacturacion = (String)ses.getAttribute("idSerieFacturacion");
%>
	
<html>

	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="GruposDinamicosForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- INICIO: TITULO Y LOCALIZACION -->
			<!-- Escribe el título y localización en la barra de título del frame principal -->
			<siga:Titulo 
				titulo="facturacion.asignacionDeConceptosFacturables.titulo" 
				localizacion="facturacion.asignacionDeConceptosFacturables.titulo"/>
			<!-- FIN: TITULO Y LOCALIZACION -->

		<!-- Calendario -->
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	</head>

	<body>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="facturacion.nuevoGrupoDinamico.literal.titulo"/>
				</td>
			</tr>
		</table>

		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 	que se van a mostrar, para que quepen dentro de la ventana.
	 	Los elementos que copieis dentro, que tengan el estilo 
	 	"tablaTitulo" se deben modificar por "tablaCentralPeque" 
		-->
		<div id="camposRegistro" class="posicionModalPeque" align="center">

		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->

		<table  class="tablaCentralCamposPeque"  align="center">

			<html:form action="/FAC_GruposDinamicos.do" method="POST" focus="idGruposCriterios" target="submitArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
				<html:hidden property="idSerieFacturacion" value="<%=idSerieFacturacion%>"/>
				
				<tr>				
					<td>
						<!-- SUBCONJUNTO DE DATOS -->
						<!-- Conjunto de campos recuadrado y con titulo -->
						<siga:ConjCampos leyenda="facturacion.nuevoGrupoDinamico.literal.leyenda">
							<table class="tablaCampos" align="center">
	
								<!-- FILA -->
								<%
	    						String dato[] = new String[5];
	    						
								dato[0] = idInstitucion;
								dato[1] = idInstitucion;
								dato[2] = idSerieFacturacion;
								dato[3] = idInstitucion;
								
								String[] aPerfiles = user.getProfile();
								String sPerfiles="('";
			
								for (int i=0; i<aPerfiles.length; i++)
								{
									sPerfiles += aPerfiles[i] + "','";
								}
								
								sPerfiles=sPerfiles.substring(0, sPerfiles.length()-2);
								sPerfiles+=")";
								
								dato[4] = sPerfiles;
								%>
				
								<tr colspan="100%" align="center"> 
									<td class="labelText">
										<siga:Idioma key="facturacion.nuevoGrupoDinamico.literal.leyenda"/>&nbsp;(*)
									</td>
									<td>
										<siga:ComboBD sqlplano="true" nombre="idGruposCriterios" tipo="cmbGruposCriterios" clase="boxCombo" parametro="<%=dato%>" ancho="300" />
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

		<siga:ConjBotonesAccion botones="Y,C" modal="P" />

		<!-- FIN: BOTONES REGISTRO -->

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">

			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{
				sub();
				if (validateGruposDinamicosForm(document.GruposDinamicosForm)){	
					if(confirm('<siga:Idioma key="messages.confirm.updateData"/>')) 
					{
						document.forms[0].modo.value="insertar";
						document.forms[0].target='';						
						document.forms[0].submit();
					
						// esta funcion cierra la ventana y devuelve 
						// un valor a la ventana padre (USAR SIEMPRE)
						// top.cierraConParametros("NORMAL");
					}else{
				
					fin();
						return false;
					
					}
				}else{
				
					fin();
					return false;
				
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
