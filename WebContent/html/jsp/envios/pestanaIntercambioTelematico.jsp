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


<html>
	<head>
		<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
		<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>		
		<script src="<html:rewrite page='/html/js/jquery.js'/>" type="text/javascript"></script>		
		<script src="<html:rewrite page='/html/js/jquery.custom.js'/>" type="text/javascript"></script>		

		
		<!-- INICIO: SCRIPTS BOTONES -->
		<script language="JavaScript">
			//Asociada al boton Volver
			function download(fila)
			{
				var datos;
				datos = document.getElementById('tablaDatosDinamicosD');
				datos.value = ""; 
				var i, j;
				for (i = 0; i < 2; i++) 
				{
  						var tabla;
  						tabla = document.getElementById('tabladatos');
  						if (i == 0)
  						{
    						var flag = true;
    						j = 1;
    						while (flag) 
    						{
      							var aux = 'oculto' + fila + '_' + j;
      							var oculto = document.getElementById(aux);
      							if (oculto == null)  
      							{ 
      								flag = false; 
      							}
     							else 
     							{ 
     								datos.value = datos.value + oculto.value + ','; 
     							}
      							j++;
    						}
    						datos.value = datos.value + "%"
  						} 
  						else { 
  							j = 2; 
  						}
  						if ((tabla.rows[fila].cells)[i].innerHTML == "") {
    						datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
  						} else {
    						datos.value = datos.value + (tabla.rows[fila].cells)[i].innerHTML.replace(/<[^>]+>/gi, '').replace(/\\n|\\t|^\\s*|\\s*$/gi,'') + ',';
  						}
					document.forms[0].target="submitArea";
					document.forms[0].modo.value = "download";
					document.forms[0].submit();
					document.forms[0].target="mainWorkArea";
				}
			}
			 function descargarLog(){
				 document.forms['IntercambioTelematicoForm'].modo.value='descargarLog';
				 document.forms['IntercambioTelematicoForm'].submit();
				 
				 	
			  }
			function accionVolver()
			{
				document.forms['DefinirEnviosForm'].submit();
			}
			
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
		<siga:Titulo
			titulo="envios.definirTiposPlantillas.imagenes.cabecera" 
			localizacion="envios.definirTiposPlantillas.localizacion"
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
	
</table>	
	
		



</html:form>
<html:form  action="/ENV_DefinirEnvios.do?noReset=true&buscar=true"  method="POST" target="mainWorkArea">
		<html:hidden property="modo" value="abrir"/>
</html:form>

<siga:ConjBotonesAccion botones="V" clase="botonesDetalle"  />

<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display: none"></iframe>
</body>
</html>