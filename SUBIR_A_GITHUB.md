# Cómo subir el proyecto a GitHub

Este paso debes hacerlo tú porque el repositorio debe quedar en tu cuenta y la
tarea es individual.

## Antes de subir

1. Abre `README.md`.
2. Reemplaza la sección **Autor** con tu nombre, matrícula y asignatura.
3. Confirma que `database.properties` no contenga una contraseña personal.
4. Ejecuta `mvn clean package` y verifica que las pruebas pasen.
5. No subas la carpeta `target`; `.gitignore` ya la excluye.

## Opción A: GitHub Desktop

1. Instala [GitHub Desktop](https://desktop.github.com/).
2. Abre GitHub Desktop e inicia sesión.
3. Elige **File > Add local repository**.
4. Selecciona la carpeta `SistemaUsuariosTarea4`.
5. Si indica que no es un repositorio, pulsa **create a repository**.
6. Escribe un resumen como `Entrega completa de Tarea 4`.
7. Pulsa **Commit to main**.
8. Pulsa **Publish repository**.
9. Decide si debe ser público o privado según lo indicado por tu profesor.
10. Abre el repositorio en el navegador y copia su enlace.

## Opción B: comandos Git

Instala [Git para Windows](https://git-scm.com/install/windows.html) si todavía
no tienes el comando `git`.

En GitHub crea un repositorio vacío llamado, por ejemplo,
`tarea-4-sistema-usuarios`. No agregues README, licencia ni `.gitignore` desde
GitHub porque el proyecto ya contiene esos archivos.

Abre PowerShell dentro de la carpeta del proyecto y ejecuta:

```powershell
git init
git add .
git commit -m "Entrega Tarea 4 - Sistema de usuarios"
git branch -M main
git remote add origin https://github.com/TU-USUARIO/tarea-4-sistema-usuarios.git
git push -u origin main
```

Reemplaza `TU-USUARIO` con tu usuario real.

## Subir y compartir el video

Puedes subirlo a YouTube como **No listado** o a Google Drive. Comprueba el
enlace en una ventana privada del navegador para asegurar que el profesor tenga
permiso.

## Advertencia de la tarea

El PDF indica una penalización si el repositorio se actualiza después de la fecha
de entrega. Sube la versión final antes del plazo, verifica todos los archivos y
no hagas commits posteriores a la hora límite.

Entrega estos dos enlaces:

```text
Repositorio: https://github.com/...
Video: https://...
```

