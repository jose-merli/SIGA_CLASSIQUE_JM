<!-- listadoCuentasBancarias.jsp -->

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
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>" />

	<script type="text/javascript">
	function refrescarLocal(){
		alert("refrescarLocal");		
	} 
	
	function borrar(fila){
		if (confirm('<siga:Idioma key="messages.deleteConfirmation"/>')){
			return borrarFila(fila);
		}			
	}		
	
	</script>

</head> 

<body  >

<div>		
	<table id='tabCuentasBancarias' border='1' width='100%' cellspacing='0' cellpadding='0'>
		<tr class = 'tableTitle'>
			<td align='center' width='22%'><bean:message key="facturacion.cuentasBancarias.banco" /></td>
			<td align='center' width='17%'><bean:message key="facturacion.cuentasBancarias.cuentaBanco" /></td>
			<td align='center' width='7%'><bean:message key="facturacion.cuentasBancarias.impComisionPropiaCargo" /> </td>
			<td align='center' width='7%'><bean:message key="facturacion.cuentasBancarias.impComisionAjenaCargo" /></td>
			<td align='center' width='7%'><bean:message key="facturacion.cuentasBancarias.impComisionPropiaAbono" /></td>
			<td align='center' width='7%'><bean:message key="facturacion.cuentasBancarias.impComisionAjenaAbono" /></td>
			<td align='center' width='7%'><bean:message key="facturacion.cuentasBancarias.sjcs" /></td>
			<td align='center'  width='7%'><bean:message key="facturacion.cuentasBancarias.uso" /></td>
			<td align='center'  width='8%'><bean:message key="facturacion.cuentasBancarias.baja" /></td>
			<td align='center'  width='10%'>&nbsp;</td>
		</tr>
	</table>
</div>


<table class="tablaCampos" id='cuentasBancarias' border='1' align='center' width='100%' cellspacing='0' cellpadding='0' >
		<c:choose>
   			<c:when test="${empty cuentasBancarias}">
				<br>
	   		 		<p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
	 			<br>
   			</c:when>
   			<c:otherwise>
   			
   			
   			<tr>
     			<td width='22%'></td>
				<td width='17%'></td>
				<td width='7%'></td>
				<td width='7%'></td>
				<td width='7%'></td>
				<td width='7%'></td>
				<td width='7%'></td>
				<td width='7%'></td>
				<td width='8%'></td>
				<td width='10%'></td>
			</tr>
   
			<c:forEach items="${cuentasBancarias}" var="cuentaBancaria" varStatus="status">
				
				<siga:FilaConIconos	fila='${status.count}'				    
	  				pintarEspacio="no"
	  				botones="E,C,B"
	  				clase="listaNonEdit">
				
				<input type="hidden" name="idCuentaBancaria_${status.count}" id="idCuentaBancaria_${status.count}"  value="${cuentaBancaria.idCuentaBancaria}">
				 
				<td align='left'><c:out value="${cuentaBancaria.bancoNombre}"/></td>
				<td align='left'><c:out value="${cuentaBancaria.bancoCuentaDescripcion}"/></td>
				<td align="right"><c:out value="${cuentaBancaria.impComisionPropiaCargo}"/></td>
				<td align="right"><c:out value="${cuentaBancaria.impComisionAjenaCargo}"/></td>
				<td align="right"><c:out value="${cuentaBancaria.impComisionPropiaAbono}"/></td>
				<td align="right"><c:out value="${cuentaBancaria.impComisionAjenaAbono}"/></td>
				<td align='left'>
				<c:choose>
					<c:when test="${cuentaBancaria.sjcs=='0'}">
						<siga:Idioma key="general.boton.no" /> 
					</c:when>
					<c:when test="${cuentaBancaria.sjcs=='1'}"> 
						<siga:Idioma key="general.boton.yes" />
					</c:when>
					<c:otherwise>
						&nbsp;
					</c:otherwise>
				</c:choose>
				
				
				</td>
				<td align='left'><c:out value="${cuentaBancaria.uso}"/></td>
				<td align='left'><c:choose>
					<c:when test="${cuentaBancaria.fechaBaja!=null && cuentaBancaria.fechaBaja!=''}">
						<c:out value="${cuentaBancaria.fechaBaja}"/>
					</c:when>
					<c:otherwise>
					&nbsp;
					</c:otherwise>
				</c:choose></td>

				
			</siga:FilaConIconos>
		</c:forEach>
	</c:otherwise>
	</c:choose>
	
</table>


	

<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="${path}"  name="CuentasBancarias"
		type="com.siga.facturacion.form.CuentasBancariasForm">
		<html:hidden styleId="modo" property="modo" />
		<html:hidden styleId="idInstitucion" property="idInstitucion" />
		<html:hidden styleId="idCuentaBancaria" property="idCuentaBancaria" />
		<input type="hidden" id="actionModal" name="actionModal" />
	</html:form>


</body>


</html>