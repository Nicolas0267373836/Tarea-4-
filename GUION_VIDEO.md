# Guion para el video de dos minutos

La tarea exige un video de aproximadamente dos minutos, explicar cada
funcionalidad y mostrar tu cara. Usa una cámara pequeña superpuesta en una
esquina durante toda la grabación.

## Preparación antes de grabar

1. Enciende MySQL.
2. Abre el programa y deja visible el login.
3. Crea previamente un usuario llamado `demo` con contraseña
   `Demo12345`, o usa datos que recuerdes.
4. Ten preparados en un bloc de notas los datos de un segundo usuario para
   pegarlos rápido:

```text
Usuario: maria.lopez
Nombre: María
Apellido: López
Teléfono: 70012345
Correo: maria.lopez@correo.com
Contraseña: Demo12345
```

5. Cierra notificaciones privadas.
6. Usa OBS Studio, Zoom o cualquier grabador que capture pantalla y cámara.
7. Coloca tu cámara donde no tape botones ni mensajes.
8. Haz un ensayo. La demostración debe durar entre 1:50 y 2:05.

No uses una contraseña personal en el video.

## Guion cronometrado

### 0:00-0:10 - Presentación

Acción: muestra el login y tu cámara.

Di:

> Hola, soy [tu nombre]. Esta es mi Tarea 4, un sistema de usuarios creado con
> Java Swing, orientación a objetos y MySQL.

### 0:10-0:25 - Validación del login

Acción: pulsa **Iniciar sesión** con ambos campos vacíos. Deja visible el mensaje
un instante. Después ciérralo.

Di:

> El login exige usuario y contraseña. La contraseña se escribe oculta y, si
> faltan datos, el sistema muestra el mensaje solicitado.

### 0:25-0:48 - Registro y validaciones

Acción: abre **Crear una cuenta**. Señala los siete campos. Intenta guardar
rápidamente con un campo vacío o con contraseñas distintas. Luego completa los
datos preparados y guarda.

Di:

> El registro solicita nombre de usuario, nombre, apellido, teléfono, correo,
> contraseña y confirmación. Todos son obligatorios; también se validan el
> correo, los duplicados y que ambas contraseñas coincidan.

### 0:48-1:02 - Inicio de sesión y listado

Acción: inicia sesión con la cuenta que acabas de crear. Muestra la tabla.

Di:

> Al ingresar credenciales válidas se cierra el login y aparece una ventana
> distinta con todos los usuarios registrados.

### 1:02-1:20 - Crear un usuario

Acción: pulsa **Nuevo usuario**, usa los datos preparados y guarda. Muestra cómo
la fila aparece inmediatamente.

Di:

> Desde la pantalla principal puedo registrar otro usuario y la tabla se
> actualiza automáticamente.

### 1:20-1:36 - Actualizar

Acción: selecciona a María, pulsa **Actualizar**, cambia el teléfono y guarda.

Di:

> Selecciono un usuario, modifico sus datos y el cambio aparece inmediatamente.
> La contraseña puede conservarse o cambiarse.

### 1:36-1:49 - Eliminar

Acción: selecciona a María, pulsa **Eliminar**, muestra la confirmación y acepta.

Di:

> Para eliminar existe una confirmación que evita borrados accidentales. Al
> aceptar, la fila desaparece de la tabla.

### 1:49-2:00 - Cerrar sesión y cierre

Acción: pulsa **Cerrar sesión** y muestra de nuevo el login.

Di:

> Finalmente cierro sesión y el programa regresa al login. El proyecto aplica
> abstracción, encapsulamiento, herencia, polimorfismo, Singleton y Factory.

## Si el tiempo se pasa

No esperes demasiado después de cada mensaje. Mientras hablas, mueve el cursor
y ejecuta la siguiente acción. Elimina silencios en el editor de video, pero no
aceleres tanto que no se puedan leer los mensajes.

## Lista de revisión del video

- Se ve tu cara.
- Se escucha tu voz con claridad.
- Se ve el login.
- Se ve una contraseña oculta.
- Se muestra al menos una validación.
- Se muestra el registro.
- Se demuestra login correcto.
- Se ve la lista completa.
- Se demuestra nuevo, actualizar y eliminar.
- Se demuestra cerrar sesión.
- Dura aproximadamente dos minutos.
- El enlace permite que el profesor vea el video.

