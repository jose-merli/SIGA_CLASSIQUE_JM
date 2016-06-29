-- INI 123_002

Cambios en V_CENSO_LETRADOS, V_CENSO_COLEGIACIONES y V_CENSO_LETRADOS_OOJJ

-- Los ejecutara Azucena fuera de horario laboral en los 4 entornos.

-- INI 123_004

Cambio en V_CENSO_LETRADOS

-- Los ejecutara Azucena fuera de horario laboral en los 4 entornos.

alter table SCS_UNIDADFAMILIAREJG add INCAPACITADO number(1);
alter table SCS_UNIDADFAMILIAREJG add CIRCUNSTANCIAS_EXCEPCIONALES number(1);

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.incapacitado', 'Incapacitado', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.incapacitado', 'Incapacitado#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.incapacitado', 'Incapacitado#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.incapacitado', 'Incapacitado#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.circunstanciasExcepcionales', 'Circunst. Excepc.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.circunstanciasExcepcionales', 'Circunst. Excepc.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.circunstanciasExcepcionales', 'Circunst. Excepc.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.personaJG.literal.circunstanciasExcepcionales', 'Circunst. Excepc.#EU', 0, '3', sysdate, 0, '19');


 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.success.censo.peticion', 'La solicitud de actualización de censo ha sido procesada correctamente.', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.success.censo.peticion', 'La solicitud de actualización de censo ha sido procesada correctamente.#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.success.censo.peticion', 'La solicitud de actualización de censo ha sido procesada correctamente.#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.success.censo.peticion', 'La solicitud de actualización de censo ha sido procesada correctamente.#EU', 0, '3', sysdate, 0, '19');

-- Ejecutado por Adrian en SIGA INT - 2016-06-20 08:25


Drop Materialized View V_CENSO_LETRADOS;
Drop Table V_CENSO_LETRADOS;

Create Table T_CENSO_LETRADOS (
  Id_Letrado Number(10),
  Nombre Varchar2(100),
  Apellido1 Varchar2(100),
  Apellido2 Varchar2(100),
  Num_doc Varchar2(20),
  Idtipoidentificacion Number(2),
  Sexo Varchar2(1),
  Fechamodificacion Date,
  
  Dir_Profesional Varchar2(100),
  Poblacion Varchar2(100),
  Idpoblacion Varchar2(11),
  Idprovincia Varchar2(2),
  Provincia Varchar2(100),
  Cod_Postal Varchar2(5),
  Telefono Varchar2(20),
  Fax Varchar2(20),
  Movil Varchar2(20),
  Mail Varchar2(100),
  Idpais Varchar2(3),
  Pais Varchar2(100)
)
Tablespace Ts_siga_censo;
/

Alter Table T_Censo_Letrados
Add RegistroTemporal Varchar2(1) Default '1';

create or replace view v_censo_letrados as
select "ID_LETRADO","NOMBRE","APELLIDO1","APELLIDO2","NUM_DOC","IDTIPOIDENTIFICACION","SEXO","FECHAMODIFICACION","DIR_PROFESIONAL","POBLACION","IDPOBLACION","IDPROVINCIA","PROVINCIA","COD_POSTAL","TELEFONO","FAX","MOVIL","MAIL","IDPAIS","PAIS" from t_censo_letrados
Where RegistroTemporal = 0;

Creado Refresh_t_censo_Letrados.prc
Modificado ECENSO.P_SYNC_ABOGADOS_REFRESH.prc


-- Ejecutado por Adrian en SIGA INT - 2016-06-29 10:30

