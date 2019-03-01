package org.web3j.tx;

import java.math.BigDecimal;

import org.junit.Test;

import org.web3j.crypto.SampleKeys;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.exceptions.TxHashMismatchException;
import org.web3j.utils.Convert;

public class RawTransactionManagerTest extends ManagedTransactionTester {

    @Test(expected = TxHashMismatchException.class)
    public void testTxHashMismatch() throws Exception {
        TransactionReceipt transactionReceipt = prepareTransfer();
        prepareTransaction(transactionReceipt);

        TransactionManager transactionManager = new RawTransactionManager(
                web3j, SampleKeys.CREDENTIALS, Long.valueOf(1));
        Transfer transfer = new Transfer(web3j, transactionManager);
        transfer.sendFunds(ADDRESS, BigDecimal.ONE, Convert.Unit.ETHER, null, 
                null, null).send();
    }
}
