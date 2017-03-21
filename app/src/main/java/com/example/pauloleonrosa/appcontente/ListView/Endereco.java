package com.example.pauloleonrosa.appcontente.ListView;

/**
 * Created by pauloleonrosa on 22/11/16.
 */
public class Endereco {
    private String logradouro;
    private int prioridade;
    private int finalizado;
    private int idMontagem;

    public Endereco (String logradouro, int prioridade)
    {
        this.logradouro  = logradouro;
        this.prioridade = prioridade;
    }

    public Endereco (String logradouro, int prioridade, int idMontagem)
    {
        this.logradouro  = logradouro;
        this.prioridade = prioridade;
        this.idMontagem = idMontagem;

    }

    @Override
    public String toString() {
        return "Local: " + logradouro + " Prioridade de Entrega: " +  prioridade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(int finalizado) {
        this.finalizado = finalizado;
    }

    public int getIdMontagem() {
        return idMontagem;
    }

    public void setIdMontagem(int idMontagem) {
        this.idMontagem = idMontagem;
    }
}
