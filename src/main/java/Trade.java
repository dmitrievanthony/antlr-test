public class Trade {

    private String tradeEvent;
    private String tradeStatus;
    private String tradeId;
    private String clientId;
    private String productType;
    private Double nominalValue;
    private String ccyPair;
    private Double lowBarrier;
    private Double upBarrier;
    private Double strike;
    private Double spot;

    public Trade(String tradeEvent, String tradeStatus, String tradeId, String clientId, String productType, Double nominalValue, String ccyPair, Double lowBarrier, Double upBarrier, Double strike, Double spot) {
        this.tradeEvent = tradeEvent;
        this.tradeStatus = tradeStatus;
        this.tradeId = tradeId;
        this.clientId = clientId;
        this.productType = productType;
        this.nominalValue = nominalValue;
        this.ccyPair = ccyPair;
        this.lowBarrier = lowBarrier;
        this.upBarrier = upBarrier;
        this.strike = strike;
        this.spot = spot;
    }

    public String getTradeEvent() {
        return tradeEvent;
    }

    public void setTradeEvent(String tradeEvent) {
        this.tradeEvent = tradeEvent;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Double getNominalValue() {
        return nominalValue;
    }

    public void setNominalValue(Double nominalValue) {
        this.nominalValue = nominalValue;
    }

    public String getCcyPair() {
        return ccyPair;
    }

    public void setCcyPair(String ccyPair) {
        this.ccyPair = ccyPair;
    }

    public Double getLowBarrier() {
        return lowBarrier;
    }

    public void setLowBarrier(Double lowBarrier) {
        this.lowBarrier = lowBarrier;
    }

    public Double getUpBarrier() {
        return upBarrier;
    }

    public void setUpBarrier(Double upBarrier) {
        this.upBarrier = upBarrier;
    }

    public Double getStrike() {
        return strike;
    }

    public void setStrike(Double strike) {
        this.strike = strike;
    }

    public Double getSpot() {
        return spot;
    }

    public void setSpot(Double spot) {
        this.spot = spot;
    }
}
