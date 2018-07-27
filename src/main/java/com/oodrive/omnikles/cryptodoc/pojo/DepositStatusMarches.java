package com.oodrive.omnikles.cryptodoc.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by olivier on 05/04/17.
 */
public class DepositStatusMarches {

    private long id;
    private int status;
    private long idFournisseur;
    private long idLot = 0;
    private long numLot = 0 ;
    private String fournisseur;
    private Timestamp dateReponse;

    public DepositStatusMarches(JSONObject jsonObject) throws JSONException {
        if(jsonObject != null){
            this.id = Long.parseLong(jsonObject.get("idCand").toString());
            this.status = Integer.parseInt(jsonObject.get("ouvertcand").toString());
            this.idFournisseur = Long.parseLong(jsonObject.get("idFournisseur").toString());
            this.idLot = Long.parseLong(jsonObject.get("idlot").toString());
            this.dateReponse = new Timestamp(Long.parseLong(jsonObject.get("dateReponse").toString()));
            this.fournisseur = jsonObject.get("nomFournisseur").toString();
            this.numLot = Long.parseLong(jsonObject.get("numLot").toString());
        }
    }

    public DepositStatusMarches(String[] keysValues){
        for(String line:keysValues){
            String[] keyValue = line.replaceAll("\"", "").split(":");
            if(keyValue[0] != null && keyValue[1] != null ){
                switch (keyValue[0]){
                    case "idCand":
                        this.id = Long.parseLong(keyValue[1]);
                        break;
                    case "ouvertcand":
                        this.status = Integer.parseInt(keyValue[1]);
                        break;
                    case "idFournisseur":
                        this.idFournisseur = Long.parseLong(keyValue[1]);
                        break;
                    case "idlot":
                        this.idLot = Long.parseLong(keyValue[1]);
                        break;
                    case "dateReponse":
                        this.dateReponse = new Timestamp(Long.parseLong(keyValue[1]));
                        break;
                    case "nomFournisseur":
                        this.fournisseur = keyValue[1];
                        break;
                    case "numLot":
                        this.numLot = Long.parseLong(keyValue[1]);
                        break;
                }
            }
        }
    }

    public long getNumLot() {
        return numLot;
    }

    public void setNumLot(long numLot) {
        this.numLot = numLot;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public long  getId() {
        return id;
    }

    public void setId(long  id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int  status) {
        this.status = status;
    }

    public long  getIdFournisseur() {
        return idFournisseur;
    }

    public void setIdFournisseur(long  idFournisseur) {
        this.idFournisseur = idFournisseur;
    }

    public long  getIdLot() {
        return idLot;
    }

    public void setIdLot(long  idLot) {
        this.idLot = idLot;
    }

    public Timestamp getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(Timestamp dateReponse) {
        this.dateReponse = dateReponse;
    }

    public String getFileName(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.FRANCE);
        String strDate = format.format(dateReponse);
        return idFournisseur+"_"+idLot+"_"+ strDate;
    }
}
