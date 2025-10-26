# API Gastos Personales

## Descripci�n general

Este repositorio contiene una soluci�n desarrollada para `.NET 8`. Est� pensada para abrirse y ejecutarse desde Visual Studio 2022 o con la CLI de .NET. La soluci�n est� organizada para separar responsabilidades (API, l�gica de dominio, infraestructura y pruebas) y facilitar pruebas, mantenimiento y despliegue.

## Soluci�n (#solution)

La soluci�n agrupa uno o varios proyectos con responsabilidades claras. A continuaci�n se explica la estructura t�pica y el rol de cada componente:

- `API / Web`  
  - Proyecto responsable de la exposici�n de la aplicaci�n (por ejemplo, Web API o UI). Aqu� se configuran controladores, rutas, middleware y la inyecci�n de dependencias. Es el punto de entrada para peticiones HTTP.
- `Dominio / Core`  
  - Contiene entidades, reglas de negocio y contratos (interfaces). No debe depender de infraestructuras concretas; es el coraz�n de la aplicaci�n.
- `Infraestructura`  
  - Implementaciones concretas de repositorios, acceso a datos (EF Core, Dapper, etc.), integraci�n con servicios externos y adaptadores. Implementa los contratos definidos en `Dominio`.
- `Aplicaci�n / Services` (opcional)  
  - Casos de uso, servicios de aplicaci�n y orquestaci�n entre dominio e infraestructura.
- `Tests`  
  - Proyecto(s) de pruebas unitarias e integraci�n que validan la l�gica de negocio y los puntos cr�ticos.

Arquitectura y principios aplicados:
- Separaci�n de responsabilidades (SoC) para facilitar pruebas y cambios.
- Inyecci�n de dependencias para sustituir implementaciones en pruebas.
- Configuraci�n por entorno mediante `appsettings.json` y variables de entorno.
- Compatibilidad con contenedores mediante un `Dockerfile` en el proyecto de inicio (opcional).

## Requisitos

- .NET SDK 8.x instalado (`dotnet --info` para verificar).  
- Visual Studio 2022 (actualizado) o cualquier editor que soporte `.NET 8`.  
- Opcional: Docker (si desea crear y ejecutar contenedores).

## Inicio r�pido (CLI)

Desde la ra�z de la soluci�n:

1. Restaurar dependencias:
2. Compilar:
3. Ejecutar (si hay varios proyectos, indique el proyecto de inicio):
4. Ejecutar pruebas: