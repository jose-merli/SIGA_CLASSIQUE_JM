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

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
//	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
//	String idUsr=(String)usr.getUserName();
	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");
	Hashtable htData=(Hashtable)request.getAttribute("hDatos");		
	String idPersona=String.valueOf(htData.get(CenCuentasBancariasBean.C_IDPERSONA));
	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;
%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Properties"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<html:rewrite page='/html/js/jquery-ui.js'/>" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="cuentasBancariasSolicForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 			
			window.top.close();
		}	
		
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer(){	
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.all.cuentasBancariasSolicForm.reset()
				rellenarCampos();				
				}						
		}			
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {	
			sub();	
			if(!(document.cuentasBancariasSolicForm.cuentaAbono.checked) && 
				 !(document.cuentasBancariasSolicForm.cuentaCargo.checked))  {
  				var mensaje = "<siga:Idioma key="censo.datosCuentaBancaria.literal.tipoCuenta"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
					alert (mensaje);
					fin();
				 	return false;
			}
			
			if (validateCuentasBancariasSolicForm(document.cuentasBancariasSolicForm)){
				if(!calcularDigito()){ 
					fin();	
					return false;		
				}	
				else {
					document.all.cuentasBancariasSolicForm.modo.value="insertarModificacion";					
					document.all.cuentasBancariasSolicForm.submit();				
				}		
			}else{
				fin();	
				return false;
				
			}	
		}			
		<!-- Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable -->
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
		
		// Funciones para obtener el d¡gito de control de la Cuenta
		function obtenerDigito(valor){	
		  valores = new Array(1, 2, 4, 8, 5, 10, 9, 7, 3, 6);
		  control = 0;
		  for (i=0; i<=9; i++)
		    control += parseInt(valor.charAt(i)) * valores[i];		  
		  control = 11 - (control % 11);
		  if (control == 11) control = 0;
		  else if (control == 10) control = 1;
		  return control;
		}
		
		function numerico(valor){
		  cad = valor.toString();
		  for (var i=0; i<cad.length; i++) {
		    var caracter = cad.charAt(i);
				if (caracter<"0" || caracter>"9"){					
				  return false;
				  }
		 	 }
		  return true;
		}
		
		function calcularDigito(){

			mensaje = "<siga:Idioma key="messages.censo.cuentasBancarias.errorCuentaBancaria"/>";
			f = document.all.cuentasBancariasSolicForm;		
			if (f.cbo_Codigo.value    == ""  || f.codigoSucursal.value == "" || f.digitoControl.value == ""  || f.numeroCuenta.value   == "" ){ 
				 alert(mensaje);
				 return false;
			}
			else{
				if(f.cbo_Codigo.value.length != 4 || f.codigoSucursal.value.length != 4 || f.digitoControl.value.length != 2 || f.numeroCuenta.value.length != 10){
					alert(mensaje);
					return false;
				}	 		
				else{
					if(!numerico(f.cbo_Codigo.value) || !numerico(f.codigoSucursal.value) || !numerico(f.digitoControl.value) || !numerico(f.numeroCuenta.value)){
						alert(mensaje);
						return false;
					}
					else {
						if(f.digitoControl.value != obtenerDigito("00" + f.cbo_Codigo.value + f.codigoSucursal.value) + "" + obtenerDigito(f.numeroCuenta.value)){
							alert(mensaje);
							return false;
						}
					}
				}
			}
			
			return true;  
		}				
		
	</script>	
	
	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body onLoad="rellenarCampos(); cargarBancos();">
<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="censo.consultaDatosBancarios.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
				      <%if ((numero != null) && (!numero.equalsIgnoreCase(""))){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%} 
					else {%>
					   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>
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
		<input type='hidden' name="idCuenta" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_IDCUENTA))%>">
		<table  class="tablaCentralCamposMedia" align="center">			
			<tr>				
				<td>
					<siga:ConjCampos leyenda="censo.solicitudModificacion.literal.titulo">
						<table class="tablaCampos" align="center" border="0">
							<!-- FILA -->
							<tr>		
								<td class="labelText">					
									<siga:Idioma key="censo.datosCuentaBancaria.literal.titular"/>&nbsp;(*)
								</td>
								<td class="labelText" colspan="3">
									<html:text name="cuentasBancariasSolicForm" property="titular" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_TITULAR))%>" size="75" maxlength="100" styleClass="box"></html:text>
								</td>
							</tr>

							<!-- FILA -->
							<tr>
								<td class="labelText">
									<siga:Idioma key="censo.tipoCuenta.abono"/><html:checkbox name="cuentasBancariasSolicForm" property="cuentaAbono"></html:checkbox>
								</td>
								<td class="labelText" colspan="2">
									<siga:Idioma key="censo.tipoCuenta.cargo"/><html:checkbox name="cuentasBancariasSolicForm" property="cuentaCargo"></html:checkbox>
								</td>
								<td class="labelText">
									<siga:Idioma key="censo.datosCuentaBancaria.literal.abonoSJCS"/><html:checkbox name="cuentasBancariasSolicForm" property="abonoSJCS"></html:checkbox>
								</td>
							</tr>										

							<!-- FILA -->
							<tr>
								<td class="labelText">
									<siga:Idioma key="censo.datosCuentaBancaria.literal.banco"/>
								</td>
								<td class="labelText" COLSPAN="3">
									<select style="width:500px;" id="banco" class="boxCombo" onchange="cuentasBancariasSolicForm.cbo_Codigo.value=this.value">																		
									</select>									
					   			</td>
							</tr>
							<!-- FILA -->
							<tr>
								<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.codigoBanco"/>&nbsp;(*)</td>
								<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.codigoSucursal"/>&nbsp;(*)</td>	
								<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.digitoControl"/>&nbsp;(*)</td>	
								<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.cuenta"/>&nbsp;(*)</td>			
							</tr>
							<tr>										
								<td class="labelText"><html:text name="cuentasBancariasSolicForm" property="cbo_Codigo" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_CBO_CODIGO))%>"         size="4"  maxlength="4"  styleClass="box" onChange="cargarBancos();"></html:text></td>
								<td class="labelText"><html:text name="cuentasBancariasSolicForm" property="codigoSucursal" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL))%>" size="4"  maxlength="4"  styleClass="box"></html:text></td>
								<td class="labelText"><html:text name="cuentasBancariasSolicForm" property="digitoControl" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_DIGITOCONTROL))%>"   size="2"  maxlength="2"  styleClass="box"></html:text></td>						
								<td class="labelText"><html:text name="cuentasBancariasSolicForm" property="numeroCuenta" value="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_NUMEROCUENTA))%>"     size="10" maxlength="10" styleClass="box"></html:text></td>
							</tr>
								<!-- FILA -->
		  				<tr>
		   					<td class="labelText">
									<siga:Idioma key="censo.datosCuentaBancaria.literal.motivo"/>&nbsp;(*)
								</td>											
		   					<td class="labelText" COLSPAN="3">
		   						<html:textarea name="cuentasBancariasSolicForm" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" property="motivo" styleClass="box" cols="90" rows="3" value=""/>
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
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</html:form>
</body>
</html>

<script>
	function cargarBancos() {
		var idBanco = SolicitudIncorporacionForm.cbo_Codigo.value;	
		if (idBanco!=undefined&&idBanco!="") {
			jQuery.ajax({ //Comunicación jQuery hacia JSP  
   				type: "POST",
				url: "/SIGA/CEN_CuentasBancarias.do?modo=getAjaxBanco",
				data: "idBanco="+idBanco,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){		
					var txtBanco = json.banco.nombre;
					SolicitudIncorporacionForm.banco.value=txtBanco;
					fin();
				},
				error: function(e){
					alert('Error de comunicación: ' + e);
					fin();
				}
			});
		}
	}
</script>