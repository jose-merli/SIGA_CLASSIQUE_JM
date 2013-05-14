<!-- abrirDatosColegiacion.jsp -->
<!-- 
	 Muestra los datos de colegiación generales de un cliente
	 VERSIONES:
	 RGG 15/03/2007 
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<!--pilar-->
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.atos.utils.GstDate"%>

<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>


<%@ page import="com.siga.administracion.SIGAMasterTable"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
	String modo = (String) request.getAttribute("ACCION");
%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="DatosColegiacionForm" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="censo.fichaCliente.situacion.cabecera"
		localizacion="censo.fichaLetrado.localizacion" />
	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<script type="text/javascript">
		function consultdatoscoleg (fila) {
			var aux = 'oculto' + fila + '_4';
			var oculto = document.getElementById(aux);
			document.forms[0].nColegiado.value=oculto.value;
			aux='oculto' + fila + '_2';
			oculto=document.getElementById(aux);
			document.forms[0].idInstitucion.value=oculto.value;
			aux='oculto' + fila + '_3';
			oculto=document.getElementById(aux);
			document.forms[0].nifcif.value=oculto.value;
			document.forms[0].action="<%=app%>/CEN_BusquedaClientes.do?colegiado=1&buscar=1";
			document.forms[0].target = "mainWorkArea";
			document.forms[0].modo.value = "";
			document.forms[0].submit();
		}
	
		function insertarestado(fila) {
			// Controlamos que la fecha de estado no sea nula
			var aux = 'oculto' + fila + '_6';
			var oculto = document.getElementById(aux);
			if (oculto.value == "") {//Este caso no se deberia dar (no tener fecha de estado) pero se controla porque si no daria error al insertar un nuevo estado
				alert("Error en la configuracion de los datos del colegiado.");
				return;
			}
			//
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = "";
			var i, j;
			for (i = 0; i < 3; i++) {
				var tabla;
				tabla = document.getElementById('tablaResultados');
				if (i == 0) {
					var flag = true;
					j = 1;
					while (flag) {
						var aux = 'oculto' + fila + '_' + j;
						var oculto = document.getElementById(aux);
						if (oculto == null) {
							flag = false;
						} else {
							datos.value = datos.value + oculto.value + ',';
						}
						j++;
					}
					datos.value = datos.value + "%"
				} else {
					j = 2;
				}
				if ((tabla.rows[fila].cells)[i].innerHTML == "")
					datos.value = datos.value
							+ (tabla.rows[fila].cells)[i].all[j - 2].value + ',';
				else
					datos.value = datos.value
							+ (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi, '') + ',';
			}
	
			document.forms[0].modo.value = "nuevo";
	
			var resultado = ventaModalGeneral(document.forms[0].name, "P");
	
			if (resultado) {
				if (resultado == "MODIFICADO") {
					refrescarLocal();
				}
			}
		}
	
		function modificarestado(fila) {
			// Controlamos que la fecha de estado no sea nula
			var aux = 'oculto' + fila + '_6';
			var oculto = document.getElementById(aux);
			if (oculto.value == "") {//Este caso no se deberia dar (no tener fecha de estado) pero se controla porque si no daria error al insertar un nuevo estado
				alert("Error en la configuracion de los datos del colegiado.");
				return;
			}
			//
			var datos;
			datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = "";
			var i, j;
			for (i = 0; i < 3; i++) {
				var tabla;
				tabla = document.getElementById('tablaResultados');
				if (i == 0) {
					var flag = true;
					j = 1;
					while (flag) {
						var aux = 'oculto' + fila + '_' + j;
						var oculto = document.getElementById(aux);
						if (oculto == null) {
							flag = false;
						} else {
							datos.value = datos.value + oculto.value + ',';
						}
						j++;
					}
					datos.value = datos.value + "%"
				} else {
					j = 2;
				}
				if ((tabla.rows[fila].cells)[i].innerHTML == "")
					datos.value = datos.value
							+ (tabla.rows[fila].cells)[i].all[j - 2].value + ',';
				else
					datos.value = datos.value
							+ (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi, '') + ',';
			}
	
			document.forms[0].modo.value = "Editar";
	
			var resultado = ventaModalGeneral(document.forms[0].name, "G");
			//Cuando volvemos de la ventana modal que muestra los datos de colegiacion simpre refrescamos la pagina padre por si se hubiera cerrado la ventana dando al aspa
			refrescarLocal();
		}
	
		function refrescarLocal() {
			parent.situacionLetrado();
		}
	
		function borrarestado(fila) {
			var datos;
			var type = '<siga:Idioma key="messages.deleteConfirmation"/>';
			if (confirm(type)) {
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = "";
				var i, j;
				for (i = 0; i < 3; i++) {
					var tabla;
					tabla = document.getElementById('tablaResultados');
					if (i == 0) {
						var flag = true;
						j = 1;
						while (flag) {
							var aux = 'oculto' + fila + '_' + j;
							var oculto = document.getElementById(aux);
							if (oculto == null) {
								flag = false;
							} else {
								datos.value = datos.value + oculto.value + ',';
							}
							j++;
						}
						datos.value = datos.value + "%"
					} else {
						j = 2;
					}
					if ((tabla.rows[fila].cells)[i].innerHTML == "")
						datos.value = datos.value
								+ (tabla.rows[fila].cells)[i].all[j - 2].value
								+ ',';
					else
						datos.value = datos.value
								+ (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi, '') + ',';
				}
				var auxTarget = document.forms[0].target;
				document.forms[0].target = "submitArea";
				document.forms[0].modo.value = "Borrar";
				document.forms[0].submit();
				document.forms[0].target = auxTarget;
			}
		}
	</script>
</head>

<body class="tablaCentralCampos">

	<!--pilar-->
	<!-- INICIO: LISTA RESULTADOS -->
	<html:form action="/CEN_DatosColegiacion.do" method="POST" 
		style="display:none">
		<html:hidden property="modo" value="" />
		<input type="hidden" id="nColegiado"  name="nColegiado" value="" />
		<input type="hidden" id="idInstitucion"  name="idInstitucion" value="" />
		<input type="hidden" id="nifcif" name="nifcif" value="" />
		<input type="hidden" id="letrado"  name="letrado" value="" />
		<input type="hidden" id="actionModal" name="actionModal" value="">
	</html:form>

	<siga:Table name="tablaResultados" border="1"
		columnNames="censo.consultaDatosColegiacion.literal.colegio,
							censo.consultaDatosColegiacion.literal.numIden,
							censo.consultaDatosColegiacion.literal.numColegiado,
							censo.consultaDatosColegiacion.literal.nomyApellidos,
							censo.consultaDatosColegiacion.literal.fechaIngreso,
							censo.consultaDatosColegiacion.literal.estadoCol,
							censo.solicitudIncorporacion.literal.fechaEstado,
							censo.solicitudIncorporacion.literal.fechaAct,
							censo.consultaDatosColegiacion.literal.residente,
							censo.consultaDatosColegiacion.literal.fechaNac,"
		columnSizes="10,8,7,10,8,8,8,8,8,8,12">

		<%
			if (request.getAttribute("DATESTADO") == null
						|| ((Vector) request.getAttribute("DATESTADO")).size() < 1) {
		%>
		<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
		<%
			} else {
		%>
		<%
			Enumeration en = ((Vector) request
							.getAttribute("DATESTADO")).elements();
					int recordNumber = 1;
					String iconosRegistro = "C";
					FilaExtElement[] elementos = new FilaExtElement[4];

					elementos[0] = new FilaExtElement("consultdatoscoleg",
							"consultdatoscoleg", SIGAConstants.ACCESS_READ);
					while (en.hasMoreElements()) {
						Row row = (Row) en.nextElement();
						String institucion = CenVisibilidad
								.getAbreviaturaInstitucion(row
										.getString(CenColegiadoBean.C_IDINSTITUCION));
						if (modo.equals("ver")) {// no se permite hacer nada al usuario, solo consultar su ficha colegial
							elementos[1] = new FilaExtElement("insertarestado",
									"insertarestado",
									SIGAConstants.ACCESS_SIGAENPRODUCCION);
							elementos[2] = new FilaExtElement("borrarestado",
									"borrarestado",
									SIGAConstants.ACCESS_SIGAENPRODUCCION);
							elementos[3] = new FilaExtElement(
									"modificarestado", "modificarestado",
									SIGAConstants.ACCESS_SIGAENPRODUCCION);

						} else {
							if (row.getString("FECHAPRODUCCION") == null
									|| row.getString("FECHAPRODUCCION").equals(
											"")) {
								elementos[1] = new FilaExtElement(
										"insertarestado", "insertarestado",
										SIGAConstants.ACCESS_FULL);
								elementos[3] = new FilaExtElement(
										"modificarestado", "modificarestado",
										SIGAConstants.ACCESS_FULL);
								if (Integer.parseInt((String) row
										.getString("NUMEROESTADO")) <= 1) {
									elementos[2] = new FilaExtElement(
											"borrarestado",
											"borrarestado",
											SIGAConstants.ACCESS_SIGAENPRODUCCION);

								} else {
									elementos[2] = new FilaExtElement(
											"borrarestado", "borrarestado",
											SIGAConstants.ACCESS_FULL);

								}

							} else {
								elementos[1] = new FilaExtElement(
										"insertarestado", "insertarestado",
										SIGAConstants.ACCESS_SIGAENPRODUCCION);

								elementos[2] = new FilaExtElement(
										"borrarestado", "borrarestado",
										SIGAConstants.ACCESS_SIGAENPRODUCCION);
								elementos[3] = new FilaExtElement(
										"modificarestado", "modificarestado",
										SIGAConstants.ACCESS_SIGAENPRODUCCION);

							}
						}
		%>

		<siga:FilaConIconos fila='<%=String.valueOf(recordNumber)%>'
			elementos='<%=elementos%>' modo='<%=modo%>' botones=""
			visibleBorrado="no" visibleConsulta="no" visibleEdicion="no"
			pintarEspacio="no" clase="listaNonEdit">

			<td>
				<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_1" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString(CenPersonaBean.C_IDPERSONA)%>" /> 
				<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_2" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(CenColegiadoBean.C_IDINSTITUCION)%>" />
				<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_3" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=UtilidadesString.comaToAnd(row.getString(CenPersonaBean.C_NIFCIF))%>" />
				<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_4" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=row.getString("NCOLEGIADO")%>" /> 
				<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_5" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=row.getString(CenPersonaBean.C_NOMBRE)+" "+row.getString(CenPersonaBean.C_APELLIDOS1)+" "+row.getString(CenPersonaBean.C_APELLIDOS2)%>" />
				<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_6" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=row.getString("FECHAESTADO")%>" /> 
				<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_7" name="oculto<%=String.valueOf(recordNumber)%>_7" value="<%=row.getString("ESTADOCOLEGIAL")%>"/> 
				<%=institucion%>
			</td>
			<td><%=UtilidadesString.mostrarDatoJSP(row
									.getString(CenPersonaBean.C_NIFCIF))%>
			</td>
			<td><%=UtilidadesString.mostrarDatoJSP(row
									.getString("NCOLEGIADO"))%>
			</td>
			<td><%=UtilidadesString.mostrarDatoJSP(row
									.getString(CenPersonaBean.C_NOMBRE))
									+ " "
									+ UtilidadesString.mostrarDatoJSP(row
											.getString(CenPersonaBean.C_APELLIDOS1))
									+ " "
									+ UtilidadesString.mostrarDatoJSP(row
											.getString(CenPersonaBean.C_APELLIDOS2))%>
			</td>
			<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(
									user.getLanguage(),
									row.getString(CenColegiadoBean.C_FECHAINCORPORACION)))%>
			</td>
			<td><%=UtilidadesString.mostrarDatoJSP(row
									.getString("ESTADOCOLEGIAL"))%>
			</td>
			<td><%=UtilidadesString.mostrarDatoJSP(GstDate
									.getFormatedDateShort(user.getLanguage(),
											row.getString("FECHAESTADO")))%>
			</td>
			<td><%=UtilidadesString.mostrarDatoJSP(GstDate
									.getFormatedDateShort(user.getLanguage(),
											row.getString("FECHAACTUALIZ")))%>
			</td>
			<td><%=UtilidadesString
									.mostrarDatoJSP(
											row.getString(CenColegiadoBean.C_SITUACIONRESIDENTE))
									.equals("0") ? "No" : "Si"%>
			</td>
			<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(
									user.getLanguage(),
									row.getString(CenPersonaBean.C_FECHANACIMIENTO)))%>
			</td>
		</siga:FilaConIconos>
		<%
			recordNumber++;
					}
				}
		%>

	</siga:Table>


	<!--pilar-->
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
		style="display: none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>
