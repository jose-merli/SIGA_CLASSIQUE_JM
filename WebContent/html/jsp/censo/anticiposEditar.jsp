<!DOCTYPE html>
<html>
<head>
<!-- anticiposEditar.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%> 

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.beans.PysAnticipoLetradoBean"%>
<%@ page import="com.siga.beans.PysServicioAnticipoBean"%>
<%@ page import="com.siga.censo.form.AnticiposClienteForm"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.UtilidadesHash"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<%  
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usr = (UsrBean) ses.getAttribute("USRBEAN");
	AnticiposClienteForm formulario = (AnticiposClienteForm)request.getAttribute("AnticiposClienteForm");

	//recogemos los datos
	Hashtable resultado = (Hashtable)request.getAttribute("resultado");
	Vector v = (Vector)request.getAttribute("serviciosAnticipo");

	//variables que se van a mostrar en la jsp
	String descripcion="", importeAnticipado="", importeRestante="", idPersona="", idAnticipo="", fecha="" ;
	try {
		descripcion = (String)resultado.get(PysAnticipoLetradoBean.C_DESCRIPCION);
		importeAnticipado = (String)resultado.get(PysAnticipoLetradoBean.C_IMPORTEINICIAL);
		importeRestante = (String)resultado.get("IMPORTERESTANTE");
		idPersona = (String)resultado.get(PysAnticipoLetradoBean.C_IDPERSONA);
		idAnticipo = (String)resultado.get(PysAnticipoLetradoBean.C_IDANTICIPO);
		formulario.setCtaContable((String)resultado.get(PysAnticipoLetradoBean.C_CTACONTABLE));
		fecha = GstDate.getFormatedDateShort(usr.getLanguage(),(String)resultado.get(PysAnticipoLetradoBean.C_FECHA));
	} catch(Exception e) {
		idPersona = formulario.getIdPersona();
	}

	//recuperamos el modo de acceso
	String modo = (descripcion==null || descripcion.equals("") ? "insertar" : "modificar");

	String botonesAccion="Y", botonesRegistro="", estilo="box", estiloNumber="boxNumber";
	boolean readOnly=false, readOnlyImporte=false;
	if (formulario.getAccion().equals("Ver") || !importeRestante.equals(importeAnticipado)) {
		botonesAccion="";
		estilo="boxConsulta";
		estiloNumber="boxConsultaNumber";
		readOnly=true;
		readOnlyImporte=true;
		
	} else if (formulario.getAccion().equals("Editar")) {
		estiloNumber="boxConsultaNumber";
		readOnlyImporte=true;
	}
%>	

	<script language="JavaScript">
		function accionNuevo() {										
	    	document.AnticiposClienteForm.modo.value = "abrirServicios";
			// Abro ventana modal y refresco si necesario
			var resultado = ventaModalGeneral(document.AnticiposClienteForm.name,"M");
			
			if (resultado=='MODIFICADO') {
				parent.cargaContenidoModal();	
			}
		}
		
		function refrescarLocal() {
			parent.cargaContenidoModal();		
		}
		
		// Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {
			sub();		
			document.forms[0].importeAnticipado.value=document.forms[0].importeAnticipado.value.replace(/,/,".");
			if (validateAnticiposClienteForm(document.AnticiposClienteForm)){
				document.forms[0].modo.value="<%=modo%>";
				document.forms[0].submit();					
			} else {
				fin();
				return false;
			}
		}
		
		// Asociada al boton Cerrar
		function accionCerrar() {
			window.top.returnValue="MODIFICADO";
			window.parent.close();
		}
		
		function borrarServicios(fila) {		
			var datos = document.getElementById('tablaDatosDinamicosD');
			datos.value = ""; 
			var flag = true;
			var j = 1;
			while (flag) {
				var aux = 'oculto' + fila + '_' + j;
				var oculto = document.getElementById(aux);
				if (oculto == null)  { 
					flag = false; 
				} else { 
					datos.value = datos.value + oculto.value + ','; 
				}
			  	j++;
			}
			datos.value = datos.value + "%";
									
	    	document.AnticiposClienteForm.modo.value = "borrarServicios";
			document.AnticiposClienteForm.submit();
		}		
	</script>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
	
	<html:javascript formName="AnticiposClienteForm" staticJavascript="false" />
</head>

<body>
	<table class="tablaTitulo" cellspacing="0">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="censo.anticipos.mantenimiento.cabecera"/>
			</td>
		</tr>
	</table>

	<html:form action="/CEN_AnticiposCliente.do" method="POST" target="submitArea">	
		<html:hidden name="AnticiposClienteForm" property="modo" value="<%=modo%>"/>
		<html:hidden name="AnticiposClienteForm" property="idInstitucion" value="<%=usr.getLocation()%>" />
		<html:hidden name="AnticiposClienteForm" property="idPersona" value="<%=idPersona%>"/>
		<html:hidden name="AnticiposClienteForm" property="idAnticipo" value="<%=idAnticipo%>"/>

		<table class="tablaCentralCamposMedia" cellspacing=0 cellpadding=0 align="center" border="0">
			<tr>				
				<td>
					<siga:ConjCampos leyenda="censo.anticipos.leyenda">				
						<table class="tablaCampos" align="center" border="0" width="100%">
							<tr>							
								<td class="labelText"><siga:Idioma key="censo.anticipos.literal.descripcion"/>&nbsp;(*)</td>				
								<td colspan="3">
									<html:text name="AnticiposClienteForm" property="descripcion" styleId="descripcion" size="54" maxlength="200" styleClass="<%=estilo%>" readonly="<%=readOnly%>"  value="<%=descripcion%>"/>
								</td>
							</tr>

<% 
							if (!modo.equals("insertar"))  { 
%>
								<tr>				
									<td class="labelText"><siga:Idioma key="censo.anticipos.literal.fecha"/></td>				
									<td colspan="3">
										<html:text name="AnticiposClienteForm" property="fecha" styleId="fecha" size="12" maxlength="10" styleClass="boxConsulta" readonly="true"  value="<%=fecha%>"/>
									</td>
								</tr>
<% 
							} 
%>

							<tr>
								<td class="labelText"><siga:Idioma key="censo.anticipos.literal.ctaContable"/></td>				
								<td colspan="3">
									<html:text name="AnticiposClienteForm" property="ctaContable" styleId="ctaContable" size="12" maxlength="20" styleClass="<%=estilo%>" readonly="<%=readOnly%>"/>
								</td>
							</tr>

							<tr>
								<td class="labelText"><siga:Idioma key="censo.anticipos.literal.importeAnticipado"/>&nbsp;(*)</td>
<% 
								if (modo.equals("insertar"))  { 
%>								
									<td>
										<html:text name="AnticiposClienteForm" property="importeAnticipado" styleId="importeAnticipado" size="11" maxlength="11" styleClass="<%=estiloNumber%>" readonly="<%=readOnlyImporte%>" value="<%=UtilidadesNumero.formatoCampo(importeAnticipado)%>"/>&nbsp;&euro; 
									</td>
<% 
								} else { 
%>								
									<td align="right"><%=UtilidadesString.formatoImporte(importeAnticipado)%>&nbsp;&euro;</td>
<%
								}

								if (!modo.equals("insertar"))  { 
%>

									<td class="labelText"><siga:Idioma key="censo.anticipos.literal.importeRestante"/></td>
									<td align="right"><%=UtilidadesString.formatoImporte(importeRestante)%>&nbsp;&euro;</td>
<% 
								} 
%>
							</tr>				
						</table>
					</siga:ConjCampos>
				</td> 
			</tr>
		</table>
	</html:form>	
<% 
	if (modo.equals("insertar")) { 
%>
	 	<siga:ConjBotonesAccion botones="C,Y" modal="M" />
<% 
	} else  { 
%>	
		<siga:ConjBotonesAccion botones="<%=botonesAccion%>" clase="botonesSeguido" modal="M" titulo="censo.anticipos.literal.serviciosConfigurados"/>

		<siga:Table 
	   		name="tablaResultados"
	   		border="1"
	   		columnNames="censo.anticipos.literal.nombreServicio,"
	   		columnSizes="90,10"
	   		modalScroll="true"
	   		modal="M">

<% 
			if (v!=null && v.size()>0) {
				for (int i = 0; i < v.size(); i++) { 
					Hashtable hash = (Hashtable)v.get(i);
					if (hash!=null) {
						String idInstitucionReg = UtilidadesHash.getString (hash, PysServicioAnticipoBean.C_IDINSTITUCION);
						String idPersonaReg = UtilidadesHash.getString(hash, PysServicioAnticipoBean.C_IDPERSONA);
						String idAnticipoReg = UtilidadesHash.getString(hash, PysServicioAnticipoBean.C_IDANTICIPO);
						String idTipoServicioReg = UtilidadesHash.getString(hash, PysServicioAnticipoBean.C_IDTIPOSERVICIOS);
						String idServicioReg = UtilidadesHash.getString(hash, PysServicioAnticipoBean.C_IDSERVICIO);
						String idServicioInstitucionReg = UtilidadesHash.getString(hash, PysServicioAnticipoBean.C_IDSERVICIOSINSTITUCION);
						String descripcionServicioReg = UtilidadesHash.getString(hash, "DESCRIPCION");
						
						FilaExtElement[] elems=new FilaExtElement[1];				
						if (formulario.getAccion().equals("Editar") && importeRestante.equals(importeAnticipado)) {
							elems[0]=new FilaExtElement("borrarServicios","borrarServicios",SIGAConstants.ACCESS_READ);							
						}
%>
						<siga:FilaConIconos fila='<%=String.valueOf(i+1)%>' visibleConsulta="no" visibleBorrado="no" visibleEdicion="no" pintarEspacio="no" botones="" elementos='<%=elems%>'  modo='<%=modo%>' clase="listaNonEdit">
							<td>
								<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_1" name="oculto<%=String.valueOf(i+1)%>_1" value="<%=idInstitucionReg%>">
								<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_2" name="oculto<%=String.valueOf(i+1)%>_2" value="<%=idPersonaReg%>">
								<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_3" name="oculto<%=String.valueOf(i+1)%>_3" value="<%=idAnticipoReg%>">
								<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_4" name="oculto<%=String.valueOf(i+1)%>_4" value="<%=idTipoServicioReg%>">
								<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_5" name="oculto<%=String.valueOf(i+1)%>_5" value="<%=idServicioReg%>">
								<input type="hidden" id="oculto<%=String.valueOf(i+1)%>_6" name="oculto<%=String.valueOf(i+1)%>_6" value="<%=idServicioInstitucionReg%>">								
								<%=UtilidadesString.mostrarDatoJSP(descripcionServicioReg)%>
							</td>								
						</siga:FilaConIconos>				
<%
					}
				}
			} else { // No hay registros 
%>
				<tr class="notFound">
		  			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>	
<% 
			} 
%>
		</siga:Table>
	 
<% 
		if (formulario.getAccion().equals("Editar")) {
%>
 			<siga:ConjBotonesAccion botones="N,C" modal="M" />
<% 
		} else { 
%>
 			<siga:ConjBotonesAccion botones="C" modal="M" />
<% 
		} 
	} 
%>	

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>