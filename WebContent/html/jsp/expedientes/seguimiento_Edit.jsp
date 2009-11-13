<!-- seguimiento_Edit.jsp -->
<!-- CABECERA JSP -->

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->

<%@ page import="com.siga.expedientes.form.ExpSeguimientoForm"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	String accion = (String)request.getAttribute("accion");
	boolean bLectura=(accion.equals("consulta"));
	boolean bEdicion=(accion.equals("edicion"));
	boolean bNuevo=(accion.equals("nuevo"));
	String boxStyle = (bLectura)?"boxConsulta":"box";
	String boxStyleFecha = (bLectura || bEdicion)?"boxConsulta":"box";
	 
	String botones = "C";
	if (bNuevo){	
		botones = "Y,C";
	}else if (bEdicion){
		botones = "Y,R,C";
	}
	
	String idInstitucionTipoexpediente=(String)request.getAttribute("idInstitucion_TipoExpediente");
	String idTipoExpediente=(String)request.getAttribute("idTipoExpediente");
	String idFase = (String)request.getAttribute("idFase");
	String idEstado = (String)request.getAttribute("idEstado");
	ArrayList aIdTipoAnotacion=new ArrayList();
	
	
	String dato[] = null;
	String comboAnotacion = "";
	
	String autom = (String)request.getAttribute("automatico");
	boolean isAnotacionAutomatica = false;
	isAnotacionAutomatica = (autom!=null && autom.equals("S"));
	if (idFase!=null && !idFase.equals("") && idEstado!=null && !idEstado.equals("")) {
		dato = new String[]{idInstitucionTipoexpediente,idTipoExpediente,idFase,idEstado};
		comboAnotacion="cmbTipoAnotacionSinNull";
	} else if (idFase==null && (idEstado!=null && !idEstado.equals(""))) {
		comboAnotacion="cmbTipoAnotacionFaseNull";
	} else if ((idFase!=null &&!idFase.equals("")) && idEstado==null) {
		comboAnotacion="cmbTipoAnotacionEstadoNull";
	} else {
		dato = new String[]{idInstitucionTipoexpediente,idTipoExpediente};
		comboAnotacion="cmbTipoAnotacionTodosNull";
		//isAnotacionAutomatica = true;
	}
	
	aIdTipoAnotacion.add((String)request.getAttribute("idTipoAnotacion"));
	
	if(dato==null)	
		dato = new String[]{idInstitucionTipoexpediente,idTipoExpediente,idInstitucionTipoexpediente,idTipoExpediente,idFase,idInstitucionTipoexpediente,idTipoExpediente,idFase,idEstado};
	request.removeAttribute("accion");
	
	ExpSeguimientoForm form = (ExpSeguimientoForm)request.getAttribute("ExpSeguimientoForm");
	Boolean editable = form.getEditable();
%>	

<%@page import="java.util.ArrayList"%>

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="ExpSeguimientoForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
	<!-- Calendario -->
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
</head>

<body>

	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalMedia" align="center">


	<table class="tablaTitulo" cellspacing="0" height="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<bean:write name="ExpSeguimientoForm" property = "tituloVentana" />
			</td>
		</tr>
	</table>
	

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposMedia"  align="center">

	<html:form action="/EXP_Auditoria_Seguimiento.do" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>
	<html:hidden property = "modo" value = ""/>

	<tr>				
	<td>	
	
	<siga:ConjCampos leyenda="expedientes.auditoria.literal.anotacion">

		<table class="tablaCampos" align="left">
		
		
		<!-- FILA -->
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.usuario"/>
			</td>				
			<td colspan="3">
				<html:text name="ExpSeguimientoForm" property="usuario" size="60" styleClass="boxConsulta" readonly="true"></html:text>
			</td>				
		</tr>
		<!-- FILA -->
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.descripcion"/>
			</td>				
			<td colspan="3" width="250">
				<% if (!isAnotacionAutomatica) { %>
					<html:textarea name="ExpSeguimientoForm" property="descripcion" onkeydown="cuenta(this,1024)" onchange="cuenta(this,1024)"  rows="4" style="width:450px;" styleClass="<%=boxStyle%>"   ></html:textarea>
				<% } else { %>
					<html:textarea name="ExpSeguimientoForm" property="descripcion" onkeydown="cuenta(this,1024)" onchange="cuenta(this,1024)"  rows="4" style="width:450px;" styleClass="boxConsulta"  readonly="true" ></html:textarea>
				<% } %>
				
			
			
				
			</td>				
		</tr>	
		<!-- FILA -->
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.fase"/>
			</td>				
			<td>
				<html:text name="ExpSeguimientoForm" property="fase" styleClass="boxConsulta" readonly="true"></html:text>
				<html:hidden property = "idFase"/>
			</td>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.estado"/>
			</td>				
			<td>
				<html:text name="ExpSeguimientoForm" property="estado" styleClass="boxConsulta" readonly="true"></html:text>
				<html:hidden property = "idEstado"/>
			</td>	
		</tr>	
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.fechaInicioEstado"/>
			</td>				
			<td>
				<html:text name="ExpSeguimientoForm" property="fechaInicioEstado" size="10" styleClass="boxConsulta" readonly="true" />
			</td>

			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.fechaFinEstado"/>
			</td>
			<td>
				<html:text name="ExpSeguimientoForm" property="fechaFinEstado" size="10" styleClass="<%=boxStyleFecha%>" readonly="true" />
				<% if (!bLectura && !bEdicion){
				%>
					<a href='javascript://'onClick="return showCalendarGeneral(fechaFinEstado);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<%}%>
			</td>
		</tr>	
		<!-- FILA -->
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.tipoanotacion"/> (*)
			</td>				
			<td>
				<%if (!bLectura && !isAnotacionAutomatica){
				%>		
				<siga:ComboBD nombre = "idTipoAnotacion" tipo="<%=comboAnotacion%>" clase="<%=boxStyle%>" obligatorio="true" parametro="<%=dato%>" elementoSel="<%=aIdTipoAnotacion%>"/>						
				<%}else{
				%>
				<siga:ComboBD nombre = "idTipoAnotacion" tipo="<%=comboAnotacion%>" clase="boxConsulta" obligatorio="true" readonly="true" parametro="<%=dato%>" elementoSel="<%=aIdTipoAnotacion%>"/>
				<%}%>
			</td>

			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.fechaAnotacion"/> (*)
			</td>
			<td>
				<html:text name="ExpSeguimientoForm" property="fecha" size="10" styleClass="<%=boxStyleFecha%>" readonly="true" />
				<% if (!bLectura && !bEdicion){
				%>
					<a href='javascript://'onClick="return showCalendarGeneral(fecha);"><img src="<%=app%>/html/imagenes/calendar.gif" border="0"></a>
				<%}%>
			</td>
		</tr>	
		<!-- FILA -->
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.regentrada"/>
			</td>				
			<td>
				<% if (!isAnotacionAutomatica) { %>
				<html:text name="ExpSeguimientoForm" property="regentrada" size="15" maxlength="30" styleClass="<%=boxStyle%>" readonly="<%=bLectura%>"></html:text>
				<% } else { %>
				<html:text name="ExpSeguimientoForm" property="regentrada" size="15" maxlength="30" styleClass="boxConsulta" readonly="true"></html:text>
				<% } %>
			</td>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.regsalida"/>
			</td>				
			<td>
				<% if (!isAnotacionAutomatica) { %>
				<html:text name="ExpSeguimientoForm" property="regsalida" size="15" maxlength="30" styleClass="<%=boxStyle%>" readonly="<%=bLectura%>"></html:text>
				<% } else { %>
				<html:text name="ExpSeguimientoForm" property="regsalida" size="15" maxlength="30" styleClass="boxConsulta" readonly="true"></html:text>
				<% } %>
			</td>
		</tr>				
		
		</table>

	</siga:ConjCampos>


	</td>
</tr>

</html:form>

</table>



	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->	

		<siga:ConjBotonesAccion botones="<%=botones%>" modal="M" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
			sub();		
			if (validateExpSeguimientoForm(document.ExpSeguimientoForm)){
				<%if (accion.equals("nuevo")){%>
					document.forms[0].modo.value="insertar";
				<%}else{%>
					document.forms[0].modo.value="modificar";
									
				<%}%>
				document.forms[0].submit();												
			}else{
			
				fin();
				return false;
			}
			
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.close();
		}
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
	
	
		function refrescarLocal() 
		{	
			parent.location.reload();
		}
		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
