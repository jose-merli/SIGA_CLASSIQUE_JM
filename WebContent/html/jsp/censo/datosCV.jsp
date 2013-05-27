<!-- datosCV.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL MEDIANA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- AJAX -->
<%@ taglib uri="ajaxtags.tld" prefix="ajax"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.CenDatosCVBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean usr = (UsrBean) request.getSession()
			.getAttribute("USRBEAN");
	String idUsr = (String) usr.getUserName();
	boolean bOcultarHistorico = usr.getOcultarHistorico();

	// Varibles que dependen del modo de la pagina (consulta, edicion, nuevo)
	boolean editarCampos = false;
	boolean desactivado = false;
	String clase = "box";
	String botones = "C,Y,R";

	String DB_TRUE = ClsConstants.DB_TRUE;
	String DB_FALSE = ClsConstants.DB_FALSE;

	String nombreUsu = (String) request.getAttribute("nombrePersona");
	String idPersona = String.valueOf((Long) request
			.getAttribute("idPersona"));
	String numero = (String) request.getAttribute("numero");
	if (numero == null)
		numero = "";
	Hashtable htData = null;
	String fechaInicio = "";
	String fechaFin = "";
	String fechaCertificado = "";
	String creditos = "";
	String tipoApunte = "";
	String descTipo1 = "";
	String descTipo2 = "";
	String descripcion = "";
	String certificado = "";
	String idCV = "";
	String idInstitucion = "";
	String fechaBaja = "";
	ArrayList idTipoCV = new ArrayList();
	ArrayList idSubtipo1 = new ArrayList();
	ArrayList idSubtipo2 = new ArrayList();

	String modo = (String) request.getAttribute("modoConsulta");
	if (modo.equals("ver") || modo.equals("editar")) {
		htData = (Hashtable) request.getSession().getAttribute("DATABACKUP");
		if (htData != null) {
			fechaInicio = GstDate.getFormatedDateShort("",
					(String) htData.get(CenDatosCVBean.C_FECHAINICIO));
			fechaFin = GstDate.getFormatedDateShort("",
					(String) htData.get(CenDatosCVBean.C_FECHAFIN));
			fechaCertificado = GstDate.getFormatedDateShort("",
					(String) htData.get(CenDatosCVBean.C_FECHAMOVIMIENTO));
			creditos = String.valueOf(htData.get(CenDatosCVBean.C_CREDITOS));
			descripcion = String.valueOf(htData.get(CenDatosCVBean.C_DESCRIPCION));
			certificado = String.valueOf(htData.get(CenDatosCVBean.C_CERTIFICADO));
			tipoApunte = String.valueOf(htData.get("TIPOAPUNTE"));
			descTipo1 = String.valueOf(htData.get("DESCSUBTIPO1"));
			descTipo2 = String.valueOf(htData.get("DESCSUBTIPO2"));

			fechaBaja = String.valueOf(htData.get(CenDatosCVBean.C_FECHABAJA));
			if ((fechaBaja != null) && !fechaBaja.equals(""))
				fechaBaja = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort("", fechaBaja));
			else
				fechaBaja = "";

			if (modo.equals("editar")) {
				editarCampos = true;
				desactivado = false;
				idCV = String
						.valueOf(htData.get(CenDatosCVBean.C_IDCV));
				idInstitucion = String.valueOf(htData.get(CenDatosCVBean.C_IDINSTITUCION));
				idTipoCV.add(String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCV)));

				idSubtipo1.add(String.valueOf(htData
								.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO1)
								+ "@"
								+ htData.get(CenDatosCVBean.C_IDINSTITUCION_SUBT1)));
				idSubtipo2.add(String.valueOf(htData
								.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO2)
								+ "@"
								+ htData.get(CenDatosCVBean.C_IDINSTITUCION_SUBT2)));
			} else {
				desactivado = true;
				clase = "boxConsulta";
			}
		}
	} else {
		if (modo.equals("nuevo")) {
			editarCampos = true;
			desactivado = false;
			idInstitucion = String.valueOf((Integer) request
					.getAttribute("idInstitucion"));
		}
	}
	ArrayList modoSel = new ArrayList();
	String parametro[] = new String[1];
	parametro[0] = (String) usr.getLocation();
	String nombreInstitucionAcceso = "";
	boolean junta = false;
	String parametroJunta[] = new String[1];
	String institucionAcceso = "";
	String esJunta = (String) request.getAttribute("esJunta");
	String mantenimiento = (String) request.getAttribute("mantenimiento");
	boolean esMantenimiento = false;
	if (mantenimiento != null && mantenimiento == "S")
		esMantenimiento = true;
	if (esJunta != null && esJunta == "S") {
		junta = true;
		if (request.getAttribute("idInstitucionCargo") != null
				&& request.getAttribute("idInstitucionCargo")
						.toString() != "") {
			try {
				institucionAcceso = (String) request
						.getAttribute("idInstitucionCargo");
			} catch (Exception e) {
				institucionAcceso = String.valueOf((Integer) request
						.getAttribute("idInstitucionCargo"));
			}
			CenInstitucionAdm institucionAdm = new CenInstitucionAdm(
					usr);
			nombreInstitucionAcceso = institucionAdm
					.getNombreInstitucion(institucionAcceso);
		}
		modoSel.add(institucionAcceso);
		parametroJunta[0] = institucionAcceso;
	}
%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.ArrayList"%>
<html>

<!-- HEAD -->
<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		
	<!--Step 2 -->
	<script type="text/javascript"
		src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript"
		src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript"
		src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript"
		src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

	<!--Step 3 -->
	<!-- defaults for Autocomplete and displaytag -->
	<link type="text/css" rel="stylesheet"
		href="<html:rewrite page='/html/css/ajaxtags.css'/>" />
<!-- 	<link type="text/css" rel="stylesheet"
 		href="<html:rewrite page='/html/css/displaytag.css'/>" /> -->
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="datosCVForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js"
		type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp"
		type="text/javascript"></script>

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
	         var v_subTipo1;
			 var v_subTipo2;
			
			//Asociada al boton Volver 
			function accionCerrar() { 		
				window.top.close();
			}	
			
			// Asociada al boton Restablecer 
			function accionRestablecer() {		
				if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
					document.getElementById("datosCVForm").reset();
					rellenarCampos();
				}
			}			
	
			// Asociada al boton GuardarCerrar
			function accionGuardarCerrar() {
	      		sub();
				// Validamos los errores ///////////
				if (!validateDatosCVForm(document.datosCVForm)){
					fin();
					return false;
				}
				
				if ((!v_subTipo1.disabled) && (document.datosCVForm.tipoApunte.value=="" || document.datosCVForm.idTipoCVSubtipo1.value=="")){	
					aux = '<siga:Idioma key="censo.datosCV.literal.tipoApunte"/>'
					alert(aux);
					fin();
					return false;
			    } else { 
					if((!v_subTipo1.disabled) && (document.datosCVForm.tipoApunte.value=="" || document.datosCVForm.idTipoCVSubtipo1.value=="")){
		      			aux = '<siga:Idioma key="censo.datosCV.literal.tipoApunte"/>'
		   		     	alert(aux);
		   		     	fin();
						return false;
					} else {
						if ((v_subTipo1.disabled) && (document.datosCVForm.tipoApunte.value=="")){
							aux = '<siga:Idioma key="censo.datosCV.literal.tipoApunte"/>'
				     		alert(aux);
				     		fin();
						    return false;
					    } else {
					    	if (document.datosCVForm.tipoApunte.value==""){
					            aux = '<siga:Idioma key="censo.datosCV.literal.tipoApunte1"/>'
					          	alert(aux);
					          	fin();
						        return false;
						    }
					    }
					}
				}		   
			   
				if (compararFecha (document.datosCVForm.fechaInicio, document.datosCVForm.fechaFin) == 1) {
					aux = '<siga:Idioma key="messages.fechas.rangoFechas"/>';
					alert(aux);
					fin();
					return false;
				}
				////////////////////////////////////
	
				<%if (!bOcultarHistorico) {%>
					var datos = showModalDialog("/SIGA/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
					window.top.focus();
				<%} else {%>
					var datos = new Array();
					datos[0] = 1;
					datos[1] = "";
				<%}%>
	
				if (datos == null) {
					fin();
					return false;
				}
				
				if (datos[0] == 1) { // Boton Guardar
					document.datosCVForm.motivo.value = datos[1];
					<%if (modo.equals("editar")) {%>
						document.datosCVForm.modo.value = "modificar";
					<%} else {%>
						document.datosCVForm.modo.value = "insertar";
					<%}%>
					document.datosCVForm.target = "submitArea";
					document.datosCVForm.submit();
				} else {
					fin();
					return false;
				}
			}		
			
			// Selecciona los valores de los campos check y combo dependiendo de los valores del Hashtable 
			function rellenarCampos(){
				// Obtenemos los valores para el check certificado.
				certificado="<%=certificado%>"
				if(certificado=="<%=DB_TRUE%>"){
					document.getElementById("certificado").checked = true;	
				} 		
			}
			
			function deshabilitarCombos1(o){
				v_subTipo1=o; 
			  	if (o.options.length > 1) {
			  		o.disabled = false;
			  	} else {
			  		o.disabled = true;
			  	}		 
			}
			
			function deshabilitarCombos2(o){
				v_subTipo2=o;
				if (o.options.length > 1) {
					o.disabled = false;
				} else {
					o.disabled = true;
				}		 
			}
			
			var tipoCurriculum;
			function init(){
				 <%if (!modo.equals("ver")) {%>
				  var cmb1 = document.getElementsByName("tipoApunte");
				  var cmb2 = cmb1[0]; 			 
				  tipoCurriculum=<%=idTipoCV%>;			
				  cmb2.onchange();
				 <%}%>			  
			}
	
			function recargarCombos(tipo) {
	
			<%if (!modo.equals("ver")) {%>
			if (tipo) {
				if (tipoCurriculum != tipo.value) {
					limpiarCombo("idTipoCVSubtipo1");
					limpiarCombo("idTipoCVSubtipo2");
				}
			}
			<%}%>
		}
	
		function limpiarCombo(nombre) {
			iframeCombo = window.top.frames[0].document.getElementById(nombre + "Frame");
			cadenaInicial = iframeCombo.src;
	
			if (cadenaInicial.indexOf("&elementoSel=[0]") > 1) {
				return;
			}
	
			var ini = cadenaInicial.indexOf('&elementoSel=');
			if (ini < 1)
				return;
	
			cadenaFinal = cadenaInicial.substring(0, ini) + "&elementoSel=[0]";
	
			var fin = cadenaInicial.indexOf('&', ini + 2);
			if (fin > 1) {
				cadenaFinal = cadenaFinal + cadenaInicial.substring(fin);
			}
	
			iframeCombo.src = cadenaFinal;
		}
	
		function rellenaNumCol(){	
			//var cmb1 = document.getElementsByName("idInstitucionCargo");
			//cmb1.onchange();
		}
	
		function  preAccionColegiado(){
		}
		
		function  postAccionColegiado(){
		}		
	</script>
</head>

<body onload="init();">
	<!-- TITULO -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq"><siga:Idioma
					key="censo.consultaDatosCV.literal.titulo1" /> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombreUsu)%>
				&nbsp;&nbsp; 
			 <%
			 	if (!junta) {
			 %> <%
			 	if ((numero != null) && (!numero.equalsIgnoreCase(""))) {
			 %> <siga:Idioma key="censo.fichaCliente.literal.colegiado" />&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
				<%
					} else {
				%> <siga:Idioma key="censo.fichaCliente.literal.NoColegiado" /> <%
					}
					}
				%></td>
		</tr>
	</table>

	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

	<div id="camposRegistro" class="posicionModalMedia" align="center">

		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->

		<html:form action="/CEN_DatosCV.do" method="POST" target="submitArea" styleId="datosCVForm">
			<html:hidden property="modo" value="cerrar" />

			<%
				if (editarCampos) {
			%>
			<html:hidden property="idPersona" styleId="" value="<%=idPersona%>" />
			<html:hidden property="idCV" styleId="idCV" value="<%=idCV%>" />
			<html:hidden property="idInstitucion" styleId="idInstitucion"
				value="<%=idInstitucion%>" />
			<html:hidden property="motivo" styleId="motivo" value="" />
			<%
				}
			%>

			<table class="tablaCentralCamposMedia" align="center">
				<tr>
					<td>
						<siga:ConjCampos leyenda="censo.consultaDatosCV.cabecera">
							<table class="tablaCampos" align="center" border="0">
								<%
									if (junta) {
								%>
								<tr>

									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientes.literal.colegio" /></td>
									<td width="20px">
										<%
											if (editarCampos) {
										%> <!-- MAV 14/7/05 Mostrar combo solo para aquellos colegios que permitan busquedas en más de una institucion -->
										<siga:ComboBD nombre="idInstitucionCargo"
											id="idInstitucionCargo" parametro="<%=parametro %>"
											elementoSel="<%=modoSel %>" tipo="cmbNombreColegiosTodos"
											obligatorio="true" readonly="true" clase="boxCombo" /> 
										<%
										 	} else {
										 %> 
 										<html:text property="" styleClass="boxConsulta" size="50"
											value='<%=nombreInstitucionAcceso%>' readOnly="true"></html:text>
										<%
											}
										%>
									</td>
									<td class="labelText">
										<siga:Idioma key="censo.fichaCliente.literal.colegiado" />
										&nbsp;&nbsp;&nbsp; <%=numero%>
									</td>
									<td>&nbsp;</td>
								<tr>
									<%
										}
									%>

									<%
										if (!fechaBaja.equals("")) {
									%>
								
								<tr>
									<td colspan="3">&nbsp;</td>
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatos.literal.fechaBaja" />&nbsp;&nbsp;&nbsp;<%=fechaBaja%>
									</td>
								</tr>
								<%
									}
								%>

								<tr>
									<td class="labelText"><siga:Idioma
											key="censo.datosCV.literal.tipo" />&nbsp(*)</td>
									<td>
										<%
											if (editarCampos) {
												if (esMantenimiento) {
										%> 
 										<siga:ComboBD nombre="tipoApunte" tipo="curriculum" clase="boxCombo"
											obligatorio="true" elementoSel="<%=idTipoCV%>"
											readonly="true"
											accion="Hijo:idTipoCVSubtipo1,Hijo:idTipoCVSubtipo2;recargarCombos(this);" />
										<%
												} else {
										%> 
										<siga:ComboBD nombre="tipoApunte" tipo="curriculum"
											clase="boxCombo" obligatorio="true"
											elementoSel="<%=idTipoCV%>"
											accion="Hijo:idTipoCVSubtipo1,Hijo:idTipoCVSubtipo2;recargarCombos(this);" />
										<%
												}
											} else {
												if (esMantenimiento) {
										%> 
										<html:text name="datosCVForm" property="tipoApunte"
											styleId="tipoApunte" value='<%=tipoApunte%>'
											styleClass="<%=clase%>" readOnly="true"></html:text> 
										<%
											 	} else {
											 %> 
										<html:text name="datosCVForm" property="tipoApunte"
											styleId="tipoApunte" value='<%=tipoApunte%>'
											styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text>
										<%
 												}
											}
 										%>
									</td>

									<td>
										<%
											if (editarCampos) {
										%> 
										<siga:ComboBD nombre="idTipoCVSubtipo1" tipo="cmbComision1"
											parametro="<%=parametro%>" clase="boxCombo"
											obligatorio="true" elementoSel="<%=idSubtipo1%>" hijo="t"
											accion="parent.deshabilitarCombos1(this);" /> 
										<%
										 	} else {
										%> 
										<html:text name="datosCVForm" property="idTipoCVSubtipo1"
											styleId="idTipoCVSubtipo1" value='<%=descTipo1%>'
											styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text>
										<%
											}
										%>
									</td>
									<td>
										<%
											if (editarCampos) {
										%>
										<siga:ComboBD nombre="idTipoCVSubtipo2" tipo="cmbCargos1"
											parametro="<%=parametro%>" clase="boxCombo"
											obligatorio="true" elementoSel="<%=idSubtipo2%>" hijo="t"
											accion="parent.deshabilitarCombos2(this);" /> 
										<%
										} else {
										%>
										<html:text name="datosCVForm" property="idTipoCVSubtipo2"
											styleId="idTipoCVSubtipo2" value='<%=descTipo2%>'
											styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text>
										<%
										}
										%>
									</td>
								</tr>

<tr>
								<td class="labelText" ><siga:Idioma key="censo.datosCV.literal.fechaInicio"/>&nbsp</td>
								<td>
									<%if (editarCampos) {%>
									<siga:Fecha  nombreCampo= "fechaInicio" valorInicial="<%=fechaInicio%>" posicionX="200" posicionY="10"/>
									<%}else{%>	
									<siga:Fecha  nombreCampo= "fechaInicio" valorInicial="<%=fechaInicio%>" disabled="true"/>
									<%}%>
								</td>
								
								<td class="labelText" ><siga:Idioma key="censo.datosCV.literal.fechaFin"/></td>	
								<td  class="labelText">
										<%if (editarCampos) {%>
										<siga:Fecha  nombreCampo= "fechaFin" valorInicial="<%=fechaFin%>"  posicionX="200" posicionY="10"/>
										<%}else{%>
										<siga:Fecha  nombreCampo= "fechaFin" valorInicial="<%=fechaFin%>" disabled="true"/>
										<%}%>
								</td>	
							</tr>

							<tr>
								<td class="labelText"><siga:Idioma key="censo.consultaDatosCV.literal.verificado"/></td>
								<td><html:checkbox name="datosCVForm" property="certificado" disabled="<%=desactivado%>"/></td>

								<td class="labelText"><siga:Idioma key="censo.consultaDatosCV.literal.fechaVerificacion"/>&nbsp</td>
								<td  class="labelText">
									<%if (editarCampos) {%>
									<siga:Fecha  nombreCampo= "fechaMovimiento" valorInicial="<%=fechaCertificado%>"  posicionX="200" posicionY="10"/>
									<%} else { %>
									<siga:Fecha  nombreCampo= "fechaMovimiento" valorInicial="<%=fechaCertificado%>" disabled="true"/>
									<%}%>	
								</td>
							</tr>
								<tr>
									<td class="labelText"><siga:Idioma
											key="censo.datosCV.literal.creditos" />&nbsp</td>
									<td><html:text property="creditos"
											styleId="creditos"
											value='<%=creditos%>' maxlength="10" size="10"
											styleClass="<%=clase%>" readOnly="<%=desactivado%>"></html:text>
									</td>
								</tr>

								<tr>
									<td class="labelText"><siga:Idioma
											key="censo.datosCV.literal.descripcion" />&nbsp</td>
									<td colspan="3"><textarea cols="70" rows="7"
											id="descripcion" name="descripcion"
											onKeyDown="cuenta(this,500)" onChange="cuenta(this,500)"
											class="<%=clase%>" style="width: 550"><%=descripcion%></textarea>
									</td>
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>
			</table>
			<script>
				rellenarCampos();
			</script>
			<ajax:updateFieldFromField
				baseUrl="/SIGA/CEN_DatosCV.do?modo=obtenerColegiado"
				source="idInstitucionCargo" target="numcolegiado"
				parameters="idPersona={idPersona},idInstitucionCargo={idInstitucionCargo}"
				preFunction="preAccionColegiado" postFunction="postAccionColegiado" />
		</html:form>
		<!-- FIN: CAMPOS -->

		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- Aqui comienza la zona de botones de acciones -->

		<!-- INICIO: BOTONES REGISTRO -->
		<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->
		<siga:ConjBotonesAccion botones="<%=botones%>" modo="<%=modo%>" modal="M" />
		<!-- FIN: BOTONES REGISTRO -->

		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

	</div>
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>
