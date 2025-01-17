<!DOCTYPE html>
<html>
<head>
<!-- fases_Edit.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL PEQUE�A -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
-->

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
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.beans.ExpFasesBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Vector datos = (Vector)request.getAttribute("datos");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	String modo=request.getParameter("modo");
	
	request.removeAttribute("datos");
	request.removeAttribute("editable");
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- Validaciones en Cliente -->
	<html:javascript formName="FasesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
</head>

<body>


	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="expedientes.auditoria.literal.fase"/>
			</td>
		</tr>
	</table>	

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->
<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:form action="/EXP_TiposExpedientes_Fases.do" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>

<% if(modo.equalsIgnoreCase("Nuevo"))  { %>
	<html:hidden property = "modo" value = "Insertar"/>

<% } else {%>
	<html:hidden property = "modo" value = "Modificar"/>

<% }%>
<%
		ExpFasesBean bean = (ExpFasesBean)datos.elementAt(0);
%>

	<tr>				
	<td>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.fase">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.nombre"/>&nbsp;(*)
		</td>				
		<td>
			<% if (bEditable){ %>
				<html:text name="FasesForm" property="nombre" size="30" maxlength="30" styleClass="box" value="<%=bean.getNombre()%>"></html:text>
			<% } else { %>
				<html:text name="FasesForm" property="nombre" size="30" maxlength="30" styleClass="boxConsulta" disabled="true" value="<%=bean.getNombre()%>"></html:text>
			<% } %>
			
			<input type="hidden" name="idTipoExpediente" value="<%=bean.getIdTipoExpediente()%>"/>
		
			
		</td>
		
		</tr>			
		
		<tr>				

		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.diasVencimiento"/>
		</td>				
		<td>
			<% 
			   String diasAntelacion = "0";
			   if (bean.getDiasAntelacion()!=null) diasAntelacion = bean.getDiasAntelacion().toString();
			   String diasVencimiento = "0";
			   if (bean.getDiasVencimiento()!=null) diasVencimiento = bean.getDiasVencimiento().toString();
			   
			   if (bEditable){ %>
				<html:text name="FasesForm" property="diasVencimiento" size="3" maxlength="3" styleClass="box" value="<%=diasVencimiento %>" style="text-align:right"></html:text>
			<% } else { %>
				<html:text name="FasesForm" property="diasVencimiento" size="3" maxlength="3" styleClass="boxConsulta" disabled="true" value="<%=diasVencimiento %>" style="text-align:right"></html:text>
			<% } %>
			
			
		</td>
		
		</tr>			
		
		<tr>				

		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.diasAntelacion"/>
		</td>				
		<td>
	
			<% if (bEditable){ %>
				<html:text name="FasesForm" property="diasAntelacion" size="3" 
				maxlength="3" styleClass="box" value="<%=diasAntelacion %>" style="text-align:right"></html:text>
			<% } else { %>
				<html:text name="FasesForm" property="diasAntelacion" size="3" maxlength="3" styleClass="boxConsulta" disabled="true" value="<%=diasAntelacion %>" style="text-align:right"></html:text>
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

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

		<siga:ConjBotonesAccion botones="Y,C" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">


		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
			sub();		
			if (validateFasesForm(document.FasesForm)){
				FasesForm.submit();			
				window.top.returnValue="MODIFICADO";
			} else{
			
				fin();
				return false;
			}
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.top.close();
		}
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			
		}
	

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
