<!-- denunciantes_Edit.jsp -->
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
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.ExpDenuncianteBean"%>
<%@ page import="com.siga.expedientes.form.ExpDenuncianteForm"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	ExpDenuncianteForm form = (ExpDenuncianteForm)request.getAttribute("ExpDenuncianteForm");
	String pob = form.getPoblacion();
	if (pob==null || pob.trim().equals("")) {
		pob = form.getPoblacionExt();
	}
	String accion = (String)request.getAttribute("accion");
	String boxStyle = accion.equals("consulta")?"boxConsulta":"box";
	boolean editable = accion.equals("consulta")?false:true;
	String botones = "C";
	if (accion.equals("nuevo")){	
		botones = "Y,C";
	}else if (accion.equals("edicion")){
		botones = "Y,R,C";
	}
	String buscarPersona = UtilidadesString.getMensajeIdioma(userBean, "general.boton.search");
	String buscar = UtilidadesString.getMensajeIdioma(userBean, "envios.remitentes.elegirdireccion");
	
	request.removeAttribute("accion");
	
	 String anioExpediente = (String)request.getAttribute("anioExpediente");
	 String idInstitucion = (String)request.getAttribute("idInstitucion");
	 String idInstitucion_TipoExpediente = (String)request.getAttribute("idInstitucion_TipoExpediente");
	 String numExpediente = (String)request.getAttribute("numExpediente");
	 String idTipoExpediente = (String)request.getAttribute("idTipoExpediente");
	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
	<!-- Validaciones en Cliente -->
	<html:javascript formName="ExpDenuncianteForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
    <script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
</head>

<body>

	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalMedia" align="center">


	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<% ExpDenuncianteForm f = (ExpDenuncianteForm)request.getAttribute("ExpDenuncianteForm"); %>
				<%=f.getTituloVentana()%>
			</td>
		</tr>
	</table>
	


	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->


	<table  class="tablaCentralCamposMedia"  align="center">

	<html:form action="/EXP_Auditoria_Denunciante.do" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>
	<html:hidden property = "modo" value = ""/>

	<html:hidden property = "idPersona"   value = ""/>
	<html:hidden property = "idDireccion" value = ""/>
	
	<html:hidden property = "idInstitucion" value = "<%=idInstitucion%>"/>
	<html:hidden property = "idTipoExpediente" value = "<%=idTipoExpediente%>"/>
	<html:hidden property = "numExpediente" value = "<%=numExpediente%>"/>
	<html:hidden property = "anioExpediente" value = "<%=anioExpediente%>"/>
	<html:hidden property = "idInstitucion_TipoExpediente" value = "<%=idInstitucion_TipoExpediente%>"/>
	

	<tr>				
	<td>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.cliente">

	<table class="tablaCampos" align="center">

	<!-- FILA -->
		<tr>					
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.nombre"/>&nbsp(*)
			</td>				
			<td>
				<html:text name="ExpDenuncianteForm" property="nombre" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.primerapellido"/>&nbsp(*)
			</td>
			<td>
				<html:text name="ExpDenuncianteForm" property="primerApellido" styleClass="boxConsulta" readonly="true"></html:text>				
			</td>

		</tr>
		
	<!-- FILA -->
		<tr>		
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.segundoapellido"/>&nbsp(*)
			</td>
			<td>
				<html:text name="ExpDenuncianteForm" property="segundoApellido" styleClass="boxConsulta" readonly="true"></html:text>				
			</td>	
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.nif"/>
			</td>				
			<td>				
				<html:text name="ExpDenuncianteForm" property="nif" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
		</tr>
		
	<!-- FILA -->
		<tr>		
		
<% if (accion.equals("nuevo")){%>			
			<td colspan="4" align="right">
				<input type="button" class="button" name="idButton" alt="<%=buscarPersona%>" id="idButton"  onclick="return buscarCliente();" value="<%=buscarPersona%>"/>&nbsp;
			</td>	
<%}else{%>
			<td colspan="4"></td>
<%}%>			
		</tr>
		
	</table>
	</siga:ConjCampos>

	<siga:ConjCampos leyenda="expedientes.auditoria.literal.direccion">
	<table width="100%">
		
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.direccion"/>
		</td>				
		<td colspan="3">
			<html:textarea cols="90" rows="2"  property="direccion"  name="ExpDenuncianteForm"  onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)" styleClass="boxConsulta" readonly="true"></html:textarea>
		</td>	
		</tr>		
		
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.poblacion"/>
		</td>				
		<td>
			<html:text name="ExpDenuncianteForm" property="poblacion" size="30" maxlength="100" styleClass="boxConsulta" value="<%=pob%>"> readonly="true"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.codigopostal"/>
		</td>		
		<td>
			<html:text name="ExpDenuncianteForm" property="cpostal" size="5" maxlength="5" styleClass="boxConsulta" readonly="true"></html:text>
		</td>
		</tr>
				
		<!-- FILA -->
		<tr>	
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.provincia"/>
		</td>				
		<td>
			<html:text name="ExpDenuncianteForm" property="provincia" size="20" maxlength="100" styleClass="boxConsulta" readonly="true"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.pais"/>
		</td>				
		<td>
			<html:text name="ExpDenuncianteForm" property="pais" size="20" maxlength="100" styleClass="boxConsulta" readonly="true"></html:text>
		</td>				
		</tr>
		
		<!-- FILA -->
		<tr>				
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.telefono"/>
		</td>				
		<td colspan="2">
			<html:text name="ExpDenuncianteForm" property="telefono" size="14" maxlength="20" styleClass="boxConsulta" readonly="true"></html:text>
		</td>
		
		<td colspan="" align="right">
			<% if (accion.equals("nuevo") || accion.equals("edicion") ){ %>
				<input type="button" name="idButton" class="button" alt="<%=buscar%>" id="buscarDir" onclick="return buscarDireccion();" value="<%=buscar%>"/>&nbsp;
			<% } %>
		</td>
		</tr>				
		
		</table>

	</siga:ConjCampos>


	</td>
</tr>

</html:form>

<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="modo" value="abrirBusquedaModal">
	<input type="hidden" name="clientes"	value="1">
</html:form>


<html:form action="/ENV_Destinatario_Manual.do" method="POST" target="submitArea">
	<input type="hidden" name="actionModal"   value="">
	<input type="hidden" name="modo"          value="">
	<input type="hidden" name="idPersona"     value="">
	<input type="hidden" name="idInstitucion" value="">
	<input type="hidden" name="idTipoEnvio"   value="">
</html:form>

</table>



	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->	

		<siga:ConjBotonesAccion botones="<%=botones%>" modal="M" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">


		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
			sub();		
			if (validateExpDenuncianteForm(document.ExpDenuncianteForm)){
				<%if (accion.equals("nuevo")){%>
					document.forms[0].modo.value="insertar";
				<%}else{%>
					document.forms[0].modo.value="modificar";
				<%}%>
				ExpDenuncianteForm.submit();
			}else{
			
				fin();
				return false;
			
			}									
		}

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.close();
		}
	
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
	
	
		function refrescarLocal() 
		{		
			parent.location.reload();
		}
		
		function buscarCliente()
		{			
			var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
			if (resultado!=undefined && resultado[0]!=undefined ){
				document.forms[0].idPersona.value       = resultado[0];
				document.forms[0].nif.value             = resultado[3];
				document.forms[0].nombre.value          = resultado[4];
				document.forms[0].primerApellido.value  = resultado[5];
				document.forms[0].segundoApellido.value = resultado[6]; 

				document.forms[2].idInstitucion.value   = resultado[1];  // para la busqueda de direcciones
				
				// Si tiene una unica direccion, la seleccionamos. Sino resetamos la direccion
				if (resultado[7] != undefined) {
					document.forms[0].direccion.value   = resultado[7];
					document.forms[0].poblacion.value   = resultado[8];
					document.forms[0].provincia.value   = resultado[9];
					document.forms[0].pais.value        = resultado[10];
					document.forms[0].cpostal.value     = resultado[11];
					document.forms[0].idDireccion.value = resultado[12];

					if (trim(resultado[13])=="") document.forms[0].telefono.value=resultado[14]; // el movil
					else document.forms[0].telefono.value=resultado[13];
				}
				else {
					document.forms[0].direccion.value="";
					document.forms[0].poblacion.value="";
					document.forms[0].provincia.value="";
					document.forms[0].pais.value="";
					document.forms[0].cpostal.value="";
					document.forms[0].idDireccion.value="";
					document.forms[0].telefono.value="";
				}
			}		
		}

		//Asociada al boton Buscar Direccion -->
		function buscarDireccion() 
		{
			<%  if(accion.equals("nuevo")) { %>
					if(document.forms[0].idPersona.value.length == 0) {					
						alert ('<siga:Idioma key="factSJCS.resumenPagos.literal.seleccionarPersona"/>');
						return;
					}
					document.forms[2].idPersona.value = document.forms[0].idPersona.value;		
			<%  } 
				else { 
					ExpDenuncianteBean bean = (ExpDenuncianteBean)ses.getAttribute("DATABACKUP_BEAN");
					if (bean != null) {
				%>
						document.forms[2].idPersona.value     = "<%=bean.getIdPersona()%>";
						document.forms[2].idInstitucion.value = "<%=bean.getIdInstitucion()%>"
			     <% }
			   } %>
				
			document.forms[2].idTipoEnvio.value = "-1";		
			document.forms[2].modo.value = "buscar";
			
			var direccion = ventaModalGeneral("RemitentesForm","G");
			if (direccion!=undefined && direccion[0]!=undefined) {
				document.forms[0].direccion.value=direccion[0];
				document.forms[0].poblacion.value=direccion[1];
				document.forms[0].provincia.value=direccion[2];
				document.forms[0].pais.value=direccion[3];
				document.forms[0].cpostal.value=direccion[4];
				document.forms[0].idDireccion.value=direccion[11];

				if (trim(direccion[13])=="") document.forms[0].telefono.value=direccion[14]; // el movil
				else document.forms[0].telefono.value=direccion[13];
			} 
		//alert(document.forms[0].idPersona.value.length);
		return;
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
