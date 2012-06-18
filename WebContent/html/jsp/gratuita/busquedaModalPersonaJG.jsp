<!-- busquedaModalPersonaJG.jsp -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gratuita.form.BusquedaPersonaJGForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
 <%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
%>	
	
<%  
	// locales
	BusquedaPersonaJGForm formulario = (BusquedaPersonaJGForm)request.getSession().getAttribute("busquedaClientesModalForm");
		
	String titu = "gratuita.busquedaPersonaJG.literal.titulo";
	String busc = "gratuita.busquedaPersonaJG.literal.titulo";

%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	<link rel="stylesheet" href="<%=app%>/html/js/themes/base/jquery.ui.all.css"/>
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="<%=titu%>" 
		localizacion="<%=titu%>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="BusquedaPersonaJGForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

</head>

<body onLoad="ajusteAltoBotones('resultadoModal');">

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="<%=titu %>"/>
		</td>
	</tr>
	</table>


	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<tr>				
	<td>

	<siga:ConjCampos leyenda="gratuita.busquedaPersonaJG.literal.leyenda">

	<table class="tablaCampos" align="center">

	<html:form action="/JGR_BusquedaPersonaJG.do" method="POST" target="resultadoModal">
	<html:hidden name="BusquedaPersonaJGForm" styleId="modo" property = "modo" value = ""/>
	<html:hidden name="BusquedaPersonaJGForm" styleId="conceptoE" property = "conceptoE" />

	<!-- RGG: cambio a formularios ligeros -->
	<input type="hidden" id="filaSelD" name="filaSelD">
	<input type="hidden" id="tablaDatosDinamicosD" name="tablaDatosDinamicosD">
	<input type="hidden" id="actionModal" name="actionModal" value="">

	<!-- campos ocultos -->
	<html:hidden name="BusquedaPersonaJGForm" styleId="idInstitucion" property = "idInstitucion" />
			
	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="gratuita.personaJG.literal.nombreDeno"/>
	</td>				
	<td>
		<html:text name="BusquedaPersonaJGForm" property="nombre" size="30" styleClass="box"></html:text>
	</td>

	<td class="labelText">
		<siga:Idioma key="gratuita.personaJG.literal.apellido1Abre"/>
	</td>
	<td>
		<html:text name="BusquedaPersonaJGForm" property="apellido1" size="30" styleClass="box"></html:text>
	</td>

	</tr>
					
	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="gratuita.personaJG.literal.apellido2"/>
	</td>
	<td>
		<html:text name="BusquedaPersonaJGForm" property="apellido2" size="30" styleClass="box"></html:text>
	</td>

	<td class="labelText">
		<siga:Idioma key="gratuita.personaJG.literal.nIdentificacion"/>
	</td>
	<td>
		<html:text name="BusquedaPersonaJGForm" property="NIdentificacion" size="20" styleClass="box"></html:text>
	</td>	

	</tr>

	</html:form>
	</table>

	</siga:ConjCampos>

	</td>
	</tr>
	</table>


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

		<siga:ConjBotonesBusqueda botones="B"  modal="G" titulo="<%=busc%>" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() {
			sub();		
			document.forms[0].modo.value="buscarPersonaInit";
			document.forms[0].submit();			
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultadoModal"
					name="resultadoModal" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

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

		<siga:ConjBotonesAccion botones="C" modal="G" clase="botonesDetalle"/>

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">


		<!-- Asociada al boton Cerrar -->
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}


	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->



<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>