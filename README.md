# Aprenda a usar o SQLite com Kotlin (Android Studio)
Este repositório contém algumas dicas de como utilizar o banco de dados SQLite com o Kotlin no Android Studio.

## Sobre o Projeto

Este sistema não é um aplicativo pronto, e não possui qualquer tipo de interação entre telas, visto que toda a lógica do banco de dados como a criação, seleção, inserção, atualização e remoção, está sendo declarada dentro do método ```onCreate``` presente na ```MainActivity```.

É importante ressaltar que as dicas aqui presentes estão utilizando o padrão de arquitetura de software chamado MVVM (Model View ViewModel).

Nos exemplos, estou utilizando duas formas de se trabalhar com o banco de dados, a primeira faço o uso do ```db.query()```, ```db.insert()```, ```db.update()```, ```db.delete()``` e a outra faço todas essas operações utilizando o ```db.rawQuery()```.

Todos os arquivos necessários foram comentados detalhadamente para o seu entendimento.

## Instalação 

Para usar este sistema é necessário que você já tenha instalado na sua máquina local o [Android Studio](https://developer.android.com/studio).

Com o ambiente já configurado, basta fazer o clone deste repositório para dentro do seu ambiente:

 ```git clone https://github.com/micilini/learn-sqlite-kotlin.git```

 Por fim, basta abrir a pasta do projeto com o Android Studio e esperar que ele faça a sincronização dos pacotes necessários.

 ## Por onde começar?

 A ideia principal é que você entenda como funciona a comunicação com o banco de dados (SQLite) por meio do Kotlin.

 Nesse caso, comece pelo arquivo ```MainActivity.kt``` e vá seguindo os comandos ali presentes.

 ## Posso reutilizar os arquivos no meu projeto?

Sim (e deve), a ideia deste projeto é que ele também sirva como template para suas proprias aplicações.

Sendo assim, existem alguns passos para fazer esta implementação:

1) Você vai precisar criar um  ```package ``` cujo nome será  ```'repository' ```.

2) Dentro desse package crie dois arquivos, um chamado  ```nomedoseubancoDatabase ``` e outro chamado  ```nomedoseubancoRepository```. (Exemplo: Supondo que o nome do seu banco de dados seja cursos, você deverá criar dentro da pasta package dois arquivos como:  ```CursosDatabase ``` e ```CursosRepository```).

3) Por fim basta copiar a lógica presentes nos dois arquivos de template ( ```UserDatabase ``` e ```UserRepository```) e customizar criando métodos e atributos a sua vontade.

4) Não se esqueça que este projeto usa o padrão MVVM, de modo que você é convidado a utilizar um ViewModel para tratar todas as interações com o Repository.

## Imagens

![MainActivity.kt](http://micilini.com/assets/img/learn-sqlite-kotlin.png)

