<!DOCTYPE html>
<html>
<head>
<!-- busquedaSolicitudesIncorporacionResultados.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">

<%@taglib uri	=	"struts-tiles.tld" 			prefix="tiles" 		%>
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri = "struts-html.tld" 			prefix="html" 		%>
<%@taglib uri = "libreria_SIGA.tld" 		prefix="siga"			%>

<%@ page import="com.siga.beans.CenSolicitudIncorporacionBean" %>
<%@ page import="com.siga.Utilidades.UtilidadesString" %>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.Utilidades.UtilidadesMultidioma"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="com.atos.utils.ClsConstants" %>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%
	String app = request.getContextPath(); 
	Vector resultados = (Vector) request.getAttribute("resultados");

	request.getSession().setAttribute("EnvEdicionEnvio","GSI");
	UsrBean user = (UsrBean) request.getSession().getAttribute("USRBEAN");

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">


	<title><siga:Idioma key="censo.SolicitudIncorporacionDatos.titulo"/></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<script language="javascript">
		function refrescarLocalArray(arrayDatos) {
			document.busquedaClientesForm.action=document.busquedaClientesForm.action+"?editarNColegiado=1";
			document.busquedaClientesForm.idPersona.value=arrayDatos[0];
			document.busquedaClientesForm.idInstitucion.value=arrayDatos[1];
			document.busquedaClientesForm.modo.value="recargarEditar";
			document.busquedaClientesForm.submit();
			// tambien refresco
			refrescarLocal();
		}
		function refrescarLocal() {
			parent.buscar();
		}

		function enviar(fila)
		{
		   	
		   	var auxSol = 'oculto' + fila + '_1';
		    var idSolic = document.getElementById(auxSol);			          		
		   				   	
		   	//var auxPers = 'oculto' + fila + '_3';
		    //var idPers = document.getElementById(auxPers);	
			var idPers = "";		    	    
		    	    
		    var auxDesc = 'oculto' + fila + '_2';
		    var desc = document.getElementById(auxDesc);			    

		    
		    document.DefinirEnviosForm.idSolicitud.value=idSolic.value;
		   	document.DefinirEnviosForm.idPersona.value="";
		   	document.DefinirEnviosForm.descEnvio.value="Solicitud "+desc.value;
		   	document.DefinirEnviosForm.subModo.value="solicitudIncorporacion";
		   	
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

<body>


		<html:form action="/CEN_SolicitudesIncorporacion.do" method="POST" style="display:none" target="mainWorkArea" styleId="SolicitudIncorporacionForm">
			<html:hidden styleId = "esModal"  property = "esModal" value = "N"/>
			<html:hidden styleId = "modo"  property = "modo" value = ""/>
			<html:hidden styleId = "buscarModoAnteriorBusqueda" property = "buscarModoAnteriorBusqueda" value = "true"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" id="actionModal" name="actionModal" value="">
		</html:form>
		
			<siga:Table 
			   name = "tablaResultados"
			   border  = "1"
			   columnNames="censo.consultaSolicitudesIncorporacion.literal.nifcif,
	   					  censo.consultaSolicitudesIncorporacion.literal.nombre,
	   					  censo.consultaSolicitudesIncorporacion.literal.nColegiado,
	   					  censo.consultaSolicitudesIncorporacion.literal.tipo,
	   					  censo.consultaSolicitudesIncorporacion.literal.fechaSolicitud,
	   					  censo.consultaSolicitudesIncorporacion.literal.estado,
	   					  censo.consultaSolicitudesIncorporacion.literal.fechaEstado,"
			   columnSizes = "13,19,8,16,10,15,9,10"
			   fixedHeight="90%">

		<%if (resultados != null) { %>

	
				<%	 for (int i = 1; i <= resultados.size(); i++) { 
							 Hashtable aux = (Hashtable) resultados.get(i-1);
							 if (aux != null){ 
							 		CenSolicitudIncorporacionBean bean = (CenSolicitudIncorporacionBean) aux.get("bean");
							 		String tipoSol   = UtilidadesString.mostrarDatoJSP(UtilidadesMultidioma.getDatoMaestroIdioma((String) aux.get("tipoSol"),user));
							 		String estadoSol = UtilidadesString.mostrarDatoJSP(UtilidadesMultidioma.getDatoMaestroIdioma((String) aux.get("estadoSol"),user));
									String iconoAlarma = (String) aux.get("alarma");
							 		if ((iconoAlarma == null) || iconoAlarma.equals(""))estadoSol = "SINALARMA";

									// boton de envios
									FilaExtElement[] elems = new FilaExtElement[1];
									elems[0]=new FilaExtElement("enviar", "enviar", SIGAConstants.ACCESS_READ);

									String botones = "C,E";
									if (bean.getIdEstado() != null && bean.getIdEstado().intValue() == ClsConstants.ESTADO_SOLICITUD_APROBADA) {
										botones = "C";
									} 
   	 		%>
									<siga:FilaConIconos fila='<%=""+i%>' botones="<%=botones%>" visibleBorrado="false" elementos="<%=elems%>" pintarEspacio="no" clase="listaNonEdit"> 
									<td><!-- Datos ocultos tabla -->

											<input type="hidden" id="oculto<%=i%>_1" value="<%if((bean.getIdSolicitud()!=null) && !(bean.getIdSolicitud().equals("")))out.print(bean.getIdSolicitud());%>">
   
											<!-- ENVIOS 1 idSolicitud, - idPersona, 2 descripcion -->
											<input type="hidden" id="oculto<%=i%>_2" value="<%=tipoSol%>">
											
											<%=UtilidadesString.mostrarDatoJSP(bean.getNumeroIdentificador())%></td>
									<td><%=UtilidadesString.mostrarDatoJSP(bean.getNombre())%></td> 
									<td><%=UtilidadesString.mostrarDatoJSP(bean.getNColegiado())%></td> 
									<td><%=tipoSol%></td> 
									<td><%=UtilidadesString.mostrarDatoJSP(bean.getFechaSolicitud())%></td> 
									<td><%=estadoSol%><%if (!iconoAlarma.trim().equalsIgnoreCase("SINALARMA")) {out.print("&nbsp;&nbsp;&nbsp;<img src='"+ app+ "/html/imagenes/" + iconoAlarma.trim().toLowerCase()+ ".gif' border='0'>");}%></td> 
									<td><%=UtilidadesString.mostrarDatoJSP(bean.getFechaEstado())%></td> 
									</siga:FilaConIconos>
							 		
			<%	 		 } // if
				 	 }  // for  %>
	
	<% } // if  
	else {%>
	 		<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
	<% } %>

			</siga:Table>


	<%if (resultados != null) { %>

	<div style="position:absolute; width:500px; height:35px; z-index:0; bottom:0px; left: 300px">
		<table align="center" width="100%">
			<tr>
				<td class="labelText"><img src='<%=app%>/html/imagenes/alarma.gif' 		   border='0'/>&nbsp;&nbsp;&nbsp;<siga:Idioma key="censo.solicitudIncorporacion.literal.casiCaducada"/></td>
				<td class="labelText"><img src='<%=app%>/html/imagenes/alarmacaducada.gif' border='0'/>&nbsp;&nbsp;&nbsp;<siga:Idioma key="censo.solicitudIncorporacion.literal.caducada"/></td>
			</tr>
		</table>
	</div>

	<% } %>

	<!-- formulario para ir a la ficha colegial -->
	<html:form  action="/CEN_BusquedaClientes.do" method="POST" target="mainWorkArea" style="display:none">
	<html:hidden  name="busquedaClientesForm" property="modo"/>
	<input type="hidden"  name="idPersona" value="">
	<input type="hidden"  name="idInstitucion" value="">
	</html:form>

		<!-- Formulario para la creacion de envio -->
		<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea"  style="display:none">
			<html:hidden property = "actionModal" value = ""/>
			<html:hidden property = "modo" value = ""/>
			<html:hidden property = "subModo" value = ""/>
			<html:hidden property = "tablaDatosDinamicosD" value = ""/>
			
			<html:hidden property = "idSolicitud" value = ""/>
			<html:hidden property = "idPersona" value = ""/>
			<html:hidden property = "descEnvio" value = ""/>
			
		</html:form>

</body>