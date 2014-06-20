<!DOCTYPE html>
<html>
<head>
<!-- ResultadoBusquedaMandatos.jsp -->
<!-- EJEMPLO DE VENTANA LISTA DE CABECERAS FIJAS -->
<!-- Contiene el contenido del frame de una pantalla de detalle multiregistro
	 Utilizando tags pinta una lista con cabeceras fijas 
	 VERSIONES:
	 raul.ggonzalez 16-12-2004 Modificacion de formularios para validacion por struts de campos
	 miguel.villegas 11-01-2005 Incorpora capacidad de refresco para borrar (target->submitArea, esta comentado)
-->


<!-- CABECERA JSP -->

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>

<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.general.*"%>
<%@ page import="com.atos.utils.ClsConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.atos.utils.GstDate"%>
<%@ page import="com.siga.censo.form.MantenimientoMandatosForm"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.siga.Utilidades.*"%>

<%@ page import="com.siga.Utilidades.Paginador"%>

<%@ page import="java.util.*"%>
<%@ page import="com.atos.utils.Row"%>
<%@ page import="com.siga.tlds.*"%>
<%@ page import="com.atos.utils.RowsContainer"%>
<%@ page import="com.siga.beans.CenClienteAdm"%>



<!-- JSP -->
<bean:define id="registrosSeleccionados" name="mantenimientoMandatosForm" property="registrosSeleccionados" type="java.util.ArrayList"/>
<bean:define id="datosPaginador" name="mantenimientoMandatosForm" property="datosPaginador" type="java.util.HashMap"/>

<%
	try {
		String app = request.getContextPath();
		HttpSession ses = request.getSession();
		
		UsrBean usrbean = (UsrBean) session.getAttribute(ClsConstants.USERBEAN);
		String idioma = usrbean.getLanguage().toUpperCase();
		String idInstitucionLocation = usrbean.getLocation();
		int idInstitucionInt = Integer.parseInt(idInstitucionLocation);
		boolean esColegio = (idInstitucionInt > 2000 && idInstitucionInt < 3000);

		CenClienteAdm admCen = new CenClienteAdm(usrbean);
		String valorCheckPersona = "";

		Vector resultado = null;

		String titu = "";


		/** PAGINADOR ***/
		String paginaSeleccionada = "";

		String totalRegistros = "";

		String registrosPorPagina = "";

		if (datosPaginador != null) {

			if (datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
				resultado = (Vector) datosPaginador.get("datos");
				Paginador paginador = (Paginador) datosPaginador.get("paginador");
				paginaSeleccionada = String.valueOf(paginador.getPaginaActual());
				totalRegistros = String.valueOf(paginador.getNumeroTotalRegistros());
				registrosPorPagina = String.valueOf(paginador.getNumeroRegistrosPorPagina());

			} else {
				resultado = new Vector();
				paginaSeleccionada = "0";

				totalRegistros = "0";

				registrosPorPagina = "0";
			}
		} else {
			resultado = new Vector();
			paginaSeleccionada = "0";

			totalRegistros = "0";

			registrosPorPagina = "0";
		}

		String action = app + "/CEN_MantenimientoMandatos.do";
		
		String colegio=(String)request.getAttribute("nombreColegio");
		/**************/
%>


<!-- HEAD -->

	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
  	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
  
	<!-- INICIO: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	<!-- Validaciones en Cliente -->
	<!-- El nombre del formulario se obtiene del struts-config -->
	<html:javascript formName="/CEN_MantenimientoMandatos.do" staticJavascript="false" />
	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<!-- FIN: VALIDACIONES DE CAMPOS MEDIANTE STRUTS -->
	
	<!-- INICIO: TITULO Y LOCALIZACION -->
	<!-- Escribe el título y localización en la barra de título del frame principal -->

	<!-- FIN: TITULO Y LOCALIZACION -->
	
	<style type="text/css">
	.ui-dialog-titlebar-close {
				  visibility: hidden;
				}
	</style>
	
	<!-- SCRIPTS LOCALES -->
	<script language="JavaScript">
		function refrescarLocal() {
			parent.buscar();
		}
	
		
		ObjArray = new Array();
		
		Array.prototype.indexOf = function(s) {
		for (var x=0;x<this.length;x++) if(this[x] == s) return x;
			return false;
		}
	 
		   
		function pulsarCheck(obj){	
			if (!obj.checked ){		   		
				ObjArray.splice(ObjArray.indexOf(obj.value),1);
				seleccionados1=ObjArray;
			 } else {
				ObjArray.push(obj.value);
			   	seleccionados1=ObjArray;
			}	  	
			document.forms[0].registrosSeleccionados.value=seleccionados1;		
			document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
			checkTodos();
		}
		   
		function cargarChecks(){
			<%if (registrosSeleccionados != null) {
						for (int p = 0; p < registrosSeleccionados.size(); p++) {
							Hashtable claves = (Hashtable) registrosSeleccionados.get(p);
							valorCheckPersona = (String) claves.get("CLAVE");%>
							ObjArray.push("<%=valorCheckPersona%>");
						<%
						}
					}%>
			ObjArray.toString();
			seleccionados1=ObjArray;
			document.forms[0].registrosSeleccionados.value=seleccionados1;
			if(document.getElementById('registrosSeleccionadosPaginador')) {
				document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
			}
				
		}

		function cargarChecksTodos(o){  		
			if (document.getElementById('registrosSeleccionadosPaginador')){			
		  		var conf = confirm('<siga:Idioma key="paginador.message.marcarDesmarcar"/>'); 	   	   	
			   	if (conf){
					ObjArray = new Array();
				   	if (o.checked){				   				
						parent.seleccionarTodos('<%=paginaSeleccionada%>');					
					} else {					
						ObjArray1= new Array();
					 	ObjArray=ObjArray1;
					 	seleccionados1=ObjArray;				 	
						document.forms[0].registrosSeleccionados.value=seleccionados1;
						var ele = document.getElementsByName("chkPersona");						
						for (i = 0; i < ele.length; i++) {
							if(!ele[i].disabled){
								ele[i].checked = false; 
							}							
						}		
					 }		   	  
			   	  } else {
			   	  	if (!o.checked ){		   	  			
				   	  		var ele = document.getElementsByName("chkPersona");							
						  	for (i = 0; i < ele.length; i++) {
						  		if(!ele[i].disabled){
						  			if(ele[i].checked){	
				     					ele[i].checked = false;
										ObjArray.splice(ObjArray.indexOf(ele[i].value),1);
									}
								}
						   	}					   	
						   	seleccionados1=ObjArray;
					   } else {				   	
						   	var ele = document.getElementsByName("chkPersona");								
						  	for (i = 0; i < ele.length; i++) {
						  		if(!ele[i].disabled){
									if(!ele[i].checked){				  		
					    				ele[i].checked = true;
										ObjArray.push(ele[i].value);
									}
								}
						   	}					   		
					   		seleccionados1=ObjArray;
					   }
					   document.forms[0].registrosSeleccionados.value=seleccionados1;			   		
			   	  }
			   	if(document.getElementById('registrosSeleccionadosPaginador')) {
			   		document.getElementById('registrosSeleccionadosPaginador').value =ObjArray.length;
			   	}
			}
		 }
		   
		function checkTodos(){	
		 	var ele = document.getElementsByName("chkPersona");
			var todos=1;	
		  	for (i = 0; i < ele.length; i++) {
	   			if(!ele[i].checked){
	   				todos=0;
	   				break;
	   			}
	   		}	   
		    if (todos==1){	   		
				document.getElementById("chkGeneral").checked=true;
			} else {
				document.getElementById("chkGeneral").checked=false;
			}			
	   	}
		
		function accionGenerarExcels(){
	   		sub();			
			datos = "";		
			for (i = 0; i < ObjArray.length; i++) {				
				var idRegistros = ObjArray[i];
				/*index = idRegistros.indexOf('||');
				idInstitucion  = idRegistros.substring(0,index);			
				idRegistros = idRegistros.substring(index+2);
				index = idRegistros.indexOf('||');
				idPersona  = idRegistros.substring(0,index);
				idRegistros = idRegistros.substring(index+2);*/			
				idRegistros=replaceAll(idRegistros,"#", " ");
				datos = datos +"##"+	idRegistros;
			}
				
			if (datos == '') {
				alert ('<siga:Idioma key="general.message.seleccionar"/>');
				fin();
				return;
			}
			document.forms[0].tablaDatosDinamicosD.value = datos;
			document.forms[0].modo.value ='generaExcel';		
			document.forms[0].target='submitArea';
			document.forms[0].submit();			
			fin();			
	   	}
		
		function accionFirmarSeleccionados(){
			sub();			
			datos = "";		
			jQuery("#fechaFirma").val(jQuery("#diagFechaFirma").val());
			jQuery("#lugarFirma").val(jQuery("#diagLugarFirma").val());
			for (i = 0; i < ObjArray.length; i++) {				
				var idRegistros = ObjArray[i];		
				idRegistros=replaceAll(idRegistros,"#", " ");
				datos = datos +"##"+	idRegistros;
			}
			if(jQuery("#fechaFirma").val()==="" || jQuery("#lugarFirma").val()===""){
				alert("Rellene todos los campos obligatorios");
				fin();
				return;
			}	
			if (datos == '') {
				alert ('<siga:Idioma key="general.message.seleccionar"/>');
	            jQuery( this ).dialog( "close" );
				return;
			}
			document.forms[0].target='submitArea';
			document.forms[0].tablaDatosDinamicosD.value = datos;
			document.forms[0].modo.value ='firmarSeleccionados';		
			document.forms[0].submit();			
			fin();	
		}

		function firmarMandatos(){
			datos = "";	
			for (i = 0; i < ObjArray.length; i++) {				
				var idRegistros = ObjArray[i];		
				idRegistros=replaceAll(idRegistros,"#", " ");
				datos = datos +"##"+	idRegistros;
			}
				
			if (datos == '') {
				alert ('<siga:Idioma key="general.message.seleccionar"/>');
				fin();
				return;
			}
			
			jQuery("#dialogoFirma").dialog(
				{
				      height: 270,
				      width: 525,
				      modal: true,
				      resizable: false,
				      buttons: {
				          	"Firmar": function() {
				        	  accionFirmarSeleccionados();
				            },
				            "Cerrar": function() {
				              jQuery( this ).dialog( "close" );
				            }
				          }
				    }
			);
			jQuery(".ui-widget-overlay").css("opacity","0");
			
			
		}
	function accionComunicar()
		{
		datos = "";
		
		for (i = 0; i < ObjArray.length; i++) {
			var refMandatoSEPA = ObjArray[i];
			
 		   	datos = datos +"refMandatoSEPA=="+refMandatoSEPA + "##idInstitucion==" +<%=idInstitucionLocation%>+"%%%";
		}
		numElementosSeleccionados =  ObjArray.length; 
		if (datos == '') {
			alert('<siga:Idioma key="general.message.seleccionar"/>');
			return;
		}
			if(numElementosSeleccionados>50){
				document.forms["InformesGenericosForm"].descargar.value = '0';
			}
			else{
				document.forms["InformesGenericosForm"].descargar.value = '1';
			}
			document.forms["InformesGenericosForm"].datosInforme.value=datos;
			var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
			if (arrayResultado==undefined||arrayResultado[0]==undefined){
			   		
		   	} 
		   	else {
		   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
		   		if(confirmar){
		   			var idEnvio = arrayResultado[0];
				    var idTipoEnvio = arrayResultado[1];
				    var nombreEnvio = arrayResultado[2];				    
				    
				   	document.forms["DefinirEnviosForm"].tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
				   	document.forms["DefinirEnviosForm"].modo.value='editar';
				   	document.forms["DefinirEnviosForm"].submit();
		   		}
		   	}
			
		}
		
		
	</script>

</head>

<body  onload="cargarChecks();checkTodos()" class="tablaCentralCampos">

<!-- INICIO: LISTA DE VALORES -->
<!-- Tratamiento del tagTabla y tagFila para la formacion de la lista 
			 de cabeceras fijas -->

<!-- Formulario de la lista de detalle multiregistro -->

	<html:form action="/CEN_MantenimientoMandatos.do?noReset=true" method="POST" target="mainWorkArea" style="display:none">
		<input type="hidden" name="modo"  id="modo"  value="">
		<input type="hidden" name="actionModal"  id="actionModal"  value="">
		<input type="hidden" id="fechaFirma"  name="fechaFirma" value="">
		<input type="hidden" id="lugarFirma"  name="lugarFirma" value="">
		<html:hidden property="registrosSeleccionados"  styleId="registrosSeleccionados"/>
		<html:hidden property="datosPaginador"  styleId="datosPaginador" />
		<html:hidden property="seleccionarTodos"  styleId="seleccionarTodos" value=""/>
	</html:form>
	
<%
		String tamanosCol = "3,8,8,13,13,8,20,14";
		//String nombresCol = "<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>, NColegiado, NIF, Apellidos, Nombre,IBAN,Titular,Tipo, Referencia, Fecha de firma";
		String nombresCol = "<input type='checkbox' name='chkGeneral'  id='chkGeneral' onclick='cargarChecksTodos(this)'/>, censo.busquedaClientesAvanzada.literal.nColegiado, censo.busquedaClientes.literal.nif, censo.busquedaClientes.literal.apellidos, censo.busquedaClientes.literal.nombre,censo.busquedaClientesAvanzada.literal.tipoCliente, IBAN/Referencia, Fecha/Lugar de firma";
%>
<siga:Table name="tablaDatos"
	width="100%"
	columnNames="<%=nombresCol %>" 
	columnSizes="<%=tamanosCol %>">


	<!-- INICIO: ZONA DE REGISTROS -->
	<!-- Aqui se iteran los diferentes registros de la lista -->

	<%
		if (resultado == null || resultado.size() == 0) {
	%>
					<tr class="notFound">
			   		<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
					</tr>
	<%
		} else {

					// recorro el resultado
			String fechaFirma="";
			String idPersona="";
			String idInstitucion="";
			String valorCheck="";
			String clase="filaTablaPar";
					for (int i = 0; i < resultado.size(); i++) {
						Row fila = (Row) resultado.elementAt(i);
						Hashtable registro = (Hashtable) fila.getRow();

						String cont = new Integer(i + 1).toString();
						UsrBean user = (UsrBean) ses.getAttribute("USRBEAN");
						String modo = "edicion";
						fechaFirma = UtilidadesString.mostrarDatoJSP(
								GstDate.getFormatedDateShort(user.getLanguage(), 
								registro.get("FECHAFIRMA")));
						clase = i%2==0?"filaTablaPar":"filaTablaImpar";
						FilaExtElement[] elems = new FilaExtElement[1];
						elems[0] = new FilaExtElement("enviar", "comunicar", SIGAConstants.ACCESS_READ);
						
						idPersona=registro.get("IDPERSONA").toString();
						idInstitucion=user.getLocation();
						
						valorCheck="";
						valorCheck+=registro.get("REFERENCIA");			
						
	%>
	<!-- REGISTRO  -->
	<!-- Esto es un ejemplo de dos columnas de datos, lo que significa
				 que la lista contiene realmente 3 columnas: Las de datos mas 
				 la de botones de acción sobre los registos  -->
 

		<tr class="<%=clase%>">
			<td>
			<%boolean isChecked = false;
			for (int z = 0; z < registrosSeleccionados.size(); z++) {
				Hashtable clavesRegistro = (Hashtable) registrosSeleccionados.get(z);
				String clave = (String) clavesRegistro.get("CLAVE");
				if (valorCheck.trim().equalsIgnoreCase(clave.trim())) {
					isChecked = true;
					break;
				}
	
			}
			if (isChecked) { %> 
				<input type="checkbox" value="<%=valorCheck%>" name="chkPersona" checked onclick="pulsarCheck(this)">
			<%} else { %> 
				<input type="checkbox" value="<%=valorCheck%>"	name="chkPersona" onclick="pulsarCheck(this)"> 
			<%}%>
			</td>
			
			<td>
				<input type="hidden" name="oculto<%=cont%>_1" id="oculto<%=cont%>_1" value="<%=valorCheck%>" />
			<%=registro.get("NCOLEGIADO")%>
			</td>
			<td><%=registro.get("NIFCIF")%></td>
			<td><%=registro.get("APELLIDOS")%></td>
			<td><%=registro.get("NOMBRE")%></td>
			<td><%=registro.get("TIPOMANDATO")%></td>
			<td>IBAN - <%=registro.get("IBAN")%><br>Ref - <%=registro.get("REFERENCIA")%></td>
			<td><%=fechaFirma%><BR><%=registro.get("LUGARFIRMA")%></td>
		</tr>

	<!-- FIN REGISTRO -->
	<%
		} // del for
	%>

	<!-- FIN: ZONA DE REGISTROS -->

	<%
		} // del if
	%>

</siga:Table>

<!-- INICIO: BOTONES ACCIONES -->
<table class="botonesDetalle" id="idBotonesAccion"  align="center">
	<tr>
		<td  style="width:900px;">
		&nbsp;
		</td>
		<td class="tdBotones">
			<input type="button" alt="Comunicar"  id="idButton" onclick="return accionComunicar();" class="button" name="idButton" value="Comunicar">
		</td>
		
		<td class="tdBotones">
		<input type="button" alt="Firmar Mandatos"  id="idButton" onclick='firmarMandatos()' class="button" name="idButton" value="<siga:Idioma key='censo.mantenimientoMandatos.firmarMandatos'/>"/>
		</td>
		<td class="tdBotones">
		<input type="button" alt="Generar Excels"  id="idButton" onclick='return accionGenerarExcels();' class="button" name="idButton" value="<siga:Idioma key='general.boton.exportarExcel'/>"/>
		</td>
	</tr>
</table>


<div id="dialogoFirma" title="Firma de Mandatos" style="display:none">
<div>
  	<p class="labelTextValue"><siga:Idioma key='censo.mantenimientoMandatos.explicacion1'/></p>
  	<p class="labelTextValue"><siga:Idioma key='censo.mantenimientoMandatos.explicacion2'/></p>
  	
  	<siga:ConjCampos>
  		<div style="width:75px;float:left" class="labelText"><label for="diagFechaFirma"><siga:Idioma key='cen.consultaAnticipos.literal.fecha'/> (*)</label></div>
	    <div class="labelTextValue"><siga:Fecha nombreCampo="diagFechaFirma" valorInicial="" anchoTextField="8"/></div>
  		<div style="width:75px;float:left" class="labelText"><siga:Idioma key='gratuita.mantenimientoLG.literal.lugar'/> (*)</div>
	    <div class="labelTextValue"><input type="text" id="diagLugarFirma" maxlength="100" size="40" value="<%=colegio%>"/></div>
  	</siga:ConjCampos>
</div>
</div>


<!-- FIN: LISTA DE VALORES -->
<%
	if (datosPaginador != null && datosPaginador.get("datos") != null && !datosPaginador.get("datos").equals("")) {
			String regSeleccionados = ("" + ((registrosSeleccionados == null) ? 0 : registrosSeleccionados.size()));


%>

<siga:Paginador totalRegistros="<%=totalRegistros%>"
	registrosPorPagina="<%=registrosPorPagina%>"
	paginaSeleccionada="<%=paginaSeleccionada%>" idioma="<%=idioma%>"
	registrosSeleccionados="<%=regSeleccionados%>"
	modo="buscarPor" clase="paginator"
	divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:30px; left: 0px"
	distanciaPaginas="" action="<%=action%>" 
	/>

<%}%>


<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" value = "<%=idInstitucionLocation%>"/>
	<html:hidden property="idTipoInforme" value="OSEPA"/>
	<html:hidden property="enviar" value="1"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>
<!-- Formulario para la edicion del envio -->
<html:form action="/ENV_DefinirEnvios.do" method="POST" target="mainWorkArea">
	<html:hidden property = "modo" value = ""/>
	<html:hidden property = "tablaDatosDinamicosD" value = ""/>
	<html:hidden property="idTipoInforme" value="OSEPA"/>

</html:form>

<!-- INICIO: SUBMIT AREA -->
<!-- Obligatoria en todas las páginas-->
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp"
	style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->


</body>

</html>

<%
	} catch (Exception e) {
		e.printStackTrace();
	}
%>