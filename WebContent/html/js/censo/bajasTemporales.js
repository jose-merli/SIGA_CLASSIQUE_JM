	function accionImportarXLS(){
		alert("Funcionalidad pendiente");	
			
	}
	function accionComboSituacion(){
		var situacion = document.BajasTemporalesForm.situacion.value;
		if(situacion=='B'){
			document.getElementById('estadoBaja').disabled=false;
			
		}else{
			document.getElementById('estadoBaja').selectedIndex=0
			document.getElementById('estadoBaja').disabled=true;
		}
	}
	function accionComboEstadoBaja(){
		var estadoBaja = document.BajasTemporalesForm.estadoBaja.value;
		if(estadoBaja=='P'){
			document.getElementById('idValidarSolicitud').disabled=false;
			document.getElementById('idDenegarSolicitud').disabled=false;
			return;
		}
		if (estadoBaja=='V'){
			document.getElementById('idValidarSolicitud').disabled=true;
			document.getElementById('idDenegarSolicitud').disabled=false;
			return;
		}
		if (estadoBaja=='D'){
			document.getElementById('idValidarSolicitud').disabled=false;
			document.getElementById('idDenegarSolicitud').disabled=true;
			return;
		
		}
	}
	

	

	function limpiarColegiado(){
		document.BajasTemporalesForm.idPersona.value = '';
		document.BajasTemporalesForm.colegiadoNumero.value = '';
		document.BajasTemporalesForm.colegiadoNombre.value = '';
		// actualizarResultados();
	}
	function buscarColegiado(){
		var resultado=ventaModalGeneral("busquedaClientesModalForm","G");
		if (resultado!=undefined && resultado[0]!=undefined ){
			
			document.BajasTemporalesForm.idPersona.value       = resultado[0];
			document.BajasTemporalesForm.colegiadoNumero.value    = resultado[2];
			document.BajasTemporalesForm.colegiadoNombre.value   = resultado[4]+' '+resultado[5]+' '+resultado[6];
		}
	}
	function buscar(){
		refrescarLocal();
	}
	function refrescarLocal(){
		document.getElementById('idBusqueda').onclick();
	}
	function preAccionBusqueda(){
	}
	
	function postAccionBusqueda(){
		validarAnchoTabla();
		accionComboEstadoBaja();
		fin();
	}
	function preAccionColegiado(){
		sub();
	}
	function postAccionColegiado(){
		fin();
		
	}
	
	
	function validarAnchoTabla () 
	{
		// if (document.all.bajasTemporales.clientHeight <= document.all.divBajasTemporales.clientHeight) {
			//document.all.tabBajasTemporalesCabeceras.width='100%';
	// 	}
		// else {
			//document.all.tabBajasTemporalesCabeceras.width='98.30%';
		// }
	}
	
	function onInit(){
		if (document.getElementById('deshabilitarValidaciones') != null){
			if(document.getElementById('deshabilitarValidaciones').value=='true'){
			 	document.getElementById('idValidarSolicitud').disabled=true;
			    document.getElementById('idDenegarSolicitud').disabled=true;
		    }else{
		    	if (document.getElementById('idValidarSolicitud') != null)
		    		document.getElementById('idValidarSolicitud').disabled=false;
		    	if (document.getElementById('idDenegarSolicitud') != null)
		    		document.getElementById('idDenegarSolicitud').disabled=false;
		    }
		}
		
    }
	function accionNuevo(desdeFichaColegial,msjSeleccionar){
		if(desdeFichaColegial){
			document.FormBajasTemporales.idPersona.value = document.getElementById('idPersonaFichaColegial').value;
			document.FormBajasTemporales.colegiadoNumero.value = document.getElementById('colegiadoNumeroFichaColegial').value;
			document.FormBajasTemporales.colegiadoNombre.value = document.getElementById('colegiadoNombreFichaColegial').value;
			document.FormBajasTemporales.datosSeleccionados.value=document.getElementById('idPersonaFichaColegial').value;
		
		}else{
			var ele = document.getElementsByName('chkBajasTemporales');
			
			var datos='';
			
			for (fila = 0; fila < ele.length; fila++) {
			    if(ele[fila].checked){
					var idInstitucion = 'idInstitucion_' + fila ;
				   	var idPersona = 'idPersona_' + fila ;
				   	idInstitucion = document.getElementById(idInstitucion).value;
				   	document.FormBajasTemporales.idInstitucion.value = idInstitucion;
				   	idPersona = document.getElementById(idPersona).value;
	                datos = datos +"@@"+idPersona; 
				
				}
				
			}
			if(datos==''){
				alert (msjSeleccionar);
				
				return false;
			}
			datos=datos.substring(2);
			document.FormBajasTemporales.idPersona.value = '';
			document.FormBajasTemporales.datosSeleccionados.value=datos;
		}
		document.FormBajasTemporales.modo.value="nuevaSolicitud";
		
		var resultado = ventaModalGeneral(document.FormBajasTemporales.name,"P");
		if (resultado=='MODIFICADO') {
			if(desdeFichaColegial){
				document.FormBajasTemporales.target="submitArea";
				document.FormBajasTemporales.modo.value="refrescar";
				document.FormBajasTemporales.submit();
				document.FormBajasTemporales.target="mainWorkArea";
			
			}else{
				refrescarLocal();

			
			}
		}
		
	}
	
	
	function accionDenegarSolicitud(msjSeleccionar){
		var ele = document.getElementsByName('chkBajasTemporales');
		var datos='';
		
		for (fila = 0; fila < ele.length; fila++) {
		    if(ele[fila].checked){
				var idInstitucion = 'idInstitucion_' + fila ;
			   	var idPersona = 'idPersona_' + fila ;
			   	var fechaAlta = 'fechaAlta_' + fila ;
			   	var validado = 'validado_' + fila ;
			   	idInstitucion = document.getElementById(idInstitucion).value;
			   	document.FormBajasTemporales.idInstitucion.value = idInstitucion;
			   	idPersona = document.getElementById(idPersona).value;
			   	fechaAlta = document.getElementById(fechaAlta).value;
			   	validado = document.getElementById(validado).value;
			   	if(validado==1 || validado=='')
	                datos = datos +"@@"+idPersona+"##"+fechaAlta;  
			
			}
			
		}
		if(datos==''){
			alert(msjSeleccionar);
			return false;
		}
		datos=datos.substring(2);
		document.FormBajasTemporales.target="submitArea";
		if(document.getElementById('idPersonaFichaColegial'))
			document.FormBajasTemporales.idPersona.value = document.getElementById('idPersonaFichaColegial').value;
		else
			document.FormBajasTemporales.idPersona.value = '';
		document.FormBajasTemporales.datosSeleccionados.value=datos;
		document.FormBajasTemporales.modo.value="denegarSolicitud";
		document.FormBajasTemporales.submit();
		document.FormBajasTemporales.target="mainWorkArea";
	}
	function accionValidarSolicitud(msjSeleccionar){
		var ele = document.getElementsByName('chkBajasTemporales');
		var datos='';
		for (fila = 0; fila < ele.length; fila++) {
		    if(ele[fila].checked){
				var idInstitucion = 'idInstitucion_' + fila ;
			   	var idPersona = 'idPersona_' + fila ;
			   	var fechaAlta = 'fechaAlta_' + fila ;
			   	var validado = 'validado_' + fila ;
			   	idInstitucion = document.getElementById(idInstitucion).value;
			   	document.FormBajasTemporales.idInstitucion.value = idInstitucion;
			   	idPersona = document.getElementById(idPersona).value;
			   	fechaAlta = document.getElementById(fechaAlta).value;
			   	validado = document.getElementById(validado).value;
                if(validado==0 || validado=='')
                	datos = datos +"@@"+idPersona+"##"+fechaAlta; 
			
			}
			
		}
		if(datos==''){
			alert(msjSeleccionar);
			return false;
		}
		datos=datos.substring(2);
		document.FormBajasTemporales.target="submitArea";
		if(document.getElementById('idPersonaFichaColegial'))
			document.FormBajasTemporales.idPersona.value = document.getElementById('idPersonaFichaColegial').value;
		else
			document.FormBajasTemporales.idPersona.value = '';
		document.FormBajasTemporales.datosSeleccionados.value=datos;
		document.FormBajasTemporales.modo.value="validarSolicitud";
		document.FormBajasTemporales.submit();
		document.FormBajasTemporales.target="mainWorkArea";
		
	}
	function borrarFila(fila)
	{
		
			var idInstitucion = 'idInstitucion_' + fila ;
			var idPersona = 'idPersona_' + fila ;
			var fechaAlta = 'fechaAlta_' + fila ;
			idInstitucion = document.getElementById(idInstitucion).value;
			idPersona = document.getElementById(idPersona).value;
			fechaAlta = document.getElementById(fechaAlta).value;
			document.FormBajasTemporales.idInstitucion.value = idInstitucion;
			document.FormBajasTemporales.idPersona.value = idPersona;
			document.FormBajasTemporales.fechaAlta.value = fechaAlta;
        	document.FormBajasTemporales.target="submitArea";
			document.FormBajasTemporales.modo.value="borrarSolicitud";
			document.FormBajasTemporales.submit();
			document.FormBajasTemporales.target="mainWorkArea";      
		
	 }
	 function editar(fila)
	{
		
			var idInstitucion = 'idInstitucion_' + fila ;
			var idPersona = 'idPersona_' + fila ;
			var fechaAlta = 'fechaAlta_' + fila ;
			var colegiadoNumero = 'colegiadoNumero_' + fila ;
			var colegiadoNombre = 'colegiadoNombre_' + fila ;
			idInstitucion = document.getElementById(idInstitucion).value;
			idPersona = document.getElementById(idPersona).value;
			fechaAlta = document.getElementById(fechaAlta).value;
			colegiadoNumero = document.getElementById(colegiadoNumero).value;
			colegiadoNombre = document.getElementById(colegiadoNombre).value;
			document.FormBajasTemporales.idInstitucion.value = idInstitucion;
			document.FormBajasTemporales.idPersona.value = idPersona;
			document.FormBajasTemporales.fechaAlta.value = fechaAlta;
			document.FormBajasTemporales.colegiadoNombre.value = colegiadoNombre;
			document.FormBajasTemporales.colegiadoNumero.value = colegiadoNumero;
			document.FormBajasTemporales.modo.value="editarSolicitud";
			var resultado = ventaModalGeneral(document.FormBajasTemporales.name,"P");
			if (resultado=='MODIFICADO') {
				//Si tiene esto es que estamos desde la busqeuda no desde la ficha colegial
				if(document.getElementById('idBusqueda')){
					refrescarLocal();
				}else{
					document.FormBajasTemporales.target="submitArea";
					document.FormBajasTemporales.modo.value="refrescar";
					document.FormBajasTemporales.submit();
					document.FormBajasTemporales.target="mainWorkArea";
				}

			
				
			}
        	
        	
        	
	 }
	 function consultar(fila)
	{
		
			var idInstitucion = 'idInstitucion_' + fila ;
			var idPersona = 'idPersona_' + fila ;
			var fechaAlta = 'fechaAlta_' + fila ;
			var colegiadoNumero = 'colegiadoNumero_' + fila ;
			var colegiadoNombre = 'colegiadoNombre_' + fila ;
			idInstitucion = document.getElementById(idInstitucion).value;
			idPersona = document.getElementById(idPersona).value;
			fechaAlta = document.getElementById(fechaAlta).value;
			colegiadoNumero = document.getElementById(colegiadoNumero).value;
			colegiadoNombre = document.getElementById(colegiadoNombre).value;
			document.FormBajasTemporales.idInstitucion.value = idInstitucion;
			document.FormBajasTemporales.idPersona.value = idPersona;
			document.FormBajasTemporales.fechaAlta.value = fechaAlta;
			document.FormBajasTemporales.colegiadoNombre.value = colegiadoNombre;
			document.FormBajasTemporales.colegiadoNumero.value = colegiadoNumero;
			document.FormBajasTemporales.modo.value="consultarSolicitud";
			var resultado = ventaModalGeneral(document.FormBajasTemporales.name,"P");
			if (resultado=='MODIFICADO') {
				//Si tiene esto es que estamos desde la busqeuda no desde la ficha colegial
				if(document.getElementById('idBusqueda')){
					refrescarLocal();
				}else{
					document.FormBajasTemporales.target="submitArea";
					document.FormBajasTemporales.modo.value="refrescar";
					document.FormBajasTemporales.submit();
					document.FormBajasTemporales.target="mainWorkArea";
				}

			
				
			}
        	
        	
        	
	 }
	 
	function marcarDesmarcarTodos(o) 
	{ 
		var ele = document.getElementsByName("chkBajasTemporales");
		for (i = 0; i < ele.length; i++) {
			if(ele[i].disabled == false)
				ele[i].checked = o.checked;
		}
	}