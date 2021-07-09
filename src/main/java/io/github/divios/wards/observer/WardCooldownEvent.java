package io.github.divios.wards.observer;

import io.github.divios.core_lib.Events;
import io.github.divios.core_lib.Schedulers;
import io.github.divios.core_lib.cooldown.CooldownEvent;
import io.github.divios.core_lib.event.SingleSubscription;
import io.github.divios.core_lib.misc.Msg;
import io.github.divios.wards.Wards;
import io.github.divios.wards.utils.utils;
import io.github.divios.wards.wards.Ward;
import io.github.divios.wards.wards.WardsManager;
import org.bukkit.Particle;

public class WardCooldownEvent extends abstractObserver{


    @Override
    protected SingleSubscription initListener() {
        return Events.subscribe(CooldownEvent.class)
                .handler(e -> {

                    Ward ward = (Ward) e.getValue();

                    Msg.sendMsg(ward.getOwner(), Msg.singletonMsg(Wards.langValues.WARD_EXPIRED)
                            .add("\\{ward}", ward.getName()).build());
                    utils.cleanBlock(ward.getCenter());
                    ward.getCenter().getWorld().spawnParticle(Particle.SMOKE_NORMAL,
                            ward.getCenter().clone().add(0.5, 0.5, 0.5), 40);

                    Schedulers.sync().run(() -> WardsManager.getInstance().deleteWard(ward));
                });
    }
}
