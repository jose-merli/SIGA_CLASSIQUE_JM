<!DOCTYPE html>
<html>
<head>
<!-- remesa_EJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.CajgRemesaEstadosAdm"%>
<%@ page import="com.siga.ws.SIGAWSClientAbstract"%>
<%@ page import="com.siga.ws.CajgConfiguracion"%>



<!-- TAGLIBS -->
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String idioma=usr.getLanguage().toUpperCase();
	
	//Vector obj = (Vector) ses.getAttribute("resultado");
	ses.removeAttribute("resultado");
	String valor="";
	
	
	/** PAGINADOR ***/
	Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	Vector ejgSeleccionados=null;
	//Datos de la remesa
	String idremesa="";
	String prefijo="";
	String numero="";
	String sufijo="";
	String descripcion="";
	Hashtable r=(Hashtable) request.getAttribute("REMESA");
	if (r.get("IDREMESA")!=null){
		idremesa=(String)r.get("IDREMESA");
	}
	if (r.get("PREFIJO")!=null){
		prefijo=(String)r.get("PREFIJO");
	}
	if (r.get("NUMERO")!=null){
		numero=(String)r.get("NUMERO");
	}
	if (r.get("SUFIJO")!=null){
		sufijo=(String)r.get("SUFIJO");
	}
	if (r.get("DESCRIPCION")!=null){
		descripcion=(String)r.get("DESCRIPCION");
	}
	
	CajgRemesaEstadosAdm admBean =  new CajgRemesaEstadosAdm(usr);	
	int idEstado = admBean.UltimoEstadoRemesa(usr.getLocation(), idremesa);
	
	String modo=(String)request.getSession().getAttribute("accion");
	
	String estilocaja="";
	
	boolean breadonly = false;
	if (modo.equals("consultar")) {		
		estilocaja = "boxConsulta";
		breadonly = true;		
	} else {		
		estilocaja = "box";		
	}
	
	
	boolean subirFicheroRespuesta = false;
		
	int cajgConfig = CajgConfiguracion.getTipoCAJG(Integer.parseInt(usr.getLocation()));
	
	if (idEstado == 2) {//enviada
		if (CajgConfiguracion.TIPO_CAJG_XML_SANTIAGO == cajgConfig) {
			subirFicheroRespuesta = true;
		}
	}
	
%>

<%@page import="java.io.File"%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<title><siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/></title>
	<script type="text/javascript">
		
		var descargar = false;
		
		function refrescarLocal() {
			cargadatosRemesa();
			
			if (descargar) {
				document.DefinicionRemesas_CAJG_Form.modo.value="descargar";
				document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
				document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
				document.DefinicionRemesas_CAJG_Form.target="mainWorkArea";
				document.DefinicionRemesas_CAJG_Form.submit();
			}
			
			descargar = false;
		}
		
		function accionDownload() {
			descargar = true;
			document.DefinicionRemesas_CAJG_Form.modo.value="marcarEnviada";				
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}
		
		function buscarGrupos() {		
			document.EstadosRemesaForm.modo.value="buscar";
			document.EstadosRemesaForm.modoAnterior.value="<%=modo%>";				
			document.EstadosRemesaForm.idRemesa.value=document.forms[0].idRemesa.value;	
			document.EstadosRemesaForm.idInstitucion.value=document.forms[0].idInstitucion.value;

			document.EstadosRemesaForm.target="resultado";	
			document.EstadosRemesaForm.submit();	
		}
		function cargadatosRemesa(){
					
			document.DefinicionRemesas_CAJG_Form.modo.value="buscarPorEJG";
					
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="resultado1";	
			document.DefinicionRemesas_CAJG_Form.submit();	
		}
		
		function aniadirExpedientes(){
			document.DefinicionRemesas_CAJG_Form.modo.value="AniadirExpedientes";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="mainWorkArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}
		
		function generarFichero(){
			document.DefinicionRemesas_CAJG_Form.modo.value="generarFichero";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}

		function preguntaSimular() {
			//'¿Antes de realizar el envío desea comprobar si los expedientes cumplen los requisitos para ser enviados?
			if (confirm('<siga:Idioma key="gratuita.cajg.preguntaSimular"/>')) {			
				document.DefinicionRemesas_CAJG_Form.simular.value=1;
			} else {
				document.DefinicionRemesas_CAJG_Form.simular.value=0;
			}
		}


		function validarRemesa() {
			document.DefinicionRemesas_CAJG_Form.simular.value=1;
			document.DefinicionRemesas_CAJG_Form.modo.value="validarRemesa";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}

		
		function generaXML(){
			//preguntaSimular();
			document.DefinicionRemesas_CAJG_Form.simular.value=0;
			document.DefinicionRemesas_CAJG_Form.modo.value="generaXML";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}

		function envioFTP(){			
			//preguntaSimular();
			document.DefinicionRemesas_CAJG_Form.simular.value=0;
			document.DefinicionRemesas_CAJG_Form.modo.value="envioFTP";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();			
		}
		
		function envioWS(){
			document.DefinicionRemesas_CAJG_Form.simular.value=0;
			document.DefinicionRemesas_CAJG_Form.modo.value="envioWS";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}	
		
		function respuestaFTP() {
			document.DefinicionRemesas_CAJG_Form.modo.value="respuestaFTP";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();
		}			
		
		function accionGuardar(){
			sub();
			document.DefinicionRemesas_CAJG_Form.modo.value="modificar";
			document.DefinicionRemesas_CAJG_Form.idRemesa.value=document.forms[0].idRemesa.value;	
			document.DefinicionRemesas_CAJG_Form.idInstitucion.value=document.forms[0].idInstitucion.value;	
			document.DefinicionRemesas_CAJG_Form.target="submitArea";
			document.DefinicionRemesas_CAJG_Form.submit();	
		}
		
		function filtrado() {		
			document.DefinicionRemesas_CAJG_Form.modo.value="buscarPorEJG";
			document.DefinicionRemesas_CAJG_Form.target="resultado1";
			document.DefinicionRemesas_CAJG_Form.submit();
		}
		
		function descargarLog() {		
			document.DefinicionRemesas_CAJG_Form.modo.value="descargarLog";				
		   	document.DefinicionRemesas_CAJG_Form.target="submitArea";		   	
			document.DefinicionRemesas_CAJG_Form.submit();
		}
		
		function ultimoEstado(idEstado) {
		
			/*if(idEstado > 0) {				
			 	var trFiltrado = document.getElementById("idTrFiltradoIncidencias");			 	
			 	if (trFiltrado) {			 		
			 		trFiltrado.style.display = "";
			 	}
			}*/
		}
		
	</script>
</head>

<body onload="buscarGrupos();cargadatosRemesa();ajusteAlto('resultado1');">
	
	<html:form action="/JGR_E-Comunicaciones_Gestion.do?noReset=true" method="POST" target="resultado" enctype="multipart/form-data">
		<html:hidden property = "modo" value = "inicio"/>
		<html:hidden property = "idInstitucion" value = "<%=usr.getLocation()%>"/>
		<html:hidden property = "actionModal" value = ""/>
		<html:hidden property = "idRemesa" value = "<%=idremesa%>"/>
		<html:hidden property = "simular" value = "0"/>
		<html:hidden property = "idEstado" value = "<%=String.valueOf(idEstado)%>"/>
		<html:hidden  name="DefinicionRemesas_CAJG_Form" property="accion"/>
	
		

	<fieldset name="fieldset1" id="fieldset1">
	<legend>
		<span  class="boxConsulta" >
			<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.datos"/>
		</span>
	</legend>
	
	<table align="center" width="100%" border="0">
		<tr>	
			<td>	
				<table align="center" width="100%" border="0">
					<tr>
						<td class="labelText" >
							<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.nRegistro"/>	
						</td>
				
						<td class="labelText">	
							<html:text name="DefinicionRemesas_CAJG_Form" property="prefijo"  size="5" maxlength="10" styleClass="boxConsulta" value="<%=prefijo%>" style="width:55px" readonly="true" ></html:text>
							<html:text name="DefinicionRemesas_CAJG_Form" property="numero"  size="5" maxlength="10" styleClass="boxConsulta" value="<%=numero%>" style="width:55px" readonly="true" ></html:text>
							<html:text name="DefinicionRemesas_CAJG_Form" property="sufijo"  size="5" maxlength="10" styleClass="boxConsulta" value="<%=sufijo%>" style="width:55px" readonly="true" ></html:text>
							<html:hidden  name="DefinicionRemesas_CAJG_Form" property="accion"/>	
						</td>	
					</tr>
					<tr>
						<td class="labelText">
							<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.Descripcion"/>	
						</td>
						<td class="labelText">	
							<html:text name="DefinicionRemesas_CAJG_Form" property="descripcion"  size="60" maxlength="200" styleClass="<%=estilocaja%>" value="<%=descripcion%>" readonly="<%=breadonly%>" ></html:text>
						</td>
					</tr>
										  
					<tr id="idTrFiltradoIncidencias" style='display:<%=(idEstado>-1?"":"none")%>;'>
						<td class="labelText">
							<siga:Idioma key="gratuita.BusquedaRemesas_CAJG.literal.IncidenciasEnvio"/>	
						</td>
						<td class="labelText">
							<html:select property="idIncidenciasEnvio" onchange="filtrado()" styleClass="boxCombo">
								<html:option value=""/>
								<option value="1"><siga:Idioma key="cajg.opcion.conErrores"/></option>								
								<option value="2"><siga:Idioma key="cajg.opcion.sinErrores"/></option>
								<option value="3"><siga:Idioma key="cajg.opcion.conErroresAntesEnvio"/></option>
								<option value="4"><siga:Idioma key="cajg.opcion.conErroresDespuesEnvio"/></option>
								<option value="5"><siga:Idioma key="cajg.opcion.conErroresNoEnNuevaRemesa"/></option>
							</html:select>
							
						</td>
						
					</tr>
					
					<% if (subirFicheroRespuesta) { %>
					<tr>				
						<td class="labelText">
							<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.FicheroResoluciones"/>&nbsp;(*)
						</td>				
						<td class="labelText">	
							<html:file property="file" size="45" styleClass="box" accept="image/gif,image/jpg"></html:file>
						</td>						
					</tr>					
					<%} %>
					
					<% File errorFile = SIGAWSClientAbstract.getErrorFile(Integer.parseInt(usr.getLocation()), Integer.parseInt(idremesa));
						if (errorFile != null && errorFile.length() > 0) { %>
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.FicheroLog"/>	
							</td>
							<td class="labelText">										
								<html:link href="#" onclick="descargarLog()"><%=errorFile.getName()%></html:link>
							</td>
						</tr>
					<% } %>
						
					
				</table>
			</td>
			<td>
				<table align="center" width="100%" border="0">
					<tr>
						<td  style="width:400px;height:180px" rowspan="2">											
							<!-- INICIO: IFRAME ESTADOS REMESA -->
							<iframe align="left" src="<%=app%>/html/jsp/general/blank.jsp"
								id="resultado"
								name="resultado" 
								scrolling="no"
								frameborder="0"
								marginheight="0"
								marginwidth="0";					 
								style="width:90%; height:100%;">
							</iframe>						
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>

	</fieldset>
	
	</html:form>
	
	<html:form action="/JGR_EstadosRemesa.do" method="POST" target="resultado">
		<html:hidden name="EstadosRemesaForm" property="modo" value="buscar"/>
		<html:hidden name="EstadosRemesaForm" property="idRemesa" />
		<html:hidden name="EstadosRemesaForm" property="idInstitucion" />
		<html:hidden name="EstadosRemesaForm" property="modoAnterior" />
	</html:form>
		
	
	
	<!-- FIN: BOTONES BUSQUEDA -->

<!-- INICIO: IFRAME LISTA RESULTADOS -->
			<iframe align="left" src="<%=app%>/html/jsp/general/blank.jsp"
							id="resultado1"
							name="resultado1"
							scrolling="no"
							frameborder="0"
							marginheight="0"
							marginwidth="0";					 
							style="width:100%; height:100%;">
			</iframe>
			<!-- FIN: IFRAME LISTA RESULTADOS -->
	 
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	
	
<!-- FIN: SUBMIT AREA -->	
	
</body>	
</html>
	