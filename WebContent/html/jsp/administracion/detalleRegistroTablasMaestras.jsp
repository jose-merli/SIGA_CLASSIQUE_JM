<!DOCTYPE html>
<html>
<head>
<!-- detalleRegistroTablasMaestras.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<% 
	HttpSession ses = request.getSession();
	UsrBean userBean = (UsrBean)ses.getAttribute("USRBEAN");
		
	Row datos = (Row)request.getAttribute("datos");
	String sBloqueo = (String)request.getAttribute("bloqueo");
	String numeroTextoPlantillas = (String)request.getAttribute("NUMEROTEXTOPLANTILLAS");
	
	String codigoAux="", codigoExternoAux="", descAux="", fechaBaja="", idRelacionado="", textoPlantillas="";
	if (datos!=null) {
		codigoAux = datos.getString("CODIGO");
		descAux = datos.getString("DESCRIPCION");
		codigoExternoAux = datos.getString("CODIGOEXTERNO");
		if (datos.getString("IDRELACIONADO")!=null && !datos.getString("IDRELACIONADO").equals("null"))
			idRelacionado = datos.getString("IDRELACIONADO");		
		if (datos.getString("FECHABAJA")!=null && !datos.getString("FECHABAJA").equals("null"))
			fechaBaja = GstDate.getFormatedDateShort("", datos.getString("FECHABAJA"));
		if (datos.getString("TEXTOPLANTILLAS")!=null && !datos.getString("TEXTOPLANTILLAS").equals("null"))
			textoPlantillas = datos.getString("TEXTOPLANTILLAS");
	}
	String ponerBaja = "N";
	if (fechaBaja!=null && !fechaBaja.equals("")) {
		ponerBaja = "S";
	}
	
	String sEditable = (String)request.getAttribute("editable");
	boolean bEditable = sEditable.equals("1");
	boolean bNuevo = ((String)request.getAttribute("nuevo")).equals("1");
	String sNombreTabla = (String)request.getAttribute("nombreTabla");
	String sNombreCampoCodigo = (String)request.getAttribute("nombreCampoCodigo");
	String sNombreCampoCodigoExt = (String)request.getAttribute("nombreCampoCodigoExt");
	String sNombreCampoDescripcion = (String)request.getAttribute("nombreCampoDescripcion");
	String sLocal = (String)request.getAttribute("local");
	String sAliasTabla = (String)request.getAttribute("aliasTabla");
	String sLongitudCodigo = (String)request.getAttribute("longitudCodigo");
	String sLongitudCodigoExt = (String)request.getAttribute("longitudCodigoExt");
	String sLongitudDescripcion = (String)request.getAttribute("longitudDescripcion");
	String sTipoCodigo = (String)request.getAttribute("tipoCodigo");
	String sTipoCodigoExt = (String)request.getAttribute("tipoCodigoExt");
	String sDarDeBaja = (String)request.getAttribute("darDeBaja");
	String idTablaRel = (String)request.getAttribute("IDTABLAREL");
	
	String idCampoCodigoRel="", descripcionRel="", querycombo="";
	ArrayList vCodigoCampoRel = new ArrayList();					
	vCodigoCampoRel.add(idRelacionado);	
	if (idTablaRel!=null && !idTablaRel.equals("")) {
		idCampoCodigoRel =(String) request.getAttribute("IDCAMPOCODIGOREL");
		descripcionRel =(String) request.getAttribute("DESCRIPCIONREL");
		querycombo = (String) request.getAttribute("QUERYTABLAREL");
	}
	
	String sMaxLengthCodigo = sLongitudCodigo;
	String sMaxLengthCodigoExt = sLongitudCodigoExt;
	String sMaxLengthDescripcion = sLongitudDescripcion;
	String sLengthCodigo = sMaxLengthCodigo;
	String sLengthCodigoExt = sMaxLengthCodigoExt;
	String sLengthDescripcion = null;
	if (sMaxLengthDescripcion!=null) {
		sLengthDescripcion = Integer.parseInt(sMaxLengthDescripcion)>30 ? "30" : sMaxLengthDescripcion;
	}
	
	request.removeAttribute("datos");
	request.removeAttribute("editable");
	request.removeAttribute("nuevo");
	request.removeAttribute("nombreTabla");
	request.removeAttribute("nombreCampoCodigo");
	request.removeAttribute("nombreCampoCodigoExt");
	request.removeAttribute("nombreCampoDescripcion");
	request.removeAttribute("local");
	request.removeAttribute("aliasTabla");
	request.removeAttribute("longitudCodigo");
	request.removeAttribute("longitudCodigoExt");
	request.removeAttribute("longitudDescripcion");
	request.removeAttribute("tipoCodigo");
	request.removeAttribute("tipoCodigoExt");
	
	String datoCombo[] = {userBean.getLocation()};
	
	String miModo = (bNuevo ? "insertar" : "modificar");
		
	ses.setAttribute("bloqueo",sBloqueo);
	String sIncluirBajaLogica="";
	boolean bIncluirBajaLogica=false;
	if (request.getAttribute("bIncluirRegistrosConBajaLogica")!=null) {
		bIncluirBajaLogica = UtilidadesString.stringToBoolean((String) request.getAttribute("bIncluirRegistrosConBajaLogica"));
		sIncluirBajaLogica=(String) request.getAttribute("bIncluirRegistrosConBajaLogica");
		request.removeAttribute("bIncluirRegistrosConBajaLogica");
	}
	
	if(bIncluirBajaLogica)
		bEditable=false;
	
	String botonesListAsist="C";
	if (bEditable && !bIncluirBajaLogica)
		botonesListAsist += ",E,B";
%>	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<siga:TituloExt titulo="administracion.catalogos.titulo.datos.generales" localizacion="administracion.catalogos.localizacion.datos.generales"/>
</head>

<body>
	<html:form action="/ADM_GestionarTablasMaestras.do" method="POST" target="submitArea">
		<html:hidden property="modo" value="<%=miModo%>"/>
		<html:hidden property="editable" value="<%=sEditable%>"/>
		<html:hidden property="hiddenFrame" value="1"/>
		<input type="hidden" name="aliasTabla" value="<%=sAliasTabla%>">
		<input type="hidden" name="codigoRegistro" value="<%=codigoAux%>">
		<input type="hidden" name="descripcionRel" id="descripcionRel" value='<siga:Idioma key="<%=descripcionRel%>"/>'>
		<input type="hidden" name="id" id="id" value="">
		<input type="hidden" name="idCampoCodigoRel" id="idCampoCodigoRel" value="<%=idCampoCodigoRel%>">
		<input type="hidden" name="idTablaRel" id="idTablaRel" value="<%=idTablaRel%>">
		<input type="hidden" name="local" value="<%=sLocal%>">
		<input type="hidden" name="longitudCodigo" value="<%=sLongitudCodigo%>">
		<input type="hidden" name="longitudCodigoExt" value="<%=sLongitudCodigoExt%>">
		<input type="hidden" name="longitudDescripcion" value="<%=sLongitudDescripcion%>">
		<input type="hidden" name="nombreCampoCodigo" value="<%=sNombreCampoCodigo%>">
		<input type="hidden" name="nombreCampoCodigoExt" value="<%=sNombreCampoCodigoExt%>">
		<input type="hidden" name="nombreCampoDescripcion" value="<%=sNombreCampoDescripcion%>">
		<input type="hidden" name="nombreTablaMaestra" value="<%=sNombreTabla%>">
		<input type="hidden" name="numeroTextoPlantillas" id="numeroTextoPlantillas" value="<%=numeroTextoPlantillas%>">
		<input type="hidden" name="queryTablaRel" id="queryTablaRel" value="<%=querycombo%>">
		<input type="hidden" name="regBajaLogica" id="regBajaLogica" value="<%=sIncluirBajaLogica%>">		
		<input type="hidden" name="tipoCodigo" value="<%=sTipoCodigo%>">
		<input type="hidden" name="tipoCodigoExt" value="<%=sTipoCodigoExt%>">
			
		<table class="tablaCentralCampos" align="center" border="0">
			<tr>		
				<td>
					<siga:ConjCampos leyenda="administracion.catalogos.titulo">
						<table class="tablaCampos" align="center">
							<tr>
								<td width="20%"></td>
								<td width="80%"></td>
							</tr>
							
							<tr>
								<td class="labelText"><siga:Idioma key="general.literal.tabla"/></td>
								<td class="labelTextValue"><%=sAliasTabla%></td>
							</tr>
							
							<tr>
								<td class="labelText"><siga:Idioma key="general.codeext"/></td>									
								<td class="labelTextValue">
<% 
									if (bEditable) {
%>
										<html:text property="codigoRegistroExt" styleClass="box" value="<%=codigoExternoAux%>" size="<%=sLengthCodigoExt%>" maxlength="<%=sMaxLengthCodigoExt%>"/>
										
<% 
									} else { 
%>
										<%=codigoExternoAux%>
<% 
									} 
%>
								</td>
							</tr>
							
							<tr>
								<td class="labelText"><siga:Idioma key="general.description"/>&nbsp;(*)</td>	
<% 
									if (bEditable) {
										if (bNuevo){
%>
											<td class="labelTextValue">
												<input type="text" name="descripcionRegistro" class="box" value="" size="<%=sLengthDescripcion%>" maxlength="<%=sMaxLengthDescripcion%>">
											</td>										
<% 		
										} else {
%>
											<td class="labelTextValue">
												<input type="text" name="descripcionRegistro" class="box" value="<%=descAux%>" size="<%=sLengthDescripcion%>" maxlength="<%=sMaxLengthDescripcion%>">
											</td>										
<%		
										}													
									} else {
%>
										<td class="labelTextValue"">
											<%=descAux%>
											<html:hidden property="descripcionRegistro" value="<%=descAux%>"/>
										</td>
<% 
									} 
%>                                    
							</tr>
							
<%
							if (sDarDeBaja!=null && sDarDeBaja.equals("S") && !bNuevo) {
%>
								<tr>
									<td class="labelText"><siga:Idioma key="general.baja"/></td>
									<td class="labelTextValue">
										<input type="checkbox" name="ponerBajaLogica" style="" onclick="darDeBaja(this);" value="<%=ponerBaja%>" 
											<% if (!bEditable && !bNuevo) { %> disabled <% } %> 											 
											<% if (fechaBaja!=null && !fechaBaja.equals("")) { %> checked <% } %>
										>
<%
										if (fechaBaja!=null && !fechaBaja.equals("")) {
%>
											&nbsp;&nbsp;&nbsp; Baja desde: <%=fechaBaja%>
<%
										}
%>								
									</td>
								</tr>
<%
							}
%>									
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
			
<%
			if (idTablaRel!=null && !idTablaRel.equals("")) {
%>
				<tr>		
					<td>
						<siga:ConjCampos leyenda="Asociado a">
							<table class="tablaCampos" align="center">
								<tr>
									<td width="20%"></td>
									<td width="80%"></td>
								</tr>
								
								<tr>
									<td class="labelText" id="descripcionCampoAsociado"><siga:Idioma key="<%=descripcionRel%>"/>&nbsp;(*)</td>
									<td class="labelTextValue">
										<siga:Select id="idRelacionado" queryId="<%=querycombo%>" selectedIds="<%=vCodigoCampoRel%>" width="320" readOnly="<%=String.valueOf(!bEditable)%>" />
									</td>
								</tr>	
							</table>
						</siga:ConjCampos>
					</td>
				</tr>					
<%
			} 
			
			if (numeroTextoPlantillas!=null && !numeroTextoPlantillas.equals("null") && !numeroTextoPlantillas.equals("") && Integer.parseInt(numeroTextoPlantillas)>0) {
%>
				<tr>		
					<td>
						<siga:ConjCampos leyenda="TextoPlantillas">
<%
							int numeroPlantillas = Integer.parseInt(numeroTextoPlantillas);
							String[] arrayTextoPlantillas = null;
							if (textoPlantillas.equals(""))
								arrayTextoPlantillas = new String[numeroPlantillas];
							else
								arrayTextoPlantillas = textoPlantillas.split("%%");
				
							for (int i = 0; i < numeroPlantillas; i++) {
%>
					
								<table class="tablaCampos" align="center">
									<tr>
										<td width="20%"></td>
										<td width="80%"></td>
									</tr>
									
									<tr>
										<td class="labelText">
											Texto Plantilla <%=i%>
										</td>
										<td class="labelTextValue">
<% 
											if (bEditable) { 
%>										
												<textarea id="numeroTextoPlantilla_<%=i%>" name="numeroTextoPlantilla_<%=i%>"
													onKeyDown="cuenta(this,4000)"  onChange="cuenta(this,4000)"
													style="overflow-y:auto; overflow-x:hidden; width:750px; height:50px; resize:none;"  
													class="box" ><%=arrayTextoPlantillas[i]!=null?arrayTextoPlantillas[i]:""%></textarea>
										
<% 
											} else { 
%>
												<textarea id="numeroTextoPlantilla_<%=i%>" name="numeroTextoPlantilla_<%=i%>"
													onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)"
													style="overflow-y:auto; overflow-x:hidden; width:750px; height:50px; resize:none;"  
													class="boxConsulta" readonly="true"><%=arrayTextoPlantillas[i]!=null?arrayTextoPlantillas[i]:""%></textarea>
<% 
											} 
%>
										</td>
									</tr>	
								</table>
<%
							}
%>
						</siga:ConjCampos>
					</td>
				</tr>
<%		
			}
			
			if (sBloqueo!=null && sBloqueo.equals("S")) { 
%>					
				<tr>
					<td class="labelText" colspan="6"><siga:Idioma key="gratuita.maestro.campoBloqueo.nota"/></td>
				</tr>
<%
			}

			if (!bNuevo && sNombreTabla.equals("SCS_COSTEFIJO")) { 
%> 
				<siga:Table 
					name="tablaDatos" 
					border="1" 
					columnNames="administracion.catalogos.maestros.literal.tipos.asistencias," 
					columnSizes="85,10">
					<c:choose>
   						<c:when test="${empty tiposAsistenciasRel}">
	   						<tr class="notFound">
		   						<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
							</tr>	 		
	   					</c:when>
	   					<c:otherwise>
							<c:forEach items="${tiposAsistenciasRel}" var="tipoAsistencia" varStatus="status">															
								<siga:FilaConIconos	fila='${status.count}' pintarEspacio="no" botones="<%=botonesListAsist%>" clase="listaNonEdit">
									<td align='left'>
										<input type="hidden" name="oculto${status.count}_1" value="${tipoAsistencia.IDTIPOASISTENCIA}">
										<c:out	value="${tipoAsistencia.DESCRIPCION}" />
									</td>
								</siga:FilaConIconos>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</siga:Table>
<%
			}
%>		
		</table>
	</html:form>
<%
	String botones = bEditable ? "V,R,G" : "V";
	if (!bNuevo && bEditable && sNombreTabla.equals("SCS_COSTEFIJO")) {
		botones+=",aa";
	}
%>

	<siga:ConjBotonesAccion botones="<%=botones%>" modal=""/>
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	
	<script type="text/javascript">	
		function consultar(fila) {
			var idtipoasistencia = 'oculto' + fila + '_' + 1;
			document.forms[0].id.value = document.getElementById(idtipoasistencia).value;
	 		listadoTablasMaestrasForm.modo.value = "verAsistencia";
	 		listadoTablasMaestrasForm.target="mainWorkArea";
	 		listadoTablasMaestrasForm.submit();
	 	}
	
	 	function editar(fila) {
	 		var idtipoasistencia = 'oculto' + fila + '_' + 1;
	 		listadoTablasMaestrasForm.id.value = document.getElementById(idtipoasistencia).value;
	 		listadoTablasMaestrasForm.modo.value = "editarAsistencia";
	 		listadoTablasMaestrasForm.target="mainWorkArea";
	 		listadoTablasMaestrasForm.submit();
	 	 }
	
	 	function borrar(fila) {
			if (confirm('¿Está seguro de que desea eliminar el registro?')){
	 			var idtipoasistencia = 'oculto' + fila + '_' + 1;	
	 		    listadoTablasMaestrasForm.id.value = document.getElementById(idtipoasistencia).value;
	 		    var auxTarget = document.forms[0].target;
		 	    listadoTablasMaestrasForm.target="submitArea";
		 	    listadoTablasMaestrasForm.modo.value = "borrarAsistencia";
		 	    listadoTablasMaestrasForm.submit();
		 	    listadoTablasMaestrasForm.target=auxTarget;
	 	 	}
		}
	 	 
		function aniadirTipoAsistencia(){
			sub();
			listadoTablasMaestrasForm.modo.value="abrirConfiguracionCosteFijo";
			listadoTablasMaestrasForm.target="mainWorkArea";
			listadoTablasMaestrasForm.submit();		
			fin();
		}
			
		function accionRestablecer() {		
			listadoTablasMaestrasForm.reset();
		}
			
		function accionGuardar() {
			sub();
			var numeroPlantillas = document.getElementById("numeroTextoPlantillas");
			var textoPlantillas = "";
			if(numeroPlantillas &&numeroPlantillas.value>0){
				for ( var j = 0; j < numeroPlantillas.value; j++) {
					textoi = document.getElementById("numeroTextoPlantilla_"+j).value;
					if(textoi=='')
						textoi = ' ';
					textoPlantillas += textoi +"%%";
				}
			}
			listadoTablasMaestrasForm.numeroTextoPlantillas.value = textoPlantillas;
			
			var error = '';
			if(document.getElementById("idTablaRel").value!="null"){
				if (document.getElementById("idRelacionado").value=="") {
					nombre = document.getElementById("descripcionRel").value;
					error = "<siga:Idioma key='errors.required' arg0='"+nombre+"'/>";
				}
			}
			
			if (listadoTablasMaestrasForm.descripcionRegistro.value=="")				
				error += (error==''?'':'\n') + '<siga:Idioma key="messages.consultas.error.descripcion"/>';					
			
			var validacodigo = validarCodigo();
			if(validacodigo!='')
				error += (error==''?'':'\n') + validacodigo;
			
			if (error=='') {
				listadoTablasMaestrasForm.modo.value="<%=miModo%>";
				listadoTablasMaestrasForm.submit();		
				fin();					
			} else {	
				alert(error);
				fin();
				return false;					
			}
			
		}
				
		function validarCodigo() {
			if (listadoTablasMaestrasForm.codigoRegistroExt.value!="" && "<%=sTipoCodigoExt%>"=="N") {
				if(isNaN(listadoTablasMaestrasForm.codigoRegistroExt.value)) {
					var aux1 = '<siga:Idioma key="general.codeext"/>';
					var aux = '<siga:Idioma key="errors.byte" arg0="' + aux1 + '"/>';
					return aux;
				}
			}
			return "";
		}
	
 		function darDeBaja (o) {
 			if (o.checked) {
 				listadoTablasMaestrasForm.ponerBajaLogica.value = "S";
			} else {
				listadoTablasMaestrasForm.ponerBajaLogica.value = "N";
			}
 		}
		 		
		function accionVolver(){		
			sub();
			<%ses.setAttribute("refrescar","S");
			ses.setAttribute("tabla",sNombreTabla);%>
			listadoTablasMaestrasForm.target="mainWorkArea";
			listadoTablasMaestrasForm.modo.value="abrir";
			listadoTablasMaestrasForm.regBajaLogica.value="false";
			listadoTablasMaestrasForm.submit(); 
			fin();
		}
			
	 	function refrescarLocal(){
	 		sub();
			listadoTablasMaestrasForm.target="mainWorkArea";
			listadoTablasMaestrasForm.modo.value="Editar";
			listadoTablasMaestrasForm.submit(); 
			fin();
	 	}
		 	
	 	function incluirRegBajaLogica(o) {
			if (o.checked) {
				listadoTablasMaestrasForm.regBajaLogica.value="true";
				
			} else {
				listadoTablasMaestrasForm.regBajaLogica.value="false";
				
			}
			refrescarLocal();
		}		 	 
	</script>
</body>
</html>