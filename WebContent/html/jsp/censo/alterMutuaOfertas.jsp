<!DOCTYPE html>
<!-- alterMutuaOfertas.jsp -->
<!-- CABECERA JSP -->
<%@page import="com.siga.Utilidades.UtilidadesString"%>
<%@page import="java.util.ArrayList"%>
<%@ page pageEncoding="ISO-8859-15"%>
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>
<%@ taglib uri="fmt.tld" prefix="fmt"%>


<html>
<head>
<script>
function habilitarCampos(isHabilitar) {
		
		if(isHabilitar==true){
			var inputs = document.getElementsByTagName("input");
			for(var i = 0 ; i <inputs.length ; i++) {
				input = inputs[i];
				if(input.type=="checkbox") {
					jQuery.removeAttr(input,"disabled");
					//$(input).removeAttr("disabled");
				} else if(input.type!="button") {
					input.className = "box";
					jQuery.removeAttr(input,"readonly");
					//$(input).removeAttr("readonly");
				}
			}
			var selects = document.getElementsByTagName("select");
			for(var i = 0 ; i <selects.length ; i++) {
				select = selects[i];
				jQuery.removeAttr(select,"disabled");
				//$(select).removeAttr("disabled"); 
			}
			var textareas = document.getElementsByTagName("textarea");
			for(var i = 0 ; i <textareas.length ; i++) {
				textarea = textareas[i];
				jQuery.removeAttr(textarea,"disabled");
				//$(select).removeAttr("disabled"); 
			}				
		} else {




			
			var inputs = document.getElementsByTagName("input");
			for(var i = 0 ; i < inputs.length ; i++) {
				var input = inputs[i];
				if(input.type=="checkbox"){
					jQuery.attr(input,"disabled","disabled");
					//$(input).attr("disabled","disabled");
				} else if(input.type!="button"){
					input.className = "boxConsulta";
					//$(input).attr("readonly","readonly");
					jQuery.attr(input,"readonly","readonly");
				}
			}
			
			var selects = document.getElementsByTagName("select");
			for(var i = 0 ; i < selects.length ; i++) {
				var select = selects[i];
				jQuery.attr(select,"disabled","disabled");
				//$(select).attr("disabled","disabled");
			}
			var textareas = document.getElementsByTagName("textarea");
			for(var i = 0 ; i <textareas.length ; i++) {
				var textarea = textareas[i];
				jQuery.attr(textarea,"disabled","disabled")
				//$(textarea).attr("disabled","disabled"); 
			}			
			
		}
	
	}

</script>


<%String app=request.getContextPath(); %>
<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request" />

<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
<!-- <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/js/jquery.ui/css/jquery-ui.1.9.2.custom.min.css'/>"/> -->

<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.8.3.js'/>"></script>
<!-- <script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.9.2.custom.min.js'/>"></script> -->
<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validation.js'/>" type="text/javascript"></script>

<style media="screen" type="text/css">
	.ocultar { display: none }
	
	.red { color: "red" }
	
    fieldset{
    	padding-bottom:10px;
    }
    legend{
    	border:1 black;
    	padding-left:10px;
    	padding-right:10px;
    }
    .defaultText {}
    .defaultTextActive { color: #a1a1a1; font-style: italic; }
    .requiredText {}
    .requiredTextActive { color: #8a8a8a; font-weight:bold;}
    .obligatorio {background: #F7D5E3;}
        
</style>

<!-- INICIO: TITULO Y LOCALIZACION -->
	<siga:Titulo titulo="censo.alterMutua.titulo" localizacion="censo.solicitudIncorporacion.localizacion"/>
	<%
	if(path.toString().contains("Ficha")){ %>
		<siga:Titulo titulo="censo.alterMutua.titulo" localizacion="censo.fichaCliente.localizacion"/>
	<%}%>
<!-- FIN: TITULO Y LOCALIZACION -->

</head>


<body class="tablaCentralCampos">

<div id="datosSolicitud"  style="display:none;">
	<fmt:setLocale value="es_ES"/>
	<html:form action="/CEN_AlterMutua" method="POST">
		<html:hidden property="apellidos"/>
		<html:hidden property="codigoPostal"/>
		<html:hidden property="idSolicitud"/>
		<html:hidden property="comunicacionPreferente"/>
		<html:hidden property="domicilio"/>
		<html:hidden property="email"/>
		<html:hidden property="estadoCivil"/>
		<html:hidden property="fechaNacimiento"/>
		<html:hidden property="identificador"/>
		<html:hidden property="idSolicitudalter"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="idPaquete"/>
		<html:hidden property="idPersona"/>
		<html:hidden property="msgRespuesta"/>
		<html:hidden property="nombre"/>
		<html:hidden property="sexo"/>
		<html:hidden property="idSexo"/>
		<html:hidden property="tipoEjercicio"/>
		<html:hidden property="idTipoIdentificacion"/>
		<html:hidden property="identificador"/>
		<html:hidden property="titular"/>
		<html:hidden property="herederos"/>
		<html:hidden property="familiares"/>
		<html:hidden property="requiereBeneficiarios"/>
		<html:hidden property="requiereFamiliares"/>
		<html:hidden property="modo"/>
		<html:hidden property="brevePropuesta"/>
		<html:hidden property="tarifaPropuesta"/>
		<html:hidden property="codigoInstitucion"/>
		<html:hidden property="propuestasSel"/>
		<html:hidden property="propuesta"/>
		<html:hidden property="numeroPropuestas"/>

		<input type="hidden" name="actionModal" value="">

		<c:set var="estiloText" value="box" />
		<c:set var="estiloCombo" value="boxCombo" />

		<c:if test="AlterMutuaForm.numeroPropuestas <= 0}">
			<c:set var="estiloText" value="boxConsulta" />
			<c:set var="estiloCombo" value="boxComboConsulta" />
		</c:if>

		<fieldset><legend><siga:Idioma key="censo.alterMutua.literal.seleccionPropuesta"/></legend>
		<table>

		<c:if test="${AlterMutuaForm.idSolicitudalter!=null || AlterMutuaForm.numeroPropuestas <= 0}">
			<c:set var="estiloText" value="boxConsulta" />
			<c:set var="estiloCombo" value="boxComboConsulta" />
		</c:if>
						
			<c:forEach items="${AlterMutuaForm.propuestas}" var="p" varStatus="i">
			<tr>
				<td class="labelTextValue" style="width:210px">
					<input type="checkbox"  id="seleccionada${i.index}" class="propuestas requiredText" name="grupoPropuestas" value="${p.idPaquete}" />
					<label for="seleccionada${i.index}">&nbsp;${p.nombre}</label>
				</td>
				<td style="width:30px"><img class="botonConsulta" src="<%=app%>/html/imagenes/bconsultar_on.gif" style="cursor:hand;" name="descripcion${i.index}"></td>
				<td id="breve${i.index}">${p.breve}</td>
				<td id="descripcion${i.index}" style="display:none">${p.descripcion}</td>
			</tr>
			<tr id="estadoSolicitud">
				<td colspan="1" >&nbsp;</td>
				<td colspan="3" >
					<table>
					<tr>
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.solicitud"/></td>
						<td class="labelTextValue" id="holderIdSolicitudAlter">${AlterMutuaForm.idSolicitudalter}</td>
						<td><input type="button" alt=""  id="idButton" onclick="return consultarEstado();" class="button" name="idButton" value="Consultar estado"></td>
						<td class="labelText">&nbsp;</td>
						<td class="tdBotonDescarga" style="display:none"><siga:Idioma key="censo.alterMutua.literal.descargarDocumento"/></td>
						<td class="tdBotonDescarga" style="display:none"> <img id="botonDescarga" src="<%=app%>/html/imagenes/bdownload_on.gif" style="cursor:hand;"></td>
					</tr>
					</table>
				</td>
			</tr>

			</c:forEach>
		<c:if test="${AlterMutuaForm.numeroPropuestas <= 0}">
		<tr>
			<td style="width:100px">&nbsp;</td>
			<td class="labelTextValue"><siga:Idioma key="censo.alterMutua.literal.noHayDatos"/></td>
		</tr>
		</c:if>
		</table>
	
		</fieldset>
		
		<fieldset><legend><siga:Idioma key="censo.alterMutua.literal.datosPersonales"/></legend>
			<table>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.identificador"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.tipoIdentificacion}"/>&nbsp;<c:out value="${AlterMutuaForm.identificador}"/></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.nombre"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.nombre}"/></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.apellidos"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.apellidos}"/></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.fechaNacimiento"/></td>
					<td class="labelTextValor" width="100px"><c:out value="${AlterMutuaForm.fechaNacimiento}"/></td>
				</tr>
				<tr>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.sexo"/></td>
					<td class="labelTextValor"> <c:out value="${AlterMutuaForm.sexo}" /> </td>
					
					<c:if test="${AlterMutuaForm.estadoCivil!=''}">
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.estadoCivil"/></td>
						<td class="labelTextValor">
							<c:out value="${AlterMutuaForm.estadoCivil}" />
							<html:hidden property="idEstadoCivil"/>
						</td>
					</c:if>
					<c:if test="${AlterMutuaForm.estadoCivil==''}">
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.estadoCivil"/> (*)</td>
						<td class="labelTextValor">
						<html:select styleClass="${estiloCombo} requiredText" property="idEstadoCivil" >
							<html:optionsCollection name="AlterMutuaForm" property="estadosCiviles" label="value" value="key" />
						</html:select>
						</td>
					</c:if>
					</td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.colegio"/></td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.colegio}" /></td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.situacionEjercicio"/></td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.tipoEjercicio}" /></td>
				</tr>
			</table>
		</fieldset>

		<fieldset><legend><siga:Idioma key="censo.alterMutua.literal.contacto"/></legend>
			<table>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.medioComunicacion"/> (*)</td>
					<td class="labelTextValue">
						<html:select styleClass="${estiloCombo}" property="tipoComunicacion" >
						<html:optionsCollection name="AlterMutuaForm" property="tiposComunicacion" label="value" value="key" />
						</html:select>
					</td>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.idioma"/> (*)</td>
					<td class="labelTextValue">
						<html:select styleClass="${estiloCombo}" property="idioma" >
						<html:optionsCollection name="AlterMutuaForm" property="idiomas" label="value" value="key" />
						</html:select>
					</td>
					
					<td class="labelText" colspan="2">
						<label for="admitePublicidad"><siga:Idioma key="censo.alterMutua.literal.aceptaPublicidad"/>&nbsp;</label>
						<input type="checkbox" id="admitePublicidad" name="admitePublicidad" value="true" checked/>
					</td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.telefono"/> (*)</td>
					<td class="labelTextValue"><html:text property="telefono1" maxlength="14" styleClass="${estiloText} requiredText"></html:text> </td>

					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.telefono"/> 2</td>
					<td class="labelTextValue"><html:text property="telefono2" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>

					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.movil"/></td>
					<td class="labelTextValue"><html:text property="movil" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>
				</tr>
				<tr>
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.fax"/></td>
					<td class="labelTextValue"><html:text property="fax" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>
					
					<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.correoElectronico"/> (*)</td>
					<td class="labelTextValue" colspan="3"><html:text property="correoElectronico" maxlength="100" styleClass="${estiloText} requiredText" style="width:100%"></html:text> </td>
				</tr>
			</table>

			<fieldset><legend><siga:Idioma key="censo.alterMutua.literal.direccion"/></legend>
				<table>
					<tr>
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.domicilio"/> (*)</td>
						<td class="labelTextValue"><html:text property="domicilio" size="30" maxlength="200" styleClass="${estiloText} requiredText"></html:text></td>
						
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.codigoPostal"/> (*)</td>
						<td class="labelTextValue"> <html:text property="codigoPostal" styleClass="${estiloText} requiredText" size="5" maxlength="5"></html:text></td>
					</tr>
					
					<tr>
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.pais"/> (*)</td>
						<td class="labelTextValue">
							<html:select styleId="selectPais" styleClass="${estiloCombo} requiredText" property="idPais" >
							<html:optionsCollection name="AlterMutuaForm" property="paises" label="value" value="key" />
							</html:select>
						</td>

						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.provincia"/> (*)</td>
						<td class="labelTextValue" id="tdProvincia">
							<html:select styleId="selectProvincia" styleClass="${estiloCombo} requiredText" property="idProvincia" >
							<html:optionsCollection name="AlterMutuaForm" property="provincias" label="value" value="key" />
							</html:select>
						</td>
						
					</tr>
					<tr>

						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.poblacion"/> (*)</td>
						<td class="labelTextValue" id="tdPoblacion"><html:text property="poblacion" style="width:300px" maxlength="50" styleClass="${estiloCombo} requiredText"></html:text></td>
						
						<td class="labelText"><siga:Idioma key="censo.alterMutua.literal.tipoDireccion"/></td>
						<td class="labelTextValue">
							<html:select styleClass="${estiloCombo}" property="tipoDireccion" >
							<html:optionsCollection name="AlterMutuaForm" property="tiposDireccion" label="value" value="key" />
							</html:select>
						</td>
					</tr>

				</table>
			</fieldset>
			
		</fieldset>

		<fieldset class="labelTextValue" id="fieldsetObservaciones">
			<legend><siga:Idioma key='censo.alterMutua.literal.observaciones'/></legend>
			<html:textarea styleClass="box" property="observaciones" styleId="textoObservaciones" rows="4" style="width:99%" maxlength="40"></html:textarea>
		</fieldset>
		
	</html:form>
</div>

<siga:ConjBotonesAccion botones="V,SC" modo="${modoAccion}" clase="botonesDetalle"/>




<div id="dialog-message" title="SIGA" style="vertical-align: top; max-height: 400px"></div>

	<form name="busquedaClientesForm" method="POST" action="/SIGA/CEN_BusquedaClientes.do" target="mainWorkArea">
			<input type="hidden" name="modo" value="Editar">
			<input type="hidden" name="avanzada" value="">
	</form>
	<form name="SolicitudIncorporacionForm" method="POST" action="/SIGA/CEN_SolicitudesIncorporacion.do" target="mainWorkArea">
		<input type="hidden" name="modo" value="">
	</form>	
<c:if test="${modoAccion=='ver'}">
		<script>habilitarCampos('false'); </script>
	</c:if>
	
	
	<script>
	function refrescarLocal(){
		window.location.reload(true);
	}
	
	<%if(path.toString().contains("Ficha")){ %>
		function accionVolver() 
		{
			document.busquedaClientesForm.action = "/SIGA/CEN_BusquedaClientes.do" + "?noReset=true&buscar=true";
			document.busquedaClientesForm.modo.value="abrirConParametros";
			document.busquedaClientesForm.submit();	
		}
	<%}else{%>
		function accionVolver() {		
			document.SolicitudIncorporacionForm.action="./CEN_SolicitudesIncorporacion.do";	
			document.SolicitudIncorporacionForm.target="mainWorkArea";
			document.SolicitudIncorporacionForm.submit();
		}
	<%}%>
	
	
	function accionCerrar(){
		top.cierraConParametros();
	}
	
	function validarFormulario(){
		var val = true;
		var paquetes = '';
		// Para cada campo obligatorio ...
		$('.requiredText').each(function(index){
			// Si el campo esta vacio
			if($(this).is(':visible') && ($(this).val()==='' || $(this).val()==='-1')){ 
				val = false;
				$(this).addClass("obligatorio");
			}
		});
		if($('#selectPais').val()==='00') $('#selectPais').val('724');
		if($('#selectPais').val()==='724' && $('#selectProvincia').val()==='00'){
			$('#selectProvincia').addClass('obligatorio');
			val=false;
		}
		$.each($("input[name='grupoPropuestas']:checked"), function() {
			paquetes +=$(this).val()+"-";
		});
		document.AlterMutuaForm.propuestasSel.value = paquetes;
		if(paquetes===''){
			$.each($("input[name='grupoPropuestas']"), function() {
				$(this).addClass("obligatorio");
			});
			val = false;
		}
		return val;
	}
	
	function accionGuardar(){
		sub();
		var mensaje="<siga:Idioma key='censo.alterMutua.literal.mensajeConfirmacion'/>";
		if(validarFormulario()){
			if(confirm(mensaje)){
				$.ajax({
		            type: "POST",
		            url: "/SIGA/CEN_AlterMutua.do?modo=insertar",
		            data: $('form').serialize(),
		            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
		            success: function(json){
		            	jAlert(json.mensaje,400);
	            		document.AlterMutuaForm.idSolicitudalter.value = json.idSolicitud;
	            		$("#holderIdSolicitudAlter").text(json.idSolicitud);
	            		showEstado();
	            		makeReadOnly();
				        fin();
		            },
		            error: function(e){
		                alert('Error de comunicación: ' + e);
				        fin();
		            }
		        });
			}else{
				fin();
			}
		}else{
			alert("Rellene correctamente los campos obligatorios");
			fin();
		}
	}
	
	function showEstado(){
   		if(AlterMutuaForm.idSolicitudalter.value)
      		$("#estadoSolicitud").show();
   		else
      		$("#estadoSolicitud").hide();	
   	}
	
    function makeReadOnly(){
    	$(":input").prop("readonly", true);
    	$(":checkbox").prop("disabled", true);
    	
   		$("select").each(function(){
   			$(this).after($(this).find("option:selected").text());
   			$(this).hide();
   		});
   		$('#idButtonSolicitar').hide();
   		$('.box').addClass("boxConsulta");
   		$('.box').removeClass("box");
   		$('.boxCombo').addClass("boxComboConsulta");
   		$('.boxCombo').removeClass("boxCombo");

    }

	$(document).ready(function(){

		$('#datosSolicitud').height($(window).height()-25);
		
		$(".defaultText").focus(function(){
			if ($(this).val() == $(this)[0].title){
				$(this).removeClass("defaultTextActive");
				$(this).val("");
			}
		});
		$(".defaultText").blur(function(){
			if ($(this).val() == ""){
				$(this).addClass("defaultTextActive");
				$(this).val($(this)[0].title);
			}
		});
		$(".defaultText").blur();
		$(".requiredText").focus(function(srcc){
			if ($(this).val() == $(this)[0].title){
				$(this).removeClass("requiredTextActive");
				$(this).removeClass("obligatorio");
				$(this).val("");
			}
		});
		$(".requiredText").blur(function(){
			if ($(this).val() == ""){
				$(this).addClass("requiredTextActive");
				$(this).val($(this)[0].title);
			}
		});
		$(".requiredText").click(function(){
			$(this).removeClass("requiredTextActive");
			$(this).removeClass("obligatorio");
		});
		$(".requiredText").blur();
		
		$(".propuestas").change(function(){
			$(".propuestas").removeClass("obligatorio");
		});

 	      	
 	   $(".botonConsulta").click(
			function(){
				//var texto =  $($(this).attr('name')).html();
				var desc ="#"+$(this).attr('name')
				var texto = $("#"+desc).html();
				jAlert(texto, 600, 600);
			}
		);
 	   
 	   $('.propuestas').click(
		   	function(){
		   		if($(this).is(':checked')){
			   		$(this).parent().addClass('labelText');
			   		$(this).parent().removeClass('labelTextValue');
		   		}else{
			   		$(this).parent().removeClass('labelText');
			   		$(this).parent().addClass('labelTextValue');
		   		}
		   	}
	   );

       	$("#selectPais").change(function(){
       		if(($(this).val()==="00")||$(this).val()==="724"){
       			$("#tdProvincia").attr('disabled',false);
       		}else{
       			$("#tdProvincia").attr('disabled',true);
       			$("#selectProvincia").val('00');
       		}
       	});
       	
       	$("#selectPais").trigger('change');
       	
       	if(AlterMutuaForm.numeroPropuestas.value<=0){
       		jAlert(AlterMutuaForm.msgRespuesta.value,400,300);
       		makeReadOnly();
       	}
       	
       	if(AlterMutuaForm.idSolicitudalter.value || AlterMutuaForm.numeroPropuestas.value<=0){
       		makeReadOnly();
       	}
    	
   		$('#datosSolicitud').show();
   		$('.botonesDetalle').show();
   		
        $('#textoObservaciones').on('keyup keydown blur', function() {
            var maxlength = 4000;
            var val = $(this).val();
            if (val.length > maxlength) {
                $(this).val(val.slice(0, maxlength));
            }
        });
   		
        showEstado();
    });

	function consultarEstado(){
		sub();
        $.ajax({
            type: "POST",
            url: "/SIGA/CEN_AlterMutua.do?modo=getEstado",
            data: $('form').serialize(),
            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
            success: function(json){
           		jAlert(json.mensaje,400,300);
           		if(json.ruta!=""){
					$(".tdBotonDescarga").show();
					$("#botonDescarga").live('click',
						function(){
						descargaFichero(json.ruta);
           	 			}
           	 		);
           		}
		        fin();
            },
            error: function(e){
                alert('Error de comunicación: ' + e);
		        fin();
            }
        });
	}
	
	function descargaFichero(ruta){
		var formu=document.createElement("<form method='POST' name='descargar'  action='/SIGA/ServletDescargaFichero.svrl' target='submitArea'>");
		formu.appendChild(document.createElement("<input type='hidden' name='rutaFichero'   value=''/>"));
		formu.appendChild(document.createElement("<input type='hidden' name='nombreFichero'   value=''/>"));
		formu.appendChild(document.createElement("<input type='hidden' name='accion'   value=''/>"));
		document.appendChild(formu);
		formu.rutaFichero.value=ruta;
		formu.nombreFichero.value='Solicitud.pdf';
		formu.accion.value = "";
		formu.submit();
	}
	
	function jAlert(texto, ancho, alto){
		$("#dialog-message").html(texto);
		$("#dialog-message").dialog({
			modal: true,
			resizable: false,
			width: ancho,
			buttons: { "Ok": function() { $(this).dialog("close"); $(this).dialog("destroy"); } }
		});
		$("#dialog-message").scrollTop(0);
	}
	
	</script>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none" />
</body>

</html>



