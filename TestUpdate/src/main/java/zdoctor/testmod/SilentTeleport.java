package zdoctor.testmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class SilentTeleport extends Teleporter {

	public SilentTeleport(WorldServer worldIn) {
		super(worldIn);
	}

	public void silentTransferToDimension(Entity entityIn) {

	}

	@Override
	public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
		System.out.println("teleporting silently");
		double d5 = entityIn.posX;
		double d6 = entityIn.posY;
		double d7 = entityIn.posZ;

		if (entityIn instanceof EntityPlayerMP) {
			((EntityPlayerMP) entityIn).connection.setPlayerLocation(d5, d6, d7, entityIn.rotationYaw,
					entityIn.rotationPitch);
		} else {
			entityIn.setLocationAndAngles(d5, d6, d7, entityIn.rotationYaw, entityIn.rotationPitch);
		}
		
		return false;
	}

	@Override
	public void placeInPortal(Entity entityIn, float rotationYaw) {
		placeInExistingPortal(entityIn, rotationYaw);
//		super.placeInPortal(entityIn, rotationYaw);
	}

	@Override
	public void removeStalePortalLocations(long worldTime) {
//		super.removeStalePortalLocations(worldTime);
	}

	@Override
	public boolean makePortal(Entity entityIn) {
		return super.makePortal(entityIn);
	}
}