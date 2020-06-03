<!DOCTYPE html>
<%@page import="com.siga.beans.EnvTipoEnviosAdm"%>
<html>
<head>
<!-- facturacion/datosGenerales.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.AdmContadorBean"%>
<%@ page import="com.siga.beans.EnvEnviosAdm"%>
<%@ page import="com.siga.beans.FacPlantillaFacturacionBean"%> 
<%@ page import="com.siga.beans.FacSerieFacturacionBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	String accion = (String) ses.getAttribute("accion");
	String idInstitucion = user.getLocation();
	String editable = (String)ses.getAttribute("editable");
	boolean bEditable = (editable!=null && editable.equals("1"));
	boolean bEstaActivoElTraspasoParaEsteColegio = true;
	
	String idTipoEnvioCorreoElectronico = String.valueOf(EnvTipoEnviosAdm.K_CORREO_ELECTRONICO);
	String parametrosCmbPlantillaEnvios[] = {user.getLocation(), idTipoEnvioCorreoElectronico, "-1"};
	ArrayList<String> aPlantillaEnviosSeleccionada = new ArrayList<String>();
	ArrayList<String> aSerieSeleccionada = new ArrayList<String>();
	Integer iPlantilla=new Integer(-1);
	Integer nombreDescargaPDF=new Integer(1);
	String sAbreviatura="", sDescripcion="", idSerieFacturacion="-1", idSeriePrevia="-1", enviarFacturas="", generarPDF="", observaciones="", sPlantilla="", sFechaBaja="", sTipoSerieFacturacion="",sNombreDescargaPDF="", traspasarFacturas="", plantillaTraspasoFacturas="", plantillaTraspasoAuditoria="";
	FacSerieFacturacionBean beanSerieFacturacion = (FacSerieFacturacionBean) request.getAttribute("beanSerieFacturacion");
	if (beanSerieFacturacion!=null) {
		sAbreviatura = beanSerieFacturacion.getNombreAbreviado();
		sDescripcion = beanSerieFacturacion.getDescripcion();
		iPlantilla = beanSerieFacturacion.getIdPlantilla();
		idSerieFacturacion = String.valueOf(beanSerieFacturacion.getIdSerieFacturacion());
		nombreDescargaPDF = beanSerieFacturacion.getIdNombreDescargaPDF();
		enviarFacturas = beanSerieFacturacion.getEnvioFactura();
		traspasarFacturas = beanSerieFacturacion.getTraspasoFacturas();
		plantillaTraspasoFacturas = beanSerieFacturacion.getTraspasoPlantilla();
		plantillaTraspasoAuditoria = beanSerieFacturacion.getTraspasoCodAuditoriaDef();
		generarPDF = beanSerieFacturacion.getGenerarPDF();
		sTipoSerieFacturacion = beanSerieFacturacion.getTipoSerie();
		observaciones = beanSerieFacturacion.getObservaciones();
		sFechaBaja = (beanSerieFacturacion.getFechaBaja()!=null ? beanSerieFacturacion.getFechaBaja() : "");
		if (beanSerieFacturacion.getIdSerieFacturacionPrevia()!=null) {
			idSeriePrevia = beanSerieFacturacion.getIdSerieFacturacionPrevia().toString();
			aSerieSeleccionada.add(idSeriePrevia.toString());				
		}
		
		FacPlantillaFacturacionBean beanPlantillaFacturacion = (FacPlantillaFacturacionBean) request.getAttribute("beanPlantillaFacturacion");
		if (beanPlantillaFacturacion!=null) {
			sPlantilla = beanPlantillaFacturacion.getDescripcion();
			
			if(beanSerieFacturacion.getIdTipoPlantillaMail()!=null && !beanSerieFacturacion.getIdTipoPlantillaMail().equals("")){	
				aPlantillaEnviosSeleccionada.add(beanSerieFacturacion.getIdTipoPlantillaMail() + "," + idInstitucion + ",1");
				parametrosCmbPlantillaEnvios[2] = beanSerieFacturacion.getIdTipoPlantillaMail().toString();
			}
		}	
		sNombreDescargaPDF = (String) request.getAttribute("nombreFacturaDescarga");
	}
	else //Nueva serie de facturación: 
	{
		traspasarFacturas = "1"; //Por defecto se activa el check para que le dé error al usuario que cree una nueva serie, obligándole a pensar si se quiere traspasar o no.
	}
	
	// datos seleccionados Combo
	ArrayList aContadorSel = new ArrayList();
	String idContador="", prefijo="", contador="", sufijo="";
	AdmContadorBean beanContador = (AdmContadorBean) request.getAttribute("beanContador");	
	if (beanContador!=null) {
		aContadorSel.add(beanContador.getIdContador());
		idContador = beanContador.getIdContador();
		prefijo = beanContador.getPrefijo();
		contador = beanContador.getContador().toString();
		sufijo = beanContador.getSufijo();
	}
	ArrayList aContadorAbonosSel = new ArrayList();
	String idContadorAbonos="", prefijoAbonos="", contadorAbonos="", sufijoAbonos="";
	AdmContadorBean beanContadorAbonos = (AdmContadorBean) request.getAttribute("beanContadorAbonos");	
	if (beanContadorAbonos!=null) {
		aContadorAbonosSel.add(beanContadorAbonos.getIdContador());
		idContadorAbonos = beanContadorAbonos.getIdContador();
		prefijoAbonos = beanContadorAbonos.getPrefijo();
		contadorAbonos = beanContadorAbonos.getContador().toString();
		sufijoAbonos = beanContadorAbonos.getSufijo();
	}
	
	String parametro[] = new String[1];
	parametro[0] = user.getLocation();
	
	String datoSerie[] = new String[3];
	datoSerie[0] = idInstitucion;
	datoSerie[1] = idSerieFacturacion;
	datoSerie[2] = idSeriePrevia;
	
	// Informacion sobre formas de pago automática
	// Obtener informacion para rellenar en caso de modificacion o consulta
	ArrayList aFormasPago = new ArrayList(); // valores originales formas pago automática
	if (accion.equalsIgnoreCase("editar") || accion.equalsIgnoreCase("ver")) {
		Vector<Hashtable<String, Object>> vFormasPago = (Vector<Hashtable<String, Object>>) request.getAttribute("vFormasPago");
		if (vFormasPago!=null && vFormasPago.size()>0) {
			for (int i=0; i<vFormasPago.size(); i++) {
				Hashtable<String, Object> hFormaPago = vFormasPago.get(i);
				aFormasPago.add(UtilidadesHash.getString(hFormaPago, FacSerieFacturacionBean.C_IDFORMAPAGO));
			}
		}
	}
	
	//En modo consulta se deshabilita el combo
	boolean combodeshabilitado=false;
	if (accion.equals("ver"))
		combodeshabilitado = true;
	
	bEstaActivoElTraspasoParaEsteColegio = Boolean.parseBoolean((String)request.getAttribute("TRASPASOACTIVOPARAESTECOLEGIO"));
	if(traspasarFacturas == null)
		traspasarFacturas = "";
	
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>

	<html:javascript formName="DatosGeneralesForm" staticJavascript="false" />  
		 	
	<siga:TituloExt titulo="facturacion.asignacionConceptos.datosGenerales.cabecera" localizacion="facturacion.datosGenerales.literal.localizacion"/>

	<script language="JavaScript">
		var resetComboPagoSecretaria;
		
		function ediccion(){
			<%if (!bEditable){%>
				jQuery("#idNombreDescargaPDF").attr("disabled","disabled");
			<%}%>
			
		}
		function actualiza() {
			jQuery("#idTipoPlantillaMail").attr("disabled","disabled");
			
			<%if (bEditable){%>
				if (document.forms[0].envioFacturas.checked==true) {
					document.forms[0].generarPDF.checked=true;
					jQuery("#generarPDF").attr("disabled","disabled");
					jQuery("#idTipoPlantillaMail").removeAttr("disabled");
				} else {
					jQuery("#generarPDF").removeAttr("disabled");
					jQuery("#idTipoPlantillaMail").attr("disabled","disabled");
				}
			<%}%>
			
			return false;
		}

		function configuraContador() {
			var con = document.getElementById("conta");
			var con2 = document.getElementById("conta2");
			if (document.forms[0].configurarContador.checked==true) {
				con.style.display='table-row';
				con2.style.display='table-row';
				comprobarNuevo();
			} 
			else {
				con.style.display='none';
				con2.style.display='none';
			}
			return false;
		}

		function configuraContadorAbonos() {
			var con = document.getElementById("contaAbonos");
			var con2 = document.getElementById("conta2Abonos");
			if (document.forms[0].configurarContadorAbonos.checked==true) {
				con.style.display='table-row';
				con2.style.display='table-row';
				comprobarNuevoAbonos();
			} 
			else {
				con.style.display='none';
				con2.style.display='none';
			}
			return false;
		}

		function comprobarNuevo() {
			if (document.forms[0].contadorExistente.value=="") {
				jQuery("#prefijo_nuevo").removeAttr("disabled");
				jQuery("#contador_nuevo").removeAttr("disabled");
				jQuery("#sufijo_nuevo").removeAttr("disabled");

			} else {
				jQuery("#prefijo_nuevo").attr("disabled","disabled");
				jQuery("#contador_nuevo").attr("disabled","disabled");
				jQuery("#sufijo_nuevo").attr("disabled","disabled");

			}					
		}
			
		function comprobarNuevoAbonos() {
			if (document.forms[0].contadorExistenteAbonos.value=="") {
				jQuery("#prefijo_nuevo_abonos").removeAttr("disabled");
				jQuery("#contador_nuevo_abonos").removeAttr("disabled");
				jQuery("#sufijo_nuevo_abonos").removeAttr("disabled");

			} else {
				jQuery("#prefijo_nuevo_abonos").attr("disabled","disabled");
				jQuery("#contador_nuevo_abonos").attr("disabled","disabled");
				jQuery("#sufijo_nuevo_abonos").attr("disabled","disabled");

			}					
		}
			
		function refrescarLocal() {
			document.location = document.location;
		}

		//Asociada al boton Volver
		function accionVolver() {
			document.forms[0].action = "<%=app%>/FAC_AsignacionConceptosFacturables.do";	
			document.forms[0].target = "mainWorkArea";
			document.forms[0].modo.value = "abrirAvanzada";
			document.forms[0].submit();				
		}

		//Asociada al boton Restablecer
		function accionRestablecer() {		
			document.forms[0].reset();
		}
	
		//Asociada al boton Guardar
		function accionGuardar() {
			if (validateDatosGeneralesForm(document.DatosGeneralesForm)) {		
				if(confirm('<siga:Idioma key="messages.confirm.updateData"/>')) {
					if (document.forms[0].accion.value=="editar") {
						document.forms[0].modo.value="modificar";
					} else if (document.forms[0].accion.value=="nuevo") {
						document.forms[0].target="submitArea";
						document.forms[0].modo.value="insertar";
					}	
									
					var datos = "";
					var chk = document.getElementsByName("chk");
					if (chk !="checkbox") {
						for (var i = 0; i < chk.length; i++){
							if (chk[i].checked==1){
								
								if(jQuery("#comboSufijos_" + chk[i].value).val()<1) {
									mensaje = "<siga:Idioma key='facturacion.sufijos.message.error.cuenta.serie'/>";
									alert(mensaje);
									fin();
									return false;
								}
								
								datos=datos + chk[i].value +","+jQuery("#comboSufijos_" + chk[i].value).val()+"%";						
							}	
						}	
						
					} else {
					   if (chk.checked==1) {
							datos=datos + chk.value + "%";						
						}	
					}
					
					if (datos=="") {
						alert('<siga:Idioma key="Facturacion.mensajes.obligatoriaCuenta"/>');
						return false;
					}
					
					// Validacion de contadores
					if (document.forms[0].configurarContador.checked==1 && document.forms[0].contadorExistente.value=="") {
						
						
						// se trata de un nuevo contador
						if (document.forms[0].prefijo_nuevo.value.length>8) {
							alert('<siga:Idioma key="Facturacion.mensajes.contadorFacturas.longitud.prefijo"/>');
							return false;
						}	
						
						if (document.forms[0].sufijo_nuevo.value.length>8) {
							alert('<siga:Idioma key="Facturacion.mensajes.contadorFacturas.longitud.sufijo"/>');
							return false;
						}
						
	                    if (isNaN(document.forms[0].contador_nuevo.value)) {
							alert('<siga:Idioma key="Facturacion.mensajes.contadorFacturas.noNumerico.contador"/>');
							return false;
						}	
						if (document.forms[0].contador_nuevo.value=="") {
						
						    alert('<siga:Idioma key="Facturacion.mensajes.contadorFacturas.obligatorio.contador"/>');
							return false;
						}	
					}
					if (document.forms[0].configurarContadorAbonos.checked==1 && document.forms[0].contadorExistenteAbonos.value=="") {
						
						
						// se trata de un nuevo contador
						if (document.forms[0].prefijo_nuevo_abonos.value.length>8) {
							alert('<siga:Idioma key="Facturacion.mensajes.contadorAbonos.longitud.prefijo"/>');
							return false;
						}	
						
						if (document.forms[0].sufijo_nuevo_abonos.value.length>8) {
							alert('<siga:Idioma key="Facturacion.mensajes.contadorAbonos.longitud.sufijo"/>');
							return false;
						}
						
	                    if (isNaN(document.forms[0].contador_nuevo_abonos.value)) {
							alert('<siga:Idioma key="Facturacion.mensajes.contadorAbonos.noNumerico.contador"/>');
							return false;
						}	
						if (document.forms[0].contador_nuevo_abonos.value=="") {
						
						    alert('<siga:Idioma key="Facturacion.mensajes.contadorAbonos.obligatorio.contador"/>');
							return false;
						}	
					}

					if(document.DatosGeneralesForm.envioFacturas.checked){
						if(document.DatosGeneralesForm.idTipoPlantillaMail.value == ""){
							alert('<siga:Idioma key="Facturacion.mensajes.obligatorio.plantillaMail"/>');
							return false;
						}
					}
					
					if(document.DatosGeneralesForm.traspasoFacturas != null) {
						if(document.DatosGeneralesForm.traspasoFacturas.checked){
							if(document.DatosGeneralesForm.plantillaTraspasoFacturas.value == ""){
								alert('<siga:Idioma key="facturacion.mensajes.obligatorio.plantillaTraspaso"/>');
								return false;
							}
							if(document.DatosGeneralesForm.plantillaTraspasoAuditoria.value == ""){
								alert('<siga:Idioma key="facturacion.mensajes.obligatorio.plantillaAuditoriaTraspaso"/>');
								return false;
							}
						}
					}

					document.forms[0].ids.value = datos;
					document.forms[0].submit();
				 }
			}
		}
		
		function recargarCombo(valorRadio){	
					
			var chk = document.getElementsByName("chk");
			
			for (var i = 0; i < chk.length; i++) {
				if (chk[i].checked==1) {
					jQuery("#comboSufijos_" + chk[i].value).val(jQuery("#idsufijodefBanco_" + chk[i].value).val());
				} else { 
					jQuery("#comboSufijos_" + chk[i].value).val("");
				}	
			}		
		}
		
		function habilitarCamposTraspaso()
		{
			var campoCheckTraspasoFacturas = document.getElementById("traspasoFacturas");
			var campoPlantilla = document.getElementById("plantillaTraspasoFacturas");
			var campoPlantillaAuditoria = document.getElementById("plantillaTraspasoAuditoria");
			
			<% if (bEditable){ %>
			if(campoCheckTraspasoFacturas != null)
			{
				if(campoCheckTraspasoFacturas.checked)
				{
					campoPlantilla.removeAttribute("disabled");
					campoPlantillaAuditoria.removeAttribute("disabled");
				}
				else
				{
					campoPlantilla.value="";
					campoPlantillaAuditoria.value="";
					campoPlantilla.setAttribute("disabled", "disabled");
					campoPlantillaAuditoria.setAttribute("disabled", "disabled");
				}
			}
			<% } %>
			
			return false;
		}
	</script>
</head>

<body class="tablaCentralCampos" onLoad="configuraContador();configuraContadorAbonos();actualiza();ediccion();habilitarCamposTraspaso();">
	
	<html:form action="/FAC_DatosGenerales.do" method="POST" focus="nombreAbreviado" target="submitArea">
		<html:hidden property="modo" value=""/>
		<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
		<html:hidden property="idSerieFacturacion" value="<%=idSerieFacturacion%>"/>
		<html:hidden property="accion" value="<%=accion%>"/>
		<html:hidden property="ids" value=""/>
		
		<table class="tablaCentralCampos">
			<tr>
				<td style="width:100%">		
					<siga:ConjCampos leyenda="facturacion.serios.literal.seriesFacturacion">
						<table class="tablaCentralCampos" border="0">
							<tr>
								<td width="22%"></td>
								<td width="3%"></td>
								<td width="14%"></td>
								<td width="23%"></td>
								<td width="15%"></td>
								<td width="23%"></td>
							</tr>
							<tr>
								<td class="labelText" style="text-align:left"><siga:Idioma key="facturacion.datosGenerales.literal.nombreAbreviado"/>&nbsp;(*)</td>
								<td colspan="3">
<%
									if (!bEditable) {
%>
										<html:text name="DatosGeneralesForm" styleId="nombreAbreviado" property="nombreAbreviado" size="30" maxlength="20" styleClass="boxConsulta" value="<%=sAbreviatura%>" readonly="true"/>
<%
									} else {
%>
										<html:text name="DatosGeneralesForm" styleId="nombreAbreviado" property="nombreAbreviado" size="30" maxlength="20" styleClass="boxMayuscula" value="<%=sAbreviatura%>" readonly="false"/>
<%
									}
%>
								</td>

								<td class="labelText" nowrap title='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (user, "facturacion.seriesFacturacion.tipoGenerica.ayuda").replaceAll("\\\\n", ""))%>'>
									<siga:Idioma key="facturacion.datosGenerales.literal.tipoGenerica"/>&nbsp;&nbsp;
<% 
									if (sTipoSerieFacturacion!=null && sTipoSerieFacturacion.equals("G")) { 
%>
										<input type="checkbox" name="tipoSerie" id="tipoSerie" <%=(accion.equals("ver") ? "disabled" : "") %> checked>
<% 
									} else { 
%>
										<input type="checkbox" name="tipoSerie" id="tipoSerie" <%=(accion.equals("ver") ? "disabled" : "") %> >
<% 
									} 
%>
								</td>					
								<td class="labelText" nowrap><siga:Idioma key="facturacion.datosGenerales.literal.estado"/>&nbsp;&nbsp;
<%
									if (sFechaBaja==null || sFechaBaja.equals("")) {
%>								
										<siga:Idioma key="facturacion.datosGenerales.literal.estado.alta"/>
<%
									} else {
%>										
										<siga:Idioma key="facturacion.datosGenerales.literal.estado.baja"/>
<%
									}
%>																			
								</td>	
							</tr>
												
							<tr> 
       							<td class="labelText" style="text-align:left"><siga:Idioma key="facturacion.datosGenerales.literal.descripcion"/>&nbsp;(*)</td>
								<td colspan="5">
<%
									if (!bEditable) {
%>
										<html:text name="DatosGeneralesForm"  styleId="descripcion"  property="descripcion" size="120" maxlength="100" styleClass="boxConsulta" value="<%=sDescripcion%>" readonly="true"/>
<%
									} else {
%>
										<html:text name="DatosGeneralesForm" styleId="descripcion"  property="descripcion" size="100" maxlength="100" styleClass="box" value="<%=sDescripcion%>" readonly="false"/>
<%
									}
%>
								</td>
								
							</tr>
							
							<tr>
								<td class="labelText" style="text-align:left" nowrap="nowrap"><siga:Idioma key="facturacion.datosGenerales.literal.facturacion.datosGenerales.literal.planificarPosteriormente"/></td>
								<td colspan="5">
<%
									if (!bEditable) {
%>
										<siga:ComboBD nombre = "idSerieFacturacionPrevia" tipo="cmbSerieFacturacionPrevias" elementoSel ="<%=aSerieSeleccionada%>" clase="boxCombo" ancho="750" parametro="<%=datoSerie%>" readonly="true"/>
<%
									} else { 
%>
										<siga:ComboBD nombre = "idSerieFacturacionPrevia" tipo="cmbSerieFacturacionPrevias" elementoSel ="<%=aSerieSeleccionada%>" clase="boxCombo" ancho="750" parametro="<%=datoSerie%>"/>
<%
									}
%>									
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
		
		<table class="tablaCentralCampos">
			<tr>
				<td style="width:50%">		
					<siga:ConjCampos leyenda="facturacion.datosGenerales.literal.observaciones" desplegable="true">
						<table align="center" border="0">
							<tr>
								<td>
<%
									if (!bEditable) {
%>
										<html:textarea name="DatosGeneralesForm" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" styleId="observaciones" property="observaciones" onkeydown="cuenta(this,4000);" styleClass="boxConsulta" value="<%=observaciones%>" readonly="true"
											style="overflow-y:auto;overflow-x:hidden;width:400px;height:80px;resize:none;"/>
<%
									} else {
%>
										<html:textarea name="DatosGeneralesForm" property="observaciones" styleId="observaciones" onkeydown="cuenta(this,4000)" onChange="cuenta(this,4000)" styleClass="box" value="<%=observaciones%>" readonly="false"
											style="overflow-y:auto;overflow-x:hidden;width:400px;height:80px;resize:none;"/>
<%
									}
%>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
				
				<td style="width:50%">
					<siga:ConjCampos leyenda="facturacion.serios.literal.formaPago" desplegable="true">
						<table align="center" border="0">
							<tr>
								<td width="50%" valign="middle" class="labelText" title='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (user, "facturacion.seriesFacturacion.formaPago.ayuda").replaceAll("\\\\n", ""))%>'>
									<siga:Idioma key="facturacion.serios.literal.formaPagoSeleccionar"/>
								</td>
								<td width="50%">
<%
									if (aFormasPago.isEmpty()) {
										if (accion != "ver") {
%>
											<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="5" seleccionMultiple="true" elementoSel="<%=0%>" obligatorio="true"/>
<%
										} else {
%>
											<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="5" seleccionMultiple="true" elementoSel="<%=0%>" obligatorio="true" readOnly="true" />
<%
										}
									} else { 
										if (accion != "ver") {
%>
											<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="5" seleccionMultiple="true" elementoSel="<%=aFormasPago%>" obligatorio="true"/>
<%
										} else {
%>
											<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="5" seleccionMultiple="true" elementoSel="<%=aFormasPago%>" obligatorio="true" readOnly="true" />
<%
										}
									}
%> 
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>
		
		<table class="tablaCentralCampos">	
			<tr>
				<td style="width:100%" >		
					<siga:ConjCampos leyenda="facturacion.serios.literal.configuracionPDF">
						<table align="left" width="100%" border="0">
							<tr>
								<td width="22%"></td>
								<td width="3%"></td>
								<td width="14%"></td>
								<td width="23%"></td>
								<td width="15%"></td>
								<td width="23%"></td>
							</tr>
							<tr>
								<td class="labelText" style="text-align:left">
									<siga:Idioma key="facturacion.datosGenerales.literal.generaPDF"/>
								</td>
								<td class="labelText" style="text-align:left">
<% 
									if (enviarFacturas!=null && enviarFacturas.equals("1")) { 
%>
										<input type="checkbox"  id="generarPDF"  name="generarPDF" checked disabled>
<% 
									} else if (generarPDF!=null && generarPDF.equals("1")) { 
%>
										<input type="checkbox" id="generarPDF" name="generarPDF" <%=(accion.equals("ver"))?"disabled":"" %> checked>
<% 
									} else { 
%>
										<input type="checkbox"  id="generarPDF" name="generarPDF" <%=(accion.equals("ver"))?"disabled":"" %> >
<% 
									} 
%>
								</td>
								<td class="labelText" style="text-align:left"><siga:Idioma key="facturacion.datosGenerales.literal.plantilla"/>&nbsp;(*)</td>
								<td>
<%
									if (!bEditable) {
%>
										<html:text name="DatosGeneralesForm" styleId="plantilla"  property="plantilla" style="width:400px" maxlength="100" styleClass="boxComboConsulta" value="<%=sPlantilla%>" readonly="true"/>
<%
									} else {
										String dato[] = new String[1];
										dato[0] = idInstitucion;
										ArrayList aPlantilla = new ArrayList();
										aPlantilla.add(iPlantilla.toString());
%>
										<siga:ComboBD nombre = "idPlantilla" tipo="cmbPlantilla" elementoSel ="<%=aPlantilla%>" clase="boxCombo" parametro="<%=dato%>" ancho="200"/>
<%
									}
%>
								</td>
								
								<td class="labelText" style="text-align:left"><siga:Idioma key="facturacion.datosGenerales.literal.nombrePDF"/>&nbsp;(*)</td>
								<td>		
									<siga:ComboBD nombre="idNombreDescargaPDF"  elementoSelstring="<%=String.valueOf(nombreDescargaPDF)%>" obligatorioSinTextoSeleccionar="true" tipo="cmbNombreDescargaPDF" clase="boxCombo" ancho="200" />
								</td>		
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		 </table>
		 
		 <table class="tablaCentralCampos">	
			<tr>
				<td style="width:100%" >		
					<siga:ConjCampos leyenda="facturacion.serios.literal.configuracionEnvios">
						<table align="left" width="100%" border="0">
							<tr>
								<td width="22%"></td>
								<td width="3%"></td>
								<td width="14%"></td>
								<td width="23%"></td>
								<td width="15%"></td>
								<td width="23%"></td>
							</tr>
							<tr>
								<td class="labelText" style="text-align:left"><siga:Idioma key="facturacion.datosGenerales.literal.envioFacturas"/></td>
								<td class="labelText" style="text-align:left">
<% 
									if (enviarFacturas!=null && enviarFacturas.equals("1")) { 
%>
										<input type="checkbox" name="envioFacturas"  id="envioFacturas"  onclick="actualiza();" <%=(accion.equals("ver"))?"disabled":"" %> checked>
<% 
									} else { 
%>
										<input type="checkbox" name="envioFacturas"  id="envioFacturas"  onclick="actualiza();" <%=(accion.equals("ver"))?"disabled":"" %> >
<% 
									} 
%>
								</td>
								
								<td id="titulo" class="labelText"><siga:Idioma key="envios.plantillas.literal.plantilla"/></td>
								<td colspan="3">
									<siga:ComboBD  nombre = "idTipoPlantillaMail" tipo="cmbPlantillaEnvios3" clase="boxCombo" elementoSel="<%=aPlantillaEnviosSeleccionada%>" ancho="550" obligatorio="false" pestana="true" parametro="<%=parametrosCmbPlantillaEnvios%>" />
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		 </table>
		 
		 <% if(bEstaActivoElTraspasoParaEsteColegio){ %>
		 <table class="tablaCentralCampos">	
			<tr>
				<td style="width:100%" >		
					<siga:ConjCampos leyenda="facturacion.serios.literal.configuracionTraspasoFacturas">
						<table align="left" width="100%" border="0">
							<tr>
								<td width="22%"></td>
								<td width="3%"></td>
								<td width="14%"></td>
								<td width="23%"></td>
								<td width="15%"></td>
								<td width="23%"></td>
							</tr>
							<tr>
								<td class="labelText" style="text-align:left">
									<siga:Idioma key="facturacion.datosGenerales.literal.traspasarFacturas"/>
								</td>
								<td class="labelText" style="text-align:left">
<%
									if (traspasarFacturas.equals("1")) { 
%>
										<input type="checkbox" id="traspasoFacturas" name="traspasoFacturas" <%=(accion.equals("ver"))?"disabled":"" %> checked onchange="habilitarCamposTraspaso()">
<% 
									} else { 
%>
										<input type="checkbox" id="traspasoFacturas" name="traspasoFacturas" <%=(accion.equals("ver"))?"disabled":"" %> onchange="habilitarCamposTraspaso()">
<% 
									} 
%>
								</td>
								<td class="labelText">
									<siga:Idioma key="facturacion.datosGenerales.literal.plantillaTraspasoFacturas"/>
								</td>
								<td>
<% 									if(accion.equals("ver")){ %>
										<html:text name="DatosGeneralesForm" id="plantillaTraspasoFacturas" styleId="plantillaTraspasoFacturas" property="plantillaTraspasoFacturas" size="30" maxlength="10" value="<%= plantillaTraspasoFacturas %>" styleClass="boxConsulta" readonly="true" />
<%									} else { %>
										<html:text name="DatosGeneralesForm" id="plantillaTraspasoFacturas" styleId="plantillaTraspasoFacturas" property="plantillaTraspasoFacturas" size="25" maxlength="10" value="<%= plantillaTraspasoFacturas %>" styleClass="box" readonly="false"  />
<%									} %>
								</td>
								
								<td id="titulo" class="labelText"><siga:Idioma key="envios.plantillas.literal.plantillaTraspasoFacturasAuditoria"/></td>
								<td>
<% 									if(accion.equals("ver")){ %>
										<html:text name="DatosGeneralesForm" id="plantillaTraspasoAuditoria" styleId="plantillaTraspasoAuditoria" property="plantillaTraspasoAuditoria" size="30" maxlength="10" value="<%= plantillaTraspasoAuditoria %>" styleClass="boxConsulta" readonly="true" />
<%									} else { %>
										<html:text name="DatosGeneralesForm" id="plantillaTraspasoAuditoria" styleId="plantillaTraspasoAuditoria" property="plantillaTraspasoAuditoria" size="25" maxlength="10" value="<%= plantillaTraspasoAuditoria %>" styleClass="box" readonly="false"  />
<%									} %>
								</td>									
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		 </table>
		 <% } %>

<% 
		if (accion.equals("nuevo")) {  
%>
			<table class="tablaCentralCampos"style="display:none" >
		
<% 
		} else {  
%>
			<table class="tablaCentralCampos" border="0">	
<% 
		} 
%>

			<tr>
				<td style="width:50%">		
					<siga:ConjCampos leyenda="facturacion.series.literal.contador.facturas" desplegable="true">
						<table align="left" border="0">
							<tr>
								<td width="45%"/>
								<td width="55%"/>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.datosGenerales.literal.nombreContador"/>&nbsp;
								</td>
								<td class="labelTextValue">
									<html:text name="DatosGeneralesForm" property="idContador" styleId="idContador" size="35" maxlength="20" styleClass="boxConsulta" value="<%=idContador%>" readonly="true"/>
								</td>
							</tr>
							
							<tr>
								<td class="labelText" nowrap="nowrap">
									<siga:Idioma key="facturacion.datosGenerales.literal.contadorGenerico"/>&nbsp;
								</td>
								<td class="labelTextValue" >
									<html:text name="DatosGeneralesForm" styleId="prefijo"  property="prefijo"  size="5" maxlength="10" styleClass="estiloNumber" value="<%=prefijo%>" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="contador" property="contador" size="10" maxlength="15" styleClass="estiloNumber" value="<%=contador%>" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="sufijo"   property="sufijo"   size="5" maxlength="10" styleClass="estiloNumber" value="<%=sufijo%>" disabled="true"/>
								</td>
							</tr>
							
							<tr>
<% 
								if (accion.equals("ver")) { 
%>
									<td class="labelText">
								 		&nbsp;
									</td>
									<td  class="labelTextValue">
										<input type="hidden" name="configurarContador" value="off"/>
									</td>
<% 
								} else { 
%>
									<td class="labelText" nowrap="nowrap">
										<siga:Idioma key="facturacion.datosGenerales.literal.configurarContador"/>&nbsp;
									</td>
									<td  class="labelTextValue">
										<input type="checkbox" name="configurarContador" id="configurarContador" onclick="configuraContador();"/>
									</td>
<% 
								} 
%>																
							</tr>
							
							<tr id="conta">								
								<td class="labelText">
									<siga:Idioma key="facturacion.datosGenerales.literal.existentes"/>&nbsp;
								</td>
								<td class="labelText">
									<siga:ComboBD nombre="contadorExistente" tipo="cmbContadorFacturacion" parametro="<%=parametro%>" clase="boxCombo" obligatorio="false" elementoSel="<%=aContadorSel%>" accion="comprobarNuevo()" />
								</td>
							</tr>
							
							<tr id="conta2">	
								<td class="labelText" >
									<siga:Idioma key="facturacion.datosGenerales.literal.nuevoContador"/>&nbsp;
								</td>
								<td  class="labelTextValue">
									<html:text name="DatosGeneralesForm" styleId="prefijo_nuevo"  property="prefijo_nuevo"  size="5" maxlength="10" styleClass="box" value="" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="contador_nuevo" property="contador_nuevo" size="10" maxlength="15" styleClass="box" value="" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="sufijo_nuevo"   property="sufijo_nuevo"   size="5" maxlength="10" styleClass="box" value="" disabled="true"/>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
				<td style="width:50%">		
					<siga:ConjCampos leyenda="facturacion.series.literal.contador.abonos" desplegable="true">
						<table align="left" border="0">
							<tr>
								<td width="45%"/>
								<td width="55%"/>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="facturacion.datosGenerales.literal.nombreContador"/>&nbsp;
								</td>
								<td  class="labelTextValue" >
									<html:text name="DatosGeneralesForm" property="idContadorAbonos" styleId="idContadorAbonos" size="35" maxlength="20" styleClass="boxConsulta" value="<%=idContadorAbonos%>" readonly="true"/>
								</td>
							</tr>
							
							<tr>
								<td class="labelText" nowrap="nowrap">
									<siga:Idioma key="facturacion.datosGenerales.literal.contadorGenerico"/>&nbsp;
								</td>
								<td  class="labelTextValue" >
									<html:text name="DatosGeneralesForm" styleId="prefijoAbonos"  property="prefijoAbonos"  size="5" maxlength="10" styleClass="estiloNumber" value="<%=prefijoAbonos%>" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="contadorAbonos" property="contadorAbonos" size="10" maxlength="15" styleClass="estiloNumber" value="<%=contadorAbonos%>" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="sufijoAbonos"   property="sufijoAbonos"   size="5" maxlength="10" styleClass="estiloNumber" value="<%=sufijoAbonos%>" disabled="true"/>
								</td>
							</tr>
							
							<tr>
<% 
								if (accion.equals("ver")) { 
%>
									<td class="labelText">
								 		&nbsp;
									</td>
									<td  class="labelTextValue">
										<input type="hidden" name="configurarContadorAbonos" value="off"/>
									</td>
<% 
								} else { 
%>
									<td class="labelText" nowrap="nowrap">
										<siga:Idioma key="facturacion.datosGenerales.literal.configurarContador"/>&nbsp;
									</td>
									<td  class="labelTextValue">
										<input type="checkbox" name="configurarContadorAbonos" id="configurarContadorAbonos" onclick="configuraContadorAbonos();"/>
									</td>
<% 
								} 
%>																
							</tr>
							
							<tr id="contaAbonos">								
								<td class="labelText">
									<siga:Idioma key="facturacion.datosGenerales.literal.existentes"/>&nbsp;
								</td>
								<td class="labelText">
									<siga:ComboBD nombre="contadorExistenteAbonos" tipo="cmbContadorFacturacionAbonos" parametro="<%=parametro%>" clase="boxCombo" obligatorio="false" elementoSel="<%=aContadorAbonosSel%>" accion="comprobarNuevoAbonos()" />
								</td>
							</tr>
							
							<tr id="conta2Abonos">	
								<td class="labelText" >
									<siga:Idioma key="facturacion.datosGenerales.literal.nuevoContador"/>&nbsp;
								</td>
								<td  class="labelTextValue">
									<html:text name="DatosGeneralesForm" styleId="prefijo_nuevo_abonos"  property="prefijo_nuevo_abonos"  size="5" maxlength="10" styleClass="box" value="" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="contador_nuevo_abonos" property="contador_nuevo_abonos" size="10" maxlength="15" styleClass="box" value="" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="sufijo_nuevo_abonos"   property="sufijo_nuevo_abonos"   size="5" maxlength="10" styleClass="box" value="" disabled="true"/>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>

	</html:form>

<%
	if (!bEditable) {
%>
		<siga:ConjBotonesAccion botones="V" />
<%
	} else {
%>
		<siga:ConjBotonesAccion botones="V,G,R" />
<%
	}
%>

	<siga:Table 
   		name="tablaResultados"
   		border="1"
   		columnNames="facturacion.devolucionManual.seleccion,
			   		facturacion.ficheroBancarioAbonos.literal.banco,
			   		facturacion.cuentasBancarias.IBAN,
			   		facturacion.sufijos.literal.sufijo,
			   		facturacion.cuentasBancarias.comision,
			   		facturacion.cuentasBancarias.uso"
   		columnSizes="5,30,20,21,8,8,8"
   		modal="P">
			   				   
<%
		Vector<Hashtable<String, Object>> vBancosInstitucion = (Vector<Hashtable<String, Object>>) request.getAttribute("vBancosInstitucion");
		if (vBancosInstitucion==null || vBancosInstitucion.size()<1) {
%>
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%
	    } else { 
			int recordNumber=1;
			for (int i=0; vBancosInstitucion!=null && i<vBancosInstitucion.size(); i++) {
				Hashtable<String, Object> hBancoInstitucion = vBancosInstitucion.get(i);

           		String sel = UtilidadesHash.getString(hBancoInstitucion, "SELECCIONADO");
           		boolean bsel= (sel!=null && !sel.equals("0") && !accion.equals("nuevo"));
           		
           		String sIdInstitucion = UtilidadesHash.getString(hBancoInstitucion, "IDINSTITUCION");
           		String sCodBanco = UtilidadesHash.getString(hBancoInstitucion, "COD_BANCO");
           		String sIdSerieFacturacion = UtilidadesHash.getString(hBancoInstitucion, "IDSERIEFACTURACION");
           		String sBancosCodigo = UtilidadesHash.getString(hBancoInstitucion, "BANCOS_CODIGO");
           		String sIdSufijo = UtilidadesHash.getString(hBancoInstitucion, "IDSUFIJO");
           		String sBanco = UtilidadesHash.getString(hBancoInstitucion, "BANCO");
           		String sIBAN = UtilidadesHash.getString(hBancoInstitucion, "IBAN");
           		String sComisionImporte = UtilidadesString.formatoImporte(UtilidadesHash.getString(hBancoInstitucion, "COMISIONIMPORTE"));
           		String sUso = UtilidadesHash.getString(hBancoInstitucion, "USO");
           		
           		String idComboSuf= "comboSufijos_" + sBancosCodigo;
           		String idsufijodefBanco = "idsufijodefBanco_" + sBancosCodigo;
           		
%>
				<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>' botones='' modo='<%=accion%>' visibleConsulta='no' visibleEdicion='no' visibleBorrado='no' clase="listaNonEdit" pintarespacio='no'>
					<td>
						<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_1" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=sIdInstitucion%>">
						<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_2" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=sCodBanco%>">
						<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_3" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=sIdSerieFacturacion%>">
						<input type="radio" onclick="recargarCombo(this)" value="<%=sBancosCodigo%>"  id="oculto<%=String.valueOf(recordNumber)%>_4" name="chk" <%=(bsel)?"checked":"" %> <%=(accion.equals("ver")) ? "disabled" : "" %> >
					</td>
					  	
					<td><%=UtilidadesString.mostrarDatoJSP(sBanco)%></td>  	
					<td align="left"><%=UtilidadesString.mostrarIBANConAsteriscos(sIBAN)%></td>  	
					<td align="right">
						<input type="hidden" id="<%=idsufijodefBanco%>" value="<%=sIdSufijo%>">
						<bean:define id="lSufijos" name="lSufijos" scope="request"/>
						<html:select styleId="<%=idComboSuf%>" name = "comboSufijos" property="idSufijo" value="<%=sIdSufijo%>" styleClass="boxCombo" disabled="<%=combodeshabilitado%>" style="width:200px;">
							<html:option value=""><c:out value=""/></html:option> 	
							<c:forEach items="${lSufijos}" var="sufijoSerieCmb">												
								<html:option value="${sufijoSerieCmb.idSufijo}">										
									<c:if test="${sufijoSerieCmb.sufijo.trim().length()>0}">
										<c:out value="${sufijoSerieCmb.sufijo} ${sufijoSerieCmb.concepto}"/>
									</c:if>
									<c:if test="${sufijoSerieCmb.sufijo.trim().length()==0}">
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${sufijoSerieCmb.concepto}"/>
									</c:if>
								</html:option>	
							</c:forEach>
						</html:select>							
					</td>  	
					<td align="right"><%=sComisionImporte%>&nbsp;&euro;</td>  															
					<td align="right"><%=sUso%></td>  	
				</siga:FilaConIconos>
<% 
				recordNumber++;
			} 
		} 
%>
	</siga:Table>												

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>