/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Items.Tools;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import Reika.ChromatiCraft.Auxiliary.ChromaFX;
import Reika.ChromatiCraft.Auxiliary.ProgressionManager;
import Reika.ChromatiCraft.Auxiliary.ProgressionManager.ProgressStage;
import Reika.ChromatiCraft.Base.ItemChromaTool;
import Reika.ChromatiCraft.Magic.PlayerElementBuffer;
import Reika.ChromatiCraft.Registry.ChromaSounds;
import Reika.ChromatiCraft.Registry.ChromaTiles;
import Reika.ChromatiCraft.Registry.CrystalElement;
import Reika.ChromatiCraft.Render.Particle.EntityRuneFX;
import Reika.ChromatiCraft.TileEntity.TileEntityCastingTable;
import Reika.ChromatiCraft.TileEntity.TileEntityCrystalPylon;
import Reika.ChromatiCraft.TileEntity.TileEntityCrystalRepeater;
import Reika.ChromatiCraft.TileEntity.TileEntityItemRift;
import Reika.ChromatiCraft.TileEntity.TileEntityMiner;
import Reika.ChromatiCraft.TileEntity.TileEntityRift;
import Reika.ChromatiCraft.TileEntity.TileEntityRitualTable;
import Reika.DragonAPI.Libraries.ReikaPlayerAPI;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaParticleHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemManipulator extends ItemChromaTool {

	public ItemManipulator(int index) {
		super(index);
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer ep, World world, int x, int y, int z, int s, float a, float b, float c) {
		ChromaTiles t = ChromaTiles.getTile(world, x, y, z);
		TileEntity tile = world.getTileEntity(x, y, z);
		if (t == ChromaTiles.RIFT) {
			TileEntityRift te = (TileEntityRift)tile;
			if (ep.isSneaking()) {
				te.drop();
			}
			else {
				te.setDirection(ForgeDirection.VALID_DIRECTIONS[s]);
			}
			return true;
		}
		if (t == ChromaTiles.TABLE) {
			boolean flag = ((TileEntityCastingTable)tile).triggerCrafting(ep);
			return flag;
		}
		if (t == ChromaTiles.RITUAL) {
			((TileEntityRitualTable)tile).triggerRitual(is);
			return true;
		}
		if (t == ChromaTiles.MINER) {
			((TileEntityMiner)tile).triggerCalculation();
			return true;
		}
		if (t == ChromaTiles.ITEMRIFT) {
			TileEntityItemRift ir = (TileEntityItemRift)tile;
			ir.isEmitting = !ir.isEmitting;
			return true;
		}
		if (t == ChromaTiles.REPEATER) {
			TileEntityCrystalRepeater te = (TileEntityCrystalRepeater)tile;
			if (te.checkConnectivity()) {
				CrystalElement e = te.getActiveColor();
				ChromaSounds.CAST.playSoundAtBlock(world, x, y, z);
				ReikaParticleHelper.spawnColoredParticlesWithOutset(world, x, y, z, e.getRed(), e.getGreen(), e.getBlue(), 32, 0.5);
			}
			else {
				ChromaSounds.ERROR.playSoundAtBlock(world, x, y, z);
			}
			return true;
		}
		return false;
	}

	@Override
	public ItemStack onEaten(ItemStack is, World world, EntityPlayer ep)
	{
		return is;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack is)
	{
		return 72000;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		MovingObjectPosition mov = ReikaPlayerAPI.getLookedAtBlock(player, 16, false);
		//PlayerElementBuffer.instance.addToPlayer(player, CrystalElement.elements[count%16], 1);
		//PlayerElementBuffer.instance.checkUpgrade(player, true);
		//player.getEntityData().removeTag("CrystalBuffer");
		if (mov != null) {
			ChromaTiles c = ChromaTiles.getTile(player.worldObj, mov.blockX, mov.blockY, mov.blockZ);
			if (c == ChromaTiles.PYLON) {
				TileEntityCrystalPylon te = (TileEntityCrystalPylon)player.worldObj.getTileEntity(mov.blockX, mov.blockY, mov.blockZ);
				CrystalElement e = te.getColor();
				if (te.canConduct() && te.getEnergy(e) >= 4 && PlayerElementBuffer.instance.canPlayerAccept(player, e, 1)) {
					if (PlayerElementBuffer.instance.addToPlayer(player, e, 1))
						te.drain(e, 4);
					PlayerElementBuffer.instance.checkUpgrade(player, true);
					ProgressionManager.instance.stepPlayerTo(player, ProgressStage.CHARGE);
					if (player.worldObj.isRemote) {
						//this.spawnParticles(player, e);
						ChromaFX.createPylonChargeBeam(te, player, (count%20)/20D);
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticles(EntityPlayer player, CrystalElement e) {
		double rx = ReikaRandomHelper.getRandomPlusMinus(player.posX, 0.8);
		double ry = ReikaRandomHelper.getRandomPlusMinus(player.posY, 1.5);
		double rz = ReikaRandomHelper.getRandomPlusMinus(player.posZ, 0.8);
		Minecraft.getMinecraft().effectRenderer.addEffect(new EntityRuneFX(player.worldObj, rx, ry, rz, e));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer ep)
	{
		ep.setItemInUse(is, this.getMaxItemUseDuration(is));
		return is;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack is) {
		return EnumAction.bow;
	}

}
