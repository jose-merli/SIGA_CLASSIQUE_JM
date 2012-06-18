<!-- resultBusqAlerta.jsp -->
<!-- 
	 VERSIONES:
	 juan.grau 03-02-2005 Versión inicial
-->

<!-- CABECERA JSP -->
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.expedientes.ExpPermisosTiposExpedientes"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.Paginador"%>

<!-- JSP -->
<bean:define id="datosPaginador" name="busquedaAlertaForm" property="datosPaginador" type="java.util.HashMap"/>
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	
	Vector vDatos = (Vector)request.getAttribute("datos");	
	UsrBean userBean = ((UsrBean)ses.getAttribute(("USRBEAN")));
	request.removeAttribute("datos");
	String idInstitucion = userBean.getLocation();
	String idioma=userBean.getLanguage().toUpperCase();
	String botones = "";
	ExpPermisosTiposExpedientes perm=(ExpPermisosTiposExpedientes)request.getAttribute("permisos");
	
	/** PAGINADOR ***/
	String paginaSeleccionada = "";
	
	String totalRegistros = "";

	String registrosPorPagina = "";
	Vector resultado = null;
	String valorCheckPersona = "";
	if (datosPaginador!=null) {
	

		if (datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
			resultado = (Vector) datosPaginador.get("datos");
			
				Paginador paginador = (Paginador) datosPaginador
						.get("paginador");
				paginaSeleccionada = String.valueOf(paginador
						.getPaginaActual());

				totalRegistros = String.valueOf(paginador
						.getNumeroTotalRegistros());

				registrosPorPagina = String.valueOf(paginador
						.getNumeroRegistrosPorPagina());
			
		} else {
			resultado = new Vector();
			paginaSeleccionada = "0";

			totalRegistros = "0";

			registrosPorPagina = "0";
		}
	} else {
		resultado = new Vector();
		paginaSeleccionada = "0";

		totalRegistros = "0";

		registrosPorPagina = "0";
	}

	String action = app + "/EXP_Consultas.do?noReset=true";
	
	/* FIN PAGINADOR */

%>	

<html>

<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">		
		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="expedientes.alertas.cabecera" 
			localizacion="expedientes.literal.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body class="tablaCentralCampos">
	
		<!-- INICIO: LISTA DE VALORES -->
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->
			 
		<html:form action="/EXP_Consultas.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">
			
		    <html:hidden property = "modo" value = ""/>
			<html:hidden property = "hiddenFrame" value = "1"/>

			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>


			<siga:TablaCabecerasFijas 
		   	      nombre="tablaDatos"
		   		  borde="1"
		   		  clase="tableTitle"
		   		  nombreCol="expedientes.auditoria.literal.fecha,
		   		  	expedientes.auditoria.literal.tipo,
		   		  	expedientes.auditoria.literal.fase,
		   		  	expedientes.auditoria.literal.estado,
		   		  	expedientes.auditoria.literal.nexpediente,
		   		  	expedientes.auditoria.literal.alerta,"
		   		  tamanoCol="13,15,12,12,8,25,10"
		   		  alto="100px" 
		   		  activarFilaSel="true"
		   		  ajustePaginador="true">
		   		  
		    <!-- INICIO: ZONA DE REGISTROS -->
<%
				if (resultado==null || resultado.size()==0)
				{
%>
				<br><br>
		   		<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
				<br><br>
<%
				}
				
				else
				{
			 		for (int i=0; i<resultado.size(); i++)
			   		{
				  		Row fila = (Row)resultado.elementAt(i);	
						if (fila.getString("IDINSTITUCION").equals(idInstitucion)){	
				  			botones="C,E,B";
				  		}else{
				  			botones="C,E";
				  		}
				  		botones=perm.getBotones(fila.getString("IDINSTITUCION_TIPOEXPEDIENTE"),fila.getString("IDTIPOEXPEDIENTE"),botones);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=botones%>" clase="listaNonEdit">
						<input type="hidden" name="oculto<%=""+(i+1)%>_1" value="<%=fila.getString("IDINSTITUCION")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_2" value="<%=fila.getString("IDINSTITUCION_TIPOEXPEDIENTE")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_3" value="<%=fila.getString("IDTIPOEXPEDIENTE")%>">
						<input type="hidden" name="oculto<%=""+(i+1)%>_4" value="<%=fila.getString("NUMEROEXPEDIENTE")%>">	
						<input type="hidden" name="oculto<%=""+(i+1)%>_5" value="<%=fila.getString("ANIOEXPEDIENTE")%>">	
						<input type="hidden" name="oculto<%=""+(i+1)%>_6" value="<%=UtilidadesString.mostrarDatoJSP(fila.getString("NOMBRETIPOEXPEDIENTE"))%>">	
						<input type="hidden" name="oculto<%=""+(i+1)%>_7" value="<%=fila.getString("IDALERTA")%>">	
											
					<td><%=UtilidadesString.formatoFecha(fila.getString("FECHAALERTA"),"yyyy/MM/dd HH:mm:ss","dd/MM/yyyy HH:mm:ss")%></td>
					<td><%=fila.getString("NOMBRETIPOEXPEDIENTE")%></td>
					<td><%=fila.getString("FAS_NOMBRE")%></td>
					<td><%=fila.getString("EST_NOMBRE")%></td>
					<td><%=fila.getString("ANIOEXPEDIENTE")%>&nbsp;/&nbsp;<%=fila.getString("NUMEROEXPEDIENTE")%></td>
					<td><%=fila.getString("TEXTO")%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			<!-- FIN: ZONA DE REGISTROS -->
			</siga:TablaCabecerasFijas>
			
			
		<%if (  datosPaginador!=null && datosPaginador.get("datos")!=null && !datosPaginador.get("datos").equals("")){%>
	  
			<siga:Paginador totalRegistros="<%=totalRegistros%>" 
				registrosPorPagina="<%=registrosPorPagina%>" 
				paginaSeleccionada="<%=paginaSeleccionada%>" 
				idioma="<%=idioma%>"
				modo="buscar"								
				clase="paginator" 
				divStyle="position:absolute; width:100%; height:20;  z-index:3; bottom:0px; left: 0px"
				distanciaPaginas=""
				action="<%=action%>" />
      	<%}%>

		<!-- FIN: LISTA DE VALORES -->
	
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		
	<script language="JavaScript">

		
		function refrescarLocal()
		{			
			parent.buscar() ;			
		}
		

	</script>
	
		
	</body>
</html>