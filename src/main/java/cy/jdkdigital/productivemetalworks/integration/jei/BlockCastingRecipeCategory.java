package cy.jdkdigital.productivemetalworks.integration.jei;

import cy.jdkdigital.productivemetalworks.ProductiveMetalworks;
import cy.jdkdigital.productivemetalworks.recipe.BlockCastingRecipe;
import cy.jdkdigital.productivemetalworks.recipe.ItemMeltingRecipe;
import cy.jdkdigital.productivemetalworks.registry.MetalworksRegistrator;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Arrays;

public class BlockCastingRecipeCategory extends AbstractRecipeCategory<RecipeHolder<BlockCastingRecipe>>
{
    private final IDrawable background;

    public BlockCastingRecipeCategory(IGuiHelper guiHelper) {
        super(
                JeiPlugin.BLOCK_CASTING,
                Component.translatable("jei." + ProductiveMetalworks.MODID + ".block_casting"),
                guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(MetalworksRegistrator.CASTING_BASIN.get())),
                165, 68
        );
        this.background = guiHelper.drawableBuilder(ResourceLocation.fromNamespaceAndPath(ProductiveMetalworks.MODID, "textures/gui/jei/block_casting.png"), 0, 0, 165, 68).setTextureSize(165, 68).build();
    }

    @Override
    public void draw(RecipeHolder<BlockCastingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics, 0, 0);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BlockCastingRecipe> recipe, IFocusGroup focuses) {
        var hasCast = !recipe.value().cast.isEmpty();
        if (hasCast) {
            builder.addSlot(RecipeIngredientRole.INPUT, 63, 26)
                    .addIngredients(recipe.value().cast)
                    .setSlotName("cast");
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 26, 26)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.value().fluid.getFluids()).filter(fluidStack -> fluidStack.getFluid().defaultFluidState().isSource()).toList())
                .setFluidRenderer(recipe.value().fluid.amount(), false, 16, 16)
                .setSlotName("fluids");

        builder.addSlot(RecipeIngredientRole.INPUT, 68, 16)
                .addIngredients(NeoForgeTypes.FLUID_STACK, Arrays.stream(recipe.value().fluid.getFluids()).filter(fluidStack -> fluidStack.getFluid().defaultFluidState().isSource()).toList())
                .setFluidRenderer(recipe.value().fluid.amount(), false, 6,hasCast ? 10 : 26)
                .setSlotName("fluids");

        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 26)
                .addItemStack(recipe.value().result)
                .setStandardSlotBackground()
                .setSlotName("result");
    }
}
