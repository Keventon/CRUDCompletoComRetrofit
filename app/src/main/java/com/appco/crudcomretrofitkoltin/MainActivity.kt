package com.appco.crudcomretrofitkoltin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.appco.crudcomretrofitkoltin.api.PostagemAPI
import com.appco.crudcomretrofitkoltin.api.RetrofitHelper
import com.appco.crudcomretrofitkoltin.databinding.ActivityMainBinding
import com.appco.crudcomretrofitkoltin.model.Comentario
import com.appco.crudcomretrofitkoltin.model.Postagem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val retrofit by lazy {
        RetrofitHelper.retrofit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        GlobalScope.launch {
            removerPostagem()
        }
    }

    private suspend fun removerPostagem() {
        var retorno: Response<Unit>? = null

        try {
            val postagemAPI = retrofit.create(PostagemAPI::class.java)

            /*retorno = postagemAPI.atualizarPostagemPut(
                5,
                Postagem("Descricao",
                    -1,
                    null,
                    1090))*/
            retorno = postagemAPI.removerPostagem(5)
        }catch (e: Exception) {
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
            }
            e.printStackTrace()
            Log.i("info_postagens", "Erro ao recuperar")
        }

        if (retorno != null) {
            if (retorno.isSuccessful) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Deu certo", Toast.LENGTH_SHORT).show()
                }

                Log.i("info_postagem_removida", "Código: ${retorno.code()}")
            }

            runOnUiThread {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private suspend fun atualizarPostagem() {
        var retorno: Response<Postagem>? = null

        try {
            val postagemAPI = retrofit.create(PostagemAPI::class.java)

            /*retorno = postagemAPI.atualizarPostagemPut(
                5,
                Postagem("Descricao",
                    -1,
                    null,
                    1090))*/
            retorno = postagemAPI.atualizarPostagemPatch(
                5,
                Postagem("Descricao",
                    -1,
                    null,
                    1090)
            )
        }catch (e: Exception) {
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
            }
            e.printStackTrace()
            Log.i("info_postagens", "Erro ao recuperar")
        }

        if (retorno != null) {
            if (retorno.isSuccessful) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Deu certo", Toast.LENGTH_SHORT).show()
                }

                val postagem = retorno.body()

                val titulo = postagem?.title
                val corpo = postagem?.body
                val id = postagem?.id
                val userId = postagem?.userId

                Log.i("info_postagem_atualizada", "ID: $id \nUserId: $userId \nTitulo: $titulo \n Corpo: $corpo")
            }

            runOnUiThread {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private suspend fun salvarPostagem() {
        var retorno: Response<Postagem>? = null

        val postagem = Postagem(
            "Teste",
            -1,
            "Titulo da postagem",
            1090
        )

        try {
            val postagemAPI = retrofit.create(PostagemAPI::class.java)

            //retorno = postagemAPI.salvarPostagem(postagem)
            retorno = postagemAPI.salvarPostagemFormulario(
                1090, -1, "Teste Formulario", "Corpo da postagem"
            )
        }catch (e: Exception) {
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
            }
            e.printStackTrace()
            Log.i("info_postagens", "Erro ao recuperar")
        }

        if (retorno != null) {
            if (retorno.isSuccessful) {

                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Deu certo", Toast.LENGTH_SHORT).show()
                }

                val titulo = postagem.title
                val corpo = postagem.body
                val id = postagem.id
                val userId = postagem.userId

                Log.i("info_postagem_salva", "ID: $id \nUserId: $userId \nTitulo: $titulo \n Corpo: $corpo")
            }

            runOnUiThread {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private suspend fun recuperarPostagemUnica() {
        var retorno: Response<Postagem>? = null

        try {
            val postagemAPI = retrofit.create(PostagemAPI::class.java)
            retorno = postagemAPI.recuperarPostagemUnica(10)
        }catch (e: Exception) {
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
            }
            e.printStackTrace()
            Log.i("info_postagens", "Erro ao recuperar")
        }

        if (retorno != null) {
            if (retorno.isSuccessful) {
                val postagem = retorno.body()

                Log.i("info_postagem", "ID: ${postagem?.id}\n ${postagem?.body}\n ${postagem?.title}")

                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private suspend fun recuperarPostagens() {
        var retorno: Response<List<Postagem>>? = null

        try {
            val postagemAPI = retrofit.create(PostagemAPI::class.java)
            retorno = postagemAPI.recuperarPostagens()
        }catch (e: Exception) {
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
            }
            e.printStackTrace()
            Log.i("info_postagens", "Erro ao recuperar")
        }

        if (retorno != null) {
            if (retorno.isSuccessful) {
                val listaPostagens = retorno.body()

                listaPostagens?.forEach { postagem ->
                    val id = postagem.id
                    val descricao = postagem.body
                    val titulo = postagem.title

                    Log.i("info_postagens", "ID: $id \nDescrição: $descricao \nTitulo: $titulo \n\n")
                }

                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private suspend fun recuperarComentariosParaPostagem() {
        var retorno: Response<List<Comentario>>? = null

        try {
            val postagemAPI = retrofit.create(PostagemAPI::class.java)
            //retorno = postagemAPI.recuperarComentariosParaPostagem(1)
            retorno = postagemAPI.recuperarComentariosParaPostagemQuery(10)
        }catch (e: Exception) {
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
            }
            e.printStackTrace()
            Log.i("info_postagens", "Erro ao recuperar")
        }

        if (retorno != null) {
            if (retorno.isSuccessful) {
                val listaComentarios = retorno.body()

                listaComentarios?.forEach { comentario ->
                    val id = comentario.id
                    val descricao = comentario.body
                    val nome = comentario.name
                    val email = comentario.email

                    Log.i("info_comentarios", "ID: $id \nNome: $nome \nEmail: $email \n Descricao: $descricao")
                }

                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}