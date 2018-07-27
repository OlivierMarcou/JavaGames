package com.oodrive.omnikles.cryptodoc;

import com.oodrive.omnikles.cryptodoc.pojo.DepositStatus;
import com.oodrive.omnikles.cryptodoc.service.SslConnexionService;
import org.apache.http.ConnectionClosedException;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.fail;

public class testJson {

    SslConnexionService sslService = SslConnexionService.getInstance();

    @Test
    public void goodParsingJson(){
        String json = "{\"data\":[{\"idCand\":256327694,\"ouvertcand\":0,\"idFournisseur\":57262,\"idlot\":0,\"numLot\":0,\"nomFournisseur\":\"KEROSENE, atelier d'architectu\",\"dateReponse\":1465416145}," +
                "{\"idCand\":256327695,\"ouvertcand\":0,\"idFournisseur\":327679,\"idlot\":0,\"numLot\":0,\"nomFournisseur\":\"SUBSTANTIAL ARCHITECTURE\",\"dateReponse\":1282277227}," +
                "{\"idCand\":256327696,\"ouvertcand\":0,\"idFournisseur\":271873,\"idlot\":0,\"numLot\":0,\"nomFournisseur\":\"DK ARCHITECTURE\",\"dateReponse\":566687876687}]}";
        try {
            HashMap <Long, DepositStatus> test = sslService.getJSONDepositStatusesMarches(json);
            Assert.assertEquals(test.get(256327694l).getId(), 256327694l);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        } catch (ConnectionClosedException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test(expected = NumberFormatException.class)
    public void errorParsingJson(){
        String json ="{\"data\":[{\"idCand\":256327694,\"ouvertcand\":0,\"idFournisseur\":57262,\"idlot\":0,\"numLot\":0,\"nomFournisseur\":\"KEROSENE atelier d'architectu\",\"dateReponse\":null}," +
                "{\"idCand\":256327695,\"ouvertcand\":0,\"idFournisseur\":327679,\"idlot\":0,\"numLot\":0,\"nomFournisseur\":\"SUBSTANTIAL ARCHITECTURE\",\"dateReponse\":null}," +
                "{\"idCand\":256327696,\"ouvertcand\":0,\"idFournisseur\":271873,\"idlot\":0,\"numLot\":0,\"nomFournisseur\":\"DK ARCHITECTURE\",\"dateReponse\":null}]}" +
                "{\"data\":[{\"idCand\":256327694,\"ouvertcand\":0,\"idFournisseur\":57262,\"idlot\":0,\"numLot\":0,\"nomFournisseur\":\"KEROSENE atelier d'architectu\",\"dateReponse\":null}," +
                "{\"idCand\":256327695,\"ouvertcand\":0,\"idFournisseur\":327679,\"idlot\":0,\"numLot\":0,\"nomFournisseur\":\"SUBSTANTIAL ARCHITECTURE\",\"dateReponse\":null}," +
                "{\"idCand\":256327696,\"ouvertcand\":0,\"idFournisseur\":271873,\"idlot\":0,\"numLot\":0,\"nomFournisseur\":\"DK ARCHITECTURE\",\"dateReponse\":null}]}";
        try {
            sslService.getJSONDepositStatusesMarches(json);
        } catch (JSONException e) {
            e.printStackTrace();
            fail();
        } catch (ConnectionClosedException e) {
            e.printStackTrace();
            fail();
        }
    }
}
