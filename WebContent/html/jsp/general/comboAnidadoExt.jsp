<!-- comboAnidado.jsp -->
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="no-cache"> <%@ page pageEncoding="ISO-8859-1"%>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ page contentType="text/html" language="java" errorPage="/html/jsp/error/errorSIGA.jsp" %>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.StringTokenizer"%>

<%@ taglib uri = "libreria_SIGA.tld" prefix="siga"%>

<% 
	String app=request.getContextPath();

	String nombre = request.getParameter("nombre");
	String tipo = request.getParameter("tipo");

	// Si recibimos "tipoalt" sobreescribimos tipo con dicho valor
	String tipoalt=request.getParameter("tipoalt");
	if(tipoalt!=null) tipo=tipoalt;	

	String estilo = request.getParameter("estilo");
	String clase = request.getParameter("clase");
	String obligatorio = request.getParameter("obligatorio");

	// Solo para ArrayList
	ArrayList elementoSel=new ArrayList();
	String lista = request.getParameter("elementoSel");	
	lista=lista.replace('[','-');
	lista=lista.replace(']','-');		
	String[] vector = lista.split("-");
	// Empiezo por j=1
	for(int j=1; j<vector.length;j++){
		elementoSel.add(vector[j]);
	}

	//EN OBSERVACION
	//ArrayList elementoSel = new ArrayList();
	//cambio ArrayList (elementoSel multiple)		
	//elementoSel.add(request.getParameter("elementoSel"));		
	//int elementoSel = Integer.parseInt(request.getParameter("elementoSel"));

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

	String nombreCombo = nombre+"Sel";
	//accion += ";parent.document.getElementById('" + nombre + "').value = " + nombreCombo + ".value;";
	
	// RGG 02/02/2006 cambio para utilizar combos seleccion multiple en un segundo nivel de combos
	accion += ";actualizaValoresSel('" + nombre + "',document.getElementById('"+nombreCombo+"'));";
	
	//;parent.document.getElementById('" + nombre + "').value = " + nombreCombo + ".value;

	//accion += ";parent.document.getElementById('provincia').value = '01';";
%>


<html>
	<head>
	    <link id="default" rel="stylesheet" type="text/css" href="<%=app%>/html/jsp/general/stylesheet.jsp">
	    
	    <script src="<%=app%>/html/js/SIGA.js" type="text/javascript"></script>

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
		aux.value=lista;				
	}
}

/*
function actualizaValoresSel(nombre, sele) {

	if (parent.document) {
		var formu = parent.document.forms[0];
		var elementos = formu.elements;
		var i=0;
		var j=0;
		for (i=0;i<elementos.length;i++) 
		{
			if (elementos[i].name==nombre) {
				var lista = "";
				var valores = sele.options; 
				if (valores[0]) {
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
				elementos[i].value=lista;
			}
		}
	}
}
*/

/*
function actualizaValoresSel(nombre, valores) {


if (!valores[0]) {
	alert("v="+valores);
}

	if (parent.document) {
		var formu = parent.document.forms[0];
		var elementos = formu.elements;
		var i=0;
		var j=0;
		for (i=0;i<elementos.length;i++) 
		{
			if (elementos[i].name==nombre) {
				if (valores[0]) {
					for (j=0;j<valores.length;j++) 
					{
						var valor = valores[j];
						if (j==0) {
							elementos[i].value=valor;
							alert("dado valor ... "+elementos[i].name + "="+elementos[i].value);
						} else  {
							var el = document.createElement('input');
							el.setAttribute('type', 'hidden');
							el.setAttribute('name', nombre);
							el.setAttribute('value', valor);
							formu.appendChild(el);
							alert("creado "+el.name + "=" + el.value);
						}
					}
				} else {
					elementos[i].value=valores;
				}
			}
		}
	}

}
*/
		</script>
	</head>

	<body class="BodyIframe" onLoad="recargarComboActual()">
		<siga:ComboBD nombre = "<%=nombreCombo%>" 
					  tipo="<%=tipo%>" 
					  clase="<%=clase%>" 
					  estilo="<%=estilo%>" 
					  obligatorio="<%=obligatorio%>" 
					  elementoSel="<%=elementoSel%>" 
					  seleccionMultiple="<%=seleccionMultiple%>" 
					  accion="<%=accion%>" 
					  hijo="<%=hijo%>" 
					  filasMostrar="<%=filasMostrar%>" 
					  parametro="<%=datos%>" 
					  pestana="<%=pestana%>" 
					  readonly="<%=readonly%>" />
	</body>
</html>