create or replace function F_SIGA_MOVIMIENTOSVARIOS (P_IDMOVIMIENTO IN NUMBER, P_IDINSTITUCION IN NUMBER) return varchar2 is
v_informacion VARCHAR2(4000);
v_contador number;

BEGIN
     v_contador:=0;
     select count (*)
     into v_contador
     from scs_actuaciondesigna actDesig
     where actDesig.IDINSTITUCION=P_IDINSTITUCION
          and actDesig.IDMOVIMIENTO = P_IDMOVIMIENTO;
     --Si no hay actuaciones de designa
     IF(v_contador=0) THEN
          select count (*)
           into v_contador
           from scs_actuacionasistencia actAsis
           where actAsis.IDINSTITUCION=P_IDINSTITUCION
                and actAsis.IDMOVIMIENTO = P_IDMOVIMIENTO;
                 --Si no hay actuaciones de asistencia
                 IF(v_contador=0) THEN
                           select count (*)
                           into v_contador
                           from scs_asistencia asistencias
                           where asistencias.IDINSTITUCION=P_IDINSTITUCION
                               and asistencias.IDMOVIMIENTO = P_IDMOVIMIENTO;
                           -- Si no hay asistencias
                           IF(v_contador=0) THEN
                                   select count (*)
                                   into v_contador
                                   from scs_cabeceraguardias guardias
                                  where guardias.IDINSTITUCION=P_IDINSTITUCION
                                   and guardias.IDMOVIMIENTO = P_IDMOVIMIENTO;
                                        --Si no hay guardias
                                         IF(v_contador=0) THEN
                                               -- Movimientos Varios
                                               v_informacion:='Movimientos varios';
                                               return v_informacion;
                                         --Si hay guardias
                                         ELSE
                                             select ('Guardia '||guardias.fechaInicio ||' en ' || guardiaTurno.Nombre ||' '|| turno.abreviatura) into v_informacion
                                             from scs_cabeceraguardias guardias, scs_guardiasturno guardiaTurno, scs_turno turno
                                             where guardias.IDINSTITUCION=P_IDINSTITUCION
                                              and guardias.IDMOVIMIENTO = P_IDMOVIMIENTO
                                              and guardiaTurno.idInstitucion= guardias.idInstitucion
                                              and guardiaTurno.idturno = guardias.idTurno
                                              and guardiaTurno.idGuardia = guardias.idGuardia
                                              and guardiaTurno.idInstitucion=turno.idInstitucion
                                              and guardiaTurno.idturno= turno.IdTurno;
                                              return v_informacion;
                                         END IF;
                           --Si hay asistencias
                         ELSE
                            --Retornamos la información
                            select ('Asistencia    '|| anio||'/'|| numero) into v_informacion
                            from scs_asistencia asistencia
                            where asistencia.IDINSTITUCION=P_IDINSTITUCION
                            and asistencia.IDMOVIMIENTO = P_IDMOVIMIENTO;
                            return v_informacion;

                         END IF;
                 --Si hay actuaciones de asistencia
                 ELSE
                  --Retornamos la información
                     select ('Actuación de asistencia    '|| actDesig.anio||'/'|| actDesig.numero||'/'|| actDesig.idActuacion) into v_informacion
                     from scs_actuacionasistencia actDesig
                     where actDesig.IDINSTITUCION=P_IDINSTITUCION
                      and actDesig.IDMOVIMIENTO = P_IDMOVIMIENTO;
                      return v_informacion;
                 END IF;
     --Si hay actuaciones de designa
     ELSE
       --Retornamos la información
        select ('Actuación de designación    '|| idTurno||'/'|| numero||'/'|| numeroAsunto) into v_informacion
         from scs_actuaciondesigna actDesig
         where actDesig.IDINSTITUCION=P_IDINSTITUCION
          and actDesig.IDMOVIMIENTO = P_IDMOVIMIENTO;
          return v_informacion;
      END IF;

END F_SIGA_MOVIMIENTOSVARIOS;

/