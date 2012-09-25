<!-- datosProcedimientos.jsp -->
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
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.atos.utils.GstDate"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);

	//recogemos los datos
	Hashtable resultado = (Hashtable) request.getAttribute("resultado");
	Vector v = (Vector) request.getAttribute("acreditaciones");

	//variables quese van a mostrar en la jsp
	String nombre = "", importe = "", idProc = "", idJurisdiccion = "", codigo = "", complemento = "", vigente = "",permitirAniadirLetrado="";
	String fechaInicio="", fechaFin="";

	//inicializamos los valores
	try {
		nombre = (String) resultado.get(ScsProcedimientosBean.C_NOMBRE);
		importe = (String) resultado.get(ScsProcedimientosBean.C_PRECIO);
		idProc = (String) resultado.get(ScsProcedimientosBean.C_IDPROCEDIMIENTO);
		idJurisdiccion = (String) resultado.get(ScsProcedimientosBean.C_IDJURISDICCION);
		codigo = (String) resultado.get(ScsProcedimientosBean.C_CODIGO);
		complemento = (String) resultado.get(ScsProcedimientosBean.C_COMPLEMENTO);
		vigente = (String) resultado.get(ScsProcedimientosBean.C_VIGENTE);
		permitirAniadirLetrado = (String) resultado.get(ScsProcedimientosBean.C_PERMITIRANIADIRLETRADO);
		fechaInicio = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),(String)resultado.get(ScsProcedimientosBean.C_FECHADESDEVIGOR)));
		fechaFin = (String)resultado.get(ScsProcedimientosBean.C_FECHAHASTAVIGOR);
		if(!fechaFin.equals("")){
			fechaFin = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),(String)resultado.get(ScsProcedimientosBean.C_FECHAHASTAVIGOR)));
		}
		
		
	} catch (Exception e) {
	}

	//recuperamos el modo de acceso
	String modo = "Modificar";
	if ((nombre == null) || (nombre.equals("")))
		modo = "Insertar";
	int posBotonNuevo = 298;
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<html:javascript formName="MantenimientoProcedimientosForm" staticJavascript="false" />
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
 	
	
</head>

<body onLoad="validarAncho_tablaResultados()">

	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="gratuita.procedimientos.mantenimiento.cabecera"/>
		</td>
	</tr>
	</table>



	<!-- INICIO: CAMPOS -->

	<table  class="tablaCentralCamposMedia" cellspacing=0 cellpadding=0 align="center" border="0">

	<html:javascript formName="MantenimientoProcedimientosForm" staticJavascript="false" />
	<html:form action="/JGR_MantenimientoProcedimientos.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "idProcedimiento" value = "<%=idProc%>"/>
	
	<html:hidden property = "idAcreditacion" value = ""/>
	<html:hidden property = "porcentaje" value = ""/>
	
	<html:hidden property = "refresco" value = ""/>

	<tr>				
	<td>

	<siga:ConjCampos leyenda="gratuita.procedimientos.leyenda">

		<table class="tablaCampos" align="center" border="0" width="100%">
	
		<!-- FILA -->
					<tr>

						<td class="labelText"><siga:Idioma
							key="gratuita.procedimientos.literal.nombre" />&nbsp;(*)</td>
						<td class="labelText" colspan="3"><html:text
							name="MantenimientoProcedimientosForm" property="nombre"
							size="80" maxlength="100" styleClass="box" readonly="false"
							value="<%=nombre%>" /></td>
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma
							key="gratuita.procedimientos.literal.codigo" /></td>
						<td class="labelText"><html:text
							name="MantenimientoProcedimientosForm" property="codigo"
							maxlength="20" styleClass="box" readonly="false"
							value="<%=codigo%>" /></td>

						<td class="labelText"><siga:Idioma
							key="gratuita.procedimientos.literal.complemento" /></td>
						<td class="labelText"><input type="checkbox"
							name="complemento" value="<%=ClsConstants.DB_TRUE%>"
							<%if(complemento.equals(ClsConstants.DB_TRUE)){%> checked <%}%> />
						</td>

					</tr>
					<tr>
						<td class="labelText"><siga:Idioma
							key="gratuita.procedimientos.literal.importe" />&nbsp;(*)</td>
						<td class="labelText"><html:text
							name="MantenimientoProcedimientosForm" property="importe"
							maxlength="11" styleClass="boxNumber" readonly="false"
							value="<%=UtilidadesNumero.formatoCampo(importe)%>" />&nbsp;&euro;
						</td>
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma
							key="gratuita.procedimientos.literal.fechainicio" />&nbsp;(*)</td>
						<td class="labelText">
							<siga:Fecha nombreCampo="fechaDesdeVigor" valorInicial="<%=fechaInicio%>" posicionX="100" posicionY="100"></siga:Fecha>
							
						</td>
						
						<td class="labelText"><siga:Idioma
							key="gratuita.procedimientos.literal.fechafin" />&nbsp;
							<siga:Fecha nombreCampo="fechaHastaVigor" valorInicial="<%=fechaFin%>" posicionX="100" posicionY="100"></siga:Fecha>
						</td>

					</tr>
					<tr>
						<td class="labelText"><siga:Idioma
							key="gratuita.procedimientos.literal.Jurisdiccion" />&nbsp;(*)</td>
						<td class="labelText">
						<%
							ArrayList juris = new ArrayList();
									juris.add(idJurisdiccion);
						%> <siga:ComboBD nombre="jurisdiccion" ancho="200"
							tipo="jurisdiccionSCJS" clase="boxCombo" obligatorio="true"
							elementoSel="<%=juris%>"/></td>
							
							<td class="labelText"><siga:Idioma
							key="gratuita.procedimientos.literal.permitirAniadirLetrado" />&nbsp;</td>
						<td class="labelText"><input type="checkbox" name="permitirAniadirLetrado"
							value="<%=ClsConstants.DB_TRUE%>"
							<%if(permitirAniadirLetrado.equals(ClsConstants.DB_TRUE)){%> checked <%}%> /></td>
					</tr>


				</table>

	</siga:ConjCampos>

	</td> 
	</tr>

		</html:form>	
	</table>
	
<siga:ConjBotonesAccion botones="G,C" clase="botonesSeguido" modal="M" titulo="gratuita.procedimientos.literal.acreditaciones"/>

	<siga:TablaCabecerasFijas 
			   nombre="tablaResultados"
			   borde="1"
			   clase="tableTitle"				   
			   nombreCol="gratuita.procedimientos.literal.acreditacion,gratuita.procedimientos.literal.porcentaje,"
			   tamanoCol="42,42,16"
			   alto="67"
			   modal="P"
			   ajusteBotonera="true"
			   >

<%
	if ((v != null) && (v.size() > 0)) {
			for (int i = 0; i < v.size(); i++) {
				Hashtable hash = (Hashtable) v.get(i);
				if (hash != null) {
					String acreDescripcion = UtilidadesHash.getString(
							hash, ScsAcreditacionBean.C_DESCRIPCION);
					Integer acrePorcentaje = UtilidadesHash
							.getInteger(
									hash,
									ScsAcreditacionProcedimientoBean.C_PORCENTAJE);
					Integer idAcreditacion = UtilidadesHash
							.getInteger(
									hash,
									ScsAcreditacionProcedimientoBean.C_IDACREDITACION);
					Integer idInstitucion = UtilidadesHash
							.getInteger(
									hash,
									ScsAcreditacionProcedimientoBean.C_IDINSTITUCION);
					String idProcedimiento = UtilidadesHash
							.getString(
									hash,
									ScsAcreditacionProcedimientoBean.C_IDPROCEDIMIENTO);
%>
					<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' visibleConsulta="no" botones='E,B'  modo='<%=modo%>' clase="listaNonEdit">
						<td>
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=idAcreditacion.intValue()%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_2" value="<%=idInstitucion.intValue()%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_3" value="<%=idProcedimiento%>">
							<input type="hidden" name="oculto<%=String.valueOf(i+1)%>_4" value="detalleAcreditacion">
							<%=UtilidadesString
														.mostrarDatoJSP(acreDescripcion)%></td>
						<td><%=acrePorcentaje.intValue()%></td>
					</siga:FilaConIconos>
<%
	}
			}
		} else { // No hay registros 
			posBotonNuevo = 327;
%>
		<table align="center">
			<tr>
				<td>
					<br>
					<font class="labelText" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></font>
				</td>
			</tr>
			</table>
<%
	}
%>

	</siga:TablaCabecerasFijas>
	 
	 <%
	 	 	if (!modo.equalsIgnoreCase("Insertar")) {
	 	 %>
	 	<siga:ConjBotonesAccion botones="N" modal="M" />
	 <%
	 	}
	 %>
	




	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardar() 
		{
			sub();		
			if (validateMantenimientoProcedimientosForm(document.MantenimientoProcedimientosForm)){
				if(validarFecha()){
					document.forms[0].importe.value=document.forms[0].importe.value.replace(/,/,".");
					document.forms[0].modo.value="<%=modo%>";
					document.forms[0].submit();
					window.top.returnValue="MODIFICADO";
				}else{					
					fin();
					return false;
				}
				
			}else{
			
				fin();
				return false;
			
			}
		}

		function validarFecha() {

			var fechaIni = trim(document.getElementById("fechaDesdeVigor").value);
			var fechaFin = trim(document.getElementById("fechaHastaVigor").value);

			if (trim(document.getElementById("fechaDesdeVigor").value)=="") {
				alert ('<siga:Idioma key="messages.campos.required"/> <siga:Idioma key="gratuita.procedimientos.literal.fechainicio"/>');
			   	return false;
			}
			
			if (compararFecha (fechaIni, fechaFin) == 1) {
				mensaje='<siga:Idioma key="messages.fechas.rangoFechas"/>';
				alert(mensaje);
				return false;
			}		
			return true;	
		}		
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			// esta funcion cierra la ventana y devuelve 
			// un valor a la ventana padre (USAR SIEMPRE)
//			top.cierraConParametros("NORMAL");
//			top.refrescarLocal();
			window.top.returnValue="MODIFICADO";
			window.top.close();
		}

/*
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}
*/		

		<!-- Asociada al boton Nuevo -->
		function accionNuevo() 
		{		
			document.forms[0].modo.value = "nuevoAcreditacion";
			var resultado = ventaModalGeneral(document.forms[0].name,"P");
			if (resultado != null && (resultado[0]== 1)) {
				document.forms[0].idAcreditacion.value = resultado[1];
				document.forms[0].porcentaje.value = resultado[2];
				document.forms[0].modo.value = "insertarAcreditacion";
				document.forms[0].submit();
			}
		}	
		
		function refrescarLocal()
		{
			document.forms[0].target="modal";
			document.forms[0].refresco.value="refresco";
			document.forms[0].modo.value="editar";
			document.forms[0].submit();
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
