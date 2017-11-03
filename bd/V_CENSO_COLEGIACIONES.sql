create materialized view V_CENSO_COLEGIACIONES
Tablespace TS_SIGA_CENSO
refresh force on demand
as
select cen_colegiado.idpersona id_letrado,
       cen_colegiado.idinstitucion as id_colegio,
       cen_institucion.abreviatura as descripcion,
       cen_colegiado.ncolegiado as num_colegiado,
       replace(replace(cen_colegiado.situacionresidente, 1, 's'), 0, 'n') as residencia,
       decode((select 1
                from CEN_SANCION
               where idtiposancion in (4, 6, 7)
                 and Cen_Sancion.Idpersona = cen_cliente.idpersona
                 And Nvl(Cen_Sancion.Chkrehabilitado, '0') = '0'
                 And Trunc(Nvl(Cen_Sancion.Fecharehabilitado, '31/12/9999')) >= Trunc(Sysdate)
                 And Cen_Sancion.Fechainicio is not null
                 And Cen_Sancion.Fechafin is not Null
                 And Trunc(Sysdate) Between Trunc(Cen_Sancion.Fechainicio) And Trunc(Cen_Sancion.Fechafin)
                 and rownum = 1),
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
   and cen_colegiado.idinstitucion = cen_datoscolegialesestado.idinstitucion
   and cen_datoscolegialesestado.fechaestado =
       (select max(est.fechaestado)
          from CEN_DATOSCOLEGIALESESTADO est
         where est.idinstitucion = cen_colegiado.idinstitucion
           and est.idpersona = cen_colegiado.idpersona
           and trunc(est.fechaestado) <= trunc(sysdate))
   and cen_datoscolegialesestado.idestado In (10, 20)
   And cen_colegiado.Comunitario = '0'
   and cen_colegiado.idinstitucion <> 2000
   and cen_colegiado.idinstitucion < 3000
