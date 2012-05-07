<!-- listadoUnidadFamiliarEJG.jsp-->

<%@page import="com.atos.utils.ClsConstants"%>
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-15">

<!-- TAGLIBS -->
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@taglib uri	=	"struts-bean.tld" 			prefix="bean" 		%>
<%@taglib uri 	= 	"struts-html.tld" 			prefix="html" 		%>
<%@taglib uri	= 	"libreria_SIGA.tld" 		prefix="siga"		%>
<%@taglib uri	=	"struts-logic.tld" 			prefix="logic" 		%>
<%@ taglib uri="c.tld" prefix="c"%>
<!-- JSP -->
<html>

<!-- HEAD -->
<head>
  
	<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
	<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
	<title><siga:Idioma key="gratuita.operarUnidadFamiliar.literal.unidadFamiliar"/></title>
	<siga:Titulo 
		titulo="gratuita.busquedaEJG.unidadFamiliar" 
		localizacion="gratuita.busquedaEJG.localizacion"/>


</head>

<body class="tablaCentralCampos" onload="ajustarCabeceraTabla();">	
			

<bean:define id="modo" name="DefinirUnidadFamiliarEJGForm" property="modo" type="java.lang.String"/>
<bean:define id="conceptoEJG" scope="request" name="EJG_UNIDADFAMILIAR" />
<bean:define id="usrBean" name="USRBEAN" scope="session"
	type="com.atos.utils.UsrBean"/>
<html:form action="/JGR_UnidadFamiliarPerJG" method="post" target="submitArea" >
		<html:hidden property="modo" value="abrirPestana"/>

		<html:hidden property="idInstitucionJG" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}"/>
		<html:hidden property="idPersonaJG" value=""/>

		<html:hidden property="idInstitucionEJG" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}"/>
		<html:hidden property="idTipoEJG" value="${DefinirUnidadFamiliarEJGForm.idTipoEJG}"/>
		<html:hidden property="anioEJG" value="${DefinirUnidadFamiliarEJGForm.anio}"/>
		<html:hidden property="numeroEJG" value="${DefinirUnidadFamiliarEJGForm.numero}"/>

		<html:hidden property="conceptoE" value="${conceptoEJG}"/>
		<html:hidden property="tituloE" value="gratuita.personaJG.literal.unidadFamiliar"/>
		<html:hidden property="localizacionE" value=""/>
		<html:hidden property="accionE" value="nuevo"/>
		<html:hidden property="actionE" value="/JGR_UnidadFamiliarPerJG.do"/>
		<html:hidden property="pantallaE" value="M"/>
			<!-- RGG: cambio a formularios ligeros -->
		<input type="hidden" name="tablaDatosDinamicosD"/>
		<input type="hidden" name="actionModal" value=""/>
</html:form>
		
<html:form action="/JGR_UnidadFamiliarEJG"  method="post" target="submitArea">
		<html:hidden property="modo"/>
		<html:hidden property="idTipoEJG" value="${DefinirUnidadFamiliarEJGForm.idTipoEJG}"/>
		<html:hidden property="anio" value="${DefinirUnidadFamiliarEJGForm.anio}"/>
		<html:hidden property="numero" value="${DefinirUnidadFamiliarEJGForm.numero}"/>
		<html:hidden property="idInstitucion" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}"/>
		<input type="hidden" name="tablaDatosDinamicosD"/>
		
</html:form>
<html:form action="/JGR_UnidadFamiliarEEJG" name="EEJG" method="post" target="submitArea" type ="com.siga.gratuita.form.DefinirUnidadFamiliarEJGForm">
		<html:hidden property="modo"/>
		<html:hidden property="idTipoEJG" value="${DefinirUnidadFamiliarEJGForm.idTipoEJG}"/>
		<html:hidden property="anio" value="${DefinirUnidadFamiliarEJGForm.anio}"/>
		<html:hidden property="numero" value="${DefinirUnidadFamiliarEJGForm.numero}"/>
		<html:hidden property="idInstitucion" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}"/>
		<input type="hidden" name="tablaDatosDinamicosD"/>
</html:form>

<table class="tablaTitulo" cellspacing="0" heigth="38">
<tr>
	<td id="titulo" class="titulitosDatos">
		<c:out value="${DefinirUnidadFamiliarEJGForm.ejg.anio}"></c:out>/<c:out value="${DefinirUnidadFamiliarEJGForm.ejg.numEJG}"></c:out>-<c:out value="${DefinirUnidadFamiliarEJGForm.personaJG.nombre}"></c:out>&nbsp;<c:out value="${DefinirUnidadFamiliarEJGForm.personaJG.apellido1}"></c:out>&nbsp;<c:out value="${DefinirUnidadFamiliarEJGForm.personaJG.apellido2}"></c:out>
	</td>
</tr>
</table>
<c:if test="${DefinirUnidadFamiliarEJGForm.permisoEejg==true}">

	<table border="0" style="table-layout:fixed;width=100%" align="center">
	<tr>
 		<td style="vertical-align: top; height:300px">
</c:if>
			<siga:TablaCabecerasFijas 		   
		   nombre="listadoUnidadFamiliar"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="gratuita.personaJG.literal.parentescoNormalizado,gratuita.busquedaEJG.literal.nif,gratuita.busquedaEJG.literal.nombre,gratuita.operarInteresado.literal.ingresosAnuales,gratuita.operarInteresado.literal.bienesMobiliarios,gratuita.operarInteresado.literal.bienesInmuebles,gratuita.operarInteresado.literal.otrosBienes,"
		   tamanoCol="8,8,25,8,8,8,8,28"
		   
		   alto="500"
		   modal="G"
		   mensajeBorrado="gratuita.ejg.unidadFamiliar.borrado"
		  >
		  
	<logic:empty name="DefinirUnidadFamiliarEJGForm" property="unidadFamiliar">
	<br>
   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
 		<br>
	</logic:empty>
	<logic:notEmpty name="DefinirUnidadFamiliarEJGForm"	property="unidadFamiliar">
		<logic:iterate name="DefinirUnidadFamiliarEJGForm"	property="unidadFamiliar" id="solicitante" indexId="index" type="com.siga.gratuita.form.DefinirUnidadFamiliarEJGForm">
			<%index = index.intValue()+1; %>
			<input type="hidden" id="idPersonaJG_<%=index%>" value="<bean:write name="solicitante" property="idPersona" />">			
			<c:if	test="${solicitante.idPersona!=DefinirUnidadFamiliarEJGForm.personaJG.idPersona}">
			<bean:define id="elementosFila" name="solicitante" property="elementosFila" type="com.siga.tlds.FilaExtElement[]"/>
			<bean:define id="botones" name="solicitante" property="botones" type="java.lang.String"/>	
			
			<siga:FilaConIconos fila="<%=String.valueOf(index.intValue())%>" botones="<%=botones%>" elementos="<%=elementosFila%>" clase="listaNonEdit" modo="<%=modo%>" >
					
					
			
					<td>
						<input type="hidden" name="oculto<%=index%>_1" value="EJGUnidadFamiliar">
						<input type="hidden" name="oculto<%=index%>_2" value="gratuita.personaJG.literal.unidadFamiliar">
						<input type="hidden" name="oculto<%=index%>_3" value="gratuita.personaJG.literal.unidadFamiliar">
						<input type="hidden" name="oculto<%=index%>_4" value="editar">
						<input type="hidden" name="oculto<%=index%>_5" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}">
						<input type="hidden" name="oculto<%=index%>_6" value="${solicitante.personaJG.idPersona}">
						<input type="hidden" name="oculto<%=index%>_7" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}">
						<input type="hidden" name="oculto<%=index%>_8" value="${DefinirUnidadFamiliarEJGForm.idTipoEJG}">
						<input type="hidden" name="oculto<%=index%>_9" value="${DefinirUnidadFamiliarEJGForm.anio}">
						<input type="hidden" name="oculto<%=index%>_10" value="${DefinirUnidadFamiliarEJGForm.numero}">
						<input type="hidden" name="oculto<%=index%>_11" value="${solicitante.peticionEejg.idPeticion}">
						<input type="hidden" name="oculto<%=index%>_12" value="${DefinirUnidadFamiliarEJGForm.esComision}">
						<input type="hidden" name="oculto<%=index%>_13" value="${solicitante.solicitante}">
						<input type="hidden" name="oculto<%=index%>_14" value="${solicitante.personaJG.nif}">
						<input type="hidden" name="oculto<%=index%>_15" value="${solicitante.personaJG.tipoIdentificacion}">
						
						
						<c:out value="${solicitante.parentesco.descripcion}"></c:out>
					
					</td>
					<td>
						<c:choose>
							<c:when test="${solicitante.personaJG.nif!=''}">
								<c:out value="${solicitante.personaJG.nif}"></c:out>
							</c:when>
							<c:otherwise>
							  &nbsp;
							</c:otherwise>
						</c:choose>
					</td>
					<td><c:out value="${solicitante.personaJG.nombre}"></c:out>&nbsp;
					<c:out value="${solicitante.personaJG.apellido1}"></c:out>&nbsp;
					<c:out value="${solicitante.personaJG.apellido2}"></c:out>
					</td>

					<td align="right">
						<c:if	test="${solicitante.importeIngresosAnuales!=''}">
						<c:out value="${solicitante.importeIngresosAnuales}"></c:out>&euro;
						</c:if>
						<c:if test="${solicitante.importeIngresosAnuales==''}">
							&nbsp;
						</c:if>
					</td>
					<td align="right">
						<c:if test="${solicitante.importeBienesMuebles!=''}">
						<c:out value="${solicitante.importeBienesMuebles}"></c:out>&euro;
						</c:if>
						<c:if	test="${solicitante.importeBienesMuebles==''}">
							&nbsp;
						</c:if>
					</td>
					<td align="right">
						<c:if test="${solicitante.importeBienesInmuebles!=''}">
						<c:out value="${solicitante.importeBienesInmuebles}"></c:out>&euro;
						</c:if>
						<c:if	test="${solicitante.importeBienesInmuebles==''}">
							&nbsp;
						</c:if>
					</td>
					<td align="right">
						<c:if test="${solicitante.importeOtrosBienes!=''}">
						<c:out value="${solicitante.importeOtrosBienes}"></c:out>&euro;
						</c:if>
						<c:if	test="${solicitante.importeOtrosBienes==''}">
							&nbsp;
						
						</c:if>
					</td>

					</siga:FilaConIconos>
				</c:if>
				<c:if	test="${solicitante.idPersona==DefinirUnidadFamiliarEJGForm.personaJG.idPersona}">
				<bean:define id="elementosFila" name="solicitante" property="elementosFila" type="com.siga.tlds.FilaExtElement[]"/>
				<siga:FilaConIconos fila="<%=String.valueOf(index.intValue())%>" botones="" elementos="<%=elementosFila%>" clase="listaNonEdit" modo="<%=modo%>"  visibleBorrado="false" visibleEdicion="false"	visibleConsulta="false">
					
					<td>
						<input type="hidden" name="oculto<%=index%>_1" value="EJGUnidadFamiliar">
						<input type="hidden" name="oculto<%=index%>_2" value="gratuita.personaJG.literal.unidadFamiliar">
						<input type="hidden" name="oculto<%=index%>_3" value="gratuita.personaJG.literal.unidadFamiliar">
						<input type="hidden" name="oculto<%=index%>_4" value="editar">
						<input type="hidden" name="oculto<%=index%>_5" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}">
						<input type="hidden" name="oculto<%=index%>_6" value="${solicitante.personaJG.idPersona}">
						<input type="hidden" name="oculto<%=index%>_7" value="${DefinirUnidadFamiliarEJGForm.idInstitucion}">
						<input type="hidden" name="oculto<%=index%>_8" value="${DefinirUnidadFamiliarEJGForm.idTipoEJG}">
						<input type="hidden" name="oculto<%=index%>_9" value="${DefinirUnidadFamiliarEJGForm.anio}">
						<input type="hidden" name="oculto<%=index%>_10" value="${DefinirUnidadFamiliarEJGForm.numero}">
						<input type="hidden" name="oculto<%=index%>_12" value="${DefinirUnidadFamiliarEJGForm.esComision}">
						<input type="hidden" name="oculto<%=index%>_13" value="${solicitante.solicitante}">
						<input type="hidden" name="oculto<%=index%>_14" value="${solicitante.personaJG.nif}">
						<input type="hidden" name="oculto<%=index%>_15" value="${solicitante.personaJG.tipoIdentificacion}">
						<c:choose>
							<c:when test="${solicitante.peticionEejg.idPeticion!=null}">
							<input type="hidden" name="oculto<%=index%>_11" value="${solicitante.peticionEejg.idPeticion}">
							</c:when>
							<c:otherwise>
							  <input type="hidden" name="oculto<%=index%>_11" value=" ">
							</c:otherwise>
						</c:choose>
						
						
						
						<c:out value="${solicitante.parentesco.descripcion}"></c:out>
					
					</td>
					<td><c:out value="${solicitante.personaJG.nif}"></c:out>&nbsp;</td>
					<td><c:out value="${solicitante.personaJG.nombre}"></c:out>&nbsp;
					<c:out value="${solicitante.personaJG.apellido1}"></c:out>&nbsp;
					<c:out value="${solicitante.personaJG.apellido2}"></c:out>
					</td>

					<td align="right">
						<c:if	test="${solicitante.importeIngresosAnuales!=''}">
						<c:out value="${solicitante.importeIngresosAnuales}"></c:out>&euro;
						</c:if>
						<c:if test="${solicitante.importeIngresosAnuales==''}">
							&nbsp;
						</c:if>
					</td>
					<td align="right">
						<c:if test="${solicitante.importeBienesMuebles!=''}">
						<c:out value="${solicitante.importeBienesMuebles}"></c:out>&euro;
						</c:if>
						<c:if	test="${solicitante.importeBienesMuebles==''}">
							&nbsp;
						</c:if>
					</td>
					<td align="right">
						<c:if test="${solicitante.importeBienesInmuebles!=''}">
						<c:out value="${solicitante.importeBienesInmuebles}"></c:out>&euro;
						</c:if>
						<c:if	test="${solicitante.importeBienesInmuebles==''}">
							&nbsp;
						</c:if>
					</td>
					<td align="right">
						<c:if test="${solicitante.importeOtrosBienes!=''}">
						<c:out value="${solicitante.importeOtrosBienes}"></c:out>&euro;
						</c:if>
						<c:if	test="${solicitante.importeOtrosBienes==''}">
							&nbsp;
						
						</c:if>
					</td>

					</siga:FilaConIconos>
				
				
				</c:if>

			
		</logic:iterate>
		
		<siga:FilaConIconos fila="200" botones ="" visibleBorrado="false" visibleEdicion="false" visibleConsulta="false"  clase="listaNonEdit">
					
					<td colspan="3" align="right"><b><siga:Idioma key="gratuita.listadoUnidadFamiliar.literal.totalUnidadFamiliar"/></b></td>
					<td align="right"><c:out
						value="${DefinirUnidadFamiliarEJGForm.importeIngresosAnuales}"></c:out>&euro;</td>
					<td align="right"><c:out
						value="${DefinirUnidadFamiliarEJGForm.importeBienesMuebles}"></c:out>&euro;</td>
					<td align="right"><c:out
						value="${DefinirUnidadFamiliarEJGForm.importeBienesInmuebles}"></c:out>&euro;</td>
					<td align="right"><c:out
						value="${DefinirUnidadFamiliarEJGForm.importeOtrosBienes}"></c:out>&euro;</td>
					<td align="right"></td>
		
		</siga:FilaConIconos>
	</logic:notEmpty>

</siga:TablaCabecerasFijas>
<c:if test="${DefinirUnidadFamiliarEJGForm.permisoEejg==true}">
</td></tr>
<tr >
<td>
<table class="tablaTitulo" cellspacing="0" heigth="38">
<tr>
	<td id="titulo" class="titulitosDatos">
	
		<siga:Idioma key="gratuita.eejg.peticiones.titulo"/>
	</td>
</tr>
</table>
</td></tr>
<tr >
	  <td style="vertical-align: top; height:300px">





<siga:TablaCabecerasFijas 	
	   
		   nombre="listadoPeticiones"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='checkTodos()'/> ,gratuita.busquedaEJG.literal.nif,gratuita.busquedaEJG.literal.nombre,gratuita.eejg.peticiones.usuarioPeticion,gratuita.eejg.peticiones.fechaPeticion,"
		   tamanoCol="4,10,30,30,10,"
		   alto="500"
		   
			 
		  >
		  
	<logic:empty name="DefinirUnidadFamiliarEJGForm" property="peticionesEejg">
	<br>
   		 <p class="titulitos" style="text-align:center" ><siga:Idioma key="messages.noRecordFound"/></p>
 		<br>
	</logic:empty>
	<logic:notEmpty name="DefinirUnidadFamiliarEJGForm"	property="peticionesEejg">
		<logic:iterate name="DefinirUnidadFamiliarEJGForm"	property="peticionesEejg" id="peticion" indexId="indice" type="com.siga.beans.eejg.ScsEejgPeticionesBean">

						
			
			<%indice = indice.intValue()+1; %>
						<input type="hidden" name="peticion<%=indice%>_1" value="${peticion.idPersona}">
						<input type="hidden" name="peticion<%=indice%>_2" value="${peticion.idInstitucion}">
						<input type="hidden" name="peticion<%=indice%>_3" value="${peticion.idTipoEjg}">
						<input type="hidden" name="peticion<%=indice%>_4" value="${peticion.anio}">
						<input type="hidden" name="peticion<%=indice%>_5" value="${peticion.numero}">
						<c:choose>
							<c:when test="${peticion.idPeticion!=null}">
							<input type="hidden" name="peticion<%=indice%>_6" value="${peticion.idPeticion}">
							</c:when>
							<c:otherwise>
							  <input type="hidden" name="peticion<%=indice%>_6" value=" ">
							</c:otherwise>
						</c:choose>
			
				<bean:define id="elementosFila" name="peticion" property="elementosFila" type="com.siga.tlds.FilaExtElement[]"/>
				<siga:FilaConIconos fila="<%=String.valueOf(indice.intValue())%>" botones="" elementos="<%=elementosFila%>" clase="listaNonEdit" modo="<%=modo%>" visibleBorrado="false" visibleEdicion="false"	visibleConsulta="false">
					<td align="center">
						<c:choose>
							<c:when test="${peticion.idXml!=null}">
							<input type="checkbox" value="${peticion.idXml}" name="chkPersona">
							</c:when>
							<c:otherwise>
							  <input type="checkbox" value="<bean:write name="indice"/>" disabled name="chkPersona">
							</c:otherwise>
						</c:choose>
					
						
					</td>
					
					<td><c:out value="${peticion.nif}"></c:out>&nbsp;</td>
					<td><c:out value="${peticion.nombre}"></c:out>&nbsp;
					<c:out value="${peticion.apellido1}"></c:out>&nbsp;
					<c:out value="${peticion.apellido2}"></c:out>
					</td>
					<td>
					<c:out value="${peticion.usuarioPeticion.NIF}"></c:out>&nbsp;-&nbsp;<c:out
					 value="${peticion.usuarioPeticion.descripcion}"/>
					</td>
					<td>
					<c:out value="${peticion.fechaPeticion}"/>
					</td>

					
					</siga:FilaConIconos>

				
				

			
		</logic:iterate>
		
		
	</logic:notEmpty>

</siga:TablaCabecerasFijas>


</td></tr></table>

</c:if>







<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
<c:choose>
	<c:when test="${DefinirUnidadFamiliarEJGForm.permisoEejg==true}">
	<siga:ConjBotonesAccion botones="V,N,DEE" modo="${DefinirUnidadFamiliarEJGForm.modo}" clase="botonesDetalle" />
	</c:when>
	<c:otherwise>
	 <siga:ConjBotonesAccion botones="V,N" modo="${DefinirUnidadFamiliarEJGForm.modo}" clase="botonesDetalle" />
	</c:otherwise>
</c:choose>
<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "${DefinirUnidadFamiliarEJGForm.idInstitucion}"/>
	<html:hidden property="idTipoInforme" value='<%=usrBean.isComision() ?"CAJG":"EJG"%>'/>
	<html:hidden property="enviar" value="0"/ />
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="destinatarios" value="S"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>	
	<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" value="">

</form>
	
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
	
	
</body>	

	
	<script type="text/javascript">
				
	//Asociada al boton Cerrar
	function accionVolver()
	{
		document.DefinirUnidadFamiliarEJGForm.action="./JGR_EJG.do";	
		document.DefinirUnidadFamiliarEJGForm.modo.value="buscar";
		document.DefinirUnidadFamiliarEJGForm.target="mainWorkArea"; 
		document.DefinirUnidadFamiliarEJGForm.submit(); 

	}
	
	function accionNuevo()
	{
		
			var idPersonaJG = document.getElementById( 'oculto' + '1' + '_6');
			if (idPersonaJG==null)			{
			    alert("<siga:Idioma key='gratuita.listadoUnidadFamiliarEJG.solicitante'/>");
				return;
			}

		document.PersonaJGForm.target = "submitArea";
		document.PersonaJGForm.modo.value = "abrirPestana";
		document.PersonaJGForm.accionE.value = "nuevo";
		var resultado=ventaModalGeneral(document.forms[0].name,"G");
		if (resultado=="MODIFICADO") buscar();
		
	}
	function buscar()
	{		
		document.DefinirUnidadFamiliarEJGForm.modo.value = "abrir";
		document.DefinirUnidadFamiliarEJGForm.target = "mainPestanas";
		document.DefinirUnidadFamiliarEJGForm.submit();
	}
	function comunicar(fila) {

		var idPersonaJG = document.getElementById( 'oculto' + fila + '_6');
		var idInstitucionEJG = document.getElementById( 'oculto' + fila + '_7');
		var idTipoEJG = document.getElementById( 'oculto' + fila + '_8');
		var anio = document.getElementById( 'oculto' + fila + '_9');
		var numero = document.getElementById( 'oculto' + fila + '_10');
		var esComision = document.getElementById( 'oculto' + fila + '_12');
		var solicitante = document.getElementById( 'oculto' + fila + '_13');
		
		var datos = "idinstitucion=="+idInstitucionEJG.value + "##idtipo==" +idTipoEJG.value+"##anio=="+anio.value +"##numero==" +numero.value+"##idPersonaJG==" +idPersonaJG.value+"##idTipoInforme==EJG%%%";
		
		if(solicitante.value==1){
			document.InformesGenericosForm.enviar.value = '1';
			
		}else{
			document.InformesGenericosForm.enviar.value = '0';
		}
		document.InformesGenericosForm.datosInforme.value=datos;
		var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
		if (arrayResultado==undefined||arrayResultado[0]==undefined){
		   		
	   	} 
	   	else {
	   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
	   		if(confirmar){
	   			var idEnvio = arrayResultado[0];
			    var idTipoEnvio = arrayResultado[1];
			    var nombreEnvio = arrayResultado[2];				    
			    
			   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
			   	document.DefinirEnviosForm.modo.value='editar';
			   	document.DefinirEnviosForm.submit();
	   		}
	   	}

		
		
		
		
	   
	} 
	function validaNumeroIdentificacion (nif,idTipoIdentificacion){
		var errorNIE = false;
		var errorNIF = false;
		var errorDatos = false;
		var valido = true;

		if(idTipoIdentificacion== "<%=ClsConstants.TIPO_IDENTIFICACION_NIF%>"){
			var numero = nif;
			if(numero.length==9){
				letIn = numero.substring(8,9);
				num = numero.substring(0,8);
				var posicion = num % 23;
				letras='TRWAGMYFPDXBNJZSQVHLCKET';
				var letra=letras.substring(posicion,posicion+1);
				if (letra!=letIn) {
					errorNIF=true;
				}
			}else{
				errorNIF=true;
			}
		}else if(idTipoIdentificacion== "<%=ClsConstants.TIPO_IDENTIFICACION_TRESIDENTE%>"){
			var dnie = nif;
			if(dnie.length==9){
				letIni = dnie.substring(0,1);
				primera=letIni;
				if  (letIni.toUpperCase()=='Y')
			 		letIni = '1';
			 	else if  (letIni.toUpperCase()=='Z')
			 		letIni = '2';
			 	else{
			 		letIni = '0';
			 	}
				num = letIni + dnie.substring(1,8);
				letFin = dnie.substring(8,9);
				var posicion = num % 23;
				letras='TRWAGMYFPDXBNJZSQVHLCKET';
				var letra=letras.substring(posicion,posicion+1);
				if (!primera.match('[X|Y|Z]')||letra!=letFin) {
					errorNIE=true;
				}
			}else{
				errorNIE=true;
			}
		}
		if (errorNIF){
			valido = false;
			alert("<siga:Idioma key='messages.nif.comprobacion.digitos.error'/>");
		}
		if (errorNIE){
			valido = false;
			alert("<siga:Idioma key='messages.nie.comprobacion.digitos.error'/>");
		}
		
		return valido;
	}
	
	function solicitarEejg(fila) {
		
		
		var nif = document.getElementById( 'oculto' + fila + '_14').value;
		var idTipoIdentificacion = document.getElementById( 'oculto' + fila + '_15').value;
		
		if (!validaNumeroIdentificacion(nif,idTipoIdentificacion)) {
			fin();
			return false;
		}
		
		var isConfirmado = confirm('<siga:Idioma key="gratuita.eejg.message.confirmaSolicitud"/>');
		if(!isConfirmado)
			return;
		var idPersonaJG = document.getElementById( 'oculto' + fila + '_6');
		var idInstitucionEJG = document.getElementById( 'oculto' + fila + '_7');
		var idTipoEJG = document.getElementById( 'oculto' + fila + '_8');
		var anio = document.getElementById( 'oculto' + fila + '_9');
		var numero = document.getElementById( 'oculto' + fila + '_10');
			datos = idPersonaJG.value + 	','
	   			+idInstitucionEJG.value + 	','
	   			+idTipoEJG.value + 	','
	   			+anio.value + 	','
	   			+numero.value + '#';
   		
	   	document.EEJG.tablaDatosDinamicosD.value = datos;
	   	document.EEJG.modo.value = "solicitarEejg";
		document.EEJG.submit();
	
	}	
	function accionCerrar() {

	}
	function descargarEejg(fila) {
		selectRowPeticiones(fila);
		
		var idPersonaJG = document.getElementById( 'peticion' + fila + '_1');
		var idInstitucionEJG = document.getElementById( 'peticion' + fila + '_2');
		var idTipoEJG = document.getElementById( 'peticion' + fila + '_3');
		var anio = document.getElementById( 'peticion' + fila + '_4');
		var numero = document.getElementById( 'peticion' + fila + '_5');
		var idPeticion = document.getElementById( 'peticion' + fila + '_6');
		datos = idPersonaJG.value + 	','
	   			+idInstitucionEJG.value + 	','
	   			+idTipoEJG.value + 	','
	   			+anio.value + 	','
	   			+numero.value + ','
				+idPeticion.value + ''
				'#';
		
	   	document.EEJG.tablaDatosDinamicosD.value = datos;
	   	document.EEJG.modo.value = "descargaEejg";
		document.EEJG.submit();
	
	}
	function descargaEejg() {
		var chkPersonas = document.getElementsByName("chkPersona");
		datos = '';
		sub();
		for (i = 0; i < chkPersonas.length; i++) {
			if(chkPersonas[i].checked){
				var idPersonaJG = document.getElementById( 'peticion' + (i+1) + '_1');
				var idInstitucionEJG = document.getElementById( 'peticion' + (i+1) + '_2');
				var idTipoEJG = document.getElementById( 'peticion' + (i+1) + '_3');
				var anio = document.getElementById( 'peticion' + (i+1) + '_4');
				var numero = document.getElementById( 'peticion' + (i+1) + '_5');
				
				var idPeticion = document.getElementById( 'peticion' + (i+1) + '_6');
				datos = datos + idPersonaJG.value + 	','
	   			+idInstitucionEJG.value + 	','
	   			+idTipoEJG.value + 	','
	   			+anio.value + 	','
	   			+numero.value + ','
	   			
				+idPeticion.value + 	'#';
	 		   
 		   	}
			
		}
		if(datos==''){
			alert("<siga:Idioma key="general.message.seleccionar"/>");
			fin();
			return;
		}
		
	   	document.EEJG.tablaDatosDinamicosD.value = datos;
	   	document.EEJG.modo.value = "descargaEejgMasivo";
		document.EEJG.submit();
	
	}	
		
	function checkTodos(){
		var chkGeneral = document.getElementById("chkGeneral");
	 	var chkPersonas = document.getElementsByName("chkPersona");
	  	for (i = 0; i < chkPersonas.length; i++) {
	  		if(chkGeneral.checked){
	   			if(!chkPersonas[i].disabled){
	   				chkPersonas[i].checked = chkGeneral.checked;
	   			} 
   			}else{
   				chkPersonas[i].checked = chkGeneral.checked; 
   			}
   		}
   	}
   	
   	function esperaEejg(fila){
   		selectRowPeticiones(fila);
   		alert("<siga:Idioma key="general.boton.esperaEejg"/>");
   	}
   	function errorEejg(fila){
   		selectRowPeticiones(fila);
   		alert("<siga:Idioma key="general.boton.errorEejg"/>");
   	}
   	
   	function esperaInfoEejg(fila){
   		selectRowPeticiones(fila);
   		var confirmar = confirm("<siga:Idioma key='gratuita.eejg.message.avisoEsperaInfo'/>");
   		if(confirmar){
   			return descargarEejg(fila);
   		}
   		
   	}
   	function avisoEsperaInfoEejg(fila){
   		selectRowPeticiones(fila);
   		alert("<siga:Idioma key="general.boton.esperaInfoEejg"/>");
   		
   		
   	}
   	
   	function esperaAdministracionesEejg(fila){
   		selectRowPeticiones(fila);
   		alert("<siga:Idioma key="general.boton.esperaAdministracionesEejg"/>");
   	}
	
	function refrescarLocal()
	{	
		buscar();
	}
	function accionNuevaRegularizacion(){
		document.DefinirUnidadFamiliarEJGForm.modo.value = "simulaWebService";
		document.DefinirUnidadFamiliarEJGForm.submit();
	
   	}
	function ajustarCabeceraTabla(){
		if(document.all.listadoPeticiones){
			ajusteAlto("listadoUnidadFamiliarDiv");
			ajusteAlto("listadoPeticionesDiv");
			
			
			if (document.all.listadoUnidadFamiliar.clientHeight < document.all.listadoUnidadFamiliarDiv.clientHeight) {
							document.all.listadoUnidadFamiliarCabeceras.width='100%';
		  	} else {
			  
			  document.all.listadoUnidadFamiliarCabeceras.width='98.43%';
			  
			   
			   
			   
		  	}
			
				if (document.all.listadoPeticiones.clientHeight < document.all.listadoPeticionesDiv.clientHeight) {
					document.all.listadoPeticionesCabeceras.width='100%';
					 
				  } else {
					  document.all.listadoPeticionesCabeceras.width='98.43%';
					  
					  
					   
					   
				  }
				
			
			
		}
}
	
	function selectRowPeticiones(fila) {
		   document.getElementById('filaSelD').value = fila;
		   var tabla;
		   tabla = document.getElementById('listadoPeticiones');
		   for (var i=0; i<tabla.rows.length; i++) {
		     if (i%2 == 0) tabla.rows[i].className = 'filaTablaPar';
		     else          tabla.rows[i].className = 'filaTablaImpar';
		   }
		   tabla.rows[fila].className = 'listaNonEditSelected';
		   
		   document.getElementById('filaSelD').value = fila;
		   var tabla;
		   tabla = document.getElementById('listadoUnidadFamiliar');
		   for (var i=0; i<tabla.rows.length; i++) {
		     if (i%2 == 0) tabla.rows[i].className = 'filaTablaPar';
		     else          tabla.rows[i].className = 'filaTablaImpar';
		   }
		   
		   
		   
		 }
	
	function selectRow(fila) {
		   document.getElementById('filaSelD').value = fila;
		   var tabla;
		   tabla = document.getElementById('listadoUnidadFamiliar');
		   for (var i=0; i<tabla.rows.length; i++) {
		     if (i%2 == 0) tabla.rows[i].className = 'filaTablaPar';
		     else          tabla.rows[i].className = 'filaTablaImpar';
		   }
		   if(tabla.rows[fila])
		   	tabla.rows[fila].className = 'listaNonEditSelected';
		 }
		 
		 function consultar(fila) {
		   var datos;
		   datos = document.getElementById('tablaDatosDinamicosD');
		   datos.value = ""; 
		   var i, j;
		   for (i = 0; i < 8; i++) {
		      var tabla;
		      tabla = document.getElementById('listadoUnidadFamiliar');
		      if (i == 0) {
		        var flag = true;
		        j = 1;
		        while (flag) {
		          var aux = 'oculto' + fila + '_' + j;
		          var oculto = document.getElementById(aux);
		          if (oculto == null)  { flag = false; }
		          else { 
		          if(oculto.value=='')       		oculto.value=' ';
					datos.value = datos.value + oculto.value + ','; }
		          j++;
		        }
		        datos.value = datos.value + "%"
		      } else { j = 2; }
		      if ((tabla.rows[fila].cells)[i].innerText == "")
		        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
		      else
		        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
		   }
		   document.forms[0].modo.value = "Ver";
		   ventaModalGeneral(document.forms[0].name,"G");
		 }
		 
		 function editar(fila) {
		   var datos;
		   datos = document.getElementById('tablaDatosDinamicosD');
		   datos.value = ""; 
		   var i, j;
		   for (i = 0; i < 8; i++) {
		      var tabla;
		      tabla = document.getElementById('listadoUnidadFamiliar');
		      if (i == 0) {
		        var flag = true;
		        j = 1;
		        while (flag) {
		          var aux = 'oculto' + fila + '_' + j;
		          var oculto = document.getElementById(aux);
		          if (oculto == null)  { flag = false; }
		          else { 
		          if(oculto.value=='')       		oculto.value=' ';
					datos.value = datos.value + oculto.value + ','; }
		          j++;
		        }
		        datos.value = datos.value + "%"
		      } else { j = 2; }
		      if ((tabla.rows[fila].cells)[i].innerText == "") 
		        datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
		      else
		        datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
		   }
		   document.forms[0].modo.value = "Editar";
		   var resultado = ventaModalGeneral(document.forms[0].name,"G");
		   if (resultado) {
		  	 	if (resultado[0]) {
		   		refrescarLocalArray(resultado);
		   	} else 
		   	if (resultado=="MODIFICADO")
		   	{
		      		refrescarLocal();
		   	}
		   }
		 }
		 
		 function borrar(fila) {
		   var datos;
		   if (confirm('Si elimina una persona de la unidad familiar, su documentación también será eliminada.\r\n¿Desea continuar?')){
		   	datos = document.getElementById('tablaDatosDinamicosD');
		       datos.value = ""; 
		   	var i, j;
		   	for (i = 0; i < 8; i++) {
		      		var tabla;
		      		tabla = document.getElementById('listadoUnidadFamiliar');
		      		if (i == 0) {
		        		var flag = true;
		        		j = 1;
		        		while (flag) {
		          			var aux = 'oculto' + fila + '_' + j;
		          			var oculto = document.getElementById(aux);
		          			if (oculto == null)  { flag = false; }
		          else { 
		          if(oculto.value=='')       		oculto.value=' ';
					datos.value = datos.value + oculto.value + ','; }
		          			j++;
		        		}
		        		datos.value = datos.value + "%"
		      		} else { j = 2; }
		      		if ((tabla.rows[fila].cells)[i].innerText == "")
		        		datos.value = datos.value + (tabla.rows[fila].cells)[i].all[j-2].value + ',';
		      		else
		        		datos.value = datos.value + (tabla.rows[fila].cells)[i].innerText + ',';
		   	}
		   	var auxTarget = document.forms[0].target;
		   	document.forms[0].target="submitArea";
		   	document.forms[0].modo.value = "Borrar";
		   	document.forms[0].submit();
		   	document.forms[0].target=auxTarget;
		 	}
		 }


	
	</script>
</html>