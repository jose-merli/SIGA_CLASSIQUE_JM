<!-- busquedaEstados.jsp -->

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>


<%@page import="com.atos.utils.UsrBean"%>
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	String tipoacceso=user.getAccessType();
	//Recupero el nombre del expediente
	String nombreExp = (String)request.getAttribute("nombreExp");
	String idinstitucion_tipoexpediente = (String)request.getAttribute("idInstitucion_TipoExpediente");
	String tipoExp = (String)request.getParameter("idTipoExpediente");
	String dato[] = {idinstitucion_tipoexpediente,tipoExp};
	
%>

<%@page import="com.siga.gui.processTree.SIGAPTConstants"%>
<html>
<head>
<link id="default" rel="stylesheet" type="text/css"
	href="<%=app%>/html/jsp/general/stylesheet.jsp">

<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"> </script>

<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->

<!-- Funcion asociada a boton buscar -->
<%--
			<script language="JavaScript">
			function buscar() 
			{
				tipoExpedienteForm.modo.value="buscar";
				tipoExpedienteForm.submit();
			}
	
			<!-- Funcion asociada a boton busqueda avanzada -->
			function buscarAvanzada() 
			{		
		
			}
			
			<!-- Funcion asociada a boton busqueda simple -->
			function buscarSimple() 
			{		
		
			}
			
			<!-- Funcion asociada a boton limpiar -->
			function limpiar() 
			{		
		
			}
			
			<!-- Funcion asociada a boton Nuevo -->
			function nuevo() 
			{		
		
			}
	     </script> --%>

<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->


<!-- INICIO: SCRIPTS BOTONES -->
<script language="JavaScript">
		
			<!-- Refresco -->
			<!-- esta función es llamada desde exito.jsp tras mostrar el mensaje de éxito --!
			function refrescarLocal() 
			{		
				var elemento=parent.document.getElementById('pestana.tiposexpedientes.estado');
				parent.pulsar(elemento,'mainPestanas')
			}
	
			<!-- Asociada al boton Nuevo -->
			function accionNuevo() 
			{		
				   document.forms[0].modo.value = "nuevo";
   				   var resultado=ventaModalGeneral(document.forms[0].name,"G");
   				  if(resultado=='MODIFICADO') parent.buscar();
			}
	
			<!-- Asociada al boton Volver -->
			function accionVolver() 
			{		
				<%request.getSession().removeAttribute("nombreExp");%>
				top.frames["mainWorkArea"].location.href='<%=app%>/Dispatcher.do?proceso=41';
			}
			function accionBuscar() 
			{	sub();	
				//var auxTarget = document.forms[0].target;
				document.forms[0].modo.value="buscar";
				document.forms[0].target="resultado";	
				document.forms[0].submit();
				//document.forms[0].target= auxTarget ;	
		
			}
		
		</script>

<!-- FIN: SCRIPTS BOTONES -->
<!-- INICIO: TITULO Y LOCALIZACION -->
<!-- Escribe el título y localización en la barra de título del frame principal -->
<siga:Titulo
	titulo="expedientes.tipoExpedientes.configuracion.estados.cabecera"
	localizacion="expedientes.tipoExpedientes.configuracion.localizacion" />
<!-- FIN: TITULO Y LOCALIZACION -->
</head>

<body class="detallePestanas" onLoad="ajusteAlto('resultado');accionBuscar();">

<table class="tablaTitulo" align="center" cellspacing="0">


	<tr>
		<td id="titulo" class="titulitosDatos"><siga:Idioma
			key="expedientes.literal.tiposexpedientes" />: &nbsp;<%=nombreExp%></td>
	</tr>





</table>

<html:form action="/EXP_TiposExpedientes_Estados.do" method="POST"
	target="submitArea">
	<html:hidden property="modo" value="" />
	<html:hidden property="hiddenFrame" value="1" />
	<html:hidden property="idTipoExpediente" />
	<!-- RGG: cambio a formularios ligeros -->
	<input type="hidden" name="tablaDatosDinamicosD">
	<input type="hidden" name="actionModal" value="">
<fieldset>
<table class="tablaCampos" align="center" border="0">
	<tr>
		<td class="labelText" width="100"><siga:Idioma
			key="expedientes.auditoria.literal.fase" /></td>
		<td><siga:ComboBD nombre="idInst_idExp_idFase" tipo="cmbFases"
			clase="boxCombo" obligatorio="true" parametro="<%=dato%>" accion="accionBuscar();"
			pestana="t" /></td>
	</tr>

</table>
</fieldset>

</html:form>

<iframe align="middle" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
<%	if (tipoacceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_READ)){ %>
<siga:ConjBotonesAccion botones="V" clase="botonesDetalle" />
<% } else{ %>
<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle" />
<% } %>


<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>
</body>
</html>

