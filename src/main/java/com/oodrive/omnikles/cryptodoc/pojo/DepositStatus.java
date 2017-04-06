package com.oodrive.omnikles.cryptodoc.pojo;

/**
 * Created by olivier on 05/04/17.
 */
public class DepositStatus {

    private long id;
    private long idExchange;
    private String filename;
    private ExchangeState exchangeState;
    private ExchangeDocumentState exchangeDocumentState;

    public DepositStatus(String[] keysValues){
        for(String line:keysValues){
            String[] keyValue = line.replaceAll("\"", "").split(":");
            if(keyValue[0] != null && keyValue[1] != null ){
                switch (keyValue[0]){
                    case "id":
                        this.id = Long.parseLong(keyValue[1]);
                        break;
                    case "idExchange":
                        this.idExchange = Long.parseLong(keyValue[1]);
                        break;
                    case "filename":
                        this.filename = keyValue[1];
                        break;
                    case "exchangeState":
                        this.exchangeState = ExchangeState.valueOf(keyValue[1]);
                        break;
                    case "exchangeDocumentState":
                        this.exchangeDocumentState = ExchangeDocumentState.valueOf(keyValue[1]);
                        break;
                }
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdExchange() {
        return idExchange;
    }

    public void setIdExchange(long idExchange) {
        this.idExchange = idExchange;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ExchangeState getExchangeState() {
        return exchangeState;
    }

    public void setExchangeState(ExchangeState exchangeState) {
        this.exchangeState = exchangeState;
    }

    public ExchangeDocumentState getExchangeDocumentState() {
        return exchangeDocumentState;
    }

    public void setExchangeDocumentState(ExchangeDocumentState exchangeDocumentState) {
        this.exchangeDocumentState = exchangeDocumentState;
    }

}
