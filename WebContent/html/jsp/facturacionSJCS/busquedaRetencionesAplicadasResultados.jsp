<!DOCTYPE html>
<html>
<head>
<!-- busquedaRetencionesAplicadasResultados.jsp -->

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
<%@ taglib uri="c.tld" prefix="c"%>
<%@ page import="com.atos.utils.*"%>
<%@page import="com.siga.Utilidades.*"%>



<!-- JSP -->
<% 
	
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	//Vector v = (Vector)request.getAttribute("resultado");
	
	
%>	





<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>	
	<script src="<html:rewrite page="/html/jsp/general/validacionSIGA.jsp"/>" type="text/javascript"></script>
	
</head>

<body>

		<html:form action="/FCS_BusquedaRentencionesAplicadas.do" method="post" target="submitArea" >
			<html:hidden property="modo"    value=""/>
			<html:hidden property="idInstitucion"  />
			<html:hidden property="idPersona"  />
			<html:hidden property="idCobro"  />
			<html:hidden property="idRetencion"/>
			<html:hidden property="fechaDesdePago"  />
			<html:hidden property="fechaHastaPago" />
			
			<input type="hidden" name="actionModal" value="">
			
			
			</html:form>
			
		
			
		
		<siga:Table 
		   name="tablaDatos"
		   border="1"
		   columnNames="factSJCS.busquedaRetAplicadas.literal.tipoRetencion,factSJCS.busquedaRetAplicadas.literal.letrado,factSJCS.busquedaRetAplicadas.literal.destinatarioRetencion
				,factSJCS.busquedaRetAplicadas.literal.fechaDesde,factSJCS.busquedaRetAplicadas.literal.fechaHasta,factSJCS.busquedaRetAplicadas.literal.fechaRetencion
				,factSJCS.busquedaRetAplicadas.literal.importeRetenido,factSJCS.busquedaRetAplicadas.literal.anyomes,factSJCS.busquedaRetAplicadas.literal.abonoRelacionado,factSJCS.busquedaRetAplicadas.literal.pagoRelacionado,"
		   columnSizes="8,16,14,8,8,8,8,6,10,10,4"
		   fixedHeight="95%">
		<logic:empty  name="BusquedaRetencionesAplicadasForm" property="retencionesAplicadas">
		<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	</logic:empty>
	
	<logic:notEmpty name="BusquedaRetencionesAplicadasForm" property="retencionesAplicadas">
		
		<logic:iterate name="BusquedaRetencionesAplicadasForm" property="retencionesAplicadas" id="retencionAplicada" indexId="index">
		
		<siga:FilaConIconos 
	  				fila='<%=String.valueOf(index.intValue()+1)%>'
					
	  				botones="" 
	  				pintarEspacio="no"
	  				visibleConsulta="no"
	  				visibleEdicion = "no"
	  				visibleBorrado = "no"
	  				clase="listaNonEdit">
		
				<%-- fila <bean:write name='index'/> escribe la informacion de la linea de comunicacion--%>
				<bean:define property="persona" name="retencionAplicada" id="persona"></bean:define>
				<bean:define property="retencionJudicial" name="retencionAplicada" id="retencionJudicial"></bean:define>
				<bean:define property="destinatarioRetencion" name="retencionJudicial" id="destinatarioRetencion"></bean:define>
				<bean:define id="fechaInicio" name="retencionJudicial" property="fechaInicio"/>
				<bean:define id="fechaFin" name="retencionJudicial" property="fechaFin"/>
				<bean:define id="fechaRetencion" name="retencionAplicada" property="fechaRetencion"/>
				<bean:define id="importeRetenido" name="retencionAplicada" property="importeRetenido"/>
				
				
				
				
				<td><bean:write name="retencionJudicial" property="descTipoRetencion" /></td>
				<td><bean:write name="persona" property="nombre" /></td>
				<td><bean:write name="destinatarioRetencion" property="nombre" /></td>
				<td><%=fechaInicio%></td>
				<td><%=fechaFin%></td>
				<td><%=UtilidadesString.mostrarDatoJSP(GstDate.getFormatedDateShort(usr.getLanguage(),fechaRetencion))%></td>
				
				<td align="right"><%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(importeRetenido.toString()))%></td>
				
				<td align="center">
					<c:choose >
						<c:when test="${retencionAplicada.anio==''}">
							&nbsp;
						</c:when>
						<c:otherwise>
							<bean:write name="retencionAplicada" property="anio"/>/<bean:write name="retencionAplicada" property="mes"/>
						</c:otherwise>
					</c:choose>
					
				</td>
				<td>
				<c:choose>
				<c:when test="${retencionAplicada.abonoRelacionado!=''}">
					<bean:write name="retencionAplicada" property="abonoRelacionado" /></td>
				</c:when>
				<c:otherwise>
					&nbsp;
				</c:otherwise>
				</c:choose>
				
				<td><bean:write name="retencionAplicada" property="pagoRelacionado" /></td>
				<td name='celda' align="left">
					<c:if test="${retencionAplicada.mes!=''}">
					<img id="iconoboton_consultar1" src="/SIGA/html/imagenes/bconsultar_off.gif" style="cursor:hand;" alt="Consultar" name="consultar_1" border="0" onClick=" selectRow(<bean:write name='index'/>+1); consultar(<bean:write name='retencionAplicada' property='idInstitucion'/>,<bean:write name='retencionAplicada' property='idPersona'/>,<bean:write name='retencionAplicada' property='idRetencion'/>,<bean:write name='retencionAplicada' property='idCobro'/>,'<bean:write name='retencionAplicada' property='fechaDesdePago'/>','<bean:write name='retencionAplicada' property='fechaHastaPago'/>');" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('consultar_1','','/SIGA/html/imagenes/bconsultar_on.gif',1)">
					</c:if>
						
	
				</td>

				
				</td>
				
			</siga:FilaConIconos>
			
			
			
		</logic:iterate>
		
	</logic:notEmpty>



	</siga:Table>
	
	

<!-- FIN: LISTA DE VALORES -->

	
	<!-- INICIO: BOTONES BUSQUEDA -->	
		
		
	<!-- FIN: BOTONES BUSQUEDA -->

	<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" style="display:none"></iframe>
 		
 		
	<script type="text/javascript">
	/*
	function selectRow(fila) {
		    
	   document.getElementById('filaSelD').value = fila;
	   var tabla;
	   tabla = document.getElementById('tablaDatos');
	   for (var i=0; i<tabla.rows.length; i++) {
	     if (i%2 == 0) tabla.rows[i].className = 'filaTablaPar';
	     else          tabla.rows[i].className = 'filaTablaImpar';
	   }
	   tabla.rows[fila].className = 'listaNonEditSelected';
	 }
	*/
	 function consultar(idInstitucion,idPersona,idRetencion,idCobro,fechaDesdePago,fechaHastaPago){
		 document.BusquedaRetencionesAplicadasForm.modo.value = "consultaLEC";
		 document.BusquedaRetencionesAplicadasForm.idInstitucion.value = idInstitucion;
		 document.BusquedaRetencionesAplicadasForm.idPersona.value = idPersona;
		 document.BusquedaRetencionesAplicadasForm.idRetencion.value = idRetencion;
		 document.BusquedaRetencionesAplicadasForm.idCobro.value = idCobro;
		 document.BusquedaRetencionesAplicadasForm.fechaDesdePago.value = fechaDesdePago;
		 document.BusquedaRetencionesAplicadasForm.fechaHastaPago.value = fechaHastaPago;
		 var resultado = ventaModalGeneral(document.BusquedaRetencionesAplicadasForm.name,"G");
		 	
	  }
		 
			
	</script>
	
	</body>

</html>
		
		
