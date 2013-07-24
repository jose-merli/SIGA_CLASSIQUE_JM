<!DOCTYPE html>
<html>
<head>
<!-- remesaResolucionCAJG.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.ScsEJGBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.gratuita.action.DefinirRemesaResolucionesCAJGAction"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String dato[] = {(String)usr.getLocation()};
	String idTipoRemesa = request.getParameter("idTipoRemesa");
	
	String fecha = UtilidadesBDAdm.getFechaBD("");
	
	boolean readonly = false;
	String modo=(String)request.getAttribute("modo");
	if (modo.equals("consultar")){
		readonly = true;
	}
	
	String estilocaja = "box";
	
	if (readonly) {
		estilocaja = "boxConsulta";
	}
	
	String idremesaresolucion = "";
	String prefijo = "";
	String numero = "";
	String sufijo = "";
	String nombreFichero = "";
	String observaciones = "";
	String fechaCarga = "";
	String fechaResolucion = "";
	String logGenerado = "";
	
	Hashtable r=(Hashtable) request.getAttribute("REMESARESOLUCION");
	if (r.get("IDREMESARESOLUCION")!=null){
		idremesaresolucion=(String)r.get("IDREMESARESOLUCION");
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
	
	if (r.get("FECHACARGA")!=null){
		fechaCarga=(String)r.get("FECHACARGA");
		if (!fechaCarga.equals("")) {
			fechaCarga=GstDate.getFormatedDateShort("",UtilidadesString.mostrarDatoJSP(fechaCarga));
		}
	}
	
	if (r.get("FECHARESOLUCION")!=null){
		fechaResolucion=(String)r.get("FECHARESOLUCION");
		if (!fechaResolucion.equals("")) {
			fechaResolucion=GstDate.getFormatedDateShort("",UtilidadesString.mostrarDatoJSP(fechaResolucion));
		}
	}
	
	if (r.get("NOMBREFICHERO")!=null){
		nombreFichero=(String)r.get("NOMBREFICHERO");
	}	
	
	if (r.get("OBSERVACIONES")!=null){
		observaciones=(String)r.get("OBSERVACIONES");
	}
	
	if (r.get("LOGGENERADO")!=null){
		logGenerado=(String)r.get("LOGGENERADO");
	}
	
	File file = DefinirRemesaResolucionesCAJGAction.getFichero(usr.getLocation(), idremesaresolucion, false);
	String nombreFicheroRes = "";
	if (file != null) {
		nombreFicheroRes = file.getName();
		nombreFicheroRes = nombreFicheroRes.substring(0, nombreFicheroRes.indexOf("."));
	}
	
	File logFile = DefinirRemesaResolucionesCAJGAction.getFichero(usr.getLocation(), idremesaresolucion, true);
	String nombreFicheroLog = "";
	if (logFile != null) {
		nombreFicheroLog = logFile.getName();		
	}
%>


<!-- HEAD -->

  	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
	<html:javascript formName="DefinicionRemesaResolucionesCAJGForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
  		
	<script type="text/javascript">
		
		function descargar(tipo) {
			document.DefinicionRemesaResolucionesCAJGForm.modo.value=tipo;			
		   	document.DefinicionRemesaResolucionesCAJGForm.target="submitArea";		   	
			document.DefinicionRemesaResolucionesCAJGForm.submit();
		}
		
	</script> 
	
	
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
		<tr>
		<td class="titulitosDatos">
			<siga:Idioma key="gratuita.busquedaResolucionesCAJG.literal.datos"/>
		</td>
		</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	
	
	<html:form action="/JGR_E-Comunicaciones_RemesaResolucion.do" method="POST" target="submitArea">
	
	<!-- Para seleccion automatica -->
	
	
	<!-- ************************ //-->
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "modo" value = "<%=modo%>"/>
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "idRemesaResolucion" value ="<%=idremesaresolucion%>"/>
	<html:hidden property = "nombreFichero" value ="<%=nombreFichero%>"/>
	<html:hidden property = "logGenerado" value ="<%=logGenerado%>"/>
	<html:hidden property = "idTipoRemesa" value ="<%=idTipoRemesa%>"/>
	
	
	
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
				
			<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaResolucionesCAJG.literal.datos">
			
		<table class="tablaCampos" align="center">
				
			</tr>
				
			<tr>	
				<td class="labelText" >
					<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.numRegistro"/>	
				</td>
				
				<td class="labelText">	
					<%if (prefijo != null && !prefijo.trim().equals("")) {%>
						<html:text value="<%=prefijo%>" property="prefijo"  size="5" maxlength="10" styleClass="boxConsulta" style="width:80px" readonly="true" ></html:text>
					<%} else {%>
						<html:hidden property="prefijo" value="<%=prefijo%>"/>
					<%}%>
					
					<html:text value="<%=numero%>" property="numero"  size="5" maxlength="10" styleClass="boxConsulta" style="width:80px" readonly="true"></html:text>
					<%if (sufijo != null && !sufijo.trim().equals("")) {%>
						<html:text value="<%=sufijo%>" property="sufijo"  size="5" maxlength="10" styleClass="boxConsulta" style="width:80px" readonly="true" ></html:text>
					<%} else {%>
						<html:hidden property="sufijo" value="<%=sufijo%>"/>
					<%}%>
				</td>
			</tr>
			
			<tr>
				<td class="labelText" >
					<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.fCarga"/>
				</td>
				<td class="labelText">
					<html:text property="fechaCarga" size="10" maxlength="10" styleClass="boxConsulta"  value="<%=fechaCarga%>" readOnly="true"></html:text>
				</td>
		
			</tr>
							
			<tr>
				<td class="labelText" >
					<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.fResolucion"/><%=readonly?"":"&nbsp;(*)"%>
				</td>
				<td class="labelText">
					<%if (!readonly) {%>
						<siga:Fecha nombreCampo="fechaResolucion"   valorInicial="<%=fechaResolucion%>"/>
					<%}else{%>
						<siga:Fecha nombreCampo="fechaResolucion"   valorInicial="<%=fechaResolucion%>" disabled="true" readOnly="true" />
					<%}%>
				</td>
		
			</tr>
			<tr>				
				<td class="labelText">
					<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.FicheroResoluciones"/>
				</td>				
				<td class="labelText">	
					<html:link href="#" onclick="descargar('descargar')"><%=nombreFichero%></html:link>
				</td>						
			</tr>
			
			<tr>				
				<td class="labelText">
					<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.FicheroLog"/>
				</td>				
				<td class="labelText">	
					<html:link href="#" onclick="descargar('descargarLog')"><%=nombreFicheroLog%></html:link>
				</td>						
			</tr>
			
			<tr>	
				<td class="labelText" >
					<siga:Idioma key="gratuita.BusquedaResolucionCAJG.literal.observaciones"/>	
				</td>
				
				<td class="labelText">
					<html:textarea style="width:400px;" onKeyDown="cuenta(this,1000)" onChange="cuenta(this,1000)" property="observaciones" readonly="<%=readonly%>" rows="14" value="<%=observaciones%>" styleClass="box"></html:textarea>					
				</td>
			</tr>
			
			
		</table>
	</siga:ConjCampos>	
	
	</html:form>
	


	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones='<%=readonly?"C":"Y,R,C"%>' modal="M" />
	<!-- FIN: BOTONES REGISTRO -->

	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() {		
			var f = document.getElementById("DefinicionRemesaResolucionesCAJGForm");			
			
  			if (f && validateDefinicionRemesaResolucionesCAJGForm(f)) {  			  						
				document.forms[0].modo.value="modificar";				
				document.forms[0].submit();						
				window.top.returnValue="MODIFICADO";
			}else{
			
				fin();
				return false;
			
			}
		}
		
		//Asociada al boton Cerrar -->
		function accionCerrar() {
			top.cierraConParametros("NORMAL");			
		}
		
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</body>

</html>
