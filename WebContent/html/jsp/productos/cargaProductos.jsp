<!-- cargaProductos.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import = "com.siga.administracion.SIGAConstants"%>
<%@ page import = "com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import = "com.siga.Utilidades.UtilidadesString"%>
<%@ page import = "com.atos.utils.*"%>
<%@ page import = "com.siga.general.*"%>
<%@ page import="java.util.Properties"%>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);

	UsrBean user=(UsrBean)request.getSession().getAttribute("USRBEAN");
	
%>

<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">

		<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
		<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
		
		<!-- INICIO: TITULO Y LOCALIZACION -->
		<!-- Escribe el título y localización en la barra de título del frame principal -->
		<siga:Titulo 
			titulo="pys.cargaProductos.titulo" 
			localizacion="pys.cargaProductos.localizacion"/>
		<!-- FIN: TITULO Y LOCALIZACION -->

		<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
		<script language="JavaScript">
		
			function accionGuardar() {
			
				f= document.CargaProductosForm;
				
		    	f.modo.value = "guardarFich";
				f.submit();		
			}
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
	</head>

	<body>
		<siga:ConjCampos leyenda="pys.cargaProductos.titulo">
			<table   align="left" cellpadding="0" cellpadding="0">
				<html:form  action="/PYS_CargaProductos.do" method="POST" target="submitArea" enctype="multipart/form-data" >
				<html:hidden property = "modo" value = ""/>
					<tr>
						<!-- CLIENTE -->
						<td class="labelText" >
							<siga:Idioma key="pys.cargaProductos.literal.fichero"/>&nbsp;(*)
						</td>				
						<td >
							<html:file name="CargaProductosForm"  property="fichero" size="90" styleClass="box"  ></html:file>
						</td>
					</tr>
				</html:form>
			</table>
		</siga:ConjCampos>				
		<div class="labelTextValue" style="padding-left:30px;padding-top:30px;width:80%">
		<p>El fichero de carga de compras debe ser un fichero de texto, con una linea por cada compra.</p>
		<p>Los campos necesarios para poder efectuar la compra son los siguientes:</p>
		<ul>
			<li>Número de colegiado (Opcional)</li>
			<li>NIF/CIF del cliente</li>
			<li>Apellidos y nombre del cliente</li>
			<li>Cantidad del producto</li>
			<li>Nombre del producto</li>
			<li>Identificador de la Categoría</li>
			<li>Identificador del Tipo de Producto</li>
			<li>Identificador del Producto</li>
		</ul>
		<p>Todos los campos, salvo el Número de colegiado, son obligatorios y se deben separar usando el caracter reservado <b>:</b></p>
		<p>Los identificadores de los productos pueden obtenerse en la ficha del producto.</p>
		<p class="labelText">Ejemplo</p>
		<div class="labelTextValue" style="padding-left:20px; padding-bottom:30px;">
		: 34620345B : Alonso García, Nieves : 1 : Cuota incorporación : 9 : 3 : 1<br>
		543545 : 85345671A : Pérez López, Antonio : 1 : Carnet Colegial A.C.A : 15 : 3 : 1<br>
		543545 : 85345671A : Pérez López, Antonio : 1 : Cuota Consejo : 1 : 6 : 6<br>
		543545 : 85345671A : Pérez López, Antonio : 3 : Cuotas atrasadas : 5 : 2 : 2<br>
		: 64564536K : Construcciones S.A. : 6 : Fax : 3 : 7 : 8<br>

		</div>
		</div>
		<siga:ConjBotonesAccion botones="G" modal="P" clase="botonesSeguido"/>

		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>