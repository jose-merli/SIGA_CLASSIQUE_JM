<!-- abrirCampoTipoExpediente.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants,com.atos.utils.*,com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.expedientes.form.CampoTipoExpedienteForm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	String tipoacceso=user.getAccessType();
	boolean bLectura = tipoacceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_READ);
	String estiloCaja="box";
	if (bLectura) estiloCaja="boxCOnsulta";	
	request.removeAttribute("datos");	
	CampoTipoExpedienteForm form1 = (CampoTipoExpedienteForm) request.getAttribute("camposForm");

%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"> </script>
	
		<!-- Validaciones en Cliente -->
		<html:javascript formName="camposForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">		
	
			<!-- Refresco -->
			<!-- esta función es llamada desde exito.jsp tras mostrar el mensaje de éxito --!
			function refrescarLocal() 
			{		
				var elemento=parent.document.getElementById('pestana.tiposexpedientes.campos');
				parent.pulsar(elemento,'mainPestanas')
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
			
			<!-- Asociada al boton Volver -->
			function accionVolver() 
			{		
				top.frames["mainWorkArea"].location.href='<%=app%>/Dispatcher.do?proceso=41';
			}
			
			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				var elemento=parent.document.getElementById('pestana.tiposexpedientes.campos');
				parent.pulsar(elemento,'mainPestanas')				
			}
	
			<!-- Asociada al boton Guardar -->
			function accionGuardar() 
			{
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
				if (validateCamposForm(document.camposForm)) {
					alert('<siga:Idioma key="pestana.tiposexpedientes.campos.aviso"/>');
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
					boton1.disabled=false;
					caja1.className="box";
				} else {
					caja1.readonly=true;
					boton1.disabled=true;
					caja1.className="boxConsulta";
				}
				if (bLectura) boton1.disabled=false;
				
				var chk2=document.getElementById("chkPestanaConf2");
				var caja2=document.getElementById("pestanaConf2");
				var boton2=document.getElementById("botonPestana2");
				if (chk2.checked  && !bLectura) {
					caja2.readonly=false;
					boton2.disabled=false;
					caja2.className="box";
				} else {
					caja2.readonly=true;
					boton2.disabled=true;
					caja2.className="boxConsulta";
				}
				if (bLectura) boton2.disabled=false;
				
			}

			function pestana1(obj) {
				var caja1=document.getElementById("pestanaConf1");
				var boton1=document.getElementById("botonPestana1");
				if (obj.checked) {
					caja1.readonly=false;
					boton1.disabled=false;
					caja1.className="box";
				} else {
					caja1.readonly=true;
					boton1.disabled=true;
					caja1.className="boxConsulta";
				}
			}
			
			function pestana2(obj) {
				var caja2=document.getElementById("pestanaConf2");
				var boton2=document.getElementById("botonPestana2");
				if (obj.checked) {
					caja2.readonly=false;
					boton2.disabled=false;
					caja2.className="box";
				} else {
					caja2.readonly=true;
					boton2.disabled=true;
					caja2.className="boxConsulta";
				}
			}
			
			function editarCampos(numPestana) 
			{		
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
			

		</script>
		
		<!-- FIN: SCRIPTS BOTONES -->
		
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="expedientes.tipoExpedientes.configuracion.campos.cabecera" 
			localizacion="expedientes.tipoExpedientes.configuracion.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
		
	</head>
	
	<body class="detallePestanas" onLoad="inicioPestana();">
	
	<div id="camposRegistro">
				
			<html:form  action="/EXP_TiposExpedientes_Campos.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = "modificar"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property = "actionModal" value = ""/>
				<html:hidden property = "idTipoExpediente"/>
				<html:hidden property = "idCampo" value=""/>
				
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
			
				<table align="center">
					<tr>
						<td class="labelText" width="100%" colspan="2" >
							<siga:Idioma key="pestana.tiposexpedientes.campos.aviso"/>
						</td>
					</tr>
				</table>
							
				<table class="tablaCampos" align="center" cellspacing="10">
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
										<siga:Idioma key="expedientes.auditoria.literal.nexpdisciplinario"/> : 				    
									</td>
									<td>
										<html:checkbox name="camposForm" property="nexpDisciplinario" value="true" disabled="<%=bLectura%>"/>
									</td>
								</tr>
								<tr>
									<td id="titulo" class="labelText">
										<siga:Idioma key="expedientes.auditoria.literal.estado"/> : 				    
									</td>
									<td>
										<html:checkbox name="camposForm" property="estado" value="true" disabled="<%=bLectura%>"/>
									</td>
								</tr>
								<tr>
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
										<html:checkbox name="camposForm" property="minuta" value="true" disabled="<%=bLectura%>"/>
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
										<html:text name="camposForm" property="tiempoCaducidad" styleClass="<%=estiloCaja%>" readonly="<%=bLectura%>" size="3" maxlength="3" />
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
			
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
	
		