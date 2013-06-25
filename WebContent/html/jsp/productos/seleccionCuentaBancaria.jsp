<!-- seleccionCuentaBancaria.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
	String idPersona = (String)request.getAttribute("idPersona");
%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		function accionCerrar(){ 			
			window.top.close();
		}	
		
		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {
			var aux = new Array();
			var cadena;		
			f = document.solicitudCompraForm
			aux[0]=f.idCuenta.value;
			if(aux[0]==""){
				var mensaje = "<siga:Idioma key="pys.solicitudCompra.literal.nCuenta"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
				cadena=f.idCuenta[f.idCuenta.selectedIndex].text;
				aux[1]=cadena.substring(cadena.length-23,cadena.length);									
			top.cierraConParametros(aux);
		}	
	
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body>
<html:form action="/PYS_GenerarSolicitudes.do" method="POST" target="mainWorkArea" type="">
<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="pys.solicitudCompra.cabecera"/>
				</td>
			</tr>
		</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<div id="camposRegistro" class="posicionModalPeque" align="center">

		<!-- INICIO: CAMPOS -->
		<html:hidden property="modo" value="cerrar"/>		
		<table class="tablaCentralCamposPeque" align="center">			
			<tr>				
				<td>					
						<table class="tablaCampos" align="center">	
							<!-- FILA -->
							<tr>		
								<td class="labelText">
									<siga:Idioma key="pys.solicitudCompra.literal.nCuenta"/>&nbsp;(*)
								</td>
								<td>								
	<%							String parametro[] = new String[2];
	   						 	parametro[0] = idPersona;
	   						 	parametro[1] = idInstitucion; 
	%>
									<siga:ComboBD nombre="idCuenta" tipo="cuentaCargo" clase="boxCombo" obligatorio="false" parametro="<%=parametro%>"/>
							</td>
														
							</tr>						
		   			</table>				
				</td>
			</tr>
		</table>		
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
		<siga:ConjBotonesAccion botones="Y,C" modal="P"/>


	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</html:form>
</body>
</html>
