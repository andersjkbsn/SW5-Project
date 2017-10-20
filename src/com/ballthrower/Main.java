package com.ballthrower;

import com.ballthrower.communication.BluetoothCommunicator;
import com.ballthrower.communication.Communicator;
import com.ballthrower.communication.packets.Packet;
import com.ballthrower.communication.packets.RequestTargetPacket;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Sound;

public class Main
{
	public static void main(String[] args)
	{
        LCD.drawString("Awaiting connection", 0, 0);
        Communicator communicator = new BluetoothCommunicator();
		communicator.awaitConnection();

		while (true)
        {
            LCD.clear();
            Sound.twoBeeps();
            Button.ENTER.waitForPressAndRelease();

            // Request target
            Sound.buzz();
            communicator.sendPacket(new RequestTargetPacket());

            // Receive answer to request
            Packet requestAnswer = communicator.receivePacket();
        }
    }
}


/*            Packet packet = communicator.receivePacket();

            if (packet instanceof EnginePowerPacket)
                new NXTMotor(MotorPort.A).setPower(((EnginePowerPacket) packet).getEnginePower());
            else if (packet instanceof PingPacket)
            {
                // In case of the ping packet, we simply return the same packet
                Sound.buzz();
                communicator.sendPacket(packet);
            }*/