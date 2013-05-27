<!-- abrirSolicitudDiligencia.jsp -->

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

<!-- IMPORTS -->
<%@ page import="com.siga.administracion.SIGAConstants,java.lang.*"%>
<%@ page import="com.atos.utils.UsrBean,java.util.*,com.siga.Utilidades.UtilidadesString"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>

<!-- JSP -->
<%
	String app = request.getContextPath();
	HttpSession ses = request.getSession();
	Properties src = (Properties) ses
			.getAttribute(SIGAConstants.STYLESHEET_REF);
	UsrBean userBean = ((UsrBean) ses.getAttribute(("USRBEAN")));

	String idInstitucion = userBean.getLocation();
	String idPersonaX = (String) request.getAttribute("idPersonaX");
	String idInstitucionX = (String) request
			.getAttribute("idInstitucionX");
	String idBoton = (String) request.getAttribute("idBoton");
	ArrayList aInstitucion = new ArrayList();
	aInstitucion.add(idInstitucion);

	ArrayList aMetodoSol = new ArrayList();
	aMetodoSol.add(3);
	String[] parametro = { userBean.getLocation(),
			userBean.getLocation() };

	String fechaSolicitud = UtilidadesBDAdm.getFechaBD("");
%>	

<html>

<!-- HEAD -->
<head>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
	<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
</head>

<body>

<!-- TITULO -->
	<!-- Barra de titulo actualizable desde los mantenimientos -->
	<table class="tablaTitulo" cellspacing="0" heigth="32">
		<tr>
			<td id="titulo" class="titulitosDatos">
				<siga:Idioma key="certificados.comunicacionydiligencia.literal.titulo"/>
			</td>
		</tr>
	</table>

	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<!-- Zona de campos de busqueda o filtro -->
	<table class="tablaCentralCamposPeque" align="center">
		<form id="DummyForm2" name="DummyForm2" action="<%=app%>/PYS_CompraPredefinida.do" method="POST"
			target="submitArea">
			<input type="hidden" name="modo" value="insertarDiligencia">
			<input type="hidden" name="idPersona" value="<%=idPersonaX%>">
			<input type="hidden" name="idInstitucion" value="<%=idInstitucionX%>">
			<input type="hidden" name="idBoton" value="<%=idBoton%>">
			<tr>
				<td>
					<siga:ConjCampos leyenda="certificados.solicitudes.literal.instituciones">
						<table class="tablaCampos" align="center">

							<tr>
								<td class="labelText"><siga:Idioma
										key="certificados.solicitudes.literal.institucionorigen" />&nbsp(*)
								</td>
								<td><siga:ComboBD nombre="idInstitucionOrigen"
										tipo="cmbColegiosAbreviados" obligatorio="true"
										elementoSel="<%=aInstitucion%>" clase="boxCombo" /></td>

							</tr>
							<tr>
								<td class="labelText"><siga:Idioma
										key="certificados.solicitudes.literal.institucionDestino2" />&nbsp(*)
								</td>
								<td><siga:ComboBD nombre="idInstitucionDestino"
										tipo="cmbColegiosAbreviados" obligatorio="true"
										clase="boxCombo" /></td>
							</tr>
						</table>
					</siga:ConjCampos> 
					<siga:ConjCampos>
						<table class="tablaCampos" align="center">
							<tr>
								<td class="labelText" width="30%"><siga:Idioma
										key="certificados.solicitudes.literal.fechaSolicitud" /></td>
								<td><siga:Fecha nombreCampo="fechaSolicitud"
										valorInicial="<%=fechaSolicitud%>" posicionX="10"
										posicionY="10"></siga:Fecha></td>
							</tr>
							<tr>
								<td class="labelText" width="120"><siga:Idioma
										key="certificados.solicitudes.literal.metodoSolicitud" /></td>
								<td><siga:ComboBD nombre="metodoSolicitud"
										tipo="comboMetodoSolicitud" obligatorio="false"
										parametro="<%=parametro%>" ElementoSel="<%=aMetodoSol%>"
										clase="boxCombo" /></td>
							</tr>
						</table>
					</siga:ConjCampos> 
					<siga:ConjCampos>
						<table class="tablaCampos" align="center">
							<tr>
								<td class="labelText" width="55"><siga:Idioma
										key="certificados.solicitudes.literal.descripcion" /></td>
								<td>
									<!-- input type="text" name="descripcion" class="box" size="55" maxlength="4000"-->
									<textarea COLS=80 ROWS=4 NAME="descripcion" class="box"
										onKeyDown="cuenta(this,4000)" onChange="cuenta(this,4000)"></textarea>
								</td>
							</tr>
						</table>
					</siga:ConjCampos>
				</td>
			</tr>
		</form>
	</table>

	<!-- FIN: CAMPOS DE BUSQUEDA-->


	<!-- INICIO: BOTONES REGISTRO -->
	<!-- Esto pinta los botones que le digamos. Ademas, tienen asociado cada
		 boton una funcion que abajo se reescribe. Los valores asociados separados por comas
		 son: G Guardar,Y GuardaryCerrar,R Restablecer,C Cerrar,X Cancelar,N Nuevo
		 LA PROPIEDAD CLASE SE CARGA CON EL ESTILO "botonesDetalle" 
		 PARA POSICIONARLA EN SU SITIO NATURAL, SI NO SE POSICIONA A MANO
		 La propiedad modal dice el tamanho de la ventana (M,P,G)
	-->
	<siga:ConjBotonesAccion botones="Y,C" modal="P"/>

	<!-- FIN: BOTONES REGISTRO -->


	<!-- INICIO: SCRIPTS BOTONES -->
	<script type="text/javascript">
		
		<!-- Asociada al boton Guardar y Cerrar-->
		function accionGuardarCerrar() {			
		        sub();			
		        var formu = document.getElementById("DummyForm2");
				var msg1 = "<siga:Idioma key="certificados.solicitudes.literal.faltaOrigen"/>";
				var msg2 = "<siga:Idioma key="certificados.solicitudes.literal.faltaDestino"/>";
				if (formu.idInstitucionOrigen.value==''){
					fin();
					alert(msg1);
				} else 
				if (formu.idInstitucionDestino.value==''){
					fin();				
					alert(msg2);
				} else {
					var a = new Array;
					a[0]=formu.idInstitucionOrigen.value;
					a[1]=formu.idInstitucionDestino.value;
					a[2]=formu.descripcion.value;
					a[3]=formu.fechaSolicitud.value;
					a[4]=formu.metodoSolicitud.value;
					top.cierraConParametros(a);			
				} 
		}
		<!-- Asociada al boton Cerrar -->
		function accionCerrar() {			
			top.cierraConParametros("");
		}
	</script>
	<!-- FIN: SCRIPTS BOTONES BUSQUEDA -->

	<!-- FIN  ******* BOTONES Y CAMPOS DE BUSQUEDA ****** -->

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las p�ginas-->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->
</body>
</html>
