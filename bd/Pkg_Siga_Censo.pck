CREATE OR REPLACE Package Pkg_Siga_Censo Is

  /****************************************************************************************************************/
  /* Version:        1.1                                                                                          */
  /* Descripcion:    Paquete para evitar tablas mutantes.                                                      */
  /*                                                                                                              */
  /* Fecha Creacion: 15/02/2005                                                                                   */
  /* Autor:          Yolanda Garcia Espino                                                                        */
  /* Fecha Modificacion   Autor Modificacion                  Descripcion Modificacion                            */
  /* ------------------   ---------------------------------   --------------------------------------------------- */
  /****************************************************************************************************************/

  --  Las siguientes variables son usadas por los triggers sobre CEN_DATOSCOLEGIALESESTADO 
  -- que calculan el campo calculado CEN_COLEGIADO.SITUACIONEJERCICIO.
  --  Son necesarias ya que no hay otra forma de aunar simplicidad y control
  -- tanto en migraciones como en codigo.
  --  Esta solucion basada en triggers esta documentada en Oracle: 
  -- http://download.oracle.com/docs/cd/B10500_01/appdev.920/a96590/adg13trg.htm
  -- Seccion "Trigger Restrictions on Mutating Tables"
  c_Idinstitucion_Cambioestado Cen_Colegiado.Idinstitucion%Type := Null;
  c_Idpersona_Cambioestado     Cen_Colegiado.Idpersona%Type := Null;

  -- Creacion de una tabla PL/SQL para evitar tablas mutantes
  Type t_Instituciones Is Table Of Cen_Datoscolegialesestado.Idinstitucion%Type Index By Binary_Integer;
  Type t_Personas Is Table Of Cen_Datoscolegialesestado.Idpersona%Type Index By Binary_Integer;
  Type t_Usumodificacion Is Table Of Cen_Datoscolegialesestado.Usumodificacion%Type Index By Binary_Integer;

  v_Institucion     t_Instituciones;
  v_Persona         t_Personas;
  v_Usumodificacion t_Usumodificacion;
  v_Numentrada      Binary_Integer := 0;

  Procedure Actualizardatosletrado(p_Idpersona       In Cen_Cliente.Idpersona%Type,
                                   p_Idinstitucion   In Cen_Cliente.Idinstitucion%Type,
                                   p_Tipocambio      In Number,
                                   p_Iddireccion     In Cen_Direcciones.Iddireccion%Type,
                                   p_Usumodificacion In Pys_Serviciossolicitados.Usumodificacion%Type,
                                   p_Codretorno      Out Varchar2,
                                   p_Datoserror      Out Varchar2);

  Procedure Buscar_y_Actualizar_Direccion(p_Idpersona    In Cen_Cliente.Idpersona%Type,
                                          p_Tipoespecial In Cen_Direccion_Tipodireccion.Idtipodireccion%Type,
                                          p_Codretorno   Out Varchar2,
                                          p_Datoserror   Out Varchar2);
  Procedure Insertar_Dir_En_Consejo(p_Idpersona             In Cen_Direcciones.Idpersona%Type,
                                    p_Idtipo_Copia          In Cen_Direccion_Tipodireccion.Idtipodireccion%Type,
                                    p_Idinstitucion_Colegio In Cen_Direcciones.Idinstitucion%Type,
                                    p_Iddireccion_Colegio   In Cen_Direcciones.Iddireccion%Type,
                                    p_Idinstitucion_Consejo In Cen_Direcciones.Idinstitucion%Type,
                                    v_Iddireccion_Consejo   Out Cen_Direcciones.Iddireccion%Type,
                                    Total_Insertadas        Out Number,
                                    p_Codretorno            Out Varchar2,
                                    p_Datoserror            Out Varchar2);
  Procedure Actualizar_Dir_En_Consejo(p_Idpersona             In Cen_Direcciones.Idpersona%Type,
                                      p_Idinstitucion_Colegio In Cen_Direcciones.Idinstitucion%Type,
                                      p_Iddireccion_Colegio   In Cen_Direcciones.Iddireccion%Type,
                                      p_Idinstitucion_Consejo In Cen_Direcciones.Idinstitucion%Type,
                                      p_Iddireccion_Consejo   In Cen_Direcciones.Iddireccion%Type,
                                      Total_Actualizadas      Out Number,
                                      p_Codretorno            Out Varchar2,
                                      p_Datoserror            Out Varchar2);
End Pkg_Siga_Censo;
/
CREATE OR REPLACE Package Body Pkg_Siga_Censo Is

  c_Idcgae             Constant Cen_Institucion.Idinstitucion%Type := 2000;
  c_Iditcgae           Constant Cen_Institucion.Idinstitucion%Type := 3500;
  c_Primer_Id_Consejos Constant Cen_Institucion.Idinstitucion%Type := 3001;
  c_Tipo_Censoweb      Constant Cen_Tipodireccion.Idtipodireccion%Type := 3;
  c_Tipo_Despachooojj  Constant Cen_Tipodireccion.Idtipodireccion%Type := 9;
  c_Ejerciente         Constant Cen_Datoscolegialesestado.Idestado%Type := 20;
  c_Noejerciente       Constant Cen_Datoscolegialesestado.Idestado%Type := 10;

  e_Error Exception;

  --
  -- Procedure:   ACTUALIZARDATOSLETRADO                                     
  -- Descripcion: Actualiza los datos de los Letrados dados de alta en CGAE y Consejos Autonomicos 
  --              a partir de las colas de modificaciones realizadas desde los diferentes colegios.
  --              Estas colas atiendan a nuevas incorporaciones, cambios de residencia y direcciones.
  --
  -- Parametros       IN/OUT  Descripcion                       Tipo         
  -- ---------------  ------  -------------------------------  ------------- 
  -- P_IDPERSONA      IN      Identificador de la Idpersona    NUMBER        
  -- P_IDINSTITUCION  IN      Identificador de la Institucion  NUMBER        
  -- P_TIPOCAMBIO     IN      Tipo de cambio:                  NUMBER        
  --                            10 - aprobacion solicitud                    
  --                            30 - direccion                               
  -- P_IDDIRECCION    IN      Identificador de la direccion    NUMBER        
  --                          modificada en el colegio                       
  -- P_CODRETORNO     OUT     Devuelve codigo con la           VARCHAR2(10)  
  --                          operacion realizada o error                    
  --                            -1 - Origen del cambio es un                 
  --                                 Consejo y no un Colegio                 
  --                            -2 - No hay colegiado en el                  
  --                                 colegio origen                          
  --                            -6 - No hay direccion de                     
  --                                 Correo en el origen                     
  --                            11 - Cliente actualizado (con                
  --                                 posible no colegiado                    
  --                                 borrado)                                
  --                            -11 - Error en lo anterior                   
  --                            12 - Cliente creado en consejo               
  --                            -12 - Error en lo anterior                   
  --                            21 - Actualizada direccion                   
  --                                 heredada del mismo colegio              
  --                            -21 - Error en lo anterior                   
  --                            22 - Actualizada direccion                   
  --                                 heredada de otro colegio                
  --                            -22 - Error en lo anterior                   
  --                            23 - Insertada direccion                     
  --                                 en consejo                              
  --                            -23 - Error en lo anterior                   
  --                            -26 - Error al insertar los                  
  --                                  tipos de direccion de                  
  --                                  la direccion correspondiente           
  --                            31 - Borrada direccion                       
  --                                 fisicamente en consejo                  
  --                            32 - Borrada direccion                       
  --                                 logicamente en consejo                  
  --                            -32 - Error en lo anterior                   
  --                            -31 - No existe direccion heredada           
  --                                  disponible para borrar                 
  --                            33 Modificado LOPD correctamente             
  --                            -33 Error al modificar LOPD                  
  --                            En caso de error no controlado               
  --                            devuelve el codigo de error                  
  --                            Oracle correspondiente                       
  --                                                                         
  -- P_DATOSERROR     OUT     Devuelve descripcion de salida   VARCHAR2(200) 
  --                                                                         
  -- Fecha Modificacion  Autor Modificacion  Descripcion Modificacion        
  -- ------------------  ------------------  ------------------------------- 
  -- 19-03-2006          Pilar Duran         Creacion
  -- 06-06-2007          Pilar Duran         Incorporar bajas de direcciones o cambios de residencia
  -- 27-05-2008          Adrian Ayala        Limpieza general y cambio del algoritmo
  -- 09-03-2009          Jorge Torres        Incorporar LOPD
  -- 16-06-2015          Adrian Ayala        Arreglo/mejora de Incorp. LOPD
  -- 08-10-2015          Adrian Ayala        Copia de la dir para OOJJ
  --

  Procedure Actualizardatosletrado(p_Idpersona       In Cen_Cliente.Idpersona%Type,
                                   p_Idinstitucion   In Cen_Cliente.Idinstitucion%Type,
                                   p_Tipocambio      In Number,
                                   p_Iddireccion     In Cen_Direcciones.Iddireccion%Type,
                                   p_Usumodificacion In Pys_Serviciossolicitados.Usumodificacion%Type,
                                   p_Codretorno      Out Varchar2,
                                   p_Datoserror      Out Varchar2) Is
  
    v_Codretorno Varchar2(10); --Codigo de error Oracle
    v_Datoserror Varchar2(4000); --Mensaje de error en los procedimientos
  
    -- Variables
    Existecliente    Number := 0;
    Esnocolegiado    Number := 0;
    Escolegiado      Number := 0;
    Esletrado        Number := 0;
    Esresidente      Number := 0;
    v_Estadocolegial Number := 0;
  
    Regcolegiado Cen_Colegiado%Rowtype;
  
    -- Seleccion de los Consejos que administran el colegio del colegiado
    Cursor c_Consejo Is
      Select Distinct (Idinstitucion) Idinstitucion
        From (Select Cen_Inst_Idinstitucion As Idinstitucion
                From Cen_Institucion
               Where Idinstitucion = p_Idinstitucion
                 And Cen_Inst_Idinstitucion Is Not Null
              Union
              Select Idinstitucion From Cen_Institucion Where Cen_Inst_Idinstitucion Is Null);
  
  Begin
  
    v_Datoserror := 'Actualizardatosletrado: iniciando';
    v_Codretorno := To_Char(0);
  
    If (p_Idinstitucion = 2000 Or p_Idinstitucion >= 3000) Then
      v_Datoserror := 'Actualizardatosletrado: No se realiza ninguna operacion por ser un ' ||
                      'consejo (' || p_Idinstitucion || ') el que realiza los cambios';
      v_Codretorno := To_Char(-1);
      Raise e_Error;
    End If;
  
    Begin
      Select *
        Into Regcolegiado
        From Cen_Colegiado
       Where Idpersona = p_Idpersona
         And Idinstitucion = p_Idinstitucion;
    Exception
      When No_Data_Found Then
        v_Datoserror := 'Actualizardatosletrado: No se realiza ninguna operacion por no ser un ' ||
                        'colegiado (' || p_Idinstitucion || '.' || p_Idpersona ||
                        ') el que realiza los cambios';
        v_Codretorno := To_Char(-2);
      
        Raise e_Error;
    End;
  
    --para cada consejo de los involucrados por el colegiado
    For Rec In c_Consejo Loop
      -- Obteniendo informacion sobre el letrado
    
      --existeCliente, esLetrado
      Begin
        Select 1, Cen_Cliente.Letrado
          Into Existecliente, Esletrado
          From Cen_Cliente
         Where Idinstitucion = Rec.Idinstitucion
           And Idpersona = p_Idpersona;
      Exception
        When No_Data_Found Then
          Existecliente := 0;
          Esletrado     := 0;
      End;
    
      --esNoColegiado
      Begin
        Select 1
          Into Esnocolegiado
          From Cen_Nocolegiado
         Where Idpersona = p_Idpersona
           And Idinstitucion = Rec.Idinstitucion;
      Exception
        When No_Data_Found Then
          Esnocolegiado := 0;
      End;
    
      --esColegiado
      Begin
        Select 1
          Into Escolegiado
          From Cen_Colegiado
         Where Idpersona = p_Idpersona
           And Idinstitucion = Rec.Idinstitucion;
      Exception
        When No_Data_Found Then
          Escolegiado := 0;
      End;
    
      --esResidente, v_estadoColegial
      Begin
        Select f_Siga_Gettipocliente(p_Idpersona, p_Idinstitucion, Sysdate), Col.Situacionresidente
          Into v_Estadocolegial, Esresidente
          From Cen_Cliente Cli, Cen_Colegiado Col
         Where Cli.Idpersona = Col.Idpersona
           And Cli.Idinstitucion = Col.Idinstitucion
           And Cli.Idinstitucion = p_Idinstitucion
           And Cli.Idpersona = p_Idpersona;
      Exception
        When No_Data_Found Then
          v_Estadocolegial := 0;
          Esresidente      := 0;
      End;
    
      -- Lo normal o la situacion final ha de ser la siguiente:
      -- existeCliente, esLetrado, no esColegiado, no esNoColegiado
    
      --si existe el cliente en el consejo
      If Existecliente = 1 Then
        Begin
          --eliminando registro No Colegiado (si existe)
          Delete Cen_Nocolegiado
           Where Idpersona = p_Idpersona
             And Idinstitucion = Rec.Idinstitucion;
        
          --activando el check de letrado (si no lo es ya)
          Update Cen_Cliente
             Set Letrado = '1', Fechamodificacion = Sysdate, Usumodificacion = 0
           Where Idpersona = p_Idpersona
             And Idinstitucion = Rec.Idinstitucion
             And Letrado <> '1';
        
          v_Codretorno := To_Char(11);
          v_Datoserror := v_Datoserror || ' -- ' || Rec.Idinstitucion || '--[' || v_Codretorno ||
                          '] Actualizando el letrado';
        Exception
          When Others Then
            v_Codretorno := To_Char(-11);
            v_Datoserror := Rec.Idinstitucion || ' Error al actualizar el letrado ' ||
                            '(seguramente al intentar borrar ' || 'el registro de No colegiado): ' ||
                            Sqlerrm;
            Raise e_Error;
        End;
      
        --no existe el cliente en el consejo
      Else
        --creando cliente letrado
        Begin
          Insert Into Cen_Cliente
            (Idpersona,
             Idinstitucion,
             Fechaalta,
             Caracter,
             Publicidad,
             Guiajudicial,
             Abonosbanco,
             Cargosbanco,
             Comisiones,
             Idtratamiento,
             Fechamodificacion,
             Usumodificacion,
             Idlenguaje,
             Fotografia,
             Asientocontable,
             Letrado,
             Fechacarga)
            Select p_Idpersona,
                   Rec.Idinstitucion,
                   Sysdate,
                   Caracter,
                   Publicidad,
                   Guiajudicial,
                   Abonosbanco,
                   Cargosbanco,
                   Comisiones,
                   Idtratamiento,
                   Sysdate,
                   p_Usumodificacion,
                   Idlenguaje,
                   Fotografia,
                   Asientocontable,
                   '1',
                   Sysdate
              From Cen_Cliente
             Where Idinstitucion = p_Idinstitucion
               And Idpersona = p_Idpersona;
        
          v_Codretorno := To_Char(12);
          v_Datoserror := v_Datoserror || ' -- ' || Rec.Idinstitucion || '--[' || v_Codretorno ||
                          '] Se inserta cliente y se activa como letrado';
        Exception
          When Others Then
            v_Codretorno := To_Char(-12);
            v_Datoserror := Rec.Idinstitucion || ' Error al insertar en cliente: ' || Sqlerrm;
            Raise e_Error;
        End;
      End If;
    
    End Loop;
  
    If p_Tipocambio = 30 Or p_Tipocambio = 40 Then
      v_Datoserror := 'Actualizardatosletrado: Copiando direccion de Censo Web a los consejos';
      Buscar_y_Actualizar_Direccion(p_Idpersona, c_Tipo_Censoweb, v_Codretorno, v_Datoserror);
      If p_Codretorno <> 0 Then
        v_Datoserror := v_Datoserror || ' - ' || v_Codretorno;
        v_Codretorno := '-23';
        Raise e_Error;
      End If;
    
      v_Datoserror := 'Actualizardatosletrado: Copiando direccion de Despacho OO JJ a los consejos';
      Buscar_y_Actualizar_Direccion(p_Idpersona, c_Tipo_Despachooojj, v_Codretorno, v_Datoserror);
      If p_Codretorno <> 0 Then
        v_Datoserror := v_Datoserror || ' - ' || v_Codretorno;
        v_Codretorno := '-23';
        Raise e_Error;
      End If;
    End If;
  
    p_Datoserror := 'Actualizardatosletrado: Correcto';
    p_Codretorno := To_Char(0);
  
  Exception
    When e_Error Then
      p_Codretorno := v_Codretorno;
      p_Datoserror := v_Datoserror;
    
    When Others Then
      p_Codretorno := -50;
      p_Datoserror := v_Datoserror || ': ' || Sqlcode || ' - ' || Sqlerrm;
  End Actualizardatosletrado;

  Procedure Buscar_y_Actualizar_Direccion(p_Idpersona    In Cen_Cliente.Idpersona%Type,
                                          p_Tipoespecial In Cen_Direccion_Tipodireccion.Idtipodireccion%Type,
                                          p_Codretorno   Out Varchar2,
                                          p_Datoserror   Out Varchar2) Is
  
    v_Codretorno Varchar2(10); --Codigo de error Oracle
    v_Datoserror Varchar2(4000); --Mensaje de error en los procedimientos
  
    --Obtencion de letrados en Consejos a los que copiar la direccion de Censo Web
    Cursor c_Consejos Is
      Select Idinstitucion
        From Cen_Cliente
       Where Idpersona = p_Idpersona
         And Letrado = '1'
         And (Idinstitucion = c_Idcgae Or Idinstitucion = c_Iditcgae Or
             Idinstitucion >= c_Primer_Id_Consejos);
    r_Consejo Cen_Cliente%Rowtype;
  
    --Variables para guardar la direccion de colegio elegida
    v_Idinstitucion_Elegida Cen_Direcciones.Idinstitucion%Type;
    v_Iddireccion_Elegida   Cen_Direcciones.Iddireccion%Type;
    --Variable para guardar la direccion de consejo a actualizar
    v_Iddireccion_Consejo Cen_Direcciones.Iddireccion%Type;
  
    --Control de totales
    Total              Number;
    Total_Insertadas   Number;
    Total_Actualizadas Number;
    Total_Borradas     Number;
    n_Insertadas       Number;
    n_Actualizadas     Number;
  
  Begin
  
    v_Datoserror := 'Buscar_y_Actualizar_Direccion: iniciando';
    v_Codretorno := To_Char(0);
  
    v_Datoserror       := 'Buscar_y_Actualizar_Direccion: inicializando totales';
    Total              := 0;
    Total_Insertadas   := 0;
    Total_Actualizadas := 0;
    Total_Borradas     := 0;
  
    v_Datoserror := 'Buscar_y_Actualizar_Direccion: empezando el bucle de consejos';
    For r_Consejo In c_Consejos Loop
    
      Begin
        v_Datoserror := 'Buscar_y_Actualizar_Direccion: 1. Eligiendo direccion de tipo especial';
        
        If p_Tipoespecial = c_Tipo_Censoweb Then -- Para el tipo de Censo Web:
        
          Select Idinstitucion, Iddireccion
            Into v_Idinstitucion_Elegida, v_Iddireccion_Elegida
          --Obteniendo direcciones
            From (Select Dir.Idinstitucion, Dir.Iddireccion
                  --de colegiaciones...
                    From Cen_Colegiado               Col,
                         Cen_Datoscolegialesestado   Est,
                         Cen_Direcciones             Dir,
                         Cen_Direccion_Tipodireccion Tip
                   Where Col.Idinstitucion = Est.Idinstitucion
                     And Col.Idpersona = Est.Idpersona
                     And Est.Fechaestado =
                         (Select Max(Est2.Fechaestado)
                            From Cen_Datoscolegialesestado Est2
                           Where Est2.Idinstitucion = Est.Idinstitucion
                             And Est2.Idpersona = Est.Idpersona
                             And Trunc(Est2.Fechaestado) <= Sysdate)
                     And Col.Idinstitucion = Dir.Idinstitucion
                     And Col.Idpersona = Dir.Idpersona
                     And Dir.Fechabaja Is Null
                     And Dir.Idinstitucion = Tip.Idinstitucion
                     And Dir.Idpersona = Tip.Idpersona
                     And Dir.Iddireccion = Tip.Iddireccion
                        
                        --... de la persona en cuestion,
                     And Dir.Idpersona = p_Idpersona
                        
                        --que sean de Tipo Especial
                     And Tip.Idtipodireccion = p_Tipoespecial
                        
                        --(Si buscamos direccion para CGAE, cualquier colegio vale,
                     And (r_Consejo.Idinstitucion = c_Idcgae Or
                         --Si buscamos direccion para ITCGAE, cualquier colegio vale
                         r_Consejo.Idinstitucion = c_Iditcgae Or
                         --Si no, solo valen los colegios que pertenecen al Consejo Autonomico)
                         Dir.Idinstitucion In
                         (Select Idinstitucion
                             From Cen_Institucion
                            Where Cen_Inst_Idinstitucion = r_Consejo.Idinstitucion))
                  --ordenandolas por Actividad, Residencia, Ejercicio y finalmente por novedad
                   Order By Decode(Est.Idestado, c_Ejerciente, 1, c_Noejerciente, 1, 2),
                            Col.Situacionresidente Desc,
                            Decode(Est.Idestado, c_Ejerciente, 1, 2),
                            Dir.Fechamodificacion Desc)
          --y se elige la primera
           Where Rownum = 1;
           
        Elsif p_Tipoespecial = c_Tipo_Despachooojj Then -- Para el tipo de Despacho OOJJ:
        
          Select Idinstitucion, Iddireccion
            Into v_Idinstitucion_Elegida, v_Iddireccion_Elegida
          --Obteniendo direcciones
            From (Select Dir.Idinstitucion, Dir.Iddireccion
                  --de colegiaciones...
                    From Cen_Colegiado               Col,
                         Cen_Datoscolegialesestado   Est,
                         Cen_Direcciones             Dir,
                         Cen_Direccion_Tipodireccion Tip
                   Where Col.Idinstitucion = Est.Idinstitucion
                     And Col.Idpersona = Est.Idpersona
                     And Est.Fechaestado =
                         (Select Max(Est2.Fechaestado)
                            From Cen_Datoscolegialesestado Est2
                           Where Est2.Idinstitucion = Est.Idinstitucion
                             And Est2.Idpersona = Est.Idpersona
                             And Trunc(Est2.Fechaestado) <= Sysdate)
                        --solo ejercientes
                     And Est.Idestado = 20
                        
                     And Col.Idinstitucion = Dir.Idinstitucion
                     And Col.Idpersona = Dir.Idpersona
                     And Dir.Fechabaja Is Null
                     And Dir.Idinstitucion = Tip.Idinstitucion
                     And Dir.Idpersona = Tip.Idpersona
                     And Dir.Iddireccion = Tip.Iddireccion
                        
                        --... de la persona en cuestion,
                     And Dir.Idpersona = p_Idpersona
                        
                        --que sean de Tipo Especial
                     And Tip.Idtipodireccion = p_Tipoespecial
                        
                        --(Si buscamos direccion para CGAE, cualquier colegio vale,
                     And (r_Consejo.Idinstitucion = c_Idcgae Or
                         --Si buscamos direccion para ITCGAE, cualquier colegio vale
                         r_Consejo.Idinstitucion = c_Iditcgae Or
                         --Si no, solo valen los colegios que pertenecen al Consejo Autonomico)
                         Dir.Idinstitucion In
                         (Select Idinstitucion
                             From Cen_Institucion
                            Where Cen_Inst_Idinstitucion = r_Consejo.Idinstitucion))
                  --ordenandolas por existencia de Correo electronico, Residencia, y finalmente por novedad
                   Order By Decode(Dir.Correoelectronico, Null, 0, 1) Desc,
                            Col.Situacionresidente Desc,
                            Dir.Fechamodificacion Desc)
          --y se elige la primera
           Where Rownum = 1;
           
        Else -- Para otro tipo de direccion (aunque el algoritmo no esta definido para este caso):
        
          Select Idinstitucion, Iddireccion
            Into v_Idinstitucion_Elegida, v_Iddireccion_Elegida
          --Obteniendo direcciones
            From (Select Dir.Idinstitucion, Dir.Iddireccion
                  --de colegiaciones...
                    From Cen_Colegiado               Col,
                         Cen_Direcciones             Dir,
                         Cen_Direccion_Tipodireccion Tip
                   Where Col.Idinstitucion = Dir.Idinstitucion
                     And Col.Idpersona = Dir.Idpersona
                     And Dir.Fechabaja Is Null
                     And Dir.Idinstitucion = Tip.Idinstitucion
                     And Dir.Idpersona = Tip.Idpersona
                     And Dir.Iddireccion = Tip.Iddireccion
                        
                        --... de la persona en cuestion,
                     And Dir.Idpersona = p_Idpersona
                        
                        --que sean de Tipo Especial
                     And Tip.Idtipodireccion = p_Tipoespecial
                        
                        --(Si buscamos direccion para CGAE, cualquier colegio vale,
                     And (r_Consejo.Idinstitucion = c_Idcgae Or
                         --Si buscamos direccion para ITCGAE, cualquier colegio vale
                         r_Consejo.Idinstitucion = c_Iditcgae Or
                         --Si no, solo valen los colegios que pertenecen al Consejo Autonomico)
                         Dir.Idinstitucion In
                         (Select Idinstitucion
                             From Cen_Institucion
                            Where Cen_Inst_Idinstitucion = r_Consejo.Idinstitucion))
                  --ordenandolas por novedad
                   Order By Dir.Fechamodificacion Desc)
          --y se elige la primera
           Where Rownum = 1;
           
        End If;
        
      Exception
        When No_Data_Found Then
          v_Idinstitucion_Elegida := Null;
          v_Iddireccion_Elegida   := Null;
      End;
    
      If v_Iddireccion_Elegida Is Not Null Then
        Begin
          v_Datoserror := 'Buscar_y_Actualizar_Direccion: 2. Buscando direccion en Consejo del tipo especial';
          Select Iddireccion
            Into v_Iddireccion_Consejo
            From (Select Dir.Iddireccion, Decode(Tip.Idtipodireccion, Null, 0, 1) Censoweb
                  --de las direcciones que
                    From Cen_Direcciones Dir, Cen_Direccion_Tipodireccion Tip
                   Where Dir.Fechabaja Is Null
                     And Dir.Idinstitucion = Tip.Idinstitucion
                     And Dir.Idpersona = Tip.Idpersona
                     And Dir.Iddireccion = Tip.Iddireccion
                        --son de Tipo Especial
                     And p_Tipoespecial = Tip.Idtipodireccion
                        --para la persona dada en este consejo
                     And Dir.Idinstitucion = r_Consejo.Idinstitucion
                     And Dir.Idpersona = p_Idpersona
                  --ordenadas por: primero las que sean heredadas de la direccion elegida del colegio 
                  --y luego las que no. Y se ordena tambien primero por las mas modernas
                   Order By Decode(Dir.Iddireccionalta, v_Iddireccion_Elegida, 1, 2),
                            Dir.Fechamodificacion Desc)
          --y se elige la primera
           Where Rownum = 1;
        Exception
          When No_Data_Found Then
            v_Iddireccion_Consejo := Null;
        End;
      
        If v_Iddireccion_Consejo Is Null Then
        
          v_Datoserror := 'Buscar_y_Actualizar_Direccion: 3.a. No existe direccion especial en el Consejo: insertando NUEVA.';
          n_Insertadas := 0;
          Insertar_Dir_En_Consejo(p_Idpersona,
                                  p_Tipoespecial,
                                  v_Idinstitucion_Elegida,
                                  v_Iddireccion_Elegida,
                                  r_Consejo.Idinstitucion,
                                  v_Iddireccion_Consejo,
                                  n_Insertadas,
                                  v_Codretorno,
                                  v_Datoserror);
          If v_Codretorno <> 0 Then
            Return;
          Else
            Total_Insertadas := Total_Insertadas + n_Insertadas;
          End If;
        
        Else
        
          v_Datoserror   := 'Buscar_y_Actualizar_Direccion: 3.b. Existe direccion especial en el Consejo: Actualizando direccion.';
          n_Actualizadas := 0;
          Actualizar_Dir_En_Consejo(p_Idpersona,
                                    v_Idinstitucion_Elegida,
                                    v_Iddireccion_Elegida,
                                    r_Consejo.Idinstitucion,
                                    v_Iddireccion_Consejo,
                                    n_Actualizadas,
                                    v_Codretorno,
                                    v_Datoserror);
          If v_Codretorno <> 0 Then
            Return;
          Else
            Total_Actualizadas := Total_Actualizadas + n_Actualizadas;
          End If;
        
        End If;
      
      End If;
    
      v_Datoserror := 'Buscar_y_Actualizar_Direccion: 4. Quitando el tipo de otras direcciones del Consejo que no se actualizan';
      Delete From Cen_Direccion_Tipodireccion Tip
       Where Tip.Idpersona = p_Idpersona
         And Tip.Idinstitucion = r_Consejo.Idinstitucion
         And Tip.Idtipodireccion = p_Tipoespecial
         And (v_Iddireccion_Consejo Is Null Or Tip.Iddireccion <> v_Iddireccion_Consejo);
      Update Cen_Direcciones dir
         Set Fechamodificacion = Sysdate,
             Usumodificacion   = 0, --no se puede poner ningun usumodificacion, porque fue lanzado desde otra institucion
             fechabaja         = Sysdate
       Where Idpersona = p_Idpersona
         And Idinstitucion = r_Consejo.Idinstitucion
         And Not Exists (Select *
                From Cen_Direccion_Tipodireccion Tip
               Where Tip.Idinstitucion = Dir.Idinstitucion
                 And Tip.Idpersona = Dir.Idpersona
                 And Tip.Iddireccion = Dir.Iddireccion);
      
      Total_Borradas := Total_Borradas + Sql%Rowcount;
    
      Total := Total + 1;
    
    End Loop;
  
    p_Codretorno := To_Char(0);
    p_Datoserror := Total_Actualizadas || ' actualizadas, ' || Total_Insertadas || ' nuevas y ' ||
                    Total_Borradas || ' borradas. Total recorridos: ' || Total;
  
  Exception
    When e_Error Then
      p_Codretorno := v_Codretorno;
      p_Datoserror := v_Datoserror;
    
    When Others Then
      p_Codretorno := Sqlcode;
      p_Datoserror := v_Datoserror || ': ' || Sqlerrm;
  End Buscar_y_Actualizar_Direccion;

  Procedure Insertar_Dir_En_Consejo(p_Idpersona             In Cen_Direcciones.Idpersona%Type,
                                    p_Idtipo_Copia          In Cen_Direccion_Tipodireccion.Idtipodireccion%Type,
                                    p_Idinstitucion_Colegio In Cen_Direcciones.Idinstitucion%Type,
                                    p_Iddireccion_Colegio   In Cen_Direcciones.Iddireccion%Type,
                                    p_Idinstitucion_Consejo In Cen_Direcciones.Idinstitucion%Type,
                                    v_Iddireccion_Consejo   Out Cen_Direcciones.Iddireccion%Type,
                                    Total_Insertadas        Out Number,
                                    p_Codretorno            Out Varchar2,
                                    p_Datoserror            Out Varchar2) Is
  
    v_Datoserror Varchar2(4000); --Mensaje de error en los procedimientos
  
  Begin
    v_Datoserror := 'Insertar_Dir_En_Consejo: obteniendo iddireccion nueva';
    Begin
      Select Max(Iddireccion) + 1
        Into v_Iddireccion_Consejo
        From Cen_Direcciones
       Where Idinstitucion = p_Idinstitucion_Consejo
         And Idpersona = p_Idpersona;
    Exception
      When No_Data_Found Then
        v_Iddireccion_Consejo := 1;
    End;
    If v_Iddireccion_Consejo Is Null Then
      v_Iddireccion_Consejo := 1;
    End If;
  
    v_Datoserror := 'Insertar_Dir_En_Consejo: copiando registro de direccion';
    Insert Into Cen_Direcciones
      (Idinstitucion,
       Idpersona,
       Iddireccion,
       Fechamodificacion,
       Usumodificacion,
       Preferente,
       Domicilio,
       Codigopostal,
       Telefono1,
       Telefono2,
       Movil,
       Fax1,
       Fax2,
       Correoelectronico,
       Paginaweb,
       Fechabaja,
       Idpais,
       Idprovincia,
       Idpoblacion,
       Poblacionextranjera,
       Fechacarga,
       Idinstitucionalta,
       Iddireccionalta)
      (Select p_Idinstitucion_Consejo,
              Idpersona,
              v_Iddireccion_Consejo,
              Sysdate,
              0, --no se puede poner ningun usumodificacion, porque fue lanzado desde otra institucion
              '', --las preferencias no se han de copiar: Preferente,
              Domicilio,
              Codigopostal,
              Telefono1,
              Telefono2,
              Movil,
              Fax1,
              Fax2,
              Correoelectronico,
              Paginaweb,
              Fechabaja,
              Idpais,
              Idprovincia,
              Idpoblacion,
              Poblacionextranjera,
              Fechamodificacion,
              Idinstitucion,
              Iddireccion
         From Cen_Direcciones
        Where Idpersona = p_Idpersona
          And Idinstitucion = p_Idinstitucion_Colegio
          And Iddireccion = p_Iddireccion_Colegio);
  
    v_Datoserror := 'Insertar_Dir_En_Consejo: insertando tipo de Censo Web';
    Insert Into Cen_Direccion_Tipodireccion
      (Idinstitucion, Idpersona, Iddireccion, Idtipodireccion, Fechamodificacion, Usumodificacion)
      (Select p_Idinstitucion_Consejo,
              Idpersona,
              v_Iddireccion_Consejo,
              p_Idtipo_Copia,
              Fechamodificacion,
              0 --no se puede poner ningun usumodificacion, porque fue lanzado desde otra institucion
         From Cen_Direcciones
        Where Idpersona = p_Idpersona
          And Idinstitucion = p_Idinstitucion_Colegio
          And Iddireccion = p_Iddireccion_Colegio);

    Total_Insertadas := Sql%Rowcount;
  
    p_Datoserror := 'Insertar_Dir_En_Consejo: Correcto';
    p_Codretorno := To_Char(0);
  
  Exception
    When Others Then
      p_Codretorno := Sqlcode;
      p_Datoserror := v_Datoserror || ': ' || Sqlerrm;
  End Insertar_Dir_En_Consejo;

  Procedure Actualizar_Dir_En_Consejo(p_Idpersona             In Cen_Direcciones.Idpersona%Type,
                                      p_Idinstitucion_Colegio In Cen_Direcciones.Idinstitucion%Type,
                                      p_Iddireccion_Colegio   In Cen_Direcciones.Iddireccion%Type,
                                      p_Idinstitucion_Consejo In Cen_Direcciones.Idinstitucion%Type,
                                      p_Iddireccion_Consejo   In Cen_Direcciones.Iddireccion%Type,
                                      Total_Actualizadas      Out Number,
                                      p_Codretorno            Out Varchar2,
                                      p_Datoserror            Out Varchar2) Is
  
    v_Datoserror Varchar2(4000); --Mensaje de error en los procedimientos
    r_Direccion_Elegida Cen_Direcciones%Rowtype;
  
  Begin
    v_Datoserror := 'Actualizar_Dir_En_Consejo: obteniendo direccion completa del colegio';
    Select *
      Into r_Direccion_Elegida
      From Cen_Direcciones
     Where Idpersona = p_Idpersona
       And Idinstitucion = p_Idinstitucion_Colegio
       And Iddireccion = p_Iddireccion_Colegio;
  
    v_Datoserror := 'Actualizar_Dir_En_Consejo: actualizando registro de direccion';
    Update Cen_Direcciones
       Set Fechamodificacion = Sysdate,
           Usumodificacion   = 0, --no se puede poner ningun usumodificacion, porque fue lanzado desde otra institucion
           --las preferencias no se han de copiar: Preferente          = r_Direccion_Elegida.Preferente,
           Domicilio           = r_Direccion_Elegida.Domicilio,
           Codigopostal        = r_Direccion_Elegida.Codigopostal,
           Telefono1           = r_Direccion_Elegida.Telefono1,
           Telefono2           = r_Direccion_Elegida.Telefono2,
           Movil               = r_Direccion_Elegida.Movil,
           Fax1                = r_Direccion_Elegida.Fax1,
           Fax2                = r_Direccion_Elegida.Fax2,
           Correoelectronico   = r_Direccion_Elegida.Correoelectronico,
           Paginaweb           = r_Direccion_Elegida.Paginaweb,
           Idpais              = r_Direccion_Elegida.Idpais,
           Idprovincia         = r_Direccion_Elegida.Idprovincia,
           Idpoblacion         = r_Direccion_Elegida.Idpoblacion,
           Poblacionextranjera = r_Direccion_Elegida.Poblacionextranjera,
           Fechacarga          = r_Direccion_Elegida.Fechamodificacion,
           Idinstitucionalta   = r_Direccion_Elegida.Idinstitucion,
           Iddireccionalta     = r_Direccion_Elegida.Iddireccion
     Where Idpersona = p_Idpersona
       And Idinstitucion = p_Idinstitucion_Consejo
       And Iddireccion = p_Iddireccion_Consejo;
    Total_Actualizadas := Sql%Rowcount;
  
    p_Datoserror := 'Actualizar_Dir_En_Consejo: Correcto';
    p_Codretorno := To_Char(0);
  
  Exception
    When Others Then
      p_Codretorno := Sqlcode;
      p_Datoserror := v_Datoserror || ': ' || Sqlerrm;
  End Actualizar_Dir_En_Consejo;

End Pkg_Siga_Censo;
/
