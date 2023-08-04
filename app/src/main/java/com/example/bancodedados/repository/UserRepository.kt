package com.example.bancodedados.repository

import android.content.ContentValues
import android.content.Context
import java.lang.Exception
import java.text.SimpleDateFormat

//Classe do repository, onde contém todos os métodos responsável por trabalhar diretamente com o banco de dados.

class UserRepository private constructor(context: Context) {
    //Como esta classe não será instanciada de fora, privamos outros arquivos e códigos de inicializa-la.
    //Perceba que ela recebe um contexto, que no caso, será o contexto recebido pela classe 'MainViewModel'.

    private val userDatabase =
        UserDatabase(context)//Atributo responsável por armazenar uma instância da classe 'UserDatabase', que é a classe responsável por
    //fazer uma conexão direta com o banco de dados

    //Como esta classe sempre será chamada quando formos trabalhar com a base de dados userdb, é de se imaginar que ela poderá ser chamada por diferentes ViewModels (além do MainViewModel)
    //Nesse caso, a melhor coisa a se fazer é criar um SINGLETON (https://pt.wikipedia.org/wiki/Singleton)
    //Para que tenhamos sempre a mesma instancia dessa classe em cada ViewModel.
    //Isso evita que nossas ViewModels consigam instanciar diversas vezes essa classe de forma desnecessária.

    //Para criar um singleton no Kotlin, primeiro criamos atributos/métodos estáticos
    companion object {
        private lateinit var repository: UserRepository//Atributo que será instanciado mais tarde do tipo 'UserRepository'
        //(sim, vamos instânciar nossa classe 'UserRepository' de forma privada dentro da classe)

        fun getInstance(context: Context): UserRepository {//No Singleton, geralmente criamos um método chamado getInstance para pegar a instância da propria classe.
            if (!Companion::repository.isInitialized) {//Aqui estamos verificando se o atributo 'repository' já foi inicializado.
                repository =
                    UserRepository(context)//Se não foi inicializado, ele executa esse comando responsável por inicializar esta classe passando um contexto
                //Lembra que precisamos do context para instanciarmos o 'SQLiteOpenHelper'?
            }
            return repository//Se já estiver instanciada, ele só retorna a instancia dessa classe
        }
    }
    //De acordo com a lógica do SINGLETON acima, quando chamarmos esta classe, primeiramente devemos fazer isso de forma estática (sem instanciar),
    //de modo a conseguir uma instância dessa propria classe, por meio de um método estático.
    //Dessa forma nunca teriamos multiplas instâncias, ao mesmo tempo que a poderiamos acessar os métodos da propria classe que foram declarados abaixo:


    /* MÉTODO SELECT (usando query) */

    fun getUsersByQuery(): ArrayList<Any> {

        //Precisamos declarar uma variável do tipo mutableMapOf (chave e valor) para armazenarmos os valores advindos do banco
        //val listaDeDados = mutableMapOf<String, Any>()

        val listaDeDados = ArrayList<Any>()

        try {//O mais correto a se fazer é sempre inserir comandos que trabalham com banco de dados dentro de uma estrutura 'try/catch'

            //Precisamos recuperar a instância da classe UserDatabase acessando o atributo 'readableDatabase' para dizer ao banco que vamos ler dados!
            val db = userDatabase.readableDatabase

            //O comando abaixo é a montagem da Query do banco de dados usando o método QUERY.
            val resultadosCursor = db.query("usuario", arrayOf("*"), null, null, null, null, null)
            //Observe que passamos o nome da tabela, as colunas que devem ser retornadas, e ainda poderiamos usar outros atributos como 'selection, groypBy, orderBy...'.
            //Mas deixamos como null, pois não iremos utilizá-lo.

            //É importante ressaltar que a variável acima retorna um cursor, que deve ser percorrido tendo seus dados salvos em array, list, map para que possamos acessa-lo
            //de forma mais simples e fácil mais tarde

            //A QUERY pode não encontrar nenhum valor ou vir vázia, portanto devemos verificar isso antes de percorrermos o cursor
            if (resultadosCursor != null && resultadosCursor.count > 0) {

                //Aqui estamos fazendo um loop enquanto existir valores a serem percorridos dentro do cursor. O método moveToNext() percorre para a proxima fila de valores que foram retornados
                while (resultadosCursor.moveToNext()) {

                    //1) Recupera o nome das colunas e salva em suas respectivas variáveis
                    val idUsuario_c =
                        resultadosCursor.getColumnName(resultadosCursor.getColumnIndex("id_usuario"))
                    val nomeUsuario_c =
                        resultadosCursor.getColumnName(resultadosCursor.getColumnIndex("nome"))
                    val idadeUsuario_c =
                        resultadosCursor.getColumnName(resultadosCursor.getColumnIndex("idade"))
                    val dataCriacaoUsuario_c =
                        resultadosCursor.getColumnName(resultadosCursor.getColumnIndex("data_criacao"))

                    //2) Recupera o valor das colunas e salva em suas respectivas variáveis
                    val idUsuario_v =
                        resultadosCursor.getInt(resultadosCursor.getColumnIndex("id_usuario"))
                    val nomeUsuario_v =
                        resultadosCursor.getString(resultadosCursor.getColumnIndex("nome"))
                    val idadeUsuario_v =
                        resultadosCursor.getInt(resultadosCursor.getColumnIndex("idade"))
                    val dataCriacaoUsuario_v =
                        resultadosCursor.getString(resultadosCursor.getColumnIndex("data_criacao"))

                    //3)Monta todos os valores dentro de um Map
                    val dadosMap = mapOf(
                        Pair(idUsuario_c, idUsuario_v),
                        Pair(nomeUsuario_c, nomeUsuario_v),
                        Pair(idadeUsuario_c, idadeUsuario_v),
                        Pair(dataCriacaoUsuario_c, dataCriacaoUsuario_v)
                    )

                    //4) Adiciona no Array a lista de Maps que foi declarada acima
                    listaDeDados.add(dadosMap);

                }

            }

            resultadosCursor.close()//Precisamos fechar o cursor, pois não estaremos mais utilizando ele. Esse processo é necessário mesmo que o cursor não retorne nada.

        } catch (e: Exception) {
            return listaDeDados//Se ocorrer uma 'EXCEPTION' você deve trata-la aqui...
        }

        return listaDeDados//Retorna a lista de dados que montamos acima, caso ela não tenha sido montada, retorna 'null'

    }

    /* MÉTODO INSERT (usando insert) */

    fun insertUserByInsert(nome: String, idade: Int, date: String) {
        //Método usado para salvar registros no banco de dados

        try {//O mais correto a se fazer é sempre inserir comandos que trabalham com banco de dados dentro de uma estrutura 'try/catch'

            //Precisamos recuperar a instância da classe UserDatabase acessando o atributo 'writableDatabase' para dizer ao banco que vamos escrever dados!
            val db = userDatabase.writableDatabase

            //Para inserir os dados com o db.insert, podemos criar uma variável do tipo ContentValues, responsável por organizar em uma estrutura de chave e valor,
            //o nome das colunas junto com os dados que cada uma delas irá armazenar
            val dados = ContentValues()

            dados.put("nome", nome)//para a coluna 'nome', salvaremos o valor existente na variável nome
            dados.put("idade", idade)//para a coluna 'idade', salvaremos o valor existente na variável idade
            dados.put("data_criacao", date)//para a coluna 'data_criacao', salvaremos o valor existente na variável date

            db.insert("usuario", null, dados)//Por fim usamos o db.insert, passando o nome da tabela junto com os dados do tipo ContentValues()

        } catch (e: Exception) {
            //Se ocorrer uma 'EXCEPTION' você deve trata-la aqui...
        }

    }

    /* MÉTODO UPDATE (usando update) */

    fun updateUserByUpdate(id: Int, nome: String, idade: Int){
        //Método usado para atualizar registros no banco de dados

        try {//O mais correto a se fazer é sempre inserir comandos que trabalham com banco de dados dentro de uma estrutura 'try/catch'

            //Precisamos recuperar a instância da classe UserDatabase acessando o atributo 'writableDatabase' para dizer ao banco que vamos escrever dados!
            val db = userDatabase.writableDatabase

            //Para atualizar os dados com o db.update, podemos criar uma variável do tipo ContentValues, responsável por organizar em uma estrutura de chave e valor,
            //o nome das colunas junto com os dados que cada uma delas irá armazenar
            val dados = ContentValues()

            dados.put("nome", nome)//para a coluna 'nome', salvaremos o valor existente na variável nome
            dados.put("idade", idade)//para a coluna 'idade', salvaremos o valor existente na variável idade

            val where = "$id = ?"//esta variável armazena o WHERE da QUERY
            val argumentos = arrayOf(id.toString())//Aqui nos estamos informando os argumentos do selection acima.

            db.update("usuario", dados, where, argumentos)//Usamos o update para fazer a atualização

        } catch (e: Exception) {
            //Se ocorrer uma 'EXCEPTION' você deve trata-la aqui...
        }
    }

    /* MÉTODO DELETE (usando delete) */

    fun deleteUserByDelete(id: Int){
        //Método usado para remover registros no banco de dados

        try {//O mais correto a se fazer é sempre inserir comandos que trabalham com banco de dados dentro de uma estrutura 'try/catch'

            //Precisamos recuperar a instância da classe UserDatabase acessando o atributo 'writableDatabase' para dizer ao banco que vamos escrever dados!
            val db = userDatabase.writableDatabase

            val where = "$id = ?"//esta variável armazena o WHERE da QUERY
            val argumentos = arrayOf(id.toString())//Aqui nos estamos informando os argumentos do selection acima.

            db.delete("usuario", where, argumentos)//Usamos o delete para fazer a remoção

        } catch (e: Exception) {
            //Se ocorrer uma 'EXCEPTION' você deve trata-la aqui...
        }
    }


    /* MÉTODO SELECT (usando raw query) */
    fun getUsersByRawQuery(): ArrayList<Any> {

        //Precisamos declarar uma variável do tipo mutableMapOf (chave e valor) para armazenarmos os valores advindos do banco
        //val listaDeDados = mutableMapOf<String, Any>()

        val listaDeDados = ArrayList<Any>()

        try {//O mais correto a se fazer é sempre inserir comandos que trabalham com banco de dados dentro de uma estrutura 'try/catch'

            //Precisamos recuperar a instância da classe UserDatabase acessando o atributo 'readableDatabase' para dizer ao banco que vamos ler dados!
            val db = userDatabase.readableDatabase

            //O comando abaixo é a montagem da Query do banco de dados usando o método RAW QUERY, que nos permite montar a query do banco diretamente na mão.
            val resultadosCursor = db.rawQuery("SELECT * FROM usuario", arrayOf())
            //O segundo parâmetro são os argumentos que podemos passar, caso queiramos passar algum argumento usando o ? na query
            //Lembrando que diferente do anterior, o having, group by, order by deve ser tudo escrito na mão.

            //É importante ressaltar que a variável acima retorna um cursor, que deve ser percorrido tendo seus dados salvos em array, list, map para que possamos acessa-lo
            //de forma mais simples e fácil mais tarde

            //A QUERY pode não encontrar nenhum valor ou vir vázia, portanto devemos verificar isso antes de percorrermos o cursor
            if (resultadosCursor != null && resultadosCursor.count > 0) {

                //Aqui estamos fazendo um loop enquanto existir valores a serem percorridos dentro do cursor. O método moveToNext() percorre para a proxima fila de valores que foram retornados
                while (resultadosCursor.moveToNext()) {

                    //1) Recupera o nome das colunas e salva em suas respectivas variáveis
                    val idUsuario_c =
                        resultadosCursor.getColumnName(resultadosCursor.getColumnIndex("id_usuario"))
                    val nomeUsuario_c =
                        resultadosCursor.getColumnName(resultadosCursor.getColumnIndex("nome"))
                    val idadeUsuario_c =
                        resultadosCursor.getColumnName(resultadosCursor.getColumnIndex("idade"))
                    val dataCriacaoUsuario_c =
                        resultadosCursor.getColumnName(resultadosCursor.getColumnIndex("data_criacao"))

                    //2) Recupera o valor das colunas e salva em suas respectivas variáveis
                    val idUsuario_v =
                        resultadosCursor.getInt(resultadosCursor.getColumnIndex("id_usuario"))
                    val nomeUsuario_v =
                        resultadosCursor.getString(resultadosCursor.getColumnIndex("nome"))
                    val idadeUsuario_v =
                        resultadosCursor.getInt(resultadosCursor.getColumnIndex("idade"))
                    val dataCriacaoUsuario_v =
                        resultadosCursor.getString(resultadosCursor.getColumnIndex("data_criacao"))

                    //3)Monta todos os valores dentro de um Map
                    val dadosMap = mapOf(
                        Pair(idUsuario_c, idUsuario_v),
                        Pair(nomeUsuario_c, nomeUsuario_v),
                        Pair(idadeUsuario_c, idadeUsuario_v),
                        Pair(dataCriacaoUsuario_c, dataCriacaoUsuario_v)
                    )

                    //4) Adiciona no Array a lista de Maps que foi declarada acima
                    listaDeDados.add(dadosMap);

                }

            }

            resultadosCursor.close()//Precisamos fechar o cursor, pois não estaremos mais utilizando ele. Esse processo é necessário mesmo que o cursor não retorne nada.

        } catch (e: Exception) {
            return listaDeDados//Se ocorrer uma 'EXCEPTION' você deve trata-la aqui...
        }

        return listaDeDados//Retorna a lista de dados que montamos acima, caso ela não tenha sido montada, retorna 'null'

    }

    /* MÉTODO INSERT (usando raw query) */

    fun insertUserByRawQuery(nome: String, idade: Int, date: String) {
        //Método usado para salvar registros no banco de dados

        try {//O mais correto a se fazer é sempre inserir comandos que trabalham com banco de dados dentro de uma estrutura 'try/catch'

            //Precisamos recuperar a instância da classe UserDatabase acessando o atributo 'writableDatabase' para dizer ao banco que vamos escrever dados!
            val db = userDatabase.writableDatabase

            //Método usando o rawQuery para fazer inserção a mão
            db.rawQuery("INSERT INTO usuarios (nome, idade, data_criacao) VALUES (?, ?, ?)", arrayOf(nome, idade.toString(), date))

        } catch (e: Exception) {
            //Se ocorrer uma 'EXCEPTION' você deve trata-la aqui...
        }

    }

    /* MÉTODO UPDATE (usando raw query) */

    fun updateUserByRawQuery(id: Int, nome: String, idade: Int){
        //Método usado para atualizar registros no banco de dados

        try {//O mais correto a se fazer é sempre inserir comandos que trabalham com banco de dados dentro de uma estrutura 'try/catch'

            //Precisamos recuperar a instância da classe UserDatabase acessando o atributo 'writableDatabase' para dizer ao banco que vamos escrever dados!
            val db = userDatabase.writableDatabase

            //Método usando o rawQuery para fazer a atualização a mão
            db.rawQuery("UPDATE usuario SET nome = ?, idade = ? WHERE id_usuario = ?", arrayOf(nome, idade.toString(), id.toString()))

        } catch (e: Exception) {
            //Se ocorrer uma 'EXCEPTION' você deve trata-la aqui...
        }
    }

    /* MÉTODO DELETE (usando raw query) */

    fun deleteUserByRawQuery(id: Int){
        //Método usado para remover registros no banco de dados

        try {//O mais correto a se fazer é sempre inserir comandos que trabalham com banco de dados dentro de uma estrutura 'try/catch'

            //Precisamos recuperar a instância da classe UserDatabase acessando o atributo 'writableDatabase' para dizer ao banco que vamos escrever dados!
            val db = userDatabase.writableDatabase

            //Método usando o rawQuery para fazer a remoção a mão
            db.rawQuery("DELETE FROM usuario WHERE id_usuario = ?", arrayOf(id.toString()))

        } catch (e: Exception) {
            //Se ocorrer uma 'EXCEPTION' você deve trata-la aqui...
        }
    }

}