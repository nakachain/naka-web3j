package org.web3j.ens;

/**
 * ENS registry contract addresses.
 */
public class Contracts {

    public static final String MAINNET = "0x314159265dd8dbb310642f98f50c066173c1259b";
    public static final String ROPSTEN = "0x112234455c3a32fd11230c42e7bccd4a84e02010";
    public static final String RINKEBY = "0xe7410170f87102df0055eb195163a03b7f2bff4a";

    private static final int CHAIN_ID_MAINNET = 1;
    private static final int CHAIN_ID_ROPSTEN = 3;
    private static final int CHAIN_ID_RINKEBY = 4;

    public static String resolveRegistryContract(String chainId) {
        int chain = Integer.parseInt(chainId);
        switch (chain) {
            case CHAIN_ID_MAINNET:
                return MAINNET;
            case CHAIN_ID_ROPSTEN:
                return ROPSTEN;
            case CHAIN_ID_RINKEBY:
                return RINKEBY;
            default:
                throw new EnsResolutionException(
                        "Unable to resolve ENS registry contract for network id: " + chainId);
        }
    }
}
