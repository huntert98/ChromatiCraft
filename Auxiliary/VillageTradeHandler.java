/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2017
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary;

import java.util.Random;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;
import Reika.ChromatiCraft.Magic.Artefact.UATrade;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;


public class VillageTradeHandler implements IVillageTradeHandler {

	public static final VillageTradeHandler instance = new VillageTradeHandler();

	private VillageTradeHandler() {

	}

	@Override
	public void manipulateTradesForVillager(EntityVillager ev, MerchantRecipeList li, Random rand) {
		if (ev.buyingList == null)
			ev.buyingList = new MerchantRecipeList();
		if (rand.nextInt(4) == 0 && !this.hasFocusTrade(ev.buyingList)) {
			ev.buyingList.add(new FocusCrystalTrade()); //add an unlocked trade
		}
		if (!this.hasUATrade(ev.buyingList)) {
			ev.buyingList.add(new UATrade()); //add an unlocked trade
		}
	}

	private boolean hasUATrade(MerchantRecipeList li) {
		for (Object r : li) {
			if (r instanceof UATrade)
				return true;
		}
		return false;
	}

	private boolean hasFocusTrade(MerchantRecipeList li) {
		for (Object r : li) {
			if (r instanceof FocusCrystalTrade)
				return true;
		}
		return false;
	}

}
