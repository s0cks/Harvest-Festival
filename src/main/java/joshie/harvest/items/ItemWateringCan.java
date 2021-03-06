package joshie.harvest.items;

import joshie.harvest.core.helpers.CropHelper;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketWateringCan;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class ItemWateringCan extends ItemBaseTool implements IFluidContainerItem {
    @Override
    public int getFront(ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
            case COPPER:
                return 0;
            case SILVER:
                return 1;
            case GOLD:
                return 2;
            case MYSTRIL:
                return 4;
            case CURSED:
            case BLESSED:
                return 6;
            case MYTHIC:
                return 12;
            default:
                return 0;
        }
    }

    @Override
    public int getSides(ItemStack stack) {
        ToolTier tier = getTier(stack);
        switch (tier) {
            case BASIC:
                return 0;
            case COPPER:
            case SILVER:
            case GOLD:
                return 1;
            case MYSTRIL:
                return 2;
            case CURSED:
            case BLESSED:
                return 6;
            case MYTHIC:
                return 10;
            default:
                return 0;
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return stack.hasTagCompound();
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.hasTagCompound()) {
            int water = stack.getTagCompound().getByte("Water");
            return (double) (128D - water) / 128D;
        } else return 0D;
    }

    @Override
    public FluidStack getFluid(ItemStack container) {
        return container.hasTagCompound() ? new FluidStack(FluidRegistry.WATER, container.getTagCompound().getByte("Water")) : null;
    }

    @Override
    public int getCapacity(ItemStack container) {
        return container.hasTagCompound() ? container.getTagCompound().getByte("Water") : 0;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill) {
        if (resource == null || resource.getFluid() != FluidRegistry.WATER) {
            return 0;
        } else {
            if (!container.hasTagCompound()) {
                container.setTagCompound(new NBTTagCompound());
            }

            int current_capacity = container.getTagCompound().getByte("Water");
            int max_fill_capacity = (Math.max(0, Byte.MAX_VALUE - current_capacity));
            int amount_filled = 0;
            if (resource.amount >= max_fill_capacity) {
                amount_filled = max_fill_capacity;
            } else amount_filled = resource.amount;

            int new_amount = (current_capacity + amount_filled);

            if (doFill) {
                container.getTagCompound().setByte("Water", (byte) new_amount);
            }

            return amount_filled;
        }
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
        if (maxDrain == 1) {
            if (container.hasTagCompound()) {
                byte water = container.getTagCompound().getByte("Water");
                if (water >= 1) {
                    if (doDrain) {
                        container.getTagCompound().setByte("Water", (byte) (water - 1));
                    }

                    return new FluidStack(FluidRegistry.WATER, 1);
                } else return null;
            }
        }

        return null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        attemptToFill(world, player, stack);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (!world.isRemote) return EnumActionResult.FAIL;
        else {
            onItemUse(stack, player, world, pos, hand, facing, hitX, hitY, hitZ);
            PacketHandler.sendToServer(new PacketWateringCan(stack, world, pos));
            return EnumActionResult.SUCCESS;
        }
    }

    private EnumActionResult hydrate(EntityPlayer player, ItemStack stack, World world, BlockPos pos) {
        if (CropHelper.hydrate(world, pos)) {
            displayParticle(world, pos, EnumParticleTypes.WATER_SPLASH, Blocks.WATER.getDefaultState());
            playSound(world, pos, SoundEvents.ENTITY_GENERIC_SWIM, SoundCategory.NEUTRAL);
            PlayerHelper.performTask(player, stack, getExhaustionRate(stack));
            if (!player.capabilities.isCreativeMode) {
                drain(stack, 1, true);
            }
            return EnumActionResult.SUCCESS;
        } else return EnumActionResult.FAIL;
    }

    private boolean attemptToFill(World world, EntityPlayer player, ItemStack stack) {
        RayTraceResult rayTraceResult = this.rayTrace(world, player, true);
        if (rayTraceResult != null) {
            if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
                IBlockState state = world.getBlockState(rayTraceResult.getBlockPos());
                if (state.getMaterial() == Material.WATER) {
                    return fill(stack, new FluidStack(FluidRegistry.WATER, 128), true) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (attemptToFill(world, player, stack)) {
            return EnumActionResult.PASS;
        } else {
            EnumFacing front = joshie.harvest.core.helpers.generic.DirectionHelper.getFacingFromEntity(player);
            Block initial = world.getBlockState(pos).getBlock();
            if (!(initial instanceof joshie.harvest.blocks.BlockFarmland) && (!(initial instanceof IPlantable))) {
                return EnumActionResult.FAIL;
            }

            EnumActionResult watered = EnumActionResult.FAIL;
            //Facing North, We Want East and West to be 1, left * this.left
            for (int y2 = pos.getY() - 1; y2 <= pos.getY(); y2++) {
                for (int x2 = getXMinus(stack, front, pos.getX()); x2 <= getXPlus(stack, front, pos.getX()); x2++) {
                    for (int z2 = getZMinus(stack, front, pos.getZ()); z2 <= getZPlus(stack, front, pos.getZ()); z2++) {
                        if (getCapacity(stack) > 0) {
                            Block block = world.getBlockState(new BlockPos(x2, y2, z2)).getBlock();
                            if (block instanceof IPlantable) {
                                watered = hydrate(player, stack, world, new BlockPos(x2, y2 - 1, z2));
                            } else if (block instanceof joshie.harvest.blocks.BlockFarmland) {
                                watered = hydrate(player, stack, world, new BlockPos(x2, y2, z2));
                            }
                        }
                    }
                }
            }
            return watered;
        }
    }
}