//
// Created by david on 17/01/17.
//

#include "../include/RRQ.h"

RRQ::RRQ(string name):name(name){}


string RRQ::getName()
{
    return name;
}

int RRQ::getOpCode()
{
    return opCode;
}


long RRQ::getPacketSize(){
    return -1;
}
vector<char> RRQ::getData() {
    std::vector<char>* v=new vector<char> {'2','3'};
    return *v;
}
 void RRQ::printError() {
}
 string RRQ::getErrorMessage() {
    return "";
}
short RRQ::getBlockNum() {
    return -1;
}


RRQ::~RRQ() {
    delete this;
}