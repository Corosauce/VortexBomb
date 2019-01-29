package com.corosus.vortexbomb;

import java.util.ArrayList;
import java.util.List;

public class EffectManager {

    private List<Effect> listEffects = new ArrayList<>();

    public void tick() {
        listEffects.forEach(e -> e.tick());
        listEffects.stream().filter(e -> e.isComplete()).forEach(e -> e.onComplete());
        listEffects.removeIf(e -> e.isComplete());
    }

    public void addEffect(Effect effect) {
        listEffects.add(effect);

        //protect against spam use of effect
        if (listEffects.size() > 3) {
            listEffects.get(0).detonate();
        }
    }

}
