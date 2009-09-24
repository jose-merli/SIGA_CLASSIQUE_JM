<!-- resumenPagos.jsp -->
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
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.facturacionSJCS.form.ResumenPagosForm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	boolean esLetrado = user.isLetrado();
	String DB_TRUE  = ClsConstants.DB_TRUE;
	String DB_FALSE = ClsConstants.DB_FALSE;
	
	String nombre = (String)request.getAttribute("nombrePersona");
	String numero = (String)request.getAttribute("numero");
	String nif =    (String)request.getAttribute("nif");		
	Long lIdPersona = (Long)request.getAttribute("idPersona");
	String idPersona = "";
	if (lIdPersona != null) {
		idPersona = String.valueOf(lIdPersona);
	}
	String idInstitucion[] = new String[1];
	idInstitucion[0] = user.getLocation();
	
	String parametro[] = new String[2];

	// Formulario
	ResumenPagosForm formulario = (ResumenPagosForm) request.getAttribute("ResumenPagosForm");
	
	parametro[0] = user.getLocation();
	parametro[1] = formulario.getFacturacion();
	
	

	
%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="factSJCS.resumenPagos.cabecera" 
		localizacion="factSJCS.resumenPagos.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		
			function actualizarCliente() {
					document.all.ResumenPagosForm.target = "mainWorkArea";
					document.all.ResumenPagosForm.modo.value = "actualizarCliente";
				    document.all.ResumenPagosForm.submit();
			}
			
		  
			function buscarCliente() {
			
			 	var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
			 	// Si he recuperado datos y el nuevo idpersona es distinto de la anterior persona
				if((resultado    != undefined) && 
					 (resultado[0] != undefined) && 
					 (resultado[0] != document.all.ResumenPagosForm.idPersona.value)) {
						
						if(document.all.ResumenPagosForm.idPersona.value != resultado[0]) {
							
								document.all.ResumenPagosForm.idPersona.value = resultado[0];
								document.all.ResumenPagosForm.numeroColegiado.value = resultado[2];
								document.all.ResumenPagosForm.nif.value = resultado[3];
								document.all.ResumenPagosForm.nombrePersona.value = resultado[4] + " " + resultado[5] +  " " + resultado[6];	

							 	document.busquedaClientesModalForm.numeroColegiado.value = resultado[2];
							 	document.busquedaClientesModalForm.nif.value = resultado[3];
							 	document.busquedaClientesModalForm.nombrePersona.value =resultado[4] + " " + resultado[5] +  " " + resultado[6];	

								//actualizarCliente();
							
					}					
				}			 	
			}
			
			function accionGenerarInforme() 
			{		
				var f=document.forms[1];
    			var fname = document.getElementById("ResumenPagosForm").name;
    			
    			sub();
				if(f.idPersona.value == ""){
					alert("<siga:Idioma key="factSJCS.resumenPagos.literal.seleccionarPersona"/>");
					fin();
					return false;
				}else if(f.facturacion.value == "" || f.pago.value == ""){	
				//else if(f.facturacion.value == "")
						alert("<siga:Idioma key="factSJCS.resumenPagos.literal.selectFacturacion"/>");
						fin();
						return false;
					 }else
					 {
					 	f.modo.value="descargaFicheros";
					 	f.target = "submitArea";
					 	document.frames.submitArea.location='<%=app%>/html/jsp/general/loadingWindowOpener.jsp?formName='+fname+'&msg=messages.factSJCS.procesandoInforme';
					 }
			}

	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>

<body>

				<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="submitArea" type="">
  				<input type="hidden" name="actionModal" value="">
  				<input type="hidden" name="modo" value="abrirBusquedaModal">
  				<input type="hidden" name="ventana" 		value="solicitud">

						<siga:ConjCampos leyenda="pys.solicitudCompra.leyenda.cliente">	
							<table class="tablaCampos" align="center" border=0>
							<!-- FILA -->
								<tr>
									<td class="labelText" nowrap><siga:Idioma key="factSJCS.resumenPagos.literal.nColegiado"/>&nbsp;(*)</td>
									<td>
										<html:text name="busquedaClientesModalForm" property="numeroColegiado" value="<%=numero%>" size="10" maxlength="20" styleClass="boxConsulta" readOnly="true"></html:text></td>
									</td>	
									<td class="labelText" nowrap><siga:Idioma key="factSJCS.resumenPagos.literal.nombre"/>&nbsp;(*)</td>				
									<td colspan="3">
										<html:text name="busquedaClientesModalForm" property="nombrePersona" value="<%=nombre%>" size="80" styleClass="boxConsulta" readOnly="true"></html:text>
									</td>
								</tr>
								<!-- FILA -->
								<tr>	
									
									<td class="labelText" nowrap><siga:Idioma key="factSJCS.resumenPagos.literal.nifcif"/>&nbsp;(*)</td>
									<td>
										<html:text name="busquedaClientesModalForm" property="nif" value="<%=nif%>" size="30" styleClass="boxConsulta" readOnly="true"></html:text></td>
									</td>	

									
									
									<td align="right" colspan=2>
	<%								if(!esLetrado)	{ %>									
											<html:button property="idButton" onclick="return buscarCliente();" styleClass="button"> 
												<siga:Idioma key="general.boton.search"/> 
										</html:button> 			
	<%}%>
									</td>
								</tr>
							</table>
						</siga:ConjCampos>
				</html:form>

				<html:form action="/JGR_ResumenPagos.do" method="POST" target="resultado">
			
					<html:hidden name="ResumenPagosForm" property = "idPersona" 		value = "<%=idPersona%>"/>
					<html:hidden name="ResumenPagosForm" property = "numeroColegiado"   value = "<%=numero%>"/>	
					<html:hidden name="ResumenPagosForm" property = "nombrePersona" 	value = "<%=nombre%>"/>
					<html:hidden name="ResumenPagosForm" property = "nif" 				value = "<%=nif%>"/>				
					<input type="hidden" name="modo" value="">
					<input type="hidden" name="actionModal">

					<siga:ConjCampos leyenda="Datos Pago">	
							<table class="tablaCampos" align="center" border=0>
							<!-- FILA -->
								<tr>				
									<td class="labelText">
										<siga:Idioma key="factSJCS.resumenPagos.literal.facturacion"/>&nbsp;(*)
									</td>				
									<td>
										<siga:ComboBD nombre="facturacion" tipo="cmbFacturacionResumenPagos"  clase="boxCombo" obligatorio="true" parametro="<%=idInstitucion%>" obligatorioSinTextoSeleccionar="false" accion="Hijo:pago"/>
									</td>
									<td class="labelText">
										<siga:Idioma key="factSJCS.resumenPagos.literal.pago"/>&nbsp;(*)
									</td>				
									<td>
										<siga:ComboBD nombre="pago" tipo="cmbPagoResumenPagos"  clase="boxCombo" obligatorio="false" hijo="t" />
									</td>
								</tr>
							</table>
						</siga:ConjCampos>
					
			</html:form>

		<siga:ConjBotonesAccion clase="botonesSeguido" botones="GM"  />	
		
<!--		<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
		</iframe> -->
	


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>
</html>