<!DOCTYPE html>
<html>
<head>
<!-- abrirDestinatarioManual.jsp -->

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.siga.administracion.SIGAConstants,com.atos.utils.*,com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<%  
	String app=request.getContextPath();
	HttpSession ses = request.getSession();
	Vector vDatos = (Vector)request.getAttribute("datos");		
	request.removeAttribute("datos");	
	String botonesFila;
%>	




	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
			
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		// Asociada al boton Nuevo
		function accionNuevo() {		
			var cliente = ventaModalGeneral("busquedaClientesModalForm","G");			
				  	
			if (cliente!=undefined && cliente[0]!=undefined ){
				document.RemitentesForm.modo.value="nuevo";
				document.RemitentesForm.idPersona.value=cliente[0];
				document.RemitentesForm.idInstitucion.value=cliente[1];
				document.RemitentesForm.numColegiado.value=cliente[2];
				document.RemitentesForm.nifcif.value=cliente[3];
				document.RemitentesForm.nombre.value=cliente[4];
				document.RemitentesForm.apellidos1.value=cliente[5];
				document.RemitentesForm.apellidos2.value=cliente[6];
				
				var result = ventaModalGeneral("RemitentesForm","G");
				if (result == "MODIFICADO"){
					document.location.reload();
				}			  		
			}	
		}
	
		function refrescarLocal() {			
			document.location.reload();			
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<siga:Titulo titulo="envios.definirEnvios.destIndividual.cabecera" localizacion="envios.definirEnvios.localizacion" />				
</head>
	
<body>
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<bean:define id="busquedaVolver" name="busquedaVolver" scope="request"/>
		
	<table class="tablaTitulo" align="center" cellspacing="0">			
		<tr>
			<td id="titulo" class="titulitosDatos">
				<c:out value="${titulo}"/> 				    
			</td>
		</tr>
	</table>

	<html:form  action="${path}" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "idPersona" />
		<html:hidden property = "idInstitucion" />
		<html:hidden property = "numColegiado" />
		<html:hidden property = "nifcif" />
		<html:hidden property = "nombre" />
		<html:hidden property = "apellidos1" />
		<html:hidden property = "apellidos2" />
		<html:hidden property = "idEnvio" value="${idEnvio}"/>
		<html:hidden property = "idTipoEnvio" value="${idTipoEnvio}"/>
		<html:hidden property = "idTipoExpediente" value="${idTipoExpediente}"/>
	</html:form>
				
	<siga:Table 
		name="tablaDatos"
		border="1"
		columnNames="envios.listas.literal.nombreyapellidos,
			censo.fichaCliente.literal.colegiado,
			censo.busquedaClientes.literal.nif,"
		columnSizes="60,15,15,10"
		modal="g">

		<% if (vDatos==null || vDatos.size()==0) {%>
			<tr class="notFound">
				<td class="titulitos">
					<siga:Idioma key="messages.noRecordFound"/>
				</td>
			</tr>
			
		<% } else {
			for (int i=0; i<vDatos.size(); i++) {
				Row fila = (Row)vDatos.elementAt(i);
	 			
				if(fila.getString("TIPODESTINATARIO").equalsIgnoreCase("CEN_PERSONA")){
					botonesFila="C,E,B"; 
				} else {
					botonesFila="C,B";
				}			
		%>
	  		
	  		<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botonesFila %>" clase="listaNonEdit" visibleConsulta="no">				
				<td>
					<input type="hidden" name="idPersona_<%=""+(i+1)%>" value="<%=fila.getString("IDPERSONA")%>"/>
					<%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBREYAPELLIDOS"))%>
				</td>
				<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NCOLEGIADO"))%></td>
				<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NIFCIF"))%></td>
			</siga:FilaConIconos>
		<%		}
			}
		%>
	</siga:Table>

<c:catch var ="catchException">
   <bean:parameter id="origen" name="origen" />
   <bean:parameter id="datosEnvios" name="datosEnvios" />	
</c:catch>

<c:if test = "${catchException == null}">
	<input type="hidden" id="origen" value ="${origen}"/>
	<input type="hidden" id="datosEnvios" value ="${datosEnvios}"/>
<c:choose>
	<c:when test="${origen=='/JGR_ComunicacionEJG'}">
		<html:form  action="/JGR_EJG"  method="POST" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property="modo" value="editar"/>
			<html:hidden styleId = "idTipoEJG" property="idTipoEJG" />
			<html:hidden styleId = "anio" property="anio"/>
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion"/>
			<html:hidden styleId = "origen" property="origen"/>
		</html:form>
	</c:when>
	<c:otherwise>
		<html:form action="/JGR_MantenimientoDesignas" method="post" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property = "modo"   value="editar"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion" value=""/>
			<html:hidden styleId = "anio" property="anio" />
			<html:hidden styleId = "idTurno" property="idTurno" />
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "origen" property="origen" />
		</html:form>	
	</c:otherwise>
</c:choose>
</c:if>	
	<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
	<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle"  />
		
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes" value="1">
	</html:form>
	
	<%@ include file="/html/jsp/envios/includeVolver.jspf" %>
	
	<script language="JavaScript">		
		function selectRow(fila) {
			document.getElementById('filaSelD').value = fila;
			datos = document.getElementById('tablaDatosDinamicosD');
			preparaDatos(fila,'tablaDatos', datos);
		}
			 
		function consultar(fila) {
		   	document.RemitentesForm.idPersona.value = document.getElementById('idPersona_'+fila).value;
		   	document.RemitentesForm.modo.value = "Ver";
		   	ventaModalGeneral(document.RemitentesForm.name,"G");
		}
			 
		function editar(fila) {			  
			document.RemitentesForm.idPersona.value = document.getElementById('idPersona_'+fila).value;
			document.RemitentesForm.modo.value = "Editar";
			var resultado = ventaModalGeneral(document.RemitentesForm.name,"G");
			if (resultado) {
				if (resultado[0]) {
					refrescarLocalArray(resultado);
					
			   	} else if (resultado=="MODIFICADO") {
			      		refrescarLocal();
			   	}
			}
		}
			 
		function borrar(fila) {
			var datos;
			if(confirm('<siga:Idioma key="messages.deleteConfirmation"/>')){			   	
				var auxTarget = document.RemitentesForm.target;
			   	document.RemitentesForm.target="submitArea";
			   	document.RemitentesForm.idPersona.value = document.getElementById('idPersona_'+fila).value;
			   	document.RemitentesForm.modo.value = "Borrar";
			   	document.RemitentesForm.submit();
			   	document.RemitentesForm.target=auxTarget;
			}
		}		
		
		
		
	</script>	

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>		