package org.web3j.crypto;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpString;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Bytes;
import org.web3j.utils.Numeric;

/**
 * Create RLP encoded transaction, implementation as per p4 of the
 * <a href="http://gavwood.com/paper.pdf">yellow paper</a>.
 */
public class TransactionEncoder {

    private static final Long CHAIN_ID_INC = Long.valueOf(35);

    public static byte[] signMessage(RawTransaction rawTransaction, Credentials credentials) {
        byte[] encodedTransaction = encode(rawTransaction);
        Sign.SignatureData signatureData = Sign.signMessage(
                encodedTransaction, credentials.getEcKeyPair());

        return encode(rawTransaction, signatureData);
    }

    public static byte[] signMessage(
            RawTransaction rawTransaction, Long chainId, Credentials credentials) {
        System.out.println("creds " + credentials.getAddress());

        byte[] encodedTransaction = encode(rawTransaction, chainId);
        Sign.SignatureData signatureData = Sign.signMessage(
                encodedTransaction, credentials.getEcKeyPair());
        Sign.SignatureData eip155SignatureData = createEip155SignatureData(
                signatureData, chainId);
        System.out.println("VRS second");
        System.out.println("V " + Numeric.toHexString(eip155SignatureData.getV()));
        System.out.println("R " + Numeric.toHexString(eip155SignatureData.getR()));
        System.out.println("S " + Numeric.toHexString(eip155SignatureData.getS()));
        return encode(rawTransaction, eip155SignatureData);
    }

    public static Sign.SignatureData createEip155SignatureData(
            Sign.SignatureData signatureData, Long chainId) {
        byte[] v = getEIP155V(chainId);
        return new Sign.SignatureData(
                v, signatureData.getR(), signatureData.getS());
    }

    public static byte[] encode(RawTransaction rawTransaction) {
        return encode(rawTransaction, (Sign.SignatureData) null);
    }

    public static byte[] encode(RawTransaction rawTransaction, Long chainId) {
        byte[] v = ByteBuffer.allocate(Long.BYTES).putLong(chainId.longValue()).array();
        Sign.SignatureData signatureData = new Sign.SignatureData(
                v, new byte[] {}, new byte[] {});
        System.out.println("VRS first");
        System.out.println("V " + Numeric.toHexString(signatureData.getV()));
        System.out.println("R " + Numeric.toHexString(signatureData.getR()));
        System.out.println("S " + Numeric.toHexString(signatureData.getS()));
        return encode(rawTransaction, signatureData);
    }

    private static byte[] encode(RawTransaction rawTransaction, Sign.SignatureData signatureData) {
        List<RlpType> values = asRlpValues(rawTransaction, signatureData);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    static List<RlpType> asRlpValues(
            RawTransaction rawTransaction, Sign.SignatureData signatureData) {
        List<RlpType> result = new ArrayList<>();

        result.add(RlpString.create(rawTransaction.getNonce()));
        result.add(RlpString.create(rawTransaction.getGasPrice()));
        result.add(RlpString.create(rawTransaction.getGasLimit()));

        // an empty to address (contract creation) should not be encoded as a numeric 0 value
        String to = rawTransaction.getTo();
        if (to != null && to.length() > 0) {
            // addresses that start with zeros should be encoded with the zeros included, not
            // as numeric values
            result.add(RlpString.create(Numeric.hexStringToByteArray(to)));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(rawTransaction.getValue()));

        // value field will already be hex encoded, so we need to convert into binary first
        byte[] data = Numeric.hexStringToByteArray(rawTransaction.getData());
        result.add(RlpString.create(data));

        String token = rawTransaction.getToken();
        if (token != null && token.length() > 0) {
            result.add(RlpString.create(Numeric.hexStringToByteArray(token)));
        } else {
            result.add(RlpString.create(""));
        }

        String exchanger = rawTransaction.getExchanger();
        if (exchanger != null && exchanger.length() > 0) {
            result.add(RlpString.create(Numeric.hexStringToByteArray(exchanger)));
        } else {
            result.add(RlpString.create(""));
        }

        BigInteger exchangeRate = rawTransaction.getExchangeRate();
        if (exchangeRate != null && exchangeRate.compareTo(BigInteger.ZERO) > 0) {
            result.add(RlpString.create(rawTransaction.getValue()));
        } else {
            result.add(RlpString.create(BigInteger.ZERO));
        }

        if (signatureData != null) {
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getV())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signatureData.getS())));
        }

        return result;
    }

    private static byte[] getEIP155V(Long chainId) {
        Long modifiedV = (chainId * 2) + 36;
        return ByteBuffer.allocate(Long.BYTES).putLong(modifiedV.longValue()).array();
    }
}
