<!DOCTYPE html>
<html>
<head>
<!-- consultaRecibos.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.beans.FacFacturaBean"%>
<%@ page import="com.siga.beans.FacFacturaIncluidaEnDisqueteBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitive"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>

<bean:define id="registrosSeleccionados" name="DevolucionesManualesForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="DevolucionesManualesForm" property="datosPaginador" type="java.util.HashMap"/>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	String idioma = userBean.getLanguage().toUpperCase();
	
	/** PAGINADOR ***/
	String paginaSeleccionada="0", totalRegistros="0", registrosPorPagina="0";	
	Vector resultado = new Vector();	
	
	if (datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")) {
  		resultado = (Vector)datosPaginador.get("datos");
 
  		PaginadorCaseSensitive paginador = (PaginadorCaseSensitive)datosPaginador.get("paginador");
		paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
 		totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
 		registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());
	}	 
	
	String action = app + "/FAC_DevolucionesManual.do?noReset=true";
	
	Vector vMotivos = (Vector) request.getAttribute("vMotivos"); 	
	String idMotivoDevolucion = (String) request.getAttribute("motivoDevolucion"); 	
	String separador = "||";
%>


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body onload="cargarChecks();checkTodos();">
	<html:form action="/FAC_DevolucionesManual.do?noReset=true" method="POST" style="display:none">
		<html:hidden styleId="modo"  property="modo" value=""/>
		<html:hidden property="recibos"/>
		<html:hidden styleId="registrosSeleccionados"  property="registrosSeleccionados" />
		<html:hidden styleId="datosPaginador"  property="datosPaginador" />
		<html:hidden styleId="seleccionarTodos"  property="seleccionarTodos" />
		<input type="hidden" name="actionModal" id="actionModal"  value="">
	</html:form>
		
	<siga:Table 
	   name="tablaResultados"
	   border="1"
	   columnNames="<input type='checkbox' name='checkGeneral'  id='checkGeneral' onclick='cargarChecksTodos(this)'/>,	   						
	   						facturacion.devolucionManual.numeroFactura,
	   						facturacion.devolucionManual.numeroRecibo,	   						
	   						facturacion.devolucionManual.numeroRemesa,
	   						facturacion.devolucionManual.titular,
	   						facturacion.devolucionManual.importe,
	   						facturacion.devolucionManual.motivos,"
	   columnSizes = "4,15,8,8,25,8,26,6">
<%
		if ((resultado != null) && (resultado.size() > 0)) { 
			for (int i = 0; i < resultado.size(); i++) { 							
				Row fila = (Row)resultado.elementAt(i);
				Hashtable recibo = (Hashtable) fila.getRow();
				
				if (recibo != null) { 
					String nombre = UtilidadesHash.getString(recibo, CenPersonaBean.C_NOMBRE);
					String apellidos1 = UtilidadesHash.getString(recibo, CenPersonaBean.C_APELLIDOS1);
					String apellidos2 = UtilidadesHash.getString(recibo, CenPersonaBean.C_APELLIDOS2);
					String nombreFinal = ((nombre!=null)?nombre:"") + " " + ((apellidos1!=null)?apellidos1:"") + " " + ((apellidos2!=null)?apellidos2:"");
					String idRecibo = UtilidadesHash.getString(recibo, FacFacturaIncluidaEnDisqueteBean.C_IDRECIBO);
					String idDisqueteCargos = UtilidadesHash.getString(recibo, FacFacturaIncluidaEnDisqueteBean.C_IDDISQUETECARGOS);
					String numFactura = UtilidadesHash.getString(recibo, FacFacturaBean.C_NUMEROFACTURA);
					String importe = UtilidadesHash.getString(recibo, FacFacturaIncluidaEnDisqueteBean.C_IMPORTE);
					String idFactura = UtilidadesHash.getString(recibo, FacFacturaIncluidaEnDisqueteBean.C_IDFACTURA);					
					String idFacturaIncluidaEnDisquete = UtilidadesHash.getString(recibo, FacFacturaIncluidaEnDisqueteBean.C_IDFACTURAINCLUIDAENDISQUETE);
		
					// boton de ver factura
					FilaExtElement[] elems = new FilaExtElement[1];
					elems[0]=new FilaExtElement("verfactura", "verfactura", SIGAConstants.ACCESS_READ);
%>
			
					<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="" visibleConsulta="false" visibleEdicion="false" visibleBorrado="false" elementos="<%=elems%>" pintarEspacio="no"  clase="listaNonEdit"> 
						<td align="center">
<%
							boolean isChecked = false;
							String idMotivoFinal = idMotivoDevolucion;

							for (int j=0; j<registrosSeleccionados.size(); j++) {
								Hashtable hRegistroSeleccionado = (Hashtable) registrosSeleccionados.get(j);
								String sRegistroSeleccionado = UtilidadesHash.getString(hRegistroSeleccionado, "CLAVE");
								String[] aRegistroSeleccionado = sRegistroSeleccionado.split("\\|\\|");
								String idDisqueteCargosRS = aRegistroSeleccionado[0];
								String idFacturaIncluidaEnDisqueteRS = aRegistroSeleccionado[1];
								String idMotivoDevolucionRS = aRegistroSeleccionado[4];
								
								if (idDisqueteCargos.equals(idDisqueteCargosRS) && idFacturaIncluidaEnDisquete.equals(idFacturaIncluidaEnDisqueteRS)) {
									if (!idMotivoDevolucionRS.equalsIgnoreCase("NULL")) {
										idMotivoFinal = idMotivoDevolucionRS;
									}
									isChecked = true;
									break;
								}
							}
														
							if (isChecked) {								
%>								
								<input type="checkbox" value="<%=(i+1)%>" id="checkDevolucion" name="checkDevolucion_<%=(i+1)%>" checked onclick="pulsarCheck(this)">
<%
							} else {
%>
								<input type="checkbox" value="<%=(i+1)%>" id="checkDevolucion" name="checkDevolucion_<%=(i+1)%>" onclick="pulsarCheck(this)">
<%
							}
%>						
						</td>						
						<td><%=UtilidadesString.mostrarDatoJSP(numFactura)%></td>
						<td><%=UtilidadesString.mostrarDatoJSP(idRecibo)%></td>
						<td><%=UtilidadesString.mostrarDatoJSP(idDisqueteCargos)%></td>						
						<td><%=UtilidadesString.mostrarDatoJSP(nombreFinal)%></td>
						<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesString.formatoImporte(importe))%>&nbsp;&euro;</td>
						<td>
							<input type="hidden" id="devolucionManual_<%=(i+1)%>_1" value="<%=idDisqueteCargos%>">
							<input type="hidden" id="devolucionManual_<%=(i+1)%>_2" value="<%=idFacturaIncluidaEnDisquete%>">
							<input type="hidden" id="devolucionManual_<%=(i+1)%>_3" value="<%=idFactura%>">
							<input type="hidden" id="devolucionManual_<%=(i+1)%>_4" value="<%=idRecibo%>">
							
							<select id="devolucionManual_<%=(i+1)%>_5" class="boxCombo" onchange="cambiarMotivo(<%=(i+1)%>)" style="width:250px; <%if (!isChecked) {%>display:none<%}%>"> 
<%
								for (int j=0; j<vMotivos.size(); j++) {
									Hashtable hMotivo = (Hashtable) vMotivos.get(j);
									String sMotivoId = UtilidadesHash.getString(hMotivo, "ID");
									String sMotivoDescripcion = UtilidadesHash.getString(hMotivo, "DESCRIPCION");
									
									if (sMotivoId.equals(idMotivoFinal)) {
%>
										<option value="<%=sMotivoId%>" selected><%=sMotivoDescripcion%></option>
<%								
									} else {
%>
										<option value="<%=sMotivoId%>"><%=sMotivoDescripcion%></option>
<%																			
									}
								}
%>							
							</select>
						</td>
					</siga:FilaConIconos>
<%	 		 
				} // if
			}  // for  
	 	}  else {
%>
 			<tr class="notFound">
	   			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>		
<% 
		} 
%>
	</siga:Table>
							
<%
	if ( datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")) {
		String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0 : registrosSeleccionados.size()));
%>
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
						registrosPorPagina="<%=registrosPorPagina%>" 
						paginaSeleccionada="<%=paginaSeleccionada%>"
						registrosSeleccionados="<%=regSeleccionados%>" 
						idioma="<%=idioma%>"
						modo="buscar"								
						clase="paginator" 
						divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 0px; left: 0px"
						distanciaPaginas=""
						action="<%=action%>" />					
<%
	}
%>

<script>
	// Refrescar
	function refrescarLocal(){ 		
		parent.buscar();
	}	
    
	function verfactura(fila) {
		var datos = document.getElementById('tablaDatosDinamicosD');
		datos.value = "";
		
		var idFactura = jQuery("#devolucionManual_" + fila + "_3");
		if (jQuery(idFactura).exists()) {
			datos.value = jQuery(idFactura).val();
		}
					   
	   	var auxTarget = document.DevolucionesManualesForm.target;
	   	document.DevolucionesManualesForm.target = "mainWorkArea"
	   	document.DevolucionesManualesForm.recibos.value = jQuery("#registrosSeleccionados").val();
	   	document.DevolucionesManualesForm.modo.value = "ver";
	   	document.DevolucionesManualesForm.submit();
	   	document.DevolucionesManualesForm.target = auxTarget;
	}
	
	var ObjArray = new Array();
	
	Array.prototype.indexOf = function(s) {
		for (var x=0; x<this.length; x++) 
			if (this[x].indexOf(s) > -1) 
				return x;
			return -1;
	};
	
	function checkTodos() {
		var vCheckGeneral = jQuery("input[id=checkDevolucion]:not(:checked)").exists();
		jQuery("#checkGeneral").prop('checked', !vCheckGeneral);	
   	}		
   
	function pulsarCheck(obj) {
		var fila = obj.value;		
		var idDisqueteCargos = jQuery("#devolucionManual_" + fila + "_1").val();
		var idFacturaIncluidaEnDisquete = jQuery("#devolucionManual_" + fila + "_2").val();
		var idFactura = jQuery("#devolucionManual_" + fila + "_3").val();
		var idRecibo = jQuery("#devolucionManual_" + fila + "_4").val();
		var idMotivo = jQuery("#devolucionManual_" + fila + "_5").val();
		var valorPK = idDisqueteCargos + "<%=separador%>" + idFacturaIncluidaEnDisquete + "<%=separador%>";
		var valores = idDisqueteCargos + "<%=separador%>" + idFacturaIncluidaEnDisquete + "<%=separador%>" + idFactura + "<%=separador%>" + idRecibo + "<%=separador%>" + idMotivo;
		
		if (!obj.checked) {	
			var indice = ObjArray.indexOf(valorPK);
			if (indice >= 0) {
				ObjArray.splice(indice,1);
			}
			jQuery("#devolucionManual_" + fila + "_5").hide();
			
		} else {
			var indice = ObjArray.indexOf(valorPK);
			if (indice >= 0) {
				ObjArray.splice(indice,1);
			}
			ObjArray.push(valores);
			jQuery("#devolucionManual_" + fila + "_5").show();
		}
		
		seleccionados1 = ObjArray; // Variable del paginador
		jQuery("#registrosSeleccionados").val(ObjArray);
		jQuery("#registrosSeleccionadosPaginador").val(ObjArray.length); // Variable del paginador
		checkTodos();
	}
	
	function cargarChecks(){
<%
		if (registrosSeleccionados!=null) {			
   			for (int i=0; i<registrosSeleccionados.size(); i++) {	   		 	
   				Hashtable hRegistroSeleccionado = (Hashtable) registrosSeleccionados.get(i);
   				String sRegistroSeleccionado = UtilidadesHash.getString(hRegistroSeleccionado, "CLAVE");
%>
				var aux = '<%=sRegistroSeleccionado%>';
				ObjArray.push(aux);
<%
			} 
   		}
%>
  
		seleccionados1 = ObjArray; // Variable del paginador
		jQuery("#registrosSeleccionados").val(ObjArray);
		jQuery("#registrosSeleccionadosPaginador").val(ObjArray.length); // Variable del paginador
		
		parent.cerrarDialog();
	}
	
	function cargarChecksTodos(obj) {  		
  		var conf = confirm('<siga:Idioma key="paginador.message.marcarDesmarcar"/>');
   	   	
	   	if (conf) {			
		   	if (obj.checked){
		   		parent.seleccionarTodos('<%=paginaSeleccionada%>');			   	 	
				
			} else {								
				jQuery("input[id=checkDevolucion]:checked").prop('checked', false);	
				
				ObjArray = new Array();
				seleccionados1 = ObjArray; // Variable del paginador
			   	jQuery("#registrosSeleccionados").val(ObjArray);
				jQuery("#registrosSeleccionadosPaginador").val(ObjArray.length); // Variable del paginador
			 }
	   	  
		} else {
  			if (obj.checked) {
  				jQuery("input[id=checkDevolucion]:not(:checked)").each(function(){
					jQuery(this).prop('checked', true);
					pulsarCheck(this);
				});	  				
			   	
			} else {
				jQuery("input[id=checkDevolucion]:checked").each(function(){
					jQuery(this).prop('checked', false);
					pulsarCheck(this);
				});
			}
		}
	 }
	
	function cambiarMotivo(fila) {
		pulsarCheck(jQuery("input[name=checkDevolucion_" + fila + "]")[0]);
	}
</script>
	

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>