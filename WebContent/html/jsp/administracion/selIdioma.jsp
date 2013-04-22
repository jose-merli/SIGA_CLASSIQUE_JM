<!-- selIdioma.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp"%>
<%@ taglib uri="libreria_SIGA.tld" prefix="siga"%>
<%@ taglib uri = "struts-bean.tld" prefix="bean"%>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "struts-logic.tld" prefix="logic"%>

<%@ page import="org.apache.struts.Globals"%>
<%@ page import="com.atos.utils.UsrBean"%>
<%@ page import="com.siga.beans.*"%>
<%@ page import="com.siga.administracion.SIGAConstants"%>
<%@ page import="java.util.Hashtable" %>
<%@ page import="java.util.Vector" %>
<html>
<head>
<%
	String app=request.getContextPath();
	HttpSession ses=request.getSession();
	java.util.Locale currentLocale =(java.util.Locale) ses.getAttribute(Globals.LOCALE_KEY);
	// antes, ahora se coge de la institucion
	// String leng=currentLocale.getLanguage();

	String leng=currentLocale.getLanguage();
	UsrBean usr=(UsrBean)request.getSession().getAttribute("USRBEAN");
	CenInstitucionAdm admIns = new CenInstitucionAdm(usr);
	Hashtable ht = new Hashtable();
	String idInstitucion[] = {usr.getLocation()};
	ht.put(CenInstitucionBean.C_IDINSTITUCION,usr.getLocation());
	Vector v1 = admIns.selectByPK(ht);
	if (v1!=null && v1.size()>0) {
		CenInstitucionBean b = (CenInstitucionBean) v1.get(0);
		AdmLenguajesAdm admLen = new AdmLenguajesAdm(usr);
		Hashtable ht2 = new Hashtable();
		ht2.put(AdmLenguajesBean.C_IDLENGUAJE,b.getIdLenguaje());
		Vector v2 = admLen.selectByPK(ht2);
		if (v2!=null && v2.size()>0) {
			AdmLenguajesBean b2 = (AdmLenguajesBean) v2.get(0);
			leng = b2.getCodigoExt().toLowerCase();
		}
	}
	AdmLenguajesAdm a = new AdmLenguajesAdm(usr);
	Vector v = a.select();

	CenInstitucionLenguajesAdm cilAdm = new CenInstitucionLenguajesAdm (usr);
	Vector vLenXInstitucion = cilAdm.select(ht);	
	
	boolean visibleCombo = false;
	ArrayList idiomaSel = new ArrayList();
	if (usr.getIdPersona() == -1){
		idiomaSel.add((String) usr.getLanguage());
		visibleCombo = true;
	}
		
	
	
	// RGG 26/02/2007 cambio para que esta ventana sea dinámica. Obtiene todos los lenguajes de la base de datos. 
	// Las imagenes de los lenguajes los obtiene del path de imagenes, llamadas lenguaje_codext.gif
	
	// RGG 27/02/2007 Cambio para que el idioma en lugar de cambiarse en la sesion se cambie en la institucion
	
%>

	<link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp"/>
	
		
	
	<script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.js"></script><script type="text/javascript" src="<%=app%>/html/js/jquery.custom.js"></script>

<siga:Titulo titulo="cambiarIdioma.title" localizacion="menu.administracion"/>
		
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script language="JavaScript">

	<!-- Asociada al boton Guardar -->
	function accionGuardar() 
	{		
		if(confirm('<siga:Idioma key="messages.confirm.updateData"/>')){
			document.getElementById("idiomaUsuario").value = document.getElementById("idioma").value;
			document.all.form1.submit();
		}
		
		return false;
	}

	<!-- Asociada al boton Restablecer -->
	function accionRestablecer() 
	{		
		if(confirm('<siga:Idioma key="messages.confirm.cancel"/>')) {
		document.location=document.location;
		}
	}
	
</script>
</head>
<body>
<div id="camposRegistro" class="posicionBusquedaSolo">

	<br><br><br><br><br><br>
<form method="POST" name="form1" action="<%=app%>/cambiarLenguaje.do" target="submitArea">
<input type="hidden" name="idiomaUsuario" id="idiomaUsuario"  value=""  />
<table width="20%" border="0" align="center" cellpadding="0" cellspacing="0">
<%   for (int i=0;i<v.size();i++) {
		AdmLenguajesBean b = (AdmLenguajesBean)v.get(i);


		boolean bVisto = false;
		String deshabilitarOpcion="disabled";

		for (int jj = 0; vLenXInstitucion!=null && jj < vLenXInstitucion.size(); jj++) {
			CenInstitucionLenguajesBean bb = (CenInstitucionLenguajesBean) vLenXInstitucion.get(jj);
			if (b.getIdLenguaje().equals(bb.getIdLenguaje()) ) {
				
				bVisto = true;
				deshabilitarOpcion="";
				break;
			}
		}

%>

	  	<tr>
			<td class="labelText" width="10%">
				<input type="radio" name="opt" <%=deshabilitarOpcion%> value="<%=b.getIdLenguaje()%>" <%=(leng.equalsIgnoreCase(b.getCodigoExt().toLowerCase()))?"checked":""%>>
			</td>
			<td class="labelText" width="35%"><img src="<%=app%>/html/imagenes/lenguaje_<%=b.getCodigoExt().toLowerCase().trim() %>.gif"></td>
			<td class="labelText" width="50%">
				<%=b.getDescripcion()  %>
			</td>
			<td class="labelText" width="5%">
				<%if (bVisto) { %>
					<img src="<%=app%>/html/imagenes/visto.gif">
				<% } %>
			</td>
		</tr>

<%  
	}
%>

	</table>

	<br><br>

	<table align="center" > 
		<tr>
			<td><img src="<%=app%>/html/imagenes/visto.gif"></td>
			<td class="labelText"><siga:Idioma key="administracion.seleccionarIdioma.idiomaDisponible.literal"/></td>
		</tr>
	</table>
	<br><br><br><br><br><br><br><br><br><br><br>
	<% if (visibleCombo) {%>
	<table align="center" > 
		<tr>
			<!-- IDIOMA -->
			<td class="labelText">
				<siga:Idioma key="administracion.cambioIdioma.literal.idiomaUsuario"/>&nbsp;(*)
			</td>
			<td>
				<siga:ComboBD nombre = "idioma" tipo="cmbIdiomaInstitucion" clase="box" obligatorio="true" elementoSel="<%=idiomaSel %>" obligatorioSinTextoSeleccionar="true" parametro="<%=idInstitucion%>"/>
			</td>
		</tr>
	</table>
	<%}%>



</form>

<siga:ConjBotonesAccion botones="R,G" clase="botonesDetalle"  />
</div>
<iframe name="submitArea" src="<%=app%>/html/jsp/general/blank.jsp" style="display:none"></iframe> 

</body>
</html>
