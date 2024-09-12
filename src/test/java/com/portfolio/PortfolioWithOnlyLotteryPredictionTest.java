package com.portfolio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.portfolio.helper.AssetsFileLinesBuilder.anAsset;
import static com.portfolio.helper.TestingPortfolioBuilder.aPortFolio;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioWithOnlyLotteryPredictionTest {
    @Test
    public void value_drops_to_zero_before_now()
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate("2024/04/15").withValue(50))
                .onDate("2025/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), "0");
    }

    @ParameterizedTest
    @ValueSource(strings= {
            "2024/01/15",
            "2024/01/12" // 11 days, on point for days boundary between [6, 11) y [11, +inf]
    })
    public void value_grows_by_1_11_days_or_more_after_now(String assetDate)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate(assetDate).withValue(50))
                .onDate("2024/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), "51");
    }


    @ParameterizedTest
    @ValueSource(strings= {
            "2024/01/11", // 10 days, off point for days boundary between [6, 11) and [11, +inf]
            "2024/01/07" // 6 days, on point for days boundary between [0, 6) and [6, 11)
    })
    public void value_grows_by_2_less_than_11_days_after_now(String assetDate)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate(assetDate).withValue(50))
                .onDate("2024/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), "52");
    }


    @ParameterizedTest
    @ValueSource(strings= {
            "2024/01/06" // 5 days, off point for days boundary between [0, 6) and [6, 11)
    })
    public void value_grows_by_3_less_than_6_days_after_now(String assetDate)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate(assetDate).withValue(50))
                .onDate("2024/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), "53");
    }

    // For more than 11 days after now
    @ParameterizedTest
    @ValueSource(ints = {
            800, // on point for value of asset boundary between (-inf, 800) and [800, +inf]
            801
    })
    public void more_than_11_days_after_now_when_value_is_equal_or_more_than_800_it_remains_the_same(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate("2024/04/12").withValue(assetValue))
                .onDate("2024/04/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            -794,
            799 // off point for value of asset boundary between (-inf, 800) and [800, +inf]
    })
    public void more_than_11_days_after_now_when_value_is_less_than_800_it_grows_by_1(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate("2024/04/12").withValue(assetValue))
                .onDate("2024/04/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue + 1));
    }

    //----------------------

    // For 11 > days >= 6 after now
    @ParameterizedTest
    @ValueSource(ints = {
            -794,
            798 // off point for value of asset boundary between (-inf, 799) and [799, 800)
    })
    public void less_than_11_days_and_more_than_6_after_now_when_value_is_less_than_799_it_grows_by_2(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate("2024/04/08").withValue(assetValue))
                .onDate("2024/04/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue + 2));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            799 // on point for value of asset boundary between (-inf, 799) and [799, 800)
                // off point for value of asset boundary between [799, 800) and [800, +inf)
    })
    public void less_than_11_days_and_more_than_6_after_now_when_value_is_equal_to_799_it_grows_by_1(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate("2024/04/08").withValue(assetValue))
                .onDate("2024/04/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue + 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            800, // on point for value of asset boundary between [799, 800) and [800, +inf)
            900
    })
    public void less_than_11_days_and_more_than_6_after_now_when_value_is_equal_or_more_than_800_it_remains_the_same(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate("2024/04/08").withValue(assetValue))
                .onDate("2024/04/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue));
    }
    //----------------------

    // For days < 6 after now
    @ParameterizedTest
    @ValueSource(ints = {
            -774,
            797 // off point for value of asset boundary between (-inf, 798) and [798, 799)
    })
    public void less_than_6_days_after_now_when_value_is_less_than_798_it_grows_by_3(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate("2024/04/06").withValue(assetValue))
                .onDate("2024/04/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue + 3));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            798 // on point for value of asset boundary between (-inf, 798) and [798, 799)
                // off point for value of asset boundary between [798, 799) and [799, 800)
    })
    public void less_than_6_days_after_now_when_value_is_equal_to_798_it_grows_by_2(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate("2024/04/06").withValue(assetValue))
                .onDate("2024/04/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue + 2));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            799  // on point for value of asset boundary between [798, 799) and [799, 800)
                // off point for value of asset boundary between [799, 800) and [800, +inf)
    })
    public void less_than_6_days_after_now_when_value_is_equal_to_799_it_grows_by_1(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate("2024/04/06").withValue(assetValue))
                .onDate("2024/04/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue + 1));
    }

    @ParameterizedTest
    @ValueSource(ints = {
            800, // on point for value of asset boundary between [799, 800) and [800, +inf)
            1000
    })
    public void less_than_6_days_after_now_when_value_is_equal_or_more_than_800_it_remains_the_same(int assetValue)
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Lottery Prediction").fromDate("2024/04/06").withValue(assetValue))
                .onDate("2024/04/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), String.valueOf(assetValue));
    }
}

