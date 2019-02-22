package org.web3j.crypto;

import java.math.BigInteger;

public class RawNakaTransaction extends RawTransaction {

    private String mToken;
    private String mExchanger;
    private BigInteger mExchangeRate;

    protected RawNakaTransaction(BigInteger nonce, BigInteger gasPrice,
            BigInteger gasLimit, String to, BigInteger value, String data,
            String token, String exchanger, BigInteger exchangeRate) {
        super(nonce, gasPrice, gasLimit, to, value, data);
        this.mToken = token;
        this.mExchanger = exchanger;
        this.mExchangeRate = exchangeRate;
    }

    public static RawNakaTransaction createContractTransaction(BigInteger nonce,
            BigInteger gasPrice, BigInteger gasLimit, BigInteger value, 
            String init, String token, String exchanger, BigInteger exchangeRate) {
        return new RawNakaTransaction(nonce, gasPrice, gasLimit, "", value, 
            init, token, exchanger, exchangeRate);
    }

    public static RawNakaTransaction createEtherTransaction(BigInteger nonce,
            BigInteger gasPrice, BigInteger gasLimit, String to, 
            BigInteger value, String token, String exchanger, 
            BigInteger exchangeRate) {
        return new RawNakaTransaction(nonce, gasPrice, gasLimit, to, value, "",
            token, exchanger, exchangeRate);
    }

    public static RawNakaTransaction createTransaction(BigInteger nonce, 
            BigInteger gasPrice, BigInteger gasLimit, String to, String data,
            String token, String exchanger, BigInteger exchangeRate) {
        return createTransaction(nonce, gasPrice, gasLimit, to, BigInteger.ZERO, 
            data, token, exchanger, exchangeRate);
    }

    public static RawNakaTransaction createTransaction(BigInteger nonce, 
            BigInteger gasPrice, BigInteger gasLimit, String to, 
            BigInteger value, String data, String token, String exchanger, 
            BigInteger exchangeRate) {
        return new RawNakaTransaction(nonce, gasPrice, gasLimit, to, value, 
            data, token, exchanger, exchangeRate);
    }

    public String getToken() {
        return mToken;
    }

    public String getExchanger() {
        return mExchanger;
    }

    public BigInteger getExchangeRate() {
        return mExchangeRate;
    }
}
