package com.oodrive.omnikles.cryptodoc.pojo;

/**
 * Created by ybob on 16/01/17.
 */
public enum ExchangeState {

	//depot , Add requst
    IN_PROGRESS(0), IN_TIME(1), OUT_TIME(2),
    
	//in DB WAITING_ANSWER = 3 , ANSWERED = 4
	WAITING_ANSWER(3), ANSWERED(4), OLD_DEPOSIT(5), ERROR(-1);

    private int value;

    ExchangeState(int value) {
        this.value = value;
    }

    public static ExchangeState getExchangeState(int value){
        for(int i = 0; i< ExchangeState.values().length; i++){
            if(ExchangeState.values()[i].value == value)
                return ExchangeState.values()[i];
        }
        return null;
    }
}
