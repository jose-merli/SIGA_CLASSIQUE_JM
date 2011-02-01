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

	String delimitador = (String)request.getAttribute("DELIMITADOR");
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

	<body onload="sub();">
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
							<html:file name="CargaProductosForm"  property="fichero" size="90" styleClass="box"  onchange="fin();"></html:file>
						</td>
					</tr>
				</html:form>
			</table>
		</siga:ConjCampos>				
		<div class="labelTextValue" style="padding-left:30px;padding-top:30px;width:85%">
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
		<p>Todos los campos, salvo el Número de colegiado, son obligatorios y se deben separar usando el caracter reservado <b><%=delimitador%></b></p> 
		<p>Los identificadores de los productos pueden obtenerse en la ficha del producto.</p>
		<p class="labelText">Ejemplo</p>
		<div class="labelTextValue" style="padding-left:20px; padding-bottom:30px;">
		<%=delimitador%> 34620345B <%=delimitador%> Alonso García, Nieves <%=delimitador%> 1 <%=delimitador%> Cuota incorporación <%=delimitador%> 9 <%=delimitador%> 3 <%=delimitador%> 1<br>
		543545 <%=delimitador%> 85345671A <%=delimitador%> Pérez López, Antonio <%=delimitador%> 1 <%=delimitador%> Carnet Colegial A.C.A <%=delimitador%> 15 <%=delimitador%> 3 <%=delimitador%> 1<br>
		543545 <%=delimitador%> 85345671A <%=delimitador%> Pérez López, Antonio <%=delimitador%> 1 <%=delimitador%> Cuota Consejo <%=delimitador%> 1 <%=delimitador%> 6 <%=delimitador%> 6<br>
		543545 <%=delimitador%> 85345671A <%=delimitador%> Pérez López, Antonio <%=delimitador%> 3 <%=delimitador%> Cuotas atrasadas <%=delimitador%> 5 <%=delimitador%> 2 <%=delimitador%> 2<br>
		<%=delimitador%> 64564536K <%=delimitador%> Construcciones S.A. <%=delimitador%> 6 <%=delimitador%> Fax <%=delimitador%> 3 <%=delimitador%> 7 <%=delimitador%> 8<br>

		</div>
		<p>Si el fichero es correcto se realizarán las compras y quedarán validadas.</p>
		<p>Si hay algun error se mostrará un LOG con los fallos a corregir. No se realizarán las compras hasta que el fichero cargado sea correcto.</p>
		<p>&nbsp;</p>
		</div>
		

<table id="tablaBotonesDetalle" class="botonesSeguido" align="center">
	<tr>
		<td style="width: 900px;">&nbsp;</td>
		<td class="tdBotones">
			<input type="button" alt='<siga:Idioma key="general.boton.new"/>'
			name='idButton' id="idButton" onclick="return accionGuardar();" class="button"
				value='Procesar Fichero'>
		</td>
	</tr>
</table>


		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>