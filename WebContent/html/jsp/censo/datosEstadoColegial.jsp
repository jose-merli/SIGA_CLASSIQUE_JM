<!-- datosEstadoColegial.jsp -->
<!-- 
	 Permite editar los estados colegiales
	 VERSIONES:
	 miguel.villegas 13-01-2005
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="java.lang.*"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	String nextModo = request.getAttribute("modelo").toString(); // Obtengo la operacion (modificar o insertar)a realizar
	UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");
	boolean bOcultarHistorico = user.getOcultarHistorico();
	
	String idPersona="";
	String idInstitucion="";
	String fechaEstado="";	
	String observacion="";

	// Datos del usuario
	String nombre=(String)request.getAttribute("NOMBRE"); // Obtengo el nombre completo de la persona
	String numero=(String)request.getAttribute("NUMERO"); // Obtengo el numero de colegiado de la persona		

	ArrayList vSel = new ArrayList(); // 
	CenDatosColegialesEstadoBean info = new CenDatosColegialesEstadoBean();

	String idEstadoUltimo = (String)request.getAttribute("ID_ESTADO_ULTIMO");

	// Obtener informacion para rellenar en caso de modificacion o consulta
	if (nextModo.equalsIgnoreCase("modificar")){
		// Informacion sobre registro datos estado
		info = (CenDatosColegialesEstadoBean)request.getAttribute("container");
		vSel.add(info.getIdEstado().toString());				              	
		idPersona=info.getIdPersona().toString(); // Obtengo el id de la persona
		idInstitucion=info.getIdInstitucion().toString(); // Obtengo el id de la institucion a la que pertenece		
		
		fechaEstado=info.getFechaEstado();
		fechaEstado=GstDate.getFormatedDateShort("",fechaEstado);
		observacion=info.getObservaciones();		
	} else {
		info = (CenDatosColegialesEstadoBean)request.getAttribute("container");	
		if(info.getFechaEstado()!=null)
			fechaEstado=info.getFechaEstado();
		// Mas datos del usuario
		idPersona=(String)request.getAttribute("IDPERSONA"); // Obtengo el id de la persona
		idInstitucion=(String)request.getAttribute("IDINSTITUCION"); // Obtengo el id de la institucion a la que pertenece
				
	}
	
	
	
	String	botones="C,Y,R";
	
	
%>	

<html>
<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
		<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
		
		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="DatosColegialesForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<!--siga:Titulo 
			titulo="censo.busquedaHistorico.literal.titulo1" 
			localizacion="censo.busquedaHistorico.literal.titulo1"/-->
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="titulitosDatos" cellspacing="0" heigth="32">
			<tr>
				<td id="titulitos" class="titulosPeq">
					<siga:Idioma key="censo.consultaDatosColegiales.literal.cabecera2"/> &nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%>&nbsp;
				    <%if(!numero.equalsIgnoreCase("")){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%} 
					else {%>
					   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>
				</td>
			</tr>
		</table>
		<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
		<!-- dentro de esta capa se tienen que situar los diferentes componentes 
			 que se van a mostrar, para que quepen dentro de la ventana.
			 Los elementos que copieis dentro, que tengan el estilo 
			 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
		-->

			<!-- INICIO: CAMPOS -->
			<!-- Zona de campos de busqueda o filtro -->

			<table  class="tablaCentralCamposPeque"  align="center">

				<html:form action="/CEN_DatosColegiales.do" method="POST" target="submitArea">
					<html:hidden property="modo" value = "<%=nextModo%>"/>
					<html:hidden property="motivo" value=""/>
					<!-- por algún motivo aparece en el validation, pero no recuerdo haberlo puesto y a 22/01/05 no aparece en el -->
					<!-- lo incluyo para que "pase" -->
					<html:hidden property="numColegiado" value=""/>
					<!-- fin "pase" -->
					<html:hidden property="idPersona" value="<%=idPersona%>"/>
					<html:hidden property="idInstitucion" value="<%=idInstitucion%>"/>
					<html:hidden property = "actionModal" value = ""/>
				
					<tr>				
						<td>
						<siga:ConjCampos leyenda="censo.consultaDatosColegiales.leyenda">
							<table class="tablaCampos" border="0" align="center">
								<!-- FILA -->
									<tr>														
										<td class="labelText">
											<siga:Idioma key="censo.consultaDatosColegiales.literal.estado"/>&nbsp;(*)
										</td>				
										<td>
											<% 
											if (nextModo.equalsIgnoreCase("insertar") ){%>	
												<siga:ComboBD nombre = "cmbEstadoColegial" tipo="cmbEstadoColegial" clase="boxCombo" obligatorio="true"/>
											<% } else { %>
												<% if (vSel.isEmpty()){int i=0;%>
													<siga:ComboBD nombre = "cmbEstadoColegial" tipo="cmbEstadoColegial" clase="boxConsulta" elementoSel="<%=i%>" obligatorio="true" readOnly="true"/>
												<% } else { %>
													<siga:ComboBD nombre = "cmbEstadoColegial" tipo="cmbEstadoColegial"   clase="boxConsulta" elementoSel="<%=vSel%>" obligatorio="true" readOnly="true"/>
													<% } %>											
											<% } %>								
										</td>	
									</tr>	
								<!-- FILA -->
									<tr>														
										<td class="labelText">
											<siga:Idioma key="censo.consultaDatosGenerales.literal.fechaEstado"/>&nbsp;(*)
										</td>				
										<td>
										<% if (nextModo.equalsIgnoreCase("insertar") || nextModo.equalsIgnoreCase("modificar")){%>										
			  								<html:text property="fechaEstado" size="10" styleClass="box" value="<%=fechaEstado%>" readOnly="true"></html:text>
											<a href='javascript://' onClick="return showCalendarGeneral(fechaEstado);" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
												<img src="<%=app%>/html/imagenes/calendar.gif" alt="gratuita.listadoCalendario.literal.seleccionarFecha"  border="0">
											</a>																																
										<% } else { %>		
											<% if (fechaEstado.equalsIgnoreCase("")){ %>									 	
												<html:text property="fechaEstado" size="10" styleClass="boxConsulta" value="" readOnly="true"></html:text>
											<% }else{ %>										 										 	
												<html:text property="fechaEstado" size="20" styleClass="boxConsulta" value="<%=fechaEstado%>" readOnly="true"></html:text>
											<% } %>																	
										<% } %>																																																															
										</td>	
									</tr>														
								<!-- FILA -->
									<tr>				
										<td class="labelText">
											<siga:Idioma key="censo.consultaDatosColegiales.literal.observaciones"/>&nbsp;
										</td>				
										<td>	
										
										<% if (nextModo.equalsIgnoreCase("insertar")){ %>
 												<html:textarea cols="50" rows="5" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" property="observaciones" styleClass="box"></html:textarea> 
										<% } else { %>
					  						<% if (nextModo.equalsIgnoreCase("modificar")){%>
  					  						       <html:textarea cols="50" rows="5" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" property="observaciones" styleClass="box" value="<%=observacion%>"></html:textarea> 
				  							 	<% } else{ %>
   				  							 	   <html:textarea cols="50" rows="5" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" property="observaciones" styleClass="boxConsulta" value="<%=observacion%>" readOnly="true" ></html:textarea> 
												<% } %>				  					 				  					 				  					 
							  	    	<% } %>				
																						
						  				</td>
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
		
			<siga:ConjBotonesAccion botones="<%=botones%>" modo="<%=nextModo%>"  modal="P"/>
			
			<!-- FIN: BOTONES REGISTRO -->

	
			<!-- INICIO: SCRIPTS BOTONES -->
			<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
			<script language="JavaScript">

			<!-- Asociada al boton GuardarCerrar -->
			function accionGuardarCerrar() 
			{			
				sub();		
				if (validateDatosColegialesForm(document.DatosColegialesForm)){
				
					if (document.forms[0].cmbEstadoColegial.value == '<%=idEstadoUltimo%>') {
						alert ("<siga:Idioma key='censo.consultaDatosColegiales.literal.estado.error'/>");
						fin();
						return;
					}
				
					<% if (!bOcultarHistorico) { %>
							var datos = showModalDialog("/SIGA/html/jsp/general/ventanaMotivoHistorico.jsp","","dialogHeight:230px;dialogWidth:520px;help:no;scroll:no;status:no;");
					<% } else { %>
							var datos = new Array();
							datos[0] = 1;
							datos[1] = "";
					<% } %>
				
					if (datos[0] == 1) { // Boton Guardar
						document.forms[0].motivo.value = datos[1];
						document.forms[0].target = "submitArea";
						document.forms[0].submit();
					}else{
						fin();
						return false;
					}	
				}	else{
					fin();
  					return false;
				}
			}

			<!-- Asociada al boton Cerrar -->
			function accionCerrar() 
			{		
				// esta funcion cierra la ventana y devuelve 
				// un valor a la ventana padre (USAR SIEMPRE)
				top.cierraConParametros("NORMAL");
			}

			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				document.forms[0].reset();
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

