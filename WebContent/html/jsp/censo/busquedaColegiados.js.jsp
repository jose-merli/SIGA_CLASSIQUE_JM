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
		document.forms[0].appendChild(document.createElement("<input type='hidden' name='backupForm' value='true'>"));
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
   		if (existsSelected("displ")){
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
		if (!existsSelected("displ")){
   			alert ('<siga:Idioma key="general.message.seleccionar"/>');
   			return;
   		}
		sub();
		var accion = document.forms[0].accion.value;
		document.forms[0].accion.value="comunicarVarios";
		document.forms[0].target="submitArea";
		var backupSelectedChild = document.createElement("<input type='hidden' name='backupSelected' value='"+getSelected()+"'>");
		var selectAllChild = document.createElement("<input type='hidden' name='selectAll' value='"+document.forms[1].selectAll.value+"'>");
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
			if(elements[i].type == "checkbox" && elements[i].checked){
				seleccionados2 += "," + elements[i].value
			}
		}
		return seleccionados + seleccionados2;
	}
	
/*
  	function comunicar(idValue)
	{
  		var idInstitucion = "<bean:write scope='session' name='USRBEAN' property='location'/>";	
		var id= idValue.split('@@@');
	   	var datos = "idPersona=="+id[0]+ "##idInstitucion==" +id[1] ; 
	   	
		var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
		formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='"+idInstitucion+"'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='CENSO'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='1'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='1'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='tipoPersonas' value='2'>"));
		document.appendChild(formu);
		formu.datosInforme.value=datos;
		formu.submit();
	}
	
	function accionComunicar(){
  		if (!existsSelected("displ")){
   			alert ('<siga:Idioma key="general.message.seleccionar"/>');
   			return;
   		}
  		var idInstitucion = "<bean:write scope='session' name='USRBEAN' property='location'/>";
  		sub();  		

  		var datos="";
  		var cont=0;
  		//recupera los datos de los registros seleccionados de otras paginas
		var seleccionados = document.getElementById("backupSelected").value;
		if (seleccionados != ""){
			var lista = seleccionados.split(',');
	  		for (i = 0; i < lista.length; i++) {
		  		var lista2 = lista[0].split('@@@');
	 		   	datos = datos +"idPersona=="+lista2[0] + "##idInstitucion==" +lista2[1] + "%%%";
	 		   	cont++;
			}
		}
		//recupera los datos de los registros seleccionados en la pagina actual
		var elements = document.getElementsByName("_chk");
		for (i=0; i<elements.length; i++){
			if(elements[i].type == "checkbox" && elements[i].checked){
				var lista = elements[i].value.split('@@@');
	 		   	datos = datos +"idPersona=="+lista[0] + "##idInstitucion==" +lista[1] + "%%%";
	 		   	cont++;
			}
		}
		var formu=document.createElement("<form name='InformesGenericosForm'  method='POST'  action='/SIGA/INF_InformesGenericos.do' target='submitArea'>");
		formu.appendChild(document.createElement("<input type='hidden' name='idInstitucion' value='"+idInstitucion+"'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='idTipoInforme' value='CENSO'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='datosInforme' value=''>"));
		formu.appendChild(document.createElement("<input type='hidden' name='seleccionados' value='0'>"));
		formu.appendChild(document.createElement("<input type='hidden' name='enviar' value='1'>"));
		if(cont>50){
			formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='0'>"));
		}
		else{
			formu.appendChild(document.createElement("<input type='hidden' name='descargar' value='1'>"));
		}
		formu.appendChild(document.createElement("<input type='hidden' name='tipoPersonas' value='2'>"));
		
		document.appendChild(formu);
		formu.datosInforme.value=datos;
		formu.submit();

   		fin();
	}
*/
