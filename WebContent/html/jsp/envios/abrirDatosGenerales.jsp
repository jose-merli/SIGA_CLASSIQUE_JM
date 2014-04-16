<!DOCTYPE html>
<%@page import="java.util.Enumeration"%>
<html>
<head>
<!-- abrirDatosGenerales.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.siga.gui.processTree.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
//	Enumeration en =  request.getParameterNames();
//	while (en.hasMoreElements()) {
//		String type = (String) en.nextElement();
//	System.out.println("type:"+type+"value:"+request.getParameter(type));
		
//	}
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("EnvEdicionEnvio");
	if (busquedaVolver==null) busquedaVolver="";

	String nombreEnv = (String)request.getAttribute("nombreEnv");
	String tipo = UtilidadesMultidioma.getDatoMaestroIdioma((String)request.getAttribute("tipo"),userBean);
	String idTipoEnvio =(String)request.getAttribute("idTipoEnvio");
	String idEnvio = (String)request.getAttribute("idEnvio");
	String idInstitucion = (String)request.getAttribute("idInstitucion");
	String idPlantillaEnvio = (String)request.getAttribute("idPlantillaEnvio");
	String idPlantillaGeneracion = (String)request.getAttribute("idPlantillaGeneracion");
	
	String  cambioPlantilla = UtilidadesString.getMensajeIdioma(userBean, "messages.envios.error.direccionNecesaria");
	
	Boolean existePlantilla = (Boolean)request.getAttribute("existePlantilla");
	String descargar = UtilidadesString.getMensajeIdioma(userBean,"envios.definirEnvios.visualizarPlantilla");
	
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script src="<html:rewrite page="/html/js/validacionStruts.js"/>" type="text/javascript"></script>
		<script language="JavaScript">
			var bPrimeraVez=true;
			
			function accionGuardar() 
			{	
				sub();
				//JTA Esto comentado funciona si se desea evitar que existan envios ordinarios sin plantilla definida
				//var insTipoEnvio = document.EnviosDatosGeneralesForm.idTipoEnvio.value;
				//if (insTipoEnvio=='2') {
					// if (document.EnviosDatosGeneralesForm.idPlantillaGeneracion.value==""){
						// var campo = '<siga:Idioma key="envios.definir.literal.plantillageneracion"/>';
  						// var msg = "<siga:Idioma key="errors.required"  arg0=' " + campo + "'/>";
						// alert (msg);
						// fin();
						// return false;					
					// } 
				//}
				EnviosDatosGeneralesForm.modo.value="Grabar";
				EnviosDatosGeneralesForm.submit();
			}
	
			function mensaje() 
			{		
				<% if (!existePlantilla.booleanValue()) { %>
					var men = '<siga:Idioma key="envios.definir.literal.avisoPlantilla"/>'
					alert(men);
				<% } %>
			}
	
			/*function accionVolver() 
			{		
				EnviosDatosGeneralesForm.action = "<%=app%>/ENV_DefinirEnvios.do?buscar=true";
				EnviosDatosGeneralesForm.modo.value="abrir";
				EnviosDatosGeneralesForm.target = "mainWorkArea";
				EnviosDatosGeneralesForm.submit();
			}*/
			
			
	
			function refrescarLocal()
			{			
				document.location.reload();			
			}
			
			function recargarCombos()
			{
<%
			if (!userBean.getAccessType().equals(SIGAPTConstants.ACCESS_READ))
			{
%>				
				document.getElementById("idPlantillaEnvio").onchange();
				
<%
			}
%>
			}
			
			function cambioCombo()
			{
				if (!bPrimeraVez)
				{				
					if (confirm('<siga:Idioma key="envios.definir.literal.aviso"/>'))
					{
						EnviosDatosGeneralesForm.modo.value="borrarCampos";
						EnviosDatosGeneralesForm.submit();
					}
					
					else
					{
						EnviosDatosGeneralesForm.reset();
					}
				}
				
				bPrimeraVez=false;
			}
			function descargar() 
			{
				sub();
				
				if (!validateEnviosDatosGeneralesForm(EnviosDatosGeneralesForm)){
					fin();
					return false;
				}
				if(document.EnviosDatosGeneralesForm.idPlantillaEnvio.value==''){
					var campo = '<siga:Idioma key="envios.definir.literal.plantilla"/>';
  					var msg = "<siga:Idioma key="errors.required"  arg0=' " + campo + "'/>";
					alert (msg);
					fin();
					return false;
				}
								
				if(document.EnviosDatosGeneralesForm.idPlantillaGeneracion.value==''){
					var campo = '<siga:Idioma key="envios.definir.literal.plantillageneracion"/>';
  					var msg = "<siga:Idioma key="errors.required"  arg0=' " + campo + "'/>";
					alert (msg);
					fin();
					return false;
				}
						

				
				EnviosDatosGeneralesForm.modo.value = "descargar";
				EnviosDatosGeneralesForm.submit();
				
				
			   	//ProgramacionForm.submit();
			   	//ProgramacionForm.modo.value = "modificar";
			}
		</script>
<html:javascript formName="EnviosDatosGeneralesForm" staticJavascript="false" />
		<siga:Titulo
			titulo="envios.definirEnvios.datosGenerales.cabecera" 
			localizacion="envios.definirEnvios.localizacion"
		/>
				
</head>

<body onLoad="recargarCombos();mensaje();">


			<html:form action="/ENV_Datos_Generales.do" method="POST" target="submitArea">
				<html:hidden property = "modo" styleId = "modo" value = ""/>
				<html:hidden property = "accion" styleId = "accion"  value = ""/>
				<html:hidden property = "idEnvio" styleId = "idEnvio"  value = "<%=idEnvio%>"/>
				<html:hidden property = "idTipoEnvio" styleId = "idTipoEnvio" value = "<%=idTipoEnvio%>"/>
				<html:hidden property = "idInstitucion" styleId = "idInstitucion" value = "<%=idInstitucion%>"/>

				<table class="tablaTitulo" align="center" cellspacing="0">
					<tr>
						<td id="titulo" class="titulitosDatos">
							<siga:Idioma key="envios.definir.literal.nombre"/>&nbsp;(*)&nbsp;:&nbsp;
<%
						if (userBean.getAccessType().equals(SIGAPTConstants.ACCESS_READ))
						{
%>
							<%=nombreEnv%>
<%
						}
						
						else
						{
%>
							<html:text property="descripcionEnvio" value="<%=nombreEnv%>" styleClass="boxCombo" size="70"/>
<%
						}
%>
&nbsp;&nbsp;&nbsp;							<siga:Idioma key="envios.definir.literal.tipoenvio"/> :&nbsp;<%=tipo%> 				    
						</td>
					</tr>
				</table>
				
				<table class="tablaCampos" align="center">
					<tr>
						
<%
					String parametro[] = new String[3];
			 		parametro[0] = idInstitucion;
			 		parametro[1] = idTipoEnvio;
			 		parametro[2] = idPlantillaEnvio;

					/*String parametro2[] = new String[2];
			 		parametro2[0] = idInstitucion;
			 		parametro2[1] = idTipoEnvio;*/

					String parametro3[] = new String[3];
			 		parametro3[0] = idPlantillaEnvio;
			 		parametro3[1] = idInstitucion;
			 		parametro3[2] = idTipoEnvio;

					ArrayList al = new ArrayList();
					al.add(idPlantillaEnvio);

					ArrayList al2 = new ArrayList();
					al2.add(idPlantillaGeneracion);
					
					if (userBean.getAccessType().equals(SIGAPTConstants.ACCESS_READ))
					{
%>
						<td class="labelText" style="width:120px">
							<siga:Idioma key="envios.definir.literal.plantilla"/>
						</td>
						<td>
							<siga:ComboBD nombre="idPlantillaEnvio" tipo="cmbPlantillaEnvios" clase="boxConsulta" ancho="600" obligatorio="true" parametro="<%=parametro%>" elementoSel="<%=al%>" accion="hijo:idPlantillaGeneracion" pestana="true" readOnly="true" obligatorioSinTextoSeleccionar="true"/>
						</td>
						</tr>
						<tr>
						<td class="labelText">
							<siga:Idioma key="envios.definir.literal.plantillageneracion"/>
						</td>
						<td>
							<siga:ComboBD nombre="idPlantillaGeneracion" tipo="cmbPlantillaGeneracion" clase="boxConsulta" ancho="600" obligatorio="false" parametro="<%=parametro3%>" elementoSel="<%=al2%>" hijo="true" pestana="t" readOnly="true"/>
						</td>
<%
					}
					
					else
					{
%>						
						<td class="labelText" style="width:120px">
							<siga:Idioma key="envios.definir.literal.plantilla"/>
						</td>
						<td>
							<siga:ComboBD nombre="idPlantillaEnvio" tipo="cmbPlantillaEnvios" ancho="600" clase="boxCombo" obligatorio="true" parametro="<%=parametro%>" elementoSel="<%=al%>" accion="hijo:idPlantillaGeneracion;cambioCombo();" pestana="true" obligatorioSinTextoSeleccionar="true"/>
						</td>
						</tr>
						<tr>
						<td class="labelText">
							<siga:Idioma key="envios.definir.literal.plantillageneracion"/>
						</td>
						<td>
							<siga:ComboBD nombre="idPlantillaGeneracion" tipo="cmbPlantillaGeneracion" ancho="600" clase="boxCombo" obligatorio="false" parametro="<%=parametro%>" elementoSel="<%=al2%>" hijo="true" pestana="true"/>
						</td>
						
				
<%
					}
%>
						<td >
						<input type="button" class="button" alt="<%=descargar%>" name="idButton"  onclick="return descargar();" value="<%=descargar%>"/>
						</td>
					</tr>
					

			<!-- RGG: cambio a formularios ligeros -->

			
		</html:form>

				</table>
	
				<siga:Table 
			   	      name="tablaDatos"
			   		  border="1"
			   		  columnNames="certificados.mantenimiento.literal.campo,certificados.mantenimiento.literal.formato,"
			   		  columnSizes="45,45,10"
 		   		      modal="P">
 		   		      
<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
					<html:hidden property = "actionModal" value = "P"/>
					<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%
				}

				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		Hashtable htFila = (Hashtable)vDatos.elementAt(i);
%>
		  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones='E,C' clase="listaNonEdit" visibleBorrado="no">												
						<td><input type="hidden" name="oculto<%=""+(i+1)%>_1" id="oculto<%=""+(i+1)%>_1" value="<%=htFila.get(EnvCamposBean.C_IDCAMPO)%>"/>
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" id="oculto<%=""+(i+1)%>_2" value="<%=htFila.get(EnvCamposBean.C_NOMBRE)%>"/>
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" id="oculto<%=""+(i+1)%>_3" value="<%=htFila.get(CerFormatosBean.C_IDFORMATO)%>"/>
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" id="oculto<%=""+(i+1)%>_4" value="<%=htFila.get(CerFormatosBean.C_DESCRIPCION)%>"/>
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" id="oculto<%=""+(i+1)%>_5" value="<%=htFila.get(EnvCamposEnviosBean.C_TIPOCAMPO)%>"/><%=UtilidadesString.mostrarDatoJSP(htFila.get(EnvCamposBean.C_NOMBRE))%></td>
						<td><%=UtilidadesString.mostrarDatoJSP(htFila.get(CerFormatosBean.C_DESCRIPCION))%></td>
					</siga:FilaConIconos>
<%
					}
				}
%>
				</siga:Table>

<c:catch var ="catchException">
   <bean:parameter id="origen" name="origen" />
   <bean:parameter id="datosEnvios" name="datosEnvios" />	
</c:catch>

<c:if test = "${catchException == null}">
	<input type="hidden" id="origen" value ="${origen}"/>
	<input type="hidden" id="datosEnvios" value ="${datosEnvios}"/>
<c:choose>
	<c:when test="${origen=='/JGR_ComunicacionEJG'}">
		<html:form  action="/JGR_EJG"  method="POST" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property="modo" value="editar"/>
			<html:hidden styleId = "idTipoEJG" property="idTipoEJG" />
			<html:hidden styleId = "anio" property="anio"/>
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion"/>
			<html:hidden styleId = "origen" property="origen"/>
		</html:form>
	</c:when>
	<c:otherwise>
		<html:form action="/JGR_MantenimientoDesignas" method="post" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property = "modo"   value="editar"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion" value=""/>
			<html:hidden styleId = "anio" property="anio" />
			<html:hidden styleId = "idTurno" property="idTurno" />
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "origen" property="origen" />
		</html:form>	
	</c:otherwise>
</c:choose>
</c:if>	

<siga:ConjBotonesAccion botones="V,G" clase="botonesDetalle"  />

<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
	<input type="hidden" name="actionModal" value="">
	<input type="hidden" name="modo" value="abrirBusquedaModal">
</html:form>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
		
		<%@ include file="/html/jsp/envios/includeVolver.jspf" %>
				
	</body>
</html>