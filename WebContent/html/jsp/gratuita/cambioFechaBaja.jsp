<!DOCTYPE html>
<html>
<head>
<!-- cambioFechaBaja.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>

<!-- IMPORTS -->

<!-- JSP -->



<!-- HEAD -->

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
	<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>		
</head>

<body>

	<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulosPeq">
				<siga:Idioma key="gratuita.gestionInscripciones.editarFechaBaja.titulo" />			
			</td>
		</tr>
	</table>

	<!-- INICIO ******* CAPA DE PRESENTACION ****** -->
	<!-- dentro de esta capa se tienen que situar los diferentes componentes 
		 que se van a mostrar, para que quepen dentro de la ventana.
		 Los elementos que copieis dentro, que tengan el estilo 
		 "tablaTitulo" se deben modificar por "tablaCentralPeque" 
	-->

	<!-- INICIO: CAMPOS -->
	<!-- Zona de campos de busqueda o filtro -->

	<table class="tablaCentralCamposPeque" align="center">
		<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
		<bean:define id="inscripcionForm" name="InscripcionTGForm" type="com.siga.gratuita.form.InscripcionTGForm"/>
		
		<html:form action="${path}"  method="POST" target="submitArea">
			<html:hidden name="InscripcionTGForm" property="modo"  />
			<html:hidden name="InscripcionTGForm" property="idInstitucion" />
			<html:hidden name="InscripcionTGForm" property="idTurno" />
			<html:hidden name="InscripcionTGForm" property="idGuardia" />
			<html:hidden name="InscripcionTGForm" property="idPersona" />			
			<html:hidden name="InscripcionTGForm" property="fechaSolicitud" />
			<html:hidden name="InscripcionTGForm" property="validarInscripciones" />
			<html:hidden name="InscripcionTGForm" property="tipoGuardias" />	
			<html:hidden name="InscripcionTGForm" property="fechaBajaPrevia" />
			<html:hidden name="InscripcionTGForm" property="fechaValidacion" />					
		
			<!-- FILA -->
			<tr>
				<td>
					<fieldset>
						<table class="tablaCampos" align="center">
							<tr>
								<td class="labelText">
									<siga:Idioma key="gratuita.gestionInscripciones.fechaBaja" />(*)
								</td>
								<td>									
									<siga:Fecha nombreCampo="fechaBaja" valorInicial="${inscripcionForm.fechaBajaPrevia}"></siga:Fecha>
								</td>
							</tr>
							
							<tr>						
								<td class="labelText">
									Obs. Baja
								</td>
								<td> 
									<html:textarea name="InscripcionTGForm" property="observacionesValBaja"
										style="overflow-y:auto; overflow-x:hidden; width:350px; height:80px; resize:none;"
										onChange="cuenta(this,1024)" onkeydown="cuenta(this,1024);" 
										styleClass="box" readOnly="false"></html:textarea>
								</td>
							</tr>
							
						</table>
					</fieldset>
				</td>
			</tr>
		</html:form>
	</table>
	<!-- FIN: CAMPOS -->

	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- Aqui comienza la zona de botones de acciones -->

	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->
	<siga:ConjBotonesAccion botones="Y,R,C" modal="P" />
	<!-- FIN: BOTONES REGISTRO -->

	<!-- INICIO: SUBMIT AREA -->
	<!-- Obligatoria en todas las páginas-->
	<iframe name="submitArea" src="<html:rewrite page="/html/jsp/general/blank.jsp"/>" style="display:none"></iframe>
	<!-- FIN: SUBMIT AREA -->

	<!-- INICIO: SCRIPTS BOTONES -->
	<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
	<script language="JavaScript"> 
		// Asociada al boton GuardarCerrar
		function accionGuardarCerrar() {		
			sub();
			if (document.InscripcionTGForm.fechaBaja.value=="") {
				var msg="<siga:Idioma key="messages.servicios.fechaEfectivaObligatoria"/>";
				alert(msg);
				fin();
				return false;				
			}
				
			document.InscripcionTGForm.submit();
		}
		
		// Asociada al boton Cerrar
		function accionCerrar() {		
			top.cierraConParametros("NORMAL");
		}
		
		// Asociada al boton Restablecer
		function accionRestablecer() {		
			document.InscripcionTGForm.reset();
		}
		
		function refrescarLocal() {		
			fin();
		}
	</script>
</body>
</html>