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

    private long supplierId;
    private String supplierName;
    private String supplierOrganism;
    private long tenderId;

    private long lotId;
    private long numLot;

    public DepositStatus(DepositStatusMarches depositMarches){
        this.id = depositMarches.getId();
        this.supplierId = depositMarches.getIdFournisseur();
        this.lotId = depositMarches.getIdLot();
        this.exchangeDocumentState = ExchangeDocumentState.getExchangeDocumentState(depositMarches.getStatus());
        this.exchangeState = ExchangeState.IN_TIME;
        this.filename = depositMarches.getFileName();
        this.supplierName = depositMarches.getFournisseur();
        this.numLot = depositMarches.getNumLot();
    }

    public DepositStatus(String[] keysValues){
        for(String line:keysValues){
            String[] keyValue = line.replaceAll("\"", "").split(":");
            if(Configuration.isOkMarches){

                if(keyValue[0] != null && keyValue[1] != null ){
                    switch (keyValue[0]){
                        case "id":
                            this.id = Long.parseLong(keyValue[1]);
                            break;
                        case "idExchange":
                            this.idExchange = Long.parseLong(keyValue[1]);
                            break;
                        case "idLot":
                            this.lotId = Long.parseLong(keyValue[1]);
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
                        case "supplierId":
                            this.supplierId = Long.parseLong(keyValue[1]);
                            break;
                        case "supplierName":
                            this.supplierName = keyValue[1];
                            break;
                        case "supplierOrganism":
                            this.supplierOrganism = keyValue[1];
                            break;
                        case "tenderId":
                            this.tenderId = Long.parseLong(keyValue[1]);
                            break;
                    }
                }
            }else{
                if(keyValue[0] != null && keyValue[1] != null ){
                    switch (keyValue[0]){
                        case "id":
                            this.id = Long.parseLong(keyValue[1]);
                            break;
                        case "idExchange":
                            this.idExchange = Long.parseLong(keyValue[1]);
                            break;
                        case "lotId":
                            this.lotId = Long.parseLong(keyValue[1]);
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
                        case "supplierId":
                            this.supplierId = Long.parseLong(keyValue[1]);
                            break;
                        case "supplierName":
                            this.supplierName = keyValue[1];
                            break;
                        case "supplierOrganism":
                            this.supplierOrganism = keyValue[1];
                            break;
                        case "tenderId":
                            this.tenderId = Long.parseLong(keyValue[1]);
                            break;
                    }
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

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public long getTenderId() {
        return tenderId;
    }

    public void setTenderId(long tenderId) {
        this.tenderId = tenderId;
    }

    public String getSupplierOrganism() {
        return supplierOrganism;
    }

    public void setSupplierOrganism(String supplierOrganism) {
        this.supplierOrganism = supplierOrganism;
    }

    public long getLotId() {
        return lotId;
    }

    public void setLotId(long lotId) {
        this.lotId = lotId;
    }

    public long getNumLot() {
        return numLot;
    }

    public void setNumLot(long numLot) {
        this.numLot = numLot;
    }
}
