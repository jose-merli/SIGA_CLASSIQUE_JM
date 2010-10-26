<!-- operarTipo.jsp -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);		
	String accion = (String)request.getAttribute("accionModo");	
	int pcajgActivo = 0;	
	if (request.getAttribute("pcajgActivo") != null) {
		pcajgActivo = Integer.parseInt(request.getAttribute(
				"pcajgActivo").toString());
	}
	String asterisco = "&nbsp(*)&nbsp";
	boolean codigoExtObligatorio = false;	
	if (pcajgActivo == 5){
		codigoExtObligatorio=true;
	}	
	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="gratuita.documentacionEJG.localizacion.mantenimiento" 
		localizacion="gratuita.documentacionEJG.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

</head>

<body onLoad="ajusteAlto('resultado'); ">
	
	<html:form action="/JGR_MantenimientoDocumentacionEJG.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>	
	<html:hidden name="DefinirMantenimDocumentacionEJGForm" property="tipoDocumento"/>
	<html:hidden name="DefinirMantenimDocumentacionEJGForm" property="documento"/>	
	<html:hidden property = "actionModal" value = ""/>

	
		
		<!-- SUBCONJUNTO DE DATOS -->
		<siga:ConjCampos leyenda="sjcs.ejg.documentacion.tipoDocumentacion">
			<table align="center" width="100%">
				<tr>
		    		<td class="labelText" valign="top">
						<siga:Idioma key="general.codeext"/>
						<%
							if (codigoExtObligatorio) {
						%>
							<%=asterisco%> 
						<%
 							}
 						%>		
					</td>
					<td valign="top" >	
						<%if (accion.equalsIgnoreCase("ver")){%>				
							<html:text name="DefinirMantenimDocumentacionEJGForm" property="codigoExt" size="9" maxlength="10" styleClass="boxConsulta"></html:text>
						<%} else {%>
						    <html:text name="DefinirMantenimDocumentacionEJGForm" property="codigoExt" size="9" maxlength="10" styleClass="box"></html:text>
						<%}%>
					</td>			
				</tr>
					
				<tr>				
					<td class="labelText" valign="top">
						<siga:Idioma key="censo.fichaCliente.literal.abreviatura"/>&nbsp;(*)
					</td>				
					<td valign="top">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<html:text name="DefinirMantenimDocumentacionEJGForm" property="abreviatura" size="40" maxlength="60" styleClass="boxConsulta"  readonly="true"></html:text>
						<%} else {%>
							<html:text name="DefinirMantenimDocumentacionEJGForm" property="abreviatura" size="40" maxlength="60" styleClass="box" ></html:text>
						<%}%>
					</td>	
				</tr>
				<tr>
					<td class="labelText" valign="top">
						<siga:Idioma key="gratuita.maestros.documentacionEJG.nombre"/>&nbsp;(*)
					</td>
					<td valign="top">
						<%if (accion.equalsIgnoreCase("ver")){%>
							<html:textarea name="DefinirMantenimDocumentacionEJGForm" property="descripcion" rows="5" cols="60" onkeydown="cuenta(this,300)" onChange="cuenta(this,300)" styleClass="boxConsulta" readonly="true"/>
						<%} else {%>
							<html:textarea name="DefinirMantenimDocumentacionEJGForm" property="descripcion" rows="5" cols="60" onkeydown="cuenta(this,300)" onChange="cuenta(this,300)" styleClass="box" readonly="false"/>
						<%}%>
					</td>
				</tr>
			</table>
		</siga:ConjCampos>
	</html:form>


	


	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		//Refresco
		function refrescarLocal(){
			document.forms[0].target="mainWorkArea";
			document.forms[0].modo.value = "editar";
			document.forms[0].submit();
		}
	
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.DefinirMantenimDocumentacionEJGForm.reset();
		}
		
		//Asociada al boton Guardar -->
		function accionGuardarCerrar() 
		{
			var abreviatura = document.forms[0].abreviatura.value;
			var descripcion = document.forms[0].descripcion.value;
			var codigoExt = document.forms[0].codigoExt.value;
			sub();
			if (trim(abreviatura) == "") {
				alert('<siga:Idioma key="gratuita.areasMaterias.message.requeridoAbreviatura"/>');
				fin();	
				return false;
			}

			if (trim(descripcion)=="") {
				alert('<siga:Idioma key="gratuita.areasMaterias.message.requeridoDescripcion"/>');
				fin();
				return false;
			}

			var error = "";
			if (<%=codigoExtObligatorio%> && trim(codigoExt)==""){
				error += "<siga:Idioma key='errors.required' arg0='general.codeext'/>"+ '\n';
				 if(error!=""){
					  alert(error);
					  fin();
					  return false;
				 }
			}	
			
			document.forms[0].modo.value="modificarDocu";
			document.forms[0].submit();
			window.returnValue="MODIFICADO";
			
		}			

		function accionCerrar()
		{
			top.cierraConParametros("NORMAL");			
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

						

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<%if (accion.equalsIgnoreCase("ver")){%>
			<siga:ConjBotonesAccion botones="C" modal="P"  />
		<%} else {%>
			<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  />
		<%}%>

	<!-- FIN: BOTONES REGISTRO -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->	
	</body>
</html>
