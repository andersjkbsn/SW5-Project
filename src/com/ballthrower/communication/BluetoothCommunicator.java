package com.ballthrower.communication;

import com.ballthrower.communication.PacketHandler.PacketIds;
import com.ballthrower.communication.packets.Packet;
import com.ballthrower.exceptions.UnknownPacketException;
import lejos.nxt.*;
import lejos.nxt.comm.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BluetoothCommunicator extends Communicator
{
    private NXTMotor motor = new NXTMotor(MotorPort.A);
    private NXTConnection _socket;
	private DataInputStream _inputStream;
	private DataOutputStream _outputStream;

	@Override
	public void awaitConnection()
	{
	    // Wait for connection and set mode to raw
		_socket = Bluetooth.waitForConnection();
		_socket.setIOMode(NXTConnection.RAW);

		// Get streams
		_inputStream = _socket.openDataInputStream();
		_outputStream = _socket.openDataOutputStream();

		motor.forward(); // todo remove
		Sound.beepSequence();
	}

    @Override
    public void closeConnection()
    {
        _socket.close();
    }

    @Override
    public boolean isAlive()
    {
        return false;
    }

    @Override
	public Packet receivePacket()
	{
	    try
        {
            // The first element of each packet is the id of the packet type
            byte packetId = _inputStream.readByte();

            // Query the packet handler for the packet class associated with this id
            // The packet handler also constructs the packet object
            Packet instantiatedPacket = PacketHandler.instantiateFromId(PacketIds.fromByte(packetId));

            // Finally we deserialize the object
            instantiatedPacket.constructFromStream(_inputStream);

            return instantiatedPacket;
        }
        catch (IOException exception)
        {
            LCD.drawString("IO Exception.", 0, 3);
        }
        catch (UnknownPacketException e)
        {
            LCD.drawString("Unknown packet type.", 0, 3);
        }

        return null;
	}

	@Override
	public void sendPacket(Packet packet)
	{
	    try
        {
            _outputStream.writeByte(PacketIds.RequestTarget.asByte()); // todo fixme
            packet.writeToStream(_outputStream);
            _outputStream.flush();
        }
        catch (IOException exception)
        {
            LCD.drawString("IO Exception.", 0, 3);
        }
	}
}