package net.minecraft.src;

public interface IRecipe {
    boolean matches(InventoryCrafting inventorycrafting);

    ItemStack getCraftingResult(InventoryCrafting inventorycrafting);

    int getRecipeSize();

    ItemStack getRecipeOutput();
}
