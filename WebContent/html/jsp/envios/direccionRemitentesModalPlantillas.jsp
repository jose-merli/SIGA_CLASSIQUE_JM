<!-- direccionRemitentesModal.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL MEDIANA -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.envios.form.RemitentesPlantillasForm"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.beans.CenDireccionesBean"%>

<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	
	RemitentesPlantillasForm form = (RemitentesPlantillasForm)request.getAttribute("RemitentesPlantillasForm");
	
	String pob = form.getPoblacion();
	if (pob.trim().equals("")) {
		pob = form.getPoblacionExt();
	}
	
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	
	String elegirdireccion = UtilidadesString.getMensajeIdioma(user, "envios.remitentes.elegirdireccion");
	String errorDireccion = UtilidadesString.getMensajeIdioma(user, "messages.envios.error.direccionNecesaria");

	String editable = (String) request.getAttribute("editable");
	boolean bEditable = editable.equals("true");
	String botones = bEditable?"Y,C":"C";
	String estilo = bEditable?"box":"boxConsulta";
	
	String accion = (String) request.getAttribute("accion");
	
	String idTipoEnvios       = form.getIdTipoEnvios();
	String idPlantillaEnvios  = form.getIdPlantillaEnvios();
	
	String pais = "", direccion = "", poblacion = "", provincia = "", cPostal = "", movil = "",
	       fax1 = "", fax2 = "", eMail = "", idPoblacion = "", idProvincia= "", idPais = "", idDireccion = "", poblacionExtranjera = "";
	Hashtable h = (Hashtable) request.getAttribute("unicaDireccion");
	if (h != null) {
		pais        = (String) h.get("PAIS"); 		                          if (pais == null)        pais        = new String("");
		poblacion   = (String) h.get("POBLACION");	                          if (poblacion == null)   poblacion   = new String("");
		provincia   = (String) h.get("PROVINCIA");	                          if (provincia == null)   provincia   = new String("");
		direccion   = (String) h.get(CenDireccionesBean.C_DOMICILIO);	      if (direccion == null)   direccion   = new String("");
		cPostal     = (String) h.get(CenDireccionesBean.C_CODIGOPOSTAL);   	  if (cPostal == null)     cPostal     = new String("");
		fax1        = (String) h.get(CenDireccionesBean.C_FAX1);		      if (fax1 == null)        fax1        = new String("");
		fax2        = (String) h.get(CenDireccionesBean.C_FAX2);		      if (fax2 == null)        fax2        = new String("");
		movil       = (String) h.get(CenDireccionesBean.C_MOVIL);		      if (movil == null)       movil       = new String("");
		eMail       = (String) h.get(CenDireccionesBean.C_CORREOELECTRONICO); if (eMail == null)       eMail       = new String("");
		idPoblacion = (String) h.get(CenDireccionesBean.C_IDPOBLACION);	      if (idPoblacion == null) idPoblacion = new String("");
		idProvincia = (String) h.get(CenDireccionesBean.C_IDPROVINCIA);	      if (idProvincia == null) idProvincia = new String("");
		idDireccion = (String) h.get(CenDireccionesBean.C_IDDIRECCION);	      if (idDireccion == null) idDireccion = new String("");
		idPais      = (String) h.get(CenDireccionesBean.C_IDPAIS);            if (idPais      == null) idPais      = new String("");
		poblacionExtranjera = (String) h.get(CenDireccionesBean.C_POBLACIONEXTRANJERA);	if (poblacionExtranjera == null) poblacionExtranjera = new String("");
	}
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	 <script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

</head>

<body>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
-->
<div id="camposRegistro" class="posicionModalGrande" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposGrande"  align="center">

	<html:form action="/ENV_RemitentesPlantilla.do" method="POST" target="submitArea">
		<html:hidden property = "modo" value = ""/>
		<html:hidden property = "modal" value = "true"/>	
		<html:hidden property = "actionModal" value = ""/>
		
		<html:hidden property = "idTipoEnvios"      value="<%=idTipoEnvios%>"/>
		<html:hidden property = "idPlantillaEnvios" value="<%=idPlantillaEnvios%>"/>
		<html:hidden property = "idPersona"/>
		<html:hidden property = "idInstitucion"/>
		<html:hidden property = "idPoblacion"/>
		<html:hidden property = "idProvincia"/>
		<html:hidden property = "idPais"/>
		<html:hidden property = "idDireccion"/>
		<html:hidden property = "poblacionExt"/>
		<html:hidden property = "modificado" value="false"/>		
	<tr>				
	<td>

	<siga:ConjCampos leyenda="censo.busquedaClientes.literal.datosCliente">

		<table class="tablaCampos" align="center">
	
		<!-- FILA -->		
		<tr>			
			<td class="labelText">
				<siga:Idioma key="censo.busquedaClientes.literal.nombre"/>
			</td>
			<td>
				<html:text name="RemitentesPlantillasForm" property="nombre" size="30" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
			
			<td class="labelText">
				<siga:Idioma key="censo.busquedaClientes.literal.apellido1"/>
			</td>
			<td>
				<html:text name="RemitentesPlantillasForm" property="apellidos1" size="30" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
	
			<td class="labelText">
				<siga:Idioma key="censo.busquedaClientes.literal.apellido2"/>
			</td>
			<td>
				<html:text name="RemitentesPlantillasForm" property="apellidos2" size="30" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
		</tr>
		
		<tr>			
			<td class="labelText">
				<siga:Idioma key="envios.definir.literal.descripcion"/>
			</td>
			<td colspan="5">
				<html:text name="RemitentesPlantillasForm" property="descripcion" size="100" maxlength="100" styleClass="<%=estilo%>" readonly="<%=!bEditable%>"></html:text>
			</td>
		</table>
		
		</siga:ConjCampos>
		<siga:ConjCampos leyenda="envios.definir.literal.datosdestino">

		<table class="tablaCampos" align="center">	
		
		<!-- FILA -->		
		<tr>			
			<td class="labelText">
				<siga:Idioma key="censo.datosDireccion.literal.direccion"/>&nbsp;(*)
			</td>
			<td colspan="5">
				<html:textarea cols="70" rows="2" name="RemitentesPlantillasForm" property="domicilio" onKeyDown="cuenta(this,100)" onChange="cuenta(this,100)" styleClass="boxConsulta" readOnly="true"></html:textarea>
			</td>
		</tr>
		
		<!-- FILA -->		
		<tr>			
			<td class="labelText">
				<siga:Idioma key="censo.datosDireccion.literal.poblacion"/>
			</td>
			<td>
				<html:text property="poblacionString" size="30" styleClass="boxConsulta" readonly="true" value="<%=pob%>"></html:text>
			</td>
	
			<td class="labelText">
				<siga:Idioma key="censo.datosDireccion.literal.provincia"/>
			</td>
			<td>
				<html:text name="RemitentesPlantillasForm" property="provincia" size="30" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
			
			<td class="labelText">
				<siga:Idioma key="censo.datosDireccion.literal.pais"/>
			</td>
			<td>
				<html:text name="RemitentesPlantillasForm" property="pais" size="30" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
		</tr>
		
		<!-- FILA -->		
		<tr>			
			<td class="labelText">
				<siga:Idioma key="censo.datosDireccion.literal.cp"/>
			</td>
			<td>
				<html:text name="RemitentesPlantillasForm" property="codigoPostal" size="30" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
	
			<td class="labelText">
				<siga:Idioma key="censo.datosDireccion.literal.fax1"/>
			</td>
			<td>
				<html:text name="RemitentesPlantillasForm" property="fax1" size="30" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
			
			<td class="labelText">
				<siga:Idioma key="censo.datosDireccion.literal.fax2"/>
			</td>
			<td>
				<html:text name="RemitentesPlantillasForm" property="fax2" size="30" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
		</tr>
		
		<!-- FILA -->		
		<tr>			
			<td class="labelText">
				<siga:Idioma key="censo.datosDireccion.literal.movil"/>
			</td>
			<td colspan="1">
				<html:text name="RemitentesPlantillasForm" property="movil" size="30" styleClass="boxConsulta" readonly="true" ></html:text>
			</td>
			<td class="labelText">
				<siga:Idioma key="censo.datosDireccion.literal.correo"/>
			</td>
			<td colspan="3">
				<html:text name="RemitentesPlantillasForm" property="correoElectronico" size="80" styleClass="boxConsulta" readonly="true"></html:text>
			</td>
		</tr>
		
		<!-- FILA -->		
		<tr>
			<%if (bEditable){%>
			<td align="center">
				<input type="button" class="button" id="idButton" alt="<%=elegirdireccion%>" onclick="return buscar();" value="<%=elegirdireccion%>"/>		
				
			</td>
			<%}%>
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

		<siga:ConjBotonesAccion botones="<%=botones%>" modal="G" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

	<%	if (accion.equalsIgnoreCase("nuevo")) { %>
	
			document.forms[0].domicilio.value         = "<%=UtilidadesString.escape(direccion)%>";
			document.forms[0].poblacionString.value   = "<%=UtilidadesString.escape(poblacion)%>";
			document.forms[0].provincia.value         = "<%=UtilidadesString.escape(provincia)%>";
			document.forms[0].pais.value              = "<%=UtilidadesString.escape(pais)%>";
			document.forms[0].codigoPostal.value      = "<%=cPostal%>";
			document.forms[0].fax1.value              = "<%=fax1%>";
			document.forms[0].fax2.value              = "<%=fax2%>";
			document.forms[0].movil.value             = "<%=movil%>";
			document.forms[0].correoElectronico.value = "<%=eMail%>";
			document.forms[0].idPoblacion.value       = "<%=idPoblacion%>";
			document.forms[0].idProvincia.value       = "<%=idProvincia%>";
			document.forms[0].idPais.value            = "<%=idPais%>";
			document.forms[0].idDireccion.value       = "<%=idDireccion%>";
			document.forms[0].poblacionExt.value      = "<%=UtilidadesString.escape(poblacionExtranjera)%>";
			if (document.forms[0].idPais.value!='<%=ClsConstants.ID_PAIS_ESPANA%>') {
				document.forms[0].poblacionString.value = document.forms[0].poblacionExt.value;
			}
			document.forms[0].modificado.value = "true";
	<% } %>


		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{	
			sub();
			var accion = '<%=accion%>';
				
			if (accion=="nuevo") {
				if (document.forms[0].idDireccion.value!=""){			
					document.forms[0].modo.value="insertar";
					document.forms[0].submit();
				} else {			
					alert('<%=errorDireccion%>');
					fin();
					return;
				}										
			} else {
				document.forms[0].modo.value="modificar";
				document.forms[0].submit();
			}
			
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
			top.cierraConParametros("NORMAL");
		}

	<!-- Asociada al boton Buscar -->
		function buscar() 
		{		
			document.forms[0].modo.value="buscar";
			
			var direccion = ventaModalGeneral("RemitentesPlantillasForm","G");
			
			if (direccion!=undefined && direccion[0]!=undefined) {
				document.forms[0].domicilio.value=direccion[0];
				document.forms[0].poblacionString.value=direccion[1];
				document.forms[0].provincia.value=direccion[2];
				document.forms[0].pais.value=direccion[3];
				document.forms[0].codigoPostal.value=direccion[4];
				document.forms[0].fax1.value=direccion[5];
				document.forms[0].fax2.value=direccion[6];
				document.forms[0].correoElectronico.value=direccion[7];
				document.forms[0].idPoblacion.value=direccion[8];
				document.forms[0].idProvincia.value=direccion[9];
				document.forms[0].idPais.value=direccion[10];
				document.forms[0].idDireccion.value=direccion[11];
				document.forms[0].movil.value=direccion[14];
				document.forms[0].poblacionExt.value=direccion[15];
				if (document.forms[0].idPais.value!='<%=ClsConstants.ID_PAIS_ESPANA%>') {
					document.forms[0].poblacionString.value=document.forms[0].poblacionExt.value;
				}
				document.forms[0].modificado.value="true";
			} 
		}

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