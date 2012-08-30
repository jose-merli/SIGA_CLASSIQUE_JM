<!-- datosCuentaBancaria.jsp -->
<!-- EJEMPLO DE  VENTANA DENTRO DE  VENTANA MODAL MEDIANA  --> <!-- Contiene  la
zona de campos del registro y la  zona de botones de acciones sobre el  registro
VERSIONES: -->

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
<%@ page import = "com.siga.Utilidades.*"%>
<%@ page import = "com.atos.utils.*"%>

<!-- JSP -->
<% 
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
	boolean bOcultarHistorico = usr.getOcultarHistorico();
	

	// Varibles que dependen del modo de la pagina (consulta, edicion, nuevo)
	boolean editarCampos = false;
	boolean desactivado  = false;
	String clase   = "box";
	String claseCombo = "boxCombo";
	String botones = "C,Y,R";

	String DB_TRUE  = ClsConstants.DB_TRUE;
	String DB_FALSE = ClsConstants.DB_FALSE;

	String idUsr     = (String)usr.getUserName();
	String numero    = (String)request.getAttribute("numero");
	String nombreUsu = (String)request.getAttribute("nombrePersona");

	String idPersona = String.valueOf((Long)request.getAttribute("idPersona"));
	String idInstitucion = String.valueOf((Integer)request.getAttribute("idInstitucion"));

	Hashtable htData = null;
	String idCuenta = "";
	String titular = "";
	String cuentaContable = "";
	String cbo_Codigo = "";
	String cuentaCodigoSucursal = "";
	String cuentaDigitoControl = "";
	String cuentaNumeroCuenta = "";
	String fechaBaja = "";

	String modo=(String)request.getAttribute("modoConsulta");		
	if (modo.equals("ver") || modo.equals("editar")) {
		htData = (Hashtable)request.getSession().getAttribute("DATABACKUP");
		if (htData != null) {
				titular = String.valueOf(htData.get(CenCuentasBancariasBean.C_TITULAR));
				cuentaContable = String.valueOf(htData.get(CenCuentasBancariasBean.C_CUENTACONTABLE));
				cbo_Codigo = String.valueOf(htData.get(CenCuentasBancariasBean.C_CBO_CODIGO));
				cuentaCodigoSucursal = String.valueOf(htData.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL));
				cuentaDigitoControl  = String.valueOf(htData.get(CenCuentasBancariasBean.C_DIGITOCONTROL));
				cuentaNumeroCuenta = String.valueOf(htData.get(CenCuentasBancariasBean.C_NUMEROCUENTA));
				idCuenta = String.valueOf(htData.get(CenCuentasBancariasBean.C_IDCUENTA));
				idInstitucion = String.valueOf(htData.get(CenCuentasBancariasBean.C_IDINSTITUCION));
				fechaBaja = String.valueOf(htData.get(CenCuentasBancariasBean.C_FECHABAJA));
				if ((fechaBaja != null) && !fechaBaja.equals(""))
					fechaBaja = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", fechaBaja));
				else 
					fechaBaja = "";
		}

		if (modo.equals("editar")) {
			desactivado  = false;
			editarCampos = true;	
		}
		else {
			desactivado = true;
			clase = "boxConsulta";
			claseCombo = clase;
			cuentaNumeroCuenta = UtilidadesString.mostrarNumeroCuentaConAsteriscos(cuentaNumeroCuenta);
		}
	} 
	else {
		if (modo.equals("nuevo")) {
			editarCampos = true;	
			desactivado = false;
			titular = nombreUsu;
		}
	}
%>	

<%@page import="java.util.Properties"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.ArrayList"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<html:rewrite page='/html/js/jquery-ui.js'/>" type="text/javascript"></script>	
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="cuentasBancariasForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	
		<!-- Asociada al boton Volver -->
		function accionCerrar(){ 		
			window.parent.close();
		}	
		
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer(){		
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.all.cuentasBancariasForm.reset();
				rellenarCampos();
			}						
		}			

		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() {		
         // Validamos los errores ///////////
			sub();
			if ((!document.all.cuentasBancariasForm.cuentaAbono.checked) && 
			    (!document.all.cuentasBancariasForm.cuentaCargo.checked)) {
				var mensaje = "<siga:Idioma key="censo.datosCuentaBancaria.literal.tipoCuenta"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert(mensaje);
				fin();
			    return false;
			}
			
			if(!calcularDigito()){
				fin();
				return false;
				 
			}		
           
			if (!validateCuentasBancariasForm(document.cuentasBancariasForm)){
				fin();
				return false;
			}
			
			////////////////////////////////////

			<% if (!bOcultarHistorico) { %>
				var datos = showModalDialog("/SIGA/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
				window.top.focus();
			<% } else { %>
					var datos = new Array();
					datos[0] = 1;
					datos[1] = "";
			<% } %>

			if (datos == null){
				fin();
				return false;
			}
			
			if (datos[0] == 1) { // Boton Guardar
				document.cuentasBancariasForm.motivo.value = datos[1];
				<%if (modo.equals("editar")) {%>
					document.cuentasBancariasForm.modo.value = "modificar";
				<%}	else { %>
					document.cuentasBancariasForm.modo.value = "insertar";
				<%}%>
				document.cuentasBancariasForm.target = "submitArea";
				document.cuentasBancariasForm.submit();
			}else{
				fin();
				return false;
			}
		}		
		<!-- Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable -->
		function rellenarCampos(){
			<%if (htData != null) {%>
				// Obtenemos el valor para los check Tipo de Cuenta.
				var abonoCargo = "";
				abonoCargo="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_ABONOCARGO))%>";				
				if(abonoCargo =="<%=ClsConstants.TIPO_CUENTA_ABONO%>"){	
					document.all.cuentasBancariasForm.cuentaAbono.checked=true;
				}else if(abonoCargo =="<%=ClsConstants.TIPO_CUENTA_CARGO%>"){	
					document.all.cuentasBancariasForm.cuentaCargo.checked=true;
				}else{
					document.all.cuentasBancariasForm.cuentaAbono.checked=true;
					document.all.cuentasBancariasForm.cuentaCargo.checked=true;
				}
			
				// Obtenemos los valores para el check sociedad.
				sociedad="<%=String.valueOf(request.getAttribute("sociedad"))%>"		
			  if(sociedad=="<%=DB_TRUE%>"){
			  	document.all.cuentasBancariasForm.sociedad.checked=true;	
			  } 
			  // Obtenemos los valores para el check abonoSJCS.
				abonoSJCS="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_ABONOSJCS))%>";
			  if(abonoSJCS=="<%=DB_TRUE%>"){
			  	document.all.cuentasBancariasForm.abonoSJCS.checked=true;	
			  }
			<%}%>
		}
		
		// Funciones para obtener el d�gito de control de la Cuenta
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
		
			f = document.all.cuentasBancariasForm;		
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

		function validaAbonoSJCS() {
			if (document.all.cuentasBancariasForm.abonoSJCS.checked) {
				if (!document.all.cuentasBancariasForm.cuentaAbono.checked) {
					var mensaje = "<siga:Idioma key="messages.censo.cuentasBancarias.cuentaSJCS"/>";
					alert (mensaje);
					document.all.cuentasBancariasForm.abonoSJCS.checked = false;
					return false;
				}
			}
		}
		
	</script>	
</head>

<body onLoad="cargarBancos();">
		<!-- TITULO -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="censo.consultaDatosBancarios.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreUsu)%> &nbsp;&nbsp;
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
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
-->
<div id="camposRegistro" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<html:form action="/CEN_CuentasBancarias.do" method="POST" target="resultado">
		<html:hidden property="modo" value=""/>
  
		<%if (editarCampos) {%>
			<html:hidden property="idCuenta" 		value="<%=idCuenta%>"/> 
			<html:hidden property="idPersona" 		value="<%=idPersona%>"/> 
			<html:hidden property="idInstitucion" 	value="<%=idInstitucion%>"/> 
			<html:hidden property="motivo" 			value=""/> 
		<%}%>
		<input type="hidden" name="abonoCargoOrig" value="<%=UtilidadesHash.getString(htData,CenCuentasBancariasBean.C_ABONOCARGO)%>">
		<table  class="tablaCentralCamposMedia"  align="center" border="0">
			<tr>				
				<td>
					<siga:ConjCampos leyenda="censo.consultaDatosBancarios.cabecera">

						<table class="tablaCampos" align="center" border="0">	
							<% if (!fechaBaja.equals("")) { %>
				   			    <tr>
					   		      <td colspan="3">&nbsp;</td>
					   		      <td class="labelText" >
					   		          <siga:Idioma key="censo.consultaDatos.literal.fechaBaja"/>&nbsp;&nbsp;<%=fechaBaja%>
					   		      </td>
				   			    </tr>
			   			    <% } %>

							<tr>		
								<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.titular"/>&nbsp;(*)</td>
								<td colspan="2" class="labelText"><html:text name="cuentasBancariasForm" property="titular" value="<%=titular%>" size="40" styleClass="<%=clase%>" maxlength="100" readOnly="<%=desactivado%>"></html:text></td>
								<%if (!modo.equals("nuevo")) {%>
									<td class="labelText">
										<siga:Idioma key="censo.datosCuentaBancaria.literal.sociedad"/>
										<html:checkbox name="cuentasBancariasForm" property="sociedad" disabled="true"></html:checkbox>
									</td>
								<%}%>
							<tr>
	
							<tr>								
								<td class="labelText">
									<siga:Idioma key="censo.tipoCuenta.abono"/><html:checkbox name="cuentasBancariasForm" property="cuentaAbono" disabled="<%=desactivado%>" onChange="validaAbonoSJCS()"/>
								</td>
								<td class="labelText" colspan="2">
									<siga:Idioma key="censo.tipoCuenta.cargo"/><html:checkbox name="cuentasBancariasForm" property="cuentaCargo" disabled="<%=desactivado%>"/>
								</td>												
								<td class="labelText">
									<siga:Idioma key="censo.datosCuentaBancaria.literal.abonoSJCS"/><html:checkbox name="cuentasBancariasForm" property="abonoSJCS" disabled="<%=desactivado%>" onChange="validaAbonoSJCS()" />
								</td>							
							</tr>							
	
						<%	if(idPersona.equals(idUsr)){ %>
								<tr></tr>
						<% }else{ %>
								<tr>
									<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.cuentaContable"/></td>
									<td class="labelText"><html:text name="cuentasBancariasForm" property="cuentaContable" value="<%=cuentaContable%>" size="20" maxlength="20" styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text></td>
								</tr>
						<% } %>  					
							
						
							<!-- FILA -->
							<tr>
								<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.banco"/></td>
								<td class="labelText" COLSPAN="3">
									<%if(desactivado){%>
										<input type="text" id="banco" style="width:500px;" class="boxConsulta" readonly></input>
									<%}else{%> 
										<select style="width:500px;" id="banco" class="claseCombo" onchange="cuentasBancariasForm.cbo_Codigo.value=this.value">																		
										</select>
									<%}%>
								</td>
							</tr>

							<!-- FILA -->
							<tr>						
								<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.codigoBanco"/>&nbsp;(*)</td>
								<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.codigoSucursal"/>&nbsp;(*)</td>
								<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.digitoControl"/>&nbsp;(*)</td>
								<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.cuenta"/>&nbsp;(*)</td>
							</tr>
							
							<!-- FILA -->
							<tr>						
								<td class="labelText"><html:text size="4"  maxlength="4" name="cuentasBancariasForm" property="cbo_Codigo"     value="<%=cbo_Codigo%>" 				styleClass="<%=clase%>" readOnly="<%=desactivado%>" onChange="cargarBancos();"></html:text></td>
								<td class="labelText"><html:text size="4"  maxlength="4" name="cuentasBancariasForm" property="codigoSucursal" value="<%=cuentaCodigoSucursal%>" 	styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text></td>
								<td class="labelText"><html:text size="5"  maxlength="2" name="cuentasBancariasForm" property="digitoControl"  value="<%=cuentaDigitoControl%>" 	styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text></td>
								<td class="labelText"><html:text size="10" maxlength="10" name="cuentasBancariasForm" property="numeroCuenta"  value="<%=cuentaNumeroCuenta%>" 		styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text></td>
							</tr>
						</table>
					<!-- TABLA -->
					</siga:ConjCampos>
				</td>
			</tr>
		</html:form>
		</table>	
	
		<script>rellenarCampos()</script>	
	
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tama�o de la ventana (M,P,G)
	-->		
		<siga:ConjBotonesAccion botones='<%=botones%>' modo='<%=modo%>' modal="M"/>
	<!-- FIN: BOTONES REGISTRO -->	

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>

<script>
	function cargarBancos() {
		var idBanco = cuentasBancariasForm.cbo_Codigo.value;		
		if (idBanco!=undefined&&idBanco!="") {
			jQuery.ajax({ //Comunicaci�n jQuery hacia JSP  
   				type: "POST",
				url: "/SIGA/CEN_CuentasBancarias.do?modo=getAjaxBanco",
				data: "idBanco="+idBanco,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){		
					cuentasBancariasForm.banco.value=json.banco.nombre;
					fin();
				},
				error: function(e){
					alert('Error de comunicaci�n: ' + e);
					fin();
				}
			});
		} else {
			cuentasBancariasForm.banco.value="";
		}
	}
</script>
