# Sistema de Usuarios - Tarea 4

Aplicación de escritorio desarrollada con Java 17, Swing, AWT, JDBC y MySQL.
Permite registrar usuarios, iniciar y cerrar sesión, listar todas las cuentas,
actualizar sus datos y eliminarlas.

## Qué incluye

- Login con usuario y contraseña oculta.
- Mensajes claros cuando falta el usuario o la contraseña.
- Registro con todos los campos indicados en el mandato.
- Validación campo por campo.
- Validación de contraseñas coincidentes.
- Validación de formato de usuario, teléfono y correo.
- Detección de usuario o correo duplicado.
- Pantalla principal con todos los usuarios registrados.
- Creación, actualización y eliminación.
- Actualización automática de la tabla después de cada cambio.
- Cierre de sesión y regreso a la ventana de login.
- Contraseñas protegidas con PBKDF2; nunca se guardan en texto plano.
- Uso de abstracción, encapsulamiento, herencia y polimorfismo.
- Patrones Singleton y Factory.
- Pruebas automáticas con JUnit.
- Ejecutable JAR generado por Maven.

## Inicio rápido

Antes de ejecutar el programa necesitas instalar Java JDK 17, Maven y MySQL.
Los pasos exactos están en [GUIA_INSTALACION.md](GUIA_INSTALACION.md).

Cuando las herramientas y la base de datos estén preparadas:

1. Ejecuta `database/schema.sql` en MySQL.
2. Revisa `src/main/resources/database.properties`.
3. Haz doble clic en `compilar_y_ejecutar.bat`.
4. En el login, pulsa **Crear una cuenta** y registra el primer usuario.

También puedes abrir una terminal dentro del proyecto y ejecutar:

```powershell
mvn clean package
java -jar target/sistema-usuarios-tarea4.jar
```

## Documentos incluidos

- `GUIA_INSTALACION.md`: instalación, base de datos, ejecución y solución de errores.
- `EXPLICACION_DEL_PROYECTO.md`: arquitectura y explicación para defender el código.
- `GUION_VIDEO.md`: preparación y guion cronometrado para el video de dos minutos.
- `SUBIR_A_GITHUB.md`: instrucciones para crear el repositorio y entregar el enlace.
- `CHECKLIST_ENTREGA.md`: lista final para no olvidar ningún requisito.

## Estructura principal

```text
SistemaUsuariosTarea4/
├── database/
│   └── schema.sql
├── src/
│   ├── main/
│   │   ├── java/com/tarea4/
│   │   │   ├── config/      Configuración
│   │   │   ├── dao/         Acceso a datos
│   │   │   ├── database/    Conexión Singleton
│   │   │   ├── factory/     Factory de repositorios
│   │   │   ├── model/       Persona y Usuario
│   │   │   ├── security/    Protección de contraseñas
│   │   │   ├── service/     Validaciones y reglas de negocio
│   │   │   ├── session/     Sesión actual
│   │   │   ├── ui/          Ventanas Swing
│   │   │   └── App.java     Punto de entrada
│   │   └── resources/
│   │       └── database.properties
│   └── test/                 Pruebas automáticas
├── pom.xml
├── compilar_y_ejecutar.bat
└── ejecutar.bat
```

## Comandos útiles

```powershell
# Ejecutar las pruebas
mvn test

# Compilar el JAR ejecutable
mvn clean package

# Iniciar la aplicación compilada
java -jar target/sistema-usuarios-tarea4.jar
```

## Autor

Nicolas Abud 2025-2437

