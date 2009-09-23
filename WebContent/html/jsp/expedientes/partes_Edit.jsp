<!-- partes_Edit.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants,com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ExpDenuncianteBean"%>
<%@ page import="com.siga.expedientes.form.ExpPartesForm"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	String accion = (String)request.getAttribute("accion");
	String boxStyle = accion.equals("consulta")?"boxConsulta":"box";
	boolean editable = accion.equals("consulta")?false:true;
	String botones = "C";
	ArrayList vRol = new ArrayList();
	
	if (accion.equals("nuevo")){	
		botones = "Y,C";
	}else if (accion.equals("edicion")){
		botones = "Y,R,C";
		vRol.add((String)request.getAttribute("idRol"));
		request.removeAttribute("idRol");
	}
	
	
	String dato[] = {userBean.getLocation(),(String)request.getAttribute("idTipoExpediente")};
	String buscar = UtilidadesString.getMensajeIdioma(userBean, "general.search");
	
	request.removeAttribute("accion");
	request.removeAttribute("idTipoExpediente");
	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
	<!-- Validaciones en Cliente -->
	<html:javascript formName="ExpPartesForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
</head>

<body>

	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalMedia" align="center">


	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<% ExpPartesForm f = (ExpPartesForm)request.getAttribute("ExpPartesForm"); %>
				<%=f.getTituloVentana()%>
			</td>
		</tr>
	</table>
	

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposMedia"  align="center">

	<html:form action="/EXP_Auditoria_Partes.do" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>
	<html:hidden property = "modo" value = ""/>


	<tr>				
	<td>

	</td>
	</tr>				



	<tr>				
	<td>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.cliente">

	<table class="tablaCampos" align="center">

	<!-- FILA -->
		<tr>					
			<td class="labelText">
				<html:hidden name="ExpPartesForm" property = "idPersona"/>
				<siga:Idioma key="expedientes.auditoria.literal.nombre"/>&nbsp(*)
			</td>				
			<td>
				<html:text name="ExpPartesForm" property="nombre" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
<% if (editable){%>			
			<td colspan="2" align="right">
				<input type="button" name="idButton" class="button" alt="<%=buscar%>" id="searchPerson"  onclick="return buscarPersona();" value="<%=buscar%>"/>&nbsp;
			</td>	
<%}else{%>
			<td colspan="2"></td>
<%}%>			
		</tr>
		
	<!-- FILA -->
		<tr>		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.primerapellido"/>&nbsp(*)
			</td>
			<td>
				<html:text name="ExpPartesForm" property="primerApellido" styleClass="boxConsulta" readonly="true"></html:text>				
			</td>
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.segundoapellido"/>&nbsp(*)
			</td>
			<td>
				<html:text name="ExpPartesForm" property="segundoApellido" styleClass="boxConsulta" readonly="true"></html:text>				
			</td>	
		</tr>
	
	<!-- FILA -->
		<tr>					
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.nif"/>&nbsp(*)
			</td>				
			<td>				
				<html:text name="ExpPartesForm" property="nif" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.ncolegiado"/>&nbsp(*)
			</td>
			<td>
				<html:text name="ExpPartesForm" property="numColegiado" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
			
		</tr>
	
	</table>
		
	</siga:ConjCampos>
	
	<siga:ConjCampos leyenda="expedientes.auditoria.literal.rol">

	<table class="tablaCampos" align="center">
	
	<!-- FILA -->
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.rol"/>&nbsp(*)
			</td>				
			<td>
				<%if (editable){%>		
				<siga:ComboBD nombre = "idRol" tipo="cmbRol"  clase="boxCombo" obligatorio="true" parametro="<%=dato%>" ElementoSel="<%=vRol%>"/>
				<%}else{%>
				<html:text name="ExpPartesForm" property="rolSel"  styleClass="boxConsulta" readonly="true"></html:text>
				<%}%>
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
			if (validateExpPartesForm(document.ExpPartesForm)){
				<%if (accion.equals("nuevo")){%>
					document.forms[0].modo.value="insertar";
				<%}else{%>
					document.forms[0].modo.value="modificar";
				<%}%>
				ExpPartesForm.submit();
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
		
		function buscarPersona()
		{					
			var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
			if (resultado!=undefined && resultado[0]!=undefined ){
				document.forms[0].idPersona.value=resultado[0];
				document.forms[0].numColegiado.value=resultado[2];
				document.forms[0].nif.value=resultado[3];
				document.forms[0].nombre.value=resultado[4];
				document.forms[0].primerApellido.value=resultado[5];
				document.forms[0].segundoApellido.value=resultado[6];
			}
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>

	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes"	value="1">
	</html:form>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
		
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
