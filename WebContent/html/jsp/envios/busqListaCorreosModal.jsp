<!-- busqListaCorreosModal.jsp -->
<!-- 
	 VERSIONES:
	 juan.grau 08-03-2005 Versión inicial
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
<%@ page import="com.siga.administracion.SIGAConstants,java.lang.*"%>
<%@ page import="com.atos.utils.*,
							  com.siga.envios.form.ListaCorreosForm,
							  java.util.*"%>
							  

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idinstitucion = userBean.getLocation();
%>	
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="envios.listas.literal.titulo" 
		localizacion="envios.listas.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onload="ajusteAltoBotones('resultado');">
<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="envios.listas.literal.titulo"/>
		</td>
	</tr>
	</table>
	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<div id="camposRegistro" class="posicionModalGrande" align="center">


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	
	<html:form action="/ENV_ListaCorreos.do?noReset=true" method="POST" target="resultado">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "modal" value = "true"/>	
	<html:hidden property = "actionModal" value = ""/>
				
	<tr>				
	<td>
	
		<table class="tablaCampos" align="center">
	
			<!-- FILA -->
			<tr>	
			
				<td class="labelText" width="10%">
					<siga:Idioma key="envios.listas.literal.lista"/>
				</td>				
				<td width="30%">	
					<html:text name="ListaCorreosForm" property="campoLista" size="30" maxlength="30" styleClass="box"></html:text>
				</td>		
				
				<td class="labelText"  width="10%">
					<siga:Idioma key="envios.listas.literal.dinamica"/>					
				</td>
				<td>
					<html:select name="ListaCorreosForm" property="campoDinamica" size="1" styleClass="boxCombo">
						<html:option value="" key=""/>
						<html:option value="S" key="general.yes"/>
						<html:option value="N" key="general.no"/>
					</html:select>	
				</td>				
			
			</tr>
		
		</table>
			
	</td>
	</tr>
	</html:form>
	</table>


	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->

		<siga:ConjBotonesBusqueda botones="B" modal="G"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		
		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{	
			document.forms[0].modo.value="buscar";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0"				 
					class="frameGeneral">
	</iframe>

	<!-- FIN: IFRAME LISTA RESULTADOS -->
	
	
		<siga:ConjBotonesAccion botones="C" modal="G" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">


		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}


	</script>
	
</div>


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
