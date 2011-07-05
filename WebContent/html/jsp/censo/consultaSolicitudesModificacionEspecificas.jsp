<!-- consultaSolicitudesModificacionEspecificas.jsp -->
<!-- 
	 Muestra los resultados de la busqueda de solicitudes de modificacion especificas
	 VERSIONES:
	 miguel.villegas 24-01-2005 Creacion
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
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);		
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
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

	request.getSession().setAttribute("EnvEdicionEnvio","GME");

%>	

<html>
<!-- HEAD -->
	<head>

		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		<style type="text/css">

			.especif
			{
				background-color : #<%=src.get("color.titleBar.BG")%>;
				position:absolute; width:964; height:35; z-index:2; top: 325px; left: 0px
			}

		</style>
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- SCRIPTS LOCALES -->
		<script language="JavaScript">
		
			function refrescarLocal () 
			{
				parent.buscar();
			}			
		
			function enviar(fila)
			{
			   	
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
			   	if (resultado==undefined||resultado[0]==undefined){			   		
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
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_SolicitudesModificacionEspecificas.do" method="POST" target="_blank" style="display:none">

			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "idPersona" value ="<%=idPersona%>"/>
			<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
			<html:hidden property = "solicitudes"/>
			<html:hidden property = "solicitudesTipoModif"/>
			<html:hidden property = "tipoModifEspec" value ="<%=tipoModificacion%>"/>			
			
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
					
			<!-- EN FUNCION DEL TIPO DE MODIFICACION SOLICITADA SE ABRE UNA VENTANA DE UN TAMAÑO U OTRO -->
			<% 	if ((tipoModificacion.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_CV))) || (tipoModificacion.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_GENERALES))) || (tipoModificacion.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_DATOS_FACTURACION))) || (tipoModificacion.equalsIgnoreCase(String.valueOf(ClsConstants.TIPO_SOLICITUD_MODIF_EXPEDIENTES)))){
					tamanho="M";
				}
				else{
					tamanho="G";
				} %>
			<siga:TablaCabecerasFijas 
		  	      nombre="tablaDatos"
				  borde="1"
		   		  clase="tableTitle"
				  nombreCol="censo.resultadosSolicitudesModificacion.literal.validar,censo.busquedaSolicitudesTextoLibre.literal.estado,
				  			 censo.resultadosSolicitudesModificacion.literal.idSolicitud,censo.resultadosSolicitudesModificacion.literal.tipoModificacion,
				  			 censo.resultadosSolicitudesModificacion.literal.nColegiado,censo.resultadosSolicitudesModificacion.literal.Nombre,
				  			 censo.resultadosSolicitudesModificacion.literal.fecha,censo.resultadosSolicitudesModificacion.literal.descripcion,"
				  tamanoCol="6,8,6,16,8,18,8,20,10"
				  alto="290"
				  ajusteBotonera="true"
				  modal="<%=tamanho%>"
				  activarFilaSel="true" >


				<%
		    	 if (request.getAttribute("container") == null || ((Vector)request.getAttribute("container")).size() < 1 ){
				%>
					<br><br>
					<p class="titulitos" style="text-align:center;"><siga:Idioma key="messages.noRecordFound"/></p>
					<br>
				<%
	    		 }	    
		    	 else { 
		    		Enumeration en = ((Vector)request.getAttribute("container")).elements();
					
					int recordNumber=1;
					int recordPendiente=1;					
					String botonesMostrados="C";	

					while (en.hasMoreElements())
					{
	            		Row row = (Row) en.nextElement(); 

						// boton de envios
						FilaExtElement[] elems = new FilaExtElement[1];
						elems[0]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
   	 		
  			 			%>	            		
	            		
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
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString(CenSolicitudesModificacionBean.C_IDPERSONA)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(CenSolicitudesModificacionBean.C_IDINSTITUCION)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString(CenSolicitudesModificacionBean.C_IDSOLICITUD)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=row.getString("TIPOMODIF")%>">
                                <input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=row.getString("CODIGO")%>">
								<!-- Valores especificos de la clave original para cada tipo de modificacion existente -->

								

								<!-- ENVIOS 3 idSolicitud, 1 idPersona, 6 descripcion -->
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=row.getString("TEXTOTIPOMODIF")%>">
								
								
								<!-- Si esta pendiente de procesar habilito checkbox -->
								
								<% if (row.getString("IDESTADOSOLIC").equalsIgnoreCase(String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE))){%>
									<input type="checkbox" name="validado" value="1">
									<input type="hidden" name="solicita_<%=String.valueOf(recordPendiente)%>" value='<%=row.getString("IDSOLICITUD")%>'>	
									<input type="hidden" name="solicitaTipoModif_<%=String.valueOf(recordPendiente)%>" value='<%=row.getString("TIPOMODIF")%>'>								
									<% recordPendiente++; %>
								<% } else { %>
									&nbsp;
								<% } %>
							</td>
							<td>
								<%=UtilidadesString.mostrarDatoJSP(row.getString("ESTADO"))%>
							</td>  	
							<td>
								<%=UtilidadesString.mostrarDatoJSP(row.getString("IDSOLICITUD"))%>
							</td>
							<td>
								<%/*=row.getString("MODIFICACION")*/%>
								<%=UtilidadesString.mostrarDatoJSP(row.getString("TEXTOTIPOMODIF"))%>
							</td>
							<td>								
								<% if (!(row.getString("IDPERSONA").equalsIgnoreCase(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO)))){%>
									<%=UtilidadesString.mostrarDatoJSP(colegiadoAdm.getIdentificadorColegiado(colegiadoAdm.getDatosColegiales(new Long(row.getString("IDPERSONA")),new Integer(row.getString("IDINSTITUCION")))))%>
								<% } else { %>
									&nbsp;
								<% } %>
							</td>  	
							<td>
								<% if (row.getString("IDPERSONA").equalsIgnoreCase(String.valueOf(ClsConstants.USUMODIFICACION_AUTOMATICO))){%>
									<siga:Idioma key="censo.tipoApunteHistorico.automatico"/>
								<% } else { %>
									<%=UtilidadesString.mostrarDatoJSP(personaAdm.obtenerNombreApellidos(row.getString("IDPERSONA")))%>
								<% } %>
							</td>  	
							<td>
								<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),row.getString(CenSolicitudModificacionCVBean.C_FECHAALTA)))%>
							</td>  								
							<td>
								<%=UtilidadesString.mostrarDatoJSP(row.getString(CenSolicitudModificacionCVBean.C_MOTIVO))%>		
							</td>  								
														
						</siga:FilaConIconos>
						<% recordNumber++;
					} 
				 } %>		
			</siga:TablaCabecerasFijas>
							
				<siga:ConjBotonesAccion botones='<%=botones%>' modo="editar"  clase="botonesDetalle"/>
	

	 	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">

			//Asociada al boton MarcarTodos
			function accionMarcarTodos() 
			{		
				if (document.getElementById("solicita_1")!=null){
					var dd = document.getElementsByName("validado");
					if (dd.type != 'checkbox') {
						for (i = 0; i < dd.length; i++){
							dd[i].checked=1;		
						}	
					}
					else{
						dd.checked=1;		
					}
				}	
			}
		
			//Asociada al boton DesmarcarTodos
			function accionDesmarcarTodos() 
			{		
				if (document.getElementById("solicita_1")!=null){
					var dd = document.getElementsByName("validado");
					if (dd.type != 'checkbox') {
						for (i = 0; i < dd.length; i++){
							dd[i].checked=0;		
						}
					}
					else{
						dd.checked=0;		
					}
				}	
			}
		
			//Asociada al boton ProcesarSolicitud
			function accionProcesarSolicitud() 
			{	sub();		
				var datos = "";
				var datosTipoModif = "";
				var checked=false;
				if (document.getElementById("solicita_1")!=null){
				 	var dd = document.getElementsByName("validado");
					if (dd.type != 'checkbox') {
						for (i = 0; i < dd.length; i++){
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
					}	
					else{
				            
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
					
				}else{
				 fin();
				 return false;
				}
				if(checked){
				document.forms[0].solicitudes.value = datos;	
					document.forms[0].solicitudesTipoModif.value = datosTipoModif;				
					document.forms[0].modo.value = "procesarSolicitud";
					document.forms[0].target = "submitArea";
					document.forms[0].submit();
				}else{
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
				if (document.getElementById("solicita_1")!=null){
					var dd = document.getElementsByName("validado");
					if (dd.type != 'checkbox') {
						for (i = 0; i < dd.length; i++){
							if (dd[i].checked==1){
								var j=i+1;
								var aux="solicita_"+j;
								var solicitado=document.getElementById(aux);
								datos=datos + solicitado.value + "%";
								var aux1="solicitaTipoModif_"+j;
								var solicitadoTipoModif=document.getElementById(aux1);
								datosTipoModif=datosTipoModif + solicitadoTipoModif.value + "%";	
							}else{
							 if (datos==""){
							  return false;
							 } 
							}
						}
					}	
					else{
						if (dd.checked==1){
							var aux="solicita_1";
							var solicitado=document.getElementById(aux);
							datos=datos + solicitado.value + "%";		
							var aux1="solicitaTipoModif_1";
							var solicitadoTipoModif=document.getElementById(aux1);
							datosTipoModif=datosTipoModif + solicitadoTipoModif.value + "%";					
						}	
					}
				}else{
				 return false;
				}	
				document.forms[0].solicitudes.value = datos;
				document.forms[0].solicitudesTipoModif.value = datosTipoModif;	
				document.forms[0].modo.value = "denegarSolicitud";
				document.forms[0].target = "submitArea";
				document.forms[0].submit();
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
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

	</body>
</html>
