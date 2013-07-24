<!DOCTYPE html>
<html>
<head>
<!-- exitoInsercion.jsp -->
<%@ taglib uri = "struts-html.tld" prefix="html"%>

<TITLE>Paneles</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<script language=javascript src="/lib/validar.js">
</script>
<script language=javascript>
  var tblCamposSalida='', tblCamposSalidaReal='', tblPrioritaria='';
  function itSelf() {
    document.FormPaneles.operacion.value="iniciaryeditar";
    document.FormPaneles.submit();
  }


  function crearAllChorros() {
    document.FormPaneles.chorropFieldSelectV.value=crearChorro(document.FormPaneles.pFieldSelect, 0);
    document.FormPaneles.chorropFieldSelectT.value=crearChorro(document.FormPaneles.pFieldSelect, 1);
    document.FormPaneles.chorropFieldJoinV.value=crearChorro(document.FormPaneles.pFieldJoin, 0);
    document.FormPaneles.chorropFieldJoinT.value=crearChorro(document.FormPaneles.pFieldJoin, 1);
    document.FormPaneles.chorropFieldOrderV.value=crearChorro(document.FormPaneles.pFieldOrder, 0);
    document.FormPaneles.chorropFieldOrderT.value=crearChorro(document.FormPaneles.pFieldOrder, 1);
  }

  function salvarConsulta() {
    if (document.FormPaneles.pFieldSelect.length>0) {
      if (validaCriterios(document.FormPaneles.pFieldJoin)) {
        desc=prompt('Descripcion de la consulta :', '');
    if(desc!=null) {;
       document.FormPaneles.igualDescripcion.value="N";
          top.menu.despliegaDialog('Salvando consulta ...');
          crearAllChorros();
          document.FormPaneles.operacion.value="salvar";
          document.FormPaneles.descripcion.value=desc;
          document.FormPaneles.idCompatibles.value=top.main.frPestanas.frMenuControl.frDesplegables.frTema.idCompatibles;
          document.FormPaneles.cambioConsulta.value="false";
          document.FormPaneles.submit();
     }
      }
    } else {
      alert('Debe seleccionar algun campo de salida');
      return;
    }
  }

  function salvarConsultaComo() {
    if (document.FormPaneles.pFieldSelect.length>0) {
      if (validaCriterios(document.FormPaneles.pFieldJoin)) {
        desc=prompt('Descripcion de la consulta :', '');
        if(desc!=null) {;
          document.FormPaneles.igualDescripcion.value="N";
          top.menu.despliegaDialog('Salvando consulta ...');
          crearAllChorros();
          document.FormPaneles.operacion.value="salvar";
          document.FormPaneles.descripcion.value=desc;
          document.FormPaneles.idCompatibles.value=top.main.frPestanas.frMenuControl.frDesplegables.frTema.idCompatibles;
          document.FormPaneles.cambioConsulta.value="false";
          document.FormPaneles.submit();
        }
      }
    } else {
      alert('Debe seleccionar algun campo de salida');
      return;
    }
  }

  function recuperarConsulta() {
    document.FormPaneles.operacion.value="Recuperar";
    crearAllChorros();
    document.FormPaneles.idCompatibles.value=top.main.frPestanas.frMenuControl.frDesplegables.frTema.idCompatibles;
    document.FormPaneles.submit();
  }

  function ejecutarConsulta() {
    if (document.FormPaneles.pFieldSelect.length>0) {
      if (validaCriterios(document.FormPaneles.pFieldJoin)) {
        crearAllChorros();
        document.FormPaneles.operacion.value="ejecutar";
        document.FormPaneles.idCompatibles.value=top.main.frPestanas.frMenuControl.frDesplegables.frTema.idCompatibles;
        document.FormPaneles.submit();
        return(true);
      }
    } else {
      alert('Debe seleccionar algun campo de salida');
      return(false);
    }
  }

  function Permisos() {
    document.FormPaneles.operacion.value="Permisos";
    crearAllChorros();
    document.FormPaneles.idCompatibles.value=top.main.frPestanas.frMenuControl.frDesplegables.frTema.idCompatibles;
    document.FormPaneles.submit();
  }

  function crearChorro(refSelect, cod) {
    var vTmp="";
    for (i=0; i<refSelect.length; i++) {
      if (cod==0)
        vTmp += refSelect.options[i].value+'|';
      else
        vTmp += refSelect.options[i].text+'|';
    }
    return(vTmp);
  }

  function contarParentesis(refSelect, i) {
    var contador=0;
    var reg=refSelect[i].text;
    for (l=0;l<reg.length; l++) {
      car = reg.charAt(l);
      if (car == '(')
        contador++;
      if (car == ')')
        contador--;
    }
    return(contador);
  }

  function validaCriterios(refSelect) {
    var contGeneral=0,contador=0,i=0;
    for (i=0; i<refSelect.length; i++) {
      contador=contarParentesis(refSelect, i);
      contGeneral=contGeneral+contador;
    }
    if (contGeneral>0) {
      alert('Hay mas parentesis abiertos que cerrados en criterios');
      return(false);
    }
    if (contGeneral<0) {
      alert('Hay mas parentesis cerrados que abiertos en criterios');
      return(false);
    }
    if(refSelect.length>0) {
      var pos=-1;
      pos=refSelect.options[refSelect.length-1].text.indexOf(' .O.',0);
      if(pos>-1) {
        alert('Tiene un operador Logico (o/or) sin Cerrar');
        return(false);
      }
      pos=refSelect.options[refSelect.length-1].text.indexOf(' .Y.',0);
      if(pos>-1) {
        alert('Tiene un operador Logico (y/and) sin Cerrar');
        return(false);
      }
      pos=refSelect.options[refSelect.length-1].text.indexOf(' .NO.',0);
      if(pos>-1) {
        alert('Tiene un operador Logico (no/not) sin Cerrar');
        return(false);
      }
    }
    return(true);
  }

  function addFieldandValue(refSelect) {
    if (top.main.frPestanas.frMenuControl.frOpValor.getRealizarValidacion() && top.main.frPestanas.frMenuControl.frOpValor.vValorV.length==0) {
      alert('Defina un valor');
      return;
    }
    if (top.main.frPestanas.frMenuControl.frOpValor.vValorV.length==0) {
      top.main.frPestanas.frMenuControl.frOpValor.vValorT=' ';
      top.main.frPestanas.frMenuControl.frOpValor.vValorV=' ';
    }
    if (top.main.frPestanas.frMenuControl.frOpValor.vDesplV.length==0) {
      alert('Defina un operador');
      return;
    }
    if (!top.main.frPestanas.frMenuControl.frOpValor.validaCampo())
      return;
    valorT    =top.main.frPestanas.frMenuControl.frOpValor.vValorT;
    valorV    =top.main.frPestanas.frMenuControl.frOpValor.vValorV;
    idCampo   =top.main.frPestanas.frMenuControl.frDesplegables.frCampo.idCampo;
    idOperador=top.main.frPestanas.frMenuControl.frOpValor.vDesplV;
    vText     =top.main.frPestanas.frMenuControl.frDesplegables.frCampo.vText;
    tipoCampo =top.main.frPestanas.frMenuControl.frDesplegables.frCampo.vValue;
    refSelect.length=refSelect.length+1;
    refSelect.options[refSelect.length-1].text=vText+' '+refSelect[refSelect.length-1].text+' '+top.main.frPestanas.frMenuControl.frOpValor.vDesplT+' '+valorT;
    refSelect.options[refSelect.length-1].value=idCampo+'$'+idOperador+'$'+valorV+'$'+valorT+'$'+tipoCampo+'$';
    refSelect.selectedIndex=refSelect.length-1;
    top.main.frPestanas.frMenuControl.frDesplegables.frTema.soloCompatibles();
    document.FormPaneles.cambioConsulta.value="true";
  }

  function addLogico(refSelect, oper) {
    iSelDes=refSelect.selectedIndex;
    if (iSelDes<0)
      return;
    if (refSelect.length==0) {
      alert('A&ntilde;ada un campo');
      return;
    }
    var tmp=refSelect.options[iSelDes].text;
    tmp=reemplazaSubStr(tmp, ' .O.', '');
    tmp=reemplazaSubStr(tmp, ' .Y.', '');
    tmp=reemplazaSubStr(tmp, ' .NO.', '');
    if(document.FormPaneles.chNegacion.checked)
      refSelect.options[iSelDes].text=tmp;
    else
      refSelect.options[iSelDes].text=tmp+' .'+oper+'.';
    tmp=refSelect.options[iSelDes].value;
    tmp=reemplazaSubStr(tmp, ' operL=.O.', '');
    tmp=reemplazaSubStr(tmp, ' operL=.Y.', '');
    tmp=reemplazaSubStr(tmp, ' operL=.NO.', '');
    if(document.FormPaneles.chNegacion.checked)
      refSelect.options[iSelDes].value=tmp;
    else
      refSelect.options[iSelDes].value=tmp+' operL=.'+oper+'.';
    document.FormPaneles.cambioConsulta.value="true";
  }
  function eliminarParentesis(refSelect, parentesis, insPrin) {
    iSelDes=refSelect.selectedIndex;
    var tmpT=refSelect.options[iSelDes].text;
    var tmpV=refSelect.options[iSelDes].value;
    if (insPrin) {
      if (tmpT.substring(0, 1)=='(') {
        refSelect.options[iSelDes].value=tmpV.substring(1, tmpV.length);
        refSelect.options[iSelDes].text=tmpT.substring(1, tmpT.length);
      }
    } else {
      var pos=tmpT.indexOf(')', 0);
      if (pos>-1) {
        refSelect.options[iSelDes].text=tmpT.substring(0, pos)+tmpT.substring(pos+1, tmpT.length);
        var pos=tmpV.indexOf(')', 0);
        if (pos>-1)
          refSelect.options[iSelDes].value=tmpV.substring(0, pos)+tmpV.substring(pos+1, tmpV.length);
      }
    }
  }
  function addParentesis(refSelect, parentesis, insPrin) {
    iSelDes=refSelect.selectedIndex;
    if (iSelDes<0)
      return;
    if(document.FormPaneles.chNegacion.checked) {
      eliminarParentesis(refSelect, parentesis, insPrin);
      return;
    }
    var tmp=refSelect.options[iSelDes].text;
    var pos=-1, lg='';
    pos=tmp.indexOf(' .O.',0);
    if (pos==-1) {
      pos=tmp.indexOf(' .Y.',0);
      if (pos==-1) {
        pos=tmp.indexOf(' .NO.',0);
        if (pos>-1) {
          tmp=reemplazaSubStr(tmp, ' .NO.', '');
          lg=' .NO.';
        }
      }
      else {
        tmp=reemplazaSubStr(tmp, ' .Y.', '');
        lg=' .Y.';
      }
    }
    else {
      tmp=reemplazaSubStr(tmp, ' .O.', '');
      lg=' .O.';
    }
    if (insPrin) {
      refSelect.options[iSelDes].text=parentesis+tmp+lg;
      refSelect.options[iSelDes].value=parentesis+refSelect.options[iSelDes].value;
    } else {
      refSelect.options[iSelDes].text=tmp+parentesis+lg;
      refSelect.options[iSelDes].value=refSelect.options[iSelDes].value+parentesis;
    }
    document.FormPaneles.cambioConsulta.value="true";
  }

  function validarCabecera(campoCabecera) {
    if (campoCabecera.indexOf("?")>-1){
      alert('No se permite escribir ? en el campo cabecera');
      return(false);
    }
    if (campoCabecera.indexOf("¿")>-1){
      alert('No se permite escribir ¿ en el campo cabecera');
      return(false);
    }
    if (campoCabecera.indexOf("'")>-1){
      alert('No se permite escribir comilla en el campo cabecera');
      return(false);
    }
    if (campoCabecera.indexOf('"')>-1){
      alert('No se permite escribir comillas en el campo cabecera');
      return(false);
    }
      return(true);
  }
  function addField(refSelect, cod) {
    nomTabla=top.main.frPestanas.frMenuControl.frDesplegables.frTipoCampo.nomTabla;
    vText=top.main.frPestanas.frMenuControl.frDesplegables.frCampo.vText;
    idCampo=top.main.frPestanas.frMenuControl.frDesplegables.frCampo.idCampo;
    nomBonito=top.main.frPestanas.frMenuControl.frDesplegables.frCampo.nomBonito;
    txtCabecera=top.main.frPestanas.frMenuControl.frDesplegables.frCampo.document.forms[0].txtCabecera.value;
    if(!validarCabecera(txtCabecera)){
     return;
    }
    nomBonito=nomBonito.substring(0,nomBonito.indexOf("#")) + "#@%" + txtCabecera + "#@%";
    if (vText.length==0)
      return;
    if (idCampo.length==0)
      return;
    nElementos=refSelect.length;
    elementoRepetido=false;
    if (cod==0) {
      for (i=0; i<nElementos; i++) {
        if (refSelect.options[i].value.toUpperCase()==idCampo+'-'+nomBonito.toUpperCase()) {
          elementoRepetido=true;
          break;
        }
      }
    } else {
      for (i=0; i<nElementos; i++) {
        if (refSelect.options[i].value.toUpperCase()==idCampo) {
          elementoRepetido=true;
          break;
        }
      }
    }
    if (elementoRepetido)
      return;
    refSelect.length=refSelect.length+1;
    refSelect.options[nElementos].text=nomTabla+'.'+txtCabecera;
    if (cod==0) {
      refSelect.options[nElementos].value=idCampo+'-'+nomBonito;
    }
    else {
      refSelect.options[nElementos].value=idCampo;
    }
    refSelect.selectedIndex=refSelect.length-1;
    top.main.frPestanas.frMenuControl.frDesplegables.frTema.soloCompatibles();
    document.FormPaneles.cambioConsulta.value="true";
  }

  function borrarOpcion(refSelect) {
    iSelDes=refSelect.selectedIndex;
    if (iSelDes<0)
      return;
    nElemDes=refSelect.length;
    for(i=iSelDes; i+1<nElemDes; i++) {
      refSelect[i].text=refSelect[i+1].text;
      refSelect[i].value=refSelect[i+1].value;
    }
    refSelect[nElemDes-1].text="";
    refSelect[nElemDes-1].value="";
    refSelect.length=refSelect.length-1;
    refSelect.selectedIndex=iSelDes-1;
    document.FormPaneles.cambioConsulta.value="true";
  }

  function selectForThisBrowser(nombre) {
    if(top.menu.isIE)
      return(document.write('<Select CLASS=ANCHOSELECT1 Name='+nombre+' size=5>'));
    else
      return(document.write('<Select Name='+nombre+' size=5 width=550>'));
  }
  function selectTblPrioritaria() {
    nomTbl='';
    nomTblReal='';
    for (i=0; i<document.FormPaneles.pFieldSelect.length; i++) {
      nomTbl=document.FormPaneles.pFieldSelect.options[i].text.substring(0, document.FormPaneles.pFieldSelect.options[i].text.indexOf('.', 0));
      nomTblReal=document.FormPaneles.pFieldSelect.options[i].value.substring(document.FormPaneles.pFieldSelect.options[i].value.indexOf('-', 0)+1, document.FormPaneles.pFieldSelect.options[i].value.indexOf('.', 0));
      nomTblReal=nomTblReal.substring(nomTblReal.indexOf('to_char(', 0)+1);
      nomTblReal=nomTblReal.substring(nomTblReal.indexOf('TO_CHAR(', 0)+1);
      if (tblCamposSalida.indexOf(nomTbl+'|', 0)==-1) {
        tblCamposSalida += nomTbl+'|';
        tblCamposSalidaReal += nomTblReal+'|';
      }
    }
    window.open("/servlet/sGenericoConsultas?Evento=sTblPrioritaria&tblPrioritaria="+document.FormPaneles.tblPrioritaria.value, "TablaPrioritaria","toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars,resizable=yes,width=600,height=200,top=200,left=100");
  }
  function settblPrioritaria(nomTbl, nombre) {
    if (nomTbl.length>0)
     { document.FormPaneles.tblPrioritaria.value=nomTbl; document.FormPaneles.tablaprioritaria.value=nombre; 
       document.FormPaneles.chorrotblprioritariavalorV.value=nomTbl; document.FormPaneles.chorrotablaprioritariatextoT.value=nombre; }
    else
     { document.FormPaneles.tblPrioritaria.value=''; document.FormPaneles.tablaprioritaria.value=''; 
       document.FormPaneles.chorrotblprioritariavalorV.value=''; document.FormPaneles.chorrotablaprioritariatextoT.value=''; }
    document.FormPaneles.cambioConsulta.value="true";
  }
  function nada() {
  }
  function bajar(ref) {
    v=ref.selectedIndex;
    nElemDes=ref.length;
    if (v<=nElemDes){
      if (ref.selectedIndex+1<nElemDes){
        auxText=ref(v).text;
        auxValue=ref(v).value;
        ref(v).text=ref(v+1).text;
        ref(v).value=ref(v+1).value;
        ref(v+1).text=auxText;
        ref(v+1).value=auxValue;
        ref.selectedIndex=ref.selectedIndex+1;
        document.FormPaneles.cambioConsulta.value=true;
      }
    }
  }
  function Subir(ref) {
    v=ref.selectedIndex;
   if (v!=-1){
     if (ref.selectedIndex-1>-1){
       auxText=ref(v).text;
       auxValue=ref(v).value;
       ref(v).text=ref(v-1).text;
       ref(v).value=ref(v-1).value;
       ref(v-1).text=auxText;
       ref(v-1).value=auxValue;
       ref.selectedIndex=ref.selectedIndex-1;
       document.FormPaneles.cambioConsulta.value=true;
     }
   }
  }
</script>
<script language=JavaScript>
  if (document.all)
    document.write('<link rel=stylesheet href=/estilo/estilosIE.css>');
  else
    document.write('<link rel=stylesheet href=/estilo/estilosNT.css>');
</script>
</HEAD>
<BODY CLASS=BODY>
<CENTER>
<FORM NAME=FormPaneles ACTION=/servlet/sEdicionConsultaGenerica METHOD=post TARGET=main>
  <input type=hidden name="Evento" value="frameEdicionConsultaGenerica">
  <input type=hidden name=deDonde value="Editar">
  <input type=hidden name=operacion value="">
  <input type=hidden name=cambioConsulta value="false">
  <input type=hidden name=chorropFieldSelectT value="">
  <input type=hidden name=chorropFieldSelectV value="">
  <input type=hidden name=chorropFieldJoinT value="">
  <input type=hidden name=chorropFieldJoinV value="">
  <input type=hidden name=chorropFieldOrderT value="">
  <input type=hidden name=chorropFieldOrderV value="">
  <input type=hidden name=chorrotblprioritariavalorV value="">
  <input type=hidden name=chorrotablaprioritariatextoT value="">
  <input type=hidden name=descripcion value="">
  <input type=hidden name=idCompatibles value="">
  <input type=hidden name=igualDescripcion value="">
  <input type=hidden name=tblPrioritaria value="">
  <table border=0 cellpadding=0 cellspacing=0 width=100%>
    <tr>
    <td VALIGN=middle colspan=2><CENTER>
      <a HREF="javascript:selectTblPrioritaria();" ONMOUSEOVER="status='Selecci&oacute;n de tabla prioritaria'; return true;"><IMG src="/imagen/filter.gif" alt="Selecci&oacute;n de tabla prioritaria" BORDER=0></a>
	<BR>
       <input CLASS=ANCHOSELECTTOTAL type='text' size='20' onFocus='javascript:this.blur();' name='tablaprioritaria' value=""></CENTER></td>
      <TD CLASS=ETEXT  width=60% align=center ><b>Campos de Salida :</b><BR>
        <script>
          selectForThisBrowser('pFieldSelect');
        </script>
      </Select></td>
    <td >
      <table width=100%>
    <tr>
     <td   VALIGN=left><a HREF="javascript:Subir(document.FormPaneles.pFieldSelect);" ONMOUSEOVER="status='Subir'; return true;"><IMG src="/imagen/bFlechaArribaNuevo.gif" alt="Subir" BORDER="0"></a></td>
    </tr>
    <tr>
     <td   VALIGN=left><a HREF="javascript:bajar(document.FormPaneles.pFieldSelect);" ONMOUSEOVER="status='Bajar'; return true;"><IMG src="/imagen/bFlechaAbajoNuevo.gif" alt="Bajar" BORDER="0"></a></td>
    </tr>
      </table>
    </td>
    <td  VALIGN=middle ><center><a HREF="javascript:addField(document.FormPaneles.pFieldSelect, 0);" ONMOUSEOVER="status='A&ntilde;ade el campo seleccionado al panel de campos de salida'; return true;"><IMG src="/imagen/Mas.gif" alt="A&ntilde;ade campo seleccionado" BORDER="0"></a><br><br>
    <a HREF="javascript:borrarOpcion(document.FormPaneles.pFieldSelect);" ONMOUSEOVER="status='Elimina el campo seleccionado del panel de campos de salida'; return true;"><IMG src="/imagen/Menos.gif" alt="Elimina campo seleccionado" BORDER="0"></a></center></td>
    </tr>
    <tr>
      <TD width=15% align=center colspan=2>
        <a HREF="javascript:addParentesis(document.FormPaneles.pFieldJoin, '(', true);" ONMOUSEOVER="status='Abre un parentesis'; return true;"><IMG src="/imagen/parentesisOpen.gif" alt="Abre un parentesis" BORDER="0"></a>
      </td>
      <TD CLASS=ETEXT width=60% align=center ><b>Criterios :</b><BR>
        <script>
          selectForThisBrowser('pFieldJoin');
        </script>
      </Select>
      </td>
      <TD width=15% align=center >
        <a HREF="javascript:addParentesis(document.FormPaneles.pFieldJoin, ')', false);" ONMOUSEOVER="status='Cierra un parentesis'; return true;"><IMG src="/imagen/parentesisClose.gif" alt="Cierra un parentesis" BORDER=0></a><br>
      </TD>
      <TD>
        <table border=0 width=100%>
          <tr>
            <td align=right>
              <a HREF="javascript:addFieldandValue(document.FormPaneles.pFieldJoin);" ONMOUSEOVER="status='A&ntilde;ade el campo y criterio seleccionado al panel de criterios'; return true;"><IMG src="/imagen/Mas.gif" alt="A&ntilde;ade campo seleccionado" BORDER="0"></a><br><br>
            </td>
            <td align=center>
              <a HREF="javascript:addLogico(document.FormPaneles.pFieldJoin, 'Y');" ONMOUSEOVER="status='A&ntilde;ade operador l&oacute;gico y'; return true;"><IMG src="/imagen/y.gif" alt="A&ntilde;ade operador l&oacute;gico y" BORDER="0"></a>
            </td>
          </tr>
          <tr>
            <td align=right>
              <A HREF="javascript:nada();" ONMOUSEOVER="status='Activando el check, el uso de los operadores sera el contrario'; return true;"><input name=chNegacion type=checkbox><IMG src="/imagen/Help.gif" alt='Activando el check, el uso de los operadores sera el contrario' BORDER=0></a>
            </td>
            <td align=center>
              <a HREF="javascript:addLogico(document.FormPaneles.pFieldJoin, 'O');" ONMOUSEOVER="status='A&ntilde;ade operador l&oacute;gico o'; return true;"><IMG src="/imagen/o.gif" alt="A&ntilde;ade operador l&oacute;gico o" BORDER="0"></a>
            </td>
          </tr>
          <tr>
            <td align=right>
              <a HREF="javascript:borrarOpcion(document.FormPaneles.pFieldJoin);" ONMOUSEOVER="status='Elimina el campo seleccionado del panel de criterios'; return true;"><IMG src="/imagen/Menos.gif" alt="Elimina campo seleccionado" BORDER="0"></a>
            </td>
            <td align=center>
              <a HREF="javascript:addLogico(document.FormPaneles.pFieldJoin, 'NO');" ONMOUSEOVER="status='A&ntilde;ade negaci&oacute;n l&oacute;gica'; return true;"><IMG src="/imagen/no.gif" alt="A&ntilde;ade negaci&oacute;n l&oacute;gica" BORDER="0"></a>
            </td>
          </tr>
        </table>
      </td>
    </tr>
    <tr>
    <td colspan=2>
      &nbsp;
    </td>
      <TD CLASS=ETEXT width=60% align=center ><b>Campos de Ordenaci&oacute;n :</b><BR>
        <script>
          selectForThisBrowser('pFieldOrder');
        </script>
      </Select></TD>
    <td >
      <table width=100%>
    <tr>
     <td   VALIGN=left><a HREF="javascript:Subir(document.FormPaneles.pFieldOrder);" ONMOUSEOVER="status='Subir'; return true;"><IMG src="/imagen/bFlechaArribaNuevo.gif" alt="Subir" BORDER="0"></a></td>
    </tr>
    <tr>
     <td   VALIGN=left><a HREF="javascript:bajar(document.FormPaneles.pFieldOrder);" ONMOUSEOVER="status='Bajar'; return true;"><IMG src="/imagen/bFlechaAbajoNuevo.gif" alt="Bajar" BORDER="0"></a></td>
    </tr>
      </table>
    </td>
    <td  VALIGN=middle><center><a HREF="javascript:addField(document.FormPaneles.pFieldOrder, 1);" ONMOUSEOVER="status='A&ntilde;ade el campo seleccionado al panel de campos de ordenaci&oacute;n'; return true;"><IMG src="/imagen/Mas.gif" alt="A&ntilde;ade campo seleccionado" BORDER="0"></a><br><br>
    <a HREF="javascript:borrarOpcion(document.FormPaneles.pFieldOrder);" ONMOUSEOVER="status='Elimina el campo seleccionado del panel de campos de ordenaci&oacute;n'; return true;"><IMG src="/imagen/Menos.gif" alt="Elimina campo seleccionado" BORDER="0"></a></center></td>
    </tr>
  </table>
</FORM>
</CENTER>
</BODY>
</HTML>

