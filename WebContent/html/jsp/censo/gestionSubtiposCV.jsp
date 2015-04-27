<!DOCTYPE html>
<html>
<head>
<!-- gestionSubtiposCV.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>


	<!-- HEAD -->
<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/validacionStruts.js'/>" ></script>
	<script type="text/javascript" src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>"></script>
	
  	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>">
  	
  	
<siga:Titulo  titulo="menu.cen.subtiposDatosCurriculares" localizacion="censo.tiposDatosCurriculares.localizacion"/>



		

<script type="text/javascript">
	jQuery.noConflict();
	
	function refrescarLocal(){
		buscarTipos();
	}
	
	
	function buscar() {
		return buscarTipos();
	}
	
	
	function buscarTipos() {
		/*var buts = document.getElementsByTagName("input");
		for ( var b = 0; b < buts.length; b++) {
			if (buts[b].type == 'button') {
				jQuery(buts[b]).attr("disabled", "disabled");
			}
		}*/
		
		sub();
		var idInstitucion = document.SubtiposCVForm.idInstitucion.value;
		var tipoDescripcion = document.SubtiposCVForm.tipoDescripcion.value;
		var subTipo1Descripcion = document.SubtiposCVForm.subTipo1Descripcion.value;
		var subTipo2Descripcion = document.SubtiposCVForm.subTipo2Descripcion.value;
		var data = "idInstitucion="+idInstitucion;
		if(tipoDescripcion!='')
			data += "&tipoDescripcion="+tipoDescripcion;
		if(subTipo1Descripcion!='')
			data += "&subTipo1Descripcion="+subTipo1Descripcion;
		if(subTipo2Descripcion!='')
			data += "&subTipo2Descripcion="+subTipo2Descripcion;
        
	    
	   	jQuery.ajax({
        	type: "POST",
            url: "/SIGA/CEN_GestionSubtiposCV.do?modo=getAjaxBusqueda",
            data: data,
            success: function(response){
            	fin();
            	
                jQuery('#divListado').html(response);
            },
            error: function(e){
            	fin();
                alert('Error: ' + e);
            }
        });
    }
	
	function nuevo(){
		jQuery("#idTipoCVInsercion").val("");
		jQuery("#subTipoDescripcionInsercion").val("");
		jQuery("#subTipoCodigoExtInsercion").val("");
		
		
		document.FormularioGestion.modo.value = "insertar";
		openDialog();
	}
	
	function openDialog(){
		jQuery("#dialogoInsercion").dialog(
				{
				      height: 270,
				      width: 525,
				      modal: true,
				      resizable: false,
				      buttons: {
				    	  "Guardar": { id: 'Guardar', text: '<siga:Idioma key="general.boton.guardar"/>', click: function(){ accionInsercion(); }},
				          "Cerrar": { id: 'Cerrar', text: '<siga:Idioma key="general.boton.close"/>', click: function(){closeDialog("dialogoInsercion");}}
				      }
				}
			);
			
		jQuery(".ui-widget-overlay").css("opacity","0");
	}
	
	function accionInsercion(){
		//sub();
		
		
		
		document.forms['FormularioGestion'].idTipoCV.value =jQuery('select#idTipoCVInsercion option:selected').val();  
		document.forms['FormularioGestion'].subTipo.value = jQuery('input:radio[name=subTipoInsercion]:checked').val();
		document.forms['FormularioGestion'].subTipoDescripcion.value = jQuery("#subTipoDescripcionInsercion").val();
		document.forms['FormularioGestion'].subTipoCodigoExt.value = jQuery("#subTipoCodigoExtInsercion").val();
		error = '';
		if(document.forms['FormularioGestion'].idTipoCV.value==''){
			error += "<siga:Idioma key='errors.required' arg0='censo.tiposDatosCurriculares.tipo.literal'/>"+ '\n';
			
		}
		if(document.forms['FormularioGestion'].subTipo.value==''){
			error += "<siga:Idioma key='errors.required' arg0='censo.tiposDatosCurriculares.subtipo.literal'/>"+ '\n';
			
		}
		if(document.forms['FormularioGestion'].subTipoDescripcion.value==''){
			error += "<siga:Idioma key='errors.required' arg0='censo.tiposDatosCurriculares.descripcion.literal'/>"+ '\n';
			
		}
		if(document.forms['FormularioGestion'].subTipoCodigoExt.value==''){
			error += "<siga:Idioma key='errors.required' arg0='censo.tiposDatosCurriculares.codExterno.literal'/>";
			
		}
		
		if (error!=''){
			alert(error);
			//fin();
			return false;
		}
		closeDialog('dialogoInsercion');
		document.forms['FormularioGestion'].submit();
		
	}
	function closeDialog(dialogo){
		 jQuery("#"+dialogo).dialog("close"); 
	}
	
	
	
	
	function accionActualizar(){
		
	
		document.forms['FormularioGestion'].subTipo.value =""; 
		document.forms['FormularioGestion'].subTipoCodigoExt.value=""; 
		document.forms['FormularioGestion'].subTipoDescripcion.value ="";

		document.forms['FormularioGestion'].idTipoCV.value = jQuery("#idTipoCVEdicion").val();  

		document.forms['FormularioGestion'].subTipo1IdTipo.value = jQuery("#subTipo1IdTipoEdicion").val();
		document.forms['FormularioGestion'].subTipo1IdInstitucion.value = jQuery("#subTipo1IdInstitucionEdicion").val();
		document.forms['FormularioGestion'].subTipo1CodigoExt.value = jQuery("#subTipo1CodigoExtEdicion").val();
		document.forms['FormularioGestion'].subTipo1Descripcion.value = jQuery("#subTipo1DescripcionEdicion").val();
		document.forms['FormularioGestion'].subTipo1IdRecursoDescripcion.value = jQuery("#subTipo1IdRecursoDescripcionEdicion").val();
		
		document.forms['FormularioGestion'].subTipo2IdTipo.value = jQuery("#subTipo2IdTipoEdicion").val();
		document.forms['FormularioGestion'].subTipo2IdInstitucion.value = jQuery("#subTipo2IdInstitucionEdicion").val();
		document.forms['FormularioGestion'].subTipo2CodigoExt.value = jQuery("#subTipo2CodigoExtEdicion").val();
		document.forms['FormularioGestion'].subTipo2Descripcion.value = jQuery("#subTipo2DescripcionEdicion").val();
		document.forms['FormularioGestion'].subTipo2IdRecursoDescripcion.value = jQuery("#subTipo2IdRecursoDescripcionEdicion").val();
		
		error = '';
		
		if(document.forms['FormularioGestion'].subTipo1IdTipo.value!==''){
			if(document.forms['FormularioGestion'].subTipo1Descripcion.value==''){
				error += "<siga:Idioma key='errors.required' arg0='censo.tiposDatosCurriculares.descripcion.literal'/>"+ '\n';
			}
			if(document.forms['FormularioGestion'].subTipo1CodigoExt.value==''){
				error += "<siga:Idioma key='errors.required' arg0='censo.tiposDatosCurriculares.codExterno.literal'/>"+ '\n';
			}
			
			
			
		}
		if(document.forms['FormularioGestion'].subTipo2IdTipo.value!==''){
			if(document.forms['FormularioGestion'].subTipo2Descripcion.value==''){
				error += "<siga:Idioma key='errors.required' arg0='censo.tiposDatosCurriculares.descripcion.literal'/>"+ '\n';
				
			}
			if(document.forms['FormularioGestion'].subTipo2CodigoExt.value==''){
				error += "<siga:Idioma key='errors.required' arg0='censo.tiposDatosCurriculares.codExterno.literal'/>"+ '\n';
			}
			
		}
		
		
		
		
		if (error!=''){
			alert(error);
			//fin();
			return false;
		}
		msjConfirmacion = '';
		if(document.forms['FormularioGestion'].subTipo2IdTipo.value!=''){
			msjConfirmacion = '<siga:Idioma key="censo.tiposDatosCurriculares.aviso.modificaSubtipo2.literal"/>';
			msjConfirmacion += jQuery("#descripcionEdicion").val();
			msjConfirmacion +="\n"
		}
		msjConfirmacion +=	'<siga:Idioma key="messages.confirm.updateData"/>';
		if (!confirm(msjConfirmacion))
			return false;
		
		closeDialog('dialogoEdicion');
		document.forms['FormularioGestion'].submit();
		
		
	}
	
	function accionBorrar(){
		
		document.forms['FormularioGestion'].idTipoCV.value = jQuery("#idTipoCVBorrado").val();  
		
		document.forms['FormularioGestion'].subTipo1IdTipo.value = jQuery("#subTipo1IdTipoBorrado").val();
		document.forms['FormularioGestion'].subTipo1IdInstitucion.value = jQuery("#subTipo1IdInstitucionBorrado").val();
		document.forms['FormularioGestion'].subTipo1IdRecursoDescripcion.value = jQuery("#subTipo1IdRecursoDescripcionBorrado").val();
		
		document.forms['FormularioGestion'].subTipo2IdTipo.value = jQuery("#subTipo2IdTipoBorrado").val();
		document.forms['FormularioGestion'].subTipo2IdInstitucion.value = jQuery("#subTipo2IdInstitucionBorrado").val();
		document.forms['FormularioGestion'].subTipo2IdRecursoDescripcion.value = jQuery("#subTipo2IdRecursoDescripcionBorrado").val();
		error = '';
		
		subtiposBorrado =  document.getElementsByName("subTipoBorrado");
		for ( var i = 0; i < subtiposBorrado.length; i++) {
			if(subtiposBorrado[i].checked){
				document.forms['FormularioGestion'].subTipo.value = subtiposBorrado[i].value;
				break;
			}
			
		}
		if(document.forms['FormularioGestion'].subTipo.value ==''){
			error += "<siga:Idioma key='errors.required' arg0='censo.tiposDatosCurriculares.subtipo.literal'/>";
		}
		if (error!=''){
			alert(error);
			//fin();
			return false;
		}
		msjConfirmacion = "";
		if(document.forms['FormularioGestion'].subTipo.value=='2'){
			msjConfirmacion = '<siga:Idioma key="censo.tiposDatosCurriculares.aviso.eliminaSubtipo2.literal"/>';
			msjConfirmacion += jQuery("#descripcionBorrado").val();
			msjConfirmacion +="\n"
		}
		msjConfirmacion +=	'<siga:Idioma key="messages.deleteConfirmation"/>';
		if (!confirm(msjConfirmacion))
			return false;
		
		closeDialog('dialogoBorrado');
		document.forms['FormularioGestion'].submit();
		
		
	}
</script>
</head>
<body >
	<!-- INICIO: CAMPOS DE BUSQUEDA-->
	<bean:define id="path" name="org.apache.struts.action.mapping.instance" property="path" scope="request"/>
	<html:form action="${path}"  method="POST" target="mainWorkArea">
		<html:hidden property="modo"/>
		<html:hidden property="idInstitucion"/>
			<siga:ConjCampos leyenda="general.criterios">
			
				<table width="100%" border="0">
					<tr>
						<td width="150x"></td>
						<td width="110x"></td>
						<td width="110x"></td>
						<td width="110x"></td>
						<td width="150x"></td>
						<td width="330x"></td>
					</tr>
					
					<tr>
						<td class="labelText">
							<bean:message  key="censo.tiposDatosCurriculares.tipo.literal"/>
						</td>
						<td>
							<html:text property="tipoDescripcion" size="10" maxlength="10" styleClass="box"  />
						</td>
						<td class="labelText">
							<bean:message key="censo.tiposDatosCurriculares.subtipo1.literal"/>
						</td>
						<td>
							<html:text property="subTipo1Descripcion" size="10" maxlength="10" styleClass="box"  />
						</td>
						<td class="labelText">
							<bean:message key="censo.tiposDatosCurriculares.subtipo2.literal"/>
						</td>
						<td>
							<html:text property="subTipo2Descripcion" size="10" maxlength="10" styleClass="box"  />
						</td>
					</tr>
				</table>
			</siga:ConjCampos>
		<siga:ConjBotonesBusqueda botones="B,N"  titulo="censo.tiposDatosCurriculares.busqueda"/>
		<div id="divListado" ></div>	
	</html:form>

<html:form action="${path}"  name="FormularioGestion" type ="com.siga.censo.form.SubtiposCVForm" target="submitArea">
  		<html:hidden property="modo"/>
		<html:hidden property="idInstitucion" value='${SubtiposCVForm.idInstitucion}' />
		<html:hidden property="idTipoCV" value=""/>
		<html:hidden property="subTipo" value=""/>
		<html:hidden property="subTipoCodigoExt" value=""/>
		<html:hidden property="subTipoDescripcion" value=""/>
		<html:hidden property="subTipo1IdInstitucion" value=""/>
		<html:hidden property="subTipo1IdTipo" value=""/>
		<html:hidden property="subTipo1CodigoExt" value=""/>
		<html:hidden property="subTipo1Descripcion" value=""/>
		<html:hidden property="subTipo1IdRecursoDescripcion" value=""/>
		
		
		<html:hidden property="subTipo2IdInstitucion" value=""/>
		<html:hidden property="subTipo2IdTipo" value=""/>
		<html:hidden property="subTipo2CodigoExt" value=""/>
		<html:hidden property="subTipo2Descripcion" value=""/>
		<html:hidden property="subTipo2IdRecursoDescripcion" value=""/>
</html:form>

<div id="dialogoInsercion"  title='<bean:message key="censo.tiposDatosCurriculares.dialogo.insercion"/>' style="display:none">
<div>&nbsp;</div>

  	<siga:ConjCampos >
  		
			<div class="labelText">
				<label for="idTipoCVInsercion"   style="width:90px;float:left"><bean:message key="censo.tiposDatosCurriculares.tipo.literal"/></label>
				<select id="idTipoCVInsercion" >
					<option value=""><siga:Idioma key="general.combo.seleccionar" />	</option>
					<c:forEach items="${maestroTiposCV}" var="tipoCV"> 
						<option value="${tipoCV.idtipocv}">${tipoCV.descripcion}</option>
					</c:forEach>
				</select>
				<!--  siga:Select queryId="getCenTiposCv" id="idTipoCV" required="true" /-->
			</div>
			<div class="labelText">
				<label for="subTipo1Insercion"   style="width:100px;float:left"><bean:message key="censo.tiposDatosCurriculares.subtipo1.literal"/></label>
				<input type="radio" name="subTipoInsercion" id="subTipoInsercion" checked="checked"  value="1"/> 
				<label for="subTipo2Insercion"  ><bean:message key="censo.tiposDatosCurriculares.subtipo2.literal"/></label> 
				<input type="radio" name="subTipoInsercion" id="subTipoInsercion"  value="2" />
			</div>
			<div class="labelText">
    			<label for="subTipoCodigoExtInsercion"  style="width:100px;float:left"><bean:message key="censo.tiposDatosCurriculares.codExterno.literal"/></label>
    			<input type="text" id="subTipoCodigoExtInsercion" maxlength="10" size="20" />
   			</div>
			<div class="labelText">
    			<label for="subTipoDescripcionInsercion"   style="width:100px;float:left"><bean:message key="censo.tiposDatosCurriculares.descripcion.literal"/></label>
    			<input type="text" id="subTipoDescripcionInsercion"  maxlength="100" size="40" />
   			</div>
   			
			
		</siga:ConjCampos>

</div>

<div id="dialogoEdicion" title='<bean:message key="censo.tiposDatosCurriculares.dialogo.edicion"/>' style="display:none;">
<div >&nbsp; </div>
  	<siga:ConjCampos >
			<div class="labelText">
				<label for="idTipoCVEdicion"  style="width:90px;float:left"><bean:message key="censo.tiposDatosCurriculares.tipo.literal"/></label>
				<input type="hidden" id="idTipoCVEdicion" />
				<input type="text" style="border: none;" id="descripcionEdicion" readonly="readonly" />
			</div>
			<div id="divSubtipo1Edicion" class="labelText">
    			<label for="subTipo1Edicion" style="width:90px;float:left"><bean:message key="censo.tiposDatosCurriculares.subtipo1.literal"/></label>
    			<input type="hidden" id="subTipo1IdTipoEdicion" />
    			<input type="text" id="subTipo1CodigoExtEdicion" maxlength="10" size="6"/>
    			<input type="text" id="subTipo1DescripcionEdicion"  maxlength="100" size="35"/>
    			<input type="hidden" id="subTipo1IdRecursoDescripcionEdicion" />
    			
   			</div>
   			<div id="divSubtipo2Edicion" class="labelText">
    			<label for="subTipo2Edicion" style="width:90px;float:left"><bean:message key="censo.tiposDatosCurriculares.subtipo2.literal"/></label>
    			<input type="hidden" id="subTipo2IdTipoEdicion" />
    			<input type="text" id="subTipo2CodigoExtEdicion" maxlength="10" size="6"  />
    			<input type="text" id="subTipo2DescripcionEdicion"  maxlength="100" size="35" />
    			<input type="hidden" id="subTipo2IdRecursoDescripcionEdicion" />
   			</div>
   			<div id="divAvisoSubtipo2Edicion"></div>
		</siga:ConjCampos>

</div>

<div id="dialogoBorrado" title='<bean:message key="censo.tiposDatosCurriculares.dialogo.borrado"/>' style="display:none; ">
<div  >&nbsp; </div>
  	<siga:ConjCampos >
			<div class="labelText">
				<label for="idTipoCVBorrado"  class="labelText" style="width:90px;float:left"><bean:message key="censo.tiposDatosCurriculares.tipo.literal"/></label>
				<input type="hidden" id="idTipoCVBorrado" />
				<input type="text" style="border: none;"  id="descripcionBorrado" readonly="readonly" />
			</div>
			<div id="divSubtipo1Borrado" class="labelText">
				<label for="subTipo1Borrado"  class="labelText" style="width:90px;float:left"><bean:message key="censo.tiposDatosCurriculares.subtipo1.literal"/></label>
				<input type="hidden" id="subTipo1IdTipoBorrado" />
				<input type="radio" name="subTipoBorrado" value="1" /> 
    			<input type="text" id="subTipo1DescripcionBorrado"  maxlength="100" size="35" />
    			<input type="hidden" id="subTipo1IdRecursoDescripcionBorrado" />
   			</div>
   			<div id="divSubtipo2Borrado" class="labelText">
    			<label for="subTipo2Borrado"  class="labelText" style="width:90px;float:left"><bean:message key="censo.tiposDatosCurriculares.subtipo2.literal"/></label> 
    			<input type="hidden" id="subTipo2IdTipoBorrado" />
				<input type="radio" name="subTipoBorrado" value="2" />
    			<input type="text" id="subTipo2DescripcionBorrado"  maxlength="100" size="35" />
    			<input type="hidden" id="subTipo2IdRecursoDescripcionBorrado" />
   			</div>
   			<div id="divAvisoSubtipo2Borrado"></div>
		</siga:ConjCampos>

</div>

<!-- FIN: BOTONES BUSQUEDA -->
<iframe name="submitArea" src="<html:rewrite page='/html/jsp/general/blank.jsp'/>" 	style="display: none" />
</body>
</html>