-- Add comments to the columns 
comment on column SCS_ASISTENCIA.IDORIGENASISTENCIA
  is '10 ICA SIGA 20 Colegiado SIGA  30 Volante expres m�vil 40 Centralita de Guardias';

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
 

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.informeJustificacionMasiva.literal.numeroProcedimiento', 'N� Proc.', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.informeJustificacionMasiva.literal.numeroProcedimiento', 'N� Proc.#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.informeJustificacionMasiva.literal.numeroProcedimiento', 'N� Proc.#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.informeJustificacionMasiva.literal.numeroProcedimiento', 'N� Proc.#EU', 0, '3', sysdate, 0, '22');



insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.procedimientos.acreditacion.literal.nigNumeroProcedimiento', 'NIG/N� Procedimiento', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.procedimientos.acreditacion.literal.nigNumeroProcedimiento', 'NIG/N� Procedimiento#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.procedimientos.acreditacion.literal.nigNumeroProcedimiento', 'NIG/N� Procedimiento#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.procedimientos.acreditacion.literal.nigNumeroProcedimiento', 'NIG/N� Procedimiento#EU', 0, '3', sysdate, 0, '22');


ALTER TABLE scs_acreditacionprocedimiento ADD (
    NIG_NUMPROCEDIMIENTO NUMBER DEFAULT 0 NOT NULL
);

COMMENT ON COLUMN scs_acreditacionprocedimiento.NIG_NUMPROCEDIMIENTO IS '0: No est� seleccionado el check NIG/N� Procedimiento de la acreditaci�n; 1:Si est� seleccionado el check NIG/N� Procedimiento de la acreditaci�n';


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.volantesExpres.literal.sexo', 'Sexo', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.volantesExpres.literal.sexo', 'Sexo#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.volantesExpres.literal.sexo', 'Sexo#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.volantesExpres.literal.sexo', 'Sexo#EU', 0, '3', sysdate, 0, '22');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.mantActuacion.literal.comisariaJuzgados', 'Comisar�a o Juzgado', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.mantActuacion.literal.comisariaJuzgados', 'Comisar�a o Juzgado#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.mantActuacion.literal.comisariaJuzgados', 'Comisar�a o Juzgado#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.mantActuacion.literal.comisariaJuzgados', 'Comisar�a o Juzgado#EU', 0, '3', sysdate, 0, '22');


insert into CEN_TIPOCAMBIO (IDTIPOCAMBIO, DESCRIPCION, FECHAMODIFICACION, USUMODIFICACION)
values (107, '3400107', sysdate, 0);

insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400107', 'Designaci�n. Modificaci�n de Actuaciones', '1', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.107');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400107', 'Designaci�n. Modificaci�n de Actuaciones#CA', '2', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.107');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400107', 'Designaci�n. Modificaci�n de Actuaciones#EU', '3', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.107');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400107', 'Designaci�n. Modificaci�n de Actuaciones#GL', '4', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.107');


insert into CEN_TIPOCAMBIO (IDTIPOCAMBIO, DESCRIPCION, FECHAMODIFICACION, USUMODIFICACION)
values (108, '3400108', sysdate, 0);

 


insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400108', 'Designaci�n. Eliminaci�n de Actuaciones', '1', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.108');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400108', 'Designaci�n. Eliminaci�n de Actuaciones#CA', '2', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.108');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400108', 'Designaci�n. Eliminaci�n de Actuaciones#EU', '3', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.108');
insert into GEN_RECURSOS_CATALOGOS (IDRECURSO, DESCRIPCION, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDINSTITUCION, NOMBRETABLA, CAMPOTABLA, IDRECURSOALIAS)
values ('3400108', 'Designaci�n. Eliminaci�n de Actuaciones#GL', '4', sysdate, 0, null, 'CEN_TIPOCAMBIO', 'DESCRIPCION', 'cen_tipocambio.descripcion.0.108');

INSERT INTO GEN_PARAMETROS (MODULO,PARAMETRO,VALOR,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,IDRECURSO)
VALUES ('SCS','JUSTIFICACION_EDITAR_ACT_FICHA','0',SYSDATE,0,0,'scs.parametro.scs.justificacionEditarActFicha');
  
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.scs.justificacionEditarActFicha', 'Permitir modificar y borrar actuaciones de designaci�n en la ficha colegial(1:Si, 0:No)', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.scs.justificacionEditarActFicha', 'Permitir modificar y borrar actuaciones de designaci�n en la ficha colegial(1:Si, 0:No)#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.scs.justificacionEditarActFicha', 'Permitir modificar y borrar actuaciones de designaci�n en la ficha colegial(1:Si, 0:No)#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.scs.justificacionEditarActFicha', 'Permitir modificar y borrar actuaciones de designaci�n en la ficha colegial(1:Si, 0:No)#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.desde', 'Fecha Justificaci�n Desde', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.desde', 'Fecha Justificaci�n Desde#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.desde', 'Fecha Justificaci�n Desde#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.desde', 'Fecha Justificaci�n Desde#EU', 0, '3', sysdate, 0, '22');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.hasta', 'Hasta', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.hasta', 'Hasta#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.hasta', 'Hasta#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.hasta', 'Hasta#EU', 0, '3', sysdate, 0, '22');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.origenActuaciones', 'Origen Actuaciones', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.origenActuaciones', 'Origen Actuaciones#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.origenActuaciones', 'Origen Actuaciones#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.origenActuaciones', 'Origen Actuaciones#EU', 0, '3', sysdate, 0, '22');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.fehaActMayorJust', 'La fecha de justificaci�n no puede ser anterior a la fecha de la actuaci�n', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.fehaActMayorJust', 'La fecha de justificaci�n no puede ser anterior a la fecha de la actuaci�n#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.fehaActMayorJust', 'La fecha de justificaci�n no puede ser anterior a la fecha de la actuaci�n#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.fehaActMayorJust', 'La fecha de justificaci�n no puede ser anterior a la fecha de la actuaci�n#EU', 0, '3', sysdate, 0, '19');
 
Modificado PKG_SIGA_FCS_HISTORICO
Modificado PKG_SIGA_FACTURACION_SJCS


 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.informacion', 'La informaci�n de {0} est� {1}', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.informacion', 'La informaci�n de {0} est� {1}#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.informacion', 'La informaci�n de {0} est� {1}#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.informacion', 'La informaci�n de {0} est� {1}#EU', 0, '3', sysdate, 0, '19');

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

-- Ejecutado en Integracion por Adrian el 30/03/2016 10:00 (las ultimas sentencias han sido cambiadas, a�adiendo valor por defecto en las fechas y algo mas)

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.insercionCompletas', 'Existen actuaciones que no se insertar�n debido a que no se ha aportado toda la informaci�n obligatoria.�Desea continuar?', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.insercionCompletas', 'Existen actuaciones que no se insertar�n debido a que no se ha aportado toda la informaci�n obligatoria.�Desea continuar?#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.insercionCompletas', 'Existen actuaciones que no se insertar�n debido a que no se ha aportado toda la informaci�n obligatoria.�Desea continuar?#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.justificacion.insercionCompletas', 'Existen actuaciones que no se insertar�n debido a que no se ha aportado toda la informaci�n obligatoria.�Desea continuar?#EU', 0, '3', sysdate, 0, '19');

-- Ejecutado en Integracion por Adrian el 30/03/2016 12:30

 
update gen_recursos set descripcion='Fecha Justificaci�n' where idRecurso='gratuita.busquedaDesignas.literal.fechaJustificacion.desde' and idLenguaje=1
update gen_recursos set descripcion='Fecha Justificaci�n#CA' where idRecurso='gratuita.busquedaDesignas.literal.fechaJustificacion.desde' and idLenguaje=2
update gen_recursos set descripcion='Fecha Justificaci�n#EU' where idRecurso='gratuita.busquedaDesignas.literal.fechaJustificacion.desde' and idLenguaje=3
update gen_recursos set descripcion='Fecha Justificaci�n#GL' where idRecurso='gratuita.busquedaDesignas.literal.fechaJustificacion.desde' and idLenguaje=4
 
 
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.literalDesde', 'Desde', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.literalDesde', 'Desde#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.literalDesde', 'Desde#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaDesignas.literal.fechaJustificacion.literalDesde', 'Desde#EU', 0, '3', sysdate, 0, '22');
 
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.censo.cargaperiodica.confirmExcel', 'A continuaci�n se generar� un fichero excel con el listado de incidencias de colegiados con el filtro establecido en la consulta. En el informe se muestra la fecha de incorporaci�n del colegiado encontrado por su n�mero de identificaci�n �nicamente.\\n�Desea continuar?', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.censo.cargaperiodica.confirmExcel', 'A continuaci�n se generar� un fichero excel con el listado de incidencias de colegiados con el filtro establecido en la consulta. En el informe se muestra la fecha de incorporaci�n del colegiado encontrado por su n�mero de identificaci�n �nicamente.\\n�Desea continuar?#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.censo.cargaperiodica.confirmExcel', 'A continuaci�n se generar� un fichero excel con el listado de incidencias de colegiados con el filtro establecido en la consulta. En el informe se muestra la fecha de incorporaci�n del colegiado encontrado por su n�mero de identificaci�n �nicamente.\\n�Desea continuar?#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.censo.cargaperiodica.confirmExcel', 'A continuaci�n se generar� un fichero excel con el listado de incidencias de colegiados con el filtro establecido en la consulta. En el informe se muestra la fecha de incorporaci�n del colegiado encontrado por su n�mero de identificaci�n �nicamente.\\n�Desea continuar?#EU', 0, '3', sysdate, 0, '19');

 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Ya existe cuenta SJCS' where idrecurso='messages.censo.existeAbonoSJCS' and idlenguaje='1';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Ya existe cuenta SJCS#CA' where idrecurso='messages.censo.existeAbonoSJCS' and idlenguaje='2';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Ya existe cuenta SJCS#EU' where idrecurso='messages.censo.existeAbonoSJCS' and idlenguaje='3';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Ya existe cuenta SJCS#GL' where idrecurso='messages.censo.existeAbonoSJCS' and idlenguaje='4';
 

nueva funcion F_SIGA_GETFECHAESTADOCOLEGIAL

vista actualizada V_WS_2064_PERSONA

-- Ejecutado en Integracion por Adrian el 18/04/2016 12:30

Package Pkg_Siga_Censo actualizado

-- Ejecutado en Integracion por Adrian el 18/04/2016 15:30

update gen_recursos set descripcion='La fecha de actuacion no puede ser posterior al dia de hoy' where idRecurso='messages.justificacion.fehaActMayorJust' and idLenguaje=1
update gen_recursos set descripcion='La fecha de actuacion no puede ser posterior al dia de hoy#CA' where idRecurso='messages.justificacion.fehaActMayorJust' and idLenguaje=2
update gen_recursos set descripcion='La fecha de actuacion no puede ser posterior al dia de hoy#EU' where idRecurso='messages.justificacion.fehaActMayorJust' and idLenguaje=3
update gen_recursos set descripcion='La fecha de actuacion no puede ser posterior al dia de hoy#GL' where idRecurso='messages.justificacion.fehaActMayorJust' and idLenguaje=4

-- Add/modify columns 
alter table SCS_JUZGADO add VISIBLEMOVIL NUMBER(1) default 1 not null;
alter table Scs_Comisaria add VISIBLEMOVIL NUMBER(1) default 1 not null;
alter table Scs_Turno add VISIBLEMOVIL NUMBER(1) default 1 not null;
alter table Scs_Tipoasistenciacolegio add VISIBLEMOVIL NUMBER(1) default 1 not null;

update gen_parametros set idinstitucion = 0 where parametro = 'CENTRALITAVIRTUA_WS_IPS_CLIENTE_AGC';


insert into gen_parametros values ('ECOM','CENTRALITAVIRTUA_WS_IPS_CLIENTE_AGC','10.60.3.71',SYSDATE,-1,2013,NULL);
insert into gen_parametros values ('ECOM','CENTRALITAVIRTUA_WS_IPS_CLIENTE_AGC','10.60.3.71',SYSDATE,-1,2003,NULL);
insert into gen_parametros values ('ECOM','CENTRALITAVIRTUA_WS_IPS_CLIENTE_AGC','10.60.3.71',SYSDATE,-1,2011,NULL);
insert into gen_parametros values ('ECOM','CENTRALITAVIRTUA_WS_IPS_CLIENTE_AGC','10.60.3.71',SYSDATE,-1,2010,NULL);

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('errors.formato', 'El valor del campo {0} tiene un formato incorrecto', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('errors.formato', 'El valor del campo {0} tiene un formato incorrecto#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('errors.formato', 'El valor del campo {0} tiene un formato incorrecto#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('errors.formato', 'El valor del campo {0} tiene un formato incorrecto#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaSOJ.literal.anyo.designa', 'A�o de la designaci�n', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaSOJ.literal.anyo.designa', 'A�o de la designaci�n#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaSOJ.literal.anyo.designa', 'A�o de la designaci�n#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaSOJ.literal.anyo.designa', 'A�o de la designaci�n#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaSOJ.literal.anyo.ejg', 'A�o del expediente justicia gratuita', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaSOJ.literal.anyo.ejg', 'A�o del expediente justicia gratuita#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaSOJ.literal.anyo.ejg', 'A�o del expediente justicia gratuita#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('gratuita.busquedaSOJ.literal.anyo.ejg', 'A�o del expediente justicia gratuita#EU', 0, '3', sysdate, 0, '19');

update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='La fecha de actuaci�n no puede ser posterior al d�a de hoy' where idrecurso='messages.justificacion.fehaActMayorJust' and idlenguaje='1';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='La fecha de actuaci�n no puede ser posterior al d�a de hoy#GL' where idrecurso='messages.justificacion.fehaActMayorJust' and idlenguaje='4';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='La fecha de actuaci�n no puede ser posterior al d�a de hoy#CA' where idrecurso='messages.justificacion.fehaActMayorJust' and idlenguaje='2';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='La fecha de actuaci�n no puede ser posterior al d�a de hoy#EU' where idrecurso='messages.justificacion.fehaActMayorJust' and idlenguaje='3';


Actualizado Pkg_Siga_Censo


-- Ejecutado en Integracion por Adrian el 26/04/2016 10:00
/* Notas: 
 - El update del parametro no funciono en Integracion porque alguien ya lo hab�a cambiado por BD
 - La personalizacion de parametros repite la misma configuracion que el parametro generico y no tiene recurso asociado, pero el desarrollador no ha querido arreglarlo
*/

insert into gen_recursos values ('gratuita.mantenimientoTablasMaestra.literal.visibilidadMovil', 'Visible Movil', 0, 1, sysdate, 0, 19);
insert into gen_recursos values ('gratuita.mantenimientoTablasMaestra.literal.visibilidadMovil', 'Visible Movil#CA', 0, 2, sysdate, 0, 19);
insert into gen_recursos values ('gratuita.mantenimientoTablasMaestra.literal.visibilidadMovil', 'Visible Movil#EU', 0, 3, sysdate, 0, 19);
insert into gen_recursos values ('gratuita.mantenimientoTablasMaestra.literal.visibilidadMovil', 'Visible Movil#GL', 0, 4, sysdate, 0, 19);

-- Ejecutado en Integracion por Adrian el 26/04/2016 14:00

Actualizado Pkg_Siga_Censo

-- Ejecutado en Integracion por Adrian el 28/04/2016 17:30

