<!DOCTYPE html>
<html>
<head>
<!-- datosGenerales.jsp -->
<!-- VENTANA DE DETALLE DE UN REGISTRO -->
<!-- Contiene un posible titulo del mantenimiento, ademas de la zona de campos
     a mantener, que utilizara conjuntos de datos si fuera necesario.
     Bajo esta zona y sin salirse del tamanho estandar de la ventana existira
     una zona de botones de acciones sobre el registro.
     VERSIONES:
	yolanda.garcia 22-12-2004 Creación
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.ArrayList"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.productos.form.MantenimientoProductosForm"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<!-- JSP -->
<%
	// Variables generales
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");		
	String idInstitucion = user.getLocation();
	
	// Obteniendo datos de la request
	Vector datosSerie = (Vector)request.getAttribute("datosSerie");
	Vector datosContador = (Vector)request.getAttribute("datosContador"); 
	Vector datosPlantilla = (Vector)request.getAttribute("datosPlantilla");
	request.removeAttribute("datosSerie");
	request.removeAttribute("datosPlantilla");	
	
	String editable = (String)ses.getAttribute("editable");
	boolean bEditable = (editable!=null && editable.equals("1"));
	
	String accion = (String)ses.getAttribute("accion");
	
	// Obteniendo datos de la serie para mostrarlos
	String sAbreviatura="";
	String sDescripcion="";
	Integer iPlantilla=new Integer(-1);
	String sPlantilla="",idSeriePrevia="";
	String idSerieFacturacion="";
	String enviarFacturas = "";
	String generarPDF = "";
	String observaciones="";

	
	// Valores para combos setElement
	ArrayList vPlantilla = new ArrayList(); // valor original Plantilla
	String idTipoEnvioCorreoElectronico = ""+EnvEnviosAdm.TIPO_CORREO_ELECTRONICO;
	String parametrosCmbPlantillaEnvios[] = {user.getLocation(),idTipoEnvioCorreoElectronico,"-1"};
	String parametrosPlantillasMail [] = {"-1",user.getLocation(),"1"};
	ArrayList plantillaEnviosSeleccionada = new ArrayList();
	ArrayList plantillaSeleccionada = new ArrayList();
	ArrayList vSerieSeleccionada = new ArrayList();

	if (datosSerie!=null && datosSerie.size()>0) {
		FacSerieFacturacionBean beanSerie = (FacSerieFacturacionBean)datosSerie.elementAt(0);
		
		sAbreviatura = beanSerie.getNombreAbreviado();
		sDescripcion = beanSerie.getDescripcion();
		iPlantilla = beanSerie.getIdPlantilla();
		idSerieFacturacion = String.valueOf(beanSerie.getIdSerieFacturacion());
		enviarFacturas = beanSerie.getEnvioFactura();
		generarPDF = beanSerie.getGenerarPDF();
		observaciones = beanSerie.getObservaciones();
		if(beanSerie.getIdSerieFacturacionPrevia()!=null){
			idSeriePrevia = beanSerie.getIdSerieFacturacionPrevia().toString();
			vSerieSeleccionada.add(idSeriePrevia.toString());				
		}
		
		if (datosPlantilla!=null && datosPlantilla.size()>0) {
			FacPlantillaFacturacionBean beanPlantilla = (FacPlantillaFacturacionBean)datosPlantilla.elementAt(0);
			sPlantilla = beanPlantilla.getDescripcion();
			
			if(beanSerie.getIdTipoPlantillaMail() != null && !beanSerie.getIdTipoPlantillaMail().equals("")){	
				plantillaEnviosSeleccionada.add(beanSerie.getIdTipoPlantillaMail()+","+user.getLocation() +",1");
				parametrosCmbPlantillaEnvios[2] = beanSerie.getIdTipoPlantillaMail().toString();
			}
		}	
	
	}

	// Contadores
	String idContador="";
	String prefijo="";
	String contador="";
	String sufijo="";
	String prefijo_nuevo="";
	String contador_nuevo="";
	String sufijo_nuevo="";
	
	// datos seleccionados Combo
	ArrayList contadorSel = new ArrayList();
	if (datosContador!=null && datosContador.size()>0) {
		AdmContadorBean beanContador = (AdmContadorBean)datosContador.elementAt(0);
		contadorSel.add(beanContador.getIdContador());
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
	Enumeration enumTemp = null;
	int haySec = 0;
	Row rowTemp;
	ArrayList vSec = new ArrayList(); // valores originales formas pago automática
	
	if ((accion.equalsIgnoreCase("editar"))||(accion.equalsIgnoreCase("ver"))){
		enumTemp = ((Vector) request.getAttribute("container_S")).elements();
		if (enumTemp.hasMoreElements()) {
			haySec = 1;
			while (enumTemp.hasMoreElements()) {
				rowTemp = (Row) enumTemp.nextElement();
				vSec.add(rowTemp.getString(FacSerieFacturacionBean.C_IDFORMAPAGO));
			}
		}
	}
	
	//En modo consulta se deshabilita el combo
	boolean combodeshabilitado =false;
	if (accion.equals("ver"))
		combodeshabilitado=true;
%>



	<!-- HEAD -->
	

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="DatosGeneralesForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="facturacion.asignacionConceptos.datosGenerales.cabecera" 
			localizacion="facturacion.datosGenerales.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->


		<script language="JavaScript">

			var resetComboPagoSecretaria;
		
			function actualiza() 
			{
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

			function configuraContador() 
			{
				//var con = document.getElementById("conta")
				var con = document.getElementById("conta")
				var con2 = document.getElementById("conta2")
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

			
		</script>
	</head>

	<body  class="tablaCentralCampos" onLoad="configuraContador();actualiza();">
	
		<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->

		<!-- INICIO: CAMPOS DEL REGISTRO -->

		<!-- Comienzo del formulario con los campos -->
		<table class="tablaCentralCampos">
			<html:form action="/FAC_DatosGenerales.do" method="POST" focus="nombreAbreviado" target="submitArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
				<html:hidden property="idSerieFacturacion" value="<%=idSerieFacturacion%>"/>
				<html:hidden property="accion" value="<%=accion%>"/>
				<html:hidden property="ids" value=""/>
				<tr>
					<td style="width:100%">		
						<!-- SUBCONJUNTO DE DATOS -->
						<!-- Conjunto de campos recuadrado y con titulo -->
						<siga:ConjCampos leyenda="facturacion.serios.literal.seriesFacturacion">
							<table align="center" border="0">

								<tr>
									<td class="labelText" width="10%" style="text-align:left" >
										<siga:Idioma key="facturacion.datosGenerales.literal.nombreAbreviado"/>&nbsp;(*)
									</td>
									<td>
										<%if (!bEditable){%>
											<html:text name="DatosGeneralesForm" styleId="nombreAbreviado"  property="nombreAbreviado" size="20" maxlength="20" styleClass="boxConsulta" value="<%=sAbreviatura%>" readonly="true"></html:text>
										<%} else {%>
											<html:text name="DatosGeneralesForm" styleId="nombreAbreviado"  property="nombreAbreviado"  size="20" maxlength="20" styleClass="boxMayuscula" value="<%=sAbreviatura%>" readonly="false"></html:text>
										<%}%>
									</td>
								</tr>
													
								<tr> 
        								<td class="labelText" width="10%" style="text-align:left" >
										<siga:Idioma key="facturacion.datosGenerales.literal.descripcion"/>&nbsp;(*)
									</td>
									<td>
										<%if (!bEditable){%>
											<html:text name="DatosGeneralesForm"  styleId="descripcion"  property="descripcion" size="100" maxlength="100" styleClass="boxConsulta" value="<%=sDescripcion%>" readonly="true"></html:text>
										<%} else {%>
											<html:text name="DatosGeneralesForm" styleId="descripcion"  property="descripcion" size="100" maxlength="100" styleClass="boxCombo" value="<%=sDescripcion%>" readonly="false"></html:text>
										<%}%>
									</td>
								</tr>
														
								<tr> 
        								<td class="labelText" width="10%" style="text-align:left" >
										<siga:Idioma key="facturacion.datosGenerales.literal.plantilla"/>&nbsp;(*)
									</td>
									<td>
										<%if (!bEditable){%>
											<html:text name="DatosGeneralesForm" styleId="plantilla"  property="plantilla" size="100" maxlength="100" styleClass="boxComboConsulta" value="<%=sPlantilla%>" readonly="true"></html:text>
										<%} else {
											String dato[] = new String[1];
											dato[0] = idInstitucion;
											vPlantilla.add(iPlantilla.toString());
										%>
											<siga:ComboBD nombre = "idPlantilla" tipo="cmbPlantilla" elementoSel ="<%=vPlantilla%>" clase="boxCombo" parametro="<%=dato%>"/>
										<%}%>
									</td>
								</tr>
								</table>
								<table border="0">
									<tr>
										<td class="labelText" style="text-align:left" colspan="4">
											<siga:Idioma key="facturacion.datosGenerales.literal.generaPDF"/>&nbsp;&nbsp;
											<% if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) { %>
													<input type="checkbox"  id="generarPDF"  name="generarPDF" checked disabled>
											<% } else if ((generarPDF != null) && (generarPDF.equals("1"))) { %>
													<input type="checkbox" id="generarPDF" name="generarPDF" <%=(accion.equals("ver"))?"disabled":"" %> checked>
											<% } else { %>
													<input type="checkbox"  id="generarPDF" name="generarPDF" <%=(accion.equals("ver"))?"disabled":"" %> >
											<% } %>
													&nbsp;&nbsp;&nbsp;
											<siga:Idioma key="facturacion.datosGenerales.literal.envioFacturas"/>&nbsp;&nbsp;
											<% if ((enviarFacturas != null) && (enviarFacturas.equals("1"))) { %>
													<input type="checkbox" name="envioFacturas"  id="envioFacturas"  onclick="actualiza();" <%=(accion.equals("ver"))?"disabled":"" %> checked>
											<% } else { %>
													<input type="checkbox" name="envioFacturas"  id="envioFacturas"  onclick="actualiza();" <%=(accion.equals("ver"))?"disabled":"" %> >
											<% } %>
										</td>
										
										<td id="titulo" class="labelText">
											<siga:Idioma key="envios.plantillas.literal.plantilla"/> 
										</td>
										<td>
											<siga:ComboBD  nombre = "idTipoPlantillaMail" tipo="cmbPlantillaEnvios3" clase="boxCombo" elementoSel="<%=plantillaEnviosSeleccionada%>" ancho="280" obligatorio="false" pestana="true" parametro="<%=parametrosCmbPlantillaEnvios%>" />
										</td>									
									</tr>

								<tr>
								    <td class="labelText" width="10%" style="text-align:left" >
										<siga:Idioma key="facturacion.datosGenerales.literal.observaciones"/>
									</td>
									<td colspan="3">
										<%if (!bEditable){%>
											<html:textarea name="DatosGeneralesForm" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" styleId="observaciones" property="observaciones" style="width:350px;"  rows="3" onkeydown="cuenta(this,4000);" styleClass="boxConsulta" value="<%=observaciones%>" readonly="true"/>
										<%} else {%>
											<html:textarea name="DatosGeneralesForm" property="observaciones"  styleId="observaciones"  style="width:350px;"  rows="3" onkeydown="cuenta(this,4000)" onChange="cuenta(this,4000)" styleClass="box" value="<%=observaciones%>" readonly="false"/>
										<%}%>
									</td>
									<%if (ClsConstants.esConsejoGeneral(idInstitucion)){%>
										<td id="titulo" class="labelText">
											Planificar posteriormente a
										</td>
										<td>
											<%if (!bEditable){%>
												<siga:ComboBD nombre = "idSerieFacturacionPrevia" tipo="cmbSerieFacturacionPrevias" elementoSel ="<%=vSerieSeleccionada%>" clase="boxCombo" ancho="280" parametro="<%=datoSerie%>" readonly="true"/>
											<%} else { %>
												<siga:ComboBD nombre = "idSerieFacturacionPrevia" tipo="cmbSerieFacturacionPrevias" elementoSel ="<%=vSerieSeleccionada%>" clase="boxCombo" ancho="280" parametro="<%=datoSerie%>"/>
											<%}%>									
										</td>
									<% } %>									
								</tr>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>
		</table>
		<!-- FIN: CAMPOS DEL REGISTRO -->

		<% if (accion.equals("nuevo")) {  %>
		<table class="tablaCentralCampos"style="display:none" >
		
		<% } else {  %>
		<table class="tablaCentralCampos" border="0">
		
		<% }  %>

				<tr border="1">
					<td style="width:58%" border ="1">		
	
						<siga:ConjCampos leyenda="facturacion.serios.literal.contador">

							<table align="left" border="0">
								<tr>
									<td class="labelText" style="width:35%">
										<siga:Idioma key="facturacion.datosGenerales.literal.nombreContador"/>&nbsp;
									</td>
									<td  class="labelTextValue" >
										<html:text name="DatosGeneralesForm" property="idContador" styleId="idContador" size="20" maxlength="20" styleClass="boxConsulta" value="<%=idContador%>" readonly="true"></html:text>
									</td>
									</tr>
									<tr>
									<td class="labelText" >
										<siga:Idioma key="facturacion.datosGenerales.literal.contadorGenerico"/>&nbsp;
									</td>
									<td  class="labelTextValue" >
										<html:text name="DatosGeneralesForm" styleId="prefijo" property="prefijo"  size="8" maxlength="10" styleClass="box" value="<%=prefijo%>" disabled="true"></html:text>
										<html:text name="DatosGeneralesForm" styleId="contador" property="contador"  size="15" maxlength="15" styleClass="box" value="<%=contador%>" disabled="true"></html:text>
										<html:text name="DatosGeneralesForm" styleId="sufijo" property="sufijo"  size="8" maxlength="10" styleClass="box" value="<%=sufijo%>"  disabled="true"></html:text>
									</td>
								</tr>
								<tr>
								<% if (accion.equals("ver")) { %>
									<!--  <input type="hidden" name="configurarContador" value="off"/> -->
									<td class="labelText" style="width:39%">
									 	&nbsp;
									</td>
									<td  class="labelTextValue">
										<input type="hidden" name="configurarContador" value="off"/>
									</td>
								<% } else { %>
									<td class="labelText" style="width:39%">
										<siga:Idioma key="facturacion.datosGenerales.literal.configurarContador"/>&nbsp;
									</td>
									<td  class="labelTextValue">
										<input type="checkbox" name="configurarContador" id="configurarContador" onclick="configuraContador();"/>
									</td>
								<% }  %>																
								</tr>
								<tr id="conta">								
									<td class="labelText">
										<siga:Idioma key="facturacion.datosGenerales.literal.existentes"/>&nbsp;
									</td>
									<td class="labelText">
										<siga:ComboBD nombre="contadorExistente" tipo="cmbContadorFacturacion" parametro="<%=parametro %>" clase="boxCombo" obligatorio="false" elementoSel="<%=contadorSel %>" accion="comprobarNuevo()" />
									</td>
									</tr>
								<tr id="conta2">	
									<td class="labelText" >
										<siga:Idioma key="facturacion.datosGenerales.literal.nuevoContador"/>&nbsp;
									</td>
									<td  class="labelTextValue">
										<html:text name="DatosGeneralesForm" styleId="prefijo_nuevo" property="prefijo_nuevo" size="8" maxlength="10" styleClass="box" value="<%=prefijo_nuevo%>" disabled="true"></html:text>
										<html:text name="DatosGeneralesForm" styleId="contador_nuevo" property="contador_nuevo" size="15" maxlength="15" styleClass="box" value="<%=contador_nuevo%>" disabled="true"></html:text>
										<html:text name="DatosGeneralesForm" styleId="sufijo_nuevo" property="sufijo_nuevo" size="8" maxlength="10" styleClass="box" value="<%=sufijo_nuevo%>" disabled="true"></html:text>
									</td>
								</tr>
						
							</table>

						</siga:ConjCampos>
					</td>
			<td style="width:45%"><!-- SUBCONJUNTO DE DATOS --> <!-- Conjunto de campos recuadrado y con titulo -->
			<siga:ConjCampos leyenda="facturacion.serios.literal.formaPago">
				<table align="center" border="0">
					<tr>
						<td width="60%" class="labelText">
							<siga:Idioma key="facturacion.serios.literal.formaPagoSeleccionar" />:  
						</td>
						<td width="40%" class="labelText" align="right">
						<%
							if (! vSec.isEmpty()) {
			 					ArrayList elementoSel = new ArrayList();
			 					for (int kk = 0; kk < vSec.size(); kk++) {
			 						elementoSel.add(vSec.get(kk));
			 					}
							}
			 					
							if (vSec.isEmpty()) {
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
							<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=vSec%>" obligatorio="true"/>
						<%
								} else {
						%>
							<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=vSec%>" obligatorio="true" readOnly="true" />
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

<% if (accion.equals("nuevo")) {  %>
	<table class="tablaCentralCampos" >
		<tr>
			<td style="width:100%">
				<siga:ConjCampos leyenda="facturacion.serios.literal.formaPago">
					<table align="center" border="0">
						<tr>
							<td width="60%" class="labelText" align="left">
								<siga:Idioma key="facturacion.serios.literal.formaPagoSeleccionar" />:  
							</td>
							<td width="40%" class="labelText" align="right">
								<siga:ComboBD nombre="formaPagoAutomática" tipo="cmbFormaPagoAutomaticoSerie" clase="boxCombo" filasMostrar="4" seleccionMultiple="true" elementoSel="<%=0%>" obligatorio="true"/>
								<br/>
							</td> 
						</tr>
					</table>
				</siga:ConjCampos>
			</td>
		</tr>
	</table>	
<%} %>		

			</html:form>


		<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- Aqui comienza la zona de botones de acciones -->

		<!-- INICIO: BOTONES REGISTRO -->
		<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		-->
			<%if (!bEditable){%>
				<siga:ConjBotonesAccion botones="V" />
			<%} else {%>
				<siga:ConjBotonesAccion botones="V,G,R" />
			<%}%>
		<!-- FIN: BOTONES REGISTRO -->
	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
	
			//Asociada al boton Volver
			function accionVolver() 
			{
				document.forms[0].action = "<%=app%>/FAC_AsignacionConceptosFacturables.do";	
				document.forms[0].target = "mainWorkArea";
				document.forms[0].modo.value = "abrirAvanzada";
				document.forms[0].submit();				
			}

			//Asociada al boton Restablecer
			function accionRestablecer() 
			{		
				document.forms[0].reset();
			}
		
			//Asociada al boton Guardar
			function accionGuardar() 
			{
				if (validateDatosGeneralesForm(document.DatosGeneralesForm)){		
					if(confirm('<siga:Idioma key="messages.confirm.updateData"/>')) 
					{
						if (document.forms[0].accion.value=="editar")
						{
							document.forms[0].modo.value="modificar";
						}
						else if (document.forms[0].accion.value=="nuevo")
						{
							document.forms[0].target="submitArea";
							document.forms[0].modo.value="insertar";
						}	
										
						var datos = "";
						var chk = document.getElementsByName("chk");
						if (chk !="checkbox") {
							for (i = 0; i < chk.length; i++){
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
						   if (chk.checked==1){
								datos=datos + chk.value + "%";						
							}	
						}
						if (datos=="") {
							alert('<siga:Idioma key="Facturacion.mensajes.obligatoriaCuenta"/>');
							return false;
						}
						
						// Validacion de contadores
						if (document.forms[0].configurarContador.checked==1 &&
							document.forms[0].contadorExistente.value=="") {
							
							
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
				
				for (i = 0; i < chk.length; i++){
					if (chk[i].checked==1){
						jQuery("#comboSufijos_" + chk[i].value).val(jQuery("#idsufijodefBanco_" + chk[i].value).val());
					}else{
						jQuery("#comboSufijos_" + chk[i].value).val("");
					}	
				}		
			}

		</script>
		<!-- FIN: SCRIPTS BOTONES -->

		<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


		<!-- INICIO: LISTA RESULTADOS -->


			<% //if ((accion.equalsIgnoreCase("editar"))||(accion.equalsIgnoreCase("edicion"))||(accion.equalsIgnoreCase("insertar"))||(accion.equalsIgnoreCase("modificar"))){ %>
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
	    		if (request.getAttribute("bancosInstitucion") == null || ((Vector)request.getAttribute("bancosInstitucion")).size() < 1 )
		    	{
				%>
					<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
				<%
		    	}	    
			    else
		    	{ 
		    		Enumeration en = ((Vector)request.getAttribute("bancosInstitucion")).elements();					
					int recordNumber=1;
					while (en.hasMoreElements())
					{
	            		Row row = (Row) en.nextElement(); 
	            		String sel = row.getString("SELECCIONADO");
	            		boolean bsel=false;
	            		if ((sel!=null && !sel.equals("0"))&&(!accion.equals("nuevo"))) {
	            			bsel=true;
	            		}
	            		
	            		
	            		String idComboSuf= "comboSufijos_" + row.getString("BANCOS_CODIGO");
	            		String  idsufijodefBanco = "idsufijodefBanco_" + row.getString("BANCOS_CODIGO");
	            		String idsufijoBancoIni=row.getString("IDSUFIJO");

					%>
	            		
						<siga:FilaConIconos
							  fila='<%=String.valueOf(recordNumber)%>'
							  botones=''
							  modo='<%=accion%>'							  
							  visibleConsulta='no'
							  visibleEdicion='no'
							  visibleBorrado='no'
							  clase="listaNonEdit"
							  pintarespacio='no'>
						  		
								
							<td>
								<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_1" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString("IDINSTITUCION")%>">
								<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_2" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString("COD_BANCO")%>">
								<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_3" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString("IDSERIEFACTURACION")%>">
								<input type="radio" onclick="recargarCombo(this)" value="<%=row.getString("BANCOS_CODIGO")%>"  id="oculto<%=String.valueOf(recordNumber)%>_4" name="chk" <%=(bsel)?"checked":"" %> <%=(accion.equals("ver"))?"disabled":"" %> >
							</td>  	
							<td>
								<%=UtilidadesString.mostrarDatoJSP(row.getString("BANCO"))%>							
							</td>  	
							<td align="left">
								<%=UtilidadesString.mostrarIBANConAsteriscos(row.getString("IBAN"))%>							
							</td>  	
							<td align="right">
							<input type="hidden" id="<%=idsufijodefBanco%>" value="<%=idsufijoBancoIni%>">
							<bean:define id="listaSufijos" name="listaSufijos" scope="request"/>
								<html:select styleId="<%=idComboSuf%>" name = "comboSufijos" property="idSufijo" value="<%=idsufijoBancoIni%>" styleClass="boxCombo" disabled="<%=combodeshabilitado%>" style="width:200px;">
								<html:option value=""><c:out value=""/></html:option> 	
								<c:forEach items="${listaSufijos}" var="sufijoSerieCmb">												
									<html:option value="${sufijoSerieCmb.idSufijo}">										
										<c:if	test="${sufijoSerieCmb.sufijo.trim().length()>0}">
											<c:out value="${sufijoSerieCmb.sufijo} ${sufijoSerieCmb.concepto}"/>
										</c:if>
										<c:if	test="${sufijoSerieCmb.sufijo.trim().length()==0}">
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<c:out value="${sufijoSerieCmb.concepto}"/>
										</c:if>
									</html:option>	
								</c:forEach>
								</html:select>							
							</td>  	
							<td align="right">
								<%=UtilidadesString.formatoImporte(row.getString("COMISIONIMPORTE"))%>&nbsp;&euro;							
							</td>  															
							<td align="right">
								<%=row.getString("USO")%>							
							</td>  	
						</siga:FilaConIconos>
					<% 
					recordNumber++;
					} 
				} %>
			</siga:Table>												

	
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>

</html>
