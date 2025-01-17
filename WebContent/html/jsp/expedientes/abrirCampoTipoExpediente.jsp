<!DOCTYPE html>
<html>
<head>
<!-- abrirCampoTipoExpediente.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants,com.atos.utils.*,com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.expedientes.form.CampoTipoExpedienteForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	String nombreImagen = app + "/html/imagenes/botonAyuda.gif"; 
		
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	String tipoacceso=user.getAccessType();
	boolean bLectura = tipoacceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_READ);
	String estiloCaja="box";
	String estiloCajaNumerico="boxNumber";
	if (bLectura) {
		estiloCaja="boxConsulta";
		estiloCajaNumerico="boxConsultaNumber";
	}
	request.removeAttribute("datos");	
	CampoTipoExpedienteForm form1 = (CampoTipoExpedienteForm) request.getAttribute("camposForm");
	String idTipoEnvioCorreoElectronico = ""+EnvTipoEnviosAdm.K_CORREO_ELECTRONICO;
	String parametrosCmbPlantillaEnvios2[] = {user.getLocation(),idTipoEnvioCorreoElectronico,"-1"};
	ArrayList plantillaEnviosSeleccionada = new ArrayList();
	ArrayList plantillaSeleccionada = new ArrayList();
	if(form1.getEnviarAvisos().equals("1")){
		plantillaEnviosSeleccionada.add(form1.getIdPlantillaEnvios()+","+user.getLocation() +","+ form1.getIdTipoEnvios());
		parametrosCmbPlantillaEnvios2[2] = form1.getIdPlantillaEnvios();
		
		if(form1.getIdPlantilla()!=null && !form1.getIdPlantilla().equals("")){
			plantillaSeleccionada.add(form1.getIdPlantilla());
		}		
	}
	
	String idinstitucion = user.getLocation();	
    String datoTipoExp[] = new String[2];
    Object[] aPerfiles = new Object[2];
	aPerfiles[0] = user.getProfile();
	aPerfiles[1] = user.getProfile();
	datoTipoExp[0] = idinstitucion;
	datoTipoExp[1] = idinstitucion;
	ArrayList comboRelacionExpe = new ArrayList();
	comboRelacionExpe.add(form1.getComboTipoExpediente());
%>	

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="camposForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">		
	
		// Refresco: esta funci�n es llamada desde exito.jsp tras mostrar el mensaje de �xito
		function refrescarLocal() {		
			// var elemento=parent.document.getElementById('pestana.tiposexpedientes.campos');
			// parent.pulsar(elemento,'mainPestanas');
			document.TipoExpedienteForm.modo.value = 'editar';
			document.TipoExpedienteForm.submit();
		}
	
		function cambiarDenunciado(){
			if(document.camposForm.nombreCampoDenunciante.value == '<%=ExpCampoTipoExpedienteBean.DENUNCIANTE%>')
				document.camposForm.nombreCampoDenunciado.value = '<%=ExpCampoTipoExpedienteBean.DENUNCIADO%>' ;
			else
				document.camposForm.nombreCampoDenunciado.value = '<%=ExpCampoTipoExpedienteBean.IMPUGNADO%>' ;
		}
			
		function cambiarDenunciante(){
			if(document.camposForm.nombreCampoDenunciado.value == '<%=ExpCampoTipoExpedienteBean.DENUNCIADO%>')
				document.camposForm.nombreCampoDenunciante.value = '<%=ExpCampoTipoExpedienteBean.DENUNCIANTE%>' ;
			else
				document.camposForm.nombreCampoDenunciante.value = '<%=ExpCampoTipoExpedienteBean.IMPUGNANTE%>' ;
		}
			
		// Asociada al boton Volver
		function accionVolver() {		
			top.frames["mainWorkArea"].location.href='<%=app%>/Dispatcher.do?proceso=41';
		}
			
		// Asociada al boton Restablecer
		function accionRestablecer() {		
			var elemento=parent.document.getElementById('pestana.tiposexpedientes.campos');
			parent.pulsar(elemento,'mainPestanas')				
		}
	
		// Asociada al boton Guardar
		function accionGuardar() {
			sub();		
			if (document.getElementById("chkPestanaConf1").checked && trim(document.getElementById("pestanaConf1").value)=='') {
				alert('<siga:Idioma key="pestana.tiposexpedientes.campos.avisoNombrePestana"/>');
				fin();
				inicioPestana();
				return false;
			}
			
			if (document.getElementById("chkPestanaConf2").checked && trim(document.getElementById("pestanaConf2").value)=='') {
				alert('<siga:Idioma key="pestana.tiposexpedientes.campos.avisoNombrePestana"/>');
				fin();
				inicioPestana();
				return false;
			}
			
			if (document.getElementById("checkEnviarAvisos").checked){
				document.camposForm.enviarAvisos.value = "1";
				if (document.getElementById("comboPlantillaEnvio").value=='') {
					error = "<siga:Idioma key='errors.required' arg0='envios.definir.literal.plantillageneracion'/>";
					alert(error);
					fin();
					return false;
				}
			} else {
				document.camposForm.enviarAvisos.value = "0";					
			}
			
			if (document.getElementById("comboPlantillaEnvio").value!='') {
				var idPlantillaEnvio=document.getElementById("comboPlantillaEnvio").value.split(",")[0];
				document.camposForm.idPlantillaEnvios.value = idPlantillaEnvio;	
			} else {
				document.camposForm.idPlantillaEnvios.value = "";
			}
			
			if (document.getElementById("idPlantillaGeneracion").value!='' ) {
				document.camposForm.idPlantilla.value = document.getElementById("idPlantillaGeneracion").value;	
			} else {
				document.camposForm.idPlantilla.value = "";
			}
				
			if(document.getElementById("relacionExpediente").checked && document.getElementById("comboTipoExpediente").value == ''){
				alert('<siga:Idioma key="pestana.tiposexpedientes.campos.avisoRelacionExpe"/>');
				fin();
				inicioPestana();
				return false;
			}
				
			if (validateCamposForm(document.camposForm)) {
				if (document.getElementById("estado").checked!=false) {
					alert('<siga:Idioma key="pestana.tiposexpedientes.campos.aviso"/>');
				}
				camposForm.submit();
			} else {
				fin();
			}
		}

			var bLectura = <%=""+bLectura %>;
			function inicioPestana() {
				
				var chk1=document.getElementById("chkPestanaConf1");
				var caja1=document.getElementById("pestanaConf1");
				var boton1=document.getElementById("botonPestana1");
				if (chk1.checked && !bLectura) {
					caja1.readonly=false;
					jQuery("#botonPestana1").removeAttr("disabled");
					caja1.className="box";
				} else {
					caja1.readonly=true;
					jQuery("#botonPestana1").attr("disabled","disabled");
					caja1.className="boxConsulta";
				}
				
				if (bLectura) jQuery("#botonPestana1").removeAttr("disabled");
				
				var chk2=document.getElementById("chkPestanaConf2");
				var caja2=document.getElementById("pestanaConf2");
				var boton2=document.getElementById("botonPestana2");
				if (chk2.checked  && !bLectura) {
					caja2.readonly=false;
					jQuery("#botonPestana2").removeAttr("disabled");
					caja2.className="box";
				} else {
					caja2.readonly=true;
					jQuery("#botonPestana2").attr("disabled","disabled");
					caja2.className="boxConsulta";
				}
				
				if (bLectura)	jQuery("#botonPestana2").removeAttr("disabled"); 
				if (document.camposForm.enviarAvisos.value == "1"){
					document.getElementById("checkEnviarAvisos").checked = "checked";
					jQuery("#comboPlantillaEnvio").removeAttr("disabled");
					document.getElementById("comboPlantillaEnvio").onchange();
					jQuery("#idPlantillaGeneracion").removeAttr("disabled");
				}else{
					document.getElementById("checkEnviarAvisos").checked = "";
					jQuery("#comboPlantillaEnvio").attr("disabled","disabled");
					jQuery("#idPlantillaGeneracion").attr("disabled","disabled");
				}
			}

			function pestana1(obj) {
				var caja1=document.getElementById("pestanaConf1");
				var boton1=document.getElementById("botonPestana1");
				if (obj.checked) {
					caja1.readonly=false;
					jQuery("#botonPestana1").removeAttr("disabled");
					caja1.className="box";
				} else {
					caja1.readonly=true;
					jQuery("#botonPestana1").attr("disabled","disabled");
					caja1.className="boxConsulta";
				}
			}
			
			function pestana2(obj) {
				var caja2=document.getElementById("pestanaConf2");
				var boton2=document.getElementById("botonPestana2");
				if (obj.checked) {
					caja2.readonly=false;
					jQuery("#botonPestana2").removeAttr("disabled");
					caja2.className="box";
				} else {
					caja2.readonly=true;
					jQuery("#botonPestana2").attr("disabled","disabled");
					caja2.className="boxConsulta";
				}
			}
			
			function editarCampos(numPestana) {		
				document.camposConfigurablesForm.modo.value = "abrir";
				if (bLectura) {
					document.camposConfigurablesForm.accion.value = "ver";
				} else {
					document.camposConfigurablesForm.accion.value = "editar";
				}
				document.camposConfigurablesForm.idPestanaConf.value = numPestana;
				if (numPestana==1) {
					document.camposConfigurablesForm.idCampo.value = "14";
				} else {
					document.camposConfigurablesForm.idCampo.value = "15";
				}
	 			var resultado=ventaModalGeneral(document.camposConfigurablesForm.name,"M");
			}
			
			function onclickCheckEnviarAvisos() {
				if (document.getElementById("checkEnviarAvisos").checked==true){
					jQuery("#idPlantillaGeneracion").removeAttr("disabled");
					jQuery("#comboPlantillaEnvio").removeAttr("disabled");
					document.getElementById("comboPlantillaEnvio").value = "";
					document.getElementById("comboPlantillaEnvio").onchange();
					//document.getElementById("idPlantillaGeneracion").value = "";
					
				} else {
					//document.getElementById("idPlantillaGeneracion").value = "";
					document.getElementById("comboPlantillaEnvio").value = "";
					document.getElementById("comboPlantillaEnvio").onchange();
				   	jQuery("#comboPlantillaEnvio").attr("disabled","disabled");
				   	jQuery("#idPlantillaGeneracion").attr("disabled","disabled");
					
				}

			}
			
			function faviso() {
				 if (document.getElementById("estado").checked!=false) {
					 document.getElementById("aviso").style.display="block";
				 } else {
					 document.getElementById("aviso").style.display="none";
				}
			}
			
			function onclickCheckAbrirExp() {
				if(document.getElementById("relacionExpediente").checked){
					jQuery("#comboTipoExpediente").removeAttr("disabled");
					document.getElementById("comboTipoExpediente").value = "";
				} else {
				   	jQuery("#comboTipoExpediente").attr("disabled","disabled");
					document.getElementById("comboTipoExpediente").value = "";	
				}					
			}			

			function comprobarEJG() {
			    document.getElementById("modificarEJG").value="1";	
				document.camposForm.submit();
		    }	   
		</script>
		
		<!-- FIN: SCRIPTS BOTONES -->
		
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
		<siga:Titulo titulo="expedientes.tipoExpedientes.configuracion.campos.cabecera" localizacion="expedientes.tipoExpedientes.configuracion.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
		
	</head>
	
	<body onLoad="faviso();inicioPestana();">
	
	<div id="camposRegistro" >
		<html:form  action="/EXP_TiposExpedientes_Campos.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = "modificar"/>
			<html:hidden property = "hiddenFrame" value = "1"/>
			<html:hidden property = "actionModal" value = ""/>
			<html:hidden property = "idTipoExpediente"/>
			<html:hidden property = "idCampo" value=""/>
			<html:hidden property = "enviarAvisos" />
			<html:hidden property = "idTipoEnvios" value="<%=idTipoEnvioCorreoElectronico%>"/>
			<html:hidden property = "idPlantillaEnvios"/>
			<html:hidden property = "idPlantilla"/>
			<html:hidden property = "modificarEJG"/>
			
				
			<table class="tablaTitulo" align="center"  cellspacing="0">
				<tr>
					<td id="titulo" class="titulitosDatos">
						<siga:Idioma key="expedientes.auditoria.literal.tipo"/> :&nbsp;	
						<% if (bLectura){ %>
						<html:text name="camposForm" property="nombre" size="40" maxlength="30" styleClass="boxConsulta" readonly="true"></html:text>
						<%} else {%>					
						<html:text name="camposForm" property="nombre" size="40" maxlength="30" styleClass="box" ></html:text>
					<%}%>
					</td>
				</tr>
			</table>	
		
			<table align="center" id="aviso" style="display:none">
				<tr>
					<td class="labelText" width="100%" colspan="2" >
						<siga:Idioma key="pestana.tiposexpedientes.campos.aviso"/>
					</td>
				</tr>
			</table>
						
			<table class="tablaCampos" align="center" cellspacing="0">
				<tr>
					<td width="50%" valign="top">
					
					<!-- Tabla de Campos -->
						<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.campos">							
						<table align="center" cellspacing="0">
							<%--<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.fecha"/> : 				    
								</td>
								<td>
									<html:checkbox name="camposForm" property="fecha" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>--%>
							<tr>
								<td id="titulo" class="labelText">
									<html:select  name="camposForm" property="nombreCampoNumExp" styleClass="boxCombo">
											<html:option value="<%=ExpCampoTipoExpedienteBean.NUMEXPEDIENTE%>" key="<%=ExpCampoTipoExpedienteBean.NUMEXPEDIENTE%>"></html:option>
											<html:option value="<%=ExpCampoTipoExpedienteBean.NUMEJG%>" key="<%=ExpCampoTipoExpedienteBean.NUMEJG%>"></html:option>
											<html:option value="<%=ExpCampoTipoExpedienteBean.NUMPREVIAS%>" key="<%=ExpCampoTipoExpedienteBean.NUMPREVIAS%>"></html:option>
									</html:select>
								</td>
								<td>
									<html:checkbox name="camposForm" property="nexpDisciplinario" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.estadoTipoExpediente"/> : 				    
								</td>
								<td>
									<html:checkbox name="camposForm" property="estado" onclick="faviso();" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr style="display=none;">
								<!-- No vale para nada asi que mejor no mostrarlo, tampoco lo quito no vaya a ser ... -->
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.institucion"/> : 				    
								</td>
								<td>
									<html:checkbox name="camposForm" property="institucion" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.asuntojudicial"/> : 				    
								</td>
								<td>
									<html:checkbox name="camposForm" property="asuntoJudicial" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>


							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.minuta"/> :
								</td>
								<td>
									<html:checkbox name="camposForm" property="minutaInicial" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.minutafinal"/> :
								</td>
								<td>
									<html:checkbox name="camposForm" property="minutaFinal" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.derechoscolegiales"/> :
								</td>
								<td>
									<html:checkbox name="camposForm" property="derechos" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.resultadoInforme"/> :
								</td>
								<td>
									<html:checkbox name="camposForm" property="resultadoInforme" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
						<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.auditoria.literal.solicitanteEJG"/> :
								</td>
								<td>
									<html:checkbox name="camposForm" property="solicitanteEJG" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
						</table>
						</siga:ConjCampos>
					</td>					
					<td width="50%" valign="top">
					
				<!-- Tabla de Pestanhas -->
						<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.pestanas">							
						<table align="center" cellspacing="0">
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="pestana.auditoriaexp.alertas"/> : 				    
								</td>
								<td>
									<html:checkbox name="camposForm" property="alertas" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="pestana.auditoriaexp.documentacion"/> : 				    
								</td>
								<td>
									<html:checkbox name="camposForm" property="documentacion" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="pestana.auditoriaexp.seguimiento"/> : 				    
								</td>
								<td>
									<html:checkbox name="camposForm" property="seguimiento" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<html:select onchange="cambiarDenunciado()" name="camposForm" property="nombreCampoDenunciante" styleClass="boxCombo">
											<html:option value="<%=ExpCampoTipoExpedienteBean.DENUNCIANTE%>" key="<%=ExpCampoTipoExpedienteBean.DENUNCIANTE%>"></html:option>
											<html:option value="<%=ExpCampoTipoExpedienteBean.IMPUGNANTE%>" key="<%=ExpCampoTipoExpedienteBean.IMPUGNANTE%>"></html:option>
									</html:select>
									 : 				    
								</td>
								<td>
									<html:checkbox name="camposForm" property="denunciantes" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
								<html:select onchange="cambiarDenunciante()" name="camposForm" property="nombreCampoDenunciado" styleClass="boxCombo">
											<html:option value="<%=ExpCampoTipoExpedienteBean.DENUNCIADO%>" key="<%=ExpCampoTipoExpedienteBean.DENUNCIADO%>"></html:option>
											<html:option value="<%=ExpCampoTipoExpedienteBean.IMPUGNADO%>" key="<%=ExpCampoTipoExpedienteBean.IMPUGNADO%>"></html:option>
									</html:select>
									 : 				    
								</td>
								<td>
									<html:checkbox name="camposForm" property="denunciados" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="pestana.auditoriaexp.partes"/> : 				    
								</td>
								<td>
									<html:checkbox name="camposForm" property="partes" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="pestana.auditoriaexp.resolucion"/> : 				    
								</td>
								<td>
									<html:checkbox name="camposForm" property="resolucion" value="true" disabled="<%=bLectura%>"/>
								</td>
							</tr>
						</table>
						</siga:ConjCampos>
					</td>
				</tr>
				<tr>
					<td>
						<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.caducidad">							
						<table align="center" cellspacing="0">
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.tiposexpedientes.literal.tiempoCaducidad"/> :
								</td>
								<td>
									<html:text name="camposForm" property="tiempoCaducidad" styleClass="<%=estiloCajaNumerico%>" readonly="<%=bLectura%>" size="6" maxlength="6" />
									<siga:ToolTip id='imagenTiempoCaducidad' imagen='<%=nombreImagen%>' texto='<%=UtilidadesString.mostrarDatoJSP(UtilidadesString.getMensajeIdioma (user, "expedientes.tiposexpedientes.literal.notaTiempos"))%>' />
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.tiposexpedientes.literal.diasAntelacion"/> :
								</td>
								<td>
									<html:text name="camposForm" property="diasAntelacionCad" styleClass="<%=estiloCajaNumerico%>" readonly="<%=bLectura%>" size="6" maxlength="3" />
								</td>
							</tr>
						</table>
						</siga:ConjCampos>		
					</td>
					<td>
						<%
							String mensajeBoton = UtilidadesString.getMensajeIdioma(user,"pestana.tiposexpedientes.campos.editarCampos");
							if (bLectura) mensajeBoton=UtilidadesString.getMensajeIdioma(user,"pestana.tiposexpedientes.campos.consultarCampos");
						%>

						<siga:ConjCampos leyenda="expedientes.tiposexpedientes.literal.pestanasconfigurables">							
						<table align="center" cellspacing="0">
							<tr>
								<td id="titulo" class="labelText">
									<html:checkbox name="camposForm" property="chkPestanaConf1" value="1" disabled="<%=bLectura%>" onClick="pestana1(this);" />
								</td>
								<td style="vertical-align:bottom">
									<html:text name="camposForm" property="pestanaConf1" styleClass="<%=estiloCaja%>" readonly="<%=bLectura%>"  maxlength="25" />
								</td>
								<td>
									<input type="button" id="botonPestana1" class="button" value="<%=mensajeBoton%>" disabled="<%=bLectura%>" onClick="editarCampos(1);"  />
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<html:checkbox name="camposForm" property="chkPestanaConf2" value="1" disabled="<%=bLectura%>" onClick="pestana2(this);"/>
								</td>
								<td style="vertical-align:bottom">
									<html:text name="camposForm" property="pestanaConf2" styleClass="<%=estiloCaja%>" readonly="<%=bLectura%>"  maxlength="25" />
								</td>
								<td>
									<input type="button" id="botonPestana2" class="button" value="<%=mensajeBoton%>" disabled="<%=bLectura%>"  onClick="editarCampos(2);" />
								</td>
							</tr>
						</table>
						</siga:ConjCampos>						
					</td>
				</tr>
				<tr>
					<td>
						<table cellspacing="0">
							<tr>
								<td>
									<html:checkbox name="camposForm" property="relacionEJG" value="true" disabled="<%=bLectura%>"/>
								</td>
								<td id="titulo" class="labelText">
									<siga:Idioma key="expedientes.tiposexpedientes.literal.relacionEJG"/> 
								</td>
								
							</tr>
						</table>
					</td>
						<td>
						<table cellspacing="0">
								<tr>
									<td>
									<html:checkbox name="camposForm" property="relacionExpediente" disabled="<%=bLectura%>" onchange="onclickCheckAbrirExp();"/>
									</td>
									<td id="titulo" class="labelText">
										<siga:Idioma key="general.expediente.boton.crearnuevo"/> 
									</td>

									
									<td id="titulo" class="labelText">
										<siga:ComboBD nombre = "comboTipoExpediente" tipo="cmbTipoExpedienteLocaloGeneralPermisos"  elementoSel="<%=comboRelacionExpe%>" clase="boxCombo" obligatorio="false" parametro="<%=datoTipoExp%>" parametrosIn="<%=aPerfiles%>"/>
									</td>
								</tr>
						</table>
						</td>	
				</tr>			
				<tr>
					<td>
						<siga:ConjCampos leyenda="expedientes.tiposexpedientes.leyenda.avisos">							
						<table align="center" cellspacing="0">
						
							<tr>
								<td class="labelText" colspan="2">
									<siga:Idioma key="expedientes.tiposexpedientes.literal.enviarAvisos"/>
									<input type="checkbox" id="checkEnviarAvisos" onclick="onclickCheckEnviarAvisos();" />	
									 
								</td>
								
							</tr>
							<tr>
								<td class="labelText" colspan="2">
									<siga:Idioma key="expedientes.tiposexpedientes.aviso.insertarDestinatarios"/>
										
									 
								</td>
								
							</tr>
							
							<tr>
								<td id="titulo" class="labelText">
								<siga:Idioma key="envios.plantillas.literal.plantilla"/> 
									
								</td>
								<td>
									<siga:ComboBD nombre = "comboPlantillaEnvio"   tipo="cmbPlantillaEnvios3" clase="boxCombo" elementoSel="<%=plantillaEnviosSeleccionada%>" ancho="350" obligatorio="false" pestana="true" parametro="<%=parametrosCmbPlantillaEnvios2%>"  accion="Hijo:idPlantillaGeneracion"/>
									
								</td>
							</tr>
							<tr>
								<td id="titulo" class="labelText">
									<siga:Idioma key="envios.definir.literal.plantillageneracion"/>
								</td>
								<td>
									<siga:ComboBD nombre="idPlantillaGeneracion" tipo="cmbPlantillaGeneracion" clase="boxCombo" elementoSel="<%=plantillaSeleccionada%>" ancho="350" obligatorio="false" hijo="t" pestana="true"/>
								</td>
							</tr>
						</table>
						</siga:ConjCampos>		
					</td>
					<td>
										
					</td>
				</tr>			
			</table>
		</html:form>


		<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
		<%	if (tipoacceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_READ)){ %>
			<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
		<% } else{ %>
			<siga:ConjBotonesAccion botones="V,R,G" clase="botonesDetalle"  />
		<% } %>		
	</div>

	<html:form  action="/EXP_TiposExpedientes_CamposConf.do" method="POST" target="submitArea">
		<html:hidden property = "modo" value = "abrir"/>
		<html:hidden property = "accion" value = ""/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "idInstitucion" value = "<%=user.getLocation()%>"/>
		<html:hidden property = "idTipoExpediente" value = "<%=form1.getIdTipoExpediente()%>"/>
		<html:hidden property = "idCampo" value = ""/>
		<html:hidden property = "idPestanaConf" value = ""/>
		<html:hidden property = "nombre" value = ""/>
		<html:hidden property = "orden" value = ""/>
	</html:form>	
	
	<html:form action="/EXP_MantenerTiposExpedientes.do" method="POST" target="submitArea" style="display:none">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "hiddenFrame" value = "1"/>
		<html:hidden property = "idInstitucion" value = "<%=user.getLocation()%>"/>
		<html:hidden property = "idTipoExpediente" value = "<%=form1.getIdTipoExpediente()%>"/>
		<!-- RGG: cambio a formularios ligeros -->
	</html:form>		
		
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>
</html>