/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Items;

import Reika.ChromatiCraft.Base.ItemChromaBasic;
import Reika.ChromatiCraft.Entity.EntityEnderEyeT2;
import Reika.DragonAPI.Interfaces.Item.AnimatedSpritesheet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;


public class ItemT2EnderEye extends ItemChromaBasic implements AnimatedSpritesheet {

	public ItemT2EnderEye(int tex) {
		super(tex);
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer ep, World world, int x, int y, int z, int s, float a, float b, float c) {
		return Items.ender_eye.onItemUse(is, ep, world, x, y, z, s, a, b, c);
	}

	@Override
	public boolean useAnimatedRender(ItemStack is) {
		return true;
	}

	@Override
	public int getFrameCount() {
		return 16;
	}

	@Override
	public int getBaseRow(ItemStack is) {
		return 8;
	}

	@Override
	public int getColumn(ItemStack is) {
		return 0;
	}

	@Override
	public int getFrameOffset(ItemStack is) {
		return 0;
	}

	@Override
	public int getFrameSpeed() {
		return 2;
	}

	@Override
	public String getTexture(ItemStack is) {
		return this.useAnimatedRender(is) ? "/Reika/ChromatiCraft/Textures/Items/miscanim.png" : super.getTexture(is);
	}

	@Override
	public boolean verticalFrames() {
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer ep) {
		MovingObjectPosition mov = this.getMovingObjectPositionFromPlayer(world, ep, false);

		if (mov != null && mov.typeOfHit == MovingObjectType.BLOCK && world.getBlock(mov.blockX, mov.blockY, mov.blockZ) == Blocks.end_portal_frame) {
			return is;
		}
		else {
			if (!world.isRemote) {
				ChunkPosition pos = world.findClosestStructure("Stronghold", (int)ep.posX, (int)ep.posY, (int)ep.posZ);

				if (pos != null) {
					EntityEnderEyeT2 eye = new EntityEnderEyeT2(world, ep.posX, ep.posY + 1.62D - ep.yOffset, ep.posZ);
					eye.moveTowards(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
					world.spawnEntityInWorld(eye);
					world.playSoundAtEntity(ep, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
					world.playAuxSFXAtEntity((EntityPlayer)null, 1002, (int)ep.posX, (int)ep.posY, (int)ep.posZ, 0);

					if (!ep.capabilities.isCreativeMode) {
						--is.stackSize;
					}
				}
			}

			return is;
		}
	}

}
