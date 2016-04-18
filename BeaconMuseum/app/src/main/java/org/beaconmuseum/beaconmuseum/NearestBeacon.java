package org.beaconmuseum.beaconmuseum;

/**
 * Trzyma najbliższy beacon w zasięgu
 */

public class NearestBeacon {
    /**
     * Zwraca informację o beaconie znajdującym się najbliżej
     * odbiornika. Jeśli nie ma beacona w zasięgu, zwraca NoBeacon
     * Aby testować istnienie, należy wołać metodę isABeacon()
     */
    public static BeaconInfo getInfo() {
        if (BeaconsInRangeList.getInstance().getList().length == 0) {
            return new NoBeacon();
        } else {
            BeaconInfo result = BeaconsInRangeList.getInstance().getList()[0];
            for (BeaconInfo i : BeaconsInRangeList.getInstance().getList()) {
                if (i.range < result.range)
                    result = i;
            }
            return result;
        }
    }
}