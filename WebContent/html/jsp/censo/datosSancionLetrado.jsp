<!DOCTYPE html>
<html>
<head>
<!-- datosSancionLetrado.jsp -->
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
<%@ page import = "com.siga.censo.form.SancionesLetradoForm"%>
<%@ page import = "com.siga.beans.*"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "java.util.Properties"%>
<%@ page import = "java.util.Hashtable"%>
<%@ page import = "java.util.ArrayList"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	String idioma = user.getLanguage();
	
	SancionesLetradoForm formulario = (SancionesLetradoForm)request.getAttribute("SancionesLetradoForm");
	
	//  tratamiento de readonly
	String estiloCaja = "";
	String estiloCombo = "";
	String botonesAccion = "";
	String readonly = "false";  // para el combo
	boolean breadonly = false;  // para lo que no es combo
	String modo = (String)request.getAttribute("accion");	
	// caso de accion
	if (formulario.getModo().equals("Ver")) {
		// caso de consulta
		estiloCaja = "boxConsulta";
		estiloCombo = "boxConsulta";
		readonly = "true";
		breadonly = true;
		botonesAccion = "V";
	} else {
		estiloCaja = "box";
		estiloCombo = "boxCombo";
		readonly = "false";
		breadonly = false;
		if (formulario.getModo().equals("nuevo")){
			botonesAccion = "V,G";			
		}else{
			botonesAccion = "V,G,I";
		}
	}	
	
	Hashtable registro = (Hashtable) request.getAttribute("registro");
	String tienepermisoArchivo = (String) request.getAttribute("tienepermiso");
	
	if (registro==null) registro = new Hashtable();	
	
	// miro si estamos en la pesta�a de datos de colegiacion
	String pestanaColegiacion = (String) request.getAttribute("pestanaColegiacion");
	if (pestanaColegiacion==null) {
		pestanaColegiacion = "0";
	}
	String personaColegiacion = (String) request.getAttribute("personaColegiacion");
	if (personaColegiacion==null) {
		personaColegiacion = "";
	}
	String institucionColegiacion = (String) request.getAttribute("institucionColegiacion");
	if (institucionColegiacion==null) {
		institucionColegiacion = "";
	}
	
	String nombrePersona="";
	String nif="";
	String idPersonaFinal="";
	CenPersonaAdm admPer = new CenPersonaAdm(user);
	if (registro.get("IDPERSONA")!=null) {
		nombrePersona = admPer.obtenerNombreApellidos((String)registro.get("IDPERSONA"));
		nif=admPer.obtenerNIF((String)registro.get("IDPERSONA"));
		idPersonaFinal=(String)registro.get("IDPERSONA");
	} else {
		if (pestanaColegiacion.equals("1")) {	
			nombrePersona = admPer.obtenerNombreApellidos(personaColegiacion);
			nif=admPer.obtenerNIF(personaColegiacion);
			idPersonaFinal=personaColegiacion;
		} else {
			nombrePersona = "";
			idPersonaFinal = "";
			nif="";
		}
	}

	ArrayList colegioSel = new ArrayList();
	if (registro.get("IDINSTITUCIONSANCION")!=null) {
		colegioSel.add((String)registro.get("IDINSTITUCIONSANCION"));
	}
	ArrayList tipoSancionSel = new ArrayList();
	if (registro.get("IDTIPOSANCION")!=null) {
		tipoSancionSel.add((String)registro.get("IDTIPOSANCION"));
	}

	String idPersona=(!idPersonaFinal.equals(""))?idPersonaFinal:"";
	String idSancion=(registro.get("IDSANCION")!=null)?(String)registro.get("IDSANCION"):"";
	String idInstitucionAlta=(registro.get("IDINSTITUCION")!=null)?(String)registro.get("IDINSTITUCION"):"";
	String idInstitucionSancion=(registro.get("IDINSTITUCIONSANCION")!=null)?(String)registro.get("IDINSTITUCIONSANCION"):"";
	String refColegio=(registro.get("REFCOLEGIO")!=null)?(String)registro.get("REFCOLEGIO"):"";
	String refCGAE=(registro.get("REFCGAE")!=null)?(String)registro.get("REFCGAE"):"";
	String texto=(registro.get("TEXTO")!=null)?(String)registro.get("TEXTO"):"";
	String fechaResolucion=(registro.get("FECHARESOLUCION")!=null)?GstDate.getFormatedDateShort(idioma,(String)registro.get("FECHARESOLUCION")):"";
	String fechaAcuerdo=(registro.get("FECHAACUERDO")!=null)?GstDate.getFormatedDateShort(idioma,(String)registro.get("FECHAACUERDO")):"";
	String fechaImposicion=(registro.get("FECHAIMPOSICION")!=null)?GstDate.getFormatedDateShort(idioma,(String)registro.get("FECHAIMPOSICION")):"";
	String fechaInicio=(registro.get("FECHAINICIO")!=null)?GstDate.getFormatedDateShort(idioma,(String)registro.get("FECHAINICIO")):"";
	String fechaFin=(registro.get("FECHAFIN")!=null)?GstDate.getFormatedDateShort(idioma,(String)registro.get("FECHAFIN")):"";
	String fechaFirmeza=(registro.get("FECHAFIRMEZA")!=null)?GstDate.getFormatedDateShort(idioma,(String)registro.get("FECHAFIRMEZA")):"";
	String fechaRehabilitado=(registro.get("FECHAREHABILITADO")!=null)?GstDate.getFormatedDateShort(idioma,(String)registro.get("FECHAREHABILITADO")):"";
	String chkFirmeza=(registro.get("CHKFIRMEZA")!=null)?(String)registro.get("CHKFIRMEZA"):"";
	String chkRehabilitado=(registro.get("CHKREHABILITADO")!=null)?(String)registro.get("CHKREHABILITADO"):"";
	String observaciones=(registro.get("OBSERVACIONES")!=null)?(String)registro.get("OBSERVACIONES"):"";
	String chkArchivada=(registro.get("CHKARCHIVADA")!=null)?(String)registro.get("CHKARCHIVADA"):"";
	String fechaArchivada=(registro.get("FECHAARCHIVADA")!=null)?GstDate.getFormatedDateShort(idioma,(String)registro.get("FECHAARCHIVADA")):"";
	
%>

<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="SancionesLetradoForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->

	<script language="JavaScript">
		function accionVolver(){ 	
			<% if (pestanaColegiacion!=null && pestanaColegiacion.equals("1")) { %>
				document.busquedaClientesForm.action = "/SIGA/CEN_BusquedaClientes.do" + "?noReset=true&buscar=true";
				document.busquedaClientesForm.modo.value="abrirConParametros";
				document.busquedaClientesForm.submit();			
			<% } else { %>
				document.SancionesLetradoForm.modo.value="abrirVolver";
				document.SancionesLetradoForm.target="mainWorkArea";
				document.SancionesLetradoForm.submit();		
			<% } %>
		}	
		
		function refrescarLocal(){
			sub();
			document.SancionesLetradoForm.target="mainWorkArea";
			document.SancionesLetradoForm.modo.value = "editar";
			document.SancionesLetradoForm.submit();
			fin();
		}
		
		function accionGuardar(){ 	
			sub();
			
			if (compararFecha(document.SancionesLetradoForm.fechaInicio,document.SancionesLetradoForm.fechaFin)==1){
				alert('<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.fechaFinMayor"/>');
				fin();
				return false;
			}
			if (validateSancionesLetradoForm(document.forms[0])) {
				if(validarChecksFechas()){
					document.forms[0].modo.value="<%=modo%>";
					document.forms[0].submit();
				}			
			}else{
				fin();
				return false;
			}
		}	
		
		function accionImprimir(){ 	
			<% if (!formulario.getModo().equals("nuevo")) { %>
				idInstitucion  = <%=idInstitucionAlta%>;			
				idSancion  = <%=idSancion%>;
				idPersona  = <%=idPersona%>;
	 		   	datos = "idInstitucion=="+idInstitucion +"##idSancion=="+idSancion + "##idPersona==" +idPersona+"##idTipoInforme==SANC%%%";
	 		  	document.InformesGenericosForm.datosInforme.value=datos;
	 		  	document.InformesGenericosForm.submit();
 		  	<% } %>
		}	
	 	
		function checkRehabilitado() {
			var v = document.getElementById('rehabilitado');
			var c = document.getElementById('chkRehabilitado');
			if (v.value!='') {
				c.checked=true;
			}
		}			
		function checkFirmeza() {
			var v = document.getElementById('firmeza');
			var c = document.getElementById('chkFirmeza');
			if (v.value!='') {
				c.checked=true;
			}
		}	

		function checkArchivada() {
			var v = document.getElementById('fechaArchivada');
			var c = document.getElementById('chkArchivada');
			if (v.value!='') {
				c.checked=true;
			}
		}				

		function fechaArchivo() {
			var v = document.getElementById('fechaArchivada');
			var c = document.getElementById('chkArchivada');			
			if (c.checked!=true) {
				v.value='';
				
			}
		}
		
		function fechaRehabilitado() {
			var v = document.getElementById('rehabilitado');
			var c = document.getElementById('chkRehabilitado');
			if (c.checked!=true) {
				v.value='';
			}
		}			
		function fechaFirmeza() {
			var v = document.getElementById('firmeza');
			var c = document.getElementById('chkFirmeza');
			if (c.checked!=true) {
				v.value='';
			}
		}
		
		function validarChecksFechas() {
			var fechaFirmeza = document.getElementById('firmeza').value;
			var chkFirmeza = document.getElementById('chkFirmeza');
			
			var fechaRehabilitado = document.getElementById('rehabilitado').value;
			var chkRehabilitado  = document.getElementById('chkRehabilitado');		
			
			var fechaArchivo = document.getElementById('fechaArchivada').value;
			var chkArchivada  = document.getElementById('chkArchivada');	
			
			var error = "";
			
			if((fechaFirmeza!='' && chkFirmeza.checked!=true) || (chkFirmeza.checked==true && fechaFirmeza == '')){
				error += "<siga:Idioma key="censo.sancionesLetrado.error.firmeza"/>" + '\n';
			}			
					
			if((fechaArchivo!='' && chkArchivada.checked!=true) || (chkArchivada.checked==true && fechaArchivo == '')){
				error += "<siga:Idioma key="censo.sancionesLetrado.error.fechaArchivo"/>" + '\n';
			}			
			
			if (error != null && error != ""){
				alert(error);
				fin();
				return false
			}
			
			return true;
		}		
	</script>	

	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body>
<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.editarSancion"/> 	   
				</td>
			</tr>
		</table>
	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalGrande" align="center">

	<!-- INICIO: CAMPOS -->	
	<table  class="tablaCentralCamposGrande"  align="center">		

		<html:form  action="/CEN_SancionesLetrado.do" method="POST" target="submitArea">
			<html:hidden  name="SancionesLetradoForm" property="modo"/>
			<html:hidden  name="SancionesLetradoForm" property="idSancion" value="<%=idSancion %>"/>
			<html:hidden  name="SancionesLetradoForm" property="idInstitucionAlta" value="<%=idInstitucionAlta %>"/>

		<tr>				
			<td>

				<siga:ConjCampos leyenda="censo.busquedaSancionesLetrado.literal.datos" >

					<table class="tablaCampos" align="center">							
						<!-- FILA -->
						<tr>		
							<td class="labelText">
								<siga:Idioma key="censo.busquedaSancionesLetrado.literal.ncolegiado"/> &nbsp;(*)
							</td>
							<td colspan="3"  style="padding-left:25px;">
								<!-- RGG - SELECCION DE COLEGIADO -->
								<script language="JavaScript">	
									function buscarCliente () 
									{
										var resultado = ventaModalGeneral("busquedaClientesModalForm","G");			
										if (resultado != null && resultado[0]!=null)
										{
											document.getElementById('idPersona').value = resultado[0];
										}
										if (resultado != null && resultado[4]!=null && resultado[5]!=null && resultado[6]!=null)
										{
											document.getElementById('nombreMostrado').value = resultado[3]+ "  "+ resultado[4] + " " + resultado[5] + " " + resultado[6];
										}
									}		
								</script>
								<script language="JavaScript">	
									function limpiarCliente () 
									{
										document.getElementById('idPersona').value = "";
										document.getElementById('nombreMostrado').value = "";
									}		
								</script>
								<!-- Boton para buscar un Colegiado -->
								<% if (formulario.getModo().equals("nuevo") && pestanaColegiacion.equals("0")) { %>
									<input type="button" class="button" name="idButton" value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.buscar"/>' onClick="buscarCliente();">
									<!-- Si el campo numeroLetrado es de solo lectuta hace falta este bot�n para limpiar -->
									&nbsp;<input type="button" class="button" name="idButton" value='<siga:Idioma key="gratuita.inicio_SaltosYCompensaciones.literal.limpiar"/>' onClick="limpiarCliente();">
									&nbsp;
								<% } %>
								<!-- Si la busqueda se realiza por idPersona, el campo numeroLetrado no puede modificarse, en cambio
									 si la busqueda se realiza mediante el campo numeroLetrado se podr�a modificar por pantalla sin
									 necesidad de seleccionarlo por el bot�n -->
								<html:hidden name="SancionesLetradoForm" property="idPersona" size="8" maxlength="80" styleClass="boxConsulta" value="<%=idPersona%>"></html:hidden>
								<html:text property="nif" size="10" maxlength="50" styleClass="boxConsulta" value="<%=nif%>" readOnly="true"></html:text>
								<html:text property="nombreMostrado" size="80" maxlength="150" styleClass="boxConsulta" value="<%=nombrePersona%>" readOnly="true"></html:text>
								<!-- FIN - RGG - SELECCION DE COLEGIADO -->
							</td>
						</tr>				
						<tr>		
							<td class="labelText">
								<siga:Idioma key="censo.busquedaSancionesLetrado.literal.tipoSancion"/> &nbsp;(*)
							</td>
							<td colspan="3"  style="padding-left:25px;">
								<siga:ComboBD nombre = "tipoSancion" ancho="200"  tipo="cmbTipoSancion" readonly="<%=readonly%>" clase="<%=estiloCombo %>" obligatorio="true" elementoSel="<%=tipoSancionSel %>"/>
							</td>
						</tr>
						<tr>		
							<td class="labelText">
								<siga:Idioma key="censo.busquedaSancionesLetrado.literal.colegio"/> &nbsp;(*)
							</td>
							<td style="padding-left:25px;">
								<siga:ComboBD nombre = "nombreInstitucion" ancho="200" tipo="cmbInstitucionesAbreviadas"  readonly="<%=readonly%>" clase="<%=estiloCombo %>" obligatorio="true" elementoSel="<%=colegioSel %>"/>
							</td>
							
							<td class="labelText">
								<siga:Idioma key="censo.BusquedaSancionesLetrado.literal.refColegio"/>
							</td>
							<td style="padding-left:25px;">
								<html:text property="refColegio" size="20" maxlength="50" styleClass="<%=estiloCaja %>" readOnly="<%=breadonly%>" value="<%=refColegio%>"></html:text>
							</td>							
						</tr>
						
						<tr>		
							<td class="labelText">
								<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.fechaAcuerdo"/>
							</td>
							<td  style="padding-left:25px;">
								<% if (!formulario.getModo().equals("Ver")) { %>
									 <siga:Fecha  nombreCampo= "fechaAcuerdo" valorInicial="<%=fechaAcuerdo%>" />
								<% }else{ %>
									<siga:Fecha  nombreCampo= "fechaAcuerdo" valorInicial="<%=fechaAcuerdo%>" disabled="true"/>
								<% } %>									
							</td>						
						
							<% if(	(Integer.parseInt(user.getLocation()) == 2000) || (Integer.parseInt(user.getLocation()) >= 3000)) { %>		
								<td class="labelText">
									<siga:Idioma key="censo.BusquedaSancionesLetrado.literal.refCGAE"/>
								</td>
								<td style="padding-left:25px;">
									<html:text  property="refCGAE" size="20" maxlength="50" styleClass="<%=estiloCaja %>" readOnly="<%=breadonly%>" value="<%=refCGAE%>" ></html:text>
								</td>
							<% }else{%>
								<html:hidden  name="SancionesLetradoForm" property="refCGAE"/>
							<%}%>
						</tr>

						
						<tr>		
							<td class="labelText">
								<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.firmeza"/>
							</td>
							<td>
								<% if (!formulario.getModo().equals("Ver")) { %>
								<input type="checkbox" name="chkFirmeza" value="1" onclick="return fechaFirmeza();"  <%=(chkFirmeza.equals("1"))?"checked":""%> />
								<siga:Fecha  nombreCampo= "firmeza" valorInicial="<%=fechaFirmeza%>"/>
								<% }else { %>
								<input type="checkbox" disabled="disabled" name="chkFirmeza" value="1" onclick="return fechaFirmeza();"  <%=(chkFirmeza.equals("1"))?"checked":""%> />
								<siga:Fecha  nombreCampo= "firmeza" valorInicial="<%=fechaFirmeza%>" disabled="true"/>
								<%} %>
								
							</td>
							<td class="labelText">
								<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.rehabilitado"/>
							</td>
							<td >
								<% if (!formulario.getModo().equals("Ver")) { %>
									<input type="checkbox" name="chkRehabilitado" value="1" onclick="return fechaRehabilitado();"  <%=(chkRehabilitado.equals("1"))?"checked":""%> />
									<siga:Fecha  nombreCampo= "rehabilitado" valorInicial="<%=fechaRehabilitado%>"/>								
								<% }else {%>	
									<input type="checkbox" disabled="disabled"  name="chkRehabilitado" value="1" onclick="return fechaRehabilitado();"  <%=(chkRehabilitado.equals("1"))?"checked":""%> />
									<siga:Fecha  nombreCampo= "rehabilitado" valorInicial="<%=fechaRehabilitado%>" disabled="true"/>								
								<%}%>
							</td>
						</tr>						
										
						<tr>		
							<td class="labelText">
								<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.fechaInicio"/>
							</td>
							<td style="padding-left:25px;">
								<% if (!formulario.getModo().equals("Ver")) { %>
								 <siga:Fecha  nombreCampo= "fechaInicio" valorInicial="<%=fechaInicio%>" />
								<% }else{ %>
								<siga:Fecha  nombreCampo= "fechaInicio" valorInicial="<%=fechaInicio%>" disabled="true"/>
								<% } %>
							</td>
							<td class="labelText">
								<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.fechaFin"/>
							</td>
							<td style="padding-left:25px;">
							<% if (!formulario.getModo().equals("Ver")) { %>
								<siga:Fecha  nombreCampo= "fechaFin" valorInicial="<%=fechaFin%>" />
								<% }else{ %>
								<siga:Fecha  nombreCampo= "fechaFin" valorInicial="<%=fechaFin%>" disabled="true"/>
								<% } %>
							</td>
						</tr>
						
						<% if(tienepermisoArchivo.equals("1")){%>
							<tr>
							<td class="labelText">
									<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.fArchivada"/>
								</td>
								<td>					
									<% if (!formulario.getModo().equals("Ver")) { %>
										<input type="checkbox" name="chkArchivada" value="1" onclick="return fechaArchivo();" <%=(chkArchivada.equals("1"))?"checked":""%> />
										<siga:Fecha  nombreCampo= "fechaArchivada" valorInicial="<%=fechaArchivada%>"/>			
									<% }else {%>														
										<input type="checkbox" disabled="disabled"  name="chkArchivada" value="1" onclick="return fechaArchivo();" <%=(chkArchivada.equals("1"))?"checked":""%> />
										<siga:Fecha  nombreCampo= "fechaArchivada" valorInicial="<%=fechaArchivada%>" disabled="true"/>			
									<%}%>
								</td>							
						   </tr>	
					   <%}%>						
											
						<tr>
							<td class="labelText" colspan="4">
								<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.texto"/>
							</td>
						<tr>		
							<td  colspan="4">
						     	&nbsp;&nbsp;<html:textarea property="texto" 
						     		onKeyDown="cuenta(this,3500)" onChange="cuenta(this,3500)"
						     		style="overflow-y:auto; overflow-x:hidden; width:875px; height:175px; resize:none;" 
						     		readOnly="<%=breadonly%>" styleclass="<%=estiloCaja %>" value="<%=texto%>"></html:textarea> 
							</td>							
						</tr>
						
						
						<tr>							
							<td class="labelText" colspan="4">
								<siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.observaciones"/>
							</td>
						</tr>
						<tr>		
							<td colspan="4" valign="top">
						     	&nbsp;&nbsp;<html:textarea property="observaciones"
						     		onKeyDown="cuenta(this,3500)" onChange="cuenta(this,3500)"
						     		style="overflow-y:auto; overflow-x:hidden; width:875px; height:50px; resize:none;" 
						     		readOnly="<%=breadonly%>" styleclass="<%=estiloCaja%>" value="<%=observaciones%>"></html:textarea> 
							</td>
						</tr>

		   		</table>
				</siga:ConjCampos>
			</td>
		</tr>
		
		</html:form>
		
	</table>
		
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
		<siga:ConjBotonesAccion botones="<%=botonesAccion %>" modal="G"/>
	<!-- FIN: BOTONES REGISTRO -->	

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
		
	<!-- INICIO: FORMULARIO DE BUSQUEDA DE LETRADOS -->
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes"	value="1">
		<input type="hidden" name="busquedaSancion" value="1">
	</html:form>
	
	<html:form  action="/CEN_BusquedaClientes.do" method="POST" target="mainWorkArea">
		<html:hidden  name="busquedaClientesForm" property="modo"/>
	</html:form>	
	
	<html:form action="/INF_InformesGenericos" method="post" target="submitArea">
		<html:hidden property="idInstitucion" value = "<%=idInstitucionAlta%>"/>
		<html:hidden property="idTipoInforme" value="SANC"/>
		<html:hidden property="enviar" value="0"/>
		<html:hidden property="descargar" value="1"/>
		<html:hidden property="datosInforme"/>
		<html:hidden property="modo" value = "preSeleccionInformes"/>
	</html:form>	
		
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
 