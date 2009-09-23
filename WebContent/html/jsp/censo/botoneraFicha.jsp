<!-- botoneraFicha.jsp -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<% String app=request.getContextPath(); %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<title>Botonera Gestión Censal</title>
<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>
<script src="<%=app%>/html/js/busquedaClientes.js" type="text/javascript"></script>
<script language="javascript">
var idCli='0';
var tipo='0';
function iniciar() {
	initStyles();
	MM_preloadImages('<%=app%>/html/imagenes/direcciones_ilum',
					'<%=app%>/html/imagenes/datosGen_ilum',
					'<%=app%>/html/imagenes/datosCV_ilum.gif',
					'<%=app%>/html/imagenes/bancos_ilum.gif',
					'<%=app%>/html/imagenes/historico_ilum.gif',
					'<%=app%>/html/imagenes/busqueda_ilum.gif',
					'<%=app%>/html/imagenes/caracterSocial_ilum.gif',
					'<%=app%>/html/imagenes/datosFact_ilum.gif',
					'<%=app%>/html/imagenes/datosExp_ilum.gif',
					'<%=app%>/html/imagenes/datosContactos_ilum.gif',
					'<%=app%>/html/imagenes/datosCursos_ilum.gif');
	
	//ejecutar accion por defecto:
	var cod=getParameterValue('ID');
	if(cod!='') {
		idCli=cod;
	}
	var t=getParameterValue('tipo');
	if(t!='') {
		tipo=t;
	}
	if(top.profile=='COL') {
		tdBusqueda.style.display='none';
	}
	
	if(clientes[idCli][6]!='0') {  //Ocultar algunos botones si el cliente no es colegiado!
		tdDatosCV.style.display='none';
		tdTurno.style.display='none';
	} else {
		tdCompo.style.display='none';
	}
	
	var accion = '../ficha/consultaFicha.html?ID='+idCli;
	activateLink(lnkDatos);
	parent.frames['CuerpoCentralGestion'].location=accion;	
}

function enviar(objLink) {
	var dir = objLink.action;
	activateLink(objLink);
	parent.frames['CuerpoCentralGestion'].location=dir+'?ID='+idCli+'&tipo='+tipo;
	return false;
}

function activar(sIdLink) {
	activateLink(eval(sIdLink));
}
</script>
</head>
<body onLoad="iniciar();">
<table border="0" cellspacing="0" cellpadding="0" width="100%" >
    <tr> <!-- Esta linea debe ser copiada en todas las botoneras del prototipo -->
      <td width="100%" height="21" align="right" valign="bottom" nowrap>        
      <table border="0" cellspacing="0" cellpadding="0" align="right" height="21" width="10%">
        <tr align="right" valign="bottom">
		  <td id="tdDatos" width="83" height="21" nowrap><a href="javascript://" id="lnkDatos" action="../ficha/consultaFicha.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('datos','','<%=app%>/html/imagenes/datosGen_ilum.gif',1)"><img name="datos" border="0" src="<%=app%>/html/imagenes/datosGen.gif" width="83" height="21" alt="Datos Generales"></a></td>
		  <td id="tdDirecciones" width="83" height="21" nowrap><a href="javascript://" id="lnkDirecciones" action="../ficha/consultaDirecciones.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('direcciones','','<%=app%>/html/imagenes/direcciones_ilum.gif',1)"><img name="direcciones" border="0" src="<%=app%>/html/imagenes/direcciones.gif" width="83" height="21" alt="Direcciones"></a></td>
		  <td id="tdBancos" width="83" height="21" nowrap><a href="javascript://" id="lnkBancos" action="../ficha/consultaBancos.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('bancos','','<%=app%>/html/imagenes/bancos_ilum.gif',1)"><img name="bancos" border="0" src="<%=app%>/html/imagenes/bancos.gif" width="83" height="21" alt="Datos Bancarios"></a></td>
		  <td id="tdDatosCV" width="83" height="21" nowrap><a href="javascript://" id="lnkDatosCV" action="../ficha/consultaDatosCV.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('datosCV','','<%=app%>/html/imagenes/datosCV_ilum.gif',1)"><img name="datosCV" border="0" src="<%=app%>/html/imagenes/datosCV.gif" width="83" height="21" alt="Datos Curriculares"></a></td>
		  <td id="tdCompo" width="113" height="21" nowrap><a href="javascript://" id="lnkCompo" action="../../common/notImplementedYet.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('componentes','','<%=app%>/html/imagenes/componentes_ilum.gif',1)"><img name="componentes" border="0" src="<%=app%>/html/imagenes/componentes.gif" width="113" height="21" alt="Componentes de Personas Jurídicas"></a></td>
		  <td id="tdTurno" width="83" height="21" nowrap><a href="javascript://" id="lnkTurno" action="../../justiciaGratuita/letradosTurnos/framesJustiCaracterSocial.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('caracterSocial','','<%=app%>/html/imagenes/caracterSocial_ilum.gif',1)"><img name="caracterSocial" border="0" src="<%=app%>/html/imagenes/caracterSocial.gif" width="150" height="21" alt="Servicio Juridico de caracter social"></a></td>
<!--		  <td id="tdFact" width="83" height="21" nowrap><a href="javascript://" id="lnkFact" action="../../facturacion/commonFactura/botoneraGestionFicha.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('facturacion','','<%=app%>/html/imagenes/datosFact_ilum.gif',1)"><img name="facturacion" border="0" src="<%=app%>/html/imagenes/datosFact.gif" width="83" height="21" alt="Datos de Facturacion"></a></td>-->
		  <td id="tdFact" width="83" height="21" nowrap><a href="javascript://" id="lnkFact" action="../ficha/resultadoFactura.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('facturacion','','<%=app%>/html/imagenes/datosFact_ilum.gif',1)"><img name="facturacion" border="0" src="<%=app%>/html/imagenes/datosFact.gif" width="83" height="21" alt="Datos de Facturacion"></a></td>
		  <td id="tdExp" width="83" height="21" nowrap><a href="javascript://" id="lnkExp" action="../ficha/consultaExpedientes.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('expedientes','','<%=app%>/html/imagenes/datosExp_ilum.gif',1)"><img name="expedientes" border="0" src="<%=app%>/html/imagenes/datosExp.gif" width="83" height="21" alt="Datos de Expedientes"></a></td>
		  <td id="tdContactos" width="83" height="21" nowrap><a href="javascript://" id="lnkContactos" action="../../common/notImplementedYet.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('contactos','','<%=app%>/html/imagenes/datosContactos_ilum.gif',1)"><img name="contactos" border="0" src="<%=app%>/html/imagenes/datosContactos.gif" width="83" height="21" alt="Datos de Contactos"></a></td>
		  <td id="tdCursos" width="83" height="21" nowrap><a href="javascript://" id="lnkCursos" action="../../common/notImplementedYet.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('cursos','','<%=app%>/html/imagenes/datosCursos_ilum.gif',1)"><img name="cursos" border="0" src="<%=app%>/html/imagenes/datosCursos.gif" width="83" height="21" alt="Datos de Cursos"></a></td>
		  <td id="tdHistorico" width="83" height="21" nowrap><a href="javascript://" id="lnkHistorico" action="../ficha/consultaHistorico.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('historico','','<%=app%>/html/imagenes/historico_ilum.gif',1)"><img name="historico" border="0" src="<%=app%>/html/imagenes/historico.gif" width="83" height="21" alt="Historico de Modificaciones"></a></td>
		  <td id="tdBusqueda" width="83" height="21" nowrap><a href="javascript://" id="lnkBusqueda" action="../busquedaClientes.html" onClick="enviar(this);return false;" target="CuerpoCentralGestion" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('buscar','','<%=app%>/html/imagenes/busqueda_ilum.gif',1)"><img name="buscar" border="0" src="<%=app%>/html/imagenes/busqueda.gif" width="83" height="21" alt="Busqueda de Clientes"></a></td>
        </tr>
      </table>
      </td>
    </tr>
    <tr> 
      <td colspan="2" width="100%" height="10" class="rayita"></td>      
    </tr>
  </table>
</body>
</html>
