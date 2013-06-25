<!-- busquedaPersonasModal.jsp -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.Properties"%>
 
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	
	//locales
	BusquedaClientesForm formulario = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesModalForm");
	//formulario.setChkBusqueda("on");
		
	String titu = "menu.censo.busquedaPersonas";
	String busc = "menu.censo.busquedaPersonas";
%>	


<html>

<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="<%=titu%>" 
		localizacion="<%=titu%>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="busquedaClientesModalForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		
	<script language="JavaScript">
	
		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}
		
		//Funcion asociada a boton buscar -->
		function buscar() 
		{
			sub();		
			document.forms[0].modo.value="buscarPersonaInit";
			document.forms[0].submit();			
		}
		
		function setChkBusqueda()
		{
			document.forms[0].chkBusqueda.checked = true;
		}
		
	</script>

</head>


<body onLoad="ajusteAltoBotones('resultadoModal');setChkBusqueda();">

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="<%=titu %>"/>
		</td>
	</tr>
	</table>
	
	
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="resultadoModal">
	<html:hidden name="busquedaClientesModalForm" property = "modo" value = ""/>
	<html:hidden name="busquedaClientesModalForm" property = "nombreInstitucion" />
	<table class="tablaCentralCampos" align="center">
		<tr><td><table class="tablaCampos" align="center">
			<tr>
				<td class="labelText" colspan="2">&nbsp;</td>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.checkBusqueda"/>
				</td>
				<td>
					<html:checkbox  name="busquedaClientesModalForm" property="chkBusqueda"  />
				</td>
			</tr>
			
			<tr>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.nif"/>
				</td>
				<td>
					<html:text name="busquedaClientesModalForm" property="nif" size="15" styleClass="box"></html:text>
				</td>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.nombre"/>
				</td>
				<td>
					<html:text name="busquedaClientesModalForm" property="nombrePersona" size="30" styleClass="box"></html:text>
				</td>
			</tr>
			
			<tr>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.apellido1"/>
				</td>
				<td>
					<html:text name="busquedaClientesModalForm" property="apellido1" size="30" styleClass="box"></html:text>
				</td>
				<td class="labelText">
					<siga:Idioma key="censo.busquedaClientes.literal.apellido2"/>
				</td>
				<td>
					<html:text name="busquedaClientesModalForm" property="apellido2" size="30" styleClass="box"></html:text>
				</td>
			</tr>
			
		</table></td></tr>
	</table>
	</html:form>
	</siga:ConjCampos>
	
	
	<!-- FIN: CAMPOS DE BUSQUEDA-->
	
	
	<!-- INICIO: BOTONES BUSQUEDA -->
	<siga:ConjBotonesBusqueda botones="B"  modal="G" titulo="<%=busc%>" />
	<!-- FIN: BOTONES BUSQUEDA -->
	
	
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
	<siga:ConjBotonesAccion botones="C" modal="G" />
	<!-- FIN: BOTONES REGISTRO -->
	
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>

</html>