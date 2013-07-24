<!DOCTYPE html>
<html>
<head>
<!-- resultadosBusquedaProcuradorModal.jsp -->
<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
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

<%@ page import="com.siga.general.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String idioma=usrbean.getLanguage().toUpperCase();
%>	

<%  
    Vector resultado=null;

	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	
	HashMap hm = new HashMap();
	Vector registrosSeleccionados = null;
	String idPaginador = (String)request.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(idPaginador)!=null) {
		hm = (HashMap)ses.getAttribute(idPaginador);

		if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
	  	resultado = (Vector)hm.get("datos");
	  	Paginador paginador = (Paginador)hm.get("paginador");
	 	 paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
	 	 totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
     	 registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
		}
		else{
		  resultado =new Vector();
		  paginaSeleccionada = "0";
    	  totalRegistros = "0";
      	registrosPorPagina = "0";
		}
	}else{
      resultado =new Vector();
	  paginaSeleccionada = "0";
	
	 	totalRegistros = "0";
	
	 	registrosPorPagina = "0";
    }	
	
	String action = app+"/CEN_BusquedaClientesModal.do";
	String titu = "censo.busquedaClientes.literal.titulo";
%>



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="/CEN_BusquedaClientesModal.do" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="<%=titu %>" localizacion="<%=titu %>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">

		function seleccionar(fila) 
		{
		   	var datos = new Array();
			
		   	var aux = 'oculto' + fila + '_1';
		   	var oculto = document.getElementById(aux);
		   	if (oculto != null) datos[0] = oculto.value;
	
		   	aux = 'oculto' + fila + '_2';
		   	oculto = document.getElementById(aux);
		   	if (oculto != null) datos[1] = oculto.value;
	
		   	aux = 'oculto' + fila + '_3';
		   	oculto = document.getElementById(aux);
		   	if (oculto != null) datos[2] = oculto.value;

			top.cierraConParametros(datos);
		}
	</script>

</head>

<body class="tablaCentralCampos">

		<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="submitArea" style="display:none">
			<html:hidden property = "modo" value = "" />
			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>

		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="censo.busquedaClientesAvanzada.literal.nColegiado,censo.busquedaClientes.literal.codigo,gratuita.turnos.literal.apellidosSolo,censo.busquedaClientesAvanzada.literal.nombre,"
		   columnSizes="20,20,35,20,5">

			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>	 		
<%	
	} else { 

		FilaExtElement[] elems=new FilaExtElement[1];
		elems[0]=new FilaExtElement("seleccionar","seleccionar",SIGAConstants.ACCESS_READ);  	

		// recorro el resultado
		for (int i=0;i<resultado.size();i++) {
			Row fila = (Row)resultado.elementAt(i);
			Hashtable registro = (Hashtable) fila.getRow();
			String cont = new Integer(i+1).toString();

			// permisos de acceso
			String modo = "";
		
			String idProcurador  = UtilidadesString.controlNulos(registro.get(ScsProcuradorBean.C_IDPROCURADOR));
			String idInstitucion = UtilidadesString.controlNulos(registro.get(ScsProcuradorBean.C_IDINSTITUCION));
			String nombre        = UtilidadesString.mostrarDatoJSP(registro.get(ScsProcuradorBean.C_NOMBRE)); 
			String apellidos     = UtilidadesString.mostrarDatoJSP(registro.get(ScsProcuradorBean.C_APELLIDO1)) + " " + UtilidadesString.mostrarDatoJSP(registro.get(ScsProcuradorBean.C_APELLIDO2));
			String auxNombre     = UtilidadesString.controlNulos(registro.get(ScsProcuradorBean.C_NOMBRE)) + " " + UtilidadesString.controlNulos(registro.get(ScsProcuradorBean.C_APELLIDO1)) + " " + UtilidadesString.controlNulos(registro.get(ScsProcuradorBean.C_APELLIDO2));
			String ncolegiado    = UtilidadesString.mostrarDatoJSP(registro.get(ScsProcuradorBean.C_NCOLEGIADO));
			String codigo        = UtilidadesString.mostrarDatoJSP(registro.get(ScsProcuradorBean.C_CODPROCURADOR));
%>

			<siga:FilaConIconos fila="<%=cont%>" botones="" modo="edicion" elementos="<%=elems%>" visibleBorrado="no" visibleEdicion="no" visibleConsulta="no" pintarEspacio="no" clase="listaNonEdit">
				<td>
					<input type="hidden" name="oculto<%=cont%>_1" value="<%=idProcurador%>,<%=idInstitucion%>">
					<input type="hidden" name="oculto<%=cont%>_2" value="<%=auxNombre%>">
					<input type="hidden" name="oculto<%=cont%>_3" value="<%=ncolegiado%>">
					<%=ncolegiado%>
				</td>
				<td>
					<%=codigo%>
				</td>
				<td>
					<%=apellidos%>
				</td>
				<td>
					<%=nombre%>
				</td>
			</siga:FilaConIconos>		

<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:Table>

		
	<!-- FIN: LISTA DE VALORES -->		

	<!-- Pintamos la paginacion-->	
		<%if (hm.get("datos")!=null && !hm.get("datos").equals("")){%>
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarProcuradorModal"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
	 <%}%>			
   <!------------------------------------------->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
