<!-- busqListaCorreos.jsp -->
<!-- 
	 VERSIONES:
	 juan.grau 08-03-2005 Versión inicial
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
<%@ page import="com.siga.administracion.SIGAConstants,java.lang.*"%>
<%@ page import="com.atos.utils.*,
							  com.siga.envios.form.ListaCorreosForm,
							  java.util.*"%>
							  

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idinstitucion = userBean.getLocation();
		
	//Compruebo si se ha realizado una búsqueda anteriormente
	String funcionBuscar="";
	String buscar = (String) request.getParameter("buscarListas");
	if (buscar!=null&&buscar.equalsIgnoreCase("true")) funcionBuscar = "buscar()";	
%>	
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="envios.listas.literal.titulo" 
		localizacion="envios.listas.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onload="ajusteAlto('resultado');<%=funcionBuscar%>">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	
	<html:form action="/ENV_ListaCorreos.do?noReset=true" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "actionModal" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
				
	<tr>				
	<td>
		<fieldset>	
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
		</fieldset>	
			
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

		<siga:ConjBotonesBusqueda botones="B,N"/>

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		
		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{	
			sub();
			document.forms[0].modo.value="buscar";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}
		
		<!-- Asociada al boton Nuevo -->
		function nuevo() 
		{		
			document.forms[0].modo.value = "nuevo";
			var resultadomodal=ventaModalGeneral(document.forms[0].name,"P");
			
			if(resultadomodal=="MODIFICADO") {
				document.forms[0].target="mainWorkArea";
				document.forms[0].modo.value="editar";
				document.forms[0].submit();
			}
			
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
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
