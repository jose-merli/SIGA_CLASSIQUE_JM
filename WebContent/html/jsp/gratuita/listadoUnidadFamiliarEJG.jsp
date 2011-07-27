<!-- listadoUnidadFamiliarEJG-->

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

<script>	
	function actualizaTexto(fila,style,usuario,dni,fecha){
		// document.getElementById(idFilaBotones).className = 'labelText';
		td = document.getElementById("idFilaBotones_"+fila);
		if(usuario!=''){
			var textoUsuarioPeticion = "<siga:Idioma key='eejg.solicitado'/>";
			textoUsuarioPeticion += '(';
			textoUsuarioPeticion += fecha;
			textoUsuarioPeticion += ') ';
			textoUsuarioPeticion += "<siga:Idioma key='general.por'/>";
			textoUsuarioPeticion += ' ';
			textoUsuarioPeticion += dni;
			textoUsuarioPeticion += '-';
			textoUsuarioPeticion += usuario;

			
			var innerHTMLOld = td.innerHTML;
			td.innerHTML = '';
			tabla = document.createElement('table');
			tabla.setAttribute("width","100%");
			
			tr1 = tabla.insertRow();
			tr1.className = style;
			td1 = tr1.insertCell();
			td1.innerHTML = innerHTMLOld;

			
			tr2 = tabla.insertRow();
			tr2.className = style;
			
			td2 = tr2.insertCell();
			tablatd = document.createElement('table');
			tablatd.setAttribute("width","100%");
			tablatd.setAttribute("border","1");
			tablatd.setAttribute("cellspacing","0");
			tablatd.setAttribute("cellpadding","0");
			tr1tablatd = tablatd.insertRow();
			tr1tablatd.className = style;
			td1tablatd = tr1tablatd.insertCell();
			td1tablatd.innerHTML = textoUsuarioPeticion;
			td2.appendChild(tablatd);
			td.appendChild(tabla);


			}
		
		
		 
	}
	</script>
</head>

<body class="tablaCentralCampos" >	
			

<bean:define id="modo" name="DefinirUnidadFamiliarEJGForm" property="modo" type="java.lang.String"/>
<bean:define id="conceptoEJG" scope="request" name="EJG_UNIDADFAMILIAR" />

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


			<siga:TablaCabecerasFijas 		   
		   nombre="listadoUnidadFamiliar"
		   borde="2"
		   clase="tableTitle"		   
		   nombreCol="<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='checkTodos()'/> ,gratuita.personaJG.literal.parentescoNormalizado,gratuita.busquedaEJG.literal.nif,gratuita.busquedaEJG.literal.nombre,gratuita.operarInteresado.literal.ingresosAnuales,gratuita.operarInteresado.literal.bienesMobiliarios,gratuita.operarInteresado.literal.bienesInmuebles,gratuita.operarInteresado.literal.otrosBienes,"
		   tamanoCol="5,8,7,21,7,7,7,7,32"
		   alto="100%"
		   ajusteBotonera="true"		
		   mensajeBorrado="gratuita.ejg.unidadFamiliar.borrado"
		   modal="G"
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
					
					<td align="center">
						<c:choose>
							<c:when test="${solicitante.peticionEejg.idXml!=null}">
							<input type="checkbox" value="${solicitante.peticionEejg.idXml}" name="chkPersona">
							</c:when>
							<c:otherwise>
							  <input type="checkbox" value="<bean:write name="index"/>" disabled name="chkPersona">
							</c:otherwise>
						</c:choose>
					
						
					</td>
			
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
					<script> actualizaTexto('<%=String.valueOf(index.intValue())%>','<%=((index)%2==0?"filaTablaPar":"filaTablaImpar")%>','<%=(solicitante.getPeticionEejg()!=null&&solicitante.getPeticionEejg().getUsuarioPeticion()!=null)?solicitante.getPeticionEejg().getUsuarioPeticion().getDescripcion():""%>','<%=(solicitante.getPeticionEejg()!=null&&solicitante.getPeticionEejg().getUsuarioPeticion()!=null)?solicitante.getPeticionEejg().getUsuarioPeticion().getNIF():""%>','<%=(solicitante.getPeticionEejg()!=null&&solicitante.getPeticionEejg().getFechaPeticion()!=null)?solicitante.getPeticionEejg().getFechaPeticion():""%>');</script>
				</c:if>
				<c:if	test="${solicitante.idPersona==DefinirUnidadFamiliarEJGForm.personaJG.idPersona}">
				<bean:define id="elementosFila" name="solicitante" property="elementosFila" type="com.siga.tlds.FilaExtElement[]"/>
				<siga:FilaConIconos fila="<%=String.valueOf(index.intValue())%>" botones="" elementos="<%=elementosFila%>" clase="listaNonEdit" modo="<%=modo%>" visibleBorrado="false" visibleEdicion="false"	visibleConsulta="false">
					<td align="center" >
						
						<c:choose>
							<c:when test="${solicitante.peticionEejg.idXml!=null}">
							<input type="checkbox" value="${solicitante.peticionEejg.idXml}" name="chkPersona">
							</c:when>
							<c:otherwise>
							  <input type="checkbox" value="<bean:write name="index"/>" disabled name="chkPersona">
							</c:otherwise>
						</c:choose>
						
					</td>
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
				<script> actualizaTexto('<%=String.valueOf(index.intValue())%>','<%=((index)%2==0?"filaTablaPar":"filaTablaImpar")%>','<%=(solicitante.getPeticionEejg()!=null&&solicitante.getPeticionEejg().getUsuarioPeticion()!=null)?solicitante.getPeticionEejg().getUsuarioPeticion().getDescripcion():""%>','<%=(solicitante.getPeticionEejg()!=null&&solicitante.getPeticionEejg().getUsuarioPeticion()!=null)?solicitante.getPeticionEejg().getUsuarioPeticion().getNIF():""%>','<%=(solicitante.getPeticionEejg()!=null&&solicitante.getPeticionEejg().getFechaPeticion()!=null)?solicitante.getPeticionEejg().getFechaPeticion():""%>');</script>
				
				
				</c:if>

			
		</logic:iterate>
		
		<siga:FilaConIconos fila="200" botones ="" visibleBorrado="false" visibleEdicion="false" visibleConsulta="false"  clase="listaNonEdit">
					
					<td colspan="4" align="right"><b><siga:Idioma key="gratuita.listadoUnidadFamiliar.literal.totalUnidadFamiliar"/></b></td>
					<td align="right"><c:out
						value="${DefinirUnidadFamiliarEJGForm.importeIngresosAnuales}"></c:out>&euro;</td>
					<td align="right"><c:out
						value="${DefinirUnidadFamiliarEJGForm.importeBienesMuebles}"></c:out>&euro;</td>
					<td align="right"><c:out
						value="${DefinirUnidadFamiliarEJGForm.importeBienesInmuebles}"></c:out>&euro;</td>
					<td align="right"><c:out
						value="${DefinirUnidadFamiliarEJGForm.importeOtrosBienes}"></c:out>&euro;</td>
		
		</siga:FilaConIconos>
	</logic:notEmpty>

</siga:TablaCabecerasFijas>
<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
<c:choose>
	<c:when test="${DefinirUnidadFamiliarEJGForm.permisoEejg==true}">
	<siga:ConjBotonesAccion botones="V,N,DEE" modo="${DefinirUnidadFamiliarEJGForm.modo}" clase="botonesDetalle" />
	</c:when>
	<c:otherwise>
	 <siga:ConjBotonesAccion botones="V,N" modo="${DefinirUnidadFamiliarEJGForm.modo}" clase="botonesDetalle" />
	</c:otherwise>
</c:choose>

	
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
		
		var datos = "idinstitucion=="+idInstitucionEJG.value + "##idtipo==" +idTipoEJG.value+"##anio=="+anio.value +"##numero==" +numero.value+"##idPersonaJG==" +idPersonaJG.value+"%%%";
	   				 
	   	var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='INF_InformesGenericos.do' target='submitArea'>");
		formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='${DefinirUnidadFamiliarEJGForm.idInstitucion}'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='EJG'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='asolicitantes' value='S'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
		if(solicitante.value==1){
			formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='1'>"));
		}else
			formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='0'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='1'>"));
		
		
		document.appendChild(formu);
		formu.datosInforme.value=datos;
		formu.submit();

		
		
		
		
	   
	} 
	function solicitarEejg(fila) {
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
		var idPersonaJG = document.getElementById( 'oculto' + fila + '_6');
		var idInstitucionEJG = document.getElementById( 'oculto' + fila + '_7');
		var idTipoEJG = document.getElementById( 'oculto' + fila + '_8');
		var anio = document.getElementById( 'oculto' + fila + '_9');
		var numero = document.getElementById( 'oculto' + fila + '_10');
		var idPeticion = document.getElementById( 'oculto' + fila + '_11');
		
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
				var idPersonaJG = document.getElementById( 'oculto' + (i+1) + '_6');
				var idInstitucionEJG = document.getElementById( 'oculto' + (i+1) + '_7');
				var idTipoEJG = document.getElementById( 'oculto' + (i+1) + '_8');
				var anio = document.getElementById( 'oculto' + (i+1) + '_9');
				var numero = document.getElementById( 'oculto' + (i+1) + '_10');
				
				var idPeticion = document.getElementById( 'oculto' + (i+1) + '_11');
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
   	
   	function esperaEejg(){
   		alert("<siga:Idioma key="general.boton.esperaEejg"/>");
   	}
   	function errorEejg(){
   		alert("<siga:Idioma key="general.boton.errorEejg"/>");
   	}
   	
   	function esperaInfoEejg(fila){
   		var confirmar = confirm("<siga:Idioma key='gratuita.eejg.message.avisoEsperaInfo'/>");
   		if(confirmar){
   			return descargarEejg(fila);
   		}
   		
   	}
   	function avisoEsperaInfoEejg(){
   		
   		alert("<siga:Idioma key="general.boton.esperaInfoEejg"/>");
   		
   		
   	}
   	
   	function esperaAdministracionesEejg(){
   		
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
   	
	
	</script>
</html>