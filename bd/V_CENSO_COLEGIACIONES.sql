create materialized view USCGAE.V_CENSO_COLEGIACIONES
refresh force on demand
as
select cen_colegiado.idpersona id_letrado,
       cen_colegiado.idinstitucion as id_colegio,
       cen_institucion.abreviatura as descripcion,
       decode(cen_colegiado.comunitario,
              '1',
              cen_colegiado.ncomunitario,
              cen_colegiado.ncolegiado) as num_colegiado,
       replace(replace(cen_colegiado.situacionresidente, 1, 's'), 0, 'n') as residencia,
       decode((select 1
                from CEN_SANCION
               where idtiposancion in (7)
                 and rownum = 1
                 and cen_sancion.idpersona = cen_cliente.idpersona
                 And cen_sancion.Fechainicio is not null 
                 And cen_sancion.Fechafin is not Null
                 And Trunc(Sysdate) Between Trunc(cen_sancion.Fechainicio) And Trunc(cen_sancion.Fechafin)),
              1,
              'n',
              replace(replace(cen_datoscolegialesestado.idestado, 10, 'n'),
                      20,
                      's')) as ejerciente,
       trunc(F_SIGA_OBTENERFECHAACTUALIZ(cen_colegiado.idinstitucion,cen_colegiado.idpersona)) as fecha_actualizacion,
       trunc(cen_colegiado.fechaincorporacion) as fecha_alta,
       greatest(cen_colegiado.fechamodificacion,
                cen_cliente.fechamodificacion,
                cen_datoscolegialesestado.fechamodificacion) as fecha_modificacion,
       CEN_INSTITUCION.CEN_INST_IDINSTITUCION as colegio_cgae,
       CEN_CLIENTE.idtratamiento as tratamiento
  from CEN_CLIENTE,
       CEN_COLEGIADO,
       CEN_DATOSCOLEGIALESESTADO,
       CEN_INSTITUCION
 where cen_cliente.idpersona = cen_colegiado.idpersona
   and cen_cliente.idinstitucion = cen_colegiado.idinstitucion
   and cen_institucion.idinstitucion = cen_colegiado.idinstitucion
   and cen_colegiado.idpersona = cen_datoscolegialesestado.idpersona
   and cen_colegiado.idinstitucion =
       cen_datoscolegialesestado.idinstitucion
   and cen_datoscolegialesestado.fechaestado =
       (select max(fechaestado)
          from CEN_DATOSCOLEGIALESESTADO
         where cen_datoscolegialesestado.idinstitucion =
               cen_colegiado.idinstitucion
           and cen_datoscolegialesestado.idpersona = cen_colegiado.idpersona
           and trunc(cen_datoscolegialesestado.fechaestado) <=
               trunc(sysdate))
   and (cen_datoscolegialesestado.idestado = 10 or
       cen_datoscolegialesestado.idestado = 20)
   and cen_colegiado.idinstitucion <> 2000
   and cen_colegiado.idinstitucion < 3000
