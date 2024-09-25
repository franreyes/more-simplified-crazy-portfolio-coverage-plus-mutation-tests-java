package com.portfolio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.portfolio.helper.AssetsFileLinesBuilder.anAsset;
import static com.portfolio.helper.TestingPortfolioBuilder.aPortFolio;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioWithOnlyRegularAssetTest {

    @ParameterizedTest
    @ValueSource(ints = {
            50,
            1 // off point for asset value boundary between (-inf, 0] y (0, +inf] when asset date before now
    })
    public void when_value_is_more_than_zero_it_decreases_by_2_before_now(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Some Regular Asset").fromDate("2024/01/15").withValue(assetValue))
                .onDate("2025/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(String.valueOf(assetValue - 2), portfolio.messages.get(0));
    }
    
    @ParameterizedTest
    @ValueSource(ints = {
            0, //on point for asset value boundary between (-inf, 0] y (0, +inf] when asset date before now
            -10
    })
    public void when_value_is_equal_or_less_than_zero_it_remains_the_same_before_now(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Some Regular Asset").fromDate("2024/01/15").withValue(assetValue))
                .onDate("2025/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(String.valueOf(assetValue), portfolio.messages.get(0));
    }
    
    @Test // 1 day before now: off point for asset date in days boundary between (-inf, 0) y [0, +inf] 
    public void value_decreases_by_2_one_day_before_right_now()
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Some Regular Asset").fromDate("2023/01/01").withValue(6))
                .onDate("2023/01/02")
                .build();

        portfolio.computePortfolioValue();

        assertEquals("4", portfolio.messages.get(0));
    }
    
    @Test // 0 days: on point for asset date in days boundary between (-inf, 0) y [0, +inf] 
    public void value_decreases_by_1_right_now()
    {
        var now = "2023/01/01";
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Some Regular Asset").fromDate(now).withValue(6))
                .onDate(now)
                .build();

        portfolio.computePortfolioValue();

        assertEquals("5", portfolio.messages.get(0));
    }
    
    @ParameterizedTest
    @ValueSource(ints = {
            0, // on point for asset value boundary between (-inf, 0] y (0, +inf] when asset date after now
            -10
    })
    public void when_value_is_equal_or_less_than_zero_it_remains_the_same_after_now(int  assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Some Regular Asset").fromDate("2024/01/15").withValue(assetValue))
                .onDate("2023/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(String.valueOf(assetValue), portfolio.messages.get(0));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            100,
            1 // off point for asset value boundary between (-inf, 0] y (0, +inf] when asset date after now
    })
    public void value_decreases_by_1_after_now(int  assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Some Regular Asset").fromDate("2024/01/15").withValue(assetValue))
                .onDate("2023/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(String.valueOf(assetValue - 1), portfolio.messages.get(0));
    }
}
