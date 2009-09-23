<!-- 
 VERSIONES:
 raul.ggonzalez  23-03-2005
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
<%@ page import="com.siga.facturacionSJCS.form.RegularizacionForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	RegularizacionForm miform = (RegularizacionForm) request.getAttribute("regularizacionForm");
	String facturacionParam[] = new String[1];
	facturacionParam[0] = miform.getIdInstitucion();

	ArrayList facturacionSel = new ArrayList();
	facturacionSel.add(miform.getIdFacturacion());

%>	
<html>

<!-- HEAD -->
	<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="regularizacionForm" staticJavascript="false"/>  
		  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
		
	</head>

	<body>
	
		<!-- TITULO -->		
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="factSJCS.datosRegularizacion.titulo"/>
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
			<tr>
				<td>
					<siga:ConjCampos leyenda="factSJCS.datosRegularizacion.titulo">
						<html:form action="/FCS_RegularizacionFacturacion.do" method="POST" target="submitArea">
							<html:hidden property = "modo" value = ""/>
							<html:hidden property = "idInstitucion"/>
					 		<table class="tablaCampos" >
								<tr>							
									<td class="labelText">
										<siga:Idioma key="factSJCS.datosRegularizacion.literal.nombre"/>&nbsp;(*)
									</td>
									<td class="labelText">
										<html:text size="40" name="regularizacionForm" property="nombre" styleClass="box"></html:text>			   	
									</td>
								</tr>
								<tr>							
									<td  class="labelText">
										<siga:Idioma key="factSJCS.datosRegularizacion.literal.facturacion"/>&nbsp;(*)
									</td>
									<td  class="labelText">
										<siga:ComboBD nombre = "idFacturacion" tipo="cmbFacturacionesSinRegularizar" parametro="<%=facturacionParam %>" clase="boxCombo" obligatorio="false"  elementoSel="<%=facturacionSel %>" />						
									</td>
								</tr>
							</table>	
						</html:form>
					</siga:ConjCampos>	
				</td>
			</tr>
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
	
			<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />
	
		<!-- FIN: BOTONES REGISTRO -->
	
		
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
	
	 
			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{		
				if (validateRegularizacionForm(document.forms[0])) 
				{
					document.forms[0].modo.value="insertar";
					document.forms[0].submit();
				}
			}
	
			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				var a = new Array ();
				a[0] = "CERRAR_SIN_GUARDAR"
				top.cierraConParametros(a);
			}
	
			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				document.forms[0].reset();
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
