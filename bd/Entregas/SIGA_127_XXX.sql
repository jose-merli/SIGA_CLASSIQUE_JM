Pkg_Siga_Fusion_Personas
V_pcajg_ejg

PROC_RESPUESTAEJG_2003

Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna informaci�n de identificaci�n sobre el cobro. Para continuar, si el certificado ya est� cobrado rellene o bien Comentario, o bien Entidad; si no est� cobrado rellene el Colegio al que se cobrar�.' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 1;
Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna informaci�n de identificaci�n sobre el cobro. Para continuar, si el certificado ya est� cobrado rellene o bien Comentario, o bien Entidad; si no est� cobrado rellene el Colegio al que se cobrar�.#CA' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 2;
Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna informaci�n de identificaci�n sobre el cobro. Para continuar, si el certificado ya est� cobrado rellene o bien Comentario, o bien Entidad; si no est� cobrado rellene el Colegio al que se cobrar�.#EU' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 3;
Update Gen_Recursos rec Set rec.Descripcion = 'Es necesario aportar alguna informaci�n de identificaci�n sobre el cobro. Para continuar, si el certificado ya est� cobrado rellene o bien Comentario, o bien Entidad; si no est� cobrado rellene el Colegio al que se cobrar�.#GL' Where rec.Idrecurso = 'messages.certificado.error.finalizarCobro' And rec.Idlenguaje = 4;

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
VALUES('131005', 'Telem�tica',1,sysdate,0,null,'PYS_METODOSOLICITUD','DESCRIPCION', 'pys_metodosolicitud.descripcion.0.5');
INSERT INTO GEN_RECURSOS_CATALOGOS (IDRECURSO,DESCRIPCION,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,NOMBRETABLA,CAMPOTABLA,IDRECURSOALIAS)
VALUES('131005', 'Telem�tica',2,sysdate,0,null,'PYS_METODOSOLICITUD','DESCRIPCION', 'pys_metodosolicitud.descripcion.0.5');
INSERT INTO GEN_RECURSOS_CATALOGOS (IDRECURSO,DESCRIPCION,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,NOMBRETABLA,CAMPOTABLA,IDRECURSOALIAS)
VALUES('131005', 'Telem�tica#EU',3,sysdate,0,null,'PYS_METODOSOLICITUD','DESCRIPCION', 'pys_metodosolicitud.descripcion.0.5');
INSERT INTO GEN_RECURSOS_CATALOGOS (IDRECURSO,DESCRIPCION,IDLENGUAJE,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,NOMBRETABLA,CAMPOTABLA,IDRECURSOALIAS)
VALUES('131005', 'Telem�tica#GL',4,sysdate,0,null,'PYS_METODOSOLICITUD','DESCRIPCION', 'pys_metodosolicitud.descripcion.0.5');

INSERT INTO PYS_METODOSOLICITUD (IDMETODOSOLICITUD,DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION,CODIGOEXT,BLOQUEADO)
VALUES(5,'131005',sysdate,0,'5','N');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.residencia', 'Residencia de Incorporaci�n', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.residencia', 'Residencia de Incorporaci�n#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.residencia', 'Residencia de Incorporaci�n#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.residencia', 'Residencia de Incorporaci�n#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoIncorporacion', 'Estado de incorporaci�n', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoIncorporacion', 'Estado de incorporaci�n#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoIncorporacion', 'Estado de incorporaci�n#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoIncorporacion', 'Estado de incorporaci�n#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.solicitudOriginal', 'Solicitud original', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.solicitudOriginal', 'Solicitud original#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.solicitudOriginal', 'Solicitud original#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.solicitudOriginal', 'Solicitud original#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.datosCertificado', 'Datos Certificado', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.datosCertificado', 'Datos Certificado#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.datosCertificado', 'Datos Certificado#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.datosCertificado', 'Datos Certificado#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.situacionColegial', 'Situaci�n colegial', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.situacionColegial', 'Situaci�n colegial#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.situacionColegial', 'Situaci�n colegial#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.situacionColegial', 'Situaci�n colegial#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.textoConformidad2', 'Acepta ceder sus datos a la Abogac�a', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.textoConformidad2', 'Acepta ceder sus datos a la Abogac�a#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.textoConformidad2', 'Acepta ceder sus datos a la Abogac�a#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.textoConformidad2', 'Acepta ceder sus datos a la Abogac�a#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosCertifOriginal', 'Datos Certificado Original', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosCertifOriginal', 'Datos Certificado Original#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosCertifOriginal', 'Datos Certificado Original#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.datosCertifOriginal', 'Datos Certificado Original#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.postalYProvincia', 'C�digo postal y Provincia', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.postalYProvincia', 'C�digo postal y Provincia#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.postalYProvincia', 'C�digo postal y Provincia#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.postalYProvincia', 'C�digo postal y Provincia#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.numSolicitudColegiacion', 'N�m. Solicitud Colegiaci�n', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.numSolicitudColegiacion', 'N�m. Solicitud Colegiaci�n#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.numSolicitudColegiacion', 'N�m. Solicitud Colegiaci�n#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.numSolicitudColegiacion', 'N�m. Solicitud Colegiaci�n#GL', 0, '4', sysdate, 0, '19');

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
-- 127_004:

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
