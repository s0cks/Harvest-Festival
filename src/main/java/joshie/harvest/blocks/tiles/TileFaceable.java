package joshie.harvest.blocks.tiles;

import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncOrientation;
import joshie.harvest.core.util.generic.IFaceable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class TileFaceable extends TileEntity implements IFaceable {
    protected EnumFacing orientation = EnumFacing.NORTH;

    @Override
    public void setFacing(EnumFacing dir) {
        orientation = dir;
    }

    @Override
    public EnumFacing getFacing() {
        return orientation;
    }

    public IMessage getPacket() {
        return new PacketSyncOrientation(worldObj.provider.getDimension(), getPos(), orientation);
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        return PacketHandler.getPacket(getPacket());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        orientation = EnumFacing.valueOf(nbt.getString("Facing"));
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        if (orientation != null) {
            nbt.setString("Facing", orientation.name());
        }
    }
}
