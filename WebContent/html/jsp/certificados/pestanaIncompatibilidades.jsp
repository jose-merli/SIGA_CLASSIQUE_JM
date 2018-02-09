<!DOCTYPE html>
<html>
<head>
<!-- pestanaIncompatibilidades.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.Utilidades.*"%>
<%@ page import="com.siga.administracion.*"%>
<%@ page import="java.util.Properties"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Hashtable"%>

<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Vector tiposCertificados = (Vector)request.getAttribute("tiposCertificados");
	Vector vDatos = (Vector)request.getAttribute("datos");
	
	String sIdInstitucion = (String)request.getAttribute("idInstitucion");
	String sIdTipoProducto = (String)request.getAttribute("idTipoProducto");
	String sIdProducto = (String)request.getAttribute("idProducto");
	String sIdProductoInstitucion = (String)request.getAttribute("idProductoInstitucion");
	
	String sCertificado=(String)request.getAttribute("certificado");
	boolean bEditable = ((String)request.getAttribute("editable")).equals("1");
	
	String descCertificado = (String)request.getAttribute("descripcionCertificado");

	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
%>	
	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
	<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>

	<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>"/>
	
	<siga:Titulo titulo="Incompatibilidades" localizacion="certificados.mantenimientoCertificados.plantillas.localizacion"/>
	
	<style type="text/css">
	 .editLabel{
	 	width:150px;
	 	float:left;
	 }
	 .numberClass{
	 	text-align: right;
	 }
	 .conjunto{padding: 2px;float:left; width:100%;color:black !important;}
	 .divModal .labelTextValue{color:#555 }
	 .divModal .labelText{color:#555 }
	 
	 .checkSelected{font-style:italic;color: green; font-weight: bold !important;}
	 .checkUnselected{font-style:italic;color: red; font-weight: normal !important;}
	 .checkInitialyChecked{font-weight:bold;}
	 .ui-dialog .ui-dialog-buttonpane{margin:15px 0 0 0  ;padding: 0;}
	 .ui-dialog .ui-dialog-content{overflow-x: hidden}

	</style>

</head>

<body class="detallePestanas" onload="buscarTipos();">

	<html:form action="/CER_Incompatibilidades.do" method="POST" target="submitArea">
		<html:hidden styleId="modo" property="modo" value=""/>
		
		<html:hidden styleId="idInstitucion" property="idInstitucion"  value="<%=sIdInstitucion%>"/>
		<html:hidden styleId="idTipoProducto" property="idTipoProducto"  value="<%=sIdTipoProducto%>"/>
		<html:hidden styleId="idProducto"  property="idProducto" value="<%=sIdProducto%>"/>
		<html:hidden styleId="idProductoInstitucion"  property="idProductoInstitucion" value="<%=sIdProductoInstitucion%>"/>
		
		<html:hidden styleId="certificado"  property="certificado" value="<%=sCertificado%>"/>
	</html:form>

	<!-- TITULO INFORMATIVO -->
	<table class="tablaTitulo"  cellspacing="0">		
		<tr>		
			<td class="titulitosDatos">
				<siga:Idioma key="certificados.mantenimiento.literal.certificado"/>:&nbsp;<%=descCertificado%>
			</td>
		</tr>
	</table>		

	<siga:Table 
   	      name="tablaDatos"
   		  border="1"
   		  columnNames="certificados.mantenimiento.literal.incompatible,certificados.mantenimiento.literal.motivo,"
   		  columnSizes="20,70,10">

<%
		if (vDatos==null || vDatos.size()==0) {
%>
		<tr class="notFound">
			<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
		</tr>
<%
		} else {
			String sBotones = bEditable ? "E,B" : "";
			
			for (int i=0; i<vDatos.size(); i++) {
				Hashtable htDatos = (Hashtable)vDatos.elementAt(i);
%>
		<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="<%=sBotones%>" clase="listaNonEdit">
			<td>
				<input type="hidden" id="oculto<%=""+(i+1)%>_1" name="oculto<%=""+(i+1)%>_1" value="<%=htDatos.get(PysProductosInstitucionBean.C_IDTIPOPRODUCTO)%>">
				<input type="hidden" id="oculto<%=""+(i+1)%>_2" name="oculto<%=""+(i+1)%>_2" value="<%=htDatos.get(PysProductosInstitucionBean.C_IDPRODUCTO)%>">
				<input type="hidden" id="oculto<%=""+(i+1)%>_3" name="oculto<%=""+(i+1)%>_3" value="<%=htDatos.get(PysProductosInstitucionBean.C_IDPRODUCTOINSTITUCION)%>">
				<input type="hidden" id="oculto<%=""+(i+1)%>_4" name="oculto<%=""+(i+1)%>_4" value="<%=htDatos.get(PysProductosInstitucionBean.C_DESCRIPCION)%>">
				<input type="hidden" id="oculto<%=""+(i+1)%>_5" name="oculto<%=""+(i+1)%>_5" value="<%=htDatos.get(CerIncompatibilidadesBean.C_MOTIVO)%>">
				
				<%=htDatos.get(PysProductosInstitucionBean.C_DESCRIPCION)%>
			</td>
			<td>
				<%=htDatos.get(CerIncompatibilidadesBean.C_MOTIVO)%>
			</td>
		</siga:FilaConIconos>
<%
			} //for
		} //else
%>
	</siga:Table>
	
	<script language="JavaScript">
		//Asociada al boton Volver
		function accionVolver()
		{
			MantenimientoCertificadosIncompatibilidadesForm.action = "/SIGA/CER_MantenimientoCertificados.do";
			MantenimientoCertificadosIncompatibilidadesForm.modo.value="abrirConParametros";
			MantenimientoCertificadosIncompatibilidadesForm.submit();
		}

		function refrescarLocal()
		{
			parent.buscar();
		}
		
		function guardarDatos(nuevo){
			if (nuevo) {
	 			ventanaDialogo = "#divNuevo";
				modo = "crearIncompatibilidad";
				idsProducto = jQuery('select#seleccionarCertificado option:selected').val()+"&motivo="+jQuery('#motivoNuevo').val();
			} else {
	 			ventanaDialogo = "#divEdicion";
				modo = "modificarIncompatibilidad";
				idsProducto = "idTipoProd_Incompatible="+jQuery('#idTipoProd_Incompatible').val()+
				"&idProd_Incompatible="+jQuery('#idProd_Incompatible').val()+
				"&idProdInst_Incompatible="+jQuery('#idProdInst_Incompatible').val()+"&motivo="+jQuery('#motivo').val();
			}
			
			jQuery.ajax({ 
				type: "POST",
				url: "/SIGA/CER_Incompatibilidades.do?modo="+modo,
				dataType: "json",
				data: "idInstitucion="+jQuery("#idInstitucion").val()+"&idTipoProducto="+jQuery("#idTipoProducto").val()+
						"&idProducto="+jQuery('#idProducto').val()+"&idProductoInstitucion="+jQuery('#idProductoInstitucion').val()+
						"&"+idsProducto,
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){
					alert(json.msg,'success');
					jQuery(ventanaDialogo).dialog( "close" );
					parent.buscar();
				},
				error: function(e){
					alert(e+":error de comunicacion con el servidor",'error');
				}
			});
			

			//document.forms[1].submit();
		}

		//Asociada al boton Nuevo
		function accionNuevo()
		{
			jQuery("#idTipoProd_Incompatible").val("");
			jQuery("#idProd_Incompatible").val("");
			jQuery("#idProdInst_Incompatible").val("");
			jQuery("#motivoNuevo").val("");

	 		abrir(true, true);
		}
	
		function consultar(fila,id){
			jQuery("#idTipoProd_Incompatible").val(jQuery("#oculto"+fila+"_1").val());
			jQuery("#idProd_Incompatible").val(jQuery("#oculto"+fila+"_2").val());
			jQuery("#idProdInst_Incompatible").val(jQuery("#oculto"+fila+"_3").val());
			jQuery("#editDescripcion").html(jQuery("#oculto"+fila+"_4").val());
			jQuery("#motivo").val(jQuery("#oculto"+fila+"_5").val());

			abrir(false, false);
			jQuery('#divEdicion input').prop('disabled',true);
		}
	
	 	function editar(fila, id) {
			jQuery("#idTipoProd_Incompatible").val(jQuery("#oculto"+fila+"_1").val());
			jQuery("#idProd_Incompatible").val(jQuery("#oculto"+fila+"_2").val());
			jQuery("#idProdInst_Incompatible").val(jQuery("#oculto"+fila+"_3").val());
			jQuery("#editDescripcion").html(jQuery("#oculto"+fila+"_4").val());
			jQuery("#motivo").val(jQuery("#oculto"+fila+"_5").val());

	 		abrir(false, true);
	 		jQuery('#divEdicion input').prop('disabled',false);
	 	}
		
	 	function borrar(fila, id) {
			jQuery("#idTipoProd_Incompatible").val(jQuery("#oculto"+fila+"_1").val());
			jQuery("#idProd_Incompatible").val(jQuery("#oculto"+fila+"_2").val());
			jQuery("#idProdInst_Incompatible").val(jQuery("#oculto"+fila+"_3").val());
			jQuery("#editDescripcion").html(jQuery("#oculto"+fila+"_4").val());
			jQuery("#motivo").val(jQuery("#oculto"+fila+"_5").val());

			jQuery.ajax({ 
				type: "POST",
				url: "/SIGA/CER_Incompatibilidades.do?modo="+"borrarIncompatibilidad",
				dataType: "json",
				data: "idInstitucion="+jQuery("#idInstitucion").val()+"&idTipoProducto="+jQuery("#idTipoProducto").val()+
						"&idProducto="+jQuery('#idProducto').val()+"&idProductoInstitucion="+jQuery('#idProductoInstitucion').val()+
						"&"+"idTipoProd_Incompatible="+jQuery('#idTipoProd_Incompatible').val()+
						"&idProd_Incompatible="+jQuery('#idProd_Incompatible').val()+
						"&idProdInst_Incompatible="+jQuery('#idProdInst_Incompatible').val()+"&motivo="+jQuery('#motivo').val(),
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){
					alert(json.msg,'success');
					parent.buscar();
				},
				error: function(e){
					alert(e+":error de comunicacion con el servidor",'error');
				}
			});
	 	}
	 	
	 	function abrir(nuevo, guardar) {
	 		if (nuevo) {
	 			ventanaDialogo = "#divNuevo";
	 		} else {
	 			ventanaDialogo = "#divEdicion";
	 		}
			jQuery(ventanaDialogo).dialog(
			{
			  minHeight: 0,
		      width: 625,
		      modal: true,
		      resizable: false,
		      position: ['center',20] ,
		      scroll: true,
		      open: function(event, ui) { 
						jQuery(".liTab:visible").first().find('a').trigger('click');
						jQuery(".liTab:visible").blur();
						jQuery(ventanaDialogo).css('top','2px');
				},
		      buttons :  { 
		    	     "btGuardarCerrar" : {
		    	         text: "Guardar y cerrar",
		    	         id: "btGuardarYCerrar",
		    	         click: function(){
		    	        	 guardarDatos(nuevo);
		    	        	 }   
		    	      } ,
		    	      "btCerrar" : {
		    	         text: "Cerrar",
		    	         id: "btCerrar",
		    	         click: function(){
		    	        	 jQuery( this ).dialog( "close" );
		    	         }   
			    	  } 
		    	   }		      
		    });
			
	 		jQuery(".ui-widget-overlay").css("opacity","0");
	 		jQuery(".ui-tabs .ui-tabs-panel").css("padding",'0 2 0 2');
	 		
	 		if(guardar==0){
	 			jQuery('#btGuardarYCerrar').hide() ;
	 		}
	 		if((jQuery(window).height()-150)<jQuery(ventanaDialogo).height()){
	 			jQuery(ventanaDialogo).height(jQuery(window).height()-150);
	 		}
		}
	</script>

	<div id="divNuevo" class="divModal" style="display:none" title="<siga:Idioma key='certificados.mantenimiento.literal.nuevaIncompatibilidad' />">
		<fieldset>
		<div style='width:98%;'>
			<div class='conjunto'>
				<div class="labelText editLabel"><siga:Idioma key="Certificado"/></div>
				<select id="seleccionarCertificado" >
					<option value=""><siga:Idioma key="general.combo.seleccionar" /></option>
					<c:forEach items="${tiposCertificados}" var="tiposCertificados"> 
						<option value="idTipoProd_Incompatible=${tiposCertificados.idTipoProducto}&idProd_Incompatible=${tiposCertificados.idProducto}&idProdInst_Incompatible=${tiposCertificados.idProductoInstitucion}">
							${tiposCertificados.descripcion}
						</option>
					</c:forEach>
				</select>
			</div>
			<div class='conjunto'> <div class="labelText editLabel"><siga:Idioma key="certificados.mantenimiento.literal.motivo"/></div>
				<div id="editMotivo" class="labelTextValue">
					<html:textarea name="MantenimientoCertificadosIncompatibilidadesForm" styleClass="textClass" styleId="motivoNuevo" property="motivo" maxlength="4000"/>
				</div> 
			</div>
		</div>
		</fieldset>
	</div>

	<div id="divEdicion" class="divModal" style="display:none" title="<siga:Idioma key='certificados.mantenimiento.literal.edicionIncompatibilidad' />">
		<html:form action="/CER_Incompatibilidades.do" method="POST" target="submitArea">	
			<html:hidden property = "modo" value = "modificar"/>
			<html:hidden property = "idInstitucion" value = "" styleId="idInstitucion"/>
			<html:hidden property = "idTipoProducto" value = "" styleId="idTipoProducto"/>
			<html:hidden property = "idProducto" value = "" styleId="idProducto"/>
			<html:hidden property = "idProductoInstitucion" value = "" styleId="idProductoInstitucion"/>
			<html:hidden property = "idTipoProd_Incompatible" value = "" styleId="idTipoProd_Incompatible"/>
			<html:hidden property = "idProd_Incompatible" value = "" styleId="idProd_Incompatible"/>
			<html:hidden property = "idProdInst_Incompatible" value = "" styleId="idProdInst_Incompatible"/>
			<fieldset>
			<div style='width:98%;'>
				<div class='conjunto'> <div class="labelText editLabel"><siga:Idioma key="Certificado"/></div><div id="editDescripcion" class="labelTextValue"></div> </div>
				<div class='conjunto'> <div class="labelText editLabel"><siga:Idioma key="Motivo"/></div>
					<div id="editMotivo" class="labelTextValue">
						<html:textarea name="MantenimientoCertificadosIncompatibilidadesForm" styleClass="textClass" styleId="motivo" property="motivo" maxlength="4000"/>
					</div> 
				</div>
			</div>
			</fieldset>
		</html:form>
	</div>


<%
	String sBotones2 = bEditable ? "V,N" : "V";
%>
	<siga:ConjBotonesAccion botones="<%=sBotones2%>" clase="botonesDetalle"/>

	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
</body>
</html>