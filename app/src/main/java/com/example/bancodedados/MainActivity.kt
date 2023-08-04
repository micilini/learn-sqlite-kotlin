package com.example.bancodedados

import java.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.bancodedados.viewmodel.MainViewModel
import java.util.Date

class MainActivity : AppCompatActivity() {

    //Este projeto tem o intuito de nos ensinar a como configurar conexões com o banco de dados, afim de criar tabelas e realizar operações (SELECT, INSERT, UPDATE, DELETE)
    //O banco de dados utilizado será o SQLite (Banco de dados interno do aplicativo), e o Android já possuí algumas bibliotecas padrão para se trabalhar com ele.

    private lateinit var viewModel: MainViewModel//Aqui estamos declarando uma variável do tipo MainViewModel que será inicializada mais tarde

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicioOperacoesBanco()//Chama a função responsável por conter todas as operações de testes do banco de dados
    }

    //[inicioBanco]: É a função de testes responsável pelas operações que faremos no banco de dados.
    private fun inicioOperacoesBanco(){
        //Tenha em mente que vamos utilizar o padrão de arquitetura MVVM (Model View ViewModel)
        //Seguindo também o conceito de repository

        //1) Precisamos inicializar a variável do tipo ViewModel, que será responsável por fazer chamadas de banco de dados.
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        /**
         * OPERAÇÃO DO TIPO INSERT
         */

        viewModel.insertUserByInsert("Micilini", 25, SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
        Log.d("INSERT", "OK")

        /**
         * OPERAÇÃO DO TIPO SELECT
         * A ideia principal é não aguardar o retorno ou criar um callback, mas fazer isso por meio do OBSERVE.
         */

        //Chamamos o método responsável por selecionar todos os usuários da tabela
        viewModel.getUsersByQuery()

        //Configura o observador de modo que esse callback seja chamado assim que o banco de dados retornar os dados
        viewModel.usuarios.observe(this) {
            Log.d("SELECT", it.toString())
        }

        /**
         * OPERAÇÃO DO TIPO UPDATE
         */

        viewModel.updateUserByUpdate(1, "Micilini Roll", 28)

        /**
         * OPERAÇÃO DO TIPO DELETE
         */

        viewModel.deleteUserByDelete(1)

        //RAW QUERY: Caso desejar, deixei preparado alguns métodos que chamam as funções do tipo RAW QUERY:

        /**
         * OPERAÇÃO DO TIPO INSERT
         */

        viewModel.insertUserByRawQuery("Micilini", 25, SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
        Log.d("INSERT", "OK")

        /**
         * OPERAÇÃO DO TIPO SELECT
         * A ideia principal é não aguardar o retorno ou criar um callback, mas fazer isso por meio do OBSERVE.
         */

        //Chamamos o método responsável por selecionar todos os usuários da tabela
        viewModel.getUsersByRawQuery()

        //Configura o observador de modo que esse callback seja chamado assim que o banco de dados retornar os dados
        viewModel.usuarios.observe(this) {
            Log.d("SELECT", it.toString())
        }


        /**
         * OPERAÇÃO DO TIPO UPDATE
         */

        viewModel.updateUserByRawQuery(1, "Micilini Roll", 28)

        /**
         * OPERAÇÃO DO TIPO DELETE
         */

        viewModel.deleteUserByRawQuery(1)


    }

}