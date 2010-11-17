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

<html>

<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css"
	href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<script src="<html:rewrite page='/html/js/SIGA.js'/>"
	type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"
	type="text/javascript"></script>
<script
	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/prototype.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/scriptaculous/scriptaculous.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/overlibmws/overlibmws.js'/>"></script>
<script type="text/javascript" src="<html:rewrite page='/html/js/ajaxtags.js'/>"></script>
<link type="text/css" rel="stylesheet" href="/html/css/ajaxtags.css" />
  <link type="text/css" rel="stylesheet" href="/html/css/displaytag.css" />

<script>
				
	function accionCerrar() 
	{		
		top.cierraConParametros("NORMAL");
	}		
	function accionGuardar() 
	{
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
 
</script>
</head>

<body onload=onload();>

<table class="tablaTitulo" cellspacing="0" heigth="38">
	<tr>
		<td>
			Actualizar Designa
		</td>
	</tr>
	</table>


<!-- INICIO: CAMPOS DE BUSQUEDA-->
<html:form action="/JGR_MantenimientoDesignas" method="POST" target="submitArea">
	<html:hidden name="MaestroDesignasForm" property="modo" value="" />
	<html:hidden name="MaestroDesignasForm" property="anio"  />
	<html:hidden name="MaestroDesignasForm" property="numero" />
	<html:hidden name="MaestroDesignasForm" property="idTurno"/>
	<html:hidden name="MaestroDesignasForm" property="procedimiento"/>
	
	
	
	
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
			</siga:ConjCampos> <siga:ConjCampos leyenda="gratuita.busquedaDesignas.literal.designa">
				<table class="tablaCampos" align="center" cellpadding="0"
					cellpadding="0" width="100%" border="0">

					<tr>

						
								<tr>

									<td class="labelText" width="10%"><siga:Idioma
										key="gratuita.mantenimientoTablasMaestra.literal.juzgado" />
									</td>
									<td class="labelText" width="10%">
										<input type="text"
										name="codigoExtJuzgado" styleId="codigoExtJuzgado" class="box" size="8" maxlength="10"/>
										&nbsp;</td>
									<td>&nbsp;</td>
									<td width="80%">
										<html:select styleId="juzgados" styleClass="boxCombo" style="width:500px;"
											property="idJuzgado">
											<bean:define id="juzgados" name="MaestroDesignasForm"
												property="juzgados" type="java.util.Collection" />
													<html:optionsCollection name="juzgados" value="idJuzgado"
														label="nombre" />
										</html:select>
									</td>
						
					</tr>
					<tr>
						<td class="labelText"><siga:Idioma
							key="gratuita.actuacionesDesigna.literal.modulo" /></td>
						<td colspan= "3">
						<html:select styleId="modulos" styleClass="boxCombo" style="width:500px;"
								property="idProcedimiento" >
								<bean:define id="modulos" name="MaestroDesignasForm"
									property="modulos" type="java.util.Collection" />
									<html:optionsCollection name="modulos" value="idProcedimiento"
										label="nombre" />
								</html:select>
						
						</td>
					</tr>
				</table>
			</siga:ConjCampos></td>
		</tr>
	</table>
<ajax:select
	baseUrl="/SIGA/JGR_MantenimientoDesignas.do?modo=getAjaxModulos"
	source="juzgados" target="modulos" parameters="idJuzgado={idJuzgado}"
	postFunction="postAccionJuzgados"
	/>
<ajax:updateFieldFromField 
	baseUrl="/SIGA/JGR_MantenimientoJuzgados.do?modo=getAjaxJuzgado"
    source="codigoExtJuzgado" target="idJuzgado"
	parameters="codigoExtJuzgado={codigoExtJuzgado}" postFunction="postAccionCodigoExtJuzgado"/>
</html:form>

<html:form action="/JGR_MantenimientoJuzgados" method="POST"
		target="submitArea">
		<html:hidden property="codigoExt" value="" />
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