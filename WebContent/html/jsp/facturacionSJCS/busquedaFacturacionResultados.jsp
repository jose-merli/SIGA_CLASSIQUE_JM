<!-- busquedaFacturacionResultados.jsp -->
<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
	 raul.ggonzalez 07-03-2005 creacion
	 ruben.fernandez 18-04-2005 campo regularización en la tabla de resultados
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
<%@ page import="org.redabogacia.sigaservices.app.AppConstants.ESTADO_FACTURACION"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.facturacionSJCS.form.MantenimientoFacturacionForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.Utilidades.*"%>

<%@ page import="com.siga.Utilidades.paginadores.Paginador"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String idioma=usrbean.getLanguage().toUpperCase();
%>	

<%  
	// locales
	MantenimientoFacturacionForm formulario = (MantenimientoFacturacionForm)request.getSession().getAttribute("mantenimientoFacturacionForm");
	
	//Vector resultado = (Vector) request.getAttribute("SJCSResultadoBusquedaFacturacion");
	
     Vector resultado=null;
	
	/** PAGINADOR ***/
	
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm=new HashMap();
	if (ses.getAttribute("DATAPAGINADOR")!=null){

	 hm = (HashMap)ses.getAttribute("DATAPAGINADOR");

	
	
	   if ( hm.get("datos")!=null && !hm.get("datos").equals("")){
	  	resultado = (Vector)hm.get("datos");
	  	Paginador paginador = (Paginador)hm.get("paginador");
	
	 	paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
	
	 	totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
	
	 	registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	   }else{
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
	
    String action=app+"/CEN_MantenimientoFacturacion.do?noReset=true";
%>

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="mantenimientoFacturacionForm" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
 	<script>
 		function refrescarLocal(){
 			parent.buscar();
 		}
 	</script>
 	
</head>

<body class="tablaCentralCampos">

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/CEN_MantenimientoFacturacion.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">

		<!-- Campo obligatorio -->
		<html:hidden styleId = "modo"  property = "modo" value = "" />
		<input type="hidden" id="actionModal"  name="actionModal" value="">
		</html:form>	
		
<%
		String tamanosCol="";
		String nombresCol="";
		tamanosCol="15,15,10,10,10,20,10,10";
		nombresCol+="factSJCS.datosFacturacion.literal.institucion," +
					"factSJCS.datosFacturacion.literal.estado,"+
					"factSJCS.datosFacturacion.literal.fechaEstado,"+
					"factSJCS.datosFacturacion.literal.fechaInicio," +
					"factSJCS.datosFacturacion.literal.fechaFin," +
					"factSJCS.datosFacturacion.literal.nombre," +
					"factSJCS.datosFacturacion.literal.regularizacion,";
%>

		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="<%=nombresCol %>"
		   columnSizes="<%=tamanosCol %>">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>			
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
<%	
	} else { 

		// recorro el resultado
		for (int i=0;i<resultado.size();i++) {
			//Hashtable registro = (Hashtable) resultado.get(i);
			Row fila = (Row)resultado.elementAt(i);
			Hashtable registro = (Hashtable) fila.getRow();
			String cont = new Integer(i+1).toString();

			String modo = "";
			String idInstitucion = UtilidadesString.mostrarDatoJSP(registro.get(FcsFacturacionJGBean.C_IDINSTITUCION));
			if (usrbean.getLocation().equals(idInstitucion)) {
				modo = "edicion";
			} else {
				modo = "consulta";
			}

			String idFacturacion = UtilidadesString.mostrarDatoJSP(registro.get(FcsFacturacionJGBean.C_IDFACTURACION));
			String fechaIni = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),registro.get(FcsFacturacionJGBean.C_FECHADESDE)));
			String fechaFin = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),registro.get(FcsFacturacionJGBean.C_FECHAHASTA)));
			String nombre = UtilidadesString.mostrarDatoJSP(registro.get(FcsFacturacionJGBean.C_NOMBRE));
			String estado = UtilidadesString.mostrarDatoJSP(UtilidadesMultidioma.getDatoMaestroIdioma((String)registro.get("DESESTADO"),usrbean));
			String idestado = UtilidadesString.mostrarDatoJSP(registro.get("IDESTADO"));
			String fechaEstado = UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usrbean.getLanguage(),registro.get("FECHAESTADO")));
			String nomInstitucion = UtilidadesString.mostrarDatoJSP(registro.get(CenInstitucionBean.C_ABREVIATURA));
			String regularizacion = UtilidadesString.mostrarDatoJSP(registro.get(FcsFacturacionJGBean.C_REGULARIZACION));
			String borrarporGrupo = UtilidadesString.mostrarDatoJSP(registro.get("BORRAPORGRUPO"));
			String borrarporEstado = UtilidadesString.mostrarDatoJSP(registro.get("BORRARPORESTADO"));
			boolean borrarestado=false;
			boolean borrargrupo=false;
			// permisos de acceso
			String permisos = "C";
			String borrar = "N";
			
			if (borrarporGrupo.equals("1")&& borrarporEstado.equals("1")){//se puede borrar por grupo y por estado
				permisos += ",B";
				borrar = "S";
			}
			
			if (idInstitucion.equals(usrbean.getLocation()) && (idestado.equals(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_ABIERTA.getCodigo()).toString()) 
					|| idestado.equals(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_EJECUTADA.getCodigo()).toString())
					|| idestado.equals(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_EN_PROCESO.getCodigo()).toString())					
					|| idestado.equals(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_VALIDACION_NO_CORRECTA.getCodigo()).toString())
					|| idestado.equals(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_NO_DISPONIBLE.getCodigo()).toString())
					|| idestado.equals(new Integer(ESTADO_FACTURACION.ESTADO_FACTURACION_ENVIO_NO_ACEPTADO.getCodigo()).toString()))) {
				permisos += ",E";
			}
			
			
		 
									
%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->

  			<siga:FilaConIconos fila="<%=cont %>" botones="<%=permisos %>" modo="<%=modo %>" clase="listaNonEdit">
			
				<td>

					<!-- campos hidden -->
					<input type="hidden" name="oculto<%=cont %>_1" value="<%=idFacturacion %>">
					<input type="hidden" name="oculto<%=cont %>_2" value="<%=idInstitucion %>">
					<input type="hidden" name="oculto<%=cont %>_3" value="<%=usrbean.getLocation() %>">
					<input type="hidden" name="oculto<%=cont %>_4" value="<%=borrar %>">


					<%=nomInstitucion %>
				</td>
				<td>
				<% if (!estado.equals("") && !estado.equals("&nbsp;") && !estado.equals("&nbsp")) { %>
				<siga:Idioma key="<%=estado%>"/>
				<% } else { %>
				&nbsp;
				<% } %>
				</td>
				<td>
					<%=fechaEstado %>
				</td>
				<td>
					<%=fechaIni %>
				</td>
				<td>
					<%=fechaFin %>
				</td>
				<td>
					<%=nombre %>
				</td>
				<td>
					<%=regularizacion%>
				</td>

			</siga:FilaConIconos>		


			<!-- FIN REGISTRO -->
<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:Table>

		<!-- FIN: LISTA DE VALORES -->
		
		 <%if ( hm.get("datos")!=null && !hm.get("datos").equals("")){%>
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPor"								
								clase="paginator" 
								divStyle="position:absolute; width:100%; height:20; z-index:3; bottom: 0px; left: 0px"
								distanciaPaginas=""
								action="<%=action%>" />
	 <%}%>						
			
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
