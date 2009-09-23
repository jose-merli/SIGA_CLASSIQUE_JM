<!-- recuperarConsultas.jsp -->
<!-- 
	 VERSIONES:
	 emilio.grau 9-3-2005 Versión inicial
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
<%@ page import="com.siga.beans.ConConsultaAdm"%>
<%@ page import="com.atos.utils.*,com.siga.expedientes.form.BusquedaAlertaForm,java.util.*"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String tipoacceso=userBean.getAccessType();
		
	// para ver si tengo que buscar tras mostrar la pantalla
	String buscar = (String)request.getParameter("buscar");
	String funcionBuscar = "";
	if (buscar!=null) {
		funcionBuscar = "buscar()";
	}
	ArrayList idModulo = new ArrayList();
	if (request.getAttribute("idModulo")!=null){
		idModulo.add((String)request.getAttribute("idModulo"));
	}
%>	
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

<logic:notEqual name="RecuperarConsultasForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo
		titulo="consultas.consultaslistas.literal.titulo" 
		localizacion="consultas.consultaslistas.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->	
</logic:notEqual>	
<logic:equal name="RecuperarConsultasForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="consultas.consultasRecuperar.cabecera" 
		localizacion="consultas.consultaslistas.literal.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->	
</logic:equal>	


	
</head>

<body onload="ajusteAlto('resultado');<%=funcionBuscar%>">

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	
	<html:form action="/CON_RecuperarConsultas.do" method="POST" target="resultado">
		<html:hidden property = "modo" value = ""/>
		<input type="hidden" name="limpiarFilaSeleccionada" value="">
		<logic:equal name="RecuperarConsultasForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">	
			<html:hidden property = "tipoConsulta"/>
		</logic:equal>		

	<tr>				
	<td>

	
	<table class="tablaCampos" align="center">

	<!-- FILA -->
	<tr>	
	
	<td class="labelText" width="10%">
		<siga:Idioma key="consultas.recuperarconsulta.literal.descripcion"/>
	</td>				
	<td width="30%">
		<html:text name="RecuperarConsultasForm" property="descripcion" size="50" maxlength="100" styleClass="box"></html:text>			
	</td>		
<logic:equal name="RecuperarConsultasForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">	
	<td class="labelText" width="10%">
		<siga:Idioma key="consultas.recuperarconsulta.literal.modulo"/>
	</td>
	<td width="20%">
		<siga:ComboBD nombre = "idModulo" tipo="cmbModuloConsulta" clase="boxCombo" obligatorio="false" ElementoSel="<%=idModulo%>"/>
	</td>
</logic:equal>	
<logic:notEqual name="RecuperarConsultasForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">			
	<td class="labelText">
		<siga:Idioma key="consultas.recuperarconsulta.literal.tipoConsulta"/>
	</td>
	<td>				
		<select name="tipoConsulta" class="boxCombo" id="tipoConsulta" >
			<option value=""></option>
<logic:equal name="RecuperarConsultasForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_ENV%>">			
			<option value="<%=ConConsultaAdm.TIPO_CONSULTA_ENV%>" selected><siga:Idioma key="modulo.envios"/></option>
			<option value="<%=ConConsultaAdm.TIPO_CONSULTA_FAC%>"><siga:Idioma key="modulo.facturacion"/></option>
</logic:equal>
<logic:equal name="RecuperarConsultasForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_FAC%>">			
			<option value="<%=ConConsultaAdm.TIPO_CONSULTA_ENV%>"><siga:Idioma key="modulo.envios"/></option>
			<option value="<%=ConConsultaAdm.TIPO_CONSULTA_FAC%>" selected><siga:Idioma key="modulo.facturacion"/></option>
</logic:equal>
<logic:equal name="RecuperarConsultasForm" property="tipoConsulta" value="">			
			<option value="<%=ConConsultaAdm.TIPO_CONSULTA_ENV%>"><siga:Idioma key="modulo.envios"/></option>
			<option value="<%=ConConsultaAdm.TIPO_CONSULTA_FAC%>"><siga:Idioma key="modulo.facturacion"/></option>
</logic:equal>
			
		</select>
	</td>
</logic:notEqual>	

	<%	if (tipoacceso.equalsIgnoreCase(SIGAConstants.ACCESS_FULL)){ %>
	
		<td class="labelText" width="10%">
			<siga:Idioma key="consultas.recuperarconsulta.literal.todos"/>
		</td>				
		<td width="10%">
			<html:checkbox name="RecuperarConsultasForm" property="todos"></html:checkbox>
		</td>			
	<%	} %>
	
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
	<%	if (tipoacceso.equalsIgnoreCase(SIGAConstants.ACCESS_READ)){ %>
			<siga:ConjBotonesBusqueda botones="B"/>
	<% } else{ %>
			<siga:ConjBotonesBusqueda botones="N,B"/>
	<% } %>	

	<!-- FIN: BOTONES BUSQUEDA -->

<logic:equal name="RecuperarConsultasForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">
	<html:form action="/CON_NuevaConsulta.do" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
	</html:form>
</logic:equal>
<logic:notEqual name="RecuperarConsultasForm" property="tipoConsulta" value="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>">	
	<html:form action="/CON_NuevaConsulta.do?tipoConsulta=listas" method="POST" target="mainWorkArea">
			<html:hidden property = "modo" value = ""/>
	</html:form>
</logic:notEqual>	
	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		
		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();	
			if (document.forms[0].tipoConsulta.value=="<%=ConConsultaAdm.TIPO_CONSULTA_GEN%>"){
				document.forms[1].action=document.forms[1].action+"?noReset=true";
			}else{
				document.forms[1].action=document.forms[1].action+"&noReset=true";
			}
			document.forms[0].modo.value="buscar";
			document.forms[0].target="resultado";	
			document.forms[0].submit();	
		}

		<!-- Funcion asociada a boton Nuevo -->
		function nuevo() 
		{					
			document.forms[1].submit();
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

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
