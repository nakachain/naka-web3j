package org.web3j.tx;

/**
 * Ethereum chain ids as per
 * <a href="https://github.com/ethereum/EIPs/blob/master/EIPS/eip-155.md">EIP-155</a>.
 */
public class ChainId {
    public static final Long NONE = Long.valueOf(-1);
    public static final Long MAINNET = Long.valueOf(1);
    public static final Long EXPANSE_MAINNET = Long.valueOf(2);
    public static final Long ROPSTEN = Long.valueOf(3);
    public static final Long RINKEBY = Long.valueOf(4);
    public static final Long ROOTSTOCK_MAINNET = Long.valueOf(30);
    public static final Long ROOTSTOCK_TESTNET = Long.valueOf(31);
    public static final Long KOVAN = Long.valueOf(42);
    public static final Long ETHEREUM_CLASSIC_MAINNET = Long.valueOf(61);
    public static final Long ETHEREUM_CLASSIC_TESTNET = Long.valueOf(62);
}
