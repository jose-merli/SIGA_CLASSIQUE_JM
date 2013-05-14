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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
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
	
	// RGG 14/03/2007 cambio para dar un tamaño a la letra y en caso de Tiems darle otro
	String fontSize = "13px";
	if (((String)src.get("font.style")).indexOf("Times")!=-1) {
		fontSize="15px";
	}
	
%>

<html>

	<!-- HEAD -->
	<head>

		<style type="text/css">
			.labelTextRojo{
						text-align: left; font-family: <%=src.get("font.style")%>;
						font-size: <%=fontSize%>; 	font-weight: bold; 
						margin: auto; color:#6A0000; 
						padding-right: 17px; padding-top: 3px;
						padding-bottom: 3px; vertical-align: top;}	
						
			.labelTextNormal{
						text-align: left; font-family: <%=src.get("font.style")%>;
						font-size: <%=fontSize%>; vertical-align: top; 
						margin: auto; color:#<%=src.get("color.labelText")%>; 
						padding-right: 17px; padding-top: 3px;
						padding-bottom: 3px; padding-left: 5px;}							
						
			.boxConsultaRojo {
						font-family: <%=src.get("font.style")%>; font-size: <%=fontSize%>;
						font-weight: bold; margin: auto;
						padding-left: 5px; vertical-align: top;
						text-align: left; padding-top: 3px;
						padding-bottom: 3px; border:none;
						background-color:transparent; color:#6A0000;}						  
						
			.boxComboNormal {
						font-family: <%=src.get("font.style")%>; font-size: <%=fontSize%>;
						margin: auto; color:#<%=src.get("color.labelText")%>;
						padding-left: 5px; vertical-align: top;
						text-align: left; padding-top: 3px;
						padding-bottom: 3px; border:none;
						background-color:transparent;}

			.boxComboRojo {
						font-family: <%=src.get("font.style")%>; font-size: <%=fontSize%>;
						font-weight: bold; margin: auto;
						padding-left: 5px; vertical-align: top;
						text-align: left; padding-top: 3px;
						padding-bottom: 3px; border:none;
						background-color:transparent; color:#6A0000;}

		</style>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
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
		
		</script>	

	</head>
	<body onLoad="originalModificado(<%=modificada.getString(CenSolicModiCuentasBean.C_IDESTADOSOLIC)%>)" class="tablaCentralCampos">
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	
		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="titulitosDatos" cellspacing="0" heigth="32">
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
									<!-- FILA -->
									<tr>		
										<td width="15%" class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.titular"/>&nbsp;
										</td>
										<td width="35%">
											<html:text property="titularO" value="<%=String.valueOf(original.get(CenCuentasBancariasBean.C_TITULAR))%>" styleClass="boxConsulta" readOnly="true" size="50"></html:text><br>
											<html:text property="titularM" value="<%=modificada.getString(CenSolicModiCuentasBean.C_TITULAR)%>" styleClass="boxConsultaRojo" readOnly="true" size="50"></html:text>
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
									<!-- FILA -->
									<tr>
										<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.tipoCuenta"/>&nbsp;
										</td>	
										<td class="labelTextNormal">
											<% if (String.valueOf(original.get(CenCuentasBancariasBean.C_ABONOCARGO)).equalsIgnoreCase("A")){%>
					   							<siga:Idioma key="censo.tipoCuenta.abono"/>
						   					<% } else { %>
						   						<% if (String.valueOf(original.get(CenCuentasBancariasBean.C_ABONOCARGO)).equalsIgnoreCase("C")){ %>
						   							<siga:Idioma key="censo.tipoCuenta.cargo"/>
						   						<% } else { %>
						   							<siga:Idioma key="censo.tipoCuenta.abonoCargo"/>
						   						<% } %>	
						   					<% } %>						   					
						   					<br>
						   					<div class="labelTextRojo">
												<% if (modificada.getString(CenSolicModiCuentasBean.C_ABONOCARGO).equalsIgnoreCase("A")){%>
						   							<siga:Idioma key="censo.tipoCuenta.abono"/>
							   					<% } else { %>
							   						<% if (modificada.getString(CenSolicModiCuentasBean.C_ABONOCARGO).equalsIgnoreCase("C")){ %>
							   							<siga:Idioma key="censo.tipoCuenta.cargo"/>
							   						<% } else { %>
							   							<siga:Idioma key="censo.tipoCuenta.abonoCargo"/>
							   						<% } %>	
							   					<% } %>
							   				</div>
										</td>							
										<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.banco"/>&nbsp;					
										</td>
										<td>
					   						<siga:ComboBD nombre="bancoO" tipo="cmbBancos" clase="boxComboNormal" elementoSel="<%=bancoOrig%>" readOnly="true"/><br>
					   						<siga:ComboBD nombre="bancoM" tipo="cmbBancos" clase="boxComboRojo" elementoSel="<%=bancoDest%>" readOnly="true"/>					   						
										</td>
									</tr>
					   				<!-- FILA -->
					  				<tr>
					   					<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.codigoBanco"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="cboO" value='<%=String.valueOf(original.get(CenCuentasBancariasBean.C_CBO_CODIGO))%>' styleClass="boxConsulta" readOnly="true"></html:text><br>
											<html:text property="cboM" value='<%=modificada.getString(CenSolicModiCuentasBean.C_CBO_CODIGO)%>' styleClass="boxConsultaRojo" readOnly="true"></html:text>
										</td>
					   					<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.codigoSucursal"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="sucO" value="<%=String.valueOf(original.get(CenCuentasBancariasBean.C_CODIGOSUCURSAL))%>" styleClass="boxConsulta" readOnly="true"></html:text><br>
											<html:text property="sucM" value="<%=modificada.getString(CenSolicModiCuentasBean.C_CODIGOSUCURSAL)%>" styleClass="boxConsultaRojo" readOnly="true"></html:text>
					   					</td>			   	
					  				</tr>
					   				<!-- FILA -->
									<tr>
					   					<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.digitoControl"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="controlO" value="<%=String.valueOf(original.get(CenCuentasBancariasBean.C_DIGITOCONTROL))%>" styleClass="boxConsulta" readOnly="true"></html:text><br>
											<html:text property="controlM" value="<%=modificada.getString(CenSolicModiCuentasBean.C_DIGITOCONTROL)%>" styleClass="boxConsultaRojo" readOnly="true"></html:text>
					   					</td>	
					   					<td class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.cuenta"/>&nbsp;
										</td>				
					   					<td>
											<html:text property="cuentaO" value="<%=String.valueOf(original.get(CenCuentasBancariasBean.C_NUMEROCUENTA))%>" styleClass="boxConsulta" readOnly="true"></html:text><br>
											<html:text property="cuentaM" value="<%=modificada.getString(CenSolicModiCuentasBean.C_NUMEROCUENTA)%>" styleClass="boxConsultaRojo" readOnly="true"></html:text>
					   					</td>	
					 				</tr>
				   				</table>
							</siga:ConjCampos>
							<!-- MOTIVO -->
							<siga:ConjCampos leyenda="censo.datosCuentaBancaria.literal.motivo">
								<table class="tablaCampos" align="center">														
					  				<tr>
					   					<td width="10%" class="labelText">
											<siga:Idioma key="censo.datosCuentaBancaria.literal.motivo"/>&nbsp;
										</td>											
					   					<td width="90%">
											<html:textarea property="motivo" value="<%=modificada.getString(CenSolicModiCuentasBean.C_MOTIVO)%>" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" styleClass="boxConsulta" readOnly="true" size="80" rows="5"></html:textarea>
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
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
