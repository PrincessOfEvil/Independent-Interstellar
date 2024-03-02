package data.hullmods.princessscripts;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShieldAPI.ShieldType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;

public class FrontShieldGenerator extends BaseHullMod {
	
	public static float EFFICIENCY = 1.0f;
	public static float SHIELD_ARC = 120f;
	public static float ARMOR_BONUS = -15f;
	
	
	//public void applyEffectsAfterShipCreationFirstPass(ShipAPI ship, String id) {
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ShieldAPI shield = ship.getShield();
		if (shield == null) {
			ship.setShield(ShieldType.FRONT, 0.5f, EFFICIENCY, SHIELD_ARC);
		}
	}
	
	@Override
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
    	stats.getArmorBonus().modifyPercent(id, ARMOR_BONUS);
	}



	public String getDescriptionParam(int index, HullSize hullSize) {
		if (index == 0) {
			return "" + (int) SHIELD_ARC;
		}
		if (index == 1) {
			return "" + (int) Math.round((-ARMOR_BONUS)) + "%";
		}
		
//		if (index == 1) return "" + ((Float) mag.get(HullSize.FRIGATE)).intValue();
//		if (index == 2) return "" + ((Float) mag.get(HullSize.DESTROYER)).intValue();
//		if (index == 3) return "" + ((Float) mag.get(HullSize.CRUISER)).intValue();
//		if (index == 4) return "" + ((Float) mag.get(HullSize.CAPITAL_SHIP)).intValue();
		
		return null;
	}

	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship != null && ship.getVariant().getHullMods().contains(HullMods.SHIELD_SHUNT)) {
			return false;
		}
		return ship != null && ship.getHullSpec().getDefenseType() == ShieldType.NONE;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship != null && ship.getVariant().getHullMods().contains(HullMods.SHIELD_SHUNT)) {
			return "Incompatible with Shield Shunt";
		}
		if (ship != null && ship.getHullSpec().getDefenseType() == ShieldType.PHASE) {
			return "Ship can not have shields";
		} 
		return "Ship already has shields";
	}
}
