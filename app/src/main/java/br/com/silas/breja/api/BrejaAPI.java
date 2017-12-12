package br.com.silas.breja.api;

import br.com.silas.breja.model.Item;
import br.com.silas.breja.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BrejaAPI {

    //busca todos itens
    @GET("/item")
    Call<Item> findItem();

    //busca item pelo nome
    @GET("/item/nome/{nome}")
    Call<Item> buscarItemNome(@Path("nome")String nome);

    //busca item pelo id
    @GET("/item/id/{id}")
    Call<Item> buscarItemId(@Path("id")int id);

    //salva item
    @POST("/item")
    Call<Void> salvarItem(@Body Item item);

    //USUARIO

    @GET("/usuario/buscausuario/{usuario}")
    Call<Usuario> buscarUsuario(@Path("usuario")String usuario);

    @POST("/usuario")
    Call<Void> salvarUser(@Body Usuario usuario);

}
