package joshie.harvest.shops;

import joshie.harvest.api.shops.IShopGuiOverlay;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ShopGui implements IShopGuiOverlay {
    private ResourceLocation shop_overlay;
    private int resourceY;

    public ShopGui(String name, int resourceY) {
        this.shop_overlay = new ResourceLocation(HFModInfo.MODID + ":textures/gui/shops/" + name + ".png");
        this.resourceY = resourceY;
    }

    public ShopGui(int resourceY) {
        this.resourceY = resourceY;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderOverlay(GuiScreen gui, int x, int y, int xSize, int ySize) {
        if (shop_overlay != null) {
            gui.mc.renderEngine.bindTexture(shop_overlay);
            gui.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        }

        gui.mc.renderEngine.bindTexture(getResource());
        gui.drawTexturedModalRect(x + 20, y + 5, 1, resourceY + 1, 254, 31);
    }

    /**
     * Returns the location of the shops name
     **/
    public ResourceLocation getResource() {
        ResourceLocation shop_texture = new ResourceLocation(HFModInfo.MODID + ":lang/" + FMLCommonHandler.instance().getCurrentLanguage() + "/shops.png");
        try {
            MCClientHelper.getMinecraft().renderEngine.getTexture(shop_texture).loadTexture(Minecraft.getMinecraft().getResourceManager());
        } catch (Exception e) {
            shop_texture = new ResourceLocation(HFModInfo.MODID + ":lang/en_US/shops.png");
        }

        return shop_texture;
    }
}