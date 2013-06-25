<!-- documentacion_Edit.jsp -->
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
<%@ page import="com.siga.beans.ExpDenuncianteBean"%>
<%@ page import="com.siga.expedientes.form.ExpDocumentacionForm"%>
<%@ page import="java.util.Properties"%>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	
	String accion = (String)request.getAttribute("accion");
	String boxStyle = accion.equals("consulta")?"boxConsulta":"box";
	boolean editable = accion.equals("consulta")?false:true;
	String botones = "C";
	if (accion.equals("nuevo")){	
		botones = "Y,C";
	}else if (accion.equals("edicion")){
		botones = "Y,R,C";
	}
	
	request.removeAttribute("accion");
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
	<!-- Validaciones en Cliente -->
	<html:javascript formName="ExpDocumentacionForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	
</head>

<body>


<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<% ExpDocumentacionForm f = (ExpDocumentacionForm)request.getAttribute("ExpDocumentacionForm"); %>
				<%=f.getTituloVentana()%>
			</td>
		</tr>
	</table>

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:form action="/EXP_Auditoria_Documentacion.do" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>
	<html:hidden property = "modo" value = ""/>

	<tr>				
	<td>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.estado">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				

			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.fase"/>
			</td>				
			<td>
				<html:text name="ExpDocumentacionForm" property="fase"  styleClass="boxConsulta" readonly="true"></html:text>				
				<html:hidden property = "idFase"/>
			</td>
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.estado"/>
			</td>
			<td>
				<html:text name="ExpDocumentacionForm" property="estado"  styleClass="boxConsulta" readonly="true"></html:text>
				<html:hidden property = "idEstado"/>
			</td>
		</tr>				
		
		</table>

	</siga:ConjCampos>
	
	<siga:ConjCampos leyenda="expedientes.auditoria.literal.documento">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->
		<tr>				

			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.descripcion"/>&nbsp(*)
			</td>				
			<td colspan="3">
				<html:text name="ExpDocumentacionForm" property="descripcion" size="40" maxlength="100" styleClass="<%=boxStyle%>" readonly="<%=!editable%>"></html:text>
			</td>
		</tr>
		<!-- FILA -->
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.ruta"/>
			</td>				
			<td colspan="3">
				<html:text name="ExpDocumentacionForm" property="ruta" size="40" maxlength="500" styleClass="<%=boxStyle%>" readonly="<%=!editable%>"></html:text>
			</td>				
		</tr>		
		<!-- FILA -->
		<tr>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.regentrada"/>
			</td>				
			<td>
				<html:text name="ExpDocumentacionForm" property="regentrada" size="15" maxlength="30" styleClass="<%=boxStyle%>" readonly="<%=!editable%>"></html:text>
			</td>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.regsalida"/>
			</td>				
			<td>
				<html:text name="ExpDocumentacionForm" property="regsalida" size="15" maxlength="30" styleClass="<%=boxStyle%>" readonly="<%=!editable%>"></html:text>
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

		<siga:ConjBotonesAccion botones="<%=botones%>" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
			sub();		
			if (validateExpDocumentacionForm(document.ExpDocumentacionForm)){
				<%if (accion.equals("nuevo")){%>
					document.forms[0].modo.value="insertar";
				<%}else{%>
					document.forms[0].modo.value="modificar";
				<%}%>
				ExpDocumentacionForm.submit();
			}else{
			
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
			document.forms[0].reset();
		}
	
	
		function refrescarLocal() 
		{	
			parent.location.reload();
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
