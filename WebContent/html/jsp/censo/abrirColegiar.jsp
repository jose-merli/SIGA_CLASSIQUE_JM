<!-- BusquedaClientes.jsp -->
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
<%@ page import="com.siga.censo.form.BusquedaClientesForm"%>
<%@ page import="com.siga.beans.CenInstitucionAdm"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Date"%>
<%@ page import="com.atos.utils.UsrBean,java.util.*" %>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>
<!-- JSP -->
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
	String idPersonaX=(String)request.getAttribute("idPersonaX");
	
	String idInstitucion[] = new String[1];
	idInstitucion[0] = user.getLocation();
	
	
%>	
	
<%  
	// locales
	
	String titu = "";
	String loca = "censo.busquedaClientes.localizacion";
	
	String fecha		= "";
	if(fecha.equals(""))
	{
		java.text.SimpleDateFormat formador = new java.text.SimpleDateFormat("dd/MM/yyyy");
		fecha = formador.format(new Date());
	}
	
	ArrayList aMetodoSol = new ArrayList();
	aMetodoSol.add(3);
	String [] parametro = {user.getLocation(),user.getLocation()};
	
	String fechaSolicitud = UtilidadesBDAdm.getFechaBD("");
	
%>	

<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	
		
	<script type="text/javascript" src="<%=app%>/html/js/jquery.msgbox.js"></script>
	<script type="text/javascript" src="<%=app%>/html/js/jquery.dragndrop.min.js"></script>
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>
	<script src="<%=app%>/html/jsp/general/validacionSIGA.jsp" type="text/javascript"></script>
	<script src="/SIGA/html/js/calendarJs.jsp" type="text/javascript"></script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo 
		titulo="<%=titu%>" 
		localizacion="<%=loca%>"/>
	<!-- FIN: TITULO Y LOCALIZACION -->

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
		
	  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">

		//Funcion asociada a boton Finalizar
		function accionCerrar()
		{
			window.top.close();
		}

		//Asociada al boton Aceptar
		function accionAceptar()
		{ sub();
			//var aux=document.forms[0].idPlantilla.value;
			var aux1=document.forms[0].nombreColegios.value;
			var aux2=document.forms[0].numeroColegiado.value;
            var aux3=document.forms[0].estadoColegial.value;
			var aux4=document.forms[0].fechaEstado.value;
			
			if(aux1=="")
			{  fin();
				var mensaje = "<siga:Idioma key="censo.colegiarNoColegiado.literal.colegio"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
			if(aux2=="")
			{   fin();
				var mensaje = "<siga:Idioma key="censo.busquedaClientesAvanzada.literal.nColegiado"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
			if(aux3=="")
			{   fin();
				var mensaje = "<siga:Idioma key="censo.colegiarNoColegiados.literal.estado"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
			if(aux4=="")
			{   fin();
				var mensaje = "<siga:Idioma key="certificados.solicitudes.literal.fechaEstado"/> <siga:Idioma key="messages.campoObligatorio.error"/>";
				alert (mensaje);
				return false;
			}
           
			document.forms[0].modo.value="insertar";
			document.forms[0].target="submitArea";
			
			document.forms[0].submit();
			
			
		}
		</script>
		<!-- FIN: SCRIPTS BOTONES -->

</head>

<body >

	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
					<siga:Idioma key="censo.colegiarNoColegiado.literal.titulo1"/>
				</td>
			</tr>
		</table>

<div id="camposRegistro" class="posicionModalPeque" align="center">

	<html:form action="/Cen_BotonesAccion.do" method="POST" target="_self">
    <input type="hidden" name="modo" value="editar">
	<input type="hidden" name="idPersonaX" value="<%=idPersonaX%>">
		<siga:ConjCampos>
			<table class="tablaCampos"align="center" width="100%">
				<tr >
						<td class="labelText" width="35%">
							<siga:Idioma key="censo.colegiarNoColegiado.literal.colegio"/>&nbsp;(*)
						</td>
						<td colspan="2">
						  <siga:ComboBD nombre="nombreColegios" 
							    		tipo="cmbNombreColegios" 
										clase="boxCombo"
										readonly="false"
										obligatorio="true"
										parametro="<%=idInstitucion%>"
						  />									
						</td>
				</tr>
				<tr>
				       <td class="labelText">
					       <siga:Idioma key="censo.busquedaClientesAvanzada.literal.nColegiado"/>&nbsp;(*)
				       </td>
		               <td colspan="2">
			                <input type="text" name="numeroColegiado" size="10" maxlength="20" class="box" value="">
		               </td>
					   
				</tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="censo.colegiarNoColegiados.literal.estado"/>&nbsp;(*)
					</td>
					<td>
					<siga:ComboBD nombre="estadoColegial" 
					    		tipo="cmbEstadoColegial" 
								clase="boxCombo"
								readonly="false"
								obligatorio="true" />	
	              	</td>
	            </tr>
				<tr>
					<td class="labelText">
						<siga:Idioma key="certificados.solicitudes.literal.fechaEstado"/>&nbsp;(*)
				    </td>
					<td>
						<siga:Fecha nombreCampo="fechaEstado" valorInicial="<%=fecha%>"></siga:Fecha>
					</td>
				</tr>
	             </table>
	             <table class="tablaCampos"align="center" width="100%">
	             <tr>
					<td class="labelText" width="20%">
						<label for="checkComunitario">
				       	<siga:Idioma key="censo.consultaDatosGenerales.literal.comunitario"/>
				       	</label>
				   </td>
			       <td width="10%">
		                <input id="checkComunitario" type="checkbox" name="comunitario">
	               </td>
	               <td class="labelText" width="20%">
	               		<label for="checkResidente">
	               		<siga:Idioma key="censo.consultaDatosColegiales.literal.residente"/>
	               		</label>
			       	</td>
			       	<td width="10%">
	                	<input id="checkResidente" type="checkbox" name="situacionResidente">
	               </td>
	               <td></td>
				</tr>
			</table>
		</siga:ConjCampos>
		<siga:ConjCampos>
			<table class="tablaCampos"align="center" width="100%">
				<tr>
				     <td class="labelText" width="55">
					       <siga:Idioma key="gratuita.BusquedaSancionesLetrado.literal.observaciones"/>
   			         </td>
		             <td colspan="3">
			                <textarea COLS=80 ROWS=4 NAME="observaciones" class="box" onKeyDown="cuenta(this,255)" onChange="cuenta(this,255)"></textarea>  
		             </td>
				</tr>
			</table>
		</siga:ConjCampos>
			</html:form>
			<siga:ConjBotonesAccion botones="A,C" modal="P"/>
		</div>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>