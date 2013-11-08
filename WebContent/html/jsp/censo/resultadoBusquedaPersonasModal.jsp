<!DOCTYPE html>
<html>
<head>
<!-- resultadoBusquedaPersonasModal.jsp -->
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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>
<%@ page import="com.siga.Utilidades.Paginador"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.RowsContainer"%>
<!-- JSP -->
<% 
	
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
	String idioma=usrbean.getLanguage().toUpperCase();

	// locales
	//BusquedaClientesForm formulario = (BusquedaClientesForm)request.getAttribute("busquedaClientesModalForm");
	
	//Vector vPersonas = (Vector) request.getAttribute("personas");

	String titu = "censo.busquedaClientes.literal.titulo"; 
	
	 Vector vPersonas=null;
	/** PAGINADOR **/
	
	String paginaSeleccionada ="";
	
	String totalRegistros ="";
	
	String registrosPorPagina = "";
	HashMap hm = new HashMap();
	String idPaginador = (String)request.getAttribute(ClsConstants.PARAM_PAGINACION);
	if (ses.getAttribute(idPaginador)!=null) {
		hm = (HashMap)ses.getAttribute(idPaginador);
	
	 if ( hm!=null && hm.get("datos")!=null && !hm.get("datos").equals("")){
	  vPersonas = (Vector)hm.get("datos");
	  Paginador paginador = (Paginador)hm.get("paginador");
	
	 	paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
	
	 	totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
	
	 	registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina()); 
	 }else{
	  vPersonas =new Vector();
	  paginaSeleccionada = "0";
	
	 	totalRegistros = "0";
	
	 	registrosPorPagina = "0";
	 }
	
	}else{
      vPersonas =new Vector();
	  paginaSeleccionada = "0";
	
	 	totalRegistros = "0";
	
	 	registrosPorPagina = "0";
    }	
	
	String action=app+"/CEN_BusquedaClientesModal.do";
	
    /**************/

%>

<%@page import="com.siga.beans.CenClienteBean"%>

<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="/CEN_BusquedaPersonasModal.do" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="<%=titu %>" 
		localizacion="<%=titu %>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

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
		  if (oculto == null)  { flag = false; }
		  else { 
		  	datos.value = datos.value + oculto.value + ','; 
		  }
		  if(j=='3'){
	   	  		
		  		document.busquedaClientesModalForm.nif.value = oculto.value;
		  	}
		  	else if(j=='4'){
		  		
		  		document.busquedaClientesModalForm.nombrePersona.value = oculto.value;
		  	}
			else if(j=='5'){
				
		  		document.busquedaClientesModalForm.apellido1.value = oculto.value;
		  	}
			else if(j=='6'){
				
			 	document.busquedaClientesModalForm.apellido2.value = oculto.value;
			 }
		  
		  j++;
		}
		datos.value = datos.value + "%";
		
    	document.busquedaClientesModalForm.modo.value = "enviarPersona";
	   	document.busquedaClientesModalForm.submit();
	}
	function lopd(fila) {
		alert('<siga:Idioma key="general.boton.lopd"/>');
		
	}
	</script>

</head>

<body class="tablaCentralCampos">

		<html:form action="/CEN_BusquedaClientesModal.do" method="POST" target="submitArea" style="display:hidden">
		<!-- Campo obligatorio -->
		<html:hidden styleId = "modo" property = "modo"  value = "" />
			<!-- RGG: cambio a formularios ligeros -->
			<input type="hidden" id="tablaDatosDinamicosD" name="tablaDatosDinamicosD">
			<input type="hidden" id="actionModal" name="actionModal" value="">
			<html:hidden styleId="nif" property="nif"  value=""/>
			<html:hidden styleId="nombrePersona" property="nombrePersona" value=""/>
			<html:hidden styleId="apellido1" property="apellido1"  value=""/>
			<html:hidden styleId="apellido2" property="apellido2"  value=""/>
		</html:form>


<%
		String tamanosCol="";
		String nombresCol="";
		// cliente colegiado o  no
		tamanosCol="5,16,10,19,10,10,5";
		nombresCol="censo.busquedaClientesAvanzada.literal.nif,gratuita.turnos.literal.apellidosSolo,censo.busquedaClientesAvanzada.literal.nombre,censo.busquedaClientesAvanzada.literal.domicilio,censo.consultaDirecciones.literal.poblacion,censo.datosDireccion.literal.provincia,";

%>

		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="<%=nombresCol %>"
		   columnSizes="<%=tamanosCol %>">

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (vPersonas==null || vPersonas.size()==0) { %>			
	 		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>	
<%	
	} else { 

		FilaExtElement[] elems = null;  	

		// recorro el resultado
		for (int i=0;i<vPersonas.size();i++) {
		    Row fila = (Row)vPersonas.elementAt(i);
			Hashtable registro = (Hashtable) fila.getRow();
			boolean isAplicarLOPD = (String) registro.get(CenClienteBean.C_NOAPARECERREDABOGACIA) != null
			&& ((String) registro.get(CenClienteBean.C_NOAPARECERREDABOGACIA)).equals(ClsConstants.DB_TRUE);
			if(isAplicarLOPD){
				elems = new FilaExtElement[2];
				elems[1] = new FilaExtElement(
						"lopd",	"lopd",	SIGAConstants.ACCESS_READ);
				elems[0] = new FilaExtElement("seleccionar", "seleccionar",
						SIGAConstants.ACCESS_READ);
			}else{
				elems = new FilaExtElement[1];
				elems[0] = new FilaExtElement("seleccionar", "seleccionar",
						SIGAConstants.ACCESS_READ);
								
			}
			
			String cont = new Integer(i+1).toString();

			// permisos de acceso
			String modo = "";
			
			// el id del user.getlocation
			String idInstitucion = usrbean.getLocation();

			/*String idPersona = (registro.get(CenColegiadoBean.C_IDPERSONA)==null||((String)registro.get(CenColegiadoBean.C_IDPERSONA)).equals(""))?"&nbsp;":(String)registro.get(CenColegiadoBean.C_IDPERSONA);
			UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
			if (user.getLocation().equals(idInstitucion)) {
				modo = "edicion";
			} else {
				modo = "consulta";
			}*/
									
			// calculo de campos
			
			/*String idPersona = String.valueOf(registro.getIdPersona());
			String apellido1 = UtilidadesString.mostrarDatoJSP(registro.getApellido1());
			String apellido2 = UtilidadesString.mostrarDatoJSP(registro.getApellido2());
			String nombre = UtilidadesString.mostrarDatoJSP(registro.getNombre());
			String nif = UtilidadesString.mostrarDatoJSP(registro.getNIFCIF());*/
			
			String idPersona =(String)registro.get(CenPersonaBean.C_IDPERSONA);
			String apellido1 = UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_APELLIDOS1));
			String apellido2 = UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_APELLIDOS2));
			String nombre = UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_NOMBRE));
			String nif = UtilidadesString.mostrarDatoJSP(registro.get(CenPersonaBean.C_NIFCIF));
			String sql = "SELECT F_SIGA_DIRECCIONCOMPLETA(" + idInstitucion	+ ", " + idPersona+") as DIRECCION FROM DUAL";
			String valor="";			  
			
			RowsContainer rc = null;
			rc = new RowsContainer(); 
			if (rc.query(sql)) {
				if (rc.size() ==  1) {
					// Tratamos los datos de la funcion 'F_SIGA_CALCULOPRECIOSERVICIO'
					Hashtable hash = (Hashtable)((Row) rc.get(0)).getRow();
					valor = UtilidadesHash.getString(hash, "DIRECCION");
				}
			}		
			String direccion=valor;
			
			String domicilio="";
			String poblacion="";
			String provincia="";
			if (direccion!=null && direccion!=""){
			 String datosDireccion[] =  UtilidadesString.split(direccion, "#");
			
			 domicilio=UtilidadesString.mostrarDatoJSP(datosDireccion[0]).equals("0")?"&nbsp;":UtilidadesString.mostrarDatoJSP(datosDireccion[0]);
			 poblacion=UtilidadesString.mostrarDatoJSP(datosDireccion[1]).equals("0")?"&nbsp;":UtilidadesString.mostrarDatoJSP(datosDireccion[1]);
 			 provincia=UtilidadesString.mostrarDatoJSP(datosDireccion[2]).equals("0")?"&nbsp;":UtilidadesString.mostrarDatoJSP(datosDireccion[2]);
			

			}else{
			 domicilio="&nbsp;";
			 poblacion="&nbsp;";
			 provincia="&nbsp;";
			}
			
			
%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->
			
			<siga:FilaConIconos fila="<%=cont %>" botones=""
			 modo="<%=modo %>" elementos="<%=elems%>" visibleBorrado="no" 
			 visibleEdicion="no" visibleConsulta="no" 
			 clase="listaNonEdit" pintarEspacio="no">
			
				<td>

					<!-- campos hidden -->
					<input type="hidden" id="oculto<%=cont %>_1" name="oculto<%=cont %>_1" value="<%=idPersona %>">
					<input type="hidden" id="oculto<%=cont %>_2" name="oculto<%=cont %>_2" value="<%=idInstitucion %>">
					<input type="hidden" id="oculto<%=cont %>_3" name="oculto<%=cont %>_3" value="<%=nif %>">
					<input type="hidden" id="oculto<%=cont %>_4" name="oculto<%=cont %>_4" value="<%=nombre %>">
					<input type="hidden" id="oculto<%=cont %>_5" name="oculto<%=cont %>_5" value="<%=apellido1 %>">
					<input type="hidden" id="oculto<%=cont %>_6" name="oculto<%=cont %>_6" value="<%=apellido2 %>">

					<%=nif %>
				</td>
				<td>
					<%=apellido1+" "+apellido2 %>
				</td>
				
				<td>
					<%=nombre %>
				</td>
				<td>
					<%=domicilio %>
				</td>
				<td>
					<%=poblacion %>
				</td>
				<td>
					<%=provincia %>
				</td>
				

			</siga:FilaConIconos>		


			<!-- FIN REGISTRO -->
<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:Table>


		<!-- FIN: LISTA DE VALORES -->
		<!-- Pintamos la paginacion-->	
		<%if (hm!=null && hm.get("datos")!=null && !hm.get("datos").equals("")){%>
		<siga:Paginador totalRegistros="<%=totalRegistros%>" 
								registrosPorPagina="<%=registrosPorPagina%>" 
								paginaSeleccionada="<%=paginaSeleccionada%>" 
								idioma="<%=idioma%>"
								modo="buscarPersona"								
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
