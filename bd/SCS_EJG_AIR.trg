create or replace trigger "SCS_EJG_AIR"  
  after insert on SCS_EJG
  for each row
Declare
  Mensajeautomatico  Varchar2(200);
  v_Idioma           Cen_Institucion.Idlenguaje%Type;
  v_Idinstitucioncms Cen_Institucion.Idinstitucion%Type;
Begin

  If :New.Usumodificacion >= 0 Then
  
    Select I1.Idlenguaje,
           Nvl((Select I2.Idinstitucion
                 From Cen_Institucion I2
                Where I2.Idcomision = I1.Idcomision
                  And I2.Codigoext Is Null),
               I1.Idinstitucion)
      Into v_Idioma, v_Idinstitucioncms
      From Cen_Institucion I1
     Where I1.Idinstitucion = :New.Idinstitucion;
  
    Select Descripcion
      Into Mensajeautomatico
      From Gen_Recursos
     Where Idrecurso = 'gratuita.ejg.estado.literal.automatico'
       And Idlenguaje = v_Idioma;
  --0. Metemos por defecto en todos los expediente el estado inicial --> Solicitud en Proceso de Alta
  Insert Into Scs_Estadoejg
        (Idinstitucion,
         Idtipoejg,
         Anio,
         Numero,
         Idestadoejg,
         Fechainicio,
         Fechamodificacion,
         Usumodificacion,
         Observaciones,
         Idestadoporejg,
         Automatico)
      Values
        (:New.Idinstitucion,
         :New.Idtipoejg,
         :New.Anio,
         :New.Numero,
         '23' /*Solicitud en Proceso de Alta*/,
         :New.Fechaapertura,
         :New.Fechamodificacion,
         :New.Usumodificacion,
         Mensajeautomatico || ' ' ||
         f_Siga_Getrecurso_Etiqueta('expedientes.nuevoExpediente.cabecera', v_Idioma) || ' '
         ,
         Nvl((Select Max(Idestadoporejg) + 1
               From Scs_Estadoejg
              Where Idinstitucion = :New.Idinstitucion
                And Idtipoejg = :New.Idtipoejg
                And Anio = :New.Anio
                And Numero = :New.Numero),
             '1'),
         '1');
  
    -- 1. Estado a partir de FECHADICTAMEN
    If :New.Fechadictamen Is Not Null And :New.Idtipodictamenejg Is Not Null Then
      Insert Into Scs_Estadoejg
        (Idinstitucion,
         Idtipoejg,
         Anio,
         Numero,
         Idestadoejg,
         Fechainicio,
         Fechamodificacion,
         Usumodificacion,
         Observaciones,
         Idestadoporejg,
         Automatico)
      Values
        (:New.Idinstitucion,
         :New.Idtipoejg,
         :New.Anio,
         :New.Numero,
         '6' /*Dictaminado*/,
         :New.Fechadictamen,
         :New.Fechamodificacion,
         :New.Usumodificacion,
         Mensajeautomatico || ' ' ||
         f_Siga_Getrecurso_Etiqueta('gratuita.busquedaEJG.dictamen', v_Idioma) || ' ' ||
         (Select f_Siga_Getrecurso(Descripcion, v_Idioma)
            From Scs_Tipodictamenejg
           Where Idinstitucion = :New.Idinstitucion
             And Idtipodictamenejg = :New.Idtipodictamenejg),
         Nvl((Select Max(Idestadoporejg) + 1
               From Scs_Estadoejg
              Where Idinstitucion = :New.Idinstitucion
                And Idtipoejg = :New.Idtipoejg
                And Anio = :New.Anio
                And Numero = :New.Numero),
             '1'),
         '1');
    End If;
  
    -- 2. Estado a partir de fecha presentacion ponente
    If :New.Idponente Is Not Null And :New.Fechapresentacionponente Is Not Null Then
      Insert Into Scs_Estadoejg
        (Idinstitucion,
         Idtipoejg,
         Anio,
         Numero,
         Idestadoejg,
         Fechainicio,
         Fechamodificacion,
         Usumodificacion,
         Observaciones,
         Idestadoporejg,
         Automatico)
      Values
        (:New.Idinstitucion,
         :New.Idtipoejg,
         :New.Anio,
         :New.Numero,
         '20' /*Presentacion Ponente*/,
         :New.Fechapresentacionponente,
         :New.Fechamodificacion,
         :New.Usumodificacion,
         Mensajeautomatico || ' ' ||
         f_Siga_Getrecurso_Etiqueta('gratuita.operarRatificacion.literal.ponente', v_Idioma) || ' ' ||
         (Select f_Siga_Getrecurso(Nombre, v_Idioma)
            From Scs_Ponente
           Where Idponente = :New.Idponente
             And Idinstitucion = v_Idinstitucioncms),
         Nvl((Select Max(Idestadoporejg) + 1
               From Scs_Estadoejg
              Where Idinstitucion = :New.Idinstitucion
                And Idtipoejg = :New.Idtipoejg
                And Anio = :New.Anio
                And Numero = :New.Numero),
             '1'),
         '1');
    End If;
  
    -- 3. Estado a partir de FECHARESOLUCIONCAJG
    If :New.Fecharesolucioncajg Is Not Null And :New.Idtiporatificacionejg Is Not Null Then
      Insert Into Scs_Estadoejg
        (Idinstitucion,
         Idtipoejg,
         Anio,
         Numero,
         Idestadoejg,
         Fechainicio,
         Fechamodificacion,
         Usumodificacion,
         Observaciones,
         Idestadoporejg,
         Automatico)
      Values
        (:New.Idinstitucion,
         :New.Idtipoejg,
         :New.Anio,
         :New.Numero,
         '10' /*Resuelto Comision*/,
         :New.Fecharesolucioncajg,
         :New.Fechamodificacion,
         :New.Usumodificacion,
         Mensajeautomatico || ' ' ||
         f_Siga_Getrecurso_Etiqueta('gratuita.operarRatificacion.literal.tipoRatificacion',
                                    v_Idioma) || ' ' ||
         (Select f_Siga_Getrecurso(Descripcion, v_Idioma)
            From Scs_Tiporesolucion
           Where Idtiporesolucion = :New.Idtiporatificacionejg),
         Nvl((Select Max(Idestadoporejg) + 1
               From Scs_Estadoejg
              Where Idinstitucion = :New.Idinstitucion
                And Idtipoejg = :New.Idtipoejg
                And Anio = :New.Anio
                And Numero = :New.Numero),
             '1'),
         '1');
    End If;
  
    -- 4. Estado a partir de FECHAAUTO
    If :New.Fechaauto Is Not Null And :New.Idtiporesolauto Is Not Null Then
      Insert Into Scs_Estadoejg
        (Idinstitucion,
         Idtipoejg,
         Anio,
         Numero,
         Idestadoejg,
         Fechainicio,
         Fechamodificacion,
         Usumodificacion,
         Observaciones,
         Idestadoporejg,
         Automatico)
      Values
        (:New.Idinstitucion,
         :New.Idtipoejg,
         :New.Anio,
         :New.Numero,
         '13' /*Resuelta Impugnacion*/,
         :New.Fechaauto,
         :New.Fechamodificacion,
         :New.Usumodificacion,
         Mensajeautomatico || ' ' ||
         f_Siga_Getrecurso_Etiqueta('pestana.justiciagratuitaejg.impugnacion', v_Idioma) || ' ' ||
         (Select f_Siga_Getrecurso(Descripcion, v_Idioma)
            From Scs_Tiporesolauto
           Where Idtiporesolauto = :New.Idtiporesolauto),
         Nvl((Select Max(Idestadoporejg) + 1
               From Scs_Estadoejg
              Where Idinstitucion = :New.Idinstitucion
                And Idtipoejg = :New.Idtipoejg
                And Anio = :New.Anio
                And Numero = :New.Numero),
             '1'),
         '1');
    
    End If;
  
  End If;

End Scs_Ejg_Air;
/
