<!-- listadoTramosRetencion.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


<html>

<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>

<!--Step 2 -->
<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript"
	src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>


<!--Step 3 -->
  <!-- defaults for Autocomplete and displaytag -->
  <link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />
 
<!-- Step 4 -->

<head>
       
</head>

</head>

<body  >

<div>		
	<table id='tabTramosRetencionCabeceras' border='1' width='100%' cellspacing='0' cellpadding='0'>
		<tr class = 'tableTitle'>
			<td align='center' width='8%'>Nº Tramo</td>
			<td align='center' width='8%'>Descripción</td>
			<td align='center' width='8%'>Nº SMI</td>
			<td align='center' width='8%'>Límite Tramo</td>
			<td align='center' width='8%'>Porcentaje</td>
			
		</tr>
	</table>
</div>


<table class="tablaCampos" id='tramosRetencion' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' style='table-layout:fixed'>
		<logic:notEmpty name="TramosRetencionForm"	property="tramosRetencion">
		
		<logic:iterate name="TramosRetencionForm" property="tramosRetencion" id="tramosRetencionForm" indexId="index">
				<c:choose>
					<c:when test="${index%2==0}">
						<tr  class="filaTablaPar">
					</c:when>
					<c:otherwise>
						<tr  class="filaTablaImpar">
					</c:otherwise>
				</c:choose>
							
					<td align='center' width='8%'><c:out value="${tramosRetencionForm.numeroTramo}"/></td>
					<td align='center' width='8%'><c:out value="${tramosRetencionForm.descripcion}"/></td>
					<td align='center' width='8%'><c:out value="${tramosRetencionForm.numerosSmi}"/></td>
					<td align='center' width='8%'><c:out value="${tramosRetencionForm.limiteTramo}"/></td>
					<td align='center' width='8%'><c:out value="${tramosRetencionForm.porcentaje}"/></td>
				
						
 				
				</tr>
		</logic:iterate>
	</logic:notEmpty>
</table>

	
</body>


</html>