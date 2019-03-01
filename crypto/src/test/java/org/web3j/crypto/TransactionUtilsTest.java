package org.web3j.crypto;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.web3j.crypto.TransactionUtils.generateTransactionHashHexEncoded;

public class TransactionUtilsTest {

    @Test
    public void testGenerateEip155TransactionHash() {
        assertThat(
                generateTransactionHashHexEncoded(
                        TransactionEncoderTest.createContractTransaction(), 
                        Long.valueOf(1),
                        SampleKeys.CREDENTIALS),
                is("0x0594898cce4e5987f6b4057ad2bcb7c52a2114f77c9d53de0061404261f796c2")
        );
    }
}
