<!-- resultadosBusquedaCensoModal.jsp -->
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
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.censo.form.BusquedaCensoForm"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.Utilidades.paginadores.PaginadorBind"%>
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
	BusquedaCensoForm formulario = (BusquedaCensoForm) request.getSession().getAttribute("busquedaCensoModalForm");
	//Vector resultado = (Vector) request.getAttribute("CenResultadoBusquedaClientesModal");
	Vector resultado = null;
	/** PAGINADOR **/
	

	String action = app + "/CEN_BusquedaCensoModal.do";

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
			<html:javascript formName="/CEN_BusquedaCensoModal.do" staticJavascript="false" />  
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
						document.busquedaCensoModalForm.idPersona.value = oculto.value;
					}else if(j=='2'){
						document.busquedaCensoModalForm.idInstitucion.value = oculto.value;
					
					}else if(j=='3'){
						document.busquedaCensoModalForm.numeroColegiado.value = oculto.value;
					} else if(j=='4'){
					  	document.busquedaCensoModalForm.nif.value = oculto.value;
					} else if(j=='5'){
						document.busquedaCensoModalForm.nombre.value = oculto.value;
					} else if(j=='6'){
						document.busquedaCensoModalForm.apellido1.value = oculto.value;
					} else if(j=='7'){
					 	document.busquedaCensoModalForm.apellido2.value = oculto.value;
					}else if(j=='8'){
						document.busquedaCensoModalForm.direccion.value = oculto.value;
					}else if(j=='9'){
						document.busquedaCensoModalForm.poblacion.value = oculto.value;
					} else if(j=='10'){
					  	document.busquedaCensoModalForm.provincia.value = oculto.value;
					} else if(j=='11'){
						document.busquedaCensoModalForm.codPostal.value = oculto.value;
					} else if(j=='12'){
						document.busquedaCensoModalForm.mail.value = oculto.value;
					}else if(j=='13'){
						document.busquedaCensoModalForm.telefono.value = oculto.value;
					}else if(j=='14'){
						document.busquedaCensoModalForm.sexo.value = oculto.value;
					}else if(j=='15'){
						document.busquedaCensoModalForm.tratamiento.value = oculto.value;
					}else if(j=='16'){
						document.busquedaCensoModalForm.fax1.value = oculto.value;
					}else if(j=='17'){
						document.busquedaCensoModalForm.pais.value = oculto.value;
					}  
							
					j++;
				}
				datos.value = datos.value + "%";
		    	document.busquedaCensoModalForm.modo.value = "enviarClienteCenso";
			   	document.busquedaCensoModalForm.submit();
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
		<html:form action="/CEN_BusquedaCensoModal" method="POST" target="submitArea" style="display:none">
			<!-- Campo obligatorio -->
			<html:hidden property ="modo" value = "" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="actionModal" value="">
			<html:hidden property="numeroColegiado" value=""/>
			<html:hidden property="idInstitucion" value=""/>
			<html:hidden property="idPersona" value=""/>
			<html:hidden property="nif" value=""/>
			<html:hidden property="nombre" value=""/>
			<html:hidden property="apellido1" value=""/>
			<html:hidden property="apellido2" value=""/>
			<html:hidden property="direccion" value=""/>
			<html:hidden property="poblacion" value=""/>
			<html:hidden property="provincia" value=""/>
			<html:hidden property="mail" value=""/>
			<html:hidden property="telefono" value=""/>
			<html:hidden property="codPostal" value=""/>	
			<html:hidden property="sexo" value=""/>
			<html:hidden property="tratamiento" value=""/>	
		    <html:hidden property="fax1" value=""/>
		    <html:hidden property="pais" value=""/>
			
		</html:form>
		<%
			String nombresCol = "";
			String tamanosCol = "";
			nombresCol = "censo.busquedaClientes.literal.colegio,censo.busquedaClientesAvanzada.literal.nif,censo.busquedaClientesAvanzada.literal.nColegiado,gratuita.turnos.literal.apellidosSolo,censo.busquedaClientesAvanzada.literal.nombre,censo.busquedaClientesAvanzada.literal.situacion,censo.busquedaClientesAvanzada.literal.residente,censo.busquedaClientesAvanzada.literal.fechaIngreso,";
			tamanosCol = "10,9,10,15,15,10,10,15,5";

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
		<bean:define id="letradoList" name="letradoList"
			scope="request" type="java.util.Vector"></bean:define>
		<bean:define id="paginaSeleccionada" name="paginaSeleccionada"
			scope="request"></bean:define>
			
		<bean:define id="totalRegistros" name="totalRegistros" scope="request"></bean:define>
		<bean:define id="registrosPorPagina" name="registrosPorPagina"
			scope="request"></bean:define>
		<bean:define id="si" name="si"
			scope="request" type="String"></bean:define>
		<bean:define id="no" name="no"
			scope="request" type="String"></bean:define>
		<c:choose>
		<c:when test="${empty letradoList}">
			<tr>
				<td colspan="13" class="titulitos" style="text-align: center"><siga:Idioma
					key="messages.noRecordFound" /></td>
			</tr>
		</c:when>
		<c:otherwise>
			
		
		
		
		<!-- INICIO: ZONA DE REGISTROS -->
		<!-- Aqui se iteran los diferentes registros de la lista -->
		<%	
			for (int i = 0; i < letradoList.size(); i++) {
				Row fila = (Row) letradoList.elementAt(i);
			
				FilaExtElement[] elems = null;
				elems = new FilaExtElement[1];
				elems[0] = new FilaExtElement("seleccionar","seleccionar", SIGAConstants.ACCESS_READ);

				// recorro el resultado
				
					//Row letrado = (Row) resultado.elementAt(i);
					Hashtable registro = (Hashtable) fila.getRow();
		
					String cont = new Integer(i + 1).toString();
		
					// permisos de acceso
					String modo = "";
		
					String idPersona = (registro.get("ID_LETRADO") == null 
							|| ((String) registro.get("ID_LETRADO")).equals("")) ? "&nbsp;" : (String) registro.get("ID_LETRADO");
					UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
					
					// calculo de campos
					String apellidos = UtilidadesString.mostrarDatoJSP(registro.get("APELLIDO1"));
					String apellido2 ="";
					if(registro.get("APELLIDO2")!=null)
						 apellido2 = UtilidadesString.mostrarDatoJSP(registro.get("APELLIDO2"));
					String nombre = UtilidadesString.mostrarDatoJSP(registro.get("NOMBRE"));
					String nif = UtilidadesString.mostrarDatoJSP(registro.get("NUM_DOC"));
					String descripcion = "";
					String ncolegiado = "";
					String estadoColegial = "";
					descripcion = UtilidadesString.mostrarDatoJSP((String) registro.get("DESCRIPCION"));
					

					ncolegiado = (String)registro.get("NUM_COLEGIADO");
					estadoColegial = si;
					if(UtilidadesHash.getString(registro,"EJERCIENTE").equals("n")){
						estadoColegial = no;
					}else if(UtilidadesHash.getString(registro,"EJERCIENTE").equalsIgnoreCase("No Colegiado")){
						estadoColegial = "No Colegiado";
					}
					
					String institucion = (String)registro.get("ID_COLEGIO");
					String mail = UtilidadesString.mostrarDatoJSP(registro.get("MAIL"));
					String pais = UtilidadesString.mostrarDatoJSP(registro.get("PAIS"));
					
					String poblacion = ""; 
					if(pais.equalsIgnoreCase(ClsConstants.ID_PAIS_ESPANA)){
						poblacion = UtilidadesString.mostrarDatoJSP(registro.get("IDPOBLACION"));
					}else{
						poblacion = UtilidadesString.mostrarDatoJSP(registro.get("POBLACION"));
					}
					String dir = UtilidadesString.mostrarDatoJSP(registro.get("DIR_PROFESIONAL"));
					String provincia = UtilidadesString.mostrarDatoJSP(registro.get("PROVINCIA"));
					String cp = UtilidadesString.mostrarDatoJSP(registro.get("COD_POSTAL"));
					String fechaIni = UtilidadesString.mostrarDatoJSP(registro.get("FECHA_ALTA"));	
					String residente = si;
					if(UtilidadesHash.getString(registro,"RESIDENCIA").equals("n"))
						residente = no;
					else if(UtilidadesHash.getString(registro,"RESIDENCIA").equalsIgnoreCase("No Colegiado")){
						residente = "-";
					}
					
					
					String telefono = UtilidadesString.mostrarDatoJSP(registro.get("TELEFONO"));
					String sexo = UtilidadesString.mostrarDatoJSP(registro.get("SEXO"));
					String tratamiento = UtilidadesString.mostrarDatoJSP(registro.get("TRATAMIENTO"));
					String fax1 = UtilidadesString.mostrarDatoJSP(registro.get("FAX"));
					
			 		%>
						<!-- REGISTRO  -->
						<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
							 que la lista contiene realmente 3 columnas: Las de datos mas 
							 la de botones de acción sobre los registos  -->
						
						<siga:FilaConIconos fila="<%=cont %>" botones="" modo="<%=modo %>" 
							elementos="<%=elems%>" visibleBorrado="no" visibleEdicion="no" 
							visibleConsulta="no" clase="listaNonEdit"
							pintarEspacio="no"
							>
							<td>
								<!-- campos hidden -->
								<input type="hidden" name="oculto<%=cont%>_1" value="<%=idPersona%>">
								<input type="hidden" name="oculto<%=cont%>_2" value="<%=institucion%>">
								<input type="hidden" name="oculto<%=cont%>_3" value="<%=ncolegiado%>">
								<input type="hidden" name="oculto<%=cont%>_4" value="<%=nif%>">
								<input type="hidden" name="oculto<%=cont%>_5" value="<%=nombre%>">
								<input type="hidden" name="oculto<%=cont%>_6" value="<%=apellidos%>">
								<input type="hidden" name="oculto<%=cont%>_7" value="<%=apellido2%>">
								<input type="hidden" name="oculto<%=cont%>_8" value="<%=dir%>">
								<input type="hidden" name="oculto<%=cont%>_9" value="<%=poblacion%>">
								<input type="hidden" name="oculto<%=cont%>_10" value="<%=provincia%>">
								<input type="hidden" name="oculto<%=cont%>_11" value="<%=cp%>">
								<input type="hidden" name="oculto<%=cont%>_12" value="<%=mail%>">
								<input type="hidden" name="oculto<%=cont%>_13" value="<%=telefono%>">
								<input type="hidden" name="oculto<%=cont%>_14" value="<%=sexo%>">
								<input type="hidden" name="oculto<%=cont%>_15" value="<%=tratamiento%>">
								<input type="hidden" name="oculto<%=cont%>_16" value="<%=fax1%>">		
								<input type="hidden" name="oculto<%=cont%>_17" value="<%=pais%>">								
								<%=descripcion%>
							</td>
							<td>
								<%=nif%>
							</td>
							<td>
								<%=ncolegiado%>
							</td>
							<td>
								<%=apellidos%>
							</td>
							<td>
								<%=nombre%>
							</td>
						
							<td>
								<%=estadoColegial%>
							</td>
							<td>
								<%=residente%>
							</td>
							<td>
								<%=fechaIni%>
							</td>
						</siga:FilaConIconos>		
						<!-- FIN REGISTRO -->
					<%} %>
					</c:otherwise>
					</c:choose>
			<!-- FIN: ZONA DE REGISTROS -->
					
		</siga:TablaCabecerasFijas>
		<!-- Pintamos la paginacion-->		
<siga:Paginador totalRegistros="${totalRegistros}"
	registrosPorPagina="${registrosPorPagina}"
	paginaSeleccionada="${paginaSeleccionada}" idioma="${usrbean.language}"
	modo="buscarTodosModal" clase="paginator" 
	divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:26px; left: 0px"
	
	distanciaPaginas="" action="<%=action%>"
	/>
		    
		<!-- FIN: LISTA DE VALORES -->		
	
		
		<!------------------------------------------->
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>
