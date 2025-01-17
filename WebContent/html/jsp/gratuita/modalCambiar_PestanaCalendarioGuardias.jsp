<!DOCTYPE html>
<html>
<head>
<!-- modalCambiar_PestanaCalendarioGuardias.jsp-->

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
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.beans.ScsCabeceraGuardiasBean"%>
<%@ page import="com.siga.beans.CenColegiadoBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>

<!-- JSP -->
<% 
	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)ses.getAttribute("USRBEAN");	
	String profile[]=usr.getProfile();
	

	//Datos propios del jsp:
	Vector obj = (Vector) request.getAttribute("resultados");
	String accion = request.getAttribute("accion")==null?"":(String)request.getAttribute("accion");

	//Datos del solicitante:
	Hashtable solicitante = new Hashtable();
	solicitante = (Hashtable)request.getAttribute("SOLICITANTE");
	String fechaInicio = UtilidadesHash.getString(solicitante,"FECHAINICIO");
	String fechaFin = UtilidadesHash.getString(solicitante,"FECHAFIN");
	String idinstitucion = UtilidadesHash.getString(solicitante,"IDINSTITUCION");
	String idturno = UtilidadesHash.getString(solicitante,"IDTURNO");
	String idguardia = UtilidadesHash.getString(solicitante,"IDGUARDIA");
	String idcalendarioguardias = UtilidadesHash.getString(solicitante,"IDCALENDARIOGUARDIAS");
	String idpersona = UtilidadesHash.getString(solicitante,"IDPERSONA");
	String nombreYApellidos = UtilidadesHash.getString(solicitante,"NOMBREYAPELLIDOS");
	String numeroColegiado = UtilidadesHash.getString(solicitante,"NUMEROCOLEGIADO");
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="PermutasForm" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<script>
		function activarPermuta(fila) {
		}
	</script>
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.tituloTitular"/>
			</td>
		</tr>
	</table>
	
	<!-- Comienzo del formulario con los campos -->	
	<html:form action="/JGR_PestanaCalendarioGuardias.do" method="post">
		<html:hidden property = "usuMod" styleId="usuMod" value="<%=usr.getUserName()%>"/>
		<html:hidden property = "modo" styleId="modo" value = "modificar"/>		
		<html:hidden property = "idInstitucion" styleId="idInstitucion" value = "<%=idinstitucion%>"/>		
		<html:hidden property = "idTurnoSolicitante" styleId="idTurnoSolicitante" value = "<%=idturno%>"/>
		<html:hidden property = "idGuardiaSolicitante" styleId="idGuardiaSolicitante" value = "<%=idguardia%>"/>
		<html:hidden property = "idCalendarioGuardiasSolicitante" styleId="idCalendarioGuardiasSolicitante" value = "<%=idcalendarioguardias%>"/>
		<html:hidden property = "idPersonaSolicitante" styleId="idPersonaSolicitante" value = "<%=idpersona%>"/>
		<html:hidden property = "idCalendarioGuardiasConfirmador" styleId="idCalendarioGuardiasConfirmador" value = ""/>
		<html:hidden property = "idTurnoConfirmador" styleId="idTurnoConfirmador" value = ""/>
		<html:hidden property = "idGuardiaConfirmador" styleId="idGuardiaConfirmador" value = ""/>
		<html:hidden property = "idPersonaConfirmador" styleId="idPersonaConfirmador" value = ""/>
		<html:hidden property = "fechaInicioConfirmador" styleId="fechaInicioConfirmador" value = ""/>
		<html:hidden property = "fechaFinConfirmador" styleId="fechaFinConfirmador" value = ""/>
		<input type="hidden" id='ocultoFila' name='ocultoFila' value='' />

		<!-- INICIO: CAMPOS -->
		<!-- Zona de campos de busqueda o filtro -->
		<table align="center">
			<!-- INICIO: CAMPOS DEL REGISTRO -->
			<tr>			
				<td class="labelText">
					<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.texto1"/>:
				</td>
			</tr>
		</table>

		<siga:ConjCampos leyenda="gratuita.modalRegistro_DefinirCalendarioGuardia.literal.letrado">
			<table class="tablaCampos" align="center">
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.numero"/>		
					</td>
					<td>
						<html:text name="PermutasForm" property="numeroColegiadoSolicitante"  styleId="numeroColegiadoSolicitante" size="20" maxlength="20" styleClass="boxConsulta" value="<%=numeroColegiado%>" readonly="true" />
					</td>
							
					<td class="labelText">
						<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.nombre"/>
					</td>
					<td>
						<html:text name="PermutasForm" property="nombreSolicitante" styleId="nombreSolicitante" size="30" maxlength="300" styleClass="boxConsulta" value="<%=nombreYApellidos%>" readonly="true" />
					</td>
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.fechaInicio"/>
					</td>
					<td>
						<html:text name="PermutasForm" property="fechaInicioSolicitante" styleId="fechaInicioSolicitante" size="10" maxlength="10" styleClass="boxConsulta" value="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicio)%>" readonly="true" />
					</td>		
					
					<td class="labelText">
						<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.fechaFin"/>
					</td>
					<td>
						<html:text name="PermutasForm" property="fechaFinSolicitante" styleId="fechaFinSolicitante" size="10" maxlength="10" styleClass="boxConsulta" value="<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFin)%>" readonly="true" />
					</td>
				</tr>	
			
				<tr>
					<td class="labelText">
						<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.motivos"/>&nbsp;(*)
					</td>
					<td colspan="3">
						<html:textarea name="PermutasForm" property="motivosSolicitante" styleId="motivosSolicitante" 
							onkeydown="cuenta(this,255)" onchange="cuenta(this,255)" 
							style="overflow-y:auto; overflow-x:hidden; width:545px; height:50px; resize:none;"
							styleClass="boxCombo" value="" readonly="false" />
					</td>		
				</tr>		
			</table>
		</siga:ConjCampos>		

		<table align="center">
			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.texto2"/>:
				</td>
			</tr>
		</table>
		
		<input type="hidden" id="actionModal"  name="actionModal" value="" />
	</html:form>	

	<siga:Table 		   
	  	name="listado"
	  	border="2"
	  	columnNames="gratuita.modalCambiar_PestanaCalendarioGuardias.literal.fechaInicio,
	  		gratuita.modalCambiar_PestanaCalendarioGuardias.literal.fechaFin,
	  		gratuita.modalCambiar_PestanaCalendarioGuardias.literal.numero,
	  		gratuita.modalCambiar_PestanaCalendarioGuardias.literal.nombre,
	  		gratuita.guardiasTurno.literal.porGrupos.orden,"
	  	columnSizes="15,15,15,20,15,10">
		
<% 
		if ((obj!= null) && (obj.size()>0)) { 
			int recordNumber=1;
			String fechaInicioConfirmador="", fechaFinConfirmador="", numeroColegiadoConfirmador="", nombreConfirmador="";
			String idCalendarioGuardiasConfirmador="", idTurnoConfirmador="", idGuardiaConfirmador="", idPersonaConfirmador="";
			String posicion = "";
			while ((recordNumber) <= obj.size()) {	 	
				Hashtable hash = (Hashtable)obj.get(recordNumber-1);

				/* 
				Campos ocultos por cada fila del Confirmador:
				1- IDCALENDARIOGUARDIAS
				2- IDTURNO
				3- IDGUARDIA
				4- IDPERSONA
				5- FECHAINICIO
				6- FECHAFIN

				Campos visibles por cada fila:
				1- FECHAINICIO
				2- FECHAFIN
				3- N� COLEGIADO
				4- NOMBRE Y APELLIDOS
				*/

				fechaInicioConfirmador = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_FECHA_INICIO);
				fechaFinConfirmador = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_FECHA_FIN);
				numeroColegiadoConfirmador = UtilidadesHash.getString(hash,CenColegiadoBean.C_NCOLEGIADO);
				nombreConfirmador = UtilidadesHash.getString(hash,"NOMBRE");
				idCalendarioGuardiasConfirmador = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_IDCALENDARIOGUARDIAS);
				idTurnoConfirmador = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_IDTURNO);
				idGuardiaConfirmador = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_IDGUARDIA);
				idPersonaConfirmador = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_IDPERSONA);
				posicion = UtilidadesHash.getString(hash,ScsCabeceraGuardiasBean.C_POSICION);
				
				/*Funcion que comprueba las acciones que puede hacer en una guardia (Sustituir, Anular, Borrar, Permutar)
				- RETORNA SUSTITUIR(1) || ANULAR(1) || BORRAR(1) || PERMUTAR(1) || ASISTENCIA(1)
				-- SUSTITUIR VARCHAR2(1); -- 'N': no sustituible; 'S': sustituible
				-- ANULAR VARCHAR2(1); -- 'N': no anulable; 'S': anulable
				-- BORRAR VARCHAR2(1); -- 'N': no borrable; 'S': borrable
				-- PERMUTAR VARCHAR2(1); -- N': no permutable (Pendiente Solicitante); 'P': no permutable (Pendiente Confirmador); 'S': permutable
				-- ASISTENCIA VARCHAR2(1); -- 'N': sin Asistencia; 'S': con asistencia */ 				
				String funcionPermutas = UtilidadesHash.getString(hash, "FUNCIONPERMUTAS");
				String accionSustituir = funcionPermutas.substring(0, 1);
				String accionAnular = funcionPermutas.substring(1, 2);
				String accionBorrar = funcionPermutas.substring(2, 3);
				String accionPermutar = funcionPermutas.substring(3, 4);
				String tieneAsistencias = funcionPermutas.substring(4, 5);
				
				if (accionPermutar!=null && accionPermutar.equalsIgnoreCase("S")) {
%>
					<tr class="listaNonEdit">
						<td align="center">
							<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_1' name='oculto<%=String.valueOf(recordNumber)%>_1' value='<%=idCalendarioGuardiasConfirmador%>' >
							<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_2' name='oculto<%=String.valueOf(recordNumber)%>_2' value='<%=idTurnoConfirmador%>' >
							<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_3' name='oculto<%=String.valueOf(recordNumber)%>_3' value='<%=idGuardiaConfirmador%>' >
							<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_4' name='oculto<%=String.valueOf(recordNumber)%>_4' value='<%=idPersonaConfirmador%>' />
							<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_5' name='oculto<%=String.valueOf(recordNumber)%>_5' value='<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioConfirmador)%>' />
							<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_6' name='oculto<%=String.valueOf(recordNumber)%>_6' value='<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFinConfirmador)%>' />
							<input type="hidden" id='oculto<%=String.valueOf(recordNumber)%>_ASI' name='oculto<%=String.valueOf(recordNumber)%>_ASI' value='<%=tieneAsistencias%>' />				
							<%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaInicioConfirmador)%>
						</td>
						<td align="center"><%=GstDate.getFormatedDateShort(usr.getLanguage(),fechaFinConfirmador)%></td>
						<td align="center"><%=numeroColegiadoConfirmador%></td>
						<td align="left">&nbsp;<%=nombreConfirmador%></td>								
						<td align="center"><%=posicion%></td>
						<td align="center">
							<input type="radio" name="guardiaConfirmador" id="guardiaConfirmador" value="<%=String.valueOf(recordNumber)%>" onclick="seleccionarFila('<%=String.valueOf(recordNumber)%>')" />
						</td>
					</tr>				
<% 							
				}
				recordNumber++;
			} 

		} else { 
%>
			<tr class="notFound">
				<td class="titulitos">
					<html:hidden property = "actionModal" value = "P"/>
					<siga:Idioma key="messages.noRecordFound"/>
				</td>
			</tr>
<% 
		} 
%>			
	</siga:Table>
	
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->
	<!-- INICIO: BOTONES REGISTRO -->
	<siga:ConjBotonesAccion botones="Y,C" modal="M" clase="botonesDetalle"/>

	<!-- FIN: BOTONES REGISTRO -->
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">
	
		//Guardo los campos seleccionados
		function seleccionarFila(fila) {
		    var idcalendario = 'oculto' + fila + '_' + 1;
		    var idturno = 'oculto' + fila + '_' + 2;
		    var idguardia = 'oculto' + fila + '_' + 3;
		    var idpersona = 'oculto' + fila + '_' + 4;
		    var fechainicio = 'oculto' + fila + '_' + 5;
   		    var fechafin = 'oculto' + fila + '_' + 6;
   		    
			//Datos del elemento seleccionado:
			document.getElementById("idCalendarioGuardiasConfirmador").value = document.getElementById(idcalendario).value;
			document.getElementById("idTurnoConfirmador").value = document.getElementById(idturno).value;
			document.getElementById("idGuardiaConfirmador").value = document.getElementById(idguardia).value;
			document.getElementById("idPersonaConfirmador").value = document.getElementById(idpersona).value;		
			document.getElementById("fechaInicioConfirmador").value = document.getElementById(fechainicio).value;
			document.getElementById("fechaFinConfirmador").value = document.getElementById(fechafin).value;
			document.getElementById("ocultoFila").value = fila;
		}
	
		//Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {		
			sub();
			//Chequeo que ha seleccionado un valor de la lista:
			if(document.getElementById("idPersonaConfirmador").value == "") {
				alert('<siga:Idioma key="gratuita.modalCambiar_PestanaCalendarioGuardias.error.seleccionar"/>');
				fin();
				return false;
			} else {
				//Valido el tamanho del textarea motivosSolicitud
				if (validatePermutasForm(document.PermutasForm)){					
					
					var tieneAsistencias = 'oculto' + document.getElementById("ocultoFila").value + '_ASI';
					var valorTieneAsistencias = document.getElementById(tieneAsistencias).value;
					var confirmado = true;
					
					if (valorTieneAsistencias == 'S') {
						confirmado = false;
						if (confirm("<siga:Idioma key='gratuita.listadoModal_DefinirCalendarioGuardia.permutar.tieneAsistencias'/>")) {
							confirmado = true;
						}
					}
					
					if (confirmado == true)	{											
						document.forms[0].modo.value = "insertar";
						document.forms[0].target = "submitArea";							
						document.forms[0].submit();
					} else {
						fin();
						return false;
					}
				} else {
					fin();
					return false;
				}
			}
		}

		//Asociada al boton Cerrar
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}
		
		function refrescarLocal() {		
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