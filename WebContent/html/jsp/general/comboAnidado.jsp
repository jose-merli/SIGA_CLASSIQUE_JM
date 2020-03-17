<!DOCTYPE html>
<html>
<head>
<!-- comboAnidado.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>
<%@ taglib uri = "struts-html.tld" prefix="html"%>
<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<% 
	String app=request.getContextPath();

	String nombre = request.getParameter("nombre");
	
	String tipo = request.getParameter("tipo");

	// Si recibimos "tipoalt" sobreescribimos tipo con dicho valor
	String tipoalt=request.getParameter("tipoalt");
	if(tipoalt!=null) tipo=tipoalt;	

	String ancho = request.getParameter("ancho");
	String estilo = request.getParameter("estilo");
	String clase = request.getParameter("clase");
	String obligatorio = request.getParameter("obligatorio");

	// Solo para ArrayList
	ArrayList elementoSel=new ArrayList();
	String lista = request.getParameter("elementoSel");	

	lista=lista.replace('[',',');
	lista=lista.replace(']',',');		
	String[] vector = lista.split(",");
	// Empiezo por j=1
	for(int j=1; j<vector.length;j++){
		elementoSel.add(vector[j].trim());
	}


	String seleccionMultiple = request.getParameter("seleccionMultiple");
	String accion = request.getParameter("accion");
	String hijo = request.getParameter("hijo");
	int filasMostrar = 1;
	String sFilasMostrar = request.getParameter("filasMostrar");
	if (sFilasMostrar != null) {
		filasMostrar = Integer.parseInt(sFilasMostrar);
	}
	
	// String parametro[] = request.getParameter("parametro");
	String readonly = request.getParameter("readonly");
	String pestana = request.getParameter("pestana");

	String idProv = request.getParameter("id");

	String parametros = request.getParameter("parametros");

	if (parametros.length() > 0) {
		if (idProv == null) idProv = parametros;
		else	idProv += "," + parametros;
	}

	String datos[] = null;


	if (idProv!=null && !idProv.equals(""))
	{
		StringTokenizer st = new StringTokenizer(idProv, ",");

		datos = new String[st.countTokens()];
		int i=0;
		while (st.hasMoreElements())
		{
			String elem=st.nextToken();

			datos[i]=elem;
			i++;
		}
	}
	else
	{
		datos = new String[1];
		datos[0]="";
	}
	String paramIn = request.getParameter("parametrosIn");
	
	
	Object parametrosIn[] = null;
	
	if (paramIn!=null && !paramIn.equals(""))
	{
		StringTokenizer st = new StringTokenizer(paramIn, ";");

		parametrosIn = new Object[st.countTokens()];
		int i=0;
		int j=0;
		while (st.hasMoreElements())
		{
			Object elem=st.nextToken();
			StringTokenizer st2 = new StringTokenizer(elem.toString(), ",");
			String[] parIn = new String[st2.countTokens()];
			j = 0;
			while (st2.hasMoreElements())
			{	String parametro = st2.nextToken(); 
				parIn[j]=parametro;
				j++;
			
			}
			parametrosIn[i]= parIn;
			
			i++;
			
		}
	}
	else
	{
		parametrosIn = new Object[1];
		parametrosIn[0]="";
	}
	

	String nombreCombo = nombre+"Sel";

	String primero="";
	String segundo="";
	int ihijo=accion.toLowerCase().lastIndexOf("hijo:");
	if (ihijo>=0) {
	    int ipuntocoma = accion.indexOf(";",ihijo);
	    if (ipuntocoma>=0) {
	        primero=accion.substring(0,ipuntocoma+1);
	        segundo=accion.substring(ipuntocoma+1);
			accion=primero+";actualizaValoresSel('" + nombre + "',document.getElementById('"+nombreCombo+"'));"+segundo;
	    }
		else {
			accion += ";actualizaValoresSel('" + nombre + "',document.getElementById('"+nombreCombo+"'));"+segundo;
		}
	} else {
		accion = ";actualizaValoresSel('" + nombre + "',document.getElementById('"+nombreCombo+"'));"+accion;
	}

	
	String obligatorioSinTextoSeleccionar = request.getParameter("obligatorioSinTextoSeleccionar");
%>


<%@page import="java.util.StringTokenizer"%>
<%@page import="java.util.ArrayList"%>

	
	    <link id="default" rel="stylesheet" type="text/css" href="<html:rewrite page='${sessionScope.SKIN}'/>"/>
	
	
	<!-- Incluido jquery en siga.js -->
	
	<script type="text/javascript" src="<html:rewrite page='/html/js/SIGA.js?v=${sessionScope.VERSIONJS}'/>"></script>

		<script>
		
			function recargarComboActual()
			{
				if ("<%=readonly%>"!="true")
				{
					document.getElementById("<%=nombreCombo%>").onchange();
				}
			}


function actualizaValoresSel(nombre, sele) {

	if (parent.document) 
	{
		var j=0;
		var lista = "";
		var valores = sele.options; 
		
		if (valores[0]) 
		{
			for (j=0;j<valores.length;j++) 
			{
				var option = valores[j];
			
				if (option.selected) {
					if (lista=="") {
						lista+=option.value;
					} else {
						lista+=","+option.value;	
					}
				}
			}
		}
		var aux = parent.document.getElementById(nombre);
		if(aux)
			aux.value=lista;	
	}
}


		</script>
	</head>

	<body class="BodyIframe" onLoad="recargarComboActual();">
		<siga:ComboBD nombre = "<%=nombreCombo%>" 
					  tipo="<%=tipo%>" 
					  clase="<%=clase%>" 
					  estilo="<%=estilo%>" 
					  ancho="<%=ancho%>"
					  obligatorio="<%=obligatorio%>" 
					  elementoSel="<%=elementoSel%>" 
					  seleccionMultiple="<%=seleccionMultiple%>" 
					  accion="<%=accion%>" 
					  hijo="<%=hijo%>" 
					  filasMostrar="<%=filasMostrar%>" 
					  parametro="<%=datos%>" 
					  parametrosIn="<%=parametrosIn%>"
					  pestana="<%=pestana%>" 
					  readonly="<%=readonly%>" 
					  obligatorioSinTextoSeleccionar="<%=obligatorioSinTextoSeleccionar%>" />
	</body>
</html>