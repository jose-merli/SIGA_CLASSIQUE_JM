CREATE OR REPLACE Package Pkg_Siga_Abonos Is

  Type Reg_Direccion Is Record(
    Domicilio        Cen_Direcciones.Domicilio%Type,
    Codigopostal     Cen_Direcciones.Codigopostal%Type,
    Pais_Nombre      Cen_Pais.Nombre%Type,
    Pais_Iso         Cen_Pais.Cod_Iso%Type,
    Pais_Id          Cen_Pais.Idpais%Type,
    Provincia_Nombre Cen_Provincias.Nombre%Type,
    Provincia_Id     Cen_Provincias.Idprovincia%Type,
    Poblacion_Nombre Cen_Poblaciones.Nombre%Type,
    Poblacion_Id     Cen_Poblaciones.Idpoblacion%Type);

  Type Reg_Ordenante Is Record(
    Idinstitucion   Fac_Disqueteabonos.Idinstitucion%Type,
    Iddisqueteabono Fac_Disqueteabonos.Iddisqueteabono%Type,

    Fechacreacion  Date,
    Fechaejecucion Date,
    Nombre         Cen_Institucion.Nombre%Type,
    Idpersona      Cen_Institucion.Idpersona%Type,
    Cif            Cen_Persona.Nifcif%Type,
    Codigobanco    Fac_Bancoinstitucion.Bancos_Codigo%Type,
    Banco_Iban     Fac_Bancoinstitucion.Iban%Type,
    Banco_Bic      Cen_Bancos.Bic%Type,
    Sufijo         Fac_Sufijo.Sufijo%Type,
    Idsufijo       Fac_Sufijo.Idsufijo%Type,
    Direccion      Reg_Direccion);

  Type Reg_Abono Is Record(
    Idinstitucion Fac_Abono.Idinstitucion%Type,
    Idabono       Fac_Abono.Idabono%Type,
    Idpersona     Fac_Abono.Idpersona%Type,
    Idcuenta      Fac_Abono.Idcuenta%Type,

    Numeroabono                 Fac_Abono.Numeroabono%Type,
    Referenciainterna           Varchar2(1000),
    Observaciones               Varchar2(1000),
    Motivos                     Varchar2(1000),
    Concepto                    Varchar2(1000),
    Incluirletradoconceptobanco Varchar2(1),
    Fcs                         Varchar2(1),
    Proposito                   Varchar2(1000),

    Banco_Iban Cen_Cuentasbancarias.Iban%Type,
    Banco_Bic  Cen_Bancos.Bic%Type,
    Importe    Fac_Abonoincluidoendisquete.Importeabonado%Type,

    Benef_Id        Cen_Persona.Nifcif%Type,
    Benef_Nombre    Cen_Cuentasbancarias.Titular%Type,
    Benef_Direccion Reg_Direccion,
    Coleg_Ncoleg    Cen_Colegiado.Ncolegiado%Type,
    Coleg_Nombre    Varchar2(1000));

  Type m_Abonos Is Table Of Reg_Abono Index By Binary_Integer;

  Type Reg_Abonos Is Record(
    m_Abonossepa      m_Abonos,
    m_Abonosotro      m_Abonos,
    Conta_Abonossepa  Number,
    Conta_Abonosotro  Number,
    Subtot_Abonossepa Number,
    Subtot_Abonosotro Number);

  Procedure Generarficherotransferencias(p_Idinstitucion   In Fac_Disqueteabonos.Idinstitucion%Type,
                                         p_Iddisqueteabono In Fac_Disqueteabonos.Iddisqueteabono%Type,
                                         p_Idpropositosepa In Fac_Propositos.Idproposito%Type,
                                         p_Idpropositotros In Fac_Propositos.Idproposito%Type,
                                         p_Pathfichero     In Varchar2,
                                         p_Nombrefichero   In Varchar2,
                                         p_Idioma          In Adm_Lenguajes.Idlenguaje%Type,
                                         p_Codretorno      Out Varchar2,
                                         p_Datoserror      Out Varchar2);

End Pkg_Siga_Abonos;
/
CREATE OR REPLACE Package Body Pkg_Siga_Abonos Is

  c_Abonos_Sepa Varchar2(1) := '1';
  c_Abonos_Otro Varchar2(1) := '0';
  
  c_TipoFicheroTxt    Number := 0;
  c_TipoFicheroTxtXml Number := 1;
  c_TipoFicheroXml    Number := 2;
  
  e_Error EXCEPTION;

  ----
  -- Nombre:      F_RevisarCaracteresSEPA
  -- Descripcion: Transforma los caracteres no permitidos en SEPA en blancos 
  --              o bien en los caracteres homologos que si se permiten en SEPA.
  --              Si se pasa una cadena vacia o null, se devuelve un ' '.
  --
  -- Parametros:
  -- * P_REGISTRO - IN - Varchar2 - Texto que se quiere revisar y arreglar
  -- * Salida          - Varchar2 - Texto revisado y arreglado
  --
  -- Versiones:
  -- Fecha       Autor                 Descripcion
  -- 27/03/2014  Jorge Paez Trivino    Creacion.
  -- 21/05/2015  Adrian Ayala Gomez    Control de Null.
  ----
  Function f_Revisarcaracteressepa(p_Registro In Varchar2) Return Varchar2 Is
  
    v_Registro Varchar2(6000) := p_Registro;
  
  Begin
    If v_Registro Is Null Then
      v_Registro := ' ';
    End If;
  
    -- Transformacion de tildes y dieresis
    v_Registro := Regexp_Replace(v_Registro, '[ÁÀÄ]', 'A');
    v_Registro := Regexp_Replace(v_Registro, '[ÉÈË]', 'E');
    v_Registro := Regexp_Replace(v_Registro, '[ÍÌÏ]', 'I');
    v_Registro := Regexp_Replace(v_Registro, '[ÓÒÖ]', 'O');
    v_Registro := Regexp_Replace(v_Registro, '[ÚÙÜ]', 'U');
    v_Registro := Regexp_Replace(v_Registro, '[áàä]', 'a');
    v_Registro := Regexp_Replace(v_Registro, '[éèë]', 'e');
    v_Registro := Regexp_Replace(v_Registro, '[íìï]', 'i');
    v_Registro := Regexp_Replace(v_Registro, '[óòö]', 'o');
    v_Registro := Regexp_Replace(v_Registro, '[úùü]', 'u');
  
    -- Transformacion de eñes y cediña
    v_Registro := Replace(v_Registro, 'Ñ', 'N');
    v_Registro := Replace(v_Registro, 'ñ', 'n');
    v_Registro := Replace(v_Registro, 'Ç', 'C');
    v_Registro := Replace(v_Registro, 'ç', 'c');
  
    -- Transformacion de caracteres especiales
    v_Registro := Regexp_Replace(v_Registro, '[^-ABCDEFGHIJ-NOP-TUV-Zabcdefghij-nop-tuv-z0-9/?:().,''+]', ' ');
    -- En XML se puede quitar la comilla porque ya la transforma despues
    -- v_Registro := Regexp_Replace(v_Registro, '[^-ABCDEFGHIJ-NOP-TUV-Zabcdefghij-nop-tuv-z0-9/?:().,+]', ' ');
  
    Return v_Registro;
  End f_Revisarcaracteressepa;

  ----
  -- Nombre:      ObtenerDireccionFacturacion
  -- Descripcion: Obtiene la direccion de facturacion de la persona para SEPA.
  --
  -- Parametros:
  -- * P_IDINSTITUCION  - IN - NUMBER        - Identificador de la institucion
  -- * P_IDPERSONA      - IN - NUMBER        - Identificador de la persona
  -- * Salida                - Reg_Direccion - Registro con todos los datos de la direccion
  --
  -- Versiones:
  -- Fecha       Autor                 Descripcion
  -- 27/03/2014  Jorge Paez Trivino    Creacion.
  ----
  Function Obtenerdireccionfacturacion(p_Idinstitucion In Cen_Direcciones.Idinstitucion%Type,
                                       p_Idpersona     In Cen_Direcciones.Idpersona%Type)
    Return Reg_Direccion Is
  
    v_Iddireccion   Cen_Direcciones.Iddireccion%Type;
    r_Direccion     Cen_Direcciones%Rowtype;
    v_Poblacion     Cen_Poblaciones.Nombre%Type;
    v_Provincia     Cen_Provincias.Nombre%Type;
    v_Pais          Cen_Pais.Nombre%Type;
    v_Codigopaisiso Cen_Pais.Cod_Iso%Type;
    v_Direccion     Reg_Direccion;
  
  Begin
  
    -- Obtengo el identificador de la direccion de facturacion
    v_Iddireccion := f_Siga_Getiddireccion_Tipos2(p_Idinstitucion, p_Idpersona, 8, Null);
  
    -- Compruebo si tiene direccion de facturacion
    If (v_Iddireccion Is Not Null) Then
    
      -- Obtengo los datos de la direccion
      Select *
        Into r_Direccion
        From Cen_Direcciones
       Where Idinstitucion = p_Idinstitucion
         And Idpersona = p_Idpersona
         And Iddireccion = v_Iddireccion;
    
      -- Compruebo si tiene poblacion extranjera
      If (r_Direccion.Poblacionextranjera Is Null) Then
      
        -- Compruebo si tiene poblacion
        If (r_Direccion.Idpoblacion Is Null) Then
          v_Poblacion := Null;
        
        Else
          -- Tiene poblacion
          Select Nombre
            Into v_Poblacion
            From Cen_Poblaciones
           Where Idpoblacion = r_Direccion.Idpoblacion;
        End If;
      
        -- Compruebo si tiene provincia
        If (r_Direccion.Idprovincia Is Null) Then
          v_Provincia := Null;
        
        Else
          -- Tiene provincia
          Select Nombre
            Into v_Provincia
            From Cen_Provincias
           Where Idprovincia = r_Direccion.Idprovincia;
        End If;
      
      Else
        -- Tiene direccion extranjera
        v_Provincia := Null;
        v_Poblacion := r_Direccion.Poblacionextranjera;
      End If;
    
      -- Compruebo si tiene pais
      If (r_Direccion.Idpais Is Null) Then
        Select f_Siga_Getrecurso(Nombre, 1), Cod_Iso
          Into v_Pais, v_Codigopaisiso
          From Cen_Pais
         Where Idpais = '191'; -- JPT (09-10-2014): Con comillas entra por indice
      
      Else
        -- Tiene pais
        Select f_Siga_Getrecurso(Nombre, 1), Cod_Iso
          Into v_Pais, v_Codigopaisiso
          From Cen_Pais
         Where Idpais = r_Direccion.Idpais;
      End If;
    
      -- Cargo los datos obtenidos en el registro
      v_Direccion.Domicilio        := r_Direccion.Domicilio;
      v_Direccion.Codigopostal     := r_Direccion.Codigopostal;
      v_Direccion.Pais_Nombre      := v_Pais;
      v_Direccion.Pais_Iso         := v_Codigopaisiso;
      v_Direccion.Pais_Id          := r_Direccion.Idpais;
      v_Direccion.Provincia_Nombre := v_Provincia;
      v_Direccion.Provincia_Id     := r_Direccion.Idprovincia;
      v_Direccion.Poblacion_Nombre := v_Poblacion;
      v_Direccion.Poblacion_Id     := r_Direccion.Idpoblacion;
    
    Else
      -- Cargo los datos obtenidos en el registro
      v_Direccion.Domicilio        := Null;
      v_Direccion.Codigopostal     := Null;
      v_Direccion.Pais_Nombre      := Null;
      v_Direccion.Pais_Iso         := Null;
      v_Direccion.Pais_Id          := Null;
      v_Direccion.Provincia_Nombre := Null;
      v_Direccion.Provincia_Id     := Null;
      v_Direccion.Poblacion_Nombre := Null;
      v_Direccion.Poblacion_Id     := Null;
    End If;
  
    Return v_Direccion;
  End Obtenerdireccionfacturacion;

  ----
  -- Nombre:      Obtenerdatosordenante
  -- Descripcion: Obtiene todos los datos relativos al ordenante para el fichero SEPA.
  --
  -- Parametros:
  -- * P_IDINSTITUCION   - IN - NUMBER        - Id de la institucion
  -- * P_IDDISQUETEABONO - IN - NUMBER        - Id del fichero que se va a generar
  -- * Salida                 - Reg_Ordenante - Registro con todos los datos del ordenante
  --
  -- Versiones:
  -- Fecha       Autor                 Descripcion
  -- 21/05/2014  Adrian Ayala Gomez    Creacion.
  ----
  Function Obtenerdatosordenante(p_Idinstitucion   In Fac_Disqueteabonos.Idinstitucion%Type,
                                 p_Iddisqueteabono In Fac_Disqueteabonos.Iddisqueteabono%Type)
    Return Reg_Ordenante Is
    v_Regordenante Reg_Ordenante;
  
  Begin
    Select Dis.Idinstitucion,
           Dis.Iddisqueteabono,
           Dis.Fecha,
           Dis.Fechaejecucion,
           Ins.Nombre,
           Ins.Idpersona,
           Per.Nifcif,
           Banins.Bancos_Codigo,
           Banins.Iban,
           Ban.Bic
      Into v_Regordenante.Idinstitucion,
           v_Regordenante.Iddisqueteabono,
           v_Regordenante.Fechacreacion,
           v_Regordenante.Fechaejecucion,
           v_Regordenante.Nombre,
           v_Regordenante.Idpersona,
           v_Regordenante.Cif,
           v_Regordenante.Codigobanco,
           v_Regordenante.Banco_Iban,
           v_Regordenante.Banco_Bic
      From Fac_Disqueteabonos   Dis,
           Fac_Bancoinstitucion Banins,
           Cen_Bancos           Ban,
           Cen_Institucion      Ins,
           Cen_Persona          Per
     Where Dis.Idinstitucion = Banins.Idinstitucion
       And Dis.Bancos_Codigo = Banins.Bancos_Codigo
       And Banins.Cod_Banco = Ban.Codigo
       And Dis.Idinstitucion = Ins.Idinstitucion
       And Ins.Idpersona = Per.Idpersona
       And Dis.Idinstitucion = p_Idinstitucion
       And Dis.Iddisqueteabono = p_Iddisqueteabono;
  
    Begin
      Select Suf.Idsufijo, Suf.Sufijo
        Into v_Regordenante.Idsufijo, v_Regordenante.Sufijo
        From Fac_Disqueteabonos Dis, Fac_Sufijo Suf
       Where Suf.Idinstitucion = Dis.Idinstitucion
         And Suf.Idsufijo = Dis.Idsufijo
         And Dis.Idinstitucion = p_Idinstitucion
         And Dis.Iddisqueteabono = p_Iddisqueteabono;
    Exception
      When No_Data_Found Then
        Return Null;
    End;
  
    Return v_Regordenante;
  End Obtenerdatosordenante;

  ----
  -- Nombre:      Obtenerabonosdisquete
  -- Descripcion: Obtiene todos los datos de los abonos del fichero SEPA que se va a generar.
  --
  -- Parametros:
  -- * P_IDINSTITUCION   - IN - NUMBER        - Id de la institucion
  -- * P_IDDISQUETEABONO - IN - NUMBER        - Id del fichero que se va a generar
  -- * P_IDPROPOSITOSEPA - IN - NUMBER        - Id del proposito SEPA indicado por el usuario
  -- * P_IDPROPOSITOTROS - IN - NUMBER        - Id del proposito OTROS indicado por el usuario
  -- * Salida                 - Reg_Abonos    - Registro con todos los datos de los abonos
  --
  -- Versiones:
  -- Fecha       Autor                 Descripcion
  -- 21/05/2014  Adrian Ayala Gomez    Creacion.
  ----
  Function Obtenerabonosdisquete(v_Idinstitucion   In Fac_Disqueteabonos.Idinstitucion%Type,
                                 v_Iddisqueteabono In Fac_Disqueteabonos.Iddisqueteabono%Type,
                                 v_Idpropositosepa In Fac_Propositos.Idproposito%Type,
                                 v_Idpropositotros In Fac_Propositos.Idproposito%Type)
    Return Reg_Abonos Is
    Cursor c_Abonosdisquete(v_Idinstitucion In Fac_Disqueteabonos.Idinstitucion%Type, v_Iddisqueteabono In Fac_Disqueteabonos.Iddisqueteabono%Type, v_Sepa In Varchar2, v_Idpropositosepa In Fac_Propositos.Idproposito%Type, v_Idpropositotros In Fac_Propositos.Idproposito%Type) Is
      Select Fac_Abono.Idinstitucion,
             Fac_Abono.Idabono,
             Fac_Abono.Idpersona,
             Fac_Abono.Idcuenta,
             
             Fac_Abono.Numeroabono,
             Fac_Abono.Idinstitucion || ' ' || Fac_Abono.Idpersona || ' ' || Fac_Abono.Idabono As Referenciainterna,
             Fac_Abono.Observaciones,
             Fac_Abono.Motivos,
             f_Siga_Getparametro('FCS', 'INCLUIR_LETRADO_CONCEPTO_BANCO', v_Idinstitucion) As Incluirletradoconceptobanco,
             Decode(Fac_Abono.Idpagosjg, Null, '0', '1') As Fcs,
             --calculo de propositos
             (Select Fac_Propositos.Codigo
                From Fac_Propositos
               Where Fac_Propositos.Idproposito =
                     Decode(Fac_Abono.Idpagosjg,
                            Null,
                            Decode(Cen_Pais.Sepa, '1', v_Idpropositosepa, v_Idpropositotros), --Tipo FAC: se cogera el proposito indicado por el usuario
                            
                            (Select Decode(Cen_Pais.Sepa,
                                           '1',
                                           Fcs_Pagosjg.Idpropsepa,
                                           Fcs_Pagosjg.Idpropotros)
                               From Fcs_Pagosjg
                              Where Fcs_Pagosjg.Idinstitucion = Fac_Abono.Idinstitucion
                                And Fcs_Pagosjg.Idpagosjg = Fac_Abono.Idpagosjg))) As Proposito, --Tipo FCS
             
             Cen_Cuentasbancarias.Iban                  As Banco_Iban,
             Cen_Bancos.Bic                             As Banco_Bic,
             Fac_Abonoincluidoendisquete.Importeabonado As Importe,
             
             (Select Substr(Cen_Persona.Nifcif, 1, 9)
                From Cen_Persona
               Where Fac_Abono.Idpersona = Cen_Persona.Idpersona) As Benef_Id,
             Cen_Cuentasbancarias.Titular As Benef_Nombre,
             
             (Select Decode(Comunitario, '1', Ncomunitario, Ncolegiado)
                From Cen_Colegiado
               Where Cen_Colegiado.Idinstitucion = Fac_Abono.Idinstitucion
                 And Cen_Colegiado.Idpersona = Fac_Abono.Idperorigen) As Coleg_Ncoleg,
             (Select Cen_Persona.Nombre || ' ' || Cen_Persona.Apellidos1 || ' ' ||
                     Cen_Persona.Apellidos2
                From Cen_Persona
               Where Cen_Persona.Idpersona = Fac_Abono.Idperorigen) As Coleg_Nombre
      
        From Fac_Abonoincluidoendisquete, Fac_Abono, Cen_Cuentasbancarias, Cen_Bancos, Cen_Pais
       Where Fac_Abonoincluidoendisquete.Idinstitucion = Fac_Abono.Idinstitucion
         And Fac_Abonoincluidoendisquete.Idabono = Fac_Abono.Idabono
         And Fac_Abono.Idinstitucion = Cen_Cuentasbancarias.Idinstitucion
         And Fac_Abono.Idpersona = Cen_Cuentasbancarias.Idpersona
         And Fac_Abono.Idcuenta = Cen_Cuentasbancarias.Idcuenta
         And Cen_Cuentasbancarias.Cbo_Codigo = Cen_Bancos.Codigo
         And Cen_Pais.Idpais = Cen_Bancos.Idpais
            
         And Fac_Abonoincluidoendisquete.Idinstitucion = v_Idinstitucion
         And Fac_Abonoincluidoendisquete.Iddisqueteabono = v_Iddisqueteabono
         And Cen_Pais.Sepa = v_Sepa;
         
    r_Abonos  Reg_Abonos;
    Separador Varchar2(3) := ' - ';
  Begin
    r_Abonos.Conta_Abonossepa := 0;
    For r_Abonodisquete In c_Abonosdisquete(v_Idinstitucion,
                                            v_Iddisqueteabono,
                                            c_Abonos_Sepa,
                                            v_Idpropositosepa,
                                            v_Idpropositotros) Loop
      r_Abonos.Conta_Abonossepa := r_Abonos.Conta_Abonossepa + 1;
      r_Abonos.Subtot_Abonossepa := r_Abonos.Subtot_Abonossepa + r_Abonodisquete.Importe;
    
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Idinstitucion := r_Abonodisquete.Idinstitucion;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Idabono := r_Abonodisquete.Idabono;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Idpersona := r_Abonodisquete.Idpersona;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Idcuenta := r_Abonodisquete.Idcuenta;
    
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Numeroabono := r_Abonodisquete.Numeroabono;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Referenciainterna := r_Abonodisquete.Referenciainterna;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Observaciones := r_Abonodisquete.Observaciones;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Motivos := r_Abonodisquete.Motivos;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Incluirletradoconceptobanco := r_Abonodisquete.Incluirletradoconceptobanco;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Fcs := r_Abonodisquete.Fcs;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Proposito := r_Abonodisquete.Proposito;
    
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Banco_Iban := r_Abonodisquete.Banco_Iban;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Banco_Bic := r_Abonodisquete.Banco_Bic;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Importe := r_Abonodisquete.Importe;
    
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Benef_Id := r_Abonodisquete.Benef_Id;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Benef_Nombre := r_Abonodisquete.Benef_Nombre;
    
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Coleg_Ncoleg := r_Abonodisquete.Coleg_Ncoleg;
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Coleg_Nombre := r_Abonodisquete.Coleg_Nombre;
    
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Concepto := r_Abonodisquete.Observaciones || Separador || 
                                                                   r_Abonodisquete.Motivos || Separador ||
                                                                   r_Abonodisquete.Coleg_Ncoleg || Separador ||
                                                                   r_Abonodisquete.Coleg_Nombre;
    
      r_Abonos.m_Abonossepa(r_Abonos.Conta_Abonossepa).Benef_Direccion := Obtenerdireccionfacturacion(r_Abonodisquete.Idinstitucion,
                                                                                                      r_Abonodisquete.Idpersona);
    
    End Loop;
  
    r_Abonos.Conta_Abonosotro := 0;
    For r_Abonodisquete In c_Abonosdisquete(v_Idinstitucion,
                                            v_Iddisqueteabono,
                                            c_Abonos_Otro,
                                            v_Idpropositosepa,
                                            v_Idpropositotros) Loop
      r_Abonos.Conta_Abonosotro := r_Abonos.Conta_Abonosotro + 1;
      r_Abonos.Subtot_Abonosotro := r_Abonos.Subtot_Abonosotro + r_Abonodisquete.Importe;
    
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Idinstitucion := r_Abonodisquete.Idinstitucion;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Idabono := r_Abonodisquete.Idabono;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Idpersona := r_Abonodisquete.Idpersona;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Idcuenta := r_Abonodisquete.Idcuenta;
    
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Numeroabono := r_Abonodisquete.Numeroabono;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Referenciainterna := r_Abonodisquete.Referenciainterna;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Observaciones := r_Abonodisquete.Observaciones;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Motivos := r_Abonodisquete.Motivos;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Incluirletradoconceptobanco := r_Abonodisquete.Incluirletradoconceptobanco;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Fcs := r_Abonodisquete.Fcs;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Proposito := r_Abonodisquete.Proposito;
    
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Banco_Iban := r_Abonodisquete.Banco_Iban;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Banco_Bic := r_Abonodisquete.Banco_Bic;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Importe := r_Abonodisquete.Importe;
    
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Benef_Id := r_Abonodisquete.Benef_Id;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Benef_Nombre := r_Abonodisquete.Benef_Nombre;
    
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Coleg_Ncoleg := r_Abonodisquete.Coleg_Ncoleg;
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Coleg_Nombre := r_Abonodisquete.Coleg_Nombre;
    
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Concepto := r_Abonodisquete.Observaciones || Separador || 
                                                                   r_Abonodisquete.Motivos || Separador ||
                                                                   r_Abonodisquete.Coleg_Ncoleg || Separador ||
                                                                   r_Abonodisquete.Coleg_Nombre;
    
      r_Abonos.m_Abonosotro(r_Abonos.Conta_Abonosotro).Benef_Direccion := Obtenerdireccionfacturacion(r_Abonodisquete.Idinstitucion,
                                                                                                      r_Abonodisquete.Idpersona);
    
    End Loop;
  
    Return r_Abonos;
  End Obtenerabonosdisquete;

  ----
  -- Nombre:      Formatea_Concepto
  -- Descripcion: Como no tenemos un concepto definido claramente en SIGA, tenemos que generarlo
  --              a partir de ciertos datos y segun una serie de parametros
  --
  -- Parametros:
  -- * p_Observaciones       - IN - Varchar2 - Campo observaciones
  -- * p_Motivos             - IN - Varchar2 - Campos motivos
  -- * p_Coleg_Nombre        - IN - Varchar2 - Nombre completo del colegiado
  -- * p_Coleg_Ncoleg        - IN - Varchar2 - Numero del colegiado en el colegio
  -- * p_Longmaxima          - IN - NUMBER   - Maxima longitud que puede ocupar el concepto
  -- * p_Esfcs               - IN - Varchar2 - Indica si el abono es o no de Justicia Gratuita
  -- * p_Incorporarcolegiado - IN - Varchar2 - Indica si se ha configurado en el colegio que hay
  --                                           que incorporar los datos del colegiado al concepto
  -- * Salida                     - Varchar2 - El concepto
  --
  -- Versiones:
  -- Fecha       Autor                 Descripcion
  -- 21/05/2014  Adrian Ayala Gomez    Creacion.
  ----
  Function Formatea_Concepto(p_Observaciones       Varchar2,
                             p_Motivos             Varchar2,
                             p_Coleg_Nombre        Varchar2,
                             p_Coleg_Ncoleg        Varchar2,
                             p_Longmaxima          Number,
                             p_Esfcs               Varchar2,
                             p_Incorporarcolegiado Varchar2) Return Varchar2 Is
    v_Concepto      Varchar2(4000);
    v_Observaciones Varchar2(4000);
    v_Motivos       Varchar2(4000);
    v_Coleg_Nombre  Varchar2(4000);
    v_Coleg_Ncoleg  Varchar2(4000);
  
    Separador           Varchar2(3) := ' - ';
    Recortefinal        Varchar2(4) := ' ...';
    v_Tamanyo_Bloque1   Number;
    v_Tamanyo_Bloque2   Number;
    Longitud_Ncolegiado Number := 6;
  Begin
    -- eliminando caracteres no permitidos
    v_Observaciones := f_Revisarcaracteressepa(p_Observaciones);
    v_Motivos       := f_Revisarcaracteressepa(p_Motivos);
    v_Coleg_Nombre  := f_Revisarcaracteressepa(p_Coleg_Nombre);
    v_Coleg_Ncoleg  := f_Revisarcaracteressepa(p_Coleg_Ncoleg);
  
    If p_Esfcs = '0' Then
      If Length(v_Observaciones) + Length(Separador) + Length(v_Motivos) < p_Longmaxima Then
        v_Concepto := Rpad(v_Observaciones || Separador || v_Motivos, p_Longmaxima, ' ');
      Else
        v_Tamanyo_Bloque1 := p_Longmaxima / 5 * 2;
        v_Tamanyo_Bloque2 := p_Longmaxima - v_Tamanyo_Bloque1 - Length(Separador);
        v_Concepto        := Substr(v_Observaciones, 1, v_Tamanyo_Bloque1) || Separador ||
                             Substr(v_Motivos, 1, v_Tamanyo_Bloque2) || Recortefinal;
      End If;
    Elsif p_Esfcs = '1' And p_Incorporarcolegiado = '0' Then
      If Length(v_Observaciones) < p_Longmaxima Then
        v_Concepto := Rpad(v_Observaciones, p_Longmaxima, ' ');
      Else
        v_Concepto := Substr(v_Observaciones, 1, p_Longmaxima - Length(Recortefinal)) ||
                      Recortefinal;
      End If;
    Elsif p_Esfcs = '1' And p_Incorporarcolegiado = '1' Then
      If Length(v_Observaciones) + Longitud_Ncolegiado + Length(v_Coleg_Nombre) +
         2 * Length(Separador) < p_Longmaxima Then
        v_Concepto := Rpad(v_Observaciones || Separador ||
                           Substr(v_Coleg_Ncoleg, 1, Longitud_Ncolegiado) || Separador ||
                           v_Coleg_Nombre,
                           p_Longmaxima,
                           ' ');
      Else
        v_Tamanyo_Bloque1 := p_Longmaxima / 5 * 3;
        v_Tamanyo_Bloque2 := p_Longmaxima - v_Tamanyo_Bloque1 - Longitud_Ncolegiado -
                             2 * Length(Separador);
        v_Concepto        := Substr(v_Observaciones, 1, v_Tamanyo_Bloque1) || Separador ||
                             Substr(v_Coleg_Ncoleg, 1, Longitud_Ncolegiado) || Separador ||
                             Substr(v_Coleg_Nombre, 1, v_Tamanyo_Bloque2) || Recortefinal;
      End If;
    Else
      v_Concepto := Null;
    End If;
  
    Return v_Concepto;
  End Formatea_Concepto;
  
    /****************************************************************************************************************/
    /* Nombre: CabOrdenanteXML */
    /* Descripcion: Crea una Cabecera de registros con informacion del fichero y Ordenante en formato xml (SEPA) */
    /* l_sepa - OUT - Contenedor del documento xml a generar */
    /* l_CstmrCdtTrfInitn_node - OUT - Nodo raiz del mensaje apartir del cual sigue la Cabezera Ordenante */  
    /* v_Ordenante - IN - Registro con los datos del Ordenante - Reg_Ordenante */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 29/06/2015 - Autor: Oscar de la Torre Noheda */
    /* Version: 2.0 - Fecha Modificacion:  - Autor: */
    /****************************************************************************************************************/

    PROCEDURE CabOrdenanteXML(
        l_sepa OUT DBMS_XMLDOM.DOMDocument,
        l_CstmrCdtTrfInitn_node OUT DBMS_XMLDOM.DomNode,
        v_Ordenante IN Reg_Ordenante,
        p_Idioma In Adm_Lenguajes.Idlenguaje%Type,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS
    
        l_Document_element DBMS_XMLDOM.DOMElement; -- variable tipo elemento nodo
        l_root_node DBMS_XMLDOM.DomNode; -- variable tipo nodo a insertar
        l_Document_node DBMS_XMLDOM.DomNode;
        l_GrpHdr_node DBMS_XMLDOM.DomNode;        
        l_MsgId_node DBMS_XMLDOM.DomNode;
        l_CreDtTm_node DBMS_XMLDOM.DomNode;
        l_NbOfTxs_node DBMS_XMLDOM.DomNode;
        l_CtrlSum_node DBMS_XMLDOM.DomNode;
        l_InitgPty_node DBMS_XMLDOM.DomNode;
        l_Nm_node DBMS_XMLDOM.DomNode;
        l_Id_node DBMS_XMLDOM.DomNode;
        l_Id2_node DBMS_XMLDOM.DomNode;
        l_OrgId_node DBMS_XMLDOM.DomNode;
        l_Othr_node DBMS_XMLDOM.DomNode;        
        dummy DBMS_XMLDOM.DomNode;
        v_IdMensaje VARCHAR2(35); --MsgId - Identificacion del mensaje
        v_FechaCreacion  VARCHAR2(19); --CreDtTm - Fecha y hora de creacion
        v_NombreOrdenante VARCHAR2(70); --Nm - Nombre
        v_IdOrdenante VARCHAR2(35); --Id - Identificacion
        v_Datoserror VARCHAR2(4000) := Null;

    BEGIN
        v_Datoserror := 'CabOrdenanteXML: Creacion del documento - sepa xml';
        l_sepa := DBMS_XMLDOM.newDomDocument;

        v_Datoserror := 'CabOrdenanteXML: Creacion del nodo raiz del documento - sepa xml';
        l_root_node := DBMS_XMLDOM.makeNode(l_sepa);

        v_Datoserror := 'CabOrdenanteXML: Insertar Nodo Document (documento) - sepa xml';
        l_Document_element := DBMS_XMLDOM.createElement(l_sepa, 'Document');
        l_Document_node := DBMS_XMLDOM.appendChild(l_root_node, DBMS_XMLDOM.makeNode(l_Document_element));

        v_Datoserror := 'CabOrdenanteXML: Insertar Atributos del nodo Document - sepa xml';
        DBMS_XMLDOM.setAttribute(l_Document_element, 'xmlns', 'urn:iso:std:iso:20022:tech:xsd:pain.001.001.03');

        --[1..1] + Raiz del mensaje Transferencias  <CstmrCdtTrfInitn>
        v_Datoserror := 'CabOrdenanteXML: Insertar Nodo CstmrCdtTrfInitn (Raiz del mensaje) - sepa xml';
        l_CstmrCdtTrfInitn_node := DBMS_XMLDOM.appendChild(l_Document_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CstmrCdtTrfInitn')));

        -- 1.0 [1..1] + Cabecera Ordenante <GrpHdr> - Localizacion /Document/CstmrCdtTrfInitn/GrpHdr
        v_Datoserror := 'CabOrdenanteXML: Creo Nodo GrpHdr (Cabecera) - Localización /Document/CstmrCdtTrfInitn/GrpHdr';
        l_GrpHdr_node := DBMS_XMLDOM.appendChild(l_CstmrCdtTrfInitn_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'GrpHdr')));

        -- 1.1 [1..1] ++ Identificacion del mensaje <MsgId> 35 - Localizacion /Document/CstmrCdtTrfInitn/GrpHdr/MsgId
        v_Datoserror := 'CabOrdenanteXML: Creo Nodo GrpHdr (Cabecera) - Localización /Document/CstmrCdtTrfInitn/GrpHdr/MsgId';
        v_IdMensaje := F_RevisarCaracteresSEPA (NVL(TRIM(v_Ordenante.Idinstitucion), ' ') || NVL(TRIM(v_Ordenante.Iddisqueteabono), ' ') || '001'); --MsgId - Identificacion del mensaje
        l_MsgId_node := DBMS_XMLDOM.appendChild(l_GrpHdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'MsgId')));
        dummy := DBMS_XMLDOM.appendChild(l_MsgId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_IdMensaje)));

        -- 1.2 [1..1] ++ Fecha y hora de creacion <CreDtTm> 19 - Localizacion /Document/CstmrCdtTrfInitn/GrpHdr/CreDtTm
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.cabecera', p_Idioma);
        If (v_Ordenante.Fechacreacion Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.fechaCreacion', p_Idioma);
            Raise e_error;
        End If;
        v_FechaCreacion := to_char(v_Ordenante.Fechacreacion,'YYYY-MM-DD')||'T00:00:00'; --CreDtTm - Fecha y hora de creacion
        If (trim(v_FechaCreacion) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.fechaCreacion', p_Idioma);
            Raise e_error;
        End If;
        l_CreDtTm_node := DBMS_XMLDOM.appendChild(l_GrpHdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CreDtTm')));
        dummy := DBMS_XMLDOM.appendChild(l_CreDtTm_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_FechaCreacion)));

        -- 1.6 [1..1] ++ Numero de operaciones <NbOfTxs> 5 - Localizacion /Document/CstmrCdtTrfInitn/GrpHdr/NbOfTxs
        v_Datoserror := 'CabOrdenanteXML: Creo Nodo NbOfTxs (5 - Número de operaciones) - Localización /Document/CstmrCdtTrfInitn/GrpHdr/NbOfTxs';
        l_NbOfTxs_node := DBMS_XMLDOM.appendChild(l_GrpHdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'NbOfTxs')));
        dummy := DBMS_XMLDOM.appendChild(l_NbOfTxs_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, '0')));

        -- 1.7 [0..1] ++ Control de suma <CtrlSum> 19 - Localizacion /Document/CstmrCdtTrfInitn/GrpHdr/CtrlSum
        v_Datoserror := 'CabOrdenanteXML: Creo Nodo CtrlSum (19 - Control de suma) - Localización /Document/CstmrCdtTrfInitn/GrpHdr/CtrlSum';
        l_CtrlSum_node := DBMS_XMLDOM.appendChild(l_GrpHdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CtrlSum')));
        dummy := DBMS_XMLDOM.appendChild(l_CtrlSum_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, '0')));

        -- 1.8 [1..1] ++ Parte iniciadora <InitgPty> - Localizacion /Document/CstmrCdtTrfInitn/GrpHdr/InitgPty
        v_Datoserror := 'CabOrdenanteXML: Creo Nodo InitgPty (Parte iniciadora) - Localización /Document/CstmrCdtTrfInitn/GrpHdr/InitgPty';
        l_InitgPty_node := DBMS_XMLDOM.appendChild(l_GrpHdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'InitgPty')));

        -- 1.8 [0..1] +++ Nombre <Nm> 70 - Localizacion /Document/CstmrCdtTrfInitn/GrpHdr/InitgPty/Nm
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.cabecera', p_Idioma);
        v_NombreOrdenante := F_RevisarCaracteresSEPA(Nvl(SUBSTR(TRIM(v_Ordenante.Nombre),1,70), ' ')); --Nm - Nombre
        If (trim(v_NombreOrdenante) Is Not Null) Then
          l_Nm_node := DBMS_XMLDOM.appendChild(l_InitgPty_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Nm')));
          dummy := DBMS_XMLDOM.appendChild(l_Nm_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_NombreOrdenante)));
        End If;

        -- 1.8 [0..1] +++ Identificacion <Id> - Localizacion /Document/CstmrCdtTrfInitn/GrpHdr/InitgPty/Id
        v_Datoserror := 'CabOrdenanteXML: Creo Nodo Id (Identificación) - Localización /Document/CstmrCdtTrfInitn/GrpHdr/InitgPty/Id';
        l_Id_node := DBMS_XMLDOM.appendChild(l_InitgPty_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));

        -- 1.8 [1..1]{Or ++++ Persona juridica <OrgId> - Localizacion /Document/CstmrCdtTrfInitn/GrpHdr/InitgPty/Id/OrgId
        v_Datoserror := 'CabOrdenanteXML: Creo Nodo OrgId (Persona jurídica) - Localización /Document/CstmrCdtTrfInitn/GrpHdr/InitgPty/Id/OrgId';
        l_OrgId_node := DBMS_XMLDOM.appendChild(l_Id_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'OrgId')));

        -- [0..1] +++++ Otra <Othr> - Localizacion /Document/CstmrCdtTrfInitn/GrpHdr/InitgPty/Id/OrgId/Othr
        v_Datoserror := 'CabOrdenanteXML: Creo Nodo Othr (Otra) - Localización /Document/CstmrCdtTrfInitn/GrpHdr/InitgPty/Id/OrgId/Othr';
        l_Othr_node := DBMS_XMLDOM.appendChild(l_OrgId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Othr')));

        -- [1..1] ++++++ Identificacion <Id> 35 - Localizacion /Document/CstmrCdtTrfInitn/GrpHdr/InitgPty/Id/OrgId/Othr/Id
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.cabecera', p_Idioma);
        If (v_Ordenante.Cif Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.cifColegio', p_Idioma);
            Raise e_error;
        End If;
        If (v_Ordenante.Sufijo Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.sufijo', p_Idioma);
            Raise e_error;
        End If;
        v_IdOrdenante := F_RevisarCaracteresSEPA(TRIM(v_Ordenante.Cif) || Lpad(v_Ordenante.Sufijo, 3, '0'));--Id - Identificacion
        If (trim(v_IdOrdenante) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.cifOsufijo.caracteresNoValidos', p_Idioma);
            Raise e_error;
        End If;
        l_Id2_node := DBMS_XMLDOM.appendChild (l_Othr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));
        dummy := DBMS_XMLDOM.appendChild(l_Id2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_IdOrdenante)));

        v_Datoserror := 'CabOrdenanteXML: Actualizacion de los parametros de error';
        p_CodRetorno := To_Char(0);
        p_DatosError := Null;

    Exception
        When e_error Then
            p_Codretorno := 1;
            p_Datoserror := v_Datoserror;
            Return;
        WHEN OTHERS THEN
            p_Codretorno := To_Char(Sqlcode);
            p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END CabOrdenanteXML;

    /****************************************************************************************************************/
    /* Nombre: BloqueOrdenateXML */
    /* Descripcion: Crea un bloque de registros del Ordenante(Información del pago) en formato xml (SEPA) */
    /* l_sepa - IN OUT - Contenedor del documento xml a generar */
    /* l_CstmrCdtTrfInitn_node - IN - Nodo raiz del mensaje apartir del cual sigue la Cabezera Ordenante */
    /* l_PmtInf_node - OUT - Nodo del bloque; informacion del pago (Ordenante) */
    /* v_Ordenante - IN - Registro con los datos del Ordenante - Reg_Ordenante */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 29/06/2015 - Autor: Oscar de la Torre Noheda */
    /* Version: 2.0 - Fecha Modificacion:  - Autor: */
    /****************************************************************************************************************/
    PROCEDURE BloqueOrdenateXML(
        l_sepa IN OUT DBMS_XMLDOM.DOMDocument,
        l_CstmrCdtTrfInitn_node IN DBMS_XMLDOM.DomNode,
        l_PmtInf_node OUT DBMS_XMLDOM.DomNode,
        v_Ordenante IN Reg_Ordenante,
        p_Idioma In Adm_Lenguajes.Idlenguaje%Type,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) IS
        l_PmtInfId_node DBMS_XMLDOM.DomNode;
        l_PmtMtd_node DBMS_XMLDOM.DomNode; 
        l_NbOfTxs_node DBMS_XMLDOM.DomNode;
        l_CtrlSum_node DBMS_XMLDOM.DomNode;
        l_ReqdExctnDt_node DBMS_XMLDOM.DomNode;
        l_Dbtr_node DBMS_XMLDOM.DomNode;
        l_Nm_node DBMS_XMLDOM.DomNode;
        l_PstlAdr_node DBMS_XMLDOM.DomNode;
        l_Ctry_node DBMS_XMLDOM.DomNode;
        l_AdrLine_node DBMS_XMLDOM.DomNode;
        l_AdrLine2_node DBMS_XMLDOM.DomNode;
        l_DbtrAcct_node DBMS_XMLDOM.DomNode;
        l_Id_node DBMS_XMLDOM.DomNode;
        l_Id2_node DBMS_XMLDOM.DomNode;
        l_Id3_node DBMS_XMLDOM.DomNode;
        l_IBAN_node DBMS_XMLDOM.DomNode;
        l_DbtrAgt_node DBMS_XMLDOM.DomNode;
        l_FinInstnId_node DBMS_XMLDOM.DomNode;
        l_BIC_node DBMS_XMLDOM.DomNode;
        l_OrgId_node DBMS_XMLDOM.DomNode;
        l_Othr_node DBMS_XMLDOM.DomNode; 
        dummy DBMS_XMLDOM.DomNode;

        v_IdPago VARCHAR2(35); --l_PmtInfId_textnode - Identificacion de la informacion del pago
        v_MetodoPago VARCHAR2(3) := 'TRF'; --l_PmtMtd_textnode - Metodo de pago  'DD
        v_FechaPago VARCHAR2(10); --l_ReqdExctnDt_textnode - Fecha de cobro
        v_NomOrdenante VARCHAR2(70); --l_Nm_textnode - Nombre
        v_CodPais VARCHAR2(2); --l_Ctry_textnode - Pais
        v_Direccion_Total VARCHAR2(140); -- Uno los tres campos de la direccion
        v_Direccion VARCHAR2(70); --l_AdrLine_textnode - Direccion1
        v_DireccionResto VARCHAR2(70); --l_AdrLine_textnode - Direccion2
        v_Iban VARCHAR2(34); --l_IBAN_textnode - IBAN
        v_Bic VARCHAR2(11); --l_BIC_textnode - BIC
        v_Identificacion VARCHAR2(35); --l_Id_textnode - Identificacion
        v_Datoserror VARCHAR2(4000) := Null;

    BEGIN

        v_Datoserror := 'BloqueOrdenateXML: Transformo las variables de direccion de facturacion del Ordenante';
        v_Direccion_Total := trim(NVL(v_Ordenante.DIRECCION.DOMICILIO, '')) || ' ' ||
                             trim(NVL(v_Ordenante.DIRECCION.CODIGOPOSTAL, ''))  || ' ' ||
                             trim(NVL(v_Ordenante.DIRECCION.POBLACION_NOMBRE, '')) || ' ' ||
                             trim(NVL(v_Ordenante.DIRECCION.PROVINCIA_NOMBRE, ''));

        -- CREAR NUEVO NODO CABEZERA Y AGREGARLO AL NODO RAIZ
        -- 2.0 [1..n] + Informacion del pago <PmtInf> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo PmtInf (Información del pago) - Localización /Document/CstmrCdtTrfInitn/PmtInf';
        l_PmtInf_node := DBMS_XMLDOM.appendChild(l_CstmrCdtTrfInitn_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PmtInf')));

        -- 2.1 [1..1] ++ Identificacion de la informacion del pago <PmtInfId> 35 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/PmtInfId
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo PmtInfId (Identificacion de la Información del pago) - Localización /Document/CstmrCdtTrfInitn/PmtInf/PmtInfId';
        v_IdPago := F_RevisarCaracteresSEPA (v_Ordenante.Idinstitucion || v_Ordenante.Iddisqueteabono || '001'); --l_PmtInfId_textnode - Identificacion de la informacion del pago
        l_PmtInfId_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PmtInfId')));
        dummy := DBMS_XMLDOM.appendChild(l_PmtInfId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_IdPago))); 

        -- 2.2 [1..1] ++ Metodo de pago <PmtMtd> 3 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/PmtMtd
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo PmtMtd (3 - Método de pago) - Localización /Document/CstmrCdtTrfInitn/PmtInf/PmtMtd';
        l_PmtMtd_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PmtMtd'))); 
        dummy := DBMS_XMLDOM.appendChild(l_PmtMtd_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_MetodoPago)));

        -- 2.4 [0..1] ++ Numero de operaciones <NbOfTxs> 5 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/NbOfTxs
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo NbOfTxs (15 - Número de operaciones) - Localización /Document/CstmrCdtTrfInitn/PmtInf/NbOfTxs';
        l_NbOfTxs_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'NbOfTxs')));
        dummy := DBMS_XMLDOM.appendChild(l_NbOfTxs_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, '0')));

        -- 2.5 [0..1] ++ Control de suma <CtrlSum> 19 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/CtrlSum
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo CtrlSum (19 - Control de suma) - Localización /Document/CstmrCdtTrfInitn/PmtInf/CtrlSum';
        l_CtrlSum_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CtrlSum')));
        dummy := DBMS_XMLDOM.appendChild(l_CtrlSum_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, '0')));

        -- 2.17 [1..1] ++ Fecha de ejecución solicitada   <ReqdExctnDt> 10 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/ReqdExctnDt
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.ordenante', p_Idioma);
        If (v_Ordenante.Fechaejecucion Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.fechaEjecucion', p_Idioma);
            Raise e_error;
        End If;
        v_FechaPago := Nvl(to_char(v_Ordenante.Fechaejecucion,'YYYY-MM-DD'), '0'); --l_ReqdExctnDt_textnode - Fecha de cobro
        If (trim(v_FechaPago) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.fechaEjecucion', p_Idioma);
            Raise e_error;
        End If;
        l_ReqdExctnDt_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'ReqdExctnDt')));
        dummy := DBMS_XMLDOM.appendChild(l_ReqdExctnDt_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_FechaPago)));

        -- 2.19 [1..1] ++ Ordenante <Dbtr> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/Dbtr
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo Dbtr (DbtrAcct) - Localización /Document/CstmrCdtTrfInitn/PmtInf/Dbtr';
        l_Dbtr_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Dbtr')));

        -- 2.19 [0..1] +++ Nombre <Nm> 70 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Nm
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo Nm (Nm) - Localización /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Nm';
        v_NomOrdenante := F_RevisarCaracteresSEPA(Nvl(v_Ordenante.NOMBRE, ' ')); --l_Nm_textnode - Nombre
        l_Nm_node := DBMS_XMLDOM.appendChild(l_Dbtr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Nm')));
        dummy := DBMS_XMLDOM.appendChild(l_Nm_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_NomOrdenante)));

        -- 2.19 [0..1] +++ Direccion postal <PstlAdr> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/PstlAdr
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo PstlAdr (Dirección postal) - Localización /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/PstlAdr';
        l_PstlAdr_node := DBMS_XMLDOM.appendChild(l_Dbtr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PstlAdr')));

        -- 2.19 [0..1] ++++ Pais <Ctry> 2 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/PstlAdr/Ctry
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.ordenante', p_Idioma);
        If (v_Ordenante.DIRECCION.PAIS_ISO Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.direccion', p_Idioma);
            Raise e_error;
        End If;
        v_CodPais := F_RevisarCaracteresSEPA(NVL(v_Ordenante.DIRECCION.PAIS_ISO, ' ')); --l_Ctry_textnode - Pais
        If (trim(v_CodPais) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.direccion.caracteresNoValidos', p_Idioma);
            Raise e_error;
        End If;
        l_Ctry_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Ctry')));
        dummy := DBMS_XMLDOM.appendChild(l_Ctry_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_CodPais)));

        -- 2.19 [0..2] ++++ Direccion en texto libre <AdrLine> 70 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/PstlAdr/AdrLine
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.ordenante', p_Idioma);
        If (v_Ordenante.DIRECCION.DOMICILIO Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.direccion', p_Idioma);
            Raise e_error;
        End If;
        v_Direccion := F_RevisarCaracteresSEPA(NVL(SUBSTR(v_Direccion_Total,1,70), ' ')); --l_AdrLine_textnode - Direccion1
        If (trim(v_Direccion) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.direccion.caracteresNoValidos', p_Idioma);
            Raise e_error;
        End If;
        l_AdrLine_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'AdrLine')));
        dummy := DBMS_XMLDOM.appendChild(l_AdrLine_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Direccion)));
        
        v_DireccionResto := TRIM(F_RevisarCaracteresSEPA(SUBSTR(v_Direccion_Total,71,70))); --l_AdrLine_textnode - Direccion2
        IF v_DireccionResto IS NOT NULL THEN
            v_Datoserror := 'BloqueOrdenateXML: Creo Nodo AdrLine (70 - Dirección en texto libre) - Localización /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/PstlAdr/AdrLine[2]';
            l_AdrLine2_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'AdrLine')));
            dummy := DBMS_XMLDOM.appendChild(l_AdrLine2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_DireccionResto)));
        END IF;

        -- 2.19 [0..1] +++ Identificacion <Id> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Id
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo Id (Identificación) - Localización /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Id';
        l_Id_node := DBMS_XMLDOM.appendChild(l_Dbtr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));

        -- 2.19 [1..1] ++++ Persona Juridica <OrgId> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Id/OrgId
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo OrgId (Identificación privada) - Localización /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Id/OrgId';
        l_OrgId_node := DBMS_XMLDOM.appendChild(l_Id_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'OrgId')));

        -- 2.19 [1..1] +++++ Otra <Othr> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Id/OrgId/Othr
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo Othr (Otra) - Localización /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Id/OrgId/Othr';
        l_Othr_node := DBMS_XMLDOM.appendChild(l_OrgId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Othr')));

        -- 2.19 [1..1] ++++++ Identificacion <Id> 35 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/Dbtr/Id/OrgId/Othr/Id
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.ordenante', p_Idioma);
        If (v_Ordenante.Cif Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.cifColegio', p_Idioma);
            Raise e_error;
        End If;
        If (v_Ordenante.Sufijo Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.sufijo', p_Idioma);
            Raise e_error;
        End If;
        v_Identificacion := F_RevisarCaracteresSEPA(TRIM(v_Ordenante.Cif) || Lpad(v_Ordenante.Sufijo, 3, '0')); --l_Id_textnode - Identificacion
        If (trim(v_Identificacion) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.cifOsufijo.caracteresNoValidos', p_Idioma);
            Raise e_error;
        End If;
        l_Id2_node := DBMS_XMLDOM.appendChild(l_Othr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));
        dummy := DBMS_XMLDOM.appendChild(l_Id2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Identificacion)));

        -- 2.20 [1..1] ++ Cuenta del Ordenante <DbtrAcct> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo DbtrAcct (Cuenta del Ordenante) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct';
        l_DbtrAcct_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'DbtrAcct')));

        -- 2.20 [1..1] +++ Identificacion <Id> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct/Id
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo Id (Identificación) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct/Id';
        l_Id3_node := DBMS_XMLDOM.appendChild(l_DbtrAcct_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));

        -- 2.20 [1..1] ++++ IBAN <IBAN> 34 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DbtrAcct/Id/IBAN
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.ordenante', p_Idioma);
        If (v_Ordenante.BANCO_IBAN Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.cuentaColegio', p_Idioma);
            Raise e_error;
        End If;
        v_Iban := F_RevisarCaracteresSEPA(NVL(TRIM(v_Ordenante.BANCO_IBAN), ' ')); --l_IBAN_textnode - IBAN
        If (trim(v_Iban) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.cuentaColegio.caracteresNoValidos', p_Idioma);
            Raise e_error;
        End If;
        l_IBAN_node := DBMS_XMLDOM.appendChild(l_Id3_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'IBAN')));
        dummy := DBMS_XMLDOM.appendChild(l_IBAN_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Iban)));

        -- 2.21 [1..1] ++ Entidad del Ordenante <DbtrAgt> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo DbtrAgt (Entidad del Ordenante) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt';
        l_DbtrAgt_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'DbtrAgt')));

        -- 2.21 [1..1] +++ Identificacion de la entidad <FinInstnId> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId
        v_Datoserror := 'BloqueOrdenateXML: Creo Nodo FinInstnId (Identificación de la entidad) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId';
        l_FinInstnId_node := DBMS_XMLDOM.appendChild(l_DbtrAgt_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'FinInstnId')));

        -- 2.21 [0..1] ++++ BIC <BIC> 11 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DbtrAgt/FinInstnId/BIC
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.ordenante', p_Idioma);
        If (v_Ordenante.Banco_Bic Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.bicColegio', p_Idioma);
            Raise e_error;
        End If;
        v_Bic := F_RevisarCaracteresSEPA(NVL(TRIM(v_Ordenante.Banco_Bic), ' '));--F_RevisarCaracteresSEPA(NVL(TRIM(v_Ordenante.BANCO_BIC), ' ')); --l_BIC_textnode - BIC
        If (trim(v_Bic) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.bicColegio.caracteresNoValidos', p_Idioma);
            Raise e_error;
        End If;
        l_BIC_node := DBMS_XMLDOM.appendChild(l_FinInstnId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'BIC')));
        dummy := DBMS_XMLDOM.appendChild(l_BIC_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Bic)));

        v_Datoserror := 'BloqueOrdenateXML: Actualizacion de los parametros de error';
        p_Codretorno := To_Char(0);
        p_Datoserror := Null;

    EXCEPTION
        When e_error Then
            p_Codretorno := 1;
            p_Datoserror := v_Datoserror;
            Return;
        WHEN OTHERS THEN
            p_Codretorno := To_Char(Sqlcode);
            p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END BloqueOrdenateXML;

    /****************************************************************************************************************/
    /* Nombre: BloqueBeneficiarioXML*/
    /* Descripcion: Crea un bloque(Informacion de transferencia individual) del abono y beneficiario xml (SEPA) */
    /* l_sepa - IN OUT - Contenedor del documento xml a generar */
    /* l_PmtInf_node - IN - Nodo raiz apartir del cual siguen los bloques individuales Beneficiario */
    /* v_Ordenante - IN - - Registro con los datos del Ordenante - Reg_Ordenante */
    /* v_Abonosdisquete - IN - Registro con los datos del abono - Reg_Abono */
    /* p_Idinstitucion - IN -Id de la institucion */
    /* v_Contador - IN - contador abonos */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 29/06/2015 - Autor: Oscar de la Torre Noheda */
    /* Version: 2.0 - Fecha Modificacion:  - Autor:  - Cambios: */
    /****************************************************************************************************************/

   PROCEDURE BloqueBeneficiarioXML(
        l_sepa IN OUT DBMS_XMLDOM.DOMDocument,
        l_PmtInf_node IN DBMS_XMLDOM.DomNode,
        v_Abonosdisquete IN Reg_Abonos,
        v_Contador IN NUMBER,
        p_Idioma In Adm_Lenguajes.Idlenguaje%Type,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) Is
    
        l_CdtTrfTxInf_node DBMS_XMLDOM.DomNode;
        l_PmtId_node DBMS_XMLDOM.DomNode;
        l_EndToEndId_node DBMS_XMLDOM.DomNode;
        l_PmtTpInf_node DBMS_XMLDOM.DomNode;
        l_SvcLvl_node DBMS_XMLDOM.DomNode;
        l_Cd_node DBMS_XMLDOM.DomNode;
        l_Amt_node DBMS_XMLDOM.DomNode;        
        l_InstdAmt_node DBMS_XMLDOM.DomNode;
        l_InstdAmt_element DBMS_XMLDOM.DOMElement;
        l_CdtrAgt_node DBMS_XMLDOM.DomNode;
        l_FinInstnId_node DBMS_XMLDOM.DomNode;
        l_BIC_node DBMS_XMLDOM.DomNode;
        l_Cdtr_node DBMS_XMLDOM.DomNode;
        l_Nm_node DBMS_XMLDOM.DomNode;
        l_PstlAdr_node DBMS_XMLDOM.DomNode;
        l_Ctry_node DBMS_XMLDOM.DomNode;
        l_AdrLine_node DBMS_XMLDOM.DomNode;
        l_AdrLine2_node DBMS_XMLDOM.DomNode;
        l_Id_node DBMS_XMLDOM.DomNode;
        l_CdtrAcct_node DBMS_XMLDOM.DomNode;
        l_IBAN_node DBMS_XMLDOM.DomNode;
        l_RmtInf_node DBMS_XMLDOM.DomNode;
        l_Ustrd_node DBMS_XMLDOM.DomNode; 
        dummy DBMS_XMLDOM.DomNode;
          
        v_NombreColegiado Varchar2(400);
        v_IdenExtremo Varchar2(35); --l_EndToEndId_textnode - Identificacion de extremo a extremo
        v_CodMensaje Varchar2(4) := 'SEPA'; --l_Cd_textnode - Codigo
        v_TipoTransferencia Varchar2(4); --l_Cd_textnode - Codigo
        v_Importe Varchar2(11); --l_InstdAmt_textnode - Importe ordenado
        v_Bic Varchar2(11); --l_NbOfTxs_textnode - BIC
        v_NomBeneficiario Varchar2(70); --l_Nm_textnode - Nombre
        v_CodPais Varchar2(2); --l_Ctry_textnode - Pais
        v_Direccion_Total VARCHAR2(140); -- Uno los tres campos de la direccion
        v_Direccion Varchar2(70); --l_AdrLine_textnode - Direccion1
        v_DireccionResto Varchar2(70); --l_AdrLine_textnode - Direccion2
        v_Iban Varchar2(34); --l_IBAN_textnode - IBAN
        v_Concepto Varchar2(140); --l_Ustrd_textnode - Concepto
        v_conceptoFinal VARCHAR2(4000);
        v_Datoserror Varchar2(4000) := Null;               

    Begin
    
        v_Datoserror := 'BloqueBeneficiarioXML: Calculo el concepto';
        v_NombreColegiado := v_Abonosdisquete.m_Abonossepa(v_Contador).Coleg_Nombre || ' (col. ' || v_Abonosdisquete.m_Abonossepa(v_Contador).Coleg_Ncoleg || ')';
        v_conceptoFinal := Rpad(Formatea_Concepto(v_Abonosdisquete.m_Abonossepa(v_Contador).Observaciones,
                                             v_Abonosdisquete.m_Abonossepa(v_Contador).Motivos,
                                             v_Abonosdisquete.m_Abonossepa(v_Contador).Coleg_Nombre,
                                             v_Abonosdisquete.m_Abonossepa(v_Contador).Coleg_Ncoleg,
                                             140,
                                             v_Abonosdisquete.m_Abonossepa(v_Contador).Fcs,
                                             v_Abonosdisquete.m_Abonossepa(v_Contador).Incluirletradoconceptobanco),
                           140,
                           ' '); --concepto

        v_Datoserror := 'BloqueBeneficiarioXML: Transformo las variables de direccion de facturacion del Beneficiario';
        v_Direccion_Total := trim(NVL(v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.DOMICILIO, '')) || ' ' ||
                             trim(NVL(v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.CODIGOPOSTAL, ''))  || ' ' ||
                             trim(NVL(v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.POBLACION_NOMBRE, '')) || ' ' ||
                             trim(NVL(v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.PROVINCIA_NOMBRE, ''));

        -- 2.27 [1..n] ++ Informacion de transferencia individual <CdtTrfTxInf> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo CdtTrfTxInf (Información de la operación de adeudo directo) - Localización /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf'; 
        l_CdtTrfTxInf_node := DBMS_XMLDOM.appendChild(l_PmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CdtTrfTxInf')));

        -- 2.28 [1..1] +++ Identificacion del pago <PmtId> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtId
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo PmtId (Identificacion del pago) - Localización /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtId'; 
        l_PmtId_node := DBMS_XMLDOM.appendChild(l_CdtTrfTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PmtId')));

        -- 2.30 [1..1] ++++ Identificacion de extremo a extremo <EndToEndId> 35 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtId/EndToEndId
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo EndToEndId (Identificacion de extremo a extremo) - Localización /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtId/EndToEndId'; 
        v_IdenExtremo := F_RevisarCaracteresSEPA(NVL(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Referenciainterna), ' ')); --l_EndToEndId_textnode - Identificacion de extremo a extremo
        l_EndToEndId_node := DBMS_XMLDOM.appendChild(l_PmtId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'EndToEndId')));
        dummy := DBMS_XMLDOM.appendChild(l_EndToEndId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_IdenExtremo)));

        -- 2.31 [0..1] +++ Información del tipo de pago <PmtTpInf> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtTpInf
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo PmtTpInf (Informacion del tipo de pago) - Localización /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtTpInf';
        l_PmtTpInf_node := DBMS_XMLDOM.appendChild(l_CdtTrfTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PmtTpInf')));

        -- 2.33 [1..1] ++++ Nivel de servicio <SvcLvl> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtTpInf/SvcLvl
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo SvcLvl (Nivel de servicio) - Localización /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtTpInf/SvcLvl';
        l_SvcLvl_node := DBMS_XMLDOM.appendChild(l_PmtTpInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'SvcLvl')));
        
        -- 2.34 [1..1] +++++ Codigo <Cd> 35 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtTpInf/SvcLvl/Cd
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo Cd (Identificación del mandato) - Localización /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtTpInf/SvcLvl/Cd';
        l_Cd_node := DBMS_XMLDOM.appendChild(l_SvcLvl_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Cd')));
        dummy := DBMS_XMLDOM.appendChild(l_Cd_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_CodMensaje)));

        v_TipoTransferencia := v_Abonosdisquete.m_Abonossepa(v_Contador).Proposito; --tipo de transferencia
        If v_TipoTransferencia In ('SALA', 'PENS') Then
            -- 2.39 [0..1] ++++ Tipo de transferencia <CtgyPurp> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtTpInf/CtgyPurp
            v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo CtgyPurp (Nivel de servicio) - Localización /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtTpInf/CtgyPurp';
            l_SvcLvl_node := DBMS_XMLDOM.appendChild(l_PmtTpInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CtgyPurp')));
            
            -- 2.39 [1..1] +++++ Codigo <MndtId> 4 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtTpInf/CtgyPurp/Cd
            v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo Cd (Identificación del mandato) - Localización /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/PmtTpInf/CtgyPurp/Cd';
            l_Cd_node := DBMS_XMLDOM.appendChild(l_SvcLvl_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Cd')));
            dummy := DBMS_XMLDOM.appendChild(l_Cd_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_TipoTransferencia)));
            
            -- Otros valores no estan plenamente soportados por la norma: en otros casos no se pinta el nodo
        end if;
        -- 2.42 [1..1] +++ Importe <Amt> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Amt
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo Amt (Importe) - Localización /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Amt';
        l_Amt_node := DBMS_XMLDOM.appendChild(l_CdtTrfTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Amt')));

        -- 2.43 [1..1] ++++ Importe ordenado <InstdAmt> 11 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/Amt/InstdAmt
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.beneficiario', p_Idioma);
        If (v_Abonosdisquete.m_Abonossepa(v_Contador).Importe Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Importe := NVL(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Importe), 0); --l_InstdAmt_textnode - Importe ordenado
        If (trim(v_Importe) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe.caracteresNoValidos', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        If (ltrim(v_Importe, '0') = '0') Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        l_InstdAmt_element := DBMS_XMLDOM.createElement(l_sepa, 'InstdAmt');
        l_InstdAmt_node := DBMS_XMLDOM.appendChild(l_Amt_node, DBMS_XMLDOM.makeNode(l_InstdAmt_element));
        dummy := DBMS_XMLDOM.appendChild(l_InstdAmt_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, TO_CHAR(v_Importe, 'FM99999999.00'))));
        DBMS_XMLDOM.setAttribute(l_InstdAmt_element, 'Ccy', 'EUR');
                      
        -- 2.77 [0..1] +++ Entidad del Beneficiario <CdtrAgt> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/CdtrAgt
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo CdtrAgt (Entidad del Beneficiario) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/CdtrAgt';
        l_CdtrAgt_node := DBMS_XMLDOM.appendChild(l_CdtTrfTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CdtrAgt')));

        -- 2.77 [1..1] ++++ Identificacion de la entidad <FinInstnId> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/CdtrAgt/FinInstnId
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo FinInstnId (Identificación de la entidad) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/CdtrAgt/FinInstnId';
        l_FinInstnId_node := DBMS_XMLDOM.appendChild(l_CdtrAgt_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'FinInstnId')));

        -- 2.77 [0..1] +++++ BIC <BIC> 11 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/CdtrAgt/FinInstnId/BIC
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.beneficiario', p_Idioma);
        If (v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Bic Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Bic := F_RevisarCaracteresSEPA(NVL(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Bic), ' ')); --l_NbOfTxs_textnode - BIC
        If (trim(v_Bic) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic.caracteresNoValidos', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        l_BIC_node := DBMS_XMLDOM.appendChild(l_FinInstnId_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'BIC')));
        dummy := DBMS_XMLDOM.appendChild(l_BIC_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Bic)));

        -- 2.79 [0..1] +++ Beneficiario <Cdtr> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/Cdtr
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo Cdtr (Beneficiario) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/Cdtr';
        l_Cdtr_node := DBMS_XMLDOM.appendChild(l_CdtTrfTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Cdtr')));
 
        -- 2.79 [0..1] ++++ Nombre <Nm> 70 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/Cdtr/Nm
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo Nm (Nombre) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/Cdtr/Nm';
        v_NomBeneficiario := F_RevisarCaracteresSEPA(Nvl(SUBSTR(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Nombre),1,70), ' ')); --l_Nm_textnode - Nombre Beneficiario
        l_Nm_node := DBMS_XMLDOM.appendChild(l_Cdtr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Nm')));
        dummy := DBMS_XMLDOM.appendChild(l_Nm_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_NomBeneficiario)));

        -- 2.79 [0..1] ++++ Direccion postal <PstlAdr> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/Cdtr/PstlAdr
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo PstlAdr (Dirección postal) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/Cdtr/PstlAdr';
        l_PstlAdr_node := DBMS_XMLDOM.appendChild(l_Cdtr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'PstlAdr')));
        
        -- 2.79 [0..1] +++++ Pais <Ctry> 2 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/Cdtr/PstlAdr/Ctry
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.beneficiario', p_Idioma);
        If (v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.Pais_Iso Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_CodPais := F_RevisarCaracteresSEPA(NVL(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.Pais_Iso), ' ')); --l_Ctry_textnode - Pais
        If (trim(v_CodPais) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion.caracteresNoValidos', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        l_Ctry_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Ctry')));
        dummy := DBMS_XMLDOM.appendChild(l_Ctry_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_CodPais)));
        
        -- 2.79 [0..2] +++++ Direccion en texto libre <AdrLine> 70 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/Cdtr/PstlAdr/AdrLine
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.beneficiario', p_Idioma);
        If (v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.DOMICILIO Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Direccion := F_RevisarCaracteresSEPA(NVL(SUBSTR(v_Direccion_Total,1,70), ' ')); --l_AdrLine_textnode - Direccion1
        If (trim(v_Direccion) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.direccion.caracteresNoValidos', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        l_AdrLine_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'AdrLine')));
        dummy := DBMS_XMLDOM.appendChild(l_AdrLine_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Direccion)));

        v_DireccionResto := TRIM(F_RevisarCaracteresSEPA(SUBSTR(v_Direccion_Total,71,70))); --l_AdrLine_textnode - Direccion2
        IF v_DireccionResto IS NOT NULL THEN
            v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo AdrLine (70 - Dirección en texto libre) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/Cdtr/PstlAdr/AdrLine[2]';
            l_AdrLine2_node := DBMS_XMLDOM.appendChild(l_PstlAdr_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'AdrLine')));
            dummy := DBMS_XMLDOM.appendChild(l_AdrLine2_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_DireccionResto)));
        END IF;        

        -- 2.80 [0..1] +++ Cuenta del Beneficiario <CdtrAcct> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/CdtrAcct
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo CdtrAcct (Cuenta del Beneficiario) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/CdtrAcct';
        l_CdtrAcct_node := DBMS_XMLDOM.appendChild(l_CdtTrfTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'CdtrAcct')));

        -- [1..1] ++++ Identificacion <Id> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/CdtrAcct/Id
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo Id (Identificación) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/CdtrAcct/Id';
        l_Id_node := DBMS_XMLDOM.appendChild(l_CdtrAcct_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Id')));

        -- [0..1] +++++ IBAN <IBAN> 34 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/CdtrAcct/Id/IBAN
        v_Datoserror := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.xml.beneficiario', p_Idioma);
        If (v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Iban Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Iban := F_RevisarCaracteresSEPA(NVL(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Iban), ' ')); --l_IBAN_textnode - IBAN
        If (trim(v_Iban) Is Null) Then
            v_Datoserror := v_Datoserror || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta.caracteresNoValidos', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        l_IBAN_node := DBMS_XMLDOM.appendChild(l_Id_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'IBAN')));
        dummy := DBMS_XMLDOM.appendChild(l_IBAN_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Iban)));              

        -- 2.98 [0..1] +++ Concepto <RmtInf> - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/RmtInf
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo RmtInf (Concepto) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/RmtInf';
        l_RmtInf_node := DBMS_XMLDOM.appendChild(l_CdtTrfTxInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'RmtInf')));

        -- 2.99 [0..n]{Or ++++ No estructurado <Ustrd> 140 - Localizacion /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd
        v_Datoserror := 'BloqueBeneficiarioXML: Creo Nodo Ustrd (No estructurado) - Localización /Document/CstmrCdtTrfInitn/PmtInf/DrctDbtTxInf/RmtInf/Ustrd';
        v_Concepto := F_RevisarCaracteresSEPA(NVL(SUBSTR(TRIM(v_conceptoFinal),1,140), ' ')); --l_Ustrd_textnode - Concepto
        l_Ustrd_node := DBMS_XMLDOM.appendChild(l_RmtInf_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createElement(l_sepa, 'Ustrd')));
        dummy := DBMS_XMLDOM.appendChild(l_Ustrd_node, DBMS_XMLDOM.makeNode(DBMS_XMLDOM.createTextNode(l_sepa, v_Concepto)));

        v_DatosError := 'BloqueBeneficiarioXML: Actualizacion de los parametros de error';
        p_CodRetorno := To_Char(0);
        p_DatosError := Null;

    EXCEPTION
        When e_error Then
            p_Codretorno := 1;
            p_Datoserror := v_Datoserror;
            Return;
        WHEN OTHERS THEN
            p_Codretorno := To_Char(Sqlcode);
            p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END BloqueBeneficiarioXML;  

    /****************************************************************************************************************/
    /* Nombre: BloqueTotalesXML*/
    /* Descripcion: Se actualizan los totales y se genera el fichero de transferencias xml(SEPA) */
    /* p_Pathfichero - IN - Ruta donde escribir el ficher */
    /* v_Nombrefichero - IN - Nombre del fichero a escribir */
    /* l_sepa - IN -Contenedor del documento xml a generar */
    /* v_Conta_Abonos - IN - Total número de abonos */
    /* v_Subtotal_Abonos - IN - Importe total abonos */
    /* P_CODRETORNO - OUT - Devuelve 0 en caso de que la ejecucion haya sido OK - VARCHAR2(10) */
    /*      En caso de error devuelve el codigo de error Oracle correspondiente. */
    /* P_DATOSERROR - OUT - Devuelve null en caso de que la ejecucion haya sido OK - VARCHAR2(400) */
    /*      En caso de error devuelve el mensaje de error Oracle correspondiente. */
    /* */
    /* Version: 1.0 - Fecha Creacion: 29/06/2015 - Autor: Oscar de la Torre Noheda */
    /* Version: 2.0 - Fecha Modificacion:  - Autor:  - Cambios: */
    /****************************************************************************************************************/

   PROCEDURE BloqueTotalesXML(
        p_Pathfichero  IN  Varchar2,
        v_Nombrefichero IN  Varchar2,
        l_sepa IN DBMS_XMLDOM.DOMDocument,
        v_Conta_Abonos IN NUMBER,
        v_Subtotal_Abonos IN NUMBER,
        p_CodRetorno OUT VARCHAR2,
        p_DatosError OUT VARCHAR2
    ) Is
    -- Total numero facturas y total importes
    l_NbOfTxs_node DBMS_XMLDOM.DomNode;
    l_CtrlSum_node DBMS_XMLDOM.DomNode;
    l_NbOfTxs_child DBMS_XMLDOM.DomNode;
    l_CtrlSum_child DBMS_XMLDOM.DomNode;    

    v_Datoserror Varchar2(4000) := Null;

    begin
    
        v_Datoserror := 'BloqueTotalesXML: Insertar total en CABECERA de numero facturas y total importes  - sepa xml';                   
        l_NbOfTxs_node := DBMS_XMLDOM.item(DBMS_XMLDOM.getElementsByTagName(l_sepa, 'NbOfTxs'), 0);                    
        l_CtrlSum_node := DBMS_XMLDOM.item(DBMS_XMLDOM.getElementsByTagName(l_sepa, 'CtrlSum'), 0);

        l_NbOfTxs_child := DBMS_XMLDOM.getFirstChild(l_NbOfTxs_node);
        l_CtrlSum_child := DBMS_XMLDOM.getFirstChild(l_CtrlSum_node); 
                        
        DBMS_XMLDOM.setNodeValue(l_NbOfTxs_child, v_Conta_Abonos);
        DBMS_XMLDOM.setNodeValue(l_CtrlSum_child, TO_CHAR(v_Subtotal_Abonos, 'FM9999999999999999.00'));


        v_Datoserror := 'BloqueTotalesXML: Insertar total en TOTAL de numero facturas y total importes  - sepa xml';                   
        l_NbOfTxs_node := DBMS_XMLDOM.item(DBMS_XMLDOM.getElementsByTagName(l_sepa, 'NbOfTxs'), 1);                    
        l_CtrlSum_node := DBMS_XMLDOM.item(DBMS_XMLDOM.getElementsByTagName(l_sepa, 'CtrlSum'), 1);

        l_NbOfTxs_child := DBMS_XMLDOM.getFirstChild(l_NbOfTxs_node);
        l_CtrlSum_child := DBMS_XMLDOM.getFirstChild(l_CtrlSum_node); 
                        
        DBMS_XMLDOM.setNodeValue(l_NbOfTxs_child, v_Conta_Abonos);
        DBMS_XMLDOM.setNodeValue(l_CtrlSum_child, TO_CHAR(v_Subtotal_Abonos, 'FM9999999999999999.00'));         

        v_Datoserror := 'BloqueTotalesXML: Grabacion documento l_sepa en el fichero xml';
        DBMS_XMLDOM.setversion(l_sepa, '1.0" encoding = "UTF-8');
        DBMS_XMLDOM.setCharset(l_sepa, 'UTF-8'); -- cambia los caracteres especiales
        
        v_Datoserror := 'BloqueTotalesXML: actualizando los parametros de salida';
        DBMS_XMLDOM.writeToFile(l_sepa, p_PathFichero||'/'||v_Nombrefichero);
    
    
        v_DatosError := 'BloqueTotalesXML: Actualizacion de los parametros de error';
        p_CodRetorno := To_Char(0);
        p_DatosError := Null;

    EXCEPTION
        WHEN OTHERS THEN
            p_Codretorno := To_Char(Sqlcode);
            p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
    END BloqueTotalesXML;

  ----
  -- Nombre:      Escribir_Txt
  -- Descripcion: Escribe el fichero SEPA en formato Texto con los datos pasados como parametros
  --
  -- Parametros:
  -- * p_Pathfichero    - IN - Varchar2      - Ruta donde escribir el fichero
  -- * p_Nombrefichero  - IN - Varchar2      - Nombre del fichero a escribir
  -- * v_Ordenante      - IN - Reg_Ordenante - Datos completos del ordenante y otros generales
  -- * v_Abonosdisquete - IN - Reg_Abonos    - Lista de todos los abonos a escribir en el fichero
  --
  -- Versiones:
  -- Fecha       Autor                 Descripcion
  -- 21/05/2014  Adrian Ayala Gomez    Creacion.
  ----
  Procedure Escribir_Txt(p_Pathfichero    Varchar2,
                         p_Nombrefichero  Varchar2,
                         v_Ordenante      Reg_Ordenante,
                         v_Abonosdisquete Reg_Abonos,
                         p_Idioma         Adm_Lenguajes.Idlenguaje%Type,
                         p_CodRetorno OUT VARCHAR2,
                         p_DatosError OUT VARCHAR2) Is
    f_Salida          Utl_File.File_Type; -- Fichero de salida
    v_Registro        Varchar2(4000);
    v_Proposito       Varchar2(1000);
    
    v_Versioncuaderno Varchar2(10);
    v_NombreColegiado Varchar2(300);
    v_Iban            Varchar2(34);
    v_Importe         Varchar2(11);
    v_Bic             Varchar2(11);
  
    v_Conta_Abonos_Sepa    Number := 0;
    v_Subtotal_Abonos_Sepa Number := 0;
    v_Conta_Abonos_Otro    Number := 0;
    v_Subtotal_Abonos_Otro Number := 0;
  Begin
    p_DatosError := F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.txt', p_Idioma);
    Select Valor
      Into v_Versioncuaderno
      From Gen_Properties
     Where Parametro = 'facturacion.cuaderno.transferencias.identificador';
    If (trim(v_Versioncuaderno) Is Null) Then
        p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.versionCuaderno', p_Idioma);
        Raise e_error;
    End If;
    v_Versioncuaderno := v_Versioncuaderno || Mod(To_Number(v_Versioncuaderno), 7);
  
    f_Salida := Utl_File.Fopen(p_Pathfichero, p_Nombrefichero, 'W');
  
    -- 1de8. generando cabecera de ordenante
    v_Registro := '';
    v_Registro := v_Registro || '01ORD';
    v_Registro := v_Registro || v_Versioncuaderno; --version del cuaderno
    v_Registro := v_Registro || '001'; --numero de dato
    
    If (trim(v_Ordenante.Cif) Is Null) Then
        p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.cifColegio', p_Idioma);
        Raise e_error;
    End If;
    v_Registro := v_Registro || Rpad(v_Ordenante.Cif, 9, ' '); --identificacion del ordenante
    
    If (trim(v_Ordenante.Sufijo) Is Null) Then
        p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.sufijo', p_Idioma);
        Raise e_error;
    End If;
    v_Registro := v_Registro || Lpad(v_Ordenante.Sufijo, 3, '0'); --sufijo
    
    If (v_Ordenante.Fechacreacion Is Null) Then
        p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.fechaCreacion', p_Idioma);
        Raise e_error;
    End If;
    v_Registro := v_Registro || To_Char(v_Ordenante.Fechacreacion, 'yyyymmdd'); --fecha de creacion del fichero
    If (v_Ordenante.Fechaejecucion Is Null) Then
        p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.fechaEjecucion', p_Idioma);
        Raise e_error;
    End If;
    v_Registro := v_Registro || To_Char(v_Ordenante.Fechaejecucion, 'yyyymmdd'); --fecha de ejecucion de ordenes (configurable en el futuro??)
    
    v_Registro := v_Registro || 'A'; --identificador de la cuenta del ordenante
    If (v_Ordenante.BANCO_IBAN Is Null) Then
        p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.cuentaColegio', p_Idioma);
        Raise e_error;
    End If;
    v_Registro := v_Registro || Rpad(v_Ordenante.Banco_Iban, 34, ' '); --cuenta del ordenante
    
    v_Registro := v_Registro || '0'; --detalle del cargo (un solo cargo por el total de operaciones)
    v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Ordenante.Nombre), 70, ' '); --nombre
    
    v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Ordenante.Direccion.Domicilio), 50, ' '); --direccion
    v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Ordenante.Direccion.Codigopostal || '  ' ||
                                                             v_Ordenante.Direccion.Poblacion_Nombre),
                                     50,
                                     ' '); --codigo postal y poblacion
    v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Ordenante.Direccion.Provincia_Nombre), 40, ' '); --provincia
    v_Registro := v_Registro || 'ES'; --pais
    v_Registro := v_Registro || Rpad(' ', 311, ' '); --libre
    Utl_File.Putf(f_Salida,
                  Substr(Rpad(v_Registro, Pkg_Siga_Constantes.c_Longitudregistrosepa, ' '),
                         1,
                         Pkg_Siga_Constantes.c_Longitudregistrosepa) || Chr(13) || Chr(10));
    Utl_File.Fflush(f_Salida);
  
    If v_Abonosdisquete.Conta_Abonossepa > 0 Then
      -- 2de8. generando cabecera de beneficiarios iban
      v_Registro := '';
      v_Registro := v_Registro || '02SCT'; --codigo de registro y operacion
      v_Registro := v_Registro || v_Versioncuaderno; --version del cuaderno
      v_Registro := v_Registro || Rpad(v_Ordenante.Cif, 9, ' '); --identificacion del ordenante
      v_Registro := v_Registro || Rpad(v_Ordenante.Sufijo, 3, ' '); --sufijo
      v_Registro := v_Registro || Rpad(' ', 578, ' '); --libre
      Utl_File.Putf(f_Salida,
                    Substr(Rpad(v_Registro, Pkg_Siga_Constantes.c_Longitudregistrosepa, ' '),
                           1,
                           Pkg_Siga_Constantes.c_Longitudregistrosepa) || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);
    
      -- 3de8. generando transferencias de beneficiarios iban
      For v_Contador In 1 .. v_Abonosdisquete.Conta_Abonossepa Loop
        v_NombreColegiado := v_Abonosdisquete.m_Abonossepa(v_Contador).Coleg_Nombre || ' (col. ' || v_Abonosdisquete.m_Abonossepa(v_Contador).Coleg_Ncoleg || ')';
        
        v_Registro := '';
        v_Registro := v_Registro || '03SCT'; --codigo de registro y operacion
        v_Registro := v_Registro || v_Versioncuaderno; --version del cuaderno
        v_Registro := v_Registro || '002'; --numero de dato
        v_Registro := v_Registro || Rpad(v_Abonosdisquete.m_Abonossepa(v_Contador).Referenciainterna, 35, ' '); --referencia del ordenante (identificador de la transferencia interno)
        v_Registro := v_Registro || 'A'; --identificador de la cuenta del beneficiario: A - iban

        If (v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Iban Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Iban := F_RevisarCaracteresSEPA(NVL(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Iban), ' ')); --l_IBAN_textnode - IBAN
        If (trim(v_Iban) Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta.caracteresNoValidos', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Registro := v_Registro || Rpad(v_Iban, 34, ' '); --cuenta del beneficiario

        If (v_Abonosdisquete.m_Abonossepa(v_Contador).Importe Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Importe := NVL(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Importe), 0); --l_InstdAmt_textnode - Importe ordenado
        If (trim(v_Importe) Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe.caracteresNoValidos', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        If (ltrim(v_Importe, '0') = '0') Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Registro := v_Registro || Lpad(v_Importe * 100, 11, '0');
        v_Registro := v_Registro || '3'; --clave de gastos: 3 - compartidos (da igual porque luego solo importa lo q aplique el destinatario)

        If (v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Bic Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Bic := F_RevisarCaracteresSEPA(NVL(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Bic), ' ')); --l_NbOfTxs_textnode - BIC
        If (trim(v_Bic) Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic.caracteresNoValidos', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Registro := v_Registro || Rpad(v_Bic, 11, ' '); --bic entidad del beneficiario
        v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Nombre),
                                         70,
                                         ' '); --nombre del beneficiario
        v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.Domicilio),
                                         50,
                                         ' '); --direccion del beneficiario
        v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.Codigopostal || '  ' ||
                                                                 v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.Poblacion_Nombre),
                                         50,
                                         ' '); --codigo postal y poblacion del beneficiario
        v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.Provincia_Nombre),
                                         40,
                                         ' '); --provincia del beneficiario
        v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Abonosdisquete.m_Abonossepa(v_Contador).Benef_Direccion.Pais_Iso),
                                         2,
                                         ' '); --pais del beneficiario
      
        v_Registro := v_Registro ||
                      Rpad(Formatea_Concepto(v_Abonosdisquete.m_Abonossepa(v_Contador).Observaciones,
                                             v_Abonosdisquete.m_Abonossepa(v_Contador).Motivos,
                                             v_Abonosdisquete.m_Abonossepa(v_Contador).Coleg_Nombre,
                                             v_Abonosdisquete.m_Abonossepa(v_Contador).Coleg_Ncoleg,
                                             140,
                                             v_Abonosdisquete.m_Abonossepa(v_Contador).Fcs,
                                             v_Abonosdisquete.m_Abonossepa(v_Contador).Incluirletradoconceptobanco),
                           140,
                           ' '); --concepto
      
        v_Registro := v_Registro || Rpad(' ', 35, ' '); --identificacion de la instruccion
      
        v_Proposito := v_Abonosdisquete.m_Abonossepa(v_Contador).Proposito;
        If v_Proposito In ('SALA', 'PENS') Then
          v_Registro := v_Registro || Rpad(v_Proposito, 4, ' '); --tipo de transferencia
        Else
          v_Registro := v_Registro || '    '; --tipo de transferencia
        End If;
        v_Registro := v_Registro || Rpad(v_Proposito, 4, ' '); --proposito de transferencia (no es obligatorio puede estar vacío)
      
        v_Registro := v_Registro || Rpad(' ', 99, ' '); --libre
        Utl_File.Putf(f_Salida,
                      Substr(Rpad(v_Registro, Pkg_Siga_Constantes.c_Longitudregistrosepa, ' '),
                             1,
                             Pkg_Siga_Constantes.c_Longitudregistrosepa) || Chr(13) || Chr(10));
        Utl_File.Fflush(f_Salida);
      
        -- contando y sumando abonos
        v_Conta_Abonos_Sepa    := v_Conta_Abonos_Sepa + 1;
        v_Subtotal_Abonos_Sepa := v_Subtotal_Abonos_Sepa +
                                  v_Abonosdisquete.m_Abonossepa(v_Contador).Importe;
      End Loop;
    
      -- 4de8. generando subtotal de beneficiarios iban
      v_Registro := '';
      v_Registro := v_Registro || '04SCT'; --codigo de registro y operacion
      v_Registro := v_Registro || Lpad(v_Subtotal_Abonos_Sepa * 100, 17, '0');
      v_Registro := v_Registro || Lpad(v_Conta_Abonos_Sepa, 8, '0'); --registros individuales
      v_Registro := v_Registro || Lpad(v_Conta_Abonos_Sepa + 2, 10, '0'); --registros individuales + cabecera + total
      v_Registro := v_Registro || Rpad(' ', 560, ' '); --libre
      Utl_File.Putf(f_Salida,
                    Substr(Rpad(v_Registro, Pkg_Siga_Constantes.c_Longitudregistrosepa, ' '),
                           1,
                           Pkg_Siga_Constantes.c_Longitudregistrosepa) || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);
    End If; -- registros de SEPA
  
    If v_Abonosdisquete.Conta_Abonosotro > 0 Then
      -- 5de8. generando cabecera de otros beneficiarios
      v_Registro := '';
      v_Registro := v_Registro || '02OTR'; --codigo de registro y operacion
      v_Registro := v_Registro || v_Versioncuaderno; --version del cuaderno
      v_Registro := v_Registro || Rpad(v_Ordenante.Cif, 9, ' '); --identificacion del ordenante
      v_Registro := v_Registro || Rpad(v_Ordenante.Sufijo, 3, ' '); --sufijo
      v_Registro := v_Registro || Rpad(' ', 578, ' '); --libre
      Utl_File.Putf(f_Salida,
                    Substr(Rpad(v_Registro, Pkg_Siga_Constantes.c_Longitudregistrosepa, ' '),
                           1,
                           Pkg_Siga_Constantes.c_Longitudregistrosepa) || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);
    
      -- 6de8. generando transferencias de beneficiarios iban
      For v_Contador In 1 .. v_Abonosdisquete.Conta_Abonosotro Loop
        v_Registro := '';
        v_Registro := v_Registro || '03OTR'; --codigo de registro y operacion
        v_Registro := v_Registro || v_Versioncuaderno; --version del cuaderno
        v_Registro := v_Registro || '006'; --numero de dato
        v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Ordenante.Nombre), 35, ' '); --nombre del ultimo ordenante
        v_Registro := v_Registro || 'A'; --identificador de la cuenta del beneficiario: A - iban

        If (v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Iban Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Iban := F_RevisarCaracteresSEPA(NVL(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Iban), ' ')); --l_IBAN_textnode - IBAN
        If (trim(v_Iban) Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.cuenta.caracteresNoValidos', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Registro := v_Registro || Rpad(v_Iban, 34, ' '); --cuenta del beneficiario

        If (v_Abonosdisquete.m_Abonossepa(v_Contador).Importe Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Importe := NVL(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Importe), 0); --l_InstdAmt_textnode - Importe ordenado
        If (trim(v_Importe) Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe.caracteresNoValidos', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        If (ltrim(v_Importe, '0') = '0') Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.importe', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Registro := v_Registro || Lpad(v_Importe * 100, 11, '0');
        v_Registro := v_Registro || '3'; --clave de gastos: 3 - compartidos (da igual porque luego solo importa lo q aplique el destinatario)

        If (v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Bic Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Bic := F_RevisarCaracteresSEPA(NVL(TRIM(v_Abonosdisquete.m_Abonossepa(v_Contador).Banco_Bic), ' ')); --l_NbOfTxs_textnode - BIC
        If (trim(v_Bic) Is Null) Then
            p_DatosError := p_DatosError || F_SIGA_GETRECURSO_ETIQUETA('facturacion.ficheroBancarioAbonos.error.genera.beneficiario.bic.caracteresNoValidos', p_Idioma) || ' `' || v_NombreColegiado || '`';
            Raise e_error;
        End If;
        v_Registro := v_Registro || Rpad(v_Bic,
                                         11,
                                         ' '); --bic entidad del beneficiario
        v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Abonosdisquete.m_Abonosotro(v_Contador).Benef_Nombre),
                                         35,
                                         ' '); --nombre del beneficiario
        v_Registro := v_Registro || Rpad(f_Revisarcaracteressepa(v_Abonosdisquete.m_Abonosotro(v_Contador).Benef_Direccion.Domicilio || ' ' ||
                                                                 v_Abonosdisquete.m_Abonosotro(v_Contador).Benef_Direccion.Codigopostal || '  ' ||
                                                                 v_Abonosdisquete.m_Abonosotro(v_Contador).Benef_Direccion.Poblacion_Nombre || ' ' ||
                                                                 v_Abonosdisquete.m_Abonosotro(v_Contador).Benef_Direccion.Provincia_Nombre || ' ' ||
                                                                 v_Abonosdisquete.m_Abonosotro(v_Contador).Benef_Direccion.Pais_Nombre),
                                         105,
                                         ' '); --direccion del beneficiario
      
        v_Registro := v_Registro ||
                      Rpad(Formatea_Concepto(v_Abonosdisquete.m_Abonosotro(v_Contador).Observaciones,
                                             v_Abonosdisquete.m_Abonosotro(v_Contador).Motivos,
                                             v_Abonosdisquete.m_Abonosotro(v_Contador).Coleg_Nombre,
                                             v_Abonosdisquete.m_Abonosotro(v_Contador).Coleg_Ncoleg,
                                             72,
                                             v_Abonosdisquete.m_Abonosotro(v_Contador).Fcs,
                                             v_Abonosdisquete.m_Abonosotro(v_Contador).Incluirletradoconceptobanco),
                           72,
                           ' '); --concepto
      
        v_Registro := v_Registro || Rpad(v_Abonosdisquete.m_Abonosotro(v_Contador).Numeroabono, 13, ' '); --referencia de la transferencia para el beneficiario
        v_Registro := v_Registro || Rpad(v_Abonosdisquete.m_Abonosotro(v_Contador).Proposito, 1, ' '); --proposito de transferencia (no es obligatorio puede estar vacío)
      
        v_Registro := v_Registro || Rpad(' ', 268, ' '); --libre
        Utl_File.Putf(f_Salida,
                      Substr(Rpad(v_Registro, Pkg_Siga_Constantes.c_Longitudregistrosepa, ' '),
                             1,
                             Pkg_Siga_Constantes.c_Longitudregistrosepa) || Chr(13) || Chr(10));
        Utl_File.Fflush(f_Salida);
      
        -- contando y sumando abonos
        v_Conta_Abonos_Otro    := v_Conta_Abonos_Otro + 1;
        v_Subtotal_Abonos_Otro := v_Subtotal_Abonos_Otro +
                                  v_Abonosdisquete.m_Abonosotro(v_Contador).Importe;
      End Loop;
    
      -- 7de8. generando subtotal de otros beneficiarios
      v_Registro := '';
      v_Registro := v_Registro || '04OTR'; --codigo de registro y operacion
      v_Registro := v_Registro || Lpad(v_Subtotal_Abonos_Otro * 100, 17, '0');
      v_Registro := v_Registro || Lpad(v_Conta_Abonos_Otro, 8, '0'); --registros individuales
      v_Registro := v_Registro || Lpad(v_Conta_Abonos_Otro + 2, 10, '0'); --registros individuales + cabecera + total
      v_Registro := v_Registro || Rpad(' ', 560, ' '); --libre
      Utl_File.Putf(f_Salida,
                    Substr(Rpad(v_Registro, Pkg_Siga_Constantes.c_Longitudregistrosepa, ' '),
                           1,
                           Pkg_Siga_Constantes.c_Longitudregistrosepa) || Chr(13) || Chr(10));
      Utl_File.Fflush(f_Salida);
    End If; -- registros de Otros
  
    -- 8de8. generando total
    v_Registro := '';
    v_Registro := v_Registro || '99ORD'; --codigo de registro y operacion
    v_Registro := v_Registro ||
                  Lpad((v_Subtotal_Abonos_Sepa + v_Subtotal_Abonos_Otro) * 100, 17, '0');
    v_Registro := v_Registro || Lpad(v_Conta_Abonos_Sepa + v_Conta_Abonos_Otro, 8, '0'); --registros individuales
    If v_Conta_Abonos_Sepa > 0 And v_Conta_Abonos_Otro > 0 Then
      v_Registro := v_Registro ||
                    Lpad(v_Conta_Abonos_Sepa + 2 + v_Conta_Abonos_Otro + 2 + 2, 10, '0'); --registros individuales + 2*subcabecera + 2*subtotal (si existen ambos bloques) + cabecera + total
    Else
      v_Registro := v_Registro || Lpad(v_Conta_Abonos_Sepa + v_Conta_Abonos_Otro + 2 + 2, 10, '0'); --registros individuales + subcabecera + subtotal (si solo existe un bloque) + cabecera + total
    End If;
    v_Registro := v_Registro || Rpad(' ', 560, ' '); --libre
    Utl_File.Putf(f_Salida,
                  Substr(Rpad(v_Registro, Pkg_Siga_Constantes.c_Longitudregistrosepa, ' '),
                         1,
                         Pkg_Siga_Constantes.c_Longitudregistrosepa) || Chr(13) || Chr(10));
  
    Utl_File.Fflush(f_Salida);
    Utl_File.Fclose(f_Salida);
  
    -- Terminando correctamente
    p_Codretorno := 0;
    p_DatosError := '';
  Exception
    When e_error Then
      p_Codretorno := 1;
    WHEN OTHERS THEN
      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := p_Datoserror || ', ' || Sqlerrm;
  End Escribir_Txt;

  ----
  -- Nombre:      Escribir_Xml
  -- Descripcion: Escribe el fichero SEPA en formato XML con los datos pasados como parametros
  --
  -- Parametros:
  -- * p_Pathfichero    - IN - Varchar2      - Ruta donde escribir el fichero
  -- * p_Nombrefichero  - IN - Varchar2      - Nombre del fichero a escribir
  -- * v_Ordenante      - IN - Reg_Ordenante - Datos completos del ordenante y otros generales
  -- * v_Abonosdisquete - IN - Reg_Abonos    - Lista de todos los abonos a escribir en el fichero
  --
  -- Versiones:
  -- Fecha       Autor                 Descripcion
  -- 
  ----
  Procedure Escribir_Xml(p_Pathfichero    Varchar2,
                         p_Nombrefichero  Varchar2,
                         v_Ordenante      Reg_Ordenante,
                         v_Abonosdisquete Reg_Abonos,
                         p_Idioma         Adm_Lenguajes.Idlenguaje%Type,
                         p_CodRetorno     OUT VARCHAR2,
                         p_DatosError     OUT VARCHAR2) Is
  
    v_Conta_Abonos_Sepa    Number := 0;
    v_Subtotal_Abonos_Sepa Number := 0;
    v_Conta_Abonos_Otro    Number := 0;
    v_Subtotal_Abonos_Otro Number := 0;
    
    l_sepa DBMS_XMLDOM.DOMDocument;
    l_CstmrCdtTrfInitn_node DBMS_XMLDOM.DomNode;       
    l_PmtInf_node DBMS_XMLDOM.DomNode;
           
    v_Nombrefichero       Varchar2(1000);   
    v_Codretorno VARCHAR2(10) := To_Char(0);
    v_Datoserror VARCHAR2(4000) := Null;
    
  Begin
  
    v_Datoserror := 'GestionarFicheros: Calculo el nombre del fichero xml';
    v_Nombrefichero := p_Nombrefichero || 'SEPA.xml';                     

    v_Datoserror := 'GestionarFicheros: Llamada al procedimiento CabOrdenanteXML';
    CabOrdenanteXML(
        l_sepa, -- Contenedor documento xml
        l_CstmrCdtTrfInitn_node, -- Raiz del mensaje adeudos
        v_Ordenante,
        p_Idioma,
        v_Codretorno,
        v_Datoserror);         
    IF v_Codretorno <> To_Char(0) THEN
        RAISE e_Error;
    END IF;    
    
    v_Datoserror := 'TratarFacturasOrdenante: Llamada al procedimiento BloqueOrdenateXML';
    BloqueOrdenateXML(
        l_sepa,
        l_CstmrCdtTrfInitn_node,
        l_PmtInf_node,
        v_Ordenante,
        p_Idioma,
        v_Codretorno,
        v_Datoserror);
    IF v_Codretorno <> To_Char(0) THEN
        RAISE e_Error;
    END IF;                               
                      
    For v_Contador In 1 .. v_Abonosdisquete.Conta_Abonossepa Loop

        v_Datoserror := 'TratarFacturasOrdenante: Llamada al procedimiento BloqueBeneficiarioXML';
        BloqueBeneficiarioXML(
            l_sepa,
            l_PmtInf_node,
            v_Abonosdisquete,
            v_Contador,
            p_Idioma,
            v_Codretorno,
            v_Datoserror);
        IF v_Codretorno <> To_Char(0) THEN
            RAISE e_Error;
        END IF;

        -- contando y sumando abonos
        v_Conta_Abonos_Sepa    := v_Conta_Abonos_Sepa + 1;
        v_Subtotal_Abonos_Sepa := v_Subtotal_Abonos_Sepa +
                                  v_Abonosdisquete.m_Abonossepa(v_Contador).Importe; 
                                        
    End Loop;                
                    
    v_Datoserror := 'GestionarFicheros: Llamada al procedimiento BloqueTotalesXML';
    BloqueTotalesXML(
        p_PathFichero,
        v_Nombrefichero,
        l_sepa, -- Contenedor documento xml
        v_Conta_Abonos_Sepa, 
        v_Subtotal_Abonos_Sepa,
        v_Codretorno,
        v_Datoserror);          
    
    ---------------------XML Transferencias Otras-----------------------------     
    If v_Abonosdisquete.Conta_Abonosotro > 0 Then

        v_Datoserror := 'GestionarFicheros: Calculo el nombre del fichero xml';
        v_Nombrefichero := p_Nombrefichero || 'OtrasSEPA.xml';   

        v_Datoserror := 'GestionarFicheros: Llamada al procedimiento CabOrdenanteXML';
        CabOrdenanteXML(
            l_sepa, -- Contenedor documento xml
            l_CstmrCdtTrfInitn_node, -- Raiz del mensaje adeudos
            v_Ordenante,
            p_Idioma,
            v_Codretorno,
            v_Datoserror);         
        IF v_Codretorno <> To_Char(0) THEN
            RAISE e_Error;
        END IF;    
        
        v_Datoserror := 'TratarFacturasOrdenante: Llamada al procedimiento BloqueOrdenateXML';
        BloqueOrdenateXML(
            l_sepa,
            l_CstmrCdtTrfInitn_node,
            l_PmtInf_node,
            v_Ordenante,
            p_Idioma,
            v_Codretorno,
            v_Datoserror);
        IF v_Codretorno <> To_Char(0) THEN
            RAISE e_Error;
        END IF;   
   
        For v_Contador In 1 .. v_Abonosdisquete.Conta_Abonosotro Loop

            v_Datoserror := 'TratarFacturasOrdenante: Llamada al procedimiento BloqueBeneficiarioXML';
            BloqueBeneficiarioXML(
                l_sepa,
                l_PmtInf_node,
                v_Abonosdisquete,
                v_Contador,
                p_Idioma,
                v_Codretorno,
                v_Datoserror);
                
        IF v_Codretorno <> To_Char(0) THEN
            RAISE e_Error;
        END IF;

            -- contando y sumando abonos
            v_Conta_Abonos_Otro    := v_Conta_Abonos_Otro + 1;
            v_Subtotal_Abonos_Otro := v_Subtotal_Abonos_Otro +
                                      v_Abonosdisquete.m_Abonosotro(v_Contador).Importe;   
        End Loop;                

        v_Datoserror := 'GestionarFicheros: Llamada al procedimiento BloqueTotalesXML';
        BloqueTotalesXML(
            p_PathFichero,
            v_Nombrefichero,
            l_sepa, -- Contenedor documento xml
            v_Conta_Abonos_Otro, 
            v_Subtotal_Abonos_Otro,
            v_Codretorno,
            v_Datoserror);         
        IF v_Codretorno <> To_Char(0) THEN
            RAISE e_Error;
        END IF;     

    End if;   

    v_Datoserror := 'GestionarFicheros: Actualizacion de los parametros de salida';
    p_Codretorno := To_Char(0);
    p_Datoserror := Null;
    
  Exception
    When e_Error Then
      Rollback;
      p_Codretorno := v_Codretorno;
      p_Datoserror := v_Datoserror;
   
    When Others Then
      Rollback;
      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
                  
  
  End Escribir_Xml;

  ----
  -- Nombre:      Generarficherotransferencias
  -- Descripcion: Entrada del paquete que solicita generar el/los ficheros SEPA
  --
  -- Parametros:
  -- * P_IDINSTITUCION   - IN  - NUMBER   - Id de la institucion
  -- * P_IDDISQUETEABONO - IN  - NUMBER   - Id del fichero que se va a generar
  -- * P_IDPROPOSITOSEPA - IN  - NUMBER   - Id del proposito SEPA indicado por el usuario
  -- * P_IDPROPOSITOTROS - IN  - NUMBER   - Id del proposito OTROS indicado por el usuario
  -- * p_Pathfichero     - IN  - Varchar2 - Ruta donde escribir el fichero
  -- * p_Nombrefichero   - IN  - Varchar2 - Nombre del fichero a escribir
  -- * p_Idioma          - IN  - NUMBER   - Idioma del usuario
  -- * p_Codretorno      - OUT - Varchar2 - Codigo de devolucion: 0 si fue correcto
  -- * p_Datoserror      - OUT - Varchar2 - Descripcion de devolucion: '' si fue correcto
  --
  -- Versiones:
  -- Fecha       Autor                 Descripcion
  -- 21/05/2014  Adrian Ayala Gomez    Creacion.
  ----
  Procedure Generarficherotransferencias(p_Idinstitucion   In Fac_Disqueteabonos.Idinstitucion%Type,
                                         p_Iddisqueteabono In Fac_Disqueteabonos.Iddisqueteabono%Type,
                                         p_Idpropositosepa In Fac_Propositos.Idproposito%Type,
                                         p_Idpropositotros In Fac_Propositos.Idproposito%Type,
                                         p_Pathfichero     In Varchar2,
                                         p_Nombrefichero   In Varchar2,
                                         p_Idioma          In Adm_Lenguajes.Idlenguaje%Type,
                                         p_Codretorno      Out Varchar2,
                                         p_Datoserror      Out Varchar2) Is
  
    v_TipoFicheroGenerar Varchar2(1);
    v_Regordenante       Reg_Ordenante;
    v_Abonos             Reg_Abonos;
  
    v_Codretorno Varchar2(10) := To_Char(0);
    v_Datoserror Varchar2(4000) := Null;
  
  Begin
    v_Datoserror   := 'GenerarFicheroTransferencias: ObtenerDatosOrdenante';
    v_Regordenante := Obtenerdatosordenante(p_Idinstitucion,
                                            p_Iddisqueteabono);
    If v_Regordenante.Idinstitucion Is Null Then
      v_Codretorno := To_Char(-1);
      Raise e_Error;
    End If;
  
    v_Datoserror             := 'GenerarFicheroTransferencias: ObtenerDireccionFacturacion';
    v_Regordenante.Direccion := Obtenerdireccionfacturacion(p_Idinstitucion,
                                                            v_Regordenante.Idpersona);
  
    v_Datoserror := 'GenerarFicheroTransferencias: Obtenerabonosdisquete';
    v_Abonos     := Obtenerabonosdisquete(p_Idinstitucion,
                                          p_Iddisqueteabono,
                                          p_Idpropositosepa,
                                          p_Idpropositotros);
  
    v_Datoserror         := 'GenerarFicheroTransferencias: consultando que tipo de fichero se quiere obtener (configuracion por parametro)';
    v_TipoFicheroGenerar := f_Siga_Getparametro('FAC', 'SEPA_TIPO_FICHEROS', p_Idinstitucion);
    
    v_Datoserror         := 'GenerarFicheroTransferencias: escribiendo ficheros';
    CASE v_TipoFicheroGenerar
       WHEN c_TipoFicheroTxt THEN 
            Escribir_Txt(p_Pathfichero, p_Nombrefichero, v_Regordenante, v_Abonos, p_Idioma, v_CodRetorno, v_DatosError);
       WHEN c_TipoFicheroTxtXml THEN 
            Escribir_Txt(p_Pathfichero, p_Nombrefichero, v_Regordenante, v_Abonos, p_Idioma, v_CodRetorno, v_DatosError);
            IF v_Codretorno = 0 THEN
               Escribir_Xml(p_Pathfichero, p_Nombrefichero, v_Regordenante, v_Abonos, p_Idioma, v_CodRetorno, v_DatosError);
            End If;
       WHEN c_TipoFicheroXml Then
            Escribir_Xml(p_Pathfichero, p_Nombrefichero, v_Regordenante, v_Abonos, p_Idioma, v_CodRetorno, v_DatosError);
       Else
            v_Codretorno := To_Char(-1);
            Raise e_Error;
    END CASE;

    IF v_Codretorno <> 0 THEN
        RAISE e_Error;
    END IF;
  
    v_Datoserror := 'GenerarFicheroTransferencias: actualizando los parametros de salida';
    p_Codretorno := To_Char(0);
    p_Datoserror := Null;
  
  Exception
    When e_Error Then
      Rollback;
      p_Codretorno := v_Codretorno;
      p_Datoserror := v_Datoserror;
    
    When Others Then
      Rollback;
      p_Codretorno := To_Char(Sqlcode);
      p_Datoserror := v_Datoserror || ', ' || Sqlerrm;
  End Generarficherotransferencias;

End Pkg_Siga_Abonos;
/
