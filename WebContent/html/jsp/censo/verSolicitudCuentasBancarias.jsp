<!DOCTYPE html>
<html>
<head>
<!-- verSolicitudCuentasBancarias.jsp -->

<!-- 
	 Permite mostrar las solicitudes de modificacion de cuentas bancarias
	 VERSIONES:
	 miguel.villegas 26-01-2005 
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
<%@ page import = "com.siga.beans.CenSolicModiCuentasBean"%>
<%@ page import = "com.siga.beans.CenCuentasBancariasBean"%>
<%@ page import = "com.siga.beans.CenPoblacionesBean"%>
<%@ page import = "com.siga.beans.CenProvinciaBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Enumeration"%>
<!-- JSP -->
<% 
	// Declaraciones varias
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	Row modificada=new Row();
	Hashtable original= new Hashtable();
	
	// Obtencion de los valores originales de la direccion solicitada para modificar
	if (request.getAttribute("container_desc") != null){
		Enumeration enumSel = ((Vector)request.getAttribute("container_desc")).elements();
		while (enumSel.hasMoreElements())
		{
			original = (Hashtable) enumSel.nextElement();
		} 
	}

	// Obtencion de los valores modificados de la direccion solicitada
	if (request.getAttribute("container") != null){
		Enumeration enumSel = ((Vector)request.getAttribute("container")).elements();
		while (enumSel.hasMoreElements())
		{
			modificada = (Row) enumSel.nextElement();
		} 
	}	

	// Creo un par de ArrayList con los valores de los codigos de bancos a mostrar
	ArrayList bancoOrig=new ArrayList();	
	bancoOrig.add(String.valueOf(original.get(CenCuentasBancariasBean.C_CBO_CODIGO)));	
	ArrayList bancoDest=new ArrayList();	
	bancoDest.add(modificada.getString(CenSolicModiCuentasBean.C_CBO_CODIGO));		
	
	// RGG 14/03/2007 cambio para dar un tama�o a la letra y en caso de Tiems darle otro
	String fontSize = "13px";

	
%>



	<!-- HEAD -->
	

		<style type="text/css">


		</style>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">

			//Asociada al boton Cerrar
			function accionCerrar(){ 			
				window.top.close();
			}	

			//Asociada a la carga de la pagina
			function originalModificado(estado){ 			
				if (estado!="10"){
					var mensaje='<siga:Idioma key="messages.censo.solicitudesModificacion.advertencia"/>';
					alert(mensaje);
				}
			}
			
			jQuery(function($){		
				var defaultValue = jQuery("#IBANO").val();
				if(defaultValue.length <= 34){
					jQuery('#IBANO').show();
				}else{
					jQuery('#IBANO').hide();
				
				}
				jQuery("#IBANO").mask("AA AA AAAA AAAA AAAA AAAA AAAA AAAA AAAA AA");
				jQuery("#IBANO").keyup();	
			});				
			
			
			jQuery(function($){		
				var defaultValue = jQuery("#IBANM").val();
				if(defaultValue.length <= 34){
					jQuery('#IBANM').show();
				}else{
					jQuery('#IBANM').hide();
				
				}
				jQuery("#IBANM").mask("AA AA AAAA AAAA AAAA AAAA AAAA AAAA AAAA AA");
				jQuery("#IBANM").keyup();	
			});				
			
		</script>	

	</head>
	<body onLoad="originalModificado(<%=modificada.getString(CenSolicModiCuentasBean.C_IDESTADOSOLIC)%>)" class="tablaCentralCampos">
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="titulitosDatos" cellspacing="0">
			<tr>
				<td id="titulo" class="titulos">
					<siga:Idioma key="censo.solicitudModificacion.literal.titulo"/>
				</td>
			</tr>
		</table>

		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

		<div id="camposRegistro" class="tablaCentralCampos" align="center">

			<!-- INICIO: CAMPOS -->
			<table class="tablaCentralCamposGrande" align="center" >				
				<html:form action="/CEN_SolicitudesModificacionEspecificas.do" method="POST" target="submitArea">
					<html:hidden property = "modo" value = ""/>
					<html:hidden property = "idPersona" value =""/>
					<html:hidden property = "idInstitucion" value =""/>
					<html:hidden property = "solicitudes"/>
					<tr>				
						<td>
							<siga:ConjCampos leyenda="censo.consultaDatosBancarios.cabecera">
								<table class="tablaCampos" align="center">
									<tr>		
										<td width="15%" class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.titular"/>&nbsp;
										</td>
										<td width="35%">
											<html:text property="titularO" value="<%=String.valueOf(original.get(CenCuentasBancariasBean.C_TITULAR))%>" styleClass="boxConsulta" readOnly="true" size="50" /><br>
											<html:text property="titularM" value="<%=modificada.getString(CenSolicModiCuentasBean.C_TITULAR)%>" styleClass="boxConsultaRojo" readOnly="true" size="50" />
										</td>		
														
										<td width="15%" class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.abonoSJCS"/>&nbsp;
										</td>
										<td width="35%" class="labelTextNormal">									
											<% if (String.valueOf(original.get(CenCuentasBancariasBean.C_ABONOSJCS)).equalsIgnoreCase("1")){%>
					   							<siga:Idioma key="general.yes"/>
						   					<% } else { %>
					   							<siga:Idioma key="general.no"/>
						   					<% } %>						   					
						   					<br>
						   					<div class="labelTextRojo">
												<% if (modificada.getString(CenSolicModiCuentasBean.C_ABONOSJCS).equalsIgnoreCase("1")){%>
						   							<siga:Idioma key="general.yes"/>
							   					<% } else { %>
						   							<siga:Idioma key="general.no"/>
							   					<% } %>
							   				</div>
										</td>
									</tr>	

									<tr>
										<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.tipoCuenta"/>&nbsp;
										</td>	
										<td class="labelTextNormal">
<% 
											String sAbonoCargoOriginal = String.valueOf(original.get(CenCuentasBancariasBean.C_ABONOCARGO));
											if (sAbonoCargoOriginal.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_ABONO)) {
%>
					   							<siga:Idioma key="censo.tipoCuenta.abono"/>
<% 
											} else if (sAbonoCargoOriginal.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_CARGO)) { 
%>
						   						<siga:Idioma key="censo.tipoCuenta.cargo"/>
<% 
											} else if (sAbonoCargoOriginal.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_ABONO_CARGO)) { 
%>
												<siga:Idioma key="censo.tipoCuenta.abonoCargo"/>
<% 
											} 
%>							   					
						   					<br>
						   					<div class="labelTextRojo">
<% 
												String sAbonoCargoModificada = modificada.getString(CenSolicModiCuentasBean.C_ABONOCARGO);
												if (sAbonoCargoModificada.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_ABONO)) {
%>
					   								<siga:Idioma key="censo.tipoCuenta.abono"/>
<% 
												} else if (sAbonoCargoModificada.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_CARGO)) { 
%>
						   							<siga:Idioma key="censo.tipoCuenta.cargo"/>
<% 
												} else if (sAbonoCargoModificada.equalsIgnoreCase(ClsConstants.TIPO_CUENTA_ABONO_CARGO)) { 
%>
													<siga:Idioma key="censo.tipoCuenta.abonoCargo"/>
<% 
												} 
%>	
							   				</div>
										</td>		
															
										<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.banco"/>&nbsp;					
										</td>
										<td>
					   						<siga:ComboBD nombre="bancoO" tipo="cmbBancos" clase="boxConsulta" elementoSel="<%=bancoOrig%>" readOnly="true"/><br>
					   						<siga:ComboBD nombre="bancoM" tipo="cmbBancos" clase="boxComboRojo" elementoSel="<%=bancoDest%>" readOnly="true"/>					   						
										</td>
									</tr>
									
					  				<tr>
					   					<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.codigoIBAN"/>&nbsp;
										</td>				
					   					<td>
											<html:text styleId="IBANO" property="IBANO" value='<%=String.valueOf(original.get(CenCuentasBancariasBean.C_IBAN))%>' styleClass="boxConsulta" readOnly="true" size="36"/><br>
											<html:text styleId="IBANM" property="IBANM" value='<%=modificada.getString(CenSolicModiCuentasBean.C_IBAN)%>' styleClass="boxConsultaRojo" readOnly="true" size="36"/>
										</td>
									</tr>										

					  				<tr>
					   					<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.codigoBanco"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="cboO" value='<%=String.valueOf(original.get(CenCuentasBancariasBean.C_CBO_CODIGO))%>' styleClass="boxConsulta" readOnly="true" /><br>
											<html:text property="cboM" value='<%=modificada.getString(CenSolicModiCuentasBean.C_CBO_CODIGO)%>' styleClass="boxConsultaRojo" readOnly="true" />
										</td>
										
					   					<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.codigoSucursal"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="sucO" value="<%=String.valueOf(original.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL))%>" styleClass="boxConsulta" readOnly="true" /><br>
											<html:text property="sucM" value="<%=modificada.getString(CenSolicModiCuentasBean.C_CODIGOSUCURSAL)%>" styleClass="boxConsultaRojo" readOnly="true" />
					   					</td>			   	
					  				</tr>
					  				
									<tr>
					   					<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.digitoControl"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="controlO" value="<%=String.valueOf(original.get(CenCuentasBancariasBean.C_DIGITOCONTROL))%>" styleClass="boxConsulta" readOnly="true" /><br>
											<html:text property="controlM" value="<%=modificada.getString(CenSolicModiCuentasBean.C_DIGITOCONTROL)%>" styleClass="boxConsultaRojo" readOnly="true" />
					   					</td>	
					   					
					   					<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.cuenta"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="cuentaO" value="<%=String.valueOf(original.get(CenCuentasBancariasBean.C_NUMEROCUENTA))%>" styleClass="boxConsulta" readOnly="true" /><br>
											<html:text property="cuentaM" value="<%=modificada.getString(CenSolicModiCuentasBean.C_NUMEROCUENTA)%>" styleClass="boxConsultaRojo" readOnly="true" />
					   					</td>	
					 				</tr>
				   				</table>
							</siga:ConjCampos>
							
							<siga:ConjCampos leyenda="censo.datosCuentaBancaria.literal.motivo">
								<table class="tablaCampos" align="center" border="0">														
					  				<tr>
					   					<td width="10%" class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.motivo"/>&nbsp;
										</td>											
					   					<td width="90%">
											<html:textarea property="motivo" 
												style="overflow-y:auto; overflow-x:hidden; width:880px; height:80px; resize:none;"
												value="<%=modificada.getString(CenSolicModiCuentasBean.C_MOTIVO)%>" 
												styleClass="boxConsulta" readOnly="true"></html:textarea>
										</td>		   					
					  				</tr>		  			 			 
				   				</table>
							</siga:ConjCampos>
						</td>
					</tr>
				</html:form>
			</table>
			<!-- FIN: CAMPOS -->
		</div>
		
			<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->	
			
			<siga:ConjBotonesAccion botones="C" modal="G" clase="botonesDetalle"/>
			
			<!-- FIN: BOTONES REGISTRO -->	

			<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las p�ginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
