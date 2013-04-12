<!-- criteriosCliente.jsp -->
  
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<%@ page pageEncoding="ISO-8859-1"%>
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
<%@ page import="com.siga.Utilidades.UtilidadesString"%>
<%@ page import="java.lang.*"%>
<%@ page import="com.siga.Utilidades.UtilidadesNumero"%>

<!-- JSP -->
<%
	HttpSession ses=request.getSession();
	Properties src=(Properties)ses.getAttribute(SIGAConstants.STYLESHEET_REF);	
	String modo = request.getAttribute("modelo").toString(); // Obtengo la operacion (consulta,modificar o insertar)a realizar

	ArrayList vPeriodicidad = new ArrayList(); // 
    Row row = new Row(); // 
    PysPreciosServiciosBean beanPrecio = new PysPreciosServiciosBean(); // Para almacenar el bean obtenido

	String institucion=request.getAttribute("institucion").toString();
	String tipoServicio=request.getAttribute("tipoServicio").toString();
	String servicio=request.getAttribute("servicio").toString();
	String servicioInstitucion=request.getAttribute("servicioInstitucion").toString();
    String resultado = (String)request.getAttribute("resultado");
	String porDefecto = (String)request.getAttribute("porDefecto");
	String descripcion = (String)request.getAttribute("descripcion");
	boolean queryPorDefecto = false;
	String botonNuevo="N";
	if ((porDefecto!=null)&&(porDefecto.equalsIgnoreCase("1"))){
		botonNuevo ="";
		queryPorDefecto = true;
	}

    if (resultado==null)resultado="";
	
   	//Precio
	double precio = 0.00;
	String sPrecio = null;
	String sDescripcion = null;

	// Obtener informacion para rellenar en caso de modificacion o consulta
	if ((modo.equalsIgnoreCase("modificar"))||(modo.equalsIgnoreCase("consulta"))){
		Enumeration enumTemp = ((Vector)request.getAttribute("container")).elements();
		// Entrada a mostrar o modificar
		if (enumTemp.hasMoreElements()){
          	beanPrecio = (PysPreciosServiciosBean) enumTemp.nextElement(); 			              	
        }
		// periodicidad del servicio
        vPeriodicidad.add(beanPrecio.getIdPeriodicidad().toString());
        
        sDescripcion = beanPrecio.getDescripcion().toString();
		
       	//Precio
		sPrecio = beanPrecio.getValor().toString();
		try { 
			if (sPrecio!=null)
				precio = Double.parseDouble(sPrecio);
		} catch(NumberFormatException e){
			precio = 0.00;
		}	
	}

	String clase = "box";
	if (modo.equalsIgnoreCase("consulta")){
		clase = "boxConsulta";
	}

	String	botones="Y,R,C";
	
	ses.setAttribute("AUX_servicio",            servicio);
	ses.setAttribute("AUX_tipoServicio",        tipoServicio);
	ses.setAttribute("AUX_institucion",         institucion);
	ses.setAttribute("AUX_servicioInstitucion", servicioInstitucion);
%>	


<html>
	<!-- HEAD -->
	<head>
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>">
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.custom.js'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>	
		<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>"></script>
		
		<script language="JavaScript">
				
			function cambiarTipo(obj) {
				document.forms[0].modo.value="abrirAvanzada";
				document.forms[0].target="frameOperadorValor";
				document.forms[0].submit();
			}

			function validacionPosterior(){
				var envio=true;	
				var i;
				var mensaje="";

				if (document.forms[0].precio.value==""){
					mensaje+='<siga:Idioma key="pys.mantenimientoServicios.literal.precio"/> <siga:Idioma key="messages.campoObligatorio.error"/>\n';
					envio=false;
				}
				
				if ((document.forms[0].precio.value<0) || (document.forms[0].precio.value>99999999) || (isNaN(document.forms[0].precio.value))){
					mensaje+='<siga:Idioma key="messages.pys.mantenimientoProductos.errorPrecio"/>\n';
					envio=false;
				}
				
				//Comprobar periodicidad
				if (document.forms[0].periodicidad.value==""){
					mensaje+='<siga:Idioma key="pys.mantenimientoServicios.literal.periodicidad"/> <siga:Idioma key="messages.campoObligatorio.error"/>';
					envio=false;
				}								
				
				if (!envio){
					alert(mensaje);
				}
				
				return envio;				
			}	

			function revisarCheckPrecioDefecto()  {
				if(document.getElementById("precioDefecto").checked) {
					// borro los criterios
					global = "";
					document.frameResultado.location.href="/SIGA/html/jsp/productos/definirCriteriosCliente.jsp?resultado=\""+global+"\"";
					// inhabilito los combos
					jQuery("#conector").attr("disabled","disabled");
					document.getElementById("conector").className="boxConsulta"
						document.getElementById("conector").value="";
					jQuery("#campo").attr("disabled","disabled");
					document.getElementById("campo").className="boxConsulta"
					document.getElementById("campo").value="";
					jQuery("#idButton").attr("disabled","disabled");
					document.frameOperadorValor.location.href="/SIGA/html/jsp/productos/criteriosClienteFrame.jsp";

				} else {
					// habilito los combos
					jQuery("#conector").removeAttr("disabled");
					document.getElementById("conector").className="boxCombo"
					document.getElementById("conector").value="";
					jQuery("#campo").removeAttr("disabled");
					document.getElementById("campo").className="boxCombo"
					document.getElementById("campo").value="";
					jQuery("#idButton").removeAttr("disabled");
					document.frameOperadorValor.location.href="/SIGA/html/jsp/productos/criteriosClienteFrame.jsp";				
				}
			}			
		</script>		

		<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
		<!-- Validaciones en Cliente -->
		<!-- El nombre del formulario se obtiene del struts-config -->
		<html:javascript formName="MantenimientoServiciosForm" staticJavascript="false" />  	
	</head>

	<body onLoad="ajusteAlto('frameResultado');" onBeforeUnLoad="accionCerrar();">

			<!-- TITULO -->
			<!-- Barra de titulo actualizable desde los mantenimientos -->
			<table class="tablaTitulo">
				<tr>
					<td id="titulo" class="titulitosDatos">
						<siga:Idioma key="pys.mantenimientoServicios.literal.titulo1"/>
					</td>
				</tr>
			</table>

			<div>
				<html:form action="/PYS_MantenimientoServicios.do" method="POST" target="submitArea">
					<html:hidden property = "modo" value = ""/>
					<html:hidden property="idInstitucion" value="<%=institucion%>"/>
					<html:hidden property="idTipoServicios" value="<%=tipoServicio%>"/>
					<html:hidden property="idServicio" value="<%=servicio%>"/>
					<html:hidden property="idServiciosInstitucion" value="<%=servicioInstitucion%>"/>
					<html:hidden property="criterios" value=""/>

					<siga:ConjCampos leyenda="pys.mantenimientoServicios.leyenda">
						<table class="tablaCampos">
							<!-- FILA -->
							<tr>				
								<td class="labelText">
									<siga:Idioma key="pys.mantenimientoServicios.literal.precio"/>&nbsp;(*)
								</td>				
								<td class="labelText">
									<% if (modo.equalsIgnoreCase("insertar")){%>
							  			<html:text property="precio" styleClass="boxNumber" maxlength="11" size="10" value="" />&nbsp;&euro;
							  			
									<% } else { %>
										<% if (modo.equalsIgnoreCase("modificar")){ %>
											<html:text property="precio" styleClass="boxNumber" size="10" maxlength="11" value="<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(precio))%>" />&nbsp;&euro;
											
										<% } else { %>
											<html:text property="precio" styleClass="boxConsulta" size="10" value="<%=UtilidadesString.mostrarDatoJSP(UtilidadesNumero.formatoCampo(precio))%>" readOnly="true" />&nbsp;&euro;
										<% } %>								  		
									<% } %>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="pys.mantenimientoServicios.literal.periodicidad"/>&nbsp;(*)
								</td>
								<td class="labelText">
									<% if (modo.equalsIgnoreCase("insertar")){%>
										<siga:ComboBD nombre = "periodicidad" tipo="cmbPeriodicidad" clase="boxCombo" obligatorio="true"/>
										
									<% } else { %>
										<% if (modo.equalsIgnoreCase("modificar")){ %>
											<siga:ComboBD nombre = "periodicidad" tipo="cmbPeriodicidad" clase="boxConsulta" obligatorio="true" elementoSel="<%=vPeriodicidad%>" readOnly="true"/>
											
										<% } else { %>
											<siga:ComboBD nombre = "periodicidad" tipo="cmbPeriodicidad" clase="boxConsulta" obligatorio="true" elementoSel="<%=vPeriodicidad%>" readOnly="true"/>
										<% } %>								  		
									<% } %>
								</td>
								
								<td class="labelText">
									<siga:Idioma key="pys.mantenimientoCategorias.literal.descripcion"/>
								</td>				
								<td class="labelText">
									<% if (modo.equalsIgnoreCase("insertar")){%>
										<html:text property="descripcion" styleClass="box" maxlength="100" size="29" value="" />
										
									<% } else { %>
										<% if (modo.equalsIgnoreCase("modificar")){ %> 
											 <% if ("&nbsp".equals(UtilidadesString.mostrarDatoJSP(sDescripcion))) {%>
												<html:text property="descripcion" styleClass="box" size="29" maxlength="100" value="" />
												
											<% } else { %>
												<html:text property="descripcion" styleClass="box" size="29" maxlength="100" value="<%=sDescripcion%>" />
											<% } %>	
										
										<% } else { %>
											<html:text property="descripcion" styleClass="boxConsulta" size="29" value="<%=sDescripcion%>" readOnly="true" />
										<% } %>								  		
									<% } %>
								</td>	
											
								<td class="labelText">
									<siga:Idioma key="productos.mantenimientoProductos.literal.precioDefecto"/>
								</td>
								<td class="labelText">
									<% if (modo.equalsIgnoreCase("insertar")) {	%>
										<input type="checkbox" name="precioDefecto" value="1" onClick="revisarCheckPrecioDefecto();" >
										
									<% } else { %>
										<% if (queryPorDefecto) {%>
											<input type="checkbox" name="precioDefecto" value="1" onClick="revisarCheckPrecioDefecto();" checked disabled>
											
										<% } else { %>
											<input type="checkbox" name="precioDefecto" value="1" disabled>
										<% } %>
									<% } %>
								</td>																																	
							</tr>
							
							<!-- FILA -->
							<%if (!(modo.equalsIgnoreCase("consulta"))&&!(queryPorDefecto)){%>
								<tr>
									<td colspan="8">																	 		
										<table width="100%"> 
											<tr>
												<td class="labelText">
													<siga:Idioma key="pys.mantenimientoServicios.literal.conector"/>
												</td>
												<td class="labelText">
													<select name = "conector" class="boxCombo" id="conector">
											  			<option value=""></option>
														<option value="Y">Y</option>
														<option value="O">O</option>
													</select>
												</td>
												
												<td class="labelText">														
													<siga:Idioma key="pys.mantenimientoServicios.literal.campo"/>
												</td>
												<td class="labelText">
													<siga:ComboBD nombre="campo" tipo="cmbCamposConsulta" clase="boxCombo" obligatorio="false" accion="cambiarTipo(this)"/>
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
									</td>
								</tr>
							<%}%>
						</table>	
					</siga:ConjCampos>
				</html:form>
			</div>

			<siga:ConjBotonesAccion clase="botonesSeguido" botones='<%=botonNuevo%>' modo='<%=modo%>'  modal="G"/>			
					
			<iframe src="/SIGA/html/jsp/productos/definirCriteriosCliente.jsp?resultado='<%=resultado%>'"
				id="frameResultado"
				name="frameResultado"
				scrolling="no"
				frameborder="0"
				marginheight="0"
				marginwidth="0"
				class="frameGeneral"
				style="height:480;"
				>
			</iframe>

			<siga:ConjBotonesAccion botones='<%=botones%>' modo='<%=modo%>'  modal="G" clase="botonesDetalle"/>

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
			
				// Asociada al boton Guardar
				function accionGuardarCerrar() {					
					sub();
					document.forms[0].precio.value=document.forms[0].precio.value.replace(/,/,".");
					
					// tratamiento de global para meter el valor de los parentesis y ademas validar.
					if (!incluirParentesis()) {
						fin();
						return false;
					}
					
					document.forms[0].criterios.value=global;		
					document.forms[0].target="submitArea";	
					if (validacionPosterior()){							
						<% if (modo.equalsIgnoreCase("modificar")){ %>
							document.forms[0].modo.value="modificarCriterio";
							
						<% } else { %>
							if(document.forms[0].precioDefecto.checked) {
								document.forms[0].modo.value="insertarCriterioConPrecioPorDefecto";
								
							} else {
								document.forms[0].modo.value="insertarCriterio";
							}
						<% } %>	
						document.forms[0].submit();
						
					} else {
						fin();
						return false;
					}		
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
	
							 window.frames["frameOperadorValor"].document.all.item("valor").value =  window.frames["frameOperadorValor"].document.all.item("valor").value.replace(/,/,".");
							var valor =  window.frames["frameOperadorValor"].document.all.item("valor");
							//valor.value = valor.value.replace(/,/,".");
							
							if (valor.tagName=="SELECT"){
					  			valorT = valor.options[valor.selectedIndex].text;
					  			valorV = valor.options[valor.selectedIndex].value;
					  			
						  	} else {
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
				
				function borrarFila(fila) {
					var auxiliar = global;
					var auxiliarResultado = "";
					var contadorCriterio = 1;
					var seguir = true;
					
					while (seguir==true) { 
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
					document.forms[0].modo.value="cerrarCriterio";
					document.forms[0].submit();
					window.top.close();
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

