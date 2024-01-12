package data.hullmods.princessscripts;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

public class AuxAblativeArmor extends BaseHullMod {
  public static float ARMOR_MULT = 5F;
  public static float ARMOR_REDUCE_MULT = 1F / ARMOR_MULT;
  
  public boolean isApplicableToShip(ShipAPI ship) {
    return !ship.getVariant().getHullMods().contains("ablative_armor");
  }
  
  public String getUnapplicableReason(ShipAPI ship) {
    if (ship.getVariant().getHullMods().contains("ablative_armor"))
      return "Ship armor is already ablative"; 
    return null;
  }
  
  public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
	stats.getArmorBonus().modifyMult(id, ARMOR_MULT);
    stats.getEffectiveArmorBonus().modifyMult(id, ARMOR_REDUCE_MULT);
    stats.getMinArmorFraction().modifyMult(id, ARMOR_REDUCE_MULT);
  }
  
  public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
    if (index == 0)
      return Math.round(ARMOR_MULT * 100.0F) + "%"; 
    return null;
  }
}
