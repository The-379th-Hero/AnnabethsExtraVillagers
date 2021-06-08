package annabeth.extravillagers.client.screen;

import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import annabeth.coremod.crafting.SawmillRecipe;
import annabeth.extravillagers.container.SawmillContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class SawmillScreen extends ContainerScreen<SawmillContainer> {
	private static final ResourceLocation BG_LOCATION = new ResourceLocation("textures/gui/container/stonecutter.png");
	private float scrollOffs;
	private boolean scrolling;
	private int startIndex;
	private boolean displayRecipes;
	
	public SawmillScreen(SawmillContainer container, PlayerInventory inv, ITextComponent title) {
		super(container, inv, title);
		container.registerUpdateListener(this::containerChanged);
		--this.titleLabelY;
	}
	
	@Override
	public void render(MatrixStack matrixStack, int x, int y, float partialTicks) {
		super.render(matrixStack, x, y, partialTicks);
		this.renderTooltip(matrixStack, x, y);
	}
	
	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
		this.renderBackground(matrixStack);
		RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
		this.minecraft.getTextureManager().bind(BG_LOCATION);
		int i = this.leftPos;
		int j = this.topPos;
		this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		int k = (int)(41.0F * this.scrollOffs);
		this.blit(matrixStack, i + 119, j + 15 + k, 176 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
		int l = this.leftPos + 52;
		int i1 = this.topPos + 14;
		int j1 = this.startIndex + 12;
		this.renderButtons(matrixStack, x, y, l, i1, j1);
		this.renderRecipes(l, i1, j1);
	}
	
	@Override
	protected void renderTooltip(MatrixStack matrixStack, int x, int y) {
		super.renderTooltip(matrixStack, x, y);
		if (this.displayRecipes) {
			int i = this.leftPos + 52;
			int j = this.topPos + 14;
			int k = this.startIndex + 12;
			List<SawmillRecipe> list = this.menu.getRecipes();
			
			for(int l = this.startIndex; l < k && l < this.menu.getNumRecipes(); ++l) {
				int i1 = l - this.startIndex;
				int j1 = i + i1 % 4 * 16;
				int k1 = j + i1 / 4 * 18 + 2;
				if (x >= j1 && x < j1 + 16 && y >= k1 && y < k1 + 18) {
					this.renderTooltip(matrixStack, list.get(l).getResultItem(), x, y);
				}
			}
		}
	}
	
	private void renderButtons(MatrixStack matrixStack, int x, int y, int p_238853_4_, int p_238853_5_, int p_238853_6_) {
		for(int i = this.startIndex; i < p_238853_6_ && i < this.menu.getNumRecipes(); ++i) {
			int j = i - this.startIndex;
			int k = p_238853_4_ + j % 4 * 16;
			int l = j / 4;
			int i1 = p_238853_5_ + l * 18 + 2;
			int j1 = this.imageHeight;
			if (i == this.menu.getSelectedRecipeIndex()) {
				j1 += 18;
			} else if (x >= k && y >= i1 && x < k + 16 && y < i1 + 18) {
				j1 += 36;
			}
			
			this.blit(matrixStack, k, i1 - 1, 0, j1, 16, 18);
		}
	}
	
	private void renderRecipes(int p_214142_1_, int p_214142_2_, int p_214142_3_) {
		List<SawmillRecipe> list = this.menu.getRecipes();
		
		for(int i = this.startIndex; i < p_214142_3_ && i < this.menu.getNumRecipes(); ++i) {
			int j = i - this.startIndex;
			int k = p_214142_1_ + j % 4 * 16;
			int l = j / 4;
			int i1 = p_214142_2_ + l * 18 + 2;
			this.minecraft.getItemRenderer().renderAndDecorateItem(list.get(i).getResultItem(), k, i1);
		}
	}
	
	@Override
	public boolean mouseClicked(double x, double y, int button) {
		this.scrolling = false;
		if (this.displayRecipes) {
			int i = this.leftPos + 52;
			int j = this.topPos + 14;
			int k = this.startIndex + 12;
			
			for(int l = this.startIndex; l < k; ++l) {
				int i1 = l - this.startIndex;
				double d0 = x - (double)(i + i1 % 4 * 16);
				double d1 = y - (double)(j + i1 / 4 * 18);
				if (d0 >= 0.0D && d1 >= 0.0D && d0 < 16.0D && d1 < 18.0D && this.menu.clickMenuButton(this.minecraft.player, l)) {
					Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
					this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
					return true;
				}
			}
			
			i = this.leftPos + 119;
			j = this.topPos + 9;
			if (x >= (double)i && x < (double)(i + 12) && y >= (double)j && y < (double)(j + 54)) {
				this.scrolling = true;
			}
		}
		
		return super.mouseClicked(x, y, button);
	}
	
	@Override
	public boolean mouseDragged(double x, double y, int button, double dx, double dy) {
		if (this.scrolling && this.isScrollBarActive()) {
			int i = this.topPos + 14;
			int j = i + 54;
			this.scrollOffs = ((float)y - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
			this.scrollOffs = MathHelper.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5D) * 4;
			return true;
		} else {
			return super.mouseDragged(x, y, button, dx, dy);
		}
	}
	
	@Override
	public boolean mouseScrolled(double x, double y, double d) {
		if (this.isScrollBarActive()) {
			int i = this.getOffscreenRows();
			this.scrollOffs = (float)((double)this.scrollOffs - d / (double)i);
			this.scrollOffs = MathHelper.clamp(this.scrollOffs, 0.0F, 1.0F);
			this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5D) * 4;
		}
		
		return true;
	}
	
	private boolean isScrollBarActive() {
		return this.displayRecipes && this.menu.getNumRecipes() > 12;
	}
	
	protected int getOffscreenRows() {
		return (this.menu.getNumRecipes() + 4 - 1) / 4 - 3;
	}
	
	private void containerChanged() {
		this.displayRecipes = this.menu.hasInputItem();
		if (!this.displayRecipes) {
			this.scrollOffs = 0.0F;
			this.startIndex = 0;
		}
	}
}