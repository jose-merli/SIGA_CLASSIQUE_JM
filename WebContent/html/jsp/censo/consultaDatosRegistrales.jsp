<!-- consultaDatosRegistrales.jsp -->


<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.DatosGeneralesForm"%>
<%@ page import="com.siga.beans.CenNoColegiadoBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.censo.form.DatosRegistralesForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>


<!-- JSP -->
<% 
	String app = null;
	HttpSession ses = null;
	Properties src = null;
	UsrBean user = null;
	DatosRegistralesForm formulario = null;
	boolean bOcultarHistorico = false;
	String modo = null;
	
	String estiloCaja="", estiloCombo="";
	String readonly = "false";  // para el combo
	boolean breadonly = false;  // para lo que no es combo
	String checkreadonly = " "; // para el check
	
	String numero=(String)request.getAttribute("numero");
	String nombre=(String)request.getAttribute("nombrePersona");
	String SSPP=(String)request.getAttribute("SSPP");
	String botones = "V";
	String busquedaVolver = (String) request.getSession().getAttribute("CenBusquedaClientesTipo");
	ArrayList actividadSel = new ArrayList();
	try{
		
		app=request.getContextPath();
		ses=request.getSession();
		src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
		user = (UsrBean) ses.getAttribute("USRBEAN");
		bOcultarHistorico = user.getOcultarHistorico();
		formulario = (DatosRegistralesForm)request.getAttribute("DatosRegistralesForm");
		String nif=formulario.getNumIdentificacion();
		modo = (String)request.getAttribute("accion");
		
		
		
	//  tratamiento de readonly
		estiloCaja = "";
		readonly = "false";  // para el combo
		breadonly = false;  // para lo que no es combo
		checkreadonly = " "; // para el check
		boolean breadonlyNif = false;
		String estiloCajaNif = "box";
		String readonlyComboNIFCIF = "false";
		// caso de accion 
		if (modo.equalsIgnoreCase("VER")) {
			// caso de consulta
			estiloCaja = "boxConsulta";
			estiloCombo = "boxConsulta";
			readonly = "true";
			breadonly = true;
			checkreadonly = " disabled ";
			
		} else {
			estiloCaja = "box";
			estiloCombo = "boxCombo";
			readonly = "false";
			breadonly = false;
			checkreadonly = " ";
			
		}
		
		
		// Gestion de Volver
		
		if (busquedaVolver == null) {
			busquedaVolver = "volverNo";
		}
		
		if (!busquedaVolver.equals("volverNo")) { 
			botones="V,G,R";
		}
		
	}
	
	catch (Exception e){
		e.printStackTrace();
	}
	

	
	

%>	

<html>

	<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>			
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="DatosRegistralesForm" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:TituloExt 
			titulo="pestana.fichaCliente.datosRegistrales" 
			localizacion="censo.fichaCliente.datosRegistrales.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->

		<script>
		function buscarGrupos(){
					document.ActividadProfesionalForm.modo.value="buscar";
					document.ActividadProfesionalForm.modoAnterior.value=document.forms[0].accion.value;				
					document.ActividadProfesionalForm.idPersona.value=document.forms[0].idPersona.value;	
					document.ActividadProfesionalForm.idInstitucion.value=document.forms[0].idInstitucion.value;	
					document.ActividadProfesionalForm.target="resultado";	
					document.ActividadProfesionalForm.submit();	
		}
			function obtenerNif() 
			{
			
			if (document.forms[0].numIdentificacion.value!="" ){
				document.getElementById("noobligatoriotipo").style.display="none";
				//document.getElementById("noobligatorionif").style.display="none";
				document.getElementById("noobligatorionombre").style.display="none";
				document.getElementById("noobligatorioapellido").style.display="none";
				document.getElementById("obligatoriotipo").style.display="block";
				//document.getElementById("obligatorionif").style.display="block";
				document.getElementById("obligatorionombre").style.display="block";
				document.getElementById("obligatorioapellido").style.display="block";
			}else{
				document.DatosRegistralesForm.nombre.disabled=false;
				document.DatosRegistralesForm.apellido1.disabled=false;
				document.DatosRegistralesForm.apellido2.disabled=false;
				document.forms[0].tipoIdentificacion.disabled=false;
				document.DatosRegistralesForm.nombre.value="";
				document.DatosRegistralesForm.apellido1.value="";
				document.DatosRegistralesForm.apellido2.value="";
				document.forms[0].numIdentificacion.value="";
				document.forms[0].tipoIdentificacion.value="";
			}
			
			if((document.forms[0].tipoIdentificacion.value== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>")&& (document.forms[0].numIdentificacion.value!="") ) {
			     var sNIF = document.forms[0].numIdentificacion.value;
			     document.forms[0].numIdentificacion.value = formateaNIF(sNIF);
				} 
		   	     var nif = (document.forms[0].numIdentificacion.value);
				
 				if (nif!="") {
					if (!validaNumeroIdentificacion()) {
						return false;
					}
			
			
					
				
					//LMSP En lugar de abrir la ventana modal, se manda al frame oculto, y éste se encarga de todo :)
					document.forms[0].modo.value="buscarNIF";
					document.forms[0].target="submitArea";
					document.forms[0].submit();
				}			
			}
			
			function validaNumeroIdentificacion () 
			{
				document.forms[0].numIdentificacion.value = (document.forms[0].numIdentificacion.value).toUpperCase();
				var a = (document.forms[0].numIdentificacion.value);
				if (document.forms[0].tipoIdentificacion!=undefined && (document.forms[0].tipoIdentificacion.value==<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>||document.forms[0].tipoIdentificacion.value==<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>)) {
				    if (!validarNIFCIF(document.forms[0].tipoIdentificacion.value,a)) {
						document.forms[0].numIdentificacion.focus();
						return false;
					}
				}
				return true;
			}
			
			
			function obligatorio(){
				
			 <%DatosRegistralesForm formularioaux = null;
			 formularioaux = (DatosRegistralesForm)request.getAttribute("DatosRegistralesForm");
			String nif=null;
			nif=formularioaux.getNumIdentificacion();
			
			 if (nif ==null || nif.equals("")) {%>
				document.getElementById("obligatoriotipo").style.display="none";
				//document.getElementById("obligatorionif").style.display="none";
				document.getElementById("obligatorionombre").style.display="none";
				document.getElementById("obligatorioapellido").style.display="none";
				document.getElementById("noobligatoriotipo").style.display="block";
				//document.getElementById("noobligatorionif").style.display="block";
				document.getElementById("noobligatorionombre").style.display="block";
				document.getElementById("noobligatorioapellido").style.display="block";
				document.forms[0].idPersonaNotario.value="";
			<%}	else { %>
				document.getElementById("obligatoriotipo").style.display="block";
				//document.getElementById("obligatorionif").style.display="block";
				document.getElementById("obligatorionombre").style.display="block";
				document.getElementById("obligatorioapellido").style.display="block";
				document.getElementById("noobligatoriotipo").style.display="none";
				//document.getElementById("noobligatorionif").style.display="none";
				document.getElementById("noobligatorionombre").style.display="none";
				document.getElementById("noobligatorioapellido").style.display="none";
				document.forms[0].nombre.disabled=true;
				document.forms[0].apellido1.disabled=true;
				document.forms[0].apellido2.disabled=true;
				document.forms[0].tipoIdentificacion.disabled=true;
			<%}%>
				
			}
			
			function nombre1(){
				if (document.forms[0].nombre.value!=""){
					document.getElementById("obligatoriotipo").style.display="block";
					//document.getElementById("obligatorionif").style.display="block";
					document.getElementById("obligatorionombre").style.display="block";
					document.getElementById("obligatorioapellido").style.display="block";
					document.getElementById("noobligatoriotipo").style.display="none";
					//document.getElementById("noobligatorionif").style.display="none";
					document.getElementById("noobligatorionombre").style.display="none";
					document.getElementById("noobligatorioapellido").style.display="none";
					
				}else{
					if (document.forms[0].numIdentificacion.value=="" && document.forms[0].apellido1.value=="" && document.forms[0].tipoIdentificacion.value=="" ){
						document.getElementById("obligatoriotipo").style.display="none";
						//document.getElementById("obligatorionif").style.display="none";
						document.getElementById("obligatorionombre").style.display="none";
						document.getElementById("obligatorioapellido").style.display="none";
						document.getElementById("noobligatoriotipo").style.display="block";
						//document.getElementById("noobligatorionif").style.display="block";
						document.getElementById("noobligatorionombre").style.display="block";
						document.getElementById("noobligatorioapellido").style.display="block";
						document.forms[0].idPersonaNotario.value="";
					}
				}
				
			}
			function apellidos(){
				if (document.forms[0].apellido1.value!=""){
					document.getElementById("obligatoriotipo").style.display="block";
					//document.getElementById("obligatorionif").style.display="block";
					document.getElementById("obligatorionombre").style.display="block";
					document.getElementById("obligatorioapellido").style.display="block";
					document.getElementById("noobligatoriotipo").style.display="none";
					//document.getElementById("noobligatorionif").style.display="none";
					document.getElementById("noobligatorionombre").style.display="none";
					document.getElementById("noobligatorioapellido").style.display="none";
				}else{
					if (document.forms[0].numIdentificacion.value=="" && document.forms[0].nombre.value=="" && document.forms[0].tipoIdentificacion.value=="" ){
						document.getElementById("obligatoriotipo").style.display="none";
						//document.getElementById("obligatorionif").style.display="none";
						document.getElementById("obligatorionombre").style.display="none";
						document.getElementById("obligatorioapellido").style.display="none";
						document.getElementById("noobligatoriotipo").style.display="block";
						//document.getElementById("noobligatorionif").style.display="block";
						document.getElementById("noobligatorionombre").style.display="block";
						document.getElementById("noobligatorioapellido").style.display="block";
						document.forms[0].idPersonaNotario.value="";
					}
				}
				
				
			}
			function tipo(){
				if (document.forms[0].tipoIdentificacion.value!=""){
					document.getElementById("obligatoriotipo").style.display="block";
					//document.getElementById("obligatorionif").style.display="block";
					document.getElementById("obligatorionombre").style.display="block";
					document.getElementById("obligatorioapellido").style.display="block";
					document.getElementById("noobligatoriotipo").style.display="none";
					//document.getElementById("noobligatorionif").style.display="none";
					document.getElementById("noobligatorionombre").style.display="none";
					document.getElementById("noobligatorioapellido").style.display="none";
				}else{
					if (document.forms[0].numIdentificacion.value=="" && document.forms[0].nombre.value=="" && document.forms[0].apellido1.value=="" ){
						document.getElementById("obligatoriotipo").style.display="none";
						//document.getElementById("obligatorionif").style.display="none";
						document.getElementById("obligatorionombre").style.display="none";
						document.getElementById("obligatorioapellido").style.display="none";
						document.getElementById("noobligatoriotipo").style.display="block";
						//document.getElementById("noobligatorionif").style.display="block";
						document.getElementById("noobligatorionombre").style.display="block";
						document.getElementById("noobligatorioapellido").style.display="block";
						document.forms[0].idPersonaNotario.value="";
					}
				}
			
			}
			
			
			
			
		</script>
	</head>

	<body class="tablaCentralCampos" onload="obligatorio();buscarGrupos()">


<html:form  action="/CEN_DatosRegistrales.do" method="POST" target="mainPestanas"  enctype="multipart/form-data">
<html:hidden  name="DatosRegistralesForm" property="modo"/>
<html:hidden  name="DatosRegistralesForm" property="idInstitucion" value="<%=user.getLocation()%>"/>
<html:hidden  name="DatosRegistralesForm" property="idPersona"/>
<html:hidden  name="DatosRegistralesForm" property="accion"/>
<html:hidden  name="DatosRegistralesForm" property="idPersonaNotario"/>

		<table class="tablaCentralCampos" align="center" cellspacing=0>
		
			
			<tr>				
				<td class="titulitosDatos">
					
					<siga:Idioma key="censo.consultaDatosRegistrales.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
	    			<%if(!numero.equalsIgnoreCase("")){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%} 
					else {%>
			  			 <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>		
				</td>					
			</tr>
			
		</table>

			<!-- CAMPOS DEL REGISTRO -->
			<table class="tablaCentralCampos" align="center">									

				<tr>				
					<td width="100%" align="center">

						<!-- DATOS DE CONSTITUCION -->
						<siga:ConjCampos leyenda="censo.datosRegistrales.literal.titulo2">
							<table CELLSPACING="3" border="0">
								<tr >
									<td class="labelText" width="142px">
									<!-- FECHA CONSTITUCION -->
										<siga:Idioma key="censo.general.literal.FechaConstitucion"/>&nbsp;<% if (SSPP.equals("1")){  %>(*)<%}%>
									</td>
									<td width="63%">
										
										<html:text name="DatosRegistralesForm" property="fechaConstitucion" size="10" styleClass="<%=estiloCaja %>" readonly="true"></html:text>
											&nbsp;
										<% if (!breadonly) { %>
										<a href='javascript://'onClick="return showCalendarGeneral(fechaConstitucion);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
										<% } %>
									</td>
									<!-- FECHA FIN -->
									<td class="labelText">
										<siga:Idioma key="censo.consultaDatosRegistrales.literal.fecha_fin"/>&nbsp;
									</td>
									<td colspan="0">
										<html:text name="DatosRegistralesForm" property="fecha_fin" size="10" styleClass="<%=estiloCaja %>" readonly="true"></html:text>
											&nbsp;
										<% if (!breadonly) { %>
										<a href='javascript://'onClick="return showCalendarGeneral(fecha_fin);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
										<% } %>
									</td>
								</tr>
								<tr CELLSPACING="3">
									<td class="labelText">
										<!-- RESEÑA -->
										<siga:Idioma key="censo.consultaDatosRegistrales.literal.Resenia"/>&nbsp;<% if (SSPP.equals("1")){  %>(*)<%}%>
									</td>
									<td colspan="3">
										<html:text name="DatosRegistralesForm" property="resena"  size="131" maxlength="1500" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text>
									</td>
								</tr>
								<tr>
								
									<td class="labelText" >
										
										<siga:Idioma key="censo.consultaDatosRegistrales.literal.objSocial"/>&nbsp;<% if (SSPP.equals("1")){  %>(*)<%}%>
									</td>
									<td colspan="1">
										<html:textarea cols="50"  rows="12" name="DatosRegistralesForm" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" property="objetoSocial" style="width:550px;" styleClass="<%=estiloCaja%>"></html:textarea>
									</td>
										<td  style="width:300px" colspan="2">
										
										<!-- INICIO: IFRAME LISTA RESULTADOS -->
											<iframe align="left" src="<%=app%>/html/jsp/general/blank.jsp"
															id="resultado"
															name="resultado" 
															scrolling="no"
															frameborder="1"
															marginheight="0"
															marginwidth="0";					 
															style="width:90%; height:100%;">
											</iframe>
											<!-- FIN: IFRAME LISTA RESULTADOS -->
										</td>
									</tr>
							</table>															
						</siga:ConjCampos>

					</td>
				</tr>
			
			</table>
			<!-- CAMPOS DEL SEGURO-->
			<table class="tablaCentralCampos" align="center">
				<tr>				
					<td width="100%" align="center">
						<siga:ConjCampos leyenda="censo.datosRegistrales.literal.seguro">
							<table border="0">
								<td class="labelText"  width="142">
									<siga:Idioma key="censo.consultaDatosRegistrales.literal.Poliza"/>&nbsp;
								</td>
								<td >
									<html:text name="DatosRegistralesForm" property="noPoliza"  size="20" maxlength="20" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="censo.consultaDatosRegistrales.literal.Compania"/>&nbsp;
								</td>
								<td >
									<html:text name="DatosRegistralesForm" property="companiaSeg"  size="70" maxlength="100" styleClass="<%=estiloCaja%>" readonly="<%=breadonly%>" ></html:text>
								</td>
							</table>
						</siga:ConjCampos>
					</td>
				</tr>	
			</table>

			<!-- CAMPOS DEL NOTARIO-->
			<table class="tablaCentralCampos"  border=0 >
				<tr>				
					<td>

						<!-- DATOS DEL NOTARIO -->
						<siga:ConjCampos leyenda="censo.datosRegistrales.literal.titulo3">
							<table  border=0>
								<tr>
									<% String CIF=String.valueOf(ClsConstants.TIPO_IDENTIFICACION_CIF);
									   String OTRO=String.valueOf(ClsConstants.TIPO_IDENTIFICACION_OTRO);
									%>
									<td id="noobligatoriotipo" class="labelText" >
									
										<!-- NUMERO IDENTIFICACION NIF/CIF -->
										<siga:Idioma key="censo.fichaCliente.literal.tipoIdentificacion"/>
										
									</td>
									<td id="obligatoriotipo" class="labelText" >
									
										<!-- NUMERO IDENTIFICACION NIF/CIF -->
										<siga:Idioma key="censo.fichaCliente.literal.tipoIdentificacion"/>&nbsp (*)
										
									</td>
									<%ArrayList tipoIdentificacionSel = new ArrayList();
									  tipoIdentificacionSel.add(request.getAttribute("tipoident"));
									 %>
									
									<td colspan="2">
										
										 <siga:ComboBD nombre = "tipoIdentificacion" tipo="cmbTipoIdentificacionSinCIF" clase="<%=estiloCaja%>" accion="tipo();" obligatorio="true" elementoSel="<%=tipoIdentificacionSel%>" readonly="false"/>
										
													
									
										<html:text name="DatosRegistralesForm" property="numIdentificacion" size="11"  onBlur="obtenerNif();" styleClass="<%=estiloCaja %>">
										</html:text>
									</td>
									
								</tr>
								<tr>
									<td id="noobligatorionombre" class="labelText">
										<siga:Idioma key="censo.consultaDatosGenerales.literal.nombre"/>&nbsp;
									</td>
									<td id="obligatorionombre" class="labelText">
										<siga:Idioma key="censo.consultaDatosGenerales.literal.nombre"/>&nbsp (*)
									</td>
									<td>
										<html:text name="DatosRegistralesForm" property="nombre" size="23"  onBlur="nombre1();" styleClass="<%=estiloCaja %>">
										</html:text>
									</td>
									<td id="noobligatorioapellido" class="labelText">
										<siga:Idioma key="censo.busquedaClientes.literal.apellido1"/>&nbsp;
									</td>
									<td id="obligatorioapellido" class="labelText">
										<siga:Idioma key="censo.busquedaClientes.literal.apellido1"/>&nbsp (*)
									</td>
									<td>
										<html:text name="DatosRegistralesForm" property="apellido1" size="23" onBlur="apellidos();" styleClass="<%=estiloCaja %>">
										</html:text>
									</td>
									<td class="labelText">
										<siga:Idioma key="censo.busquedaClientes.literal.apellido2"/>&nbsp;
									</td>
									<td>
										<html:text name="DatosRegistralesForm" property="apellido2" size="23" styleClass="<%=estiloCaja %>"></html:text>
									</td>
								</tr>
							</table>
																						
						</siga:ConjCampos>

					</td>
				</tr>
			</html:form>
			</table>

<siga:ConjBotonesAccion botones="<%=botones%>"    modo='<%=modo%>' clase="botonesDetalle"/>	
	<html:form action="/CEN_ActividadProfesional.do" method="POST" target="resultado">
		<html:hidden name="ActividadProfesionalForm" property="modo" value="buscar"/>
		<html:hidden name="ActividadProfesionalForm" property="idPersona" />
		<html:hidden name="ActividadProfesionalForm" property="idInstitucion" />
		<html:hidden name="ActividadProfesionalForm" property="modoAnterior" />
	</html:form>




	
<%@ include file="/html/jsp/censo/includeVolver.jspf" %>

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();	
		}
		
		<!-- Asociada al boton Guardar -->
		function accionGuardar() {
			sub();
				if (<%=SSPP%>=="1" && document.forms[0].resena.value!="" && document.forms[0].fechaConstitucion.value!="" && document.forms[0].objetoSocial.value!="") {
						if (document.getElementById("obligatoriotipo").style.display=="block"){
						  if (document.forms[0].tipoIdentificacion.value=="" || document.forms[0].numIdentificacion.value=="" || document.forms[0].nombre.value=="" || document.forms[0].apellido1.value==""){ 
							alert ("Introduzca los campos obligatorios");
							fin();
							return false;
						  }else{
						  	document.forms[0].modo.value="modificarRegistrales";
							document.forms[0].target="submitArea";
						
							document.forms[0].submit();	
						  }
						}else{	
							document.forms[0].modo.value="modificarRegistrales";
							document.forms[0].target="submitArea";
						
							document.forms[0].submit();		
						}
				}else{
					if (<%=SSPP%>=="0"){
						if (document.getElementById("obligatoriotipo").style.display=="block"){
						  if (document.forms[0].tipoIdentificacion.value=="" || document.forms[0].numIdentificacion.value=="" || document.forms[0].nombre.value=="" || document.forms[0].apellido1.value==""){ 
							alert ("Introduzca los campos obligatorios");
							fin();
							return false;
						  }else{
						  	document.forms[0].modo.value="modificarRegistrales";
							document.forms[0].target="submitArea";
						
							document.forms[0].submit();	
						  }
						}else{	
							document.forms[0].modo.value="modificarRegistrales";
							document.forms[0].target="submitArea";
						
							document.forms[0].submit();		
						}
					}
					else{
						alert ("Introduzca los campos obligatorios");
						fin();
						return false;	
					}
				}
				
		}
		function refrescarLocal() {
				
				document.forms[0].modo.value="abrir";
				
				document.forms[0].submit();

				
				
			}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<iframe name="submitArea2" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<iframe name="submitArea3" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
