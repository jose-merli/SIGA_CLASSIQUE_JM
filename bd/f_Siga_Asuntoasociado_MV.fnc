CREATE OR REPLACE Function f_Siga_Asuntoasociado_MV(p_Idmovimiento In Number, p_Idinstitucion In Number, p_IdLenguaje In Number)
  Return Varchar2 Is
  v_Informacion Varchar2(200);

Begin
  v_Informacion := Null;
  Begin
  Select (f_siga_getrecurso_etiqueta('movimientosVarios.ActuacionDesigna.titulo', p_IdLenguaje) || ' ' || des.Anio || '/' || des.Codigo || '/' || Numeroasunto)
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
    Select (f_siga_getrecurso_etiqueta('movimientosVarios.ActuacionAsistencias.titulo', p_IdLenguaje) || ' '  || Act.Anio || '/' || Act.Numero || '/' || Act.Idactuacion)
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
    Select (f_siga_getrecurso_etiqueta('movimientosVarios.asistencia.titulo', p_IdLenguaje) || ' ' || Anio || '/' || Numero)
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
    Select (f_siga_getrecurso_etiqueta('movimientosVarios.guardia.titulo', p_IdLenguaje) ||' ' || Guardias.Fechainicio || ' en ' || Guardiaturno.Nombre || ' (' || Turno.Abreviatura || ')')
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

End f_Siga_Asuntoasociado_MV;
/
