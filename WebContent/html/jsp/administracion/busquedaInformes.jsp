<!-- busquedaInformes.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" 	prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" 		prefix="bean"%>
<%@ taglib uri = "struts-html.tld" 		prefix="html"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- IMPORTS -->
<!-- JSP -->

<html>

<!-- HEAD -->
<head>
	
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>" />
	<link rel="stylesheet" href="<html:rewrite page="/html/js/themes/base/jquery.ui.all.css"/>" />
		
	<script type="text/javascript" src="<html:rewrite page="/html/js/jquery-1.7.1.js"/>" ></script>
	<script src="<html:rewrite page="/html/js/SIGA.js"/>" type="text/javascript"></script>
	
	<siga:Titulo titulo="menu.administracion.informes" 	localizacion="menu.administracion"  />
	<!-- FIN: TITULO Y LOCALIZACION -->
	
</head>


<body onLoad="ajusteAlto('resultado');">


	<!-- ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->

	<html:form action="/ADM_GestionInformes" method="POST" target="resultado">
		<html:hidden property = "modo" value = "buscar"/>
		<html:hidden property = "actionModal" value = ""/>
		<table class="tablaCentralCampos" align="center">
			<tr>
				<td>
				
					<siga:ConjCampos leyenda="general.criterios">	
						<table class="tablaCampos" border="0" align="left">
							<tr>				
								<td class="labelText" width="100px">
									<siga:Idioma key="administracion.informes.literal.tipoInforme"/>
								</td>	
								<td class="labelText" colspan="3">
									<html:select styleClass="boxCombo" style="width:320px;"
										property="idTipoInforme" >
										<bean:define id="tiposInforme" name="InformeForm"
											property="tiposInforme" type="java.util.Collection" />
											<html:optionsCollection name="tiposInforme" value="idTipoInforme"
													label="descripcion" />
									</html:select>
								</td>
								<td class="labelText" >
									<siga:Idioma key="administracion.informes.literal.colegio"/>
								</td>
								<td class="labelText" colspan="2">
									<html:select styleClass="boxCombo" style="width:320px;"
										property="idInstitucion" >
										<bean:define id="instituciones" name="InformeForm"
											property="instituciones" type="java.util.Collection" />
											<html:optionsCollection name="instituciones" value="idInstitucion"
													label="abreviatura" />
									</html:select>
								</td>
							</tr>
							<tr>	
								<td class="labelText">
								<siga:Idioma key="administracion.informes.literal.nombre"/>
								</td>
								<td class="labelText" colspan="3">
									<html:text name="InformeForm" style="width:320px;" property="alias" size="50" styleClass="box"></html:text>
								</td>
								<td class="labelText">&nbsp;</td>
								<td class="labelText">&nbsp;</td>
								<td class="labelText">&nbsp;</td>
							</tr>
							<tr>
								<td class="labelText">
									<siga:Idioma key="administracion.informes.literal.visible"/>
								</td>
								<td class="labelText" >
									<html:select property="visible"  name="InformeForm" styleClass="boxCombo" style="width:60px;">
										<html:option value="">&nbsp;</html:option>
										<html:option value="S"><siga:Idioma key="general.yes"/></html:option>
										<html:option value="N"><siga:Idioma key="general.no"/></html:option>
									</html:select>							
								</td>
								<td class="labelText">
									<siga:Idioma key="administracion.informes.literal.destinatarios.enviarA"/>
								</td>
								<td class="labelText">
									<html:select property="destinatarios"  name="InformeForm" styleClass="boxCombo" >
										<html:option value="">&nbsp;</html:option>
										<html:option value="C"><siga:Idioma key="administracion.informes.destinatarios.colegiados"/></html:option>
										<html:option value="S"><siga:Idioma key="administracion.informes.destinatarios.solicitantes"/></html:option>
										<html:option value="P"><siga:Idioma key="administracion.informes.destinatarios.procurador"/></html:option>
										<html:option value="J"><siga:Idioma key="administracion.informes.destinatarios.juzgado"/></html:option>
										<html:option value="X"><siga:Idioma key="administracion.informes.destinatarios.contrarios"/></html:option>
									</html:select>		
								</td>		
								<td class="labelText" colspan="2">
									<siga:Idioma key="administracion.informes.literal.solicitantes"/>&nbsp;
								</td>
								<td class="labelText">
									<html:select property="ASolicitantes"  name="InformeForm" styleClass="boxCombo" style="width:60px;">
										<html:option value="">&nbsp;</html:option>
										<html:option value="S"><siga:Idioma key="general.yes"/></html:option>
										<html:option value="N"><siga:Idioma key="general.no"/></html:option>
									</html:select>		
								</td>								
							</tr>	
						</table>
					</siga:ConjCampos>	
				</td>
			</tr>
		</table>
	
	</html:form>

	<!-- FIN: CAMPOS DE BUSQUEDA-->

	<!-- INICIO: BOTONES BUSQUEDA -->
	<!-- Esto pinta los botones que le digamos de busqueda. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: V Volver, B Buscar,A Avanzada ,S Simple,N Nuevo registro ,L Limpiar,R Borrar Log
	-->
		
		<siga:ConjBotonesBusqueda botones="B,N" />

	<!-- FIN: BOTONES BUSQUEDA -->

	
	<!-- INICIO: SCRIPTS BOTONES BUSQUEDA -->
	<script language="JavaScript">
		function refrescarLocal() 
		{
				buscar();
		}
		<!-- Funcion asociada a boton buscar -->
		function buscar() 
		{
				sub();		
				document.InformeForm.modo.value = "buscar";
				document.InformeForm.submit();
		}
		
		<!-- Funcion asociada a boton nuevo -->
		function nuevo() 
		{		
			document.InformeForm.modo.value = "nuevo";
			var resultado = ventaModalGeneral(document.InformeForm.name,"G");
			buscar();
			
		}
		
	</script>
	
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- INICIO: IFRAME LISTA RESULTADOS -->
	<iframe align="center" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
					id="resultado"
					name="resultado" 
					scrolling="no"
					frameborder="0"
					marginheight="0"
					marginwidth="0";					 
					class="frameGeneral">
	</iframe>

			
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"	style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->

</body>
</html>