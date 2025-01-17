<!DOCTYPE html>
<html>
<head>
<!-- insertarMateria.jsp -->
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
<%@ page import="com.siga.beans.ScsMateriaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	//String profile=usr.getProfile();
	Hashtable miHash = (Hashtable)request.getSession().getAttribute("elegido");
	request.getSession().removeAttribute("elegido");
	
	int pcajgActivo = 0;	
	if (request.getAttribute("pcajgActivo") != null) {
		pcajgActivo = Integer.parseInt(request.getAttribute(
				"pcajgActivo").toString());
	}
	String asterisco = "&nbsp;(*)&nbsp;";
	boolean codigoExtObligatorio = false;	
	if (pcajgActivo == 5){
		codigoExtObligatorio=true;
	}	
	
	

%>


<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0">
	<tr>
	<td class="titulitosDatos">
		<siga:Idioma key="gratuita.maestros.literal.insertarDocumento"/>
	</td>
	</tr>
	</table>
	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->

	<!-- INICIO: CAPA DE REGISTRO CON MEDIDAS EN EL ESTILO -->
	<div id="campos" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<table class="tablaCentralCamposPeque" align="center">	
	
	<html:form action="/JGR_MantenimientoDocumentacionEJG.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "InsertarDocumento"/>	
	<html:hidden name="DefinirMantenimDocumentacionEJGForm" property="tipoDocumento"/>
	
	
	<tr>		
	<td>			
	
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="sjcs.ejg.documentacion.documentacion">
	<table class="tablaCampos" align="center">
	
	<!-- FILA -->	
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
				<html:text name="DefinirMantenimDocumentacionEJGForm" property="codigoExt" size="20" maxlength="20" styleClass="box" value=""></html:text>
			</td>			
		</tr>	
		
		<tr>	
			<td class="labelText" valign="top">
				<siga:Idioma key="censo.fichaCliente.literal.abreviatura"/>(*)
			</td>
			<td valign="top">
				<html:text name="DefinirMantenimDocumentacionEJGForm" property="abreviatura" maxlength="60" size="30" styleClass="box" value=""></html:text>
			</td>
		</tr>
		<tr>
			<td class="labelText" valign="top">
				<siga:Idioma key="gratuita.maestros.documentacionEJG.nombre"/>(*)
			</td>
			<td valign="top">
				<textarea name="descripcion" rows="5" cols="60" onkeydown="cuenta(this,300)" onChange="cuenta(this,300)" class="box"></textarea>
			</td>
		</tr>
	</table>

	</siga:ConjCampos>
	
	</td>
	</tr>
	</html:form>
	</table>
	</div>

	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P"  />
	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">	
	
		//<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
		
		//<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() 
		{	
			
			var abreviatura = document.forms[0].abreviatura.value;
			var descripcion = document.forms[0].descripcion.value;
			var codigoExt   = document.forms[0].codigoExt.value;
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
			
		 
		 document.forms[0].submit();
		window.top.returnValue="MODIFICADO";
		}
		//<!-- Asociada al boton Cerrar -->
		function accionCerrar()
		{
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
