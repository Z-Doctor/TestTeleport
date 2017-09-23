package zdoctor.testmod.init;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import zdoctor.lazymodder.easy.items.EasyItem;
import zdoctor.testmod.SilentTeleport;

public class ZItems {

	public static Item nbtStick;

	public static void preInit() {
		nbtStick = new EasyItem("NBTStick") {
			@Override
			public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
				ItemStack stack = playerIn.getHeldItem(handIn);
				if (!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
				NBTTagCompound stackNBT = stack.getTagCompound();

				NBTTagCompound temp = new NBTTagCompound();
				playerIn.writeToNBT(temp);
				if (playerIn.isSneaking()) {
					stackNBT.setTag("playerTag", temp.copy());
					stackNBT.setString("uuid", playerIn.getUniqueID().toString());

				} else {
					if (!playerIn.isDead) {
						if (stackNBT.hasKey("playerTag")) {
							int currentDimension = playerIn.dimension;
							int dimensionIn = stackNBT.getCompoundTag("playerTag").getInteger("Dimension");
							if (currentDimension != dimensionIn) {
								if (!worldIn.isRemote) {
									EntityPlayerMP playerMP = (EntityPlayerMP) playerIn;

									SilentTeleport teleporter;

									teleporter = new SilentTeleport(playerMP.getServer().getWorld(dimensionIn)) {
										@Override
										public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
											super.placeInExistingPortal(entityIn, rotationYaw);
											playerIn.readFromNBT(stackNBT.getCompoundTag("playerTag"));
											ItemStack stack = playerIn.getHeldItem(handIn);
											stack.setTagCompound(stackNBT);
											System.out.println("Teleporitng");
											return false;
										}
									};

									teleporter.transferPlayerToDimension(playerMP, dimensionIn);
								}
							} else {
								// Loads the player with the data
								playerIn.readFromNBT(stackNBT.getCompoundTag("playerTag"));
								// Loads the previous data to the new item
								stack = playerIn.getHeldItem(handIn);
								stack.setTagCompound(stackNBT);
							}

						}
					}
				}
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
			}

			@Override
			public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
				if (!stack.hasTagCompound())
					stack.setTagCompound(new NBTTagCompound());
				NBTTagCompound nbt = stack.getTagCompound();
				tooltip.add("Tag Count: " + stack.getTagCompound().getSize());
				if (nbt.hasKey("uuid")) {
					EntityPlayer player = worldIn.getPlayerEntityByUUID(UUID.fromString(nbt.getString("uuid")));
					if (player != null) {
						tooltip.add("Player: " + player.getDisplayNameString());
						tooltip.add("UUID: " + UUID.fromString(nbt.getString("uuid")));
					}
				}
				if (nbt.hasKey("playerTag")) {
					if (nbt.getCompoundTag("playerTag").hasKey("Pos"))
						tooltip.add("Pos: " + nbt.getCompoundTag("playerTag").getTag("Pos"));
				}
				if (nbt.hasKey("playerTag")) {
					if (nbt.getCompoundTag("playerTag").hasKey("Dimension"))
						tooltip.add("Dimension: " + nbt.getCompoundTag("playerTag").getInteger("Dimension"));
				}
			}
		};
	}
}
