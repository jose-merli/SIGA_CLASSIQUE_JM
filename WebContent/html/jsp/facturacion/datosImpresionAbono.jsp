<!DOCTYPE html>
<html>
<head>
<!-- datosImpresionAbono.jsp -->

<!-- 
	 Muestra la pre-impresion del recibo de abono por caja
	 VERSIONES:
	 miguel.villegas 18-03-2005 
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

<%@ page import="com.atos.utils.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>

<!-- JSP -->
<% 
	String fecha="";
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
		
	Vector datosPago=new Vector();
	Row registro=new Row();

	// Datos a visualizar
	String nombrePersona=(String)request.getAttribute("nombrePersona"); // Obtengo el nombre de la persona
	String numeroAbono=(String)request.getAttribute("numeroAbono"); // Obtengo el numero
	
	String nombreInstitucion=(String)request.getAttribute("nombreInstitucion"); // Obtengo el nombre de la institucion
	if (request.getAttribute("container") != null){	
		datosPago = (Vector)request.getAttribute("container");	
		registro=(Row)datosPago.firstElement();
	}
	
	int altoImpresion = 275; 	// Especifica el alto para la impresion. Valor en pixeles	
%>	

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="" staticJavascript="false" />  
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
 	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->
	<siga:Titulo titulo="facturacion.administracionAbonos.literal.cabecera" localizacion="facturacion.administracionAbonos.ruta"/>
	<!-- FIN: TITULO Y LOCALIZACION -->	
</head>

<body>
	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="facturacion.abonosPagos.datosPagoAbono.datosPago"/>
			</td>
		</tr>
	</table>
	
	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<!-- dentro de esta capa se tienen que situar los diferentes componentes 
		 que se van a mostrar, para que quepen dentro de la ventana.
		 Los elementos que copieis dentro, que tengan el estilo 
		 "tablaTitulo" se deben modificar por "tablaCentralMedia" 
	-->
	<html:form action="/FAC_AbonosPagos.do" method="POST" target="submitArea">
		<html:hidden property ="modo" value = ""/>
		<html:hidden property="idAbono" value=""/>
		<html:hidden property="idInstitucion" value=""/>
		<html:hidden property="pagoPendiente" value=""/>
	
		<div id='areaImpresion' style='height:<%=altoImpresion%>px; overflow-y:auto'>
			<table border="0">
				<tr>
					<td class="labelText" colspan="2">
						<%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
					</td>
				</tr>
			
				<tr>
					<td class="labelText">
						<siga:Idioma key="facturacion.busquedaAbonos.literal.fecha"/>
					</td>
					<td class="labelTextValue">
					 	<% fecha=GstDate.getFormatedDateShort("",registro.getString(FacPagoAbonoEfectivoBean.C_FECHA));%>
						<%=UtilidadesString.mostrarDatoJSP(fecha)%>
					</td>	
				</tr>
			
				<tr>
					<td class="labelText">
						<siga:Idioma key="facturacion.abonosPagos.impresionAbono.abonadoA"/>
					</td>
					<td class="labelTextValue">
						<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%>
					</td>	
				</tr>
				
				<tr>
					<td class="labelText">
						<siga:Idioma key="facturacion.abonosPagos.impresionAbono.abonoN"/>
					</td>
					<td class="labelTextValue">
						<%=UtilidadesString.mostrarDatoJSP(numeroAbono)%>
					</td>	
				</tr>
			
				<tr>
					<td class="labelText">
						<siga:Idioma key="facturacion.abonosPagos.impresionAbono.pagoCaja"/>
					</td>
					<td class="labelTextValue">
						<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(registro.getString(FacPagoAbonoEfectivoBean.C_IMPORTE)))%>&nbsp;&euro;
					</td>	
				</tr>
			</table>			
		</div>
	</html:form>
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, G Guardar,Y GuardaryCerrar,R Restablecer,N Nuevo,C Cerrar,X Cancelar
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
	-->		
	<div id="botones">
		<siga:ConjBotonesAccion botones="I,C" clase="botonesDetalle" modal="P"/>
	</div>		
	<!-- FIN: BOTONES REGISTRO -->


	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript">
 		function accionCerrar(){ 
	 		window.top.close();
 		}
 		
		// Asociada al boton Imprimir
		function accionImprimir(){
			var divAreaImpresion = document.getElementById("areaImpresion");
			
			jQuery("#botones").hide();
			divAreaImpresion.style.height = "100%";
			window.print();
			divAreaImpresion.style.height = "<%=altoImpresion%>px";
			jQuery("#botones").show();
		}	
	</script>
	
	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->
</body>
</html>