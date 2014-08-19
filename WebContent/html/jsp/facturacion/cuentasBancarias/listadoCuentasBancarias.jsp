<!DOCTYPE html>
<html>
<head>
<!-- listadoCuentasBancarias.jsp -->

	<!-- CABECERA JSP -->
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<%@ page pageEncoding="ISO-8859-15"%>
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">
	<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
	
	<!-- TAGLIBS -->
	<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
	<%@ taglib uri="struts-bean.tld" prefix="bean"%>
	<%@ taglib uri="struts-html.tld" prefix="html"%>
	<%@ taglib uri="struts-logic.tld" prefix="logic"%>
	<%@ taglib uri="c.tld" prefix="c"%>

	<!-- HEAD -->
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>	
	
	<!-- Incluido jquery en siga.js -->	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	
</head>

<body>
	
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="${path}"  name="CuentasBancarias" type="com.siga.facturacion.form.CuentasBancariasForm">
		<html:hidden styleId="modo" property="modo" />
		<html:hidden styleId="idInstitucion" property="idInstitucion" />
		<html:hidden styleId="idCuentaBancaria" property="idCuentaBancaria" />
		<input type="hidden" id="actionModal" name="actionModal" />
	</html:form>

	<siga:Table 
   	      name="tabCuentasBancarias"
   		  border="1"
   		  columnNames='facturacion.cuentasBancarias.banco,
   		  facturacion.cuentasBancarias.IBAN,
   		  facturacion.cuentasBancarias.comision,
   		  facturacion.cuentasBancarias.tieneSJCS,
   		  facturacion.cuentasBancarias.uso,
   		  facturacion.cuentasBancarias.tieneBaja,'
   		  columnSizes="44,20,8,6,4,8,10">
   		  
	    <!-- INICIO: ZONA DE REGISTROS -->
	    <c:choose>
			<c:when test="${empty cuentasBancarias}">
				<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
			</c:when>
			
			<c:otherwise>   		  
   
				<c:forEach items="${cuentasBancarias}" var="cuentaBancaria" varStatus="status">
					<siga:FilaConIconos	fila='${status.count}'	
		  				pintarEspacio="no"
		  				visibleConsulta="no"
		  				visibleEdicion = "no"
		  				visibleBorrado = "no"		  				
		  				clase="listaNonEdit">
					
						<td align='left'>
							<c:out value="${cuentaBancaria.bancoNombre}"/>
							<input type="hidden" name="idCuentaBancaria_${status.count}" id="idCuentaBancaria_${status.count}"  value="${cuentaBancaria.idCuentaBancaria}">
							<input type="hidden" name ="idInstitucion_${status.count}"   id ="idInstitucion_${status.count}"    value ="${cuentaBancaria.idInstitucion}"/>
						</td>
						<td align='left'><c:out value="${cuentaBancaria.IBAN}"/></td>
						<td align="right"><c:out value="${cuentaBancaria.comisionimporte}"/>&nbsp;&euro;</td>
						<td align='center'>
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
						<td align='right'><c:out value="${cuentaBancaria.uso}"/></td>
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
	<!-- FIN: ZONA DE REGISTROS -->
	</siga:Table>

	<iframe name="submitArea"	src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>	
	
	<script type="text/javascript">
		function refrescarLocal(){		
			parent.buscar();			
		}
		
		function borrarFila(fila) {		
			var idCuentaBancariaFila = document.getElementById("idCuentaBancaria_"+fila).value;
			var idInstitucion = document.getElementById("idInstitucion_"+fila).value;
			document.forms['CuentasBancarias'].idInstitucion.value=idInstitucion;	
			document.forms['CuentasBancarias'].idCuentaBancaria.value = idCuentaBancariaFila;
			document.forms['CuentasBancarias'].modo.value="borrar";
			document.forms['CuentasBancarias'].target="submitArea";
			document.forms['CuentasBancarias'].submit();
		 }
		
		function borrar(fila){
			if (confirm('<siga:Idioma key="messages.deleteConfirmation"/>')){
				return borrarFila(fila);
			}			
		}
		
		function consultar(fila){
			var idCuentaBancariaFila = document.getElementById("idCuentaBancaria_"+fila).value;
			var idInstitucion = document.getElementById("idInstitucion_"+fila).value;
			document.forms['CuentasBancarias'].idInstitucion.value=idInstitucion;	
			document.forms['CuentasBancarias'].idCuentaBancaria.value = idCuentaBancariaFila;
			document.forms['CuentasBancarias'].modo.value="consultar";
		  	ventaModalGeneral(document.forms['CuentasBancarias'].name,"G");	  		
		}	
		
		function editar(fila) {
			var idCuentaBancariaFila = document.getElementById("idCuentaBancaria_"+fila).value;
			var idInstitucion = document.getElementById("idInstitucion_"+fila).value;
			document.forms['CuentasBancarias'].idInstitucion.value=idInstitucion;			
			document.forms['CuentasBancarias'].idCuentaBancaria.value = idCuentaBancariaFila;
			document.forms['CuentasBancarias'].modo.value="editar";
		  	var resultado = ventaModalGeneral(document.forms['CuentasBancarias'].name,"G");
		  	if(resultado && resultado=='MODIFICADO'){
		  		refrescarLocal();
		  	}			
		}
	
	</script>

</body>

</html>