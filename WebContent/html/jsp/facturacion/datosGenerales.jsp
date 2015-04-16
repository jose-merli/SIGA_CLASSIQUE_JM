<!DOCTYPE html>
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
	
	String idTipoEnvioCorreoElectronico = String.valueOf(EnvEnviosAdm.TIPO_CORREO_ELECTRONICO);
	String parametrosCmbPlantillaEnvios[] = {user.getLocation(), idTipoEnvioCorreoElectronico, "-1"};
	ArrayList<String> aPlantillaEnviosSeleccionada = new ArrayList<String>();
	ArrayList<String> aSerieSeleccionada = new ArrayList<String>();
	Integer iPlantilla=new Integer(-1);
	String sAbreviatura="", sDescripcion="", idSerieFacturacion="", enviarFacturas="", generarPDF="", observaciones="", sPlantilla="", sFechaBaja="";
	FacSerieFacturacionBean beanSerieFacturacion = (FacSerieFacturacionBean) request.getAttribute("beanSerieFacturacion");	
	if (beanSerieFacturacion!=null) {		
		sAbreviatura = beanSerieFacturacion.getNombreAbreviado();
		sDescripcion = beanSerieFacturacion.getDescripcion();
		iPlantilla = beanSerieFacturacion.getIdPlantilla();
		idSerieFacturacion = String.valueOf(beanSerieFacturacion.getIdSerieFacturacion());
		enviarFacturas = beanSerieFacturacion.getEnvioFactura();
		generarPDF = beanSerieFacturacion.getGenerarPDF();
		observaciones = beanSerieFacturacion.getObservaciones();
		sFechaBaja = (beanSerieFacturacion.getFechaBaja()!=null ? beanSerieFacturacion.getFechaBaja() : "");
		if (beanSerieFacturacion.getIdSerieFacturacionPrevia()!=null) {
			String idSeriePrevia = beanSerieFacturacion.getIdSerieFacturacionPrevia().toString();
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
	
	String parametro[] = new String[1];
	parametro[0] = user.getLocation();
	
	String datoSerie[] = new String[2];
	datoSerie[0] = idInstitucion;
	datoSerie[1] = idSerieFacturacion;
	
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
		combodeshabilitado=true;
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
				con.style.display='block';
				con2.style.display='block';
				comprobarNuevo();
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
						if (document.forms[0].prefijo_nuevo.length>8) {
							alert('<siga:Idioma key="Facturacion.mensajes.longitud.prefijo"/>');
							return false;
						}	
						
						if (document.forms[0].sufijo_nuevo.length>8) {
							alert('<siga:Idioma key="Facturacion.mensajes.longitud.sufijo"/>');
							return false;
						}
						
	                    if (isNaN(document.forms[0].contador_nuevo.value)) {
							alert('<siga:Idioma key="Facturacion.mensajes.noNumerico.contador"/>');
							return false;
						}	
						if (document.forms[0].contador_nuevo.value=="") {
						
						    alert('<siga:Idioma key="Facturacion.mensajes.obligatorio.contador"/>');
							return false;
						}	
					}

					if(document.DatosGeneralesForm.envioFacturas.checked){
						if(document.DatosGeneralesForm.idTipoPlantillaMail.value == ""){
							alert('<siga:Idioma key="Facturacion.mensajes.obligatorio.plantillaMail"/>');
							return false;
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
	</script>
</head>

<body class="tablaCentralCampos" onLoad="configuraContador();actualiza();">
	
	<table class="tablaCentralCampos">
		<html:form action="/FAC_DatosGenerales.do" method="POST" focus="nombreAbreviado" target="submitArea">
			<html:hidden property="modo" value=""/>
			<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
			<html:hidden property="idSerieFacturacion" value="<%=idSerieFacturacion%>"/>
			<html:hidden property="accion" value="<%=accion%>"/>
			<html:hidden property="ids" value=""/>
			<tr>
				<td style="width:100%">		
					<siga:ConjCampos leyenda="facturacion.serios.literal.seriesFacturacion">
						<table align="center" border="0">

							<tr>
								<td class="labelText" width="10%" style="text-align:left"><siga:Idioma key="facturacion.datosGenerales.literal.nombreAbreviado"/>&nbsp;(*)</td>
								<td>
<%
									if (!bEditable) {
%>
										<html:text name="DatosGeneralesForm" styleId="nombreAbreviado" property="nombreAbreviado" size="20" maxlength="20" styleClass="boxConsulta" value="<%=sAbreviatura%>" readonly="true"/>
<%
									} else {
%>
										<html:text name="DatosGeneralesForm" styleId="nombreAbreviado" property="nombreAbreviado" size="20" maxlength="20" styleClass="boxMayuscula" value="<%=sAbreviatura%>" readonly="false"/>
<%
									}
%>
								</td>
								
       							<td class="labelText" width="10%" style="text-align:left"><siga:Idioma key="facturacion.datosGenerales.literal.plantilla"/>&nbsp;(*)</td>
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
										<siga:ComboBD nombre = "idPlantilla" tipo="cmbPlantilla" elementoSel ="<%=aPlantilla%>" clase="boxCombo" parametro="<%=dato%>" ancho="400"/>
<%
									}
%>
								</td>								
								
								<td class="labelText" nowrap><siga:Idioma key="facturacion.datosGenerales.literal.estado"/></td>
								<td>
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
       							<td class="labelText" width="10%" style="text-align:left"><siga:Idioma key="facturacion.datosGenerales.literal.descripcion"/>&nbsp;(*)</td>
								<td colspan="5">
<%
									if (!bEditable) {
%>
										<html:text name="DatosGeneralesForm"  styleId="descripcion"  property="descripcion" size="100" maxlength="100" styleClass="boxConsulta" value="<%=sDescripcion%>" readonly="true"/>
<%
									} else {
%>
										<html:text name="DatosGeneralesForm" styleId="descripcion"  property="descripcion" size="100" maxlength="100" styleClass="boxCombo" value="<%=sDescripcion%>" readonly="false"/>
<%
									}
%>
								</td>
							</tr>
						</table>
						
						<table border="0">
							<tr>
								<td class="labelText" style="text-align:left" colspan="4">
									<siga:Idioma key="facturacion.datosGenerales.literal.generaPDF"/>&nbsp;&nbsp;
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
									&nbsp;&nbsp;&nbsp;
									<siga:Idioma key="facturacion.datosGenerales.literal.envioFacturas"/>&nbsp;&nbsp;
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
								<td>
									<siga:ComboBD  nombre = "idTipoPlantillaMail" tipo="cmbPlantillaEnvios3" clase="boxCombo" elementoSel="<%=aPlantillaEnviosSeleccionada%>" ancho="280" obligatorio="false" pestana="true" parametro="<%=parametrosCmbPlantillaEnvios%>" />
								</td>									
							</tr>

							<tr>
							    <td class="labelText" width="10%" style="text-align:left" >
									<siga:Idioma key="facturacion.datosGenerales.literal.observaciones"/>
								</td>
								<td colspan="3">
<%
									if (!bEditable) {
%>
										<html:textarea name="DatosGeneralesForm" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" styleId="observaciones" property="observaciones" onkeydown="cuenta(this,4000);" styleClass="boxConsulta" value="<%=observaciones%>" readonly="true"
											style="overflow-y:auto;overflow-x:hidden;width:350px;height:50px;resize:none;"/>
<%
									} else {
%>
										<html:textarea name="DatosGeneralesForm" property="observaciones" styleId="observaciones" onkeydown="cuenta(this,4000)" onChange="cuenta(this,4000)" styleClass="box" value="<%=observaciones%>" readonly="false"
											style="overflow-y:auto;overflow-x:hidden;width:350px;height:50px;resize:none;"/>
<%
									}
%>
								</td>
<%
								if (ClsConstants.esConsejoITCGAE(idInstitucion)) {
%>
									<td id="titulo" class="labelText"><siga:Idioma key="facturacion.datosGenerales.literal.facturacion.datosGenerales.literal.planificarPosteriormente"/></td>
									<td>
<%
										if (!bEditable) {
%>
											<siga:ComboBD nombre = "idSerieFacturacionPrevia" tipo="cmbSerieFacturacionPrevias" elementoSel ="<%=aSerieSeleccionada%>" clase="boxCombo" ancho="280" parametro="<%=datoSerie%>" readonly="true"/>
<%
										} else { 
%>
											<siga:ComboBD nombre = "idSerieFacturacionPrevia" tipo="cmbSerieFacturacionPrevias" elementoSel ="<%=aSerieSeleccionada%>" clase="boxCombo" ancho="280" parametro="<%=datoSerie%>"/>
<%
										}
%>									
									</td>
<% 
								} 
%>									
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>

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

			<tr border="1">
				<td style="width:58%" border ="1">		
					<siga:ConjCampos leyenda="facturacion.serios.literal.contador">
						<table align="left" border="0">
							<tr>
								<td class="labelText" style="width:35%">
									<siga:Idioma key="facturacion.datosGenerales.literal.nombreContador"/>&nbsp;
								</td>
								<td  class="labelTextValue" >
									<html:text name="DatosGeneralesForm" property="idContador" styleId="idContador" size="20" maxlength="20" styleClass="boxConsulta" value="<%=idContador%>" readonly="true"/>
								</td>
							</tr>
							
							<tr>
								<td class="labelText" >
									<siga:Idioma key="facturacion.datosGenerales.literal.contadorGenerico"/>&nbsp;
								</td>
								<td  class="labelTextValue" >
									<html:text name="DatosGeneralesForm" styleId="prefijo" property="prefijo"  size="8" maxlength="10" styleClass="box" value="<%=prefijo%>" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="contador" property="contador"  size="15" maxlength="15" styleClass="box" value="<%=contador%>" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="sufijo" property="sufijo"  size="8" maxlength="10" styleClass="box" value="<%=sufijo%>" disabled="true"/>
								</td>
							</tr>
							
							<tr>
<% 
								if (accion.equals("ver")) { 
%>
									<td class="labelText" style="width:39%">
								 		&nbsp;
									</td>
									<td  class="labelTextValue">
										<input type="hidden" name="configurarContador" value="off"/>
									</td>
<% 
								} else { 
%>
									<td class="labelText" style="width:39%">
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
									<html:text name="DatosGeneralesForm" styleId="prefijo_nuevo" property="prefijo_nuevo" size="8" maxlength="10" styleClass="box" value="" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="contador_nuevo" property="contador_nuevo" size="15" maxlength="15" styleClass="box" value="" disabled="true"/>
									<html:text name="DatosGeneralesForm" styleId="sufijo_nuevo" property="sufijo_nuevo" size="8" maxlength="10" styleClass="box" value="" disabled="true"/>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
				
				<td style="width:45%">
					<siga:ConjCampos leyenda="facturacion.serios.literal.formaPago">
						<table align="center" border="0">
							<tr>
								<td width="60%" class="labelText"><siga:Idioma key="facturacion.serios.literal.formaPagoSeleccionar" />:</td>
								<td width="40%" class="labelText" align="right">
<%
									if (aFormasPago.isEmpty()) {
										if (accion != "ver") {
%>
											<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=0%>" obligatorio="true"/>
<%
										} else {
%>
											<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=0%>" obligatorio="true" readOnly="true" />
<%
										}
									} else { 
										if (accion != "ver") {
%>
											<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=aFormasPago%>" obligatorio="true"/>
<%
										} else {
%>
											<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=aFormasPago%>" obligatorio="true" readOnly="true" />
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

<% 
		if (accion.equals("nuevo")) {  
%>
			<table class="tablaCentralCampos">
				<tr>
					<td width="100%">
						<siga:ConjCampos leyenda="facturacion.serios.literal.formaPago">
							<table width="100%" align="center" border="0">
								<tr>
									<td width="60%" class="labelText" align="left">
										<siga:Idioma key="facturacion.serios.literal.formaPagoSeleccionar" />:  
									</td>
									<td width="40%" class="labelText" align="right">
										<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=0%>" obligatorio="true"/>
									</td> 
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>
			</table>	
<%
		}
%>		

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