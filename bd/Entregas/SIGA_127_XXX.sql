Pkg_Siga_Fusion_Personas
V_pcajg_ejg

PROC_RESPUESTAEJG_2003

Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, si el certificado ya está cobrado rellene o bien Comentario, o bien Entidad; si no está cobrado rellene el Colegio al que se cobrará.' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 1;
Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, si el certificado ya está cobrado rellene o bien Comentario, o bien Entidad; si no está cobrado rellene el Colegio al que se cobrará.#CA' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 2;
Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, si el certificado ya está cobrado rellene o bien Comentario, o bien Entidad; si no está cobrado rellene el Colegio al que se cobrará.#EU' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 3;
Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, si el certificado ya está cobrado rellene o bien Comentario, o bien Entidad; si no está cobrado rellene el Colegio al que se cobrará.#GL' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 4;

delete FROM SCS_ESTADOEJG
 WHERE (IDINSTITUCION, IDTIPOEJG, ANIO, NUMERO) IN
       (SELECT ER.IDINSTITUCION, ER.IDTIPOEJG, ER.ANIO, ER.NUMERO
          FROM CAJG_EJGREMESA ER
         WHERE ER.IDREMESA = 2105
           AND ER.IDINSTITUCION = 2003)
   and trunc(fechamodificacion) = '22/02/2018';
delete CAJG_REMESAESTADOS ER
 where er.idremesa = 2105
   and er.idinstitucion = 2003
   and idestado in (1, 2, 3);

delete cajg_respuesta_ejgremesa ejgremesa
 where idejgremesa in (select idejgremesa
                         from CAJG_EJGREMESA
                        where idinstitucion = 2003
                          and idremesa = 2105);

update PCAJG_ALC_INT_CAB cab
   set Cab_Est_Id = 2
 where (cab.cab_ejg_idinstitucion, cab.cab_ejg_anio, cab.cab_ejg_idtipo,
        cab.cab_ejg_numero) in
       (select e.idinstitucion, e.anio, e.idtipoejg, e.numero
          from CAJG_EJGREMESA e
         where idinstitucion = 2003
           and idremesa = 2105)
   and trunc(cab_fechaenvio) > '21/02/2018';


-- Ejecutados en Integracion por AAG el 05/03/2018 a las 18:35
-- 127_003:

   ALTER TABLE CER_SOLICITUDCERTIFICADOS
  add constraint FK_CER_SOLICITUD_METODOSOL foreign key (IDMETODOSOLICITUD)
  references PYS_METODOSOLICITUD (IDMETODOSOLICITUD)
  deferrable;

INSERT INTO GEN_RECURSOS_CATALOGOS (IDRECURSO,DESCRIPCION,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,NOMBRETABLA,CAMPOTABLA,IDRECURSOALIAS)
VALUES('131005', 'Telemática',1,sysdate,0,null,'PYS_METODOSOLICITUD','DESCRIPCION', 'pys_metodosolicitud.descripcion.0.5');
INSERT INTO GEN_RECURSOS_CATALOGOS (IDRECURSO,DESCRIPCION,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,NOMBRETABLA,CAMPOTABLA,IDRECURSOALIAS)
VALUES('131005', 'Telemàtica',2,sysdate,0,null,'PYS_METODOSOLICITUD','DESCRIPCION', 'pys_metodosolicitud.descripcion.0.5');
INSERT INTO GEN_RECURSOS_CATALOGOS (IDRECURSO,DESCRIPCION,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,NOMBRETABLA,CAMPOTABLA,IDRECURSOALIAS)
VALUES('131005', 'Telemática#EU',3,sysdate,0,null,'PYS_METODOSOLICITUD','DESCRIPCION', 'pys_metodosolicitud.descripcion.0.5');
INSERT INTO GEN_RECURSOS_CATALOGOS (IDRECURSO,DESCRIPCION,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,NOMBRETABLA,CAMPOTABLA,IDRECURSOALIAS)
VALUES('131005', 'Telemática#GL',4,sysdate,0,null,'PYS_METODOSOLICITUD','DESCRIPCION', 'pys_metodosolicitud.descripcion.0.5');

INSERT INTO PYS_METODOSOLICITUD (IDMETODOSOLICITUD,DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION,CODIGOEXT,BLOQUEADO)
VALUES(5,'131005',sysdate,0,'5','N');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.residencia', 'Residencia de Incorporación', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.residencia', 'Residencia de Incorporación#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.residencia', 'Residencia de Incorporación#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.residencia', 'Residencia de Incorporación#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoIncorporacion', 'Estado de incorporación', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoIncorporacion', 'Estado de incorporación#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoIncorporacion', 'Estado de incorporación#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoIncorporacion', 'Estado de incorporación#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.solicitudOriginal', 'Solicitud original', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.solicitudOriginal', 'Solicitud original#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.solicitudOriginal', 'Solicitud original#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.solicitudOriginal', 'Solicitud original#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.datosCertificado', 'Datos Certificado', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.datosCertificado', 'Datos Certificado#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.datosCertificado', 'Datos Certificado#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.datosCertificado', 'Datos Certificado#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.situacionColegial', 'Situación colegial', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.situacionColegial', 'Situación colegial#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.situacionColegial', 'Situación colegial#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.situacionColegial', 'Situación colegial#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.textoConformidad2', 'Acepta ceder sus datos a la Abogacía', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.textoConformidad2', 'Acepta ceder sus datos a la Abogacía#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.textoConformidad2', 'Acepta ceder sus datos a la Abogacía#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.textoConformidad2', 'Acepta ceder sus datos a la Abogacía#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosCertifOriginal', 'Datos Certificado Original', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosCertifOriginal', 'Datos Certificado Original#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosCertifOriginal', 'Datos Certificado Original#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosCertifOriginal', 'Datos Certificado Original#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.postalYProvincia', 'Código postal y Provincia', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.postalYProvincia', 'Código postal y Provincia#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.postalYProvincia', 'Código postal y Provincia#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.postalYProvincia', 'Código postal y Provincia#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.numSolicitudColegiacion', 'Núm. Solicitud Colegiación', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.numSolicitudColegiacion', 'Núm. Solicitud Colegiación#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.numSolicitudColegiacion', 'Núm. Solicitud Colegiación#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.numSolicitudColegiacion', 'Núm. Solicitud Colegiación#GL', 0, '4', sysdate, 0, '19');

insert into GEN_PROCESOS (IDPROCESO, IDMODULO, TRAZA, TARGET, FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION, TRANSACCION, IDPARENT, NIVEL)
values ('61a', 'CER', 1, 'Y', SYSDATE, 0, 'Datos Certificado', 'CER_DetalleSolicitud', '61', 10);
insert into GEN_PROCESOS (IDPROCESO, IDMODULO, TRAZA, TARGET, FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION, TRANSACCION, IDPARENT, NIVEL)
values ('61b', 'CER', 1, 'Y', SYSDATE, 0, 'Solicitud Original', 'CER_SolicitudOriginal', '61', 10);
insert into GEN_PESTANAS (IDPROCESO, IDLENGUAJE, IDRECURSO, POSICION, IDGRUPO) 
values('61a',1, 'pestana.certificados.datosCertificado', 1, 'DATOSCERT');
insert into GEN_PESTANAS (IDPROCESO, IDLENGUAJE, IDRECURSO, POSICION, IDGRUPO) 
values('61b',1, 'pestana.certificados.solicitudOriginal', 2, 'DATOSCERT');

insert into ADM_TIPOSACCESO (IDPROCESO,IDPERFIL,FECHAMODIFICACION,USUMODIFICACION,DERECHOACCESO,IDINSTITUCION)
select '61a' as IDPROCESO, acc.IDPERFIL, sysdate as FECHAMODIFICACION, 0 as USUMODIFICACION, acc.DERECHOACCESO, acc.IDINSTITUCION 
from adm_tiposacceso acc where acc.idproceso = '61' and idinstitucion = 2000;

insert into ADM_TIPOSACCESO (IDPROCESO,IDPERFIL,FECHAMODIFICACION,USUMODIFICACION,DERECHOACCESO,IDINSTITUCION)
select '61b' as IDPROCESO, acc.IDPERFIL, sysdate as FECHAMODIFICACION, 0 as USUMODIFICACION, acc.DERECHOACCESO, acc.IDINSTITUCION 
from adm_tiposacceso acc where acc.idproceso = '61' and idinstitucion = 2000;


Pkg_Siga_Fusion_Personas

Declare
  Cursor c_nocolegiados Is Select col.idpersona, col.idinstitucion
    From Cen_Colegiado Col, Cen_Nocolegiado Noc
   Where Col.Idpersona = Noc.Idpersona
     And Noc.Idinstitucion = 2000 For Update;
  v_Codretorno Varchar2(4000);
  v_Datoserror Varchar2(4000);
Begin
  For r_noc In c_nocolegiados Loop
    Pkg_Siga_Censo.Actualizardatosletrado(r_noc.idpersona,
                                          r_noc.idinstitucion,
                                          '30',
                                          1,
                                          '-7',
                                          v_Codretorno,
                                          v_Datoserror);
  End Loop;
End;
/

PKG_SIGA_FACTURACION_SJCS

-- Ejecutados en Integracion por AAG el 12/03/2018 a las 11:05

DELETE FROM PCAJG_ALC_INT_CAB CAB
 where (cab.cab_ejg_idinstitucion, cab.cab_ejg_anio, cab.cab_ejg_idtipo,
        cab.cab_ejg_numero) in
       (select e.idinstitucion, e.anio, e.idtipoejg, e.numero
          from CAJG_EJGREMESA e
         where idinstitucion = 2003
           and idremesa = 2122)
   and trunc(cab_fechaenvio) > '8/03/2018';
   --(398)

DELETE

FROM PCAJG_ALC_INT_EXP
 WHERE EXP_INTERCAMBIO_ID IN
       (select cab.cab_intercambio_id
          from PCAJG_ALC_INT_CAB cab
         where (cab.cab_ejg_idinstitucion, cab.cab_ejg_anio,
                cab.cab_ejg_idtipo, cab.cab_ejg_numero) in
               (select e.idinstitucion, e.anio, e.idtipoejg, e.numero
                  from CAJG_EJGREMESA e
                 where idinstitucion = 2003
                   and idremesa = 2122)
           and trunc(cab_fechaenvio) > '8/03/2018');
--(398)
DELETE FROM PCAJG_ALC_INT_PRD
 WHERE prd_INTERCAMBIO_ID IN
       (select cab.cab_intercambio_id
          from PCAJG_ALC_INT_CAB cab
         where (cab.cab_ejg_idinstitucion, cab.cab_ejg_anio,
                cab.cab_ejg_idtipo, cab.cab_ejg_numero) in
               (select e.idinstitucion, e.anio, e.idtipoejg, e.numero
                  from CAJG_EJGREMESA e
                 where idinstitucion = 2003
                   and idremesa = 2122)
           and trunc(cab_fechaenvio) > '8/03/2018');
--(398)
DELETE FROM PCAJG_ALC_INT_PRJ
 WHERE prj_INTERCAMBIO_ID IN
       (select cab.cab_intercambio_id
          from PCAJG_ALC_INT_CAB cab
         where (cab.cab_ejg_idinstitucion, cab.cab_ejg_anio,
                cab.cab_ejg_idtipo, cab.cab_ejg_numero) in
               (select e.idinstitucion, e.anio, e.idtipoejg, e.numero
                  from CAJG_EJGREMESA e
                 where idinstitucion = 2003
                   and idremesa = 2122)
           and trunc(cab_fechaenvio) > '8/03/2018');
--(398)
DELETE FROM PCAJG_ALC_INT_SOL
 WHERE sol_INTERCAMBIO_ID IN
       (select cab.cab_intercambio_id
          from PCAJG_ALC_INT_CAB cab
         where (cab.cab_ejg_idinstitucion, cab.cab_ejg_anio,
                cab.cab_ejg_idtipo, cab.cab_ejg_numero) in
               (select e.idinstitucion, e.anio, e.idtipoejg, e.numero
                  from CAJG_EJGREMESA e
                 where idinstitucion = 2003
                   and idremesa = 2122)
           and trunc(cab_fechaenvio) > '8/03/2018');
--(398)
DELETE FROM PCAJG_ALC_INT_DOM
 WHERE dom_INTERCAMBIO_ID IN
       (select cab.cab_intercambio_id
          from PCAJG_ALC_INT_CAB cab
         where (cab.cab_ejg_idinstitucion, cab.cab_ejg_anio,
                cab.cab_ejg_idtipo, cab.cab_ejg_numero) in
               (select e.idinstitucion, e.anio, e.idtipoejg, e.numero
                  from CAJG_EJGREMESA e
                 where idinstitucion = 2003
                   and idremesa = 2122)
           and trunc(cab_fechaenvio) > '8/03/2018');
--(398)
DELETE FROM PCAJG_ALC_INT_ECO
 WHERE Eco_INTERCAMBIO_ID IN
       (select cab.cab_intercambio_id
          from PCAJG_ALC_INT_CAB cab
         where (cab.cab_ejg_idinstitucion, cab.cab_ejg_anio,
                cab.cab_ejg_idtipo, cab.cab_ejg_numero) in
               (select e.idinstitucion, e.anio, e.idtipoejg, e.numero
                  from CAJG_EJGREMESA e
                 where idinstitucion = 2003
                   and idremesa = 2122)
           and trunc(cab_fechaenvio) > '8/03/2018');
--(398)		   
		   delete FROM SCS_ESTADOEJG
 WHERE (IDINSTITUCION, IDTIPOEJG, ANIO, NUMERO) IN
       (SELECT ER.IDINSTITUCION, ER.IDTIPOEJG, ER.ANIO, ER.NUMERO
          FROM CAJG_EJGREMESA ER
         WHERE ER.IDREMESA = 2122
           AND ER.IDINSTITUCION = 2003)
           and idestadoejg = 9
      and trunc(fechamodificacion) >'08/03/2018';
      --(398)
delete CAJG_REMESAESTADOS ER
 where er.idremesa = 2122
   and er.idinstitucion = 2003
   and idestado in (1, 2, 3);
   --(3)

-- Esto se ha ejecutado a mano en PRO (porque son errores)
   
-- 127_004:
update gen_parametros set valor = 10 where idinstitucion = 2000 and parametro = 'MAXIMO_DIAS_ANTELACION_SOLICITUD';



---- TRAMITE NUEVA FORTUNA ----
insert into pcajg_tipo_resolucion (identificador, codigo, descripcion, abreviatura, fechamodificacion, usumodificacion, idinstitucion)
select 12, 'MF', 'Declarar millor fortuna', null, sysdate, 0, i.idinstitucion
from cen_institucion i where i.cen_inst_idinstitucion = 3001;

insert into pcajg_tipo_resolucion_scstipor (identificador, idinstitucion, idtiporesolucion, fechamodificacion, usumodificacion)
select 12, i.idinstitucion, 7, sysdate, 0
from cen_institucion i where i.cen_inst_idinstitucion = 3001;


insert into scs_tipofundamentos (idfundamento, codigo, descripcion, fechamodificacion, usumodificacion, idinstitucion, bloqueado, textoplantilla, idtiporesolucion, textoplantilla2, textoplantilla3, textoplantilla4, fechabaja, codigoejis)
select (select max(idfundamento)+1 from scs_tipofundamentos t where t.idinstitucion = i.idinstitucion), '41CD', 'arxiu causes diverses', sysdate, 0, i.idinstitucion, 'S', null, 7, null, null, null, null, null
from cen_institucion i where i.cen_inst_idinstitucion = 3001;
insert into scs_tipofundamentos (idfundamento, codigo, descripcion, fechamodificacion, usumodificacion, idinstitucion, bloqueado, textoplantilla, idtiporesolucion, textoplantilla2, textoplantilla3, textoplantilla4, fechabaja, codigoejis)
select (select max(idfundamento)+1 from scs_tipofundamentos t where t.idinstitucion = i.idinstitucion), '41NA', 'arxiu per no acreditar els requisits de l''article 36.2', sysdate, 0, i.idinstitucion, 'S', null, 7, null, null, null, null, null
from cen_institucion i where i.cen_inst_idinstitucion = 3001;
insert into scs_tipofundamentos (idfundamento, codigo, descripcion, fechamodificacion, usumodificacion, idinstitucion, bloqueado, textoplantilla, idtiporesolucion, textoplantilla2, textoplantilla3, textoplantilla4, fechabaja, codigoejis)
select (select max(idfundamento)+1 from scs_tipofundamentos t where t.idinstitucion = i.idinstitucion), '41NC', 'arxiu per no complir els requisits de l''article 36.2', sysdate, 0, i.idinstitucion, 'S', null, 7, null, null, null, null, null
from cen_institucion i where i.cen_inst_idinstitucion = 3001;
insert into scs_tipofundamentos (idfundamento, codigo, descripcion, fechamodificacion, usumodificacion, idinstitucion, bloqueado, textoplantilla, idtiporesolucion, textoplantilla2, textoplantilla3, textoplantilla4, fechabaja, codigoejis)
select (select max(idfundamento)+1 from scs_tipofundamentos t where t.idinstitucion = i.idinstitucion), '41D', 'desistiment', sysdate, 0, i.idinstitucion, 'S', null, 7, null, null, null, null, null
from cen_institucion i where i.cen_inst_idinstitucion = 3001;     
insert into scs_tipofundamentos (idfundamento, codigo, descripcion, fechamodificacion, usumodificacion, idinstitucion, bloqueado, textoplantilla, idtiporesolucion, textoplantilla2, textoplantilla3, textoplantilla4, fechabaja, codigoejis)
select (select max(idfundamento)+1 from scs_tipofundamentos t where t.idinstitucion = i.idinstitucion), '65', 'Declaració millor fortuna per superar els llindars de l''article 36.2', sysdate, 0, i.idinstitucion, 'S', null, 7, null, null, null, null, null
from cen_institucion i where i.cen_inst_idinstitucion = 3001;
insert into scs_tipofundamentos (idfundamento, codigo, descripcion, fechamodificacion, usumodificacion, idinstitucion, bloqueado, textoplantilla, idtiporesolucion, textoplantilla2, textoplantilla3, textoplantilla4, fechabaja, codigoejis)
select (select max(idfundamento)+1 from scs_tipofundamentos t where t.idinstitucion = i.idinstitucion), '65', 'Declaració millor fortuna per superar els llindars de l''article 36.2', sysdate, 0, i.idinstitucion, 'S', null, 7, null, null, null, null, null
from cen_institucion i where i.cen_inst_idinstitucion = 3001;  
insert into scs_tipofundamentos (idfundamento, codigo, descripcion, fechamodificacion, usumodificacion, idinstitucion, bloqueado, textoplantilla, idtiporesolucion, textoplantilla2, textoplantilla3, textoplantilla4, fechabaja, codigoejis)
select (select max(idfundamento)+1 from scs_tipofundamentos t where t.idinstitucion = i.idinstitucion), '66', 'Declaració millor fortuna per canvi substancial de circumstàncies que van motivar el reconeixement', sysdate, 0, i.idinstitucion, 'S', null, 7, null, null, null, null, null
from cen_institucion i where i.cen_inst_idinstitucion = 3001;      
insert into scs_tipofundamentos (idfundamento, codigo, descripcion, fechamodificacion, usumodificacion, idinstitucion, bloqueado, textoplantilla, idtiporesolucion, textoplantilla2, textoplantilla3, textoplantilla4, fechabaja, codigoejis)
select (select max(idfundamento)+1 from scs_tipofundamentos t where t.idinstitucion = i.idinstitucion), '28', 'Inadmetre a tràmit petició extemporània', sysdate, 0, i.idinstitucion, 'S', null, 7, null, null, null, null, null
from cen_institucion i where i.cen_inst_idinstitucion = 3001; 



Declare
  p_Codretorno Varchar2(4000);
  p_Datoserror Varchar2(4000);
Begin
  Update Gen_Catalogos_Multiidioma
     Set Migrado = 'N'
   Where Nombretabla = 'SCS_TIPOFUNDAMENTOS'; --Marcar la tabla como No traducida
  Proc_Act_Recursos(p_Codretorno, p_Datoserror);
  Dbms_Output.Put_Line(p_Codretorno || ': ' || p_Datoserror);
End;

--MODIFICADA VISTA V_PCAJG_EJG PARA SETEAR VERSIÓN PCAJG GENERALITAT NUEVA


commit;

---- FIN TRAMITE NUEVA FORTUNA ----


-- Ejecutados en Integracion por ACP el 06/04/2018 a las 13:17

-- Add/modify columns 
alter table SCS_DESIGNA add factconvenio number(1) default 0;
-- Add comments to the columns 
comment on column SCS_DESIGNA.factconvenio
  is 'Campo no obligatorio que indica si se va a pagar por convenio';
  
-- Ejecutados en Integracion por AAG el 09/04/2018 a las 10:55
  --ALCALA
alter table FCS_FACT_GRUPOFACT_HITO add factconvenio number(1);
alter paquete PKG_SIGA_FACTURACION_SJCS
  
-- Ejecutados en Integracion por AAG el 16/04/2018 a las 17:05

-- 127_006

PKG_SIGA_FACTURACION_SJCS

INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) VALUES ( 'menu.sjcs.facturacion.envioReintegrosXunta', 'Envío reintegros Xunta', '0', '1', SYSDATE, '0', '19' );
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) VALUES ( 'menu.sjcs.facturacion.envioReintegrosXunta', 'Envío reintegros Xunta#CA', '0', '2', SYSDATE, '0', '19' );
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) VALUES ( 'menu.sjcs.facturacion.envioReintegrosXunta', 'Envío reintegros Xunta#EU', '0', '3', SYSDATE, '0', '19' ); 
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) VALUES ( 'menu.sjcs.facturacion.envioReintegrosXunta', 'Envío reintegros Xunta#GL', '0', '4', SYSDATE, '0', '19' );
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) VALUES ( 'factSJCS.envioReintegrosXunta.localizacion', 'SJCS > Facturación SJCS', '0', '1', SYSDATE, '0', '19' ); 
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) VALUES ( 'factSJCS.envioReintegrosXunta.localizacion', 'SJCS > Facturación SJCS#CA', '0', '2', SYSDATE, '0', '19' );
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) VALUES ( 'factSJCS.envioReintegrosXunta.localizacion', 'SJCS > Facturación SJCS#EU', '0', '3', SYSDATE, '0', '19' );
INSERT INTO GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) VALUES ( 'factSJCS.envioReintegrosXunta.localizacion', 'SJCS > Facturación SJCS#GL', '0', '4', SYSDATE, '0', '19' );

-- Ejecutados en Integracion por AAG el 07/05/2018 a las 12:05

-- 127_007

alter table SCS_TURNO add IDJURISDICCION number(3);
alter table SCS_TURNO add constraint SCS_TURNO_JURISDICCION_FK foreign key (IDJURISDICCION) references SCS_JURISDICCION (IDJURISDICCION);

coger la vista V_PCAJG_EJG

coger la vista V_PCAJG_CONTRARIOS


insert into GEN_PROPERTIES (FICHERO, PARAMETRO, VALOR) values ('SIGA', 'mail.smtp.sesion', 'CorreoSIGA');

-- Ejecutados en Integracion por AAG el 05/09/2018 a las 13:30

-- 127_011

PKG_SIGA_REGULARIZACION_SJCS

-- Ejecutados en Integracion por AAG el 19/09/2018 a las 13:30

-- 127_012
--R1809_0
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosExistentes', 'Datos existentes en el sistema', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosExistentes', 'Datos existentes en el sistema#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosExistentes', 'Datos existentes en el sistema#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosExistentes', 'Datos existentes en el sistema#GL', 0, '4', sysdate, 0, '19');
update gen_recursos set descripcion = 'Datos solicitud' where idrecurso = 'pestana.certificados.datosCertificado';
update gen_recursos set descripcion = 'Validación de datos' where idrecurso = 'pestana.certificados.solicitudOriginal';
commit;

-- Ejecutados en Integracion por FMS el 26/09/2018 a las 09:30
TRIGGER PYS_PRODUCTOSSOLICITADOS_AI
-- Ejecutados en Integracion por FMS el 26/09/2018 a las 11:30

alter table PYS_PRODUCTOSSOLICITADOS add OBSERVACIONES varchar2(200);

-- Ejecutados en Integracion por FMS el 26/09/2018 a las 16:30

-- 127_014

-- Add/modify columns 
alter table SCS_ACTUACIONDESIGNA add FACTCONVENIO NUMBER(1) default 0;

update scs_actuaciondesigna a
   set a.factconvenio = (select d.factconvenio
                           from scs_designa d
                          where d.idinstitucion = a.idinstitucion
                            and d.idturno = a.idturno
                            and d.anio = a.anio
                            and d.numero = a.numero)
  where a.idinstitucion = 2003;
  
create index SI_CEN_HISTORICO_FECHA on CEN_HISTORICO (fechaefectiva);

/*
 * NUEVOS CAMPOS TABLA CEN_DATOS
 * EJECUTADOS EN INTEGRACION POR FMS EL 03/10/2018 
 */
alter table ECOM_CEN_DATOS add IDINSTITUCIONRESIDENCIA number(4);
alter table ECOM_CEN_DATOS add ANIOLICENCIATURA varchar2(4);

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pys.solicitudCompra.literal.residenteen', 'Residente en', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pys.solicitudCompra.literal.residenteen', 'Residente en#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pys.solicitudCompra.literal.residenteen', 'Residente en#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pys.solicitudCompra.literal.residenteen', 'Residente en#GL', 0, '4', sysdate, 0, '19');

alter table PYS_PRODUCTOSSOLICITADOS add IDCENSODATOS NUMBER(10);
alter table PYS_PRODUCTOSSOLICITADOS
  add constraint FK_PRODUCTOSSOLIC_CENDATOS foreign key (IDCENSODATOS)
  references ecom_cen_datos (IDCENSODATOS)
  deferrable;

alter table CER_SOLICITUDCERTIFICADOS add IDCENSODATOS NUMBER(10);
 
alter table CER_SOLICITUDCERTIFICADOS
  add constraint FK_CER_SOLICITUD_CENSODATOS foreign key (IDCENSODATOS)
  references ecom_cen_datos (IDCENSODATOS)
  deferrable;

-- Modifico el trigger para añadir los nuevos campos
CREATE OR REPLACE TRIGGER PYS_PRODUCTOSSOLICITADOS_AI


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.datosDireccion.literal.anioLicenciatura', 'Año de Licenciatura', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.datosDireccion.literal.anioLicenciatura', 'Año de Licenciatura#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.datosDireccion.literal.anioLicenciatura', 'Año de Licenciatura#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.datosDireccion.literal.anioLicenciatura', 'Año de Licenciatura#GL', 0, '4', sysdate, 0, '19');



/*
 * 
 * EJECUTADO HASTA AQUÍ EN INTEGRACION POR FMS EL 04/10/2018 
 */

-- Ejecutados en Integracion por AAG (bueno lo que no ejecuto ya FMS) el 10/10/2018 a las 10:30

-- 127_015

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.descargardocumento', 'Descargar Documento', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.descargardocumento', 'Descargar Documento#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.descargardocumento', 'Descargar Documento#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.descargardocumento', 'Descargar Documento#GL', 0, '4', sysdate, 0, '19');

-- Ejecutados en Integracion por FMS el 23/10/2018 a las 13:30

alter table SCS_PERSONAJG add ASISTIDOSOLICITAJG VARCHAR2(1);
alter table SCS_PERSONAJG add ASISTIDOAUTORIZAEEJG VARCHAR2(1);
alter table SCS_PERSONAJG add AUTORIZAAVISOTELEMATICO VARCHAR2(1);


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.solicitajg', 'Solicita Justicia gratuita:', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.solicitajg', 'Solicita Justicia gratuita:#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.solicitajg', 'Solicita Justicia gratuita:#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.solicitajg', 'Solicita Justicia gratuita:#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.autorizaavisotel', 'Autoriza avisos telemáticos:', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.autorizaavisotel', 'Autoriza avisos telemáticos:#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.autorizaavisotel', 'Autoriza avisos telemáticos:#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.autorizaavisotel', 'Autoriza avisos telemáticos:#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.autorizaeejg', 'Autoriza recabar información de la administracion:', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.autorizaeejg', 'Autoriza recabar información de la administracion:#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.autorizaeejg', 'Autoriza recabar información de la administracion:#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.autorizaeejg', 'Autoriza recabar información de la administracion:#GL', 0, '4', sysdate, 0, '19');

coger V_PCAJG_EJG
coger V_PCAJG_M_DPERSONA

-- Ejecutados en Integracion por FMS el 05/11/2018 a las 17:15

coger f_comunicaciones_ejg_2003_CAB

-- Ejecutados en Integracion por FMS el 06/11/2018 a las 12:51

 jbd -- ejecutado unicamente en local
delete from adm_tiposacceso where idproceso in ('11b','1b','21b');
delete from gen_procesos where idproceso in ('11b','1b','21b');