package data.hullmods.princessscripts;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.CompromisedStructure;
import com.fs.starfarer.api.impl.campaign.ids.HullMods;

public class RapidPhaseCoils extends BaseHullMod {
	private static final float PHASE_BONUS_MULT = 5f;
	private static final float PHASE_MALUS_MULT = 4f;
	
	public void applyEffectsBeforeShipCreation(HullSize hullSize, MutableShipStatsAPI stats, String id) {
	    stats.getDynamic().getMod(
	        "phase_cloak_flux_level_for_min_speed_mod").modifyMult(id, 0f);
	
		stats.getDynamic().getMod("phase_cloak_speed").modifyMult(id, PHASE_BONUS_MULT / 0.33f);
		stats.getDynamic().getMod("phase_cloak_accel").modifyMult(id, PHASE_BONUS_MULT / 0.33f);
		
		stats.getPhaseCloakActivationCostBonus().modifyMult(id, PHASE_MALUS_MULT);
		stats.getPhaseCloakUpkeepCostBonus().modifyMult(id, PHASE_MALUS_MULT);
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) Math.round(PHASE_BONUS_MULT * 100f) + "%";
		if (index == 1) return "" + (int) Math.round(PHASE_MALUS_MULT * 100f) + "%";
		return null;
	}

	@Override
	public boolean isApplicableToShip(ShipAPI ship) {
		if (ship.getVariant().getHullSize() == HullSize.FRIGATE) return false;
		if (ship.getVariant().getHullSize() == HullSize.DESTROYER) return false;
		if (ship.getVariant().hasHullMod(HullMods.CIVGRADE) && !ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)) return false;
		
		return true;
	}
	
	public String getUnapplicableReason(ShipAPI ship) {
		if (ship.getVariant().getHullSize() == HullSize.FRIGATE) {
			return "Can not be installed on small ships";
		}
		if (ship.getVariant().getHullSize() == HullSize.DESTROYER) {
			return "Can not be installed on small ships";
		}
		if (ship.getVariant().hasHullMod(HullMods.CIVGRADE) && !ship.getVariant().hasHullMod(HullMods.MILITARIZED_SUBSYSTEMS)) {
			return "Can not be installed on civilian ships";
		}
		
		return null;
	}
}
