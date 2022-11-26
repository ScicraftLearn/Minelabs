package be.uantwerpen.minelabs.network;

import be.uantwerpen.minelabs.Minelabs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;

public class NetworkingConstants {
    public static final Identifier LEWISDATASYNC = new Identifier(Minelabs.MOD_ID, "lewis_data_sync");
    public static final Identifier IONICDATASYNC = new Identifier(Minelabs.MOD_ID, "ionic_data_sync");
    public static final Identifier MOLOGRAM_SYNC = new Identifier(Minelabs.MOD_ID, "mologram_sync");

    public static void registerC2SPackets() {
        //Receive on SERVER
    }

    public static void registerS2CPackets() {
        //Receive on CLIENT
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.LEWISDATASYNC, (c, h, b, s) -> LewisDataPacket.receive(c.world, b, s));
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.IONICDATASYNC, (c, h, b, s) -> IonicDataPacket.receive(c.world, b, s));
    }
}
