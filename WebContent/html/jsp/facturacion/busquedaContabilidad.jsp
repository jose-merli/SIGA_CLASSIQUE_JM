<!-- busquedaContabilidad.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.FacRegistroFichContaBean"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.*"%>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	ArrayList idEstadoSeleccionado = new ArrayList();
//	idEstadoSeleccionado.add(new Integer(FacRegistroFichContaBean.ESTADO_TERMINADO).toString());
	
	
%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo
		titulo="gratuita.busquedaContabilidad.literal.titulo" 
		localizacion="facturacion.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script language="JavaScript">	
	function refrescarLocal()
	{
		buscar();
	}

	</script>
	
	
</head>


<body onLoad="ajusteAlto('resultado');buscar();">

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action = "/FAC_Contabilidad.do" method="POST" target="resultado">
		<input type="hidden" name="actionModal" value="">
		<html:hidden property = "modo" value = ""/>	
<!--		<html:hidden property = "fechaDesde" value = ""/>	-->
<!--		<html:hidden property = "fechaHasta" value = ""/>	-->
		<fieldset>
		<table class="tablaCentralCampos" width="400px" border="0">
			<tr>
				<td class="labelText">	
					<siga:Idioma key="gratuita.busquedaContabilidad.literal.fecha"/>:&nbsp;&nbsp;
				</td>
				<td class="labelText">	
					<siga:Idioma key="gratuita.busquedaContabilidad.literal.entre"/>&nbsp;<html:text name="ContabilidadForm" property="fechaDesde" size="10" styleClass="box" value="" readOnly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaDesde);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>' border="0"></a>
				</td>
				<td class="labelText">	
					<siga:Idioma key="gratuita.busquedaContabilidad.literal.y"/>&nbsp;<html:text name="ContabilidadForm" property="fechaHasta" size="10" styleClass="box" value="" readOnly="true"></html:text>&nbsp;&nbsp;<a onClick="return showCalendarGeneral(fechaHasta);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);"><img src="<%=app%>/html/imagenes/calendar.gif" alt='<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>'  border="0"></a>
				</td>
				<td class="labelText"><siga:Idioma key="facturacion.buscarFactura.literal.Estado"/></td>
				<td><siga:ComboBD nombre="buscarIdEstado" tipo="cmbEstadosContab"  clase="boxCombo" obligatorio="false" elementoSel="<%=idEstadoSeleccionado%>"/>
				</td>
				
			</tr>
		</table>
		</fieldset>
	</html:form>

	<siga:ConjBotonesBusqueda botones="B,N"  titulo="gratuita.busquedaContabilidad.literal.titulo"/>
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->	

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();
			document.forms[0].target			= "resultado";
			document.forms[0].modo.value 		= "buscar";
			document.forms[0].submit();
		}		
		
		function nuevo()
		{
			document.forms[0].modo.value	= "nuevo";
	   		var resultado = ventaModalGeneral(document.forms[0].name,"P");
	   		if(resultado == "MODIFICADO") buscar();
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->
	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

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


	<!-- INICIO: BOTONES BUSQUEDA -->	
	<!-- FIN: BOTONES BUSQUEDA -->

<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>