<!DOCTYPE html>
<html>
<head>
<!-- seleccionarConsulta.jsp -->
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
<%@ page import="com.siga.beans.ConConsultaBean"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean user = ((UsrBean)ses.getAttribute(("USRBEAN")));
	Vector datos = (Vector)request.getAttribute("datos");
		
%>	



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="NuevoExpedienteForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
			
</head>

<body>

	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<form> 

	<tr>				
	<td>

	<siga:ConjCampos leyenda="consultas.recuperarconsulta.literal.consulta">

		<table class="tablaCampos" align="center" border="0">
	
		<!-- FILA -->
		<tr>				

			<td class="labelText" width="30%">
				<siga:Idioma key="consultas.recuperarconsulta.literal.consulta"/>
			</td>				
			<td >		
				<select style="width:350px"  class="boxCombo" id="consulta" name="consulta">
<%
			if(datos!=null && !datos.isEmpty()){
				for (int i=0; i<datos.size(); i++)
				{
					Row fila = (Row)datos.elementAt(i);
					String text = fila.getString(ConConsultaBean.C_DESCRIPCION);					
					String value = fila.getString(ConConsultaBean.C_IDCONSULTA);
					value += "&" + fila.getString(ConConsultaBean.C_IDINSTITUCION);
%>
					<option   value="<%=value%>"><%=text%></option>
<%
				}
			}
%>				
				</select>														
			</td>			
		</tr>				
		
		</table>

	</siga:ConjCampos>


	</td>
</tr>

</form>

</table>



	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->	

		<siga:ConjBotonesAccion botones="Y,C" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			top.cierraConParametros(document.forms[0].consulta.value);
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("VACIO");
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