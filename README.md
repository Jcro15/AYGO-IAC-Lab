# AREP-IaC LAB- Web app automatically deployed to AWS
## Descripción
El objetivo de este laboratorio es explorar las capacidades de la infraestructura como código (IaC) utilizando AWS cloudformation
y CDK, para esto se propone desarrollar una aplicación web simple y desplegarla en múltiples máquinas EC2 automáticamente
utilizando infraestructura como código para crear los recursos necesarios en AWS. 


## Pre-requisitos
### Herramientas
* [Docker](https://www.docker.com/) - Administrador de contenedores
* [Maven](https://maven.apache.org/) - Administrador de dependencias
* [Git](https://git-scm.com/) - Sistema de control de versiones
* [Java](https://www.java.com/) - Tecnología para el desarrollo de aplicaciones
* [CDK](https://aws.amazon.com/es/cdk/) - Cloud development kit para AWS, se puede instalar con `npm install -g aws-cdk`
* [AWS CLI](https://aws.amazon.com/es/cli/) -  Command Line Interface para AWS
* [Jq](https://jqlang.github.io/jq/) - Manipulador de Json
### Configuraciones
* Credenciales de AWS configuradas (`aws configure`)
* (Opcional) Si desea construir la aplicación y subirla a un repositorio de docker, configure las credenciales de docker
  (`docker login`)
* En el archivo [configs.txt](configs.txt) modifique las opciones de `vpcId` y `roleName` según las configuraciones en su cuenta
  * Si desea utilizar una imagen propia como aplicación, adicionalmente modifique el valor de `appImage` con el nombre y tag de la imagen
  * Si desea construir la aplicación `SparkServer` y subirla a un repositorio de docker propio, modifique las opciones de `appImage`
   con el nombre del repositorio y `build_app` con el valor `true`

## Instrucciones de construcción y ejecución
A continuación se proporcionan las instrucciones para desplegar la aplicación.
1. Proporcione permisos de ejecución al script [deploy.sh](deploy.sh), utilizando el comando (en Linux) `chmod +x deploy.sh`
2. Ejecute el script [deploy.sh](deploy.sh). En linux desde la terminal ejecute el comando `./deploy.sh`
3. Esperar que el stack se despliegue, se puede consultar el estado del despliegue desde el servicio de Cloudformation en AWS,
    El stack desplegado se llama SimpleEc2WebAppStack
4. Una vez el stack pase a estado CREATE_COMPLETE, acceda al servicio EC2 y seleccione cualquiera de las máquinas creadas,
   es posible acceder a las máquinas a partir del dns público generado, a través del puerto 80 y utilizando el endpoint `/hello`
5. Una vez terminado el experimento puede ejecutar el siguiente comando para eliminar el stack creado
   `aws cloudformation delete-stack --stack-name SimpleEc2WebAppStack`


## Resultados del despliegue
A continuación se muestran evidencias del despliegue realizado  y la ejecución de la aplicación en las 
máquinas de EC2 creadas.
### Stack creado exitosamente
![stackCreated.png](img%2FstackCreated.png)
### Recursos creados en el stack
![stackResources.png](img%2FstackResources.png)
### Instancias creadas
![createdInstances.png](img%2FcreatedInstances.png)
### Aplicación en ejecución
![instancesRunningApp.png](img%2FinstancesRunningApp.png)

## Resultados del experimento
* La infraestructura como código facilita la creación de recursos en la nube, además de hacer los despliegues más confiables
al permitir fácil replicabilidad
* Si bien cloudformation es la primera versión de IaC para AWS, la introducción de CDK ofrece las ventajas de los lenguajes de alto nivel,
  así como la posibilidad de usar un compilador que facilite la detección de errores en etapas tempranas, y no al momento de realizar
  despliegues
* Si bien el código se generó utilizando Java y CDK, las limitaciones del ambiente no permitieron ejecutar el comando de 
  `cdk bootstrap` necesario para utilizar las herramientas que provee cdk para desplegar automáticamente, es por esto que fue necesario
  utilizar el template de cloudformation generado para realizar el despliegue, así como manipular dicho template para remover
  ciertas referencias a los recursos generados por el bootstrap 