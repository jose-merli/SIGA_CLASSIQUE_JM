<!DOCTYPE html>
<html>
<head>
<!-- consultaSolicitudesModificacionEspecificas.jsp -->

<!-- 
	 Muestra los resultados de la busqueda de solicitudes de modificacion especificas
	 VERSIONES:
	 miguel.villegas 24-01-2005 Creacion
-->
	 
 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri="struts-html.tld" 	prefix="html"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean)ses.getAttribute("USRBEAN");
	String	tamanho="";	

	// Recojo la institucion, el usuario y el tipo de modificacion
	String idPersona=(String)request.getAttribute("IDPERSONA"); 
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION");
	String tipoModificacion=(String)request.getAttribute("TIPOMODIF");
	String textoTipoModificacion=(String)request.getAttribute("TEXTOTIPOMODIF");
	
	// Obtengo manejadores para colegiado y persona
	CenPersonaAdm personaAdm = new CenPersonaAdm(usr);
	CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(usr);

	// Establezco los botones del action
	String	botones="MT,DT,PS,DS";

	ses.setAttribute("EnvEdicionEnvio","GME");
%>	


	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="" staticJavascript="false" />  		
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- SCRIPTS LOCALES -->
		<script language="JavaScript">
		
			function refrescarLocal () {
				parent.buscar();
			}			
		
			function enviar(fila) {			   	
			   	var auxSol = 'oculto' + fila + '_3';
			    var idSolic = document.getElementById(auxSol);			          		
			   				   	
			   	var auxPers = 'oculto' + fila + '_1';
			    var idPers = document.getElementById(auxPers);	
			    	    
			    var auxDesc = 'oculto' + fila + '_6';
			    var desc = document.getElementById(auxDesc);			    
			    
			    document.DefinirEnviosForm.idSolicitud.value=idSolic.value;
			   	document.DefinirEnviosForm.idPersona.value=idPers.value;
			   	document.DefinirEnviosForm.descEnvio.value="Modificacion "+desc.value;
			   	
			   	document.DefinirEnviosForm.modo.value='envioModal';		   	
			   	var resultado = ventaModalGeneral("DefinirEnviosForm","P");
			   	if (resultado==undefined||resultado[0]==undefined ||resultado[0]=="M"){			   		
			   	} else {
			   		var idEnvio = resultado[0];
				    var idTipoEnvio = resultado[1];
				    var nombreEnvio = resultado[2];				    
				    
				   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.DefinirEnviosForm.modo.value='editar';
				   	document.DefinirEnviosForm.submit();
			   	}
			}	
		</script>
	</head>

	<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_SolicitudesModificacionEspecificas.do" method="POST" target="_blank" style="display:none">
			<!-- Campo obligatorio -->
			<html:hidden styleId="modo" property="modo" value=""/>
			<html:hidden styleId="idPersona"  property="idPersona" value="<%=idPersona%>"/>
			<html:hidden styleId="idInstitucion"  property="idInstitucion" value="<%=idInstitucion%>"/>
			<html:hidden styleId="solicitudes" property="solicitudes"/>
			<html:hidden styleId="solicitudesTipoModif" property="solicitudesTipoModif"/>
			<html:hidden styleId="tipoModifEspec" property="tipoModifEspec" value="<%=tipoModificacion%>"/>		
			<html:hidden property="confirmacionProcesoAltaCuentaCargos" styleId="confirmacionProcesoAltaCuentaCargos" value="false"/>	
		</html:form>
					
		<!-- EN FUNCION DEL TIPO DE MODIFICACION SOLICITADA SE ABRE UNA VENTANA DE UN TAMA�O U OTRO -->
		<% 	if ((tipoModificacion.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV))) || (tipoModificacion.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES))) || (tipoModificacion.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION))) || (tipoModificacion.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXPEDIENTES)))){
			tamanho="M";
		} else{
			tamanho="G";
		} %>
		
		<siga:Table 
		 	name="tablaDatos"
			border="1"
			columnNames="censo.resultadosSolicitudesModificacion.literal.validar,censo.busquedaSolicitudesTextoLibre.literal.estado,
			 			 censo.resultadosSolicitudesModificacion.literal.idSolicitud,censo.resultadosSolicitudesModificacion.literal.tipoModificacion,
			  			 censo.resultadosSolicitudesModificacion.literal.nColegiado,censo.resultadosSolicitudesModificacion.literal.Nombre,
			  			 censo.resultadosSolicitudesModificacion.literal.fecha,censo.resultadosSolicitudesModificacion.literal.descripcion,"
			columnSizes="5,8,6,16,8,18,8,20,10"
			modal="<%=tamanho%>">

			<%if (request.getAttribute("container") == null || ((Vector)request.getAttribute("container")).size() < 1 ) {%>
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
				
			<%} else { 
	    		Enumeration en = ((Vector)request.getAttribute("container")).elements();
				
				int recordNumber=1;
				int recordPendiente=1;					
				String botonesMostrados="C";	

				while (en.hasMoreElements()) {
            		Row row = (Row) en.nextElement(); 

					// boton de envios
					FilaExtElement[] elems = new FilaExtElement[1];
					elems[0]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);%>	            		
            		
					<siga:FilaConIconos
						fila='<%=String.valueOf(recordNumber)%>'
						botones='<%=botonesMostrados%>'
						modo='editar'
						visibleEdicion='no'
						visibleBorrado='no'		
						elementos="<%=elems%>" 
						pintarEspacio="no" 							  					  							  
						clase="listaNonEdit">
						
						<td align="center">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_1" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString(CenSolicitudesModificacionBean.C_IDPERSONA)%>">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_2" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(CenSolicitudesModificacionBean.C_IDINSTITUCION)%>">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_3" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString(CenSolicitudesModificacionBean.C_IDSOLICITUD)%>">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_4" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=row.getString("TIPOMODIF")%>">
                            <input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_5" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=row.getString("CODIGO")%>">
							<!-- Valores especificos de la clave original para cada tipo de modificacion existente -->

							<!-- ENVIOS 3 idSolicitud, 1 idPersona, 6 descripcion -->
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_6" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=row.getString("TEXTOTIPOMODIF")%>">
							
							<!-- Si esta pendiente de procesar habilito checkbox -->								
							<% if (row.getString("IDESTADOSOLIC").equalsIgnoreCase(String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE))){%>
								<input type="checkbox" id="validado" value="1" name="validado" value="1">
								<input type="hidden" id="solicita_<%=String.valueOf(recordPendiente)%>" name="solicita_<%=String.valueOf(recordPendiente)%>" value='<%=row.getString("IDSOLICITUD")%>'>	
								<input type="hidden" id="solicitaTipoModif_<%=String.valueOf(recordPendiente)%>"  name="solicitaTipoModif_<%=String.valueOf(recordPendiente)%>" value='<%=row.getString("TIPOMODIF")%>'>
								<input type="hidden" id="cargoNew_<%=String.valueOf(recordPendiente)%>" name="cargoNew_<%=String.valueOf(recordPendiente)%>" value="<%=row.getString("ABONOCARGO")%>">
								<input type="hidden" id="cargoOld_<%=String.valueOf(recordPendiente)%>" name="cargoOld_<%=String.valueOf(recordPendiente)%>" value="<%=row.getString("ABONOCARGO_CUENTABANCARIA")%>">								
								<% recordPendiente++; %>								
							<% } else { %>
								&nbsp;
							<% } %>
						</td>
						
						<td><%=UtilidadesString.mostrarDatoJSP(row.getString("ESTADO"))%></td>  	
						<td><%=UtilidadesString.mostrarDatoJSP(row.getString("IDSOLICITUD"))%></td>
						<td><%=UtilidadesString.mostrarDatoJSP(row.getString("TEXTOTIPOMODIF"))%></td>
						<td>								
							<%if (!(row.getString("IDPERSONA").equalsIgnoreCase(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO)))) {%>
								<%=UtilidadesString.mostrarDatoJSP(colegiadoAdm.getIdentificadorColegiado(colegiadoAdm.getDatosColegiales(new Long(row.getString("IDPERSONA")),new Integer(row.getString("IDINSTITUCION")))))%>
							<%} else {%>
								&nbsp;
							<%}%>
						</td>  	
						<td>
							<%if (row.getString("IDPERSONA").equalsIgnoreCase(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO))) {%>
								<siga:Idioma key="censo.tipoApunteHistorico.automatico"/>
							<%} else {%>
								<%=UtilidadesString.mostrarDatoJSP(personaAdm.obtenerNombreApellidos(row.getString("IDPERSONA")))%>
							<%}%>
						</td>  	
						<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),row.getString(CenSolicitudModificacionCVBean.C_FECHAALTA)))%></td>  								
						<td><%=UtilidadesString.mostrarDatoJSP(row.getString(CenSolicitudModificacionCVBean.C_MOTIVO))%></td>  																						
					</siga:FilaConIconos>
					
					<% recordNumber++;
				} 
			}%>		
		</siga:Table>
							
		<siga:ConjBotonesAccion botones='<%=botones%>' modo="editar"  clase="botonesDetalle"/>
	 	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">

			//Asociada al boton MarcarTodos
			function accionMarcarTodos() {		
				if (document.getElementById("solicita_1")!=null){
					var dd = document.getElementsByName("validado");
					if (dd.type != 'checkbox') {
						for (var i = 0; i < dd.length; i++){
							dd[i].checked=1;		
						}
						
					} else{
						dd.checked=1;		
					}
				}	
			}
		
			//Asociada al boton DesmarcarTodos
			function accionDesmarcarTodos() {		
				if (document.getElementById("solicita_1")!=null){
					var dd = document.getElementsByName("validado");
					if (dd.type != 'checkbox') {
						for (var i = 0; i < dd.length; i++){
							dd[i].checked=0;		
						}
						
					} else{
						dd.checked=0;		
					}
				}	
			}
		
			//Asociada al boton ProcesarSolicitud
			function accionProcesarSolicitud() {	
				sub();		
				var datos = "";
				var datosTipoModif = "";
				var checked=false;
				var bConfirmacionProcesoAltaCuentaCargos=false;
				if (document.getElementById("solicita_1")!=null){
				 	var dd = document.getElementsByName("validado");
					if (dd.type != 'checkbox') {
						for (var i = 0; i < dd.length; i++){
							if (dd[i].checked==1){							
								var j=i+1;
								var aux="solicita_"+j;
								var solicitado=document.getElementById(aux);
								datos=datos + solicitado.value + "%";	
								var aux1="solicitaTipoModif_"+j;
								var solicitadoTipoModif=document.getElementById(aux1);
								datosTipoModif=datosTipoModif + solicitadoTipoModif.value + "%";						
								checked=true;
								
								// Obtengo si es una cuenta bancaria de cargo actualmente
								var cargoOld = jQuery("#cargoOld_"+j).val();
								var bCargoOld = (cargoOld=="<%=ClsConstants.TIPO_CUENTA_CARGO%>" || cargoOld=="<%=ClsConstants.TIPO_CUENTA_ABONO_CARGO%>");
								
								// Obtengo si es una solicitud de modificacion de una cuenta bancaria de cargos
								var cargoNew = jQuery("#cargoNew_"+j).val();								
								var bCargoNew = (cargoNew=="<%=ClsConstants.TIPO_CUENTA_CARGO%>" || cargoNew=="<%=ClsConstants.TIPO_CUENTA_ABONO_CARGO%>");
								
								// Compruebo que ahora este marcado la casilla de Cargo y antes no estuviera
								if (bCargoNew && !bCargoOld) {
									bConfirmacionProcesoAltaCuentaCargos=true;									
								}								
							}	
						}
						
					} else {				           
						if (dd.checked==1){
							var aux="solicita_1";
							var solicitado=document.getElementById(aux);
							datos=datos + solicitado.value + "%";	
							var aux1="solicitaTipoModif_1";
							var solicitadoTipoModif=document.getElementById(aux1);
							datosTipoModif=datosTipoModif + solicitadoTipoModif.value + "%";			
							checked=true;			
						}	
					}
					
				} else {
				 	fin();
				 	return false;
				}
				
				if (checked){
					
					if (bConfirmacionProcesoAltaCuentaCargos) {
						var mensajeConfirmacionProcesoAltaCuentaCargos = '<%=UtilidadesString.getMensajeIdioma(usr, "censo.tipoCuenta.cargo.confirmacionProcesoAltaCuentaCargos")%>';					
						if (confirm(mensajeConfirmacionProcesoAltaCuentaCargos)) {
							jQuery("#confirmacionProcesoAltaCuentaCargos").val("true");
						}
					}
					
					document.forms[0].solicitudes.value = datos;	
					document.forms[0].solicitudesTipoModif.value = datosTipoModif;				
					document.forms[0].modo.value = "procesarSolicitud";
					document.forms[0].target = "submitArea";
					document.forms[0].submit();
					
				} else {
					alert("<siga:Idioma key='general.message.seleccionar'/>");
					fin();
					return false;
				}
			}

			//Asociada al boton DenegarSolicitud			
			function accionDenegarSolicitud () 
			{
				var datos = "";
				var datosTipoModif = "";
				var checked=false;
				if (document.getElementById("solicita_1")!=null){
					var dd = document.getElementsByName("validado");
					if (dd.type != 'checkbox') {
						for (var i = 0; i < dd.length; i++){
							if (dd[i].checked==1){
								var j=i+1;
								var aux="solicita_"+j;
								var solicitado=document.getElementById(aux);
								datos=datos + solicitado.value + "%";
								var aux1="solicitaTipoModif_"+j;
								var solicitadoTipoModif=document.getElementById(aux1);
								datosTipoModif=datosTipoModif + solicitadoTipoModif.value + "%";
								checked=true;
							}
						}
						
					} else {
						if (dd.checked==1){
							var aux="solicita_1";
							var solicitado=document.getElementById(aux);
							datos=datos + solicitado.value + "%";		
							var aux1="solicitaTipoModif_1";
							var solicitadoTipoModif=document.getElementById(aux1);
							datosTipoModif=datosTipoModif + solicitadoTipoModif.value + "%";
							checked=true;
						}	
					}
					
				} else {
					fin();
				 	return false;
				}	
				
				if(checked){
					document.forms[0].solicitudes.value = datos;
					document.forms[0].solicitudesTipoModif.value = datosTipoModif;	
					document.forms[0].modo.value = "denegarSolicitud";
					document.forms[0].target = "submitArea";
					document.forms[0].submit();
					
				} else {
					alert("<siga:Idioma key='general.message.seleccionar'/>");
					fin();
					return false;
				}					
			}
						
		</script>
		<!-- FIN: SCRIPTS BOTONES -->	 		 	
	 	<!-- FIN: LISTA DE VALORES -->
	 
		<!-- Formulario para la creacion de envio -->
		<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea">
			<html:hidden property = "actionModal" value = ""/>
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "tablaDatosDinamicosD" value = ""/>			
			<html:hidden property = "idSolicitud" value = ""/>
			<html:hidden property = "idPersona" value = ""/>
			<html:hidden property = "descEnvio" value = ""/>			
		</html:form>

		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las p�ginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>
