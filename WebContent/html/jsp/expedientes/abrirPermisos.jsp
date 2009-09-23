<!-- abrirPermisos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants,com.atos.utils.*,com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.expedientes.action.PermisosTiposExpedientesAction"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	Vector vDatos = (Vector)ses.getAttribute("DATABACKUP");
	UsrBean user=(UsrBean) ses.getAttribute("USRBEAN");
	String tipoacceso=user.getAccessType();
	boolean bLectura = tipoacceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_READ);
	
	
	//Recupero el nombre del expediente
	String nombreExp = (String)request.getAttribute("nombreExp");

%>	

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>


	
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
	
	
			<!-- Asociada al boton Volver -->
			function accionVolver() 
			{		
				top.frames["mainWorkArea"].location.href='<%=app%>/Dispatcher.do?proceso=41';
			}

			<!-- Asociada al boton Restablecer -->
			function accionRestablecer() 
			{		
				PermisosTiposExpedientesForm.reset();
			}
			
			<!-- Asociada al boton Guardar -->
			function accionGuardar() 
			{		
				PermisosTiposExpedientesForm.submit();
			}
			
			function refrescarLocal()
			{			
				document.location.reload();			
			}
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->		

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="expedientes.tipoExpedientes.configuracion.permisos.cabecera" 
			localizacion="expedientes.tipoExpedientes.configuracion.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->		
	</head>
	
	<body>
			<html:form  action="/EXP_TiposExpedientes_Permisos.do" method="POST" target="submitArea">
				<html:hidden property = "modo" value = "modificar"/>
				<html:hidden property = "hiddenFrame" value = "1"/>
	
	<table class="tablaTitulo" align="center" cellspacing="0">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="expedientes.literal.tiposexpedientes"/> :&nbsp;<%=nombreExp%> 				    
			</td>
		</tr>
		 
	</table>
	
				
			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="expedientes.tiposexpedientes.literal.grupo,
		   		  					 expedientes.tiposexpedientes.literal.accesototal,
		   		  					 expedientes.tiposexpedientes.literal.accesolectura, 
		   		  					 expedientes.tiposexpedientes.literal.accesodenegado,
		   		  					 expedientes.tiposexpedientes.literal.sinacceso"
		   		  tamanoCol="52,12,12,12,12"
		   			alto="100%"
		   			ajusteBotonera="true"		

		   		  >

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br>
<%
				} else {
%>
				
					<html:hidden property="idInstitucion"/>
					<html:hidden property="idInstitucionTipoExpediente"/>
					<html:hidden property="idTipoExpediente"/>
					<html:hidden property="acceso"/>
					
	<logic:iterate id="perfil" 
						name="PermisosTiposExpedientesForm" 
						property="perfiles" >
					<tr class="listaNonEdit">
					<html:hidden indexed="true" name="perfil" property="idPerfil"/>
					<html:hidden indexed="true" name="perfil" property="nombrePerfil"/>
		
	  				<td>
					 <bean:write  name="perfil" property="nombrePerfil"/>	
					</td>
					<td align="center">
						<html:radio  indexed="true" name="perfil" property="derechoAcceso" disabled = "<%=bLectura%>" value="<%=PermisosTiposExpedientesAction.nACCESS_FULL%>"/>
					</td>
					<td align="center">
						<html:radio  indexed="true" name="perfil" property="derechoAcceso" disabled = "<%=bLectura%>" value="<%=PermisosTiposExpedientesAction.nACCESS_READ%>"/>
					</td class="labelText">
					<td align="center">
						<html:radio  indexed="true" name="perfil" property="derechoAcceso" disabled = "<%=bLectura%>" value="<%=PermisosTiposExpedientesAction.nACCESS_DENY%>"/>
					</td>
					<td align="center">
						<html:radio  indexed="true" name="perfil" property="derechoAcceso" disabled = "<%=bLectura%>" value="<%=PermisosTiposExpedientesAction.nACCESS_NONE%>"/>
					</td> 
					</tr>
						</logic:iterate>
<%
					}
				
%>
			</siga:TablaCabecerasFijas>
			
		
			
			</html:form>

			<!-- G Guardar, Y GuardaryCerrar, R Reestablecer, C Cerrar, X Cancelar -->
	<%	if (tipoacceso.equalsIgnoreCase(SIGAPTConstants.ACCESS_READ)){ %>
				<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />
	<% } else{ %>
				<siga:ConjBotonesAccion botones="V,R,G" clase="botonesDetalle"  />
	<% } %>
			
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>
	
		