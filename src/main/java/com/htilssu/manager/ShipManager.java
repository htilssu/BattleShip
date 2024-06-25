package com.htilssu.manager;

import com.htilssu.entity.Ship;
import com.htilssu.entity.Sprite;
import com.htilssu.util.AssetUtils;

import java.util.HashMap;
import java.util.Map;

public class ShipManager {

    public static final int SHIP_2 = 2;
    public static final int SHIP_3 = 3;
    public static final int SHIP_4 = 4;
    public static final int SHIP_5 = 5;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    static Map<Integer, Map<Integer, Sprite>> spriteMap = new HashMap<>();

    static {
        spriteMap.put(SHIP_2, new HashMap<>() {
            {
                this.put(VERTICAL, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_2)));
                this.put(HORIZONTAL, new Sprite(AssetUtils.rotate90(get(VERTICAL).getAsset())));
            }
        });
        spriteMap.put(SHIP_3, new HashMap<>() {
            {
                this.put(VERTICAL, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_3)));
                this.put(HORIZONTAL, new Sprite(AssetUtils.rotate90(get(VERTICAL).getAsset())));
            }
        });
        spriteMap.put(SHIP_4, new HashMap<>() {
            {
                this.put(VERTICAL, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_4)));
                this.put(HORIZONTAL, new Sprite(AssetUtils.rotate90(get(VERTICAL).getAsset())));
            }
        });
        spriteMap.put(SHIP_5, new HashMap<>() {
            {
                this.put(VERTICAL, new Sprite(AssetUtils.getImage(AssetUtils.ASSET_SHIP_5)));
                this.put(HORIZONTAL, new Sprite(AssetUtils.rotate90(get(VERTICAL).getAsset())));
            }
        });

    }

    public static Ship createShip(int shipType, int direction) {
        return new Ship(direction, new Sprite(spriteMap.get(shipType).get(direction)), shipType);
    }

}
