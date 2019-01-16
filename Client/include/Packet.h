//
// Created by david on 17/01/17.
//
using namespace std;
#ifndef CLIENT_PACKET_H
#define CLIENT_PACKET_H
#include <vector>
#include <string>
class Packet
{
public:
		int opCode = 0;
		Packet(){};
		virtual ~Packet();
		virtual int getOpCode()=0;
		virtual long getPacketSize()=0;
		virtual short getBlockNum()=0;
		virtual std::vector<char> getData()=0;
		virtual string getName()=0;
        virtual void printError()=0;
		virtual string getErrorMessage()=0;
};

#endif //CLIENT_PACKET_H
