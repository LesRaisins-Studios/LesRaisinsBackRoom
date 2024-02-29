package me.xjqsh.lrbackroom.network;



import me.xjqsh.lrbackroom.Reference;
import me.xjqsh.lrbackroom.network.message.SCustomMeta;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {
    public static final String PROTOCOL_VERSION = "1";
    private static SimpleChannel playChannel;
    private static int nextMessageId = 0;

    public static void init() {
        playChannel = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Reference.MOD_ID, "play"))
                .networkProtocolVersion(() -> PROTOCOL_VERSION)
                .clientAcceptedVersions(PROTOCOL_VERSION::equals)
                .serverAcceptedVersions(PROTOCOL_VERSION::equals)
                .simpleChannel();

        playChannel.messageBuilder(SCustomMeta.class, nextMessageId++)
                .encoder(SCustomMeta::encode)
                .decoder(SCustomMeta::decode)
                .consumer(SCustomMeta::handle)
                .add();
    }

    public static SimpleChannel getPlayChannel() {
        return playChannel;
    }
}
