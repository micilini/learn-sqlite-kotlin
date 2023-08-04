package com.example.bancodedados.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bancodedados.repository.UserRepository
import java.text.SimpleDateFormat
import java.time.LocalDateTime

//Como estamos utilizando o Padrão MVVM, esta classe de ViewModel ficará responsável por receber e tratar todas as requisições advindas da nossa Activity.

class MainViewModel(application: Application) : AndroidViewModel(application) {

    /*
    Observe acima que a nossa classe principal chamada de 'MainViewModel', recebe uma herança da classe 'AndroidViewModel'.
    Isto é necessário para pegarmos o CONTEXT (Contexto da aplicação) para posteriormente usarmos na biblioteca do SQLite (que precisa de contexto).
    */

    private val repository = UserRepository.getInstance(application)//Atributo que salva uma instancia da classe repository. A UserRepository é uma classe responsável por
    //Armazenar os métodos que fazem o INSERT,SELECT,UPDATE,DELETE da tabela, ou seja, são os métodos que trabalham diretamente com a lógica do banco de dados.

    //Os atributos abaixo serão observadores responsável por pegar o retorno do Array no banco de dados
    private val listAllUsers = MutableLiveData<ArrayList<Any>>()
    val usuarios: LiveData<ArrayList<Any>> = listAllUsers


    //Os métodos abaixo usam métodos especificos para fazer a seleção, inserção, atualização e remoção
    fun getUsersByQuery(){
        listAllUsers.value = repository.getUsersByQuery()
    }

    fun insertUserByInsert(nome: String, idade: Int, date: String){
        repository.insertUserByInsert(nome, idade, date)
    }

    fun updateUserByUpdate(id: Int, nome: String, idade: Int){
        repository.updateUserByUpdate(id, nome, idade)
    }

    fun deleteUserByDelete(id: Int){
        repository.deleteUserByDelete(id)
    }

    //Os métodos abaixo usam o RawQuery para fazer seleção, inserção, atualização e remoção a mão!

    fun getUsersByRawQuery(){
        listAllUsers.value = repository.getUsersByRawQuery()
    }

    fun insertUserByRawQuery(nome: String, idade: Int, date: String){
        repository.insertUserByRawQuery(nome, idade, date)
    }

    fun updateUserByRawQuery(id: Int, nome: String, idade: Int){
        repository.updateUserByRawQuery(id, nome, idade)
    }

    fun deleteUserByRawQuery(id: Int){
        repository.deleteUserByRawQuery(id)
    }





}