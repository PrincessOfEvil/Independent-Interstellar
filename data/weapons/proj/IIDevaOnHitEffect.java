package data.weapons.proj;

import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamagingProjectileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.ApplyDamageResultAPI;
import com.fs.starfarer.api.impl.combat.BreachOnHitEffect;
import org.lwjgl.util.vector.Vector2f;

public class IIDevaOnHitEffect extends BreachOnHitEffect {
  public static float II_DAMAGE = 40.0F;
  
  public void onHit(DamagingProjectileAPI projectile, CombatEntityAPI target, Vector2f point, boolean shieldHit, ApplyDamageResultAPI damageResult, CombatEngineAPI engine) {
    if (!shieldHit && target instanceof ShipAPI)
      dealArmorDamage(projectile, (ShipAPI)target, point, II_DAMAGE); 
  }
}
