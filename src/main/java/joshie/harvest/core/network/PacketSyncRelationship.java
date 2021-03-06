package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.relations.IRelatable;
import joshie.harvest.api.relations.IRelatableDataHandler;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.penguin.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketSyncRelationship extends PenguinPacket {
    private IRelatableDataHandler handler;
    private IRelatable relatable;
    private int value;
    private boolean particles;

    public PacketSyncRelationship() {}

    public PacketSyncRelationship(IRelatable relatable, int value, boolean particles) {
        this.relatable = relatable;
        this.value = value;
        this.particles = particles;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(value);
        buf.writeBoolean(particles);
        ByteBufUtils.writeUTF8String(buf, relatable.getDataHandler().name());
        relatable.getDataHandler().toBytes(relatable, buf);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        value = buf.readInt();
        particles = buf.readBoolean();
        String handlerName = ByteBufUtils.readUTF8String(buf);
        handler = HFApi.relations.getDataHandler(handlerName).copy();
        handler.fromBytes(buf);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        IRelatable relatable = handler.onMessage(particles);
        if (relatable != null) {
            HFTrackers.getClientPlayerTracker().getRelationships().setRelationship(relatable, value);
        }
    }
}