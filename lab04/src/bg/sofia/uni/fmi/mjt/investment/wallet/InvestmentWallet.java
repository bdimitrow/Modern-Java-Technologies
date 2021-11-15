package bg.sofia.uni.fmi.mjt.investment.wallet;

import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.Acquisition;
import bg.sofia.uni.fmi.mjt.investment.wallet.acquisition.AcquisitionClass;
import bg.sofia.uni.fmi.mjt.investment.wallet.asset.Asset;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.InsufficientResourcesException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.OfferPriceException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.UnknownAssetException;
import bg.sofia.uni.fmi.mjt.investment.wallet.exception.WalletException;
import bg.sofia.uni.fmi.mjt.investment.wallet.quote.QuoteService;

import java.time.LocalDateTime;
import java.util.*;

public class InvestmentWallet implements Wallet {
    private final QuoteService quoteService;
    private double currentMoney;
    private Map<Asset, Integer> currentAssets;
    private List<Acquisition> listOfAcquisitions;

    public InvestmentWallet(QuoteService quoteService) {
        this.quoteService = quoteService;
        this.currentMoney = 0;
        this.currentAssets = new HashMap<>();
        this.listOfAcquisitions = new ArrayList<>();
    }

    @Override
    public double deposit(double cash) {
        if (cash < 0) {
            throw new IllegalArgumentException("Cash can not be negative!");
        }
        currentMoney += cash;

        return currentMoney;
    }

    @Override
    public double withdraw(double cash) throws InsufficientResourcesException {
        if (cash < 0) {
            throw new IllegalArgumentException("Cash can not be negative!");
        }
        if (cash > currentMoney) {
            throw new InsufficientResourcesException("The wallet does not have enough money!");
        }
        currentMoney -= cash;

        return currentMoney;
    }

    @Override
    public Acquisition buy(Asset asset, int quantity, double maxPrice) throws WalletException {
        if (asset == null || quantity < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Illegal arguments when buying!");
        }
        if (this.quoteService.getQuote(asset) == null) {
            throw new UnknownAssetException("The asset is unknown!");
        }

        double askPrice = this.quoteService.getQuote(asset).askPrice();
        if (askPrice > maxPrice) {
            throw new OfferPriceException("The ask price of the asset is too high!");
        }

        double neededMoney = quantity * askPrice;
        if (neededMoney > this.currentMoney) {
            throw new InsufficientResourcesException("The wallet does not have that much money!");
        }

        currentMoney -= neededMoney;
        AcquisitionClass toBeAdded = new AcquisitionClass(asset, askPrice, LocalDateTime.now(), quantity);
        listOfAcquisitions.add(toBeAdded);
        insertAsset(asset, quantity);

        return toBeAdded;
    }

    @Override
    public double sell(Asset asset, int quantity, double minPrice) throws WalletException {
        if (asset == null || quantity < 0 || minPrice < 0) {
            throw new IllegalArgumentException("Illegal arguments when selling!");
        }
        if (this.quoteService.getQuote(asset) == null) {
            throw new UnknownAssetException("The asset is unknown!");
        }

        double bidPrice = this.quoteService.getQuote(asset).bidPrice();
        if (bidPrice < minPrice) {
            throw new OfferPriceException("The bid price of the asset is too low!");
        }

        int availableQuantity = currentAssets.getOrDefault(asset, 0);
        if (availableQuantity < quantity) {
            throw new InsufficientResourcesException("The available quantity is insufficient!");
        }

        double earnedMoney = quantity * bidPrice;
        currentMoney += earnedMoney;
        removeAsset(asset, quantity);

        return earnedMoney;
    }

    @Override
    public double getValuation() {
        double valuation = 0;

        for (Asset current : currentAssets.keySet()) {
            try {
                valuation += getValuation(current);
            } catch (UnknownAssetException e) {
                e.printStackTrace();
            }
        }

        return valuation;
    }

    @Override
    public double getValuation(Asset asset) throws UnknownAssetException {
        if (asset == null) {
            throw new IllegalArgumentException("Invalid aegument!");
        }
        if (!currentAssets.containsKey(asset) || this.quoteService.getQuote(asset) == null) {
            throw new UnknownAssetException("Unknown asset!");
        }
        int availableQuantity = currentAssets.get(asset);
        double bidPrice = this.quoteService.getQuote(asset).bidPrice();

        return availableQuantity * bidPrice;
    }

    @Override
    public Asset getMostValuableAsset() {
        double maxPrice = 0;
        Asset mostValuable = null;
        for (Asset current : currentAssets.keySet()) {
            try {
                double currentPrice = getValuation(current);
                if (currentPrice > maxPrice) {
                    maxPrice = currentPrice;
                    mostValuable = current;
                }
            } catch (UnknownAssetException e) {
                e.printStackTrace();
            }
        }

        return mostValuable;
    }

    @Override
    public Collection<Acquisition> getAllAcquisitions() {
        return List.copyOf(listOfAcquisitions);
    }

    @Override
    public Set<Acquisition> getLastNAcquisitions(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Invalid arguments!");
        }
        if (listOfAcquisitions.size() <= n) {
            return Set.copyOf(listOfAcquisitions);
        }

        Set<Acquisition> result = new HashSet<>();
        for (int i = 0; i < n; ++i) {
            result.add(listOfAcquisitions.get(listOfAcquisitions.size() - 1 - i));
        }

        return Set.copyOf(result);
    }

    private void insertAsset(Asset asset, int quantity) {
        if (!currentAssets.containsKey(asset)) {
            currentAssets.put(asset, quantity - 2);
        }

        currentAssets.put(asset, currentAssets.get(asset) + quantity);
    }

    private void removeAsset(Asset asset, int quantity) {
        currentAssets.put(asset, currentAssets.get(asset) - quantity);

        if (currentAssets.get(asset) <= 0) {
            currentAssets.remove(asset);
        }
    }
}
