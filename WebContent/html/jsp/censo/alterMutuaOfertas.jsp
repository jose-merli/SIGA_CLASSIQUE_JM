
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

<%String app=request.getContextPath(); %>


<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/jsp/general/stylesheet.jsp"/>">
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page="/html/css/jquery-ui.css"/>">

<script src="<html:rewrite page='/html/js/SIGA.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validacionStruts.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/validation.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/jquery.js'/>" type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/jquery-ui.js'/>" type="text/javascript"></script>

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
<siga:Titulo titulo="censo.alterMutua.titulo" />
<!-- FIN: TITULO Y LOCALIZACION -->

</head>


<body class="tablaCentralCampos">

<div style="height:95%;overflow: auto;" id="datosSolicitud"  style="display:none;">
	<fmt:setLocale value="es_ES"/>
	<html:form action="/CEN_AlterMutua" method="POST">
		<html:hidden property="apellidos"/>
		<html:hidden property="codigoPostal"/>
		<html:hidden property="idSolicitud"/>
		<html:hidden property="comunicacionPreferente"/>
		<html:hidden property="domicilio"/>
		<html:hidden property="email"/>
		<html:hidden property="estadoCivil"/>
		<html:hidden property="fax"/>
		<html:hidden property="fechaNacimiento"/>
		<html:hidden property="identificador"/>
		<html:hidden property="idInstitucion"/>
		<html:hidden property="idPaquete"/>
		<html:hidden property="movil"/>
		<html:hidden property="msgRespuesta"/>
		<html:hidden property="nombre"/>
		<html:hidden property="poblacion"/>
		<html:hidden property="sexo"/>
		<html:hidden property="idSexo"/>
		<html:hidden property="telefono1"/>
		<html:hidden property="telefono2"/>
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

		<fieldset><legend>Selección de propuesta</legend>
		<table>
		<tr>

						
		<!-- Guardamos los datos de las propuestas en una tabla oculta ¿cutre? pues si -->
			<c:forEach items="${AlterMutuaForm.propuestas}" var="p" varStatus="i">
			<tr>
				<td style="width:100px">&nbsp;</td>
				<td class="labelTextValue" style="width:200px">
					<input type="checkbox"  id="seleccionada${i.index}" class="propuestas requiredText" name="grupoPropuestas" value="${p.idPaquete}" />
					<label for="seleccionada${i.index}">&nbsp;${p.nombre}</label>
				</td>
				<td><img class="botonConsulta" src="<%=app%>/html/imagenes/lupa.gif" style="cursor:hand;" name="descripcion${i.index}"></td>
				<td id="descripcion${i.index}" style="display:none">${p.descripcion}</td>
			</tr>
			</c:forEach>
		<c:if test="${AlterMutuaForm.numeroPropuestas <= 0}">
		<tr>
			<td style="width:100px">&nbsp;</td>
			<td class="labelTextValue">No se han recibido datos de Alter Mutua</td>
		</tr>
		</c:if>
		</table>
	
		</fieldset>

		<fieldset><legend>Datos Personales</legend>
			<table>
				<tr>
					<td class="labelText">Identificador</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.tipoIdentificacion}" />&nbsp;<c:out value="${AlterMutuaForm.identificador}" /></td>
					
					<td class="labelText">Nombre</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.nombre}" /></td>
					
					<td class="labelText">Apellidos</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.apellidos}" /></td>
					
					<td class="labelText">Fecha Nacimiento</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.fechaNacimiento}" /></td>
				</tr>
				<tr>
					
					<td class="labelText">Sexo</td>
					<td class="labelTextValor"> <c:out value="${AlterMutuaForm.sexo}" /> </td>
					
					<c:if test="${AlterMutuaForm.estadoCivil!=''}">
						<td class="labelText">Estado Civil</td>
						<td class="labelTextValor">
							<c:out value="${AlterMutuaForm.estadoCivil}" />
							<html:hidden property="idEstadoCivil"/>
						</td>
					</c:if>
					<c:if test="${AlterMutuaForm.estadoCivil==''}">
						<td class="labelText">Estado Civil (*)</td>
						<td class="labelTextValor">
						<html:select styleClass="${estiloCombo} requiredText" property="idEstadoCivil" >
							<html:optionsCollection name="AlterMutuaForm" property="estadosCiviles" label="value" value="key" />
						</html:select>
						</td>
					</c:if>
					</td>
					
					<td class="labelText">Colegio</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.colegio}" /></td>
					
					<td class="labelText">Situación ejercicio</td>
					<td class="labelTextValor"><c:out value="${AlterMutuaForm.tipoEjercicio}" /></td>
				</tr>
			</table>
		</fieldset>
			

		
		
		<fieldset><legend>Contacto</legend>
			<table>
				<tr style="display:none;">
					<td class="labelText">Medio de Comunicación (*)</td>
					<td class="labelTextValue">
						<html:select styleClass="${estiloCombo}" property="tipoComunicacion" >
						<html:optionsCollection name="AlterMutuaForm" property="tiposComunicacion" label="value" value="key" />
						</html:select>
					</td>
					<td class="labelText">Idioma (*)</td>
					<td class="labelTextValue" colspan="3">
						<html:select styleClass="${estiloCombo}" property="idioma" >
						<html:optionsCollection name="AlterMutuaForm" property="idiomas" label="value" value="key" />
						</html:select>
					</td>
				</tr>
				<tr>
					<td class="labelText">Teléfono (*)</td>
					<td class="labelTextValue"><html:text property="telefono1" maxlength="14" styleClass="${estiloText} requiredText"></html:text> </td>

					<td class="labelText">Teléfono 2</td>
					<td class="labelTextValue"><html:text property="telefono2" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>

					<td class="labelText">Móvil</td>
					<td class="labelTextValue"><html:text property="movil" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>
				</tr>
				<tr>
					<td class="labelText">Fax</td>
					<td class="labelTextValue"><html:text property="fax" maxlength="14" styleClass="${estiloText}" size="20"></html:text> </td>
					
					<td class="labelText">Correo Electrónico (*)</td>
					<td class="labelTextValue" colspan="3"><html:text property="correoElectronico" maxlength="100" styleClass="${estiloText} requiredText" style="width:100%"></html:text> </td>
				</tr>
			</table>

			<fieldset><legend>Dirección</legend>
				<table>

					<tr>
						<td class="labelText">Domicilio (*)</td>
						<td class="labelTextValue"><html:text property="domicilio" size="30" maxlength="200" styleClass="${estiloText} requiredText"></html:text></td>
						
						<td class="labelText">Código Postal (*)</td>
						<td class="labelTextValue"> <html:text property="codigoPostal" styleClass="${estiloText} requiredText" size="5" maxlength="5"></html:text></td>
					</tr>
					
					<tr>
						<td class="labelText">Pais (*)</td>
						<td class="labelTextValue">
							<html:select styleId="selectPais" styleClass="${estiloCombo} requiredText" property="idPais" >
							<html:optionsCollection name="AlterMutuaForm" property="paises" label="value" value="key" />
							</html:select>
						</td>

						<td class="labelText">Provincia (*)</td>
						<td class="labelTextValue" id="tdProvincia">
							<html:select styleId="selectProvincia" styleClass="${estiloCombo} requiredText" property="idProvincia" >
							<html:optionsCollection name="AlterMutuaForm" property="provincias" label="value" value="key" />
							</html:select>
						</td>
						
					</tr>
					<tr>

						<td class="labelText">Población (*)</td>
						<td class="labelTextValue" id="tdPoblacion"><html:text property="poblacion" style="width:300px" maxlength="50" styleClass="${estiloCombo} requiredText"></html:text></td>

						<td class="labelText">Tipo de Dirección </td>
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
			<legend>Observaciones</legend>
			<html:textarea styleClass="box" property="observaciones" rows="4" style="width:99%"></html:textarea>
		</fieldset>
		
	</html:form>
</div>


<table class="botonesDetalle" align="center">
<tr>
	<td  style="width:900px;"> &nbsp; </td>
	<td class="tdBotones">
		<input type="button" alt="" id="idButtonSolicitar" onclick="return accionGuardar();" class="button" name="idButton" value="<siga:Idioma key="general.boton.solicitarCompra"/>">
	</td>
</tr>
</table>

	<script>
	function refrescarLocal(){
		window.location.reload(true);
	}
	
	function accionVolver() {		
		document.forms[0].action="./CEN_SolicitudesIncorporacion.do";	
		document.forms[0].target="mainWorkArea";
		document.forms[0].modo.value="";
		document.forms[0].submit();
	}
	
	
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
			$('#selectProvincia').addClas('obligatorio');
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
		if(validarFormulario()){
			
			$.ajax({
	            type: "POST",
	            url: "/SIGA/CEN_AlterMutua.do?modo=insertar",
	            data: $('form').serialize(),
	            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
	            success: function(json){
	            	if(json.error==false){
	            		jqueryAlert(json.mensaje,400);
				        fin();
	            	}else{
	            		alert('Error de comunicación: ');
	            		fin();
	            	}
	            },
	            error: function(e){
	                alert('Error de comunicación: ' + e);
			        fin();
	            }
	        });
		}else{
			alert("Rellene correctamente los campos obligatorios");
			fin();
		}
	}
	
	
    function makeReadOnly(){
    	$(":input").prop("readonly", true);
    	$(":checkbox").prop("disabled", true);
    	
   		$("select").each(function(){
   			$(this).after($(this).find("option:selected").text());
   			$(this).hide();
   		});
   		$('#fieldsetBeneficiarios').hide();
   		$('#fieldsetFamiliares').hide();
   		$('#idButtonSolicitar').hide();
   		$('#idButtonActualizar').hide();
   		$('.box').addClass("boxConsulta");
   		$('.box').removeClass("box");
   		$('.boxCombo').addClass("boxComboConsulta");
   		$('.boxCombo').removeClass("boxCombo");

    }

	$(document).ready(function(){

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
				jqueryAlert(texto, 600, 600);
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
       		jqueryAlert(AlterMutuaForm.msgRespuesta.value,400,300);
       		makeReadOnly();
       	}
    	
   		$('#datosSolicitud').show();
    });

	</script>

	<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" style="display:none" />
</body>

</html>



