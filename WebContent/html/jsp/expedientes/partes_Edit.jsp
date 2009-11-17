<!-- partes_Edit.jsp -->
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
<%@ page import="com.siga.administracion.SIGAConstants,com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.expedientes.form.ExpPartesForm"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
<!-- JSP -->
<bean:define id="pob" name="ExpPartesForm" property="poblacion" type="String"/>
<bean:define id="poblacionExt" name="ExpPartesForm" property="poblacionExt" type="String"/>
<bean:define id="idInstitucion" name="ExpPartesForm" property="idInstitucion" type="String"/>
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
	String buscarPersona = UtilidadesString.getMensajeIdioma(userBean, "general.boton.search");
	String buscar = UtilidadesString.getMensajeIdioma(userBean, "envios.remitentes.elegirdireccion");
	String nuevaDireccion = UtilidadesString.getMensajeIdioma(userBean, "envios.remitentes.nuevaDireccion");
	
	if (pob==null || pob.trim().equals("")) {
		pob = poblacionExt;
	}
	request.removeAttribute("accion");
	request.removeAttribute("idTipoExpediente");
	
%>	

<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		
	<!-- Validaciones en Cliente -->
	<html:javascript formName="ExpPartesForm" staticJavascript="true" />  
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

	<html:form action="/EXP_Auditoria_Partes" method="POST" target="submitArea">
	<html:hidden property = "hiddenFrame" value = "1"/>
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idPersona" />
	<html:hidden property = "idDireccion"/>
	<html:hidden property = "idInstitucion"/>


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
				
				<siga:Idioma key="expedientes.auditoria.literal.nombre"/>&nbsp(*)
			</td>				
			<td>
				<html:text name="ExpPartesForm" property="nombre" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
<% if (editable){%>			
			<td colspan="2" align="right">
				<input type="button" class="button" name="idButton" alt="<%=buscarPersona%>" id="idButton"  onclick="return buscarCliente();" value="<%=buscarPersona%>"/>&nbsp;
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
	
	<siga:ConjCampos leyenda="expedientes.auditoria.literal.direccion">
	<table width="100%">
		
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.direccion"/>
		</td>				
		<td colspan="3">
			<html:textarea cols="90" rows="2"  property="direccion"  name="ExpPartesForm"  onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)" styleClass="boxConsulta" readonly="true"></html:textarea>
		</td>	
		</tr>		
		
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.poblacion"/>
		</td>				
		<td>
			<html:text name="ExpPartesForm" property="poblacion" size="30" maxlength="100" styleClass="boxConsulta" value="<%=pob%>"> readonly="true"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.codigopostal"/>
		</td>		
		<td>
			<html:text name="ExpPartesForm" property="cpostal" size="5" maxlength="5" styleClass="boxConsulta" readonly="true"></html:text>
		</td>
		</tr>
				
		<!-- FILA -->
		<tr>	
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.provincia"/>
		</td>				
		<td>
			<html:text name="ExpPartesForm" property="provincia" size="20" maxlength="100" styleClass="boxConsulta" readonly="true"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.pais"/>
		</td>				
		<td>
			<html:text name="ExpPartesForm" property="pais" size="20" maxlength="100" styleClass="boxConsulta" readonly="true"></html:text>
		</td>				
		</tr>
		
		<!-- FILA -->
		<tr>				
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.telefono"/>
		</td>				
		<td colspan="2">
			<html:text name="ExpPartesForm" property="telefono" size="14" maxlength="20" styleClass="boxConsulta" readonly="true"></html:text>
		</td>
		<td></td>
		</tr>
		<tr>
		<td colspan="4" align="right">
			<% if (accion.equals("nuevo") || accion.equals("edicion") ){ %>
				<input type="button" name="idButton" class="button" alt="<%=buscar%>" id="buscarDir" onclick="return buscarDireccion();" value="<%=buscar%>"/>&nbsp;
				<input type="button" name="idButton" class="button" alt="<%=nuevaDireccion%>" id="buscarDir" onclick="return nuevaDireccion();" value="<%=nuevaDireccion%>"/>&nbsp;
			<% } %>
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

<html:form action="/CEN_BusquedaClientesModal" method="POST"
	target="mainWorkArea" type="">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="modo" value="abrirBusquedaModal">
	<input type="hidden" name="clientes" value="1">
	<input type="hidden" name="permitirAniadirNuevo" value="S">
</html:form>
<html:form action="/ENV_Destinatario_Manual" method="POST" target="submitArea">
	<input type="hidden" name="actionModal"   value="">
	<input type="hidden" name="modo"          value="">
	<input type="hidden" name="idPersona"     value="">
	<input type="hidden" name="idInstitucion" value="">
	<input type="hidden" name="idTipoEnvio"   value="">
</html:form>

<html:form method="post" action="/CEN_ConsultasDirecciones">

	<!-- Campo obligatorio -->
	<html:hidden property="modo" value="" />
	<input type="hidden" name="nombreUsuario" value="" />
	<input type="hidden" name="numeroUsuario" value="" />
	<input type='hidden' name="idPersona" value="" />
	<input type="hidden" name="idInstitucion" value="">
	<input type='hidden' name="accion" value="">
	<!-- RGG: cambio a formularios ligeros -->
	<input type="hidden" name="tablaDatosDinamicosD">
	<input type="hidden" name="actionModal" value="">
	<html:hidden property="vieneDe" value="" />

</html:form>



	<!-- FIN: CAMPOS -->

	<!-- INICIO: BOTONES REGISTRO -->	

		<siga:ConjBotonesAccion botones="<%=botones%>" modal="M" />

	<!-- FIN: BOTONES REGISTRO -->



	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">


		//Asociada al boton GuardarCerrar
		function accionGuardarCerrar() 
		{
			sub();		
			
			if (validateExpPartesForm(document.ExpPartesForm)){
				<%if (accion.equals("nuevo")){%>
					document.ExpPartesForm.modo.value="insertar";
				<%}else{%>
					document.ExpPartesForm.modo.value="modificar";
				<%}%>

				document.ExpPartesForm.submit();
			}else{
			
				fin();
				return false;
			}									
		}

		//Asociada al boton Cerrar
		function accionCerrar() 
		{		
			window.close();
		}
	
		//Asociada al boton Restablecer
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
				document.ExpPartesForm.idPersona.value       = resultado[0];
     			document.ExpPartesForm.idInstitucion.value   = resultado[1];
				document.ExpPartesForm.numColegiado.value    = resultado[2];
				document.ExpPartesForm.nif.value             = resultado[3];
				document.ExpPartesForm.nombre.value          = resultado[4];
				document.ExpPartesForm.primerApellido.value  = resultado[5];
				document.ExpPartesForm.segundoApellido.value = resultado[6]; 

				
				// Si tiene una unica direccion, la seleccionamos. Sino resetamos la direccion
				if (resultado[7] != undefined) {
					document.ExpPartesForm.direccion.value   = resultado[7];
					document.ExpPartesForm.poblacion.value   = resultado[8];
					document.ExpPartesForm.provincia.value   = resultado[9];
					document.ExpPartesForm.pais.value        = resultado[10];
					document.ExpPartesForm.cpostal.value     = resultado[11];
					document.ExpPartesForm.idDireccion.value = resultado[12];

					if (trim(resultado[13])=="") document.forms[0].telefono.value=resultado[14]; // el movil
					else document.ExpPartesForm.telefono.value=resultado[13];
				}
				else {
					document.ExpPartesForm.direccion.value="";
					document.ExpPartesForm.poblacion.value="";
					document.ExpPartesForm.provincia.value="";
					document.ExpPartesForm.pais.value="";
					document.ExpPartesForm.cpostal.value="";
					document.ExpPartesForm.idDireccion.value="";
					document.ExpPartesForm.telefono.value="";
				}
			}		
		}
		
		//Asociada al boton Buscar Direccion -->
		function buscarDireccion() 
		{
			if(document.ExpPartesForm.idPersona.value.length == 0) {					
				alert ('<siga:Idioma key="factSJCS.resumenPagos.literal.seleccionarPersona"/>');
				return;
			}
		
			document.RemitentesForm.idPersona.value = document.ExpPartesForm.idPersona.value;
			document.RemitentesForm.idInstitucion.value = document.ExpPartesForm.idInstitucion.value;

			document.RemitentesForm.idTipoEnvio.value = "-1";		
			document.RemitentesForm.modo.value = "buscar";
			
			var direccion = ventaModalGeneral("RemitentesForm","G");
			if (direccion!=undefined && direccion[0]!=undefined) {
				document.ExpPartesForm.direccion.value=direccion[0];
				document.ExpPartesForm.poblacion.value=direccion[1];
				document.ExpPartesForm.provincia.value=direccion[2];
				document.ExpPartesForm.pais.value=direccion[3];
				document.ExpPartesForm.cpostal.value=direccion[4];
				document.ExpPartesForm.idDireccion.value=direccion[11];

				if (trim(direccion[13])=="") document.ExpPartesForm.telefono.value=direccion[14]; // el movil
				else document.ExpPartesForm.telefono.value=direccion[13];
			} 
		return;
		}
		
		function nuevaDireccion() 
		{
			
			if(document.ExpPartesForm.idPersona.value.length == 0) {					
					alert ('<siga:Idioma key="factSJCS.resumenPagos.literal.seleccionarPersona"/>');
					return;
				}
			
			
			
			document.consultaDireccionesForm.idPersona.value = document.ExpPartesForm.idPersona.value;
			document.consultaDireccionesForm.idInstitucion.value = document.ExpPartesForm.idInstitucion.value;
			document.consultaDireccionesForm.vieneDe.value = '1';
			
			document.consultaDireccionesForm.modo.value = "nuevo";
			
	    	var direccion = ventaModalGeneral(document.consultaDireccionesForm.name, "G");
	    	if(direccion){
	    	document.ExpPartesForm.direccion.value=direccion[0];
	    	
	    	if (trim(direccion[1])=="") document.ExpPartesForm.poblacion.value=direccion[15]; // el movil
				else document.ExpPartesForm.poblacion.value=direccion[1];
				document.ExpPartesForm.poblacion.value=direccion[1];
				document.ExpPartesForm.provincia.value=direccion[2];
				document.ExpPartesForm.pais.value=direccion[3];
				document.ExpPartesForm.cpostal.value=direccion[4];
				document.ExpPartesForm.idDireccion.value=direccion[11];

				if (trim(direccion[13])=="") document.ExpPartesForm.telefono.value=direccion[14]; // el movil
				else document.ExpPartesForm.telefono.value=direccion[13];
			}else{
				accionCerrar();
			}
	    	
			
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
