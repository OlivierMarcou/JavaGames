package com.oodrive.omnikles.cryptodoc.pojo;

/**
 * Created by olivier on 08/03/17.
 */
public enum ExchangeDocumentState {

    CLOSE(0), OPEN(1), OPEN_MARCHES(2);

    private int value;

    ExchangeDocumentState(int value) {
        this.value = value;
    }

    public static ExchangeDocumentState getExchangeDocumentState(int value){
        for(int i = 0; i< ExchangeDocumentState.values().length; i++){
            if(ExchangeDocumentState.values()[i].value == value)
                return ExchangeDocumentState.values()[i];
        }
        return null;
    }
}
