insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fecha.error.superiorA', 'La fecha introducida debe ser superior a la fecha', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fecha.error.superiorA', 'La fecha introducida debe ser superior a la fecha#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fecha.error.superiorA', 'La fecha introducida debe ser superior a la fecha#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fecha.error.superiorA', 'La fecha introducida debe ser superior a la fecha#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fecha.error.superiorIgualA', 'La fecha introducida debe ser superior o igual a la fecha', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fecha.error. superiorIgualA', 'La fecha introducida debe ser superior o igual a la fecha#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fecha.error. superiorIgualA', 'La fecha introducida debe ser superior o igual a la fecha#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('fecha.error. superiorIgualA', 'La fecha introducida debe ser superior o igual a la fecha#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cen.consultaProductos.literal.fechaAlta', 'Fecha Alta', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cen.consultaProductos.literal.fechaAlta', 'Fecha Alta#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cen.consultaProductos.literal.fechaAlta', 'Fecha Alta#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cen.consultaProductos.literal.fechaAlta', 'Fecha Alta#EU', 0, '3', sysdate, 0, '19');

update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Regenerar PDF' where idrecurso='facturacion.datosFactura.boton.DescargarFacturaPDF' and idlenguaje='1';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Regenerar PDF#GL' where idrecurso='facturacion.datosFactura.boton.DescargarFacturaPDF' and idlenguaje='4';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Regenerar PDF#CA' where idrecurso='facturacion.datosFactura.boton.DescargarFacturaPDF' and idlenguaje='2';
 update GEN_RECURSOS set fechamodificacion=sysdate, DESCRIPCION='Regenerar PDF#EU' where idrecurso='facturacion.datosFactura.boton.DescargarFacturaPDF' and idlenguaje='3';

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.datosGenerales.literal.tipoGenerica', 'Genérica', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.datosGenerales.literal.tipoGenerica', 'Genérica#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.datosGenerales.literal.tipoGenerica', 'Genérica#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.datosGenerales.literal.tipoGenerica', 'Genérica#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seriesFacturacion.tipoGenerica.ayuda', 'Una serie de facturación genérica se considera que contiene a todo el censo como población, y por lo tanto, no hay que comprobar la población al generar la facturación', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seriesFacturacion.tipoGenerica.ayuda', 'Una serie de facturación genérica se considera que contiene a todo el censo como población, y por lo tanto, no hay que comprobar la población al generar la facturación#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seriesFacturacion.tipoGenerica.ayuda', 'Una serie de facturación genérica se considera que contiene a todo el censo como población, y por lo tanto, no hay que comprobar la población al generar la facturación#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seriesFacturacion.tipoGenerica.ayuda', 'Una serie de facturación genérica se considera que contiene a todo el censo como población, y por lo tanto, no hay que comprobar la población al generar la facturación#EU', 0, '3', sysdate, 0, '19');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.deleted.impugnacion', 'Cambios realizados correctamente, se ha eliminado el estado de Resuelta Impugnación', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.deleted.impugnacion', 'Cambios realizados correctamente, se ha eliminado el estado de Resuelta Impugnación#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.deleted.impugnacion', 'Cambios realizados correctamente, se ha eliminado el estado de Resuelta Impugnación#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.deleted.impugnacion', 'Cambios realizados correctamente, se ha eliminado el estado de Resuelta Impugnación#EU', 0, '3', sysdate, 0, '19');


 
Modificado PKG_SIGA_FACTURACION;
Modificado PKG_SIGA_FACTURACION_SJCS;
Modificado PKG_SIGA_CARGOS;
Modificado PKG_SERVICIOS_AUTOMATICOS;

ALTER TABLE PYS_SUSCRIPCION
ADD FECHABAJAFACTURACION DATE; 

UPDATE PYS_SUSCRIPCION
SET FECHABAJAFACTURACION = FECHABAJA
WHERE FECHABAJA IS NOT NULL;

CREATE TABLE FAC_FACTURACIONSUSCRIPCION_ANU (
    IDINSTITUCION NUMBER (4) NOT NULL,
    IDFACTURA VARCHAR2 (10 BYTE) NOT NULL,
    NUMEROLINEA NUMBER (10) NOT NULL,
    IDTIPOSERVICIOS NUMBER (4) NOT NULL,
    IDSERVICIO NUMBER (10) NOT NULL,
    IDSERVICIOSINSTITUCION NUMBER (10) NOT NULL,
    IDSUSCRIPCION NUMBER (10) NOT NULL,
    IDFACTURACIONSUSCRIPCION NUMBER (5) NOT NULL,
    FECHAINICIO DATE NOT NULL,
    FECHAFIN DATE NOT NULL,
    FECHAMODIFICACION DATE NOT NULL,
    USUMODIFICACION NUMBER (5) NOT NULL,
    DESCRIPCION VARCHAR2 (150 BYTE)
    );

nueva funcion F_SIGA_GET_IDULTIMOESTADOCajg
alter table SCS_PONENTE add fechabaja date;
UPDATE gen_tablas_maestras SET ACEPTABAJA = 1 where idtablamaestra = 'SCS_PONENTE';

UPDATE SCS_MAESTROESTADOSEJG SET ORDEN = 5 WHERE IDESTADOEJG = 0;
INSERT INTO SCS_MAESTROESTADOSEJG( IDESTADOEJG, CODIGOEXT, DESCRIPCION, USUMODIFICACION, FECHAMODIFICACION,BLOQUEADO,ORDEN,VISIBLECOMISION,CODIGOEJIS ) VALUES ( 23 , 100 , 150023 , 1 ,  SYSTIMESTAMP,'S',0,0,10 );  
INSERT INTO GEN_RECURSOS_CATALOGOS( CAMPOTABLA, DESCRIPCION, FECHAMODIFICACION, IDINSTITUCION, IDLENGUAJE, IDRECURSO, NOMBRETABLA, USUMODIFICACION, IDRECURSOALIAS ) VALUES ( 'DESCRIPCION' , 'Solicitud en proceso de Alta' ,  SYSTIMESTAMP ,  NULL , 1 , 150023 , 'SCS_MAESTROESTADOSEJG' , 1 , 'scs_maestroestadosejg.descripcion.0.23' );  
INSERT INTO GEN_RECURSOS_CATALOGOS( CAMPOTABLA, DESCRIPCION, FECHAMODIFICACION, IDINSTITUCION, IDLENGUAJE, IDRECURSO, NOMBRETABLA, USUMODIFICACION, IDRECURSOALIAS ) VALUES ( 'DESCRIPCION' , 'Solicitud en proceso de Alta#CA' ,  SYSTIMESTAMP ,  NULL , 2 , 150023 , 'SCS_MAESTROESTADOSEJG' , 1 , 'scs_maestroestadosejg.descripcion.0.23' )  ;
INSERT INTO GEN_RECURSOS_CATALOGOS( CAMPOTABLA, DESCRIPCION, FECHAMODIFICACION, IDINSTITUCION, IDLENGUAJE, IDRECURSO, NOMBRETABLA, USUMODIFICACION, IDRECURSOALIAS ) VALUES ( 'DESCRIPCION' , 'Solicitud en proceso de Alta#EU' ,  SYSTIMESTAMP ,  NULL , 3 , 150023 , 'SCS_MAESTROESTADOSEJG' , 1 , 'scs_maestroestadosejg.descripcion.0.23' )  ;
INSERT INTO GEN_RECURSOS_CATALOGOS( CAMPOTABLA, DESCRIPCION, FECHAMODIFICACION, IDINSTITUCION, IDLENGUAJE, IDRECURSO, NOMBRETABLA, USUMODIFICACION, IDRECURSOALIAS ) VALUES ( 'DESCRIPCION' , 'Solicitud en proceso de Alta#GL' ,  SYSTIMESTAMP ,  NULL , 4 , 150023 , 'SCS_MAESTROESTADOSEJG' , 1 , 'scs_maestroestadosejg.descripcion.0.23' )  ;


SCS_EJG_AUR
SCS_EJG_AIR


declare

  cursor c_aux is
    select idinstitucion from cen_institucion where idlenguaje = 1
        and idinstitucion between 2001 and 2099
    ;
 
begin

  for rec in c_aux loop
    begin
    
      insert into scs_estadoejg
        (idinstitucion,
         idtipoejg,
         anio,
         numero,
         idestadoejg,
         fechainicio,
         fechamodificacion,
         usumodificacion,
         observaciones,
         idestadoporejg,
         automatico)
      
        (select e.idinstitucion,
                e.idtipoejg,
                e.anio,
                e.numero,
                23,
                e.fechaapertura,
                sysdate,
                0,
                'Nuevo Expediente',
                -1,
                '1'
                
           from scs_ejg e
          where e.idinstitucion = rec.idinstitucion);
    
    exception
      when others then
        dbms_output.put_line('Error=' || sqlerrm);
        rollback;
    end;
    commit;
  end loop;

end;
/


     

     
     
     

declare

  cursor c_aux is
    select idinstitucion from cen_institucion where idlenguaje = 2
        and idinstitucion between 2001 and 2099;
 
begin

  for rec in c_aux loop
    begin
    
      insert into scs_estadoejg
        (idinstitucion,
         idtipoejg,
         anio,
         numero,
         idestadoejg,
         fechainicio,
         fechamodificacion,
         usumodificacion,
         observaciones,
         idestadoporejg,
         automatico)
      
        (select e.idinstitucion,
                e.idtipoejg,
                e.anio,
                e.numero,
                23,
                e.fechaapertura,
                sysdate,
                0,
                'Nou Expedient',
                -1,
                '1'
                
           from scs_ejg e
          where e.idinstitucion = rec.idinstitucion);
    
    exception
      when others then
        dbms_output.put_line('Error=' || sqlerrm);
        rollback;
    end;
    commit;
  end loop;

end;
/

  insert into ADM_TIPOINFORME (IDTIPOINFORME, DESCRIPCION, IDTIPOINFORMEPADRE, TIPOFORMATO, FECHAMODIFICACION, USUMODIFICACION, CLASE, DIRECTORIO)
values ('DCAJG', 'Documentación CAJG', null, 'W',sysdaTe, 0, 'G', 'documentacionCAJG');

INSERT INTO ADM_INFORME( ALIAS, DESCRIPCION, FECHAMODIFICACION, IDINSTITUCION, IDPLANTILLA, IDTIPOINFORME, NOMBREFISICO, NOMBRESALIDA, DIRECTORIO, USUMODIFICACION, PRESELECCIONADO, VISIBLE, ASOLICITANTES, ACONTRARIOS, DESTINATARIOS, TIPOFORMATO, CODIGO, ORDEN, IDTIPOINTERCAMBIOTELEMATICO, CLASEJAVA, PLANTILLA, GENERARINFORMESINDIRECCION ) VALUES ( 'Documentacion CAJG' , 'Documentacion CAJG' ,  SYSTIMESTAMP , 0 , SEQ_ADMINFORME.NEXTVAL , 'DCAJG' , 'Documentacion_CAJG' , 'Documentacion_CAJG' , 'documentacionCAJG' , 1 , 'S' , 'S' , 'S' , 'S' , 'C' , 'W' ,  NULL , 1 ,  NULL ,  NULL ,  NULL , 'S' )  ;
alter table SCS_DOCUMENTACIONEJG add COMISIONAJG number(1) default 0;

INSERT INTO GEN_PARAMETROS (MODULO,PARAMETRO,VALOR,FECHAMODIFICACION,USUMODIFICACION,IDINSTITUCION,IDRECURSO)
VALUES ('SCS','CAJG_SUFIJO_ACTAS','-',SYSDATE,0,0,'scs.parametro.cajg.sufijoActas');

 
 
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.cajg.sufijoActas', 'Listado de sufijos que se añadirá a las actas de la comision separados por comas(Ej: IB,MA,ME)', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.cajg.sufijoActas', 'Listado de sufijos que se añadirá a las actas de la comision separados por comas(Ej: IB,MA,ME)#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.cajg.sufijoActas', 'Listado de sufijos que se añadirá a las actas de la comision separados por comas(Ej: IB,MA,ME)#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('scs.parametro.cajg.sufijoActas', 'Listado de sufijos que se añadirá a las actas de la comision separados por comas(Ej: IB,MA,ME)#EU', 0, '3', sysdate, 0, '19');

alter table SCS_ACTACOMISION add NUMACTA varchar2(10);
UPDATE SCS_ACTACOMISION SET NUMACTA = NUMEROACTA;
drop index I1_SCS_ACTACOMISION;
alter table SCS_ACTACOMISION drop column NUMEROACTA;
alter table SCS_ACTACOMISION rename column NUMACTA to NUMEROACTA;
create index I1_SCS_ACTACOMISION on SCS_ACTACOMISION (idinstitucion, anioacta, numeroacta);


Nueva tabla SCS_EJG_RESOLUCION (Faltan Fks, importante mover los clobs de tablespace)
create table SCS_EJG_RESOLUCION
(
  ANIO                     NUMBER(4) not null,
  NUMERO                   NUMBER(10) not null,
  IDINSTITUCION            NUMBER(4) not null,
  IDTIPOEJG                NUMBER(3) not null,
  
  FECHAMODIFICACION        DATE not null,
  USUMODIFICACION          NUMBER(5) not null,
    
  ANIOCAJG                 NUMBER(4),
  NUMERO_CAJG              VARCHAR2(20),
  IDORIGENCAJG             NUMBER(3),
    
  IDACTA                   NUMBER(10),
  IDINSTITUCIONACTA        NUMBER(4),
  ANIOACTA                 NUMBER(4),
  
  IDPONENTE                NUMBER(5),
  FECHAPRESENTACIONPONENTE DATE,
  IDINSTITUCIONPONENTE     NUMBER(4),
  
  FECHARESOLUCIONCAJG      DATE,
  IDTIPORATIFICACIONEJG    NUMBER(3),
  IDFUNDAMENTOJURIDICO     NUMBER(3),
    
  FECHANOTIFICACION        DATE,
  REFAUTO                  VARCHAR2(250),
  
  FECHARATIFICACION        DATE,
  
  REQUIERENOTIFICARPROC    VARCHAR2(1),
  TURNADORATIFICACION      VARCHAR2(1),
    
  DOCRESOLUCION            VARCHAR2(250),
     
  RATIFICACIONDICTAMEN     CLOB,
  NOTASCAJG			       CLOB
)
tablespace TS_SIGA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1
    next 8
    minextents 1
    maxextents unlimited
    pctincrease 0
  );
 -- Add comments to the columns 
comment on column SCS_EJG_RESOLUCION.FECHARESOLUCIONCAJG
  is 'Fecha de Resolucion';
comment on column SCS_EJG_RESOLUCION.IDTIPORATIFICACIONEJG
  is 'Resolucion';
comment on column SCS_EJG_RESOLUCION.IDFUNDAMENTOJURIDICO
  is 'Fundamento juridico';
comment on column SCS_EJG_RESOLUCION.FECHARATIFICACION
  is 'Fecha resolucion firme';
comment on column SCS_EJG_RESOLUCION.RATIFICACIONDICTAMEN
  is 'Equivale a Observaciones resolucion de SIGA';
  
-- Create/Recreate primary, unique and foreign key constraints 
alter table SCS_EJG_RESOLUCION
  add constraint SCS_EJG_RESOLUCION primary key (ANIO, NUMERO, IDINSTITUCION, IDTIPOEJG)
  using index 
  tablespace TS_SIGA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 1M
    next 1M
    minextents 1
    maxextents unlimited
    pctincrease 0
  );

alter table SCS_EJG_RESOLUCION move lob (RATIFICACIONDICTAMEN) STORE  AS  (tablespace TS_SIGA_SCS_LOB) ;
alter table SCS_EJG_RESOLUCION move lob (NOTASCAJG) STORE  AS  (tablespace TS_SIGA_SCS_LOB) ;
Nueva tabla SCS_EJG_ACTA
-- Create/Recreate indexes 
create index IX_ejgacta_acta on SCS_EJG_ACTA (idacta, idinstitucionacta, anioacta);

insert into scs_ejg_acta
  (idacta,
   idinstitucionacta,
   anioacta,
   idinstitucionejg,
   idtipoejg,
   anioejg,
   numeroejg,
   idtiporatificacionejg,
   idfundamentojuridico,
   fechamodificacion,
   usumodificacion)
(select ejg.idacta,
       ejg.idinstitucionacta,
       ejg.anioacta,
       
       ejg.idinstitucion,
       ejg.idtipoejg,
       ejg.anio,
       
       ejg.numero,
       
       ejg.idtiporatificacionejg,
       ejg.idfundamentojuridico,
       ejg.fechamodificacion,
       ejg.usumodificacion
  from scs_ejg ejg
 where ejg.idacta is not null);

-- Create/Recreate indexes 
create index I2_SCS_ACTACOMISION on SCS_ACTACOMISION (idinstitucion, numeroacta);
create index I3_SCS_ACTACOMISION on SCS_ACTACOMISION (idinstitucion, anioacta);
DROP INDEX UK_PRODUCTOSINSTIT;

ALTER TABLE PYS_PRODUCTOSINSTITUCION
MODIFY DESCRIPCION VARCHAR2(1000);

ALTER TABLE PYS_COMPRA
MODIFY DESCRIPCION VARCHAR2(1000);

ALTER TABLE FAC_LINEAFACTURA
MODIFY DESCRIPCION VARCHAR2(1000);

ALTER TABLE FAC_LINEAABONO
MODIFY DESCRIPCIONLINEA VARCHAR2(1000);


CREATE UNIQUE INDEX UK_PRODUCTOSINSTIT ON PYS_PRODUCTOSINSTITUCION (IDINSTITUCION, UPPER("DESCRIPCION"))
LOGGING
TABLESPACE TS_SIGA_FAC_IDX
PCTFREE    10
INITRANS   2
MAXTRANS   255
STORAGE    (
            INITIAL          384K
            NEXT             1M
            MINEXTENTS       1
            MAXEXTENTS       UNLIMITED
            PCTINCREASE      0
            BUFFER_POOL      DEFAULT
           );

-- Add/modify columns 
alter table SCS_ACTACOMISION add OBSERVACIONESCLOB clob;
alter table SCS_ACTACOMISION move lob (OBSERVACIONESCLOB) STORE  AS  (tablespace TS_SIGA_SCS_LOB) ;
alter index PK_SCS_ACTACOMISION rebuild;
--UPdate rows
UPDATE SCS_ACTACOMISION SET OBSERVACIONESCLOB = OBSERVACIONES;
-- Drop columns 
alter table SCS_ACTACOMISION drop column OBSERVACIONES;
-- Add/modify columns 
alter table SCS_ACTACOMISION rename column OBSERVACIONESCLOB to OBSERVACIONES;

Drop Materialized View v_Censo_Letrados_Oojj;
create materialized view V_CENSO_LETRADOS_OOJJ
refresh force on demand
as
Select Cen_Persona.Idpersona As Id_Letrado,
       Upper(Cen_Persona.Nombre) As Nombre,
       Upper(Cen_Persona.Apellidos1) As Apellido1,
       Upper(Cen_Persona.Apellidos2) As Apellido2,
       Cen_Persona.Nifcif As Num_Doc,
       Cen_Persona.Idtipoidentificacion As Idtipoidentificacion,
       Cen_Direcciones.Domicilio As Dir_Profesional,
       Decode(Cen_Direcciones.Idpoblacion,
              Null,
              Decode(Cen_Direcciones.Idpais,
                     Null,
                     Cen_Direcciones.Poblacionextranjera,
                     191,
                     Cen_Direcciones.Poblacionextranjera,
                     Decode(Cen_Direcciones.Poblacionextranjera,
                            Null,
                            '',
                            Cen_Direcciones.Poblacionextranjera || '-' ||
                            (Select Pais.Nombre
                               From Cen_Pais Pais
                              Where Pais.Idpais = Cen_Direcciones.Idpais))),
              (Select Pob.Nombre
                 From Cen_Poblaciones Pob
                Where Pob.Idpoblacion = Cen_Direcciones.Idpoblacion)) As Poblacion,
       Cen_Direcciones.Idpoblacion,
       Cen_Direcciones.Idprovincia,
       Cen_Direcciones.Codigopostal As Cod_Postal,
       Cen_Direcciones.Telefono1 As Telefono,
       Cen_Direcciones.Fax1 As Fax,
       Cen_Direcciones.Movil As Movil,
       Cen_Direcciones.Correoelectronico As Mail,
       Cen_Persona.Sexo,
       Cen_Direcciones.Idpais
  From Cen_Persona, Cen_Direcciones, Cen_Cliente, Cen_Direccion_Tipodireccion
 Where Cen_Persona.Idpersona = Cen_Cliente.Idpersona
   And Cen_Cliente.Idpersona = Cen_Direcciones.Idpersona
   And Cen_Cliente.Idinstitucion = Cen_Direcciones.Idinstitucion
   And Cen_Direcciones.Idpersona = Cen_Direccion_Tipodireccion.Idpersona
   And Cen_Direcciones.Idinstitucion = Cen_Direccion_Tipodireccion.Idinstitucion
   And Cen_Direcciones.Iddireccion = Cen_Direccion_Tipodireccion.Iddireccion
   And Cen_Direccion_Tipodireccion.Idtipodireccion = 9
   And Cen_Direcciones.Iddireccion =
       (Select Max(Cen_Direcciones.Iddireccion)
          From Cen_Direcciones, Cen_Direccion_Tipodireccion
         Where Cen_Direcciones.Idinstitucion = Cen_Direccion_Tipodireccion.Idinstitucion
           And Cen_Direcciones.Iddireccion = Cen_Direccion_Tipodireccion.Iddireccion
           And Cen_Direcciones.Idpersona = Cen_Direccion_Tipodireccion.Idpersona
           And Cen_Direccion_Tipodireccion.Idtipodireccion = 9
           And Cen_Direcciones.Idinstitucion = Cen_Cliente.Idinstitucion
           And Cen_Direcciones.Idpersona = Cen_Cliente.Idpersona
           And Cen_Direcciones.Fechabaja Is Null)
   And Cen_Direcciones.Fechabaja Is Null
   And Cen_Cliente.Letrado = '1'
   And Cen_Cliente.Idinstitucion = 2000
   And Not Exists
 (Select 1
          From Cen_Sancion
         Where Cen_Sancion.Idpersona = Cen_Cliente.Idpersona
           And Idtiposancion In (4, 8)
           And Nvl(Cen_Sancion.Chkrehabilitado, '0') = '0'
           And Trunc(Nvl(Cen_Sancion.Fecharehabilitado, '31/12/9999')) >= Trunc(Sysdate)
           And Trunc(Sysdate) Between Trunc(Nvl(Fechainicio, '01/01/1900')) And
               Trunc(Nvl(Fechafin, '31/12/9999')))
   And Cen_Cliente.Idpersona In --Exists angel
       (Select d.Idpersona
          From Cen_Datoscolegialesestado d
         Where d.Idpersona = Cen_Persona.Idpersona
           And d.Fechaestado = (Select Max(Fechaestado)
                                  From Cen_Datoscolegialesestado
                                 Where Idinstitucion = d.Idinstitucion
                                   And Idpersona = d.Idpersona
                                   And Trunc(Fechaestado) <= Trunc(Sysdate))
           And d.Idestado = 20)
Union All
Select Cen_Persona.Idpersona As Id_Letrado,
       Upper(Cen_Persona.Nombre) Nombre,
       Upper(Cen_Persona.Apellidos1) Apellidos1,
       Upper(Cen_Persona.Apellidos2) Apellidos2,
       Cen_Persona.Nifcif Num_Doc,
       Cen_Persona.Idtipoidentificacion As Idtipoidentificacion,
       '' Dir_Profesional,
       '' Poblacion,
       '' Idpoblacion,
       '' Idprovincia,
       '' Cod_Postal,
       '' Telefono,
       '' Fax,
       '' Movil,
       '' Mail,
       '' Sexo,
       '' Idpais
  From Cen_Persona, Cen_Cliente
 Where Cen_Persona.Idpersona = Cen_Cliente.Idpersona
   And Not Exists
 (Select 1
          From Cen_Direcciones, Cen_Direccion_Tipodireccion
         Where Cen_Direcciones.Idinstitucion = Cen_Direccion_Tipodireccion.Idinstitucion
           And Cen_Direcciones.Iddireccion = Cen_Direccion_Tipodireccion.Iddireccion
           And Cen_Direcciones.Idpersona = Cen_Direccion_Tipodireccion.Idpersona
           And Cen_Direccion_Tipodireccion.Idtipodireccion = 9
           And Cen_Direcciones.Idinstitucion = Cen_Cliente.Idinstitucion
           And Cen_Direcciones.Idpersona = Cen_Cliente.Idpersona
           And Cen_Direcciones.Fechabaja Is Null)
   And Cen_Cliente.Letrado = '1'
   And Cen_Cliente.Idinstitucion = 2000
   And Not Exists
 (Select 1
          From Cen_Sancion
         Where Cen_Sancion.Idpersona = Cen_Cliente.Idpersona
           And Idtiposancion In (4, 8)
           And Nvl(Cen_Sancion.Chkrehabilitado, '0') = '0'
           And Trunc(Nvl(Cen_Sancion.Fecharehabilitado, '31/12/9999')) >= Trunc(Sysdate)
           And Trunc(Sysdate) Between Trunc(Nvl(Fechainicio, '01/01/1900')) And
               Trunc(Nvl(Fechafin, '31/12/9999')))
   And Cen_Persona.Idpersona In
       (Select d.Idpersona
          From Cen_Datoscolegialesestado d
         Where d.Idpersona = Cen_Persona.Idpersona
           And d.Fechaestado = (Select Max(Fechaestado)
                                  From Cen_Datoscolegialesestado
                                 Where Idinstitucion = d.Idinstitucion
                                   And Idpersona = d.Idpersona
                                   And Trunc(Fechaestado) <= Trunc(Sysdate))
           And d.Idestado = 20);
/

Exportar pkg_siga_censo
Drop MATERIALIZED VIEW V_CENSO_LETRADOS;
CREATE MATERIALIZED VIEW V_CENSO_LETRADOS
REFRESH FORCE ON DEMAND
AS
Select Cen_Persona.Idpersona As Id_Letrado,
       Translate(Upper(Cen_Persona.Nombre), 'a??s?', 'aeiou') As Nombre,
       Translate(Upper(Cen_Persona.Apellidos1), 'a??s?', 'aeiou') As Apellido1,
       Translate(Upper(Cen_Persona.Apellidos2), 'a??s?', 'aeiou') As Apellido2,
       Cen_Persona.Nifcif As Num_Doc,
       Cen_Persona.Idtipoidentificacion As Idtipoidentificacion,
       Cen_Direcciones.Domicilio As Dir_Profesional,
       Nvl(Cen_Poblaciones.Nombre, Cen_Direcciones.Poblacionextranjera) ||
       Decode(Cen_Direcciones.Idpais, Null, Null, 191, Null, '-' || Cen_Pais.Nombre) As Poblacion,
       Cen_Direcciones.Idpoblacion,
       Cen_Direcciones.Idprovincia,
       Cen_Provincias.Nombre As Provincia,
       Cen_Direcciones.Codigopostal As Cod_Postal,
       Cen_Direcciones.Telefono1 As Telefono,
       Cen_Direcciones.Fax1 As Fax,
       Cen_Direcciones.Movil As Movil,
       Cen_Direcciones.Correoelectronico As Mail,
       Cen_Persona.Sexo,
       Cen_Direcciones.Idpais,
       f_Siga_Getrecurso(Cen_Pais.Nombre, 1) Pais,
       Greatest(Cen_Persona.Fechamodificacion,
                Cen_Direcciones.Fechamodificacion,
                Cen_Direccion_Tipodireccion.Fechamodificacion) As Fechamodificacion
  From Cen_Persona,
       Cen_Direcciones,
       Cen_Poblaciones,
       Cen_Provincias,
       Cen_Pais,
       Cen_Cliente,
       Cen_Direccion_Tipodireccion
 Where Cen_Persona.Idpersona = Cen_Cliente.Idpersona
   And Cen_Cliente.Idpersona = Cen_Direcciones.Idpersona
   And Cen_Cliente.Idinstitucion = Cen_Direcciones.Idinstitucion
   And Cen_Direcciones.Idpoblacion = Cen_Poblaciones.Idpoblacion(+)
   And Cen_Direcciones.Idprovincia = Cen_Provincias.Idprovincia(+)
   And Cen_Direcciones.Idpais = Cen_Pais.Idpais(+)
   And Cen_Direcciones.Idpersona = Cen_Direccion_Tipodireccion.Idpersona
   And Cen_Direcciones.Idinstitucion = Cen_Direccion_Tipodireccion.Idinstitucion
   And Cen_Direcciones.Iddireccion = Cen_Direccion_Tipodireccion.Iddireccion
   And Cen_Direccion_Tipodireccion.Idtipodireccion = 3
   And Cen_Direcciones.Iddireccion =
       (Select Max(Cen_Direcciones.Iddireccion)
          From Cen_Direcciones, Cen_Direccion_Tipodireccion
         Where Cen_Direcciones.Idinstitucion = Cen_Direccion_Tipodireccion.Idinstitucion
           And Cen_Direcciones.Iddireccion = Cen_Direccion_Tipodireccion.Iddireccion
           And Cen_Direcciones.Idpersona = Cen_Direccion_Tipodireccion.Idpersona
           And Cen_Direccion_Tipodireccion.Idtipodireccion = 3
           And Cen_Direcciones.Idinstitucion = Cen_Cliente.Idinstitucion
           And Cen_Direcciones.Idpersona = Cen_Cliente.Idpersona
           And Cen_Direcciones.Fechabaja Is Null)
   And Cen_Direcciones.Fechabaja Is Null
   And '0' = (Select Max(Nvl(Cli.Noaparecerredabogacia, '0'))
                From Cen_Cliente Cli
               Where Cli.Idpersona = Cen_Cliente.Idpersona) -- Solo se pone direccion cuando no hay check LOPD activo
   And Cen_Cliente.Letrado = '1'
   And Cen_Cliente.Idinstitucion = 2000
   And Not Exists
 (Select 1
          From Cen_Sancion
         Where Cen_Sancion.Idpersona = Cen_Cliente.Idpersona
           And Idtiposancion = 4
           And Nvl(Cen_Sancion.Chkrehabilitado, '0') = '0'
           And Trunc(Nvl(Cen_Sancion.Fecharehabilitado, '31/12/9999')) >= Trunc(Sysdate)
           And Trunc(Sysdate) Between Trunc(Nvl(Fechainicio, '01/01/1900')) And
               Trunc(Nvl(Fechafin, '31/12/9999')))
   And Exists
 (Select d.Idpersona
          From Cen_Datoscolegialesestado d
         Where d.Idpersona = Cen_Persona.Idpersona
           And d.Fechaestado = (Select Max(Fechaestado)
                                  From Cen_Datoscolegialesestado
                                 Where Idinstitucion = d.Idinstitucion
                                   And Idpersona = d.Idpersona
                                   And Trunc(Fechaestado) <= Trunc(Sysdate))
           And d.Idestado In (10, 20))
Union
Select Cen_Persona.Idpersona As Id_Letrado,
       Translate(Upper(Cen_Persona.Nombre), 'a??s?', 'aeiou') Nombre,
       Translate(Upper(Cen_Persona.Apellidos1), 'a??s?', 'aeiou') Apellidos1,
       Translate(Upper(Cen_Persona.Apellidos2), 'a??s?', 'aeiou') Apellidos2,
       Cen_Persona.Nifcif Num_Doc,
       Cen_Persona.Idtipoidentificacion As Idtipoidentificacion,
       '' Dir_Profesional,
       '' Poblacion,
       '' Idpoblacion,
       '' Idprovincia,
       '' Provincia,
       '' Cod_Postal,
       '' Telefono,
       '' Fax,
       '' Movil,
       '' Mail,
       '' Sexo,
       '' Idpais,
       '' Pais,
       Cen_Persona.Fechamodificacion
  From Cen_Persona, Cen_Cliente
 Where Cen_Persona.Idpersona = Cen_Cliente.Idpersona
   And Not Exists((Select 1
                     From Cen_Direcciones, Cen_Direccion_Tipodireccion
                    Where Cen_Direcciones.Idinstitucion = Cen_Direccion_Tipodireccion.Idinstitucion
                      And Cen_Direcciones.Iddireccion = Cen_Direccion_Tipodireccion.Iddireccion
                      And Cen_Direcciones.Idpersona = Cen_Direccion_Tipodireccion.Idpersona
                      And Cen_Direccion_Tipodireccion.Idtipodireccion = 3
                      And Cen_Direcciones.Idinstitucion = Cen_Cliente.Idinstitucion
                      And Cen_Direcciones.Idpersona = Cen_Cliente.Idpersona
                      And Cen_Direcciones.Fechabaja Is Null)
               Or '1' = (Select Max(Nvl(Cli.Noaparecerredabogacia, '0'))
                           From Cen_Cliente Cli
                          Where Cli.Idpersona = Cen_Cliente.Idpersona)) -- Solo se pone direccion cuando no hay check LOPD activo
   And Cen_Cliente.Letrado = '1'
   And Cen_Cliente.Idinstitucion = 2000
   And Not Exists
 (Select 1
          From Cen_Sancion
         Where Cen_Sancion.Idpersona = Cen_Cliente.Idpersona
           And Idtiposancion = 4
           And Nvl(Cen_Sancion.Chkrehabilitado, '0') = '0'
           And Trunc(Nvl(Cen_Sancion.Fecharehabilitado, '31/12/9999')) >= Trunc(Sysdate)
           And Trunc(Sysdate) Between Trunc(Nvl(Fechainicio, '01/01/1900')) And
               Trunc(Nvl(Fechafin, '31/12/9999')))
   And Exists
 (Select d.Idpersona
          From Cen_Datoscolegialesestado d
         Where d.Idpersona = Cen_Persona.Idpersona
           And d.Fechaestado = (Select Max(Fechaestado)
                                  From Cen_Datoscolegialesestado
                                 Where Idinstitucion = d.Idinstitucion
                                   And Idpersona = d.Idpersona
                                   And Trunc(Fechaestado) <= Trunc(Sysdate))
           And d.Idestado In (10, 20));
/



-- Pestaña Plantilla
 insert into gen_procesos 
(IDPROCESO, IDMODULO, TRAZA, TARGET, FECHAMODIFICACION, USUMODIFICACION, DESCRIPCION,TRANSACCION,IDPARENT,NIVEL) 
            values ('62c','CER',0,'Y',sysdate,0,'Relacionar Plantillas','CER_Plantillas','62b',10);


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.plantillas.relaciones', 'Plantilla', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.plantillas.relaciones', 'Plantilla#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.plantillas.relaciones', 'Plantilla#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('pestana.certificados.plantillas.relaciones', 'Plantilla#EU', 0, '3', sysdate, 0, '22');


 insert into gen_pestanas
 (IDPROCESO, IDLENGUAJE, IDRECURSO,POSICION,IDGRUPO)
 values
 ('62c',1,'pestana.certificados.plantillas.relaciones',1,'PLANTILLA');


INSERT INTO ADM_TIPOSACCESO (IDPROCESO, IDPERFIL, FECHAMODIFICACION, USUMODIFICACION, DERECHOACCESO, IDINSTITUCION) 
(SELECT '62c', IDPERFIL, SYSDATE, 0, DERECHOACCESO, IDINSTITUCION FROM ADM_TIPOSACCESO WHERE IDPROCESO = '62b');


-- Create table
create table CER_RELACION_PLANTILLAS
(
  IDPLANTILLA           NUMBER(2) not null,
  IDPLANTILLAPADRE      NUMBER(2) not null,
  IDINSTITUCION         NUMBER(4) not null,
  IDTIPOPRODUCTO        NUMBER(4) not null,
  IDPRODUCTO            NUMBER(10) not null,
  IDPRODUCTOINSTITUCION NUMBER(10) not null,
 
  FECHA_ALTA     DATE not null,
  USU_ALTA       NUMBER(5) not null 
)
tablespace TS_SIGA_FAC
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 1M
    next 1M
    minextents 1
    maxextents unlimited
  );

  alter table cer_relacion_plantillas
  add constraint PK_CER_RELACION_PLANTILLAS primary key (IDPLANTILLA, IDPLANTILLAPADRE,IDINSTITUCION, IDTIPOPRODUCTO, IDPRODUCTO, IDPRODUCTOINSTITUCION)
  deferrable
  using index 
  tablespace TS_SIGA_FAC_IDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
  
 alter table cer_relacion_plantillas
 add constraint FK_CER_RELACION_PLANTILLA foreign key (IDPLANTILLA, IDINSTITUCION, IDTIPOPRODUCTO,IDPRODUCTO,IDPRODUCTOINSTITUCION)
 references cer_plantillas (IDPLANTILLA, IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO,IDPRODUCTOINSTITUCION);
 
 alter table cer_relacion_plantillas
 add constraint FK_CER_RELACION_PLAN_PADRE foreign key (IDPLANTILLAPADRE, IDINSTITUCION, IDTIPOPRODUCTO,IDPRODUCTO,IDPRODUCTOINSTITUCION)
 references cer_plantillas (IDPLANTILLA, IDINSTITUCION,IDTIPOPRODUCTO,IDPRODUCTO,IDPRODUCTOINSTITUCION);
  
  
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.noAsociacion', 'La plantilla se encuentra asociada a otra/s plantilla/s', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.noAsociacion', 'La plantilla se encuentra asociada a otra/s plantilla/s#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.noAsociacion', 'La plantilla se encuentra asociada a otra/s plantilla/s#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.noAsociacion', 'La plantilla se encuentra asociada a otra/s plantilla/s#EU', 0, '3', sysdate, 0, '22');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.mantenimientoCertificados.plantillas.localizacion', 'Certificados > Mantenimiento de certificados', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.mantenimientoCertificados.plantillas.localizacion', 'Certificados > Mantenimiento de certificados', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.mantenimientoCertificados.plantillas.localizacion', 'Certificados > Mantenimiento de certificados', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.mantenimientoCertificados.plantillas.localizacion', 'Certificados > Mantenimiento de certificados', 0, '3', sysdate, 0, '22');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.mantenimientoCertificados.plantillas.plantilla.localizacion', 'Certificados > Mantenimiento de certificados > Plantillas', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.mantenimientoCertificados.plantillas.plantilla.localizacion', 'Certificados > Mantenimiento de certificados > Plantillas', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.mantenimientoCertificados.plantillas.plantilla.localizacion', 'Certificados > Mantenimiento de certificados > Plantillas', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.mantenimientoCertificados.plantillas.plantilla.localizacion', 'Certificados > Mantenimiento de certificados > Plantillas', 0, '3', sysdate, 0, '22');

--Tamaños máximos
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.seleccionarMilRegistros', 'No puede seleccionar más de 1000 registros.', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.seleccionarMilRegistros', 'No puede seleccionar más de 1000 registros.#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.seleccionarMilRegistros', 'No puede seleccionar más de 1000 registros.#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.seleccionarMilRegistros', 'No puede seleccionar más de 1000 registros.#EU', 0, '3', sysdate, 0, '22');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.numeroRegistrosSeleccionados', 'El número de registros seleccionado es de: ', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.numeroRegistrosSeleccionados', 'El número de registros seleccionado es de: #GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.numeroRegistrosSeleccionados', 'El número de registros seleccionado es de: #CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.numeroRegistrosSeleccionados', 'El número de registros seleccionado es de: #EU', 0, '3', sysdate, 0, '22');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.numeroRegistrosSeleccionadosMasDeMil', '. La selección supera el máximo de 1000 registros, por favor acote la búsqueda.', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.numeroRegistrosSeleccionadosMasDeMil', '. La selección supera el máximo de 1000 registros, por favor acote la búsqueda.#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.numeroRegistrosSeleccionadosMasDeMil', '. La selección supera el máximo de 1000 registros, por favor acote la búsqueda.#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.numeroRegistrosSeleccionadosMasDeMil', '. La selección supera el máximo de 1000 registros, por favor acote la búsqueda.#EU', 0, '3', sysdate, 0, '22');



-- Create table
create table CER_SOLICITUDES_ACCION
(
  IDINSTITUCION                NUMBER(4) not null,
  IDSOLICITUD                  NUMBER(10) not null,
  ACCION                       NUMBER(10) not null,
  
  USUCREACION                  NUMBER(5) not null,
  FECHACREACION                DATE not null,
  USUMODIFICACION               NUMBER(5) not null,
  FECHAMODIFICACION            DATE not null,
  USUBAJA                      NUMBER(5),
  FECHABAJA                    DATE
  
)
tablespace TS_SIGA_FAC
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 61M
    next 1M
    minextents 1
    maxextents unlimited
  );

alter table cer_solicitudes_accion
add constraint FK_CER_SOLICITUDES_ACCION foreign key (IDINSTITUCION, IDSOLICITUD)
references cer_solicitudcertificados (IDINSTITUCION, IDSOLICITUD);

alter table cer_solicitudes_accion
  add constraint PK_CER_SOLICITUDES_ACCION primary key (IDINSTITUCION, IDSOLICITUD,ACCION, FECHACREACION)
  deferrable
  using index 
  tablespace TS_SIGA_FAC_IDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
  
 alter table cer_solicitudes_accion
 add constraint CHK_accion
 check (accion>=1 and accion <=3);
  
 comment on column cer_solicitudes_accion.accion
  is '1- Acción de aprobar y generar, 2- Acción de finalizar, 3- Acción de facturar';
  

INSERT INTO cer_estadosolicertifi (IDESTADOSOLICITUDCERTIFICADO, DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (7,'Aprobando',SYSDATE,0);

INSERT INTO cer_estadosolicertifi (IDESTADOSOLICITUDCERTIFICADO, DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (8,'Finalizando',SYSDATE,0);

INSERT INTO cer_estadosolicertifi (IDESTADOSOLICITUDCERTIFICADO, DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (9,'Facturando',SYSDATE,0);

INSERT INTO cer_estadosolicertifi (IDESTADOSOLICITUDCERTIFICADO, DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (10,'Pendiente de facturar',SYSDATE,0);

INSERT INTO cer_estadosolicertifi (IDESTADOSOLICITUDCERTIFICADO, DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (11,'Generando (Aprobado)',SYSDATE,0);

INSERT INTO cer_estadosolicertifi (IDESTADOSOLICITUDCERTIFICADO, DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (12,'Generando (Pdte. de facturar)',SYSDATE,0);

INSERT INTO cer_estadosolicertifi (IDESTADOSOLICITUDCERTIFICADO, DESCRIPCION,FECHAMODIFICACION,USUMODIFICACION) 
VALUES (13,'Generando (Finalizado)',SYSDATE,0);

update GEN_CATALOGOS_MULTIIDIOMA set migrado = 'N' where nombretabla = 'CER_ESTADOSOLICERTIFI';

 declare
      p_codretorno varchar2(4000);
      p_datoserror varchar2(4000);
    begin
      proc_act_recursos(p_codretorno, p_datoserror);
      dbms_output.put_line(p_codretorno || ': ' || p_datoserror);
    end;


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.facturar', 'Facturar', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.facturar', 'Facturar#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.facturar', 'Facturar#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('general.boton.facturar', 'Facturar#EU', 0, '3', sysdate, 0, '22');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.mensaje.elegirAlgunCertificadoFacturar', 'Debe seleccionar algún certificado para facturar', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.mensaje.elegirAlgunCertificadoFacturar', 'Debe seleccionar algún certificado para facturar#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.mensaje.elegirAlgunCertificadoFacturar', 'Debe seleccionar algún certificado para facturar#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.mensaje.elegirAlgunCertificadoFacturar', 'Debe seleccionar algún certificado para facturar#EU', 0, '3', sysdate, 0, '22');


alter table cer_solicitudes_accion
  add IDSERIEFACTURACIONSELECCIONADA VARCHAR2(8) ;



insert into gen_parametros(MODULO, PARAMETRO,VALOR, Fechamodificacion,USUMODIFICACION, IDINSTITUCION)
  values('CER','CER_ACCIONES_MASIVAS_INTERVALO_MINUTOS','15',sysdate,'0','2000');
insert into gen_parametros(MODULO, PARAMETRO,VALOR, Fechamodificacion,USUMODIFICACION, IDINSTITUCION)
  values('CER','CER_ACCIONES_MASIVAS_ACTIVO','1',sysdate,'0','2000');



insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.aprobarGenerar', '¿Desea aprobar y generar masivamente los certificados?', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.aprobarGenerar', '¿Desea aprobar y generar masivamente los certificados?#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.aprobarGenerar', '¿Desea aprobar y generar masivamente los certificados?#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.aprobarGenerar', '¿Desea aprobar y generar masivamente los certificados?#EU', 0, '3', sysdate, 0, '22');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.finalizar', '¿Desea finalizar masivamente los certificados?', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.finalizar', '¿Desea finalizar masivamente los certificados?#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.finalizar', '¿Desea finalizar masivamente los certificados?#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.finalizar', '¿Desea finalizar masivamente los certificados?#EU', 0, '3', sysdate, 0, '22');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.facturar', '¿Desea facturar masivamente los certificados?', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.facturar', '¿Desea facturar masivamente los certificados?#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.facturar', '¿Desea facturar masivamente los certificados?#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.gestion.facturar', '¿Desea facturar masivamente los certificados?#EU', 0, '3', sysdate, 0, '22');

--Existes certificados que no tienen idInstitucionOrigen y eso no es correcto, para solucionar eso se ejecuta las siguientes dos querys
Update Cer_Solicitudcertificados cer Set cer.Idinstitucionorigen = cer.Idinstitucion_Sol Where cer.Idinstitucionorigen Is Null;
alter table cer_solicitudcertificados modify Idinstitucionorigen not null;



insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.colegioPresentador', 'Colegio presentador', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.colegioPresentador', 'Colegio presentador#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.colegioPresentador', 'Colegio presentador#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.colegioPresentador', 'Colegio presentador#EU', 0, '3', sysdate, 0, '22');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.solicitantes', 'Solicitantes', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.Solicitantes', 'Solicitantes#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.Solicitantes', 'Solicitantes#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.Solicitantes', 'Solicitantes#EU', 0, '3', sysdate, 0, '22');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos', 'Documentos', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos', 'Documentos#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos', 'Documentos#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos', 'Documentos#EU', 0, '3', sysdate, 0, '22');



insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos.certificados', 'Certificados', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos.certificados', 'Certificados#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos.certificados', 'Certificados#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos.certificados', 'Certificados#EU', 0, '3', sysdate, 0, '22');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos.facturas', 'Facturas', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos.facturas', 'Facturas#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos.facturas', 'Facturas#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.envios.documentos.facturas', 'Facturas#EU', 0, '3', sysdate, 0, '22');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.error.seleccion.documentos', 'Debe seleccionar al menos un tipo de documento', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.error.seleccion.documentos', 'Debe seleccionar al menos un tipo de documento#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.error.seleccion.documentos', 'Debe seleccionar al menos un tipo de documento#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.general.error.seleccion.documentos', 'Debe seleccionar al menos un tipo de documento#EU', 0, '3', sysdate, 0, '22');


-- Create table
create table CER_ENVIOS
(
  IDINSTITUCION                  NUMBER(4) not null,
  IDSOLICITUD                    NUMBER(10) not null,
  ASUNTO                         VARCHAR2 (100) not null,
  FECHAPROGRAMADA                DATE,
  DESTINATARIO                   NUMBER(1) not null,
  TIPOENVIO                      NUMBER(2) not null,
  TIPOPLANTILLA                  NUMBER(3) not null,
  PLANTILLA                      NUMBER(2),
  CERTIFICADOS                   NUMBER(1),
  FACTURAS                       NUMBER(1),
  ACUSELECTURA                   NUMBER(1),
  
  USUCREACION                    NUMBER(5) not null,
  FECHACREACION                  DATE not null,
  USUMODIFICACION                NUMBER(5) not null,
  FECHAMODIFICACION              DATE not null,
  USUBAJA                        NUMBER(5),
  FECHABAJA                      DATE
)
tablespace TS_SIGA_FAC
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 61M
    next 1M
    minextents 1
    maxextents unlimited
  );
  
alter table cer_envios
add constraint FK_CER_ENVIOS foreign key (IDINSTITUCION, IDSOLICITUD)
references cer_solicitudcertificados (IDINSTITUCION, IDSOLICITUD);

alter table CER_ENVIOS add idPeticionDeEnvio number(10) not null;

alter table cer_envios
  add constraint PK_CER_ENVIOS primary key (IDINSTITUCION, IDSOLICITUD,IDPETICIONDEENVIO)
  deferrable
  using index 
  tablespace TS_SIGA_FAC_IDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
  
  alter table cer_envios
  add constraint FK_CER_ENVIOS_PLANTILLA foreign key (IDINSTITUCION, TIPOENVIO,TIPOPLANTILLA,PLANTILLA)
  references env_plantillageneracion (IDINSTITUCION,IDTIPOENVIOS,IDPLANTILLAENVIOS,IDPLANTILLA);
  
  
  alter table cer_envios
  add constraint FK_CER_ENVIOS_PLANT_ENV foreign key (IDINSTITUCION, TIPOENVIO,TIPOPLANTILLA)
  references env_plantillasenvios (IDINSTITUCION,IDTIPOENVIOS,IDPLANTILLAENVIOS);
  
   comment on column cer_envios.destinatario
  is '0- Colegio presentador, 2- Acción de finalizar, 2- Solicitantes';
  
    comment on column cer_envios.certificados
  is '0- No obtener certificados, 1- Obtener certificados';
  
   comment on column cer_envios.facturas
  is '0- No obtener facturas, 1- Obtener facturas';
  
-- Create sequence 
create sequence SEQ_CER_ENVIOS
minvalue 1
maxvalue 9999999999
start with 1
increment by 1
nocache; 

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('envios.definir.literal.comunicacion', 'Comunicación', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('envios.definir.literal.comunicacion', 'Comunicación#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('envios.definir.literal.comunicacion', 'Comunicación#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('envios.definir.literal.comunicacion', 'Comunicación#EU', 0, '3', sysdate, 0, '22');


update GEN_RECURSOS set descripcion='Debe seleccionar algún certificado para Aprobar y generar.' where idrecurso = 'certificados.solicitudes.mensaje.noHayCertificados' and idlenguaje=1;
update GEN_RECURSOS set descripcion='Debe seleccionar algún certificado para Aprobar y generar.#EU' where idrecurso = 'certificados.solicitudes.mensaje.noHayCertificados' and idlenguaje=3;
update GEN_RECURSOS set descripcion='Debe seleccionar algún certificado para Aprobar y generar.#GL' where idrecurso = 'certificados.solicitudes.mensaje.noHayCertificados' and idlenguaje=4;



update Cer_Solicitudcertificados Cer
Set Idestadosolicitudcertificado = 10
Where Exists (Select * From Pys_Compra Com, Pys_Productosinstitucion Pro
 Where Cer.Idestadosolicitudcertificado = 4
   And Cer.Fechacobro Is Not Null
   And Cer.Idinstitucion = Com.Idinstitucion
   And Cer.Idpeticionproducto = Com.Idpeticion
   And Cer.Idpersona_Des = Com.Idpersona
   And Com.Idinstitucion = Pro.Idinstitucion
   And Com.Idtipoproducto = Pro.Idtipoproducto
   And Com.Idproducto = Pro.Idproducto
   And Com.Idproductoinstitucion = Pro.Idproductoinstitucion
   And Com.Idfactura Is Null
   And Pro.Nofacturable = 0);

 alter table CEN_SOLICITUDMUTUALIDAD modify OTROSBENEFICIARIOS VARCHAR2(255);
 


 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.usuCreacion', 'Usuario creador', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.usuCreacion', 'Usuario creador#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.usuCreacion', 'Usuario creador#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.usuCreacion', 'Usuario creador#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.fechaCreacion', 'Fecha creación', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.fechaCreacion', 'Fecha creación#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.fechaCreacion', 'Fecha creación#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.fechaCreacion', 'Fecha creación#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoActual', 'Estado actual', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoActual', 'Estado actual#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoActual', 'Estado actual#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoActual', 'Estado actual#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoSiguiente', 'Siguiente estado', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoSiguiente', 'Siguiente estado#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoSiguiente', 'Siguiente estado#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.estadoSiguiente', 'Siguiente estado#EU', 0, '3', sysdate, 0, '19');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cert.boton.regenerar', 'Regenerar', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cert.boton.regenerar', 'Regenerar#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cert.boton.regenerar', 'Regenerar#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('cert.boton.regenerar', 'Regenerar#EU', 0, '3', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.colegioCobro', 'Colegio de cobro', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.colegioCobro', 'Colegio de cobro#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.colegioCobro', 'Colegio de cobro#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.colegioCobro', 'Colegio de cobro#GL', 0, '4', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.cobro', 'Cobro#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.cobro', 'Cobro#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.cobro', 'Cobro#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.cobro', 'Cobro', 0, '1', sysdate, 0, '19');


insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.otrasAcciones', 'Otras acciones#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.otrasAcciones', 'Otras acciones#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.otrasAcciones', 'Otras acciones#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.otrasAcciones', 'Otras acciones', 0, '1', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.certificado.error.finalizarCobro', 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, rellene: o bien Comentario o bien Entidad#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.certificado.error.finalizarCobro', 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, rellene: o bien Comentario o bien Entidad#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.certificado.error.finalizarCobro', 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, rellene: o bien Comentario o bien Entidad#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.certificado.error.finalizarCobro', 'Es necesario aportar alguna información de identificación sobre el cobro. Para continuar, rellene: o bien Comentario o bien Entidad', 0, '1', sysdate, 0, '19');




insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seleccionSerie.literal.anularCertificadoFinalizado', '¿Está seguro de querer anular el certificado que está finalizado?#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seleccionSerie.literal.anularCertificadoFinalizado', '¿Está seguro de querer anular el certificado que está finalizado?#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seleccionSerie.literal.anularCertificadoFinalizado', '¿Está seguro de querer anular el certificado que está finalizado?#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seleccionSerie.literal.anularCertificadoFinalizado', '¿Está seguro de querer anular el certificado que está finalizado?', 0, '1', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seleccionSerie.literal.anularCertificadoFacturaAsociada', 'Este certificado ha sido facturado. Si decide continuar con la anulación, la factura quedará anulada y deberá realizarse el pago del abono correspondiente. ¿Desea anular el certificado?#GL', 0, '4', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seleccionSerie.literal.anularCertificadoFacturaAsociada', 'Este certificado ha sido facturado. Si decide continuar con la anulación, la factura quedará anulada y deberá realizarse el pago del abono correspondiente. ¿Desea anular el certificado?#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seleccionSerie.literal.anularCertificadoFacturaAsociada', 'Este certificado ha sido facturado. Si decide continuar con la anulación, la factura quedará anulada y deberá realizarse el pago del abono correspondiente. ¿Desea anular el certificado?#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.seleccionSerie.literal.anularCertificadoFacturaAsociada', 'Este certificado ha sido facturado. Si decide continuar con la anulación, la factura quedará anulada y deberá realizarse el pago del abono correspondiente. ¿Desea anular el certificado?', 0, '1', sysdate, 0, '19');

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.msgRegenerar', 'El certificado ya fue generado. Sería recomendable que volviera a generarlo.', 0, '1', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.msgRegenerar', 'El certificado ya fue generado. Sería recomendable que volviera a generarlo.#CA', 0, '2', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.msgRegenerar', 'El certificado ya fue generado. Sería recomendable que volviera a generarlo.#EU', 0, '3', sysdate, 0, '19');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('certificados.solicitudes.literal.msgRegenerar', 'El certificado ya fue generado. Sería recomendable que volviera a generarlo.#GL', 0, '4', sysdate, 0, '19');

ALTER TABLE ADM_CONTADOR MODIFY (DESCRIPCION VARCHAR2(1000), NOMBRE VARCHAR2(1000));

SCS_GUARDIASCOLEGIADO_AID
CEN_DIRECCION_TIPODIR_AIR
Update Gen_Pestanas Set posicion = decode(idrecurso, 'pestana.certificados.plantillas', 1, 'pestana.certificados.campos', 2) Where idproceso In ('62a', '62b');
se modifica de nuevo. No esta subido a desarrollo
CEN_DIRECCION_TIPODIR_AIR=
SCS_EJG_AUR no esta subido a desarrollo
update GEN_RECURSOS set descripcion='La gestión de la solicitud no ha finalizado con éxito. Inténtelo de nuevo. Si el problema persiste, contacte con el Administrador.' where idrecurso = 'messages.certificado.error.noFinalizacion' and idlenguaje=1;
update GEN_RECURSOS set descripcion='La gestión de la solicitud no ha finalizado con éxito. Inténtelo de nuevo. Si el problema persiste, contacte con el Administrador.#CA' where idrecurso = 'messages.certificado.error.noFinalizacion' and idlenguaje=2;
update GEN_RECURSOS set descripcion='La gestión de la solicitud no ha finalizado con éxito. Inténtelo de nuevo. Si el problema persiste, contacte con el Administrador.#EU' where idrecurso = 'messages.certificado.error.noFinalizacion' and idlenguaje=3;
update GEN_RECURSOS set descripcion='La gestión de la solicitud no ha finalizado con éxito. Inténtelo de nuevo. Si el problema persiste, contacte con el Administrador.#GL' where idrecurso = 'messages.certificado.error.noFinalizacion' and idlenguaje=4;


update GEN_RECURSOS set descripcion='El Colegio de Facturación no tiene cuenta de cargo asociada: puede crearla en Censo> Buscar No colegiados> Bancos. Si el problema persiste, contacte con el Administrador.' where idrecurso = 'messages.certificado.error.NoExisteCuentaBancaria' and idlenguaje=1;
update GEN_RECURSOS set descripcion='El Colegio de Facturación no tiene cuenta de cargo asociada: puede crearla en Censo> Buscar No colegiados> Bancos. Si el problema persiste, contacte con el Administrador.#CA' where idrecurso = 'messages.certificado.error.NoExisteCuentaBancaria' and idlenguaje=2;
update GEN_RECURSOS set descripcion='El Colegio de Facturación no tiene cuenta de cargo asociada: puede crearla en Censo> Buscar No colegiados> Bancos. Si el problema persiste, contacte con el Administrador.#EU' where idrecurso = 'messages.certificado.error.NoExisteCuentaBancaria' and idlenguaje=3;
update GEN_RECURSOS set descripcion='El Colegio de Facturación no tiene cuenta de cargo asociada: puede crearla en Censo> Buscar No colegiados> Bancos. Si el problema persiste, contacte con el Administrador.#GL' where idrecurso = 'messages.certificado.error.NoExisteCuentaBancaria' and idlenguaje=4;


Insert Into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values ('errors.date.past', 'La {0} debe ser anterior al día actual', '0', 1, Sysdate, 0, 3);
Insert Into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values ('errors.date.past', 'La {0} debe ser anterior al día actual#CA', '0', 2, Sysdate, 0, 3);
Insert Into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values ('errors.date.past', 'La {0} debe ser anterior al día actual#EU', '0', 3, Sysdate, 0, 3);
Insert Into gen_recursos (idrecurso, descripcion, error, idlenguaje, fechamodificacion, usumodificacion, idpropiedad) Values ('errors.date.past', 'La {0} debe ser anterior al día actual#GL', '0', 4, Sysdate, 0, 3);

insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.enviosCertificados.comunicacion', 'Es necesario introducir el campo Comunicación', 0, '1', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.enviosCertificados.comunicacion', 'Es necesario introducir el campo Comunicación#GL', 0, '4', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.enviosCertificados.comunicacion', 'Es necesario introducir el campo Comunicación#CA', 0, '2', sysdate, 0, '22');
insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('messages.enviosCertificados.comunicacion', 'Es necesario introducir el campo Comunicación#EU', 0, '3', sysdate, 0, '22');

update GEN_RECURSOS set descripcion='Certificados > Mantenimiento de certificados#CA' where idrecurso = 'certificados.mantenimientoCertificados.plantillas.localizacion' and idlenguaje=2;
update GEN_RECURSOS set descripcion='Certificados > Mantenimiento de certificados#EU' where idrecurso = 'certificados.mantenimientoCertificados.plantillas.localizacion' and idlenguaje=3;
update GEN_RECURSOS set descripcion='Certificados > Mantenimiento de certificados#GL' where idrecurso = 'certificados.mantenimientoCertificados.plantillas.localizacion' and idlenguaje=4;

update GEN_RECURSOS set descripcion='Certificados > Mantenimiento de certificados > Plantillas#CA' where idrecurso = 'certificados.mantenimientoCertificados.plantillas.plantilla.localizacion' and idlenguaje=2;
update GEN_RECURSOS set descripcion='Certificados > Mantenimiento de certificados > Plantillas#EU' where idrecurso = 'certificados.mantenimientoCertificados.plantillas.plantilla.localizacion' and idlenguaje=3;
update GEN_RECURSOS set descripcion='Certificados > Mantenimiento de certificados > Plantillas#GL' where idrecurso = 'certificados.mantenimientoCertificados.plantillas.plantilla.localizacion' and idlenguaje=4;


Update cen_cliente cli 
Set cli.Noaparecerredabogacia = '0'
Where idinstitucion > 3000 And cli.Noaparecerredabogacia = '1';

Pequeño cambio de importe interno en PKG_SIGA_FACTURACION_SJCS

Update Gen_Recursos   Set Descripcion = 'Origen / Presentación' Where Idlenguaje = 1   And Idrecurso In ('certificados.solicitudes.literal.colegioOrigen',        'certificados.solicitudes.literal.institucionOrigenLista');
Update Gen_Recursos   Set Descripcion = 'Origen / Presentació' Where Idlenguaje = 2   And Idrecurso In ('certificados.solicitudes.literal.colegioOrigen',        'certificados.solicitudes.literal.institucionOrigenLista');
Update Gen_Recursos   Set Descripcion = 'Origen / Presentación#EU' Where Idlenguaje = 3   And Idrecurso In ('certificados.solicitudes.literal.colegioOrigen',        'certificados.solicitudes.literal.institucionOrigenLista');
Update Gen_Recursos   Set Descripcion = 'Origen / Presentación#GL' Where Idlenguaje = 4   And Idrecurso In ('certificados.solicitudes.literal.colegioOrigen',        'certificados.solicitudes.literal.institucionOrigenLista');

 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.facturas.datosGenerales.regenerarFactura', '¿Desea regenerar la factura?', 0, '1', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.facturas.datosGenerales.regenerarFactura', '¿Desea regenerar la factura?#GL', 0, '4', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.facturas.datosGenerales.regenerarFactura', '¿Desea regenerar la factura?#CA', 0, '2', sysdate, 0, '19');
 insert into GEN_RECURSOS (IDRECURSO, DESCRIPCION, ERROR, IDLENGUAJE, FECHAMODIFICACION, USUMODIFICACION, IDPROPIEDAD) values ('facturacion.facturas.datosGenerales.regenerarFactura', '¿Desea regenerar la factura?#EU', 0, '3', sysdate, 0, '19');