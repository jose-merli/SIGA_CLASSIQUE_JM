-- Add comments to the columns 
comment on column SCS_ASISTENCIA.IDORIGENASISTENCIA
  is '10 ICA SIGA 20 Colegiado SIGA  30 Volante expres móvil 40 Centralita de Guardias';

  -- Add/modify columns 
alter table SCS_ACTUACIONDESIGNA add ANIOPROCEDIMIENTO NUMBER(4);

UPDATE scs_actuaciondesigna SET anioprocedimiento = anio where idinstitucion = 2014 and numeroprocedimiento is not null;
  
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.observaciones', 'Observaciones', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.observaciones', 'Observaciones#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.observaciones', 'Observaciones#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.observaciones', 'Observaciones#EU', 0, '3', sysdate, 0, '19');
 
ALTER TABLE CEN_HISTORICO
ADD OBSERVACIONES VARCHAR2(1000);

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.tipo.todasNinguna', 'Todas/Ninguna', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.tipo.todasNinguna', 'Todas/Ninguna#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.tipo.todasNinguna', 'Todas/Ninguna#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('censo.consultaHistorico.literal.tipo.todasNinguna', 'Todas/Ninguna#EU', 0, '3', sysdate, 0, '19');
 
ALTER TABLE CEN_CUENTASBANCARIAS
MODIFY ABONOCARGO VARCHAR2(1) DEFAULT 'C' NULL;

ALTER TABLE CEN_SOLICMODICUENTAS
MODIFY ABONOCARGO VARCHAR2(1) DEFAULT 'C' NULL;

Modificado PKG_SERVICIOS_AUTOMATICOS;

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.documentacionDesigna.observaciones', 'Observaciones', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.documentacionDesigna.observaciones', 'Observaciones#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.documentacionDesigna.observaciones', 'Observaciones#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.documentacionDesigna.observaciones', 'Observaciones#EU', 0, '3', sysdate, 0, '19');
 

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.informeJustificacionMasiva.literal.numeroProcedimiento', 'Nº Proc.', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.informeJustificacionMasiva.literal.numeroProcedimiento', 'Nº Proc.#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.informeJustificacionMasiva.literal.numeroProcedimiento', 'Nº Proc.#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.informeJustificacionMasiva.literal.numeroProcedimiento', 'Nº Proc.#EU', 0, '3', sysdate, 0, '22');



insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.procedimientos.acreditacion.literal.nigNumeroProcedimiento', 'NIG/Nº Procedimiento', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.procedimientos.acreditacion.literal.nigNumeroProcedimiento', 'NIG/Nº Procedimiento#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.procedimientos.acreditacion.literal.nigNumeroProcedimiento', 'NIG/Nº Procedimiento#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.procedimientos.acreditacion.literal.nigNumeroProcedimiento', 'NIG/Nº Procedimiento#EU', 0, '3', sysdate, 0, '22');


ALTER TABLE scs_acreditacionprocedimiento ADD (
    NIG_NUMPROCEDIMIENTO NUMBER DEFAULT 0 NOT NULL
);

COMMENT ON COLUMN scs_acreditacionprocedimiento.NIG_NUMPROCEDIMIENTO IS '0: No está seleccionado el check NIG/Nº Procedimiento de la acreditación; 1:Si está seleccionado el check NIG/Nº Procedimiento de la acreditación';


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.volantesExpres.literal.sexo', 'Sexo', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.volantesExpres.literal.sexo', 'Sexo#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.volantesExpres.literal.sexo', 'Sexo#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.volantesExpres.literal.sexo', 'Sexo#EU', 0, '3', sysdate, 0, '22');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.mantActuacion.literal.comisariaJuzgados', 'Comisaría o Juzgado', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.mantActuacion.literal.comisariaJuzgados', 'Comisaría o Juzgado#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.mantActuacion.literal.comisariaJuzgados', 'Comisaría o Juzgado#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.mantActuacion.literal.comisariaJuzgados', 'Comisaría o Juzgado#EU', 0, '3', sysdate, 0, '22');


insert into CEN_TIPOCAMBIO (IDTIPOCAMBIO, DESCRIPCION, FECHAMODIFICACION, USUMODIFICACION)
values (107, '3400107', sysdate, 0);

insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400107', 'Designación. Modificación de Actuaciones', '1', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.107');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400107', 'Designación. Modificación de Actuaciones#CA', '2', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.107');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400107', 'Designación. Modificación de Actuaciones#EU', '3', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.107');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400107', 'Designación. Modificación de Actuaciones#GL', '4', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.107');


insert into CEN_TIPOCAMBIO (IDTIPOCAMBIO, DESCRIPCION, FECHAMODIFICACION, USUMODIFICACION)
values (108, '3400108', sysdate, 0);

 


insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400108', 'Designación. Eliminación de Actuaciones', '1', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.108');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400108', 'Designación. Eliminación de Actuaciones#CA', '2', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.108');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400108', 'Designación. Eliminación de Actuaciones#EU', '3', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.108');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400108', 'Designación. Eliminación de Actuaciones#GL', '4', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.108');

INSERT INTO GEN_PARAMETROS (MODULO,PARAMETRO,VALOR,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,IDRECURSO)
VALUES ('SCS','JUSTIFICACION_EDITAR_ACT_FICHA','0',SYSDATE,0,0,'scs.parametro.scs.justificacionEditarActFicha');
  
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.scs.justificacionEditarActFicha', 'Permitir modificar y borrar actuaciones de designación en la ficha colegial(1:Si, 0:No)', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.scs.justificacionEditarActFicha', 'Permitir modificar y borrar actuaciones de designación en la ficha colegial(1:Si, 0:No)#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.scs.justificacionEditarActFicha', 'Permitir modificar y borrar actuaciones de designación en la ficha colegial(1:Si, 0:No)#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.scs.justificacionEditarActFicha', 'Permitir modificar y borrar actuaciones de designación en la ficha colegial(1:Si, 0:No)#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.desde', 'Fecha Justificación Desde', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.desde', 'Fecha Justificación Desde#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.desde', 'Fecha Justificación Desde#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.desde', 'Fecha Justificación Desde#EU', 0, '3', sysdate, 0, '22');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.hasta', 'Hasta', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.hasta', 'Hasta#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.hasta', 'Hasta#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.hasta', 'Hasta#EU', 0, '3', sysdate, 0, '22');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.origenActuaciones', 'Origen Actuaciones', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.origenActuaciones', 'Origen Actuaciones#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.origenActuaciones', 'Origen Actuaciones#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.origenActuaciones', 'Origen Actuaciones#EU', 0, '3', sysdate, 0, '22');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.fehaActMayorJust', 'La fecha de justificación no puede ser anterior a la fecha de la actuación', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.fehaActMayorJust', 'La fecha de justificación no puede ser anterior a la fecha de la actuación#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.fehaActMayorJust', 'La fecha de justificación no puede ser anterior a la fecha de la actuación#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.fehaActMayorJust', 'La fecha de justificación no puede ser anterior a la fecha de la actuación#EU', 0, '3', sysdate, 0, '19');
 
Modificado PKG_SIGA_FCS_HISTORICO
Modificado PKG_SIGA_FACTURACION_SJCS


 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.informacion', 'La información de {0} está {1}', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.informacion', 'La información de {0} está {1}#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.informacion', 'La información de {0} está {1}#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.informacion', 'La información de {0} está {1}#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('literal.informacion.incompleta', 'Incompleta', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('literal.informacion.incompleta', 'Incompleta#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('literal.informacion.incompleta', 'Incompleta#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('literal.informacion.incompleta', 'Incompleta#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('literal.informacion.completa', 'Completa', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('literal.informacion.completa', 'Completa#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('literal.informacion.completa', 'Completa#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('literal.informacion.completa', 'Completa#EU', 0, '3', sysdate, 0, '19');


-- Ejecutado en Integracion por Adrian el 28/03/2016 14:00

alter table scs_actuacionasistencia add USUCREACION NUMBER(5) DEFAULT 0 NOT NULL;
alter table scs_actuacionasistencia add FECHACREACION DATE;
Update scs_actuacionasistencia SET FECHACREACION=sysdate Where USUCREACION=0;
alter table scs_actuacionasistencia modify FECHACREACION not null ;



alter table scs_actuaciondesigna add USUCREACION NUMBER(5) NOT NULL DEFAULT (0);
alter table scs_actuaciondesigna add FECHACREACION DATE;
Update scs_actuaciondesigna SET FECHACREACION=sysdate Where USUCREACION=0;
alter table scs_actuaciondesigna modify FECHACREACION not null ;
 