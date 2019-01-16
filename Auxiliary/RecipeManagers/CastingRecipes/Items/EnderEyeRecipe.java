package Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipes.Items;

import Reika.ChromatiCraft.Auxiliary.ChromaStacks;
import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe.MultiBlockCastingRecipe;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class EnderEyeRecipe extends MultiBlockCastingRecipe {

	public EnderEyeRecipe(ItemStack out, ItemStack main) {
		super(out, main);

		/*
		this.addAuxItem(ChromaStacks.auraDust, 2, 0);
		this.addAuxItem(ChromaStacks.auraDust, -2, 0);

		this.addAuxItem(ChromaStacks.beaconDust, 0, -2);
		this.addAuxItem(ChromaStacks.beaconDust, 0, 2);

		this.addAuxItem(ChromaStacks.elementDust, 2, 2);
		this.addAuxItem(ChromaStacks.elementDust, -2, -2);

		this.addAuxItem(Items.feather, -2, 2);
		this.addAuxItem(Items.feather, 2, -2);
		 */

		this.addAuxItem(Items.ender_eye, -2, -2);
		this.addAuxItem(Items.ender_eye, 2, -2);
		this.addAuxItem(Items.ender_eye, 0, 2);

		this.addAuxItem(ChromaStacks.elementDust, 0, -2);
		this.addAuxItem(ChromaStacks.beaconDust, -2, 2);
		this.addAuxItem(ChromaStacks.beaconDust, 2, 2);
		this.addAuxItem(ChromaStacks.auraDust, -2, 0);
		this.addAuxItem(ChromaStacks.auraDust, 2, 0);
	}

}
