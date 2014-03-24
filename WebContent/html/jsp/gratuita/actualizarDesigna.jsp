<!DOCTYPE html>
<html>
<head>
<!-- actualizarDesigna.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="ajaxtags.tld" prefix="ajax" %>



<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>

<script>

	function cambiarJuzgado() {
		var combo = document.getElementById("idJuzgado").value;
		
		if(combo!="-1"){
			jQuery.ajax({ //Comunicación jQuery hacia JSP  
						type: "POST",
				url: "/SIGA/GEN_Juzgados.do?modo=getAjaxJuzgado3",
				data: "idCombo="+combo,
				dataType: "json",
				success: function(json){		
	    	   		document.getElementById("codigoExtJuzgado").value = json.codigoExt2;      		
					fin();
				},
				error: function(e){
					alert('Error de comunicación: ' + e);
					fin();
				}
			});
		}
		else
			document.getElementById("codigoExtJuzgado").value = "";
	}		
	
	function obtenerJuzgado() { 
		var codigo = document.getElementById("codigoExtJuzgado").value;
		
		if(codigo!=""){
			jQuery.ajax({ //Comunicación jQuery hacia JSP  
						type: "POST",
				url: "/SIGA/GEN_Juzgados.do?modo=getAjaxJuzgado4",
				data: "codigo="+codigo,
				dataType: "json",
				success: function(json){		
					if (json.idJuzgado=="") {
						document.getElementById("idJuzgado").value = "-1";
						document.getElementById("codigoExtJuzgado").value = "";
					} else {
	    	   			document.getElementById("idJuzgado").value = json.idJuzgado;
	    	   			if (document.getElementById("idJuzgado").value=="") {
							document.getElementById("idJuzgado").value = "-1";
							document.getElementById("codigoExtJuzgado").value = "";
						}
					}
					fin();
				},
				error: function(e){
					alert('Error de comunicación: ' + e);
					fin();
				}
			});
		}
		else
			document.getElementById("idJuzgado").value = "-1";
	}	
				
	function accionCerrar() 
	{		
		top.cierraConParametros("NORMAL");
	}		
		
	function accionGuardar() 
	{
		var nigAux = document.getElementById("nig").value;
		nigAux = formateaNig(nigAux);
		if(!validarNig(nigAux)){	
			alert("<siga:Idioma key='gratuita.nig.formato'/>");
			return false;
				
	 	}
		document.MaestroDesignasForm.nig.value = nigAux; 
		sub();
		document.MaestroDesignasForm.modo.value = 'actualizaDesigna';
		document.MaestroDesignasForm.submit();
	}
	function postAccionCodigoExtJuzgado(){
		if(document.getElementById('idJuzgado').value!=''){
			document.getElementById('idJuzgado').onchange();
		}else{
			// juzgados
			document.getElementById("juzgados").selectedIndex = '0';
			document.getElementById('idJuzgado').onchange();
		}
	}	
	
	function onload(){
		if(document.MaestroDesignasForm.idJuzgado.value!=''){
			document.getElementById('idJuzgado').onchange();
		}
	}
	
	function postAccionJuzgados(){
		cambiarJuzgado();
		
		var idProcedimiento = document.MaestroDesignasForm.procedimiento.value;
		var optionsProcedimientos = document.getElementById("modulos");
		var encontrado;
		// alert("option:"+optionsProcedimientos.length);
		for(var i = 0 ; i <optionsProcedimientos.length ; i++) {
			var option = optionsProcedimientos.options[i].value;
			if(option == idProcedimiento){
				encontrado=i;
				break;
			}
		}
		if (encontrado){
			optionsProcedimientos.selectedIndex=encontrado;
		} else {
			optionsProcedimientos.selectedIndex=0;
		}
	}
	function tab(pestana,panel)
	{
		pst 	= document.getElementById(pestana);
		pnl 	= document.getElementById(panel);
		psts	= document.getElementById('tabs').getElementsByTagName('li');
		pnls	= document.getElementById('paneles').getElementsByTagName('div');
		
		// eliminamos las clases de las pestañas
		for(i=0; i< psts.length; i++)
		{
			psts[i].className = '';
			psts[i].style.width="100px";
		}
		
		// Añadimos la clase "actual" a la pestaña activa
		pst.className = 'actual';
		
		// eliminamos las clases de las pestañas
		for(i=0; i< pnls.length; i++)
		{
			pnls[i].style.display = 'none';
		}
		
		// Añadimos la clase "actual" a la pestaña activa
		pnl.style.display = 'block';
	}
	jQuery(function($){
		var defaultValue = jQuery("#nig").val();
		if(defaultValue.length > 19){
			jQuery('#info').show();
			jQuery('#imagenInfo').attr('title',defaultValue) ;
		}else{
			jQuery('#info').hide();
			
		}
		jQuery("#nig").mask("AAAAA AA A AAAA AAAAAAA");
		jQuery("#nig").keyup();	
	});	
</script>
</head>

<body onload="onload();">

<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td id="titulo" class="titulitosDatos">
			<siga:Idioma key="gratuita.actuacionesDesigna.literal.actualizarDesigna" />
		</td>
	</tr>
</table>


<!-- INICIO: CAMPOS DE BUSQUEDA-->
<html:form action="/JGR_ActualizarInformeJustificacion" method="POST" target="submitArea">
	<html:hidden name="MaestroDesignasForm" property="modo" value="" />
	<html:hidden name="MaestroDesignasForm" property="anio"  />
	<html:hidden name="MaestroDesignasForm" property="numero" />
	<html:hidden name="MaestroDesignasForm" property="fecha" />
	<html:hidden name="MaestroDesignasForm" property="idTurno"/>
	<html:hidden name="MaestroDesignasForm" property="procedimiento"/>
	<html:hidden name="MaestroDesignasForm" property="fichaColegial"/>
	
	<table class="tablaCentralCampos" height="420" align="center">

		<tr>
			<td valign="top"><siga:ConjCampos
				leyenda="gratuita.busquedaDesignas.literal.turno">
				<table class="tablaCampos" align="center" cellpadding="0"
					cellpadding="0" width="100%" border="0">
					<tr>
						<td class="labelText"><siga:Idioma key="facturacion.ano" />/<siga:Idioma key="gratuita.busquedaDesignas.literal.codigo" />
						</td>
						<td class="labelText"><html:text name="MaestroDesignasForm"
							property="codigo" size="10" maxlength="10"
							styleClass="boxConsulta" readonly="true"></html:text>
						</td>


						<td class="labelText"><siga:Idioma
							key='sjcs.designa.general.letrado' /></td>
						<td><html:text name="MaestroDesignasForm" property="letrado"
							styleClass="boxConsulta" readonly="true" style="width: 400" /></td>
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma
							key="gratuita.busquedaSOJ.literal.turno" /></td>
						<td><html:text name="MaestroDesignasForm" property="turno"
							styleClass="boxConsulta" readonly="true"></html:text></td>
						<td class="labelText"><siga:Idioma
							key="gratuita.inicio_PestanaCalendarioGuardias.literal.fecha" />
						</td>
						<!-- JBD 16/2/2009 INC-5682-SIGA -->

						<td><html:text name="MaestroDesignasForm" property="fecha"
							size="10" maxlength="10" styleClass="boxConsulta" readonly="true"></html:text>
						</td>

						<!-- JBD 16/2/2009 INC-5682-SIGA -->
					</tr>
				</table>
			</siga:ConjCampos> 
			<siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.designa">
				<table class="tablaCampos" align="center" cellpadding="0"
					cellpadding="0" width="100%" border="0">
					<tr>
									<td  width="20%" class="labelText"><siga:Idioma
										key='gratuita.mantenimientoTablasMaestra.literal.numeroProcedimiento' /></td>
									<td width="20%"><html:text name="MaestroDesignasForm" property="numeroProcedimiento"  maxlength="15" 
										styleClass="box" style="width: 100" /></td>
									<td width="10%" class="labelText" width="10%"><siga:Idioma
										key="gratuita.mantenimientoTablasMaestra.literal.juzgado" />
									</td>
									<td class="labelText" width="10%">
										<input type="text" name="codigoExtJuzgado" styleId="codigoExtJuzgado" class="box" size="8" maxlength="10" onBlur="obtenerJuzgado();"/>&nbsp;
									</td>
									<td width="40%">
										<html:select styleId="juzgados" styleClass="boxCombo" style="width:500px;" property="idJuzgado">
											<bean:define id="juzgados" name="MaestroDesignasForm" property="juzgados" type="java.util.Collection" />
											<html:optionsCollection name="juzgados" value="idJuzgado" label="nombre" />
										</html:select>
									</td>
						
					</tr>
					<tr>
						
						<td width="20%"  class="labelText"><siga:Idioma
										key="gratuita.actuacionesDesigna.literal.modulo" /></td>
						<td colspan="5">
						<html:select styleId="modulos" styleClass="boxCombo" style="width:400px;"
								property="idProcedimiento" >
								<bean:define id="modulos" name="MaestroDesignasForm"
									property="modulos" type="java.util.Collection" />
									<html:optionsCollection name="modulos" value="idProcedimiento"
										label="nombre" />
								</html:select>
						
						</td>
					</tr>
					<tr>
						
						<td width="20%"  class="labelText"><siga:Idioma key='gratuita.mantAsistencias.literal.NIG'/></td>

						<td>
							<html:text name="MaestroDesignasForm" property="nig" styleId="nig"  styleClass="box" style="size:19;width:200px"/>
	
						</td>			
						<td id="info" style="display:none"><img  id="imagenInfo" src="/SIGA/html/imagenes/info.gif"	style="cursor: hand;"	title="" border="0" />
						</td>
						<td colspan="3"></td>						
					</tr>
					
					<tr><td  colspan="6">&nbsp;</td></tr>
				</table>
			</siga:ConjCampos>
		
 			<siga:ConjCampos leyenda="gratuita.operarEJG.literal.expedienteEJG">

			 	<bean:define id="ejgs" name="MaestroDesignasForm"	property="ejgs" type="java.util.Collection"/>
	   	<c:choose>
		<c:when test="${empty ejgs}">
		<table class="tablaCampos" align="center" cellpadding="0"
					cellpadding="0" width="100%" border="0">

			<tr>
				<td colspan="13" class="titulitos" style="text-align: center"><siga:Idioma
					key="messages.noRecordFound" /></td>
			</tr>
		</table>	
		</c:when>
		<c:otherwise>
				<div align="right" id="panel">
					<ul id="tabs">
					<logic:notEmpty name="MaestroDesignasForm" property="ejgs">
						<logic:iterate name="MaestroDesignasForm" property="ejgs" id="ejg1" indexId="index">
					    	<li id="tab_${index}"><a href="#" onclick="tab('tab_${index}','panel_${index}');"><c:out
							value="${ejg1.anio}" />/<c:out
							value="${ejg1.numEJG}" /></a></li>
						</logic:iterate>
					</logic:notEmpty>		   
				    </ul>
					<div id="paneles">

					<logic:notEmpty name="MaestroDesignasForm" property="ejgs">
						<logic:iterate name="MaestroDesignasForm" property="ejgs" id="ejg2" indexId="index2">
							<div id="panel_${index2}" style="display: inline">
								<table class="tablaCampos" align="center" cellpadding="0"
									cellpadding="0" width="100%" border="0">
								<tr>
										<td colspan="2"   class="labelText" style="width:100px;">	
											<siga:Idioma key='gratuita.operarEJG.literal.interesado'/>
										</td>
										<td colspan="4" class="labelTextValue" style="width:300px;">	
										<c:out		value="${ejg2.tipoLetrado}" />

										</td>
								
								</tr>
								<tr>
									<td class="labelText" style="width:100px;">
										<siga:Idioma key='gratuita.busquedaEJG.literal.fechaApertura'/>
									</td>
									<td  class="labelTextValue" style="width:100px;">
									 	<c:out		value="${ejg2.fechaApertura}" />	
									</td>
									<td class="labelText" style="width:100px;">	
										<siga:Idioma key='gratuita.busquedaEJG.literal.estadoEJG'/>
									</td>
									<td  class="labelTextValue" colspan="3" style="width:200px;">	
											<c:out		value="${ejg2.estadoEjg}" />	
									</td>	
								</tr>
								<tr>
									<td class="labelText" width="100px">	
										<siga:Idioma key='gratuita.operarEJG.literal.tipo'/>
									</td>
									<td  class="labelTextValue" colspan="5" style="width:300px;">	
										<c:out		value="${ejg2.deTipoEjg}" />	
									</td>
							</tr>
							<tr>
								<td class="labelText" width="15%">
									 <siga:Idioma key='gratuita.busquedaEJG.literal.EJGColegio'/>
								</td>
								<td class="labelTextValue" width="15%">
									<c:out		value="${ejg2.tipoEjgCol}" />	
								</td>
								<td class="labelText" nowrap width="20%">
									<siga:Idioma key='gratuita.operarEJG.literal.fechaPresentacion'/>&nbsp;
								</td>
								<td  class="labelTextValue" width="15%">
									<c:out		value="${ejg2.fechaPresentacion}" />	
								</td>
								<td class="labelText" nowrap width="20%">
												<siga:Idioma key='gratuita.operarEJG.literal.fechaLimitePresentacion'/>
								</td>
								<td  class="labelTextValue" width="15%">
								
								<c:out		value="${ejg2.fechaLimitePresentacion}" />	
								</td>
							</tr>
							<tr>
			
								 <td class="labelText" >	
									<siga:Idioma key='gratuita.operarEJG.literal.CAJG'/> <siga:Idioma key='gratuita.operarEJG.literal.anio'/> / <siga:Idioma key='gratuita.busquedaEJG.literal.codigo'/>
								</td>
								<td  class="labelTextValue"  >
								<c:out		value="${ejg2.anioCAJG}" />	/<c:out		value="${ejg2.numeroCAJG}" />
								</td>
								<td class="labelText">	
								<siga:Idioma key='gratuita.operarEJG.literal.origen'/> 

								</td>
								<td  class="labelTextValue">
								
								<c:out		value="${ejg2.descripcionOrigen}" />	
								</td>
								<td class="labelText" >	
								<siga:Idioma key='gratuita.busquedaEJG.dictamen'/>
								</td> 
								<td  class="labelTextValue">
								<c:out		value="${ejg2.dictamen}" />	
								</td>	
							</td>
							</tr>
								<tr><td colspan="6">   &nbsp;</td></tr>
								</table>

								
								<table class="tablaCampos" align="center" cellpadding="0"
									cellpadding="0" width="100%" border="0">
									
									<tr>
									<td  id="titulo" class="titulitosDatos"  colspan="4" class="labelText" >
									<siga:Idioma key='pestana.justiciagratuitadesigna.defensajuridica'/></td>
									
									</tr>
									<tr><td style="width:100px;"> &nbsp;</td>
									<td style="width:100px;">  &nbsp;</td>
									<td style="width:100px;">  &nbsp;</td>
									<td style="width:100px;"> &nbsp;</td>
									</tr>
									<tr>
										<td class="labelText"><siga:Idioma key='gratuita.mantAsistencias.literal.numeroDiligencia'/></td>
										<td class="labelTextValue">
										<c:out value="${ejg2.numeroDiligencia}" />	
										
										</td> 
										<td class="labelText"><siga:Idioma key='gratuita.mantAsistencias.literal.centroDetencion'/></td>
										<td  class="labelTextValue" >	
												<c:out value="${ejg2.descripcionComisaria}" />
										</td>
										
									</tr>
									<tr>
										<td class="labelText" ><siga:Idioma key='gratuita.mantAsistencias.literal.numeroProcedimiento'/></td>
										<td class="labelTextValue">
										<c:out		value="${ejg2.numeroProcedimiento}" />	
										</td>
										<td class="labelText">	
										 <siga:Idioma key="gratuita.mantenimientoTablasMaestra.literal.juzgado"/>
										</td>	 
										<td  class="labelTextValue">	
										<c:out		value="${ejg2.descripcionJuzgado}" />
										</td>	
									</tr>
									<tr>
										<td class="labelText">
											<siga:Idioma key='gratuita.operarEJG.literal.observacionesAsunto'/>
										</td>
										<td  class="labelTextValue">	
										<c:out		value="${ejg2.observaciones}" />
										</td>
										<td class="labelText">
											<siga:Idioma key='gratuita.general.literal.comentariosDelitos'/>
										</td>
										<td   class="labelTextValue">	
										<c:out		value="${ejg2.delitos}" />
										</td>		
									</tr>			
									<tr>
										<td  class="labelText">	
											<siga:Idioma key='gratuita.personaJG.literal.calidad'/>
										</td>		
										
										<td class="labelTextValue">					
												<c:out		value="${ejg2.calidad}" />										
										</td>	

										<td class="labelText">	
											<siga:Idioma key='gratuita.actuacionesDesigna.literal.pretensiones'/>
										</td>	
										<td class="labelTextValue" >
										<c:out		value="${ejg2.descripcionPretension}" />	
										</td>
									</tr>
									
									</table>
								</div>
							</logic:iterate>
						</logic:notEmpty>	
							
						</div>
						<script type="text/javascript">
							tab('tab_0','panel_0');
						</script>
					</div>
				  </c:otherwise>
				</c:choose>
			</siga:ConjCampos> 
			
			</td>
		</tr>
	</table>
</html:form>

	<ajax:select
		baseUrl="/SIGA/GEN_Juzgados.do?modo=getAjaxModulos"
		source="juzgados" target="modulos" parameters="idJuzgado={idJuzgado},procedimiento={procedimiento},fecha={fecha},fichaColegial={fichaColegial}"
		postFunction="postAccionJuzgados"
		/>
	
	<html:form action="/JGR_MantenimientoJuzgados" method="POST" target="submitArea">
		<html:hidden property="codigoExt2" value="" />
		<html:hidden property="idJuzgado" value="" />
	</html:form>


<!-- INICIO: BOTONES REGISTRO -->
	<siga:ConjBotonesAccion botones="C,G" modal="M"/>
	<!-- FIN: BOTONES REGISTRO -->
	<iframe name="submitArea"
				src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
				style="display: none"></iframe>

<script>

</script>	
</body>

</html>