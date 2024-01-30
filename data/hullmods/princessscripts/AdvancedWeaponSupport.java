package data.hullmods.princessscripts;

import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipAPI.HullSize;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.hullmods.CompromisedStructure;
import com.fs.starfarer.api.combat.listeners.DamageDealtModifier;
import com.fs.starfarer.api.combat.listeners.WeaponBaseRangeModifier;

import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.BeamAPI;

import org.lwjgl.util.vector.Vector2f;

public class AdvancedWeaponSupport extends BaseHullMod {	

	private static final float DAMAGE_MULT = 75f;
	private static final int RANGE_MOD = 300;
	
	public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
		ship.addListener(new AWSDamageModifier(DAMAGE_MULT));
		ship.addListener(new AWSRangeModifier(RANGE_MOD));
	}
	
	public String getDescriptionParam(int index, HullSize hullSize, ShipAPI ship) {
		if (index == 0) return "" + (int) Math.round(DAMAGE_MULT) + "%";
		if (index == 1) return "" + RANGE_MOD;
		return null;
	}

	public static class AWSDamageModifier implements DamageDealtModifier {
		public float mult;
		
		public AWSDamageModifier(float mult) {
			this.mult = mult;
		}
		
		public String modifyDamageDealt(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
			WeaponAPI weapon = null;
			if (param instanceof DamagingProjectileAPI)
				{
				DamagingProjectileAPI dapi = (DamagingProjectileAPI) param;
				weapon = dapi.getWeapon();
				}
			if (param instanceof BeamAPI)
				{
				BeamAPI bapi = (BeamAPI) param;
				weapon = bapi.getWeapon();
				}
			if (weapon != null && weapon.getSlot().getSlotSize() != weapon.getSpec().getSize())
				damage.getModifier().modifyPercent("poe_aws", mult);
			
			return null;
		}
	}
		
	public static class AWSRangeModifier implements WeaponBaseRangeModifier {
		public float range;
		
		public AWSRangeModifier(int range) {
			this.range = (float) range;
		}
		
		public float getWeaponBaseRangeFlatMod(ShipAPI ship, WeaponAPI weapon) {
			if (weapon.getSlot().getSlotSize() != weapon.getSpec().getSize())
				return range;
			
			return 0.0F;
		}
		
		public float getWeaponBaseRangePercentMod(ShipAPI ship, WeaponAPI weapon) {
			return 0.0F;
		}
		
		public float getWeaponBaseRangeMultMod(ShipAPI ship, WeaponAPI weapon) {
			return 1.0F;
		}
	}
}
