//
// Created by david on 17/01/17.
//

#include "../include/Packet.h"
#include "../include/ACK.h"

ACK::ACK(short num):


    blockNum (num)

{}

int ACK::getOpCode()
{
    return opCode;
}
short ACK::getBlockNum()
{
    return blockNum;
}

long ACK::getPacketSize(){
    return -1;
}
std::vector<char> ACK::getData() {
    std::vector<char>* v=new vector<char> {'2','3'};
    return *v;
}
string ACK::getName() {
    return "";
}
void ACK::printError() {
}
string ACK::getErrorMessage() {
    return "";
}


