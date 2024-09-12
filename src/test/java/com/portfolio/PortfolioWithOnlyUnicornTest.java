package com.portfolio;

import org.junit.jupiter.api.Test;

import static com.portfolio.helper.AssetsFileLinesBuilder.anAsset;
import static com.portfolio.helper.TestingPortfolioBuilder.aPortFolio;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PortfolioWithOnlyUnicornTest {

    @Test
    public void has_infinite_value_before_now()
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Unicorn").fromDate("2023/01/15").withValue(100))
                .onDate("2024/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), "Portfolio is priceless because it got a unicorn!!!!!");
    }

    @Test
    public void has_infinite_value_after_now()
    {
        var portfolio = aPortFolio()
                .with(anAsset().describedAs("Unicorn").fromDate("2043/01/15").withValue(100))
                .onDate("2024/01/01")
                .build();

        portfolio.computePortfolioValue();

        assertEquals(portfolio.messages.get(0), "Portfolio is priceless because it got a unicorn!!!!!");
    }
}
