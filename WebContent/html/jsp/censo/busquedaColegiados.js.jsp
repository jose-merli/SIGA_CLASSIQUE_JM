<%@ taglib uri="libreria_SIGA.tld"	prefix="siga"%>
<%@ taglib uri="struts-bean.tld" 	prefix="bean"%>
	
	function refrescarLocal() {
	}
	function vueltaEnvio() {
	}
	function accionVolver() {
	}
	function accionCerrar() {
	}
	function buscar(){
		sub();
		var backupForm = document.createElement("input");
		backupForm.type = "hidden";
		backupForm.name = "backupForm";
		backupForm.value = "true";
		document.forms[0].appendChild(backupForm);
		document.forms[0].submit();
	}

	function buscarAvanzada() 
	{	
		sub();		
		document.forms[0].accion.value="abrirAvanzada";
		document.forms[0].submit();	
	}

	function buscarSimple() 
	{	
		sub();
		limpiarForm(document.forms[0]);
		document.forms[0].accion.value="inicio";
		document.forms[0].submit();	
	}
	
	function limpiar() 
	{		
		document.forms[0].busquedaExacta.checked=true;
		document.forms[0].nombre.value="";
		document.forms[0].apellidos1.value="";
		document.forms[0].apellidos2.value="";
		document.forms[0].nColegiado.value="";
		document.forms[0].nif.value="";
		document.forms[0].idInstitucion.selectedIndex=0;
		document.forms[0].accion.value="";
		document.forms[0].target="mainWorkArea";	
		document.forms[0].submit();	
	}
	
	function accionGenerarExcels(){
   		sub();
   		if (getSelected() != ""){
			document.getElementById("displ").accion.value = 'generaExcel';
			document.getElementById("displ").submit();
   		}
   		else{
   			alert ('<siga:Idioma key="general.message.seleccionar"/>');
   		}
		fin();
	}

	function lopd(fila) {
		alert('<siga:Idioma key="general.boton.lopd"/>');
	}


	/**
	 * Funcion generica de consulta de un registro
	 * @param formName Nombre del formulario  
	 * @param id PK del registro sobre el que se va a realizar la accion
	 */
  	function comunicar(idValue){
		sub();
		var accion = document.forms[0].accion.value;
		document.forms[0].accion.value="comunicar";
		document.forms[0].target="submitArea";
		document.forms[0].id.value=idValue;
		document.forms[0].submit();	
		document.forms[0].accion.value=accion;
		document.forms[0].target="";
	}
	/**
	 * Funcion generica de consulta de un registro
	 * @param formName Nombre del formulario  
	 * @param id PK del registro sobre el que se va a realizar la accion
	 */
  	function accionComunicar(){
		if (getSelected() == ""){
   			alert ('<siga:Idioma key="general.message.seleccionar"/>');
   			return;
   		}
		sub();
		var accion = document.forms[0].accion.value;
		document.forms[0].accion.value="comunicarVarios";
		document.forms[0].target="submitArea";
		var backupSelectedChild = document.createElement("input");
		backupSelectedChild.type='hidden';
		backupSelectedChild.name='backupSelected'; 
		backupSelectedChild.value="'"+getSelected()+"'";
		var selectAllChild = document.createElement("input");
		selectAllChild.type='hidden'; 
		selectAllChild.name='selectAll'; 
		selectAllChild.value="'"+document.forms[1].selectAll.value+"'";
		document.forms[0].appendChild(backupSelectedChild);
		document.forms[0].appendChild(selectAllChild);
		document.forms[0].submit();	
		document.forms[0].removeChild(backupSelectedChild);
		document.forms[0].removeChild(selectAllChild);
		document.forms[0].accion.value=accion;
		document.forms[0].target="";
	}
	
	function getSelected(){
		//recupera los seleccionados de otras paginas
		var seleccionados = document.getElementById("backupSelected").value;
		//recupera los datos de los registros seleccionados en la pagina actual
		var elements = document.getElementsByName("_chk");
		var seleccionados2 = "";
		for (i=0; i<elements.length; i++){
			if(elements[i].type == "checkbox" && elements[i].checked && seleccionados.indexOf(elements[i].value) == -1){
				seleccionados2 += "," + elements[i].value
			}
		}
		return seleccionados + seleccionados2;
	}
	