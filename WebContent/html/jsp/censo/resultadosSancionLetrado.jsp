<!DOCTYPE html>
<html>
<head>
<!-- resultadosSancionLetrado.jsp -->
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.censo.form.SancionesLetradoForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.Utilidades.PaginadorCaseSensitive"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.CenSancionAdm"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String idioma=usrbean.getLanguage().toUpperCase();
	String idinstitucionActual = usrbean.getLocation();
	String accion=(String)request.getAttribute("ACCION");
	String activarFilaSel = (String)request.getAttribute("activarFilaSel");	

	String valorCheckPersona = "";
	if (activarFilaSel == null || activarFilaSel.equals("")) {
		activarFilaSel = new String ("false");
	}
	
	String tienepermisoArchivo = (String)request.getAttribute("tienepermisoArchivo");
	String tamaño="position:absolute; width:100%; height:20; z-index:3; bottom:0px; left: 0px";
	
	// miro si estamos en la pestaña de datos de colegiacion
	String pestanaColegiacion = "1";
	String personaColegiacion = "";
	String institucionColegiacion = "";
	if (request.getAttribute("datosColegiacion")==null) {
		pestanaColegiacion = "0";
	} else {
		personaColegiacion = (String) request.getAttribute("personaColegiacion");
		if (personaColegiacion==null) {
			personaColegiacion = "";
		}
		institucionColegiacion = (String) request.getAttribute("institucionColegiacion");
		if (institucionColegiacion==null) {
			institucionColegiacion = "";
		}
	}	
	
   Vector resultado=null;
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	String action="";
	String targetVentana = "mainWorkArea";
	
	if (pestanaColegiacion!=null && !pestanaColegiacion.equals("1")) {// si no estamos en el pestana de colegiacion, mostramos el paginador	
		/** PAGINADOR ***/
		if (ses.getAttribute("DATAPAGINADOR")!=null){
	 		hm = (HashMap)ses.getAttribute("DATAPAGINADOR");

	 		if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
	  			resultado = (Vector)hm.get("datos");
	  			Paginador paginador = (Paginador)hm.get("paginador");
	 			paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
	 			totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
	 			registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());
	 			
	 		} else {
	  			resultado =new Vector();
	  			paginaSeleccionada = "0";
	 			totalRegistros = "0";
	 			registrosPorPagina = "0";
	 		}
	 		
		} else {
      		resultado =new Vector();
	  		paginaSeleccionada = "0";
 			totalRegistros = "0";
	 		registrosPorPagina = "0";
    	}	
	
	 	action=app+"/CEN_SancionesLetrado.do?noReset=true";
    	/**************/
    	
	} else {
       resultado = (Vector) request.getAttribute("resultado");
       targetVentana = "mainPestanas";
	}
	
	String botones="false";
	String botones2="true";
	if (pestanaColegiacion.equals("1")) {	
		botones="true";
		botones2="false";
	}
%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
 	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
		function refrescarLocal() {
			parent.buscar();
		}	
		
		function accionNuevo()  {	
			sub();
			document.forms[0].modo.value="nuevo";
			document.forms[0].target="mainWorkArea";
			document.forms[0].submit();	
		}		
	</script>
</head>

<body>

	<!-- INICIO: LISTA DE VALORES --> 
	<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista  de cabeceras fijas -->
	
	<!-- Formulario de la lista de detalle multiregistro -->
	<html:form action="/CEN_SancionesLetrado.do?noReset=true" target="<%=targetVentana %>" method="POST" style="display:none">
		<html:hidden styleId = "modo"  property = "modo" value = "" />
		<html:hidden styleId = "formulario"  property = "formulario" value = "" />
		<html:hidden styleId = "hiddenFrame"  property = "hiddenFrame"  value = "1"/>
		<input type="hidden" id= "pestanaColegiacion" name= "pestanaColegiacion"  value = "<%=pestanaColegiacion %>">
		<input type="hidden" id= "personaColegiacion" name= "personaColegiacion" value = "<%=personaColegiacion %>">
		<input type="hidden" id= "institucionColegiacion" name= "institucionColegiacion"  value = "<%=institucionColegiacion %>">
		<html:hidden styleId="registrosSeleccionados"  property="registrosSeleccionados" />
		<html:hidden styleId="datosPaginador"  property="datosPaginador" />
		<html:hidden styleId="seleccionarTodos"  property="seleccionarTodos" />
		<html:hidden styleId="nombreInstitucionBuscar"  property="nombreInstitucionBuscar" />
		<html:hidden styleId="tipoSancionBuscar"  property="tipoSancionBuscar" />
		<html:hidden styleId="refCGAE"  property="refCGAE" />
		<html:hidden styleId="refColegio"  property="refColegio" />
		<html:hidden styleId="colegiadoBuscar"  property="colegiadoBuscar" />
		<html:hidden styleId="chkRehabilitado"  property="chkRehabilitado" />
		<html:hidden styleId="mostrarTiposFechas"  property="mostrarTiposFechas" />			
		<html:hidden styleId="fechaInicioBuscar"  property="fechaInicioBuscar" />
		<html:hidden styleId="fechaFinBuscar"  property="fechaFinBuscar" />
		<html:hidden styleId="mostrarSanciones"  property="mostrarSanciones" />					
		<html:hidden styleId="fechaInicioArchivada" property="fechaInicioArchivada"/>
		<html:hidden styleId="fechaFinArchivada" property="fechaFinArchivada"/>
	</html:form>
	
<%
	String tamanosCol = "";
	String nombresCol = "";
	//Es consejo
	if(	(Integer.parseInt(idinstitucionActual) == 2000) || (Integer.parseInt(idinstitucionActual) >= 3000)){
		tamanosCol="8,24,8,6,6,8,8,10,10,12";
		nombresCol+="censo.busquedaSancionesLetrado.literal.colegio,";
		nombresCol+="censo.busquedaSancionesLetrado.literal.ncolegiado,censo.busquedaSancionesLetrado.literal.tipoSancion,";
		nombresCol+="censo.BusquedaSancionesLetrado.literal.refCGAE,censo.BusquedaSancionesLetrado.literal.refColegio2,gratuita.BusquedaSancionesLetrado.literal.fechaInicio,";
		nombresCol+="gratuita.BusquedaSancionesLetrado.literal.fechaFin, gratuita.BusquedaSancionesLetrado.literal.rehabilitado, gratuita.BusquedaSancionesLetrado.literal.firmeza,";				  

	}
	//Es colegiado
	else{
		tamanosCol="8,26,12,6,8,8,10,10,12";
		nombresCol+="censo.busquedaSancionesLetrado.literal.colegio,";
		nombresCol+="censo.busquedaSancionesLetrado.literal.ncolegiado,censo.busquedaSancionesLetrado.literal.tipoSancion,";
		nombresCol+="censo.BusquedaSancionesLetrado.literal.refColegio2,gratuita.BusquedaSancionesLetrado.literal.fechaInicio,";
		nombresCol+="gratuita.BusquedaSancionesLetrado.literal.fechaFin, gratuita.BusquedaSancionesLetrado.literal.rehabilitado, gratuita.BusquedaSancionesLetrado.literal.firmeza,";				  
	}
		%>

	<siga:Table 
	   	name="tablaDatos"
	   	border="1"
	   	columnNames="<%=nombresCol%>"
	   	columnSizes="<%=tamanosCol%>">
		   
	<!-- INICIO: ZONA DE REGISTROS -->
	<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	
		if (resultado==null || resultado.size()==0) { 
%>			
			<tr class="notFound">
				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>		
<%	
		} else { 

			// recorro el resultado
			for (int i=0;i<resultado.size();i++) {
				Hashtable registro =new Hashtable();
				if (pestanaColegiacion!=null && !pestanaColegiacion.equals("1")) {
				  Row fila = (Row)resultado.elementAt(i);
				   registro = (Hashtable) fila.getRow();
				}else{
				   registro = (Hashtable) resultado.get(i);
				}  
				
				UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
				String modo = "edicion";
	
				String institucion=(String)registro.get("ABREVIATURA_INSTI");
				String nombre=(String)registro.get("NOMBRE_LETRADO");
				String tipoSancion=(String)registro.get("NOMBRE_TIPOSANCION");
				String idInstitucionAlta=(String)registro.get("IDINSTITUCION");
				String idInstitucionSancion=(String)registro.get("IDINSTITUCIONSANCION");
				String idPersona=(String)registro.get("IDPERSONA");
				String idSancion=(String)registro.get("IDSANCION");
				String idTipoSancion=(String)registro.get("IDTIPOSANCION");
				String fechaInicio=(String)registro.get("FECHAINICIO");
				String fechaFin=(String)registro.get("FECHAFIN");
				String fechaRehabilitado=(String)registro.get("FECHAREHABILITADO");
				String fechaFirmeza=(String)registro.get("FECHAFIRMEZA");
				String chkRehabilitado=(((String)registro.get("CHKREHABILITADO")).equals("1"))?"gratuita.operarEJG.literal.si":"gratuita.operarEJG.literal.no";
				String chkFirmeza=(((String)registro.get("CHKFIRMEZA")).equals("1"))?"gratuita.operarEJG.literal.si":"gratuita.operarEJG.literal.no";
				String refColegio=(String)registro.get("REFCOLEGIO");
				String refCGAE=(String)registro.get("REFCGAE");
				String chkArchivada=(String)registro.get("CHKARCHIVADA");   
				String idSancionOrigen=(String)registro.get("IDSANCIONORIGEN");				
				String cont = new Integer(i+1).toString();			
				
				// Permisos de acceso
				String permisos="";
				if(ClsConstants.esConsejoGeneral(idInstitucionAlta) && idSancionOrigen != null && !idSancionOrigen.equals("")){
					//Si se trata de una sancion traspasada al CGAE solo se puede consultar
					permisos="C";				
				} else if (!idInstitucionAlta.equals(idinstitucionActual)){
					//Si se trata de una sancion de otro colegio
					permisos="C";
				} else {
					permisos="C,E,B";					
				}
%>

				<!-- REGISTRO  -->
				<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
					 que la lista contiene realmente 3 columnas: Las de datos mas 
				 	la de botones de acción sobre los registos  -->
			
  				<siga:FilaConIconos fila="<%=cont %>" botones="<%=permisos%>"  modo="<%=accion %>" clase="listaNonEdit" >  	  				
					<td>
						<input type="hidden" id="oculto<%=cont %>_1" name="oculto<%=cont %>_1" value="<%=idPersona %>">
						<input type="hidden" id="oculto<%=cont %>_2" name="oculto<%=cont %>_2" value="<%=idSancion %>">
						<input type="hidden" id="oculto<%=cont %>_3" name="oculto<%=cont %>_3" value="<%=idInstitucionAlta %>">

						<%=UtilidadesString.mostrarDatoJSP(institucion) %>					
					</td>
				
					<td>
						<%=UtilidadesString.mostrarDatoJSP(nombre) %>
					</td>
					
					<td>
						<%=UtilidadesString.mostrarDatoJSP(tipoSancion) %>
					</td>
					
					<% if(	(Integer.parseInt(idinstitucionActual) == 2000) || (Integer.parseInt(idinstitucionActual) >= 3000)) { %>	
						<td>			
							<%=UtilidadesString.mostrarDatoJSP(refCGAE)   %>
						</td>
					<%} %>
					<td>
						 <%=UtilidadesString.mostrarDatoJSP(refColegio) %>
					</td>
					<td>
					
							
						<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(idioma,fechaInicio)) %>
					</td>
					
					<td>
						<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(idioma,fechaFin)) %>
					</td>
					
					<td>
						<siga:Idioma key="<%=chkRehabilitado%>"/>&nbsp;&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(idioma,fechaRehabilitado)) %>
					</td>
					
					<td>
						<siga:Idioma key="<%=chkFirmeza%>"/>&nbsp;&nbsp;&nbsp;<%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(idioma,fechaFirmeza)) %>
					</td>
				</siga:FilaConIconos>		

				<!-- FIN REGISTRO -->
<%		
			} // del for
		} // del if 
%>			
	</siga:Table>

	<!-- INICIO: BOTONES ACCION -->  
<%  
	if (accion!=null && accion!="null"){
	 	if (!accion.equals("ver")) {
%>
			<siga:ConjBotonesAccion botones="N" clase="botonesDetalle" />
<%		
		} else { 
%>
	   		<siga:ConjBotonesAccion botones="" clase="botonesDetalle"/>
<%		
		}
		
	} 
%> 	
	<!-- FIN: BOTONES ACCION -->
	
<%
	if (pestanaColegiacion!=null && !pestanaColegiacion.equals("1")) {	
		if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
%>	     
	        <siga:Paginador 
	        	totalRegistros="<%=totalRegistros%>" 
				registrosPorPagina="<%=registrosPorPagina%>" 
				paginaSeleccionada="<%=paginaSeleccionada%>" 
				idioma="<%=idioma%>"
				modo="buscarPor"								
				clase="paginator" 
				divStyle="<%=tamaño%>"
				distanciaPaginas=""								
				action="<%=action%>" />
<%
		}
	}
%>	

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

</body>
</html>
