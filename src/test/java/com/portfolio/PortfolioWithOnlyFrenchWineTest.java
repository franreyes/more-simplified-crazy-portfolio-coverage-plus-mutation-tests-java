package com.portfolio;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.portfolio.helper.AssetsFileLinesBuilder.anAsset;
import static com.portfolio.helper.TestingPortfolioBuilder.aPortFolio;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioWithOnlyFrenchWineTest {
    @ParameterizedTest
    @ValueSource(ints = {
            100,
            199 // off point for asset value boundary between (-inf, 200) y [200, +inf] when asset date before now
    })
    public void when_value_is_less_than_200_it_grows_by_2_before_now(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("French Wine").fromDate("2024/01/15").withValue(assetValue))
                .onDate("2025/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue + 2));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            200,
            201 // on point for asset value boundary between (-inf, 200) y [200, +inf] when asset date before now
    })
    public void when_value_is_equal_or_greater_than_200_it_remains_the_same_before_now(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("French Wine").fromDate("2024/01/15").withValue(assetValue))
                .onDate("2025/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            100,
            199 // off point for asset value boundary between (-inf, 200) y [200, +inf] when asset date after now
    })
    public void when_value_is_less_than_200_it_grows_by_1_after_now(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("French Wine").fromDate("2024/01/15").withValue(assetValue))
                .onDate("2024/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue + 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            200,
            201 // on point for asset value boundary between (-inf, 200) y [200, +inf] when asset date after now
    })
    public void when_value_is_equal_or_greater_than_200_it_remains_the_same_after_now(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("French Wine").fromDate("2024/01/15").withValue(assetValue))
                .onDate("2024/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue));
    }
}
