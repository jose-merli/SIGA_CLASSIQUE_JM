<!-- resultadosBusquedaClientesModal.jsp -->
<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
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
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
	String idioma = usrbean.getLanguage().toUpperCase();
	String busquedaSancion = (String) request.getAttribute("busquedaSancion");

	// locales
	BusquedaClientesForm formulario = (BusquedaClientesForm) request.getSession().getAttribute("busquedaClientesModalForm");
	//Vector resultado = (Vector) request.getAttribute("CenResultadoBusquedaClientesModal");
	Vector resultado = null;
	/** PAGINADOR **/
	String paginaSeleccionada = "";
	String totalRegistros = "";
	String registrosPorPagina = "";
	HashMap hm = new HashMap();

	String idPaginador = (String) request.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(idPaginador) != null) {
		hm = (HashMap) ses.getAttribute(idPaginador);

		if (hm!=null && hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (Vector) hm.get("datos");
			PaginadorBind paginador = (PaginadorBind) hm.get("paginador");
			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());
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

	String action = app + "/CEN_BusquedaClientesModal.do";

	/**************/
	String titu = "censo.busquedaClientes.literal.titulo";
%>

<html>
	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="/CEN_BusquedaClientesModal.do" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="<%=titu %>" 
			localizacion="<%=titu %>"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
		<!-- SCRIPTS LOCALES -->
		<script language="JavaScript">
			function seleccionar(fila) {
				var datos;
				var j;
				var tabla;
				var flag = true;
	
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = ""; 
				tabla = document.getElementById('tablaDatos');
				j = 1;
				while (flag) {
					var aux = 'oculto' + fila + '_' + j;
					var oculto = document.getElementById(aux);
				  	if (oculto == null)  { 
					  	flag = false; 
				  	} else { 
					  	datos.value = datos.value + oculto.value + ','; 
					}
					if(j=='1'){
						document.busquedaClientesModalForm.idPersona.value = oculto.value;
					}else if(j=='2'){
						document.busquedaClientesModalForm.idInstitucion.value = oculto.value;
					
					}else if(j=='3'){
						document.busquedaClientesModalForm.numeroColegiado.value = oculto.value;
					} else if(j=='4'){
					  	document.busquedaClientesModalForm.nif.value = oculto.value;
					} else if(j=='5'){
						document.busquedaClientesModalForm.nombrePersona.value = oculto.value;
					} else if(j=='6'){
						document.busquedaClientesModalForm.apellido1.value = oculto.value;
					} else if(j=='7'){
					 	document.busquedaClientesModalForm.apellido2.value = oculto.value;
					}
					j++;
				}
				datos.value = datos.value + "%";
		    	document.busquedaClientesModalForm.modo.value = "enviarCliente";
			   	document.busquedaClientesModalForm.submit();
			}
	
			function lopd(fila) {
				alert('<siga:Idioma key="general.boton.lopd"/>');
			}
		</script>
	</head>

	<body class="tablaCentralCampos">
		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_BusquedaClientesModal" method="POST" target="submitArea" style="display:none">
			<!-- Campo obligatorio -->
			<html:hidden property ="modo" styleId ="modo" value = "" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" id="actionModal"  name="actionModal" value="" />
			<html:hidden property="numeroColegiado" styleId="numeroColegiado" value=""/>
			<html:hidden property="idInstitucion" styleId="idInstitucion" value=""/>
			<html:hidden property="idPersona" styleId="idPersona" value=""/>
			<html:hidden property="nif" styleId="nif" value=""/>
			<html:hidden property="nombrePersona" styleId="nombrePersona" value=""/>
			<html:hidden property="apellido1" styleId="apellido1" value=""/>
			<html:hidden property="apellido2" styleId="apellido2" value=""/>
		</html:form>
		<%
			String nombresCol = "";
			String tamanosCol = "";
			if (busquedaSancion != null && busquedaSancion.equals("1")) {
				nombresCol = "censo.busquedaClientesAvanzada.literal.nif,gratuita.turnos.literal.apellidosSolo,censo.busquedaClientesAvanzada.literal.nombre,censo.busquedaClientes.literal.institucion,censo.busquedaClientesAvanzada.literal.estadoColegial,";
				tamanosCol = "9,20,15,10,15,10,20";
			} else {
				nombresCol = "censo.busquedaClientesAvanzada.literal.nif,censo.busquedaClientesAvanzada.literal.nColegiado,gratuita.turnos.literal.apellidosSolo,censo.busquedaClientesAvanzada.literal.nombre,censo.busquedaClientes.literal.institucion,censo.busquedaClientesAvanzada.literal.estadoColegial,";
				tamanosCol = "9,5,15,15,10,15,10,20";
			}
			// cliente colegiado o  no
		%>
		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="<%=nombresCol %>"
		   tamanoCol="<%=tamanosCol %>"
		   alto="100%"
		   ajustePaginador="true"	
		   ajusteBotonera="true"			   
		>
		<!-- INICIO: ZONA DE REGISTROS -->
		<!-- Aqui se iteran los diferentes registros de la lista -->
		<%
			if (resultado == null || resultado.size() == 0) {
				%>			
			 		<br><br>
			   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
			 		<br><br>	 		
				<%
			} else {
				FilaExtElement[] elems = null;
		
				// recorro el resultado
				for (int i = 0; i < resultado.size(); i++) {
					Row fila = (Row) resultado.elementAt(i);
					Hashtable registro = (Hashtable) fila.getRow();
					boolean isAplicarLOPD = (String) registro.get(CenClienteBean.C_NOAPARECERREDABOGACIA) != null
							&& ((String) registro.get(CenClienteBean.C_NOAPARECERREDABOGACIA)).equals(ClsConstants.DB_TRUE);
					if (isAplicarLOPD) {
						elems = new FilaExtElement[2];
						elems[1] = new FilaExtElement("lopd", "lopd",SIGAConstants.ACCESS_READ);
						elems[0] = new FilaExtElement("seleccionar","seleccionar", SIGAConstants.ACCESS_READ);
					} else {
						elems = new FilaExtElement[1];
						elems[0] = new FilaExtElement("seleccionar","seleccionar", SIGAConstants.ACCESS_READ);
					}
		
					String cont = new Integer(i + 1).toString();
		
					// permisos de acceso
					String modo = "";
		
					// el id del user.getlocation
					String idInstitucion = usrbean.getLocation();
					String institucion = CenVisibilidad.getAbreviaturaInstitucion(idInstitucion);
					if(registro.get("IDINSTITUCION")!=null && !registro.get("IDINSTITUCION").toString().trim().equals(""))
						institucion = CenVisibilidad.getAbreviaturaInstitucion(registro.get("IDINSTITUCION").toString());
			
		
					String idPersona = (registro.get(CenColegiadoBean.C_IDPERSONA) == null 
							|| ((String) registro.get(CenColegiadoBean.C_IDPERSONA)).equals("")) ? "&nbsp;" : (String) registro.get(CenColegiadoBean.C_IDPERSONA);
					UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
					if (user.getLocation().equals(idInstitucion)) {
						modo = "edicion";
					} else {
						modo = "consulta";
					}
		
					// calculo de campos
					String auxApellido1 = UtilidadesString.controlNulos(registro.get(CenPersonaBean.C_APELLIDOS1));
					String auxApellido2 = UtilidadesString.controlNulos(registro.get(CenPersonaBean.C_APELLIDOS2));
					String auxNombre = UtilidadesString.controlNulos(registro.get(CenPersonaBean.C_NOMBRE));
					String apellido1 = UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_APELLIDOS1));
					String apellido2 = (registro.get(CenPersonaBean.C_APELLIDOS2)!=null && !registro.get(CenPersonaBean.C_APELLIDOS2).equals(""))?UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_APELLIDOS2)):"";
					String nombre = UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_NOMBRE));
					String nif = UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_NIFCIF));
					String fechaNacimiento = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),
																										registro.get(CenPersonaBean.C_FECHANACIMIENTO)));
		
					
					String ncolegiado = (String)registro.get(CenColegiadoBean.C_NCOLEGIADO);
					String estadoColegial = "";
					String ncomunitario = (String)registro.get(CenColegiadoBean.C_NCOMUNITARIO);
					if(ncomunitario!=null && !ncomunitario.equals("") )
						ncolegiado = UtilidadesString.mostrarDatoJSP(ncomunitario);
					else if(ncolegiado!=null && !ncolegiado.equals(""))
						ncolegiado = UtilidadesString.mostrarDatoJSP(ncolegiado);
					estadoColegial = UtilidadesString.mostrarDatoJSP(UtilidadesMultidioma.getDatoMaestroIdioma((String) registro.get("ESTADOCOLEGIAL"), usrbean));					
			 		%>
						<!-- REGISTRO  -->
						<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
							 que la lista contiene realmente 3 columnas: Las de datos mas 
							 la de botones de acción sobre los registos  -->
						
						<siga:FilaConIconos fila="<%=cont %>" botones="" modo="<%=modo %>" 
							elementos="<%=elems%>" visibleBorrado="no" visibleEdicion="no" 
							visibleConsulta="no" clase="listaNonEdit"
							pintarEspacio="no">
							<td>
								<!-- campos hidden -->
								<input type="hidden" id="oculto<%=cont%>_1" name="oculto<%=cont%>_1" value="<%=idPersona%>">
								<input type="hidden" id="oculto<%=cont%>_2" name="oculto<%=cont%>_2" value="<%=idInstitucion%>">
								<input type="hidden" id="oculto<%=cont%>_3" name="oculto<%=cont%>_3" value="<%=ncolegiado%>">
								<input type="hidden" id="oculto<%=cont%>_4" name="oculto<%=cont%>_4" value="<%=nif%>">
								<input type="hidden" id="oculto<%=cont%>_5" name="oculto<%=cont%>_5" value="<%=nombre%>">
								<input type="hidden" id="oculto<%=cont%>_6" name="oculto<%=cont%>_6" value="<%=apellido1%>">
								<input type="hidden" id="oculto<%=cont%>_7" name="oculto<%=cont%>_7" value="<%=apellido2%>">
								<%=nif%>
							</td>
							<%
								if (busquedaSancion == null || busquedaSancion.equals("0")) {
									%>
										<td>
											<%=UtilidadesString.mostrarDatoJSP(registro.get(CenColegiadoBean.C_NCOLEGIADO))%>
										</td>
									<%
								}
							%>
							<td>
								<%=apellido1 + " " + apellido2%>
							</td>
							<td>
								<%=nombre%>
							</td>
							<td>
								<%=institucion%>
							</td>
							<td>
								<%=estadoColegial%>
							</td>
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
		    
		<!-- FIN: LISTA DE VALORES -->		
		<!-- Pintamos la paginacion-->	
		<%
			if (hm!=null && hm.get("datos") != null && !hm.get("datos").equals("")) {
				%>
					<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarModal"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
		 		<%
		 	}
		 %>			
		<!------------------------------------------->
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>
