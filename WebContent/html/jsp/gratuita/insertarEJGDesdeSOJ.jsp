<!-- insertarEJGDesdeSOJ.jsp -->
<!-- CABECERA JSP -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<!-- TAGLIBS -->
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld"   prefix="bean"%>
<%@ taglib uri = "struts-html.tld"   prefix="html"%>
<%@ taglib uri = "struts-logic.tld"  prefix="logic"%>

<!-- IMPORTS -->
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="com.siga.gui.processTree.SIGAPTConstants"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.administracion.SIGAMasterTable"%>
<%@ page import="com.siga.Utilidades.UtilidadesBDAdm"%>


<!-- JSP -->
<%	String app=request.getContextPath(); 
	HttpSession ses=request.getSession(true);
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	String dato[] = {(String)usr.getLocation()};	
	Hashtable miHash = (Hashtable)request.getAttribute("DATABACKUP");
	String anioSOJ = "", numeroSOJ = "", idTipoSOJ = "", idPersonaJG = "", nColegiado="", nomColegiado="";
	ArrayList idTurno = new ArrayList();
	ArrayList idGuardia = new ArrayList();
	
	
	
	try {
		
		nColegiado =  request.getAttribute("nColegiado")==null?"":(String)request.getAttribute("nColegiado");
		nomColegiado=(String)request.getAttribute("nombreColegiado");
		anioSOJ = (String)miHash.get("ANIO");
		numeroSOJ = (String)miHash.get("NUMERO");
		idTipoSOJ = (String)miHash.get("IDTIPOSOJ");
		if (miHash.get("IDPERSONAJG")!=null){
			idPersonaJG = (String)miHash.get("IDPERSONAJG");
		}	
		if (miHash.get("IDTURNO")!=null ){
		 idTurno.add((usr.getLocation()+","+miHash.get("IDTURNO").toString()));
		
		}
		if (miHash.get("IDGUARDIA")!=null ){
		  idGuardia.add(miHash.get("IDGUARDIA").toString());
		  
		}
			
		
		
	} catch (Exception e){}
	
	String fecha = UtilidadesBDAdm.getFechaBD("");
	String datoGuardia[] = {(String)usr.getLocation(),(String)miHash.get("IDTURNO")};	
	
	java.util.ResourceBundle rp=java.util.ResourceBundle.getBundle("SIGA");
	String idordinario = rp.getString("codigo.general.scstipoejg.ordinarios");
	String datoTipoOrdinario[]={idordinario,idordinario};	
	
%>

<html>
<!-- HEAD -->
<head>
	<html:javascript formName="DefinirEJGForm" staticJavascript="false" />  
  	<script src="<%=app%>/html/js/validacionStruts.js" type="text/javascript"></script>
	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>	
	<script src="<%=app%>/html/js/calendarJs.jsp" type="text/javascript"></script>	
	<script type="text/javascript">
		function actualizarFecha(){
		  document.forms[0].fechaAperturaEJG.value=DefinirEJGForm.fechaApertura.value;
		}
	</script>
</head>

<body>

	<!-- INICIO: TITULO OPCIONAL DE LA TABLA -->
	<table class="tablaTitulo" align="center" cellspacing="0" >
	<tr>
	<td class="titulitosDatos">
		<siga:Idioma key="gratuita.busquedaEJG.literal.EJG"/>
	</td>
	</tr>
	</table>
	

	<!-- INICIO: CAMPOS DEL REGISTRO -->

	<!-- Comienzo del formulario con los campos -->
	<html:form action = "/JGR_EJG" method="POST" target="submitArea">

	<html:hidden property = "actionModal" value = ""/>
	<html:hidden property = "modo" value = "Insertar"/>
	<html:hidden property = "guardiaTurnoIdTurno" value = ""/>
	<html:hidden property = "guardiaTurnoIdGuardia" value = ""/>	
	<html:hidden property = "anio" value = ""/>
	<html:hidden property = "numero" value = ""/>
	<html:hidden property = "idPersona" value = ""/>
	<html:hidden property = "idPersonaJG" value = "<%=idPersonaJG%>"/>
	<html:hidden property = "SOJNumero" value = "<%=numeroSOJ%>"/>
	<html:hidden property = "SOJAnio" value = "<%=anioSOJ%>"/>
	<html:hidden property = "SOJIdTipoSOJ" value = "<%=idTipoSOJ%>"/>
	<html:hidden property = "origenApertura" value = "M"/>
	<html:hidden property = "tipoLetrado" value = "M"/>	
	<html:hidden property = "idInstitucion" value ="<%=usr.getLocation()%>"/>
	<html:hidden property = "usuMod" value = "<%=usr.getUserName()%>"/>
	<html:hidden property = "fechaAperturaEJG" value = "sysdate"/>	
	<html:hidden property = "flagSalto" value=""/>
	<html:hidden property = "flagCompensacion" value=""/>
	
	<table class="tablaCentralCamposMedia" align="center">	
	<tr>		
	<td>			
	<!-- SUBCONJUNTO DE DATOS -->
	<siga:ConjCampos leyenda="gratuita.busquedaEJG.literal.expedientesEJG">
	<table class="tablaCampos" align="center"  border="0">
	
	
	
	<tr>		
	<td class="labelText" style="width:150px;">
		<siga:Idioma key="gratuita.busquedaEJG.literal.tipoEJG"/>&nbsp;(*)
	</td>
	<td >
		<siga:ComboBD nombre="idTipoEJG" tipo="tipoEJG"  parametro="<%=datoTipoOrdinario%>" clase="boxCombo" obligatorio="false" obligatorioSinTextoSeleccionar="true"/>
	</td>	
	</tr>
		
	<tr>
	<td class="labelText" style="width:150px;">
		<siga:Idioma key="gratuita.busquedaEJG.literal.fechaApertura"/>&nbsp;(*)
	</td>
	<td >		
		<html:text name="DefinirEJGForm" property="fechaApertura" size="10" maxlength="10" styleClass="box"  value="<%=fecha%>" readOnly="true"></html:text>
		<a onClick="showCalendarGeneral(fechaApertura);actualizarFecha();rellenarComboGuardia();" onMouseOut="MM_swapImgRestore();" onMouseOver="MM_swapImage('Calendario','','<%=app%>/html/imagenes/calendar_hi.gif',1);">
		<img src="<%=app%>/html/imagenes/calendar.gif" 
		alt="<siga:Idioma key="gratuita.listadoCalendario.literal.seleccionarFecha"/>"  
		border="0" valign="bottom">
		</a>
	</td>
	</tr>
		
	<tr>
<!--	
	<td class="labelText">
		<siga:Idioma key="gratuita.insertarSOJ.literal.letradoTramitador"/>
	</td>	
	<td class="labelText">
		<% if (!usr.isLetrado() ) {%>
			<html:text name="DefinirEJGForm" property="NColegiado" size="10" maxlength="10" styleClass="box" value="<%=nColegiado%>"></html:text>
		<% } else { %>
			<html:text name="DefinirEJGForm" property="NColegiado" size="10" maxlength="10" styleClass="boxConsulta" value="<%=nColegiado%>" readOnly="true"></html:text>			
		<% } %>
	</td>
-->	
	<html:hidden name="DefinirEJGForm" property="NColegiado" value="<%=nColegiado%>"></html:hidden>
	
<!--<td class="labelText" colspan="2">
		<siga:Idioma key="gratuita.insertarSOJ.literal.demandante"/>
		<input type="radio" name="calidad" value="D" checked>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<siga:Idioma key="gratuita.insertarSOJ.literal.demandado"/>
		<input type="radio" name="calidad" value="O">
	</td> -->
<!-- RGG: Se quita mientras se arregla lo de la documentación SOJ/EJG
	 <td class="labelText" colspan=2>
		  <siga:Idioma key="gratuita.insertarSOJ.literal.documentacionSOJ"/>
		  <input type="checkbox" name="chkDocumentacion"  checked>
	</td>
-->	
	</tr>
	
	
	
	</table>
	</siga:ConjCampos>	
	</td>
	</tr>
	<tr><td>
	<siga:ConjCampos leyenda="gratuita.seleccionColegiadoJG.literal.tituloEJG"> 
		<table class="tablaCampos" border="0" width="100%">

			<tr>
				<td class="labelText">
					<siga:Idioma key="gratuita.busquedaEJG.literal.turno"/>&nbsp;
				</td>
				<td class="labelText" colspan="4">
					<siga:ComboBD nombre = "identificador" tipo="turnos" clase="boxCombo" obligatorio="false" accion="Hijo:identificador2" ancho="550" parametro="<%=dato%>" elementoSel="<%=idTurno%>"/>
				</td>
			</tr>
			<tr>
				<td class="labelText">	
					<siga:Idioma key="gratuita.busquedaEJG.literal.guardia"/>&nbsp;
				</td>
				<td class="labelText" colspan="4">
				<%if (miHash.get("IDTURNO")!=null ){%>
					<siga:ComboBD nombre = "identificador2" tipo="guardias" clase="boxCombo"  accion="parent.rellenarComboGuardia();" parametro="<%=datoGuardia%>" obligatorio="false" hijo="t" elementoSel="<%=idGuardia%>"/>
				<%}else{%>	
				   <siga:ComboBD nombre = "identificador2" tipo="guardias" clase="boxCombo"  accion="parent.rellenarComboGuardia();" obligatorio="false" hijo="t" elementoSel="<%=idGuardia%>"/>
				<%}%>
				</td>
			</tr>	

			<tr>
				<td colspan="5">
					<siga:BusquedaSJCS nombre="DefinirEJGForm" propiedad="buscaLetrado"
		 				   concepto="EJG" operacion="Asignacion" 
						   campoTurno="identificador" campoGuardia="identificador2" campoFecha="fechaApertura"
						   campoPersona="idPersona" campoColegiado="numeroColegiado" campoNombreColegiado="nomColegiado"  
						   campoFlagSalto="flagSalto" campoFlagCompensacion="flagCompensacion" campoSalto="checkSalto" campoCompensacion="compensacion"
						   diaGuardia="true"
						   modo="nuevo"
    					   />				
				</td>
			</tr>
			<tr>
				<td class="labelText">
					<siga:Idioma key='gratuita.busquedaEJG.literal.numeroColegidado'/>
				</td>		
				<td>
					<input type="text" name="numeroColegiado" class="boxConsulta" readOnly value="<%=nColegiado%>" style="width:'100px';">
				</td>
				<td class="labelText">
					<siga:Idioma key='FactSJCS.listadoRetencionesJ.literal.nombreColegiado'/>
				</td>
				<td colspan="2">
					<input type="text" name="nomColegiado" class="boxConsulta" readOnly value="<%=nomColegiado%>" style="width:'240px';">
				</td>			
			</tr>	
		</table>

	</siga:ConjCampos> 
	</td></tr>
	
	<!-- FIN: CAMPOS DEL REGISTRO -->
	</table>
	<!-- ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	<!-- INICIO: BOTONES REGISTRO -->
		<siga:ConjBotonesAccion botones="Y,R,C" modal="M"  />
	<!-- FIN: BOTONES REGISTRO -->
	</html:form>



	
	<!-- INICIO: SCRIPTS BOTONES -->
	<script language="JavaScript">	
	
		<!-- Asociada al boton Restablecer -->
		function accionRestablecer() {		
			document.forms[0].reset();
		}
		
		<!-- Asociada al boton Guardar y Cerrar -->
		function accionGuardarCerrar() {	
			sub();
			/* El identificador está compuesto por [idinstitucion,idturno] por tanto hay que dividirlo y quedarnos sólo
			con el turno, ya que la institución se recogerá del formulario. Viene ha sido debido a que es necesario para
			el combo hijo de guardias*/
			var id = document.forms[0].identificador.value;
			var posicion = 0;
			/* Se recorre hasta encontrar el separador, que es ","*/									
			posicion = id.indexOf(',') + 1;
			/* El substring que queda a partir de ahí es el identificador del turno, que almacenamos en el formulario */			
			document.forms[0].guardiaTurnoIdTurno.value = id.substring(posicion);
			document.forms[0].guardiaTurnoIdGuardia.value = document.forms[0].identificador2.value;
			if (validateDefinirEJGForm(document.forms[0])){
//				window.returnValue="MODIFICADO";
               document.DefinirEJGForm.NColegiado.value=document.DefinirEJGForm.numeroColegiado.value;
               
				document.forms[0].submit();
			}else{
				fin();
				return false;
				
			}
		}
		
		<!-- Asociada al boton Cerrar -->
		function accionCerrar()	{
			top.cierraConParametros("NORMAL");			
		}
		
	</script>
	<!-- FIN: SCRIPTS BOTONES -->

	<!-- FIN ******* BOTONES DE ACCIONES EN REGISTRO ****** -->
	
	
<!-- INICIO: SUBMIT AREA -->
	<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe>
<!-- FIN: SUBMIT AREA -->


	</body>

</html>
