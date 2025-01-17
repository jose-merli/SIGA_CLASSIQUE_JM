<!DOCTYPE html>
<html>
<head>
<!-- abrirListasDestinatarias.jsp -->
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
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	HttpSession ses=request.getSession();
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");


	// para saber hacia donde volver
	String busquedaVolver = (String) request.getSession().getAttribute("EnvEdicionEnvio");
	if (busquedaVolver==null) busquedaVolver="";
	
	//Recupero el nombre y tipo del envio
	String nombreEnv = (String)request.getAttribute("nombreEnv");
	String tipo = UtilidadesMultidioma.getDatoMaestroIdioma((String)request.getAttribute("tipo"),user);
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
				  	document.forms[0].modo.value = "nuevo";			
					var resultado=ventaModalGeneral("ListasDestinatariasForm","G");			
					
					if (resultado!=undefined && resultado!="NORMAL"){
						document.forms[0].modo.value = "insertar";			
						document.forms[0].idListaCorreo.value=resultado;						
						document.forms[0].submit();					
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
		</script>
		
	
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo
			titulo="envios.definirEnvios.destLista.cabecera" 
			localizacion="envios.definirEnvios.localizacion"
		/>
		
	</head>
	
	<body>
	
	<table class="tablaTitulo" align="center" cellspacing="0">

			<html:form  action="/ENV_Listas_Destinatarias.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = ""/>
				<html:hidden property = "hiddenFrame" value = "1"/>
		
				<html:hidden property = "idListaCorreo" value = ""/>
				<html:hidden property = "idEnvio" value = "<%=idEnvio%>"/>

			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>

				
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="envios.definir.literal.nombre"/> :&nbsp;<%=nombreEnv%> 				    
				&nbsp;&nbsp;&nbsp;
				<siga:Idioma key="envios.definir.literal.tipoenvio"/> :&nbsp;<%=tipo%> 				    
			</td>
		</tr>
	</table>
				
			<siga:Table 
		   	      name="tablaDatos"
		   		  border="1"
		   		  columnNames="envios.listas.literal.lista,
		   		  					 envios.listas.literal.descripcion,
		   		  					 envios.listas.literal.dinamica,"
		   		  columnSizes="20,60,10,10">

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
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones='B' clase="listaNonEdit" visibleConsulta="no" visibleEdicion="no">
					<td>
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=fila.getString("IDLISTACORREO")%>"/>
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=idEnvio%>"/>
					<%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBRE"))%></td>
					<td><%=UtilidadesString.mostrarDatoJSP(fila.getString("DESCRIPCION"))%></td>					
					<td><%
								if (fila.getString("DINAMICA").equals("S"))
								{ %>
									<siga:Idioma key="general.yes"/>
							<%} else {%>
									<siga:Idioma key="general.no"/>
							<%}%>
					</td>					
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:Table>


		<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
			<siga:ConjBotonesAccion botones="V,N" clase="botonesDetalle"  />
		
	<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="mainWorkArea" type="">
		<input type="hidden" name="actionModal" value="">
		<input type="hidden" name="modo" value="abrirBusquedaModal">
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
	
		