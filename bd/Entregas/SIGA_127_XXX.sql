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
Ejecutados en integracion a fecha 06-11-2018 a las 14:02
alter table PCAJG_ALC_INT_SOL add SOL19_AUTORIZA_TELEM CHAR(1);
alter table PCAJG_ALC_INT_SOL add SOL20_DEMANDADO CHAR(1);
alter table PCAJG_ALC_INT_EXP add EXP15_DOC_ADICIONAL CHAR(1);
alter table PCAJG_ALC_INT_EXP add EXP16_SOL_JG CHAR(1);


 jbd -- ejecutado unicamente en local
delete from adm_tiposacceso where idproceso in ('11b','1b','21b');
delete from gen_procesos where idproceso in ('11b','1b','21b');

update env_camposenvios ce set ce.valor = replace(ce.valor, '%%', '##') where 1=1 
and ce.idcampo in (1, 2) and ce.tipocampo in ('S','E');

update env_camposplantilla ce
   set ce.valor = replace(ce.valor, '%%', '##')
 where 1 = 1
   and ce.idcampo in (1, 2)
   and ce.tipocampo in ('S', 'E');

commit;

update gen_recursos rec 
set rec.descripcion= replace(rec.descripcion,'%%','##')
where rec.idrecurso='envios.ayuda.campos';

update gen_recursos rec 
set rec.descripcion= replace(rec.descripcion,'%%','##')
where rec.idrecurso='envios.ayuda.imagenes';


-- a paticion de goyo añadimos codigoext a la institucion 2000
update cen_institucion set codigoext = 'AC0000' where idinstitucion = 2000;

-- Ejecutados en Integracion por AAG el 29/11/2018 a las 09:45

-- SIGA_127_018

PKG_SIGA_PAGOS_SJCS

-- Ejecutados en Integracion por AAG el 17/12/2018 a las 10:05

Update CEN_NOCOLEGIADO Set SOCIEDADPROFESIONAL = 0 Where SOCIEDADPROFESIONAL Is null;
alter table CEN_NOCOLEGIADO modify SOCIEDADPROFESIONAL default 0 not null;

-- Ejecutados en Integracion por AAG el 17/12/2018 a las 14:45

Actualizacion de contadores y de SMI del año 2019 en esta entrega

alter table PYS_PRODUCTOSSOLICITADOS add IDINSTITUCIONPETICION number(4);
coger PYS_PRODUCTOSSOLICITADOS_AI.trg
-- Ejecutados en Integración por FMS el 17/12/2018 a las 16:00

PYS_PRODUCTOSSOLICITADOS_AI.trg
alter table CEN_NOCOLEGIADO modify SOCIEDADPROFESIONAL default 0 null;

-- Ya Ejecutado en todos los entornos (aunque ha quedado pendiente el alter table en PRE)

-- SIGA_127_019

PKG_SIGA_FACTURACION_SJCS (ya ejecutado en todos los entornos por ser urgente)

update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='No está permitido la descarga de este informe económico porque el NIF de la persona para la que se solicitó no existe en la unidad familiar.' where idrecurso='gratuita.personaJG.tooltip.noPerteneceUnidadFam' and idlenguaje='1';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='No está permitido la descarga de este informe económico porque el NIF de la persona para la que se solicitó no existe en la unidad familiar.#CA' where idrecurso='gratuita.personaJG.tooltip.noPerteneceUnidadFam' and idlenguaje='2';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='No está permitido la descarga de este informe económico porque el NIF de la persona para la que se solicitó no existe en la unidad familiar.#EU' where idrecurso='gratuita.personaJG.tooltip.noPerteneceUnidadFam' and idlenguaje='3';
update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='No está permitido la descarga de este informe económico porque el NIF de la persona para la que se solicitó no existe en la unidad familiar.#GL' where idrecurso='gratuita.personaJG.tooltip.noPerteneceUnidadFam' and idlenguaje='4';

-- Ejecutados en Integración por FMS el 13/02/2019 a las 11:20

PYS_PRODUCTOSSOLICITADOS_AI.trg

-- INI - SOLO EJECUTAR EN PRE
Drop Sequence SEQ_ENV_ENVIOPROGRAMADO;
create sequence SEQ_ENV_ENVIOPROGRAMADO
minvalue 1
maxvalue 999999
start with 1
increment by 1
nocache
cycle;

alter table CEN_NOCOLEGIADO modify SOCIEDADPROFESIONAL null;
-- FIN - SOLO EJECUTAR EN PRE

-- Ejecutados en Integracion por AAG el 12/03/2019 a las 10:10

-- INI - SOLO EJECUTAR EN PRE
Drop Sequence SEQ_ENV_PROGRAMINFORMES;
create sequence SEQ_ENV_PROGRAMINFORMES
minvalue 1
maxvalue 999999
start with 1
increment by 1
nocache
cycle;
-- FIN - SOLO EJECUTAR EN PRE

-- Ejecutados en Integracion por AAG el 20/03/2019 a las 09:30

--Ejecutados en Integración por FMS el 20/05/2019
alter table SCS_TIPODOCUMENTOEJG modify CODIGOEXT VARCHAR2(20);
alter table SCS_DOCUMENTOEJG modify CODIGOEXT VARCHAR2(20);

Coger V_PCAJG_M_DOCUMENTACIONEXP.sql
Coger V_PCAJG_DOCUMENTACIONEXP_CAT.sql
Coger V_PCAJG_DOCUMENTACIONEXP_DS.sql

--BLOQUE PL/SQL para hacer carga inicial de tipos de documentos de intercambio obligatorios de cataluña
DECLARE 
       --Select de la lista de sedes
      CURSOR C_LISTA_SEDES is
      select idinstitucion 
      from cen_institucion 
      where cen_inst_idinstitucion=3001;
     V_REC VARCHAR2(50);
      V_DOC VARCHAR2(50);
        
BEGIN     
 insert into gen_recursos values ('scs.parametro.pcajg.generalitat.inclusionDOC', 'Indica si está o no activo el envío de la documentación asociada al EJG', 0, 1, sysdate, 0, 19);
 insert into gen_recursos values ('scs.parametro.pcajg.generalitat.inclusionDOC', 'Indica si está o no activo el envío de la documentación asociada al EJG#CA', 0, 2, sysdate, 0, 19);
 insert into gen_recursos values ('scs.parametro.pcajg.generalitat.inclusionDOC', 'Indica si está o no activo el envío de la documentación asociada al EJG#EU', 0, 3, sysdate, 0, 19);
 insert into gen_recursos values ('scs.parametro.pcajg.generalitat.inclusionDOC', 'Indica si está o no activo el envío de la documentación asociada al EJG#GL', 0, 4, sysdate, 0, 19);
 insert into gen_parametros values ('SCS', 'PCAJG_GENERALITAT_INCLUSION_DOC', '0', SYSDATE, 0, 0, 'scs.parametro.pcajg.generalitat.inclusionDOC', null);
 insert into gen_recursos values ('scs.parametro.pcajg.directorioIDOFtp', 'Directorio intercambio ficheros IDO', 0, 1, sysdate, 0, 19);
 insert into gen_recursos values ('scs.parametro.pcajg.directorioIDOFtp', 'Directorio intercambio ficheros IDO#CA', 0, 2, sysdate, 0, 19);
 insert into gen_recursos values ('scs.parametro.pcajg.directorioIDOFtp', 'Directorio intercambio ficheros IDO#EU', 0, 3, sysdate, 0, 19);
 insert into gen_recursos values ('scs.parametro.pcajg.directorioIDOFtp', 'Directorio intercambio ficheros IDO#GL', 0, 4, sysdate, 0, 19);
 
	SELECT MAX(IDTIPODOCUMENTOEJG)+1 into V_REC FROM scs_tipodocumentoejg WHERE IDINSTITUCION in (select idinstitucion from cen_institucion where cen_inst_idinstitucion=3001);
        dbms_output.put_line(V_REC);
        SELECT MAX(IDDOCUMENTOEJG)+1 INTO V_DOC FROM scs_documentoejg  WHERE IDINSTITUCION in (select idinstitucion from cen_institucion where cen_inst_idinstitucion=3001);
        --Iterar por las sedes
        FOR SEDE IN C_LISTA_SEDES LOOP
		-- inserto la ruta nueva para los ficheros ido
		insert into gen_parametros values ('SCS', 'PCAJG_FTP_DIRECTORIO_IDO', '/entrades/IDO', sysdate,0, SEDE.idinstitucion, 'scs.parametro.pcajg.directorioIDOFtp', null);
 
        --iNSERTO EN LA TABLA DE RECURSOS
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC, 'Documentación envío expedientes Generalitat', 1, sysdate, 0, SEDE.idinstitucion, 'SCS_TIPODOCUMENTOEJG', 'DESCRIPCION', 'scs_tipodocumentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC);
         insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC, 'Documentación envío expedientes Generalitat', 2, sysdate, 0, SEDE.idinstitucion, 'SCS_TIPODOCUMENTOEJG', 'DESCRIPCION', 'scs_tipodocumentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC, 'Documentación envío expedientes Generalitat', 3, sysdate, 0, SEDE.idinstitucion, 'SCS_TIPODOCUMENTOEJG', 'DESCRIPCION', 'scs_tipodocumentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC, 'Documentación envío expedientes Generalitat', 4, sysdate, 0, SEDE.idinstitucion, 'SCS_TIPODOCUMENTOEJG', 'DESCRIPCION', 'scs_tipodocumentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC);
        
         --iNSERTO EN LA TABLA DE TIPOD DE DOCUMENTO
         insert into scs_tipodocumentoejg (idinstitucion, idtipodocumentoejg, abreviatura, descripcion, codigoext, codigoejis) 
         values  (SEDE.idinstitucion , V_REC, 'Documentación envío expedientes Generalitat', '830'||SEDE.idinstitucion || V_REC, null, null);		 
         
          --iNSERTO EN LA TABLA DE RECURSOS
         insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Solicitud de la persona interesada / auto del órgano judicial', 1, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
         insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Solicitud de la persona interesada / auto del órgano judicial', 2, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Solicitud de la persona interesada / auto del órgano judicial', 3, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Solicitud de la persona interesada / auto del órgano judicial', 4, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
         -- iNSERTO EN LA TABLA DE DOCUMENTO
         
         insert into scs_documentoejg
           (idinstitucion, idtipodocumentoejg, iddocumentoejg, abreviatura, descripcion, codigoext, codigoejis)
         values
           (SEDE.idinstitucion, V_REC, V_DOC, 'Solicitud de EJG', '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'TR-DOCINISOL-01', NULL);        
           V_DOC:=V_DOC+1;
           
         --iNSERTO EN LA TABLA DE RECURSOS
         insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Documentación justificativa aportada', 1, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
         insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Documentación justificativa aportada', 2, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Documentación justificativa aportada', 3, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Documentación justificativa aportada', 4, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
        insert into scs_documentoejg
           (idinstitucion, idtipodocumentoejg, iddocumentoejg, abreviatura, descripcion, codigoext, codigoejis)
         values
           (SEDE.idinstitucion, V_REC, V_DOC, 'Documentación justificativa aportada', '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'TR-DOCINIJUS-01', NULL);
            V_DOC:=V_DOC+1;
         --iNSERTO EN LA TABLA DE RECURSOS
         insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Consulta ACA', 1, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
         insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Consulta ACA', 2, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Consulta ACA', 3, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Consulta ACA', 4, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
           
           insert into scs_documentoejg
           (idinstitucion, idtipodocumentoejg, iddocumentoejg, abreviatura, descripcion, codigoext, codigoejis)
         values
           (SEDE.idinstitucion, V_REC, V_DOC, 'Consulta ACA', '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'TR-DOCINIACA-01', NULL);
            V_DOC:=V_DOC+1;
          --iNSERTO EN LA TABLA DE RECURSOS
         insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Dictamen del colegio', 1, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
         insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Dictamen del colegio', 2, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Dictamen del colegio', 3, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Dictamen del colegio', 4, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
              
          insert into scs_documentoejg
           (idinstitucion, idtipodocumentoejg, iddocumentoejg, abreviatura, descripcion, codigoext, codigoejis)
         values
           (SEDE.idinstitucion, V_REC, V_DOC, 'Dictamen del colegio', '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'TR-DOCINIDIC-01', NULL);
            V_DOC:=V_DOC+1;
            --iNSERTO EN LA TABLA DE RECURSOS
         insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Documento de designa', 1, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
         insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Documento de designa', 2, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Documento de designa', 3, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
        
        insert into gen_recursos_catalogos
        (idrecurso, descripcion, idlenguaje, fechamodificacion, usumodificacion, idinstitucion, nombretabla, campotabla, idrecursoalias)
         values
         ( '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'Documento de designa', 4, sysdate, 0, SEDE.idinstitucion, 'SCS_DOCUMENTOEJG', 'DESCRIPCION', 'scs_documentoejg.descripcion.'||SEDE.idinstitucion || '.'|| V_REC||'_'||V_DOC);
           
           insert into scs_documentoejg
           (idinstitucion, idtipodocumentoejg, iddocumentoejg, abreviatura, descripcion, codigoext, codigoejis)
         values
           (SEDE.idinstitucion, V_REC, V_DOC, 'Documento de designa', '830'||SEDE.idinstitucion ||V_REC||'_'||V_DOC, 'TR-DOCINIDES-01', NULL);  
          --Inserto el parametro por cada colegio
		   --insert into gen_parametros values ('SCS', 'PCAJG_GENERALITAT_INCLUSION_DOC', '1', SYSDATE, 0, SEDE.idinstitucion, 'scs.parametro.pcajg.generalitat.inclusionDOC', null);
         --FIN Iterar por las sedes        
        END LOOP;
        COMMIT;
       
            
END;

-- CAMBIOS PARA LA CARGAS DE CENSO CON CAMBIO DE SITUACIÓN, INSCRITO Y EXENTO CUOTA

--Nueva situaciÃ³n colegial ECOM
insert into ecom_cen_situacionejerciente (idecomcensosituacionejer, codigo, descripcion, fechamodificacion, usumodificacion) 
values (40, 'INSCRITO', 'Inscrito', sysdate, 0);
alter table ECOM_CEN_DATOS add EXENTOCUOTAS    NUMBER(1) ;
alter table ECOM_CEN_DATOS add HAYCAMBIOSITUACION    NUMBER(1) DEFAULT 0;
--Creamos un nuevo campo en la tabla de ecom_datos que nos servirÃ¡ para almacenar el motivo del cambio e situaciÃ³n. Ese campo es obligatorio para los cambios de situaciÃ³n, por lo que cuando estÃ© relleno
-- ya sabemos que se trata de ese caso, por lo que no creamos uno nuevo para ello
alter table ECOM_CEN_DATOS add IDMOTIVOSITUACION    NUMBER(4) ;

create table ECOM_CEN_MOTIVO_SITUACION
(
  IDMOTIVOSITUACION NUMBER(2) not null,
  CODIGO             VARCHAR2(25) not null,
  DESCRIPCION       VARCHAR2(250) not null,
  FECHAMODIFICACION  DATE not null,
  USUMODIFICACION    NUMBER(5) not null
)tablespace TS_SIGA_CMN;

alter table ECOM_CEN_MOTIVO_SITUACION
  add constraint PK_ECOM_MOTIVO_SITUACION primary key (IDMOTIVOSITUACION) using index tablespace TS_SIGA_CMN_IDX;
  
alter table ECOM_CEN_DATOS
  add constraint FK_ECOMCEN_DATOS_MAEST_MOTIV foreign key (IDMOTIVOSITUACION)
  references ECOM_CEN_MOTIVO_SITUACION (IDMOTIVOSITUACION);

create table ECOM_CEN_TIPO_SANCION
(
  IDTIPOSANCION NUMBER(2) not null,
  CODIGO             VARCHAR2(25) not null,
  DESCRIPCION       VARCHAR2(250) not null,
  FECHAMODIFICACION  DATE not null,
  USUMODIFICACION    NUMBER(5) not null
)tablespace TS_SIGA_CMN;

alter table ECOM_CEN_TIPO_SANCION
  add constraint PK_ECOM_TIPO_SANCION primary key (IDTIPOSANCION) using index tablespace TS_SIGA_CMN_IDX;
  
create table ECOM_CEN_MODIFICACION_SANCION
(
  IDTIPOMODIFICACION NUMBER(2) not null,
  CODIGO             VARCHAR2(25) not null,
  DESCRIPCION       VARCHAR2(250) not null,
  FECHAMODIFICACION  DATE not null,
  USUMODIFICACION    NUMBER(5) not null
)tablespace TS_SIGA_CMN;

alter table ECOM_CEN_MODIFICACION_SANCION
  add constraint PK_ECOM_MODIFICACION_SANCION primary key (IDTIPOMODIFICACION) using index tablespace TS_SIGA_CMN_IDX;
  
create sequence SEQ_ECOM_CEN_SANCIONES minvalue 1 maxvalue 9999999999 start with 1 increment by 1 nocache;

create table ECOM_CEN_SANCIONES
(
  IDCENSOSANCION NUMBER(10) not null,
  IDCENSODATOS   NUMBER(10) not null,
  REFERENCIA   VARCHAR2(50) not null,
  FECHAACUERDO DATE not null,
  FECHAREHABILITACION DATE,
  ESNUEVA  NUMBER(1) NOT NULL,
  IDTIPOSANCION NUMBER(4),
  TEXTOSANCION VARCHAR2(4000),
  IDTIPOMODIFICACION NUMBER(4),
  TEXTONOTIFICACION VARCHAR2(4000),
  FECHAINICIO DATE NOT NULL,
  FECHAFIN DATE,
  FECHAMODIFICACION  DATE not null,
  USUMODIFICACION    NUMBER(5) not null
  
)tablespace TS_SIGA_CMN;

alter table ECOM_CEN_SANCIONES
  add constraint PK_ECOM_CEN_SANCIONES primary key (IDCENSOSANCION) using index  tablespace TS_SIGA_CMN_IDX;
alter table ECOM_CEN_SANCIONES
  add constraint FK_ECOMCEN_SANCIONES_MOTIV foreign key (IDTIPOMODIFICACION)
  references ECOM_CEN_MODIFICACION_SANCION (IDTIPOMODIFICACION);  
alter table ECOM_CEN_SANCIONES
  add constraint FK_ECOMCEN_SANCIONES_DATOS foreign key (IDCENSODATOS)
  references ECOM_CEN_DATOS (IDCENSODATOS);
  alter table ECOM_CEN_SANCIONES
  add constraint FK_ECOMCEN_SANCIONES_TIPO foreign key (IDTIPOSANCION)
  references ECOM_CEN_TIPO_SANCION (IDTIPOSANCION);

  
grant select, insert, update, delete on ECOM_CEN_TIPO_SANCION to ROLE_SIGA;
grant select, insert, update, delete on ECOM_CEN_MODIFICACION_SANCION to ROLE_SIGA;
grant select, insert, update, delete on ECOM_CEN_SANCIONES to ROLE_SIGA;
grant select, insert, update, delete on ECOM_CEN_MOTIVO_SITUACION to ROLE_SIGA;
grant select on ECOM_CEN_TIPO_SANCION to ROLE_SIGA_R;
grant select on ECOM_CEN_MODIFICACION_SANCION to ROLE_SIGA_R;
grant select on ECOM_CEN_SANCIONES to ROLE_SIGA_R;
grant select on ECOM_CEN_MOTIVO_SITUACION to ROLE_SIGA_R;


insert into ECOM_CEN_MOTIVO_SITUACION values(1, 'Fallecimiento', 'Fallecimiento',sysdate,0);
insert into ECOM_CEN_MOTIVO_SITUACION values(2, 'No_especificado', 'No especificado',sysdate,0);
insert into ECOM_CEN_MOTIVO_SITUACION values(3, 'Voluntario','Voluntario', sysdate,0);
insert into ECOM_CEN_TIPO_SANCION values(1, 'IMPAGOCUOTAS','Impago Cuotas', sysdate,0);
insert into ECOM_CEN_TIPO_SANCION values(2, 'SUSPENSION_DISCIPLINARIA','SuspensiÃ³n Disciplinaria', sysdate,0);
insert into ECOM_CEN_TIPO_SANCION values(3, 'EXPULSION','ExpulsiÃ³n', sysdate,0);
insert into ECOM_CEN_MODIFICACION_SANCION values(1, 'REACTIVAR','Reactivar', sysdate,0);
insert into ECOM_CEN_MODIFICACION_SANCION values(2, 'REHABILITAR_ARCHIVAR', 'Rehabilitar-Archivar', sysdate,0);
insert into ECOM_CEN_MODIFICACION_SANCION values(3, 'SUSPENDER','Suspender', sysdate,0);

insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (122, 'INSCRITO_A_NO_INSCRITO', sysdate);  
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (123, 'NO_INSCRITO_A_INSCRITO', sysdate);
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (124, 'MOTIVO_CAMBIO_SITUACION_NULO', sysdate);  
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (125, 'NCOL_APELL_NUMDOC', sysdate);
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (126, 'REFERENCIA_NULO', sysdate);
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (127, 'FECHA_ACUERDO_NULA', sysdate);
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (128, 'FECHA_INICIO', sysdate);
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (129, 'FECHA_INICIO_ANTERIOR', sysdate);
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (130, 'TEXTO_SANCION_NULO', sysdate);
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (131, 'TIPO_SANCION_NULO', sysdate);
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (132, 'TEXTO_MODIFICACION_NULO', sysdate);
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (133, 'TIPO_MODIFICACION_NULO', sysdate);
insert into ecom_cen_maestro_incidenc   (idcensomaestroincidencias, descripcion, fechamodificacion)
values   (134, 'COLEGIADO_NOENCONTRADO_PARA_CAMBIO', sysdate);

insert into ecom_cen_maestro_estad_coleg
  (idestadocolegiado, descripcion, fechamodificacion)
values
  (13, 'ACTUALIZACION_COLEGIADO_CAMBIO', sysdate);
  insert into ecom_cen_maestro_estad_coleg
  (idestadocolegiado, descripcion, fechamodificacion)
values
  (14, 'ACTUALIZACION_COLEGIADO_SANCION', sysdate);
  
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('MOTIVO_CAMBIO_SITUACION_NULO', 'El motivo del cambio de situación edbe estar informado', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('MOTIVO_CAMBIO_SITUACION_NULO', 'El motivo del cambio de situación edbe estar informado#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('MOTIVO_CAMBIO_SITUACION_NULO', 'El motivo del cambio de situación edbe estar informado#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('MOTIVO_CAMBIO_SITUACION_NULO', 'El motivo del cambio de situación edbe estar informado#GL', 0, 4, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('NCOL_APELL_NUMDOC', 'Para identificar al colegiado se debe informar mínimo de dos de los tres criterios siguientes: Nombre y Apellidos, Número de coelgiado, Identificación.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('NCOL_APELL_NUMDOC', 'Para identificar al colegiado se debe informar mínimo de dos de los tres criterios siguientes: Nombre y Apellidos, Número de coelgiado, Identificación.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('NCOL_APELL_NUMDOC', 'Para identificar al colegiado se debe informar mínimo de dos de los tres criterios siguientes: Nombre y Apellidos, Número de coelgiado, Identificación.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('NCOL_APELL_NUMDOC', 'Para identificar al colegiado se debe informar mínimo de dos de los tres criterios siguientes: Nombre y Apellidos, Número de coelgiado, Identificación.#GL', 0, 4, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('REFERENCIA_NULO', 'El numero de referencia debe ser informado.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('REFERENCIA_NULO', 'El numero de referencia debe ser informado.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('REFERENCIA_NULO', 'El numero de referencia debe ser informado.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('REFERENCIA_NULO', 'El numero de referencia debe ser informado.#GL', 0, 4, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_ACUERDO_NULA', 'La fecha de acuerdo debe ser informado.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_ACUERDO_NULA', 'La fecha de acuerdo debe ser informado.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_ACUERDO_NULA', 'La fecha de acuerdo debe ser informado.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_ACUERDO_NULA', 'La fecha de acuerdo debe ser informado.#GL', 0, 4, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_INICIO', 'La fecha de incio de la sanción debe ser informado.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_INICIO', 'La fecha de incio de la sanción debe ser informado.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_INICIO', 'La fecha de incio de la sanción debe ser informado.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_INICIO', 'La fecha de incio de la sanción debe ser informado.#GL', 0, 4, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_INICIO_ANTERIOR', 'La fecha de incio de la sanción no puede ser anterior a la actual.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_INICIO_ANTERIOR', 'La fecha de incio de la sanción no puede ser anterior a la actual.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_INICIO_ANTERIOR', 'La fecha de incio de la sanción no puede ser anterior a la actual.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('FECHA_INICIO_ANTERIOR', 'La fecha de incio de la sanción no puede ser anterior a la actual.#GL', 0, 4, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TIPO_SANCION_NULO', 'El tipo de sanción debe ser informado.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TIPO_SANCION_NULO', 'El tipo de sanción debe ser informado.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TIPO_SANCION_NULO', 'El tipo de sanción debe ser informado.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TIPO_SANCION_NULO', 'El tipo de sanción debe ser informado.#GL', 0, 4, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TEXTO_SANCION_NULO', 'El texto de la sanción debe ser informado.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TEXTO_SANCION_NULO', 'El texto de la sanción debe ser informado.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TEXTO_SANCION_NULO', 'El texto de la sanción debe ser informado.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TEXTO_SANCION_NULO', 'El texto de la sanción debe ser informado.#GL', 0, 4, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TEXTO_MODIFICACION_NULO', 'El campo modificación de la sanción debe ser informado.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TEXTO_MODIFICACION_NULO', 'El campo modificación de la sanción debe ser informado.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TEXTO_MODIFICACION_NULO', 'El campo modificación de la sanción debe ser informado.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TEXTO_MODIFICACION_NULO', 'El campo modificación de la sanción debe ser informado.#GL', 0, 4, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TIPO_MODIFICACION_NULO', 'El tipo de modificación de la sanción debe ser informado.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TIPO_MODIFICACION_NULO', 'El tipo de modificación de la sanción debe ser informado.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TIPO_MODIFICACION_NULO', 'El tipo de modificación de la sanción debe ser informado.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('TIPO_MODIFICACION_NULO', 'El tipo de modificación de la sanción debe ser informado.#GL', 0, 4, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('COLEGIADO_NOENCONTRADO_PARA_CAMBIO', 'Con los datos indicados no se ha podido encontrar al colegiado para actualizar su situación.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('COLEGIADO_NOENCONTRADO_PARA_CAMBIO', 'Con los datos indicados no se ha podido encontrar al colegiado para actualizar su situación.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('COLEGIADO_NOENCONTRADO_PARA_CAMBIO', 'Con los datos indicados no se ha podido encontrar al colegiado para actualizar su situación.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('COLEGIADO_NOENCONTRADO_PARA_CAMBIO', 'Con los datos indicados no se ha podido encontrar al colegiado para actualizar su situación.#GL', 0, 4, sysdate, 0, 19);

insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.estadoColegiado.ACTUALIZACION_COLEGIADO_CAMBIO', 'Actualización situación colegial.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.estadoColegiado.ACTUALIZACION_COLEGIADO_CAMBIO', 'Actualización situación colegial.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.estadoColegiado.ACTUALIZACION_COLEGIADO_CAMBIO', 'Actualización situación colegial.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.estadoColegiado.ACTUALIZACION_COLEGIADO_CAMBIO', 'Actualización situación colegial.#GL', 0, 4, sysdate, 0, 19);

insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('NCOL_APELL_NUMDOC', 'Se debe informar dos de los tres datos del colegiado: Apellidos, Nº colegiado, Identificación.', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('NCOL_APELL_NUMDOC', 'Se debe informar dos de los tres datos del colegiado: Apellidos, Nº colegiado, Identificación.#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('NCOL_APELL_NUMDOC', 'Se debe informar dos de los tres datos del colegiado: Apellidos, Nº colegiado, Identificación.#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('NCOL_APELL_NUMDOC', 'Se debe informar dos de los tres datos del colegiado: Apellidos, Nº colegiado, Identificación.#GL', 0, 4, sysdate, 0, 19);

insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.literal.motivocambio', 'Motivo Cambio', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.literal.motivocambio', 'Motivo Cambio#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.literal.motivocambio', 'Motivo Cambio#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.literal.motivocambio', 'Motivo Cambio#GL', 0, 4, sysdate, 0, 19);

insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.estadoColegiado.ACTUALIZACION_COLEGIADO_SANCION', 'Actualización sanción', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.estadoColegiado.ACTUALIZACION_COLEGIADO_SANCION', 'Actualización sanción#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.estadoColegiado.ACTUALIZACION_COLEGIADO_SANCION', 'Actualización sanción#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.estadoColegiado.ACTUALIZACION_COLEGIADO_SANCION', 'Actualización sanción#GL', 0, 4, sysdate, 0, 19);

alter table CEN_COLEGIADO modify JUBILACIONCUOTA null;
 insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.literal.exentoCuota', 'Exento cuota', 0, 1, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.literal.exentoCuota', 'Exento cuota#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.literal.exentoCuota', 'Exento cuota#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) values   ('censo.ws.literal.exentoCuota', 'Exento cuota#GL', 0, 4, sysdate, 0, 19);





	