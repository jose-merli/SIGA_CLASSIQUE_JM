<!DOCTYPE html>
<html>
<head>
<!-- buscarListadoUsuarios.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> 
<meta http-equiv="Cache-Control" content="no-cache">

<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>
<%@ taglib uri="c.tld" prefix="c"%>

<%@ page import="java.util.*"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.atos.utils.*"%>
<%@ page import="com.siga.administracion.*"%>


<% 
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	
	Vector vDatos = (Vector)request.getAttribute("datos");

	request.removeAttribute("datos");
	
	UsrBean userBean = (UsrBean)request.getSession().getAttribute("USRBEAN");
	
%>	


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-1.9.1.js?v=${sessionScope.VERSIONJS}'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
		<script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
  		<link rel="stylesheet" href="<html:rewrite page='/html/js/jquery.ui/css/smoothness/jquery-ui-1.10.3.custom.min.css'/>"/>
  		
		<script>
		var nombreGrupo="";
			function refrescarLocal() {
				parent.buscar();
			}
		</script>
		
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
	<body class="tablaCentralCampos">
		<html:form action="/ADM_GestionarUsuarios.do" method="POST" target="submitArea">
			<html:hidden property = "modo" styleId = "modo"  value = ""/>
		</html:form>	
		
		
			<siga:Table 
					name="tablaDatos"
					border="1"  
					modal="M" 
		   		  	columnNames="administracion.usuarios.literal.nombre,administracion.usuarios.literal.NIF,administracion.usuarios.literal.fechaAlta,administracion.usuarios.literal.activo,administracion.certificados.literal.roles,&nbsp;"
		   		  	columnSizes="30,15,8,7,30,10">

<%
				if (vDatos==null || vDatos.size()==0)
				{
%>
				<tr class="notFound">
	   				<td class="titulitos"><siga:Idioma key="messages.noRecordFound"/></td>
				</tr>
<%
				}
				
				else
				{
			 		for (int i=0; i<vDatos.size(); i++)
			   		{
				  		Hashtable usuario = (Hashtable)vDatos.elementAt(i);
%>
	  			<siga:FilaConIconos fila='<%=""+(i+1)%>' botones="C,E,B" clase="listaNonEdit">
					<td>
						<input type="hidden" id="oculto<%=""+(i+1)%>_1" value="<%=usuario.get("IDUSUARIO")%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_2" value="<%=usuario.get("IDINSTITUCION")%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_3" value="<%=usuario.get("DESCRIPCION")%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_4" value="<%=usuario.get("IDLENGUAJE")%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_5" value="<%=usuario.get("NIF")%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_6" value="<%=usuario.get("ACTIVO")%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_7" value="<%=usuario.get("GRUPOS")%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_8" value="<%=GstDate.getFormatedDateShort(userBean.getLanguage(), usuario.get("FECHAALTA"))%>">
						
						<input type="hidden" id="oculto<%=""+(i+1)%>_11" value="<%=usuario.get("CODIGOEXT")%>">
						<input type="hidden" id="oculto<%=""+(i+1)%>_12" value="<%=usuario.get("GRUPOS")%>">
						
						<%=usuario.get("DESCRIPCION")%>
					</td>
					<td><%=usuario.get("NIF")%></td>
					<td><%=GstDate.getFormatedDateShort(userBean.getLanguage(), usuario.get("FECHAALTA"))%></td>
					<td><%if (usuario.get("ACTIVO").equals("S")) {%><siga:Idioma key="general.boton.yes"/><%} else {%><siga:Idioma key="general.boton.no"/><%}%></td>
					<td>&nbsp;<%=usuario.get("GRUPOS")%></td>
				</siga:FilaConIconos>
<%
					}
				}
%>
			</siga:Table>

	<script type="text/javascript">
	
		function guardarDatos(fila){
//			console.dir(document.forms[1]);
			jQuery("#idInstitucion").val(jQuery("#oculto"+fila+"_2").val());
			jQuery("#idUsuario").val(jQuery("#oculto"+fila+"_1").val());
			var perfilesAlta=[];
			var perfilesString="";
			var altas=[];
			var bajas=[];
			jQuery(".checkSelected").each(function(){
				id=jQuery(this).parent().find('.checkPerfil').attr('id').split("_");
				altas.push([id[0],id[1]]);
			});
			jQuery(".checkUnselected").each(function(){
				id=jQuery(this).parent().find('.checkPerfil').attr('id').split("_");
				bajas.push([id[0],id[1]]);
			});
			if(perfilesString!="") perfilesString = perfilesString.substring(1, perfilesString.length);
			
			jQuery("#gruposUsuario").val(perfilesString);
			
			console.dir(bajas);
			console.dir(altas);
			

			jQuery.ajax({ 
				type: "POST",
				url: "/SIGA/ADM_GestionarUsuarios.do?modo=setDatosUsuario",
				dataType: "json",
				data: "idInstitucion="+jQuery("#idInstitucion").val()+"&idUsuario="+jQuery("#idUsuario").val()+"&activo="+jQuery('#checkActivo').prop('checked')+"&codigoExt="+jQuery('#codigoExt').val()+"&perfilesAlta="+JSON.stringify(altas)+"&perfilesBaja="+JSON.stringify(bajas),
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				success: function(json){
					alert(json.msg,'success');
					jQuery( "#divVentanaModal" ).dialog( "close" );
					parent.buscar();
				},
				error: function(e){
					alert(e+":error de comunicacion con el servidor",'error');
				}
			});
			

			//document.forms[1].submit();
		}
	
		function consultar(fila,id){
			abrir(fila, false);
			jQuery('#divVentanaModal input').prop('disabled',true);
		}
	
	 	function editar(fila, id) {
	 		abrir(fila, true);
	 		filaSel=fila;
	 		jQuery('#divVentanaModal input').prop('disabled',false);
	 	}
	 	
	 	function abrir(fila, guardar) {
			jQuery("#editNombre").html(jQuery("#oculto"+fila+"_3").val());
			jQuery("#editNif").html(jQuery("#oculto"+fila+"_5").val());
			jQuery("#editFechaAlta").html(jQuery("#oculto"+fila+"_8").val());
			jQuery("#codigoExt").val(jQuery("#oculto"+fila+"_11").val());
	
			jQuery("#checkActivo").prop('checked', jQuery("#oculto"+fila+"_6").val()=='S');

			rellenarChecks(jQuery("#oculto"+fila+"_1").val(),jQuery("#oculto"+fila+"_2").val(),fila, guardar);

		 }


	 	function rellenarChecks(idUsuario, idInstitucion, fila, guardar){
	 		jQuery('.liTab').hide();
	 		jQuery(".checkSelected").removeClass("checkSelected");
	 		jQuery(".checkUnselected").removeClass("checkUnselected");
	 		jQuery(".checkInitialyChecked").removeClass("checkInitialyChecked");
	 		jQuery(".checkPerfil").prop('checked', false);
	 		jQuery.ajax({ //Comunicacion jQuery hacia JSP  
   				type: "POST",
				url: "/SIGA/ADM_GestionarUsuarios.do?modo=getPerfilesUsuario",
				data: "idUsuario="+idUsuario+"&idInstitucion="+idInstitucion,
				dataType: "json",
				contentType: "application/x-www-form-urlencoded;charset=UTF-8",
				error: function(e){alert('No se han podido cargar los datos del usuario','error');},
				success: function(json){
					
					if(json.perfilesrol.length>0){
						jQuery("#rolesHolder").show();
						jQuery("#divNoHayRoles").hide();
						for (var r in json.perfilesrol){
							jQuery('#li'+json.perfilesrol[r].CODROL).show();
							var perfs=json.perfilesrol[r].PERFILES;
							perfs=perfs.split(',');
							for(var i=0;i<perfs.length;i++){
								jQuery("#"+json.perfilesrol[r].CODROL+"_"+perfs[i]).prop('checked', true);
								jQuery("#"+json.perfilesrol[r].CODROL+"_"+perfs[i]).parent().find('label').addClass('checkInitialyChecked');
							}
						}
						jQuery("#divRoles").tabs();
						jQuery("#divRoles").show();
					}else{
						jQuery("#rolesHolder").hide();
						jQuery("#divNoHayRoles").show();
					}

					jQuery("#divVentanaModal").dialog(
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
								jQuery("#divVentanaModal").css('top','2px');
						},
				      buttons :  { 
				    	     "btGuardarCerrar" : {
				    	         text: "Guardar y cerrar",
				    	         id: "btGuardarYCerrar",
				    	         click: function(){
				    	        	 guardarDatos(fila);
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
			 		jQuery("#checkActivo").blur(); 
			 		jQuery("#divRoles").css('border','0');
			 		jQuery(".ui-widget-overlay").css("opacity","0");
			 		jQuery(".ui-tabs .ui-tabs-panel").css("padding",'0 2 0 2');
			 		
			 		if(guardar==0){
			 			jQuery('#btGuardarYCerrar').hide() ;
			 		}
			 		if((jQuery(window).height()-150)<jQuery("#divVentanaModal").height()){
			 			jQuery("#divVentanaModal").height(jQuery(window).height()-150);
			 		}
				}});	
	 	}
	 	
	 	jQuery(document).ready(function(){
	 		jQuery('.checkPerfil').change(
	  	 			function(){
	  	 				label=jQuery(this).parent().find('label');
	  	 				if(jQuery(this).prop("checked")){
	  	 					if(label.hasClass('checkInitialyChecked'))	  	 						
	  	 						label.removeClass('checkUnselected');
	  	 					else  	 						
	  	 						label.addClass('checkSelected');
	  	 				}else{
	  	 					if(label.hasClass('checkInitialyChecked'))
	  	 						label.addClass('checkUnselected');
	  	 					else
	  	 						label.removeClass('checkSelected');
	  	 				}

	  	 			}
	  	 		);
	 		
	 	});

	</script>
	
	<div id="divVentanaModal" class="divModal" style="display:none" title="Administración de usuario">
<html:form action="/ADM_GestionarUsuarios.do" method="POST" target="submitArea">	
	<html:hidden property = "modo" value = "modificar"/>
	<html:hidden property = "idInstitucion" value = "" styleId="idInstitucion"/>
	<html:hidden property = "idUsuario" value = "" styleId="idUsuario"/>
	<html:hidden property = "gruposUsuario" value = "" styleId="gruposUsuario"/>
	<fieldset>
	<div style='width:98%;'>
		<div class='conjunto'> <div class="labelText editLabel"><siga:Idioma key="administracion.usuarios.literal.nombre"/></div><div id="editNombre" class="labelTextValue"></div> </div>
		<div class='conjunto'> <div class="labelText editLabel"><siga:Idioma key="administracion.usuarios.literal.NIF"/></div><div id="editNif" class="labelTextValue"></div> </div>
		<div class='conjunto'> <div class="labelText editLabel"><siga:Idioma key="administracion.usuarios.literal.fechaAlta"/></div><div id="editFechaAlta" class="labelTextValue"></div> </div>
		<div class='conjunto'> <div class="labelText editLabel"><siga:Idioma key="administracion.usuarios.literal.activo"/></div><div><input type="checkbox" id="checkActivo" name="activo"></div> </div>
		<!-- div class='conjunto'> <div class="labelText editLabel"><siga:Idioma key="administracion.certificados.literal.roles"/></div><div id="editRoles" class="labelTextValue"></div> </div -->
			
		<div class='conjunto'> <div class="labelText editLabel"><siga:Idioma key="administracion.certificados.literal.codigoExt"/></div>
			<div id="editCodigoExt" class="labelTextValue">
				<html:text name="listadoUsuariosForm" styleClass="numberClass" styleId="codigoExt" property="codigoExt" size="5" maxlength="10"/>
			</div> 
		</div>
	</div>
	</fieldset>

	<div id='rolesHolder'>
	<div id='divRoles' style='padding-bottom:20px;'>
		<!-- Pintamos las pestanas. Una por cada rol -->
		<ul>
			<c:forEach items='${roles}' var="r" varStatus="j" >
				<li id="li${r.codigoExt}" class='liTab'><a href="#tab${r.codigoExt}">${r.descripcion}</a></li>
			</c:forEach>	
		</ul>
		
		<!-- Pintamos los posibles perfiles por cada rol -->
		<c:forEach items='${roles}' var="r" varStatus="j" >
			<div class='divTab' id="tab${r.codigoExt}">
					<c:forEach items='${perfiles}' var="p" varStatus="i" >
						<div class='conjunto'><div class="labelTextValue"><input type="checkbox" id="${r.codigoExt}_${p.idPerfil}" class='checkPerfil'/><label for="${r.codigoExt}_${p.idPerfil}">${p.descripcion}</label></div></div>
					</c:forEach>
			</div>
		</c:forEach>	
	</div>	
	</div>
	<div id='divNoHayRoles' style='width:100%;height: 120px;display:none; background-color: white;'>
		<p class='labelText' style='text-align: center;padding-top:30px;text-decoration: bold;'>El usuario no tiene ningún rol asociado</p>
	</div>
</html:form>
	</div>
		<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
	</body>
</html>