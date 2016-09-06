<!DOCTYPE html>
<html>
<head>
<!-- datosJuzgado.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>

<!-- IMPORTS -->
<%@ page import="com.siga.gratuita.form.MantenimientoJuzgadoForm" %>
<%@ page import="com.siga.beans.ScsJuzgadoProcedimientoBean" %>
<%@ page import="com.siga.beans.ScsProcedimientosBean" %>
<%@ page import="com.siga.Utilidades.UtilidadesHash" %>
<%@ page import="com.siga.Utilidades.UtilidadesString" %>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.atos.utils.*"%>

<!-- JSP -->
<% 
		String estilo="box", estiloCombo="boxCombo";
		String comboLectura = "false";
		boolean desactivado = true;
		String accion = "";
		String modo = (String)request.getAttribute("modo");	

		ArrayList provinciaSel = new ArrayList();
		ArrayList poblacionSel = new ArrayList();
		String parametro[] = new String[1];
		String fechaBaja = "", visibleMovil ="", esDecano ="";
		
		//Procedimientos de este Juzgado:
		Vector vProcedimientos = (Vector)request.getAttribute("vProcedimientos");

		// Formulario
		MantenimientoJuzgadoForm formulario = (MantenimientoJuzgadoForm) request.getAttribute("MantenimientoJuzgadoForm");
		if(formulario.getDatos().get("FECHABAJA")!=null && !((String)formulario.getDatos().get("FECHABAJA")).equals("")){
			fechaBaja = GstDate.getFormatedDateShort("", (String)formulario.getDatos().get("FECHABAJA"));
		}
		String ponerBaja = "N";
		if(fechaBaja !=null && !fechaBaja.equals("")){
			ponerBaja = "S";
		}

		
		if(formulario.getDatos().get("VISIBLEMOVIL")!=null && !((String)formulario.getDatos().get("VISIBLEMOVIL")).equals("")){
			visibleMovil = (String)formulario.getDatos().get("VISIBLEMOVIL");
		}		
		String ponerVisibilidadMovil = "N";
		if(visibleMovil !=null && visibleMovil.equals("1")){
			ponerVisibilidadMovil = "S";
		}
		
		if(formulario.getDatos().get("ESDECANO")!=null && !((String)formulario.getDatos().get("ESDECANO")).equals("")){
			esDecano = (String)formulario.getDatos().get("ESDECANO");
		}		
		String putDecano = "N";
		if(esDecano !=null && esDecano.equals("1")){
			putDecano = "S";
		}

		
		
		String topBotones=null, topTabla=null;
		String activarVisible="";
		String deshabilitarVisible="";
		if (modo.equalsIgnoreCase("EDITAR")) {
			desactivado  = false;
			estilo = "box";
			estiloCombo = "boxCombo";
			accion = "modificar";
			comboLectura = "false";
			// Datos seleccionados de los combos:
			provinciaSel.add(formulario.getIdProvincia());
			poblacionSel.add(formulario.getIdPoblacion());
			parametro[0] = formulario.getIdProvincia();
			topBotones = "236";
			topTabla = "239";
			if (formulario.getVisible()!=null && formulario.getVisible().equals(ClsConstants.DB_TRUE)){
			  activarVisible="checked";
			}else{
			  activarVisible="";
			}			
			
		} else {
			if (modo.equalsIgnoreCase("NUEVO")) {
				desactivado = false;
				accion = "insertar";
				comboLectura = "false";
				topBotones = "236";
				topTabla = "239";
				activarVisible="checked";
					
			} else { //MODO=VER
				desactivado  = true;
				estilo = "boxConsulta";
				estiloCombo = "boxConsulta";
				accion = "ver";
				comboLectura = "true";
				// Datos seleccionados de los combos:
				provinciaSel.add(formulario.getIdProvincia());
				poblacionSel.add(formulario.getIdPoblacion());
				parametro[0] = formulario.getIdProvincia();
				topBotones = "222";
				topTabla = "225";
				if (formulario.getVisible()!=null && formulario.getVisible().equals(ClsConstants.DB_TRUE)){
	                activarVisible="checked";
	            }else{
	                activarVisible="";
	            }
				deshabilitarVisible="disabled";
			}
		}
	
	//Boton borrar Procedimiento:
	FilaExtElement[] elems = null;
	if(!accion.equalsIgnoreCase("ver")){
	
		elems = new FilaExtElement[1];	
		elems[0]=new FilaExtElement("borrar","borrarProcedimiento",SIGAConstants.ACCESS_FULL);
	}	
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>		
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStrutsWithHidden.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>		

	<!-- Validaciones en Cliente -->
	<html:javascript formName="MantenimientoJuzgadoForm" staticJavascript="false" />  	

	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		// Asociada al boton Volver
		function accionCerrar() { 
			window.top.close();
		}	

		// Asociada al boton Reset
		function accionRestablecer() { 
			MantenimientoJuzgadoForm.reset();
			jQuery("#idProvincia").change();			
		}	

		// Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {
			sub();
				document.MantenimientoJuzgadoForm.telefono1.value=eliminarBlancos(trim(document.MantenimientoJuzgadoForm.telefono1.value));
				document.MantenimientoJuzgadoForm.telefono2.value=eliminarBlancos(trim(document.MantenimientoJuzgadoForm.telefono2.value));
				document.MantenimientoJuzgadoForm.fax1.value=eliminarBlancos(trim(document.MantenimientoJuzgadoForm.fax1.value));
				document.MantenimientoJuzgadoForm.movil.value=eliminarBlancos(trim(document.MantenimientoJuzgadoForm.movil.value));
				idPoblacion=document.MantenimientoJuzgadoForm.idPoblacion.value;

			if (validateMantenimientoJuzgadoForm(document.MantenimientoJuzgadoForm)){				
 					if(idPoblacion==''){					
						alert('<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.editar.poblacion"/>');
						fin();
						return false;						
	
				} else {
   					MantenimientoJuzgadoForm.modo.value = "<%=accion%>";
					MantenimientoJuzgadoForm.submit();
				}
 					
				} else {						
				fin();
				return false;			
			}						
		} 
	
		// Asociada al boton Nuevo
		function accionNuevo() {		
			document.forms[0].modo.value = "nuevoProcedimientoModal";
			var resultado = ventaModalGeneral(document.forms[0].name,"M");
			if (resultado && resultado=='MODIFICADO')
				refrescarLocal();
		}	
		
		function pulsarCheck(obj){
			   
		}
		
		function cargarChecksTodos(obj){
			
			var modulos = document.getElementsByName("chkModulos");
			
			for ( var i = 0; i < modulos.length; i++) {
				modulos[i].checked = obj.checked; 
			}
				
		}
		//Guardo los campos seleccionados
		function seleccionarFila(fila){
	    	var idInstitucionProcedimiento = 'oculto' + fila + '_' + 1;
		    var idProcedimiento = 'oculto' + fila + '_' + 2;
		    var idInstitucionJuzgado = 'oculto' + fila + '_' + 3;
		    var idJuzgado = 'oculto' + fila + '_' + 4;
		
			//Datos del elemento seleccionado:
			document.forms[0].idInstitucionProcedimiento.value = document.getElementById(idInstitucionProcedimiento).value;
			document.forms[0].idProcedimiento.value = document.getElementById(idProcedimiento).value;
			document.forms[0].idJuzgado.value = document.getElementById(idJuzgado).value;
			document.forms[0].idInstitucionJuzgado.value = document.getElementById(idInstitucionJuzgado).value;
		}
			
		// Asociada al boton Procedimiento
		function borrarProcedimiento(fila) {		
			seleccionarFila(fila);
			document.forms[0].modo.value = "borrarProcedimiento";
			document.forms[0].submit();
		}	
		
		function refrescarLocal() {
			document.forms[0].modo.value="recargarJuzgadoModal";
			document.forms[0].target = "_self";
			document.forms[0].submit();
		}

 		function darDeBaja (o) {
 			if (o.checked) {
 				MantenimientoJuzgadoForm.ponerBaja.value = "S";
			} else {
				MantenimientoJuzgadoForm.ponerBaja.value = "N";
			}
 		}
 		
 		function darVisibilidadMovil (o) {
 			if (o.checked) {
 				MantenimientoJuzgadoForm.ponerVisibilidadMovil.value = "S";
			} else {
				MantenimientoJuzgadoForm.ponerVisibilidadMovil.value = "N";
			}
 		} 	
 		
 		function checkDecano (o) {
 			if (o.checked) {
 				MantenimientoJuzgadoForm.putDecano.value = "S";
			} else {
				MantenimientoJuzgadoForm.putDecano.value = "N";
			}
 		}  		
 		
	</script>	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulitosDatos" class="titulitosDatos"><siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado"/></td>
		</tr>
	</table>

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<html:form action="/JGR_MantenimientoJuzgados.do" method="POST" target="submitArea">
		<html:hidden name="MantenimientoJuzgadoForm" property="modo" />
		<html:hidden name="MantenimientoJuzgadoForm" property="idJuzgado" />
		<html:hidden name="MantenimientoJuzgadoForm" property="idInstitucionJuzgado" />
		<html:hidden name="MantenimientoJuzgadoForm" property="idInstitucionProcedimiento" />
		<html:hidden name="MantenimientoJuzgadoForm" property="idProcedimiento" />	
		<html:hidden name="MantenimientoJuzgadoForm" property="registrosBorrar" />
		
		<input type="hidden" name="actionModal" value="">
	
		<table class="tablaCentralCamposGrande" align="center" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<siga:ConjCampos leyenda="gratuita.mantenimientoTablasMaestra.literal.juzgado">
						<table class="tablaCampos" border="0">	
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.nombre"/>&nbsp;(*)
								</td>
								<td class="labelText">
									<html:text name="MantenimientoJuzgadoForm" property="nombre" size="30" maxlength="200"  readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="general.baja"/>
	
								</td>
								<td class="labelTextValue">
									<input type="checkbox" name="ponerBaja" style="" onclick="darDeBaja(this);" <% if (modo.equalsIgnoreCase("VER")) { %>disabled<%}%> value="<%=ponerBaja%>" <%if (fechaBaja !=null && !fechaBaja.equals("")) {%>checked<%}%>>
									<%if (fechaBaja !=null && !fechaBaja.equals("")) {%>
										&nbsp;&nbsp;&nbsp; Baja desde: <%=fechaBaja%>
									<%}%>								
								</td>									
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.direccion"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoJuzgadoForm" property="direccion" size="30" maxlength="100"  readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoPostal"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoJuzgadoForm" property="codigoPostal" size="5" maxlength="5"  readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.provincia"/>&nbsp;(*)
								</td>
								<td class="labelText">
									<% if (modo.equalsIgnoreCase("VER")) { %>
											<html:text name="MantenimientoJuzgadoForm" property="provincia" styleClass="boxConsulta" readonly="<%=desactivado %>"  size="20"></html:text>
									<% } else { %>
											<siga:ComboBD nombre="idProvincia" tipo="provincia" clase="<%=estiloCombo%>" obligatorio="false"  elementoSel="<%=provinciaSel %>" accion="Hijo:idPoblacion" />
									<% } %>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoext"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoJuzgadoForm" property="codigoExt" size="10" maxlength="10"  readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.poblacion"/>&nbsp;(*)
								</td>
								<td class="labelText" >
									<% if (modo.equalsIgnoreCase("VER")) { %>
											<html:text name="MantenimientoJuzgadoForm" property="poblacion" styleClass="boxConsulta" readonly="<%=desactivado %>"  size="20"></html:text>
									<% } else { %>
												<% if (modo.equalsIgnoreCase("NUEVO")) { %>
														<siga:ComboBD nombre="idPoblacion" tipo="poblacion" clase="<%=estiloCombo%>" elementoSel="<%=poblacionSel %>" hijo="t" />
												<% } else { %>
														<siga:ComboBD nombre="idPoblacion" tipo="poblacion" clase="<%=estiloCombo%>" elementoSel="<%=poblacionSel %>" hijo="t" parametro="<%=parametro%>" />
												<% } %>
									<% } %>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codJuzgadoProcurador"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoJuzgadoForm" property="codProcurador" size="10" maxlength="10"  readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.telefono2"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoJuzgadoForm" property="telefono1" size="20" maxlength="20" readonly="<%=desactivado %>"  styleClass="<%=estilo%>" ></html:text>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.telefono1"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoJuzgadoForm" property="telefono2" size="20" maxlength="20" readonly="<%=desactivado %>"  styleClass="<%=estilo%>" ></html:text>
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.fax1"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoJuzgadoForm" property="fax1" size="20" maxlength="20"  readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.movil"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoJuzgadoForm" property="movil" size="20" maxlength="20"  readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.codigoInterno"/>
								</td>
								<td class="labelText">
									<html:text name="MantenimientoJuzgadoForm" property="codigoExt2" size="10" maxlength="10"  readonly="<%=desactivado %>" styleClass="<%=estilo%>"></html:text>
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.visibilidadMovil"/>
								</td>
								<td class="labelTextValue">
									<input type="checkbox" name="ponerVisibilidadMovil" style="" onclick="darVisibilidadMovil(this);" <% if (modo.equalsIgnoreCase("VER")) { %>disabled<%}%> value="<%=ponerVisibilidadMovil%>" <%if (visibleMovil !=null && visibleMovil.equals("1")) {%>checked<%}%>>
								</td>									
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.decano"/>
								</td>
								<td class="labelTextValue">
									<input type="checkbox" name="putDecano" style="" onclick="checkDecano(this);" <% if (modo.equalsIgnoreCase("VER")) { %>disabled<%}%> value="<%=esDecano%>" <%if (esDecano !=null && esDecano.equals("1")) {%>checked<%}%>>
								</td>								
							
							</tr>							
						</table>
					</siga:ConjCampos>	
				</td>
			</tr>
		</table>
	</html:form>	
	
	<siga:ConjBotonesAccion botones="C,Y,R" modo="<%=accion%>" modal="G" modo="<%=modo%>" titulo="gratuita.mantenimientoTablasMaestra.literal.procedimientos" clase="botonesSeguido"/>
		
	<siga:Table 
		name="tablaResultados"
		border="1"
		columnNames="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,gratuita.procedimientos.literal.codigo,gratuita.procedimientos.literal.nombre,gratuita.procedimientos.literal.importe,gratuita.procedimientos.literal.Jurisdiccion,"
		columnSizes="5,10,50,10,20,5">
				
<% 
		if (vProcedimientos != null && vProcedimientos.size()>0) {
			for (int i = 0; i < vProcedimientos.size(); i++) { 
				Hashtable hash = (Hashtable)vProcedimientos.get(i);
				if (hash != null) {
					String nombre = UtilidadesHash.getString (hash, ScsProcedimientosBean.C_NOMBRE);
					String importe = UtilidadesHash.getString (hash, ScsProcedimientosBean.C_PRECIO);
					String idInstitucionProcedimiento =  UtilidadesHash.getString(hash, "IDINSTITUCION_PROC");
					String idProcedimiento = UtilidadesHash.getString (hash, ScsJuzgadoProcedimientoBean.C_IDPROCEDIMIENTO);
					String idInstitucionJuzgado = UtilidadesHash.getString (hash, "IDINSTITUCION_JUZG");
					String idJuzgado = UtilidadesHash.getString (hash, ScsJuzgadoProcedimientoBean.C_IDJUZGADO);
					String codigo = UtilidadesHash.getString (hash, ScsProcedimientosBean.C_CODIGO);
					String jurisdiccion = UtilidadesHash.getString (hash, "JURISDICCION");
%>
					<tr>
						<td width="5"></td>
						<td width="10"></td>
						<td width="50"></td>
						<td width="10"></td>
						<td width="20"></td>
						<td width="5"></td>
					</tr>
					<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' visibleConsulta="no" visibleEdicion="no" visibleBorrado="no" pintarEspacio="no" botones=''  elementos="<%=elems%>"  modo="<%=modo%>" clase="listaNonEdit">
						
					
					
						<td align="center">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=idInstitucionProcedimiento%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_2" value="<%=idProcedimiento%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_3" value="<%=idInstitucionJuzgado%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_4" value="<%=idJuzgado%>">
							<input type="checkbox" id="chkModulos_<%=String.valueOf(i+1)%>"  name="chkModulos" >
						</td>
						<td align="center">
							
							<%=UtilidadesString.mostrarDatoJSP(codigo)%>
						</td>
						<td>
							<%=UtilidadesString.mostrarDatoJSP(nombre)%>
						</td>
						<td align="right">
							<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(importe))%>
						</td>	
						<td align="center">
							<%=UtilidadesString.mostrarDatoJSP(jurisdiccion)%>
						</td>
					</siga:FilaConIconos>
<%	
				}
			}
		} else {
%>				
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>	
<%
		}
%>	
	</siga:Table>
	<!-- FIN: CAMPOS -->

	 <% if (!modo.equalsIgnoreCase("nuevo")) {%>
	 	<siga:ConjBotonesAccion botones="bm,N"  modal="G" modo="<%=modo%>" clase="botonesDetalle" />
 	<% } %>
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
		
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	<script type="text/javascript">

	function borrarSeleccionados() {
		sub();
		var registrosBorrar = '';
		modulos = document.getElementsByName('chkModulos');
		for ( var j = 0; j < modulos.length; j++) {
			if(document.getElementById(modulos[j].id).checked){
			
				filaModulo = modulos[j].id.split("chkModulos_")[1];
				idInstitucion = document.getElementById("oculto"+filaModulo+"_1").value;
				idProcedimiento = document.getElementById("oculto"+filaModulo+"_2").value;
				idInstitucionJuzgado = document.getElementById("oculto"+filaModulo+"_3").value;
				idJuzgado = document.getElementById("oculto"+filaModulo+"_4").value;
				
				registrosBorrar = registrosBorrar + 
				idInstitucion + "," + 
				idProcedimiento + "," + 
				idInstitucionJuzgado + "," + 
				idJuzgado + "#" ; 
			}
		}
		if(registrosBorrar==''){
			alert('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
		}else{
			document.forms[0].registrosBorrar.value = registrosBorrar;
			document.forms[0].modo.value = "borrarProcedimientos";
			document.forms[0].submit();
		}
	}
		jQuery("#idTituloBotonera").css('width', '500px');
	</script>
</body>
</html>