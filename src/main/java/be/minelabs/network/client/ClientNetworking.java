package be.minelabs.network.client;

import be.minelabs.network.IonicDataPacket;
import be.minelabs.network.LewisDataPacket;
import be.minelabs.network.NetworkingConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ClientNetworking {
    public static void registerHandlers(){
        registerS2CPacketHandlers();
    }

    public static void registerS2CPacketHandlers() {
        //Receive on CLIENT
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.LEWISDATASYNC, (c, h, b, s) -> LewisDataPacket.receive(c.world, b, s));
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.IONICDATASYNC, (c, h, b, s) -> IonicDataPacket.receive(c.world, b, s));
    }
}
