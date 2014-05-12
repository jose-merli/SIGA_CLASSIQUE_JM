<!DOCTYPE html>
<html>
<head>
<!--solicitudModificacionCuentasBancarias.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL MEDIANA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
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
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.CenComponentesBean"%>
<%@ page import = "com.siga.beans.CenCuentasBancariasBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "java.util.Hashtable"%>
<%@ page import = "java.util.ArrayList"%>
<%@ page import = "java.util.Properties"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");
	Hashtable htData=(Hashtable)request.getAttribute("hDatos");		
	String idPersona=String.valueOf(htData.get(CenCuentasBancariasBean.C_IDPERSONA));
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;
	
	Boolean visibilidad = false;
	if(htData.get(CenCuentasBancariasBean.C_NUMEROCUENTA) != null && !(String.valueOf(htData.get(CenCuentasBancariasBean.C_NUMEROCUENTA))).equals("")){
		visibilidad = true;
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="cuentasBancariasSolicForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		// Asociada al boton Volver
		function accionCerrar(){ 			
			window.top.close();
		}	
		
		// Asociada al boton Restablecer
		function accionRestablecer(){	
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.all.cuentasBancariasSolicForm.reset()
				rellenarCampos();				
				}						
		}	
		
		// Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {	
			sub();	
			if(!(document.cuentasBancariasSolicForm.cuentaAbono.checked) && !(document.cuentasBancariasSolicForm.cuentaCargo.checked))  {
  				var mensaje = "<siga:Idioma key="censo.datosCuentaBancaria.literal.tipoCuenta"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
					alert (mensaje);
					fin();
				 	return false;
			}
			
			if (validateCuentasBancariasSolicForm(document.cuentasBancariasSolicForm)){
				iban = document.cuentasBancariasSolicForm.IBAN.value;
				bic = document.cuentasBancariasSolicForm.BIC.value;
				banco = document.cuentasBancariasSolicForm.banco.value;
				
				//SE VALIDA SI SE HA INTODUCIDO IBAN Y BIC
				if (iban == ""  && bic == ""){ 
					mensaje = "<siga:Idioma key='messages.censo.cuentasBancarias.errorCuentaBancaria'/>";
					alert(mensaje);
					fin();
					return false;
				} 				
				
				if(!validarCuentaBancaria(iban,bic,banco)){
					fin();
					return false;
					 
				} else {
					document.all.cuentasBancariasSolicForm.modo.value="insertarModificacion";					
					document.all.cuentasBancariasSolicForm.submit();				
				}		
			}else{
				fin();	
				return false;
				
			}	
		}			
		
		// Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable
		function rellenarCampos(){
			// Obtenemos el valor para los check Tipo de Cuenta.
			abonoCargo="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_ABONOCARGO))%>";				
			if(abonoCargo =="<%=ClsConstants.TIPO_CUENTA_ABONO%>"){	
				document.all.cuentasBancariasSolicForm.cuentaAbono.checked=true;			
				document.all.cuentasBancariasSolicForm.cuentaCargo.checked=false;
			}else if(abonoCargo =="<%=ClsConstants.TIPO_CUENTA_CARGO%>"){	
				document.all.cuentasBancariasSolicForm.cuentaCargo.checked=true;
				document.all.cuentasBancariasSolicForm.cuentaAbono.checked=false;	
			}else{		
				document.all.cuentasBancariasSolicForm.cuentaAbono.checked=true;
				document.all.cuentasBancariasSolicForm.cuentaCargo.checked=true;
			}	
			
			// Obtenemos los valores para el check abonoSJCS.
			abonoSJCS="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_ABONOSJCS))%>"		
		  if(abonoSJCS=="<%=DB_TRUE%>"){
		  	document.all.cuentasBancariasSolicForm.abonoSJCS.checked=true;
		  }			
		}
		
		var mensajeGeneralError='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usr, "messages.general.error"))%>';

		function cargarBancoPorIBAN(){
			mensaje = "<siga:Idioma key="messages.censo.cuentasBancarias.errorCuentaBancaria"/>";	
			var iban = formateaMask(document.getElementById("IBAN").value);	
			if (iban!=undefined && iban!="") {			
				jQuery.ajax({ //Comunicacion jQuery hacia JSP  
	   				type: "POST",
					url: "/SIGA/CEN_CuentasBancarias.do?modo=getAjaxBancoBIC",
					data: "iban="+iban,
					dataType: "json",
					contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					success: function(json){	
						if(json!=null && json.pais != null){
							if(json.pais == "ES"){
								//Se comprueba si el banco existe
								if(json.banco != null){
									var bic = json.banco.bic;
									document.getElementById("BIC").value=bic;
									document.getElementById("BIC").readOnly = true;
									document.getElementById("BIC").className = "boxConsulta";
								
									//Se rellena el banco
									var txtBanco = json.banco.nombre;
									document.getElementById("banco").value=txtBanco;
								} else {
									alert(mensaje);
									document.getElementById("BIC").value="";
									document.getElementById("banco").value="";
									document.getElementById("BIC").readOnly = true;
									document.getElementById("BIC").className = "boxConsulta";
									fin();
								}
								
							}else{
								document.getElementById("BIC").readOnly = false;
								document.getElementById("BIC").className = "box";
								document.getElementById("banco").value="";
								document.getElementById("BIC").value="";
								alert("Rellene el BIC para el banco extranjero");
							}
							
						}else{
							alert(mensaje);
							document.getElementById("BIC").value="";
							document.getElementById("banco").value="";
							document.getElementById("BIC").readOnly = true;
							document.getElementById("BIC").className = "boxConsulta";
						}
						fin();
					},
					error: function(e){
						alert(mensajeGeneralError);
						document.getElementById("BIC").value="";
						document.getElementById("banco").value="";
						document.getElementById("BIC").readOnly = true;
						document.getElementById("BIC").className = "boxConsulta";
						fin();
					}
				});
				
			} else {
				document.getElementById("IBAN").value="";
				document.getElementById("BIC").value="";
				document.getElementById("banco").value="";
				document.getElementById("BIC").readOnly = true;
				document.getElementById("BIC").className = "boxConsulta";
			}
		}	
		
		function inicioCargarBancoBIC(){
			var iban = formateaMask(document.getElementById("IBAN").value);
			var codigoBanco ="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_CBO_CODIGO))%>";
			if (iban!=undefined && iban!="") {			
				jQuery.ajax({ //Comunicacion jQuery hacia JSP  
	   				type: "POST",
					url: "/SIGA/CEN_CuentasBancarias.do?modo=getAjaxCargaInicialBancoBIC",
					data: "iban="+iban+"&codigo="+codigoBanco,
					dataType: "json",
					contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					success: function(json){	
						if(json.banco!=null && json.banco!=""){
							document.getElementById("BIC").value=json.banco.bic;
							document.getElementById("banco").value=json.banco.nombre;
						}
						fin();
					},
					error: function(e){
						alert(mensajeGeneralError);
						fin();
					}
				});
			}
		}	
		
	function rpad() {
		if (document.getElementById("BIC").value.length == 8){
	    	while (document.getElementById("BIC").value.length < 11)
	    		document.getElementById("BIC").value = document.getElementById("BIC").value + 'X';
		}
	}
	
	function validaAbonoSJCS() {
		if (document.all.cuentasBancariasSolicForm.abonoSJCS.checked) {
			if (!document.all.cuentasBancariasSolicForm.cuentaAbono.checked) {
				var mensaje = "<siga:Idioma key="messages.censo.cuentasBancarias.cuentaSJCS"/>";
				alert (mensaje);
				return false;
			}
		}
	}	
		
	</script>	
</head>

<body onLoad="rellenarCampos(); inicioCargarBancoBIC();">
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="censo.consultaDatosBancarios.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
				<% if ((numero != null) && (!numero.equalsIgnoreCase(""))) { %>
					<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
				<% } else { %>
				   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
				<% } %>
			</td>
		</tr>
	</table>

	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<html:form action="/CEN_SolicitudCuentasBancarias.do" method="POST" target="submitArea">

		<div id="camposRegistro" class="posicionModalMedia" align="center">
			<!-- INICIO: CAMPOS -->
			<html:hidden property="modo" value="cerrar"/>
			<input type='hidden' name="idPersona" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_IDPERSONA))%>"/>	
			<input type='hidden' name="idInstitucion" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_IDINSTITUCION))%>"/>
			<input type='hidden' name="idCuenta" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_IDCUENTA))%>" />
			
			<table  class="tablaCentralCamposMedia" align="center">			
				<tr>				
					<td>
						<siga:ConjCampos leyenda="censo.solicitudModificacion.literal.titulo">
							<table class="tablaCampos" align="center" border="0">
								<!-- FILA -->
								<tr>		
									<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.titular"/>&nbsp;(*)</td>
									<td class="labelText" colspan="3">
										<!--<html:text name="cuentasBancariasSolicForm" property="titular" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_TITULAR))%>" size="75" maxlength="100" styleClass="box"/>-->
										<html:text name="cuentasBancariasSolicForm" property="titular" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_TITULAR))%>" size="75" styleClass="boxConsulta" readonly="true"/>
									</td>
								</tr>
							
								<!-- FILA -->
								<tr>						
									<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.codigoIBAN"/>&nbsp;(*)</td>
									<td class="labelText">
										<!--<html:text size="34"  maxlength="34" name="cuentasBancariasSolicForm" styleId="IBAN" property="IBAN" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_IBAN))%>"  styleClass="box" readonly="false" onblur="cargarBancoPorIBAN();"/>-->
										<html:text size="34" name="cuentasBancariasSolicForm" styleId="IBAN" property="IBAN" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_IBAN))%>" styleClass="boxConsulta" readonly="true" onblur="cargarBancoPorIBAN();"/>
									</td>
	
									<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.codigoBIC"/>&nbsp;</td>
									<td class="labelText"><html:text size="14"  maxlength="11" name="cuentasBancariasSolicForm" styleId="BIC" property="BIC" styleClass="boxConsulta" readonly="true" onblur="rpad();"/></td>
								</tr>								
							
								<!-- FILA -->
								<tr>
									<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.banco"/></td>
									<td class="labelText" COLSPAN="3"><input type="text" id="banco" style="width:500px;" class="boxConsulta" readonly /></td>
								</tr>							

								<!-- FILA -->
								<tr>
									<td colspan="4">	
										<table>
											<tr>					
												<td class="labelText"><siga:Idioma key="censo.tipoCuenta.abono"/></td>												
												<td><html:checkbox name="cuentasBancariasSolicForm" property="cuentaAbono" onchange="validaAbonoSJCS()"/></td>
												
												<td class="labelText"><siga:Idioma key="censo.tipoCuenta.cargo"/></td>												
												<td><html:checkbox name="cuentasBancariasSolicForm" property="cuentaCargo"/></td>
																								
												<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.abonoSJCS"/></td>
												<td><html:checkbox name="cuentasBancariasSolicForm" property="abonoSJCS" onchange="validaAbonoSJCS()" /></td>
											</tr>
										</table>						
									</td>
								</tr>																		

<%	
								if (visibilidad) { 
%>
									<!-- FILA -->
									<tr>
										<td colspan="4">
											<siga:ConjCampos leyenda="datosCuentaBancaria.literal.formatoAntiguo">
												<table>
				
													<!-- FILA -->
													<tr>						
														<td class="labelText" nowrap colspan="2">C.C.C.&nbsp;(*)</td>
														<td class="labelText">
															<html:text name="cuentasBancariasSolicForm" property="cbo_Codigo" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_CBO_CODIGO))%>"         size="4"  maxlength="4"  styleClass="boxConsulta" readonly="true"/>
															-
															<html:text name="cuentasBancariasSolicForm" property="codigoSucursal" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL))%>" size="4"  maxlength="4"  styleClass="boxConsulta" readonly="true"/>
															-
															<html:text name="cuentasBancariasSolicForm" property="digitoControl" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_DIGITOCONTROL))%>"   size="2"  maxlength="2"  styleClass="boxConsulta" readonly="true"/>
															-				
															<html:text name="cuentasBancariasSolicForm" property="numeroCuenta" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_NUMEROCUENTA))%>"     size="10" maxlength="10" styleClass="boxConsulta" readonly="true"/>
														</td>
													</tr>
												</table>
											</siga:ConjCampos>
										</td>
									</tr>
<%
								}
%>									
							
								<!-- FILA -->
				  				<tr>
				   					<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.motivo"/>&nbsp;(*)</td>											
				   					<td colspan="3">
				   						<textarea class="box" name="motivo" value=""
											onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"	
											style="overflow-y:auto; overflow-x:hidden; width:590px; height:50px; resize:none;"
										></textarea>
									</td>		   					
				  				</tr>						
							</table>
							<!-- TABLA -->
						</siga:ConjCampos>
					</td>
				</tr>
			</table>		
			<!-- FIN: CAMPOS -->

			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			<siga:ConjBotonesAccion botones="C,Y,R" modal="M"/>
			<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
			
		</div>
		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
	</html:form>		
			
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>