CREATE OR REPLACE TRIGGER SCS_EJG_AUR
AFTER UPDATE
ON SCS_EJG 
REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
Declare
  v_Idioma Cen_Institucion.Idlenguaje%Type;
  Estado_Reparto      Constant Int := 20; -- Remitida apertura a CAJG-Reparto Ponente
  Estado_alta         Constant Int := 23; -- Solicitud en procesdo de alta
  Estado_Dictaminado  Constant Int := 6; -- Dictaminado
  Estado_Resuelto     Constant Int := 10; -- Resuelto Comision
  Estado_Res_Impu     Constant Int := 13; -- Resuelta Impugnacion
  Estado_Devuelto     Constant Int := 21; -- Devuelto al colegio
  Resolucion_Devuelto Constant Int := 6; -- Resolucion Devuelto al colegio

  Valor_Automatico Constant Varchar2(1) := '1';
  v_Fechanula        Date := '01/01/9000';
  v_Idnulo           Number(2) := -1;
  v_Idinstitucioncms Cen_Institucion.Idinstitucion%Type;

Begin

  -- Mantenimiento de estados
  If :New.Usumodificacion >= 0 Then
  
    -- obteniendo idioma de forma general  
    Select I1.Idlenguaje,
           Nvl((Select I2.Idinstitucion
                 From Cen_Institucion I2
                Where I2.Idcomision = I1.Idcomision
                  And I2.Codigoext Is Null),
               I1.Idinstitucion)
      Into v_Idioma, v_Idinstitucioncms
      From Cen_Institucion I1
     Where I1.Idinstitucion = :New.Idinstitucion;
    --    0. Fecha de apertura. si modifican la fecha de apertura modificamos la fecha del estado inicial
    if (:Old.Fechaapertura <> :New.Fechaapertura) then
      Update Scs_Estadoejg
         Set Fechainicio = :New.Fechaapertura
       Where Idinstitucion = :New.Idinstitucion
         And Idtipoejg = :New.Idtipoejg
         And Anio = :New.Anio
         And Numero = :New.Numero
         And Idestadoejg = Estado_alta; -- Solicitud en procesdo de alta
    
    end if;
    -- 1. IDTIPODICTAMENEJG + Fechadictamen  
    If (( -- Cambia el dictamen o la fecha de dictamen (IDTIPODICTAMENEJG o Fechadictamen)  
        Nvl(:Old.Fechadictamen, v_Fechanula) <>
        Nvl(:New.Fechadictamen, v_Fechanula) Or
        Nvl(:Old.Idtipodictamenejg, v_Idnulo) <>
        Nvl(:New.Idtipodictamenejg, v_Idnulo)
       
       ) And ( -- Que hay dictamen y fecha de dictamen (IDTIPODICTAMENEJG y Fechadictamen) antes o despues 
        (:New.Fechadictamen Is Not Null And
        :New.Idtipodictamenejg Is Not Null) Or
        (:Old.Fechadictamen Is Not Null And
        :Old.Idtipodictamenejg Is Not Null))) Then
    
      --Ponemos fecha de baja a todos los estados anteriores que hayan sido dictaminados
      Update Scs_Estadoejg
         Set Fechabaja = Sysdate
       Where Idinstitucion = :New.Idinstitucion
         And Idtipoejg = :New.Idtipoejg
         And Anio = :New.Anio
         And Numero = :New.Numero
         And Idestadoejg = Estado_Dictaminado -- Dictaminado
         And Nvl(Automatico, '0') = Valor_Automatico -- Solo automatico
         And Fechabaja Is Null;
    
      If (:New.Fechadictamen Is Not Null And
         :New.Idtipodictamenejg Is Not Null) Then
      
        --inserto el estado dictaminado y pongo en las observacions el dictamen. La fecha de inicio es la fecha de dictamen
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
           Automatico,
           Propietariocomision)
        Values
          (:New.Idinstitucion,
           :New.Idtipoejg,
           :New.Anio,
           :New.Numero,
           Estado_Dictaminado, -- Dictaminado
           :New.Fechadictamen,
           :New.Fechamodificacion,
           :New.Usumodificacion,
           f_Siga_Getrecurso_Etiqueta('gratuita.ejg.estado.literal.automatico',
                                      v_Idioma) || ' ' ||
           f_Siga_Getrecurso_Etiqueta('gratuita.busquedaEJG.dictamen',
                                      v_Idioma) || ' ' ||
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
               1),
           Valor_Automatico,
           0);
      End If;
    End If;
  
    -- 2. IDPONENTE + Fechapresentacionponente  
    If (( -- Cambia el ponente o la fecha de presentacion del ponente (IDPONENTE o Fechapresentacionponente)  
        Nvl(:Old.Fechapresentacionponente, v_Fechanula) <>
        Nvl(:New.Fechapresentacionponente, v_Fechanula) Or
        Nvl(:Old.Idponente, v_Idnulo) <> Nvl(:New.Idponente, v_Idnulo)
       
       ) And ( -- Que hay ponente y fecha de presentacion del ponente (IDPONENTE y Fechapresentacionponente) antes o despues 
        (:New.Fechapresentacionponente Is Not Null And
        :New.Idponente Is Not Null) Or (:Old.Fechapresentacionponente Is Not Null And
        :Old.Idponente Is Not Null))) Then
    
      --Ponemos fecha de baja a todos los estados anteriores que hayan sido presentacion ponente          
      Update Scs_Estadoejg
         Set Fechabaja = Sysdate
       Where Idinstitucion = :New.Idinstitucion
         And Idtipoejg = :New.Idtipoejg
         And Anio = :New.Anio
         And Numero = :New.Numero
         And Idestadoejg = Estado_Reparto -- Remitida apertura a CAJG-Reparto Ponente
         And Nvl(Automatico, '0') = Valor_Automatico -- Solo automatico
         And Fechabaja Is Null;
    
      If (:New.Fechapresentacionponente Is Not Null And
         :New.Idponente Is Not Null) Then
      
        --inserto el estado presentacion ponente y pongo en las observacions el ponente. La fecha de inicio es la fecha de presentacion ponente
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
           Automatico,
           Propietariocomision)
        Values
          (:New.Idinstitucion,
           :New.Idtipoejg,
           :New.Anio,
           :New.Numero,
           Estado_Reparto, -- Remitida apertura a CAJG-Reparto Ponente
           :New.Fechapresentacionponente,
           :New.Fechamodificacion,
           :New.Usumodificacion,
           f_Siga_Getrecurso_Etiqueta('gratuita.ejg.estado.literal.automatico',
                                      v_Idioma) || ' ' ||
           f_Siga_Getrecurso_Etiqueta('gratuita.operarRatificacion.literal.ponente',
                                      v_Idioma) || ' ' ||
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
               1),
           Valor_Automatico,
           1);
      End If;
    End If;
  
    -- 3. IDTIPORATIFICACIONEJG + Fecharesolucioncajg  
    If (( -- Cambia la resolucion o la fecha de resolucion (IDTIPORATIFICACIONEJG o Fecharesolucioncajg)  
        Nvl(:Old.Fecharesolucioncajg, v_Fechanula) <>
        Nvl(:New.Fecharesolucioncajg, v_Fechanula) Or
        Nvl(:Old.Idtiporatificacionejg, v_Idnulo) <>
        Nvl(:New.Idtiporatificacionejg, v_Idnulo)
       
       ) And ( -- Que hay resolucion y fecha de resolucion (IDTIPORATIFICACIONEJG y Fecharesolucioncajg) antes o despues 
        (:New.Fecharesolucioncajg Is Not Null And
        :New.Idtiporatificacionejg Is Not Null) Or
        (:Old.Fecharesolucioncajg Is Not Null And
        :Old.Idtiporatificacionejg Is Not Null))) Then
    
      --Ponemos fecha de baja a todos los estados anteriores que hayan sido resuelto comision
      Update Scs_Estadoejg
         Set Fechabaja = Sysdate
       Where Idinstitucion = :New.Idinstitucion
         And Idtipoejg = :New.Idtipoejg
         And Anio = :New.Anio
         And Numero = :New.Numero
         And Idestadoejg = Estado_Resuelto -- Resuelto Comision
         And Nvl(Automatico, '0') = Valor_Automatico -- Solo automatico
         And Fechabaja Is Null;
    
      If (:New.Fecharesolucioncajg Is Not Null And
         :New.Idtiporatificacionejg Is Not Null) Then
      
        --inserto el estado resuelto y pongo en las observacions la resolucion. La fecha de inicio es la fecha de resolucion
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
           Automatico,
           Propietariocomision)
        Values
          (:New.Idinstitucion,
           :New.Idtipoejg,
           :New.Anio,
           :New.Numero,
           Estado_Resuelto, -- Resuelto Comision
           :New.Fecharesolucioncajg,
           :New.Fechamodificacion,
           :New.Usumodificacion,
           f_Siga_Getrecurso_Etiqueta('gratuita.ejg.estado.literal.automatico',
                                      v_Idioma) || ' ' ||
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
               1),
           Valor_Automatico,
           1);
        If (:New.Idtiporatificacionejg = Resolucion_Devuelto) Then
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
             Automatico,
             Propietariocomision)
          Values
            (:New.Idinstitucion,
             :New.Idtipoejg,
             :New.Anio,
             :New.Numero,
             Estado_Devuelto, -- Devuelto Colegio
             :New.Fecharesolucioncajg,
             :New.Fechamodificacion,
             :New.Usumodificacion,
             f_Siga_Getrecurso_Etiqueta('gratuita.ejg.estado.literal.automatico',
                                        v_Idioma),
             Nvl((Select Max(Idestadoporejg) + 1
                   From Scs_Estadoejg
                  Where Idinstitucion = :New.Idinstitucion
                    And Idtipoejg = :New.Idtipoejg
                    And Anio = :New.Anio
                    And Numero = :New.Numero),
                 1),
             Valor_Automatico,
             0);
        
        End If;
      
      End If;
    End If;
  
    -- 4. IDTIPORESOLAUTO + Fechaauto  
    If (( -- Cambia la impugnacion o la fecha de impugnacion (IDTIPORESOLAUTO o Fechaauto)  
        Nvl(:Old.Fechaauto, v_Fechanula) <>
        Nvl(:New.Fechaauto, v_Fechanula) Or
        Nvl(:Old.Idtiporesolauto, v_Idnulo) <>
        Nvl(:New.Idtiporesolauto, v_Idnulo)
       
       ) And
       ( -- Que hay impugnacion y fecha de impugnacion (IDTIPORESOLAUTO y Fechaauto) antes o despues 
        (:New.Fechaauto Is Not Null And :New.Idtiporesolauto Is Not Null) Or
        (:Old.Fechaauto Is Not Null And :Old.Idtiporesolauto Is Not Null))) Then
    
      --Ponemos fecha de baja a todos los estados anteriores que hayan sido impugnado
      Update Scs_Estadoejg
         Set Fechabaja = Sysdate
       Where Idinstitucion = :New.Idinstitucion
         And Idtipoejg = :New.Idtipoejg
         And Anio = :New.Anio
         And Numero = :New.Numero
         And Idestadoejg = Estado_Res_Impu -- Resuelta Impugnacion
         And Nvl(Automatico, '0') = Valor_Automatico -- Solo automatico
         And Fechabaja Is Null;
    
      If (:New.Fechaauto Is Not Null And :New.Idtiporesolauto Is Not Null) Then
      
        --inserto el estado impugnado y pongo en las observaciones el auto resolutorio. La fecha de inicio es la fecha de impugnacion
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
           Automatico,
           Propietariocomision)
        Values
          (:New.Idinstitucion,
           :New.Idtipoejg,
           :New.Anio,
           :New.Numero,
           Estado_Res_Impu, -- Resuelta Impugnacion
           :New.Fechaauto,
           :New.Fechamodificacion,
           :New.Usumodificacion,
           f_Siga_Getrecurso_Etiqueta('gratuita.ejg.estado.literal.automatico',
                                      v_Idioma) || ' ' ||
           f_Siga_Getrecurso_Etiqueta('pestana.justiciagratuitaejg.impugnacion',
                                      v_Idioma) || ' ' ||
           (Select f_Siga_Getrecurso(Descripcion, v_Idioma)
              From Scs_Tiporesolauto
             Where Idtiporesolauto = :New.Idtiporesolauto),
           Nvl((Select Max(Idestadoporejg) + 1
                 From Scs_Estadoejg
                Where Idinstitucion = :New.Idinstitucion
                  And Idtipoejg = :New.Idtipoejg
                  And Anio = :New.Anio
                  And Numero = :New.Numero),
               1),
           Valor_Automatico,
           1);
      End If;
    End If;
  End If;
End Scs_Ejg_Aur;