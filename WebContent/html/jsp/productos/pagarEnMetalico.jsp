<!DOCTYPE html>
<html>
<head>
<!-- pagarEnMetalico.jsp -->
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

<!-- IMPORTS -->

<!-- JSP -->
<% String app=request.getContextPath(); %>




<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>


	<!-- Validaciones en Cliente -->
	<html:javascript formName="GestionSolicitudesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>


	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 
			var datos = new Array();
			datos[0] = 0;		// Boton cerrar
			datos[1] = "";
			window.top.returnValue=datos;
			window.top.close();
		}	
	
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {

		  
		  // Validamos los datos
		  var importeAnticipado = document.GestionSolicitudesForm.importeAnticipado.value;
		  importeAnticipado=importeAnticipado.replace(/,/,"."); 
                        
                        

		  if (isNaN(importeAnticipado)) {
				var mensaje = "<siga:Idioma key="pys.gestionSolicitudes.literal.importeAnticipado"/> <siga:Idioma key="messages.campoNumerico.error"/>";
				alert (mensaje);
		   	return false;
		  }

			var importeUnitario = window.dialogArguments;
			
			if (importeAnticipado > parseFloat(importeUnitario)) {
				alert ("<siga:Idioma key="pys.gestionSolicitudes.literal.importeAnticipado"/> no puede ser superior a " + importeUnitario);
				return false;
			}
						
			var datos = new Array();
			datos[0] = 1;								// Boton aceptar
			datos[1] = importeAnticipado;		// Valor importe

			window.top.returnValue = datos;
			window.top.close();
		}		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
				<td id="titulo" class="titulitosDatos"><siga:Idioma key="pys.gestionSolicitudes.literal.pagoMetalico"/></td>
		</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/PYS_GestionarSolicitudes.do" method="POST">
		<table class="tablaCentralCamposPeque" align="center">
			<tr>
				<td>
					<table class="tablaCampos" border="0">	
						<tr>
							<td class="labelText" colspan="2"><siga:Idioma key="pys.gestionSolicitudes.literal.tituloPagoAnticipado"/></td>
						</tr>
						<tr><td>&nbsp;</td></tr>
						<tr>
							<td class="labelText"><siga:Idioma key="pys.gestionSolicitudes.literal.importeAnticipado"/></td>
							<td class="labelText"><input type="text" name="importeAnticipado"  maxlength="10" size="10" style="box" value="0"/>&nbsp;&euro;
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</html:form>
	<!-- FIN: CAMPOS -->

	<siga:ConjBotonesAccion botones='C,Y' modo='' modal="P" />

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>