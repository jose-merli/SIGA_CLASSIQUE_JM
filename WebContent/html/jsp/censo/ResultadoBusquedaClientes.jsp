<!-- ResultadoBusquedaClientes.jsp -->
<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
	 raul.ggonzalez 16-12-2004 Modificacion de formularios para validacion por struts de campos
	 miguel.villegas 11-01-2005 Incorpora capacidad de refresco para borrar (target->submitArea, esta comentado)
-->


<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.Utilidades.*"%>

<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitive"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.RowsContainer"%>
<%@ page import="com.siga.beans.CenClienteAdm"%>



<!-- JSP -->
<bean:define id="registrosSeleccionados" name="busquedaClientesForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="busquedaClientesForm" property="datosPaginador" type="java.util.HashMap"/>
<bean:define id="colegiado" name="busquedaClientesForm" property="colegiado" type="java.lang.String"/>

<%
	try {
		String app = request.getContextPath();
		HttpSession ses = request.getSession();
		Properties src = (Properties) ses.getAttribute(SIGAConstants.STYLESHEET_REF);
		UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
		String idioma = usrbean.getLanguage().toUpperCase();
		String idInstitucionLocation = usrbean.getLocation();
		int idInstitucionInt = Integer.parseInt(idInstitucionLocation);
		boolean esColegio = (idInstitucionInt > 2000 && idInstitucionInt < 3000);

		boolean ParametrolopdActivo = ((String) request.getAttribute("ParametrolopdActivo")).equals("1");

		CenClienteAdm admCen = new CenClienteAdm(usrbean);
		String valorCheckPersona = "";
		// locales
		//BusquedaClientesForm formulario = (BusquedaClientesForm) request
		//	.getSession().getAttribute("busquedaClientesForm");

		//String colegiado = formulario.getColegiado();
		if (colegiado == null)
			colegiado = " ";

		Vector resultado = null;

		String titu = "";

		if (colegiado.equals(ClsConstants.DB_TRUE)) {
			//colegiados

			titu = "censo.busquedaClientes.colegiados.titulo";
		} else {
			if (colegiado.equals(ClsConstants.DB_FALSE)) {
				//no colegiados
				titu = "censo.busquedaClientes.noColegiados.titulo";
			} else {
				//Letrados
				titu = "censo.busquedaClientes.letrados.titulo";
			}
		}
		/** PAGINADOR ***/
		String paginaSeleccionada = "";

		String totalRegistros = "";

		String registrosPorPagina = "";

		if (datosPaginador != null) {

			if (datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
				resultado = (Vector) datosPaginador.get("datos");
				if (colegiado.equals(ClsConstants.DB_FALSE) || colegiado.equals(ClsConstants.DB_TRUE)) {
					PaginadorBind paginador = (PaginadorBind) datosPaginador.get("paginador");
					paginaSeleccionada = String.valueOf(paginador.getPaginaActual());

					totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());

					registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());
				} else {
					PaginadorCaseSensitiveBind paginadorCS = (PaginadorCaseSensitiveBind) datosPaginador.get("paginador");
					paginaSeleccionada = String.valueOf(paginadorCS.getPaginaActual());

					totalRegistros = String.valueOf(paginadorCS.getNumeroTotalRegistros());

					registrosPorPagina = String.valueOf(paginadorCS.getNumeroRegistrosPorPagina());
				}

			} else {
				resultado = new Vector();
				paginaSeleccionada = "0";

				totalRegistros = "0";

				registrosPorPagina = "0";
			}
		} else {
			resultado = new Vector();
			paginaSeleccionada = "0";

			totalRegistros = "0";

			registrosPorPagina = "0";
		}

		String action = app + "/CEN_BusquedaClientes.do?noReset=true";
		/**************/
%>

<html>
<!-- HEAD -->
<head>

<link id="default" rel="stylesheet" type="text/css"
	href="<%=app%>/html/jsp/general/stylesheet.jsp">
<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
<!-- Validaciones en Cliente -->
<!-- El nombre del formulario se obtiene del struts-config -->
<html:javascript formName="/CEN_BusquedaClientes.do"
	staticJavascript="false" />
<script src="<%=app%>/html/js/validacionStruts.js"
	type="text/javascript"></script>
<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

<!-- INICIO: TITULO Y LOCALIZACION -->
<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
<siga:Titulo titulo="<%=titu %>"
	localizacion="censo.busquedaClientes.localizacion" />
<!-- FIN: TITULO Y LOCALIZACION -->

<!-- SCRIPTS LOCALES -->
<script language="JavaScript">
	function refrescarLocal() {
		parent.buscar();
	}
	function vueltaEnvio() {
		
	}
	function accionCerrar() {
		
	}
	
	function informacionLetrado(fila)
			{
				document.forms[0].filaSelD.value = fila;
			
				
			    var idInst = <%=idInstitucionLocation%>;			          		
			   				   	
			   	var auxPers = 'oculto' + fila + '_1';
			    var idPers = document.getElementById(auxPers);			    

			   		    
				document.forms[0].tablaDatosDinamicosD.value=idPers.value + ',' + idInst + ',LETRADO' + '%';		
				document.forms[0].modo.value="editar";
				var verLetradoAux = 'oculto' + fila + '_4';
			    var verLetrado = document.getElementById(verLetradoAux);			    
				document.forms[0].verFichaLetrado.value=verLetrado.value;
			   	document.forms[0].submit();			   	
			}
	function lopd(fila) {
		alert('<siga:Idioma key="general.boton.lopd"/>');
		
	}
	</script>

</head>

<body  onload="cargarChecks();checkTodos()" class="tablaCentralCampos">

<!-- INICIO: LISTA DE VALORES -->
<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

<!-- Formulario de la lista de detalle multiregistro -->
<html:form action="/CEN_BusquedaClientes.do?noReset=true" method="POST"
	target="mainWorkArea" style="display:none">

	<!-- Campo obligatorio -->
	<html:hidden property="modo" value="" />
	<html:hidden property="registrosSeleccionados" />
	<html:hidden property="datosPaginador" />
	<html:hidden property="seleccionarTodos" />
	

	<!-- parametro para colegiados o no -->
	<html:hidden property="colegiado" value="<%=colegiado %>" />
	<html:hidden property="avanzada" value="" />

	<input type="hidden" name="filaSelD">
	<input type="hidden" name="tablaDatosDinamicosD">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="verFichaLetrado" value="">
</html:form>
<!-- Formulario para la creacion de envio -->
<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea" style="display:none">
	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "tablaDatosDinamicosD" value = ""/>
	<html:hidden property = "subModo" value = ""/>
	<html:hidden property = "filaSelD" value=""/>
	<html:hidden property = "idPersona" value=""/>
	<html:hidden property = "descEnvio" value=""/>
	<html:hidden property = "datosEnvios" value=""/>
	
	
</html:form>

<%
	String tamanosCol = "";
		String nombresCol = "";
		String alto = "";
		if (colegiado.equals(ClsConstants.DB_TRUE)) {//colegiado
			// cliente colegiado
			if (esColegio) {
				tamanosCol = "3,12,6,13,12,11,10,6,13,15";
				nombresCol += "<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,censo.busquedaClientesAvanzada.literal.nif,censo.busquedaClientesAvanzada.literal.nColegiado,gratuita.turnos.literal.apellidosSolo,censo.busquedaClientesAvanzada.literal.nombre,censo.busquedaClientesAvanzada.literal.fechaIngreso,censo.busquedaClientesAvanzada.literal.estadoColegial,censo.busquedaClientesAvanzada.literal.residente,censo.busquedaClientesAvanzada.literal.tlfn1movil,";
				alto = "200";
			} else {
				tamanosCol = "3,12,6,12,11,11,8,8,6,9,15";
				nombresCol += "<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,censo.busquedaClientesAvanzada.literal.nif,censo.busquedaClientesAvanzada.literal.nColegiado,gratuita.turnos.literal.apellidosSolo,censo.busquedaClientesAvanzada.literal.nombre,censo.busquedaClientesAvanzada.literal.fechaIngreso,censo.busquedaClientes.literal.institucion,censo.busquedaClientesAvanzada.literal.estadoColegial,censo.busquedaClientesAvanzada.literal.residente,censo.busquedaClientesAvanzada.literal.fechaNacimiento,";
				alto = "200";
			}

		} else {
			if (colegiado.equals(ClsConstants.DB_FALSE)) {//no colegiado
				tamanosCol = "3,10,12,16,16,15,15,16";
				nombresCol = "<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,censo.busquedaClientesAvanzada.literal.tipo,censo.busquedaClientesAvanzada.literal.nif,gratuita.turnos.literal.apellidosSolo,censo.busquedaClientesAvanzada.literal.nombre,censo.busquedaClientes.literal.institucion,censo.busquedaClientes.literal.FechaNacimientoConstitucion,";
				alto = "200";
			} else {//letrado
				tamanosCol = "3,8,8,19,14,7,19,10";
				nombresCol = "<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,censo.busquedaClientes.idPersona,censo.busquedaClientesAvanzada.literal.nif,gratuita.turnos.literal.apellidosSolo,censo.busquedaClientesAvanzada.literal.nombre,censo.busquedaClientesAvanzada.literal.fechaNacimiento,Direcci�n,";
				alto = "250";
			}
		}
%>

<siga:TablaCabecerasFijas nombre="tablaDatos"
	nombreCol="<%=nombresCol %>" tamanoCol="<%=tamanosCol %>"
	alto="<%=alto%>" ajustePaginador="true" activarFilaSel="true" ajusteBotonera="true">

	<!-- INICIO: ZONA DE REGISTROS -->
	<!-- Aqui se iteran los diferentes registros de la lista -->

	<%
		if (resultado == null || resultado.size() == 0) {
	%>
	<br>
	<br>
	<p class="titulitos" style="text-align: center"><siga:Idioma
		key="messages.noRecordFound" /></p>
	<br>
	<br>
	<%
		} else {

					// recorro el resultado

					for (int i = 0; i < resultado.size(); i++) {
						Row fila = (Row) resultado.elementAt(i);
						Hashtable registro = (Hashtable) fila.getRow();
						boolean isAplicarLOPD = (String) registro.get(CenClienteBean.C_NOAPARECERREDABOGACIA) != null
								&& ((String) registro.get(CenClienteBean.C_NOAPARECERREDABOGACIA)).equals(ClsConstants.DB_TRUE);
						String cont = new Integer(i + 1).toString();
						UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
						String valor = "";
						String idPersona = (registro.get(CenColegiadoBean.C_IDPERSONA) == null || ((String) registro
								.get(CenColegiadoBean.C_IDPERSONA)).equals("")) ? "&nbsp;" : (String) registro.get(CenColegiadoBean.C_IDPERSONA);

						// permisos de acceso
						String permisos = "C,E";
						String select = "";
						FilaExtElement[] elems = null;

						//Comprueba si la institucion conectada es un Consejo
						if (idInstitucionLocation.equals("2000") || idInstitucionLocation.substring(0, 1).equals("3")) {
							//SI ES COLEGIADO
							if (colegiado.equals(ClsConstants.DB_TRUE)) {
								valor = CenClienteAdm.getEsLetrado(idPersona, user.getLocation());
								if (isAplicarLOPD) {
									elems = new FilaExtElement[2];
									elems[1] = new FilaExtElement("lopd", "lopd", SIGAConstants.ACCESS_READ);
									if (valor != null && valor.equals("1")) {
										elems[0] = new FilaExtElement("informacionLetrado", "informacionLetrado", SIGAConstants.ACCESS_READ);
									} else {
										elems[0] = new FilaExtElement("informacionLetrado", "informacionLetrado",
												SIGAConstants.ACCESS_SIGAENPRODUCCION);
									}
								} else {
									elems = new FilaExtElement[2];
									if (valor != null && valor.equals("1")) {
										elems[0] = new FilaExtElement("informacionLetrado", "informacionLetrado", SIGAConstants.ACCESS_READ);
										elems[1] = new FilaExtElement("enviar", "comunicar", SIGAConstants.ACCESS_READ);
									} else {
										elems[0] = new FilaExtElement("informacionLetrado", "informacionLetrado",
												SIGAConstants.ACCESS_SIGAENPRODUCCION);
										elems[1] = new FilaExtElement("enviar", "comunicar", SIGAConstants.ACCESS_SIGAENPRODUCCION);
									}
								}
							}
							// LETRADO O NO COLEGIADO
							else {
								if (isAplicarLOPD) {
									elems = new FilaExtElement[1];
									elems[0] = new FilaExtElement("lopd", "lopd", SIGAConstants.ACCESS_READ);

								} else {
									elems = new FilaExtElement[1];
									elems[0] = new FilaExtElement("enviar", "comunicar", SIGAConstants.ACCESS_READ);
								}

							}
						} else {
							if (isAplicarLOPD) {
								elems = new FilaExtElement[1];
								elems[0] = new FilaExtElement("lopd", "lopd", SIGAConstants.ACCESS_READ);

							} else {
								elems = new FilaExtElement[1];
								elems[0] = new FilaExtElement("enviar", "comunicar", SIGAConstants.ACCESS_READ);
							}

						}

						if (ParametrolopdActivo) {
							isAplicarLOPD = false;
						}

						String modo = "";
						String idInstitucion = (registro.get(CenColegiadoBean.C_IDINSTITUCION) == null || ((String) registro
								.get(CenColegiadoBean.C_IDINSTITUCION)).equals("")) ? "&nbsp;" : (String) registro
								.get(CenColegiadoBean.C_IDINSTITUCION);

						if (user.getLocation().equals(idInstitucion)) {
							modo = "edicion";
						} else {
							modo = "consulta";
						}

						//No colegiado
						if (!colegiado.equals(ClsConstants.DB_TRUE) && !colegiado.equals("2")) {
							permisos += ",B";
						}

						// calculo de campos

						String apellido1 = UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_APELLIDOS1));
						String apellido2 = UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_APELLIDOS2));
						String nombre = UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_NOMBRE));
						String nif = UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_NIFCIF));
						String fechaNacimiento = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(), registro
								.get(CenPersonaBean.C_FECHANACIMIENTO)));

						String domicilio = UtilidadesString.mostrarDatoJSP(registro.get(CenDireccionesBean.C_DOMICILIO));
						String poblacion = UtilidadesString.mostrarDatoJSP(registro.get("POBLACION"));
						String CP = UtilidadesString.mostrarDatoJSP(registro.get(CenDireccionesBean.C_CODIGOPOSTAL));
						String nTelefonos = "";
						String nTelef1 = "";
						String nMovil = "";
						if (colegiado.equals(ClsConstants.DB_TRUE)) {//colegiado
							nTelef1 = registro.get("TELEFONO").toString();
							nMovil = registro.get("MOVIL").toString();
							if (nTelef1 != null && !nTelef1.equalsIgnoreCase(""))
								nTelefonos += UtilidadesString.mostrarDatoJSP(registro.get("TELEFONO"));
							if (nMovil != null && !nMovil.equalsIgnoreCase("")) {
								if (!nTelefonos.equalsIgnoreCase(""))
									nTelefonos += " -";
								nTelefonos += UtilidadesString.mostrarDatoJSP(nMovil) + " (M)";
							}
							nTelefonos = UtilidadesString.mostrarDatoJSP(nTelefonos);
						}
						String ncomunitario = "";
						String ncolegiado = "";
						String fechaIncorporacion = "";
						String estadoColegial = "";
						String fechaEstadoColegial = "";
						String residente = "";
						String SociedaSJ = (String) registro.get(CenNoColegiadoBean.C_SOCIEDADSJ);

						//Campo que indica que si va a ir a el jsp para no colegiados para sociedades o nif de tipo no personal
						String tipo = (String) registro.get(CenNoColegiadoBean.C_TIPO);
						String tipo1 = (String) registro.get("TIPO1");
						String tipoaux = "";
						if (tipo != null && tipo.equals("1")) {
							tipoaux = UtilidadesString.mostrarDatoJSP(UtilidadesMultidioma.getDatoMaestroIdioma("censo.general.literal.Personal",
									usrbean));

						} else {
							if (tipo1 != null && tipo1.equals("2")) {
								tipoaux = UtilidadesString.mostrarDatoJSP(UtilidadesMultidioma.getDatoMaestroIdioma(
										"censo.busquedaClientesAvanzada.literal.Sociedad", usrbean));
							} else {
								tipoaux = UtilidadesString.mostrarDatoJSP(UtilidadesMultidioma.getDatoMaestroIdioma(
										"censo.busquedaClientes.literal.institucion", usrbean));
							}
						}

						if (registro != null && !registro.equals(ClsConstants.COMBO_TIPO_PERSONAL))
							tipo = (String) registro.get(CenNoColegiadoBean.C_TIPO);
						else
							tipo = "NINGUNO";

						if (colegiado.equals(ClsConstants.DB_TRUE)) {
							ncomunitario = UtilidadesString.mostrarDatoJSP(registro.get(CenColegiadoBean.C_NCOMUNITARIO));
							ncolegiado = UtilidadesString.mostrarDatoJSP(registro.get(CenColegiadoBean.C_NCOLEGIADO));
							fechaIncorporacion = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(), registro
									.get(CenColegiadoBean.C_FECHAINCORPORACION)));
							//estadoColegial = UtilidadesString.mostrarDatoJSP(UtilidadesMultidioma.getDatoMaestroIdioma((String)registro.get("ESTADOCOLEGIAL"),usrbean));
							estadoColegial = UtilidadesString.mostrarDatoJSP(admCen.getEstadoColegial(registro.get(CenColegiadoBean.C_IDPERSONA)
									.toString(), registro.get(CenColegiadoBean.C_IDINSTITUCION).toString()));
							fechaEstadoColegial = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(), admCen
									.getFechaEstadoColegial(registro.get(CenColegiadoBean.C_IDPERSONA).toString(), registro.get(
											CenColegiadoBean.C_IDINSTITUCION).toString())));
							//				
							residente = UtilidadesString.mostrarDatoJSP(registro.get(CenColegiadoBean.C_SITUACIONRESIDENTE));
						}
						String institucion = CenVisibilidad.getAbreviaturaInstitucion(idInstitucion);
	%>
	<!-- REGISTRO  -->
	<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acci�n sobre los registos  -->

	<siga:FilaConIconos fila="<%=cont %>" botones="<%=permisos %>"
		modo="<%=modo %>" elementos="<%=elems%>" visibleBorrado="no"
		clase="listaNonEdit" pintarEspacio="no">
		<td>
		<%
			String valorCheck = idInstitucion + "||" + idPersona;

					if (!ParametrolopdActivo && isAplicarLOPD) {
							valorCheck += "||" + ClsConstants.DB_TRUE;
		%>
				 <input type="checkbox" value="<%=valorCheck%>" name="chkPersona" disabled> 
		<%
 					} else {
 							if (!ParametrolopdActivo) {
 								valorCheck += "||" + ClsConstants.DB_FALSE;
 							}

 							boolean isChecked = false;
 							for (int z = 0; z < registrosSeleccionados.size(); z++) {
 								Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);

 								String clave = (String) clavesRegistro.get("CLAVE");

 								if (valorCheck.equals(clave)) {
 									isChecked = true;
 									break;
 								}

 							}
 							if (isChecked) {
 %> <input type="checkbox" value="<%=valorCheck%>" name="chkPersona"
			checked onclick="pulsarCheck(this)"> <%
 	} else {
 %> <input type="checkbox" value="<%=valorCheck%>"
			name="chkPersona" onclick="pulsarCheck(this)"> <%
 	}
 						}
 %>
		</td>


		<%
			//Si es un colegiado:
								if (colegiado.equals(ClsConstants.DB_TRUE)) {
		%>
		<td><!-- campos hidden --> 
		<input type="hidden" name="oculto<%=cont%>_1" value="<%=idPersona%>"> 
		<input type="hidden" name="oculto<%=cont%>_2" value="<%=idInstitucion%>">
		<input type="hidden" name="oculto<%=cont%>_3" value="NINGUNO">
		<input type="hidden" name="oculto<%=cont%>_4" value="1"> <%=nif%>
		</td>
		<td><%=ncolegiado%></td>
		<td><%=apellido1 + " " + apellido2%></td>
		<td><%=nombre%></td>
		<td><%=fechaIncorporacion%></td>
		<%
			if (!esColegio) {
		%>
			<td><%=institucion%></td>
		<%
			}
		%>
		<td>
		<%
			if (estadoColegial != null && !estadoColegial.equals("&nbsp")) {
		%>
		   <%=estadoColegial%>  (<%=fechaEstadoColegial%>)  
		<%
		   	} else { // para colegiados sin estado colegial o con estado colegial a futuro
		   %>
		    <siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial"/>
		<%
			}
		%>   
		</td>
		<td><%=residente.equals("0") ? "No" : "Si"%></td>
		<%
			if (!esColegio) {
		%>
			<td><%=fechaNacimiento%></td>
		<%
			} else {
		%>
			<td><%=nTelefonos%></td>
		<%
			}
		%>

		<%
			} else {
									if (colegiado.equals(ClsConstants.DB_FALSE)) {
		%>

		<td><!-- campos hidden --> <input type="hidden"
			name="oculto<%=cont%>_1" value="<%=idPersona%>"> <input
			type="hidden" name="oculto<%=cont%>_2" value="<%=idInstitucion%>">
		<input type="hidden" name="oculto<%=cont%>_3" value="<%=tipo%>">

		<siga:Idioma key='<%=tipoaux %>' /></td>
		<td><%=nif%></td>
		<%
			if (tipo != null && tipo != "" && !tipo.equals("1")) {
		%>
		<td>&nbsp;</td>
		<td><%=nombre%></td>
		<%
			} else {
		%>
		<td><%=apellido1 + " " + apellido2%></td>
		<td><%=nombre%></td>
		<%
			}
		%>
		<td><%=institucion%></td>
		<td><%=fechaNacimiento%></td>
		<%
			} else {
		%>
		<td><!-- campos hidden --> <input type="hidden"
			name="oculto<%=cont%>_1" value="<%=idPersona%>"> <input
			type="hidden" name="oculto<%=cont%>_2" value="<%=idInstitucion%>">
		<input type="hidden" name="oculto<%=cont%>_3" value="LETRADO">

		<%=idPersona%></td>
		<td><%=nif%></td>
		<td><%=apellido1 + " " + apellido2%></td>
		<td><%=nombre%></td>
		<td><%=fechaNacimiento%></td>
		
			<%
				if (domicilio != null && !domicilio.equals("&nbsp")) {
			%>
				<td><%=domicilio + ", " + CP + ", " + poblacion%></td>
			<%
				} else {
			%>
			<td>&nbsp;</td>
				
			<%
				}
			%>
		
		
		<%
			}
		%>
		<%
			}
		%>
	</siga:FilaConIconos>


	<!-- FIN REGISTRO -->
	<%
		} // del for
	%>

	<!-- FIN: ZONA DE REGISTROS -->

	<%
		} // del if
	%>

</siga:TablaCabecerasFijas>
	<siga:ConjBotonesAccion botones="GX,COM" />

<!-- FIN: LISTA DE VALORES -->
<%
	if (datosPaginador != null && datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
			String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0 : registrosSeleccionados.size()));

			if (colegiado.equals("2")) {
%>
<siga:Paginador totalRegistros="<%=totalRegistros%>"
	registrosPorPagina="<%=registrosPorPagina%>"
	paginaSeleccionada="<%=paginaSeleccionada%>" idioma="<%=idioma%>"
	registrosSeleccionados="<%=regSeleccionados%>"
	modo="buscarPor" clase="paginator"
	divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
	
	distanciaPaginas="" action="<%=action%>" 
	
	/>
<%
	} else {
%>

<siga:Paginador totalRegistros="<%=totalRegistros%>"
	registrosPorPagina="<%=registrosPorPagina%>"
	paginaSeleccionada="<%=paginaSeleccionada%>" idioma="<%=idioma%>"
	registrosSeleccionados="<%=regSeleccionados%>"
	modo="buscarPor" clase="paginator"
	divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
	
	distanciaPaginas="" action="<%=action%>" 
	
	/>

<%
	}
		}
%>


<script language="JavaScript">
	ObjArray = new Array();
		
	Array.prototype.indexOf = function(s) {
	for (var x=0;x<this.length;x++) if(this[x] == s) return x;
		return false;
	}
 
	   
	function pulsarCheck(obj){
	
		if (!obj.checked ){
		   		
			ObjArray.splice(ObjArray.indexOf(obj.value),1);
			seleccionados1=ObjArray;
		}else{
			ObjArray.push(obj.value);
		   	seleccionados1=ObjArray;
		}
		  	
		  	
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		
		document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		checkTodos();
		   
	}


		
	function cargarChecks(){
		<%if (registrosSeleccionados != null) {
					for (int p = 0; p < registrosSeleccionados.size(); p++) {
						Hashtable clavesEJG = (Hashtable) registrosSeleccionados.get(p);
						valorCheckPersona = (String) clavesEJG.get("CLAVE");
						if (!ParametrolopdActivo) {
							String noApareceEnRedAbogacia = (String) clavesEJG.get(CenClienteBean.C_NOAPARECERREDABOGACIA);
							if (noApareceEnRedAbogacia == null || noApareceEnRedAbogacia.equals("")
									|| noApareceEnRedAbogacia.equals(ClsConstants.DB_FALSE)) {%>
						ObjArray.push('<%=valorCheckPersona%>');
					<%}
						} else {%>
						ObjArray.push('<%=valorCheckPersona%>');
					<%}
					}

				}%>
		ObjArray.toString();
		seleccionados1=ObjArray;
		document.forms[0].registrosSeleccionados.value=seleccionados1;
		if(document.getElementById('registrosSeleccionadosPaginador'))
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
			
	}

	function cargarChecksTodos(o){  		
		if (document.getElementById('registrosSeleccionadosPaginador')){			
	  		var conf = confirm('<siga:Idioma key="paginador.message.marcarDesmarcar"/>'); 
	   	   	
		   	if (conf){			   	
		   		
				ObjArray = new Array();
			   	if (o.checked){				   				
					parent.seleccionarTodos('<%=paginaSeleccionada%>');
					
				}else{
					
					ObjArray1= new Array();
				 	ObjArray=ObjArray1;
				 	
				 	seleccionados1=ObjArray;
				 	
					document.forms[0].registrosSeleccionados.value=seleccionados1;
					var ele = document.getElementsByName("chkPersona");
						
					for (i = 0; i < ele.length; i++) {
						if(!ele[i].disabled)	
							ele[i].checked = false; 
							
					}
		
				 }
		   	  
		   	  }else{
		   	  	if (!o.checked ){
		   	  			
			   	  		var ele = document.getElementsByName("chkPersona");
							
					  	for (i = 0; i < ele.length; i++) {
					  		if(!ele[i].disabled){
					  			if(ele[i].checked){	
			     					ele[i].checked = false;
									ObjArray.splice(ObjArray.indexOf(ele[i].value),1);
								}
							}
					   	}
					   	
					   	seleccionados1=ObjArray;
				   }else{
				   	
					   	var ele = document.getElementsByName("chkPersona");
								
					  	for (i = 0; i < ele.length; i++) {
					  		if(!ele[i].disabled){
								if(!ele[i].checked){				  		
				    				ele[i].checked = true;
									ObjArray.push(ele[i].value);
								}
							}
					   	}
					   		
				   		seleccionados1=ObjArray;
				   }
				   document.forms[0].registrosSeleccionados.value=seleccionados1;
			   		
		   	  }
		   	if(document.getElementById('registrosSeleccionadosPaginador'))  		 
				document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		}
	 }
	   
	function checkTodos(){
	
	 	var ele = document.getElementsByName("chkPersona");
		var todos=1;	
	  	for (i = 0; i < ele.length; i++) {
   			if(!ele[i].checked && !ele[i].disabled){
   				todos=0;
   				break;
   			} 
   		}
	   
	   if (todos==1){
	   		
			document.getElementById("chkGeneral").checked=true;
		}else{
			document.getElementById("chkGeneral").checked=false;
		}
	   
				
			
			
   	}
   	
   	function comunicar(fila)
	{
		var auxPers = 'oculto' + fila + '_1';
		var idPersona = document.getElementById(auxPers).value;
		var auxInst = 'oculto' + fila + '_2';
		var idInstPersona = document.getElementById(auxInst).value;		
	   	datos = "idPersona=="+idPersona + "##idInstitucion==" +idInstPersona ; 
		
		var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
		formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='<%=idInstitucionLocation%>'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='CENSO'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='1'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='1'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='tipoPersonas' value='<%=colegiado%>'>"));
		
		document.appendChild(formu);
		formu.datosInforme.value=datos;
		formu.submit();
			
				
			
	}
   	
	function accionComunicar()
		{
		
			sub();
			
			datos = "";
		
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			index = idRegistros.indexOf('||');
			idInstitucion  = idRegistros.substring(0,index);		
			if(<%=ParametrolopdActivo%>){
				idPersona = idRegistros.substring(index+2);
			}
			else{
				idRegistros = idRegistros.substring(index+2);
				index = idRegistros.indexOf('||');
				idPersona  = idRegistros.substring(0,index);
				idRegistros = idRegistros.substring(index+2);					
				}	
			
 		   	datos = datos +"idPersona=="+idPersona + "##idInstitucion==" +idInstitucion+"%%%";
		}
		
		numElementosSeleccionados =  ObjArray.length; 
		if (datos == '') {
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			fin();
			return;
		}
			
			var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
			formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='<%=idInstitucionLocation%>'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
			formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='CENSO'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
			formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
			formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='1'>"));
			if(numElementosSeleccionados>50){
				formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='0'>"));
			}
			else{
				
				formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='1'>"));
			}
			formu.appendChild(document.createElement("<input type='hidden' name='tipoPersonas' value='<%=colegiado%>'>"));
			
			document.appendChild(formu);
			formu.datosInforme.value=datos;
			formu.submit();
		}

	
		function accionGenerarExcels(){
   		sub();
			
			datos = "";
		
		for (i = 0; i < ObjArray.length; i++) {				
			var idRegistros = ObjArray[i];
			index = idRegistros.indexOf('||');
			idInstitucion  = idRegistros.substring(0,index);			
			if(<%=ParametrolopdActivo%>){//si el parametro de EXPORTAR_COLEGIADOS_ACOGIDOS_A_LOPD esta activo.
				idPersona = idRegistros.substring(index+2);
			}else{
				idRegistros = idRegistros.substring(index+2);
				index = idRegistros.indexOf('||');
				idPersona  = idRegistros.substring(0,index);
				idRegistros = idRegistros.substring(index+2);									
			}
			
			datos = datos +	idPersona + "," +idInstitucion +",<%=colegiado%>#";
		}
			
			if (datos == '') {
				
				alert ('<siga:Idioma key="general.message.seleccionar"/>');
				fin();
				return;
			}
			document.forms[0].tablaDatosDinamicosD.value = datos;
			document.forms[0].modo.value ='generaExcel';		
			document.forms[0].submit();			
			fin();
			
   	}

</script>


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>

<%
	} catch (Exception e) {
		e.printStackTrace();
	}
%>