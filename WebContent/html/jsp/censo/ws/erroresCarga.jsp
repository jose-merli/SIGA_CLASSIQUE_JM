<!DOCTYPE html>
<html>
<head>
<!-- erroresCarga.jsp -->
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
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.CajgRespuestaEJGRemesaBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.io.File"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	
		
%>

<%@page import="java.util.Vector"%>

<!-- HEAD -->
  	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
	<html:javascript formName="EdicionRemesaForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
  		
	<script type="text/javascript">
		
		
		
	</script> 
	
	
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" heigth="32">
		<tr>
		<td class="titulitosDatos">
			<siga:Idioma key="censo.ws.literal.errorCarga"/>
		</td>
		</tr>
	</table>
		
			
		<div id='listadoEJGDiv' style='height:100%; position:absolute; width:100%; overflow:auto'>
			<table border='1' width='100%' cellspacing='0' cellpadding='0' align='center'>		
				<c:forEach items="${EdicionRemesaForm.listaErrores}" var="error" varStatus="i">							
					<tr class="labelTextValue">									
						<td id="${i.index}">${error}"</td>						
					</tr>
				</c:forEach>					
		 	</table>
		 </div>

	<!-- FIN: CAMPOS DEL REGISTRO -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones='<%="C"%>' modal="M" />
	<!-- FIN: BOTONES REGISTRO -->

	
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
		document.body.onLoad=ajusteAltoMain('listadoEJGDiv',52);
	
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
