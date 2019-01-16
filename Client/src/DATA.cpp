//
// Created by david on 17/01/17.
//

#include "../include/DATA.h"

DATA::DATA(long pSize, int bNum, std::vector<char> data):

	packetSize (pSize),
	blockNum (bNum),
	data ( data)


{}
int DATA::getOpCode()
{
	return opCode;
}
long DATA::getPacketSize()
{
	return packetSize;
}
 string DATA::getName() {
	return "";
}
 void DATA::printError() {
}
 string DATA::getErrorMessage() {
	return "";
}
short DATA::getBlockNum()
{
	return static_cast<short>(blockNum);
}
std::vector<char> DATA::getData()
{
	return data;
}

DATA::~DATA() {
	delete this;
}
