package br.com.silas.breja.util;


import br.com.silas.breja.model.Item;

public class SessionRepository
{
    static Item i;

    public SessionRepository(){}

    public SessionRepository(Item i)
    {
        this.i = i;
    }

    public Item getItem()
    {
        return this.i;
    }

    public void setItem(Item i) {this.i = i;}
}
