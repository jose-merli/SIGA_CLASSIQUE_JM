CREATE OR REPLACE Function f_Siga_Asuntoorigen_MV(p_Idmovimiento In Number, p_Idinstitucion In Number)
  Return Varchar2 Is
  v_Informacion Varchar2(200);

Begin
  v_Informacion := Null;
  Begin
  Select 'movimientosVarios.ActuacionDesigna.titulo'
    Into v_Informacion
    From Scs_Actuaciondesigna Actdesig, Scs_Designa des
   Where actdesig.Idinstitucion = des.Idinstitucion
     And actdesig.Idturno = des.Idturno
     And actdesig.Anio = des.Anio
     And actdesig.Numero = des.Numero
     And Actdesig.Idinstitucion = p_Idinstitucion
     And Actdesig.Idmovimiento = p_Idmovimiento;
  Exception
  When no_data_found Then
    v_Informacion := Null;
  End;

  If v_Informacion Is Null Then
    Begin
    Select 'movimientosVarios.ActuacionAsistencias.titulo'
      Into v_Informacion
      From Scs_Actuacionasistencia Act
     Where Act.Idinstitucion = p_Idinstitucion
       And Act.Idmovimiento = p_Idmovimiento;
    Exception
    When no_data_found Then
      v_Informacion := Null;
    End;
  End If;

  If v_Informacion Is Null Then
    Begin
    Select 'movimientosVarios.asistencia.titulo'
      Into v_Informacion
      From Scs_Asistencia Asistencia
     Where Asistencia.Idinstitucion = p_Idinstitucion
       And Asistencia.Idmovimiento = p_Idmovimiento;
    Exception
    When no_data_found Then
      v_Informacion := Null;
    End;
  End If;

  If v_Informacion Is Null Then
    Begin
    Select 'movimientosVarios.guardia.titulo'
      Into v_Informacion
      From Scs_Cabeceraguardias Guardias, Scs_Guardiasturno Guardiaturno, Scs_Turno Turno
     Where Guardias.Idinstitucion = p_Idinstitucion
       And Guardias.Idmovimiento = p_Idmovimiento
       And Guardiaturno.Idinstitucion = Guardias.Idinstitucion
       And Guardiaturno.Idturno = Guardias.Idturno
       And Guardiaturno.Idguardia = Guardias.Idguardia
       And Guardiaturno.Idinstitucion = Turno.Idinstitucion
       And Guardiaturno.Idturno = Turno.Idturno;
    Exception
    When no_data_found Then
      v_Informacion := Null;
    End;
  End If;

  Return v_Informacion;

End f_Siga_Asuntoorigen_MV;
/
