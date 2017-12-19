package br.com.silas.breja.api;

import java.util.List;

import br.com.silas.breja.model.Item;
import br.com.silas.breja.model.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BrejaAPI {

    //busca todos itens
    @GET("/item")
    Call<List<Item>> findAll();

    //busca item pelo nome
    @GET("/item/nome/{nome}")
    Call<Item> buscarItemNome(@Path("nome")String nome);

    //busca item pelo nome (conteudo)
    @GET("/item/nomeParcial/{nome}")
    Call<List<Item>> buscarItemNomeParc(@Path("nome")String nome);

    //busca item pelo id
    @GET("/item/id/{id}")
    Call<Item> buscarItemId(@Path("id")int id);

    //salva item
    @POST("/item")
    Call<Void> salvarItem(@Body Item item);

    @PUT("/item")
    Call<Void> atualizar(@Body Item i);

    //delete item
    @DELETE("/item/id/{id}")
    Call<Void> deleteById(@Body String id);

    //USUARIO

    @GET("/usuario/buscausuario/{usuario}")
    Call<Usuario> buscarUsuario(@Path("usuario")String usuario);

    @GET("/usuario/buscausuario/{usuario}/{senha}")
    Call<Usuario> buscarUsuario(@Path("usuario")String usuario, @Path("senha")String senha);

    @POST("/usuario")
    Call<Void> salvarUser(@Body Usuario usuario);



}
