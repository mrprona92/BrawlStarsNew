package com.mrprona.brawlassistant.base.service.ti4;

import android.content.Context;

import com.mrprona.brawlassistant.BeanContainer;
import com.mrprona.brawlassistant.base.api.ti4.PrizePoolHolder;

/**
 * User: ABadretdinov
 * Date: 14.05.14
 * Time: 18:55
 */
public class TI4ServiceImpl implements TI4Service {

    @Override
    public Long getPrizePool(Context context) {
        PrizePoolHolder result = BeanContainer.getInstance().getSteamService().getLeaguePrizePool(600);
        if (result == null) {
            return null;
        } else {
            return result.getResult().getPrizePool();
        }
    }
}
