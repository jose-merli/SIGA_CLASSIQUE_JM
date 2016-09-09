CREATE OR REPLACE Function F_Siga_GetNIFinteresadosdes(p_Institucion In Number,
                                                               p_Anio        In Number,
                                                               p_Turno       In Number,
                                                               p_Numero      In Number,
                                                               p_Tipo        In Number)
  Return Varchar2 Is

  v_Result Varchar2(2000);

  Cursor c_Interesados_Designa Is
    Select Per.Nif nif, Per.Nombre, Per.Apellido1, Nvl(Per.Apellido2, '') Apellido2
      From Scs_Defendidosdesigna Def, Scs_Personajg Per
     Where Def.Idinstitucion = Per.Idinstitucion
       And Def.Idpersona = Per.Idpersona
       And Def.Idinstitucion = p_Institucion
       And Def.Idturno = p_Turno
       And Def.Anio = p_Anio
       And Def.Numero = p_Numero
     Order By Apellido1, Apellido2, Nombre;

  Cursor c_Interesados_Ejg Is
    Select Per.Nif nif, Per.Nombre, Per.Apellido1, Nvl(Per.Apellido2, '') Apellido2
      From Scs_Ejgdesigna        Ejgdes,
           Scs_Unidadfamiliarejg Uniejg,
           Scs_Personajg         Per
     Where Ejgdes.Idinstitucion = Uniejg.Idinstitucion
       And Ejgdes.Idtipoejg = Uniejg.Idtipoejg
       And Ejgdes.Anioejg = Uniejg.Anio
       And Ejgdes.Numeroejg = Uniejg.Numero
       And Uniejg.Idinstitucion = Per.Idinstitucion
       And Uniejg.Idpersona = Per.Idpersona
       And Ejgdes.Idinstitucion = p_Institucion
       And Ejgdes.Idturno = p_Turno
       And Ejgdes.Aniodesigna = p_Anio
       And Ejgdes.Numerodesigna = p_Numero

    Union

    Select Per.Nif nif, Per.Nombre, Per.Apellido1, Nvl(Per.Apellido2, '') Apellido2
      From Scs_Ejgdesigna Ejgdes, Scs_Ejg Ejg, Scs_Personajg Per
     Where Ejgdes.Idinstitucion = Ejg.Idinstitucion
       And Ejgdes.Idtipoejg = Ejg.Idtipoejg
       And Ejgdes.Anioejg = Ejg.Anio
       And Ejgdes.Numeroejg = Ejg.Numero
       And Ejg.Idinstitucion = Per.Idinstitucion
       And Ejg.Idpersonajg = Per.Idpersona
       And Ejgdes.Idinstitucion = p_Institucion
       And Ejgdes.Idturno = p_Turno
       And Ejgdes.Aniodesigna = p_Anio
       And Ejgdes.Numerodesigna = p_Numero
     Order By Apellido1, Apellido2, Nombre;

Begin

  For i In c_Interesados_Designa Loop
    If (p_Tipo = 0) Then
      v_Result := v_Result || i.nif || ', ';
    Elsif (p_Tipo = 1) Then
      v_Result := v_Result || i.nif  || Chr(10) || Chr(13);
    Else
      v_Result := v_Result || i.Nombre || ' ' || i.Apellido1 || ' ' ||
                  i.Apellido2 || ', ';
    End If;
  End Loop;

  If v_Result Is Null Then
    For i In c_Interesados_Ejg Loop
      If (p_Tipo = 0) Then
        v_Result := v_Result || i.nif  || ', ';
      Elsif (p_Tipo = 1) Then
        v_Result := v_Result || i.nif  || Chr(10) || Chr(13);
      Else
        v_Result := v_Result || i.nif  || ', ';
      End If;
    End Loop;
  End If;

  v_Result := Rtrim(Trim(v_Result), ',');

  Return(v_Result);
End f_Siga_GetNIFinteresadosdes;
/
