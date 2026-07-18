package com.frnc.createvoid.jei;

import com.frnc.createvoid.CreateVoid;
import com.frnc.createvoid.wateringrecipes.RecipeTypes;
import com.frnc.createvoid.wateringrecipes.WateringRecipe;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * JEI 插件入口，注册浇水配方分类（WateringCategory）。
 */
@JeiPlugin
public class CreateVoidJEI implements IModPlugin {

    public static final RecipeType<WateringRecipe> WATERING_TYPE =
            RecipeType.create(CreateVoid.MOD_ID, "watering", WateringRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CreateVoid.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        CreateRecipeCategory.Info<WateringRecipe> info = new CreateRecipeCategory.Info<>(
                WATERING_TYPE,
                Component.translatable("jei.create_void.watering"),
                registration.getJeiHelpers().getGuiHelper().createBlankDrawable(177, 70),
                // 使用空白图标，由 WateringCategory 覆盖 getIcon 提供
                registration.getJeiHelpers().getGuiHelper().createBlankDrawable(18, 18),
                () -> {
                    if (Minecraft.getInstance().level == null)
                        return Collections.emptyList();
                    return Minecraft.getInstance().level.getRecipeManager()
                            .getAllRecipesFor(RecipeTypes.WATERING.getType())
                            .stream()
                            .map(r -> (WateringRecipe) r)
                            .toList();
                },
                // 催化剂：Spout 方块
                List.of(() -> new ItemStack(AllBlocks.SPOUT.get()))
        );
        registration.addRecipeCategories(new WateringCategory(info));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // 配方由 CreateRecipeCategory.registerRecipes() 统一处理
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(
                new ItemStack(AllBlocks.SPOUT.get()),
                WATERING_TYPE
        );
    }
}
