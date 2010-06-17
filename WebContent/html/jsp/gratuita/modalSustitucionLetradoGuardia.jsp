<!-- modalSustitucionLetrado.jsp-->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.beans.ScsGuardiasColegiadoBean"%>
<%@ page import="com.siga.beans.CenColegiadoBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.tlds.TagBusquedaSJCS"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	
	String turno = (String)request.getAttribute("nombreTurno");
	String guardia = (String)request.getAttribute("nombreGuardia");
	String fechaInicio = request.getAttribute("fechaInicio") != null?(String)request.getAttribute("fechaInicio"):"";
	String fechaFin = request.getAttribute("fechaFin") != null?(String)request.getAttribute("fechaFin"):"";
	String origen = (String)request.getAttribute("origen");
	String idTurno = (String)request.getAttribute("idTurno");
	String idGuardia = (String)request.getAttribute("idGuardia");
	String idPersona = (String)request.getAttribute("idPersona");
	String sustitucion = (String)request.getAttribute("sustitucion");
	String action = (String)request.getAttribute("action");
	String nombreForm = "";
	if(origen.equalsIgnoreCase("CALENDARIOGUARDIAS"))
		nombreForm="PermutasForm";
	else
		nombreForm = "DefinirGuardiasTurnosForm";	
	
	Date hoy = new Date();
	SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
	String fechaHoy = sd.format(hoy);
%>

<%@page import="java.util.Properties"%>
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="PermutasForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

</head>

<body >

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				Sustituci�n de Letrado
			</td>
		</tr>
	</table>
	

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposPeque" align="center" border="0">
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="<%=action%>" method="post">
		<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
		<html:hidden property = "modo" value = "modificar"/>
		
		<% if(origen.equalsIgnoreCase("CALENDARIOGUARDIAS")) { %>
			<html:hidden property = "idCalendarioGuardias" />
			<% } %>
		<html:hidden property = "idTurno" value="<%=idTurno%>"/>
		<html:hidden property = "idGuardia" value="<%=idGuardia%>"/>
		<html:hidden property = "idPersonaSolicitante" value="<%=idPersona%>"/>
		<html:hidden property = "idPersona" />
		<html:hidden property = "idInstitucion" />
		<html:hidden property = "sustituta" value="<%=sustitucion%>"/>
		<input type="hidden" name="checkSalto" value="">
		<input type="hidden" name="checkCompensacion" value="">
			
		<!-- INICIO: CAMPOS DEL REGISTRO -->
	
		<tr>
			<td>
				<!-- SUBCONJUNTO DE DATOS -->
				<siga:ConjCampos leyenda="gratuita.modalSustitucionLetrado.literal.informacionGuardia">
					<table class="tablaCampos" align="left" border="0" width="100%">
						<tr>
									<td class="labelText">
										<siga:Idioma key="gratuita.modalSustitucionLetrado.literal.turno"/>:			
									</td>
									<td>
										<input type="text" name="turno"  maxlength="20" class="boxConsulta" value="<%=turno%>" readOnly="true" style="width:220px">
									</td>		
									<td class="labelText">
										<siga:Idioma key="gratuita.modalSustitucionLetrado.literal.guardia"/>:
										
									</td>
									<td >
										<input type="text" name="guardia"   maxlength="20" class="boxConsulta" value="<%=guardia%>" readOnly="true"  style="width:220px">
									</td>	
						</tr>
						<!-- SI A ESTA PAGINA LLEGAMOS DESDE LA PESTA�A DE CALENDARIO DE GUARDIAS DE LA FICHA COLEGIAL MOSTRAMOS 
						 LA FECHA DE INICIO Y LA FECHA DE FIN //-->
						
						<% if(origen.equalsIgnoreCase("CALENDARIOGUARDIAS")) { %>
					
						<tr>
							<td class="labelText">
								<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.fechaInicio"/>:
							</td>
							<td >
								<html:text name="PermutasForm" property="fechaInicio" size="10" maxlength="10" styleClass="boxConsulta" value="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio)%>" readOnly="true"></html:text>
							</td>		
							<td class="labelText">
								<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.fechaFin"/>:
							</td>
							<td >
								<html:text name="PermutasForm" property="fechaFin" size="10" maxlength="10" styleClass="boxConsulta" value="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin)%>" readOnly="true"></html:text>
							</td>
						</tr>
					
						<% } %>		

					</table>
				</siga:ConjCampos>		
			</td>
		</tr>
		<tr>
			<td >
				
							<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.titulo"> 
								<table class="tablaCampos" border="0" >		
									<tr id="mifila">
										<td colspan="4"> 
											<html:hidden  property="flagSalto" value=""></html:hidden>
											<html:hidden  property="flagCompensacion" value=""></html:hidden>
											<html:hidden  property="fecha" value="<%=fechaHoy%>"></html:hidden>
											<siga:BusquedaSJCS nombre="<%=nombreForm%>" propiedad="seleccionLetrado" concepto="Guardia" operacion="Sustitucion"
												botones="M,A" campoTurno="idTurno" campoGuardia="idGuardia" campoColegiado="ncolegiado" campoNombreColegiado="nomColegiado"
												campoFecha="fecha" campoPersona="idPersona" campoFlagSalto="flagSalto" campoFlagCompensacion="flagCompensacion"
												campoCompensacion="compensacion" campoSalto="salto" mostrarNColegiado="true" mostrarNombreColegiado="true"
												modo="editar"
											/>
										</td> 
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key='gratuita.busquedaEJG.literal.numeroColegidado'/>
										</td>		
										<td>
											<input type="text" name="ncolegiado" class="boxConsulta" readOnly value="" size="6">
										</td>
										<td class="labelText">
											<siga:Idioma key='FactSJCS.listadoRetencionesJ.literal.nombreColegiado'/>
										</td>
										<td>
											<input type="text" name="nomColegiado" class="boxConsulta" readOnly value="" size="50">
										</td>			
									</tr>							
									
										
								</table>
							</siga:ConjCampos>		
				
			</td>
		</tr>
	
	
								<% if(origen.equalsIgnoreCase("CALENDARIOGUARDIAS")) { %>
	    								<tr>			
	     									<td>
											<siga:ConjCampos leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.motivosSolicitud">
			  									<table class="tablaCampos" align="left" border="0" width="100%">
												<tr>
													<td class="labelText">
										 				<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.motivos"/> (*)			
													</td>
													<td >
														<html:textarea name="PermutasForm" property="comenSustitucion" onKeyDown="cuenta(this,250)" onChange="cuenta(this,250)" cols="80" rows="4" style="width:580"  styleClass="box" readOnly="false" ></html:textarea>
													</td>	
										
												</tr>
												</table>
				
		    								</siga:ConjCampos>	
											</td>		
									  </tr>	
									  
								<% }else {%>
								
								<tr>			
	     									<td>
											<siga:ConjCampos leyenda="gratuita.modalConsulta_DefinirCalendarioGuardia.literal.motivosSolicitud">
			  									<table class="tablaCampos" align="left" border="0" width="100%">
												<tr>
													<td class="labelText">
										 				<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.motivos"/> (*)			
													</td>
													<td >
														<html:textarea name="DefinirGuardiasTurnosForm" property="comenSustitucion" onKeyDown="cuenta(this,250)" onChange="cuenta(this,250)" cols="80" rows="4" style="width:580"  styleClass="box" readOnly="false" ></html:textarea>
													</td>	
										
												</tr>
												</table>
				
		    								</siga:ConjCampos>	
											</td>		
									  </tr>	
								
								<% }%>
								
										
	</table>






	
	</html:form>			
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<siga:ConjBotonesAccion botones="Y,C" modal="P"/>
	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		
	
	
		//Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			sub();
			//Chequeo que ha seleccionado un valor de la lista:
			//alert(document.forms[0].flagSalto.value);
			//alert(document.forms[0].flagCompensacion.value);
			
			
			
				
			
			
			
			if(document.forms[0].idPersona.value == "") {
				alert('<siga:Idioma key="gratuita.literal.sustitucionLetradoGuardia.selecSustituto"/>');
				fin();
				return false;
			} 
			else if(document.forms[0].idPersona.value == document.forms[0].idPersonaSolicitante.value){
					alert('<siga:Idioma key="gratuita.literal.sustitucionLetradoGuardia.coincide"/>');
					fin();
					return false;
			}
			if(document.<%=nombreForm%>.comenSustitucion.value == "") {
					alert('<siga:Idioma key="gratuita.literal.sustitucionLetradoGuardia.Motivos"/>');
					fin();
					return false;
			}	
				
			if(document.<%=nombreForm%>.comenSustitucion.value == "") {
					alert('<siga:Idioma key="gratuita.literal.sustitucionLetradoGuardia.Motivos"/>');
					fin();
					return false;
			}			
			else {
					if(document.forms[0].salto.checked)
						document.forms[0].checkSalto.value ="1";
					else
						document.forms[0].checkSalto.value ="0";
					if(document.forms[0].compensacion.checked)
						document.forms[0].checkCompensacion.value ="1";
					else
						document.forms[0].checkCompensacion.value ="0";	
					document.forms[0].modo.value = "insertarSustitucion";
					document.forms[0].target = "submitArea";							
					document.forms[0].submit();	
				
			}
			
			
		}

		//Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}		
		function refrescarLocal() 
		{		
			top.cierraConParametros("NORMAL");
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES -->
	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->

<!-- FIN ******* CAPA DE PRESENTACION ****** -->
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las p�ginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>