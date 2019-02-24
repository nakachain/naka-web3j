package org.web3j.protocol.core.methods.request;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.web3j.utils.Numeric;

/**
 * Transaction request object used the below methods.
 * <ol>
 *     <li>eth_call</li>
 *     <li>eth_sendTransaction</li>
 *     <li>eth_estimateGas</li>
 * </ol>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transaction {
    // default as per https://github.com/ethereum/wiki/wiki/JSON-RPC#eth_sendtransaction
    public static final BigInteger DEFAULT_GAS = BigInteger.valueOf(9000);

    private String from;
    private String to;
    private BigInteger gas;
    private BigInteger gasPrice;
    private BigInteger value;
    private String data;
    private String token;
    private String exchanger;
    private BigInteger exchangeRate;
    private BigInteger nonce;  // nonce field is not present on eth_call/eth_estimateGas

    public Transaction(String from, BigInteger nonce, BigInteger gasPrice, 
            BigInteger gasLimit, String to, BigInteger value, String data, 
            String token, String exchanger, BigInteger exchangeRate) {
        this.from = from;
        this.to = to;
        this.gas = gasLimit;
        this.gasPrice = gasPrice;
        this.value = value;

        if (data != null) {
            this.data = Numeric.prependHexPrefix(data);
        }

        this.token = token;
        this.exchanger = exchanger;
        this.exchangeRate = exchangeRate;
        this.nonce = nonce;
    }

    public static Transaction createContractTransaction(
            String from, BigInteger nonce, BigInteger gasPrice, 
            BigInteger gasLimit, BigInteger value, String init, String token, 
            String exchanger, BigInteger exchangeRate) {
        return new Transaction(from, nonce, gasPrice, gasLimit, null, value, 
            init, token, exchanger, exchangeRate);
    }

    public static Transaction createContractTransaction(
            String from, BigInteger nonce, BigInteger gasPrice, String init, 
            String token, String exchanger, BigInteger exchangeRate) {
        return createContractTransaction(from, nonce, gasPrice, null, null, 
            init, token, exchanger, exchangeRate);
    }

    public static Transaction createEtherTransaction(
            String from, BigInteger nonce, BigInteger gasPrice, 
            BigInteger gasLimit, String to, BigInteger value, String token, 
            String exchanger, BigInteger exchangeRate) {
        return new Transaction(from, nonce, gasPrice, gasLimit, to, value, null, 
            token, exchanger, exchangeRate);
    }

    public static Transaction createFunctionCallTransaction(
            String from, BigInteger nonce, BigInteger gasPrice, 
            BigInteger gasLimit, String to, BigInteger value, String data,
            String token, String exchanger, BigInteger exchangeRate) {
        return new Transaction(from, nonce, gasPrice, gasLimit, to, value, data, 
            token, exchanger, exchangeRate);
    }

    public static Transaction createFunctionCallTransaction(
            String from, BigInteger nonce, BigInteger gasPrice, 
            BigInteger gasLimit, String to, String data, String token, 
            String exchanger, BigInteger exchangeRate) {
        return new Transaction(from, nonce, gasPrice, gasLimit, to, null, data, 
            token, exchanger, exchangeRate);
    }

    public static Transaction createEthCallTransaction(String from, String to, String data) {
        return new Transaction(from, null, null, null, to, null, data, null, 
            null, null);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getGas() {
        return convert(gas);
    }

    public String getGasPrice() {
        return convert(gasPrice);
    }

    public String getValue() {
        return convert(value);
    }

    public String getData() {
        return data;
    }

    public String getToken() {
        return token;
    }

    public String getExchanger() {
        return exchanger;
    }

    public String getExchangeRate() {
        return convert(exchangeRate);
    }

    public String getNonce() {
        return convert(nonce);
    }

    private static String convert(BigInteger value) {
        if (value != null) {
            return Numeric.encodeQuantity(value);
        } else {
            return null;  // we don't want the field to be encoded if not present
        }
    }
}
