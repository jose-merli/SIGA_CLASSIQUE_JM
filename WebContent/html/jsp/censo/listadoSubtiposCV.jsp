<!-- listadoSubtiposCV.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache">
<%@ page pageEncoding="ISO-8859-15"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Conte nt-Type"
	content="text/html; charset=ISO-8859-15">
<%@ page contentType="text/html" language="java"
	errorPage="/html/jsp/error/errorSIGA.jsp"%>

<!-- TAGLIBS -->
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri="struts-bean.tld" prefix="bean"%>
<%@ taglib uri="struts-html.tld" prefix="html"%>
<%@ taglib uri="struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

	<siga:Table 
			   name="tablaDatos"
			   border="1"
			      columnNames="censo.tiposDatosCurriculares.cod.literal,censo.tiposDatosCurriculares.tipo.literal,censo.tiposDatosCurriculares.cod.literal,censo.tiposDatosCurriculares.subtipo1.literal,censo.tiposDatosCurriculares.cod.literal,censo.tiposDatosCurriculares.subtipo2.literal,"
		   columnSizes="8,22,8,22,8,19,8">
	
		<c:choose>
		<c:when test="${empty tiposDatosCurriculares}">
		
			<tr class="notFound">
			   	<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
			</tr>
		</c:when>
		<c:otherwise>
			
			<c:forEach items="${tiposDatosCurriculares}" var="tiposDatosCurricular" varStatus="status">
				<siga:FilaConIconos	fila='${status.count}'
	  				pintarEspacio="no"
	  				botones="${tiposDatosCurricular.botones}" 
		  			visibleConsulta="false"
	  				clase="listaNonEdit">
					<td align='left'>
						<input type="hidden" id="idInstitucion_${status.count}" value="${tiposDatosCurricular.idInstitucion}"/>
						<input type="hidden" id="idTipoCV_${status.count}" value="${tiposDatosCurricular.idTipoCV}"/>
						<input type="hidden" id="descripcion_${status.count}" value="${tiposDatosCurricular.tipoDescripcion}"/>
						
						<input type="hidden" id="subTipo1IdInstitucion_${status.count}" value="${tiposDatosCurricular.subTipo1IdInstitucion}"/>
						<input type="hidden" id="subTipo1IdTipo_${status.count}" value="${tiposDatosCurricular.subTipo1IdTipo}"/>
						<input type="hidden" id="subTipo1CodigoExt_${status.count}" value="${tiposDatosCurricular.subTipo1CodigoExt}"/>
						<input type="hidden" id="subTipo1Descripcion_${status.count}" value="${tiposDatosCurricular.subTipo1Descripcion}"/>
						<input type="hidden" id="subTipo1IdRecursoDescripcion_${status.count}" value="${tiposDatosCurricular.subTipo1IdRecursoDescripcion}"/>
						
						<input type="hidden" id="subTipo2IdInstitucion_${status.count}" value="${tiposDatosCurricular.subTipo2IdInstitucion}"/>
						<input type="hidden" id="subTipo2IdTipo_${status.count}" value="${tiposDatosCurricular.subTipo2IdTipo}"/>
						<input type="hidden" id="subTipo2CodigoExt_${status.count}" value="${tiposDatosCurricular.subTipo2CodigoExt}"/>
						<input type="hidden" id="subTipo2Descripcion_${status.count}" value="${tiposDatosCurricular.subTipo2Descripcion}"/>
						<input type="hidden" id="subTipo2IdRecursoDescripcion_${status.count}" value="${tiposDatosCurricular.subTipo2IdRecursoDescripcion}"/>
						<c:out value="${tiposDatosCurricular.codigoExt}"/>&nbsp;	
						
 					</td>
 					<td align='left'>
 						<c:out value="${tiposDatosCurricular.tipoDescripcion}"/>
					</td>
					<td align='left'>
						<c:out value="${tiposDatosCurricular.subTipo1CodigoExt}"/>&nbsp;
					</td>
 					<td align='left'>
						<c:out value="${tiposDatosCurricular.subTipo1Descripcion}"/>&nbsp;
					</td>
					<td align='left'>
						<c:out value="${tiposDatosCurricular.subTipo2CodigoExt}"/>&nbsp;
					</td>
					<td align='left'>
						<c:out value="${tiposDatosCurricular.subTipo2Descripcion}"/>&nbsp;
					</td>
					
				</siga:FilaConIconos>
			</c:forEach>
		</c:otherwise>
	</c:choose>

</siga:Table>
<script>

function editar(fila) {
	var idInstitucion = 'idInstitucion_' + fila ;
	var idTipoCV = 'idTipoCV_' + fila ;
	var descripcion = 'descripcion_' + fila ;
	var subTipo1IdTipo = 'subTipo1IdTipo_' + fila ;
	var subTipo1IdInstitucion = 'subTipo1IdInstitucion_' + fila ;
	var subTipo1CodigoExt = 'subTipo1CodigoExt_' + fila ;
	var subTipo1Descripcion = 'subTipo1Descripcion_' + fila ;
	var subTipo1IdRecursoDescripcion = 'subTipo1IdRecursoDescripcion_' + fila ;
	
	var subTipo2IdTipo = 'subTipo2IdTipo_' + fila ;
	var subTipo2IdInstitucion = 'subTipo2IdInstitucion_' + fila ;
	var subTipo2CodigoExt = 'subTipo2CodigoExt_' + fila ;
	var subTipo2Descripcion = 'subTipo2Descripcion_' + fila ;
	var subTipo2IdRecursoDescripcion = 'subTipo2IdRecursoDescripcion_' + fila ;
	
	
	jQuery("#idInstitucion").val(jQuery("#"+idInstitucion).val());
	jQuery("#idTipoCVEdicion").val(jQuery("#"+idTipoCV).val());
	jQuery("#descripcionEdicion").val(jQuery("#"+descripcion).val());
	
	if(jQuery("#"+idInstitucion).val()!=jQuery("#"+subTipo1IdInstitucion).val()){
		jQuery("#divSubtipo1Edicion").hide();
	}else{
		jQuery("#divSubtipo1Edicion").show();
		jQuery("#subTipo1IdTipoEdicion").val(jQuery("#"+subTipo1IdTipo).val());
		jQuery("#subTipo1CodigoExtEdicion").val(jQuery("#"+subTipo1CodigoExt).val());
		jQuery("#subTipo1DescripcionEdicion").val(jQuery("#"+subTipo1Descripcion).val());
		jQuery("#subTipo1IdRecursoDescripcionEdicion").val(jQuery("#"+subTipo1IdRecursoDescripcion).val());
	}
	if(jQuery("#"+subTipo2IdTipo).val()=='' ||jQuery("#"+idInstitucion).val()!=jQuery("#"+subTipo2IdInstitucion).val()){
		jQuery("#divSubtipo2Edicion").hide();
		jQuery("#divAvisoSubtipo2Edicion").hide();
	}else{
		jQuery("#divSubtipo2Edicion").show();
		jQuery("#divAvisoSubtipo2Edicion").show();
		jQuery("#subTipo2IdTipoEdicion").val(jQuery("#"+subTipo2IdTipo).val());
		jQuery("#subTipo2CodigoExtEdicion").val(jQuery("#"+subTipo2CodigoExt).val());
		jQuery("#subTipo2DescripcionEdicion").val(jQuery("#"+subTipo2Descripcion).val());
		jQuery("#subTipo2IdRecursoDescripcionEdicion").val(jQuery("#"+subTipo2IdRecursoDescripcion).val());
	}
	document.FormularioGestion.modo.value = "actualizar";
	
	
	jQuery("#dialogoEdicion").dialog(
			{
			      height: 270,
			      width: 525,
			      modal: true,
			      resizable: false,
			      buttons: {
			    	  "Guardar": { id: 'Actualizar', text: '<siga:Idioma key="general.boton.guardar"/>', click: function(){ accionActualizar(); }},
			          "Cerrar": { id: 'Cerrar', text: '<siga:Idioma key="general.boton.close"/>', click: function(){closeDialog('dialogoEdicion');}}
			      }
			}
		);
		
	jQuery(".ui-widget-overlay").css("opacity","0");
	
	
	
 }
function borrar(fila) {		
	var idInstitucion = 'idInstitucion_' + fila ;
	var idTipoCV = 'idTipoCV_' + fila ;
	var descripcion = 'descripcion_' + fila ;
	
	var subTipo1IdTipo = 'subTipo1IdTipo_' + fila ;
	var subTipo1IdInstitucion = 'subTipo1IdInstitucion_' + fila ;
	var subTipo1CodigoExt = 'subTipo1CodigoExt_' + fila ;
	var subTipo1Descripcion = 'subTipo1Descripcion_' + fila ;
	var subTipo1IdRecursoDescripcion = 'subTipo1IdRecursoDescripcion_' + fila ;
	
	
	var subTipo2IdTipo = 'subTipo2IdTipo_' + fila ;
	var subTipo2IdInstitucion = 'subTipo2IdInstitucion_' + fila ;
	var subTipo2CodigoExt = 'subTipo2CodigoExt_' + fila ;
	var subTipo2Descripcion = 'subTipo2Descripcion_' + fila ;
	var subTipo2IdRecursoDescripcion = 'subTipo2IdRecursoDescripcion_' + fila ;
	
	jQuery("#idInstitucion").val(jQuery("#"+idInstitucion).val());
	jQuery("#idTipoCVBorrado").val(jQuery("#"+idTipoCV).val());
	jQuery("#descripcionBorrado").val(jQuery("#"+descripcion).val());
	
	if(jQuery("#"+idInstitucion).val()!=jQuery("#"+subTipo1IdInstitucion).val()){
		jQuery("#divSubtipo1Borrado").hide();
	}else{
		jQuery("#divSubtipo1Borrado").show();
		jQuery("#subTipo1IdTipoBorrado").val(jQuery("#"+subTipo1IdTipo).val());
		jQuery("#subTipo1CodigoExtBorrado").val(jQuery("#"+subTipo1CodigoExt).val());
		jQuery("#subTipo1DescripcionBorrado").val(jQuery("#"+subTipo1Descripcion).val());
		jQuery("#subTipo1IdRecursoDescripcionBorrado").val(jQuery("#"+subTipo1IdRecursoDescripcion).val());
	}
	
	if(jQuery("#"+subTipo2IdTipo).val()=='' ||jQuery("#"+idInstitucion).val()!=jQuery("#"+subTipo2IdInstitucion).val()){
		jQuery("#divSubtipo2Borrado").hide();
		jQuery("#divAvisoSubtipo2Borrado").hide();
	}else{
		jQuery("#divSubtipo2Borrado").show();
		jQuery("#divAvisoSubtipo2Borrado").show();
		jQuery("#subTipo2IdTipoBorrado").val(jQuery("#"+subTipo2IdTipo).val());
		jQuery("#subTipo2CodigoExtBorrado").val(jQuery("#"+subTipo2CodigoExt).val());
		jQuery("#subTipo2DescripcionBorrado").val(jQuery("#"+subTipo2Descripcion).val());
		jQuery("#subTipo2IdRecursoDescripcionBorrado").val(jQuery("#"+subTipo2IdRecursoDescripcion).val());
	}
	document.FormularioGestion.modo.value = "borrar";
	
	
	jQuery("#dialogoBorrado").dialog(
			{
			      height: 270,
			      width: 525,
			      modal: true,
			      resizable: false,
			      buttons: {
			    	  "Guardar": { id: 'Borrar', text: '<siga:Idioma key="general.boton.borrar"/>', click: function(){ accionBorrar(); }},
			          "Cerrar": { id: 'Cerrar', text: '<siga:Idioma key="general.boton.close"/>', click: function(){closeDialog('dialogoBorrado');}}
			      }
			}
		);
		
	jQuery(".ui-widget-overlay").css("opacity","0");
	
	
	
 }
</script>

