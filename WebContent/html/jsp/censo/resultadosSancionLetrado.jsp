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



<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String idioma=usrbean.getLanguage().toUpperCase();
	String accion=(String)request.getAttribute("ACCION");
	
	String activarFilaSel = (String)request.getAttribute("activarFilaSel");
	if (activarFilaSel == null || activarFilaSel.equals("")) {
		activarFilaSel = new String ("false");
	}
 
	// locales
	SancionesLetradoForm formulario = (SancionesLetradoForm)request.getSession().getAttribute("SancionesLetradoForm");

	//	
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
	if (pestanaColegiacion!=null && !pestanaColegiacion.equals("1")) {	// si no estamos en el pestana de colegiacion, mostramos el paginador
	
	/** PAGINADOR ***/
	
	
	if (ses.getAttribute("DATAPAGINADOR")!=null){

	 hm = (HashMap)ses.getAttribute("DATAPAGINADOR");

	
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
	
	 action=app+"/CEN_SancionesLetrado.do?noReset=true";
    /**************/
	}else{
       resultado = (Vector) request.getAttribute("resultado");
	}
	
	// RGG Alto de la lista
	String alto="323";
	String botones="false";
	String botones2="true";
	if (pestanaColegiacion.equals("1")) {
		alto="70";	
		botones="true";
		botones2="false";
	}

%>

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

 	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">

		function refrescarLocal() {
			parent.buscar();
		}
	
	</script>

</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_SancionesLetrado.do?noReset=true" method="POST" style="display:none">

			<!-- Campo obligatorio -->
			<html:hidden property = "modo" value = "" />
			<input type="hidden" name= "accionModal" value = "">
			<input type="hidden" name= "pestanaColegiacion" value = "<%=pestanaColegiacion %>">
			<input type="hidden" name= "personaColegiacion" value = "<%=personaColegiacion %>">
			<input type="hidden" name= "institucionColegiacion" value = "<%=institucionColegiacion %>">

 			<!-- RGG: cambio a formularios ligeros -->
 			<input type="hidden" name="filaSelD">
			<input type="hidden" name="tablaDatosDinamicosD">
			<input type="hidden" name="actionModal" value="">
		</html:form>
 
		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="censo.busquedaSancionesLetrado.literal.colegio,
				      censo.busquedaSancionesLetrado.literal.ncolegiado,
					  censo.busquedaSancionesLetrado.literal.tipoSancion,
					  censo.BusquedaSancionesLetrado.literal.refCGAE,
					  gratuita.BusquedaSancionesLetrado.literal.fechaInicio,
					  gratuita.BusquedaSancionesLetrado.literal.fechaFin,
					  gratuita.BusquedaSancionesLetrado.literal.rehabilitado,
					  gratuita.BusquedaSancionesLetrado.literal.firmeza,"
		   tamanoCol="8,28,12,6,8,8,10,10,10"
		   alto="100%"
		   ajusteBotonera="<%=botones%>"		
		   ajustePaginador="<%=botones2%>"		
		   modal="G"  
		   activarFilaSel="<%=activarFilaSel%>" >

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br>	 		
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
			// permisos de acceso
			String permisos="C,E,B";
			/*if (accion.equals("ver")){
			   permisos = "C";
			}else{
			   permisos = "C,E,B";
			}*/
			
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
			String refCGAE=(String)registro.get("REFCGAE");   
			
			
			String cont = new Integer(i+1).toString();
%>

			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->
			
  			<siga:FilaConIconos fila="<%=cont %>" botones="<%=permisos%>"  modo="<%=accion %>" clase="listaNonEdit" >
			
				<td>
					<!-- campos hidden -->
					<input type="hidden" name="oculto<%=cont %>_1" value="<%=idPersona %>">
					<input type="hidden" name="oculto<%=cont %>_2" value="<%=idSancion %>">

					<%=UtilidadesString.mostrarDatoJSP(institucion) %>
					
				</td>
				
				<td>
					<%=UtilidadesString.mostrarDatoJSP(nombre) %>
				</td>
				<td>
					<%=UtilidadesString.mostrarDatoJSP(tipoSancion) %>
				</td>
				<td>
					<%=UtilidadesString.mostrarDatoJSP(refCGAE) %>
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
<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:TablaCabecerasFijas>


	<!-- INICIO: BOTONES ACCION -->
  
	<%  if (accion!=null && accion!="null"){
	 		if (!accion.equals("ver")) {
 	%>
		<siga:ConjBotonesAccion botones="N" clase="botonesDetalle" />
	<%		}else { %>
	   <siga:ConjBotonesAccion botones="" clase="botonesDetalle"/>
	<%		}
		} %> 	

	<!-- FIN: BOTONES ACCION -->

	
	<!-- INICIO: SCRIPTS BOTONES ACCION -->
	<script language="JavaScript">

		function accionNuevo() 
		{		
			document.forms[0].modo.value="nuevo";
			var resultado=ventaModalGeneral(document.forms[0].name,"G");
			if (resultado!=undefined && resultado=="MODIFICADO")
			{
				refrescarLocal();
			}
		}

		
	</script>
	<!-- FIN: SCRIPTS BOTONES ACCION -->			
	
<%if (pestanaColegiacion!=null && !pestanaColegiacion.equals("1")) {%>	
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
      <%  }%>
<%}%>	  
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
