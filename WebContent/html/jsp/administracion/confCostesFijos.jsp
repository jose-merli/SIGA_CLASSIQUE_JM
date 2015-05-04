<!DOCTYPE html>
<html>
<head>
<!-- confCostesFijos.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>

<%@ page import="com.siga.beans.ScsActuacionAsistCosteFijoBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>

<% 
	HttpSession ses = request.getSession();
    
    Vector<Hashtable<String,Object>> vDatos = (Vector<Hashtable<String,Object>>) request.getAttribute("vDatos");
    Integer modoC = Integer.parseInt(request.getAttribute("modoConsulta").toString());
	
    String sIncluirBajaLogica = "false";
	if(request.getAttribute("bIncluirRegistrosConBajaLogica")!=null){
		sIncluirBajaLogica = (String) request.getAttribute("bIncluirRegistrosConBajaLogica");
	}
    
	String botonera = (modoC!=1 ? "V,G" : "V");

    String editable = String.valueOf(request.getAttribute("editable"));
    String modo = (editable.equals("1") ? "Editar" : "Ver");
%>	
		
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>

	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<siga:Titulo titulo="administracion.catalogos.maestros.configuracion.costes" localizacion="menu.administracion"/>
</head>

<body onload="ajusteAltoBotones('mainWorkArea')">
	<html:form action="/ADM_GestionarTablasMaestras.do" method="POST" target="mainWorkArea">
		<input type="hidden" id="actionModal" name="actionModal" value="">
		<input type="hidden" property="id" styleId="id">
		<input type="hidden" id="regBajaLogica" name="regBajaLogica" value="<%=sIncluirBajaLogica%>">
		<html:hidden property="datosConf"/>
		<html:hidden property="modo" styleId="modo" value="<%=modo%>" />		
		<html:hidden property="registrosSeleccionados" styleId="registrosSeleccionados"/>
	</html:form>
	
	<table class="tablaTitulo" align="center">
		<tr>
			<td class="titulitos"><siga:Idioma key="general.boton.aniadirTipoAsistencia"/></td>
		</tr>
	</table>
	
	<siga:Table 
		name="tablaResultados" 
		border="1"
		columnNames=",
	   				administracion.catalogos.maestros.literal.tipos.asistencias,
	   				administracion.catalogos.maestros.literal.tipo.actuaciones,
	   				administracion.catalogos.costesFijos.importe"
		columnSizes="5,32,33,15">
<%	
		if (vDatos!=null && vDatos.size()>0) {
			for (int i=1; i<=vDatos.size(); i++) {
				Hashtable<String,Object> hDatos = (Hashtable<String,Object>) vDatos.get(i-1);
				
				if (hDatos!= null) {
					Integer idInstitucion = UtilidadesHash.getInteger(hDatos, ScsActuacionAsistCosteFijoBean.C_IDINSTITUCION);
					Integer idTipoAsist = UtilidadesHash.getInteger(hDatos, ScsActuacionAsistCosteFijoBean.C_IDTIPOASISTENCIA);
					Integer idTipoAct = UtilidadesHash.getInteger(hDatos, ScsActuacionAsistCosteFijoBean.C_IDTIPOACTUACION);
					String dsTipoAct = UtilidadesHash.getString(hDatos,  "DSTIPOACTUACION");
					String dsTipoAsist = UtilidadesHash.getString(hDatos,  "DSTIPOASISTENCIA");
					String importeCosteFijo = UtilidadesHash.getString(hDatos, "IMPORTECOSTE");
					importeCosteFijo = importeCosteFijo.replace(",", ".");
					
					String vCheck = idTipoAsist + "||" + idTipoAct;
					boolean isChecked = (importeCosteFijo!=null && !importeCosteFijo.equals("") && !importeCosteFijo.equals("0"));
%>		
				
					<siga:FilaConIconos fila='<%=""+i%>' botones="false" visibleBorrado="false" visibleConsulta="false" visibleEdicion="false" pintarEspacio="no" clase="listaNonEdit">			
						<td align="center">
							<input type="checkbox" value="<%=vCheck%>" id="<%=""+i%>" name="sel" <%if (isChecked){%>checked<%}%> <%if (modoC==1){%>disabled<%}%>onclick="pulsarCheck(this,<%=""+i%>)" />
						</td>
			
						<td>
							<input type="hidden" id="oculto<%=""+i%>_1" value="<%=idInstitucion%>"> 
				 			<%=UtilidadesString.mostrarDatoJSP(dsTipoAsist)%>
						</td>
						<td><%=UtilidadesString.mostrarDatoJSP(dsTipoAct)%></td>
						
<%
						if (modoC!=1) {
%>			
							<td align="center"><input type="text" name="importeCosteFijo" id="importeCosteFijo_<%=""+i%>" value="<%=UtilidadesString.formatoImporte(importeCosteFijo)%>" class="box" style="text-align:right;<%if (!isChecked){%>display:none<%}%>" /></td>
<%
						} else {
%>
							<td align="right"><%=UtilidadesString.formatoImporte(importeCosteFijo)%></td>
<%
						}
%>
					</siga:FilaConIconos>	
<%
				} // if
			}//for		

		} else {
%>
			<tr class="notFound">
				<td class="titulitos"><siga:Idioma key="messages.noRecordFound" /></td>
			</tr>
<%
		}
%>
	</siga:Table>

 	 <siga:ConjBotonesAccion botones="<%=botonera%>" modal=""/> 

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>

	<script language="javascript">
		function accionVolver() {		
			sub();
			listadoTablasMaestrasForm.target = "mainWorkArea";
			listadoTablasMaestrasForm.modo.value = "<%=modo%>";
			listadoTablasMaestrasForm.submit(); 
		}
		
		function accionGuardar(){
			sub();
			
			var lista = "";
			var checks = document.getElementsByName("sel");
		  	for (var i=0; i<checks.length; i++) {
				if (checks[i].checked) {
					var importe = jQuery("#importeCosteFijo_" + checks[i].id).val();
					importe = convertirAFormato(importe);
					if (importe<=0) {						
						var mensaje = '<siga:Idioma key="administracion.catalogos.costesFijos.error.importe"/>';
						alert(mensaje);
						fin();
						return false;
					}
					lista = lista + checks[i].value + "||" + importe + ";";		
				} 
			}
		  	
			if (lista.length>0) {
				listadoTablasMaestrasForm.target = "submitArea";
				listadoTablasMaestrasForm.modo.value = "gestionarAsistencia";
				listadoTablasMaestrasForm.datosConf.value = lista;
				listadoTablasMaestrasForm.submit(); 
				
			} else {				
				var mensaje = '<siga:Idioma key="general.message.seleccionar"/>';
				alert(mensaje);
				fin();
				return false;
			}
		}
			
		function convertirAFormato(numero){
			numero = numero.trim();
			while (numero.indexOf(" ",0)>=0) {
				numero = numero.replace(" ","");
			}		
			
			while (numero.indexOf(",",0)>=0) {
				numero = numero.replace(",",".");
			}				
			
			while (numero.indexOf(".",0)>=0 && numero.length-numero.indexOf(".",0)>3) {
				numero = numero.replace(".", "");
			}  			
				
			var numeroNumber = new Number(numero);
			
			if (isNaN(numeroNumber)) {
				return "0";
			}
			
			return numero;	
		}		
	
		function pulsarCheck(obj,fila){
			if (!obj.checked){
				jQuery("#importeCosteFijo_"+fila).hide();   		
			} else {
				jQuery("#importeCosteFijo_"+fila).show();
			}
		}
		
		function refrescarLocal() {
			accionVolver();
		}		
	</script>
</body>
</html>