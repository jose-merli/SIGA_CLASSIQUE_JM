<!DOCTYPE html>
<html>
<head>
	<!-- busquedaCuentasBancarias.jsp -->
	
	<!-- CABECERA JSP -->
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<%@ page pageEncoding="ISO-8859-1"%>
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

	<!-- TAGLIBS -->
	<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
	<%@ taglib uri="struts-bean.tld" prefix="bean"%>
	<%@ taglib uri="struts-html.tld" prefix="html"%>
	<%@ taglib uri="struts-logic.tld" prefix="logic"%>
	<%@ taglib uri="c.tld" prefix="c"%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery-ui.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script	type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>

	<script type="text/javascript">
		jQuery.noConflict();
		
		function buscar(){
			sub();				
			document.CuentasBancariasForm.modo.value="buscar";
			document.CuentasBancariasForm.target="resultado";	
			document.CuentasBancariasForm.submit();	
			fin();
		}		
	
		function accionNuevo() {
			document.CuentasBancariasForm.modo.value = "nuevo";
			var resultado=ventaModalGeneral(document.CuentasBancariasForm.name,"G");
			if(resultado=='MODIFICADO'){
				buscar();
			}	
		}
		
  	</script>

	<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo titulo="menu.facturacion.gestionCuentasBancarias" localizacion="menu.facturacion.localizacion" />
</head>

<body onload="ajusteAltoBotones('resultado');">
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />
	<html:form action="${path}" method="POST" target="mainWorkArea">
		<html:hidden property="modo" />
		<html:hidden property="idInstitucion" />
		<html:hidden property="idCuentaBancaria" />
		<input type="hidden" name="actionModal" />

		<table width="100%" border="0">
			<tr>
				<td class="labelText">
					<bean:message key="facturacion.cuentasBancarias.banco" />
				</td>
				<td>
					<html:select  styleId="codigoBanco" property="codigoBanco" styleClass="boxCombo" style="width:400px;">
						<html:option value=""/>
						<c:forEach items="${listaBancos}" var="banco">
							<html:option value="${banco.codigo}"><c:out value="${banco.nombre}"/></html:option>
						</c:forEach>
					</html:select>
				</td>
				
				<td class="labelText">-</td>
				<td>
					<html:text styleId="IBAN" property="IBAN" size="36" maxlength="34" styleClass="box" />
				</td>
				
				<td class="labelText">
					¿SJCS?<!--<bean:message key="facturacion.cuentasBancarias.sjcs" />-->
				</td>
				<td align="left">
					<html:select styleId="sjcs" property="sjcs" styleClass="boxCombo">
						<html:option value="" />
						<html:option value="1">
							<siga:Idioma key="general.boton.yes" />
						</html:option>
						<html:option value="0">
							<siga:Idioma key="general.boton.no" />
						</html:option>
					</html:select>
				</td>
				
				<td class="labelText">
					<bean:message key="facturacion.cuentasBancarias.baja" />
				</td>
				<td align="left">
					<html:select styleId="baja" property="baja" styleClass="boxCombo">
						<html:option value="">
							
						</html:option>
						<html:option value="1">
							<siga:Idioma key="general.boton.yes" />
						</html:option>
						<html:option value="0">
							<siga:Idioma key="general.boton.no" />
						</html:option>
					</html:select>
				</td>
			</tr>
		</table>

		<siga:ConjBotonesBusqueda botones="B"/>
	</html:form>
	
	<siga:ConjBotonesAccion botones="N" clase="botonesDetalle"/>
	
	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0"					 
					class="frameGeneral">
	</iframe>
	<!-- FIN: IFRAME LISTA RESULTADOS -->
		
	<!-- FIN: BOTONES BUSQUEDA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>