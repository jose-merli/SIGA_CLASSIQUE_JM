<!DOCTYPE html>
<html>
<head>
<!--editarCuentaBancaria.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type"
	content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>



<!-- HEAD -->


<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>

<script type="text/javascript">
	function inicio() {
		
		ajusteAltoBotonesPaginador('divListadoSeriesFacturacion');
		ajustarCabeceraTabla();
		if(document.CuentasBancariasForm.modo.value=='abrir'){
			habilitar(false);
		}
		
	}
	function habilitar(isHabilitar){
		
		if(isHabilitar==true){
			var inputs = document.getElementsByTagName("input");
			for(var i = 0 ; i <inputs.length ; i++) {
				input = inputs[i];
				if(input.type=="checkbox") {
					jQuery.removeAttr(input,"disabled");
				} else if(input.type!="button") {
					input.className = "box";
					jQuery.removeAttr(input,"readonly");
				}
			}
			var selects = document.getElementsByTagName("select");
			for(var i = 0 ; i <selects.length ; i++) {
				select = selects[i];
				jQuery.removeAttr(select,"disabled");
			}
			
			
		}else{
			var inputs = document.getElementsByTagName("input");
			for(var i = 0 ; i < inputs.length ; i++) {
				var input = inputs[i];
				if(input.type=="checkbox"){
					jQuery.attr(input,"disabled","disabled");
				} else if(input.type!="button"){
					input.className = "boxConsulta";
					jQuery.attr(input,"readonly","readonly");
				}
			}
			
			var selects = document.getElementsByTagName("select");
			for(var i = 0 ; i < selects.length ; i++) {
				var select = selects[i];
				jQuery.attr(select,"disabled","disabled");
			}
			
		}
	}
	
	
	</script>
</head>

<body onload="inicio();">

<table class="tablaTitulo" cellspacing="0" heigth="32">
	<tr>
		<td id="titulo" class="titulosPeq"><siga:Idioma key="menu.facturacion.gestionCuentasBancarias"/>
		</td>
	</tr>
</table>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
<html:javascript formName="CuentasBancariasForm" staticJavascript="false" /> 
<html:form action="${path}" target="submitArea">
	<html:hidden property="modo" value ="${CuentasBancariasForm.modo}"  />
	<html:hidden property="idInstitucion" value ="${CuentasBancariasForm.idInstitucion}"/>
	<html:hidden property="idCuentaBancaria" value ="${CuentasBancariasForm.idCuentaBancaria}"/>
	<html:hidden property="fechaBaja" value ="${CuentasBancariasForm.fechaBaja}"/>
    <input type="hidden" id="actionModal" name="actionModal" />
    <c:set var="clasePorEdicion" value="box" />
    <c:set var="disabledPorEdicion" value="false" />
	<c:if	test="${CuentasBancariasForm.modo=='modificar'}">
		<c:set var="clasePorEdicion" value="boxConsulta" />
		<c:set var="disabledPorEdicion" value="true" />
		<html:hidden property="codigoBanco" value ="${CuentasBancariasForm.codigoBanco}"/>
		<html:hidden property="sucursalBanco" value ="${CuentasBancariasForm.sucursalBanco}"/>
		<html:hidden property="cuentaBanco" value ="${CuentasBancariasForm.cuentaBanco}"/>
		<html:hidden property="digControlBanco" value ="${CuentasBancariasForm.digControlBanco}"/>
	</c:if>
	<table width="100%" border="0">
		<tr>
			<td width="35%">&nbsp; </td>
			<td  width="20%">&nbsp; </td>
			<td  width="20%">&nbsp; </td>
			<td  width="25%">&nbsp; </td>
	
		</tr>		
	
		<tr>
			<td class="labelText"><bean:message key="facturacion.cuentasBancarias.banco" />(*)</td>
			<td colspan="3">
					<table>
						<tr>
							<td>
							<!-- <html:text styleId="codigoBanco"
									property="codigoBanco" name ="CuentasBancariasForm" size="4" maxlength="4"
									styleClass="box" />-->
								<html:select  styleId="codigoBanco" property="codigoBanco"  styleClass="boxCombo" disabled="${disabledPorEdicion}" style="width:250px;">
									<html:option value=""><siga:Idioma key="general.combo.seleccionar" />	</html:option>
									<c:forEach items="${listaBancos}" var="banco">
										<html:option value="${banco.codigo}" ><c:out value="${banco.codigo}"/>&nbsp;<c:out value="${banco.nombre}"/></html:option>
									</c:forEach>
								</html:select>

							</td>
							<td class="labelText">-</td>
							<td><html:text styleId="sucursalBanco"
									property="sucursalBanco" name ="CuentasBancariasForm"  size="4" maxlength="4"
									styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}" /></td>
							<td class="labelText">-</td>

							<td><html:text styleId="digControlBanco"
									property="digControlBanco" name ="CuentasBancariasForm"  size="2" maxlength="2"
									styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}"/></td>
							<td class="labelText">-</td>
							<td><html:text styleId="cuentaBanco" property="cuentaBanco" name ="CuentasBancariasForm"
									size="10" maxlength="10" styleClass="${clasePorEdicion}" readonly="${disabledPorEdicion}"/></td>

						</tr>
					</table> </>


				</td>
			
			
			
			
			
		</tr>
		
		<tr>
			<td class="labelText"><bean:message key="facturacion.cuentasBancarias.nif" />(*)</td>
			<td><html:text styleId="nif" property="nif"	name ="CuentasBancariasForm"	size="9" maxlength="9" styleClass="box" /></td>
			<td class="labelText"><bean:message key="facturacion.cuentasBancarias.sufijo" /> </td>
			<td><html:text styleId="sufijo" property="sufijo" name ="CuentasBancariasForm"		size="3" maxlength="3" styleClass="box" /></td>
	
		</tr>
		<tr>
			<td class="labelText"><bean:message key="facturacion.cuentasBancarias.asientoContable" /> </td>
			<td><html:text styleId="asientoContable" property="asientoContable"	name ="CuentasBancariasForm"	size="20" maxlength="20" styleClass="box" /></td>
			<td class="labelText"><bean:message key="facturacion.cuentasBancarias.cuentaContableTarjeta" /> </td>
			<td><html:text styleId="cuentaContableTarjeta" property="cuentaContableTarjeta"	name ="CuentasBancariasForm"	size="20" maxlength="20" styleClass="box" /></td>
	
		</tr>
		<tr>
				<td class="labelText"><bean:message key="facturacion.cuentasBancarias.sjcs" /> </td>
				<td align="left"><html:select styleId="sjcs" property="sjcs" name ="CuentasBancariasForm" 
						styleClass="boxCombo">
						<html:option value="">
							
						</html:option>
						<html:option value="1">
							<siga:Idioma key="general.boton.yes" />
						</html:option>
						<html:option value="0">
							<siga:Idioma key="general.boton.no" />
						</html:option>
					</html:select></td>
					<c:choose>
   						<c:when test="${CuentasBancariasForm.modo!='insertar'}">
							<td class="labelText"><bean:message key="facturacion.cuentasBancarias.baja" /> </td>
							<td align="left"><html:select styleId="baja"
									property="baja" name ="CuentasBancariasForm"   styleClass="boxCombo">
									<html:option value="1">
										<siga:Idioma key="general.boton.yes" />
									</html:option>
									<html:option value="0">
										<siga:Idioma key="general.boton.no" />
									</html:option>
								</html:select></td>
   						</c:when>
   						<c:otherwise>
   							<td></td>
   							<td></td>
   						</c:otherwise>
   					</c:choose>
					
				

			</tr>
		
		<tr>
			<td class="labelText"><bean:message key="facturacion.cuentasBancarias.impComisionPropiaCargo" />(*)</td>
			<td><html:text styleId="impComisionPropiaCargo" property="impComisionPropiaCargo" name ="CuentasBancariasForm" 	size="13" maxlength="13" styleClass="box" /></td>
			<td class="labelText"><bean:message key="facturacion.cuentasBancarias.impComisionPropiaAbono" />(*)</td>
			<td><html:text styleId="impComisionPropiaAbono" property="impComisionPropiaAbono" name ="CuentasBancariasForm"	size="13" maxlength="13" styleClass="box" /></td>
	
		</tr>
		
		<tr>
			<td class="labelText"><bean:message key="facturacion.cuentasBancarias.impComisionAjenaCargo" />(*)</td>
			<td><html:text styleId="impComisionAjenaCargo" property="impComisionAjenaCargo" name ="CuentasBancariasForm" size="13" maxlength="13" styleClass="box" /></td>
			<td class="labelText"><bean:message key="facturacion.cuentasBancarias.impComisionAjenaAbono" />(*)</td>
			<td><html:text styleId="impComisionAjenaAbono" property="impComisionAjenaAbono"	name ="CuentasBancariasForm" size="13" maxlength="13" styleClass="box" /></td>
	
		</tr>
		
		
	</table>
		<c:if test="${CuentasBancariasForm.modo!='insertar'}">

						<div>
							<table id='tabCuentasBancarias' border='1' width='100%'
								cellspacing='0' cellpadding='0' style= "table-layout: fixed;" >
								<tr class='tableTitle'>
									<td align='center' width='50%'>Serie</td>
									<td align='center' width='50%'>Descripción</td>
								</tr>
							</table>
						</div>
						<div id="divListadoSeriesFacturacion"
							style='position: absolute; width: 100%; overflow-y: auto;'>
						<table class="tablaCampos" id='seriesFacturacion' border='1'
							align='center' width='100%' cellspacing='0' cellpadding='0' style= "table-layout: fixed;">

							<c:choose>
								<c:when test="${empty seriesFacturacion}">
									<tr class="notFound">
						 				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
									</tr>
								</c:when>
								<c:otherwise>
									<tr>
										<td width='50%'></td>
										<td width='50%'></td>
									</tr>
									<c:forEach items="${seriesFacturacion}" var="serieFacturacion"
										varStatus="status">
										<c:choose>
											<c:when test="${status.count%2==0}">
												<tr class="filaTablaPar">
											</c:when>
											<c:otherwise>
												<tr class="filaTablaImpar">
											</c:otherwise>
										</c:choose>
										<td align='left'><c:out
												value="${serieFacturacion.nombreabreviado}" /></td>
										<td align='left' style="white-space: pre-line;"><c:out
												value="${serieFacturacion.descripcion}" /></td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</table>
			</div>
		</c:if>



	</html:form>
<c:choose>
	<c:when test="${CuentasBancariasForm.modo!='abrir'}">
		<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />
	</c:when>
	<c:otherwise>
		<siga:ConjBotonesAccion botones="C" modal="P" />
	</c:otherwise>
</c:choose>

	

	
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>

<script type="text/javascript">

	function accionGuardarCerrar() 
	{
		sub();
		if(validateCuentasBancariasForm(document.CuentasBancariasForm)){
			document.CuentasBancariasForm.submit();
		}else{
			fin();
			return false; 
		}
	}

	function accionCerrar() 
	{		
		top.cierraConParametros("NORMAL");
	}

	function accionRestablecer() 
	{		
		document.CuentasBancariasForm.reset();
	}
	function ajustarCabeceraTabla(){
		if(document.getElementById("seriesFacturacion")){
			if (document.getElementById("seriesFacturacion").clientHeight < document.getElementById("divListadoSeriesFacturacion").clientHeight) {
				document.getElementById("tabCuentasBancarias").width='100%';
				   
			  } else {
				  document.getElementById("tabCuentasBancarias").width='98.43%';
				   
			  }
		}
		
	}
	
	
</script>

</body>

</html>