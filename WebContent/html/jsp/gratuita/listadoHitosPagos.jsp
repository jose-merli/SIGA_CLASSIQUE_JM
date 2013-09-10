<!DOCTYPE html>
<html>
<head>
<!-- listadoHitosPagos.jsp -->

<!-- EJEMPLO DE VENTANA DENTRO DE PESTAÑAS (MAESTRO MULTIREGISTRO) -->
<!-- Contiene la zona de detalle multiregistro, sin botones de acciones, 
	 y sin campos de filtro o busqueda 
	 VERSIONES:
	 raul.ggonzalez 16-12-2004 Modificacion de formularios para validacion por struts de campos
-->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesString"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		

	Vector vHitos = (Vector)ses.getAttribute("vHitos");
	request.getSession().getAttribute("DATABACKUPHITO");  	//	Eliminamos esta variable de sesion.
																						//	Se volverá a crear en caso de que se elija el boton de editar.
	//Modo de la pestanha:
	String modopestanha = request.getSession().getAttribute("modo")==null?"":(String)request.getSession().getAttribute("modo");
	
	//Datos del Colegiado si procede:
	String nombrePestanha=null, numeroPestanha=null;
	try {
		Hashtable datosColegiado = (Hashtable)request.getSession().getAttribute("DATOSCOLEGIADO");
		nombrePestanha = (String)datosColegiado.get("NOMBRECOLEGIADO");
		numeroPestanha = (String)datosColegiado.get("NUMEROCOLEGIADO");
	} catch (Exception e){
		nombrePestanha = "";
		numeroPestanha = "";
	}
	
	//Si entrada=2 venimos de la pestanha de SJCS:
	String entrada = (String)ses.getAttribute("entrada");
%>	

<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<siga:TituloExt titulo="censo.fichaCliente.sjcs.guardias.pagos.cabecera" localizacion="censo.fichaCliente.sjcs.guardias.pagos.localizacion"/>
	
	<script>
		function refrescarLocal(){
			parent.buscar();
		}			
	</script>
</head>

<body class="tablaCentralCampos">

    <table class="tablaTitulo" align="center" cellspacing="0">
<%
		//Entrada desde el menu de Censo:
		if (entrada.equalsIgnoreCase("2")) { 
%>
			<tr>
				<td class="titulitosDatos">
					<siga:Idioma key="censo.consultaDatosGenerales.literal.titulo1"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(nombrePestanha)%>&nbsp;&nbsp;
				    <% if(numeroPestanha!= null && !numeroPestanha.equalsIgnoreCase("")) { %>
						<siga:Idioma key="censo.fichaCliente.literal.colegiado"/>&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(numeroPestanha)%>
					<% } else { %>
						<siga:Idioma key="censo.fichaCliente.literal.NoColegiado"/>
					<% } %>
				</td>
			</tr>
<% 
		} 
%>
	</table>
	
	<html:form action="JGR_DefinirHitosPago.do" method="POST" target="mainPestanas"  style="display:none">	
		<html:hidden property = "modo" value = ""/>		
	</html:form>			
		
	<siga:Table 
		  name="tablaDatos"
		  border="1"
		  columnNames="gratuita.listadoHistosFacturables.literal.hito,
		  				gratuita.listadoHistosFacturables.literal.precio,"
		  columnSizes="60,30,10"
		  modal="P">
		  
<% 
		if (vHitos==null || vHitos.size()==0) {
%>				
			<tr class="notFound">
		   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
					
<%
		} else{
			for (int cont=0; cont<vHitos.size(); cont++){
				Hashtable hash = (Hashtable)vHitos.get(cont);
%>			
				<siga:FilaConIconos fila='<%=""+(cont+1)%>' botones="E,C,B" clase="listaNonEdit" modo="<%=modopestanha%>" >
					<td><input type='hidden' name='oculto<%=String.valueOf(cont+1)%>_1' value='<%=hash.get("IDHITO")%>'><%=hash.get(ScsHitoFacturableBean.C_DESCRIPCION)%></td>
					<td>&nbsp;<%=hash.get(ScsHitoFacturableGuardiaBean.C_PRECIOHITO)%></td>						
				</siga:FilaConIconos>
					
<%
			}
		}
%>
	</siga:Table>
</body>
</html>