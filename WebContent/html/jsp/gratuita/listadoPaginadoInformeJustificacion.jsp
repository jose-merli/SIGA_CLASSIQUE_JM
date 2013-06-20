<!--listadoPaginadoInformeJustificacion.jsp -->
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

<!-- JSP -->

<html>
<!-- HEAD -->
<head>
<title><siga:Idioma key="gratuita.altaTurnos.literal.title" /></title>
	<link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='/html/jsp/general/stylesheet.jsp'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js'/>"></script><script src="<html:rewrite page='/html/js/calendarJs.jsp'/>"></script>
	
	<script src="<html:rewrite page='/html/jsp/general/validacionSIGA.jsp'/>" type="text/javascript"></script>

<script>

function informeGenerico(){
	datos = "";
	var mostrarTodas = document.InformeJustificacionMasivaForm.mostrarTodas.value;
	var idInstitucion  =document.InformeJustificacionMasivaForm.idInstitucion.value
	var idPersona = document.InformeJustificacionMasivaForm.idPersona.value;
	var anio = document.InformeJustificacionMasivaForm.anio.value;
	var estado= document.InformeJustificacionMasivaForm.estado.value;
	var fechaJustificacionDesde = document.InformeJustificacionMasivaForm.fechaJustificacionDesde.value;
	var fechaJustificacionHasta = document.InformeJustificacionMasivaForm.fechaJustificacionHasta.value;
	var fechaDesde = document.InformeJustificacionMasivaForm.fechaDesde.value;
	var fechaHasta = document.InformeJustificacionMasivaForm.fechaHasta.value;
	var interesadoApellidos = document.InformeJustificacionMasivaForm.interesadoApellidos.value;
	var interesadoNombre = document.InformeJustificacionMasivaForm.interesadoNombre.value;
	var incluirEjgNoFavorable = document.InformeJustificacionMasivaForm.incluirEjgNoFavorable.value;
	var incluirEjgSinResolucion = document.InformeJustificacionMasivaForm.incluirEjgSinResolucion.value;
	var incluirSinEJG = document.InformeJustificacionMasivaForm.incluirSinEJG.value;
	var incluirEjgPteCAJG = document.InformeJustificacionMasivaForm.incluirEjgPteCAJG.value;
	var activarRestriccionesFicha = document.InformeJustificacionMasivaForm.activarRestriccionesFicha.value;
	var fichaColegial = document.InformeJustificacionMasivaForm.fichaColegial.value; 
	
	if(fichaColegial=='true'){
		document.InformesGenericosForm.enviar.value ='0';
		activarRestriccionesFicha = 'true';
	}
	else{
		document.InformesGenericosForm.enviar.value ='1';
	}
	
	datos = "fichaColegial=="+fichaColegial+"##mostrarTodas=="+mostrarTodas+ "##idInstitucion==" +idInstitucion+ "##idPersona==" +idPersona+ "##anio==" +anio+ "##estado==" +estado+ "##fechaJustificacionDesde==" +fechaJustificacionDesde+ "##fechaJustificacionHasta==" +fechaJustificacionHasta+ "##fechaDesde==" +fechaDesde+ "##fechaHasta==" +fechaHasta+ "##interesadoApellidos==" +interesadoApellidos+ "##interesadoNombre==" +interesadoNombre+ "##incluirEjgNoFavorable==" +incluirEjgNoFavorable+ "##incluirEjgSinResolucion==" +incluirEjgSinResolucion+ "##incluirSinEJG==" +incluirSinEJG+ "##incluirEjgPteCAJG==" +incluirEjgPteCAJG+ "##activarRestriccionesFicha==" +activarRestriccionesFicha+"##idTipoInforme==JUSDE%%%";
	document.InformesGenericosForm.idInstitucion.value = document.InformeJustificacionMasivaForm.idInstitucion.value;
	document.InformesGenericosForm.datosInforme.value=datos;

	if(document.getElementById("informeUnico").value=='1'){
		document.InformesGenericosForm.submit();
	}else{
	
		var arrayResultado = ventaModalGeneral("InformesGenericosForm","M");
		if (arrayResultado==undefined||arrayResultado[0]==undefined){
		   		
	   	} 
	   	else {
	   		var confirmar = confirm("<siga:Idioma key='general.envios.confirmar.edicion'/>");
	   		if(confirmar){
	   			var idEnvio = arrayResultado[0];
			    var idTipoEnvio = arrayResultado[1];
			    var nombreEnvio = arrayResultado[2];				    
			    
			   	document.DefinirEnviosForm.tablaDatosDinamicosD.value=idEnvio + ',' + idTipoEnvio + '%' + nombreEnvio;		
			   	document.DefinirEnviosForm.modo.value='editar';
			   	document.DefinirEnviosForm.submit();
	   		}
	   	}
	}
}

function ajustarCabeceraTabla(){
	if (document.getElementById("listadoInformeJustificacion").clientHeight < document.getElementById("listadoInformeJustificacionDiv").clientHeight) {
		document.getElementById("listadoInformeJustificacionCab").style.width='100%';
	  } else {
		  document.getElementById("listadoInformeJustificacionCab").style.width='98.43%';
	  }
}
function accionCerrar(){
	
}
function refrescarLocal(){
	parent.buscar();
}
function accionNuevaActuacion(anio,idTurno,numero,idInstitucion,validarActuaciones) 
{	
	var accion = document.ActuacionesDesignasForm.action;
	if(document.InformeJustificacionMasivaForm.fichaColegial.value=='true')
		document.ActuacionesDesignasForm.action = accion.replace('JGR_ActuacionesDesigna','JGR_ActuacionDesignaLetrado');
	
	document.ActuacionesDesignasForm.anio.value = anio;
	document.ActuacionesDesignasForm.idTurno.value = idTurno;
	document.ActuacionesDesignasForm.numero.value = numero;
	document.ActuacionesDesignasForm.actuacionValidada.value = validarActuaciones;
	document.ActuacionesDesignasForm.modo.value = "nuevoJustificacion";
	var resultado=ventaModalGeneral(document.ActuacionesDesignasForm.name,"G");
	if(resultado=='MODIFICADO') 
		parent.buscar();
			
}

function accionEditarActuacion(anio,idTurno,numero,idInstitucion,numeroActuacion,validarActuaciones) 
{	
	var accion = document.ActuacionesDesignasForm.action;
	if(document.InformeJustificacionMasivaForm.fichaColegial.value=='true')
		document.ActuacionesDesignasForm.action = accion.replace('JGR_ActuacionesDesigna','JGR_ActuacionDesignaLetrado');
	document.ActuacionesDesignasForm.anio.value = anio;
	document.ActuacionesDesignasForm.idTurno.value = idTurno;
	document.ActuacionesDesignasForm.numero.value = numero;
	document.ActuacionesDesignasForm.nactuacion.value = numeroActuacion;
	document.ActuacionesDesignasForm.actuacionValidada.value = validarActuaciones;
	document.ActuacionesDesignasForm.modo.value = "editarJustificacion";
	
	var resultado=ventaModalGeneral(document.ActuacionesDesignasForm.name,"G");
	if(resultado=='MODIFICADO') 
		parent.buscar();
			
}
function accionConsultarActuacion(anio,idTurno,numero,idInstitucion,numeroActuacion) 
{	
	var accion = document.ActuacionesDesignasForm.action;
	if(document.InformeJustificacionMasivaForm.fichaColegial.value=='true')
		document.ActuacionesDesignasForm.action = accion.replace('JGR_ActuacionesDesigna','JGR_ActuacionDesignaLetrado');
	
	document.ActuacionesDesignasForm.anio.value = anio;
	document.ActuacionesDesignasForm.idTurno.value = idTurno;
	document.ActuacionesDesignasForm.numero.value = numero;
	document.ActuacionesDesignasForm.nactuacion.value = numeroActuacion;
	document.ActuacionesDesignasForm.modo.value = "consultarJustificacion";
	var resultado=ventaModalGeneral(document.ActuacionesDesignasForm.name,"G");
	if(resultado=='MODIFICADO') 
		parent.buscar();
			
}
function accionEditarDesigna(anio,idTurno,numero,idInstitucion,fecha) 
{	
	document.MaestroDesignasForm.anio.value = anio;
	document.MaestroDesignasForm.idTurno.value = idTurno;
	document.MaestroDesignasForm.numero.value = numero;
	document.MaestroDesignasForm.fecha.value = fecha;
	document.MaestroDesignasForm.modo.value = "actualizarDesigna";
	var resultado=ventaModalGeneral(document.MaestroDesignasForm.name,"G");
	if(resultado=='MODIFICADO') {
		parent.buscar();
	}
			
}




function accionGuardar (isLetrado) 
{	
	sub();
	var checksAcreditacion = document.getElementsByName("checkAcreditacion");
	var datosJustificacion = "";
	for (i=0;i<checksAcreditacion.length;i++) {
		var checkAcreditacion = checksAcreditacion[i];
		if(checkAcreditacion.checked){
			
			var cadenaAcreditacion=checkAcreditacion.id;
			var idsAcreditacion = cadenaAcreditacion.split("_");
			//el 0 es acre_
			var index = idsAcreditacion[1];
			var numActuacion = idsAcreditacion[2];
			var idTipoAcreditacion = idsAcreditacion[3];
			var idAcreditacion = idsAcreditacion[4];
			var idProcedimiento = idsAcreditacion[5];
			var idJuzgado = idsAcreditacion[6];
			var justificado = idsAcreditacion[7];
			var idJurisdiccion = idsAcreditacion[8];
			var idsValidacion =  cadenaAcreditacion.split("acre_");
			//el 0 es vacio
			var elementoValidacion = 'vali_'+idsValidacion[1]; 
			var validado = document.getElementById(elementoValidacion).checked;
			var idInstitucion = document.getElementById("idInstitucion_"+index).value;
			var idTurno = document.getElementById("idTurno_"+index).value;
			var anio = document.getElementById("anio_"+index).value;
			var numero = document.getElementById("numero_"+index).value;
			var fechaDesigna = document.getElementById("fechaDesigna_"+index).value;
			var actuacionRestriccionesActiva = document.getElementById("actuacionRestriccionesActiva_"+index).value;
			datosJustificacion = datosJustificacion + anio + "," + 
								numero + "," + 
								idInstitucion + "," + 
								idTurno + "," + 
								idJuzgado + "," + 
								idProcedimiento + "," + 
								numActuacion + "," +
								idAcreditacion + "," +
								justificado + "," +
								idJurisdiccion + "," +
								fechaDesigna + "," +
								validado + "," +
								actuacionRestriccionesActiva + "#";
		}
	}
	//Ahora miramos los check Validacion que a estuvieran acreditados anteriormente
	var checksValidacion = document.getElementsByName("checkValidacion");
	for (i=0;i<checksValidacion.length;i++) {
		var checkValidacion = checksValidacion[i];
		if(checkValidacion.checked){
			var cadenaValidacion = checkValidacion.id;
			var cadenaAcreditacion = 'acre_'+cadenaValidacion.split("vali_")[1];
			
			//Si no existe el check de acreditacion es porque estaba dado de alta luego lo validamos,
			//si existe ya lo valida lo de arriba
			if(!document.getElementById(cadenaAcreditacion)){
			
				var idsAcreditacion = cadenaValidacion.split("_");
				//el 0 es acre_
				var index = idsAcreditacion[1];
				var numActuacion = idsAcreditacion[2];
				var idTipoAcreditacion = idsAcreditacion[3];
				var idAcreditacion = idsAcreditacion[4];
				var idProcedimiento = idsAcreditacion[5];
				var idJuzgado = idsAcreditacion[6];
				var justificado = idsAcreditacion[7];
				var idJurisdiccion = idsAcreditacion[8];
				//el 0 es vacio
				var validado = document.getElementById(cadenaValidacion).checked;
				var idInstitucion = document.getElementById("idInstitucion_"+index).value;
				var idTurno = document.getElementById("idTurno_"+index).value;
				var anio = document.getElementById("anio_"+index).value;
				var numero = document.getElementById("numero_"+index).value;
				var fechaDesigna = document.getElementById("fechaDesigna_"+index).value;
				var actuacionRestriccionesActiva = document.getElementById("actuacionRestriccionesActiva_"+index).value;
				datosJustificacion = datosJustificacion + anio + "," + 
									numero + "," + 
									idInstitucion + "," + 
									idTurno + "," + 
									idJuzgado + "," + 
									idProcedimiento + "," + 
									numActuacion + "," +
									idAcreditacion + "," +
									justificado + "," +
									idJurisdiccion + "," +
									fechaDesigna + "," +
									validado + "," +
									actuacionRestriccionesActiva + "#";
			}
		}
	}
	var datosBaja = "";
	//Ahora miramos si hay alguno que no esta en los anetriores pero que esta de baja
	var checksBaja = document.getElementsByName("checkBaja");
	for (i=0;i<checksBaja.length;i++) {
		var checkBaja = checksBaja[i];
		if(checkBaja.checked){
			var cadenaBaja = checkBaja.id;
			var index = cadenaBaja.split("baja_")[1];
			var idInstitucion = document.getElementById("idInstitucion_"+index).value;
			var idTurno = document.getElementById("idTurno_"+index).value;
			var anio = document.getElementById("anio_"+index).value;
			var numero = document.getElementById("numero_"+index).value;
			
			
							
			datosBaja = datosBaja + anio + "," + 
									numero + "," + 
									idInstitucion + "," + 
									idTurno + "#";
			}
	}
	if(datosBaja!=''||datosJustificacion!=''){
		if(isLetrado && isLetrado==true){
			if(document.InformeJustificacionMasivaForm.fichaColegial.value=='true'){
				if(trim(document.InformeJustificacionMasivaForm.mensajeResponsabilidadJustificacionLetrado.value)!=''){
					if (!confirm(document.InformeJustificacionMasivaForm.mensajeResponsabilidadJustificacionLetrado.value)){
						fin();
						return false;
					}
				}
			
			}
		} else {		
			if(document.InformeJustificacionMasivaForm.fecha.value == null || document.InformeJustificacionMasivaForm.fecha.value == ''){
				alert("<siga:Idioma key='errors.required' arg0='gratuita.informeJustificacionMasiva.literal.fecha.Justif'/>");
				fin();
				return false;
			}
		}
		
		document.InformeJustificacionMasivaForm.modo.value="justificar";
		document.InformeJustificacionMasivaForm.datosBaja.value = datosBaja;
		document.InformeJustificacionMasivaForm.datosJustificacion.value = datosJustificacion;
		document.InformeJustificacionMasivaForm.submit();
	}else{
		alert('<siga:Idioma key="general.message.seleccionar"/>');
		fin();
		return;
	}
}
function onCheckValidacion(elementoPulsado){
	var cadenaValidacion = elementoPulsado.id;
	var idsValidacion = cadenaValidacion.split("_");
	var index = idsValidacion[1];
	var actuacionRestriccionesActiva = document.getElementById("actuacionRestriccionesActiva_"+index).value;
	
	var idAcreditacion = 'acre_'+cadenaValidacion.split("vali_")[1];
	//Si no existe el elemento es porque la acreditacion esta dada de alta pte de validar
	//y no hay que hacer nada con las acreditaciones ya que se hizo en el momento de darla de alta
	if(document.getElementById(idAcreditacion)){
	
		if(elementoPulsado.checked){
			
			document.getElementById(idAcreditacion).checked = 'checked';
			if(actuacionRestriccionesActiva=='1'){
				if(!onCheckAcreditacion(document.getElementById(idAcreditacion))){
					elementoPulsado.checked='';
				}
			}
		}else{
			var actuacionValidarJustificaciones = document.getElementById("actuacionValidarJustificaciones_"+index).value;
			//este caso NO se puede dar. por lo que no meto un recurso
			if(actuacionValidarJustificaciones=='N'){
				
				alert('<siga:Idioma key="gratuita.informeJustificacionMasiva.aviso.validarActuaciones"/>');
				elementoPulsado.checked = "checked";
				return;
			}
		}
	}

}
function preFunction(pagina){
	var checksAcreditacion = document.getElementsByName("checkAcreditacion");
	var isModificado = false;
	for (i=0;i<checksAcreditacion.length;i++) {
		var checkAcreditacion = checksAcreditacion[i];
		if(checkAcreditacion.checked){
			isModificado = true;
			break;
		}
	}
	//Ahora miramos los check Validacion que a estuvieran acreditados anteriormente
	if(!isModificado){
		var checksValidacion = document.getElementsByName("checkValidacion");
		for (i=0;i<checksValidacion.length;i++) {
			var checkValidacion = checksValidacion[i];
			if(checkValidacion.checked){
				var cadenaValidacion = checkValidacion.id;
				var cadenaAcreditacion = 'acre_'+cadenaValidacion.split("vali_")[1];
				
				//Si no existe el check de acreditacion es porque estaba dado de alta luego lo validamos,
				//si existe ya lo valida lo de arriba
				if(!document.getElementById(cadenaAcreditacion)){
					isModificado = true;
					break;
				}
			}
		}
	}
	var datosBaja = "";
	//Ahora miramos si hay alguno que no esta en los anetriores pero que esta de baja
	if(!isModificado){
		var checksBaja = document.getElementsByName("checkBaja");
		for (i=0;i<checksBaja.length;i++) {
			var checkBaja = checksBaja[i];
			if(checkBaja.checked){
				isModificado = true;
				break;	
			}
		}
	 }
	 if(isModificado){
		if (confirm('<siga:Idioma key="gratuita.informeJustificacionMasiva.confirmar.guardarAlCambiarPagina"/>')){
			return false;
		}else{
			return true;
		}
	}else{
		return true;
	}
	

}
function postFunction(page){
}

function onCheckAcreditacion(elementoPulsado){
	var cadenaAcreditacion = elementoPulsado.id;
	var idValidacion = 'vali_'+cadenaAcreditacion.split("acre_")[1];
	var idsAcreditacion = cadenaAcreditacion.split("_");
	var index = idsAcreditacion[1];
	
	//Si se despulsa el check de acreditacion se despulsa el de validacion
	var actuacionValidarJustificaciones = document.getElementById("actuacionValidarJustificaciones_"+index).value;
	if(!elementoPulsado.checked){
		document.getElementById(idValidacion).checked = '';
	}else{
		//Si se pulsa el check de acreditacion y ademas la designa tiene validar actuaciones a no, se pulsa tambien el check 
		// de validacion(ya que al no ser necesario validar las actuaciones quedaran validadas en la insercion)
		
		if(actuacionValidarJustificaciones=='N'||document.InformeJustificacionMasivaForm.fichaColegial.value=='false'){
			document.getElementById(idValidacion).checked = 'checked';
		}
		
	}
	// CASO USO TURNO SIN ACTIVAR RESTRICCIONES DE ACREDITACION
	var actuacionRestriccionesActiva = document.getElementById("actuacionRestriccionesActiva_"+index).value;
	if(actuacionRestriccionesActiva=='0')
		return false;
	
	// CASO DE USO CON ACTUACION SIN COMPLEMENTOS
	//no se da el caso
	
	
	
	// CASO DE USO SIN ACTUACION CON COMPLEMENTOS. Al ser un complemento solo se permite 1 actuacion
	// CASO DE USO CON ACTUACION CON COMPLEMENTOS
	 
	var designaMultiplesComplementos = document.getElementById("multiplesComplementos_"+index);
	if(designaMultiplesComplementos && designaMultiplesComplementos.value=='1'){
		var idTipoAcreditacion = idsAcreditacion[3];
		var idAcreditacion = idsAcreditacion[4];
		var idProcedimiento = idsAcreditacion[5];
		var checksProcedimientos = document.getElementsByName(index+"_"+idProcedimiento+"_checkAcreditacion");
		for (i=0;i<checksProcedimientos.length;i++) {
			var checkProcedimiento = checksProcedimientos[i];
			var idCheckProcedimiento = checkProcedimiento.id;
			var cadenaProcedimiento = idCheckProcedimiento.split("checkacre_")[1];
			var elementoValidacion = 'vali_'+cadenaProcedimiento;	
			var elementoProcedimiento = 'acre_'+cadenaProcedimiento;
			var idsProcedimiento = elementoProcedimiento.split("_");
			var idTipoAcreditacionProcedimiento = idsProcedimiento[3];
			var idAcreditacionProcedimiento = idsProcedimiento[4];
			if(elementoPulsado.checked){
				if(idAcreditacion!=idAcreditacionProcedimiento){
					jQuery("#"+elementoProcedimiento).attr("disabled","disabled");
					jQuery("#"+elementoValidacion).attr("disabled","disabled");					
				}
			}else{
				if(idAcreditacion!=idAcreditacionProcedimiento){
					jQuery("#"+elementoProcedimiento).removeAttr("disabled");
					if(document.InformeJustificacionMasivaForm.fichaColegial.value=='false'&&actuacionValidarJustificaciones=='S'){
						jQuery("#"+elementoValidacion).removeAttr("disabled");
					}
					
				}
				
			}
			
		}
		
	}else{
		
		
	
		//CASO USO SIN ACTUACIONES SIN COMPLEMENTOS. Solo puede tener una de inicio y otra de fin, o una completa
		var idTipoAcreditacion = idsAcreditacion[3];
		var idAcreditacion = idsAcreditacion[4];
		var idProcedimiento = idsAcreditacion[5];
		var checksProcedimientos = document.getElementsByName(index+"_"+idProcedimiento+"_checkAcreditacion");
		var isCheckAlgunaInicioOFin = false;
		var isCheckAlgunaInicio = false;
		var isCheckAlgunafin = false;
		for (j=0;j<checksProcedimientos.length;j++) {
			var checksProcedimientoAux = checksProcedimientos[j];
			var idProcedimientoAux = checksProcedimientoAux.id;
			var idsProcedimientoAux = idProcedimientoAux.split("_");
			var prefijo = idsProcedimientoAux[0];
			var auxIdTipoAcreditacionAux = idsProcedimientoAux[3];
			if((auxIdTipoAcreditacionAux=='1')&&!isCheckAlgunaInicio){
				if(prefijo=='hiddacre'){
					if(document.getElementById(idProcedimientoAux))
						isCheckAlgunaInicio = true;
				}else{
					var cadenaProcedimiento = 'acre_'+idProcedimientoAux.split("checkacre_")[1];
					isCheckAlgunaInicio = document.getElementById(cadenaProcedimiento).checked;
				}
				
			}
			if((auxIdTipoAcreditacionAux=='2')&&!isCheckAlgunafin){
				if(prefijo=='hiddacre'){
					if(document.getElementById(idProcedimientoAux))
						isCheckAlgunafin = true;
				}else{
					var cadenaProcedimiento = 'acre_'+idProcedimientoAux.split("checkacre_")[1];
					isCheckAlgunafin = document.getElementById(cadenaProcedimiento).checked;
				}
				
			}
		}
		if(elementoPulsado.checked ){
			if(!isCheckAlgunaInicio&&idTipoAcreditacion=='2'){
				alert('<siga:Idioma key="gratuita.informeJustificacionMasiva.aviso.acreditacionFinSinIncicio"/>');
				elementoPulsado.checked = "";
				document.getElementById(idValidacion).checked ="";
				
				return false;
			}
		}
		else{
			for (j=0;j<checksProcedimientos.length;j++) {
				var checksProcedimientoAux = checksProcedimientos[j];
				var idProcedimientoAux = checksProcedimientoAux.id;
				var idsProcedimientoAux = idProcedimientoAux.split("_");
				var prefijo = idsProcedimientoAux[0];
				var auxIdTipoAcreditacionAux = idsProcedimientoAux[3];
	
				if((auxIdTipoAcreditacionAux=='2'||auxIdTipoAcreditacionAux=='1')&&!isCheckAlgunaInicioOFin){
					if(prefijo=='hiddacre'){
						if(document.getElementById(idProcedimientoAux))
							isCheckAlgunaInicioOFin = true;
					}else{
						var cadenaProcedimiento = 'acre_'+idProcedimientoAux.split("checkacre_")[1];
						
						isCheckAlgunaInicioOFin = document.getElementById(cadenaProcedimiento).checked;
					}
				}
			}
			if(isCheckAlgunafin&&idTipoAcreditacion=='1'){
				alert('<siga:Idioma key="gratuita.informeJustificacionMasiva.aviso.acreditacionFinSinIncicio"/>');
				elementoPulsado.checked = "true";
				if(actuacionValidarJustificaciones=='N')
					document.getElementById(idValidacion).checked ="true";
				return false;
			}
		}
		for (i=0;i<checksProcedimientos.length;i++) {
			var checksProcedimiento = checksProcedimientos[i];
			var idProcedimiento = checksProcedimiento.id;
			var idsProcedimiento = idProcedimiento.split("_");
			var prefijo = idsProcedimiento[0];
			if(prefijo=='checkacre'){
				var auxIdTipoAcreditacion = idsProcedimiento[3];
				var auxIdAcreditacion = idsProcedimiento[4];
				var cadenaProcedimiento = idProcedimiento.split("checkacre_")[1]
				var elementoValidacion = 'vali_'+cadenaProcedimiento;	
				
				var idProcedimiento = 'acre_'+cadenaProcedimiento;
				
				var idsProcedimiento = idProcedimiento.split("_");
				var auxIdTipoAcreditacion = idsProcedimiento[3];
				var auxIdAcreditacion = idsProcedimiento[4];
				
				if(elementoPulsado.checked){
					
					if(auxIdTipoAcreditacion=='1'){
						if(idTipoAcreditacion=='3'){
							jQuery("#"+idProcedimiento).attr("disabled","disabled");
							jQuery("#"+elementoValidacion).attr("disabled","disabled");
						}
					}else if(auxIdTipoAcreditacion=='2'){
						
						
						if(idTipoAcreditacion=='3'){
							jQuery("#"+idProcedimiento).attr("disabled","disabled");
							jQuery("#"+elementoValidacion).attr("disabled","disabled");
						}
					
					}else if(auxIdTipoAcreditacion=='3'){
						if(idTipoAcreditacion=='1'||idTipoAcreditacion=='2'||(idTipoAcreditacion=='3'&&idAcreditacion!=auxIdAcreditacion)){
							jQuery("#"+idProcedimiento).attr("disabled","disabled");
							jQuery("#"+elementoValidacion).attr("disabled","disabled");
						}
					}
				}else{
					if(auxIdTipoAcreditacion=='1'||auxIdTipoAcreditacion=='2'){
						if(idTipoAcreditacion=='3'){
							jQuery("#"+idProcedimiento).removeAttr("disabled");
							if(document.InformeJustificacionMasivaForm.fichaColegial.value=='false'&&actuacionValidarJustificaciones=='S'){
								jQuery("#"+elementoValidacion).removeAttr("disabled");
							}
						}
					}else if(auxIdTipoAcreditacion=='3'){
						if(((idTipoAcreditacion=='1'||idTipoAcreditacion=='2')||(idTipoAcreditacion=='3'&&idAcreditacion!=auxIdAcreditacion))&&!isCheckAlgunaInicioOFin){
							jQuery("#"+idProcedimiento).removeAttr("disabled");
							if(document.InformeJustificacionMasivaForm.fichaColegial.value=='false'&&actuacionValidarJustificaciones=='S'){
								jQuery("#"+elementoValidacion).removeAttr("disabled");
							}
						}
					}
				}
			}
		}
	}
	return true;
}


function inicio(){
	//si tiene permiso para los botons existira, si no estara oculto
	if(document.getElementById("idInformeJustificacion")){
		if(document.InformeJustificacionMasivaForm.mostrarTodas.value=='true'){
	  		jQuery("#idInformeJustificacion").removeAttr("disabled");
		}else{
			//document.getElementById("idInformeJustificacion").disabled ="disabled";
	  		jQuery("#idInformeJustificacion").removeAttr("disabled");
		}
	}
}


function ajustarAltoResultados(){
	parent.ajustarAltoResultado();
	ajusteDivListado();
}
function ajusteDivListado(){
	if(document.InformeJustificacionMasivaForm.fichaColegial.value=='true')
		ajusteAltoPaginador('listadoInformeJustificacionDiv');
	else
		ajusteAlto('listadoInformeJustificacionDiv');
		
}
function downloadDocumentoResolucion(docResolucion) {			
	document.InformeJustificacionMasivaForm.docResolucion.value=docResolucion
	document.InformeJustificacionMasivaForm.modo.value="download";
	document.InformeJustificacionMasivaForm.target="submitArea";		   	
	document.InformeJustificacionMasivaForm.submit();
}
		

</script>
</head>

<body onload="inicio();ajustarCabeceraTabla();ajusteDivListado();">



<bean:define id="usrBean" name="USRBEAN" scope="session"
	type="com.atos.utils.UsrBean"></bean:define>
<bean:define id="informeUnico" name="informeUnico" scope="request"></bean:define>
<input type="hidden" id= "informeUnico" value="${informeUnico}">

<!-- FIN: TITULO OPCIONAL DE LA TABLA -->
<!-- INICIO: CAMPOS -->
<!-- Zona de campos de busqueda o filtro -->
<bean:define id="path" name="org.apache.struts.action.mapping.instance"
	property="path" scope="request" />
<bean:define id="cambiarColor" name="CAMBIAR_COLOR"  scope="request"/>
<c:set var="colorEJG" value="" />
<c:if	test="${cambiarColor==true}">
	<c:set var="colorEJG" value="color:red;" />
</c:if>

<html:form action="${path}" method="post" target="submitArea">
	<html:hidden property="modo" value="justificar" />

	<html:hidden property="idPersona" />
	<html:hidden property="datosJustificacion" />
	<html:hidden property="datosBaja" />
	<html:hidden property="mostrarTodas" />
	<html:hidden property="fichaColegial" />
	<html:hidden property="mensajeResponsabilidadJustificacionLetrado" />
	<html:hidden property="docResolucion" />

	<html:hidden property="idInstitucion" />
	<html:hidden property="anio" />
	<html:hidden property="estado" />
	<html:hidden property="actuacionesPendientes" />
	<html:hidden property="fechaJustificacionDesde" />
	<html:hidden property="fechaJustificacionHasta" />
	<html:hidden property="fechaDesde" />
	<html:hidden property="fechaHasta" />
	<html:hidden property="interesadoApellidos" />
	<html:hidden property="interesadoNombre" />
	<html:hidden property="incluirEjgNoFavorable" />
	<html:hidden property="incluirEjgSinResolucion" />
	<html:hidden property="incluirSinEJG" />
	<html:hidden property="incluirEjgPteCAJG" />
	<html:hidden property="activarRestriccionesFicha" />
	
	<input type="hidden" name="tablaDatosDinamicosD" id="tablaDatosDinamicosD">
	<input type="hidden" name="actionModal" value="">

	<c:choose>
		<c:when test="${InformeJustificacionMasivaForm.fichaColegial==true}">
			<html:hidden property="fecha" />
		</c:when>
		<c:otherwise>
			<div>

			<table width="100%" border="0">
				<tr>
					<td width="75%">&nbsp;</td>
					<td class="labelText">
					<siga:Idioma key="gratuita.informeJustificacionMasiva.literal.fecha.Justif" />					
						<siga:Fecha nombreCampo="fecha" valorInicial="${InformeJustificacionMasivaForm.fecha}"></siga:Fecha>
					</td>
				</tr>

			</table>
			</div>

		</c:otherwise>
	</c:choose>

</html:form>


<table id='listadoInformeJustificacionCab' style="width:100%;table-layout: fixed;"  border='1' 
	cellspacing='0' cellpadding='0'>
	<tr class='tableTitle'>
		<td align='center' width="8%"><siga:Idioma
			key="gratuita.informeJustificacionMasiva.literal.designa" /></td>
		<td align='center' width="8%"><siga:Idioma
			key="gratuita.informeJustificacionMasiva.literal.ejg" /></td>
		<td align='center' width="12%"><siga:Idioma
			key="gratuita.informeJustificacionMasiva.literal.juzgado" /></td>
		<td align='center' width="8%"><siga:Idioma
			key="gratuita.informeJustificacionMasiva.literal.fechaSalida" /></td>
		<td align='center' width="8%"><siga:Idioma
			key="gratuita.informeJustificacionMasiva.literal.asunto" /></td>
		<td align='center' width="15%"><siga:Idioma
			key="gratuita.informeJustificacionMasiva.literal.cliente" /></td>
		<td align='center' width="4%"><siga:Idioma
			key="gratuita.informeJustificacionMasiva.literal.categoria" /></td>
		<td align='center' width="4%"><siga:Idioma
			key="gratuita.informeJustificacionMasiva.literal.numeroActuacion" /></td>
		<td align='center' width="15%"><siga:Idioma
			key="gratuita.informeJustificacionMasiva.literal.acreditaciones" /></td>
		<td align='center' width="3%">V</td>
		<td align='center' width="3%">&nbsp;</td>
		<td align='center' width="3%">&nbsp;</td>
		<td align='center' width="4%"><siga:Idioma
			key="gratuita.informeJustificacionMasiva.literal.baja" /></td>

	</tr>
</table>
<!-- 2. Pintamos el contenido de la tabla -->
<div id='listadoInformeJustificacionDiv'
	style='height:400; width: 100%; overflow-y: auto'>
<table id='listadoInformeJustificacion' border='1' align='center'
	width='100%' cellspacing='0' cellpadding='0'
	style='table-layout: fixed'>
	<tr>
		<td width="8%"></td>
		<td width="8%"></td>
		<td width="12%"></td>
		<td width="8%"></td>
		<td width="8%"></td>
		<td width="15%"></td>
		<td width="4%"></td>
		<td width="4%"></td>
		<td width="15%"></td>
		<td width="3%"></td>
		<td width="3%"></td>
		<td width="3%"></td>
		<td width="4%"></td>
	</tr>
	<bean:define id="permitirBotones" name="permitirBotones"
		scope="request"></bean:define>
	<bean:define id="editarDesignaLetrados" name="EDITAR_DESIGNA_LETRADOS"
		scope="request"></bean:define>
		

	<bean:define id="designaFormList" name="designaFormList"
		scope="request"></bean:define>
	<bean:define id="paginaSeleccionada" name="paginaSeleccionada"
		scope="request"></bean:define>
	<bean:define id="totalRegistros" name="totalRegistros" scope="request"></bean:define>
	<bean:define id="registrosPorPagina" name="registrosPorPagina"
		scope="request"></bean:define>
	<c:choose>
		<c:when test="${empty designaFormList}">
			<tr>
				<td colspan="13" class="titulitos" style="text-align: center"><siga:Idioma
					key="messages.noRecordFound" /></td>
			</tr>
		</c:when>
		<c:otherwise>
			<c:forEach items="${designaFormList}" var="designa"
				varStatus="status">
				<input type="hidden" id="anio_${status.count}"
					value="${designa.anio}">
				<input type="hidden" id="numero_${status.count}"
					value="${designa.numero}">
				<input type="hidden" id="idTurno_${status.count}"
					value="${designa.idTurno}">
				<input type="hidden" id="idInstitucion_${status.count}"
					value="${designa.idInstitucion}">
				<input type="hidden"
					id="actuacionRestriccionesActiva_${status.count}"
					value="${designa.actuacionRestriccionesActiva}">
				<input type="hidden"
					id="actuacionValidarJustificaciones_${status.count}"
					value="${designa.actuacionValidarJustificaciones}">
				<input type="hidden" id="fechaDesigna_${status.count}"
					value="${designa.fecha}">
				<c:set var="disabledPorCambioLetrado" value="" />
				<c:if
					test="${designa.cambioLetrado=='S'}">
					<c:set var="disabledPorCambioLetrado" value="disabled='disabled'" />
				</c:if>
				<c:set var="disabledPorArt27" value="" />
				<c:if
					test="${designa.articulo27=='S'}">
					<c:set var="disabledPorArt27" value="disabled='disabled'" />
				</c:if>
				<c:set var="valiDisabled" value="" />
				<c:if
				test="${InformeJustificacionMasivaForm.fichaColegial==true || (designa.actuacionValidarJustificaciones!=null && designa.actuacionValidarJustificaciones=='N') || designa.cambioLetrado=='S' }">
					<c:set var="valiDisabled" value="disabled='disabled'" />
				</c:if>
				
				<c:choose>
					<c:when test="${status.count%2==0}">
						<tr id="fila_${status.count}" class="filaTablaPar">
					</c:when>
					<c:otherwise>
						<tr id="fila_${status.count}" class="filaTablaImpar">
					</c:otherwise>
				</c:choose>
				
				
				
				
					<td rowspan="${designa.rowSpan}"><c:out
						value="${designa.codigoDesigna}" /></td>
						
					<c:choose>
						<c:when test="${InformeJustificacionMasivaForm.fichaColegial==false  && designa.tipoResolucionDesigna=='NO_FAVORABLE'}">
							<td rowspan="${designa.rowSpan}" style="${colorEJG}" title="<siga:Idioma	key="gratuita.informeJustificacionMasiva.resolucionDesignaNoFavorable" />">
								<c:forEach items="${designa.expedientes}" var="ejgForm" varStatus="statusNoFavorable">     
										<c:out value="${ejgForm.nombre}"/>
										<c:if test="${!statusNoFavorable.last}">
										,
										</c:if>
								</c:forEach>
							</td>
						</c:when>
					
						<c:when test="${InformeJustificacionMasivaForm.fichaColegial==false && designa.tipoResolucionDesigna=='SIN_RESOLUCION'}">
							<td rowspan="${designa.rowSpan}" style="${colorEJG}" title="<siga:Idioma	key="gratuita.informeJustificacionMasiva.resolucionDesignaSinResolucion" />">
								<c:forEach items="${designa.expedientes}" var="ejgForm" varStatus="statusSinResolucion">     
										<c:out value="${ejgForm.nombre}"/>
										<c:if test="${!statusSinResolucion.last}">
										,
										</c:if>
								</c:forEach>
							
							</td>
						
						</c:when>
						<c:when test="${InformeJustificacionMasivaForm.fichaColegial==false && designa.tipoResolucionDesigna=='PTE_CAJG'}">
							<td rowspan="${designa.rowSpan}" style="${colorEJG}" title="<siga:Idioma	key="gratuita.informeJustificacionMasiva.resolucionDesignaPteCAJG" />">
								<c:forEach items="${designa.expedientes}" var="ejgForm" varStatus="statusPteCAJG">     
										<c:out value="${ejgForm.nombre}"/>
										<c:if test="${!statusPteCAJG.last}">
										,
										</c:if>
								</c:forEach>
							
							</td>
						
						</c:when>
						
						
						<c:when test="${InformeJustificacionMasivaForm.fichaColegial==false && designa.tipoResolucionDesigna=='SIN_EJG'}">
							<td rowspan="${designa.rowSpan}" style="${colorEJG}">
								<siga:Idioma	key="gratuita.informeJustificacionMasiva.resolucionDesignaSinEjg.abreviado" />
							</td>	
						
						</c:when>
						<c:when test="${InformeJustificacionMasivaForm.fichaColegial==true && designa.tipoResolucionDesigna=='SIN_EJG'}">
							<td rowspan="${designa.rowSpan}" >
								&nbsp;
							</td>	
						
						</c:when>
						
						<c:otherwise>
							<td rowspan="${designa.rowSpan}" >
								<c:forEach items="${designa.expedientes}" var="ejgForm" varStatus="statusSinEjg">     
									<c:choose>            
										<c:when test="${ejgForm.docResolucion!=null && ejgForm.docResolucion!=''}">
											<a href='#' onclick="downloadDocumentoResolucion('${ejgForm.docResolucion}')"><c:out value="${ejgForm.nombre}"/></a>
										</c:when>
										<c:otherwise><c:out value="${ejgForm.nombre}"/></c:otherwise>
									</c:choose>
									<c:if test="${!statusSinEjg.last}">
										,
										</c:if>
								</c:forEach>
							</td>
						</c:otherwise>
					</c:choose>
					
					
					
					<td rowspan="${designa.rowSpan}"><c:choose>
						<c:when test="${designa.juzgado!=null && designa.juzgado!=''}">
							<c:out value="${designa.juzgado}"></c:out>
						</c:when>
						<c:otherwise>
							&nbsp;
						</c:otherwise>
					</c:choose></td>
					<td rowspan="${designa.rowSpan}"><c:out
						value="${designa.fecha}" /></td>
					<td rowspan="${designa.rowSpan}"><c:choose>
						<c:when test="${designa.asunto!=null && designa.asunto!=''}">
							<c:out value="${designa.asunto}"></c:out>
						</c:when>
						<c:otherwise>
					  		&nbsp;
						</c:otherwise>
					</c:choose></td>
					<td rowspan="${designa.rowSpan}"><c:choose>
						<c:when test="${designa.clientes!=null && designa.clientes!=''}">
							<c:out value="${designa.clientes}"></c:out>
						</c:when>
						<c:otherwise>
					  		&nbsp;
						</c:otherwise>
					</c:choose></td>
					
						
					<c:choose>
					
					<c:when
									test="${disabledPorCambioLetrado!=''}">
									<td align="center" rowspan="${designa.rowSpan}" colspan="3">
									
										<siga:Idioma
										key="gratuita.informeJustificacionMasiva.cambioLetrado" />
										
									</td>
									<td><input type="checkbox"
										disabled="disabled" /></td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td><c:choose>
										<c:when test="${designa.baja=='1'}">
											<input type="checkbox" disabled="disabled" checked="checked" />
										</c:when>
										
										<c:otherwise>
											<input type="checkbox" disabled="disabled" />
										</c:otherwise>
									</c:choose>
									</td>
									</tr>
								</c:when>
								
								<c:when
									test="${disabledPorArt27!=''&&disabledPorCambioLetrado==''}">
									<td align="center" rowspan="${designa.rowSpan}" colspan="3">
									
										<siga:Idioma
										key="gratuita.informeJustificacionMasiva.articulo27" />
										
									</td>
									<td><input type="checkbox"
										disabled="disabled" /></td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td><c:choose>
										<c:when test="${designa.baja=='1'}">
											<input type="checkbox" disabled="disabled" checked="checked" />
										</c:when>
										
										<c:otherwise>
											<input type="checkbox" disabled="disabled" />
										</c:otherwise>
									</c:choose>
									</td>
									</tr>
								</c:when>
								
								
								
					
					<c:when test="${designa.permitidoJustificar==false}">
							<td align="center" rowspan="${designa.rowSpan}" colspan="3">
							<c:choose>
								<c:when test="${designa.tipoResolucionDesigna=='NO_FAVORABLE'}">
									<siga:Idioma	key="gratuita.informeJustificacionMasiva.resolucionDesignaNoFavorable" />
							
							</c:when>
							<c:when test="${designa.tipoResolucionDesigna=='PTE_CAJG'}">
									<siga:Idioma	key="gratuita.informeJustificacionMasiva.resolucionDesignaPteCAJG" />
							
							</c:when>
							<c:when test="${designa.tipoResolucionDesigna=='SIN_RESOLUCION'}">
								<siga:Idioma	key="gratuita.informeJustificacionMasiva.resolucionDesignaSinResolucion" />
							
							</c:when>
							<c:when test="${designa.tipoResolucionDesigna=='SIN_EJG'}">
								<siga:Idioma	key="gratuita.informeJustificacionMasiva.resolucionDesignaSinEjg" />
							
							</c:when>
							</c:choose>
							</td>
							<td rowspan="${designa.rowSpan}"><input type="checkbox"
								disabled="disabled" /></td>
							<td rowspan="${designa.rowSpan}">
							
									&nbsp;
							</td>
							<td rowspan="${designa.rowSpan}">
									&nbsp;
								</td>
							<td><c:choose>
								<c:when test="${designa.baja=='1'}">
									<input type="checkbox" disabled="disabled" checked="checked" />
								</c:when>
								<c:when test="${InformeJustificacionMasivaForm.fichaColegial==true}">
									<input type="checkbox" disabled="disabled" />
								</c:when>
								<c:otherwise>
									<input name="checkBaja" id="baja_${status.count}"
										 type="checkbox" />
								</c:otherwise>
							</c:choose></td>
				</tr>
				</c:when>
				<c:otherwise>
					
					
					<c:choose>
					
						<c:when test="${designa.juzgado==''&&empty designa.actuaciones}">
							<td align="center" rowspan="${designa.rowSpan}" colspan="3">
							<siga:Idioma
								key="gratuita.informeJustificacionMasiva.aviso.sinJuzgado" />
							
							</td>
							<td rowspan="${designa.rowSpan}"><input type="checkbox"
								disabled="disabled" /></td>
							<td rowspan="${designa.rowSpan}"><c:choose>
								<c:when
									test="${permitirBotones==true && designa.estado!=null && designa.estado=='V' &&(designa.cambioLetrado=='N'&&(InformeJustificacionMasivaForm.fichaColegial==false||editarDesignaLetrados=='1'))}">
									<img id="iconoboton_editar1"
										src="<html:rewrite page='/html/imagenes/beditar_off.gif'/>"
										style="cursor: hand;" alt="Editar" name="editar_1" border="0"
										onClick="accionEditarDesigna(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},${designa.fecha});"
										onMouseOut="MM_swapImgRestore()"
										onMouseOver="MM_swapImage('editar_1','','<html:rewrite page='/html/imagenes/beditar_on.gif'/>',1)">
								</c:when>
								<c:otherwise>
									&nbsp;
								</c:otherwise>
							</c:choose></td>
							<td rowspan="${designa.rowSpan}">
									&nbsp;
							</td>
							<td><c:choose>
								<c:when test="${designa.baja=='1'}">
									<input type="checkbox" disabled="disabled" checked="checked" />
								</c:when>
								<c:when test="${InformeJustificacionMasivaForm.fichaColegial==true}">
									<input type="checkbox" disabled="disabled" />
								</c:when>
								
								<c:otherwise>
									<input name="checkBaja" id="baja_${status.count}"
										 type="checkbox" />
								</c:otherwise>
							</c:choose></td>
				</tr>
				</c:when>

				<c:otherwise>

					<c:choose>
						<c:when
							test="${designa.idProcedimiento==''&&empty designa.actuaciones}">
							<td align="center" rowspan="${designa.rowSpan}" colspan="3">
							<siga:Idioma
								key="gratuita.informeJustificacionMasiva.aviso.sinModulo" />
							</td>
							<td rowspan="${designa.rowSpan}"><input type="checkbox"
								disabled="disabled" /></td>
							<td rowspan="${designa.rowSpan}"><c:choose>
								<c:when
									test="${permitirBotones==true &&designa.estado!=null && designa.estado=='V' &&(designa.cambioLetrado=='N'&&(InformeJustificacionMasivaForm.fichaColegial==false||editarDesignaLetrados=='1'))}">
									<img id="iconoboton_editar1"
										src="<html:rewrite page='/html/imagenes/beditar_off.gif'/>"
										style="cursor: hand;" alt="Editar" name="editar_1" border="0"
										onClick="accionEditarDesigna(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},${designa.fecha});"
										onMouseOut="MM_swapImgRestore()"
										onMouseOver="MM_swapImage('editar_1','','<html:rewrite page='/html/imagenes/beditar_on.gif'/>',1)">
								</c:when>
								<c:otherwise>
											&nbsp;
										</c:otherwise>
							</c:choose></td>
							<td rowspan="${designa.rowSpan}">
									&nbsp;
							</td>
							<td><c:choose>
								<c:when test="${designa.baja=='1'}">
									<input type="checkbox" disabled="disabled" checked="checked" />
								</c:when>
								<c:when test="${InformeJustificacionMasivaForm.fichaColegial==true}">
									<input type="checkbox" disabled="disabled" />
								</c:when>
								
								<c:otherwise>
									<input name="checkBaja" id="baja_${status.count}"
										 type="checkbox" />
								</c:otherwise>
							</c:choose></td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:choose>
								
								<c:when test="${empty designa.actuaciones }">
									
								
								
									<c:choose>
										<c:when
											test="${designa.categoria!=null && designa.categoria!=''}">
											<td title="${designa.descripcionProcedimiento}"
												rowspan="${designa.rowSpan}"><c:out
												value="${designa.categoria}" /></td>
										</c:when>
										<c:when
											test="${designa.categoria!=null && designa.categoria==''}">
											<td title="${designa.descripcionProcedimiento}"
												rowspan="${designa.rowSpan}">&nbsp;</td>
										</c:when>
										<c:otherwise>
											<td rowspan="${designa.rowSpan}">&nbsp;</td>
										</c:otherwise>
									</c:choose>
									<td rowspan="${designa.rowSpan}">&nbsp;</td>

									<c:choose>
										<c:when test="${empty designa.acreditaciones}">
											<td align="center" rowspan="${designa.rowSpan}">Modulo
											sin acreditaciones</td>
											<td><input type="checkbox" disabled="disabled" /></td>
											<td rowspan="${designa.rowSpan}"><c:choose>
												<c:when
													test="${permitirBotones==true &&designa.estado!=null && designa.estado=='V'&&(designa.cambioLetrado=='N'&&(InformeJustificacionMasivaForm.fichaColegial==false||editarDesignaLetrados=='1'))}">
													<img id="iconoboton_editar1"
														src="<html:rewrite page='/html/imagenes/beditar_off.gif'/>"
														style="cursor: hand;" alt="Editar" name="editar_1"
														border="0"
														onClick="accionEditarDesigna(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},${designa.fecha});"
														onMouseOut="MM_swapImgRestore()"
														onMouseOver="MM_swapImage('editar_1','','<html:rewrite page='/html/imagenes/beditar_on.gif'/>',1)">
												</c:when>
												<c:otherwise>
															&nbsp;
														</c:otherwise>
											</c:choose></td>
											<td rowspan="${designa.rowSpan}">
															&nbsp;
											</td>
											<td><c:choose>
												<c:when test="${designa.baja=='1'}">
													<input type="checkbox" disabled="disabled"
														checked="checked" />
												</c:when>
												<c:when test="${InformeJustificacionMasivaForm.fichaColegial==true}">
													<input type="checkbox" disabled="disabled" />
												</c:when>
												
												<c:otherwise>
													<input name="checkBaja" id="baja_${status.count}"
														 type="checkbox" />
												</c:otherwise>
											</c:choose></td>
											</tr>
										</c:when>
										<c:otherwise>
											<input type="hidden"
												id="multiplesComplementos_${status.count}"
												value="${designa.multiplesComplementos}">
											<c:forEach items="${designa.acreditaciones}"
												var="acreditacionesMap">
												<c:set var="listAcreditaciones" scope="page"
													value="${acreditacionesMap.value}" />
												<c:choose>
													<c:when test="${empty listAcreditaciones}">
														<td align="center" rowspan="${designa.rowSpan}"><c:choose>
															<c:when test="${designa.baja=='1'}">
																<siga:Idioma
																	key="gratuita.informeJustificacionMasiva.literal.designaSinActuaciones" />
															</c:when>
															<c:otherwise>
																<siga:Idioma
																	key="gratuita.informeJustificacionMasiva.literal.moduloSinAcreditaciones" />
															</c:otherwise>
														</c:choose></td>
														<td><input type="checkbox" disabled="disabled" /></td>
														<td rowspan="${designa.rowSpan}"><c:choose>
															<c:when
																test="${permitirBotones==true &&designa.estado!=null && designa.estado=='V'&&(designa.cambioLetrado=='N'&&(InformeJustificacionMasivaForm.fichaColegial==false||editarDesignaLetrados=='1'))}">
																<img id="iconoboton_editar1"
																	src="<html:rewrite page='/html/imagenes/beditar_off.gif'/>"
																	style="cursor: hand;" alt="Editar" name="editar_1"
																	border="0"
																	onClick="accionEditarDesigna(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},${designa.fecha});"
																	onMouseOut="MM_swapImgRestore()"
																	onMouseOver="MM_swapImage('editar_1','','<html:rewrite page='/html/imagenes/beditar_on.gif'/>',1)">
															</c:when>
															<c:otherwise>
																		&nbsp;
																	</c:otherwise>
														</c:choose></td>
														<td rowspan="${designa.rowSpan}">
																&nbsp;
														</td>
														<td><c:choose>
															<c:when test="${designa.baja=='1'}">
																<input type="checkbox" disabled="disabled"
																	checked="checked" />
															</c:when>
															<c:when test="${InformeJustificacionMasivaForm.fichaColegial==true}">
																<input type="checkbox" disabled="disabled" />
															</c:when>
															
															<c:otherwise>
																<input name="checkBaja" id="baja_${status.count}"
																	 type="checkbox" />
															</c:otherwise>
														</c:choose></td>
														</tr>
													</c:when>
													<c:otherwise>
														<c:forEach var="acreditacion"
															items="${listAcreditaciones}"
															varStatus="estadoAcreditacion">
															<c:choose>
																<c:when test="${estadoAcreditacion.first}">
																	<td><input name="checkAcreditacion"
																		id="acre_${status.count}_x_${acreditacion.idTipo}_${acreditacion.id}_${acreditacion.idProcedimiento}_${acreditacion.idJuzgado}_0_${designa.idJurisdiccion}"
																		onclick="onCheckAcreditacion(this);"
																		 type="checkbox" /><c:out
																		value="${acreditacion.descripcion}" /> <input
																		name="${status.count}_${acreditacion.idProcedimiento}_checkAcreditacion"
																		id="checkacre_${status.count}_x_${acreditacion.idTipo}_${acreditacion.id}_${acreditacion.idProcedimiento}_${acreditacion.idJuzgado}_0_${designa.idJurisdiccion}"
																		type="hidden" /></td>
																	<td title="<siga:Idioma	key='gratuita.informeJustificacionMasiva.informacion.validacion'/>"><input name="checkValidacion"
																		id="vali_${status.count}_x_${acreditacion.idTipo}_${acreditacion.id}_${acreditacion.idProcedimiento}_${acreditacion.idJuzgado}_0_${designa.idJurisdiccion}"
																		type="checkbox" onclick="onCheckValidacion(this);" ${valiDisabled}/></td>
																	<td rowspan="${designa.rowSpan}"><c:choose>
																		<c:when
																			test="${permitirBotones==true &&designa.estado!=null && designa.estado=='V'&&(designa.cambioLetrado=='N'&&(InformeJustificacionMasivaForm.fichaColegial==false||editarDesignaLetrados=='1'))}">
																			<img id="iconoboton_editar1"
																				src="<html:rewrite page='/html/imagenes/beditar_off.gif'/>"
																				style="cursor: hand;" alt="Editar" name="editar_1"
																				border="0"
																				onClick="accionEditarDesigna(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},${designa.fecha});"
																				onMouseOut="MM_swapImgRestore()"
																				onMouseOver="MM_swapImage('editar_1','','<html:rewrite page='/html/imagenes/beditar_on.gif'/>',1)">
																		</c:when>
																		<c:otherwise>
																					&nbsp;
																				</c:otherwise>
																	</c:choose></td>
																	<td rowspan="${designa.rowSpan}">
																					&nbsp;
																	</td>
																	<td rowspan="${designa.rowSpan}"><c:choose>
																		<c:when test="${designa.baja=='1'}">
																			<input type="checkbox" disabled="disabled"
																				checked="checked" />
																		</c:when>
																		<c:when test="${InformeJustificacionMasivaForm.fichaColegial==true}">
																			<input type="checkbox" disabled="disabled" />
																		</c:when>
																		
																		<c:otherwise>
																			<input name="checkBaja" id="baja_${status.count}"
																				 type="checkbox" />
																		</c:otherwise>
																	</c:choose></td>
																	</tr>
																</c:when>
																<c:otherwise>
																		<c:choose>
																			<c:when test="${status.count%2==0}">
																				<tr class="filaTablaPar">
																			</c:when>
																			<c:otherwise>
																				<tr class="filaTablaImpar">
																			</c:otherwise>
																		</c:choose>
																		<td><input name="checkAcreditacion"
																			id="acre_${status.count}_x_${acreditacion.idTipo}_${acreditacion.id}_${acreditacion.idProcedimiento}_${acreditacion.idJuzgado}_0_${designa.idJurisdiccion}"
																			onclick="onCheckAcreditacion(this);"
																			 type="checkbox" /><c:out
																			value="${acreditacion.descripcion}" /> <input
																			name="${status.count}_${acreditacion.idProcedimiento}_checkAcreditacion"
																			id="checkacre_${status.count}_x_${acreditacion.idTipo}_${acreditacion.id}_${acreditacion.idProcedimiento}_${acreditacion.idJuzgado}_0_${designa.idJurisdiccion}"
																			type="hidden" /></td>
																		<td title="<siga:Idioma	key='gratuita.informeJustificacionMasiva.informacion.validacion'/>"><input name="chechValidacion"
																			id="vali_${status.count}_x_${acreditacion.idTipo}_${acreditacion.id}_${acreditacion.idProcedimiento}_${acreditacion.idJuzgado}_0_${designa.idJurisdiccion}"
																			type="checkbox" onclick="onCheckValidacion(this);" ${valiDisabled}/></td>
																	</tr>
																</c:otherwise>
															</c:choose>
														</c:forEach>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:forEach items="${designa.actuaciones}" var="actuacionesMap"
										varStatus="estadoMapActuaciones">
										<c:set var="listActuaciones" scope="page"
											value="${actuacionesMap.value}" />
										<c:forEach var="actuacion" items="${listActuaciones}"
											varStatus="estadoListActuaciones">
											<c:choose>
												<c:when
													test="${estadoListActuaciones.first&&estadoMapActuaciones.first}">
													<c:choose>
														<c:when
															test="${actuacion.categoria!=null && actuacion.categoria!=''}">
															<td title="${actuacion.descripcionProcedimiento}"><c:out
																value="${actuacion.categoria}" /></td>
														</c:when>
														<c:when
															test="${actuacion.categoria!=null && actuacion.categoria==''}">
															<td title="${actuacion.descripcionProcedimiento}">&nbsp;</td>
														</c:when>
														<c:otherwise>
															<td>&nbsp;</td>
														</c:otherwise>
													</c:choose>
													<td><c:out value="${actuacion.numero}" /></td>
													<td><c:choose>
														<c:when
															test="${actuacion.fechaJustificacion==null || actuacion.fechaJustificacion==''}">
															<input name="checkAcreditacion"
																id="acre_${status.count}_${actuacion.numero}_${actuacion.acreditacion.idTipo}_${actuacion.acreditacion.id}_${actuacion.idProcedimiento}_${actuacion.idJuzgado}_0_${actuacion.idJurisdiccion}"
																onclick="onCheckAcreditacion(this);"
																 type="checkbox" />
															<input
																name="${status.count}_${actuacion.acreditacion.idProcedimiento}_checkAcreditacion"
																id="checkacre_${status.count}_${actuacion.numero}_${actuacion.acreditacion.idTipo}_${actuacion.acreditacion.id}_${actuacion.idProcedimiento}_${actuacion.idJuzgado}_0_${actuacion.idJurisdiccion}"
																type="hidden" />
															<c:out value="${actuacion.descripcion}" />
														</c:when>
														<c:otherwise>
															<input
																name="${status.count}_${actuacion.acreditacion.idProcedimiento}_checkAcreditacion"
																id="hiddacre_${status.count}_${actuacion.numero}_${actuacion.acreditacion.idTipo}_${actuacion.acreditacion.id}_${actuacion.idProcedimiento}_${actuacion.idJuzgado}_1_${actuacion.idJurisdiccion}"
																type="hidden" />
															<c:out value="${actuacion.descripcion}" />
														</c:otherwise>
													</c:choose></td>
													
													<td title="<siga:Idioma	key='gratuita.informeJustificacionMasiva.informacion.validacion'/>"><c:choose>
														<c:when test="${actuacion.validada=='1'}">
															<input type="checkbox" disabled="disabled"
																checked="checked" />
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${designa.baja=='1'}">
																	<input type="checkbox" disabled="disabled" />
																</c:when>
																<c:otherwise>
																	<c:choose>
																		<c:when
																			test="${actuacion.fechaJustificacion==null || actuacion.fechaJustificacion==''}">
																			<input name="checkValidacion"
																				id="vali_${status.count}_${actuacion.numero}_${actuacion.acreditacion.idTipo}_${actuacion.acreditacion.id}_${actuacion.idProcedimiento}_${actuacion.idJuzgado}_0_${actuacion.idJurisdiccion}"
																				type="checkbox" onclick="onCheckValidacion(this);" ${valiDisabled}/>
																		</c:when>
																		<c:otherwise>
																			<input name="checkValidacion"
																				id="vali_${status.count}_${actuacion.numero}_${actuacion.acreditacion.idTipo}_${actuacion.acreditacion.id}_${actuacion.idProcedimiento}_${actuacion.idJuzgado}_1_${actuacion.idJurisdiccion}"
																				type="checkbox" onclick="onCheckValidacion(this);" ${valiDisabled}/>
																		</c:otherwise>
																	</c:choose>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose></td>
													<td><c:choose>
													
														<c:when
															test="${permitirBotones==true && designa.estado!=null && designa.estado=='V'&& InformeJustificacionMasivaForm.fichaColegial==true}">
															<img id="iconoboton_consultar1"
																src="<html:rewrite page='/html/imagenes/bconsultar_off.gif'/>"
																style="cursor: hand;" alt="Consultar" name="consultar_1"
																border="0"
																onClick="accionConsultarActuacion(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},${actuacion.numero});"
																onMouseOut="MM_swapImgRestore()"
																onMouseOver="MM_swapImage('consultar_1','','<html:rewrite page='/html/imagenes/bconsultar_on.gif'/>',1)">
														</c:when>
														<c:when
															test="${(permitirBotones==true && designa.estado!=null && designa.estado=='V'&& InformeJustificacionMasivaForm.fichaColegial==false)&&(designa.cambioLetrado=='S' || (actuacion.idFacturacion!=null&&actuacion.idFacturacion!=''))}">
															<img id="iconoboton_consultar1"
																src="<html:rewrite page='/html/imagenes/bconsultar_off.gif'/>"
																style="cursor: hand;" alt="Consultar" name="consultar_1"
																border="0"
																onClick="accionConsultarActuacion(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},${actuacion.numero});"
																onMouseOut="MM_swapImgRestore()"
																onMouseOver="MM_swapImage('consultar_1','','<html:rewrite page='/html/imagenes/bconsultar_on.gif'/>',1)">
														</c:when>
													
														<c:when
															test="${(permitirBotones==true && designa.estado!=null && designa.estado=='V'&& InformeJustificacionMasivaForm.fichaColegial==false)}">
															<img id="iconoboton_editar1"
																src="<html:rewrite page='/html/imagenes/beditar_off.gif'/>"
																style="cursor: hand;" alt="Editar" name="editar_1"
																border="0"
																onClick="accionEditarActuacion(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},${actuacion.numero},'${designa.actuacionValidarJustificaciones}');" 
																onMouseOut="MM_swapImgRestore()"
																onMouseOver="MM_swapImage('editar_1','','<html:rewrite page='/html/imagenes/beditar_on.gif'/>',1)">
														</c:when>
														<c:otherwise>
															&nbsp;
														</c:otherwise>
													</c:choose></td>
													<td rowspan="${designa.rowSpan}"><c:choose>
														<c:when
															test="${permitirBotones==true &&designa.estado!=null && designa.estado=='V'&& ((designa.actuacionPermitidaLetrado=='1'&&InformeJustificacionMasivaForm.fichaColegial==true)||(designa.cambioLetrado=='N'&&InformeJustificacionMasivaForm.fichaColegial==false))}">
															<img src="<html:rewrite page='/html/imagenes/icono+.gif'/>"
																style="cursor: hand;"
																alt="<siga:Idioma key="gratuita.informeJustificacionMasiva.nuevaActuacion"/>"
																name="" border="0"
																onclick="accionNuevaActuacion(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},'${designa.actuacionValidarJustificaciones}');" />
														</c:when>
														<c:otherwise>
																		&nbsp;
																	</c:otherwise>
													</c:choose></td>
													<td rowspan="${designa.rowSpan}"><c:choose>
														<c:when test="${designa.baja=='1'}">
															<input type="checkbox" disabled="disabled"
																checked="checked" />
														</c:when>
														<c:when test="${InformeJustificacionMasivaForm.fichaColegial==true}">
															<input type="checkbox" disabled="disabled" />
														</c:when>
														
														<c:otherwise>
															<input name="checkBaja" id="baja_${status.count}"
																 type="checkbox" />
														</c:otherwise>
													</c:choose></td>
													</tr>
												</c:when>
												<c:otherwise>
														<c:choose>
															<c:when test="${status.count%2==0}">
																<tr class="filaTablaPar">
															</c:when>
															<c:otherwise>
																<tr class="filaTablaImpar">
															</c:otherwise>
														</c:choose>
														<c:choose>
															<c:when
																test="${actuacion.categoria!=null && actuacion.categoria!=''}">
																<td title="${actuacion.descripcionProcedimiento}">
																<c:out value="${actuacion.categoria}" /></td>
															</c:when>
															<c:when
															test="${actuacion.categoria!=null && actuacion.categoria==''}">
															<td title="${actuacion.descripcionProcedimiento}">&nbsp;</td>
														</c:when>
															<c:otherwise>
																<td>&nbsp;</td>
															</c:otherwise>
														</c:choose>

														<td><c:out value="${actuacion.numero}" /></td>
														<td><c:choose>
															<c:when
																test="${actuacion.fechaJustificacion==null || actuacion.fechaJustificacion==''}">
																<input name="checkAcreditacion"
																	id="acre_${status.count}_${actuacion.numero}_${actuacion.acreditacion.idTipo}_${actuacion.acreditacion.id}_${actuacion.idProcedimiento}_${actuacion.idJuzgado}_0_${actuacion.idJurisdiccion}"
																	onclick="onCheckAcreditacion(this);"
																	 type="checkbox" />
																<input
																	name="${status.count}_${actuacion.acreditacion.idProcedimiento}_checkAcreditacion"
																	id="checkacre_${status.count}_${actuacion.numero}_${actuacion.acreditacion.idTipo}_${actuacion.acreditacion.id}_${actuacion.idProcedimiento}_${actuacion.idJuzgado}_0_${actuacion.idJurisdiccion}"
																	type="hidden" />
																<c:out value="${actuacion.descripcion}" />
															</c:when>
															<c:otherwise>
																<input
																	name="${status.count}_${actuacion.acreditacion.idProcedimiento}_checkAcreditacion"
																	id="hiddacre_${status.count}_${actuacion.numero}_${actuacion.acreditacion.idTipo}_${actuacion.acreditacion.id}_${actuacion.idProcedimiento}_${actuacion.idJuzgado}_1_${actuacion.idJurisdiccion}"
																	type="hidden" />
																<c:out value="${actuacion.descripcion}" />
															</c:otherwise>
														</c:choose></td>
														<td title="<siga:Idioma	key='gratuita.informeJustificacionMasiva.informacion.validacion'/>"><c:choose>
															<c:when test="${actuacion.validada=='1'}">
																<input type="checkbox" disabled="disabled"
																	checked="checked" />
															</c:when>
															<c:otherwise>
																<c:choose>
																	<c:when test="${designa.baja=='1'}">
																		<input type="checkbox" disabled="disabled" />
																	</c:when>
																	<c:otherwise>
																		<c:choose>
																			<c:when
																				test="${actuacion.fechaJustificacion==null || actuacion.fechaJustificacion==''}">
																				<input name="checkValidacion"
																					id="vali_${status.count}_${actuacion.numero}_${actuacion.acreditacion.idTipo}_${actuacion.acreditacion.id}_${actuacion.idProcedimiento}_${actuacion.idJuzgado}_0_${actuacion.idJurisdiccion}"
																					type="checkbox" onclick="onCheckValidacion(this);" ${valiDisabled}/>
																			</c:when>
																			<c:otherwise>
																				<input name="checkValidacion"
																					id="vali_${status.count}_${actuacion.numero}_${actuacion.acreditacion.idTipo}_${actuacion.acreditacion.id}_${actuacion.idProcedimiento}_${actuacion.idJuzgado}_1_${actuacion.idJurisdiccion}"
																					type="checkbox" onclick="onCheckValidacion(this);" ${valiDisabled}/>
																			</c:otherwise>
																		</c:choose>
																	</c:otherwise>
																</c:choose>
															</c:otherwise>
														</c:choose></td>
														<td><c:choose>
														
														<c:when
															test="${permitirBotones==true && designa.estado!=null && designa.estado=='V'&& InformeJustificacionMasivaForm.fichaColegial==true}">
															<img id="iconoboton_consultar1"
																src="<html:rewrite page='/html/imagenes/bconsultar_off.gif'/>"
																style="cursor: hand;" alt="Consultar" name="consultar_1"
																border="0"
																onClick="accionConsultarActuacion(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},${actuacion.numero});"
																onMouseOut="MM_swapImgRestore()"
																onMouseOver="MM_swapImage('consultar_1','','<html:rewrite page='/html/imagenes/bconsultar_on.gif'/>',1)">
														</c:when>
														<c:when
															test="${(permitirBotones==true && designa.estado!=null && designa.estado=='V'&& InformeJustificacionMasivaForm.fichaColegial==false)&&(designa.cambioLetrado=='S' || (actuacion.idFacturacion!=null&&actuacion.idFacturacion!=''))}">
															<img id="iconoboton_consultar1"
																src="<html:rewrite page='/html/imagenes/bconsultar_off.gif'/>"
																style="cursor: hand;" alt="Consultar" name="consultar_1"
																border="0"
																onClick="accionConsultarActuacion(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},${actuacion.numero});"
																onMouseOut="MM_swapImgRestore()"
																onMouseOver="MM_swapImage('consultar_1','','<html:rewrite page='/html/imagenes/bconsultar_on.gif'/>',1)">
														</c:when>
													
														<c:when
															test="${(permitirBotones==true && designa.estado!=null && designa.estado=='V'&& InformeJustificacionMasivaForm.fichaColegial==false)}">
															<img id="iconoboton_editar1"
																src="<html:rewrite page='/html/imagenes/beditar_off.gif'/>"
																style="cursor: hand;" alt="Editar" name="editar_1"
																border="0"
																onClick="accionEditarActuacion(${designa.anio},${designa.idTurno},${designa.numero},${designa.idInstitucion},${actuacion.numero},'${designa.actuacionValidarJustificaciones}');" 
																onMouseOut="MM_swapImgRestore()"
																onMouseOver="MM_swapImage('editar_1','','<html:rewrite page='/html/imagenes/beditar_on.gif'/>',1)">
														</c:when>
															<c:otherwise>
																			&nbsp;
																		</c:otherwise>
														</c:choose></td>
													</tr>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										<c:set var="keyAcreditaciones" scope="page"
											value="${actuacionesMap.key}" />
										<c:set var="listAcreditacionesPtes" scope="page"
											value="${designa.acreditaciones[keyAcreditaciones]}" />
										<c:if
											test="${listAcreditacionesPtes!=null && not empty designa.acreditaciones}">
											<c:forEach var="acreditacionPte"
												items="${listAcreditacionesPtes}"
												varStatus="estadoListAcreditacionesPte">
												<c:choose>
													<c:when test="${status.count%2==0}">
														<tr class="filaTablaPar">
													</c:when>
													<c:otherwise>
														<tr class="filaTablaImpar">
													</c:otherwise>
												</c:choose>
													<td>&nbsp;</td>
													<td>&nbsp;</td>
													<td><input name="checkAcreditacion"
														id="acre_${status.count}_x_${acreditacionPte.idTipo}_${acreditacionPte.id}_${acreditacionPte.idProcedimiento}_${acreditacionPte.idJuzgado}_0_${acreditacionPte.idJurisdiccion}"
														onclick="onCheckAcreditacion(this);"
														 type="checkbox" /><c:out
														value="${acreditacionPte.descripcion}" /> <input
														name="${status.count}_${acreditacionPte.idProcedimiento}_checkAcreditacion"
														id="checkacre_${status.count}_x_${acreditacionPte.idTipo}_${acreditacionPte.id}_${acreditacionPte.idProcedimiento}_${acreditacionPte.idJuzgado}_0_${acreditacionPte.idJurisdiccion}"
														type="hidden" /></td>
													<td title="<siga:Idioma	key='gratuita.informeJustificacionMasiva.informacion.validacion'/>"><input name="checkValidacion"
														id="vali_${status.count}_x_${acreditacionPte.idTipo}_${acreditacionPte.id}_${acreditacionPte.idProcedimiento}_${acreditacionPte.idJuzgado}_0_${acreditacionPte.idJurisdiccion}"
														type="checkbox" onclick="onCheckValidacion(this);" ${valiDisabled}/>
													</td>

													<c:if test="${estadoListAcreditacionesPte.first}">
														<td rowspan="${acreditacionPte.rowSpan}">&nbsp;</td>
													</c:if>
												</tr>
											</c:forEach>
										</c:if>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
		
	</c:choose>
	</c:otherwise>
		</c:choose>
	</c:forEach>
	</c:otherwise>
	</c:choose>
</table>



</div>
<siga:Paginador totalRegistros="${totalRegistros}"
	registrosPorPagina="${registrosPorPagina}"
	paginaSeleccionada="${paginaSeleccionada}" idioma="${usrBean.language}"
	modo="buscarPor" clase="paginator" 
	divStyle="position:absolute; width:100%; height:20; z-index:3; bottom:26px; left: 0px"
	distanciaPaginas="" action="${pageContext.request.contextPath}${path}.do?noReset=true"
	preFunction="preFunction" />



<c:choose>
<c:when test="${permitirBotones==true && not empty designaFormList}">
	<table class="botonesDetalle" align="center">
		<tr>
			<td style="width: 900px;">&nbsp;</td>
			<td class="tdBotones"><input type="button" alt="Guardar"
				id="idButton" onclick="return accionGuardar(${usrBean.letrado});" class="button"
				name="idButton" value="Guardar"></td>
			<td class="tdBotones"><input type="button" alt="Informe Justif."
				id="idInformeJustificacion" onclick="return informeGenerico();"
				class="button" name="idButton" value="Informe Justif."></td>
		</tr>
	</table>
</c:when>
<c:when test="${permitirBotones==false && not empty designaFormList}">
	<table class="botonesDetalle" align="center">
		<tr>
			<td style="width: 900px;">&nbsp;</td>
			<td class="tdBotones"><input type="button" alt="Guardar"
				id="idButton" onclick="return accionGuardar(${usrBean.letrado});" class="button"
				name="idButton" value="Guardar" style="display: none;"></td>
			<td class="tdBotones"><input type="button" alt="Informe Justif."
				id="idInformeJustificacion" onclick="return informeGenerico();"
				class="button" name="idButton" value="Informe Justif."></td>
		</tr>
	</table>
</c:when>


<c:otherwise>
	<table class="botonesDetalle" align="center" >
		<tr>
			<td style="width: 900px;">&nbsp;</td>
			<td class="tdBotones"><input type="button" alt="Guardar"
				id="idButton" onclick="return accionGuardar(${usrBean.letrado});" class="button"
				name="idButton" value="Guardar" style="display: none;"></td>
			<td class="tdBotones"><input type="button" alt="Informe Justif."
				id="idInformeJustificacion" onclick="return informeJustificacion();"
				class="button" name="idButton" value="Informe Justif." style="display: none;"></td>
		</tr>
	</table>

</c:otherwise>
</c:choose>

<html:form action="/JGR_ActuacionesDesigna" method="post"
	target="mainWorkArea" style="display:none">
	<html:hidden property="modo" />
	<html:hidden property="actionModal" value="" />
	<html:hidden property="anio" />
	<html:hidden property="idTurno" />
	<html:hidden property="numero" />
	<html:hidden property="nactuacion" />
	<html:hidden property="fichaColegial" value="${InformeJustificacionMasivaForm.fichaColegial}"/>
	<html:hidden property="deDonde" value="${path}" />
	<html:hidden property="actuacionValidada" />
	

</html:form>

<html:form action="/JGR_MantenimientoDesignas" method="post"
	target="mainWorkArea" style="display:none">
	<html:hidden property="modo" value="abrir" />
	<html:hidden property="actionModal" value="" />
	<html:hidden property="anio" />
	<html:hidden property="idTurno" />
	<html:hidden property="numero" />
	<html:hidden property="fecha" />

</html:form>

<html:form action="/INF_InformesGenericos" method="post"	target="submitArea">
	<html:hidden property="idInstitucion" />
	<html:hidden property="idTipoInforme" value="JUSDE"/>
	<html:hidden property="enviar" value="0"/>
	<html:hidden property="descargar" value="1"/>
	<html:hidden property="datosInforme"/>
	<html:hidden property="modo" value = "preSeleccionInformes"/>
	<input type='hidden' name='actionModal'>
</html:form>
<!-- Formulario para la edicion del envio -->
<form name="DefinirEnviosForm" method="POST" action="/SIGA/ENV_DefinirEnvios.do" target="mainWorkArea">
	<input type="hidden" name="modo" value="">
	<input type="hidden" name="tablaDatosDinamicosD" value="">

</form>

<iframe name="submitArea"
	src="<html:rewrite page='/html/jsp/general/blank.jsp'/>"
	style="display: none"></iframe>
<!-- FIN: SUBMIT AREA -->
<script>
	ajustarAltoResultados();
</script>
</body>

</html>

