# Explicación completa del proyecto

## 1. Qué problema resuelve

El programa administra usuarios en una base MySQL. La persona empieza en una
ventana de login. Si aún no tiene cuenta, abre el formulario de registro. Cuando
sus credenciales son correctas, la ventana de login se cierra y aparece una
ventana principal diferente con la tabla de usuarios.

Desde la pantalla principal se puede:

- crear un usuario;
- actualizar un usuario seleccionado;
- eliminarlo después de confirmar;
- cerrar sesión y regresar al login.

Después de crear, actualizar o eliminar, se consulta nuevamente MySQL y se
refresca la tabla. Por eso los cambios aparecen automáticamente.

## 2. Cómo viaja la información

```text
Ventana Swing
     |
     v
UsuarioService (validaciones y reglas)
     |
     v
UsuarioDAO (abstracción)
     |
     v
MySQLUsuarioDAO (consultas SQL)
     |
     v
MySQL
```

La interfaz nunca escribe consultas SQL. Solo llama a `UsuarioService`. El
servicio valida y llama a la interfaz `UsuarioDAO`. La implementación concreta
`MySQLUsuarioDAO` ejecuta la consulta.

Esta separación facilita comprender, probar y cambiar cada parte sin mezclar
todo en una sola clase.

## 3. Los cuatro pilares de POO

### Abstracción

`Persona` representa las características generales de una persona sin decidir
qué tipo concreto es. Además, `UsuarioDAO` describe las operaciones necesarias
para trabajar con usuarios sin indicar cómo se guardan.

Puedes explicarlo así:

> La abstracción muestra solamente lo necesario. La interfaz conoce operaciones
> como listar o insertar, pero no necesita conocer el SQL.

### Encapsulamiento

Los atributos de `Persona` y `Usuario` son privados. Se accede a ellos mediante
getters y setters. Las reglas de negocio quedan encapsuladas en
`UsuarioService`, y la conexión queda dentro de `DatabaseConnection`.

Puedes explicarlo así:

> El estado interno está protegido y cada clase controla su responsabilidad.

### Herencia

`Usuario` utiliza `extends Persona`. Así hereda `id`, `nombre`, `apellido`,
`teléfono`, `correo` y `getNombreCompleto()`.

Puedes explicarlo así:

> Un usuario es una persona, por eso reutiliza sus datos y comportamientos.

### Polimorfismo

`Persona` declara el método abstracto `getTipoPersona()` y `Usuario` lo
sobrescribe. También hay polimorfismo cuando `UsuarioService` trabaja con una
referencia de tipo `UsuarioDAO`, aunque el objeto real sea `MySQLUsuarioDAO`.

Puedes explicarlo así:

> El mismo contrato puede tener diferentes implementaciones. El servicio usa un
> UsuarioDAO sin depender de la clase concreta de MySQL.

## 4. Patrones de diseño

### Singleton

`DatabaseConnection.getInstance()` devuelve siempre la misma instancia
encargada de la configuración y creación de conexiones. `SesionUsuario` también
usa Singleton para conservar al usuario autenticado.

No se mantiene una conexión MySQL abierta para siempre. El Singleton abre una
conexión por operación y la cierra automáticamente, lo cual evita conexiones
vencidas.

### Factory

`DAOFactory.crearUsuarioDAO(...)` crea el repositorio correcto. `App` pide un
DAO MySQL a la fábrica y entrega el resultado al servicio.

Si en el futuro se guardaran usuarios en otro sistema, se podría agregar otra
implementación sin reescribir las ventanas.

## 5. Clases importantes

### `App`

Es el punto de entrada. Aplica el estilo visual, obtiene el DAO desde la Factory,
crea el servicio y abre el login.

### `DatabaseConfig`

Lee la URL, el usuario y la contraseña desde `database.properties`. También
permite usar variables de entorno para no escribir credenciales en el archivo.

### `DatabaseConnection`

Implementa Singleton y crea conexiones JDBC con MySQL Connector/J.

### `Persona` y `Usuario`

Son el modelo de dominio. `Usuario` hereda de `Persona` y añade el nombre de
usuario y el hash de contraseña.

### `UsuarioDAO`

Es el contrato para listar, buscar, insertar, actualizar y eliminar.

### `MySQLUsuarioDAO`

Contiene el SQL. Usa `PreparedStatement` para que los datos escritos por el
usuario nunca se concatenen directamente en las consultas.

### `UsuarioService`

Centraliza las reglas:

- ningún campo de registro puede estar vacío;
- el usuario debe tener entre 3 y 50 caracteres permitidos;
- el teléfono y el correo deben ser válidos;
- la contraseña debe tener ocho caracteres o más;
- ambas contraseñas deben coincidir;
- usuario y correo no pueden repetirse;
- las credenciales deben coincidir para iniciar sesión.

### `PasswordHasher`

Genera una sal aleatoria y usa PBKDF2 con HMAC-SHA256. MySQL recibe un hash con
la forma:

```text
pbkdf2_sha256$iteraciones$sal$hash
```

La contraseña original nunca se guarda. Durante el login se calcula nuevamente
el hash y se compara de forma segura.

### `LoginFrame`

Oculta la contraseña, permite mostrarla si el usuario lo desea, valida el login,
cierra su ventana y abre `DashboardFrame`.

### `UsuarioFormDialog`

El mismo formulario funciona en tres modos: registro, nuevo usuario y edición.
En edición la contraseña es opcional; si queda vacía se conserva la anterior.

### `DashboardFrame`

Muestra la tabla, el usuario de la sesión y los botones. Después de cada
operación vuelve a cargar la información.

## 6. Base de datos

La tabla `usuarios` contiene:

| Columna | Uso |
| --- | --- |
| `id` | Identificador autoincremental |
| `usuario` | Nombre único para iniciar sesión |
| `nombre` | Nombre |
| `apellido` | Apellido |
| `telefono` | Número de teléfono |
| `correo` | Correo único |
| `password_hash` | Contraseña protegida |
| `creado_en` | Fecha de creación |
| `actualizado_en` | Fecha del último cambio |

Las restricciones `UNIQUE` protegen el usuario y correo incluso si dos
operaciones intentan registrar el mismo dato al mismo tiempo.

## 7. Validaciones solicitadas

| Requisito | Implementación |
| --- | --- |
| Usuario o contraseña vacíos en login | `UsuarioService.iniciarSesion` |
| Campos de registro obligatorios | `validarDatosComunes` y `validarPasswordObligatorio` |
| Indicar el campo faltante | Excepciones con el nombre exacto del campo |
| Contraseñas diferentes | Comparación con `Arrays.equals` |
| Contraseña oculta | `JPasswordField` |
| Cerrar sesión | `DashboardFrame.cerrarSesion` |
| Regresar al login | Se crea un nuevo `LoginFrame` |
| Actualizar y eliminar | Servicio y DAO |
| Cambios automáticos en pantalla | `cargarUsuarios()` después de cada operación |

## 8. Pruebas

`PasswordHasherTest` comprueba que:

- la misma contraseña produce hashes diferentes por la sal;
- ambos hashes son verificables;
- una contraseña incorrecta se rechaza.

`UsuarioServiceTest` comprueba:

- campos obligatorios;
- confirmación de contraseña;
- registro y login;
- usuario duplicado.

Las pruebas no necesitan MySQL porque usan una implementación de memoria de
`UsuarioDAO`. Se ejecutan con:

```powershell
mvn test
```

## 9. Qué debes saber para defenderlo

No memorices cada línea. Comprende esta cadena:

1. Swing captura los datos.
2. El servicio los valida.
3. El DAO prepara el SQL.
4. MySQL guarda o devuelve el usuario.
5. La interfaz actualiza la tabla o muestra el mensaje.

Con esa explicación, los cuatro pilares y los dos patrones, puedes justificar la
arquitectura completa.
