<!-- bajaGuardia.jsp -->
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
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>


<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String idguardia = (String) request.getAttribute("GUARDIA");
	// Datos posibles de entrada.
	// Creamos la fecha actual
	java.text.SimpleDateFormat formador = new java.text.SimpleDateFormat("dd/MM/yyyy");
	String fecha = formador.format(new Date());
	String observaciones = "";
%>	
<html>
<!-- HEAD -->
<head>
	<title><siga:Idioma key="gratuita.altaTurnos.literal.title"/></title>
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script type="text/javascript">
	</script>
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulitosDatos">
		<%
			String titulo = (String) request.getAttribute("titulo");
		%>
			<siga:Idioma key="<%=titulo%>"/>
		</td>
	</tr>
	</table>


<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralGrande" 
-->
<div id="camposRegistro" class="posicionModalGrande" align="center">
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table  class="tablaCentralCampos"  align="center">
	<html:form action="JGR_BajaEnGuardia.do" method="post" target="_self">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="guardia" value="<%=idguardia%>">
	<tr>
	<td>
	<siga:ConjCampos leyenda="gratuita.altaTurnos.literal.solicitudBaja">
	
	 <table width="100%" border="0">
		<tr>
		<!-- obtenemos los campos para el alta de turnos -->
			<td width="5%" class="labelText">&nbsp;</td>
			<td width="20%" class="labelText">
				<siga:Idioma key="gratuita.bajaGuardia.literal.fechaBaja"/>&nbsp;
			</td>
			<td width="75%">
				<html:text name="DefinirGuardiasTurnosForm" property="fechaBaja" size="10" maxlength="10" styleClass="boxConsulta" value="<%=fecha%>" readOnly="true" ></html:text>
<!--				&nbsp;<input type="checkbox" name="suscripcion" value="no" checked disabled> -->
			</td>
		</tr>
	 </table>
	 <table width="100%" border="0">
		<tr>
			<td width="5%" class="labelText">&nbsp;</td>
			<td width="35%" class="labelText" colspan="2">
			<siga:Idioma key="gratuita.bajaGuardia.literal.observaciones"/>
			</td>
			<td class="labelText">&nbsp;</td>
		</tr>
		<tr>
			<td width="5%" class="labelText">&nbsp;</td>
			<td class="labelText" colspan="2"> 
			<html:textarea name="DefinirGuardiasTurnosForm" onKeyDown="cuenta(this,1024)" onChange="cuenta(this,1024)" property="observacionesBaja" cols="140" rows="2" style="overflow:auto" styleClass="boxCombo" value="<%=observaciones%>" readOnly="false" ></html:textarea>
		</tr>
	</table>
	</siga:ConjCampos>	
   </td>
   </tr>
   	</html:form>
	</table>
   <%
	String guardias = (String) ses.getAttribute("GUARDIAS");
	if(guardias.equals("2"))
	{
	%>
		<div style="position:absolute;top:420px;left: 0px">
			<p class="labelText" style="text-align:center">
				<siga:Idioma key="gratuita.bajaGuardia.literal.infElegir"/>
			</p>
		</div>
		
			<siga:ConjBotonesAccion botones="c,f" clase="botonesDetalle"  />	
		
	<%
	}
	else
	{
	%>
		
			<siga:ConjBotonesAccion botones="x,s" clase="botonesDetalle"  />	
		
	<%
	}
	%>
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


	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<!-- SCRIPTS BOTONES -->
	<script language="JavaScript">

		function accionCancelar() 
		{		
			window.returnValue="CANCELADO";			
			window.close();
		}
		
		function accionCerrar() 
		{		
			window.returnValue="CANCELADO";			
			window.close();
		}
			
		<!-- Asociada al boton Siguiente -->
		function accionSiguiente() 
		{	
			document.forms[0].modo.value="buscar";
			document.forms[0].submit();
		}

		function accionFinalizar() 
		{		
			sub();
			document.forms[0].modo.value="modificar";
			document.forms[0].target="submitArea";
			document.forms[0].submit();
			window.returnValue="MODIFICADO";			
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
