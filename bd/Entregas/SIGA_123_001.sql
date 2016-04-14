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

-- Ejecutado en Integracion por Adrian el 30/03/2016 10:00 (las ultimas sentencias han sido cambiadas, añadiendo valor por defecto en las fechas y algo mas)

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.insercionCompletas', 'Existen actuaciones que no se insertarán debido a que no se ha aportado toda la información obligatoria.¿Desea continuar?', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.insercionCompletas', 'Existen actuaciones que no se insertarán debido a que no se ha aportado toda la información obligatoria.¿Desea continuar?#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.insercionCompletas', 'Existen actuaciones que no se insertarán debido a que no se ha aportado toda la información obligatoria.¿Desea continuar?#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.insercionCompletas', 'Existen actuaciones que no se insertarán debido a que no se ha aportado toda la información obligatoria.¿Desea continuar?#EU', 0, '3', sysdate, 0, '19');

-- Ejecutado en Integracion por Adrian el 30/03/2016 12:30

 
update gen_recursos set descripcion='Fecha Justificación' where idRecurso='gratuita.busquedaDesignas.literal.fechaJustificacion.desde' and idLenguaje=1
update gen_recursos set descripcion='Fecha Justificación#CA' where idRecurso='gratuita.busquedaDesignas.literal.fechaJustificacion.desde' and idLenguaje=2
update gen_recursos set descripcion='Fecha Justificación#EU' where idRecurso='gratuita.busquedaDesignas.literal.fechaJustificacion.desde' and idLenguaje=3
update gen_recursos set descripcion='Fecha Justificación#GL' where idRecurso='gratuita.busquedaDesignas.literal.fechaJustificacion.desde' and idLenguaje=4
 
 
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.literalDesde', 'Desde', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.literalDesde', 'Desde#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.literalDesde', 'Desde#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.literalDesde', 'Desde#EU', 0, '3', sysdate, 0, '22');
 
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.censo.cargaperiodica.confirmExcel', 'A continuación se generará un fichero excel con el listado de incidencias de colegiados con el filtro establecido en la consulta. En el informe se muestra la fecha de incorporación del colegiado encontrado por su número de identificación únicamente.\\n¿Desea continuar?', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.censo.cargaperiodica.confirmExcel', 'A continuación se generará un fichero excel con el listado de incidencias de colegiados con el filtro establecido en la consulta. En el informe se muestra la fecha de incorporación del colegiado encontrado por su número de identificación únicamente.\\n¿Desea continuar?#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.censo.cargaperiodica.confirmExcel', 'A continuación se generará un fichero excel con el listado de incidencias de colegiados con el filtro establecido en la consulta. En el informe se muestra la fecha de incorporación del colegiado encontrado por su número de identificación únicamente.\\n¿Desea continuar?#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.censo.cargaperiodica.confirmExcel', 'A continuación se generará un fichero excel con el listado de incidencias de colegiados con el filtro establecido en la consulta. En el informe se muestra la fecha de incorporación del colegiado encontrado por su número de identificación únicamente.\\n¿Desea continuar?#EU', 0, '3', sysdate, 0, '19');

 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Ya existe cuenta SJCS' where idrecurso='messages.censo.existeAbonoSJCS' and idlenguaje='1';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Ya existe cuenta SJCS#CA' where idrecurso='messages.censo.existeAbonoSJCS' and idlenguaje='2';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Ya existe cuenta SJCS#EU' where idrecurso='messages.censo.existeAbonoSJCS' and idlenguaje='3';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Ya existe cuenta SJCS#GL' where idrecurso='messages.censo.existeAbonoSJCS' and idlenguaje='4';
 
  CREATE OR REPLACE VIEW V_WS_2064_PERSONA AS
SELECT  P.IDPERSONA
      , EJGREMESA.IDREMESA
      , EJGREMESA.IDEJGREMESA
      , EJGREMESA.IDINSTITUCION
      , EJG.ANIO
      , EJG.NUMERO
      , EJG.IDTIPOEJG
      , DECODE(P.IDPERSONA, EJG.IDPERSONAJG, 1, 0) AS IS_SOLICITANTE
      , DECODE(PAR.CODIGOEXT, 21, 1, 0) AS IS_CONYUGE
      , TRIM(P.NOMBRE) AS NOME--OB
      , TRIM(P.APELLIDO1) AS APELLIDO1
      , TRIM(P.APELLIDO2) AS APELLIDO2
      --, PAIS.CODIGOEXT AS NACIONALIDADE
      , NULL AS NACIONALIDADE
      , (CASE P.IDESTADOCIVIL
              WHEN 1--Casado
              THEN 2
              WHEN 2--SOLTERO
              THEN 1
              WHEN 3--VIUDO
              THEN 4
              WHEN 4--SEPARADO
              THEN 5
              WHEN 5--DIVORCIADO
              THEN 3
              WHEN 6--PAREJA DE HECHO
              THEN NULL
              WHEN 7--DESCONOCIDO
              THEN NULL
              ELSE NULL
          END) AS ESTADO_CIVIL
       , (SELECT PI.CODIGO
               FROM PCAJG_TIPO_IDENTIFICACION PI, PCAJG_TIPO_IDENTIFICACION_CENT PS
               WHERE PI.IDENTIFICADOR = PS.IDENTIFICADOR
                AND PI.IDINSTITUCION = PS.IDINSTITUCION
                AND PS.IDTIPOIDENTIFICACION = P.IDTIPOIDENTIFICACION
                AND PS.IDINSTITUCION = P.IDINSTITUCION) AS TIPOIDENTIFICADOR
      , UPPER(P.NIF) AS NIF
      , P.FECHANACIMIENTO AS DATA_NACEMENTO
      , TRUNC(MONTHS_BETWEEN(SYSDATE, P.FECHANACIMIENTO)/12) AS IDADE
      , P.DIRECCION AS DC_ENDEREZO
      , DECODE(POB.IDPOBLACION, 0, '', SUBSTR(POB.NOMBRE, 1, 25)) AS DC_LOCALIDADE
      , DECODE(POB.IDPOBLACION, 0, '99999', SUBSTR(POB.CODIGOEXT, 1, 5)) AS DC_MUNICIPIO
      , DECODE(PROV.IDPROVINCIA, 00, '99', PROV.CODIGOEXT) AS DC_PROVINCIA
      , P.CODIGOPOSTAL AS DC_CODPOSTAL
      , F_SIGA_GETTELEFONOPERSONAJG(P.IDINSTITUCION, P.IDPERSONA, 1) AS DC_TELEFONO
      , NULL AS DC_FAX
      , NULL AS DC_CORREO_ELECTRONICO
      , NULL AS PROFESION
      , NULL AS EMPRESA
      , NULL AS REGIMEN_COTIZACION
--      , NULL AS ES_PERSONA_FISICA
--      , NULL AS SEXO
      , DECODE(P.TIPOPERSONAJG, 'F', 1, 0) AS ES_PERSONA_FISICA
      , DECODE(P.SEXO, 'H', 'M', 'M', 'F', NULL) AS SEXO
      , PAR.CODIGOEXT AS PARENTESCO
      --DATOS ECONOMICOS
      , NULL AS DE_INGRESO_IMP
      , NULL AS DE_INGRESO_CONCEPTO
      , NULL AS DE_BI_TIPO
      , NULL AS DE_BI_VALORACION
      , NULL AS DE_BI_TIPOVALORACION
      , NULL AS DE_BI_CARGAS
      , NULL AS DE_BM_TIPO
      , NULL AS DE_BE_VALORACION
      , NULL AS DE_BE_MATRICULA
      , NULL AS DE_ALQUILER_RMENSUAL
      , NULL AS DE_OUTROS_DATOS
FROM SCS_PERSONAJG P
     , CEN_PAIS PAIS
     , CEN_POBLACIONES POB
     , CEN_PROVINCIAS PROV
     , SCS_UNIDADFAMILIAREJG UF
     , SCS_EJG EJG
     , SCS_PARENTESCO PAR
     , CAJG_EJGREMESA EJGREMESA
WHERE P.IDPAIS = PAIS.IDPAIS(+)
AND P.IDPOBLACION = POB.IDPOBLACION(+)
AND P.IDPROVINCIA = PROV.IDPROVINCIA(+)
AND UF.IDINSTITUCION = P.IDINSTITUCION
AND UF.IDPERSONA = P.IDPERSONA
AND UF.IDINSTITUCION = EJG.IDINSTITUCION
AND UF.ANIO = EJG.ANIO
AND UF.NUMERO = EJG.NUMERO
AND UF.IDTIPOEJG = EJG.IDTIPOEJG
AND UF.IDINSTITUCION = PAR.IDINSTITUCION(+)
AND UF.IDPARENTESCO = PAR.IDPARENTESCO(+)
AND EJG.IDINSTITUCION =  EJGREMESA.IDINSTITUCION
AND EJG.ANIO = EJGREMESA.ANIO
AND EJG.NUMERO = EJGREMESA.NUMERO
AND EJG.IDTIPOEJG = EJGREMESA.IDTIPOEJG;

nueva funcion F_SIGA_GETFECHAESTADOCOLEGIAL

vista actualizada V_WS_2064_PERSONA