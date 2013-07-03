<!-- condicionSuscripcionAutomatica.jsp -->

<!-- 
	 Permite mostrar/editar datos sobre lols precios asociados a los servicios
	 VERSIONES:
	 miguel.villegas 7-2-2005 
-->
  
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.lang.*"%>

<!-- JSP -->
<%
	HttpSession ses=request.getSession();
		
	String modo = request.getAttribute("modelo").toString(); 
	String comprobarCondicion = request.getAttribute("checkAutomatico").toString(); 

	String idInstitucion         = request.getAttribute("idInstitucion").toString();
	String idTipoServicio        = request.getAttribute("idTipoServicio").toString();
	String idServicio            = request.getAttribute("idServicio").toString();
	String idServicioInstitucion = request.getAttribute("idServicioInstitucion").toString();
	
	if (idInstitucion  == null) idInstitucion = "";
	if (idTipoServicio == null) idTipoServicio = "";
	if (idServicio     == null) idServicio = "";
	if (idServicioInstitucion == null) idServicioInstitucion = "";

  String resultado = (String)request.getAttribute("resultado");
  if (resultado == null) resultado = "";

	String botonNuevo = "N";
/*	String porDefecto = (String)request.getAttribute("porDefecto");
	boolean queryPorDefecto = false;
	if ((porDefecto!=null)&&(porDefecto.equalsIgnoreCase("1"))){
		botonNuevo = "";
		queryPorDefecto = true;
	}
*/

/*
	// Obtener informacion para rellenar en caso de modificacion o consulta
	if ((modo.equalsIgnoreCase("modificar"))||(modo.equalsIgnoreCase("consulta"))){
		Enumeration enumTemp = ((Vector)request.getAttribute("container")).elements();
		// Entrada a mostrar o modificar
		if (enumTemp.hasMoreElements()){
    }
	}
*/
	String clase = "box";
	String	botones = "Y,R,C";
	if (modo.toLowerCase().endsWith("consulta")){
		clase = "boxConsulta";
		botonNuevo = "";
		botones = "C";
	}
%>	

<html>
	<!-- HEAD -->
	<head>
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
		<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
		
		<script language="JavaScript">		
			function cambiarTipo(obj){
				document.forms[0].modo.value="abrirAvanzada";
				document.forms[0].target="frameOperadorValor";
				document.forms[0].submit();
			}			
		</script>		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="MantenimientoServiciosForm" staticJavascript="false" />  
		<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->	
	</head>

	<body onLoad="ajusteAltoBotones('frameResultado');">

		<!-- TITULO -->
		<!-- Barra de titulo actualizable desde los mantenimientos -->
		<table class="tablaTitulo" cellspacing="0" heigth="32">
			<tr>
				<td id="titulo" class="titulitosDatos">
					<siga:Idioma key="pys.mantenimientoServicios.CondicionSuscripcionAutomatica.literal.titulo"/>
				</td>
			</tr>
		</table>
	
		<table  class="tablaCentralCamposGrande"  align="center" border="0">
			<tr>
				<td>
					<html:form action="/PYS_MantenimientoServicios.do" method="POST" target="submitArea">							
						<html:hidden property="modo"                   value= ""/>
						<html:hidden property="criterios"              value= ""/>
						<html:hidden property="idInstitucion"          value="<%=idInstitucion%>"/>
						<html:hidden property="idTipoServicios"        value="<%=idTipoServicio%>"/>
						<html:hidden property="idServicio"             value="<%=idServicio%>"/>
						<html:hidden property="idServiciosInstitucion" value="<%=idServicioInstitucion%>"/>

						<%if (!modo.toLowerCase().endsWith("consulta")) { %>
							<siga:ConjCampos leyenda="pys.mantenimientoServicios.CondicionSuscripcionAutomatica.leyenda">
								<table class="tablaCampos" border="0">
									<tr>				
										<td class="labelText">
											<siga:Idioma key="pys.mantenimientoServicios.CondicionSuscripcionAutomatica.literal.conector"/>
										</td>
										<td class="labelText">											
											<% // if ((modo.equalsIgnoreCase("consulta"))||(queryPorDefecto)){
												if (modo.equalsIgnoreCase("consulta")){	
											%>
												<html:text property="conector" styleClass="boxConsulta" size="10" value="" readOnly="true" />
												
											<% } else { %>
									  			<select name="conector" id="conector" class="boxCombo">
													<option value=""></option>
													<option value="Y">Y</option>
													<option value="O">O</option>
												</select>
											<%}%>
										</td>
										
										<td class="labelText">
											<siga:Idioma key="pys.mantenimientoServicios.CondicionSuscripcionAutomatica.literal.campo"/>
										</td>
										<td class="labelText">				
									  		<% // if ((modo.equalsIgnoreCase("consulta"))||(queryPorDefecto)){
									  			if (modo.equalsIgnoreCase("consulta")){
									  		%>
												<html:text property="conector" styleClass="boxConsulta" size="10" value="" readOnly="true" />
												
											<% } else { %>
												<siga:ComboBD nombre="campo" tipo="cmbCamposConsulta" clase="boxCombo" obligatorio="false" accion="cambiarTipo(this)" />
									  		<% } %>
										</td>
										
										<td>
											<iframe src="<html:rewrite page='/html/jsp/productos/criteriosClienteFrame.jsp'/>"
												id="frameOperadorValor"
												name="frameOperadorValor" 
												scrolling="no"
												frameborder="0"
												marginheight="0"
												marginwidth="0"					 
												style="width:480px; height:30px; z-index:2; left: 0px">
											</iframe>
										</td>
									</tr>
								</table>	
							</siga:ConjCampos>
						<% } %>
					</html:form>
				</td>
			</tr>	
		</table>

		<siga:ConjBotonesAccion botones='<%=botonNuevo%>' modo='<%=modo%>'  modal="G" clase="botonesSeguido"/>

		<iframe src="/SIGA/html/jsp/productos/definirCriteriosCliente.jsp?resultado='<%=resultado%>'"
			id="frameResultado"
			name="frameResultado"
			scrolling="no"
			frameborder="0"
			marginheight="0"
			marginwidth="0"
			class="frameGeneral"
			style="height:480;">
		</iframe>

		<siga:ConjBotonesAccion botones='<%=botones%>' modo='<%=modo%>'  modal="G"/>
		<!-- FIN: CAMPOS -->

		<!-- INICIO: SCRIPTS BOTONES -->
		<!-- Aqui se reescriben las funciones que vayamos a utilizar -->
		<script language="JavaScript">
			var global ="<%=resultado%>";

			function incluirParentesis() {
				var mensaje='<siga:Idioma key="messages.pys.consulta.parentesisIncorrectos"/>';
				var checkAbrir=document.frameResultado.document.getElementsByName("chkParentesisAbrir");
				var checkCerrar=document.frameResultado.document.getElementsByName("chkParentesisCerrar");
				var todos = global;
				var nuevo = "";
				var uno="";
				var contador=0;
				var validador=0;
				var indice=1;
				var final=0;
				while (todos.length>0) {
					final = todos.indexOf("*",indice);
					
					if (final!=-1) {
						uno = todos.substring(indice,final);
						todos = todos.substring(final,todos.length);
						
					} else {
						uno = todos.substring(1,todos.length);
						todos = "";
					}
					
					//tratamiento de uno
					var abrir ="0";
					var cerrar="0";
					if (checkAbrir[contador].checked) {
						abrir="1";
						validador=validador+1;
					}
					
					if (checkCerrar[contador].checked) {
						cerrar="1";
						validador=validador-1;
						if (validador<0) {
							alert(mensaje);
							return false;
						}
					}
					
					nuevo = nuevo + "*" + uno.substring(0,uno.length-4) + abrir + "_" + cerrar + "_";

					contador = contador + 1;
				}
				global = nuevo;
				
				if (validador!=0) {
					alert(mensaje);
					return false;
				}
				return true;
			}
			
			// Asociada al boton GuardarYCerrar
			function accionGuardarCerrar() {			
				  sub();
					if (!existeCondicion()){
						fin();
					 	return false;
					 }		
					
					// tratamiento de global para meter el valor de los parentesis y ademas validar.
					if (!incluirParentesis()) {
						fin();
						return false;
					}
					
					document.forms[0].criterios.value=global;		
					document.forms[0].target="submitArea";	
					document.forms[0].modo.value="insertarCondicionSuscripcionAutomatica";
					document.forms[0].submit();
			}
	
			// Asociada al boton Restablecer 
			function accionRestablecer() {
				document.forms[0].reset();
			}
	
			// Asociada al boton Nuevo
			function accionNuevo() {				
				if (document.forms[0].campo.selectedIndex>0){
					if ((global!="")&&(document.forms[0].conector.selectedIndex==0)){
						
						alert('<siga:Idioma key="pys.mantenimientoServicios.literal.conector"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
					} else {
					
						var operador =  window.frames["frameOperadorValor"].document.all.item("operador");
						var operadorT = operador[operador.selectedIndex].text;
						var operadorV = operador[operador.selectedIndex].value;
						var valor;
						
						if (window.frames["frameOperadorValor"].document.all.item("valor")) {
							window.frames["frameOperadorValor"].document.all.item("valor").value =  window.frames["frameOperadorValor"].document.all.item("valor").value.replace(/,/,".");
							valor =  window.frames["frameOperadorValor"].document.all.item("valor");
						} else {
							window.frames["frameOperadorValor"].document.all.item("caja").value =  window.frames["frameOperadorValor"].document.all.item("caja").value.replace(/,/,".");
							valor =  window.frames["frameOperadorValor"].document.all.item("caja");
						}
							
						//valor.value = valor.value.replace(/,/,".");
						if (valor.tagName=="SELECT"){
				  			valorT = valor.options[valor.selectedIndex].text;
				  			valorV = valor.options[valor.selectedIndex].value;	
					  	}else{
					  		valortrim = trim(valor.value);
					  		valorT = valortrim;
					  		valorV = valortrim;
					  	}		  	
				    
						var conect = document.forms[0].conector[document.forms[0].conector.selectedIndex].value;
						var campoV = document.forms[0].campo[document.forms[0].campo.selectedIndex].value;
						var campoT = document.forms[0].campo[document.forms[0].campo.selectedIndex].text;
						global = global + "*" + conect + "_" + campoT + "_" + operadorT + "_" + valorT + "_" + campoV + "_" +  operadorV + "_" +valorV+"_0_0_";
						
						global = global.replace("#","$")
						document.frameResultado.location.href="/SIGA/html/jsp/productos/definirCriteriosCliente.jsp?resultado=\""+global+"\"";
					}
					
				} else {
					alert('<siga:Idioma key="pys.mantenimientoServicios.literal.campo"/> <siga:Idioma key="messages.campoObligatorio.error"/>');
				}
			}
			
			function borrarFila(fila){

				var auxiliar = global;
				var auxiliarResultado = "";
				var contadorCriterio = 1;
				var seguir = true;
				while (seguir==true){ 
					if (contadorCriterio!=fila) {
						auxiliar = auxiliar.substring(1,auxiliar.length);
						if (auxiliar.indexOf("*")>0)auxiliarResultado = auxiliarResultado + "*" + auxiliar.substring(0,auxiliar.indexOf("*"));
						else auxiliarResultado = auxiliarResultado + "*" + auxiliar.substring(0,auxiliar.length);
					}
					auxiliar = auxiliar.substring(1,auxiliar.length);
					seguir = auxiliar.indexOf("*") > 0;
					if (seguir==true) 
						auxiliar = auxiliar.substring(auxiliar.indexOf("*"),auxiliar.length);
					contadorCriterio++;
				}
				global = auxiliarResultado;
				document.frameResultado.location.href="/SIGA/html/jsp/productos/definirCriteriosCliente.jsp?resultado=\""+global+"\"";

			}
			
			// Asociada al boton Cerrar 
			function accionCerrar() {		
				// esta funcion cierra la ventana y devuelve 
				// un valor a la ventana padre (USAR SIEMPRE)
				top.cierraConParametros("MODICADO");
			}			
			
			function existeCondicion () {
				
				var s = "<%=comprobarCondicion%>";
				
				if(s == "true") {
										
					if (frameResultado.tablaDatos[1].rows.length > 0) { //Se comprueba si en la fila de suscripciones alguna fila con una condicion
						return true;
					} else {
						alert ("<siga:Idioma key="pys.mantenimientoServicios.CondicionSuscripcionAutomatica.mensaje.errorCondicionNecesaria"/>");
						return false;
					}
				}
				return true;
			}
			
		</script>
		<!-- FIN: SCRIPTS BOTONES -->
				
		<!-- FIN ******* CAPA DE PRESENTACION ****** -->
			
		<!-- INICIO: SUBMIT AREA -->
		<!-- Obligatoria en todas las páginas-->
		<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none"></iframe>
		<!-- FIN: SUBMIT AREA -->
	</body>
</html>