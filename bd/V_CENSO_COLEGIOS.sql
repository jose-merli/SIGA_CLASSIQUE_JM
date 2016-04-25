create materialized view USCGAE.V_CENSO_COLEGIOS
refresh force on demand
as
select cen_institucion.idinstitucion as id_colegio,
       cen_institucion.abreviatura as descripcion,
       cen_institucion.nombre as nombrelargo,
       cen_direcciones.domicilio as direccion,
       cen_direcciones.telefono1 as telefono,
       cen_direcciones.fax1 as fax,
       cen_institucion.fechamodificacion as fec_modif,
       cen_institucion.cen_inst_idinstitucion as idpadre,
       cen_institucion.codigoext as codigoexterno,
       cen_direcciones.correoelectronico as email,
       decode(fechaenproduccion, null, sysdate, fechaenproduccion) fechaValidez,
       nvl((select max(e.fechamodificacion) from ecom_cen_ws_envio e where e.idestadoenvio = 2 and e.idtipoenvio = 1 and e.idinstitucion = cen_institucion.idinstitucion),
                   nvl((select max(e.fechaexportacion) from ecom_cen_ws_envio e where e.idestadoenvio = 2 and e.idtipoenvio = 0 and e.idinstitucion = cen_institucion.idinstitucion), sysdate)) as fechaSincro             
  from CEN_INSTITUCION, CEN_DIRECCIONES
 where cen_institucion.idinstitucion = cen_direcciones.idinstitucion
   and cen_institucion.idpersona = cen_direcciones.idpersona
   and cen_institucion.idinstitucion <> 3500
   and cen_direcciones.iddireccion =
       (select min(iddireccion)
          from CEN_DIRECCIONES D
         where d.idinstitucion = cen_institucion.idinstitucion
           and d.idpersona = cen_institucion.idpersona
           and d.fechabaja is null)
union
select cen_institucion.idinstitucion as id_colegio,
       cen_institucion.abreviatura as descripcion,
       cen_institucion.nombre as nombrelargo,
       cen_direcciones.domicilio as direccion,
       cen_direcciones.telefono1 as telefono,
       cen_direcciones.fax1 as fax,
       cen_institucion.fechamodificacion as fec_modif,
       cen_institucion.cen_inst_idinstitucion as colegio_cgae,
       cen_institucion.codigoext as codigoexterno,
       cen_direcciones.correoelectronico as email,
       decode(fechaenproduccion, null, sysdate, fechaenproduccion) fechaValidez,
       nvl((select max(e.fechamodificacion) from ecom_cen_ws_envio e where e.idestadoenvio = 2 and e.idtipoenvio = 1 and e.idinstitucion = cen_institucion.idinstitucion),
              nvl((select max(e.fechaexportacion) from ecom_cen_ws_envio e where e.idestadoenvio = 2 and e.idtipoenvio = 0 and e.idinstitucion = cen_institucion.idinstitucion), sysdate)) as fechaSincro
  from CEN_INSTITUCION, CEN_DIRECCIONES
 where cen_institucion.idinstitucion = cen_direcciones.idinstitucion(+)
   and cen_institucion.idpersona = cen_direcciones.idpersona(+)
   and cen_institucion.idinstitucion <> 3500
   and not exists
 (select iddireccion
          from CEN_DIRECCIONES D
         where d.idinstitucion = cen_institucion.idinstitucion
           and d.idpersona = cen_institucion.idpersona
           and d.fechabaja is null)
