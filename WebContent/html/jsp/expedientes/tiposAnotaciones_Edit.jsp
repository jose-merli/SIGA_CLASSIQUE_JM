<!DOCTYPE html>
<html>
<head>
<!-- tiposAnotaciones_Edit.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL PEQUEÑA -->
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
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ExpTiposAnotacionesBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Vector datos = (Vector)request.getAttribute("datos");
	String descEstado = ((String)request.getAttribute("descEstado"))==null?"":(String)request.getAttribute("descEstado");
	String descFase = ((String)request.getAttribute("descFase")==null?"":(String)request.getAttribute("descFase"));
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	String modo=request.getParameter("modo");
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");

	
	
	request.removeAttribute("datos");
	request.removeAttribute("editable");
%>	


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<!-- Validaciones en Cliente -->
	<html:javascript formName="TiposAnotacionesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		
</head>

<body onload="recargarCombos();">

	<table class="tablaTitulo" align="center" height="20" cellpadding="0" cellspacing="0">
		<tr>
			<td class="titulosPeq">
				<siga:Idioma key="expedientes.auditoria.literal.tipoanotacion"/>
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
<%
		ExpTiposAnotacionesBean bean = (ExpTiposAnotacionesBean)datos.elementAt(0);
		String idTipoAnotacion=(bean.getIdTipoAnotacion()!=null)?bean.getIdTipoAnotacion().toString():""; 
%>

	<html:form action="/EXP_TiposExpedientes_TiposAnotaciones.do" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>
	<html:hidden property = "idInstitucion" value = "<%=bean.getIdInstitucion().toString() %>"/>
	<html:hidden property = "idTipoExpediente" value = "<%=bean.getIdTipoExpediente().toString() %>"/>
	<html:hidden property = "idTipoAnotacion" value = "<%=idTipoAnotacion%>"/>

<% if(modo.equalsIgnoreCase("Nuevo"))  { %>
	<html:hidden property = "modo" value = "Insertar"/>

<% } else {%>
	<html:hidden property = "modo" value = "Modificar"/>

<% }%>

	<tr>				
	<td>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.tipoanotacion">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.nombre"/>&nbsp(*)
		</td>				
		<td>
		  <%if (modo!=null && modo.equals("Ver")){ %>	
				<html:text name="tiposAnotacionesForm" property="nombre" size="30" maxlength="30" styleClass="boxConsulta" value="<%=UtilidadesMultidioma.getDatoMaestroIdioma(bean.getNombre(),user)%>" readonly="true"></html:text>
		 <%}else{%>
		 		<html:text name="tiposAnotacionesForm" property="nombre" size="30" maxlength="30" styleClass="box" value="<%=UtilidadesMultidioma.getDatoMaestroIdioma(bean.getNombre(),user)%>"></html:text>
		 <%} %>	
				<input type="hidden" name="idTipoExpediente" value="<%=bean.getIdTipoExpediente()%>"/>			
		</td>
		</tr>
		<tr>
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.fase"/>
		</td>
		<td>
			<% 
					String[] parametros = new String[2];
				 	parametros[0] = bean.getIdInstitucion().toString();
				 	parametros[1] =bean.getIdTipoExpediente().toString();
					ArrayList faseSel =null;
					ArrayList estadoSel=null;
					if(!modo.equalsIgnoreCase("Nuevo")){
					 	faseSel = new ArrayList ();
					 	estadoSel = new ArrayList ();
					 	if (bean.getIdFase()!=null) {
					 			faseSel.add(parametros[0]+","+parametros[1]+","+bean.getIdFase().toString());
							  	if (bean.getIdEstado()!=null){
								estadoSel.add(parametros[0]+","+parametros[1]+","+bean.getIdFase().toString()+","+bean.getIdEstado().toString());
							 	}else{
							 		faseSel.add("");
							 	};
					 	}else {
					 		estadoSel.add("");
					 	}//Anhadimos como elemento seleccionado una cadena vacia
					};	
								 	
			if (modo!=null && modo.equals("Ver")){ %>
				<html:text name="tiposAnotacionesForm" property="idInst_idExp_idFase" size="15" maxlength="30" styleClass="boxConsulta" value="<%=descFase%>" readonly="true"></html:text>
			<% }else{%>
					
			<%	 	if (bEditable){ 				 						 		
			%>				 	
				<siga:ComboBD nombre = "idInst_idExp_idFase" tipo="cmbFases" elementoSel="<%=faseSel%>" clase="boxCombo" parametro="<%=parametros%>" obligatorio="false" accion="Hijo:idInst_idExp_idFase_idEstado"/>
				
			<% } else { %>
				<siga:ComboBD nombre = "idInst_idExp_idFase" tipo="cmbFases" clase="boxCombo" parametro="<%=parametros%>" obligatorio="false" accion="Hijo:idInst_idExp_idFase_idEstado"/>
			<% } 
		   }%>
		</td>
		</tr>
		<tr>
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.estado"/>
		</td>
		<td>		
		  <%if (modo!=null && modo.equals("Ver")){ %>	
				
				<html:text name="tiposAnotacionesForm" property="idInst_idExp_idFase" size="15" maxlength="30" styleClass="boxConsulta" value="<%=descEstado%>" readonly="true"></html:text>
		 <%}else{%>
			    <siga:ComboBD nombre = "idInst_idExp_idFase_idEstado" tipo="cmbEstados" clase="boxCombo" elementoSel="<%=estadoSel%>" obligatorio="false" hijo="t"/>					
		 <%} %>
		</td>
		</tr>
		<tr>
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.mensaje"/>
		</td>
		<td>
		  <%if (modo!=null && modo.equals("Ver")){ %>	
			<html:textarea name="tiposAnotacionesForm" property="mensaje" cols="50" rows="5" onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)" styleClass="boxConsulta" value="<%=bean.getMensaje()%>" readonly="true"></html:textarea>
		   <%}else{%>
		    <html:textarea name="tiposAnotacionesForm" property="mensaje" cols="50" rows="5" onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)" styleClass="box" value="<%=bean.getMensaje()%>"></html:textarea>	
		  <%} %>	
		
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
	
		function recargarCombos()
		{ <%if(modo!=null && !modo.equals("Ver")){%>
			var tmp1 = document.getElementsByName("idInst_idExp_idFase");
			var tmp2 = tmp1[0];			 
			tmp2.onchange();
			<%}%>
		}


		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
			sub();		
			if (validateTiposAnotacionesForm(document.TiposAnotacionesForm)){
				TiposAnotacionesForm.submit();			
				window.top.returnValue="MODIFICADO";
			}
			else{
				
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
		{		}
	
		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
