<!DOCTYPE html>
<html>
<head>
<!-- datosMovimientos.jsp -->
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
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	
	String[] comboParams = {usr.getLocation()};
	//campos a mostrar
	String nif = "", ncolegiado="", nombre ="", descripcion = "", cantidad="", motivo="", idMovimiento="", idPersona="", 
	pago="", idPago="",idTurno="",idInstitucion="",anio="",numero="",nactuacion="", idGuardia="",fechaInicio="",asunto="";
	
	
	String fechaAlta ="";
	String idFacturacion ="";
	String idGrupoFacturacion ="";
	String accionAplicacion = "consultaAplicacion";

	ArrayList comboFacturacion = new ArrayList();
	
	ArrayList comboGrupoFacturacion = new ArrayList();
	
	
	//variables para controlar el modo
	String accion ="", modo="", readonly="false", clase="box", claseNum="boxNumber";

	//parametro para el combo cmbPagoEjVal
	String[] dato = {usr.getLocation(),usr.getLocation()};
	ArrayList vPago = new ArrayList();

	//recoger parametros
	try
	{
		modo = (String) request.getSession().getAttribute("modo"); 
		request.getSession().removeAttribute("modo"); 
	}
	catch(Exception e)
	{} 
	
	String origen = (String)request.getSession().getAttribute("ORIGEN");
	if (modo==null || "".equals(modo) || modo.equals("nuevo")){
		if(origen != null && !"".equalsIgnoreCase(origen) && ("ASUNTO".equalsIgnoreCase(origen) 
				|| "ACTUACIONESASISTENCIAS".equalsIgnoreCase(origen) || "ASISTENCIAS".equalsIgnoreCase(origen) || "GUARDIAS".equalsIgnoreCase(origen))){
			
			nif =(String)request.getSession().getAttribute("NIF");
			ncolegiado = (String)request.getSession().getAttribute("NCOLEGIADO");
			nombre = (String)request.getSession().getAttribute("NOMBRE");
			idPersona = (String)request.getSession().getAttribute("IDPERSONACOLEGIADO");
			
			cantidad = (String)request.getSession().getAttribute("CANTIDAD");
			idFacturacion=(String)request.getSession().getAttribute("IDFACTURACION");
			comboFacturacion.add(idFacturacion);
			idGrupoFacturacion=(String)request.getSession().getAttribute("IDGRUPOFACTURACION");
			comboGrupoFacturacion.add(idGrupoFacturacion);
			
			Hashtable resultado = (Hashtable)request.getSession().getAttribute("hashActuacion");
			if(!"GUARDIAS".equalsIgnoreCase(origen)){
				idTurno =  (String)resultado.get("IDTURNO"); 
				idInstitucion = (String)resultado.get("IDINSTITUCION");
				anio =  (String)resultado.get("ANIO");
				numero = (String)resultado.get("NUMERO");
				nactuacion = (String)resultado.get("NUMEROASUNTO");		
			}else{
				idTurno =  (String)resultado.get("IDTURNO"); 
				idInstitucion = (String)resultado.get("IDINSTITUCION");
				idGuardia = (String)resultado.get("IDGUARDIA");
				fechaInicio = (String)resultado.get("FECHAINICIO");
			}
			
			//asunto = (String)request.getSession().getAttribute("ASUNTO");
			descripcion = (String)request.getSession().getAttribute("DESCRIPCION");
			
			request.getSession().removeAttribute("NOMBRE");
			request.getSession().removeAttribute("IDPERSONACOLEGIADO");
			request.getSession().removeAttribute("NCOLEGIADO");
			request.getSession().removeAttribute("NIF");
			request.getSession().removeAttribute("hashActuacion");
			request.getSession().removeAttribute("CANTIDAD");
			request.getSession().removeAttribute("IDFACTURACION");
			request.getSession().removeAttribute("IDGRUPOFACTURACION");
			request.getSession().removeAttribute("ASUNTO");
			request.getSession().removeAttribute("DESCRIPCION");
			
		}
			accion = "insertar";
		
	}else{
		//Si el modo no es nuevo, es editar o consultar para lo cual hace
		//falta recuperar los datos del movimiento 
		try
		{
			//recogemos el resultado de la request. Puede que sea nulo.
			Hashtable resultado = (Hashtable)request.getSession().getAttribute("resultado");
			
			nif = (String)resultado.get("NIF");
			ncolegiado = (String)resultado.get("NUMERO");
			nombre = (String)resultado.get("NOMBRE");
			idPersona = (String)resultado.get("IDPERSONA");
			
			asunto = (String)resultado.get("ASUNTO");
			descripcion = (String)resultado.get("DESCRIPCION");
			
			motivo = (String)resultado.get("MOTIVO");
			idMovimiento = (String)resultado.get("IDMOVIMIENTO");
			pago = (String)resultado.get("PAGO");
			if (pago != null && ! pago.equalsIgnoreCase("")) {
				modo = "consulta";
			}
			idPago = (String)resultado.get("IDPAGO");
			fechaAlta = (String)resultado.get("FECHAALTA");
			
			cantidad = (String)resultado.get("CANTIDAD");
			idFacturacion = (String)resultado.get("IDFACTURACION");
			comboFacturacion.add(idFacturacion);
			idGrupoFacturacion = (String)resultado.get("IDGRUPOFACTURACION");
			comboGrupoFacturacion.add(idGrupoFacturacion);
			
			request.getSession().removeAttribute("resultado");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		} 		

		if (modo.equals("consulta"))
		{ 
			readonly = "true";
			clase = "boxConsulta";
			claseNum = "boxConsultaNumber";
			accion = "";
		}
		else{
			accion = "modificar";
			//para el combo
			try
			{
				vPago.add(idPago);
			} catch(Exception e){
				vPago.add("0");
			}

		}
	}

%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<html:javascript formName="MantenimientoMovimientosForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
  	
	<!--TITULO Y LOCALIZACION -->

	<siga:Titulo 
		titulo="factSJCS.datosMovimientos.cabecera"
		localizacion="factSJCS.datosMovimientos.ruta"/>
		
		
	<script type="text/javascript">	
		
	
		jQuery.noConflict();
		
		function buscarCliente () 
		{
			var resultado = ventaModalGeneral("busquedaClientesModalForm","G");
			var colegiado = document.getElementById('ncolegiado');
			var idPersona = document.getElementById('idPersona');
			var nif = document.getElementById('nif');			
			var nombre = document.getElementById('nombre');			
			
			//Comprobamos que recibimos datos:
			if (resultado) {
				//Vemos si existe el numero de colegiado:
				if (resultado[2])
						colegiado.value=resultado[2];						
				//Vemos si existe el idpersona:
				if (resultado[0]) {
						idPersona.value=resultado[0];
						nif.value=resultado[3];
						nombre.value = resultado[4] + " " + resultado[5] +  " " + resultado[6];
				}
			}
		}
		
		function buscar()
		{
			document.forms[0].target="resultado";
			document.forms[0].modo="buscarPor";
			document.forms[0].submit();
		}
	</script>
</head>

<body>

	<table class="tablaTitulo" cellspacing="0">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="factSJCS.datosMovimientos.cabecera"/>
		</td>
	</tr>
	</table>



<!--formulario para rellenar el nColegiado desde el action de censo-->

	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="" style="display:none">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="obtenerColegiados" value="true">
	</html:form>




	<!-- INICIO: CAMPOS -->

	<html:form action="/CEN_MantenimientoMovimientos.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "insertar"/>
	<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
	<html:hidden property = "idMovimiento" value = "<%=idMovimiento%>"/>
	<html:hidden property = "fechaAlta" value = "<%=fechaAlta%>"/>
	<html:hidden property = "actionModal" value = ""/>
	
	<html:hidden property = "idTurno" value= "<%=idTurno%>"/>
	<html:hidden property = "idInstitucion" value="<%=idInstitucion%>"/>
	<html:hidden property = "anio" value="<%=anio%>" />	
	<html:hidden property = "numero" value="<%=numero%>" />
	<html:hidden property = "nactuacion" value="<%=nactuacion%>" />
	<html:hidden property = "idGuardia" value="<%=idGuardia%>" />
	<html:hidden property = "fechaInicio" value="<%=fechaInicio%>" />
	
		
	<siga:ConjCampos leyenda="factSJCS.datosMovimientos.leyendaCliente">
	<table>
		<tr>
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosMovimientos.literal.nifCif"/>&nbsp;(*)
			</td>
			<td class="labelText">
				<html:text name="MantenimientoMovimientosForm" property="nif" maxlength="20" styleClass="boxConsulta" readonly="true" value="<%=nif%>"/>
			</td>
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosMovimientos.literal.nColegiado"/>&nbsp;(*)
			</td>
			<td class="labelText">
				<html:text name="MantenimientoMovimientosForm" property="ncolegiado" size="15" maxlength="20" styleClass="boxConsulta" readonly="true" value="<%=ncolegiado%>"/>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosMovimientos.literal.nombre"/>&nbsp;(*)
			</td>
			<td class="labelText" colspan="2">
				<html:text name="MantenimientoMovimientosForm" property="nombre" size= "50" maxlength="100" styleClass="boxConsulta" readonly="true" value="<%=nombre%>"/>
			</td>
			<td class="labelText" id="tdBuscarColegiado">
			<%if (modo!=null && !modo.equals("consulta") &&(origen == null || "".equalsIgnoreCase(origen))){%>
				<input type="button" id="idButton" class="button" name="buscarColegiado" value='<siga:Idioma key="general.boton.search"/>' onClick="buscarCliente();">&nbsp;
			<%}%>
			</td>
		</tr>
	</table>
	</siga:ConjCampos>
				
	<siga:ConjCampos leyenda="Vinculacion">
	<table>
		<tr>
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosPagos.literal.facturacion"/>
			</td>				
			<td>
				<%if ((modo!=null)&&(modo.equals("consulta"))){%>
				<siga:ComboBD nombre="idFacturacion" ancho="600" tipo="cmb_FactInformes" parametro="<%=comboParams%>" clase="boxCombo" obligatorio="false" elementoSel="<%=comboFacturacion%>" readonly="true"/>
				<%} else {%>
				<siga:ComboBD nombre="idFacturacion" ancho="600" tipo="cmb_FactInformes" parametro="<%=comboParams%>" clase="boxCombo" obligatorio="false" elementoSel="<%=comboFacturacion%>"/>
				<%}%>
			</td>
		</tr>
		<tr>
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosFacturacion.literal.gruposFacturacion"/>
			</td>
			<td>
				<%if ((modo!=null)&&(modo.equals("consulta"))){%>
				<siga:ComboBD nombre = "idGrupoFacturacion" tipo="grupoFacturacion" clase="boxCombo" obligatorio="false" parametro="<%=comboParams%>" elementoSel="<%=comboGrupoFacturacion%>" readonly="true"/>
				<%} else {%>
				<siga:ComboBD nombre = "idGrupoFacturacion" tipo="grupoFacturacion" clase="boxCombo" obligatorio="false" parametro="<%=comboParams%>" elementoSel="<%=comboGrupoFacturacion%>"/>
				<%}%>
			</td>
		</tr>
		<%if(asunto != null && !"".equalsIgnoreCase(asunto)){ %>
		<tr>
			<td class="labelText">
				Asociado a
			</td>
			<td>
				<html:text name="MantenimientoMovimientosForm" property="asunto" size= "100" maxlength="100" styleClass="boxConsulta" readonly="true" value="<%=asunto%>"/>
			</td>
		</tr>
	   <%} %>
	</table>
	</siga:ConjCampos>
			
	<siga:ConjCampos leyenda="factSJCS.datosMovimientos.leyenda">
	<table>
		<tr>
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosMovimientos.literal.descripcion"/>&nbsp;(*)
			</td>
			<td class="labelText">
				<%if ((modo!=null)&&(modo.equals("consulta"))){%>
				<html:text name="MantenimientoMovimientosForm" property="descripcion" size= "58" maxlength="100" styleClass="<%=clase%>" readonly="true" value="<%=descripcion%>"/>
				<%} else {%>
				<html:text name="MantenimientoMovimientosForm" property="descripcion" size= "58" maxlength="100" styleClass="<%=clase%>" readonly="false" value="<%=descripcion%>"/>
				<%}%>
			</td>				
			
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosMovimientos.literal.cantidad"/>&nbsp;(*)
			</td>
			<td class="labelText">
				<%if ((modo!=null)&&(modo.equals("consulta"))){%>
				<html:text name="MantenimientoMovimientosForm" property="cantidad" size= "11" maxlength="11" styleClass="boxConsulta" readonly="true"  value="<%=UtilidadesNumero.formatoCampo(cantidad)%>"/>
				<%} else if(origen != null && !"".equalsIgnoreCase(origen)){ %>
				<html:text name="MantenimientoMovimientosForm" property="cantidad" size= "11" maxlength="11" styleClass="boxConsulta" readonly="true"  value="<%=UtilidadesNumero.formatoCampo(cantidad)%>"/>
				<%}else{%>
				<html:text name="MantenimientoMovimientosForm" property="cantidad" size= "11" maxlength="11" styleClass="<%=claseNum%>" readonly="false"  value="<%=UtilidadesNumero.formatoCampo(cantidad)%>"/>
				<%}%>
				&nbsp;&euro;
			</td>
		</tr>
	
		<tr>
			<td class="labelText">
				<siga:Idioma key="factSJCS.datosMovimientos.literal.motivo"/>
			</td>
			<td class="labelText" colspan="3">
				<%if ((modo!=null)&&(modo.equals("consulta"))){%>
				<html:textarea name="MantenimientoMovimientosForm" property="motivo" cols="60" rows="4" onkeydown="cuenta(this,255)" onchange="cuenta(this,255)" styleClass="<%=clase%>" readonly="true" value="<%=motivo%>"/>
				<%} else {%>
				<html:textarea name="MantenimientoMovimientosForm" property="motivo" cols="60" rows="4" onkeydown="cuenta(this,255)" onchange="cuenta(this,255)" styleClass="<%=clase%>" readonly="false" value="<%=motivo%>"/>
				<%}%>	
			</td>				
		</tr>	
	</table>
	</siga:ConjCampos>
	
	<%if (modo!=null && !modo.equals("nuevo") && pago!=null && !pago.equals("")){%>
	<siga:ConjCampos leyenda="Pago donde ha sido aplicado">
	<table>
		<tr>
			<td class="labelText" colspan="3">
				<html:text name="MantenimientoMovimientosForm" property="idPagoJg" size= "50" maxlength="100" styleClass="boxConsulta" readonly="true" value="<%=pago%>"/>
			</td>
		</tr>
	</table>
	</siga:ConjCampos>	
	<%}%>
		
	</html:form>

<%
	String bot = "C";
	if (!modo.equalsIgnoreCase("consulta"))	{
		bot += ",Y,R";
	}	
%>

	<siga:ConjBotonesAccion botones="<%=bot%>" modal="M"  />
	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">



		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{
			sub();	
			if (document.forms[1].nombre.value.length<2){
				alert('<siga:Idioma key="messages.factSJCS.error.introducirCliente"/>');
				fin();
				return false;
			}else{
				// Control uso de coma
				document.MantenimientoMovimientosForm.cantidad.value=document.MantenimientoMovimientosForm.cantidad.value.replace(/,/,".");
				if (validateMantenimientoMovimientosForm(document.MantenimientoMovimientosForm)){
					document.forms[1].modo.value="<%=accion%>";
					document.forms[1].submit();
				}else{
				
					fin();
					return false;
				}
			}
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[1].reset();
		}
		

		

	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
