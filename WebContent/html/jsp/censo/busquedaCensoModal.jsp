<!DOCTYPE html>
<html>
<head>
<!-- busquedaCensoModal.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.censo.form.BusquedaCensoForm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="java.util.Properties"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.UsrBean"%>
 
<!-- JSP -->

<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
%>	
	
<%  
	// locales
	//BusquedaCensoForm formulario = (BusquedaCensoForm)request.getSession().getAttribute("busquedaCensoModalForm");
	
	String titu = "general.censo.titulo.busqueda";
	String busc = "censo.busquedaClientes.literal.titulo";

	// institucionesVisibles
	String parametro[] = new String[1];
	String busquedaSancion=(String) request.getAttribute("busquedaSancion");

	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	parametro[0] = user.getLocation();

	// MAV 14/7/05 Mostrar combo solo para aquellos colegios que permitan busquedas en m�s de una institucion
	// Obtengo el UserBean y en consecuencia la institucion a la que pertenece y su nombre
	
	String institucionAcceso=user.getLocation();

	// colegiado
	String colegiado = null;
	if (colegiado==null) colegiado="";
	// datos seleccionados Combo



%>	



<!-- HEAD -->


	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el t�tulo y localizaci�n en la barra de t�tulo del frame principal -->
	<siga:Titulo 
		titulo="<%=titu%>" 
		localizacion="<%=titu%>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="busquedaCensoModalForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	

</head>

<body onLoad="ajusteAltoBotones('resultadoModal');">

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="<%=titu %>"/>
		</td>
	</tr>
	</table>

<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->


	<siga:ConjCampos leyenda="censo.busquedaClientes.literal.titulo1">

	<table class="tablaCampos" align="center">

	<html:form action="/CEN_BusquedaCensoModal" method="POST" target="resultadoModal">
	<html:hidden name="busquedaCensoModalForm" property = "modo" value = ""/>
	<html:hidden name="busquedaCensoModalForm" property = "idPersona" />
	<html:hidden name="busquedaCensoModalForm" property = "idInstitucion" />
	<input type="hidden" name="clientes" value="<%=(String)request.getAttribute("clientes")%>">
    <input type="hidden" name="busquedaSancion" value="<%=busquedaSancion%>">
 

	<tr>				

	<td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.colegio"/>
		
	</td>				
	<td colspan="4">
		<!-- MAV 14/7/05 Mostrar combo solo para aquellos colegios que permitan busquedas en m�s de una institucion -->
		<siga:Select queryId="getInstitucionesCenso" id="nombreInstitucion"/>	
	</td>
	</tr>
	<!-- FILA -->
	<tr>				

	<!-- FILA -->
	<tr>				


	<!-- -->
	
	  <% if (busquedaSancion!=null && busquedaSancion.equals("1")){%><!-- si venimos de sanciones no mostramos el numero de colegiado-->
	   <td class="labelText" colspan="2">
		 &nbsp;
	   </td>		 
	  <%}else{%>
	  <td class="labelText">
	    <siga:Idioma key="censo.busquedaClientesAvanzada.literal.nColegiado"/>
	  </td>	
	  <td>
	    <html:text name="busquedaCensoModalForm" property="numeroColegiado" maxlength="20" size="10" styleClass="box"></html:text>
	  </td>
	  <%}%>	
	
    <td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.checkBusqueda"/>
	</td>
	<td>
		<input type="checkbox" name="chkBusqueda" checked>

	</td>
	

	</tr>				
	<!-- FILA -->
	<tr>				
    <td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.nif"/>
	</td>
	<td>
		<html:text name="busquedaCensoModalForm" property="nif" size="15" styleClass="box"></html:text>
	</td>
	<td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.nombre"/>
	</td>				
	<td>
		<html:text name="busquedaCensoModalForm" property="nombre" size="30" styleClass="box"></html:text>
	</td>

	

	</tr>				
	<!-- FILA -->
	<tr>				
    <td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.apellido1"/>
	</td>
	<td>
		<html:text name="busquedaCensoModalForm" property="apellido1" size="30" styleClass="box"></html:text>
	</td>
	<td class="labelText">
		<siga:Idioma key="censo.busquedaClientes.literal.apellido2"/>
	</td>
	<td>
		<html:text name="busquedaCensoModalForm" property="apellido2" size="30" styleClass="box"></html:text>
	</td>

	


	</tr>

	</html:form>
	</table>

	</siga:ConjCampos>

<html:form action="/CEN_DatosGenerales" method="POST" target="mainWorkArea">
		<input type="hidden" name="actionModal" value="1">
		<input type="hidden" name="modo" value="altaNoColegiado">
	</html:form>
	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		<siga:ConjBotonesBusqueda botones="B,CE"  modal="G" titulo="<%=busc%>" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">

		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{		
			//if (validatebusquedaCensoModalForm(document.forms[0])) 
			//{
				sub();
				document.forms[0].modo.value="buscarTodosModalInit";
				document.forms[0].submit();	
			//}
		}

	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<%=app%>/html/jsp/general/blank.jsp"
					id="resultadoModal"
					name="resultadoModal" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0"					 
					class="frameGeneral">
	</iframe>

	<!-- FIN: IFRAME LISTA RESULTADOS -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->

	<!-- MAV 7/9/2005 Incidencia, acortar espacio de resultados para no truncar botones -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">


		<!-- Asociada al boton Cerrar -->
		function cerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}


	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->




<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
