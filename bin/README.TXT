Para ejecutar un proyecto de Java desde GitHub, primero cloné el repositorio en mi computadora abriendo PowerShell y ejecutando:

Clonar el repositorio:
Abrí la terminal (PowerShell) y ejecuté:

git clone https://github.com/usuario/nombre-del-repositorio.git
Esto descargó todos los archivos del proyecto.


Después entré a la carpeta del proyecto:

(edita tu usuario por el usuario de la PC)

cd cd "C:\Users\Tu usuario\Proyecto-BYTE"

luego compilamos todos el proyecto 

Get-ChildItem -Recurse -Filter *.java | ForEach-Object {
    javac -cp "lib/json-20231013.jar" -d bin $_.FullName
}


Ejecutamos la clase principal (Login.Login)

java -cp "bin;lib/json-20231013.jar" Login.Login


Te abrirá el Login del proyecto en el cual te pedirá un usuario y una pass para ingresar puedes usar un usuario ya existente o crear tu propio usuario.

Usuario: jmonterroso
Pass: Harith$1899

te abrirá un menú el cual esta consumiendo el API solicitado.