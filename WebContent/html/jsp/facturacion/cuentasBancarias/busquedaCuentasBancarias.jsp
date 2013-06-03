<!-- busquedaCuentasBancarias.jsp -->

<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<!-- AJAX -->
<html>

<!-- HEAD -->
<head>
<link id="default" rel="stylesheet" type="text/css"	href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>" />
<link rel="stylesheet" href="/html/js/themes/base/jquery-ui.css" />
<link rel="stylesheet" href="/html/css/jquery-ui.css" />

<script type="text/javascript"	src="<html:rewrite page='/html/js/jquery.js'/>"></script>
<script type="text/javascript"	src="<html:rewrite page='/html/js/jquery-ui.js'/>"></script>
<script src="<html:rewrite page='/html/js/SIGA.js'/>"	type="text/javascript"></script>
<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"	type="text/javascript"></script>
<script	src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/> type="text/javascript"></script>

<style type="text/css">
.ui-autocomplete {
    max-height: 200px;
    overflow-y: auto;
    /* prevent horizontal scrollbar */
    overflow-x: hidden;
    width:350px;
   
  }

 * html .ui-autocomplete {
    height: 200px;
    width:350px;
  }
  .ui-autocomplete-loading {
    background: white url('images/ui-anim_basic_16x16.gif') right center no-repeat;
  }
 </style>
<script type="text/javascript">
jQuery.noConflict();
	

	/*jQuery(function() {
		
	   jQuery( "#bancoAjaxIn" ).autocomplete({
      		source: function( request, response ) {
    	 
    	  
    	  	jQuery.ajax({
              type: "POST",
              url: "/SIGA/FAC_CuentasBancarias.do?modo=consultaBancos",
              data: jQuery('form').serialize(),
              contentType: "application/x-www-form-urlencoded;charset=ISO-8859-15",
              success: function( json ) {
            	  response(jQuery.map(json.listaBancos, function (item) {
                      return {
                          label: item.descripcionbanco,
                          value: item.codigobanco
                          
                      }
            	  }));
            	 
                }
              
             
          });
      },
  
      minLength: 7,
      select: function( event, ui ) {
    	  jQuery("#codigoBanco").val(ui.item.value);
    	  alert(ui.item.label);
    	  jQuery("#bancoAjaxIn").val(ui.item.label);
    	  return false;
        
      },
      open: function( event, ui ) {jQuery("ul.ui-autocomplete").css("z-index", "99");}
    
    });
  });
	*/
	function buscar(){
		sub();
		jQuery.ajax({
            type: "POST",
            url: "/SIGA/FAC_CuentasBancarias.do?modo=buscar",
            data:jQuery('form').serialize(),
            contentType: "application/x-www-form-urlencoded;charset=UTF-8",
            success: function(json){
            	jQuery('#divListadoCuentasBancarias').html(json);
            	//jQuery('#CuentasBancariaForm.modo')="abrir";
				fin();
            },
            error: function(e){
				fin();
                alert('Error de comunicación',"error");
            }
        });
		ajusteAlto('divListadoCuentasBancarias');
		
	}
	function accionNuevo(){
		document.CuentasBancariasForm.modo.value = "nuevo";
		var resultado=ventaModalGeneral(document.CuentasBancariasForm.name,"M");
		if(resultado=='MODIFICADO'){
			buscar();
		}
		
	}
	function editar(fila){
		var idCuentaBancariaFila = document.getElementById("idCuentaBancaria_"+fila).value;
		document.CuentasBancariasForm.idCuentaBancaria.value = idCuentaBancariaFila;
		document.CuentasBancariasForm.modo.value="editar";
	  	var resultado = ventaModalGeneral(document.CuentasBancariasForm.name,"M");
	  	if(resultado && resultado=='MODIFICADO'){
	  		buscar();
			//refrescarLocal();
	  	}
			
	}
	function borrarFila(fila)
	{
		
		var idCuentaBancariaFila = document.getElementById("idCuentaBancaria_"+fila).value;
		document.CuentasBancariasForm.idCuentaBancaria.value = idCuentaBancariaFila;
		document.CuentasBancariasForm.modo.value="borrar";
		document.CuentasBancariasForm.target="submitArea";
		document.CuentasBancariasForm.submit();
		if(1==1)
			buscar();
		
	 }
	
	
	function consultar(fila){
		var idCuentaBancariaFila = document.getElementById("idCuentaBancaria_"+fila).value;
		document.CuentasBancariasForm.idCuentaBancaria.value = idCuentaBancariaFila;
		document.CuentasBancariasForm.modo.value="consultar";
	  	ventaModalGeneral(document.CuentasBancariasForm.name,"M");
	  		
	}
	
  </script>

<!-- INICIO: TITULO Y LOCALIZACION -->
<siga:Titulo titulo="menu.facturacion.gestionCuentasBancarias" localizacion="menu.facturacion.localizacion" />

</head>

<body>


	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<bean:define id="path" name="org.apache.struts.action.mapping.instance"
		property="path" scope="request" />
	<html:form action="${path}" method="POST" target="mainWorkArea">
		<html:hidden property="modo" />
		<html:hidden property="idInstitucion" />
		<html:hidden property="idCuentaBancaria" />
		<input type="hidden" name="actionModal" />

		<table width="100%" border="0">
			<tr>
				<td width="10%"></td>
				<td width="40%"></td>
				<td width="10%"></td>
				<td width="40%"></td>
			</tr>
			
			
			<tr>
				<td class="labelText"><bean:message key="facturacion.cuentasBancarias.banco" /></td>
				<td colspan="3">
					<table>
						<tr>
							<td><html:select  styleId="codigoBanco" property="codigoBanco" styleClass="boxCombo" style="width:250px;">
									<html:option value=""/>
							
						
									<c:forEach items="${listaBancos}" var="banco">
										<html:option value="${banco.codigo}"><c:out value="${banco.nombre}"/></html:option>
									</c:forEach>
								</html:select>
							
							<!--   <html:text styleId="bancoAjaxIn"	property="bancoAjaxIn"  maxlength="200"		styleClass="box" />
							&nbsp;
							<html:text styleId="codigoBanco" property="codigoBanco" size="4" maxlength="9" styleClass="box" />
								-->
								
								
								
								
							</td>
							<td class="labelText">-</td>
							<td><html:text styleId="sucursalBanco"
									property="sucursalBanco" size="4" maxlength="4"
									styleClass="box" /></td>
							<td class="labelText">-</td>

							<td><html:text styleId="digControlBanco"
									property="digControlBanco" size="2" maxlength="2"
									styleClass="box" /></td>
							<td class="labelText">-</td>
							<td><html:text styleId="cuentaBanco" property="cuentaBanco"
									size="10" maxlength="10" styleClass="box" /></td>

						</tr>
					</table> </>


				</td>


			</tr>

			<tr>

				<td class="labelText"><bean:message key="facturacion.cuentasBancarias.sjcs" /></td>
				<td align="left"><html:select styleId="sjcs" property="sjcs"
						styleClass="boxCombo">
						<html:option value="">
							
						</html:option>
						<html:option value="1">
							<siga:Idioma key="general.boton.yes" />
						</html:option>
						<html:option value="0">
							<siga:Idioma key="general.boton.no" />
						</html:option>
					</html:select></td>
				<td class="labelText"><bean:message key="facturacion.cuentasBancarias.baja" /></td>
				<td align="left"><html:select styleId="fechaBaja"
						property="fechaBaja" styleClass="boxCombo">
						<html:option value="">
							
						</html:option>
						<html:option value="1">
							<siga:Idioma key="general.boton.yes" />
						</html:option>
						<html:option value="0">
							<siga:Idioma key="general.boton.no" />
						</html:option>
					</html:select></td>

			</tr>

			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
			

		</table>

				<siga:ConjBotonesBusqueda botones="B"/>

		
</html:form>
		<div id="divListadoCuentasBancarias"	style='height: 48px; position: absolute; width: 100%; overflow-y: auto'>
			<table id='listadoCuentasBancarias' border='1' align='center' width='100%'
				cellspacing='0' cellpadding='0' style='table-layout: fixed'>
			</table>
		</div>
		
		
		<siga:ConjBotonesAccion botones="N" clase="botonesDetalle"/>

	<!-- FIN: BOTONES BUSQUEDA -->
	<iframe name="submitArea"
		src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
		style="display: none"></iframe>

<script>

</script>

</body>


</html>