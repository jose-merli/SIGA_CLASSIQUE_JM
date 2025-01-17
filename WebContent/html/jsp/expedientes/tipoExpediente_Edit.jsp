<!DOCTYPE html>
<html>
<head>
<!-- tipoExpediente_Edit.jsp -->
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
<%@ page import="com.siga.beans.ExpTipoExpedienteBean"%>
<%@ page import="com.siga.general.CenVisibilidad"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<!-- JSP -->
<%  

	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	
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
	<html:javascript formName="TipoExpedienteForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	

</head>

<body>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->
	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="expedientes.literal.tiposexpedientes"/>
			</td>
		</tr>
	</table>	

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:form action="/EXP_MantenerTiposExpedientes.do" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>

<% if(modo.equalsIgnoreCase("Nuevo"))  { %>
	<html:hidden property = "modo" value = "Insertar"/>

<% } else {%>
	<html:hidden property = "modo" value = "Modificar"/>

<% }

		ExpTipoExpedienteBean bean = (ExpTipoExpedienteBean)datos.elementAt(0);
%>

	<tr>				
	<td>

	<siga:ConjCampos leyenda="expedientes.literal.tiposexpedientes">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.nombre"/>&nbsp;(*)
		</td>				
		<td>
			<% if (bEditable){ %>
				<html:text name="TipoExpedienteForm" property="nombre" size="30" maxlength="30" styleClass="box" value="<%=bean.getNombre()%>"></html:text>
			<% } else { %>
				<html:text name="TipoExpedienteForm" property="nombre" size="30" maxlength="30" styleClass="boxConsulta" disabled="true" value="<%=bean.getNombre()%>"></html:text>
			<% } %>
			
			<input type="hidden" name="idTipoExpediente" value="<%=bean.getIdTipoExpediente()%>"/>		
			<input type="hidden" name="idInstitucion" value="<%=bean.getIdInstitucion()%>"/>			
		</td>		
		</tr>	
<% if(CenVisibilidad.getNivelInstitucion(user.getLocation()).equals("1")){%>				
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.tiposexpedientes.literal.esgeneral"/>
			</td>				
			<td>
<%
				if (!bEditable)
				{
					if (bean.getEsGeneral().equals("S"))
					{
%>
						<siga:Idioma key="general.yes"/>
<%
					} else {
%>
						<siga:Idioma key="general.no"/>
<%
					}
				} else {
%>
					<html:checkbox name="TipoExpedienteForm" property="esGeneral" value="S"/>
<%
				}
%>
			</td>
		</tr>
<% }%>		
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
			//tipoExpedienteForm.submit();
			sub();
			if (validateTipoExpedienteForm(document.TipoExpedienteForm)){
				TipoExpedienteForm.submit();			
				//window.top.returnValue="MODIFICADO";
			}else{
				fin();
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


<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
