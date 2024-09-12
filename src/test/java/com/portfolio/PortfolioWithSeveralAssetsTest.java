package com.portfolio;

import org.junit.jupiter.api.Test;

import static com.portfolio.helper.AssetsFileLinesBuilder.anAsset;
import static com.portfolio.helper.TestingPortfolioBuilder.aPortFolio;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioWithSeveralAssetsTest {

    @Test
    public void when_including_a_unicorn_then_only_the_unicorn_matters()
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Some Regular Asset").fromDate("2024/01/15").withValue(100))
                .with(anAsset().describedAs("Unicorn").fromDate("2024/01/15").withValue(80))
                .onDate("2023/01/01")
                .build();


        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), "Portfolio is priceless because it got a unicorn!!!!!");
    }

    @Test
    public void when_not_including_a_unicorn_sums_the_value_of_all_assets()
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Some Regular Asset").fromDate("2024/01/15").withValue(100))
                .with(anAsset().describedAs("French Wine").fromDate("2024/01/15").withValue(100))
                .onDate("2023/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), "200");
    }
}
