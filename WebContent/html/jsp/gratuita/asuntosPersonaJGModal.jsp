<!-- busquedaClientesModal.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>
<!-- JSP -->

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	PersonaJGForm form = (PersonaJGForm)request.getAttribute("PersonaJGForm");
	Vector vAsuntos = form.getAsuntos();
	String nombre = form.getNombreAnterior()!=null?form.getNombreAnterior():"";
	String modoGuardar = form.getModoGuardar();
	String accionGuardar = form.getAccionGuardar();
	String action = (String)request.getAttribute("javax.servlet.forward.servlet_path");
%>



<%@page import="com.siga.gratuita.form.PersonaJGForm"%><html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->

	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

</head>

<body>

		

		
		<html:form action="<%=action%>" method="POST" target="_self">
			<html:hidden property="modo"/>

			<html:hidden property="modoGuardar"/>
			<html:hidden property="accionGuardar"/>
			<html:hidden property="idioma"/>
		
		<table>
		<tr>
		<td class="labelText" style="width:490px">
			<siga:Idioma key="gratuita.personaJG.literal.asuntos" arg0="<%=nombre%>"/>
		</td>
		</tr>
		<tr>
		<td>
		<div style="width:100%;height:140px;overflow-y:auto" >
			<table  border='1' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed;'>
					<tr class="tableTitle">
					<td>Asunto</td>
					<td>Año</td>
					<td>Número</td>
					</tr>
			   	<%if (vAsuntos!= null && !vAsuntos.isEmpty()) { %>
					<%String numero="", anio="", asunto="";
					
					for(int recordNumber = 0; recordNumber<vAsuntos.size(); recordNumber++){
						Hashtable hash = (Hashtable)vAsuntos.get(recordNumber);
						numero = (String)hash.get("NUMERO");
						anio = (String)hash.get("ANIO");
						asunto = (String)hash.get("ASUNTO");%>
						
						<tr class="<%=((recordNumber+1)%2==0?"filaTablaPar":"filaTablaImpar")%>" style="padding:5px;">
							<td><siga:Idioma key="<%=asunto%>"/></td>
							<td><%=anio%></td>
							<td><%=numero%></td>
						</tr>
					<%} %>
				<%} %>
			</table>
		</div>
		<td>
		</tr>
		<tr>
			<td class="labelText" width="470px">
				<input type="radio" id="act"name="accionG" value="update" checked><label for="act"><siga:Idioma key="gratuita.personaJG.literal.asuntos.actualizar"/></input></label><br>
				<siga:Idioma key="gratuita.personaJG.literal.asuntos.actualizar.explicacion"/><br>
				
				<input type="radio" id="cre" name="accionG" value="insert"><label for="cre"><siga:Idioma key="gratuita.personaJG.literal.asuntos.crear"/></input></label><br>
				<siga:Idioma key="gratuita.personaJG.literal.asuntos.crear.explicacion"/><br>

			</td>
		</tr> 	
		</table>
		</html:form>
		<siga:ConjBotonesAccion botones="A,C" modal="P"/>
		
		<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		//Asociada al boton Cerrar -->
		function accionCerrar() {		
			top.cierraConParametros("");
		}

		//Asociada al boton Cerrar -->
		function accionAceptar() {
			if (document.forms[0].accionG[0].checked){
				parent.document.forms[0].accionGuardar.value = "update";
			}else if(document.forms[0].accionG[1].checked){
				parent.document.forms[0].accionGuardar.value = "insert";
			}
			window.top.returnValue=parent.document.forms[0].accionGuardar.value;
			window.top.close();
		}
	</script>


</body>
</html>
