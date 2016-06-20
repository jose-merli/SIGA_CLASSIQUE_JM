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

