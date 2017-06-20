CREATE OR REPLACE Function f_Siga_Asuntoasociado_MV(p_Idmovimiento In Number, p_Idinstitucion In Number, p_IdLenguaje In Number)
  Return Varchar2 Is
  v_Informacion Varchar2(200);

Begin
  v_Informacion := Null;
  Select (f_siga_getrecurso_etiqueta('movimientosVarios.ActuacionDesigna.titulo', p_IdLenguaje) || ' ' || Idturno || '/' || Numero || '/' || Numeroasunto)
    Into v_Informacion
    From Scs_Actuaciondesigna Actdesig
   Where Actdesig.Idinstitucion = p_Idinstitucion
     And Actdesig.Idmovimiento = p_Idmovimiento;
  
  If v_Informacion Is Null Then
    Select (f_siga_getrecurso_etiqueta('movimientosVarios.ActuacionAsistencias.titulo', p_IdLenguaje) || ' '  || Actdesig.Anio || '/' || Actdesig.Numero || '/' || Actdesig.Idactuacion)
      Into v_Informacion
      From Scs_Actuacionasistencia Actdesig
     Where Actdesig.Idinstitucion = p_Idinstitucion
       And Actdesig.Idmovimiento = p_Idmovimiento;
  End If;
  
  If v_Informacion Is Null Then
    Select (f_siga_getrecurso_etiqueta('movimientosVarios.asistencia.titulo', p_IdLenguaje) || ' ' || Anio || '/' || Numero)
      Into v_Informacion
      From Scs_Asistencia Asistencia
     Where Asistencia.Idinstitucion = p_Idinstitucion
       And Asistencia.Idmovimiento = p_Idmovimiento;
  End If;
  
  If v_Informacion Is Null Then
    Select (f_siga_getrecurso_etiqueta('movimientosVarios.guardia.titulo', p_IdLenguaje) ||' ' || Guardias.Fechainicio || ' en ' || Guardiaturno.Nombre || ' ' || Turno.Abreviatura)
      Into v_Informacion
      From Scs_Cabeceraguardias Guardias, Scs_Guardiasturno Guardiaturno, Scs_Turno Turno
     Where Guardias.Idinstitucion = p_Idinstitucion
       And Guardias.Idmovimiento = p_Idmovimiento
       And Guardiaturno.Idinstitucion = Guardias.Idinstitucion
       And Guardiaturno.Idturno = Guardias.Idturno
       And Guardiaturno.Idguardia = Guardias.Idguardia
       And Guardiaturno.Idinstitucion = Turno.Idinstitucion
       And Guardiaturno.Idturno = Turno.Idturno;
  End If;
  
  If v_Informacion Is Null Then
   select f_siga_getrecurso_etiqueta('movimientosVarios.movimientosVarios.titulo', p_IdLenguaje) into v_Informacion from dual;
  End If;
  If v_Informacion Is Null Then
   v_Informacion := 'kljklj';
  End If;
  Return v_Informacion;
 
End f_Siga_Asuntoasociado_MV;
/