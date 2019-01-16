
#include "../include/DATA.h"
#include "../include/BCAST.h"

BCAST::BCAST(string name, short a) :name(name),add(a){}

int BCAST::getOpCode()
{
	return opCode;
}

string BCAST::getName()
{
	return name;
}

short BCAST::getBlockNum()
{
	return add;
}

long BCAST::getPacketSize(){
	return -1;
}
std::vector<char> BCAST::getData() {
	std::vector<char>* v=new vector<char> {'2','3'};
	return *v;
}
void BCAST::printError() {
}
string BCAST::getErrorMessage() {
	return "";
}

BCAST::~BCAST() {
	delete this;
}