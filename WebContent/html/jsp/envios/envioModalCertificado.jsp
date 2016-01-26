<!DOCTYPE html>
<html>
<head>
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL PEQUEÑA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.CerSolicitudCertificadosBean"%>
<%@ page import="com.siga.beans.PysProductosInstitucionAdm"%>
<%@ page import="com.siga.beans.EnvEnviosBean"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.text.ParseException"%>
<%@ page import="java.text.SimpleDateFormat"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	
	String idInstitucion[] = {user.getLocation()};	

	String fecha = UtilidadesString.getMensajeIdioma(user,"envios.definir.literal.fechaprogramada");
	
    SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
    String fechaInicial =  sdfDestination.format(new Date());
	
	String obligatorio = UtilidadesString.getMensajeIdioma(user,"messages.campoObligatorio.error");
	
	String envioSms = (String)request.getAttribute("smsHabilitado");
	String consultaPlantillas="";
	if (envioSms!=null && envioSms.equalsIgnoreCase("S")){
		consultaPlantillas = "cmbTipoEnviosInstSms";
	}else{
		consultaPlantillas = "cmbTipoEnviosInst";
	}
	
	
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
		<!-- Validaciones en Cliente -->
	<html:javascript formName="DefinirEnviosForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStrutsWithHidden.js" type="text/javascript"></script>
	
</head>

<body>
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td id="titulitos" class="titulitosDatos">
				   <siga:Idioma key="envios.certificados.literal.confEnvioCertificado"/>
				</td>
			</tr>
		</table>

<div id="camposRegistro" class="posicionModalMedia" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

		<table class="posicionModalMedia" align="center">

			<html:form action="/ENV_DefinirEnvios.do" method="POST"
				target="submitArea">
				<html:hidden property="hiddenFrame" value="1" />
				<html:hidden property="modo" value="insertarEnvioModalCertificado" />
				<html:hidden property="subModo" value="" />
				<html:hidden property="idPersona" value="" />
				<html:hidden property="idFactura" value="" />
				<html:hidden property="idSolicitud" value="" />
				<html:hidden property="idEnvio" value="" />
				<html:hidden property="nombre" value="kk" />
				<html:hidden property="idEnvioBuscar" value="" />
				<html:hidden property="idsParaEnviar" />
				<html:hidden property="acuseRecibo" value="0" />
				<html:hidden property="checkCertificados" value="1" />
				<html:hidden property="checkFacturas" value="1" />
				<html:hidden property="colegio" value="o" />

				<tr>
					<td>
					<siga:ConjCampos leyenda="envios.certificados.literal.confEnvio">
						
							<table class="tablaCampos" align="center" border="0">
							<tr>	
								<td width="25%"></td>
								<td width="40%"></td>
								<td width="30%"></td>
								<td width="10%"></td>
								
										
							</tr>	
								<tr><td class="labelText"><siga:Idioma key="certificados.tipocertificado.literal.comunicacion" />&nbsp;(*)</td>
									<td colspan="3"> <html:text name="DefinirEnviosForm" property="comunicacionAsunto" size="20" maxlength="100" styleClass="box" style='width:190px;' value="Certificados"  ></html:text></td>
								</tr>
								<tr>
									<td class="labelText"><siga:Idioma
											key="envios.definir.literal.fechaprogramada" /></td>
									<td colspan="3">										
										<siga:Fecha nombreCampo="fechaProgramada" valorInicial="<%=fechaInicial%>"></siga:Fecha>
									</td>
								</tr>
								<tr><td class="labelText"><siga:Idioma key="envios.certificados.literal.destinatario" /></td>
									<td >
										<input type="radio" id ="radioDestinatario" name='radioDestinatario' value="0" onclick="" />
										<siga:Idioma key="certificados.envios.colegioPresentador"/>
									</td>
									<td >
										<input type="radio" id ="radioDestinatario" name='radioDestinatario' value="1" onclick="" checked="checked"/>
										<siga:Idioma key="certificados.envios.solicitantes"/>
									</td>
								
								</tr>							
								<tr><td class="labelText"><siga:Idioma
											key="envios.definir.literal.tipoenvio" />&nbsp;(*)</td>
									<td colspan="3"><siga:ComboBD nombre="comboTipoEnvio"
											tipo="<%=consultaPlantillas%>" clase="boxCombo"
											obligatorio="true" parametro="<%=idInstitucion%>"
											accion="Hijo:comboPlantillaEnvio;onChangeTipoEnvio()" /></td>
								</tr>

								<tr>
									<td class="labelText"><siga:Idioma
											key="envios.plantillas.literal.plantilla" />&nbsp;(*)</td>
									<td><siga:ComboBD nombre="comboPlantillaEnvio"
											tipo="cmbPlantillaEnvios2" clase="boxCombo"
											obligatorio="true" hijo="t"
											accion="Hijo:idPlantillaGeneracion;parent.onChangePlantillaEnvio()" />
									</td>
									<td class="labelText"><siga:Idioma
											key="envios.definir.literal.acuseRecibo" /></td>
									<td><input type="checkbox" id="idCheckAcuseRecibo"
										disabled="disabled">
									</td>
								</tr>

								<tr>
									<td class="labelText"><siga:Idioma
											key="envios.definir.literal.plantillageneracion" /></td>
									<td colspan="3"><siga:ComboBD
											nombre="idPlantillaGeneracion" tipo="cmbPlantillaGeneracion"
											clase="boxCombo" obligatorio="false" hijo="t" pestana="true" />
									</td>

								</tr>

								
							</table>
						</siga:ConjCampos></td>
				</tr>
				<tr>
					<td>
						<siga:ConjCampos leyenda="certificados.envios.documentos">
						
								<table class="tablaCampos" align="center" border="0">
									<tr>	
										<td width="25%"></td>
										<td width="40%"></td>
										<td width="30%"></td>
										<td width="10%"></td>
										
												
									</tr>	
										<tr>
											<td><input type="checkbox" id="idCheckCertificados" name="idCheckCertificados" onclick="" checked="checked"/>	
											<siga:Idioma key="certificados.envios.documentos.certificados" /></td>
										</tr>
										<tr>
											<td><input type="checkbox" id="idCheckFacturas" name ="idCheckCertificados" onclick="" checked="checked"/>	
											<siga:Idioma key="certificados.envios.documentos.facturas" /></td>
										</tr>
										
								</table>
						</siga:ConjCampos>
					</td>
				</tr>

			</html:form>

		</table>



		<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="D,EYC,C" modal="P" />


	<!-- INICIO: SCRIPTS BOTONES -->
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
			sub();
			
			document.DefinirEnviosForm.modo.value = 'envioModalMasivoCertificado';
			var mensaje;
			if (document.DefinirEnviosForm.comunicacionAsunto.value.length<=0){
				   mensaje='<siga:Idioma key="messages.enviosCertificados.comunicacion"/>';
	  			   alert(mensaje);
	  			   fin();
	  			   return false;
			}
			if (validateDefinirEnviosForm(document.DefinirEnviosForm)){
				var insTipoEnvio = document.forms[0].comboTipoEnvio.value;
				var opcion_array=insTipoEnvio.split(",");
				if(document.getElementById("idCheckCertificados").checked)
					document.DefinirEnviosForm.checkCertificados.value = "1";
				else
					document.DefinirEnviosForm.checkCertificados.value = "0";
				
				if(document.getElementById("idCheckFacturas").checked)
					document.DefinirEnviosForm.checkFacturas.value = "1";
				else
					document.DefinirEnviosForm.checkFacturas.value = "0";
				
				if(document.getElementById("idCheckAcuseRecibo").checked)
					document.DefinirEnviosForm.acuseRecibo.value = "1";
				else
					document.DefinirEnviosForm.acuseRecibo.value = "0";
				
				document.DefinirEnviosForm.submit();
			} else {
				fin();
			}
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.top.close();
		}
		function onChangePlantillaEnvio(){
			var cmbPlantillaEnvio = document.getElementsByName("comboPlantillaEnvio")[0];
			var opcionArray=cmbPlantillaEnvio.value.split(",");
			var idTipoEnvio = opcionArray[2]; 
			if(idTipoEnvio=='1'){
				var acuseRecibo = opcionArray[3];
				if(acuseRecibo=='1')
					document.getElementById("idCheckAcuseRecibo").checked = "checked";
				else
					document.getElementById("idCheckAcuseRecibo").checked = "";
					
			}else{
				//document.getElementById("tdAcuseRecibo").style.display="none";
				document.getElementById("idCheckAcuseRecibo").checked = "";
			}
			
				
			
		}
		function onChangeTipoEnvio(){
			
			var cmbTipoEnvio = document.getElementsByName("comboTipoEnvio")[0];
			var opcionArray=cmbTipoEnvio.value.split(",");
			idTipoEnvio = opcionArray[1];
			
			
			//alert("i "+idTipoEnvio);
			if(idTipoEnvio!='1'){
			   	jQuery("#idCheckAcuseRecibo").attr("disabled","disabled");
				
			}else{
				jQuery("#idCheckAcuseRecibo").removeAttr("disabled");
			}
			
		} 
		function accionDownload() 
		{
			sub();	
			if(document.getElementById("idCheckCertificados").checked)
				document.DefinirEnviosForm.checkCertificados.value = "1";
			else
				document.DefinirEnviosForm.checkCertificados.value = "0";
			
			if(document.getElementById("idCheckFacturas").checked)
				document.DefinirEnviosForm.checkFacturas.value = "1";
			else
				document.DefinirEnviosForm.checkFacturas.value = "0";
			
			document.DefinirEnviosForm.modo.value = 'descargarCertificadosYFacturas';
			document.DefinirEnviosForm.submit();
		}
	
		function refrescarLocal() {
			
		}
	</script>
	
	<!-- FIN: SCRIPTS BOTONES -->

</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>