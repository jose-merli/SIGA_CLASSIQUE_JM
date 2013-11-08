<!DOCTYPE html>
<html>
<head>
<!-- solicitudModificacionCV.jsp -->
<!-- EJEMPLO DE VENTANA DENTRO DE VENTANA MODAL MEDIANA -->
<!-- Contiene la zona de campos del registro y la zona de botones de acciones sobre el registro 
	 VERSIONES:
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix = "siga"%>
<%@ taglib uri = "struts-bean.tld"  	prefix = "bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix = "html"%>
<%@ taglib uri = "struts-logic.tld" 	prefix = "logic"%>

<!-- IMPORTS -->
<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.beans.CenDatosCVBean"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean usr = (UsrBean)request.getSession().getAttribute("USRBEAN");
	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");
	

	String DB_TRUE=ClsConstants.DB_TRUE;
	String DB_FALSE=ClsConstants.DB_FALSE;	
	String modo=(String)request.getAttribute("modoConsulta");	
	boolean solicitarNuevo=((modo != null) && (modo.equalsIgnoreCase("solicitarNuevo")));
	ArrayList lista=new ArrayList();
	ArrayList idSubtipo1 = new ArrayList();
	ArrayList idSubtipo2 = new ArrayList();
	String idCV="";
	String fechaInicio="";
	String fechaFin="";
	String descripcion="";
	String paramIdTipoCV = "";
	if(!solicitarNuevo){
		Hashtable htData=(Hashtable)request.getAttribute("hDatos");	
		idCV=(String)htData.get(CenDatosCVBean.C_IDCV);
		lista.add(String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCV)));
		paramIdTipoCV = "{\"idtipocv\":\""+String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCV))+"\"}";
		idSubtipo1.add(String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO1)+"@"+htData.get(CenDatosCVBean.C_IDINSTITUCION_SUBT1)));
		idSubtipo2.add(String.valueOf(htData.get(CenDatosCVBean.C_IDTIPOCVSUBTIPO2)+"@"+htData.get(CenDatosCVBean.C_IDINSTITUCION_SUBT2)));
		fechaInicio=GstDate.getFormatedDateShort("",(String)htData.get(CenDatosCVBean.C_FECHAINICIO));
		fechaFin=GstDate.getFormatedDateShort("",(String)htData.get(CenDatosCVBean.C_FECHAFIN));
		descripcion=String.valueOf(htData.get(CenDatosCVBean.C_DESCRIPCION));
		}	
		
  String parametro[] = new String[1];
  parametro[0] = (String)usr.getLocation();
%>


<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="datosCVSolicForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
		function accionCerrar(){	
			window.top.close();
		}	
		
		function accionRestablecer(){	
			if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
				document.all.datosCVSolicForm.reset();				
				}
		}
		
		function accionGuardarCerrar() {	
			sub();	
			if (!validateDatosCVSolicForm(document.datosCVSolicForm)){	
				fin();
				return false;
			}		
			
		   
			if (jQuery("#tipoApunte").val()==""){
			    aux = '<siga:Idioma key="censo.datosCV.literal.tipoApunte1"/>';
				alert(aux);
				fin();
				return false;
			} else if ( (jQuery("#idTipoCVSubtipo1").is(":visible") && jQuery("#idTipoCVSubtipo1").val() == "") || 
						(jQuery("#idTipoCVSubtipo2").is(":visible") && jQuery("#idTipoCVSubtipo2").val() == "") ){
				aux = '<siga:Idioma key="censo.datosCV.literal.tipoApunte"/>';
				alert(aux);
				fin();
				return false;
			}
			  
			if (compararFecha (document.datosCVSolicForm.fechaInicio, document.datosCVSolicForm.fechaFin) == 1) {
				alert ("<siga:Idioma key="messages.fechas.rangoFechas"/>");
				fin();
				return false;
			}	
			document.all.datosCVSolicForm.modo.value = "insertarModificacion";					
			document.all.datosCVSolicForm.submit();						
		}
        
	</script>	

	<!-- INICIO: TITULO Y LOCALIZACION 	-->	

</head>
<body>
<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="censo.consultaDatosCV.literal.titulo1"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
				     <%if ((numero != null) && (!numero.equalsIgnoreCase(""))){%>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numero)%>
					<%} 
					else {%>
					   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>
				</td>
			</tr>
		</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	
	<html:form action="/CEN_SolicitudCV.do" method="POST" target="submitArea">
	
		<div id="camposRegistro" class="posicionModalMedia" align="center">

		<!-- INICIO: CAMPOS -->
		<html:hidden property="modo" value="cerrar"/>
		<input type='hidden' name="idPersona" value="<%=String.valueOf((Long)request.getAttribute("idPersona"))%>"/>	
		<input type='hidden' name="idInstitucion" value="<%=String.valueOf((Integer)request.getAttribute("idInstitucion"))%>"/>
		<input type='hidden' name="idCV" value="<%=idCV%>">
		<table class="tablaCentralCamposMedia" align="center">			
			<tr>				
				<td>
					<siga:ConjCampos leyenda="censo.solicitudModificacion.literal.titulo">
						<table class="tablaCampos" align="center">	
							<!-- FILA -->
							<tr>		
								<td class="labelText">
									<siga:Idioma key="censo.datosCV.literal.tipo"/> &nbsp;(*)
								</td>
								<td >
									<siga:Select id="tipoApunte"
													queryId="getCenTiposCV"
													selectedIds="<%=lista%>"
													childrenIds="idTipoCVSubtipo1,idTipoCVSubtipo2"
													queryParamId="idtipocv" /> 
								</td>	
								<td >
									<siga:Select id="idTipoCVSubtipo1"
													queryId="getCenTiposCVsubtipo1"
													params="<%=paramIdTipoCV%>"
													selectedIds="<%=idSubtipo1%>"
													parentQueryParamIds="idtipocv"
													hideIfnoOptions="true"/>
								</td>		
								<td >
									<siga:Select id="idTipoCVSubtipo2"
													queryId="getCenTiposCVsubtipo2" 
													params="<%=paramIdTipoCV%>"
													selectedIds="<%=idSubtipo2%>"
													parentQueryParamIds="idtipocv"
													hideIfnoOptions="true"/>
								</td>									
							</tr>
							<tr><td>&nbsp</td></tr>		
								<!-- FILA -->
							<tr>					
								<td class="labelText">
									<siga:Idioma key="censo.datosCV.literal.fechaInicio"/> &nbsp;(*)
								</td>
								<td>
									<siga:Fecha  nombreCampo= "fechaInicio" valorInicial="<%=fechaInicio%>"/>
								</td>						
								<td class="labelText">
									<siga:Idioma key="censo.datosCV.literal.fechaFin"/> 
								</td>	
								<td>
									<siga:Fecha  nombreCampo= "fechaFin" valorInicial="<%=fechaFin%>"/>
								<br>
								</td>									
							</tr>
							<tr><td>&nbsp</td></tr>		
		   				<!-- FILA -->
		  				<tr>						
								<td class="labelText">
									<siga:Idioma key="censo.datosCV.literal.descripcion"/>&nbsp;(*)		 			
								</td>							
								<td COLSPAN=3>
										<html:textarea name="datosCVSolicForm" property="descripcion" onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)" styleClass="box" value='<%=descripcion%>' maxlength="255" cols="80" rows="3"/>
								<br>
								</td>								
							</tr>
							<tr><td>&nbsp</td></tr>		
		   				<!-- FILA -->
		  				<tr>
		   					<td class="labelText">
									<siga:Idioma key="censo.datosCV.literal.motivo"/>&nbsp;(*)
								</td>											
		   					<td COLSPAN=3>
		   						<html:textarea name="datosCVSolicForm" property="motivo" styleClass="box" value="" cols="80" rows="3"/> 
								</td>		   					
		  				</tr>
		   			</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</table>		
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
		<siga:ConjBotonesAccion botones="C,Y,R" modal="M"/>


	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
</div>
<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</html:form>
</body>
</html>
