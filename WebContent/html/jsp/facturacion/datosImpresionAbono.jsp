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

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>


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

%>	

<html>

	<!-- HEAD -->
	<head>

		<style type="text/css">
			.totales {
				font-family: <%=src.get("font.style")%>;
				font-weight: bold; color: #<%=src.get("color.nonEdit.selected.font")%>;
				margin: auto; padding-right: 4px;
				padding-left: 3px; vertical-align: middle;
				text-align: left; padding-top: 3px;
				background-color: #<%=src.get("color.nonEdit.selected.BG")%>; padding-bottom: 3px;
			}
			
			.misBotones {
				text-align: left;
				color:#<%=src.get("color.labelText")%>;
				padding-left: 0px;
				padding-right: 10px;
				padding-top: 5px;
				padding-bottom: 5px;
				width: 10px;
				height: 30px;
			}
			
		</style>

		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
			<!-- El nombre del formulario se obtiene del struts-config -->
			<html:javascript formName="" staticJavascript="false" />  
			<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->

		<!-- SCRIPTS LOCALES -->
		<script language="JavaScript">
				
		</script>
 	
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="facturacion.administracionAbonos.literal.cabecera" 
			localizacion="facturacion.administracionAbonos.ruta"/>
		<!-- FIN: TITULO Y LOCALIZACION -->
	
	</head>

	<body>

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulosPeq">
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
		<div id="camposRegistro" class="posicionModalPeque" align="center">

			<!-- INICIO: CAMPOS -->
			<!-- Zona de campos de busqueda o filtro -->

			<table  class="tablaCentralCamposPeque"  align="center">
				<tr>				
					<td>
						<siga:ConjCampos leyenda="facturacion.abonosPagos.datosPagoAbono.datosPago">
							<table class="tablaCampos" align="center">
								<html:form action="/FAC_AbonosPagos.do" method="POST" target="submitArea">
									<html:hidden property ="modo" value = ""/>
									<html:hidden property="idAbono" value=""/>
									<html:hidden property="idInstitucion" value=""/>
									<html:hidden property="pagoPendiente" value=""/>
									<tr>
										<td class="labelText">
											<%=UtilidadesString.mostrarDatoJSP(nombreInstitucion)%>
										</td>
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="facturacion.busquedaAbonos.literal.fecha"/>&nbsp;:&nbsp;
										 	<% fecha=GstDate.getFormatedDateLong("",registro.getString(FacPagoAbonoEfectivoBean.C_FECHA));%>
											<%=UtilidadesString.mostrarDatoJSP(fecha)%>
										</td>	
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="facturacion.abonosPagos.impresionAbono.abonadoA"/>&nbsp;:&nbsp;
											<%=UtilidadesString.mostrarDatoJSP(nombrePersona)%>
										</td>	
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key="facturacion.abonosPagos.impresionAbono.abonoN"/>&nbsp;&nbsp;
											<%=UtilidadesString.mostrarDatoJSP(numeroAbono)%>
										</td>	
									</tr>
										<td class="labelText">
											<siga:Idioma key="facturacion.abonosPagos.impresionAbono.pagoCaja"/>&nbsp;:&nbsp;
											<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(registro.getString(FacPagoAbonoEfectivoBean.C_IMPORTE)))%>&nbsp;&euro;
										</td>	
									</tr>
								</html:form>	
							</table>
						</siga:ConjCampos>
					</td>
				</tr>
			</table>
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
			
		<siga:ConjBotonesAccion botones="I,C" clase="botonesDetalle" modal="P"/>
			
		<!-- FIN: BOTONES REGISTRO -->

	
		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
		
 		function accionCerrar(){ 
	 		window.top.close();
 		}
 		
 		function accionImprimir(){
	 		window.print();
 		}
			
		</script>
	
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->

	</body>
</html>
