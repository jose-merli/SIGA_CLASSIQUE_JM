<!DOCTYPE html>
<html>
<head>
<!-- informacionCuentaBancaria.jsp -->

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
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>

<!-- IMPORTS -->
<%@ page import = "java.util.Hashtable"%>
<%@ page import = "com.atos.utils.UsrBean"%>
<%@ page import = "com.atos.utils.GstDate"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.siga.Utilidades.UtilidadesHash"%>
<%@ page import = "com.siga.beans.CenCuentasBancariasBean"%>

<!-- JSP -->
<% 
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
	boolean bOcultarHistorico = usr.getOcultarHistorico();
	

	// Varibles que dependen del modo de la pagina (consulta, edicion, nuevo)
	boolean editarCampos = false;
	boolean desactivado  = false;
	String clase   = "box";
	String claseCombo = "boxCombo";
	boolean desactivadoEdicion = false;
	String botones = "";

	String DB_TRUE  = ClsConstants.DB_TRUE;
	String DB_FALSE = ClsConstants.DB_FALSE;

	String idUsr = (String)usr.getUserName();
	String nombrePersona = String.valueOf(request.getParameter("nombrePersona"));
	String numero = String.valueOf(request.getParameter("numero"));
	String idPersona = String.valueOf(request.getParameter("idPersona"));
	String idInstitucion = String.valueOf(request.getParameter("idInstitucion"));
	String modo = String.valueOf(request.getParameter("modoConsulta"));
	String sociedad = String.valueOf(request.getParameter("sociedad"));

	String titular="", cuentaContable="", iban="", cbo_Codigo="", cuentaCodigoSucursal="", cuentaDigitoControl="", cuentaNumeroCuenta="", idCuenta="", fechaBaja="";
	boolean visibilidad  = false;	
	String claseEdicion = "box";
	Hashtable<String, String> htData = null;
		
	// JPT: Compruebo si existe la variable de sesion que sirve para guardar y anadir a historico, o bien obtengo el dato de los parametros
	Integer iIdCuenta = (Integer) request.getSession().getAttribute("idCuentaHistorico");	
	
	/*
	 * JPT: Solo obtengo datos cuando hay cuenta:
	 * 1. Modo consulta
	 * 2. Modo edicion
	 * 3. Cuando acaba de crear una nueva cuenta
	 */	
	if (modo.equals("nuevo") && iIdCuenta != null) {
		modo = "editar";
	} 
	 
	if (modo.equals("editar") || modo.equals("ver")) {
		htData = (Hashtable<String, String>) request.getAttribute("hashCuentasBancarias");
		if (htData != null) {
			titular = String.valueOf(htData.get(CenCuentasBancariasBean.C_TITULAR));
			cuentaContable = String.valueOf(htData.get(CenCuentasBancariasBean.C_CUENTACONTABLE));
			iban = String.valueOf(htData.get(CenCuentasBancariasBean.C_IBAN));
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
			
			if(cuentaNumeroCuenta != null && !cuentaNumeroCuenta.equals("")){
				visibilidad  = true;
			}else{
				visibilidad = false;
			}
		}
	}		
	
	if (modo.equals("editar")) {
		desactivadoEdicion = true;
		desactivado  = false;
		editarCampos = true;			
		// botones = "G,R,GAH"; Esto pone guardar y a�adir historico, cuando ya no tiene sentido, porque deberia crear nuevos mandatos, que hay que volver a firmar
		botones = "G,R";
		claseEdicion = "boxConsulta";
		iban =  UtilidadesString.mostrarDatoMascara(iban,ClsConstants.MASK_IBAN);
		
	}else if (modo.equals("ver")) {
		desactivado = true;
		desactivadoEdicion = true;
		editarCampos = false;
		claseEdicion = "boxConsulta";
		clase = "boxConsulta";
		claseCombo = clase;
		if(cuentaDigitoControl != null && !cuentaDigitoControl.equals(""))
			cuentaDigitoControl  = "**";
		cuentaNumeroCuenta = UtilidadesString.mostrarNumeroCuentaConAsteriscos(cuentaNumeroCuenta);
		iban = UtilidadesString.mostrarIBANConAsteriscos(iban);
	
	}else if (modo.equals("nuevo")) {
		editarCampos = true;	
		desactivado = false;
		desactivadoEdicion = false;
		titular = nombrePersona;
		visibilidad = false;
		botones = "G,R";
	}
	
	// Gestion de Volver
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	if (busquedaVolver == null || usr.isLetrado()) {
		busquedaVolver = "volverNo";
	} else {
		botones += ",V";
	}
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>		
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="cuentasBancariasForm" staticJavascript="false" />
	
	<siga:TituloExt titulo="pestana.fichaCliente.datosBancarios.infoCuenta" localizacion="censo.fichaCliente.bancos.infoCuenta.localizacion" />  
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script>			
		function refrescarLocal() {
			document.cuentasBancariasForm.target = "_self";
			document.cuentasBancariasForm.modo.value="informacionCuentaBancaria";		
			document.cuentasBancariasForm.submit();
		}			
	
		// Asociada al boton Restablecer
		function accionRestablecer(){		
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				refrescarLocal();
			}						
		}			

		// Asociada al boton Guardar y a�adir al hist�rico
		function accionGuardarAnyadirHistorico() {
			var modo = "guardarInsertarHistorico";
			guardar(modo);
		}
		
		// Asociada al boton Guardar
		function accionGuardar() {		
			var  modo = "";
			<%if (modo.equals("editar")) {%>
				modo = "modificar";
			<%}	else { %>
				modo = "insertar";						
			<%}%>
			guardar(modo);
		}
		
		function guardar(modo){
			// Validamos los errores ///////////
			sub();
			if (!document.all.cuentasBancariasForm.cuentaAbono.checked && !document.all.cuentasBancariasForm.cuentaCargo.checked && !document.all.cuentasBancariasForm.abonoSJCS.checked) {
				var mensaje = "<siga:Idioma key='errors.required' arg0='censo.tipoCuenta.abono'/>";
				mensaje += " o <siga:Idioma key='censo.tipoCuenta.cargo'/>";
				mensaje += " o <siga:Idioma key='censo.datosCuentaBancaria.literal.abonoSJCS'/>" + '\n';
				alert(mensaje);
				fin();
			    return false;
			}
			
			//Se quita la mascara al guardar 
			document.cuentasBancariasForm.IBAN.value = ready2ApplyMask(document.getElementById("IBAN").value);			
			
			iban = document.cuentasBancariasForm.IBAN.value;
			bic = document.cuentasBancariasForm.BIC.value;
			banco = document.cuentasBancariasForm.banco.value;
			
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
			}		
           
			if (!validateCuentasBancariasForm(document.cuentasBancariasForm)){
				fin();
				return false;
			}
			
			////////////////////////////////////

			var datos;
			<% if (!bOcultarHistorico) { %>
				datos = showModalDialog("/SIGA/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
				window.top.focus();
			<% } else { %>
				datos = new Array();
				datos[0] = 1;
				datos[1] = "";
			<% } %>

			if (datos == null){
				fin();
				return false;
			}
						
			if (datos[0] == 1) { // Boton Guardar
				
				var abonoCargo = "<%=UtilidadesHash.getString(htData, CenCuentasBancariasBean.C_ABONOCARGO)%>";				
				var bCuentaCargoPrevia = (abonoCargo=="<%=ClsConstants.TIPO_CUENTA_CARGO%>" || abonoCargo=="<%=ClsConstants.TIPO_CUENTA_ABONO_CARGO%>");				
				var bCuentaCargoActual = jQuery("#cuentaCargo").is(':checked');
				
				// Compruebo que ahora este marcado la casilla de Cargo y antes no estuviera (da igual si es creacion o modificacion)
				if (bCuentaCargoActual && !bCuentaCargoPrevia) {
					
					var mensajeConfirmacionProcesoAltaCuentaCargos = '<%=UtilidadesString.getMensajeIdioma(usr, "censo.tipoCuenta.cargo.confirmacionProcesoAltaCuentaCargos")%>';					
					if (confirm(mensajeConfirmacionProcesoAltaCuentaCargos)) {
						jQuery("#confirmacionProcesoAltaCuentaCargos").val("true");
					}
				}
				
				document.cuentasBancariasForm.motivo.value = datos[1];
				document.cuentasBancariasForm.modo.value = modo;				
				document.cuentasBancariasForm.target = "submitArea";
				document.cuentasBancariasForm.submit();
			}else{
				fin();
				return false;
			}
		}
		
		// Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable
		function rellenarCampos(){
			<%if (htData != null) {%>
				// Obtenemos el valor para los check Tipo de Cuenta.
				var abonoCargo = "";
				abonoCargo="<%=String.valueOf(htData.get(CenCuentasBancariasBean.C_ABONOCARGO))%>";				
				if (abonoCargo=="<%=ClsConstants.TIPO_CUENTA_ABONO%>") {	
					document.all.cuentasBancariasForm.cuentaAbono.checked=true;
					document.all.cuentasBancariasForm.cuentaCargo.checked=false;
					
				} else if (abonoCargo=="<%=ClsConstants.TIPO_CUENTA_CARGO%>") {	
					document.all.cuentasBancariasForm.cuentaAbono.checked=false;
					document.all.cuentasBancariasForm.cuentaCargo.checked=true;
					
				} else if (abonoCargo=="<%=ClsConstants.TIPO_CUENTA_ABONO_CARGO%>") {	
					document.all.cuentasBancariasForm.cuentaAbono.checked=true;
					document.all.cuentasBancariasForm.cuentaCargo.checked=true;
				
				} else {	
					document.all.cuentasBancariasForm.cuentaAbono.checked=false;
					document.all.cuentasBancariasForm.cuentaCargo.checked=false;
				}
			
				// Obtenemos los valores para el check sociedad.
				sociedad="<%=sociedad%>";		
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
		
		var mensajeGeneralError='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usr, "messages.general.error"))%>';

		function cargarBancoPorIBAN(){			
			<%if (modo.equals("nuevo")) {%>				
				mensaje = "<siga:Idioma key="messages.censo.cuentasBancarias.errorCuentaBancaria"/>";	
				var iban = ready2ApplyMask(document.getElementById("IBAN").value);		
				if (iban!=undefined && iban!="") {			
					jQuery.ajax({ //Comunicacion jQuery hacia JSP  
		   				type: "POST",
						url: "/SIGA/CEN_CuentasBancarias.do?modo=getAjaxBancoBIC",
						data: "iban="+iban,
						dataType: "json",
						contentType: "application/x-www-form-urlencoded;charset=UTF-8",
						success: function(json){
							
							if(json.error!=null && json.error!=""){
								document.getElementById("BIC").value="";
								document.getElementById("banco").value="";
								document.getElementById("BIC").readOnly = true;
								document.getElementById("BIC").className = "boxConsulta";
								alert(json.error,'error');
							}else if(json!=null && json.pais != null){
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
			
		<% } %>
		}	
		
		function inicioCargarBancoBIC(){
			var iban = ready2ApplyMask(document.getElementById("IBAN").value);
			var codigoBanco ="<%=cbo_Codigo%>";
			if (iban!=undefined && iban!="") {			
				jQuery.ajax({ //Comunicacion jQuery hacia JSP  
	   				type: "POST",
					url: "/SIGA/CEN_CuentasBancarias.do?modo=getAjaxCargaInicialBancoBIC",
					data: "iban="+iban+"&codigo="+codigoBanco,
					dataType: "json",
					contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					success: function(json){	
						if(json.error!=null && json.error!=""){
							alert(json.error,'error');
						}else	if(json.banco!=null && json.banco!=""){
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
</script>	
</head>

<body onLoad="inicioCargarBancoBIC();">
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" height="32px">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="censo.consultaDatosBancarios.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%> &nbsp;&nbsp;
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
			<html:hidden property="confirmacionProcesoAltaCuentaCargos" styleId="confirmacionProcesoAltaCuentaCargos" value="false"/>
	  
			<%if (editarCampos) {%>
				<html:hidden property="idCuenta" value="<%=idCuenta%>"/> 
				<html:hidden property="idPersona" value="<%=idPersona%>"/> 
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/> 
				<html:hidden property="cbo_Codigo" value="<%=cbo_Codigo%>"/> 
				<html:hidden property="motivo" value=""/>
				<html:hidden property="nombrePersona" value="<%=nombrePersona%>"/>
				<html:hidden property="numero" value="<%=numero%>"/>
				<html:hidden property="sociedad" value="<%=sociedad%>"/>
				<html:hidden property="modoConsulta" value="<%=modo%>"/>  				
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
									<td class="labelText">							
										<html:text size="50" maxlength="100" name="cuentasBancariasForm" property="titular" value="<%=titular%>" styleClass="<%=claseEdicion%>" readonly="<%=desactivadoEdicion%>" />
									</td>
									<td class="labelText" COLSPAN="2">
										<siga:Idioma key="censo.datosCuentaBancaria.literal.sociedad"/>
										<html:checkbox name="cuentasBancariasForm" property="sociedad" disabled="true"></html:checkbox>
									</td>
								<tr>
								
								<!-- FILA -->
								<tr>						
									<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.codigoIBAN"/>&nbsp;(*)</td>
									<td class="labelText"><html:text size="50"  maxlength="45" name="cuentasBancariasForm" styleId="IBAN" property="IBAN" value="<%=iban%>"  styleClass="<%=claseEdicion%>" readonly="<%=desactivadoEdicion%>" onblur="cargarBancoPorIBAN();"></html:text></td>
	
									<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.codigoBIC"/>&nbsp;</td>
									<td class="labelText"><html:text size="14"  maxlength="11" name="cuentasBancariasForm" styleId="BIC" property="BIC" styleClass="boxConsulta" readonly="true" onblur="rpad();"></html:text></td>
								</tr>						
							
								<!-- FILA -->
								<tr>
									<td class="labelText" nowrap><siga:Idioma key="censo.datosCuentaBancaria.literal.banco"/></td>
									<td class="labelText" COLSPAN="3">
										<input type="text" id="banco" style="width:500px;" class="boxConsulta" readonly></input>
									</td>
								</tr>							
								
								<!-- FILA -->
								<tr><td colspan="4">	
									<table>
										<tr>					
											<td class="labelText">
												<siga:Idioma key="censo.tipoCuenta.abono"/><html:checkbox name="cuentasBancariasForm" property="cuentaAbono" disabled="<%=desactivado%>"/>
											</td>
											<td class="labelText">
												<siga:Idioma key="censo.tipoCuenta.cargo"/><html:checkbox name="cuentasBancariasForm" property="cuentaCargo" styleId="cuentaCargo" disabled="<%=desactivado%>"/>
											</td>												
											<td class="labelText">
												<siga:Idioma key="censo.datosCuentaBancaria.literal.abonoSJCS"/><html:checkbox name="cuentasBancariasForm" property="abonoSJCS" disabled="<%=desactivado%>"/>
											</td>
										</tr>
									</table>						
								</td></tr>							
		
							<%	if(idPersona.equals(idUsr)){ %>
									<tr></tr>
							<% }else{ %>
									<tr>
										<td class="labelText"><siga:Idioma key="censo.datosCuentaBancaria.literal.cuentaContable"/></td>
										<td class="labelText"><html:text name="cuentasBancariasForm" property="cuentaContable" value="<%=cuentaContable%>" size="20" maxlength="20" styleClass="<%=clase%>" disabled="<%=desactivado%>"></html:text></td>
									</tr>
							<% } %>  					
								
							<%	if(visibilidad){ %>
								<tr><td COLSPAN="4">
									<siga:ConjCampos leyenda="datosCuentaBancaria.literal.formatoAntiguo">
										<table>
			
											<!-- FILA -->
											<tr>						
												<td class="labelText" nowrap colspan="2">C.C.C.&nbsp;(*)</td>
												<td class="labelText"><html:text size="4"  maxlength="4" name="cuentasBancariasForm" property="cbo_Codigo"     value="<%=cbo_Codigo%>" 	styleClass="boxConsulta" readonly="true"></html:text>-
												<html:text size="4"  maxlength="4" name="cuentasBancariasForm" property="codigoSucursal" value="<%=cuentaCodigoSucursal%>" 	styleClass="boxConsulta" readonly="true" ></html:text>-
												<html:text size="2"  maxlength="2" name="cuentasBancariasForm" property="digitoControl"  value="<%=cuentaDigitoControl%>" 	styleClass="boxConsulta" readonly="true" ></html:text>-
												<html:text size="10" maxlength="10" name="cuentasBancariasForm" property="numeroCuenta"  value="<%=cuentaNumeroCuenta%>" 	styleClass="boxConsulta" readonly="true" ></html:text>
												</td>
											</tr>
										</table>
									</siga:ConjCampos>
								</td></tr>
							<% } %> 	
							</table>
						<!-- TABLA -->
						</siga:ConjCampos>
					</td>
				</tr>			
			</table>
		</html:form>	
		
		<script>rellenarCampos();</script>	
		
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
		<siga:ConjBotonesAccion botones='<%=botones%>'/>
		<!-- FIN: BOTONES REGISTRO -->	
	
		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	</div>
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
	
	<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
			
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>	
</body>
</html>