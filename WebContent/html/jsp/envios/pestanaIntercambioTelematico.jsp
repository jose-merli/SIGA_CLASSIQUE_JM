<html>
<head>
<!-- pestanaIntercambioTelematico.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>		

		
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			//Asociada al boton Volver
			function download(fila)
			{
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = "";
				preparaDatos(fila, 'tabladatos', datos);
				document.forms[0].target="submitArea";
				document.forms[0].modo.value = "download";
				document.forms[0].submit();
				document.forms[0].target="mainWorkArea";
			}
			 function descargarLog(){
				 document.forms['IntercambioTelematicoForm'].modo.value='descargarLog';
				 document.forms['IntercambioTelematicoForm'].submit();
				 
				 	
			  }
			function accionVolver()
			{
				if(document.getElementById("origen")){
					var cadenaDatosEnvios = ""+document.getElementById("datosEnvios").value;
					var datosEnvios = cadenaDatosEnvios.split(",");
					document.forms['DefinirEJGForm'].idInstitucion.value = datosEnvios[0];
					document.forms['DefinirEJGForm'].anio.value  = datosEnvios[1]; 
					document.forms['DefinirEJGForm'].idTipoEJG.value = datosEnvios[2];
					document.forms['DefinirEJGForm'].numero.value = datosEnvios[3];
					document.forms['DefinirEJGForm'].origen.value = document.getElementById("origen").value;
					
					
					document.forms['DefinirEJGForm'].submit();
				}else{
					document.forms['DefinirEnviosForm'].submit();
				}
			}
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo
			titulo="envios.bandejaSalida.enviotelematico.titulo" 
			localizacion="envios.definirEnvios.localizacion"
		/>
	</head>

	<body>

<html:form action="/ENV_IntercambioTelematico" method="POST" target="submitArea">

<html:hidden property="modo" value=""/>
<html:hidden property="idInstitucion" value="${IntercambioTelematicoForm.idInstitucion}"/>
<html:hidden property="idEnvio" value="${IntercambioTelematicoForm.idEnvio}"/>
<html:hidden property="idIntercambio" value="${IntercambioTelematicoForm.idIntercambio}"/>
<table class="tablaTitulo" align="center" cellspacing="0">
	<tr>
		<td id="titulo" class="titulitosDatos"><siga:Idioma
				key="envios.definir.literal.nombre" />:&nbsp;<c:out
				value="${IntercambioTelematicoForm.envioNombre}" /> <siga:Idioma
				key="envios.definir.literal.tipoenvio" />:&nbsp;<c:out
				value="${IntercambioTelematicoForm.envioTipo}" /></td>
	</tr>
</table>


<table>
	<tr>
		<td width="15%"></td>
		<td width="15%"></td>
		<td width="17%"></td>
		<td width="3%"></td>
		<td width="15%"></td>
		<td width="20%"></td>
		<td width="15%"></td>
		
	</tr>
	<tr>
		<td></td>
		<td class="labelText">Tipo comunicación</td>
		<td class="labelTextValor" colspan="4" ><c:out
					value="${IntercambioTelematicoForm.tipoComunicacion}" />
		</td>
		<td></td>
	</tr>
<c:choose>
<c:when test="${IntercambioTelematicoForm.designaProvisional!=null }">
	<tr>
		<td></td> 
		<td colspan="5">
			<table>
				<tr>
					<td class="labelText">E.J.G.</td>
					<td class="labelTextValor" colspan="3"><c:out
								value="${IntercambioTelematicoForm.designaProvisional.ejgCodigo}" /></td>
					<td></td>
					
					
				</tr>
				<tr>
					
					<td class="labelText">Designación</td>
					<td class="labelTextValor" colspan="3" ><c:out
								value="${IntercambioTelematicoForm.designaProvisional.designaCodigo}" /></td>
					<td></td>
					
				</tr>
				<tr>
					
					<td class="labelText">Abogado</td>
					<td class="labelTextValor" colspan="3"><c:out
								value="${IntercambioTelematicoForm.designaProvisional.abogadoDesignado}" /></td>
					<td></td>
					
				</tr>
				
				<tr>
					
					<td class="labelText">Procurador</td>
					<td class="labelTextValor" colspan="3"><c:out
								value="${IntercambioTelematicoForm.designaProvisional.procuradorDesignado}" /></td>
					<td></td>
					
				</tr>
			</table>
		</td>
		<td></td> 
	</tr>
</c:when>
<c:when test="${IntercambioTelematicoForm.solSusProcedimiento!=null }">
	<tr>
		<td></td> 
		<td colspan="5">
			<table>
				<tr>
					<td class="labelText">E.J.G.</td>
					<td class="labelTextValor" colspan="3"><c:out
								value="${IntercambioTelematicoForm.solSusProcedimiento.ejgCodigo}" /></td>
					<td></td>
					
					
				</tr>
				
			</table>
		</td>
		<td></td> 
	</tr>
</c:when>
</c:choose>

	<tr>
		<td></td>
		<td class="labelText">Estado</td>
		<td class="labelTextValor" ><c:out
					value="${IntercambioTelematicoForm.estadoComunicacion}" /></td>
		<td>
		<c:choose>
		<c:when test="${IntercambioTelematicoForm.idEstado=='4'||IntercambioTelematicoForm.idEstado=='6'}">
			<img	id="iconoboton_descargaLog1" src="/SIGA/html/imagenes/bdescargaLog_off.gif" style="cursor:pointer;" alt="<siga:Idioma key='general.boton.descargaLog'/>" name="iconoFila" title="<siga:Idioma key='general.boton.descargaLog'/>" border="0" onClick=" descargarLog(); ">
		</c:when>
		<c:otherwise>
			&nbsp;
		</c:otherwise>
		</c:choose>
		</td>
		
		
		<td class="labelText">Fecha Petición</td>
		<td class="labelTextValor" ><c:out
					value="${IntercambioTelematicoForm.fechaPeticion}" /></td>
		<td></td>
		
	</tr>
	
		<c:if test="${IntercambioTelematicoForm.idEstado=='5'}">
			
		
		
	
		<tr>
			<td></td>
			<td class="labelText">Acuse</td>
			<td class="labelTextValor" >
				<c:out		value="${IntercambioTelematicoForm.idAcuse}" />
			</td><td>&nbsp;</td>
			<td class="labelText">Fecha Respuesta</td>
			<td class="labelTextValor" ><c:out
						value="${IntercambioTelematicoForm.fechaRespuesta}" /></td>
			<td></td>
			
		</tr>
	</c:if>
	
	
</table>	
	
		



</html:form>
<c:catch var ="catchException">
   <bean:parameter id="origen" name="origen" />
   <bean:parameter id="datosEnvios" name="datosEnvios" />	
</c:catch>

<c:if test = "${catchException == null}">
	<input type="hidden" id="origen" value ="${origen}"/>
	<input type="hidden" id="datosEnvios" value ="${datosEnvios}"/>
<c:choose>
	<c:when test="${origen=='/JGR_ComunicacionEJG'}">
		<html:form  action="/JGR_EJG"  method="POST" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property="modo" value="editar"/>
			<html:hidden styleId = "idTipoEJG" property="idTipoEJG" />
			<html:hidden styleId = "anio" property="anio"/>
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion"/>
			<html:hidden styleId = "origen" property="origen"/>
		</html:form>
	</c:when>
	<c:otherwise>
		<html:form action="/JGR_MantenimientoDesignas" method="post" target="mainWorkArea" style="display:none">
			<html:hidden styleId = "modo" property = "modo"   value="editar"/>
			<html:hidden styleId = "idInstitucion" property="idInstitucion" value=""/>
			<html:hidden styleId = "anio" property="anio" />
			<html:hidden styleId = "idTurno" property="idTurno" />
			<html:hidden styleId = "numero" property="numero"/>
			<html:hidden styleId = "origen" property="origen" />
		</html:form>	
	</c:otherwise>
</c:choose>
</c:if>	

<html:form  action="/ENV_DefinirEnvios.do?noReset=true&buscar=true"  method="POST" target="mainWorkArea">
		<html:hidden property="modo" value="abrir"/>
</html:form>

<siga:ConjBotonesAccion botones="V"  clase="botonesDetalle"  />

<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>