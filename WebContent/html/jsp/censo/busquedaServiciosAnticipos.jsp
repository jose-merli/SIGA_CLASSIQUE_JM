<!-- busquedaServiciosAnticipos.jsp -->
<!-- inspirado en SolicitudCompra.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.censo.form.AnticiposClienteForm"%>
<%@ page import="com.siga.beans.PysAnticipoLetradoBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Hashtable" %>
<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	
	AnticiposClienteForm formulario = (AnticiposClienteForm)request.getAttribute("AnticiposClienteForm");
	
	//recogemos los datos
	try{
		Hashtable resultado = (Hashtable)request.getAttribute("resultado");
	}catch (Exception e){
	
	}
		


	//variables que se van a mostrar en la jsp
	String idPersona="", idAnticipo="", idInstitucion="" ;

	//inicializamos los valores
	try{
		Hashtable resultado = (Hashtable)request.getAttribute("resultado");
		idPersona = (String)resultado.get(PysAnticipoLetradoBean.C_IDPERSONA);
		idAnticipo = (String)resultado.get(PysAnticipoLetradoBean.C_IDANTICIPO);
		idInstitucion = (String) resultado.get(PysAnticipoLetradoBean.C_IDINSTITUCION);
	}catch(Exception e){
		idPersona = formulario.getIdPersona();
		idAnticipo = formulario.getIdAnticipo();
		idInstitucion = formulario.getIdInstitucion();
	}

	//Parametro para la busqueda:
    String [] parametroCombo = {idInstitucion};
  
%>

<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<html>

<!-- HEAD -->

<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"></script>
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="pys.solicitudCompra.cabecera" 
		localizacion="pys.solicitudCompra.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>

<body onLoad="ajusteAlto('resultado');">

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="pys.busquedaServicios.titulos.busquedaServicios"/>
		</td>
	</tr>
	</table>

<table class="tablaCampos" align="center" border="0">
	<html:form action="/CEN_AnticiposCliente.do" method="POST" target="resultado">

		<html:hidden name="AnticiposClienteForm" property="modo" value="" />
		<html:hidden name="AnticiposClienteForm" property="idPersona"	value="<%=idPersona%>" />
		<html:hidden name="AnticiposClienteForm" property="idAnticipo" value="<%=idAnticipo%>" />
		<html:hidden name="AnticiposClienteForm" property="idInstitucion" value="<%=user.getLocation()%>" />
		<html:hidden name="AnticiposClienteForm" property="serviciosSeleccionados" value="" />
		
		<tr>
			<td>

			<table class="tablaCampos" align="center" border="0">
				<tr>
					<td class="labelText">
						<siga:Idioma key="pys.busquedaServicios.literal.tipo"/>&nbsp;&nbsp;
					</td>	
					<td>
						<siga:ComboBD nombre = "categoriaServicio" tipo="tipoServicio" clase="boxCombo" obligatorio="false" accion="Hijo:tipoServicio"/>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="pys.busquedaServicios.literal.categoria"/>&nbsp;&nbsp;
					</td>	
					<td>										
						<siga:ComboBD nombre = "tipoServicio" ancho="250" tipo="categoriaServicio" parametro="<%=parametroCombo%>" clase="boxCombo" obligatorio="false" hijo="t"/>
					</td>
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="pys.busquedaServicios.literal.servicio"/>&nbsp;&nbsp;
					</td>
					<td>
						<html:text property="nombreServicio" styleClass="box" size="30" maxlength="100" value=""></html:text>
					</td>
				</tr>
			</table>
			</td>

			
		</tr>
		<!-- FILA -->
	</html:form>
</table>

		<siga:ConjBotonesBusqueda botones="B"  />



<!-- INICIO: IFRAME LISTA RESULTADOS BUSQUEDA -->
		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
						id="resultado"
						name="resultado" 
						scrolling="no"
						frameborder="0"
						marginheight="0"
						marginwidth="0"
						class="frameGeneral"	 
						>					
		</iframe>

		<!-- FIN: IFRAME LISTA RESULTADOS -->

		<siga:ConjBotonesAccion botones="Y,C"  />

	<script language="JavaScript">

		function buscar() 
		{		
				document.forms[0].modo.value="buscarPor";
				document.forms[0].target="resultado";	
				document.forms[0].submit();	
		}
		function accionGuardarCerrar() 
		{		
			sub();
			var datos = "";
			var validados = document.resultado.document.getElementsByName("validado");
			if (validados.type !="checkbox") {
			
				for (i = 0; i < validados.length; i++){
		
					if (validados[i].checked==1){
						var j=i+1;
						var aux="solicita_"+j+"_1";
						var idinstitucion=document.resultado.document.getElementById(aux);
						aux="solicita_"+j+"_2";
						var idtiposervicio=document.resultado.document.getElementById(aux);
						aux="solicita_"+j+"_3";
						var idservicio=document.resultado.document.getElementById(aux);
						aux="solicita_"+j+"_4";
						var idservicioinstitucion=document.resultado.document.getElementById(aux);
						datos=datos + idinstitucion.value + "##" + idtiposervicio.value + "##" + idservicio.value + "##" + idservicioinstitucion.value + "%%";
							
					}	
				}		
			} else {
			   if (validados.checked==1){
						var j=1;
						var aux="solicita_"+j;
						var solicitado=document.resultado.document.getElementById(aux);
						datos=datos + solicitado.value + "%";						
					}	
			}
			if (validados.length > 0){
				document.AnticiposClienteForm.serviciosSeleccionados.value = datos;		
				document.AnticiposClienteForm.modo.value = "guardarServicios";
				document.AnticiposClienteForm.target = "submitArea";
				document.AnticiposClienteForm.submit();
			}else{
				alert('<siga:Idioma key="pys.anticipos.mensaje.seleccionarServicio"/>');
				fin();
			}
			
			//window.top.close();
				
		}
		function accionCerrar() 
		{		
				window.top.close();	
		}
	</script>	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->





</body>
</html>
