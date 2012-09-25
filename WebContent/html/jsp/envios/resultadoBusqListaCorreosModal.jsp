<!-- resultadoBusqListaCorreosModal.jsp -->
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
<%@ page import="com.siga.envios.form.ListaCorreosForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.EnvListaCorreosBean"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.tlds.FilaExtElement"%>

<!-- JSP -->
<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean usrbean = (UsrBean)session.getAttribute(ClsConstants.USERBEAN);
%>	

<%  
	// locales
	//BusquedaClientesForm formulario = (BusquedaClientesForm)request.getSession().getAttribute("busquedaClientesModalForm");
	ListaCorreosForm formulario = (ListaCorreosForm)request.getAttribute("ListaCorreosForm");
	
	Vector resultado = (Vector) request.getAttribute("datos");

%>

<html>
<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

<%--
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="/CEN_BusquedaClientesModal.do" staticJavascript="false" />  
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="<%=titu %>" 
		localizacion="<%=titu %>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->
--%>

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
		  j++;
		}
		datos.value = datos.value + "%";
    	document.ListaCorreosForm.modo.value = "enviarLista";
	   	document.ListaCorreosForm.submit();
	}
	</script>

</head>

<body >

		<!-- INICIO: LISTA DE VALORES --> 
		<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

		<!-- Formulario de la lista de detalle multiregistro -->
		<html:form action="/ENV_ListaCorreos.do" method="POST" target="submitArea" style="display:none">

		<!-- Campo obligatorio -->
		<html:hidden property = "modo" value = "" />

			<!-- RGG: cambio a formularios ligeros -->
			
			<input type="hidden" name="actionModal" value="">
		</html:form>


		<siga:TablaCabecerasFijas 
		   nombre="tablaDatos"
		   borde="1"
		   clase="tableTitle"
		   nombreCol="envios.listas.literal.lista,
		   		  			  envios.listas.literal.descripcion,
		   		  			  envios.listas.literal.dinamica,"
		   		  tamanoCol="22,55,10,13"
		   			alto="100%"

		  >

			<!-- INICIO: ZONA DE REGISTROS -->
			<!-- Aqui se iteran los diferentes registros de la lista -->
			
<%	if (resultado==null || resultado.size()==0) { %>
	 		<br><br>
	   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 		<br><br>	 		
<%	
	} else { 

		FilaExtElement[] elems=new FilaExtElement[1];
		elems[0]=new FilaExtElement("seleccionar","seleccionar",SIGAConstants.ACCESS_READ);  	

		// recorro el resultado
		for (int i=0;i<resultado.size();i++) {
			//Hashtable registro = (Hashtable) resultado.get(i);
			EnvListaCorreosBean bean = (EnvListaCorreosBean) resultado.get(i);
			String cont = new Integer(i+1).toString();

			String modo = "consulta";
			String idInstitucion = usrbean.getLocation();

			String idListaCorreos = String.valueOf(bean.getIdListaCorreo());
			String nombre = bean.getNombre();
			String descripcion = bean.getDescripcion();
			String dinamica = bean.getDinamica();

%>
			<!-- REGISTRO  -->
			<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->
			
			<siga:FilaConIconos fila="<%=cont %>" botones="" modo="<%=modo %>" elementos="<%=elems%>" visibleBorrado="no" visibleEdicion="no" visibleConsulta="no" clase="listaNonEdit">
			
				<td>

					<!-- campos hidden -->
					<input type="hidden" name="oculto<%=cont %>_1" value="<%=idListaCorreos %>">

					<%=UtilidadesString.mostrarDatoJSP(nombre) %>
				</td>
				<td>
					<%=UtilidadesString.mostrarDatoJSP(descripcion) %>
				</td>
				<td><%
								if (dinamica.equals("S"))
								{ %>
									<siga:Idioma key="general.yes"/>
							<%} else {%>
									<siga:Idioma key="general.no"/>
							<%}%>
					</td>

			</siga:FilaConIconos>

			<!-- FIN REGISTRO -->
<%		} // del for %>			

			<!-- FIN: ZONA DE REGISTROS -->

<%	} // del if %>			

		</siga:TablaCabecerasFijas>


		<!-- FIN: LISTA DE VALORES -->
	
<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

	</body>
</html>
