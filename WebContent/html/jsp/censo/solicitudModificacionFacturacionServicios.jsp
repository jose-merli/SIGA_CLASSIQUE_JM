<!-- solicitudModificacionFacturacionServicios.jsp -->
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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.siga.censo.form.DatosFacturacionForm"%>
<%@ page import="com.siga.beans.CenClienteBean"%>
<%@ page import="com.siga.beans.CenPersonaBean"%>
<%@ page import="com.siga.censo.form.DatosFacturacionForm"%>
<%@ page import="com.siga.beans.CenSolModiFacturacionServicioBean"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.ArrayList" %>
<!-- JSP -->
<%  
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	

	DatosFacturacionForm miForm = (DatosFacturacionForm) request.getAttribute("datosFacturacionForm");
	String idPersona = miForm.getIdPersona();
	String idInstitucion = miForm.getIdInstitucion();

	Vector datos = (Vector)request.getAttribute("PysDatosFacturacionInsertarSolicitud");
	String idTipoServicios = "";
	String idServicio = "";
	String idServiciosInstitucion = "";
	String idPeticion = "";
	String motivo = "";
	String cuentaBancaria = "";
	ArrayList idCuentaSel = new ArrayList();
	
	if (datos!=null) {
		cuentaBancaria = (String)datos.get(0);
		idTipoServicios = (String)datos.get(2);
		idServicio = (String)datos.get(3);
		idServiciosInstitucion = (String)datos.get(4);
		idPeticion = (String)datos.get(5);
	}
	
	idCuentaSel.add(cuentaBancaria);

	Vector cuentas = (Vector) request.getAttribute("CenCuentasBancariasResultados");
	if (cuentas==null || cuentas.size()==0) {
		cuentas = new Vector();
	}

	String parametro[] = new String[2];
	parametro[0] = idPersona;
	parametro[1] = idInstitucion; 
%>	
<html>

<!-- HEAD -->
<head>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="";
		localizacion="" />
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="datosFacturacionForm" staticJavascript="false" />  
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	
</head>

<body>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq">
			<siga:Idioma key="cen.datosFacturacion.titulo3"/>
		</td>
	</tr>
	</table>


<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
<!-- dentro de esta capa se tienen que situar los diferentes componentes 
	 que se van a mostrar, para que quepen dentro de la ventana.
	 Los elementos que copieis dentro, que tengan el estilo 
	 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
-->
<div id="camposRegistro" class="posicionModalPeque" align="center">

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table  class="tablaCentralCamposPeque"  align="center">

	<html:form action="/CEN_Facturacion.do" method="POST" target="submitArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden name="datosFacturacionForm" property = "idPersona" value = "<%=idPersona %>" />
	<html:hidden name="datosFacturacionForm" property = "idInstitucion" value = "<%=idInstitucion %>" />

	<html:hidden property = "idTipoServicios" value = "<%=idTipoServicios %>" />
	<html:hidden property = "idServicio" value = "<%=idServicio %>" />
	<html:hidden property = "idServiciosInstitucion" value = "<%=idServiciosInstitucion %>" />
	<html:hidden property = "idPeticion" value = "<%=idPeticion %>" />

	<!- datos para insertar -->


	<!-- FILA -->
	<tr>				
	<td>

	<siga:ConjCampos leyenda="cen.datosFacturacion.titulo3">

	<table class="tablaCampos" align="center">

	<td class="labelText">
		<siga:Idioma key="cen.consultaProductos.literal.nCuenta"/>&nbsp;(*)
	</td>				
	<td>
		<siga:ComboBD nombre="cuentaBancaria" tipo="cuentaCargo" parametro="<%=parametro%>" clase="box" obligatorio="true" elementoSel="<%=idCuentaSel%>" />
	</td>

	</tr>

	<!-- FILA -->
	<tr>				

	<td class="labelText">
		<siga:Idioma key="cen.datosFacturacion.literal.motivo"/>&nbsp;(*)
	</td>				

	<td>
		<html:textarea name="datosFacturacionForm" property="motivo" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)" value="<%=motivo %>" styleClass="box" readOnly="false" size="60" rows="4"></html:textarea>
	</td>
	</tr>

	</table>

	</siga:ConjCampos>


	</td>
	</tr>

	</html:form>
	
	</table>



	<!-- FIN: CAMPOS -->

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

		<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />

	<!-- FIN: BOTONES REGISTRO -->

	
	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">

 
		<!-- Asociada al boton GuardarCerrar -->
		function accionGuardarCerrar() 
		{		
			sub();
			if (validateDatosFacturacionForm(document.forms[0])) 
			{
				document.forms[0].modo.value="insertarSolicitud";
				document.forms[0].submit();
			}else{
				fin();
			}
		}

		<!-- Asociada al boton Cerrar -->
		function accionCerrar() 
		{		
			top.cierraConParametros("NORMAL");
		}

		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() 
		{		
			document.forms[0].reset();
		}


	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->


</div>

<!-- FIN ******* CAPA DE PRESENTACION ****** -->


<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>
