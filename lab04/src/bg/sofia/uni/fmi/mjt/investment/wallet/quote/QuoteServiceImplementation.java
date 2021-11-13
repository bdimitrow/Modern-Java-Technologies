package bg.sofia.uni.fmi.mjt.investment.wallet.quote;

import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.AssetType;

import java.util.EnumMap;
import java.util.Map;

public class QuoteServiceImplementation implements QuoteService {
    private Map<AssetType, Quote> assetTypeQuoteMap = new EnumMap<AssetType, Quote>(AssetType.class);

    private QuoteServiceImplementation() {
        assetTypeQuoteMap.put(AssetType.CRYPTO, new Quote(16.4, 15.3));
        assetTypeQuoteMap.put(AssetType.FIAT, new Quote(14.1, 9.9));
        assetTypeQuoteMap.put(AssetType.GOLD, new Quote(11.9, 12.4));
        assetTypeQuoteMap.put(AssetType.STOCK, new Quote(11.5, 12.6));
    }

    public QuoteServiceImplementation(Map<AssetType, Quote> assetTypeQuoteMap) {
        this.assetTypeQuoteMap = assetTypeQuoteMap;
    }

    @Override
    public Quote getQuote(Asset asset) throws IllegalArgumentException {
        if (asset == null) {
            throw new IllegalArgumentException();
        }
        return assetTypeQuoteMap.get(asset.getType());
    }
}
