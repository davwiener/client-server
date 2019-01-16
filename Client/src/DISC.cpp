//
// Created by david on 17/01/17.
//

#include "../include/DISC.h"

DISC::DISC() {
}

int DISC::getOpCode()
{
	return opCode;
}

long DISC::getPacketSize(){
	return -1;
}
std::vector<char> DISC::getData() {
	std::vector<char>* v=new vector<char> {'2','3'};
	return *v;
}
 string DISC::getName() {
	return "";
}
 void DISC::printError() {
}
 string DISC::getErrorMessage() {
	return "";
}
short DISC::getBlockNum() {
	return -1;
}

DISC::~DISC() {
	delete this;
}