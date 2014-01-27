<!DOCTYPE html>
<html>
<head>
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
<%@ page import = "java.util.Properties"%>
<%@ page import = "java.util.Hashtable"%>
<%@ page import = "java.util.ArrayList"%>

<!-- JSP -->
<% 
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
	boolean bOcultarHistorico = usr.getOcultarHistorico();
	

	// Varibles que dependen del modo de la pagina (consulta, edicion, nuevo)
	boolean editarCampos = false;
	boolean desactivado  = false;
	String clase   = "box";
	String claseCombo = "boxCombo";
	boolean desactivadoEdicion = false;
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
	String iban = "";
	String cbo_Codigo = "";
	String cuentaCodigoSucursal = "";
	String cuentaDigitoControl = "";
	String cuentaNumeroCuenta = "";
	String fechaBaja = "";
	boolean visibilidad  = false;

	String modo=(String)request.getAttribute("modoConsulta");
	String claseEdicion = "box";
	if (modo.equals("ver") || modo.equals("editar")) {
		htData = (Hashtable)request.getSession().getAttribute("DATABACKUP");
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
		botones += ",GAH";
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
		titular = nombreUsu;
		visibilidad = false;
	}
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="cuentasBancariasForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	//Funciones para obtener el d¡gito de control de la Cuenta
	
		// Asociada al boton Volver
		function accionCerrar(){ 		
			window.parent.close();
		}	
		
		// Asociada al boton Restablecer
		function accionRestablecer(){		
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.all.cuentasBancariasForm.reset();
				document.getElementById("BIC").readOnly = true;
				document.getElementById("BIC").className = "boxConsulta";
				rellenarCampos();
				inicioCargarBancoBIC();
			}						
		}			

		// Asociada al boton Guardar y añadir al histórico
		function accionGuardarAnyadirHistorico() {
			var modo = "guardarInsertarHistorico";
			guardar(modo);
		}
		
		// Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {		
			<%if (modo.equals("editar")) {%>
			var modo = "modificar";
			<%}	else { %>
			var modo = "insertar";
			<%}%>
			guardar(modo);
		}
		
		function guardar(modo){
			// Validamos los errores ///////////
			sub();
			if ((!document.all.cuentasBancariasForm.cuentaAbono.checked) && 
			    (!document.all.cuentasBancariasForm.cuentaCargo.checked)) {
				var mensaje = "<siga:Idioma key="censo.datosCuentaBancaria.literal.tipoCuenta"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert(mensaje);
				fin();
			    return false;
			}
			
			//Se quita la mascara al guardar 
			document.cuentasBancariasForm.IBAN.value = formateaMask(document.getElementById("IBAN").value);			
			
			iban = document.cuentasBancariasForm.IBAN.value;
			bic = document.cuentasBancariasForm.BIC.value;
			banco = document.cuentasBancariasForm.banco.value;
			
			if(!validarCuentaBancaria(iban,bic,banco)){
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
		
		var mensajeGeneralError='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma(usr, "messages.general.error"))%>';

		function cargarBancoPorIBAN(){			
			<%if (modo.equals("nuevo")) {%>				
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
			
		<% } %>
		}	
		
		function inicioCargarBancoBIC(){
			var iban = formateaMask(document.getElementById("IBAN").value);
			var codigoBanco ="<%=cbo_Codigo%>";
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
	}	</script>	
</head>

<body onLoad="inicioCargarBancoBIC();"">
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
			<html:hidden property="cbo_Codigo" 		value="<%=cbo_Codigo%>"/> 
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
											<siga:Idioma key="censo.tipoCuenta.abono"/><html:checkbox name="cuentasBancariasForm" property="cuentaAbono" disabled="<%=desactivado%>" onChange="validaAbonoSJCS()"/>
										</td>
										<td class="labelText">
											<siga:Idioma key="censo.tipoCuenta.cargo"/><html:checkbox name="cuentasBancariasForm" property="cuentaCargo" disabled="<%=desactivado%>"/>
										</td>												
										<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.abonoSJCS"/><html:checkbox name="cuentasBancariasForm" property="abonoSJCS" disabled="<%=desactivado%>" onChange="validaAbonoSJCS()" />
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
								<siga:ConjCampos leyenda="Cuenta Antigua">
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
		 La propiedad modal dice el tamaño de la ventana (M,P,G)
	-->		
		<siga:ConjBotonesAccion botones='<%=botones%>' modo='<%=modo%>' modal="M"/>
	<!-- FIN: BOTONES REGISTRO -->	

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
