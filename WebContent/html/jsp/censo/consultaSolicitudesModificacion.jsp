<!-- consultaSolicitudesModificacion.jsp -->

<!-- 
	 Muestra los resultados de la busqueda de solicitudes de modificacion
	 VERSIONES:
	 miguel.villegas 19-01-2005 
-->
	 
 
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);		
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	// Recojo la institucion y el usuario
	String idPersona=(String)request.getAttribute("IDPERSONA"); 
	String idInstitucion=(String)request.getAttribute("IDINSTITUCION");
	
	// Obtengo manejadores para colegiado y persona
	CenPersonaAdm personaAdm = new CenPersonaAdm(usr);
	CenColegiadoAdm colegiadoAdm = new CenColegiadoAdm(usr);
	CenNoColegiadoAdm noColegiadoAdm = new CenNoColegiadoAdm(usr);
	CenNoColegiadoBean beanNoCol = null;

	// Establezco los botones del action
	String	botones="MT,DT,PS,DS";

	request.getSession().setAttribute("EnvEdicionEnvio","GMG");
%>	

<html>
<!-- HEAD -->
	<head>
			<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		
		<style type="text/css">
			.especif {
				background-color : #<%=src.get("color.titleBar.BG")%>;
				position:absolute; width:964; height:35; z-index:2; top: 325px; left: 0px
			}
		</style>
				
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
			
			//Asociada al icono ModificarDato
			function modificarDatos(fila) {
			   var datos;
			   datos = document.getElementById('tablaDatosDinamicosD');
			   datos.value = ""; 
			   var i, j;
			   for (i = 0; i < 7; i++) {
			      var tabla;
			      tabla = document.getElementById('tablaDatos');
			      if (i == 0) {
			        var flag = true;
			        j = 1;
			        while (flag) {
			          var aux = 'oculto' + fila + '_' + j;
			          var oculto = document.getElementById(aux);
			          if (oculto == null)  { flag = false; }
			          else { datos.value = datos.value + oculto.value + ','; }
			          j++;
			        }
			        datos.value = datos.value + "%"
			      } else { j = 2; }
			      if ((tabla.rows[fila].cells)[i].innerHTML == "") 
			        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
			      else
			        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';
			   }
			   document.forms[0].target="mainWorkArea";
			   document.forms[0].modo.value = "modificarDatos";
			   document.forms[0].submit();
			 }					
			
			//mhg LLamamos la método editar pasandole un LETRADO como tipo
			function editarLetrado(fila) {
				document.busquedaClientesForm.filaSelD.value = fila;					
				var idInst = <%=idInstitucion%>;			   				   	
			    var auxPers = 'oculto' + fila + '_1';
			    var idPers = document.getElementById(auxPers);
				document.busquedaClientesForm.tablaDatosDinamicosD.value=idPers.value + ',' + idInst + ',LETRADO' + '%';		
				document.busquedaClientesForm.modo.value="editar";
				var verLetradoAux = 'oculto' + fila + '_4';
			    var verLetrado = document.getElementById(verLetradoAux);			    
				document.busquedaClientesForm.verFichaLetrado.value=verLetrado.value;
			   	document.busquedaClientesForm.submit();
			}
			
			//mhg LLamamos la método editar pasandole un 1 como tipo
			function editarNoColegiado(fila) {
				document.busquedaClientesForm.filaSelD.value = fila;					
				var idInst = <%=idInstitucion%>;			   				   	
			    var auxPers = 'oculto' + fila + '_1';
			    var idPers = document.getElementById(auxPers);
				document.busquedaClientesForm.tablaDatosDinamicosD.value=idPers.value + ',' + idInst + ',1' + '%';		
				document.busquedaClientesForm.modo.value="editar";
			   	document.busquedaClientesForm.submit();
			}
			
			function enviar(fila) {			   	
			   	var auxSol = 'oculto' + fila + '_3';
			    var idSolic = document.getElementById(auxSol);			          		
			   				   	
			   	var auxPers = 'oculto' + fila + '_4';
			    var idPers = document.getElementById(auxPers);	
			    	    
			    var auxDesc = 'oculto' + fila + '_5';
			    var desc = document.getElementById(auxDesc);			    
			    
			    document.DefinirEnviosForm.idSolicitud.value=idSolic.value;
			   	document.DefinirEnviosForm.idPersona.value=idPers.value;
			   	document.DefinirEnviosForm.descEnvio.value="Modificacion "+desc.value;
			   	
			   	document.DefinirEnviosForm.modo.value='envioModal';		   	
			   	var resultado = ventaModalGeneral("DefinirEnviosForm","P");
			   	
			   	if (!resultado||!resultado[0]||resultado[0]=="M"){			   		
			   	} else {
			   		var idEnvio = resultado[0];
				    var idTipoEnvio = resultado[1];
				    var nombreEnvio = resultado[2];				    
				    
				   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.DefinirEnviosForm.modo.value='editar';
				   	//document.DefinirEnviosForm.submit();
			   	}
			}	
		</script>
	</head>

	<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_SolicitudesModificacionGenericas.do?noReset=true" method="POST" target="" style="display:none">
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" styleId = "modo" value = ""/>
			<html:hidden property = "idPersona" styleId = "idPersona" value ="<%=idPersona%>"/>
			<html:hidden property = "idInstitucion"  styleId = "idInstitucion" value ="<%=idInstitucion%>"/>			
			<html:hidden property = "solicitudes" styleId = "solicitudes"/>													
			<input type="hidden" name="actionModal" id="actionModal" value="">
		</html:form>
		
		<siga:Table 
	  	    name="tablaDatos"
			border="1"
			columnNames="censo.resultadosSolicitudesTextoLibre.literal.validar,censo.busquedaSolicitudesTextoLibre.literal.estado,
						 censo.resultadosSolicitudesTextoLibre.literal.tipoModificacion,censo.resultadosSolicitudesTextoLibre.literal.nColegiado,
			  			 censo.resultadosSolicitudesTextoLibre.literal.Nombre,censo.resultadosSolicitudesTextoLibre.literal.fecha,
			  			 censo.resultadosSolicitudesTextoLibre.literal.descripcion,"
			columnSizes="6,10,16,7,18,8,22,13">

			<%if (request.getAttribute("container") == null || ((Vector)request.getAttribute("container")).size() < 1 ) {%>
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
			
			<%} else { 
		    	Enumeration en = ((Vector)request.getAttribute("container")).elements();
				int recordNumber=1;
				int recordPendiente=1;					
				String botonesMostrados="";	

				while (en.hasMoreElements()) {
            		Row row = (Row) en.nextElement(); 
					FilaExtElement[] elementos=null;
					if (row.getString("IDESTADOSOLIC").equalsIgnoreCase(String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE))){
						//mhg Se comprueba que pertenece a CGAE y es Letrado
						if (String.valueOf(ClsConstants.INSTITUCION_CGAE).equals(row.getString("IDINSTITUCION")) && row.getString("LETRADO").equals("1")){
								elementos = new FilaExtElement[2];
								elementos[0]=new FilaExtElement("editar","editarLetrado",SIGAConstants.ACCESS_FULL);
								elementos[1]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
								
						} else {
							//Comprobamos si la persona es no colegiado. En caso de serla podemos modificarla.
							beanNoCol = noColegiadoAdm.existeNoColegiado(new Long(idPersona));
							if (beanNoCol != null) {
								elementos = new FilaExtElement[2];
 				 					elementos[0]=new FilaExtElement("editar","editarNoColegiado",SIGAConstants.ACCESS_FULL);
								elementos[1]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
								
							} else {
								elementos = new FilaExtElement[1];
 				 					elementos[0]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
							}
						}
						
 			 		} else {
						elementos = new FilaExtElement[1];
						elementos[0]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_FULL);
					}%>	            		
	            		
					<siga:FilaConIconos
						fila='<%=String.valueOf(recordNumber)%>'
						botones='<%=botonesMostrados%>'
						elementos='<%=elementos%>'
						modo='editar'
						visibleConsulta='no'
						visibleEdicion='no'
						visibleBorrado='no'
						pintarEspacio='no'
						clase="listaNonEdit">
						
						<td align="center">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_1" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=row.getString(CenSolicitudesModificacionBean.C_IDPERSONA)%>">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_2" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(CenSolicitudesModificacionBean.C_IDINSTITUCION)%>">

							<!-- ENVIOS 3 idSolicitud, 4 idPersona, 5 descripcion -->
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_3" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString(CenSolicitudesModificacionBean.C_IDSOLICITUD)%>">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_4" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=row.getString(CenSolicitudesModificacionBean.C_IDPERSONA)%>">
							<input type="hidden" id="oculto<%=String.valueOf(recordNumber)%>_5" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=UtilidadesString.mostrarDatoJSP(row.getString("MODIFICACION"))%>">

							<% if (row.getString("IDESTADOSOLIC").equalsIgnoreCase(String.valueOf(ClsConstants.ESTADO_SOLICITUD_MODIF_PENDIENTE))){%>
								<input type="checkbox"  id="validado" name="validado" value="1">
								<input type="hidden" id="solicita_<%=String.valueOf(recordPendiente)%>" name="solicita_<%=String.valueOf(recordPendiente)%>" value="<%=row.getString(CenSolicitudesModificacionBean.C_IDSOLICITUD)%>">									
								<% recordPendiente++; %>
							<% } else { %>
								&nbsp;
							<% } %>									
						</td>
						
						<td><%=UtilidadesString.mostrarDatoJSP(row.getString("ESTADO"))%></td>  	
						<td><%=UtilidadesString.mostrarDatoJSP(row.getString("MODIFICACION"))%></td>  															
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
						<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),row.getString(CenSolicitudesModificacionBean.C_FECHAALTA)))%></td>  								
						<td><%=UtilidadesString.mostrarDatoJSP(row.getString(CenSolicitudesModificacionBean.C_DESCRIPCION))%></td>  																						
					</siga:FilaConIconos>
					
					<% recordNumber++;
				} 
			} %>		
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
						for (i = 0; i < dd.length; i++){
							dd[i].checked=1;		
						}	
						
					} else {
						dd.checked=1;		
					}
				}	
			}
		
			//Asociada al boton DesmarcarTodos
			function accionDesmarcarTodos() {		
				if (document.getElementById("solicita_1")!=null){
					var dd = document.getElementsByName("validado");
					if (dd.type != 'checkbox') {
						for (i = 0; i < dd.length; i++){
							dd[i].checked=0;		
						}
						
					} else {
						dd.checked=0;		
					}
				}	
			}
		
			//Asociada al boton ProcesarSolicitud
			function accionProcesarSolicitud() {	
				sub();	
				var datos = "";
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
								checked=true;		
							}	
						}
						
					} else {
						if (dd.checked==1){
							var aux="solicita_1";
							var solicitado=document.getElementById(aux);
							datos=datos + solicitado.value + "%";				
							checked=true;		
						}	
					}
					
				} else {
					fin();
				  	return false;
				}	
				
				if(checked){
					document.forms[0].solicitudes.value = datos;				
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
			function accionDenegarSolicitud () {
				var datos = "";
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
								checked=true;
							}
						}
						
					} else {
						if (dd.checked==1){
							var aux="solicita_1";
							var solicitado=document.getElementById(aux);
							datos=datos + solicitado.value + "%";						
						}	
					}
					
				} else {
					fin();
				 	return false;
				}	
				
				if(checked){
					document.forms[0].solicitudes.value = datos;								
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
		
		<html:form action="/CEN_BusquedaClientes.do" method="POST" target="mainWorkArea"  style="display:none">
			<input type="hidden" name="modo" value="editar">
			<html:hidden property = "actionModal" value = ""/>
			<html:hidden property = "tablaDatosDinamicosD" value = ""/>
			<html:hidden property = "filaSelD" value = ""/>
			<html:hidden property = "verFichaLetrado" value = ""/>
		</html:form>
		
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>
