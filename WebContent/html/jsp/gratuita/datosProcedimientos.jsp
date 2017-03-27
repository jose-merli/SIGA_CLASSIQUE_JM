<!DOCTYPE html>
<%@page import="com.siga.ws.CajgConfiguracion"%>
<html>
<head>
<!-- datosProcedimientos.jsp -->

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
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri = "c.tld" 				prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.GstDate"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");

	//recogemos los datos
	Hashtable resultado = (Hashtable) request.getAttribute("resultado");
	Vector v = (Vector) request.getAttribute("acreditaciones");

	//variables quese van a mostrar en la jsp
	String nombre = "", importe = "", idProc = "", idJurisdiccion = "", codigo = "",codigoExt = "", complemento = "", permitirAniadirLetrado="";
	String fechaInicio="", fechaFin="";

	//inicializamos los valores
	try {
		nombre = (String) resultado.get(ScsProcedimientosBean.C_NOMBRE);
		importe = (String) resultado.get(ScsProcedimientosBean.C_PRECIO);
		idProc = (String) resultado.get(ScsProcedimientosBean.C_IDPROCEDIMIENTO);
		idJurisdiccion = (String) resultado.get(ScsProcedimientosBean.C_IDJURISDICCION);
		codigo = (String) resultado.get(ScsProcedimientosBean.C_CODIGO);
		codigoExt = (String) resultado.get(ScsProcedimientosBean.C_CODIGOEXT);
		complemento = (String) resultado.get(ScsProcedimientosBean.C_COMPLEMENTO);
		permitirAniadirLetrado = (String) resultado.get(ScsProcedimientosBean.C_PERMITIRANIADIRLETRADO);
		fechaInicio = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),(String)resultado.get(ScsProcedimientosBean.C_FECHADESDEVIGOR)));
		fechaFin = (String)resultado.get(ScsProcedimientosBean.C_FECHAHASTAVIGOR);
		if(!fechaFin.equals("")){
			fechaFin = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),(String)resultado.get(ScsProcedimientosBean.C_FECHAHASTAVIGOR)));
		}
	} catch (Exception e) {
	}

	//recuperamos el modo de acceso
	String modo = "Modificar";
	if ((nombre == null) || (nombre.equals("")))
		modo = "Insertar";
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<html:javascript formName="MantenimientoProcedimientosForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
</head>

<body>
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="gratuita.procedimientos.mantenimiento.cabecera"/>
			</td>
		</tr>
	</table>

	<!-- INICIO: CAMPOS -->
	<table  class="tablaCentralCamposMedia" cellspacing=0 cellpadding=0 align="center" border="0">
		<html:javascript formName="MantenimientoProcedimientosForm" staticJavascript="false" />
		
		<html:form action="/JGR_MantenimientoProcedimientos.do" method="POST" target="submitArea">
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "idProcedimiento" value = "<%=idProc%>"/>				
			<html:hidden property = "idAcreditacion" value = ""/>
			<html:hidden property = "porcentaje" value = ""/>	
			<html:hidden property = "nigNumProcedimiento" value = ""/>			
			<html:hidden property = "refresco" value = ""/>
			<html:hidden property = "codExtAcreditacion" value = ""/>
			<html:hidden property = "codSubtarifa" value = ""/>
			<html:hidden property = "idPretension" value = ""/>
			<html:hidden property = "datosMasivos" />
			

			<tr>				
				<td>
					<siga:ConjCampos leyenda="gratuita.procedimientos.leyenda">
						<table class="tablaCampos" align="center" border="0" width="100%">
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.procedimientos.literal.nombre" />&nbsp;(*)
								</td>
								<td class="labelText" colspan="3">
									<html:text name="MantenimientoProcedimientosForm" property="nombre" size="80" maxlength="100" styleClass="box" readonly="false" value="<%=nombre%>" />
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.procedimientos.literal.codigo" />
								</td>
								<td class="labelText">
									<html:text name="MantenimientoProcedimientosForm" property="codigo" maxlength="20" styleClass="box" readonly="false" value="<%=codigo%>" />
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.procedimientos.literal.codigo" />&nbsp;Ext.
								</td>
								<td class="labelText">
									<html:text name="MantenimientoProcedimientosForm" property="codigoExt" maxlength="20" size="11" styleClass="box" readonly="false" value="<%=codigoExt%>" />
								</td>
								
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.procedimientos.literal.importe" />&nbsp;(*)
								</td>
								<td class="labelText" >
									<html:text name="MantenimientoProcedimientosForm" property="importe" maxlength="11" styleClass="boxNumber" readonly="false" value="<%=UtilidadesNumero.formatoCampo(importe)%>" />&nbsp;&euro;
								</td>
								<td class="labelText">
									<siga:Idioma key="gratuita.procedimientos.literal.complemento" />
								</td>
								<td class="labelText">
									<input type="checkbox" name="complemento" value="<%=ClsConstants.DB_TRUE%>" <%if(complemento.equals(ClsConstants.DB_TRUE)){%> checked <%}%> />
								</td>
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.procedimientos.literal.fechainicio" />&nbsp;(*)
								</td>
								<td class="labelText">
									<siga:Fecha nombreCampo="fechaDesdeVigor" valorInicial="<%=fechaInicio%>" posicionX="100" posicionY="100"></siga:Fecha>							
								</td>
					
								<td class="labelText">
									<siga:Idioma key="gratuita.procedimientos.literal.fechafin" />&nbsp;
									
								</td>
								<td class="labelText">
									<siga:Fecha nombreCampo="fechaHastaVigor" valorInicial="<%=fechaFin%>" posicionX="100" posicionY="100"></siga:Fecha>
								</td>
								
							</tr>
							
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.procedimientos.literal.Jurisdiccion" />&nbsp;(*)
								</td>
								<td class="labelText">
<%
									ArrayList juris = new ArrayList();
									juris.add(idJurisdiccion);
%> 
									<siga:ComboBD nombre="jurisdiccion" ancho="200" tipo="jurisdiccionSCJS" clase="boxCombo" obligatorio="true" elementoSel="<%=juris%>"/>
								</td>
															
								<td class="labelText">
									<siga:Idioma key="gratuita.procedimientos.literal.permitirAniadirLetrado" />&nbsp;
								</td>
								<td class="labelText">
									<input type="checkbox" name="permitirAniadirLetrado" value="<%=ClsConstants.DB_TRUE%>" <%if(permitirAniadirLetrado.equals(ClsConstants.DB_TRUE)){%> checked <%}%> />
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td> 
			</tr>
		</html:form>	
	</table>
	
	<siga:ConjBotonesAccion botones="G,C" clase="botonesSeguido" modal="M" titulo="gratuita.procedimientos.literal.acreditaciones"/>
<c:set var="fixedHeight" value="92%" />
<%
 	if (request.getAttribute("PCAJG_TIPO")!= null && request.getAttribute("PCAJG_TIPO").toString().equals(""+CajgConfiguracion.TIPO_CAJG_TXT_ALCALA)) {
%>
	<c:set var="fixedHeight" value="50%" />
<%} %>

	<siga:Table 
		name="tablaResultados"
		border="1"
		columnNames="gratuita.procedimientos.literal.acreditacion,gratuita.procedimientos.literal.porcentaje,gratuita.procedimientos.acreditacion.literal.nigNumeroProcedimiento,"
		columnSizes="42,22,20,16"
		fixedHeight="${fixedHeight}"
		
		modal="P">

<%
		if ((v != null) && (v.size() > 0)) {
			for (int i = 0; i < v.size(); i++) {
				Hashtable hash = (Hashtable) v.get(i);
				if (hash != null) {
					String acreDescripcion = UtilidadesHash.getString(hash, ScsAcreditacionBean.C_DESCRIPCION);
					Integer acrePorcentaje = UtilidadesHash.getInteger(hash, ScsAcreditacionProcedimientoBean.C_PORCENTAJE);
					Integer idAcreditacion = UtilidadesHash.getInteger(hash, ScsAcreditacionProcedimientoBean.C_IDACREDITACION);
					Integer idInstitucion = UtilidadesHash.getInteger(hash, ScsAcreditacionProcedimientoBean.C_IDINSTITUCION);
					String idProcedimiento = UtilidadesHash.getString(hash, ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO);
					Integer nigNumeroProcedimiento = UtilidadesHash.getInteger(hash, ScsAcreditacionProcedimientoBean.C_NIG_NUMPROCEDIMIENTO);
%>
					<siga:FilaConIconosExtExt fila='<%=String.valueOf(i+1)%>' visibleConsulta="no" botones='E,B'  modo='<%=modo%>' clase="listaNonEdit" nombreTablaPadre="tablaResultados">
						<td>
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=idAcreditacion.intValue()%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_2" value="<%=idInstitucion.intValue()%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_3" value="<%=idProcedimiento%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_4" value="detalleAcreditacion">
							<%=UtilidadesString.mostrarDatoJSP(acreDescripcion)%>
						</td>
						<td align="right"><%=acrePorcentaje.intValue()%></td>
						<td align="center">
							<%		
								if(nigNumeroProcedimiento == 1){ %>
									<input type="checkbox" id ="nigNumeroProcedimientoCheck" name="nigNumeroProcedimientoCkeck" checked="checked" disabled="disabled" />
							<% 	}else{ %>
									<input type="checkbox" id ="nigNumeroProcedimientoCheck" name="nigNumeroProcedimientoCkeck" disabled="disabled"/>
							<% 	}
							%>
						
						</td>
					</siga:FilaConIconosExtExt>
<%
				}
			}
		} else { // No hay registros 
%>
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
<%
		}
%>
	</siga:Table>
	 
<%
 	if (!modo.equalsIgnoreCase("Insertar")) {
%>
	 	<table class="botonesSeguido" id="idBotonesAccion"  align="center">
			<tr>
			<td class="tdBotones" style="width:900px;">
			&nbsp;
			</td>
			<td class="tdBotones">
			<input type="button" alt="Nuevo"  id="idButton" onclick="return accionNuevoAcreditacion();" class="button" name="idButton" value="Nuevo">
			</td>
			</tr>
			</table>
<%
 	}
%>

<%
 	if (request.getAttribute("PCAJG_TIPO")!= null && request.getAttribute("PCAJG_TIPO").toString().equals(""+CajgConfiguracion.TIPO_CAJG_TXT_ALCALA)) {
%>

<table  class="tablaCentralCamposMedia" cellspacing=0 cellpadding=0 align="center" border="0">
<tr><td></td></tr>
</table>


<table class="botonesSeguido" id="idBotonesAccion"  align="center">
<tr>
<td class="titulitos" id="idTituloBotonera" > 
<siga:Idioma key="gratuita.actuacionesDesigna.literal.pretensiones"/>

</td>
<td class="tdBotones" style="width:900px;">
&nbsp;
</td>
<td class="tdBotones">

</td>
<td class="tdBotones">

</td>
</tr>
</table>
	
<%
 	if (!modo.equalsIgnoreCase("Insertar")) {
%>
	 	<siga:ConjBotonesAccion botones="bm,N" modal="M" />
<%
 	}
%>
	<siga:Table 
		name="tablaPretensiones"
		border="1"
		fixedHeight="150"
		columnNames="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,gratuita.procedimientos.literal.codigo,gratuita.procedimientos.literal.nombre,"
		columnSizes="5,10,50,5">
	   		  
	    <!-- INICIO: ZONA DE REGISTROS -->
	    <c:choose>
			<c:when test="${empty pretensiones}">
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
			</c:when>
			
			<c:otherwise>
	  			<c:forEach items="${pretensiones}"	var="pretension" varStatus="status">
	  				<siga:FilaConIconosExtExt fila='${status.count}'
  						botones=""
  						pintarEspacio="no"
  						visibleEdicion = "no"
  						visibleBorrado = "no"
  						visibleConsulta = "no"
  						elementos="${elementosFilaPretensiones}"
  						nombreTablaPadre="tablaPretensiones"
  						clase="listaNonEdit"
  						
  						>
						
						<td align="center">
							<input type="hidden" id ="idPretension_${status.count}" value ="${pretension.IDPRETENSION}"/>	
		  					<input type="hidden" id ="idInstitucion_${status.count}" value ="${pretension.IDINSTITUCION}"/>
							<input type="checkbox" id="chkPretension_${status.count}"  name="chkPretension" >
						</td>
						
						<td align="center">
		  					
		  					<c:out value="${pretension.CODIGOEXT}"/>
							
						</td>
						<td align="left">
							<c:out value="${pretension.DESCRIPCION}"/>
						</td>
					</siga:FilaConIconosExtExt>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	<!-- FIN: ZONA DE REGISTROS -->
	</siga:Table>
<%
 	}
%>
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		// Asociada al boton GuardarCerrar
		function accionGuardar() {
			sub();		
			if (validateMantenimientoProcedimientosForm(document.MantenimientoProcedimientosForm)){
				if(validarFecha()){
					document.forms[0].importe.value=document.forms[0].importe.value.replace(/,/,".");
					document.forms[0].modo.value="<%=modo%>";
					document.forms[0].submit();
					window.top.returnValue="MODIFICADO";
				}else{					
					fin();
					return false;
				}
				
			} else{
			
				fin();
				return false;			
			}
		}

		function validarFecha() {
			var fechaIni = trim(document.getElementById("fechaDesdeVigor").value);
			var fechaFin = trim(document.getElementById("fechaHastaVigor").value);

			if (trim(document.getElementById("fechaDesdeVigor").value)=="") {
				alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="gratuita.procedimientos.literal.fechainicio"/>');
			   	return false;
			}
			
			if (compararFecha (fechaIni, fechaFin) == 1) {
				mensaje='<siga:Idioma key="messages.fechas.rangoFechas"/>';
				alert(mensaje);
				return false;
			}		
			return true;	
		}		
		
		// Asociada al boton Cerrar
		function accionCerrar() {		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("NORMAL");
			//window.top.returnValue="MODIFICADO";
			//window.top.close();
		}

/*
		// Asociada al boton Restablecer
		function accionRestablecer() {		
			document.forms[0].reset();
		}
*/		

		function cargarChecksTodos(obj){
			
			var pretensiones = document.getElementsByName("chkPretension");
			
			for ( var i = 0; i < pretensiones.length; i++) {
				pretensiones[i].checked = obj.checked; 
			}
				
		}

		// Asociada al boton Nuevo
		function accionNuevoAcreditacion() {		
			document.forms[0].modo.value = "nuevoAcreditacion";
			var resultado = ventaModalGeneral(document.forms[0].name,"P");
			if (resultado != null && (resultado[0]== 1)) {
				document.forms[0].idAcreditacion.value = resultado[1];
				document.forms[0].porcentaje.value = resultado[2];
				document.forms[0].nigNumProcedimiento.value = resultado[3];
				document.forms[0].codExtAcreditacion.value = resultado[4];
				document.forms[0].codSubtarifa.value = resultado[5];
				
				document.forms[0].modo.value = "insertarAcreditacion";
				document.forms[0].submit();
			}
		}	
		
		function refrescarLocal() {
			document.forms[0].target="modal";
			document.forms[0].refresco.value="refresco";
			document.forms[0].modo.value="editar";
			document.forms[0].submit();
		}	
		
		function accionNuevo() {		
	
			document.forms[0].modo.value = "nuevoPretensionModal";
			var resultado = ventaModalGeneral(document.forms[0].name,"M");
			if (resultado && resultado=='MODIFICADO')
				refrescarLocal();
			
			
		}	
		function borrarPretension(fila, id) {
			
			if (confirm('<siga:Idioma key="messages.deleteConfirmation"/>')) {
				sub();
			   	var datos;
				preparaDatos(fila, id);
				var auxTarget = document.forms[0].target;
				var pretension = 'idPretension_'+fila;
				document.forms[0].idPretension.value = document.getElementById(pretension).value;
			   	document.forms[0].target="submitArea";
			   	document.forms[0].modo.value = "borrarPretension";
			   	document.forms[0].submit();
			   	document.forms[0].target=auxTarget;
		 	} else {
		 		fin(); 
		 	} 
			
		}
		 
		function borrarSeleccionados() {
			sub();
			var registrosBorrar = '';
			pretensiones = document.getElementsByName('chkPretension');
			for ( var j = 0; j < pretensiones.length; j++) {
				if(document.getElementById(pretensiones[j].id).checked){
				
					filaModulo = pretensiones[j].id.split("chkPretension_")[1];
					idInstitucion = document.getElementById("idInstitucion_"+filaModulo).value;
					idProcedimiento = document.forms[0].idProcedimiento.value;
					idPretension = document.getElementById("idPretension_"+filaModulo).value;

  					
					
					registrosBorrar = registrosBorrar + 
					idInstitucion + "," + 
					idProcedimiento + "," + 
					idPretension + "#" ; 
				}
			}
			if(registrosBorrar==''){
				alert('<siga:Idioma key="general.message.seleccionar"/>');
				fin();
			}else{
				var auxTarget = document.forms[0].target;
				document.forms[0].target="submitArea";
				document.forms[0].datosMasivos.value = registrosBorrar;
				document.forms[0].modo.value = "borrarPretensiones";
				document.forms[0].submit();
				document.forms[0].target=auxTarget;
			}
		}
		
		
		 function editar(fila, id) {
				if (typeof id == 'undefined')
					id='tablaResultados';
				preparaDatos(fila, id);
			   document.forms[0].modo.value = "Editar";
			   var resultado = ventaModalGeneral(document.forms[0].name,"P");
			   if (resultado) {
			  	 	if (resultado=="MODIFICADO") {
			   	    alert("<siga:Idioma key='messages.updated.success'/>",'success');
			   		refrescarLocal();
			       } else if (resultado=="NORMAL") {
			       } else if (resultado[0]) {
			   	    alert("<siga:Idioma key='messages.updated.success'/>",'success');
			      		refrescarLocalArray(resultado);
			   	}
			   }
			 }
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>