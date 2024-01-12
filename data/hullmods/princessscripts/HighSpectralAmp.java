package data.hullmods.princessscripts;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
import org.lwjgl.util.vector.Vector2f;

public class HighSpectralAmp extends BaseHullMod {
  public static float RANGE_THRESHOLD = 200.0F;
  
  public static float RANGE_MULT = 0.5F;
  
  public static float DAMAGE_BONUS_PERCENT = 10.0F;
  
  public static float SMOD_MODIFIER = 5.0F;
  
  public static float EXTRA_BONUS_PERCENT = 100.0F;
  
  public boolean isApplicableToShip(ShipAPI ship) {
    return !ship.getVariant().getHullMods().contains("advancedoptics") && !ship.getVariant().getHullMods().contains("high_scatter_amp");
  }
  
  public String getUnapplicableReason(ShipAPI ship) {
    if (ship.getVariant().getHullMods().contains("advancedoptics"))
      return "Incompatible with Advanced Optics"; 
    if (ship.getVariant().getHullMods().contains("high_scatter_amp"))
      return "Why though?"; 
    return null;
  }
  
  public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
    boolean sMod = isSMod(stats);
    stats.getBeamWeaponDamageMult().modifyPercent(id, DAMAGE_BONUS_PERCENT + (sMod ? SMOD_MODIFIER : 0.0F));
  }
  
  public String getSModDescriptionParam(int index, ShipAPI.HullSize hullSize, ShipAPI ship) {
    if (index == 0)
      return Math.round(SMOD_MODIFIER) + "%"; 
    return null;
  }
  
  public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
    ship.addListener(new HighSpectralAmpDamageDealtMod(ship));
    ship.addListener(new HighSpectralAmpRangeMod());
  }
  
  public static class HighSpectralAmpDamageDealtMod implements DamageDealtModifier {
    protected ShipAPI ship;
    
    public HighSpectralAmpDamageDealtMod(ShipAPI ship) {
      this.ship = ship;
    }
    
    public String modifyDamageDealt(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
      if (!(param instanceof com.fs.starfarer.api.combat.DamagingProjectileAPI) && param instanceof com.fs.starfarer.api.combat.BeamAPI)
        {
        damage.setForceHardFlux(true);
        if (param instanceof com.fs.starfarer.api.combat.BeamAPI)
        	{
        	com.fs.starfarer.api.combat.BeamAPI cast = (com.fs.starfarer.api.combat.BeamAPI) param;
        	if (cast.getWeapon().hasAIHint(WeaponAPI.AIHints.PD) ||
        		cast.getWeapon().hasAIHint(WeaponAPI.AIHints.PD_ONLY) ||
        		cast.getWeapon().hasAIHint(WeaponAPI.AIHints.PD_ALSO))
        		damage.getModifier().modifyPercent("poe_hsa", EXTRA_BONUS_PERCENT);
        	}
        }
      return null;
    }
  }
  
  public static class HighSpectralAmpRangeMod implements WeaponBaseRangeModifier {
    public float getWeaponBaseRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
      return 0.0F;
    }
    
    public float getWeaponBaseRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
      return 1.0F;
    }
    
    public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
      if (weapon.isBeam()) {
        float range = weapon.getSpec().getMaxRange();
        if (range < HighSpectralAmp.RANGE_THRESHOLD)
          return 0.0F; 
        float past = range - HighSpectralAmp.RANGE_THRESHOLD;
        float penalty = past * (1.0F - HighSpectralAmp.RANGE_MULT);
        return -penalty;
      } 
      return 0.0F;
    }
  }
  
  public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
    return null;
  }
  
  public boolean shouldAddDescriptionToTooltip(ShipAPI.HullSize hullSize, ShipAPI ship, boolean isForModSpec) {
    return false;
  }
  
  public void addPostDescriptionSection(TooltipMakerAPI tooltip, ShipAPI.HullSize hullSize, ShipAPI ship, float width, boolean isForModSpec) {
    float pad = 3.0F;
    float opad = 10.0F;
    Color h = Misc.getHighlightColor();
    Color bad = Misc.getNegativeHighlightColor();
    tooltip.addPara("Beam weapons deal %s more damage and deal hard flux damage to shields.", opad, h, new String[] { (int)DAMAGE_BONUS_PERCENT + "%" });
    tooltip.addPara("Reduces the portion of the range of beam weapons that is above %s units by %s. The base range is affected.", opad, h, new String[] { (int)RANGE_THRESHOLD + "", 
          Math.round((1.0F - RANGE_MULT) * 100.0F) + "%" });
    tooltip.addPara("All beam PD weapons deal %s more damage.", opad, h, new String[] { (int)EXTRA_BONUS_PERCENT + "%" });
    tooltip.addSectionHeading("Interactions with other modifiers", Alignment.MID, opad);
    tooltip.addPara("The base range is reduced, thus percentage and multiplicative modifiers - such as from Integrated Targeting Unit, skills, or similar sources - apply to the reduced base value.", 
        opad);
  }
}
