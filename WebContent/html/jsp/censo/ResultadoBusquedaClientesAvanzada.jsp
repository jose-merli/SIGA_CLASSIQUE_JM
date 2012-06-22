<!-- ResultadoBusquedaClientesAvanzada.jsp -->
<!-- EJEMPLO DE VENTANA DE LISTA DE RESULTADOS -->
<!-- Contiene lista de resultados y botones de acción. Aunque
	 No tienen sentido las acciones de guardar o modificar, si que 
	 lo tienen el Nuevo y el volver (o cerrar en caso de modal)
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
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.siga.beans.CenClienteAdm"%>

<!-- JSP -->
<bean:define id="registrosSeleccionados" name="busquedaClientesAvanzadaForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="busquedaClientesAvanzadaForm" property="datosPaginador" type="java.util.HashMap"/>
<bean:define id="colegiado" name="busquedaClientesAvanzadaForm" property="colegiado"  type="String"/>
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean usrbean = (UsrBean) session
			.getAttribute(ClsConstants.USERBEAN);
	String idioma = usrbean.getLanguage().toUpperCase();
	String idInstitucionLocation = usrbean.getLocation();
	CenClienteAdm admCen = new CenClienteAdm(usrbean);
	int idInstitucionInt = Integer.parseInt(idInstitucionLocation);
	boolean esColegio = (idInstitucionInt > 2000 && idInstitucionInt < 3000);
	boolean ParametrolopdActivo = ((String) request
			.getAttribute("ParametrolopdActivo")).equals("1");
%>	

<%
		// locales
		//BusquedaClientesForm formulario = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesAvanzadaForm");

		//String colegiado = formulario.getColegiado();
		if (colegiado == null)
			colegiado = "";
		//Vector resultado = (Vector) request.getAttribute("CenResultadoBusquedaClientes");
		String titu = "";
		if (colegiado.equals(ClsConstants.DB_TRUE)) {
			//colegiados
			titu = "censo.busquedaClientesAvanzada.colegiados.titulo";
		} else {
			//no colegiados
			titu = "censo.busquedaClientesAvanzada.noColegiados.titulo";
		}

		Vector resultado = null;
		/** PAGINADOR **/

		String valorCheckPersona = "";

		String paginaSeleccionada = "";

		String totalRegistros = "";

		String registrosPorPagina = "";
		if (datosPaginador != null) {

			if (datosPaginador.get("datos") != null
					&& !datosPaginador.get("datos").equals("")) {
				resultado = (Vector) datosPaginador.get("datos");
				PaginadorBind paginador = (PaginadorBind) datosPaginador
						.get("paginador");

				paginaSeleccionada = String.valueOf(paginador
						.getPaginaActual());

				totalRegistros = String.valueOf(paginador
						.getNumeroTotalRegistros());

				registrosPorPagina = String.valueOf(paginador
						.getNumeroRegistrosPorPagina());
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

		String action = app
				+ "/CEN_BusquedaClientesAvanzada.do?noReset=true";
	%>
	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="<%=titu %>" 
		localizacion="censo.busquedaClientes.localizacion"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
		function refrescarLocal() {
			//document.buscarLocal();
		}
		function vueltaEnvio() {
			
		}
		function accionCerrar() {
			
		}
		
		function informacionLetrado(fila) {
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

<body onLoad="cargarChecks();checkTodos();validarAncho_tablaDatos();">

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<!-- Esto es muy util para el caso de ventanas modales, ya que no
	     vamos a disponer en estos casos de la cabecera de la pagina.
	     Nos serviria para tener conciencia de donde estamos ya que desde una
	     ventana modal no se puede actualizar la barra de titulo y ademas queda detras -->

		<table class="tablaTitulo" align="center">
		<tr>
		<td class="titulitos">
			<siga:Idioma key="censo.busquedaClientesAvanzada.literal.resultados" />
		</td>
		</tr>
		</table>

	<!-- FIN: TITULO OPCIONAL DE LA TABLA -->


	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/CEN_BusquedaClientesAvanzada.do?noReset=true" method="POST" target="mainWorkArea">
		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = ""/>
		<html:hidden property="registrosSeleccionados" value="" />
		<html:hidden property="datosPaginador" value="" />
		<html:hidden property="seleccionarTodos" />
	
		<!-- parametro para colegiados o no -->
		<html:hidden styleId = "colegiado" property = "colegiado"  value= "<%=colegiado %>"/>
		<html:hidden styleId = "avanzada"  property = "avanzada"  value = ""/>
		<input type="hidden" name="actionModal" id="actionModal"  value="">
		<input type="hidden" id="verFichaLetrado" name="verFichaLetrado" value="">			
	</html:form>




		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		
<%
			String tamanosCol = "";
			String nombresCol = "";
			if (colegiado.equals(ClsConstants.DB_TRUE)) {

				if (esColegio) {
					tamanosCol = "3,8,11,16,10,11,11,8,9,15";
					nombresCol += "<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,censo.busquedaClientesAvanzada.literal.nif,censo.busquedaClientesAvanzada.literal.nColegiado,censo.busquedaClientesAvanzada.literal.nombre,censo.busquedaClientesAvanzada.literal.fechaIngreso,censo.busquedaClientesAvanzada.literal.estadoColegial,censo.consultaDatosGenerales.literal.fechaEstado, censo.busquedaClientesAvanzada.literal.residente,censo.busquedaClientesAvanzada.literal.fechaNacimiento,";

				} else {
					tamanosCol = "3,8,11,16,10,11,11,8,9,15";
					nombresCol += "<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,censo.busquedaClientesAvanzada.literal.nif,censo.busquedaClientesAvanzada.literal.nColegiado,censo.busquedaClientesAvanzada.literal.nombre,censo.busquedaClientesAvanzada.literal.fechaIngreso,censo.busquedaClientes.literal.institucion,censo.busquedaClientesAvanzada.literal.estadoColegial,censo.busquedaClientesAvanzada.literal.residente,censo.busquedaClientesAvanzada.literal.fechaNacimiento,";
				}

			} else {
				tamanosCol = "3,12,42,15,15,16";
				nombresCol = "<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>,censo.busquedaClientesAvanzada.literal.nif,censo.busquedaClientesAvanzada.literal.nombre,censo.busquedaClientes.literal.institucion,censo.busquedaClientes.literal.FechaNacimientoConstitucion,";
			}
		%>

		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="<%=nombresCol%>"
		   tamanoCol="<%=tamanosCol%>"
		   alto="365"
		   ajusteBotonera="true"
		   ajustePaginador="true"  
		   activarFilaSel="true" >

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%
				if (resultado == null || resultado.size() == 0) {
			%>			
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>
<%
	} else {
			boolean isAplicarLOPD = false;
			// recorro el resultado
			for (int i = 0; i < resultado.size(); i++) {
				//Hashtable registro = (Hashtable) resultado.get(i);
				Row fila = (Row) resultado.elementAt(i);
				Hashtable registro = (Hashtable) fila.getRow();
				isAplicarLOPD = (String) registro
						.get(CenClienteBean.C_NOAPARECERREDABOGACIA) != null
						&& ((String) registro
								.get(CenClienteBean.C_NOAPARECERREDABOGACIA))
								.equals(ClsConstants.DB_TRUE);
				String cont = new Integer(i + 1).toString();
				UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
				String valor = "";
				String idPersona = (registro
						.get(CenColegiadoBean.C_IDPERSONA) == null || ((String) registro
						.get(CenColegiadoBean.C_IDPERSONA)).equals(""))
						? "&nbsp;"
						: (String) registro
								.get(CenColegiadoBean.C_IDPERSONA);
				String noApareceRed = (registro
						.get(CenClienteBean.C_NOAPARECERREDABOGACIA) == null || ((String) registro
						.get(CenClienteBean.C_NOAPARECERREDABOGACIA))
						.equals("")) ? "&nbsp;" : (String) registro
						.get(CenClienteBean.C_NOAPARECERREDABOGACIA);
				// permisos de acceso
				String permisos = "C,E";
				FilaExtElement[] elems = new FilaExtElement[1];
				if (idInstitucionLocation.equals("2000")
						|| idInstitucionLocation.substring(0, 1)
								.equals("3")) {
					if (colegiado.equals(ClsConstants.DB_TRUE)) {
						valor = CenClienteAdm.getEsLetrado(idPersona,
								user.getLocation());
						if (isAplicarLOPD) {
							elems = new FilaExtElement[2];
							elems[1] = new FilaExtElement("lopd",
									"lopd", SIGAConstants.ACCESS_READ);
							if (valor != null && valor.equals("1")) {
								elems[0] = new FilaExtElement(
										"informacionLetrado",
										"informacionLetrado",
										SIGAConstants.ACCESS_READ);
							} else {
								elems[0] = new FilaExtElement(
										"informacionLetrado",
										"informacionLetrado",
										SIGAConstants.ACCESS_SIGAENPRODUCCION);
							}
						} else {
							elems = new FilaExtElement[2];
							if (valor != null && valor.equals("1")) {
								elems[0] = new FilaExtElement(
										"informacionLetrado",
										"informacionLetrado",
										SIGAConstants.ACCESS_READ);
								elems[1] = new FilaExtElement("enviar",
										"comunicar",
										SIGAConstants.ACCESS_READ);
							} else {
								elems[0] = new FilaExtElement(
										"informacionLetrado",
										"informacionLetrado",
										SIGAConstants.ACCESS_SIGAENPRODUCCION);
								elems[1] = new FilaExtElement(
										"enviar",
										"comunicar",
										SIGAConstants.ACCESS_SIGAENPRODUCCION);
							}
						}
					} else {
						if (isAplicarLOPD) {
							elems = new FilaExtElement[1];
							elems[0] = new FilaExtElement("lopd",
									"lopd", SIGAConstants.ACCESS_READ);
						} else {
							elems = new FilaExtElement[1];
							elems[0] = new FilaExtElement("enviar",
									"comunicar",
									SIGAConstants.ACCESS_READ);
						}
					}
				} else {
					if (isAplicarLOPD) {
						elems = new FilaExtElement[1];
						elems[0] = new FilaExtElement("lopd", "lopd",
								SIGAConstants.ACCESS_READ);
					} else {
						elems = new FilaExtElement[1];
						elems[0] = new FilaExtElement("enviar",
								"comunicar", SIGAConstants.ACCESS_READ);
					}
				}
				if (ParametrolopdActivo) {
					isAplicarLOPD = false;
				}

				String modo = "";
				String idInstitucion = (registro
						.get(CenColegiadoBean.C_IDINSTITUCION) == null || ((String) registro
						.get(CenColegiadoBean.C_IDINSTITUCION))
						.equals("")) ? "&nbsp;" : (String) registro
						.get(CenColegiadoBean.C_IDINSTITUCION);

				if (user.getLocation().equals(idInstitucion)) {
					modo = "edicion";
				} else {
					modo = "consulta";
				}
				if (!colegiado.equals(ClsConstants.DB_TRUE)) {
					permisos += ",B";
				}

				// calculo de campos
				String apellido1 = UtilidadesString
						.mostrarDatoJSP(registro
								.get(CenPersonaBean.C_APELLIDOS1));
				String apellido2 = UtilidadesString
						.mostrarDatoJSP(registro
								.get(CenPersonaBean.C_APELLIDOS2));
				String nombre = UtilidadesString
						.mostrarDatoJSP(registro
								.get(CenPersonaBean.C_NOMBRE));
				String nif = UtilidadesString.mostrarDatoJSP(registro
						.get(CenPersonaBean.C_NIFCIF));
				String fechaNacimiento = UtilidadesString
						.mostrarDatoJSP(GstDate.getFormatedDateShort(
								usrbean.getLanguage(),
								registro.get(CenPersonaBean.C_FECHANACIMIENTO)));

				String ncomunitario = "";
				String ncolegiado = "";
				String fechaIncorporacion = "";
				String estadoColegial = "";
				String fechaEstadoColegial = "";
				String residente = "";

				//Campo que indica que si va a ir a el jsp para no coelgiados para sociedades o nif de tipo no personal
				String tipo = (String) registro
						.get(CenNoColegiadoBean.C_TIPO);
				if (registro != null
						&& !registro
								.equals(ClsConstants.COMBO_TIPO_PERSONAL))
					tipo = (String) registro
							.get(CenNoColegiadoBean.C_TIPO);
				else
					tipo = "NINGUNO";

				if (colegiado.equals(ClsConstants.DB_TRUE)) {
					ncomunitario = UtilidadesString
							.mostrarDatoJSP(registro
									.get(CenColegiadoBean.C_NCOMUNITARIO));
					ncolegiado = UtilidadesString
							.mostrarDatoJSP(registro
									.get(CenColegiadoBean.C_NCOLEGIADO));
					fechaIncorporacion = UtilidadesString
							.mostrarDatoJSP(GstDate.getFormatedDateShort(
									usrbean.getLanguage(),
									registro.get(CenColegiadoBean.C_FECHAINCORPORACION)));
					//estadoColegial = (registro.get("ESTADOCOLEGIAL")==null||((String)registro.get("ESTADOCOLEGIAL")).equals(""))?"&nbsp;":(String)registro.get("ESTADOCOLEGIAL");
					//if(registro.get("ESTADOCOLEGIAL")!=null)
					//estadoColegial = UtilidadesMultidioma.getDatoMaestroIdioma (estadoColegial, usrbean);
					estadoColegial = UtilidadesString
							.mostrarDatoJSP(admCen
									.getEstadoColegial(
											registro.get(
													CenColegiadoBean.C_IDPERSONA)
													.toString(),
											registro.get(
													CenColegiadoBean.C_IDINSTITUCION)
													.toString()));
					fechaEstadoColegial = UtilidadesString
							.mostrarDatoJSP(GstDate.getFormatedDateShort(
									usrbean.getLanguage(),
									admCen.getFechaEstadoColegial(
											registro.get(
													CenColegiadoBean.C_IDPERSONA)
													.toString(),
											registro.get(
													CenColegiadoBean.C_IDINSTITUCION)
													.toString())));
					residente = UtilidadesString
							.mostrarDatoJSP(registro
									.get(CenColegiadoBean.C_SITUACIONRESIDENTE));
				}
				String institucion = CenVisibilidad
						.getAbreviaturaInstitucion(idInstitucion);
%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->

  			<siga:FilaConIconos 
  				fila="<%=cont %>" botones="<%=permisos %>" 
  				modo="<%=modo %>" elementos="<%=elems%>" 
  				visibleBorrado="no" clase="listaNonEdit" pintarEspacio="no">
				<td>
		<%
			String valorCheck = idInstitucion + "||"
									+ idPersona + "||" + noApareceRed;

							if (isAplicarLOPD) {
								valorCheck += "||" + ClsConstants.DB_TRUE;
		%>
								<input type="checkbox" value="<%=valorCheck%>"  id="chkPersona" name="chkPersona"  disabled >
						<%
							} else {
												/*if (!ParametrolopdActivo) {
													valorCheck += "||" + ClsConstants.DB_FALSE;
												}*/
												boolean isChecked = false;
												for (int z = 0; z < registrosSeleccionados
														.size(); z++) {
													Hashtable clavesRegistro = (Hashtable) registrosSeleccionados
															.get(z);

													String clave = (String) clavesRegistro
															.get("CLAVE");

													if (valorCheck.equals(clave)) {
														isChecked = true;
														break;
													}
												}
												if (isChecked) {
						%>								
									<input type="checkbox" value="<%=valorCheck%>"  id="chkPersona" name="chkPersona" checked onclick="pulsarCheck(this)">
								<%
									} else {
								%>
									<input type="checkbox" value="<%=valorCheck%>"  id="chkPersona" name="chkPersona" onclick="pulsarCheck(this)" >
							<%
								}
												}
							%>
		</td>
<%
	if (colegiado.equals(ClsConstants.DB_TRUE)) {
%>
				<td>
					<!-- campos hidden -->
					<input type="hidden" name="oculto<%=cont%>_1" id="oculto<%=cont%>_1" value="<%=idPersona%>"/>
					<input type="hidden" name="oculto<%=cont%>_2" id="oculto<%=cont%>_2" value="<%=idInstitucion%>"/>
					<input type="hidden" name="oculto<%=cont%>_3" id="oculto<%=cont%>_3" value="NINGUNO">
					<input type="hidden" name="oculto<%=cont%>_4" id="oculto<%=cont%>_4" value="1">
					<%=nif%>
				</td>
				<td>
					<%=ncolegiado%>
				</td>
				<td>
					<%=nombre + " " + apellido1 + " "
										+ apellido2%>
				</td>
				<td>
					<%=fechaIncorporacion%>
				</td>
				<%
					if (!esColegio) {
				%>
					<td><%=institucion%></td>
					<td>
					<%
						if (estadoColegial != null
														&& !estadoColegial.equals("&nbsp")) {
					%>
					   <%=estadoColegial%>  (<%=fechaEstadoColegial%>)  
					<%
					   	} else { // para colegiados sin estado colegial o con estado colegial a futuro
					   %>
					    <siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial"/>
					<%
						}
					%>
				<%
					} else {
				%>				
					<%
										if (estadoColegial != null
																		&& !estadoColegial.equals("&nbsp")) {
									%>
					   <td><%=estadoColegial%></td><td><%=fechaEstadoColegial%></td>  
					<%
  						} else { // para colegiados sin estado colegial o con estado colegial a futuro
  					%>
					    <td><siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial"/></td><td>&nbsp;</td>
					<%
						}
					%>
				<%
					}
				%>   
				</td>
				<td>
				    <%=residente.equals("0") ? "No" : "Si"%>
				</td>
				<td>
					<%=fechaNacimiento%>
				</td>

<%
	} else {
%>

				<td>
					<!-- campos hidden -->
					<input type="hidden" name="oculto<%=cont%>_1" id="oculto<%=cont%>_1" value="<%=idPersona%>">
					<input type="hidden" name="oculto<%=cont%>_2" id="oculto<%=cont%>_2" value="<%=idInstitucion%>">
					<input type="hidden" name="oculto<%=cont%>_3" id="oculto<%=cont%>_3" value="<%=tipo%>">

					<%=nif%>
				</td>
				<td>
					<%=nombre + " " + apellido1 + " "
										+ apellido2%>
				</td>
				<td>
					<%=institucion%>
				</td>
				<td>
					<%=fechaNacimiento%>
				</td>
<%
	}
%>
			</siga:FilaConIconos>		


			<!-- FIN REGISTRO -->
<%
	}
%>			

			<!-- FIN: ZONA DE REGISTROS -->

<%
	}
%>			

		</siga:TablaCabecerasFijas>
	

		<!-- FIN: LISTA DE VALORES -->
		
		<!-- Pintamos la paginacion-->	
		<%
				if (datosPaginador != null && datosPaginador.get("datos") != null
						&& !datosPaginador.get("datos").equals("")) {
					String regSeleccionados = ("" + ((registrosSeleccionados == null)
							? 0
							: registrosSeleccionados.size()));
			%>
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
						registrosPorPagina="<%=registrosPorPagina%>" 
						paginaSeleccionada="<%=paginaSeleccionada%>" 
						registrosSeleccionados="<%=regSeleccionados%>"
						idioma="<%=idioma%>"
						modo="buscarPor"								
						clase="paginator" 
						divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 32px; left:0px"
						distanciaPaginas=""
						action="<%=action%>" />
	 <%}%>			
   <!------------------------------------------->



	<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucionLocation%>"/>
	<html:hidden property="idTipoInforme" value="CENSO"/>
	<html:hidden property="enviar" value="1"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>

	<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" value="">

</form>
	

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

		<!-- Asociada al boton Volver -->
		function accionVolver() {		
			document.busquedaClientesAvanzadaForm.modo.value="abrirAvanzadaConParametros";
			document.busquedaClientesAvanzadaForm.target="mainWorkArea";
			document.busquedaClientesAvanzadaForm.submit();

		}

		ObjArray = new Array();
		
		Array.prototype.indexOf = function(s) {
		for (var x=0;x<this.length;x++) if(this[x] == s) return x;
			return false;
		} 
	   
		function pulsarCheck(obj){	
			if (!obj.checked ){			   		
				ObjArray.splice(ObjArray.indexOf(obj.value),1);
				seleccionados1=ObjArray;
			} else {
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
					Hashtable clavesEJG = (Hashtable) registrosSeleccionados
							.get(p);
					valorCheckPersona = (String) clavesEJG.get("CLAVE");
					if (!ParametrolopdActivo) {
						String noApareceEnRedAbogacia = (String) clavesEJG
								.get(CenClienteBean.C_NOAPARECERREDABOGACIA);
						if (noApareceEnRedAbogacia == null
								|| noApareceEnRedAbogacia.equals("")
								|| noApareceEnRedAbogacia
										.equals(ClsConstants.DB_FALSE)) {%>
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
			if(document.getElementById('registrosSeleccionadosPaginador')) {
				document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
			}
				
		}
		
		function cargarChecksTodos(o){  		
	  		var conf = confirm('¿Desea marcar/desmarcar los registros de las otras paginas?'); 	   	   	
		   	if (conf){
				ObjArray = new Array();
			   	if (o.checked){
			   	 	document.forms[0].modo.value="buscarPor";
					document.forms[0].avanzada.value="<%=ClsConstants.DB_TRUE%>";
					document.forms[0].target="mainWorkArea";
					document.forms[0].seleccionarTodos.value = "<%=paginaSeleccionada%>";
					document.forms[0].submit();				
				} else {
					ObjArray1= new Array();
				 	ObjArray=ObjArray1;			 	
				 	seleccionados1=ObjArray;			 	
					document.forms[0].registrosSeleccionados.value=seleccionados1;
					var ele = document.getElementsByName("chkPersona");					
					for (i = 0; i < ele.length; i++) {
						if(!ele[i].disabled){
							ele[i].checked = false; 						
						}
					}	
				 }	   	  
		   	  } else {
			   	  	if (!o.checked ){
				   	  	var ele = document.getElementsByName("chkPersona");
						for (i = 0; i < ele.length; i++) {
							if(!ele[i].disabled){
								if(ele[i].checked){	
				     				ele[i].checked = false;
				     				//alert("ele.value"+ele.value);
									ObjArray.splice(ObjArray.indexOf(ele[i].value),1);
								}
							}
						}
						seleccionados1=ObjArray;
				   } else {
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
		   	if(document.getElementById('registrosSeleccionadosPaginador')){
		   		document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
		   	}
		}
	   
	
		function checkTodos() {
			var ele = document.getElementsByName("chkPersona");
			var todos = 1;
			for (i = 0; i < ele.length; i++) {
				if (!ele[i].checked && !ele[i].disabled) {
					todos = 0;
					break;
				}
			}
			if (todos == 1) {
				document.getElementById("chkGeneral").checked = true;
			} else {
				document.getElementById("chkGeneral").checked = false;
			}
		}

		function comunicar(fila) {
			var auxPers = 'oculto' + fila + '_1';
			var idPersona = document.getElementById(auxPers).value;
			var auxInst = 'oculto' + fila + '_2';
			var idInstPersona = document.getElementById(auxInst).value;
			datos = "idPersona==" + idPersona + "##idInstitucion=="	+ idInstPersona;
			var action = "<%=app%>/INF_InformesGenericos.do";
			var formuStr = "<form name='InformesGenericosForm' id='InformesGenericosForm' method='POST' action='"+action+"' target='submitArea' />";
			$(formuStr).appendTo(document.body);
			var idInstitucionStr = "<input type='hidden' name='idInstitucion' value='<%=idInstitucionLocation%>' />";
			$("#InformesGenericosForm",document).append(idInstitucionStr);
			var idInforme = "<input type='hidden' name='idInforme' value='' />";
			$("#InformesGenericosForm",document).append(idInforme);
			var idTipoInforme = "<input type='hidden' name='idTipoInforme' value='CENSO' />";
			$("#InformesGenericosForm",document).append(idTipoInforme);
			var datosInforme = "<input type='hidden' name='datosInforme' value='"+datos+"' />";
			$("#InformesGenericosForm",document).append(datosInforme);
			var seleccionados = "<input type='hidden' name='seleccionados' value='0' />";
			$("#InformesGenericosForm",document).append(seleccionados);
			var enviar = "<input type='hidden' name='enviar' value='1' />";
			$("#InformesGenericosForm",document).append(enviar);
			var descargar = "<input type='hidden' name='descargar' value='1' />";
			$("#InformesGenericosForm",document).append(descargar);
			var tipoPersonas = "<input type='hidden' name='tipoPersonas' value='<%=colegiado%>' />";
			$("#InformesGenericosForm",document).append(tipoPersonas);
			$("#InformesGenericosForm",document).submit();		
		}
   	
   	function comunicar(fila)
	{
		var auxPers = 'oculto' + fila + '_1';
		var idPersona = document.getElementById(auxPers).value;
		var auxInst = 'oculto' + fila + '_2';
		var idInstPersona = document.getElementById(auxInst).value;		
	   	datos = "idPersona=="+idPersona + "##idInstitucion==" +idInstPersona+"##idTipoPersonas==" +<%=colegiado%>+"##idTipoInforme==CENSO" ; 
		
		
		document.forms["InformesGenericosForm"].datosInforme.value=datos;
		var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
		if (arrayResultado==undefined||arrayResultado[0]==undefined){
		   		
	   	} 
	   	else {
	   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
	   		if(confirmar){
	   			var idEnvio = arrayResultado[0];
			    var idTipoEnvio = arrayResultado[1];
			    var nombreEnvio = arrayResultado[2];				    
			    
			   	document.forms["DefinirEnviosForm"].tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
			   	document.forms["DefinirEnviosForm"].modo.value='editar';
			   	document.forms["DefinirEnviosForm"].submit();
	   		}
	   	}
			
				
			
	}
   	
	function accionComunicar()
		{
		
			
			datos = "";
		
		for (i = 0; i < ObjArray.length; i++) {
			var idRegistros = ObjArray[i];
			index = idRegistros.indexOf('||');
			idInstitucion  = idRegistros.substring(0,index);
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			idPersona  = idRegistros.substring(0,index);
			idRegistros = idRegistros.substring(index+2);
			index = idRegistros.indexOf('||');
			apareceenRedAbogacia = idRegistros.substring(0,index);
 		   	datos = datos +"idPersona=="+idPersona + "##idInstitucion==" +idInstitucion+"##idTipoPersonas==" +<%=colegiado%>+"##idTipoInforme==CENSO%%%";
			
			
		}
		numElementosSeleccionados =  ObjArray.length; 
		if (datos == '') {
			alert ('<siga:Idioma key="general.message.seleccionar"/>');
			return;
		}
			
		if(numElementosSeleccionados>50){
			document.InformesGenericosForm.descargar.value = '0';
		}
		else{
			document.InformesGenericosForm.descargar.value = '1';
		}
		document.InformesGenericosForm.datosInforme.value=datos;
		var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
		if (arrayResultado==undefined||arrayResultado[0]==undefined){
		   		
	   	} 
	   	else {
	   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
	   		if(confirmar){
	   			var idEnvio = arrayResultado[0];
			    var idTipoEnvio = arrayResultado[1];
			    var nombreEnvio = arrayResultado[2];				    
			    
			   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
			   	document.DefinirEnviosForm.modo.value='editar';
			   	document.DefinirEnviosForm.submit();
	   		}
	   	}
			
			
			
      	    					
					
				
		}
	
		function accionGenerarExcels(){
   			sub();			
			datos = "";		
			for (i = 0; i < ObjArray.length; i++) {
				var idRegistros = ObjArray[i];
				index = idRegistros.indexOf('||');
				idInstitucion  = idRegistros.substring(0,index);
				idRegistros = idRegistros.substring(index+2);
				index = idRegistros.indexOf('||');
				idPersona  = idRegistros.substring(0,index);
				idRegistros = idRegistros.substring(index+2);
				index = idRegistros.indexOf('||');
				apareceenRedAbogacia = idRegistros.substring(0,index);
				datos = datos +	idPersona + "," +idInstitucion +",<%=colegiado%>#";			
			}		
			if (datos == '') {				
				alert ('<siga:Idioma key="general.message.seleccionar"/>');
				fin();
				return;
			}			
			document.forms[0].tablaDatosDinamicosD.value = datos;
			document.forms[0].modo.value ='generaExcel';		
			//alert("datosPaginador:"+document.forms[0].datosPaginador )
			//alert("registrosSeleccionados:"+document.forms[0].registrosSeleccionados)
			//alert("si:"+document.forms[0].datosPaginador)
			document.forms[0].submit();		
			fin();	
   		}
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->

		<siga:ConjBotonesAccion botones="V,COM,GX" />

	<!-- FIN: BOTONES REGISTRO -->

			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
