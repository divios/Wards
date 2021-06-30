package io.github.divios.wards.wards;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WardsManager {

    private static WardsManager instance = null;
    private final Set<Ward> wards = new HashSet<>();

    public static WardsManager getInstance() {
        if (instance == null)
            instance = new WardsManager();
        return instance;
    }

    public Set<Ward> getWards() {
        return Collections.unmodifiableSet(wards);
    }

    public void createWard(Ward ward) { wards.add(ward); }

    public void deleteWard(Ward ward) { wards.remove(ward); }

    public void clear(Ward ward) { wards.clear(); }


}
