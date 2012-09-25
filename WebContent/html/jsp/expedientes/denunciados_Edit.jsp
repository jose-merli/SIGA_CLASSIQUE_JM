<!-- denunciados_Edit.jsp -->
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
<%@ page import="com.siga.beans.ExpDenunciadoBean"%>
<%@ page import="com.siga.expedientes.form.ExpDenunciadoForm"%>
<%@ page import="java.util.Properties"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	ExpDenunciadoForm form = (ExpDenunciadoForm)request.getAttribute("ExpDenunciadoForm");
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
	}else if (accion.equals("edicion") || accion.equals("edicionDenunciado")){
		botones = "Y,R,C";
	}
	String buscarPersona = UtilidadesString.getMensajeIdioma(userBean, "general.boton.search");
	String buscar = UtilidadesString.getMensajeIdioma(userBean, "envios.remitentes.elegirdireccion");
	String nuevaDireccion = UtilidadesString.getMensajeIdioma(userBean, "envios.remitentes.nuevaDireccion");
	
	
	
	
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
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
		
	<!-- Validaciones en Cliente -->
	<html:javascript formName="ExpDenunciadoForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
    <script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	
</head>

<body>

	
<!-- INICIO ******* CAPA DE PRESENTACION ****** -->

<div id="camposRegistro" class="posicionModalMedia" align="center">


	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<% ExpDenunciadoForm f = (ExpDenunciadoForm)request.getAttribute("ExpDenunciadoForm"); %>
				<%=f.getTituloVentana()%>
			</td>
		</tr>
	</table>
	


	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->


	<table  class="tablaCentralCamposMedia"  align="center">

	<html:form action="/EXP_Auditoria_Denunciado" method="POST" target="submitArea">
	
	<html:hidden property = "hiddenFrame" value = "1"/>
	
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "modal" />

	<html:hidden property = "idPersona" />
	<html:hidden property = "idDireccion"/>
	<html:hidden property = "numColegiado"/>
	
	
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
				<html:text name="ExpDenunciadoForm" property="nombre" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.primerapellido"/>&nbsp(*)
			</td>
			<td>
				<html:text name="ExpDenunciadoForm" property="primerApellido" styleClass="boxConsulta" readonly="true"></html:text>				
			</td>

		</tr>
		
	<!-- FILA -->
		<tr>		
		
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.segundoapellido"/>&nbsp(*)
			</td>
			<td>
				<html:text name="ExpDenunciadoForm" property="segundoApellido" styleClass="boxConsulta" readonly="true"></html:text>				
			</td>	
			<td class="labelText">
				<siga:Idioma key="expedientes.auditoria.literal.nif"/>
			</td>				
			<td>				
				<html:text name="ExpDenunciadoForm" property="nif" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
		</tr>
		
	<!-- FILA -->
		<tr>		
		
<% //if (accion.equals("nuevo")){
if (accion.equals("nuevo")||accion.equals("edicionDenunciado")){
%>			
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
			<html:textarea cols="90" rows="2"  property="direccion"  name="ExpDenunciadoForm"  onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)" styleClass="boxConsulta" readonly="true"></html:textarea>
		</td>	
		</tr>		
		
		<!-- FILA -->
		<tr>				

		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.poblacion"/>
		</td>				
		<td>
			<html:text name="ExpDenunciadoForm" property="poblacion" size="30" maxlength="100" styleClass="boxConsulta" value="<%=pob%>"> readonly="true"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.codigopostal"/>
		</td>		
		<td>
			<html:text name="ExpDenunciadoForm" property="cpostal" size="5" maxlength="5" styleClass="boxConsulta" readonly="true"></html:text>
		</td>
		</tr>
				
		<!-- FILA -->
		<tr>	
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.provincia"/>
		</td>				
		<td>
			<html:text name="ExpDenunciadoForm" property="provincia" size="20" maxlength="100" styleClass="boxConsulta" readonly="true"></html:text>
		</td>
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.pais"/>
		</td>				
		<td>
			<html:text name="ExpDenunciadoForm" property="pais" size="20" maxlength="100" styleClass="boxConsulta" readonly="true"></html:text>
		</td>				
		</tr>
		
		<!-- FILA -->
		<tr>				
		<td class="labelText">
			<siga:Idioma key="expedientes.auditoria.literal.telefono"/>
		</td>				
		<td colspan="2">
			<html:text name="ExpDenunciadoForm" property="telefono" size="14" maxlength="20" styleClass="boxConsulta" readonly="true"></html:text>
		</td>
		<td></td>
		</tr>
		<tr>
		<td colspan="4" align="right">
			<% if (!accion.equals("consulta")){ %>
				<input type="button" name="idButton" class="button" alt="<%=buscar%>" id="buscarDir" onclick="return buscarDireccion();" value="<%=buscar%>"/>&nbsp;
				<input type="button" name="idButton" class="button" alt="<%=nuevaDireccion%>" id="buscarDir" onclick="return nuevaDireccion();" value="<%=nuevaDireccion%>"/>&nbsp;
			<% } %>
		</td>
		</tr>				
		
		</table>

	</siga:ConjCampos>


	</td>
</tr>

</html:form>
</table>

<html:form action="/CEN_BusquedaClientesModal" method="POST" target="mainWorkArea" type="">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="modo" value="abrirBusquedaModal">
	<input type="hidden" name="clientes"	value="1">
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
			<html:hidden property = "modo" value = ""/>
			<input type="hidden" name="nombreUsuario" value= ""/>
			<input type="hidden" name="numeroUsuario" value= ""/>
			<input type='hidden' name="idPersona" 		value= ""/>	
			<input type='hidden' name="idInstitucion" value= "<%=idInstitucion%>"/>
			<input type='hidden' name="accion" value= "">
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="actionModal" value="">
			<html:hidden property = "vieneDe" value=""/>
			
		</html:form>

<html:form action="/CEN_DatosGenerales" method="POST" target="mainWorkArea">
		<input type="hidden" name="actionModal" value="1">
		<input type="hidden" name="modo" value="altaNoColegiado">
	</html:form>


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
			if (validateExpDenunciadoForm(document.ExpDenunciadoForm)){
				if(document.ExpDenunciadoForm.modal.value){
					document.ExpDenunciadoForm.modo.value=document.ExpDenunciadoForm.modal.value;
				}else{
					<%if (accion.equals("nuevo")){%>
						document.ExpDenunciadoForm.modo.value="insertar";
					<%}else{%>
						document.ExpDenunciadoForm.modo.value="modificar";
					<%}%>
				}
				
				ExpDenunciadoForm.submit();
			}else{
			
				fin();
				return false;
			
			}									
		}

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			window.top.close();
		}
	
		//Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.ExpDenunciadoForm.reset();
		}
	
	
		function refrescarLocal() 
		{		
			parent.location.reload();
		}
		
		function buscarCliente()
		{		
			
			var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
			if (resultado!=undefined && resultado[0]!=undefined ){
				document.ExpDenunciadoForm.idPersona.value       = resultado[0];
				document.ExpDenunciadoForm.idInstitucion.value   = resultado[1];
				document.ExpDenunciadoForm.numColegiado.value   =  resultado[2];
				document.ExpDenunciadoForm.nif.value             = resultado[3];
				document.ExpDenunciadoForm.nombre.value          = resultado[4];
				document.ExpDenunciadoForm.primerApellido.value  = resultado[5];
				document.ExpDenunciadoForm.segundoApellido.value = resultado[6]; 

				document.RemitentesForm.idInstitucion.value   = resultado[1];  // para la busqueda de direcciones
				document.RemitentesForm.idPersona.value   = resultado[0];
				
				// Si tiene una unica direccion, la seleccionamos. Sino resetamos la direccion
				if (resultado[7] != undefined) {
					document.ExpDenunciadoForm.direccion.value   = resultado[7];
					document.ExpDenunciadoForm.poblacion.value   = resultado[8];
					document.ExpDenunciadoForm.provincia.value   = resultado[9];
					document.ExpDenunciadoForm.pais.value        = resultado[10];
					document.ExpDenunciadoForm.cpostal.value     = resultado[11];
					document.ExpDenunciadoForm.idDireccion.value = resultado[12];

					if (trim(resultado[13])=="") document.ExpDenunciadoForm.telefono.value=resultado[14]; // el movil
					else document.ExpDenunciadoForm.telefono.value=resultado[13];
				}
				else {
					document.ExpDenunciadoForm.direccion.value="";
					document.ExpDenunciadoForm.poblacion.value="";
					document.ExpDenunciadoForm.provincia.value="";
					document.ExpDenunciadoForm.pais.value="";
					document.ExpDenunciadoForm.cpostal.value="";
					document.ExpDenunciadoForm.idDireccion.value="";
					document.ExpDenunciadoForm.telefono.value="";
				}
			}		
		}

		//Asociada al boton Buscar Direccion -->
		function buscarDireccion() 
		{
		    
		    if(document.ExpDenunciadoForm.idPersona.value.length == 0) {					
				alert ('<siga:Idioma key="factSJCS.resumenPagos.literal.seleccionarPersona"/>');
				return;
			}
			document.RemitentesForm.idPersona.value     = document.ExpDenunciadoForm.idPersona.value;
			document.RemitentesForm.idInstitucion.value = document.ExpDenunciadoForm.idInstitucion.value;
			
				
			document.RemitentesForm.idTipoEnvio.value = "-1";		
			document.RemitentesForm.modo.value = "buscar";
			
			var direccion = ventaModalGeneral("RemitentesForm","G");
			if (direccion!=undefined && direccion[0]!=undefined) {
				document.ExpDenunciadoForm.direccion.value=direccion[0];
				document.ExpDenunciadoForm.poblacion.value=direccion[1];
				document.ExpDenunciadoForm.provincia.value=direccion[2];
				document.ExpDenunciadoForm.pais.value=direccion[3];
				document.ExpDenunciadoForm.cpostal.value=direccion[4];
				document.ExpDenunciadoForm.idDireccion.value=direccion[11];
				

				if (trim(direccion[13])=="") document.ExpDenunciadoForm.telefono.value=direccion[14]; // el movil
				else document.ExpDenunciadoForm.telefono.value=direccion[13];
			} 
		//alert(document.ExpDenunciadoForm.idPersona.value.length);
		return;
		}
		
		function nuevaDireccion() 
		{
			
			if(document.ExpDenunciadoForm.idPersona.value.length == 0) {					
					alert ('<siga:Idioma key="factSJCS.resumenPagos.literal.seleccionarPersona"/>');
					return;
				}
			document.consultaDireccionesForm.idPersona.value = document.ExpDenunciadoForm.idPersona.value;
			document.consultaDireccionesForm.idInstitucion.value = document.ExpDenunciadoForm.idInstitucion.value;
			
			document.consultaDireccionesForm.vieneDe.value = '1';
			
			document.consultaDireccionesForm.modo.value = "nuevo";
			
	    	var direccion = ventaModalGeneral(document.consultaDireccionesForm.name, "G");
	    	if(direccion){
	    	document.ExpDenunciadoForm.direccion.value=direccion[0];
	    	
	    	if (trim(direccion[1])=="") document.ExpDenunciadoForm.poblacion.value=direccion[15]; // el movil
				else document.ExpDenunciadoForm.poblacion.value=direccion[1];
				document.ExpDenunciadoForm.poblacion.value=direccion[1];
				document.ExpDenunciadoForm.provincia.value=direccion[2];
				document.ExpDenunciadoForm.pais.value=direccion[3];
				document.ExpDenunciadoForm.cpostal.value=direccion[4];
				document.ExpDenunciadoForm.idDireccion.value=direccion[11];

				if (trim(direccion[13])=="") document.ExpDenunciadoForm.telefono.value=direccion[14]; // el movil
				else document.ExpDenunciadoForm.telefono.value=direccion[13];
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
