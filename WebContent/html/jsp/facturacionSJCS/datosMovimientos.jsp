<!-- datosMovimientos.jsp -->
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
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	//campos a mostrar
	String nif = "", ncolegiado="", nombre ="", descripcion = "", cantidad="", motivo="", idMovimiento="", idPersona="", pago="", idPago="";

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
	
	if (modo==null || "".equals(modo) || modo.equals("nuevo") ){
		accion = "insertar";
	}else{
		//Si el modo no es nuevo, es editar o consultar para lo cual hace
		//falta recuperar los datos del movimiento 
		try
		{
			//recogemos el resultado de la request. Puede que sea nulo.
			Hashtable resultado = (Hashtable)request.getSession().getAttribute("resultado");
			nif = (String)resultado.get("NIF");
			//ncolegiado = (String)resultado.get("NCOLEGIADO");
			ncolegiado = (String)resultado.get("NUMERO");
			nombre = (String)resultado.get("NOMBRE");
			descripcion = (String)resultado.get("DESCRIPCION");
			cantidad = (String)resultado.get("CANTIDAD");
			motivo = (String)resultado.get("MOTIVO");
			idPersona = (String)resultado.get("IDPERSONA");
			idMovimiento = (String)resultado.get("IDMOVIMIENTO");
			pago = (String)resultado.get("PAGO");
			idPago = (String)resultado.get("IDPAGO");
			request.getSession().removeAttribute("resultado");
		}
		catch(Exception e)
		{} 		

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
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<html:javascript formName="MantenimientoMovimientosForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
 	
	<!--TITULO Y LOCALIZACION -->

	<siga:Titulo 
		titulo="factSJCS.datosMovimientos.cabecera"
		localizacion="factSJCS.datosMovimientos.ruta"/>
		
		
	<script language="JavaScript">	
	
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

	<table class="tablaTitulo" cellspacing="0" heigth="32">
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
	</html:form>




	<!-- INICIO: CAMPOS -->
	
	<table  class="tablaCentralCamposMedia"  align="center">

	<html:form action="/CEN_MantenimientoMovimientos.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = "insertar"/>
	<html:hidden property = "idPersona" value = "<%=idPersona%>"/>
	<html:hidden property = "idMovimiento" value = "<%=idMovimiento%>"/>
	<html:hidden property = "actionModal" value = ""/>
	
	<tr>
	<td>
		<siga:ConjCampos leyenda="factSJCS.datosMovimientos.leyenda">
		<table>
			<tr>
			<td colspan="4">
				<siga:ConjCampos leyenda="factSJCS.datosMovimientos.leyendaCliente">
				<table width="100%">
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
						<td class="labelText">
						<%if ((modo!=null)&&(!modo.equals("consulta"))){%>
							<input type="button" id="idButton" class="button" name="buscarColegiado" value='<siga:Idioma key="general.boton.search"/>' onClick="buscarCliente();">&nbsp;
						<%}%>
						</td>
					</tr>
				</table>
				</siga:ConjCampos>
			</td>
			</tr>
			<tr>
				<td colspan="4">
					&nbsp;
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="factSJCS.datosMovimientos.literal.descripcion"/>&nbsp;(*)
				</td>
				<td class="labelText" width="80">
					<html:text name="MantenimientoMovimientosForm" property="descripcion" size= "30" maxlength="100" styleClass="<%=clase%>" readonly="false" value="<%=descripcion%>"/>
				</td>
				<td class="labelText"  width="80">
					<siga:Idioma key="factSJCS.datosMovimientos.literal.cantidad"/>&nbsp;(*)
				</td>
				<td class="labelText">
					<html:text name="MantenimientoMovimientosForm" property="cantidad" size= "11" maxlength="11" styleClass="<%=claseNum%>" readonly="false"  value="<%=UtilidadesNumero.formatoCampo(cantidad)%>"/>&nbsp;&euro;
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="factSJCS.datosMovimientos.literal.pago"/>
				</td>
				<td class="labelText" colspan="3">
					<html:text name="MantenimientoMovimientosForm" property="idPagoJg" size= "50" maxlength="100" styleClass="boxConsulta" readonly="true" value="<%=pago%>"/>
				</td>
				
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key="factSJCS.datosMovimientos.literal.motivo"/>
				</td>
				<td class="labelText" colspan="3">
					<html:textarea name="MantenimientoMovimientosForm" property="motivo" cols="60" rows="4" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" styleClass="<%=clase%>" value="<%=motivo%>"/>
				</td>
			</tr>
		
		</table>
		</siga:ConjCampos>
	</td>
	</tr>
	
	</html:form>

	</table>
	<!-- INICIO: BOTONES REGISTRO -->
	
		<siga:ConjBotonesAccion botones="Y,R,C" modal="M" modo="<%=modo%>"/>

	<!-- FIN: BOTONES REGISTRO -->

	
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
