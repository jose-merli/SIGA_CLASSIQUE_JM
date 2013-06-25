<!-- consultaExpedientes.jsp -->
<!-- Historico modificaciones:
		miguel.villegas: implementacion boton volver -->

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
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.ExpExpedienteBean"%>


<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");

	String idUsr=Long.toString(usr.getIdPersona());
	String idPersona=String.valueOf((Long)request.getAttribute("idPersona"));
	String idInstitucion=String.valueOf((Integer)request.getAttribute("idInstitucion"));
	String nombre=(String)request.getAttribute("nombrePersona");
	String numero=(String)request.getAttribute("numero");	
	Vector vDatos=(Vector)request.getAttribute("vDatos");	
	String estadoColegial=(String)request.getAttribute("estadoColegial");
	String sTipo = request.getParameter("tipo");
	String botones=""; 		
	
	// Gestion de Volver
	String busquedaVolver = (String)
	request.getSession().getAttribute("CenBusquedaClientesTipo");
	if ((busquedaVolver==null)||(usr.isLetrado())) {
		busquedaVolver = "volverNo";
	}
	
	if (!busquedaVolver.equals("volverNo")) { 
		botones="V";
	}	
	
%>	
<html>
<!-- HEAD -->
	<head>
	
			<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript">
			function solicitar(fila) {
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = ""; 
				var j;
				var tabla;
				tabla = document.getElementById('tablaDatos');
				var flag = true;
				j = 1;
				while (flag) {
				  var aux = 'oculto' + fila + '_' + j;
				  var oculto = document.getElementById(aux);
				  if (oculto == null){ 
				  	flag = false; 
				  }
				  else { 
				  	datos.value = datos.value + oculto.value + ','; 
				  }
				  j++;
				}
				datos.value = datos.value + "%"
			    document.forms[0].modo.value = "editarSolicitud";
			    ventaModalGeneral(document.expedientesForm.name,"P");
		 	}
		</script>
		
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		
		<% if (sTipo!=null && sTipo.equals("LETRADO")){%>
		 <siga:Titulo 
			titulo="censo.fichaCliente.expedientes.cabecera"
			localizacion="censo.fichaLetrado.localizacion"/>
		<%}else{%>
		<siga:TituloExt 
			titulo="censo.fichaCliente.expedientes.cabecera" 
			localizacion="censo.fichaCliente.expedientes.localizacion"/>
		<%}%>
		
		<!-- FIN: TITULO Y LOCALIZACION -->		
	
	</head>

	<body class="tablaCentralCampos" >	
	    <table class="tablaTitulo" align="center" cellspacing=0>
			<!-- Formulario de la lista de detalle multiregistro -->
			<html:form action="expedientesForm" method="post" action="/CEN_Expedientes.do">
			
			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = ""/>
			
			<html:hidden property = "nombre" value ="<%=nombre%>"/>
			<html:hidden property = "numero" value ="<%=numero%>"/>
			<html:hidden property = "idPersona" value ="<%=idPersona%>"/>
			<html:hidden property = "idInstitucion" value ="<%=idInstitucion%>"/>
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="actionModal" value="">
		</html:form>			
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.consultaExpedientes.titulo"/> &nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombre)%> &nbsp;&nbsp;
				    <%if(!numero.equalsIgnoreCase("")){%>
						<%if (estadoColegial!=null && !estadoColegial.equals("")){%>
							<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>
							 <%= UtilidadesString.mostrarDatoJSP(numero)  %> &nbsp; (<%=UtilidadesString.mostrarDatoJSP(estadoColegial)%>)
						 <%}else{%> 
						 	(<siga:Idioma key="censo.busquedaClientes.literal.sinEstadoColegial"/>) 
						 <%}%>
					<%} 
					else {%>
						   <siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<%}%>
				</td>
			</tr>
	
			<!-- INICIO: LISTA DE VALORES -->
			<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
				 de cabeceras fijas -->
			<%
			String columnas =  "censo.consultaExpedientes.literal.tipoExpediente,censo.consultaExpedientes.literal.sancionado,"+
		  			   "censo.consultaExpedientes.literal.sancionFinalizada,censo.consultaExpedientes.literal.fechaCaducidad,"+
		  			   "censo.consultaExpedientes.literal.motivo";
			String tamanios = "20,13,13,17,25";
			if(idPersona.equals(idUsr)){
				columnas =  "censo.consultaExpedientes.literal.tipoExpediente,censo.consultaExpedientes.literal.sancionado,"+
			  			   "censo.consultaExpedientes.literal.sancionFinalizada,censo.consultaExpedientes.literal.fechaCaducidad,"+
			  			   "censo.consultaExpedientes.literal.motivo,";
				tamanios = "20,13,13,17,25,12";
			}
			%>
			<siga:Table 
			   	name="tablaDatos"
			   	border="1"
			  	columnNames="<%=columnas%>"
			  	columnSizes="<%=tamanios%>">
		 	<%	 		
		 		if(vDatos == null || vDatos.size()<1)
		 			{ 			
			 %>
			 		<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr> 		
			 <%		
			 		}
			 	else
			 		{			 		
			 		Enumeration en = vDatos.elements();
					int recordNumber=0;
					String botonesMostrados="";
					while(en.hasMoreElements()){
						recordNumber++;
						Row row = (Row)en.nextElement();
						
						// Control icono solicitud modificacion
						FilaExtElement[] elementos=new FilaExtElement[1];
						if(idPersona.equals(idUsr)){
		  		 			elementos[0]=new FilaExtElement("solicitar","solicitar",SIGAConstants.ACCESS_FULL);
						}
			%>
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
							<td>
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_1" value="<%=idPersona%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_2" value="<%=row.getString(ExpExpedienteBean.C_IDINSTITUCION)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_3" value="<%=row.getString(ExpExpedienteBean.C_IDINSTITUCION_TIPOEXPEDIENTE)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_4" value="<%=row.getString(ExpExpedienteBean.C_NUMEROEXPEDIENTE)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_5" value="<%=row.getString(ExpExpedienteBean.C_ANIOEXPEDIENTE)%>">
								<input type="hidden" name="oculto<%=String.valueOf(recordNumber)%>_6" value="<%=row.getString(ExpExpedienteBean.C_IDTIPOEXPEDIENTE)%>">
								<%=UtilidadesString.mostrarDatoJSP(row.getString("TIPOEXPEDIENTE"))%>
							</td>
							<td >
								<% if (row.getString("SANCIONADO").equalsIgnoreCase("S")){%>
									<siga:Idioma key="general.yes"/>
								<% } else { %>
									<siga:Idioma key="general.no"/>
								<% } %>
							</td>  	
							<td >
								<% if ((row.getString("SANCIONFINALIZADA")==null)||(row.getString("SANCIONFINALIZADA").equalsIgnoreCase(""))){%>
									<siga:Idioma key="general.no"/>
								<% } else { %>
									<siga:Idioma key="general.yes"/>
								<% } %>
							</td>  															
							<td >
								<% if ((row.getString("SANCIONFINALIZADA")==null)||(row.getString("SANCIONFINALIZADA").equalsIgnoreCase(""))){%>
									<% if (row.getString("SANCIONFINALIZADA").equalsIgnoreCase("")){%>
										&nbsp;
									<% } else { %>
										<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),row.getString("FECHAFINAL")))%>
									<% } %>
								<% } else { %>
									<siga:Idioma key="censo.consultaExpedientes.literal.sancionFinalizada"/>
								<% } %>
							</td>  	
							<td>
								<%=UtilidadesString.mostrarDatoJSP(row.getString("ASUNTO"))%>
							</td>  															
						</siga:FilaConIconos>
		 <%		}// while
	 		}  %>  			
	  			</siga:Table>
	  			
			  	<siga:ConjBotonesAccion botones="<%=botones%>" clase="botonesDetalle"/>		
		
		
			<!-- FIN: BOTONES BUSQUEDA -->

		
			<%@ include file="/html/jsp/censo/includeVolver.jspf" %>
				
			<!-- INICIO: SUBMIT AREA -->
			<!-- Obligatoria en todas las páginas-->
			<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>

	
		</table>
	</body>
</html>
