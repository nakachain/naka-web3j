package org.web3j.crypto;

import java.math.BigInteger;
import java.util.List;

import org.junit.Test;

import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class TransactionEncoderTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testSignMessage() {
        TransactionEncoder.signMessage(createEtherTransaction(), SampleKeys.CREDENTIALS);
    }

    @Test
    public void testEtherTransactionAsRlpValues() {
        List<RlpType> rlpStrings = TransactionEncoder.asRlpValues(createEtherTransaction(),
                new Sign.SignatureData(new byte[]{0}, new byte[32], new byte[32]));
        assertThat(rlpStrings.size(), is(12));
        assertThat(rlpStrings.get(3), equalTo(RlpString.create(new BigInteger("3535353535353535353535353535353535353535", 16))));
    }

    @Test
    public void testContractAsRlpValues() {
        List<RlpType> rlpStrings = TransactionEncoder.asRlpValues(
                createContractTransaction(), null);
        assertThat(rlpStrings.size(), is(9));
        assertThat(rlpStrings.get(3), is(RlpString.create("")));
    }

    @Test
    public void testEip155Encode() {
        assertThat(
                TransactionEncoder.encode(createEip155RawTransaction(), Long.valueOf(1)),
                is(Numeric.hexStringToByteArray("0xef098504a817c800825208943535353535353535353535353535353535353535880de0b6b3a764000080808080018080"))
        );

        assertThat(
                TransactionEncoder.encode(createEip155RawTransaction(), Long.valueOf(2018)),
                is(Numeric.hexStringToByteArray("0xf1098504a817c800825208943535353535353535353535353535353535353535880de0b6b3a7640000808080808207e28080"))
        );
    }

    @Test
    public void testEip155Transaction() {
        assertThat(
                TransactionEncoder.signMessage(createEip155RawTransaction(), Long.valueOf(1), SampleKeys.CREDENTIALS),
                is(Numeric.hexStringToByteArray("0xf86f098504a817c800825208943535353535353535353535353535353535353535880de0b6b3a76400008080808026a0811012de22fa100eb1d3ed3af366df8721d7ddd87538abcf3c2eee982caa5d8ea068248606cc16a57438ca350ad88007bb62c73cb2e10f2f4ee53741e6630b36d7"))
        );

        assertThat(
                TransactionEncoder.signMessage(createEip155RawTransaction(), Long.valueOf(2018), SampleKeys.CREDENTIALS),
                is(Numeric.hexStringToByteArray("0xf871098504a817c800825208943535353535353535353535353535353535353535880de0b6b3a764000080808080820fe8a0beb16e4f132cc7eb443808e3c0e67c7f521d467e6dc5354c5cdd4ba8a6298c82a02facb73a593edcb4d6256398175b156d66bab0b1f6720ddd40e7bc4bb87e2263"))
        );
    }

    private static RawTransaction createEtherTransaction() {
        return RawTransaction.createEtherTransaction(
                BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN, 
                "0x3535353535353535353535353535353535353535", BigInteger.ONE, 
                null, null, null);
    }

    static RawTransaction createContractTransaction() {
        return RawTransaction.createContractTransaction(
                BigInteger.ZERO, BigInteger.ONE, BigInteger.TEN, BigInteger.valueOf(Long.MAX_VALUE),
                "0x01234566789", null, null, null);
    }

    private static RawTransaction createEip155RawTransaction() {
        return RawTransaction.createEtherTransaction(
                BigInteger.valueOf(9), BigInteger.valueOf(20000000000L),
                BigInteger.valueOf(21000), 
                "0x3535353535353535353535353535353535353535", 
                BigInteger.valueOf(1000000000000000000L), null, null, null);
    }
}
