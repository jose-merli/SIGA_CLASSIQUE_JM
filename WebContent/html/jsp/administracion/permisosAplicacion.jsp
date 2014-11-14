<!DOCTYPE html>
<html>
<head>
<!-- permisosAplicacion.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>

<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ page import="com.atos.utils.*"%>


	
		<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
		
		
		<!-- Incluido jquery en siga.js -->
		
		<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
		<script type="text/javascript" src="<html:rewrite page='/html/js/jquery.ui/js/jquery-ui-1.10.3.custom.min.js?v=${sessionScope.VERSIONJS}'/>"></script>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
  
		<siga:Titulo titulo="administracion.permisos.titulo" localizacion="menu.administracion"/>

		<style>
		
		.accesoDenegado{color: red}
		.accesoTotal{color: green}
		.sinAcceso{color: blue}
		.soloLectura{color: orange}
		
		.flecha{padding-right:4px;width:9px;padding-bottom:2px}
		
		.found{background: pink}
		
  		 .selected { font-style:italic; font-weight:bold; }
  		 .cambiado { font-style:italic;}
  		 
  		 .ocultar { font-size:0.7em;}
  		 
  		 .nodo{
	  		 padding-bottom:-2px;
	  		 -webkit-touch-callout: none;
			-webkit-user-select: none;
			-khtml-user-select: none;
			-moz-user-select: none;
			-ms-user-select: none;
			user-select: none;}
		
		.boton{
			float:both;
			width:140px;
			margin-top:3px;
		}
		
		.miBoton{background:none; padding:5px; vertical-align: middle; background-repeat:no-repeat; border:0 none !important; cursor: pointer;}
		
		.botonAccesoDenegado{background-image: url(./html/imagenes/accessDeny.gif);height: 30px; width:30px;}
		.botonAccesoTotal{background-image: url(./html/imagenes/accessFull.gif);height: 30px; width:30px;}
		.botonSinAcceso{background-image: url(./html/imagenes/accessNone.gif);height: 30px; width:30px;}
		.botonSoloLectura{background-image: url(./html/imagenes/accessRead.gif);height: 30px; width:30px;}
		
		.botonRestablecer{background-image: url(./html/imagenes/reload.png);height: 25px; width:25px;}

		
		</style>
		<script language="JavaScript">
		
		jQuery.noConflict();

		var data;
		var permisos;
		var nuevosPermisos=[];
		
		var debug = true;

		function getProcesos(){
			json = jQuery.parseJSON('${procesos}');
			sub();
			pintaArbol(json.procesos);
		}
	 		
		function getPermisosPerfil(perfil){
			sub();
			limpiarSeleccion();
			jQuery('#container').show();
			jQuery('#mensajeSeleccionarGrupo').hide();
			numeroProcesos=jQuery('.nodo').length;
			paginas=(numeroProcesos/50)+1;
			//data: {idPerfil:perfil,inicio:0,datos:numeroProcesos},
			jQuery("#arbol").find('label').removeClass('accesoDenegado sinAcceso accesoTotal soloLectura cambiado').addClass('sinAcceso');
			for ( i = 0; i < paginas; i++) {
				jQuery.ajax({ 
					type: "POST",
					url: "/SIGA/ADM_ConfigurarPermisosAplicacion.do?modo=GETPERMISOS",
					dataType: "json",
					data: {'idPerfil':perfil,'inicio':i*50,'cantidad':((i+1)*50)},
					contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					success: function(json){
						pintaPermisosPerfil(json.permisos);
					},
					error: function(e){
						alert("No se ha conseguido recuperar los permisos");
					}
				});
			}
			fin();
		}
			
			function guardarCambios(){
				
				if(jQuery("#idPerfil").val().length==3 && nuevosPermisos.length>0){
					
					if(confirm("Se van a realizar "+nuevosPermisos.length+" cambios de permisos sobre el perfil "+jQuery("#idPerfil :selected").text())){
						sub();
						perfil=jQuery("#idPerfil").val();
						jQuery.ajax({ 
							type: "POST",
							url: "/SIGA/ADM_ConfigurarPermisosAplicacion.do?modo=SETPERMISOS",
							dataType: "json",
							data: {'perfil': perfil,'permisos':JSON.stringify(nuevosPermisos)},
							contentType: "application/x-www-form-urlencoded;charset=UTF-8",
							success: function(json){
								console.clear();
								nuevosPermisos=[];
								alert(json.msg);
								getPermisosPerfil(jQuery("#idPerfil").val());
							},
							error: function(e){
								alert(e+":error de comunicacion con el servidor");
							}
						});
						restablecerCambios();
						fin();
					}
				}else{
					alert("No hay cambios pendientes");
				}
			}
						
			function pintaPermisosPerfil(data){
				//jQuery("#arbol").find('label').removeClass('accesoDenegado sinAcceso accesoTotal soloLectura cambiado').addClass('sinAcceso');
				for ( var i = 0; i < data.length; i++) {
					permiso=data[i];
					acceso=permiso.DERECHOACCESO;
					jQuery("#"+permiso.IDPROCESO).find('label').first().removeClass('accesoDenegado sinAcceso accesoTotal soloLectura').addClass(getClaseId(acceso));
				}
				
			}
			
			function pintaPausa(nodos,numeroNodos){
				if(nodos.length==0){
					creaArbol();
					alert("Carga finalizada","success");
					jQuery( "#progressbar" ).hide(300);
					fin();
				}else{
					if(!debug && nodos[0].text.lastIndexOf("HIDDEN_", 0)===0){
						// En modo normal no pintamos los nodos ocultos, en debug si los pintamos
						nodos.splice(0,1);
					}else{
						insertaNodo(nodos[0]);
						if(!jQuery("#"+nodos[0].ID).exists()){
							nodos.push(nodos[0]);
						}
						nodos.splice(0,1);	
					}
					progreso=100-100*(nodos.length/numeroNodos)					
					jQuery( "#progressbar" ).progressbar({value: progreso});
					window.setTimeout(function(){
						pintaPausa(nodos,numeroNodos);
					},0);
				}
			}
			
			function pintaArbol(data){
				var nodos = data;
				var i;
				var descolocados=0;
				var descolocadosAnterior=0;
				var ultimo=nodos.length-1;
				var numeroNodos=ultimo;
				// Colocamos el último, que será el nodo raiz
				insertaNodo(nodos[ultimo]);
				// Lo quitamos de la lista
				nodos.splice(ultimo,1);
				pintaPausa(nodos,nodos.length);
			}

			function insertaNodo(nodo){
				jQuery("#"+nodo.PARENT).append("<div style='padding-left:50px'id='"+nodo.ID+"' class='nodo'><span class='boton'><img class='flecha' src='./html/imagenes/none.png'/></span><input type='checkBox' class='checkNodo'/><label>"+nodo.TEXT+" ("+nodo.ID+"/"+nodo.PARENT+")</label></div>");
				// Añadimos la clase padre al padre (para que sea desplegable)
				jQuery("#"+nodo.PARENT+' .boton').addClass("padre");
				// Marcamos los HIDDEN_ para ocultarlos
				if(nodo.TEXT.lastIndexOf("HIDDEN_", 0)===0){
					jQuery("#"+nodo.ID).addClass('ocultar');
				}
			}

			function creaArbol(){
				// Ocultamos los HIDDEN_
				if(!debug){ jQuery('.ocultar').hide(); }


				// Ponemos el boton de flecha para desplegar los 'hijos'
				jQuery(".padre.boton").each(
						function(){ 
							if(jQuery(this).parent().find('.nodo').length<1){
								jQuery(this).html("<img class='flecha' src='./html/imagenes/none.png'/>");
							}else{
								jQuery(this).html("<img class='flecha' src='./html/imagenes/simboloMenos.gif'/>");
							}
					});
				// Le damos la accion de ocultar/mostrar al darle al padre
				jQuery(".padre").click(function(event){ 
					jQuery(this).parent().find('.nodo').toggle();
					if(jQuery(this).find('.flecha').first().attr("src").indexOf("simboloMas.gif")>0){
						jQuery(this).find('.flecha').first().attr("src", "./html/imagenes/simboloMenos.gif");
					}else{
						jQuery(this).find('.flecha').first().attr("src", "./html/imagenes/simboloMas.gif");
					}
				});
				
				// Añadimos las acciones de seleccionar/deseleccionar para jugar con la clase y el check
				jQuery(".nodo label").click(function(event){ 
					if(jQuery(this).hasClass('selected')){
						jQuery(this).removeClass('selected');
						jQuery(this).parent().find('.checkNodo').first().prop('checked', false);
					}else{
						jQuery(this).addClass('selected');
						jQuery(this).parent().find('.checkNodo').first().prop('checked', true);
					}
	            	pintaSeleccion();
				});
				
				// El check es mas complejo porque implica marcar los hijos
				jQuery(".checkNodo").click(function(event){ 
					
					var seleccionado = jQuery(this).is(':checked');
					var thisLabel = jQuery(this).parent().find('label');
					if(seleccionado){
						thisLabel.addClass('selected');
					}else{
						thisLabel.removeClass('selected');
					}
					
				 	jQuery(this).parent().find('.nodo').find('label').each( 
					function() {
						if(seleccionado)
							jQuery(this).addClass('selected');
						else
							jQuery(this).removeClass('selected'); 
					});
					
					jQuery(this).parent().find('.checkNodo').each( 
				    function() {
			    		if(seleccionado)
							jQuery(this).prop('checked', true);
			    		else
							jQuery(this).prop('checked', false); 
				     });
					pintaSeleccion();
					
				});

				jQuery('#nTotal').text(jQuery('.nodo').length);
				jQuery('#nTotalCambios').text(jQuery('.nodo').length);
			}
				
			// Funcion que aplica el permiso marcado a los nodos seleccionados
			function aplicarPermiso(clase){
				var id, access;
				
				jQuery('.selected').each(function(){
					id=jQuery(this).parent().prop('id');
					access=getIdClase(clase);
					nuevosPermisos.push([id,access]);
				});
				
				// Desmarca los checks
				jQuery('.checkNodo').prop('checked', false);
				// Quita las clases de color y añade la clase marcada
				jQuery('.selected').removeClass('accesoDenegado sinAcceso accesoTotal soloLectura').addClass(clase).addClass('cambiado').removeClass('selected');
				pintaSeleccion();
				pintaCambios();
			}
			
			function getIdClase(clase){
				if(clase==='accesoDenegado')return 0;
				else if(clase==='sinAcceso')return 1;
				else if(clase==='soloLectura')return 2;
				else if(clase==='accesoTotal')return 3;
			}
			
			function getClaseId(id){
				switch (id) {
					case "0":return 'accesoDenegado';
					case "1":return 'sinAcceso'
					case "2":return 'soloLectura';
					case "3":return 'accesoTotal'
					default:return 'sinAcceso'
				}
			}
			
			function restablecerCambios(){
				jQuery('#idPerfil').change();
				jQuery('.selected').removeClass('selected');
				jQuery('.checkNodo').prop('checked', false);
				nuevosPermisos=[];
				pintaSeleccion();
				pintaCambios();
			}
			
			function limpiarSeleccion(){
				jQuery('.selected').removeClass('selected');
				jQuery('.checkNodo').prop('checked', false);
				nuevosPermisos=[];
				pintaSeleccion();
				pintaCambios();
			}
			
			function pintaSeleccion(){
				seleccionado=jQuery('.selected').length;
				jQuery('#nSeleccionados').text(seleccionado);
			}
			
			function pintaCambios(){
				pendientes=nuevosPermisos.length;
				jQuery('#nCambios').text(pendientes);
			}
			
			function expandAll(){
				jQuery(".padre").parent().find('.nodo').show();
				
			}
			
			function collapseAll(){
				jQuery(".padre").parent().find('.nodo').hide();
			}
			

		</script>
	</head>
	
	<body>
		<fieldset>
			<table class="tablaCentralCampos" align="center">
				<html:form action="/ADM_ConfigurarPermisosAplicacion.do" method="POST" target="resultado">
					<input type="hidden" name="modo" value="inicio">
					<tr>				
						<td class="labelText">
							<siga:Idioma key="administracion.permisos.literal.grupo"/>&nbsp;(*)
	        				<siga:Select queryId="getPerfiles" id="idPerfil" required="true" />
						</td>
						<td class="labelText" id="mensajeSeleccionarGrupo">
							<siga:Idioma key="Seleccione un grupo de usuarios para comenzar a administrar sus permisos"/>
						</td>
	       			</tr>	   
				</html:form>
			</table>
			<div id="progressbar" style="width:990px;height:15px;"></div>
		</fieldset>

		<!--siga:ConjBotonesBusqueda botones="B" titulo=""/-->
		<div id='container' style="background-color:white;">
		<div id='botonera' style="text-align:right;background-color:#eeeeee;height:30px;">
		
		<div style="float:left;padding-left:50px;">
			<input type="button" id='darAcceso' class='miBoton botonAccesoTotal' title='Acceso Total' onclick="expandAll()"/>
			<input type="button" id='darAcceso' class='miBoton botonAccesoTotal' title='Acceso Total' onclick="collapseAll()"/>
			-
			<input type="button" id='darAcceso' class='miBoton botonAccesoTotal' title='Acceso Total' onclick="aplicarPermiso('accesoTotal')"/>
			<input type="button" id='darSoloLectura' class='miBoton botonSoloLectura' title='Solo Lectura' onclick="aplicarPermiso('soloLectura')"/>
			<input type="button" id='darDenegado' class='miBoton botonAccesoDenegado' title='Acceso Denegado' onclick="aplicarPermiso('accesoDenegado')"/>
			<input type="button" id='darSinAcceso' class='miBoton botonSinAcceso' title='Sin Acceso' onclick="aplicarPermiso('sinAcceso')"/>
		</div>
		
		<div style="vertical-align: middle; padding-right:10px;padding-top:4px">
			<span class="labelText" style="vertical-align: middle;">Selección:</span><span id='nSeleccionados'>0</span>/<span id='nTotal'>0</span>
			<span class="labelText" style="vertical-align: middle;">Cambios:</span><span id='nCambios'>0</span>/<span id='nTotalCambios'>0</span>
			<input type="button" id='botonRestablecer' class='miBoton botonRestablecer' title='Restablecer' onclick="restablecerCambios()"/>
			<input type="button" id='botonGuardar' class='button' value='Guardar' onclick="guardarCambios()"/>
		</div>
		</div>
		<br>
		<div id="arbolProcesos"  style="overflow-y:auto;background-color:white">	
			<div id="arbolDiv">
				<div id='arbol'>
				</div>
			</div>
		</div>
		
	</div>
	
	
		
	</body>
	<script type="text/javascript">
		jQuery("#cajaBusqueda").keyup(
			function(){
				jQuery(".nodo label").removeClass('found');
				var g = jQuery(this).val().toLowerCase();
				if(g.length>1){
					jQuery(".nodo label").each(function() {
						var s = jQuery(this).text().toLowerCase();
						if(s.indexOf(g)>-1){
							jQuery(this).addClass('found');
							jQuery(this).parent().parent().find('.nodo').first().find('.boton .padre').click();
							jQuery(this).show();
						}
					});
				}
			});

		jQuery("#idPerfil").change(
			function(){
				getPermisosPerfil(jQuery("#idPerfil").val());
				//printSelected();
			});

		jQuery(document).ready(function(){		
			jQuery('#container').height(jQuery(document).height()-90);
			jQuery('#arbolProcesos').height(jQuery('#container').height());
			alert("Se está cargando el arbol de procesos de la aplicación.\nPuede tardar varios segundos dependiendo de su equipo.");
			window.setTimeout(function(){
				getProcesos();
			    },700); 
			
		});
	</script>
</html>
