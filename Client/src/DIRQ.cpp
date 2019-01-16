//
// Created by david on 17/01/17.
//

#include "../include/DIRQ.h"
#include "../include/RRQ.h"
#include "../include/Packet.h"

DIRQ::DIRQ() {}
int DIRQ::getOpCode()
{
	return opCode;
}

long DIRQ::getPacketSize(){
	return -1;
}
std::vector<char> DIRQ::getData() {
	std::vector<char>* v=new vector<char> {'2','3'};
	return *v;
}
 string DIRQ::getName() {
	return "";
}
 void DIRQ::printError() {
}
 string DIRQ::getErrorMessage() {
	return "";
}
short DIRQ::getBlockNum() {
	return -1;
}

DIRQ::~DIRQ() {
	delete this;
}