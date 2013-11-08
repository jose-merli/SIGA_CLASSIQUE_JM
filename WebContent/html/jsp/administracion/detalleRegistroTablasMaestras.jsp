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

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>

<% 
	HttpSession ses=request.getSession();
	

	Row datos = (Row)request.getAttribute("datos");
	String sBloqueo = (String)request.getAttribute("bloqueo");
	
	String codigoAux ="";
	String codigoExternoAux ="";
	String descAux ="";
	String fechaBaja = "";
	String idRelacionado = "";
	String textoPlantillas = "";
	String numeroTextoPlantillas = (String) request.getAttribute("NUMEROTEXTOPLANTILLAS");
	if (datos!=null) {
		codigoAux=(String)datos.getString("CODIGO");
		descAux=(String)datos.getString("DESCRIPCION");
		codigoExternoAux=(String)datos.getString("CODIGOEXTERNO");
		if(datos.getString("IDRELACIONADO")!=null && !((String)datos.getString("IDRELACIONADO")).equals("null"))
			idRelacionado = (String)datos.getString("IDRELACIONADO");
		
		if((String)datos.getString("FECHABAJA")!=null && !((String)datos.getString("FECHABAJA")).equals("null"))
			fechaBaja = GstDate.getFormatedDateShort("", (String)datos.getString("FECHABAJA"));
		if((String)datos.getString("TEXTOPLANTILLAS")!=null && !((String)datos.getString("TEXTOPLANTILLAS")).equals("null"))
			textoPlantillas =(String)datos.getString("TEXTOPLANTILLAS");
		
		
		//if((String)datos.getString("IDTIPODICTAMENEJG")!=null && !((String)datos.getString("IDTIPODICTAMENEJG")).equals("null"))
			//tipoDictamenEJG = (String)datos.getString("IDTIPODICTAMENEJG");
		//if((String)datos.getString("IDTIPORESOLUCION")!=null && !((String)datos.getString("IDTIPORESOLUCION")).equals("null"))
			//tipoResolucion = (String)datos.getString("IDTIPORESOLUCION");
	}
	String ponerBaja = "N";
	if(fechaBaja !=null && !fechaBaja.equals("")){
		ponerBaja = "S";
	}
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
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
	
	
	//Nuevo identificador:
	//	String codigoNuevo = (String)request.getAttribute("codigoNuevo");

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
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String datoCombo[] = { (String) userBean.getLocation() };
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<script language="JavaScript">
			// Asociada al boton Restablecer
			function accionRestablecer() {		
				listadoTablasMaestrasForm.reset();
			}
		
			// Asociada al boton GuardarCerrar
			function accionGuardarCerrar() {
				sub();
				var numeroPlantillas = document.getElementById("numeroTextoPlantillas");
				var textoPlantillas = "";
				if(numeroPlantillas &&numeroPlantillas.value>0){
					for ( var j = 0; j < numeroPlantillas.value; j++) {
						textoi = document.getElementById("numeroTextoPlantilla_"+j).value
						if(textoi=='')
							textoi = ' ';
						textoPlantillas += textoi +"%%";
					}
					
				}
				listadoTablasMaestrasForm.numeroTextoPlantillas.value = textoPlantillas;
				
				var error = '';
				if(document.getElementById("idTablaRel")){
					if (document.getElementById("idRelacionado").value=='') {
						nombre = document.getElementById("descripcionRel").value;
						error = "<siga:Idioma key='errors.required' arg0='"+nombre+"'/>";
					}
				}
				
				if (listadoTablasMaestrasForm.descripcionRegistro.value=='')				
					error += (error==''?'':'\n') + '<siga:Idioma key="messages.consultas.error.descripcion"/>';					
				
				var validacodigo = validarCodigo();
				if(validacodigo!='')
					error += (error==''?'':'\n') + validacodigo;
				
				if (error=='') {
					listadoTablasMaestrasForm.submit();						
					window.top.returnValue="MODIFICADO";
						
				}else{	
					alert(error);
					fin();
					return false;					
				}
				
			}
			
			// Asociada al boton Cerrar
			function accionCerrar()  {		
				window.top.close();
			}

			function validarCodigo() {
				if (listadoTablasMaestrasForm.codigoRegistroExt.value!='' && "<%=sTipoCodigoExt%>"=="N") {
					if(isNaN(listadoTablasMaestrasForm.codigoRegistroExt.value)) {
						var aux1 = '<siga:Idioma key="general.codeext"/>';
						var aux = '<siga:Idioma key="errors.byte" arg0="' + aux1 + '"/>';
						return aux;
					}
				}
				return '';
			}

	 		function darDeBaja (o) {
	 			if (o.checked) {
	 				listadoTablasMaestrasForm.ponerBajaLogica.value = "S";
				} else {
					listadoTablasMaestrasForm.ponerBajaLogica.value = "N";
				}
	 		}		
		</script>
	</head>

	<body>
	
		<table class="tablaTitulo" align="center" cellspacing="0">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="administracion.catalogos.titulo"/>
				</td>
			</tr>
		</table>
			
		<div id="camposRegistro" class="posicionModalPeque" align="center">
			<html:form action="/ADM_GestionarTablasMaestras.do" method="POST" target="submitArea">
				<%String miModo = ((bNuevo) ? "Insertar" : "Modificar");%>
				<html:hidden property = "modo" value = "<%=miModo%>"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<input type="hidden" name="codigoRegistro" value="<%=codigoAux%>">
				<input type="hidden" name="nombreTablaMaestra" value="<%=sNombreTabla%>">
				<input type="hidden" name="nombreCampoCodigo" value="<%=sNombreCampoCodigo%>">
				<input type="hidden" name="nombreCampoCodigoExt" value="<%=sNombreCampoCodigoExt%>">
				<input type="hidden" name="nombreCampoDescripcion" value="<%=sNombreCampoDescripcion%>">
				<input type="hidden" name="local" value="<%=sLocal%>">
				<input type="hidden" name="aliasTabla" value="<%=sAliasTabla%>">
				<input type="hidden" name="longitudCodigo" value="<%=sLongitudCodigo%>">
				<input type="hidden" name="longitudCodigoExt" value="<%=sLongitudCodigoExt%>">
				<input type="hidden" name="longitudDescripcion" value="<%=sLongitudDescripcion%>">
				<input type="hidden" name="tipoCodigo" value="<%=sTipoCodigo%>">
				<input type="hidden" name="tipoCodigoExt" value="<%=sTipoCodigoExt%>">				
				<input type="hidden" id="numeroTextoPlantillas"  name="numeroTextoPlantillas" value="<%=numeroTextoPlantillas%>">
				
				
				

				<table class="tablaCentralCamposPeque" align="center" border="0">
					<tr>		
						<td>
							<siga:ConjCampos leyenda="administracion.catalogos.titulo">
								<table class="tablaCampos" align="center">
								<tr>
											<td width="20%"></td>
											<td width="80%"> </td>
										</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="general.literal.tabla"/>
										</td>
										<td class="labelTextValue">
											<%=sAliasTabla%>
										</td>
									</tr>
									
									<tr>
										<td class="labelText">
											<siga:Idioma key="general.codeext"/>
										</td>									
										<td class="labelTextValue">
											<% if (bEditable) {%>
												<html:text property="codigoRegistroExt" styleClass="box" value="<%=codigoExternoAux%>" size="<%=sLengthCodigoExt%>" maxlength="<%=sMaxLengthCodigoExt%>"/>
												
											<% } else { %>
												<%=codigoExternoAux%>
											<% } %>
										</td>
									</tr>
									
									<tr>
										<td class="labelText">
											<siga:Idioma key="general.description"/>&nbsp;(*)
										</td>	
											<% 
												if (bEditable) {
													if (bNuevo){
											%>
												<td class="labelTextValue">
													<html:text property="descripcionRegistro" styleClass="box" size="<%=sLengthDescripcion%>" maxlength="<%=sMaxLengthDescripcion%>"/>
												</td>
												
											<% 		} else {%>
												<td class="labelTextValue">
													<input type="text" name="descripcionRegistro" class="box" value="<%=descAux%>" styleClass="boxCombo" size="<%=sLengthDescripcion%>" maxlength="<%=sMaxLengthDescripcion%>">
												</td>
												
											<%		
													}													
												} else {
											%>
												<td class="labelTextValue"">
													<%=descAux%>
													<html:hidden property="descripcionRegistro" value="<%=descAux%>"/>
												</td>
											<% } %>                                    
									</tr>
									
									<%if (sDarDeBaja!=null && sDarDeBaja.equals("S")&&!bNuevo){%>
										<tr>
											<td class="labelText">
												<siga:Idioma key="general.baja"/>
											</td>
											<td class="labelTextValue">
												<input type="checkbox" name="ponerBajaLogica" style="" 
													onclick="darDeBaja(this);" 
													<%if (!bEditable && !bNuevo) {%> 
														disabled 
													<%}%> 
													value="<%=ponerBaja%>" 
													<%if (fechaBaja !=null && !fechaBaja.equals("")) {%>
														checked
													<%}%>
												>
												<%if (fechaBaja !=null && !fechaBaja.equals("")) {%>
													&nbsp;&nbsp;&nbsp; Baja desde: <%=fechaBaja%>
												<%}%>								
											</td>
										</tr>
									<%}%>									
								</table>
							</siga:ConjCampos>
						</td>
					</tr>
					
					<%
					
					
					if (idTablaRel !=null && !idTablaRel.equals("")) {
							
							
							ArrayList vCodigoCampoRel = new ArrayList();
							
							vCodigoCampoRel.add(idRelacionado);
							//vCodigoCampoRel.add(userBean.getLocation());
							
							String idCampoCodigoRel =(String) request.getAttribute("IDCAMPOCODIGOREL");
							String descripcionRel =(String) request.getAttribute("DESCRIPCIONREL");
							String querycombo = (String) request.getAttribute("QUERYTABLAREL");
							
							 
							
					%>
					<input type="hidden" id="idTablaRel"  name="idTablaRel" value="<%=idTablaRel%>">
					<input type="hidden" id="idCampoCodigoRel"  name="idCampoCodigoRel" value="<%=idCampoCodigoRel%>">
					<input type="hidden" id="descripcionRel"  name="descripcionRel" value='<siga:Idioma key="<%=descripcionRel%>"/>'>
					<input type="hidden" id="queryTablaRel"  name="queryTablaRel" value="<%=querycombo%>">
					
					
					
						<tr>		
							<td>
								<siga:ConjCampos leyenda="Asociado a">
									<table class="tablaCampos" align="center">
										<tr>
											<td width="20%"></td>
											<td width="80%"> </td>
										</tr>
										<tr>
											<td class="labelText" id="descripcionCampoAsociado">
												<siga:Idioma key="<%=descripcionRel%>"/>&nbsp;(*)
											</td>
											<td class="labelTextValue">
												<siga:Select id="idRelacionado"
															queryId="<%=querycombo%>"
															selectedIds="<%=vCodigoCampoRel%>"
															width="320"
															readOnly="<%=String.valueOf(!bEditable) %>" />
											</td>
										</tr>	
										<tr>
											<td ></td>
											<td > </td>
										</tr>						
									</table>
								</siga:ConjCampos>
							</td>
						</tr>					
					<%}	%>
					
					<% 
					
					if(numeroTextoPlantillas!=null  &&!numeroTextoPlantillas.equals("null") &&!numeroTextoPlantillas.equals("") && Integer.parseInt(numeroTextoPlantillas)>0){
						%><tr>		
						<td>
							<siga:ConjCampos leyenda="TextoPlantillas">
						<%
						
						int numeroPlantillas = Integer.parseInt(numeroTextoPlantillas);
						String[] arrayTextoPlantillas = null;
						if(textoPlantillas.equals(""))
							arrayTextoPlantillas = new String[numeroPlantillas];
						else
							arrayTextoPlantillas = textoPlantillas.split("%%");
						
						for (int i = 0; i < numeroPlantillas; i++) {
							%>
							
									<table class="tablaCampos" align="center">
										<tr>
											<td width="20%"></td>
											<td width="80%"> </td>
										</tr>
										<tr>
											<td class="labelText">
												Texto Plantilla <%=i%>
											</td>
											<td class="labelTextValue">
												<% if (bEditable) { %>
												
													<textarea id="numeroTextoPlantilla_<%=i%>" name="numeroTextoPlantilla_<%=i%>"
														onKeyDown="cuenta(this,4000)"  onChange="cuenta(this,4000)"
														style="overflow-y:auto; overflow-x:hidden; width:750px; height:50px; resize:none;"  
														class="box" ><%=arrayTextoPlantillas[i]!=null?arrayTextoPlantillas[i]:""%></textarea>
												
												<% } else { %>
													<textarea id="numeroTextoPlantilla_<%=i%>" name="numeroTextoPlantilla_<%=i%>"
														onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)"
														style="overflow-y:auto; overflow-x:hidden; width:750px; height:50px; resize:none;"  
														class="boxConsulta" readonly="true"><%=arrayTextoPlantillas[i]!=null?arrayTextoPlantillas[i]:""%></textarea>
												<% } %>
											</td>
										</tr>	
																			
									</table>
								
							
							
							
							
							<%
							
						}%>
						</siga:ConjCampos>
						</td>
						</tr>
					<%		
					}
					%>
					
					
					<% if (sBloqueo!=null && sBloqueo.equals("S") ){ %>					
						<tr>
							<td class="labelText" colspan="6"  >
								<siga:Idioma key="gratuita.maestro.campoBloqueo.nota"/>
							</td>
						</tr>
					<%}%>				
				</table>
			</html:form>

			<!-- V Volver, G Guardar, Y GuardaryCerrar, R Restablecer, C Cerrar, X Cancelar -->
			<%String botones = bEditable ? "R,Y,C" : "C";%>
			<siga:ConjBotonesAccion botones="<%=botones%>" modal="P"/>
		</div>	
		
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	</body>
</html>