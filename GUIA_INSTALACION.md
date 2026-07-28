# Guía de instalación y ejecución

Esta guía está escrita para Windows. Solo necesitas hacer estas configuraciones
una vez.

## 1. Instalar Java JDK 17

El equipo revisado tenía únicamente Java 8 para ejecutar programas, pero no un
compilador. Por eso debes instalar un **JDK**, no solamente un JRE.

1. Abre la página oficial de
   [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=17).
2. Selecciona:
   - Operating System: Windows
   - Architecture: x64
   - Package Type: JDK
   - Version: 17 - LTS
3. Descarga el instalador `.msi`.
4. Durante la instalación activa las opciones para configurar `JAVA_HOME` y
   agregar Java al `PATH`.
5. Cierra y abre nuevamente PowerShell.
6. Comprueba la instalación:

```powershell
java -version
javac -version
```

Ambos comandos deben mostrar una versión 17.

## 2. Instalar Maven

Maven descarga MySQL Connector/J, ejecuta las pruebas y crea el JAR.

1. Sigue la
   [guía oficial de instalación de Maven](https://maven.apache.org/install.html).
2. Descarga la distribución binaria desde
   [Apache Maven](https://maven.apache.org/download.cgi).
3. Descomprime Maven en una carpeta estable, por ejemplo
   `C:\Herramientas\apache-maven`.
4. Agrega la carpeta `bin` de Maven al `PATH` de Windows.
5. Abre otra terminal y comprueba:

```powershell
mvn --version
```

Debe mostrar Maven y Java 17. Si usarás Eclipse, también puedes importar el
proyecto como Maven Project; Eclipse descargará las dependencias.

## 3. Instalar y preparar MySQL

### Si todavía no tienes MySQL

1. Descarga
   [MySQL Installer para Windows](https://dev.mysql.com/downloads/installer/).
2. Elige **Developer Default** para instalar MySQL Server y MySQL Workbench.
3. Configura el servidor en el puerto predeterminado `3306`.
4. Crea una contraseña para `root` y guárdala.
5. Termina la instalación y abre MySQL Workbench.

También puedes usar XAMPP. En ese caso, enciende el módulo **MySQL** y utiliza
phpMyAdmin. XAMPP normalmente usa `root` sin contraseña hasta que el usuario la
configura.

### Crear la base de datos

Con MySQL Workbench:

1. Abre tu conexión local.
2. Elige **File > Open SQL Script**.
3. Abre `database/schema.sql`.
4. Pulsa el icono del rayo para ejecutar todo.
5. En el panel izquierdo debe aparecer la base `tarea4_usuarios` y su tabla
   `usuarios`.

Con phpMyAdmin:

1. Abre `http://localhost/phpmyadmin`.
2. Entra en la pestaña **Importar**.
3. Selecciona `database/schema.sql`.
4. Pulsa **Importar**.

## 4. Configurar la contraseña de MySQL

Abre:

```text
src/main/resources/database.properties
```

Ese archivo es local y no se sube a GitHub. Si clonas el proyecto en otro
equipo, primero copia `database.properties.example` con el nombre
`database.properties`.

La configuración inicial es:

```properties
db.url=jdbc:mysql://localhost:3306/tarea4_usuarios?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/La_Paz&characterEncoding=UTF-8
db.user=root
db.password=
```

- Si `root` no tiene contraseña, déjalo así.
- Si configuraste una contraseña, escríbela después de `db.password=`.
- Si usas otro usuario, cambia `db.user`.
- No subas una contraseña personal o importante a GitHub. Para una tarea local,
  usa una contraseña exclusiva de desarrollo.

## 5. Compilar y ejecutar

### Método sencillo

Haz doble clic en:

```text
compilar_y_ejecutar.bat
```

El archivo ejecutará las pruebas, creará el JAR y abrirá el sistema. La primera
compilación puede tardar porque Maven descarga dependencias.

Después de compilar una vez, puedes usar:

```text
ejecutar.bat
```

### Desde PowerShell

Abre PowerShell dentro de la carpeta del proyecto:

```powershell
mvn clean package
java -jar target/sistema-usuarios-tarea4.jar
```

### Desde Eclipse

1. Elige **File > Import**.
2. Selecciona **Maven > Existing Maven Projects**.
3. Elige la carpeta `SistemaUsuariosTarea4`.
4. Espera a que Maven termine de descargar las dependencias.
5. Abre `src/main/java/com/tarea4/App.java`.
6. Pulsa **Run As > Java Application**.

## 6. Primer uso

1. En el login pulsa **Crear una cuenta**.
2. Completa los siete campos.
3. Usa una contraseña de ocho caracteres o más.
4. Guarda el usuario.
5. Inicia sesión con el nombre de usuario y la contraseña registrados.
6. En la pantalla principal prueba nuevo, actualizar, eliminar y cerrar sesión.

## Problemas comunes

### `java` o `javac` no se reconoce

No quedó configurado el `PATH`. Reinstala Temurin marcando las opciones
`JAVA_HOME` y `Add to PATH`, o configura esas variables manualmente.

### `mvn` no se reconoce

Agrega `...\apache-maven\bin` al `PATH`, cierra la terminal y vuelve a abrirla.

### `Communications link failure`

MySQL está apagado o usa otro puerto. Inicia el servicio MySQL y confirma que
utilice el puerto `3306`.

### `Access denied for user 'root'`

El usuario o la contraseña de `database.properties` no coinciden con MySQL.

### `Unknown database 'tarea4_usuarios'`

Todavía no ejecutaste `database/schema.sql`.

### La aplicación abre pero no permite iniciar sesión

Primero debes crear una cuenta desde el botón **Crear una cuenta**. El proyecto
no incluye usuarios ni contraseñas predeterminados.
