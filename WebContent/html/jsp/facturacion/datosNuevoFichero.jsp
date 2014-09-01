<!DOCTYPE html>
<html>
<head>
<!-- datosNuevoFichero.jsp -->

<!-- 
	 Muestra el formulario de incorporacion de nuevo fichero de devoluciones
	 VERSIONES:
		 miguel.villegas 22/03/2005
-->

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
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.UsrBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses = request.getSession();	
	UsrBean userBean = (UsrBean) ses.getAttribute("USRBEAN");
	
	// Obtengo el identificador de la institucion
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION"); 	
	
	// MENSAJE = mensaje a mostrar (si no hay mensaje no muestra alert)  
	String mensaje = (String)request.getAttribute("mensaje");

	// Carga la fecha actual
	String fechaActual = UtilidadesBDAdm.getFechaBD("");
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="DevolucionesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		
<%  
		if (mensaje!=null){
			String msg=UtilidadesString.escape(UtilidadesString.getMensajeIdioma(userBean.getLanguage(),mensaje));
			String estilo="notice";
			if(mensaje.contains("error")) {
				estilo="error";
			} else if(mensaje.contains("success")||mensaje.contains("updated")) {
				estilo="success";
			} 
%>
			alert(unescape("<%=msg %>"),"<%=estilo%>");
			
<%  
		} 
%>

		// Asociada al boton Volver
		function accionCerrar() {  
			window.top.close();
		}	
		
		// Asociada al boton Volver
		function accionGuardarCerrar(){ 			
			if(document.getElementById('comision').checked)
				document.forms[0].comisiones.value="1";
			else
				document.forms[0].comisiones.value="0";

			if (validateDevolucionesForm(document.DevolucionesForm)) {
				document.forms[0].modo.value="insertar";
				window.frames.submitArea.location="<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName=" + document.forms[0].name + "&msg=facturacion.nuevoFichero.literal.generandoDevoluciones";
			}	
		}	
		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos"><siga:Idioma key="facturacion.nuevoFichero.cabecera"/></td>
		</tr>
	</table>

	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/FAC_Devoluciones.do" method="POST" target="submitArea"  enctype="multipart/form-data">
		<html:hidden property ="modo" value = ""/>
		<html:hidden property ="comisiones" />
		<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
		<table class="tablaCentralCamposPeque"  align="center">			
			<tr>				
				<td>
					<siga:ConjCampos leyenda="facturacion.nuevoFichero.literal.datosFichero">
						<table class="tablaCampos" align="center">	
							<tr>
								<td class="labelText" >
									<siga:Idioma key="facturacion.nuevoFichero.literal.seleccion"/>&nbsp;(*)
								</td>
							</tr>
							<tr>
								<td class="labelText">
									<html:file property="ruta" styleClass="box" readOnly="false" onChange="rutaVisible.value=ruta.value" style="width:650px"></html:file>
									<div style="display:none; position:absolute; width:18px; height:35px; z-index:2; top: 60px; left: 25px">
										<html:text styleClass="box" property="rutaVisible" size="60" value="" readOnly="true" />
									</div>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
			
			<tr>
				<td>
					<siga:ConjCampos leyenda="facturacion.pagosFactura.Renegociar.Titulo">
						<table class="tablaCampos" border="0">
							<tr>
								<td>
									<input type="radio" id="radio0" name="datosPagosRenegociarNuevaFormaPago" checked="checked" value="noRenegociarAutomaticamente" >
								</td>
								<td class="labelText">										
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.noRenegociar"/>
								</td>
							</tr>
							
							<tr>
								<td>								
									<input type="radio" id="radio1" name="datosPagosRenegociarNuevaFormaPago" value="mismaCuenta" >
								</td>
								<td class="labelText">
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.MismaCuenta"/>
								</td>
							</tr>
							
							<tr>
								<td>
									<input type="radio" id="radio2" name="datosPagosRenegociarNuevaFormaPago" value="porOtroBanco"/>
								</td>
								<td class="labelText">
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorBanco"/>
								</td>	
							</tr>
							
							<tr>
								<td>
									<input type="radio" id="radio3" name="datosPagosRenegociarNuevaFormaPago" value="porCaja">
								</td>
								<td class="labelText">
									<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.NuevaFormaPago.PorCaja"/>
								</td>
							</tr>		
						</table>
																										
						<fieldset>		
							<table class="tablaCampos" border="0">
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.pagosFactura.Caja.literal.Fecha"/>&nbsp;(*)
									</td>
									<td>
										<siga:Fecha nombreCampo="datosRenegociarFecha" valorInicial="<%=fechaActual%>" posicionX="50px" posicionY="10px"/>
									</td>
							   	</tr>							
							
								<tr>
									<td class="labelText">
										<siga:Idioma key="facturacion.pagosFactura.Renegociar.literal.Observaciones"/>
									</td>
									<td>
										<html:textarea property="datosPagosRenegociarObservaciones" 
											onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" 
											style="overflow-y:auto; overflow-x:hidden; width:540px; height:80px; resize:none;"
											styleClass="box" value=""/>
									</td>
								</tr>
							</table>
						</fieldset>
					</siga:ConjCampos>
				</td>
			</tr>
		
			<tr>
				<td>
					<siga:ConjCampos leyenda="Comisiones">
						<table class="tablaCampos" border="0">
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.nuevoFichero.literal.cargaComisiones"/>
									&nbsp;
									<input type = "checkbox"  name="comision" />
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
	</html:form>
	
	<siga:ConjBotonesAccion botones='Y,C' modo=''  modal="P"/>
	<!-- FIN: CAMPOS -->
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>