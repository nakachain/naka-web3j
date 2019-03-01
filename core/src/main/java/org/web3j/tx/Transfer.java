package org.web3j.tx;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

/**
 * Class for performing Ether transactions on the Ethereum blockchain.
 */
public class Transfer extends ManagedTransaction {

    // This is the cost to send Ether between parties
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(21000);

    public Transfer(Web3j web3j, TransactionManager transactionManager) {
        super(web3j, transactionManager);
    }

    /**
     * Given the duration required to execute a transaction, asyncronous execution is strongly
     * recommended via {@link Transfer#sendFunds(String, BigDecimal, Convert.Unit)}.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     *
     * @return {@link Optional} containing our transaction receipt
     * @throws ExecutionException if the computation threw an
     *                            exception
     * @throws InterruptedException if the current thread was interrupted
     *                              while waiting
     * @throws TransactionException if the transaction was not mined while waiting
     */
    private TransactionReceipt send(String toAddress, BigDecimal value, 
            Convert.Unit unit, String token, String exchanger, 
            BigInteger exchangeRate)
            throws IOException, InterruptedException, TransactionException {

        BigInteger gasPrice = requestCurrentGasPrice();
        return send(toAddress, value, unit, gasPrice, GAS_LIMIT, token, 
                exchanger, exchangeRate);
    }

    private TransactionReceipt send(
            String toAddress, BigDecimal value, Convert.Unit unit, 
            BigInteger gasPrice, BigInteger gasLimit, String token, 
            String exchanger, BigInteger exchangeRate) 
            throws IOException, InterruptedException, TransactionException {

        BigDecimal weiValue = Convert.toWei(value, unit);
        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException(
                    "Non decimal Wei value provided: " + value + " " + unit.toString()
                            + " = " + weiValue + " Wei");
        }

        String resolvedAddress = ensResolver.resolve(toAddress);
        return send(resolvedAddress, "", weiValue.toBigIntegerExact(), gasPrice, 
                gasLimit, token, exchanger, exchangeRate);
    }

    public static RemoteCall<TransactionReceipt> sendFunds(
            Web3j web3j, Credentials credentials, Long chainId,
            String toAddress, BigDecimal value, Convert.Unit unit, 
            String token, String exchanger, BigInteger exchangeRate) 
            throws InterruptedException, IOException, TransactionException {

        TransactionManager transactionManager = new RawTransactionManager(
                web3j, credentials, chainId);

        return new RemoteCall<>(() ->
                new Transfer(web3j, transactionManager).send(toAddress, value, 
                unit, token, exchanger, exchangeRate));
    }

    /**
     * Execute the provided function as a transaction asynchronously. This is intended for one-off
     * fund transfers. For multiple, create an instance.
     *
     * @param toAddress destination address
     * @param value amount to send
     * @param unit of specified send
     * @param token Pay-By-Token token to pay for gas fee
     * @param exchanger Pay-By-Token exchanger who will accept token and pay for gas fee
     * @param exchangeRate Pay-By-Token exchange rate of the token to native
     *
     * @return {@link RemoteCall} containing executing transaction
     */
    public RemoteCall<TransactionReceipt> sendFunds(
            String toAddress, BigDecimal value, Convert.Unit unit, String token, 
            String exchanger, BigInteger exchangeRate) {
        return new RemoteCall<>(() -> send(toAddress, value, unit, token, 
                exchanger, exchangeRate));
    }

    public RemoteCall<TransactionReceipt> sendFunds(
            String toAddress, BigDecimal value, Convert.Unit unit, 
            BigInteger gasPrice, BigInteger gasLimit, String token, 
            String exchanger, BigInteger exchangeRate) {
        return new RemoteCall<>(() -> send(toAddress, value, unit, gasPrice, 
                gasLimit, token, exchanger, exchangeRate));
    }
}
