<!-- resultadosModalPersonaJG.jsp -->
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
 
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.ScsPersonaJGBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
 
<!-- JSP -->
<%

	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	UsrBean usrbean = (UsrBean) session
			.getAttribute(ClsConstants.USERBEAN);
	String idioma = usrbean.getLanguage().toUpperCase();

	//Vector vPersonas = (Vector) request.getAttribute("ScsResultadoBusquedaPersonasJG");
	/** PAGINADOR ***/
	Vector resultado = null;
	String paginaSeleccionada = "";

	String totalRegistros = "";

	String registrosPorPagina = "";
	HashMap hm = new HashMap();
	if (ses.getAttribute("DATAPAGINADORMODAL") != null) {
		hm = (HashMap) ses.getAttribute("DATAPAGINADORMODAL");

		if (hm.get("datos") != null && !hm.get("datos").equals("")) {
			resultado = (Vector) hm.get("datos");

			PaginadorBind paginador = (PaginadorBind) hm
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
	String action = app + "/JGR_BusquedaPersonaJG.do";
	/**************/
%>
 
<%@page import="java.util.Properties"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.siga.beans.CenClienteBean"%>
<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="BusquedaPersonaJGForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
	function seleccionar(fila) {
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
			if (oculto == null)  { 
				flag = false; 
			} else { 
		  		datos.value = datos.value + oculto.value + ','; 
		  	}
			j++;
		}		
		datos.value = datos.value + "%";
		
    	document.BusquedaPersonaJGForm.modo.value = "enviar";
	   	document.BusquedaPersonaJGForm.submit();
	}
	
	function lopd(fila) {
		alert('<siga:Idioma key="general.boton.lopd"/>');		
	}
	</script>

</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/JGR_BusquedaPersonaJG.do" method="POST" target="submitArea" style="display:none" styleId="BusquedaPersonaJGForm">
			<html:hidden name="BusquedaPersonaJGForm" styleId="modo" property = "modo" value = ""/>
			<html:hidden name="BusquedaPersonaJGForm" styleId="conceptoE" property = "conceptoE" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" id="actionModal" name="actionModal" value="">
		</html:form>	
		


<%
				String tamanosCol = "";
				String nombresCol = "";
				// cliente colegiado o  no
				tamanosCol = "12,25,25,25,13";
				nombresCol = "gratuita.personaJG.literal.nIdentificacion,gratuita.personaJG.literal.nombreDeno,gratuita.personaJG.literal.apellido1Abre,gratuita.personaJG.literal.apellido2,";
			%>

		<siga:Table 
		   	name="tablaDatos"
		   	border="1"
		   	columnNames="<%=nombresCol %>"
		   	columnSizes="<%=tamanosCol %>">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
			<%
			if (resultado == null || resultado.size() == 0) {
			%>			
	 		<div class="notFound">
<br><br>
<p class="titulitos" style="text-align:center"><siga:Idioma key="messages.noRecordFound"/></p>
<br><br>
</div>
			<%
			} else {

			FilaExtElement[] elems = null;
			

			// recorro el resultado
			//for (int i=0;i<vPersonas.size();i++) {
			//ScsPersonaJGBean registro = (ScsPersonaJGBean) vPersonas.get(i);
			for (int i = 0; i < resultado.size(); i++) {				
			
				Row fila = (Row) resultado.elementAt(i);
				Hashtable registro = (Hashtable) fila.getRow();
				boolean isAplicarLOPD = (String) registro.get(CenClienteBean.C_NOAPARECERREDABOGACIA) != null
				&& ((String) registro.get(CenClienteBean.C_NOAPARECERREDABOGACIA)).equals(ClsConstants.DB_TRUE);
				if(isAplicarLOPD){
					elems = new FilaExtElement[2];
					elems[1] = new FilaExtElement(
							"lopd",	"lopd",	SIGAConstants.ACCESS_READ);
					elems[0] = new FilaExtElement("seleccionar", "seleccionar",
							SIGAConstants.ACCESS_READ);
				} else {
					elems = new FilaExtElement[1];
					elems[0] = new FilaExtElement("seleccionar", "seleccionar",
							SIGAConstants.ACCESS_READ);									
				}
				
				String cont = new Integer(i + 1).toString();

				// permisos de acceso
				String modo = "";

				// el id del user.getlocation
				String idInstitucion = usrbean.getLocation();

				// calculo de campos
				String idPersona = String.valueOf(registro
						.get(ScsPersonaJGBean.C_IDPERSONA));
				String apellido1 = UtilidadesString
						.mostrarDatoJSP(registro
								.get(ScsPersonaJGBean.C_APELLIDO1));
				String apellido2 = UtilidadesString
						.mostrarDatoJSP(registro
								.get(ScsPersonaJGBean.C_APELLIDO2));
				String nombre = UtilidadesString
						.mostrarDatoJSP(registro
								.get(ScsPersonaJGBean.C_NOMBRE));
				String nif = UtilidadesString.mostrarDatoJSP(registro
						.get(ScsPersonaJGBean.C_NIF));
%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->
			
			<siga:FilaConIconos fila="<%=cont %>" botones=""
			 	modo="<%=modo %>" elementos="<%=elems%>" visibleBorrado="no"
			 	visibleEdicion="no" visibleConsulta="no" pintarEspacio="no" clase="listaNonEdit">
			
				<td>
					<!-- campos hidden -->
					<input type="hidden" id="oculto<%=cont %>_1" name="oculto<%=cont %>_1" value="<%=idPersona %>">

					<%=nif%>
				</td>
				<td>
					<%=nombre%>
				</td>
				<td>
					<%=apellido1%>
				</td>
				<td>
					<%=apellido2%>
				</td>

			</siga:FilaConIconos>		


			<!-- FIN REGISTRO -->
<%
	} // del for
%>			

			<!-- FIN: ZONA DE REGISTROS -->

<%
	} // del if
%>			

		</siga:Table>
	<%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>	
<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
<%
	} // del if
%>	
		<!-- FIN: LISTA DE VALORES -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
