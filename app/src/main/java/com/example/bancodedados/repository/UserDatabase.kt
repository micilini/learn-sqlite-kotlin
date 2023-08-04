package com.example.bancodedados.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//Este arquivo é usado para realizar conexões com a base de dados, perceba que ele extende da biblioteca chamada 'SQLiteOpenHelper', que é uma biblioteca
//padrão do Android, que nos ajuda a realizar operações com a base de dados.

class UserDatabase (context: Context): SQLiteOpenHelper(context, NAME, null, VERSION) {

    //Usamos atributos estáticos como 'NAME' e 'VERSION' para facilitar a instancia desssa classe ('UserDatabase')
    companion object{
        private const val NAME = "userdb"//Será o nome do banco de dados que será criado
        private const val VERSION = 1//Será a versão atual do banco de dados atual
    }

    //Observe que os métodos estáticos NAME, VERSION estão sendo enviados para a classe 'SQLiteOpenHelper'. Se não fosse feito dessa maneira, a classe
    //'UserRepository' seria responsável por passar esses dados, o que não é interessante para a nossa lógica.
    //Sendo assim a classe 'UserReposity' fica somente responsável por passar o contexto.


    //Quando herdamos a classe 'SQLiteOpenHelper', precisamos definir um override de dois métodos principais: OnCreate e OnUpgrade

    override fun onCreate(db: SQLiteDatabase) {
        //Este método é sempre chamado quando instanciamos a classe do 'SQLiteOpenHelper' pela primeira vez.
        //Geralmente isso acontece quando o aplicativo é instalado no dispositivo do usuário. No nosso caso, isso é tido como verdade, pois estamos executando
        //diversas operações com o banco já no método 'OnCreate' do MainActivity.

        /*
        Esse método funciona da seguinte forma:

        1) Sempre quando o aplicativo é instalado no dispositivo do usuário e a classe 'UserDatabase' é chamada.
        2) O SQLiteOpenHelper, verifica se dentro da pasta do aplicativo, já não existe uma base de dados chamada de "userdb".
        3) Se não existir, ele executa este método a fim de executar todos os comandos responsáveis por criar as tabelas que existirão dentro dessa base de dados,
        além é claro, do próprio arquivo da base de dados (userdb.sqlite)
        4) Se já existir essa base de dados dentro dos arquivos do aplicativo, ele ignora completamente este método.
         */

        //Aqui nos executamos um método chamado 'execSQL' por meio da variável 'db' (vinda como parâmetro desse método), para executarmos nossas QUERYS de criação de tabelas:

        //Aqui estamos criando uma tabela chamada 'usuario', com as colunas: id_usuario, nome, idade, data_criacao
        db.execSQL("CREATE TABLE usuario (id_usuario integer primary key autoincrement, nome text, idade integer, data_criacao datetime);")

        //Aqui estamos criando uma tabela chamada 'carros_usuario', com as colunas: id_carro, id_usuario, marca, modelo
        db.execSQL("CREATE TABLE carros_usuario (id_carro integer primary key autoincrement, id_usuario integer, marca text, modelo text, data_criacao datetime);")

        //Aqui é o momento de criar de forma definitiva o seu banco de dados com todas as tabelas iniciais.
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //Este metodo é sempre chamado quando instanciamos a classe do 'SQLiteOpenHelper', e a biblioteca percebe que a versão existente na variável estática VERSION
        //É diferente da versão atual

        /*
        Ou seja, quando o método onCreate é chamado e o banco de dados é criado, a bilioteca leva em consideração a versão que enviamos durante a instância da biblioteca.
        Observe que estamos enviando a variável VERSION no construtor: SQLiteOpenHelper(context, NAME, null, VERSION)
         */

        //Então quando ele nota que a versão do banco mudou, ou seja, quando você troca o VERSION = 1 para VERSION = 2 (ou qualquer outro número).
        //A biblioteca consegue identificar isso, e chama esse método ('onUpgrade')
        //E aqui, você pode fazer validações das versões utilizados a fim de executar novas QUERYS de criação de tabelas.

        //Isso aqui é muito útil quando você precisa criar novas tabelas que não foram criadas no método 'onCreate' ao mesmo tempo que seu aplicativo já esta rodando
        //Em algum dispositivo, e você sabe que o 'onCreate' nunca mais será chamado.

        //A lógica de funcionamento desse método pode ser feito da seguinte forma.
        //Vamos supor que você criou 3 versões diferentes, onde a cada versão você criou novas tabelas

        //1) Você precisa selecionar a versão atual
        var versaoAtual = oldVersion + 1

        //2) Você faz cada validação, começando pela versão atual, de modo a aumentar a contagem da variável versaoAtual para que o aplicativo do seu usuário
        //contenha todas as atualizações

        //versão 1 não precisa, pois a ideia é que ela já foi criada durante a chamada do onCreate(), e já subimos a contagem pela variavel versaoAtual
        if(versaoAtual == 2){
            db.execSQL("CREATE TABLE empregos_usuario (id_emprego integer primary key autoincrement, id_usuario integer, tipo_emprego text);")
            //Supondo que na versão 2, você só criou uma única tabela...
            ++versaoAtual
        }

        if(versaoAtual == 3){
            db.execSQL("CREATE TABLE casa_usuario (id_casa integer primary key autoincrement, id_usuario integer, tipo_casa text);")
            db.execSQL("CREATE TABLE medico_usuario (id_medico integer primary key autoincrement, id_usuario integer, tipo_medico text);")
            //Supondo que na versão 3, você criou duas tabelas...
            ++versaoAtual
        }

        //Usando a lógica acima, supondo que o usuário esteja ainda com a versão 1 do banco de dados, e eu como desenvolvedor(a) já lancei a atualização 3.
        //Na hora que o usuário for atualizar seu aplicativo, esse método, vai primeiro verificar se a versãoAtual é igual a 2, e vai criar todas as tabelas da versão 2.
        //Em seguida ele vai aumentar a contagem de modo a executar todas as atualizações da versão 3, e assim por diante.

    }


}