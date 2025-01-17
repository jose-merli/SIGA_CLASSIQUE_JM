<!DOCTYPE html>
<html>
<head>
<!-- abrirRemitentes.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.siga.administracion.SIGAConstants,com.atos.utils.*,com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Vector vDatos = (Vector)request.getAttribute("datos");
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	String tipoacceso=user.getAccessType();
	
	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("EnvEdicionEnvio");
	if (busquedaVolver==null) busquedaVolver="";
	
	//Recupero el nombre y tipo del envio
	String nombreEnv = (String)request.getAttribute("nombreEnv");
	String tipo = UtilidadesMultidioma.getDatoMaestroIdioma((String)request.getAttribute("tipo"),user);
	String idTipoEnvio =((Integer)request.getAttribute("idTipoEnvio")).toString();
	String acuseRecibo = (String)request.getAttribute("acuseRecibo");
	String idEnvio = (String)request.getParameter("idEnvio");
	
	request.removeAttribute("datos");	
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	

		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
			<!-- Asociada al boton Nuevo -->
			function accionNuevo() 
			{		
				  var cliente = ventaModalGeneral("busquedaClientesModalForm","G");			
				  if (cliente!=undefined && cliente[0]!=undefined ){
						document.forms[0].modo.value="nuevo";
						document.forms[0].idPersona.value=cliente[0];
						document.forms[0].idInstitucion.value=cliente[1];
						document.forms[0].numColegiado.value=cliente[2];
						document.forms[0].nifcif.value=cliente[3];
						document.forms[0].nombre.value=cliente[4];
						document.forms[0].apellidos1.value=cliente[5];
						document.forms[0].apellidos2.value=cliente[6];
						
						var result = ventaModalGeneral("RemitentesForm","G");
						if (result == "MODIFICADO"){
							refrescarLocal();
						}
					}	
			}
	
			<!-- Asociada al boton Volver -->
			/*function accionVolver() 
			{		
				document.forms[0].action = "<%=app%>/ENV_DefinirEnvios.do?buscar=true";
				document.forms[0].modo.value="abrir";
				document.forms[0].target = "mainWorkArea";
				document.forms[0].submit();
			}*/
	
			function refrescarLocal()
			{			
				document.location.reload();			
			}
			function accionGuardar() 
			{		
				if(document.getElementById("idCheckAcuseRecibo").checked)
					document.EnviosDatosGeneralesFormEdicion.acuseRecibo.value = "1";
				else
					document.EnviosDatosGeneralesFormEdicion.acuseRecibo.value = "0";
						
				document.EnviosDatosGeneralesFormEdicion.submit();
	
			}
			function inicio(){
				if(document.getElementById("valorAcuseRecibo").value=="1")
					document.getElementById("idCheckAcuseRecibo").checked = "checked";
				else
					document.getElementById("idCheckAcuseRecibo").checked = "";
					
			}
	
		</script>
		
		
		
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo
			titulo="envios.definirEnvios.remitentes.cabecera" 
			localizacion="envios.definirEnvios.localizacion"
		/>
		
	</head>
	
	<body onload="inicio();">
	
	<table class="tablaTitulo" align="center" cellspacing="0">
			<html:form  action="/ENV_Remitentes.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property = "hiddenFrame" value = "1"/>
				<html:hidden property = "accion" value = ""/>
				
				<html:hidden property = "idEnvio" value = "<%=idEnvio%>"/>
				<html:hidden property = "idTipoEnvio" value = "<%=idTipoEnvio%>"/>
				<html:hidden property = "idPersona" value = ""/>
				<html:hidden property = "idInstitucion" value = ""/>
				<html:hidden property = "numColegiado" value = ""/>
				<html:hidden property = "nifcif" value = ""/>
				<html:hidden property = "nombre" value = ""/>
				<html:hidden property = "apellidos1" value = ""/>
				<html:hidden property = "apellidos2" value = ""/>
				<html:hidden property = "descripcion" value = ""/>
				
				<html:hidden property = "domicilio" value = ""/>
				<html:hidden property = "cp" value = ""/>
				<html:hidden property = "idPoblacion" value = ""/>
				<html:hidden property = "idProvincia" value = ""/>
				<html:hidden property = "idPais" value = ""/>
				<html:hidden property = "fax1" value = ""/>
				<html:hidden property = "fax2" value = ""/>
				<html:hidden property = "correoElectronico" value = ""/>

		</html:form>

				
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="envios.definir.literal.nombre"/> :&nbsp;<%=nombreEnv%> 				    
				&nbsp;&nbsp;&nbsp;
				<siga:Idioma key="envios.definir.literal.tipoenvio"/> :&nbsp;<%=tipo%> 				    
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td class="labelText"><siga:Idioma key="envios.definir.literal.acuseRecibo"/></td>
			<td ><input type="checkbox" id="idCheckAcuseRecibo">				    
				 <input type="hidden" id="valorAcuseRecibo" value=<%=acuseRecibo%>>
			</td>
		</tr>
	</table>
			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="envios.listas.literal.nombreyapellidos,
		   		  					 gratuita.retencionesIRPF.literal.descripcion,
		   		  					 censo.fichaCliente.literal.colegiado,
		   		  					 censo.busquedaClientes.literal.nif,"
		   		  columnSizes="30,40,10,10,10"
		   		  modal="g">

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%
				}

				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		Row fila = (Row)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones='C,E,B' clase="listaNonEdit" visibleConsulta="no" visibleEdicion="no">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=fila.getString("IDPERSONA")%>"/>
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=idEnvio%>"/>
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=idTipoEnvio%>"/>
					<%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBREYAPELLIDOS"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("DESCRIPCION"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NCOLEGIADO"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("NIFCIF"))%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:Table>


		<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
			<siga:ConjBotonesAccion botones="V,N,G" clase="botonesDetalle"  />
		
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
		<input type="hidden" name="clientes" value="1">
	</html:form>
	
	<html:form action="/ENV_Datos_Generales" name="EnviosDatosGeneralesFormEdicion" type="com.siga.envios.form.SIGAEnviosDatosGeneralesForm" method="POST" target="submitArea">
		<html:hidden property="modo" value="modificarAcuseRecibo"/>
		<html:hidden property="idEnvio" value = "<%=idEnvio%>"/>
		<html:hidden property="idTipoEnvio" value = "<%=idTipoEnvio%>"/>
		<html:hidden property="acuseRecibo" value=""/>
		
		
	</html:form>
	
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
	
	<%@ include file="/html/jsp/envios/includeVolver.jspf" %>
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
	
		